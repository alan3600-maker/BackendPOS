package py.com.hidraulica.caacupe.exception;

public class NotFoundException extends RuntimeException {
  private static final long serialVersionUID = 8934917071259947254L;

  public NotFoundException(String message) { super(message); }
}
