package Resource;

import Controller.AlertaController;
import Controller.RequisicaoEnvioSMSController;
import Controller.UsuarioClienteController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Geral.ResponseBuilder;
import Model.Alerta;
import Model.AlertaPossuiUsuario;
import Model.ErrorMessage;
import Model.NivelAlerta;
import Model.RequisicaoEnvioSMS;
import Model.RequisicaoEnvioSMSTelefone;
import Model.Telefone;
import Model.UsuarioAdministrador;
import Model.UsuarioCliente;
import Resource.Autenticacao.ValidarTokenAutenticacao;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("alerta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertaResource {

    @ValidarTokenAutenticacao
    @POST
    public Response inserirAlerta(Alerta alerta) throws BancoDeDadosException, CampoIncorretoException, GenericException {

        UsuarioAdministrador ua = new UsuarioAdministrador();
        ua.setId(alerta.getIdUsuarioAdministrador());

        NivelAlerta na = new NivelAlerta();
        na.setId(alerta.getIdNivelAlerta());

        alerta.setAdministrador(ua);
        alerta.setNivelAlerta(na);

        AlertaController.inserir(alerta);

        return ResponseBuilder.getResponse(201, alerta);
    }

    @ValidarTokenAutenticacao
    @GET
    @Path("/{idUsuarioCliente}")
    public Response getUsuarioCliente(@PathParam("idUsuarioCliente") int idUsuarioCliente) throws BancoDeDadosException, GenericException {
        UsuarioCliente uc = UsuarioClienteController.retornaUsuarioCliente(idUsuarioCliente);

        ArrayList<Alerta> listaAlerta = AlertaController.retornaAlertas(uc);

        if (listaAlerta.size() == 0) {
            ErrorMessage m = new ErrorMessage("Nenhum alerta foi encontrado para tal usuário", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }

        return ResponseBuilder.getResponse(200, listaAlerta);
    }

    @ValidarTokenAutenticacao
    @Path("/usuarioPossuiAlerta/")
    @PUT
    public Response atualizarAlertaPossuiUsuario(AlertaPossuiUsuario alertaPossuiUsuario) throws BancoDeDadosException, CampoIncorretoException, GenericException {

        AlertaController.atualizarVinculoAlertaUsuario(alertaPossuiUsuario);

        return ResponseBuilder.getResponse(201, alertaPossuiUsuario);
    }

    @ValidarTokenAutenticacao
    @POST
    @Path("/enviarSMS/{idAlerta}&{mensagem}")
    public Response enviarSMS(@PathParam("idAlerta") int idAlerta, @PathParam("mensagem") String mensagem) throws BancoDeDadosException, GenericException {

        ArrayList<Telefone> listaTelefone = AlertaController.retornaTelefonesAlerta(idAlerta, true);
        
        ArrayList<RequisicaoEnvioSMSTelefone> listaRequisicoes = new ArrayList<>();
        
        for (Telefone telefone : listaTelefone) {
            RequisicaoEnvioSMSTelefone requisicaoEnvioSMSTelefone = new RequisicaoEnvioSMSTelefone();
            
            requisicaoEnvioSMSTelefone.setMensagemErro("");
            
            requisicaoEnvioSMSTelefone.setTelefone("+55" + telefone.getTelefone());
            
            listaRequisicoes.add(requisicaoEnvioSMSTelefone);
        }
        
        RequisicaoEnvioSMS requisicaoEnvioSMS = new RequisicaoEnvioSMS();
        requisicaoEnvioSMS.setHoraRequisicao(new Date());
        requisicaoEnvioSMS.setMensagem(mensagem);
        requisicaoEnvioSMS.setTelefones(listaRequisicoes);
        
       
        RequisicaoEnvioSMSController.inserir(requisicaoEnvioSMS);

        return ResponseBuilder.getResponse(201, requisicaoEnvioSMS);
    }

}
