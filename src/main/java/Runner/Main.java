package Runner;

import MLP.MLP;
import Processamento.Exemplo;
import Processamento.Holdout;
import Processamento.ProcessaDados;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {
    static Random random = new Random();//Usado na nomeacao dos arquivos

    //Metodo para o processamento de dados e divisão de conjuntos
    public static void preparaDados(String local) {
        //Processa o conjunto de dados
        ProcessaDados.processarDados(local);
        Holdout.holdout();
    }

    //Metodo para a etapa de verificacao
    public static void verificaRede() {

    }

    public static void testaRede() {

    }

    //Metodo para a etapa de testes
    public static void matrizConfusao(MLP rede, String local, List<Exemplo> conjunto) throws IOException {
        //Matriz de confusao (Pode ser executada em qualquer um dos conjuntos)
        BufferedWriter writer = new BufferedWriter(new FileWriter(local, true));

        int[][] matriz_confusao = new int[10][10];

        for (Exemplo exemplo : conjunto) {
            rede.forwardPropagation(exemplo.vetorEntradas, rede);
            int[] saida = rede.converteSaida(rede.camadaSaida);

            int esperado = exemplo.retornaRotulo();
            int obtido = rede.retornaRotulo(saida);

            matriz_confusao[esperado][obtido] += 1;
        }

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

    public static void treinamento(MLP rede, int num_epocas) throws IOException {
        double erro_da_epoca;
        int i = 0;
        do {
            erro_da_epoca = rede.treinaRede(rede);
            rede.erros_quadraticos.add(new double[]{i, erro_da_epoca});
            i++;
        } while (i < num_epocas);
    }

    public static void main(String[] args) throws IOException {
        //TODO: Gravar a rede em um arquivo após convergir
        //TODO: Implementar a etapa de testes e verificação
        String local = "src/main/resources/test_data_ID" + random.nextInt(10000) + ".txt";

        preparaDados("src/main/resources/optdigits.dat");

        int n_ocultos = 25;
        double t_aprendizado = 0.1;
        double momentum = 0.9;

        MLP rede = new MLP(n_ocultos, t_aprendizado, momentum);

        treinamento(rede, 300);

        matrizConfusao(rede, local, Holdout.conjTeste);
    }
}
