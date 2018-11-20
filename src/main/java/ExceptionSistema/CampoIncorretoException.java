package ExceptionSistema;

public class CampoIncorretoException extends Exception {

    public CampoIncorretoException(String msg) {
        super(msg);
    }

    public CampoIncorretoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
