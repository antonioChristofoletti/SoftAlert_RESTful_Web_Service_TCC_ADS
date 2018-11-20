package Controller;

import DAO.TelefoneDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Telefone;
import Model.Usuario;

public abstract class TelefoneController {

    public static void inserir(Usuario usuario) throws BancoDeDadosException {
        TelefoneDAO.inserir(usuario);
    }

    public static void remover(Usuario usuario) throws BancoDeDadosException {
        TelefoneDAO.remover(usuario);
    }

    public static Boolean telefoneExiste(Telefone telefone) throws BancoDeDadosException, GenericException {
        return TelefoneDAO.telefoneExiste(telefone);
    }
}
