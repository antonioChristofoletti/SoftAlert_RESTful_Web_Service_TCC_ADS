package Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsuarioCliente extends Usuario {

    private int id;
    
    public UsuarioCliente() {
    }

    public UsuarioCliente(@JsonProperty(required = true) int id) {
        this.id = id;
    }

    @JsonProperty(required = true)
    public int getId() {
        return id;
    }

    @JsonProperty(required = true)
    public void setId(int id) {
        this.id = id;
    }
}