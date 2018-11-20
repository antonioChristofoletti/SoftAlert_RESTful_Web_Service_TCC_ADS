package DAO;

import Controller.AlertaPossuiUsuarioController;
import Controller.AlertaRegiaoAfetadaController;
import Controller.LocalizacaoAlertaController;
import Controller.UsuarioClienteController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Alerta;
import Model.AlertaPossuiUsuario;
import Model.LocalizacaoAlerta;
import Model.NivelAlerta;
import Model.Telefone;
import Model.Usuario;
import Model.UsuarioAdministrador;
import Model.UsuarioCliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class AlertaDAO {

    public static Alerta inserir(Alerta alerta) throws BancoDeDadosException {
        try {
            ConexaoBD.AbrirConexao();

            ConexaoBD.setAutoCommit(false);

            String query = "INSERT INTO alerta (evento,dataInsercao, descricao,status,idNivelAlerta,idUsuarioAdministrador) VALUES(?,?,?,?,?,?)";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, alerta.getEvento());
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, alerta.getDescricao());
            pst.setString(4, alerta.getStatus().substring(0, 1));
            pst.setInt(5, alerta.getNivelAlerta().getId());
            pst.setInt(6, alerta.getAdministrador().getId());

            pst.execute();

            alerta.setId(getUltimoId_Alerta());

            inserirVinculoLocalizacaoEndereco(alerta);

            AlertaPossuiUsuarioController.gerarVinculoUsuarioComAlerta(alerta.getId(), alerta.getListaLocalizacoesAlerta());

            AlertaRegiaoAfetadaController.inserir(alerta.getId(), alerta.getListaAlertaRegiaoAfetada(), false);

            ConexaoBD.setCommit();

            return alerta;
        } catch (SQLException | GenericException ex) {
            ConexaoBD.setRollBack();
            ConexaoBD.setAutoCommit(true);
            throw new BancoDeDadosException("Erro ao inserir alerta. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static ArrayList<Alerta> retornaAlertas(Usuario usuario) throws BancoDeDadosException, GenericException {
        try {
            ArrayList<Alerta> listaAlerta = new ArrayList<>();

            if (usuario instanceof UsuarioCliente) {
                int idAux = ((UsuarioCliente) usuario).getId();
                UsuarioCliente uc = UsuarioClienteController.retornaUsuarioCliente(idAux);

                usuario.setIdUsuario(uc.getIdUsuario());
            }

            ConexaoBD.AbrirConexao();

            String queryVinculoAlerta_Usuario = "SELECT a.id idAlerta "
                    + "FROM alerta_possui_usuarios apu "
                    + "inner join alerta a on a.id = apu.idAlerta "
                    + "WHERE idUsuario=? "
                    + "order BY a.status ASC,a.idNivelAlerta DESC, a.dataInsercao DESC";

            PreparedStatement stmtVinculoAlerta_Usuario = ConexaoBD.getConnection().prepareStatement(queryVinculoAlerta_Usuario);
            stmtVinculoAlerta_Usuario.setInt(1, usuario.getIdUsuario());

            ResultSet rsVinculoAlerta_Usuario = stmtVinculoAlerta_Usuario.executeQuery();

            while (rsVinculoAlerta_Usuario.next()) {

                String queryAlerta = "select a.id idAlerta, "
                        + "a.dataInsercao dataInsercaoAlerta, "
                        + "a.evento eventoAlerta, "
                        + "a.descricao descricaoAlerta, "
                        + "a.status statusAlerta, "
                        + "na.id idNivelAlerta, "
                        + "na.descricao descricaoNivelAlerta, "
                        + "na.cor corNivelAlerta, "
                        + "u.nome nomeUsuariAdministrador "
                        + "from alerta a "
                        + "inner join nivelalerta na on na.id = a.idNivelAlerta "
                        + "inner join usuarioadministrador ua on ua.id = a.idUsuarioAdministrador "
                        + "inner join usuario u on u.id = ua.idUsuario "
                        + "inner join alerta_possui_usuarios apu on apu.idAlerta = a.id "
                        + "WHERE a.id=? "
                        + "GROUP BY a.id,a.dataInsercao,a.evento,a.descricao,a.status,na.id,na.descricao,na.cor,u.nome";

                PreparedStatement stmtAlerta = ConexaoBD.getConnection().prepareStatement(queryAlerta);
                stmtAlerta.setInt(1, rsVinculoAlerta_Usuario.getInt("idAlerta"));

                ResultSet rsAlerta = stmtAlerta.executeQuery();

                while (rsAlerta.next()) {
                    Alerta a = new Alerta();

                    a.setId(rsAlerta.getInt("idAlerta"));

                    Timestamp tsDataInsercao = rsAlerta.getTimestamp("dataInsercaoAlerta");

                    Date dataInsercao = new Date(tsDataInsercao.getTime());

                    a.setDataInsercao(dataInsercao);

                    a.setEvento(rsAlerta.getString("eventoAlerta"));

                    a.setDescricao(rsAlerta.getString("descricaoAlerta"));

                    String status = rsAlerta.getString("statusAlerta");

                    if (status.equals("A")) {
                        a.setStatus("Ativo");
                    }

                    if (status.equals("C")) {
                        a.setStatus("Cancelado");
                    }

                    if (status.equals("D")) {
                        a.setStatus("Desativado");
                    }

                    NivelAlerta nivelAlerta = new NivelAlerta();

                    nivelAlerta.setId(rsAlerta.getInt("idNivelAlerta"));

                    nivelAlerta.setDescricao(rsAlerta.getString("descricaoNivelAlerta"));

                    nivelAlerta.setCor(rsAlerta.getString("corNivelAlerta"));

                    UsuarioAdministrador usuarioAdministrador = new UsuarioAdministrador();
                    usuarioAdministrador.setNome(rsAlerta.getString("nomeUsuariAdministrador"));

                    a.setNivelAlerta(nivelAlerta);

                    a.setAdministrador(usuarioAdministrador);

                    a.setListaLocalizacoesAlerta(LocalizacaoAlertaController.getLocalizacoesAlerta(a, false));

                    a.setListaAlertaPossuiUsuarios(AlertaPossuiUsuarioController.getAlertaPossuiUsuario(a, usuario, false));

                    a.setListaAlertaRegiaoAfetada(AlertaRegiaoAfetadaController.retornaAlertaRegiaoAfetada(a, false));

                    listaAlerta.add(a);
                }

                rsAlerta.close();
                stmtAlerta.close();
            }

            rsVinculoAlerta_Usuario.close();
            stmtVinculoAlerta_Usuario.close();
            return (listaAlerta);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar os alertas do usuário. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }

    public static ArrayList<Telefone> retornaTelefonesAlerta(int idAlerta, boolean abrirConexao) throws BancoDeDadosException, GenericException {
        try {
            ArrayList<Telefone> listaTelefone = new ArrayList<>();

            if (abrirConexao) {
                ConexaoBD.AbrirConexao();
            }

            String query = "select t.* "
                    + "from alerta_possui_usuarios apu "
                    + "inner join usuario u on u.id = apu.idUsuario "
                    + "inner join telefone t on t.idUsuario = u.id "
                    + "where apu.idAlerta=?";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, idAlerta);
            
            ResultSet rs = stmt.executeQuery();

            int idUsuarioCicloAnterior = 0;
            UsuarioCliente usuarioClienteCicloAtual = null;

            while (rs.next()) {
                Telefone telefone = new Telefone();
                
                telefone.setId(rs.getInt("id"));
                telefone.setTelefone(rs.getString("telefone"));
                
                String status = rs.getString("status");
                if(status.equals("A")){
                    status = "Ativo";
                }else{
                    status = "Cancelado";
                }
                
                telefone.setStatus(status);
                
                listaTelefone.add(telefone);
            }

            rs.close();
            stmt.close();
            return (listaTelefone);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar os telefones dos usuários dos alertas. Erro: " + ex.getMessage());
        } finally {
            if (abrirConexao) {
                ConexaoBD.FecharConexao();
            }
        }
    }

    public static void inserirVinculoLocalizacaoEndereco(Alerta alerta) throws BancoDeDadosException {
        try {
            for (LocalizacaoAlerta localizacaoAlerta : alerta.getListaLocalizacoesAlerta()) {
                String query = "INSERT INTO alerta_possui_localizacaoAlerta (idLocalizacaoAlerta, idAlerta) VALUES(?,?)";

                PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
                pst.setInt(1, localizacaoAlerta.getId());
                pst.setInt(2, alerta.getId());
                pst.execute();
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao inserir vinculo entre a localização e o alerta. Erro: " + ex.getMessage());
        }
    }

    public static void removerVinculoLocalizacaoEndereco(Alerta alerta) throws BancoDeDadosException {
        try {
            String query = "DELETE FROM alerta_possui_localizacaoAlerta WHERE id=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setInt(1, alerta.getId());
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao remover vinculo entre a localização e o alerta. Erro: " + ex.getMessage());
        }
    }

    public static int getUltimoId_Alerta() throws BancoDeDadosException {
        try {
            String query = "SELECT AUTO_INCREMENT maxID FROM information_schema.TABLES WHERE TABLE_NAME='alerta' AND TABLE_SCHEMA='softalert'";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (!rs.first()) {
                return 1;
            }

            return rs.getInt("maxID") - 1;
        } catch (SQLException e) {
            throw new BancoDeDadosException("Erro ao retornar o último registro de alerta. Erro: " + e.getMessage());
        }
    }

    public static AlertaPossuiUsuario atualizarUsuarioPossuiAlerta(AlertaPossuiUsuario alertaPossuiUsuario) throws BancoDeDadosException {
        try {
            UsuarioCliente uc = UsuarioClienteController.retornaUsuarioCliente(alertaPossuiUsuario.getIdUsuario());
            alertaPossuiUsuario.setIdUsuario(uc.getIdUsuario());

            ConexaoBD.AbrirConexao();

            String query = "UPDATE alerta_possui_usuarios SET situacaoUsuario=?, comentario=?, dataVisualizou=? WHERE idAlerta=? AND idUsuario=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setString(1, alertaPossuiUsuario.getSituacaoUsuario());
            pst.setString(2, alertaPossuiUsuario.getComentario());

            pst.setTimestamp(3, new Timestamp(alertaPossuiUsuario.getDataVisualizou().getTime()));
            pst.setInt(4, alertaPossuiUsuario.getIdAlerta());
            pst.setInt(5, alertaPossuiUsuario.getIdUsuario());

            pst.execute();

            return alertaPossuiUsuario;
        } catch (SQLException | GenericException ex) {
            throw new BancoDeDadosException("Erro ao atualizar vinculo alerta com usuário. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }
}
