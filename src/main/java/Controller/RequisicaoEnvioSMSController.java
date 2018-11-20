package Controller;

import DAO.RequisicaoEnvioSMSDAO;
import ExceptionSistema.BancoDeDadosException;
import Model.RequisicaoEnvioSMS;

public abstract class RequisicaoEnvioSMSController {

    public static void inserir(RequisicaoEnvioSMS requisicaoEnvioSMS) throws BancoDeDadosException {
        RequisicaoEnvioSMSDAO.inserir(requisicaoEnvioSMS);
    }
}
