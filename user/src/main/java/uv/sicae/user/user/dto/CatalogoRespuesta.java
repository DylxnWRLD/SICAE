package uv.sicae.user.user.dto;

/*
    DTO usado para devolver datos simples de catálogos como roles,
    tipos de usuario o programas educativos.
*/
public class CatalogoRespuesta {
    private Integer id;
    private String nombre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}