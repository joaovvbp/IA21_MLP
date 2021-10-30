package Processamento;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessaDados {

    public ProcessaDados(){ } //Construtor padrão

    public static List<Exemplo> entradas = new ArrayList<>(); //Vetor com os exemplos de entrada

    public static void processarDados(String local) { //Função principal que deve ser chamada para ler dados e normalizar
        lerDados(local);
        normalizarDados();
    }

    static void lerDados (String local) { //Lê os dados do arquivo
        try (BufferedReader csvReader = new BufferedReader(new FileReader(local))){ //try-with-resources
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                Exemplo ex = new Exemplo();
                for (int i = 0; i < data.length; i++) {
                    if (i < 64){
                        ex.vetorEntradas[i] = Double.parseDouble(data[i]); //Converte String do arquivo para Double e coloca no vetor entrada de Processamento.Exemplo
                        if (ex.vetorEntradas[i] == 0) ex.vetorEntradas[i] = -1.0;
                    }
                    else ex.rotulo[(int) Double.parseDouble(data[i])] = 1; //Atribui o rótulo (que é o último valor)
                }
                entradas.add(ex);
            }
        } catch (IOException e) { System.out.println("Falha na leitura do arquivo em "+local); }
    }

    static void normalizarDados() { //Normaliza os dados com a divisão de cada elemento pela soma de todos elementos
        for (Exemplo ex: entradas) {
            int somaEntrada = 0;
            for (int i = 0; i < ex.vetorEntradas.length; i++) somaEntrada += ex.vetorEntradas[i]; //Calcula a soma de todos elementos
            for (int i = 0; i < ex.vetorEntradas.length; i++)
                if ((somaEntrada != 0))  //Se a soma dos elementos for igual a 0 os elementos já estão normalizados
                    ex.vetorEntradas[i] = ex.vetorEntradas[i] / somaEntrada;
        }
    }
}