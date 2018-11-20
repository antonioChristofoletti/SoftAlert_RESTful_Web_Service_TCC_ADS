package ExceptionSistema;

import Model.ErrorMessage;
import Geral.ResponseBuilder;
import com.fasterxml.jackson.core.JsonParseException;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Priority(0)
@Provider
/**
 * Esse mapper é invocado caso o formato do JSON esteja incorreto
 */
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

    public JsonParseExceptionMapper() {
    }
    
    @Override
    public Response toResponse(JsonParseException ex) {
        ErrorMessage errorMessage = new ErrorMessage("2 Erro: " + ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), ""); 

        return ResponseBuilder.getResponse(Response.Status.INTERNAL_SERVER_ERROR, errorMessage);
    }
}
