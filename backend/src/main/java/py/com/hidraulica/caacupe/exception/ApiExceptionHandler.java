package py.com.hidraulica.caacupe.exception;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("timestamp", OffsetDateTime.now().toString(),
				"status", 404, "error", "Not Found", "message", ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("timestamp", OffsetDateTime.now().toString(),
				"status", 400, "error", "Bad Request", "message", "Validation error", "fields", fieldErrors));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("timestamp", OffsetDateTime.now().toString(),
				"status", 409, "error", "Conflict", "message", ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("timestamp", OffsetDateTime.now().toString(), "status", 500, "error",
						"Internal Server Error", "message", ex.getMessage()));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("timestamp", OffsetDateTime.now().toString(),
				"status", 409, "error", "Conflict", "message", "Registro duplicado o violación de integridad"));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("timestamp", OffsetDateTime.now().toString(),
				"status", 401, "error", "Unauthorized", "message", "Credenciales incorrectas."));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> handleDenied(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("timestamp", OffsetDateTime.now().toString(),
				"status", 403, "error", "Forbidden", "message", "No tenés permisos para esta acción."));
	}
	
	@ExceptionHandler(org.springframework.dao.OptimisticLockingFailureException.class)
	public ResponseEntity<?> handleOptimisticLock(org.springframework.dao.OptimisticLockingFailureException ex) {
	  return ResponseEntity.status(409).body(Map.of(
	    "message", "Conflicto de concurrencia. Reintente la operación.",
	    "error", "OPTIMISTIC_LOCK"
	  ));
	}
// PARA PRODUCION
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
//    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
//        "timestamp", OffsetDateTime.now().toString(),
//        "status", 500,
//        "error", "Internal Server Error",
//        "message", "Ocurrió un error inesperado"
//    ));
//  }
}
