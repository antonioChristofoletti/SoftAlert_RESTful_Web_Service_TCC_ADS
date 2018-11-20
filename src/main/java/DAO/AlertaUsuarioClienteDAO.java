package DAO;

import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.AlertaUsuarioCliente;
import Model.UsuarioCliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertaUsuarioClienteDAO {

    public static void inserir(UsuarioCliente usuarioCliente, AlertaUsuarioCliente alerta) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "INSERT INTO alertaUsuarioCliente (desastreAvistado, situacaoUsuario,descricao,latitude,longitude,status,idUsuarioCliente,veracidade,descricaoVeracidade, dataInsercao) VALUES(?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, alerta.getDesastreAvistado());
            pst.setString(2, alerta.getSituacaoUsuario());
            pst.setString(3, alerta.getDescricao());
            pst.setDouble(4, alerta.getLatitude());
            pst.setDouble(5, alerta.getLongitude());
            pst.setString(6, "A");
            pst.setInt(7, usuarioCliente.getId());
            pst.setString(8, alerta.getVeracidade());
            pst.setString(9, alerta.getDescricaoVeracidade());
            pst.setTimestamp(10, new Timestamp(new Date().getTime()));

            pst.execute();

        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir endereço. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static void editar(AlertaUsuarioCliente alerta) throws BancoDeDadosException, GenericException {
        try {
            ConexaoBD.AbrirConexao();

            String query = "UPDATE alertaUsuarioCliente SET desastreAvistado=?, situacaoUsuario=?,descricao=?,latitude=?,longitude=?,status=?,idUsuarioCliente=?,veracidade=?,descricaoVeracidade=?,dataInsercao=?,idUsuarioAdministrador=? WHERE id=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, alerta.getDesastreAvistado());
            pst.setString(2, alerta.getSituacaoUsuario());
            pst.setString(3, alerta.getDescricao());
            pst.setDouble(4, alerta.getLatitude());
            pst.setDouble(5, alerta.getLongitude());
            pst.setString(6, alerta.getStatus().substring(0,1));
            pst.setInt(7, alerta.getIdUsuarioCliente());
            pst.setString(8, alerta.getVeracidade());
            pst.setString(9, alerta.getDescricaoVeracidade());
            pst.setTimestamp(10, new Timestamp(alerta.getDataInsercao().getTime()));
            pst.setInt(11, alerta.getIdUsuarioAdministrador());
            pst.setInt(12, alerta.getId());
            
            pst.execute();

        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao editar alerta. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static ArrayList<AlertaUsuarioCliente> getAlertaUsuarioCliente(UsuarioCliente usuarioCliente) throws BancoDeDadosException, GenericException {
        try {
            ArrayList<AlertaUsuarioCliente> listaAlerta = new ArrayList<>();
            ConexaoBD.AbrirConexao();

            String query = "SELECT * FROM alertaUsuarioCliente WHERE idUsuarioCliente=? ORDER BY dataInsercao DESC";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, usuarioCliente.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AlertaUsuarioCliente alerta = new AlertaUsuarioCliente();

                alerta.setId(rs.getInt("id"));
                alerta.setDesastreAvistado(rs.getString("desastreAvistado"));
                alerta.setSituacaoUsuario(rs.getString("situacaoUsuario"));
                alerta.setDescricao(rs.getString("descricao"));
                alerta.setLatitude(rs.getDouble("latitude"));
                alerta.setLongitude(rs.getDouble("longitude"));
                alerta.setVeracidade(rs.getString("veracidade"));
                alerta.setDescricaoVeracidade(rs.getString("descricaoVeracidade"));
                alerta.setIdUsuarioAdministrador(rs.getInt("idUsuarioAdministrador"));
                alerta.setIdUsuarioCliente(rs.getInt("idUsuarioCliente"));
                
                Timestamp tsDataInsercao = rs.getTimestamp("dataInsercao");
                
                Date dataInsercao = new Date(tsDataInsercao.getTime());
                
                alerta.setDataInsercao(dataInsercao);
                
                String status = rs.getString("status");
                
                if(status.equals("A"))
                    status = "Ativo";
                else
                    status = "Cancelado";
                
                alerta.setStatus(status);

                listaAlerta.add(alerta);
            }

            rs.close();
            stmt.close();
            return (listaAlerta);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar os alertas. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }
}
