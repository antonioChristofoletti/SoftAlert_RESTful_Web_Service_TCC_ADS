package Controller;

import DAO.AlertaUsuarioClienteDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Model.AlertaUsuarioCliente;
import Model.UsuarioCliente;
import java.util.ArrayList;

public class AlertaUsuarioClienteController {
    
    public static void inserir(UsuarioCliente usuarioCliente, AlertaUsuarioCliente alerta) throws BancoDeDadosException, GenericException, CampoIncorretoException {
        
        validarCampos(alerta);
        
        AlertaUsuarioClienteDAO.inserir(usuarioCliente, alerta);
    }

    public static void editar(AlertaUsuarioCliente alerta) throws BancoDeDadosException, GenericException, CampoIncorretoException {
        
       validarCampos(alerta);
        
       AlertaUsuarioClienteDAO.editar(alerta);
    }

    public static ArrayList<AlertaUsuarioCliente> getAlertaUsuarioCliente(UsuarioCliente usuarioCliente) throws BancoDeDadosException, GenericException {
       return AlertaUsuarioClienteDAO.getAlertaUsuarioCliente(usuarioCliente);
    }
    
    public static void validarCampos(AlertaUsuarioCliente alertaUsuarioCliente) throws CampoIncorretoException {

        if (alertaUsuarioCliente.getDesastreAvistado().contains("*"))
            throw new CampoIncorretoException("O desastreAvistado é inválido");

        if (alertaUsuarioCliente.getSituacaoUsuario().contains("*"))
            throw new CampoIncorretoException("A situacaoUsuario é inválido");
        
        if (alertaUsuarioCliente.getDescricao().equals(""))
            throw new CampoIncorretoException("A descricao é inválida");

        if (alertaUsuarioCliente.getLatitude() == 0 && alertaUsuarioCliente.getLongitude() == 0)
            throw new CampoIncorretoException("A localizacao é inválida");
    }
}