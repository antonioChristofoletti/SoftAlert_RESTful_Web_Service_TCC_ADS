package Resource;

import Controller.EnderecoController;
import Controller.UsuarioClienteController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Geral.ResponseBuilder;
import Model.ErrorMessage;
import Model.UsuarioAdministrador;
import Model.UsuarioCliente;
import Resource.Autenticacao.ValidarTokenAutenticacao;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("enderecoUsuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnderecoUsuarioResource {

    @ValidarTokenAutenticacao
    @Path("/enderecoUsuarioCliente/")
    @POST
    public Response inserirEnderecoUsuarioCliente(UsuarioCliente usuarioCliente) throws BancoDeDadosException, CampoIncorretoException, GenericException {
        EnderecoController.inserir(usuarioCliente);

        return ResponseBuilder.getResponse(201, usuarioCliente);
    }
       
    @ValidarTokenAutenticacao
    @Path("/enderecoUsuarioCliente/{idUsuarioCliente}")
    @GET
    public Response retornaEnderecos(@PathParam("idUsuarioCliente") int idUsuarioCliente) throws BancoDeDadosException, CampoIncorretoException, GenericException {
        UsuarioCliente uc = UsuarioClienteController.retornaUsuarioCliente(idUsuarioCliente);

        uc.setListaEnderecos(EnderecoController.retornaLista(uc));
        
        if(uc.getListaEnderecos().isEmpty())
        {
            ErrorMessage m = new ErrorMessage("Endereços não encontrados", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }
        
        return ResponseBuilder.getResponse(200, EnderecoController.retornaLista(uc));
    }
}