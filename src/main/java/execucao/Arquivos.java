package execucao;

import dados.Holdout;
import mlp.MLP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Arquivos {

    String prefixoLocal;

    public Arquivos(String prefixoLocal) {
        this.prefixoLocal = prefixoLocal;
    }

    public void limpaArquivos() throws IOException {
        PrintWriter writerKfold = new PrintWriter(prefixoLocal + "_saida_kfold.txt");
        writerKfold.print("");
        writerKfold.close();

        PrintWriter writerHoldout= new PrintWriter(prefixoLocal + "_saida_holdout.txt");
        writerHoldout.print("");
        writerHoldout.close();
    }

    public void registraSaidaKFOLD(MLP rede, int numeroNeuronios, double taxaAprendizado, double momentum, double acuracia) throws IOException {
        BufferedWriter writerSaida = new BufferedWriter(new FileWriter(prefixoLocal + "_saida_kfold.txt"));
        try (writerSaida) {
            for (int i = 0; i < rede.errosVerdadeirosFolds.size(); i++) {
                double[] erroVerdadeiro = rede.errosVerdadeirosFolds.get(i);
                writerSaida.append("ERRO VERDADEIRO FOLD ").append(String.valueOf(i + 1)).append("\n");
                writerSaida.append(String.valueOf(erroVerdadeiro[0])).append(" > e > ").append(String.valueOf(erroVerdadeiro[1]));
                writerSaida.append("\n\n");
            }

            double[] erroGeral = Main.calculaMediaKFolds(rede);
            writerSaida.append("ERRO CROSS-VALIDATION\n");
            writerSaida.append(String.valueOf(erroGeral[0])).append(" > e > ").append(String.valueOf(erroGeral[1]));
            writerSaida.append("\n");

            writerSaida.append("\n");
            writerSaida.append("NUMERO DE NEURONIOS, TAXA DE APRENDIZADO, MOMENTUM, ACURACIA\n");
            writerSaida.append(String.valueOf(numeroNeuronios)).append(", ").append(String.valueOf(taxaAprendizado)).append(", ").append(String.valueOf(momentum)).append(", ").append(String.valueOf(acuracia));
            writerSaida.append("\n");

            writerSaida.append("\n");
        }
    }

    public void registraSaidaHoldout(MLP rede, int numeroNeuronios, double taxaAprendizado, double momentum, double acuracia, int[][] matrizConfusao, ArrayList<double[]> errosQuadraticosTeste, ArrayList<double[]> errosQuadraticosValid, ArrayList<double[]> errosQuadraticosTreino, int numeroEpocas) throws IOException {
        BufferedWriter writerSaida = new BufferedWriter(new FileWriter(prefixoLocal + "_saida_holdout.txt"));
        try(writerSaida) {
            double[] erroVerdadeiro = Main.calculaErroVerdairo(rede, Holdout.conjTeste);

            writerSaida.append("NUMERO DE EPOCAS, ERRO VERDADEIRO\n");
            writerSaida.append(String.valueOf(numeroEpocas)).append(", ").append(String.valueOf(erroVerdadeiro[0])).append("> e >").append(String.valueOf(erroVerdadeiro[1]));
            writerSaida.append("\n");

            writerSaida.append("\n");

            writerSaida.append("NUMERO DE NEURONIOS, TAXA DE APRENDIZADO, MOMENTUM, ACURACIA\n");
            writerSaida.append(String.valueOf(numeroNeuronios)).append(", ").append(String.valueOf(taxaAprendizado)).append(", ").append(String.valueOf(momentum)).append(", ").append(String.valueOf(acuracia));
            writerSaida.append("\n");

            writerSaida.append("\n");

            writerSaida.append("EPOCAS, ERRO QUADRATICO CONJ TESTE\n");
            for (double[] errosQuadratico : errosQuadraticosTeste) {
                writerSaida.append(String.valueOf(errosQuadratico[0])).append(", ").append(String.valueOf(errosQuadratico[1]));
                writerSaida.append("\n");
            }

            writerSaida.append("\n");

            writerSaida.append("EPOCAS, ERRO QUADRATICO CONJ VALIDACAO\n");
            for (double[] errosQuadratico : errosQuadraticosValid) {
                writerSaida.append(String.valueOf(errosQuadratico[0])).append(", ").append(String.valueOf(errosQuadratico[1]));
                writerSaida.append("\n");
            }

            writerSaida.append("\n");

            writerSaida.append("EPOCAS, ERRO QUADRATICO TREINO\n");
            for (double[] errosQuadratico : errosQuadraticosTreino) {
                writerSaida.append(String.valueOf(errosQuadratico[0])).append(", ").append(String.valueOf(errosQuadratico[1]));
                writerSaida.append("\n");
            }

            writerSaida.append("\n");

            writerSaida.append("MATRIZ DE CONFUSAO\n");
            for (int i = 0; i < 10; i++) {
                if (i == 0) {
                    writerSaida.append(String.valueOf(acuracia)).append(", ");
                    for (int j = 0; j < 10; j++) {
                        if (j == 9) {
                            writerSaida.append(String.valueOf(j));
                        } else {
                            writerSaida.append(String.valueOf(j)).append(", ");
                        }
                    }
                    writerSaida.append("\n");
                }
                writerSaida.append(String.valueOf(i)).append(", ");
                for (int j = 0; j < 10; j++) {
                    if (j == 9) {
                        writerSaida.append(String.valueOf(matrizConfusao[i][j]));
                    } else {
                        writerSaida.append(String.valueOf(matrizConfusao[i][j])).append(", ");
                    }
                }
                writerSaida.append("\n");
            }
            writerSaida.append("\n");
        }
    }


    public void registraRede(MLP rede) throws IOException {
        BufferedWriter writerOculto = new BufferedWriter(new FileWriter(prefixoLocal + "_pesos_oculto.csv"));
        try (writerOculto) {
            writerOculto.append("NEURONIO OCULTO, PESO\n");
            for (int i = 0; i < rede.camadaOculta.tamanhoCamada; i++) {
                for (int j = 0; j < rede.camadaOculta.neuronios[i].pesos.length; j++) {
                    writerOculto.append(String.valueOf(i)).append(", ");
                    writerOculto.append(String.valueOf(rede.camadaOculta.neuronios[i].pesos[j])).append("\n");
                }
            }
        }
        BufferedWriter writerSaida = new BufferedWriter(new FileWriter(prefixoLocal + "_pesos_saida.csv", true));
        try (writerSaida) {
            writerSaida.append("NEURONIO SAIDA, PESO\n");
            for (int i = 0; i < rede.camadaSaida.tamanhoCamada; i++) {
                for (int j = 0; j < rede.camadaSaida.neuronios[i].pesos.length; j++) {
                    writerSaida.append(String.valueOf(i)).append(", ");
                    writerSaida.append(String.valueOf(rede.camadaSaida.neuronios[i].pesos[j])).append("\n");
                }
            }
        }
    }

    public void registraErroQuadratico(ArrayList<double[]> errosQuadraticos, int conjunto) throws IOException {
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
        BufferedWriter writer = new BufferedWriter(new FileWriter(prefixoLocal + posfixo, true));
        try (writer) {
            writer.append(" EPOCAS, ERRO-QUADRATICO\n");
            for (double[] errosQuadratico : errosQuadraticos) {
                writer.append(" ").append(String.valueOf(errosQuadratico[0])).append(", ").append(String.valueOf(errosQuadratico[1]));
                writer.append("\n");
            }
        }
    }

    public void registraMatrizConfusao(int neuronios, double taxaAprendizado, double momentum, int[][] matrizConfusao, double acuracia) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(prefixoLocal + "_matriz.csv", true));
        try (writer) {
            writer.append("NUMERO DE NEURONIOS, TAXA DE APRENDIZADO, MOMENTUM, ACURACIA\n");
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
                        writer.append(String.valueOf(matrizConfusao[i][j]));
                    } else {
                        writer.append(String.valueOf(matrizConfusao[i][j])).append(", ");
                    }
                }
                writer.append("\n");
            }
            writer.append("\n");
        }
    }
}
