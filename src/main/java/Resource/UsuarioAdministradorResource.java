package Resource;

import Resource.Autenticacao.ValidarTokenAutenticacao;
import Controller.UsuarioAdministradorController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Model.UsuarioAdministrador;
import Geral.ResponseBuilder;
import Model.ErrorMessage;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("usuarioAdministrador")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioAdministradorResource {
    @ValidarTokenAutenticacao
    @GET
    public Response getTodosUsuariosAdminstradores() 
            throws BancoDeDadosException, GenericException {
        ArrayList<UsuarioAdministrador> lista = UsuarioAdministradorController.retornaTodosUsuariosAdministradores();

        if (lista == null || lista.isEmpty()) {
            ErrorMessage m = new ErrorMessage("Nenhum usuário administrador foi encontrado", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }

        return ResponseBuilder.getResponse(200, lista);
    }
    
    @ValidarTokenAutenticacao
    @GET
    @Path("/{idUsuarioAdministrador}")
    public Response getUsuarioAdminstrador(@PathParam("idUsuarioAdministrador") int idUsuarioAdministrador) 
            throws BancoDeDadosException, GenericException {
        UsuarioAdministrador ua = UsuarioAdministradorController.retornaUsuarioAdministrador(idUsuarioAdministrador);

        if (ua == null) {
            ErrorMessage m = new ErrorMessage("Usuário administrador não encontrado", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }

        return ResponseBuilder.getResponse(200, ua);
    }
    
    @ValidarTokenAutenticacao
    @POST
    public Response inserirUsuarioAdministrador(UsuarioAdministrador usuarioAdministrador) 
            throws BancoDeDadosException, CampoIncorretoException, GenericException {
        UsuarioAdministrador ua = UsuarioAdministradorController.inserir(usuarioAdministrador);

        return ResponseBuilder.getResponse(201, ua);
    }
    
    @ValidarTokenAutenticacao
    @PUT
    public Response editarUsuarioAdministrador(UsuarioAdministrador usuarioAdministrador) 
            throws BancoDeDadosException, GenericException, CampoIncorretoException {
        UsuarioAdministrador ua = UsuarioAdministradorController.editar(usuarioAdministrador);

        return ResponseBuilder.getResponse(200, ua);
    }
}