package Execucao;

import MLP.MLP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Arquivos {

    String prefixo_local;

    public Arquivos(String prefixo_local){
        this.prefixo_local = prefixo_local;
    }

    public void registraRede(MLP rede) throws IOException {
        BufferedWriter writer_oculto = new BufferedWriter(new FileWriter(prefixo_local + "_pesos_oculto.csv", true));

        writer_oculto.append("NEURONIO_OCULTO, PESO\n");
        for (int i = 0; i < rede.camadaOculta.tamanhoCamada; i++) {
            for (int j = 0; j < rede.camadaOculta.neuronios[i].pesos.length; j++) {
                writer_oculto.append(String.valueOf(i)).append(", ");
                writer_oculto.append(String.valueOf(rede.camadaOculta.neuronios[i].pesos[j])).append("\n");
            }
        }
        writer_oculto.close();

        BufferedWriter writer_saida = new BufferedWriter(new FileWriter(prefixo_local + "_pesos_saida.csv", true));
        writer_saida.append("NEURONIO_SAIDA, PESO\n");
        for (int i = 0; i < rede.camadaSaida.tamanhoCamada; i++) {
            for (int j = 0; j < rede.camadaSaida.neuronios[i].pesos.length; j++) {
                writer_saida.append(String.valueOf(i)).append(", ");
                writer_saida.append(String.valueOf(rede.camadaSaida.neuronios[i].pesos[j])).append("\n");
            }
        }
        writer_saida.close();
    }

    public void registraErroQuadratico(ArrayList<double[]> erros_quadraticos) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(prefixo_local + "_epocas.csv", true));
        writer.append(" EPOCA, ERRO-QUADRATICO\n");
        for (double[] erros_quadratico : erros_quadraticos) {
            writer.append(" ").append(String.valueOf(erros_quadratico[0])).append(", ").append(String.valueOf(erros_quadratico[1]));
            writer.append("\n");
        }
        writer.close();
    }

    public void registraMatrizConfusao(int[][] matriz_confusao) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(prefixo_local + "_matriz.csv", true));
        //Escrita no arquivo (Matriz pronta em formato CSV)
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                System.out.print("0, ");
                writer.append("0, ");
                for (int j = 0; j < 10; j++) {
                    if (j == 9) {
                        System.out.print(j);
                        writer.append(String.valueOf(j));
                    } else {
                        System.out.print(j + ", ");
                        writer.append(String.valueOf(j)).append(", ");
                    }
                }
                System.out.println();
                writer.append("\n");
            }
            System.out.print(i + ", ");
            writer.append(String.valueOf(i)).append(", ");
            for (int j = 0; j < 10; j++) {
                if (j == 9) {
                    System.out.print(matriz_confusao[i][j]);
                    writer.append(String.valueOf(matriz_confusao[i][j]));
                } else {
                    System.out.print(matriz_confusao[i][j] + ", ");
                    writer.append(String.valueOf(matriz_confusao[i][j])).append(", ");
                }
            }
            System.out.println();
            writer.append("\n");
        }
        System.out.println();
        writer.append("\n");
        writer.close();
    }
}
