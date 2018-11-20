package DAO;

import Controller.TelefoneController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Telefone;
import Model.UsuarioCliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioClienteDAO {

    public static UsuarioCliente inserir(UsuarioCliente uc) throws BancoDeDadosException {
        try {
            ConexaoBD.AbrirConexao();
            ConexaoBD.setAutoCommit(false);

            int idUsuario = UsuarioDAO.inserir(uc);

            uc.setIdUsuario(idUsuario);

            String query = "INSERT INTO UsuarioCliente (idUsuario) VALUES(?)";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setInt(1, uc.getIdUsuario());
            pst.execute();

            uc.setId(getUltimoIdUsuarioCliente());

            TelefoneController.inserir(uc);

            ConexaoBD.setCommit();

            return uc;
        } catch (SQLException | GenericException ex) {
            ConexaoBD.setRollBack();
            ConexaoBD.setAutoCommit(true);
            throw new BancoDeDadosException("Erro ao inserir usuário cliente. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static UsuarioCliente editar(UsuarioCliente uc) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();
            ConexaoBD.setAutoCommit(false);

            int idUsuario = getIdUsuarioCliente(uc.getId());

            uc.setIdUsuario(idUsuario);

            UsuarioDAO.editar(uc);

            TelefoneController.remover(uc);
            TelefoneController.inserir(uc);

            ConexaoBD.setCommit();

            return uc;
        } catch (SQLException ex) {
            ConexaoBD.setRollBack();
            ConexaoBD.setAutoCommit(true);
            throw new BancoDeDadosException("Erro ao editar usuário cliente. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static ArrayList<UsuarioCliente> getTodosUsuariosClientes() throws BancoDeDadosException, GenericException {
        try {
            ArrayList<UsuarioCliente> listaUsuarios = new ArrayList<>();
            ConexaoBD.AbrirConexao();

            String query = "select u.id idUsuario,"
                    + "u.nome,"
                    + "u.cpf,"
                    + "u.dataNascimento,"
                    + "u.status,"
                    + "uc.id,"
                    + "t.telefone,"
                    + "t.id idtelefone,"
                    + "t.status statusTelefone "
                    + "from Usuario u "
                    + "inner join usuarioCliente uc on uc.idUsuario = u.id "
                    + "inner join telefone t on t.idUsuario = u.id";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            int idUsuarioCicloAnterior = 0;
            UsuarioCliente usuarioClienteCicloAtual = null;

            while (rs.next()) {
                int idRequisicaoCicloAtual = rs.getInt("idUsuario");

                if (idRequisicaoCicloAtual != idUsuarioCicloAnterior) {

                    if (usuarioClienteCicloAtual != null) {
                        listaUsuarios.add(usuarioClienteCicloAtual);
                    }

                    usuarioClienteCicloAtual = new UsuarioCliente();
                    usuarioClienteCicloAtual.setNome(rs.getString("nome"));
                    usuarioClienteCicloAtual.setCpf(rs.getString("cpf"));
                    usuarioClienteCicloAtual.setDataNascimento(rs.getDate("dataNascimento"));
                    usuarioClienteCicloAtual.setStatus(rs.getString("status"));
                    usuarioClienteCicloAtual.setId(rs.getInt("id"));
                    usuarioClienteCicloAtual.setIdUsuario(rs.getInt("idUsuario"));

                    usuarioClienteCicloAtual.setListaTelefones(new ArrayList<Telefone>());
                }
                idUsuarioCicloAnterior = idRequisicaoCicloAtual;

                Telefone t = new Telefone();

                t.setId(rs.getInt("idTelefone"));
                t.setStatus(rs.getString("statusTelefone"));
                t.setTelefone(rs.getString("telefone"));
                t.setUsuario(usuarioClienteCicloAtual);

                usuarioClienteCicloAtual.getListaTelefones().add(t);
            }

            if (idUsuarioCicloAnterior != 0) {
                listaUsuarios.add(usuarioClienteCicloAtual);
            }

            rs.close();
            stmt.close();
            return (listaUsuarios);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar os usuários clientes. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static UsuarioCliente getUsuarioCliente(int id) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "select u.id idUsuario,"
                    + "u.nome,"
                    + "u.cpf,"
                    + "u.dataNascimento,"
                    + "u.status,"
                    + "uc.id,"
                    + "t.telefone,"
                    + "t.id idtelefone,"
                    + "t.status statusTelefone "
                    + "from Usuario u "
                    + "inner join usuarioCliente uc on uc.idUsuario = u.id "
                    + "inner join telefone t on t.idUsuario = u.id "
                    + "WHERE uc.id=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            UsuarioCliente ua = null;

            while (rs.next()) {
                if (ua == null) {
                    ua = new UsuarioCliente();

                    ua.setNome(rs.getString("nome"));
                    ua.setCpf(rs.getString("cpf"));
                    ua.setDataNascimento(rs.getDate("dataNascimento"));
                    ua.setStatus(rs.getString("status"));
                    ua.setId(rs.getInt("id"));
                    ua.setIdUsuario(rs.getInt("idUsuario"));
                    ua.setListaTelefones(new ArrayList<Telefone>());
                }

                Telefone t = new Telefone();

                t.setId(rs.getInt("idTelefone"));
                t.setStatus(rs.getString("statusTelefone"));
                t.setTelefone(rs.getString("telefone"));
                t.setUsuario(ua);

                ua.getListaTelefones().add(t);
            }

            rs.close();
            stmt.close();
            return ua;
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar o usuário cliente. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static int getUltimoIdUsuarioCliente() throws BancoDeDadosException {
        try {
            String query = "SELECT AUTO_INCREMENT maxID FROM information_schema.TABLES WHERE TABLE_NAME='usuariocliente' AND TABLE_SCHEMA='softalert'";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (!rs.first()) {
                return 1;
            }

            return rs.getInt("maxID")-1;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao retornar o último registro de usuário. Erro: " + e.getMessage());
        }
    }

    public static int getIdUsuarioCliente(int id) throws BancoDeDadosException {
        try {
            String query = "SELECT idUsuario FROM UsuarioCliente WHERE id=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            rs.first();

            return rs.getInt("idUsuario");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao retornar o último registro de usuário. Erro: " + e.getMessage());
        }
    }
}