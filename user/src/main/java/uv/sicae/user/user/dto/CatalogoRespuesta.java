package uv.sicae.user.user.dto;

/**
 * DTO utilizado para devolver elementos de catálogos del sistema.
 * 
 * @author Alvaro
 */
public class CatalogoRespuesta {
    private Integer id;
    private String nombre;

    /**
     * Obtiene el valor de id.
     *
     * @return resultado generado por la operación.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Asigna el valor de id.
     *
     * @param id parámetro requerido para ejecutar la operación.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el valor de nombre.
     *
     * @return resultado generado por la operación.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el valor de nombre.
     *
     * @param nombre parámetro requerido para ejecutar la operación.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
