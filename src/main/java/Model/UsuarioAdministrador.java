package Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.ArrayList;
import java.util.Date;

public class UsuarioAdministrador extends Usuario {

    private int id;
    
    private String usuario;
    
    @JsonProperty(access = Access.WRITE_ONLY)
    private String senha;

    public UsuarioAdministrador() {
    }

    public UsuarioAdministrador(int id, String usuario, String senha) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
    }

    public UsuarioAdministrador(int id, String usuario, String senha, int idUsuario, String nome, String status, Date dataNascimento, String cpf, ArrayList<Telefone> listaTelefones, ArrayList<Endereco> listaEndereco) {
        super(idUsuario, nome, status, dataNascimento, cpf, listaTelefones, listaEndereco);
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}