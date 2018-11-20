package DAO;

import DAO.ConexaoBD;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.RequisicaoEnvioSMS;
import Model.RequisicaoEnvioSMSTelefone;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class RequisicaoEnvioSMSDAO {

    public static void inserir(RequisicaoEnvioSMS requisicaoEnvioSMS) throws BancoDeDadosException {
        try {
            ConexaoBD.AbrirConexao();
            ConexaoBD.setAutoCommit(false);

            String query = "INSERT INTO RequisicaoEnvioSMS (horaRequisicao,mensagem) VALUES(?,?)";

            int ultimoID = getProximoIDRequisicaoEnvioSMS();

            requisicaoEnvioSMS.setId(ultimoID);

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);

            Date horaRequisicao = requisicaoEnvioSMS.getHoraRequisicao();

            if (horaRequisicao == null) {
                horaRequisicao = new Date();
                requisicaoEnvioSMS.setHoraRequisicao(horaRequisicao);
            }
            
            pst.setTimestamp(1, new Timestamp(horaRequisicao.getTime()));
            pst.setString(2, requisicaoEnvioSMS.getMensagem());
            pst.execute();

            RequisicaoEnvioSMSTelefoneDAO.inserir(requisicaoEnvioSMS);

            ConexaoBD.setCommit();
        } catch (SQLException | GenericException ex) {
            ConexaoBD.setRollBack();
            ConexaoBD.setAutoCommit(true);
            throw new BancoDeDadosException("Erro ao inserir mensagem de SMS. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static int getProximoIDRequisicaoEnvioSMS() throws BancoDeDadosException {
        try {
            String query = "SELECT AUTO_INCREMENT maxID FROM information_schema.TABLES WHERE TABLE_NAME='requisicaoenviosms' AND TABLE_SCHEMA='softalert'";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (!rs.first()) {
                return 0;
            }

            return rs.getInt("maxID");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao retornar o último registro da tabela de requisição de envio SMS. Erro: " + e.getMessage());
        }
    }

    public static ArrayList<RequisicaoEnvioSMS> getRequisicaoEnvioSMSAbertas() throws BancoDeDadosException, GenericException {
        try {
            ArrayList<RequisicaoEnvioSMS> requisicoes = new ArrayList<>();
            ConexaoBD.AbrirConexao();

            String query = "SELECT re.horaRequisicao,"
                    + "re.mensagem,"
                    + "ret.telefone,"
                    + "ret.mensagemErro,"
                    + "ret.mensagemProcessada,"
                    + "ret.idRequisicaoEnvioSMS,"
                    + "ret.id idRequisicaoEnvioSMSTelefone "
                    + "FROM "
                    + "RequisicaoEnvioSMS re "
                    + "INNER JOIN RequisicaoEnvioSMSTelefone ret ON ret.idRequisicaoEnvioSMS = re.id "
                    + "WHERE ret.mensagemProcessada = 'N'"
                    + "ORDER BY ret.id";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            int idRequisicaoCicloAnterior = 0;
            RequisicaoEnvioSMS requisicaoCicloAtual = null;

            while (rs.next()) {
                int idRequisicaoCicloAtual = rs.getInt("idRequisicaoEnvioSMS");

                if (idRequisicaoCicloAtual != idRequisicaoCicloAnterior) {

                    if (requisicaoCicloAtual != null) {
                        requisicoes.add(requisicaoCicloAtual);
                    }

                    requisicaoCicloAtual = new RequisicaoEnvioSMS();

                    requisicaoCicloAtual.setHoraRequisicao(rs.getDate("horaRequisicao"));

                    requisicaoCicloAtual.setId(rs.getInt("idRequisicaoEnvioSMS"));

                    requisicaoCicloAtual.setMensagem(rs.getString("mensagem"));
                }

                idRequisicaoCicloAnterior = idRequisicaoCicloAtual;

                RequisicaoEnvioSMSTelefone requisicaoEnvioSMSTelefone = new RequisicaoEnvioSMSTelefone();

                requisicaoEnvioSMSTelefone.setId(rs.getInt("idRequisicaoEnvioSMSTelefone"));

                requisicaoEnvioSMSTelefone.setMensagemErro(rs.getString("mensagemErro"));

                requisicaoEnvioSMSTelefone.setMensagemProcessada(rs.getString("mensagemProcessada").charAt(0));

                requisicaoEnvioSMSTelefone.setRequisicaoEnvioSMS(requisicaoCicloAtual);

                requisicaoEnvioSMSTelefone.setTelefone(rs.getString("telefone"));

                requisicaoCicloAtual.getTelefones().add(requisicaoEnvioSMSTelefone);
            }

            if (idRequisicaoCicloAnterior != 0) {
                requisicoes.add(requisicaoCicloAtual);
            }

            rs.close();
            stmt.close();
            return requisicoes;
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar as mensagens de SMS. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }
}