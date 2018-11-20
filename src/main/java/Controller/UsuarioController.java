package Controller;

import DAO.UsuarioDAO;
import ExceptionSistema.BancoDeDadosException;
import ExceptionSistema.CampoIncorretoException;
import ExceptionSistema.GenericException;
import Geral.Geral;
import Model.Telefone;
import Model.Usuario;
import java.util.Date;

public abstract class UsuarioController {

    protected static void validaCamposUsuario(Usuario u) throws CampoIncorretoException, GenericException, BancoDeDadosException {
        int i = 0;
        for (Telefone e : u.getListaTelefones()) {
            if (e.getTelefone().length() < 12) {
                if (u.getListaTelefones().size() == 1) {
                    throw new CampoIncorretoException("O Telefone do usuário é inválido");
                } else {
                    throw new CampoIncorretoException("O Telefone '" + (i + 1) + "' do usuário é inválido");
                }
            }
            i++;
        }

        if (u.getCpf() != null && !u.getCpf().equals("")) {

            if (!Geral.isCPF(u.getCpf())) {
                throw new CampoIncorretoException("O CPF do usuário é inválido");
            }

            if (UsuarioDAO.cpfExiste(u.getIdUsuario(), u.getCpf())) {
                throw new CampoIncorretoException("O CPF do usuário já existe no sistema");
            }
        }

        if (u.getDataNascimento().after(new Date())) {
            throw new CampoIncorretoException("A data de nascimento é inválida. A mesma é maior que a data atual.");
        }

        if (u.getDataNascimento().before(Geral.geraData("dd/MM/yyyy", "01/01/1890"))) {
            throw new CampoIncorretoException("A data de nascimento é inválida. A mesma é menor que a data '01/01/1890'");
        }
    }
}