package DAO;

import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Alerta;
import Model.AlertaRegiaoAfetada;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AlertaRegiaoAfetadaDAO {

    public static void inserir(int idAlerta, ArrayList<AlertaRegiaoAfetada> listaRegiaoAfetada, boolean abrirConexao) throws BancoDeDadosException {
        try {

            if (abrirConexao) {
                ConexaoBD.AbrirConexao();
                ConexaoBD.setAutoCommit(false);
            }

            if (listaRegiaoAfetada == null) {
                return;
            }

            for (AlertaRegiaoAfetada alertaLocalizacao : listaRegiaoAfetada) {
                String query = "INSERT INTO alertaRegiaoAfetada (latitude,longitude,titulo,subtitulo,abrangencia,status,idAlerta, corCirculo, corBordaCirculo) values (?,?,?,?,?,?,?,?,?)";

                PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
                pst.setDouble(1, alertaLocalizacao.getLatitude());
                pst.setDouble(2, alertaLocalizacao.getLongitude());
                pst.setString(3, alertaLocalizacao.getTitulo());
                pst.setString(4, alertaLocalizacao.getSubTitulo());
                pst.setDouble(5, alertaLocalizacao.getAbrangencia());
                pst.setString(6, alertaLocalizacao.getStatus().substring(0, 1));
                pst.setInt(7, idAlerta);
                pst.setString(8, alertaLocalizacao.getCorCirculo());
                pst.setString(9, alertaLocalizacao.getCorBordaCirculo());

                pst.execute();
            }

            if (abrirConexao) {
                ConexaoBD.setCommit();
            }
        } catch (SQLException | GenericException ex) {

            if (abrirConexao) {
                ConexaoBD.setRollBack();
                ConexaoBD.setAutoCommit(true);
            }

            throw new BancoDeDadosException("Erro ao inserir as regiões afetadas no alerta. Erro: " + ex.getMessage());
        } finally {

            if (abrirConexao) {
                ConexaoBD.FecharConexao();
            }

        }
    }

    public static ArrayList<AlertaRegiaoAfetada> retornaAlertaRegiaoAfetada(Alerta alerta, boolean abrirConexao) throws BancoDeDadosException, GenericException {
        try {
            ArrayList<AlertaRegiaoAfetada> listaRegiaoAfetada = new ArrayList<>();

            if (abrirConexao) {
                ConexaoBD.AbrirConexao();
            }
            String queryAlerta = "SELECT * FROM alertaRegiaoAfetada WHERE idAlerta=? AND status='A'";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(queryAlerta);
            stmt.setInt(1, alerta.getId());

            ResultSet resulSet = stmt.executeQuery();

            while (resulSet.next()) {
                AlertaRegiaoAfetada alertaRegiaoAfetada = new AlertaRegiaoAfetada();

                alertaRegiaoAfetada.setId(resulSet.getInt("id"));
                alertaRegiaoAfetada.setLatitude(resulSet.getDouble("latitude"));
                alertaRegiaoAfetada.setLongitude(resulSet.getDouble("longitude"));
                alertaRegiaoAfetada.setTitulo(resulSet.getString("titulo"));
                alertaRegiaoAfetada.setSubTitulo(resulSet.getString("subtitulo"));
                alertaRegiaoAfetada.setAbrangencia(resulSet.getDouble("abrangencia"));
                alertaRegiaoAfetada.setIdAlerta(resulSet.getInt("idAlerta"));

                String status = resulSet.getString("status");
                if (status.equals("A")) {
                    status = "Ativo";
                } else {
                    status = "Cancelado";
                }

                alertaRegiaoAfetada.setStatus(status);

                alertaRegiaoAfetada.setCorCirculo(resulSet.getString("corCirculo"));
                alertaRegiaoAfetada.setCorBordaCirculo(resulSet.getString("corBordaCirculo"));

                listaRegiaoAfetada.add(alertaRegiaoAfetada);
            }

            resulSet.close();
            stmt.close();

            return (listaRegiaoAfetada);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar as regiões afetadas no alerta. Erro: " + ex.getMessage());
        } finally {

            if (abrirConexao) {
                ConexaoBD.FecharConexao();
            }

        }
    }
}
