package Controller;

import DAO.AlertaRegiaoAfetadaDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Alerta;
import Model.AlertaRegiaoAfetada;
import java.util.ArrayList;

public class AlertaRegiaoAfetadaController {
    
    public static void inserir(int idAlerta, ArrayList<AlertaRegiaoAfetada> listaRegiaoAfetada, boolean  abrirConexao) throws BancoDeDadosException{
        AlertaRegiaoAfetadaDAO.inserir(idAlerta, listaRegiaoAfetada, abrirConexao);
    }
    
    public static ArrayList<AlertaRegiaoAfetada> retornaAlertaRegiaoAfetada(Alerta alerta, boolean abrirConexao) throws BancoDeDadosException, GenericException{
        return AlertaRegiaoAfetadaDAO.retornaAlertaRegiaoAfetada(alerta, abrirConexao);
    }
}