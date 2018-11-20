package Model;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Token {

    private String tokenAcesso;
 
    public Token(String tokenAcesso) {
        this.tokenAcesso = tokenAcesso;
    }

    public String getTokenAcesso() {
        return tokenAcesso;
    }

    public void setTokenAcesso(String tokenAcesso) {
        this.tokenAcesso = tokenAcesso;
    }
}
