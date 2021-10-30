import Dados.Holdout;
import Dados.KFold;
import Dados.ProcessaDados;

public class TesteInformalProcessamento {

    public static void main(String[] args) {
        ProcessaDados.processarDados("src/main/resources/optdigits.dat");
        Holdout.holdout();
        KFold.kFoldCrossValidation();
    }

}