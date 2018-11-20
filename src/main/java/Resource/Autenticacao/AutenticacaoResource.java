package Resource.Autenticacao;

import Controller.UsuarioAdministradorController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Geral.ResponseBuilder;
import Model.ErrorMessage;
import Model.Token;
import Model.UsuarioAdministrador;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("autenticacao")
public class AutenticacaoResource {

    @POST
    @Path("/{autenticar}")
    public Response autenticarUsuario(UsuarioAdministrador ua) throws BancoDeDadosException, GenericException {
        Boolean resultado = UsuarioAdministradorController.autenticarUsuario(ua);

        if (!resultado) {
            return ResponseBuilder.getResponse(Response.Status.UNAUTHORIZED, new ErrorMessage("Usuário ou senha inválido", 401, ""));
        }

        String token = FiltroAutenticacao.gerarToken(ua.getUsuario(), 1);

        return ResponseBuilder.getResponse(200, new Token(token));
    }
}