package Resource;

import Controller.AlertaUsuarioClienteController;
import Controller.UsuarioClienteController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Geral.ResponseBuilder;
import Model.AlertaUsuarioCliente;
import Model.ErrorMessage;
import Model.UsuarioCliente;
import Resource.Autenticacao.ValidarTokenAutenticacao;
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

@Path("alertaUsuarioCliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertaUsuarioClienteResource {
    
    @ValidarTokenAutenticacao
    @POST
    public Response inserirAlertaUsuarioCliente(AlertaUsuarioCliente alerta) throws BancoDeDadosException, CampoIncorretoException, GenericException {
        
        UsuarioCliente uc = UsuarioClienteController.retornaUsuarioCliente(alerta.getIdUsuarioCliente());
       
        AlertaUsuarioClienteController.inserir(uc, alerta);
        
        return ResponseBuilder.getResponse(201, alerta);
    }
    
    @ValidarTokenAutenticacao
    @PUT
    public Response editarEnderecoUsuarioAdministrador(AlertaUsuarioCliente alerta) throws BancoDeDadosException, CampoIncorretoException, GenericException {
        AlertaUsuarioClienteController.editar(alerta);

        return ResponseBuilder.getResponse(200, alerta);
    }
   
    @ValidarTokenAutenticacao
    @Path("/{idUsuarioCliente}")
    @GET
    public Response retornaEnderecos(@PathParam("idUsuarioCliente") int idUsuarioCliente) throws BancoDeDadosException, CampoIncorretoException, GenericException {
        
        UsuarioCliente usuarioCliente = new UsuarioCliente();
        usuarioCliente.setId(idUsuarioCliente);
        
        ArrayList<AlertaUsuarioCliente> listaAlertaUsuarioCliente = AlertaUsuarioClienteController.getAlertaUsuarioCliente(usuarioCliente);
        
        if(listaAlertaUsuarioCliente.isEmpty())
        {
            ErrorMessage m = new ErrorMessage("Alertas não encontrados", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }
        
        return ResponseBuilder.getResponse(200, listaAlertaUsuarioCliente);
    }
}