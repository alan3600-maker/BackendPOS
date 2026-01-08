package py.com.hidraulica.caacupe.dto;

public record ClienteDto(
    Long id,
    String nombreRazonSocial,
    String ruc,
    String telefono,
    String direccion,
    String email,
    boolean activo
) {}
