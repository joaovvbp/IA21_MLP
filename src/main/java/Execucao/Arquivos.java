package Execucao;

import Dados.Exemplo;
import Dados.Holdout;
import MLP.MLP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Arquivos {

    String prefixo_local;

    public Arquivos(String prefixo_local) {
        this.prefixo_local = prefixo_local;
    }

    public void limpaArquivos() throws IOException {
        PrintWriter writer_kfold = new PrintWriter(prefixo_local + "_saida_kfold.txt");
        writer_kfold.print("");
        writer_kfold.close();

        PrintWriter writer_holdout= new PrintWriter(prefixo_local + "_saida_holdout.txt");
        writer_holdout.print("");
        writer_holdout.close();
    }

    public void registraSaidaKFOLD(MLP rede, int n_neuronios, double taxa_aprendizado, double momentum, double acuracia) throws IOException {
        BufferedWriter writer_saida = new BufferedWriter(new FileWriter(prefixo_local + "_saida_kfold.txt"));

        for (int i = 0; i < rede.erros_verdadeiros_folds.size(); i++) {
            double[] erro_verdadeiro = rede.erros_verdadeiros_folds.get(i);
            writer_saida.append("ERRO_VERDADEIRO FOLD(").append(String.valueOf(i)).append(")\n");
            writer_saida.append(String.valueOf(erro_verdadeiro[0])).append(" > e > ").append(String.valueOf(erro_verdadeiro[1]));
            writer_saida.append("\n\n");
        }

        double[] erro_geral = Main.calculaMediaKFolds(rede);
        writer_saida.append("ERRO_CROSS_VALIDATION\n");
        writer_saida.append(String.valueOf(erro_geral[0])).append(" > e > ").append(String.valueOf(erro_geral[1]));
        writer_saida.append("\n");

        writer_saida.append("\n");
        writer_saida.append("N_NEURONIOS, TAXA_APRENDIZADO, MOMENTUM, ACURACIA\n");
        writer_saida.append(String.valueOf(n_neuronios)).append(", ").append(String.valueOf(taxa_aprendizado)).append(", ").append(String.valueOf(momentum)).append(", ").append(String.valueOf(acuracia));
        writer_saida.append("\n");

        writer_saida.append("\n");
        writer_saida.close();
    }

    public void registraSaidaHoldout(MLP rede, int n_neuronios, double taxa_aprendizado, double momentum, double acuracia, int[][] matriz_confusao, ArrayList<double[]> erros_quadraticos_teste, ArrayList<double[]> erros_quadraticos_valid, ArrayList<double[]> erros_quadraticos_treino, int n_epocas) throws IOException {
        BufferedWriter writer_saida = new BufferedWriter(new FileWriter(prefixo_local + "_saida_holdout.txt"));

        double[] erro_verdadeiro = Main.calculaErroVerdairo(rede, Holdout.conjTeste);

        writer_saida.append("N_EPOCAS, ERRO_VERDADEIRO\n");
        writer_saida.append(String.valueOf(n_epocas)).append(", ").append(String.valueOf(erro_verdadeiro[0])).append("> e >").append(String.valueOf(erro_verdadeiro[1]));
        writer_saida.append("\n");

        writer_saida.append("\n");

        writer_saida.append("N_NEURONIOS, TAXA_APRENDIZADO, MOMENTUM, ACURACIA\n");
        writer_saida.append(String.valueOf(n_neuronios)).append(", ").append(String.valueOf(taxa_aprendizado)).append(", ").append(String.valueOf(momentum)).append(", ").append(String.valueOf(acuracia));
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
                writer_saida.append(String.valueOf(acuracia)).append(", ");
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

    public void registraMatrizConfusao(int neuronios, double taxaAprendizado, double momentum, int[][] matriz_confusao, double acuracia) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(prefixo_local + "_matriz.csv", true));

        writer.append("N_NEURONIOS, TAXA_APRENDIZADO, MOMENTUM, ACURACIA\n");
        writer.append(String.valueOf(neuronios)).append(", ").append(String.valueOf(taxaAprendizado)).append(", ").append(String.valueOf(momentum)).append(", ").append(String.valueOf(acuracia));
        writer.append("\n\n");

        //Escrita no arquivo (Matriz pronta em formato CSV)
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                writer.append(String.valueOf(acuracia)).append(", ");
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
