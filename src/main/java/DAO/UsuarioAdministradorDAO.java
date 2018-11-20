package DAO;

import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Telefone;
import Model.UsuarioAdministrador;
import Model.UsuarioCliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsuarioAdministradorDAO {

    public static UsuarioAdministrador inserir(UsuarioAdministrador ua) throws BancoDeDadosException {
        try {
            ConexaoBD.AbrirConexao();
            ConexaoBD.setAutoCommit(false);

            int idUsuario = UsuarioDAO.inserir(ua);

            ua.setIdUsuario(idUsuario);

            String query = "INSERT INTO UsuarioAdministrador (usuario, senha, idUsuario) VALUES(?,MD5(?),?)";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, ua.getUsuario());
            pst.setString(2, ua.getSenha());
            pst.setInt(3, ua.getIdUsuario());
            pst.execute();

            ua.setId(getUltimoIdUsuarioAdministrador());

            ConexaoBD.setCommit();

            return ua;
        } catch (SQLException | GenericException ex) {
            ConexaoBD.setRollBack();
            ConexaoBD.setAutoCommit(true);
            throw new BancoDeDadosException("Erro ao inserir usuário administrador. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static UsuarioAdministrador editar(UsuarioAdministrador ua) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();
            ConexaoBD.setAutoCommit(false);

            int idUsuario = getIdUsuarioDoUsuarioAdministrador(ua.getId());

            ua.setIdUsuario(idUsuario);

            UsuarioDAO.editar(ua);

            String query = "UPDATE UsuarioAdministrador SET usuario=?, senha=MD5(?), idUsuario=? WHERE id=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, ua.getUsuario());
            pst.setString(2, ua.getSenha());
            pst.setInt(3, ua.getIdUsuario());
            pst.setInt(4, ua.getId());
            pst.execute();

            ConexaoBD.setCommit();

            return ua;
        } catch (SQLException ex) {
            ConexaoBD.setRollBack();
            ConexaoBD.setAutoCommit(true);
            throw new BancoDeDadosException("Erro ao editar usuário administrador. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static ArrayList<UsuarioAdministrador> getTodosUsuariosAdministradores() throws BancoDeDadosException, GenericException {
        try {
            ArrayList<UsuarioAdministrador> listaUsuarios = new ArrayList<>();
            ConexaoBD.AbrirConexao();

            String query = "select u.id idUsuario,"
                    + "u.nome,"
                    + "u.cpf,"
                    + "u.dataNascimento,"
                    + "u.status,"
                    + "ua.id,"
                    + "ua.usuario,"
                    + "ua.senha,"
                    + "t.telefone,"
                    + "t.id idtelefone,"
                    + "t.status statusTelefone "
                    + "from Usuario u "
                    + "inner join usuarioadministrador ua on ua.idUsuario = u.id "
                    + "inner join telefone t on t.idUsuario = u.id";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            int idUsuarioCicloAnterior = 0;
            UsuarioAdministrador usuarioAdministradorCiclo = null;

            while (rs.next()) {
                int idRequisicaoCicloAtual = rs.getInt("idUsuario");

                if (idRequisicaoCicloAtual != idUsuarioCicloAnterior) {

                    if (usuarioAdministradorCiclo != null) {
                        listaUsuarios.add(usuarioAdministradorCiclo);
                    }

                    usuarioAdministradorCiclo = new UsuarioAdministrador();
                    usuarioAdministradorCiclo.setNome(rs.getString("nome"));
                    usuarioAdministradorCiclo.setCpf(rs.getString("cpf"));
                    usuarioAdministradorCiclo.setDataNascimento(rs.getDate("dataNascimento"));
                    usuarioAdministradorCiclo.setStatus(rs.getString("status"));
                    usuarioAdministradorCiclo.setUsuario(rs.getString("usuario"));
                    usuarioAdministradorCiclo.setSenha(rs.getString("senha"));
                    usuarioAdministradorCiclo.setId(rs.getInt("id"));
                    usuarioAdministradorCiclo.setIdUsuario(rs.getInt("idUsuario"));;

                    usuarioAdministradorCiclo.setListaTelefones(new ArrayList<Telefone>());
                }

                idUsuarioCicloAnterior = idRequisicaoCicloAtual;

                Telefone t = new Telefone();

                t.setId(rs.getInt("idTelefone"));
                t.setStatus(rs.getString("statusTelefone"));
                t.setTelefone(rs.getString("telefone"));
                t.setUsuario(usuarioAdministradorCiclo);

                usuarioAdministradorCiclo.getListaTelefones().add(t);
            }

            if (idUsuarioCicloAnterior != 0) {
                listaUsuarios.add(usuarioAdministradorCiclo);
            }

            rs.close();
            stmt.close();
            return (listaUsuarios);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar os usuários administradores. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static UsuarioAdministrador getUsuarioAdministrador(int id) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "select u.id idUsuario,"
                    + "u.nome,"
                    + "u.cpf,"
                    + "u.dataNascimento,"
                    + "u.status,"
                    + "ua.id,"
                    + "ua.usuario,"
                    + "ua.senha,"
                    + "t.telefone,"
                    + "t.id idtelefone,"
                    + "t.status statusTelefone "
                    + "from Usuario u "
                    + "inner join usuarioadministrador ua on ua.idUsuario = u.id "
                    + "inner join telefone t on t.idUsuario = u.id "
                    + "WHERE ua.id=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            UsuarioAdministrador ua = null;

            while (rs.next()) {
                if (ua == null) {
                    ua = new UsuarioAdministrador();

                    ua.setNome(rs.getString("nome"));
                    ua.setCpf(rs.getString("cpf"));
                    ua.setDataNascimento(rs.getDate("dataNascimento"));
                    ua.setStatus(rs.getString("status"));
                    ua.setUsuario(rs.getString("usuario"));
                    ua.setSenha(rs.getString("senha"));
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
            throw new BancoDeDadosException("Erro ao retornar o usuário administrador. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static int getUltimoIdUsuarioAdministrador() throws BancoDeDadosException {
        try {
            String query = "SELECT AUTO_INCREMENT maxID FROM information_schema.TABLES WHERE TABLE_NAME='usuarioAdministrador' AND TABLE_SCHEMA='softalert'";

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

    public static int getIdUsuarioDoUsuarioAdministrador(int id) throws BancoDeDadosException {
        try {
            String query = "SELECT idUsuario FROM UsuarioAdministrador WHERE id=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            rs.first();

            return rs.getInt("idUsuario");
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao retornar o último registro de usuário. Erro: " + e.getMessage());
        }
    }

    public static Boolean nomeUsuarioExiste(String nomeUsuario, int id) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "SELECT COUNT(*) quantidade FROM UsuarioAdministrador WHERE usuario=? AND id<>?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setString(1, nomeUsuario);
            stmt.setInt(2, id);

            ResultSet rs = stmt.executeQuery();

            if (!rs.first()) {
                return false;
            }

            if (rs.getInt("quantidade") != 0) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao pesquisar os nomes dos usuários administradores. Erro: " + e.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static Boolean autenticarUsuario(UsuarioAdministrador ua) throws BancoDeDadosException, GenericException {
        Boolean estahConectado = false;

        try {
            if (ConexaoBD.conectado()) {
                estahConectado = true;
            } else {
                ConexaoBD.AbrirConexao();
            }

            String query = "SELECT id FROM UsuarioAdministrador  WHERE usuario=? AND SENHA=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setString(1, ua.getUsuario());
            stmt.setString(2, ua.getSenha());

            ResultSet rs = stmt.executeQuery();

            if (rs.first()) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao validar autenticação. Erro: " + e.getMessage());
        } finally {
            if (!estahConectado) {
                ConexaoBD.FecharConexao();
            }
        }
    }
}
