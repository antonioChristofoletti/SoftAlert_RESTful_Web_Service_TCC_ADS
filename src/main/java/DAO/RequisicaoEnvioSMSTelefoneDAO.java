package DAO;

import DAO.ConexaoBD;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.RequisicaoEnvioSMS;
import Model.RequisicaoEnvioSMSTelefone;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequisicaoEnvioSMSTelefoneDAO {

    public static void inserir(RequisicaoEnvioSMS requisicaoEnvioSMS) throws BancoDeDadosException {
        try {

            ArrayList<RequisicaoEnvioSMSTelefone> lista = requisicaoEnvioSMS.getTelefones();

            for (int i = 0; i < lista.size(); i++) {
                RequisicaoEnvioSMSTelefone telefone = lista.get(i);

                String query = "INSERT INTO RequisicaoEnvioSMSTelefone (telefone,mensagemErro,mensagemProcessada,idRequisicaoEnvioSMS) VALUES(?,?,?,?)";

                PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
                pst.setString(1, telefone.getTelefone());
                
                String mensagemErro = telefone.getMensagemErro();
                
                if(mensagemErro == null){
                    mensagemErro = "";
                    telefone.setMensagemErro("");
                }
                
                pst.setString(2, mensagemErro);
                pst.setString(3, String.valueOf('N'));
                pst.setInt(4, requisicaoEnvioSMS.getId());

                pst.execute();
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir o telefone para a requisição de envio de SMS. Erro: " + ex.getMessage());
        }
    }

    public static void editar(RequisicaoEnvioSMSTelefone requisicaoEnvioSMSTelefone) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "UPDATE RequisicaoEnvioSMSTelefone SET telefone=?,mensagemErro=?,mensagemProcessada=?,idRequisicaoEnvioSMS=? WHERE id=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, requisicaoEnvioSMSTelefone.getTelefone());
            pst.setString(2, requisicaoEnvioSMSTelefone.getMensagemErro());
            pst.setString(3, String.valueOf(requisicaoEnvioSMSTelefone.getMensagemProcessada()));
            pst.setInt(4, requisicaoEnvioSMSTelefone.getRequisicaoEnvioSMS().getId());
            pst.setInt(5, requisicaoEnvioSMSTelefone.getId());

            pst.execute();
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir o telefone para a requisição de envio de SMS. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }
}
