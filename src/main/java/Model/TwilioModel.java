package Model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TwilioModel {
    private String mensagem;
    private String numberTo;

    public TwilioModel() {
    }
    
    public TwilioModel(String mensagem, String numberTo) {
        this.mensagem = mensagem;
        this.numberTo = numberTo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNumberTo() {
        return numberTo;
    }

    public void setNumberTo(String numberTo) {
        this.numberTo = numberTo;
    }
}