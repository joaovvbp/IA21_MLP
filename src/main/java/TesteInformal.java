
public class TesteInformal {

    public static void main(String[] args) {
        ProcessamentoDeArquivo.processarDados("src/main/resources/optdigits.csv");
        Holdout.holdout();
        int indice = ProcessamentoDeArquivo.entradas.get(0).retornaRotulo();
        Double a = ProcessamentoDeArquivo.entradas.get(0).vetorEntradas[3];
    }

}
