package py.com.hidraulica.caacupe.exception;

public class BusinessException extends RuntimeException {
  private static final long serialVersionUID = 7158210577138781172L;

  public BusinessException(String message) {
    super(message);
  }
}
