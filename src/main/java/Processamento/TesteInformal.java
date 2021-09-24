package Processamento;

import Processamento.Holdout;
import Processamento.ProcessamentoDeArquivo;

public class TesteInformal {

    public static void main(String[] args) {
        ProcessamentoDeArquivo.processarDados("src/main/resources/optdigits.csv");
        Holdout.holdout();
        KFoldCrossValidation.kFoldCrossValidation();
    }

}