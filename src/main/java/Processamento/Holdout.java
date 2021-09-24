package Processamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Holdout {

    static List<Exemplo> conjTreinamento = new ArrayList<>();
    static List<Exemplo> conjValidacao = new ArrayList<>();
    static List<Exemplo> conjTeste = new ArrayList<>();
    static Random random = new Random();
    static double taxaTeste = 0.0; //Não mudar
    static double taxaTreinamento = 0.0; //Não mudar
    static double taxaValidacao = 0.0; //Não mudar

    Holdout(){ }

    public static void holdout() {
        dividir();
        exibirTaxaDiv();
    }

    //Faz a divisão dos elementos entre os dois conjuntos
    public static void dividir() {
        conjTeste.clear();
        conjTreinamento.clear();
        conjValidacao.clear();
        int i;
        for (i=0; i<ProcessamentoDeArquivo.entradas.size(); i++) {
            int numero = random.nextInt(30);
            //Somei 3 ao size() na verificação para simular uma função teto
            if (numero < 10) {
                if (conjTeste.size() < (ProcessamentoDeArquivo.entradas.size()+3) / 3) {
                    conjTeste.add(ProcessamentoDeArquivo.entradas.get(i));
                } else i--;
            } else if (numero < 20) {
                if (conjValidacao.size() < (ProcessamentoDeArquivo.entradas.size()+3) / 3) {
                    conjValidacao.add(ProcessamentoDeArquivo.entradas.get(i));
                } else i--;
            } else {
                if (conjTreinamento.size() < (ProcessamentoDeArquivo.entradas.size()+3) / 3) {
                    conjTreinamento.add(ProcessamentoDeArquivo.entradas.get(i));
                } else i--;
            }
        }
    }

    public static void calcularTaxas() {
        taxaTeste = conjTeste.size()/(double)(conjTreinamento.size()+conjTeste.size()+conjValidacao.size());
        taxaTreinamento = conjTreinamento.size()/(double)(conjTreinamento.size()+conjTeste.size()+conjValidacao.size());
        taxaValidacao = conjValidacao.size()/(double)(conjTreinamento.size()+conjTeste.size()+conjValidacao.size());
    }

    public static void exibirTaxaDiv() {
        calcularTaxas();
        System.out.println("% conjunto de treinamento: "+taxaTreinamento*100+" tamanho: "+conjTreinamento.size());
        System.out.println("% conjunto de validação: "+taxaValidacao*100+" tamanho: "+conjValidacao.size());
        System.out.println("% conjunto de teste: "+taxaTeste*100+" tamanho: "+conjTeste.size());
    }

}
