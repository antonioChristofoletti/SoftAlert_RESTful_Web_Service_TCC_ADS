package Controller;

import DAO.AlertaDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Alerta;
import Model.AlertaPossuiUsuario;
import Model.Telefone;
import Model.Usuario;
import java.util.ArrayList;

public class AlertaController {
    public static void inserir(Alerta alerta) throws BancoDeDadosException {
          AlertaDAO.inserir(alerta);
    }
    
    public static void atualizarVinculoAlertaUsuario(AlertaPossuiUsuario alertaPossuiUsuario) throws BancoDeDadosException {
          AlertaDAO.atualizarUsuarioPossuiAlerta(alertaPossuiUsuario);
    }
    
    public static ArrayList<Alerta> retornaAlertas(Usuario usuario) throws BancoDeDadosException, GenericException{
        return AlertaDAO.retornaAlertas(usuario);
    }
    
    public static ArrayList<Telefone> retornaTelefonesAlerta(int idAlerta, boolean abrirConexao) throws BancoDeDadosException, GenericException{
        return AlertaDAO.retornaTelefonesAlerta(idAlerta, abrirConexao);
    }
}