package Controller;

import DAO.EnderecoDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Endereco;
import Model.Usuario;
import java.util.ArrayList;

public abstract class EnderecoController {

    public static void inserir(Usuario usuario) throws BancoDeDadosException, GenericException {
        EnderecoDAO.inserir(usuario);
    }

    public static void remover(Usuario usuario) throws BancoDeDadosException {
        EnderecoDAO.remover(usuario);
    }
    
    public static ArrayList<Endereco> retornaLista(Usuario usuario) throws BancoDeDadosException, GenericException{
        return EnderecoDAO.getTelefonesUsuario(usuario);
    }
}