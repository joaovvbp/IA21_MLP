package Relatorios;

import java.io.*;
import java.util.ArrayList;

public class Relatorio {

    public static ArrayList<Double> exemplos = new ArrayList<>();

    Relatorio(ArrayList<Double> exemplos){
        this.exemplos = exemplos;
    }

    public static void gravaErrosQuadraticosArquivo(String conjunto, ArrayList<Double> exemplos) throws IOException {
        String path = "src/main/resources/relatorio.txt";
        String ex = exemplos.toString();
        File arq = new File(path);
        if (arq.exists()) {
            FileWriter fw = new FileWriter(arq, true);
            BufferedWriter bw = new BufferedWriter(fw);

            if(conjunto.equals("Teste")){
                bw.write("###############################################################\nErros quadraticos do conjunto de Teste: \n"+ex+"\n###############################################################");
            }
            else {
                bw.write("###############################################################\nErros quadraticos do conjunto de Validacao: \n"+ex+"\n###############################################################\n\n");
            }

            bw.close();
            fw.close();

        } else {
            arq.createNewFile();
            FileWriter fw = new FileWriter(arq);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("###############################################################\nErros quadraticos do conjunto de Treinamento: \n"+ex+"\n###############################################################\n\n");

            bw.close();
            fw.close();

        }
    }

    public static void addErroQuadraticoList(double erroq){
        exemplos.add(erroq);
    }

    public static void delListErroQuadratico(){
        exemplos.clear();
    }

}
