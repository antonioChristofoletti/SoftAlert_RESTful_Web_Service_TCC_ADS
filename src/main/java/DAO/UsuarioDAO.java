package DAO;

import Model.Usuario;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public abstract class UsuarioDAO {

    public static int inserir(Usuario a) throws BancoDeDadosException {
        try {
            String query = "INSERT INTO Usuario (nome,cpf,dataNascimento,status) VALUES(?,?,?,?)";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, a.getNome());
            pst.setString(2, a.getCpf());
            pst.setTimestamp(3, new Timestamp(a.getDataNascimento().getTime()));

            String status = a.getStatus();

            if (status == null) {
                status = "A";
            }

            pst.setString(4, status);

            pst.execute();

            return getUltimoIdUsuario()-1;
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir usuário. Erro: " + ex.getMessage());
        }
    }

    public static void editar(Usuario a) throws BancoDeDadosException {
        try {
            String query = "UPDATE Usuario SET nome=?, cpf=?,dataNascimento=?,status=? WHERE id=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, a.getNome());
            pst.setString(2, a.getCpf());
            pst.setTimestamp(3, new Timestamp(a.getDataNascimento().getTime()));
            pst.setString(4, a.getStatus());
            pst.setInt(5, a.getIdUsuario());

            pst.execute();
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao editar usuário. Erro: " + ex.getMessage());
        }
    }

    public static int getUltimoIdUsuario() throws BancoDeDadosException {
        try {
            String query = "SELECT AUTO_INCREMENT maxID FROM information_schema.TABLES WHERE TABLE_NAME='usuario' AND TABLE_SCHEMA='softalert'";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (!rs.first()) {
                return 1;
            }

            return rs.getInt("maxID");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao retornar o último registro de usuário. Erro: " + e.getMessage());
        }
    }

    public static int getIdUsuario(int id) throws BancoDeDadosException, GenericException {
        Boolean estahConectado = false;
        try {
            if (ConexaoBD.conectado()) {
                estahConectado = true;
            } else {
                ConexaoBD.AbrirConexao();
            }

            String query = "SELECT u.id FROM UsuarioAdministrador ua INNER JOIN Usuario u ON u.id = ua.idUsuario where ua.id=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (!rs.first()) {
                return 0;
            }

            return rs.getInt("id");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao retornar o ID do usuário. Erro: " + e.getMessage());
        } finally {
            if (!estahConectado) {
                ConexaoBD.FecharConexao();
            }
        }
    }

    public static Boolean cpfExiste(int idUsuario, String cpf) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "";

            query = "SELECT COUNT(*) quantidade FROM Usuario where cpf=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);

            stmt.setString(1, cpf);

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
            throw new BancoDeDadosException("Erro ao pesquisar CPF. Erro: " + e.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }
}