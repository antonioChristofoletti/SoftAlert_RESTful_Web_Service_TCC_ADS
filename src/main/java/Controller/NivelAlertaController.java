package Controller;

import DAO.NivelAlertaDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.NivelAlerta;
import java.util.ArrayList;

public class NivelAlertaController {

    public static void inserir(NivelAlerta nivelAlerta) throws BancoDeDadosException, GenericException {
        NivelAlertaDAO.inserir(nivelAlerta);
    }

    public static void editar(NivelAlerta nivelAlerta) throws BancoDeDadosException, GenericException {
        NivelAlertaDAO.editar(nivelAlerta);
    }

    public static ArrayList<NivelAlerta> getNiveisAlertas() throws BancoDeDadosException, GenericException {
        return NivelAlertaDAO.getNiveisAlertas();
    }
}
