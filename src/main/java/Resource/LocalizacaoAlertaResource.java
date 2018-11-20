package Resource;

import Controller.LocalizacaoAlertaController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Geral.ResponseBuilder;
import Model.Endereco;
import Model.ErrorMessage;
import Model.LocalizacaoAlerta;
import Resource.Autenticacao.ValidarTokenAutenticacao;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("localizacaoAlerta")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LocalizacaoAlertaResource {

    @ValidarTokenAutenticacao
    @POST
    public Response inserirLocalizacaoAlerta(ArrayList<Endereco> listaEndereco) throws BancoDeDadosException, CampoIncorretoException, GenericException {
        LocalizacaoAlertaController.inserir(listaEndereco);

        return ResponseBuilder.getResponse(201, listaEndereco);
    }
    
    @ValidarTokenAutenticacao
    @GET
    public Response getLocalizacoesAlerta() throws BancoDeDadosException, CampoIncorretoException, GenericException {
        ArrayList<LocalizacaoAlerta> listaLocalizacaoAlerta = LocalizacaoAlertaController.getLocalizacoesAlerta();
        
        if (listaLocalizacaoAlerta.isEmpty()) {
            ErrorMessage m = new ErrorMessage("Localizações de alerta não foram encontrados", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }

        return ResponseBuilder.getResponse(201, listaLocalizacaoAlerta);
    }
}