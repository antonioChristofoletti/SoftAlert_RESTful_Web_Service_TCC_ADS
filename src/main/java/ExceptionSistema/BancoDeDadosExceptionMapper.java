package ExceptionSistema;

import Model.ErrorMessage;
import Geral.ResponseBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class BancoDeDadosExceptionMapper implements ExceptionMapper<BancoDeDadosException> {

    @Override
    public Response toResponse(BancoDeDadosException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "");

        return ResponseBuilder.getResponse(Response.Status.INTERNAL_SERVER_ERROR, errorMessage);
    }
}
