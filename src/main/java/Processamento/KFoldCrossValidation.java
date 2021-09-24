package Processamento;

import java.util.ArrayList;
import java.util.Collections;

public class KFoldCrossValidation {

    static ArrayList<ArrayList<Exemplo>> folds = new ArrayList<>();

    public static void kFoldCrossValidation(){
        embaralharDados();
        dividir();
        exibirTamanhos();
    }

    public static void embaralharDados(){
        Collections.shuffle(ProcessamentoDeArquivo.entradas);
    }

    public static void dividir(){
        for (Exemplo vetor : ProcessamentoDeArquivo.entradas) {
            ArrayList<Exemplo> fold = new ArrayList<>();
            for (int i = 0; i < ProcessamentoDeArquivo.entradas.size()/10; i++)
                fold.add(ProcessamentoDeArquivo.entradas.get(i));
            fold.clear();
        }
    }

    public static void exibirTamanhos(){
        for (int i = 0; i < folds.size(); i++) {
            System.out.println("fold ("+i+"): "+folds.get(i).size());
        }

    }

}
