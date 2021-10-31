package Dados;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Holdout {

    public static List<Exemplo> conjTreinamento = new ArrayList<>();
    public static List<Exemplo> conjValidacao = new ArrayList<>();
    public static List<Exemplo> conjTeste = new ArrayList<>();
    public static Random random = new Random();

    //Faz a divisão dos elementos entre os dois conjuntos
    public static void dividir() {
        conjTeste.clear();
        conjTreinamento.clear();
        conjValidacao.clear();
        int i;
        for (i=0; i< ProcessaDados.entradas.size(); i++) {
            int numero = random.nextInt(30);
            //Somei 3 ao size() na verificação para simular uma função teto
            if (numero < 10) {
                if (conjTeste.size() < (ProcessaDados.entradas.size()+3) / 3) {
                    conjTeste.add(ProcessaDados.entradas.get(i));
                } else i--;
            } else if (numero < 20) {
                if (conjValidacao.size() < (ProcessaDados.entradas.size()+3) / 3) {
                    conjValidacao.add(ProcessaDados.entradas.get(i));
                } else i--;
            } else {
                if (conjTreinamento.size() < (ProcessaDados.entradas.size()+3) / 3) {
                    conjTreinamento.add(ProcessaDados.entradas.get(i));
                } else i--;
            }
        }
    }
}
