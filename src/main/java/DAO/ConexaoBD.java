package DAO;

import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.GenericException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static Connection connection;

    private static String Driver = "com.mysql.cj.jdbc.Driver";

    private static String localizacaoBanco = "jdbc:mysql://localhost/SoftAlert?useTimezone=true&serverTimezone=UTC&autoReconnect=false&useSSL=false";

    private static String usuario = "root";

    private static String senha = "";

    public static void AbrirConexao() throws BancoDeDadosException, GenericException {
        try {
            if (connection == null) {
                Class.forName(Driver);
                ConexaoBD.connection = (Connection) DriverManager.getConnection(localizacaoBanco, usuario, senha);
            }

            if (ConexaoBD.connection.isClosed()) {
                Class.forName(Driver);
                ConexaoBD.connection = (Connection) DriverManager.getConnection(localizacaoBanco, usuario, senha);
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao abrir a conexão. Erro: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            throw new GenericException("Erro ao abrir a conexão. Erro: " + ex.getMessage());
        }
    }

    public static void setAutoCommit(Boolean valor) throws BancoDeDadosException {
        try {
             if (ConexaoBD.connection != null && !ConexaoBD.connection.isClosed()) {
                ConexaoBD.connection.setAutoCommit(valor);
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao realizar o autoCommit. Erro: " + ex.getMessage());
        }
    }

    public static void FecharConexao() throws BancoDeDadosException {
        try {
            if (ConexaoBD.connection != null && !ConexaoBD.connection.isClosed()) {
                ConexaoBD.connection.close();
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao tentar desconectar com o banco de dados. Erro: " + ex.getMessage());
        }
    }
    
    public static Boolean conectado() throws SQLException
    {
        if(connection == null || connection.isClosed())
            return false;
        
        return true;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        ConexaoBD.connection = connection;
    }

    public static Boolean setCommit() throws BancoDeDadosException {
        try {
             if (ConexaoBD.connection != null && !ConexaoBD.connection.isClosed()) {
                connection.commit();
                return true;
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao realizar o commit. Erro: " + ex.getMessage());
        }

        return false;
    }

    public static Boolean setRollBack() throws BancoDeDadosException {
        try {
            if (ConexaoBD.connection != null && !ConexaoBD.connection.isClosed()) {
                connection.rollback();
                return true;
            }
        } catch (SQLException ex) {
            throw new BancoDeDadosException("Erro ao realizar o rollback. Erro: " + ex.getMessage());
        }
        return false;
    }
}
