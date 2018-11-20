package Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AlertaUsuarioCliente {
    
    private int id;
    
    private String desastreAvistado;
    private String situacaoUsuario;
    private String descricao;
    private double latitude;
    private double longitude;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")
    private Date dataInsercao;
    
    @JsonIgnore
    private UsuarioCliente usuarioCliente;
    
    private int idUsuarioCliente;
    
    @JsonIgnore
    private UsuarioAdministrador usuarioAdministrador;
    
    private int idUsuarioAdministrador;
    
    private String veracidade;
    private String descricaoVeracidade;
    private String status;
    
    public AlertaUsuarioCliente(){
    }

    public AlertaUsuarioCliente(int id, String desastreAvistado, String situacaoUsuario, String descricao, double latitude, 
            double longitude, Date dataInsercao, UsuarioCliente usuarioCliente, int idUsuarioCliente, 
            UsuarioAdministrador usuarioAdministrador, int idUsuarioAdministrador, String veracidade, String descricaoVeracidade, 
            String status) {
        this.id = id;
        this.desastreAvistado = desastreAvistado;
        this.situacaoUsuario = situacaoUsuario;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataInsercao = dataInsercao;
        this.usuarioCliente = usuarioCliente;
        this.idUsuarioCliente = idUsuarioCliente;
        this.usuarioAdministrador = usuarioAdministrador;
        this.idUsuarioAdministrador = idUsuarioAdministrador;
        this.veracidade = veracidade;
        this.descricaoVeracidade = descricaoVeracidade;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesastreAvistado() {
        return desastreAvistado;
    }

    public void setDesastreAvistado(String desastreAvistado) {
        this.desastreAvistado = desastreAvistado;
    }

    public String getSituacaoUsuario() {
        return situacaoUsuario;
    }

    public void setSituacaoUsuario(String situacaoUsuario) {
        this.situacaoUsuario = situacaoUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDataInsercao() {
        return dataInsercao;
    }

    public void setDataInsercao(Date dataInsercao) {
        this.dataInsercao = dataInsercao;
    }

    public UsuarioCliente getUsuarioCliente() {
        return usuarioCliente;
    }

    public void setUsuarioCliente(UsuarioCliente usuarioCliente) {
        this.usuarioCliente = usuarioCliente;
    }

    public int getIdUsuarioCliente() {
        return idUsuarioCliente;
    }

    public void setIdUsuarioCliente(int idUsuarioCliente) {
        this.idUsuarioCliente = idUsuarioCliente;
    }

    public UsuarioAdministrador getUsuarioAdministrador() {
        return usuarioAdministrador;
    }

    public void setUsuarioAdministrador(UsuarioAdministrador usuarioAdministrador) {
        this.usuarioAdministrador = usuarioAdministrador;
    }

    public int getIdUsuarioAdministrador() {
        return idUsuarioAdministrador;
    }

    public void setIdUsuarioAdministrador(int udUsuarioAdministrador) {
        this.idUsuarioAdministrador = udUsuarioAdministrador;
    }

    public String getVeracidade() {
        return veracidade;
    }

    public void setVeracidade(String veracidade) {
        this.veracidade = veracidade;
    }

    public String getDescricaoVeracidade() {
        return descricaoVeracidade;
    }

    public void setDescricaoVeracidade(String descricaoVeracidade) {
        this.descricaoVeracidade = descricaoVeracidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}