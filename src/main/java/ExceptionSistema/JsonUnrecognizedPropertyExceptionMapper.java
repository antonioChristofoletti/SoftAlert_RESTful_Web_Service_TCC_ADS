package ExceptionSistema;

import Model.ErrorMessage;
import Geral.ResponseBuilder;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Priority(4000)
@Provider
/**
 * Esse mapper é invocado caso um atributo chave do JSON esteja incorreto
 */
public class JsonUnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

    @Override
    public Response toResponse(UnrecognizedPropertyException ex) {
        ErrorMessage errorMessage = new ErrorMessage("3 Erro: " + ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "");

        return ResponseBuilder.getResponse(Response.Status.INTERNAL_SERVER_ERROR, errorMessage);
    }
}