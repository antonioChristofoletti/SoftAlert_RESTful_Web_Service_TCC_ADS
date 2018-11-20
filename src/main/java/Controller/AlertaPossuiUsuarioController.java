package Controller;

import DAO.AlertaPossuiUsuarioDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Alerta;
import Model.AlertaPossuiUsuario;
import Model.LocalizacaoAlerta;
import Model.Usuario;
import java.util.ArrayList;

public class AlertaPossuiUsuarioController {

    public static void gerarVinculoUsuarioComAlerta(int idAlerta, ArrayList<LocalizacaoAlerta> listaLocalizacoesAlerta) throws BancoDeDadosException {
        AlertaPossuiUsuarioDAO.gerarVinculoUsuarioComAlerta(idAlerta, listaLocalizacoesAlerta);
    }

    public static ArrayList<AlertaPossuiUsuario> getAlertaPossuiUsuario(Alerta alerta, Usuario usuario, Boolean abrirConexao) throws BancoDeDadosException, GenericException {
        return AlertaPossuiUsuarioDAO.getAlertaPossuiUsuario(alerta, usuario, abrirConexao);
    }
}
