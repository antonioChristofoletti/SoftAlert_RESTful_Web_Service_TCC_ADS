package Controller;

import DAO.UsuarioClienteDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Model.UsuarioCliente;
import java.util.ArrayList;

public abstract class UsuarioClienteController extends UsuarioController {

    public static UsuarioCliente inserir(UsuarioCliente uc) throws BancoDeDadosException, CampoIncorretoException, GenericException {

         validaCamposUsuario(uc);

        return UsuarioClienteDAO.inserir(uc);
    }

    public static UsuarioCliente editar(UsuarioCliente uc) throws BancoDeDadosException, GenericException, CampoIncorretoException {

        validaCamposUsuario(uc);

        return UsuarioClienteDAO.editar(uc);
    }

    public static ArrayList<UsuarioCliente> retornaTodosUsuariosClientes() throws BancoDeDadosException, GenericException {
        return UsuarioClienteDAO.getTodosUsuariosClientes();
    }

    public static UsuarioCliente retornaUsuarioCliente(int id) throws BancoDeDadosException, GenericException {
        return UsuarioClienteDAO.getUsuarioCliente(id);
    }   
}