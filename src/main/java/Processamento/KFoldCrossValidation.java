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
        for (int i = 0; i < 10; i++) {
            ArrayList<Exemplo> fold = new ArrayList<>();
            for (int j = 0; j < ProcessamentoDeArquivo.entradas.size()/10; j++) {
                fold.add(ProcessamentoDeArquivo.entradas.get(j));
            }
            folds.add(fold);
        }
    }

    public static void exibirTamanhos(){
        for (int i = 0; i < folds.size(); i++) {
            System.out.println("fold ("+i+"): "+folds.get(i).size());
        }

    }

}
