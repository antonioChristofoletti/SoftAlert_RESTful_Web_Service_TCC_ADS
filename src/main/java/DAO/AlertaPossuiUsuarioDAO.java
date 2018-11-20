package DAO;

import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Alerta;
import Model.AlertaPossuiUsuario;
import Model.LocalizacaoAlerta;
import Model.Usuario;
import Model.UsuarioAdministrador;
import Model.UsuarioCliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class AlertaPossuiUsuarioDAO {

    public static void gerarVinculoUsuarioComAlerta(int idAlerta, ArrayList<LocalizacaoAlerta> listaLocalizacoesAlerta) throws BancoDeDadosException {
        try {

            ArrayList<String> listaIdsUsuarios = new ArrayList<>();

            for (LocalizacaoAlerta localizacaoAlerta : listaLocalizacoesAlerta) {

                String queryUsuario = "";
                if (localizacaoAlerta.getTipo().equals("P")) {
                    queryUsuario = "SELECT idUsuario FROM endereco WHERE pais=? GROUP BY id";
                }

                if (localizacaoAlerta.getTipo().equals("E")) {
                    queryUsuario = "SELECT idUsuario FROM endereco WHERE estado=? GROUP BY id";
                }

                if (localizacaoAlerta.getTipo().equals("C")) {
                    queryUsuario = "SELECT idUsuario FROM endereco WHERE cidade=? GROUP BY id";
                }

                PreparedStatement pstUsuario = ConexaoBD.getConnection().prepareStatement(queryUsuario);
                pstUsuario.setString(1, localizacaoAlerta.getDescricao());

                ResultSet rs = pstUsuario.executeQuery();

                while (rs.next()) {

                    if (listaIdsUsuarios.contains(rs.getString("idUsuario"))) {
                        continue;
                    }

                    String queryAlertaPossuiUsuario = "INSERT INTO softalert.alerta_possui_usuarios (situacaoUsuario,comentario,dataVisualizou,idAlerta,idUsuario) VALUES (?,?,?,?,?)";

                    PreparedStatement pstAlertaPossuiUsuarios = ConexaoBD.getConnection().prepareStatement(queryAlertaPossuiUsuario);
                    pstAlertaPossuiUsuarios.setString(1, null);
                    pstAlertaPossuiUsuarios.setString(2, null);
                    pstAlertaPossuiUsuarios.setTimestamp(3, null);
                    pstAlertaPossuiUsuarios.setInt(4, idAlerta);
                    pstAlertaPossuiUsuarios.setInt(5, rs.getInt("idUsuario"));

                    pstAlertaPossuiUsuarios.execute();

                    listaIdsUsuarios.add(rs.getString("idUsuario"));
                }
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir vinculo entre alerta e os usuários. Erro: " + ex.getMessage());
        }
    }

    public static ArrayList<AlertaPossuiUsuario> getAlertaPossuiUsuario(Alerta alerta, Usuario usuario, Boolean abrirConexao) throws BancoDeDadosException, GenericException {
        try {
            ArrayList<AlertaPossuiUsuario> listaAlertaPossuiUsuario = new ArrayList<>();

            if (abrirConexao) {
                ConexaoBD.AbrirConexao();
            }

            PreparedStatement stmt;

            if (usuario == null) {
                String query = "select * from alerta_possui_usuarios WHERE idAlerta=?";

                stmt = ConexaoBD.getConnection().prepareStatement(query);
                stmt.setInt(1, alerta.getId());
            } else {
                String query = "select * from alerta_possui_usuarios WHERE idAlerta=? AND idUsuario=?";

                stmt = ConexaoBD.getConnection().prepareStatement(query);
                stmt.setInt(1, alerta.getId());
                stmt.setInt(2, usuario.getIdUsuario());
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AlertaPossuiUsuario alertaPossuiUsuario = new AlertaPossuiUsuario();

                alertaPossuiUsuario.setId(rs.getInt("id"));
                alertaPossuiUsuario.setSituacaoUsuario(rs.getString("situacaoUsuario"));
                alertaPossuiUsuario.setComentario(rs.getString("comentario"));
                alertaPossuiUsuario.setIdAlerta(rs.getInt("idAlerta"));

                if (usuario instanceof UsuarioCliente) {
                    int idAux = ((UsuarioCliente) usuario).getId();
                    alertaPossuiUsuario.setIdUsuario(idAux);
                }

                if (usuario instanceof UsuarioAdministrador) {
                    int idAux = ((UsuarioAdministrador) usuario).getId();
                    alertaPossuiUsuario.setIdUsuario(idAux);
                }

                Timestamp tsDataInsercao = rs.getTimestamp("dataVisualizou");

                if (tsDataInsercao != null) {

                    Date dataVisualizou = new Date(tsDataInsercao.getTime());

                    alertaPossuiUsuario.setDataVisualizou(dataVisualizou);
                }

                listaAlertaPossuiUsuario.add(alertaPossuiUsuario);
            }

            if (abrirConexao) {
                rs.close();
                stmt.close();
            }

            return (listaAlertaPossuiUsuario);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar a relação alerta com os usuários. Erro: " + ex.getMessage());
        } finally {
            if (abrirConexao) {
                ConexaoBD.FecharConexao();
            }
        }
    }

}
