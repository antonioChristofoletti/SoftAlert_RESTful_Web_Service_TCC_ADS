package Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class Usuario { 
    
    @JsonIgnore
    private int idUsuario;
    
    private String nome;
    
    private String status;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    private Date dataNascimento;

    private String cpf;
    
    private ArrayList<Telefone> listaTelefones;
    
    private ArrayList<Endereco> listaEnderecos;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nome, String status, Date dataNascimento, String cpf, ArrayList<Telefone> listaTelefones, ArrayList<Endereco> listaEndereco) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.status = status;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.listaTelefones = listaTelefones;
        this.listaEnderecos = listaEndereco;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public Date getDataNascimento() {
        return dataNascimento;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @JsonProperty(required = true)
    public String getCpf() {
        return cpf;
    }
    
    @JsonProperty(required = true)
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public ArrayList<Telefone> getListaTelefones() {
        return listaTelefones;
    }

    public void setListaTelefones(ArrayList<Telefone> listaTelefones) {
        this.listaTelefones = listaTelefones;
    }

    public ArrayList<Endereco> getListaEnderecos() {
        return listaEnderecos;
    }

    public void setListaEnderecos(ArrayList<Endereco> listaEnderecos) {
        this.listaEnderecos = listaEnderecos;
    }
}