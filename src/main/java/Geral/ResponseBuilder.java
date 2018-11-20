package Geral;

import Model.ErrorMessage;
import javax.ws.rs.core.Response;

public abstract class ResponseBuilder {

    public static Response getResponse(int status) {
        return Response.status(status)
                .build();
    }

    public static Response getResponse(int status, Object content) {
        return Response.status(status)
                .entity(content)
                .build();
    }

    public static Response getResponse(Response.Status status, ErrorMessage content) {
        return Response.status(status)
                .entity(content)
                .build();
    }
}
