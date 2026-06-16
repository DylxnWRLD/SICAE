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
import uv.sicae.vehicles.vehicles.dto.obtenervehiculosid.ObtenerVehiculosPorIdRespuesta;
import uv.sicae.vehicles.vehicles.dto.registrarvehiculo.RegistrarVehiculoPeticion;
import uv.sicae.vehicles.vehicles.entity.Vehiculo;

/**
 * Clase repository para hacer consultas a la BD
 *
 * @author jeshu
 */
@Mapper
public interface VehicleRepository {

    /**
     * Método de prueba para recuperar todos los vehiculos de un usuario
     *
     * @param idUsuario
     * @return vehiculo o algo asi jeje
     */
    @Select("""
            SELECT * 
            FROM vehiculofullinfo 
            WHERE idUsuario = #{idUsuario};
            """)

    List<Vehiculo> ObtenerVehiculosPorId(@Param("idUsuario") Integer idUsuario);

    /**
     * Método para insertar un vehiculo
     *
     * @param peticion
     * @return asd
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
     * @param idUsuario
     * @return
     */
    @Select("""
            SELECT COUNT(*) 
            FROM vehiculofullinfo 
            WHERE idUsuario = #{idUsuario} AND estatus = 1;
            """)
    Integer contarVehiculosActivos(
            @Param("idUsuario") Integer idUsuario);
    
    /**
     * Método para contar los vehículos con la misma placa
     * @param Placa
     * @return 
     */
    @Select("""
            SELECT COUNT(*) 
            FROM vehiculofullinfo 
            WHERE placa = #{placa};
            """)
    int contarVehiculosPorPlaca(
            @Param("placa")String Placa);

    /**
     * Método para obtener el Id de un vehículo por su placa
     *
     * @param placa
     * @return
     */
    @Select("""
            SELECT idVehiculo
            FROM vehiculofullinfo 
            WHERE placa = #{placa};
            """)
    Integer obtenerIdPorPlaca(
            @Param("placa") String placa);
    
    
    
    /**
     * Método para editar un vehículo
     * @param peticion
     * @return 
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
     * Método para actualizar el estatus de un vehículo
     * @param peticion
     * @return 
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
