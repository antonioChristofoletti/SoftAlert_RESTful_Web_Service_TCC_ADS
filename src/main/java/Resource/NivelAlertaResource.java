package Resource;

import Controller.NivelAlertaController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Geral.ResponseBuilder;
import Model.ErrorMessage;
import Model.NivelAlerta;
import Resource.Autenticacao.ValidarTokenAutenticacao;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("nivelAlerta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NivelAlertaResource {

    @ValidarTokenAutenticacao
    @POST
    public Response inserirNivelAlerta(NivelAlerta nivelAlerta) throws BancoDeDadosException, CampoIncorretoException, GenericException {

        NivelAlertaController.inserir(nivelAlerta);

        return ResponseBuilder.getResponse(201, nivelAlerta);
    }

    @ValidarTokenAutenticacao
    @PUT
    public Response editarNivelAlerta(NivelAlerta nivelAlerta) throws BancoDeDadosException, CampoIncorretoException, GenericException {
        NivelAlertaController.editar(nivelAlerta);

        return ResponseBuilder.getResponse(200, nivelAlerta);
    }

    @ValidarTokenAutenticacao
    @GET
    public Response retornaNivelAlerta() throws BancoDeDadosException, CampoIncorretoException, GenericException {

        ArrayList<NivelAlerta> listaNiveisAlertas = NivelAlertaController.getNiveisAlertas();

        if (listaNiveisAlertas.isEmpty()) {
            ErrorMessage m = new ErrorMessage("Níveis de alerta não foram encontrados", 404, "");
            return ResponseBuilder.getResponse(404, m);
        }

        return ResponseBuilder.getResponse(200, listaNiveisAlertas);
    }
}