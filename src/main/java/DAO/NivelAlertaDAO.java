package DAO;

import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.NivelAlerta;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NivelAlertaDAO {

    public static void inserir(NivelAlerta nivelAlerta) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "INSERT INTO nivelAlerta (descricao, cor, status) VALUES(?,?,?)";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, nivelAlerta.getDescricao());
            pst.setString(2, nivelAlerta.getCor());
            pst.setString(3, nivelAlerta.getStatus().substring(0,1));

            pst.execute();

        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir nível alerta. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static void editar(NivelAlerta nivelAlerta) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "UPDATE nivelAlerta SET descricao=?, cor=?, status=? WHERE id=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, nivelAlerta.getDescricao());
            pst.setString(2, nivelAlerta.getCor());
            pst.setString(3, nivelAlerta.getStatus().substring(0,1));
            pst.setInt(4, nivelAlerta.getId());

            pst.execute();

        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao editar nível alerta. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static ArrayList<NivelAlerta> getNiveisAlertas() throws BancoDeDadosException, GenericException {
        try {
            ArrayList<NivelAlerta> listaNivelAlerta = new ArrayList<>();
            ConexaoBD.AbrirConexao();

            String query = "SELECT * FROM nivelAlerta";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NivelAlerta nivelAlerta = new NivelAlerta();

                nivelAlerta.setId(rs.getInt("id"));
                nivelAlerta.setCor(rs.getString("cor"));
                nivelAlerta.setDescricao(rs.getString("descricao"));

                String status = rs.getString("status");

                if (status.equals("A")) {
                    nivelAlerta.setStatus("Ativo");
                } else {
                    nivelAlerta.setStatus("Cancelado");
                }

                listaNivelAlerta.add(nivelAlerta);
            }

            rs.close();
            stmt.close();
            return (listaNivelAlerta);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar os níveis de alerta. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }
}
