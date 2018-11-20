package Controller;

import DAO.LocalizacaoAlertaDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Alerta;
import Model.Endereco;
import Model.LocalizacaoAlerta;
import java.util.ArrayList;

public class LocalizacaoAlertaController {
    public static void inserir(ArrayList<Endereco> listaEnderecos) throws BancoDeDadosException, GenericException{
        LocalizacaoAlertaDAO.inserir(listaEnderecos);
    }
        
    public static ArrayList<LocalizacaoAlerta> getLocalizacoesAlerta() throws BancoDeDadosException, GenericException{
        return LocalizacaoAlertaDAO.getLocalizacoesAlerta();
    }
    
    public static ArrayList<LocalizacaoAlerta> getLocalizacoesAlerta(Alerta alerta, Boolean abrirConexao) throws BancoDeDadosException, GenericException{
        return LocalizacaoAlertaDAO.getLocalizacoesAlerta(alerta, abrirConexao);
    }
}