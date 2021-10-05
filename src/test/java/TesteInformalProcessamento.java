import Processamento.Holdout;
import Processamento.KFoldCrossValidation;
import Processamento.ProcessamentoDeArquivo;

public class TesteInformalProcessamento {

    public static void main(String[] args) {
        ProcessamentoDeArquivo.processarDados("src/main/resources/optdigits.csv");
        Holdout.holdout();
        KFoldCrossValidation.kFoldCrossValidation();
    }

}