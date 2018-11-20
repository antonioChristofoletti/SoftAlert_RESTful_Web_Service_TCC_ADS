package DAO;

import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Telefone;
import Model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TelefoneDAO {

    public static void inserir(Usuario usuario) throws BancoDeDadosException {
        try {
            for (Telefone telefone : usuario.getListaTelefones()) {

                String query = "INSERT INTO Telefone (telefone, status, idUsuario) VALUES(?,?,?)";

                PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
                pst.setString(1, telefone.getTelefone());
                
                String status = telefone.getStatus();
                
                if(status == null)
                    status = "A";
                
                pst.setString(2, status);
                pst.setInt(3, usuario.getIdUsuario());
                pst.execute();
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir telefone. Erro: " + ex.getMessage());
        }
    }

    public static void remover(Usuario usuario) throws BancoDeDadosException {
        try {

            String query = "DELETE FROM Telefone WHERE idUsuario=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setInt(1, usuario.getIdUsuario());
            pst.execute();
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao remover os telefones. Erro: " + ex.getMessage());
        }
    }

    public static Boolean telefoneExiste(Telefone telefone) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            int idUsuario = telefone.getUsuario().getIdUsuario();

            String query = "SELECT COUNT(*) quantidade FROM Telefone where telefone=? AND idUsuario <> ?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);

            stmt.setString(1, telefone.getTelefone());
            stmt.setInt(2, idUsuario);

            ResultSet rs = stmt.executeQuery();

            if (!rs.first()) {
                return false;
            }

            if (rs.getInt("quantidade") == 1 && idUsuario > 0) {
                return false;
            }

            if (rs.getInt("quantidade") > 0) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao validar telefone. Erro: " + e.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static ArrayList<Telefone> getTelefonesUsuario(Usuario usuario) throws BancoDeDadosException, GenericException {
        try {
            ArrayList<Telefone> listaTelefones = new ArrayList<>();
            ConexaoBD.AbrirConexao();

            String query = "SELECT * FROM Telefone WHERE idUsuario=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, usuario.getIdUsuario());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Telefone telefone = new Telefone();
                telefone.setId(rs.getInt("id"));
                telefone.setStatus(rs.getString("status"));
                telefone.setTelefone(rs.getString("telefone"));
                telefone.setUsuario(usuario);

                listaTelefones.add(telefone);
            }

            rs.close();
            stmt.close();
            return (listaTelefones);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar os telefones do usuário. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }
}