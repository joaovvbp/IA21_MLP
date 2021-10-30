package Dados;

import java.util.ArrayList;
import java.util.Collections;

public class KFold {

    public static ArrayList<ArrayList<Exemplo>> folds = new ArrayList<>();

    public static void kFoldCrossValidation(){
        embaralharDados();
        dividir();
    }

    public static void embaralharDados(){
        Collections.shuffle(ProcessaDados.entradas);
    }

    public static void dividir(){
        embaralharDados();
        for (int i = 0; i < 10; i++) {
            ArrayList<Exemplo> fold = new ArrayList<>();
            for (int j = 0; j < ProcessaDados.entradas.size()/10; j++) {
                fold.add(ProcessaDados.entradas.get(j));
            }
            folds.add(fold);
        }
    }
}
