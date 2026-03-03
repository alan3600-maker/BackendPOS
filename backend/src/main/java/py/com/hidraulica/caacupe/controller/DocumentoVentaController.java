package py.com.hidraulica.caacupe.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import py.com.hidraulica.caacupe.dto.DocumentoVentaDto;
import py.com.hidraulica.caacupe.dto.EmitirDocumentoRequest;
import py.com.hidraulica.caacupe.service.DocumentoVentaService;

@RestController
@RequestMapping("/api/v1/ventas/{ventaId}/documentos")
public class DocumentoVentaController {

	private final DocumentoVentaService service;

	public DocumentoVentaController(DocumentoVentaService service) {
		this.service = service;
	}

	@GetMapping
	public List<DocumentoVentaDto> listar(@PathVariable Long ventaId) {
		return service.listarPorVenta(ventaId);
	}

	@PostMapping("/emitir")
	public DocumentoVentaDto emitir(@PathVariable Long ventaId, @RequestBody @Valid EmitirDocumentoRequest req) {
		return service.emitir(ventaId, req);
	}
}
