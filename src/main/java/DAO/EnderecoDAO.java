package DAO;

import Controller.LocalizacaoAlertaController;
import Controller.UsuarioClienteController;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import Model.Endereco;
import Model.Usuario;
import Model.UsuarioCliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EnderecoDAO {

    public static void inserir(Usuario usuario) throws BancoDeDadosException, GenericException {
        try {
            if(usuario instanceof UsuarioCliente)
            {
                int idAux = ((UsuarioCliente) usuario).getId();
                UsuarioCliente uc =UsuarioClienteController.retornaUsuarioCliente(idAux);
                
                usuario.setIdUsuario(uc.getIdUsuario());
            }
           
            ConexaoBD.setAutoCommit(false);
            ConexaoBD.AbrirConexao();
            
            remover(usuario);
            
            LocalizacaoAlertaController.inserir(usuario.getListaEnderecos());
            
            for (Endereco endereco : usuario.getListaEnderecos()) {

                String query = "INSERT INTO Endereco (pais,estado,cidade,endereco,latitude,longitude,nomeLocal,status,idUsuario) VALUES(?,?,?,?,?,?,?,?,?)";

                PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
                pst.setString(1, endereco.getPais());
                pst.setString(2, endereco.getEstado());
                pst.setString(3, endereco.getCidade());
                pst.setString(4, endereco.getEndereco());
                pst.setDouble(5, endereco.getLatitude());
                pst.setDouble(6, endereco.getLongitude());
                pst.setString(7, endereco.getNomeLocal());
                  
                String status = endereco.getStatus();
                
                if(status == null)
                    status = "A";
                
                pst.setString(8, status);
                
                pst.setInt(9, usuario.getIdUsuario());
                
                pst.execute();
            }
        } catch (SQLException ex) {
             ConexaoBD.setRollBack();
            ConexaoBD.setAutoCommit(true);
            throw new BancoDeDadosException("Erro ao inserir endereço. Erro: " + ex.getMessage());
        }finally{
            ConexaoBD.FecharConexao();
        }
    }

    public static void remover(Usuario usuario) throws BancoDeDadosException {
        try {
            String query = "DELETE FROM Endereco WHERE idUsuario=?";

            PreparedStatement pst = ConexaoBD.getConnection().prepareStatement(query);
            pst.setInt(1, usuario.getIdUsuario());
            pst.execute();
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao remover os endereços. Erro: " + ex.getMessage());
        }
    }

    public static ArrayList<Endereco> getTelefonesUsuario(Usuario usuario) throws BancoDeDadosException, GenericException {
        try {
            ArrayList<Endereco> listaEndereco = new ArrayList<>();
            ConexaoBD.AbrirConexao();

            String query = "SELECT * FROM Endereco WHERE idUsuario=? AND status='A'";

            PreparedStatement stmt = ConexaoBD.getConnection().prepareStatement(query);
            stmt.setInt(1, usuario.getIdUsuario());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Endereco endereco = new Endereco();
                
                endereco.setId(rs.getInt("id"));
                endereco.setPais(rs.getString("pais"));
                endereco.setEstado(rs.getString("estado"));
                endereco.setCidade(rs.getString("cidade"));
                endereco.setEndereco(rs.getString("endereco"));
                endereco.setLatitude(rs.getDouble("latitude"));
                endereco.setLongitude(rs.getDouble("longitude"));
                endereco.setNomeLocal(rs.getString("nomeLocal"));
                endereco.setStatus(rs.getString("status"));
                endereco.setUsuario(usuario);
               
                listaEndereco.add(endereco);
            }

            rs.close();
            stmt.close();
            return (listaEndereco);
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao retornar os endereços do usuário. Erro: " + ex.getMessage());
        } finally {
            ConexaoBD.FecharConexao();
        }
    }
}