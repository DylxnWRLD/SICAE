/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.sicae.parking.parking.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import uv.sicae.parking.parking.model.EspacioEstacionamiento;
import uv.sicae.parking.parking.model.Movimiento;

/**
 * Repositorio MyBatis encargado del acceso a datos del microservicio de
 * estacionamiento.
 *
 * Contiene las consultas SQL necesarias para consultar espacios, buscar
 * espacios por identificador, registrar movimientos de entrada, actualizar
 * movimientos de salida y modificar el estado de ocupación de los espacios del
 * estacionamiento.
 *
 * Esta interfaz forma parte de la capa Repository dentro de la arquitectura en
 * capas del microservicio.
 *
 * @author josec
 */
@Mapper
public interface ParkingRepository {

    /**
     * Consulta los espacios disponibles del estacionamiento.
     *
     * Obtiene únicamente los espacios que se encuentran activos y libres, es
     * decir, aquellos que no están ocupados y que pueden ser asignados a un
     * vehículo al momento de registrar una entrada.
     *
     * Los campos ocupado y estatus se convierten a valores numéricos para
     * facilitar su manejo en Java y en las respuestas JSON.
     *
     * @return lista de espacios disponibles del estacionamiento.
     */
    @Select("""
    SELECT 
        idEspacio,
        claveEspacio,
        tipo,
        ocupado + 0 AS ocupado,
        estatus + 0 AS estatus
    FROM espacioestacionamiento
    WHERE ocupado = b'0'
    AND estatus = b'1'
    ORDER BY idEspacio
    """)
    List<EspacioEstacionamiento> consultarEspacios();

    /**
     * Busca un espacio de estacionamiento por su identificador.
     *
     * Se utiliza durante el registro de entrada para validar que el espacio
     * solicitado exista, se encuentre activo y no esté ocupado antes de
     * asignarlo a un vehículo.
     *
     * @param idEspacio identificador del espacio de estacionamiento.
     * @return espacio encontrado o null si no existe.
     */
    @Select("""
        SELECT
            idEspacio,
            claveEspacio,
            tipo,
            ocupado + 0 AS ocupado,
            estatus + 0 AS estatus
        FROM espacioestacionamiento
        WHERE idEspacio = #{idEspacio}
        """)
    EspacioEstacionamiento buscarEspacioPorId(Integer idEspacio);

    /**
     * Cuenta los movimientos abiertos asociados a un vehículo.
     *
     * Un movimiento abierto es aquel que tiene registrada una hora de entrada,
     * pero todavía no cuenta con hora de salida. Esta consulta permite validar
     * que un vehículo no tenga una entrada activa y también ayuda a contar
     * cuántos vehículos de un usuario se encuentran dentro del estacionamiento.
     *
     * @param idVehiculo identificador del vehículo a consultar.
     * @return cantidad de movimientos abiertos encontrados para el vehículo.
     */
    @Select("""
        SELECT COUNT(*)
        FROM movimiento
        WHERE idVehiculo = #{idVehiculo}
        AND tiempoSalida IS NULL 
        """)
    int contarMovimientoAbiertoPorVehiculo(Integer idVehiculo);

    /**
     * Inserta un nuevo movimiento de entrada en la base de datos.
     *
     * Registra los datos iniciales del movimiento, como el identificador del
     * vehículo validado, la hora de entrada, la tarifa por hora, el tiempo de
     * creación y el espacio asignado. Los datos relacionados con la salida se
     * guardan posteriormente cuando el vehículo abandona el estacionamiento.
     *
     * El identificador generado por la base de datos se asigna al objeto
     * Movimiento recibido.
     *
     * @param movimiento objeto con la información del movimiento de entrada.
     * @return número de filas afectadas por la inserción.
     */
    @Insert("""
        INSERT INTO movimiento (
            idVehiculo,
            tiempoEntrada,
            tiempoSalida,
            minutosEstacionado,
            horasCobradas,
            costoTotal,
            tarifaHora,
            tiempoCreacion,
            tiempoActualizacion,
            idEspacio
        ) VALUES (
            #{idVehiculo},
            #{tiempoEntrada},
            NULL,
            NULL,
            NULL,
            NULL,
            #{tarifaHora},
            #{tiempoCreacion},
            NULL,
            #{idEspacio}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "idMovimiento")
    int registrarEntrada(Movimiento movimiento);

    /**
     * Marca un espacio de estacionamiento como ocupado.
     *
     * Actualiza el campo ocupado del espacio indicado después de registrar
     * correctamente la entrada de un vehículo.
     *
     * @param idEspacio identificador del espacio que será marcado como ocupado.
     * @return número de filas afectadas por la actualización.
     */
    @Update("""
        UPDATE espacioestacionamiento
        SET ocupado = b'1'
        WHERE idEspacio = #{idEspacio}
        """)
    int ocuparEspacio(Integer idEspacio);

    /**
     * Busca el movimiento abierto más reciente de un vehículo.
     *
     * Recupera la entrada activa del vehículo, junto con la clave del espacio
     * utilizado, para poder registrar su salida, calcular el tiempo de
     * permanencia y liberar el espacio correspondiente.
     *
     * @param idVehiculo identificador del vehículo a consultar.
     * @return movimiento abierto encontrado o null si el vehículo no tiene una
     * entrada activa.
     */
    @Select("""
        SELECT 
            m.idMovimiento,
            m.idVehiculo,
            m.tiempoEntrada,
            m.tiempoSalida,
            m.minutosEstacionado,
            m.horasCobradas,
            m.costoTotal,
            m.tarifaHora,
            m.tiempoCreacion,
            m.tiempoActualizacion,
            m.idEspacio,
            e.claveEspacio
        FROM movimiento m
        INNER JOIN espacioestacionamiento e ON e.idEspacio = m.idEspacio
        WHERE m.idVehiculo = #{idVehiculo}
        AND m.tiempoSalida IS NULL
        ORDER BY m.tiempoEntrada DESC
        LIMIT 1
        """)
    Movimiento buscarMovimientoAbiertoPorVehiculo(Integer idVehiculo);

    /**
     * Actualiza un movimiento con los datos de salida del vehículo.
     *
     * Registra la hora de salida, los minutos estacionados, las horas cobradas,
     * el costo total y el tiempo de actualización del movimiento. Esta
     * operación se realiza cuando el vehículo abandona el estacionamiento.
     *
     * @param movimiento objeto con los datos calculados para registrar la
     * salida.
     * @return número de filas afectadas por la actualización.
     */
    @Update("""
        UPDATE movimiento
        SET
            tiempoSalida = #{tiempoSalida},
            minutosEstacionado = #{minutosEstacionado},
            horasCobradas = #{horasCobradas},
            costoTotal = #{costoTotal},
            tiempoActualizacion = #{tiempoActualizacion}
        WHERE idMovimiento = #{idMovimiento}
        """)
    int registrarSalida(Movimiento movimiento);

    /**
     * Marca un espacio de estacionamiento como libre.
     *
     * Actualiza el campo ocupado del espacio indicado después de registrar
     * correctamente la salida del vehículo, permitiendo que el cajón vuelva a
     * estar disponible para otra entrada.
     *
     * @param idEspacio identificador del espacio que será liberado.
     * @return número de filas afectadas por la actualización.
     */
    @Update("""
        UPDATE espacioestacionamiento
        SET ocupado = b'0'
        WHERE idEspacio = #{idEspacio}
        """)
    int liberarEspacio(Integer idEspacio);
}
