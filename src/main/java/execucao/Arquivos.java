package execucao;

import dados.Holdout;
import mlp.MLP;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Arquivos {

    String local;

    public Arquivos(String prefixoLocal) {
        this.local = prefixoLocal;
    }

    public void registraSaidaHoldout(MLP redek, MLP rede, int numeroNeuronios, double taxaAprendizado, double momentum, double acuracia, int[][] matrizConfusao, ArrayList<double[]> errosQuadraticosTeste, ArrayList<double[]> errosQuadraticosValid, ArrayList<double[]> errosQuadraticosTreino, int numeroEpocas) throws IOException {
        BufferedWriter writerSaida = new BufferedWriter(new FileWriter(local));

        writerSaida.append("########################################################### ABORDAGEM K-FOLD ###########################################################");
        writerSaida.append("\n\n");

        for (int i = 0; i < redek.errosVerdadeirosFolds.size(); i++) {
            double[] erroVerdadeiro = redek.errosVerdadeirosFolds.get(i);
            double erroMedio = redek.errosMedioFolds.get(i);
            writerSaida.append("ERRO VERDADEIRO FOLD ").append(String.valueOf(i + 1)).append("\n");
            writerSaida.append(String.valueOf(erroVerdadeiro[0])).append(" > e > ").append(String.valueOf(erroVerdadeiro[1]));
            writerSaida.append("\n");
            writerSaida.append("ERRO MEDIO FOLD ").append(String.valueOf(i + 1)).append("\n");
            writerSaida.append(String.valueOf(erroMedio));
            writerSaida.append("\n\n");
        }


        double[] erroGeral = Main.calculaMediaKFolds(redek);
        writerSaida.append("ERRO CROSS-VALIDATION\n");
        writerSaida.append(String.valueOf(erroGeral[0])).append(" > e > ").append(String.valueOf(erroGeral[1]));
        writerSaida.append("\n");

        writerSaida.append("\n");
        writerSaida.append("NUMERO DE NEURONIOS, TAXA DE APRENDIZADO, MOMENTUM, ACURACIA\n");
        writerSaida.append(String.valueOf(numeroNeuronios)).append(", ").append(String.valueOf(taxaAprendizado)).append(", ").append(String.valueOf(momentum)).append(", ").append(String.valueOf(acuracia));
        writerSaida.append("\n");

        writerSaida.append("\n");

        writerSaida.append("########################################################### ABORDAGEM HOLDOUT ###########################################################");
        writerSaida.append("\n\n");

        double[] erroVerdadeiroTeste = Main.calculaErroVerdairo(rede, Holdout.conjTeste);
        double[] erroVerdadeiroValid = Main.calculaErroVerdairo(rede, Holdout.conjValidacao);
        double[] erroVerdadeiroTreino = Main.calculaErroVerdairo(rede, Holdout.conjTreinamento);

        writerSaida.append("NUMERO DE EPOCAS\n");
        writerSaida.append(String.valueOf(numeroEpocas));
        writerSaida.append("\n\n");

        writerSaida.append("ERRO VERDADEIRO CONJUNTO TESTES\n");
        writerSaida.append(String.valueOf(erroVerdadeiroTeste[0])).append(" > e > ").append(String.valueOf(erroVerdadeiroTeste[1]));
        writerSaida.append("\n\n");

        writerSaida.append("ERRO VERDADEIRO CONJUNTO VALIDACAO\n");
        writerSaida.append(String.valueOf(erroVerdadeiroValid[0])).append(" > e > ").append(String.valueOf(erroVerdadeiroValid[1]));
        writerSaida.append("\n\n");

        writerSaida.append("ERRO VERDADEIRO CONJUNTO TREINO\n");
        writerSaida.append(String.valueOf(erroVerdadeiroTreino[0])).append(" > e > ").append(String.valueOf(erroVerdadeiroTreino[1]));
        writerSaida.append("\n\n");

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

        writerSaida.append("EPOCAS, ERRO QUADRATICO CONJ TREINO\n");
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
        writerSaida.close();
    }


    public void registraRede(MLP rede) throws IOException {
        BufferedWriter writerOculto = new BufferedWriter(new FileWriter(local + "_pesos_oculto.csv"));
        try (writerOculto) {
            writerOculto.append("NEURONIO OCULTO, PESO\n");
            for (int i = 0; i < rede.camadaOculta.tamanhoCamada; i++) {
                for (int j = 0; j < rede.camadaOculta.neuronios[i].pesos.length; j++) {
                    writerOculto.append(String.valueOf(i)).append(", ");
                    writerOculto.append(String.valueOf(rede.camadaOculta.neuronios[i].pesos[j])).append("\n");
                }
            }
        }
        BufferedWriter writerSaida = new BufferedWriter(new FileWriter(local + "_pesos_saida.csv", true));
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
}
