package ExceptionSistema;

import Geral.ResponseBuilder;
import Model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CampoIncorretoExceptionMapper implements ExceptionMapper<CampoIncorretoException> {

    @Override
    public Response toResponse(CampoIncorretoException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), Response.Status.BAD_REQUEST.getStatusCode(), "");

        return ResponseBuilder.getResponse(Response.Status.BAD_REQUEST, errorMessage);
    }
}