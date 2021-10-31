package Execucao;

import MLP.MLP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Arquivos {

    String prefixo_local;

    public Arquivos(String prefixo_local) {
        this.prefixo_local = prefixo_local;
    }

    public void limpaArquivos() throws IOException {
        PrintWriter writer_oculto = new PrintWriter(prefixo_local + "_pesos_oculto.csv");
        writer_oculto.print("");
        writer_oculto.close();

        PrintWriter writer_saida_t = new PrintWriter(prefixo_local + "_epocas_teste.csv");
        writer_saida_t.print("");
        writer_saida_t.close();

        PrintWriter writer_saida_v = new PrintWriter(prefixo_local + "_epocas_valid.csv");
        writer_saida_v.print("");
        writer_saida_v.close();

        PrintWriter writer_saida_tr = new PrintWriter(prefixo_local + "_epocas_treino.csv");
        writer_saida_tr.print("");
        writer_saida_tr.close();

        PrintWriter writer_epocas = new PrintWriter(prefixo_local + "_matriz.csv");
        writer_epocas.print("");
        writer_epocas.close();

        PrintWriter writer_matriz = new PrintWriter(prefixo_local + "_pesos_saida.csv");
        writer_matriz.print("");
        writer_matriz.close();
    }

    public void registraSaida(MLP rede, int[][] matriz_confusao, ArrayList<double[]> erros_quadraticos_teste, ArrayList<double[]> erros_quadraticos_valid, ArrayList<double[]> erros_quadraticos_treino, int n_epocas) throws IOException {
        BufferedWriter writer_saida = new BufferedWriter(new FileWriter(prefixo_local + "_saida.txt"));

        writer_saida.append("N_EPOCAS, ERRO_VERDADEIRO\n");
        writer_saida.append(String.valueOf(n_epocas)).append(", ").append(Main.calculaErroVerdairo(rede));
        writer_saida.append("\n");

        writer_saida.append("\n");

        writer_saida.append("EPOCAS, ERRO_QUADRATICO_TESTE\n");
        for (double[] erros_quadratico : erros_quadraticos_teste) {
            writer_saida.append(String.valueOf(erros_quadratico[0])).append(", ").append(String.valueOf(erros_quadratico[1]));
            writer_saida.append("\n");
        }

        writer_saida.append("\n");

        writer_saida.append("EPOCAS, ERRO_QUADRATICO_VALIDACAO\n");
        for (double[] erros_quadratico : erros_quadraticos_valid) {
            writer_saida.append(String.valueOf(erros_quadratico[0])).append(", ").append(String.valueOf(erros_quadratico[1]));
            writer_saida.append("\n");
        }

        writer_saida.append("\n");

        writer_saida.append("EPOCAS, ERRO_QUADRATICO_TREINO\n");
        for (double[] erros_quadratico : erros_quadraticos_treino) {
            writer_saida.append(String.valueOf(erros_quadratico[0])).append(", ").append(String.valueOf(erros_quadratico[1]));
            writer_saida.append("\n");
        }

        writer_saida.append("\n");

        writer_saida.append("MATRIZ_CONFUSAO\n");
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                writer_saida.append("0, ");
                for (int j = 0; j < 10; j++) {
                    if (j == 9) {
                        writer_saida.append(String.valueOf(j));
                    } else {
                        writer_saida.append(String.valueOf(j)).append(", ");
                    }
                }
                writer_saida.append("\n");
            }
            writer_saida.append(String.valueOf(i)).append(", ");
            for (int j = 0; j < 10; j++) {
                if (j == 9) {
                    writer_saida.append(String.valueOf(matriz_confusao[i][j]));
                } else {
                    writer_saida.append(String.valueOf(matriz_confusao[i][j])).append(", ");
                }
            }
            writer_saida.append("\n");
        }
        writer_saida.append("\n");
        writer_saida.close();

    }

    public void registraRede(MLP rede) throws IOException {
        BufferedWriter writer_oculto = new BufferedWriter(new FileWriter(prefixo_local + "_pesos_oculto.csv"));

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

    public void registraErroQuadratico(ArrayList<double[]> erros_quadraticos, int conjunto) throws IOException {
        String posfixo = null;
        switch (conjunto) {
            case (1):
                posfixo = "_epocas_teste.csv";
                break;
            case (2):
                posfixo = "_epocas_valid.csv";
                break;
            case (3):
                posfixo = "_epocas_treino.csv";
                break;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(prefixo_local + posfixo, true));
        writer.append(" EPOCAS, ERRO-QUADRATICO\n");
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
                writer.append("0, ");
                for (int j = 0; j < 10; j++) {
                    if (j == 9) {
                        writer.append(String.valueOf(j));
                    } else {
                        writer.append(String.valueOf(j)).append(", ");
                    }
                }
                writer.append("\n");
            }
            writer.append(String.valueOf(i)).append(", ");
            for (int j = 0; j < 10; j++) {
                if (j == 9) {
                    writer.append(String.valueOf(matriz_confusao[i][j]));
                } else {
                    writer.append(String.valueOf(matriz_confusao[i][j])).append(", ");
                }
            }
            writer.append("\n");
        }
        writer.append("\n");
        writer.close();
    }
}
