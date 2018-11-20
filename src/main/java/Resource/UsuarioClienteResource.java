package Resource;

import Controller.RequisicaoEnvioSMSController;
import Controller.UsuarioClienteController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Geral.ResponseBuilder;
import Model.ErrorMessage;
import Model.RequisicaoEnvioSMS;
import Model.UsuarioCliente;
import Resource.Autenticacao.ValidarTokenAutenticacao;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("usuarioCliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioClienteResource {

    @ValidarTokenAutenticacao
    @GET
    public Response getTodosUsuariosClientes() throws BancoDeDadosException, GenericException {
        ArrayList<UsuarioCliente> lista = UsuarioClienteController.retornaTodosUsuariosClientes();

        if (lista == null || lista.isEmpty()) {
            ErrorMessage m = new ErrorMessage("Nenhum usuário cliente foi encontrado", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }

        return ResponseBuilder.getResponse(200, lista);
    }

    @ValidarTokenAutenticacao
    @GET
    @Path("/{idUsuarioCliente}")
    public Response getUsuarioCliente(@PathParam("idUsuarioCliente") int idUsuarioCliente) throws BancoDeDadosException, GenericException {
        UsuarioCliente uc = UsuarioClienteController.retornaUsuarioCliente(idUsuarioCliente);

        if (uc == null) {
            ErrorMessage m = new ErrorMessage("Usuário cliente não encontrado", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }

        return ResponseBuilder.getResponse(200, uc);
    }

    @ValidarTokenAutenticacao
    @POST
    public Response inserirUsuarioCliente(UsuarioCliente usuarioCliente) throws BancoDeDadosException, CampoIncorretoException, GenericException {
        UsuarioCliente uc = UsuarioClienteController.inserir(usuarioCliente);

        return ResponseBuilder.getResponse(201, uc);
    }

    @ValidarTokenAutenticacao
    @PUT
    public Response editarUsuarioCliente(UsuarioCliente usuarioCliente) throws BancoDeDadosException, GenericException, CampoIncorretoException {
        UsuarioCliente ua = UsuarioClienteController.editar(usuarioCliente);

        return ResponseBuilder.getResponse(200, ua);
    }

    @ValidarTokenAutenticacao
    @POST
    @Path("/enviarSMS/")
    public Response enviarSMS(RequisicaoEnvioSMS e) throws BancoDeDadosException {
        
        RequisicaoEnvioSMSController.inserir(e);
        
        return ResponseBuilder.getResponse(201, e);
    }
}
