import Relatorios.Relatorio;

import java.io.IOException;
import java.io.File;

public class TesteInformalRelatorio {

    public static void main(String[] args) throws IOException {
        File arq = new File("src/main/resources/relatorio.txt");
        arq.delete();

        for (int i = 10; i < 15; i++) {
            Relatorio.addErroQuadraticoList(i);
        }
        Relatorio.gravaErrosQuadraticosArquivo("Treinamento", Relatorio.exemplos);
        Relatorio.delListErroQuadratico();

        for (int i = 20; i < 25; i++) {
            Relatorio.addErroQuadraticoList(i);
        }
        Relatorio.gravaErrosQuadraticosArquivo("Validacao", Relatorio.exemplos);
        Relatorio.delListErroQuadratico();

        for (int i = 50; i < 55; i++) {
            Relatorio.addErroQuadraticoList(i);
        }
        Relatorio.gravaErrosQuadraticosArquivo("Teste", Relatorio.exemplos);
        Relatorio.delListErroQuadratico();
    }
}
