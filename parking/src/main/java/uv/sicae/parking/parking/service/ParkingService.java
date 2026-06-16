/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uv.sicae.parking.parking.cliente.UsuarioCliente;
import uv.sicae.parking.parking.cliente.VehiculoCliente;
import uv.sicae.parking.parking.dto.EntradaMovimientoPeticion;
import uv.sicae.parking.parking.dto.EntradaMovimientoRespuesta;
import uv.sicae.parking.parking.dto.SalidaMovimientoPeticion;
import uv.sicae.parking.parking.dto.SalidaMovimientoRespuesta;
import uv.sicae.parking.parking.exception.RecursoNoEncontradoException;
import uv.sicae.parking.parking.exception.ReglaNegocioException;
import uv.sicae.parking.parking.model.DatosToken;
import uv.sicae.parking.parking.model.EspacioEstacionamiento;
import uv.sicae.parking.parking.model.Movimiento;
import uv.sicae.parking.parking.model.UsuarioExterno;
import uv.sicae.parking.parking.model.VehiculoExterno;
import uv.sicae.parking.parking.repository.ParkingRepository;

/**
 * Servicio encargado de procesar la lógica de negocio del microservicio de
 * estacionamiento.
 *
 * Valida las reglas necesarias para consultar espacios disponibles, registrar
 * entradas, registrar salidas, verificar movimientos abiertos, calcular el
 * tiempo de permanencia, determinar las horas cobradas, calcular el costo total
 * y actualizar el estado de ocupación de los espacios.
 *
 * Además, se comunica con los microservicios de usuarios y vehículos para
 * validar que el usuario autenticado exista, se encuentre activo, que la
 * claveUsuario corresponda al usuario, que la placa pertenezca a dicho usuario
 * y que el vehículo cumpla con las reglas necesarias para entrar o salir del
 * estacionamiento.
 *
 * Esta clase forma parte de la capa Service dentro de la arquitectura en capas
 * del microservicio.
 *
 * @author josec
 */
@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final UsuarioCliente usuarioCliente;
    private final VehiculoCliente vehiculoCliente;

    /**
     * Crea el servicio de estacionamiento mediante inyección de dependencias.
     *
     * @param parkingRepository repositorio utilizado para consultar y modificar
     * la información del dominio de estacionamiento en la base de datos.
     * @param usuarioCliente cliente utilizado para comunicarse con el
     * microservicio de usuarios.
     * @param vehiculoCliente cliente utilizado para comunicarse con el
     * microservicio de vehículos.
     */
    public ParkingService(ParkingRepository parkingRepository,
            UsuarioCliente usuarioCliente,
            VehiculoCliente vehiculoCliente) {
        this.parkingRepository = parkingRepository;
        this.usuarioCliente = usuarioCliente;
        this.vehiculoCliente = vehiculoCliente;
    }

    /**
     * Consulta los espacios disponibles del estacionamiento.
     *
     * Solicita a la capa Repository la lista de espacios activos y libres
     * registrados en la base de datos de estacionamiento.
     *
     * @return lista de espacios disponibles del estacionamiento.
     */
    public List<EspacioEstacionamiento> consultarEspacios() {
        return parkingRepository.consultarEspacios();
    }

    /**
     * Registra la entrada de un vehículo al estacionamiento.
     *
     * Valida que el usuario autenticado exista, esté activo y que la
     * claveUsuario enviada corresponda con el usuario del token. Después valida
     * la placa mediante el microservicio de vehículos, comprobando que
     * pertenezca al usuario y que el vehículo se encuentre activo.
     *
     * También verifica que el vehículo no tenga una entrada activa, que el
     * usuario no tenga más de dos vehículos dentro del estacionamiento, que el
     * espacio exista, esté activo y no se encuentre ocupado. Si todas las
     * validaciones se cumplen, registra el movimiento de entrada y marca el
     * espacio como ocupado.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT
     * recibido en la solicitud.
     * @param datosToken información del usuario autenticado obtenida a partir
     * del token JWT.
     * @param peticion datos necesarios para registrar la entrada, incluyendo
     * claveUsuario, placa, espacio y tarifa por hora.
     * @return objeto EntradaMovimientoRespuesta con la información del
     * movimiento de entrada registrado.
     * @throws RecursoNoEncontradoException si el espacio no existe o si no se
     * encuentra información necesaria para completar la operación.
     * @throws ReglaNegocioException si no se cumple alguna regla de negocio del
     * estacionamiento.
     */
    @Transactional
    public EntradaMovimientoRespuesta registrarEntrada(
            String authorizationHeader,
            DatosToken datosToken,
            EntradaMovimientoPeticion peticion) {

        validarUsuario(authorizationHeader, datosToken, peticion.getClaveUsuario());

        VehiculoExterno vehiculo = validarVehiculo(
                authorizationHeader,
                datosToken,
                peticion.getPlaca(),
                true
        );

        int movimientosAbiertos = parkingRepository.contarMovimientoAbiertoPorVehiculo(vehiculo.getIdVehiculo());

        if (movimientosAbiertos > 0) {
            throw new ReglaNegocioException("El vehículo ya tiene una entrada activa");
        }

        int vehiculosDentroUsuario = contarVehiculosDentroUsuario(authorizationHeader, datosToken);

        if (vehiculosDentroUsuario >= 2) {
            throw new ReglaNegocioException("El usuario ya tiene el máximo de 2 vehículos dentro del estacionamiento");
        }

        EspacioEstacionamiento espacio = parkingRepository.buscarEspacioPorId(peticion.getIdEspacio());

        if (espacio == null) {
            throw new RecursoNoEncontradoException("El espacio de estacionamiento no existe");
        }

        if (espacio.getEstatus() == null || espacio.getEstatus() == 0) {
            throw new ReglaNegocioException("El espacio de estacionamiento no está activo");
        }

        if (espacio.getOcupado() != null && espacio.getOcupado() == 1) {
            throw new ReglaNegocioException("El espacio de estacionamiento ya está ocupado");
        }

        LocalDateTime ahora = LocalDateTime.now().withNano(0);

        Movimiento movimiento = new Movimiento();
        movimiento.setIdVehiculo(vehiculo.getIdVehiculo());
        movimiento.setIdEspacio(peticion.getIdEspacio());
        movimiento.setTarifaHora(peticion.getTarifaHora());
        movimiento.setTiempoEntrada(ahora);
        movimiento.setTiempoCreacion(ahora);

        parkingRepository.registrarEntrada(movimiento);
        parkingRepository.ocuparEspacio(peticion.getIdEspacio());

        EntradaMovimientoRespuesta respuesta = new EntradaMovimientoRespuesta();
        respuesta.setMensaje("Entrada registrada correctamente");
        respuesta.setIdMovimiento(movimiento.getIdMovimiento());
        respuesta.setIdVehiculo(movimiento.getIdVehiculo());
        respuesta.setTiempoEntrada(movimiento.getTiempoEntrada());
        respuesta.setIdEspacio(espacio.getIdEspacio());
        respuesta.setClaveEspacio(espacio.getClaveEspacio());
        respuesta.setTarifaHora(movimiento.getTarifaHora());

        return respuesta;
    }

    /**
     * Registra la salida de un vehículo del estacionamiento.
     *
     * Valida que el usuario autenticado exista, esté activo y que la
     * claveUsuario enviada corresponda con el usuario del token. Después valida
     * la placa mediante el microservicio de vehículos para confirmar que
     * pertenezca al usuario.
     *
     * Si existe un movimiento abierto para el vehículo, calcula los minutos
     * estacionados, determina las horas cobradas, calcula el costo total con
     * base en la tarifa registrada al momento de la entrada, actualiza el
     * movimiento de salida y libera el espacio ocupado.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT
     * recibido en la solicitud.
     * @param datosToken información del usuario autenticado obtenida a partir
     * del token JWT.
     * @param peticion datos necesarios para registrar la salida, incluyendo
     * claveUsuario y placa.
     * @return objeto SalidaMovimientoRespuesta con la información del
     * movimiento de salida registrado.
     * @throws RecursoNoEncontradoException si no existe una entrada activa para
     * el vehículo indicado.
     * @throws ReglaNegocioException si no se cumple alguna regla de negocio
     * relacionada con el usuario o el vehículo.
     */
    @Transactional
    public SalidaMovimientoRespuesta registrarSalida(
            String authorizationHeader,
            DatosToken datosToken,
            SalidaMovimientoPeticion peticion) {

        validarUsuario(authorizationHeader, datosToken, peticion.getClaveUsuario());

        VehiculoExterno vehiculo = validarVehiculo(
                authorizationHeader,
                datosToken,
                peticion.getPlaca(),
                false
        );

        Movimiento movimiento = parkingRepository.buscarMovimientoAbiertoPorVehiculo(vehiculo.getIdVehiculo());

        if (movimiento == null) {
            throw new RecursoNoEncontradoException("No existe una entrada activa para ese vehículo");
        }

        LocalDateTime salida = LocalDateTime.now().withNano(0);

        long minutos = Duration.between(movimiento.getTiempoEntrada(), salida).toMinutes();

        if (minutos <= 0) {
            minutos = 1;
        }

        int horasCobradas = (int) Math.ceil(minutos / 60.0);

        BigDecimal costoTotal = movimiento.getTarifaHora()
                .multiply(BigDecimal.valueOf(horasCobradas))
                .setScale(2, RoundingMode.HALF_UP);

        movimiento.setTiempoSalida(salida);
        movimiento.setMinutosEstacionado((int) minutos);
        movimiento.setHorasCobradas(horasCobradas);
        movimiento.setCostoTotal(costoTotal);
        movimiento.setTiempoActualizacion(salida);

        parkingRepository.registrarSalida(movimiento);
        parkingRepository.liberarEspacio(movimiento.getIdEspacio());

        SalidaMovimientoRespuesta respuesta = new SalidaMovimientoRespuesta();
        respuesta.setMensaje("Salida registrada correctamente");
        respuesta.setIdMovimiento(movimiento.getIdMovimiento());
        respuesta.setIdVehiculo(movimiento.getIdVehiculo());
        respuesta.setTiempoEntrada(movimiento.getTiempoEntrada());
        respuesta.setTiempoSalida(movimiento.getTiempoSalida());
        respuesta.setMinutosEstacionado(movimiento.getMinutosEstacionado());
        respuesta.setHorasCobradas(movimiento.getHorasCobradas());
        respuesta.setTarifaHora(movimiento.getTarifaHora());
        respuesta.setCostoTotal(movimiento.getCostoTotal());
        respuesta.setIdEspacio(movimiento.getIdEspacio());
        respuesta.setClaveEspacio(movimiento.getClaveEspacio());

        return respuesta;
    }

    /**
     * Valida la información del usuario autenticado.
     *
     * Comprueba que el token contenga un identificador de usuario, consulta la
     * información del usuario en UserService y verifica que exista, se
     * encuentre activo y que la claveUsuario enviada en la petición corresponda
     * con la clave del usuario autenticado.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT
     * recibido en la solicitud.
     * @param datosToken información obtenida del token JWT.
     * @param claveUsuario clave de usuario enviada en el cuerpo de la petición.
     * @throws RecursoNoEncontradoException si no se encuentra el usuario
     * autenticado.
     * @throws ReglaNegocioException si el usuario no está activo, si no se pudo
     * obtener el usuario del token o si la claveUsuario no corresponde.
     */
    private void validarUsuario(String authorizationHeader, DatosToken datosToken, String claveUsuario) {
        if (datosToken.getIdUsuario() == null) {
            throw new ReglaNegocioException("No fue posible obtener el usuario del token");
        }

        UsuarioExterno usuario = usuarioCliente.obtenerUsuario(datosToken.getIdUsuario(), authorizationHeader);

        if (usuario == null) {
            throw new RecursoNoEncontradoException("No se encontró el usuario autenticado");
        }

        if (!Boolean.TRUE.equals(usuario.getEstatus())) {
            throw new ReglaNegocioException("El usuario no está activo");
        }

        if (usuario.getClaveUsuario() == null
                || !usuario.getClaveUsuario().equalsIgnoreCase(claveUsuario)) {
            throw new ReglaNegocioException("La claveUsuario no corresponde al usuario autenticado");
        }
    }

    /**
     * Valida la placa del vehículo mediante el microservicio de vehículos.
     *
     * Consulta los vehículos asociados al usuario autenticado y verifica que la
     * placa enviada pertenezca a uno de ellos. Cuando la operación lo requiere,
     * también valida que el vehículo se encuentre activo.
     *
     * Para el registro de entrada se valida que el vehículo esté activo. Para
     * la salida, se permite validar únicamente la relación entre usuario y
     * vehículo, ya que el vehículo pudo haber cambiado de estatus después de
     * haber ingresado.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT
     * recibido en la solicitud.
     * @param datosToken información obtenida del token JWT.
     * @param placa placa del vehículo enviada en la petición.
     * @param validarActivo indica si debe validarse que el vehículo esté
     * activo.
     * @return objeto VehiculoExterno correspondiente a la placa validada.
     * @throws ReglaNegocioException si el usuario no tiene vehículos
     * registrados, si la placa no pertenece al usuario o si el vehículo no está
     * activo cuando la operación lo requiere.
     */
    private VehiculoExterno validarVehiculo(
            String authorizationHeader,
            DatosToken datosToken,
            String placa,
            boolean validarActivo) {

        List<VehiculoExterno> vehiculos = vehiculoCliente.obtenerVehiculosUsuario(
                datosToken.getIdUsuario(),
                authorizationHeader
        );

        if (vehiculos == null || vehiculos.isEmpty()) {
            throw new ReglaNegocioException("El usuario no tiene vehículos registrados");
        }

        for (VehiculoExterno vehiculo : vehiculos) {
            if (vehiculo.getPlaca() != null
                    && vehiculo.getPlaca().equalsIgnoreCase(placa)) {

                if (validarActivo && !Boolean.TRUE.equals(vehiculo.getEstatus())) {
                    throw new ReglaNegocioException("El vehículo no está activo");
                }

                return vehiculo;
            }
        }

        throw new ReglaNegocioException("La placa no pertenece al usuario autenticado");
    }

    /**
     * Cuenta cuántos vehículos del usuario autenticado se encuentran
     * actualmente dentro del estacionamiento.
     *
     * Consulta los vehículos asociados al usuario en VehicleService y, por cada
     * vehículo, revisa en la base de datos de estacionamiento si existe un
     * movimiento abierto. El resultado se utiliza para validar la regla que
     * limita a dos vehículos dentro del estacionamiento por usuario.
     *
     * @param authorizationHeader encabezado Authorization con el token JWT
     * recibido en la solicitud.
     * @param datosToken información obtenida del token JWT.
     * @return cantidad de vehículos del usuario que tienen una entrada activa.
     */
    private int contarVehiculosDentroUsuario(String authorizationHeader, DatosToken datosToken) {
        List<VehiculoExterno> vehiculos = vehiculoCliente.obtenerVehiculosUsuario(
                datosToken.getIdUsuario(),
                authorizationHeader
        );

        if (vehiculos == null || vehiculos.isEmpty()) {
            return 0;
        }

        int totalDentro = 0;

        for (VehiculoExterno vehiculo : vehiculos) {
            if (vehiculo.getIdVehiculo() != null) {
                totalDentro += parkingRepository.contarMovimientoAbiertoPorVehiculo(
                        vehiculo.getIdVehiculo()
                );
            }
        }

        return totalDentro;
    }
}
