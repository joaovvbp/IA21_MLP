package execucao;

import mlp.MLP;
import dados.Exemplo;
import dados.Holdout;
import dados.KFold;
import dados.ProcessaDados;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

//Apenas o erro quadratico e calculado dentro da MLP
public class Main {
    //Metodo para o processamento de dados e divisão de conjuntos
    public static void preparaDados(String local) {
        //Processa o conjunto de dados
        ProcessaDados.processarDados(local);
        Holdout.dividir();
        KFold.dividir();
    }

    public static double calculaErro(List<Exemplo> conjunto, MLP rede) {
        double somatorioErro = -1;
        for (Exemplo exemplo : conjunto) {
            rede.forwardPropagation(exemplo.vetorEntradas);
            if (exemplo.retornaRotulo() != rede.retornaRotulo(rede.converteSaida(rede.camadaSaida))) {
                somatorioErro += 1;
            }
        }
        return somatorioErro / conjunto.size();
    }

    public static double calculaAcuracia(double erro) {//Complexa! leia de novo se nao entender
        return 1 - erro;
    }

    public static double[] calculaErroVerdairo(MLP rede, List<Exemplo> conjunto) {
        double e = calculaErro(conjunto, rede);
        double se = Math.sqrt(e * (1 - e) / Holdout.conjTeste.size());

        double lower = e - (1.96 * se);
        double upper = e + (1.96 * se);

        return new double[]{upper, lower};
    }

    //Metodo para a etapa de testes
    public static int[][] testaRede(MLP rede, List<Exemplo> conjunto) {
        //Matriz de confusao (Pode ser executada em qualquer um dos conjuntos)
        int[][] matrizConfusao = new int[10][10];

        for (Exemplo exemplo : conjunto) {
            rede.forwardPropagation(exemplo.vetorEntradas);
            int[] saida = rede.converteSaida(rede.camadaSaida);

            int esperado = exemplo.retornaRotulo();
            int obtido = rede.retornaRotulo(saida);

            matrizConfusao[esperado][obtido] += 1;
        }

        return matrizConfusao;
    }

    public static double[] calculaMediaKFolds(MLP rede) {
        double somatorioUpper = 0;
        double somatorioLower = 0;
        for (int i = 0; i < rede.errosVerdadeirosFolds.size(); i++) {
            double[] erroVerdadeiro = rede.errosVerdadeirosFolds.get(i);
            somatorioUpper += erroVerdadeiro[0];
            somatorioLower += erroVerdadeiro[1];
        }
        return new double[]{somatorioUpper / rede.errosVerdadeirosFolds.size(), somatorioLower / rede.errosVerdadeirosFolds.size()};
    }

    public static int treinaRede(MLP rede, double acuracia, int abordagem) {
        int numeroEpocas = 0;

        if (abordagem == 1) {//Holdout
            do {
                rede.treinaRedeHoldout(rede);
                rede.errosQuadraticosTeste.add(new double[]{numeroEpocas, rede.erroFinalTeste});
                rede.errosQuadraticosValid.add(new double[]{numeroEpocas, rede.erroFinalValid});
                rede.errosQuadraticosTreino.add(new double[]{numeroEpocas, rede.erroFinalTreino});
                numeroEpocas++;
            } while (validaRede(rede, Holdout.conjValidacao) < acuracia);

            testaRede(rede, Holdout.conjTeste);
            return numeroEpocas;

        } else { //K-fold
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (j != i) {
                        rede.treinaRedeKFold(rede, KFold.folds.get(j));
                    }
                }
                rede.errosMedioFolds.add(calculaErro(KFold.folds.get(i), rede));
                rede.errosVerdadeirosFolds.add(calculaErroVerdairo(rede, KFold.folds.get(i)));
            }
            return 0;
        }
    }

    //Metodo para a etapa de verificacao
    public static double validaRede(MLP rede, List<Exemplo> conjunto) {
        return calculaAcuracia(calculaErro(conjunto, rede));
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Deseja utilizar os arquivos de entrada e saida padraoes da nossa implementacao? (Y/N)");
        String in = sc.nextLine();

        String localIn;
        String localOut;

        if (in.equals("N") || in.equals("n")) {
            System.out.println("Insira o local do arquivo de entrada: ");
            localIn = sc.nextLine();
            System.out.println("Insira o local do arquivo de saida: ");
            localOut = sc.nextLine();

        } else if (in.equals("Y") || in.equals("y")) {
            localIn = "src/main/resources/optdigits.dat";
            localOut = "src/main/resources/mlp_saida";
            System.out.println("Utilizando o arquivo de entrada (src/main/resources/optdigits.dat) e o arquivo de saida (src/main/resources/mlp_saida)");
        } else {
            System.out.println("Por favor, insira apenas Y ou N, tente novamente!");
            return;
        }

        preparaDados(localIn);
        Arquivos arquivos = new Arquivos(localOut);
        //Parametros da rede
        int neuroniosOcultos = 40;
        double taxaAprendizado = 0.1;
        double momentum = 0.4;
        double acuracia = 0.95;

        MLP redeH = new MLP(neuroniosOcultos, taxaAprendizado, momentum);
        int numeroEpocas = treinaRede(redeH, acuracia, 1);//Abordagem 1 = Holdout

        MLP redeK = new MLP(neuroniosOcultos, taxaAprendizado, momentum);
        treinaRede(redeK, acuracia, 2);//Abordagem 1 = Holdout

        arquivos.registraSaidaHoldout(redeK, redeH, neuroniosOcultos, taxaAprendizado, momentum, acuracia, testaRede(redeH, Holdout.conjTeste), redeH.errosQuadraticosTeste, redeH.errosQuadraticosValid, redeH.errosQuadraticosTreino, numeroEpocas);
    }
}
