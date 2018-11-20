package DAO;

import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Alerta;
import Model.Endereco;
import Model.LocalizacaoAlerta;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LocalizacaoAlertaDAO {

    public static void inserir(ArrayList<Endereco> listaEndereco) throws BancoDeDadosException, GenericException {
        try {
            for (Endereco endereco : listaEndereco) {
                String query = "INSERT INTO localizacaoAlerta (descricao,tipo) VALUES(?,?)";

                if (!localizacaoAlertaExiste("pais", endereco.getPais())) {
                    PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
                    pst.setString(1, endereco.getPais());
                    pst.setString(2, "P");
                    pst.execute();
                }

                if (!localizacaoAlertaExiste("estado", endereco.getEstado())) {
                    PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
                    pst.setString(1, endereco.getEstado());
                    pst.setString(2, "E");
                    pst.execute();
                }

                if (!localizacaoAlertaExiste("cidade", endereco.getCidade())) {
                    PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
                    pst.setString(1, endereco.getCidade());
                    pst.setString(2, "C");
                    pst.execute();
                }
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir localizações alerta. Erro: " + ex.getMessage());
        }
    }

    private static Boolean localizacaoAlertaExiste(String tipo, String descricao) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "select COUNT(*) quantidade from localizacaoAlerta where tipo=? and descricao=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);

            stmt.setString(1, tipo.toUpperCase().substring(0, 1));
            stmt.setString(2, descricao);

            ResultSet rs = stmt.executeQuery();

            if (!rs.first()) {
                return false;
            }

            if (rs.getInt("quantidade") > 0) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao pesquisar localização alerta. Erro: " + e.getMessage());
        }
    }

    public static ArrayList<LocalizacaoAlerta> getLocalizacoesAlerta() throws BancoDeDadosException, GenericException {
        try {
            ArrayList<LocalizacaoAlerta> listaLocalizacaoAlerta = new ArrayList<>();
            ConexaoBD.AbrirConexao();

            String query = "SELECT * FROM localizacaoalerta";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalizacaoAlerta localizacaoAlerta = new LocalizacaoAlerta();

                localizacaoAlerta.setId(rs.getInt("id"));
                localizacaoAlerta.setDescricao(rs.getString("descricao"));
                localizacaoAlerta.setTipo(rs.getString("tipo"));

                listaLocalizacaoAlerta.add(localizacaoAlerta);
            }

            rs.close();
            stmt.close();
            return (listaLocalizacaoAlerta);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar as localizações de alertas. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static ArrayList<LocalizacaoAlerta> getLocalizacoesAlerta(Alerta alerta, Boolean abrirConexao) throws BancoDeDadosException, GenericException {
        try {
            ArrayList<LocalizacaoAlerta> listaLocalizacaoAlerta = new ArrayList<>();

            if (abrirConexao) {
                ConexaoBD.AbrirConexao();
            }

            String query = "select la.id, "
                    + "la.tipo, "
                    + "la.descricao "
                    + "from localizacaoalerta la "
                    + "inner join alerta_possui_localizacaoalerta apla on apla.id = la.id WHERE apla.idAlerta=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, alerta.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalizacaoAlerta localizacaoAlerta = new LocalizacaoAlerta();

                localizacaoAlerta.setId(rs.getInt("id"));
                localizacaoAlerta.setDescricao(rs.getString("descricao"));
                localizacaoAlerta.setTipo(rs.getString("tipo"));

                listaLocalizacaoAlerta.add(localizacaoAlerta);
            }

            rs.close();
            stmt.close();
            return (listaLocalizacaoAlerta);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar as localizações de alertas. Erro: " + ex.getMessage());
        } finally {
            if (abrirConexao) {
                ConexaoBD.FecharConexao();
            }
        }
    }
}
