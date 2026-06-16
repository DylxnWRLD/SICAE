/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package uv.sicae.vehicles.vehicles.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import uv.sicae.vehicles.vehicles.dto.editarestatusvehiculo.EditarEstatusVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.editarvehiculo.EditarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.dto.registrarvehiculo.RegistrarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.entity.Vehiculo;

/**
 * Interfaz repository para hacer consultas a la BD relacionadas con los
 * vehículos utilizada desde el service
 *
 * @author jeshu
 */
@Mapper
public interface VehicleRepository {

    /**
     * Método para obtener todos los vehículos de un usuario de acuerdo a su ID
     *
     * @param idUsuario El ID del usuario
     * @return Una lista de Entidades {@link Vehiculo} asociadas a un usuario.
     * Se puede retornar vacía si no tiene vehículos asociados
     */
    @Select("""
            SELECT * 
            FROM vehiculofullinfo 
            WHERE idUsuario = #{idUsuario};
            """)

    List<Vehiculo> ObtenerVehiculosPorId(
            @Param("idUsuario") Integer idUsuario);

    /**
     * Método para registrar un nuevo vehículo, recibiendo los datos del
     * vehículo a insertar dentro de una petición
     * {@link RegistrarVehiculoPeticion}
     *
     * @param peticion El objeto {@link RegistrarVehiculoPeticion} donde se
     * envuelven todos los parámetros del vehículo a registrar
     * @return El número de filas retornadas desde la BD, 0 si no se registró el
     * vehículo y 1 si se realizó con éxito (pero no se usa jeje)
     */
    @Insert("""
            INSERT INTO `vehiculo` (
            `idUsuario`, 
            `claveVehiculo`, 
            `idModelo`, 
            `placa`, 
            `color`, 
            `anio`, 
            `descripcion`, 
            `estatus`
            ) VALUES (
            #{peticion.idUsuario},
            #{peticion.claveVehiculo},
            #{peticion.idModelo},
            #{peticion.placa},
            #{peticion.color},
            #{peticion.anio},
            #{peticion.descripcion},
            #{peticion.estatus}
            );
            """)
    int registrarVehiculo(
            @Param("peticion") RegistrarVehiculoPeticion peticion);

    /**
     * Método para contar cuantos vehiculos activos tiene un usuario por su ID
     *
     * @param idUsuario El ID del usuario para contar sus vehículos con estatus
     * de activo
     * @return El número de vehículos con estatus de activo del usuario
     * indicado. Se usa Integer aquí ya que si no existe el vehículo o el
     * usuario se retorna null
     */
    @Select("""
            SELECT COUNT(*) 
            FROM vehiculofullinfo 
            WHERE idUsuario = #{idUsuario} AND estatus = 1;
            """)
    Integer contarVehiculosActivos(
            @Param("idUsuario") Integer idUsuario);

    /**
     * Método para contar cuantos vehículos hay con una placa indicada como
     * parámetro
     *
     * @param Placa La placa a verificar
     * @return El número de vehículos con la misma placa
     */
    @Select("""
            SELECT COUNT(*) 
            FROM vehiculofullinfo 
            WHERE placa = #{placa};
            """)
    int contarVehiculosPorPlaca(
            @Param("placa") String Placa);

    /**
     * Método para verificar si existe un vehículo dado un idUsuario, idVehículo o
     * idModelo que coincidan
     * @param idUsuario El ID del usuario
     * @param idVehiculo El ID del vehículo
     * @param idModelo El ID del modelo del vehículo
     * @return 1 si existe, 0 si no existe
     */
    @Select("""
            SELECT COUNT(*) 
            FROM vehiculofullinfo 
            WHERE idUsuario = #{idUsuario} 
            AND idVehiculo = #{idVehiculo}
            AND idModelo = #{idModelo};
            """)
    int verificarExistenciaVehiculo(
            @Param("idUsuario") Integer idUsuario,
            @Param("idVehiculo") Integer idVehiculo,
            @Param("idModelo") Integer idModelo);

    /**
     * Método para obtener el ID de un vehículo utilizando una placa
     *
     * @param placa La placa para buscar el ID del vehículo
     * @return El ID del vehículo con la placa indicada. Si no existe el
     * vehículo se guarda null en el Integer
     */
    @Select("""
            SELECT idVehiculo
            FROM vehiculofullinfo 
            WHERE placa = #{placa};
            """)
    Integer obtenerIdPorPlaca(
            @Param("placa") String placa);

    /**
     * Método para editar un vehículo con una petición
     * {@link EditarVehiculoPeticion}
     *
     * @param peticion El objeto {@link EditarVehiculoPeticion} que envuelve los
     * datos para su edición
     * @return El número de filas afectadas retornadas desde la BD. Se guarda
     * null en el Integer si no existe el vehículo a editar
     */
    @Update("""
            UPDATE vehiculo 
            SET placa = #{peticion.placa}, 
            color = #{peticion.color}, 
            anio = #{peticion.anio}, 
            descripcion = #{peticion.descripcion} 
            WHERE idVehiculo = #{peticion.idVehiculo}
            AND idUsuario = #{peticion.idUsuario} 
            AND idModelo = #{peticion.idModelo};
            """)
    Integer editarVehiculo(
            @Param("peticion") EditarVehiculoPeticion peticion);

    /**
     * Método para actualizar el estatus de un vehículo con una petición
     * {@link EditarEstatusVehiculoPeticion}, que envuelve los datos para su
     * búsqueda y edición
     *
     * @param peticion El objeto {@link EditarEstatusVehiculoPeticion} que
     * envuelve los datos de la petición
     * @return El número de filas afectadas retornadas desde la BD.
     */
    @Update("""
            UPDATE vehiculo 
            SET estatus = #{peticion.estatus}
            WHERE idVehiculo = #{peticion.idVehiculo} 
            AND idUsuario = #{peticion.idUsuario};
            """)
    int actualizarEstatusVehiculo(
            @Param("peticion") EditarEstatusVehiculoPeticion peticion);

}
