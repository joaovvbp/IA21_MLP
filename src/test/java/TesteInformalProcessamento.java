import Processamento.Holdout;
import Processamento.KFold;
import Processamento.ProcessaDados;

public class TesteInformalProcessamento {

    public static void main(String[] args) {
        ProcessaDados.processarDados("src/main/resources/optdigits.dat");
        Holdout.holdout();
        KFold.kFoldCrossValidation();
    }

}