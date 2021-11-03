package execucao;

import mlp.MLP;
import dados.Exemplo;
import dados.Holdout;
import dados.KFold;
import dados.ProcessaDados;

import java.io.IOException;
import java.util.List;

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
        return new double[]{somatorioUpper/rede.errosVerdadeirosFolds.size(), somatorioLower/rede.errosVerdadeirosFolds.size()};
    }

    public static int treinaRede(MLP rede, double acuracia, int abordagem) {
        int numeroEpocas = 0;

        if (abordagem == 1) {//Holdout
            do {
                rede.treinaRedeHoldout(rede);
                rede.erros_quadraticos_teste.add(new double[]{numeroEpocas, rede.erro_final_teste});
                rede.erros_quadraticos_valid.add(new double[]{numeroEpocas, rede.erro_final_valid});
                rede.erros_quadraticos_treino.add(new double[]{numeroEpocas, rede.erro_final_treino});
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
        preparaDados("src/main/resources/optdigits.dat");

        //Parametros da rede
        int neuroniosOcultos = 30;
        double taxaAprendizado = 0.05;
        double momentum = 0.5;
        double acuracia = 0.95;

        Arquivos arquivos = new Arquivos("src/main/resources/relatorio");
        arquivos.limpaArquivos();

        MLP redeH = new MLP(neuroniosOcultos, taxaAprendizado, momentum);
        int numeroEpocas = treinaRede(redeH, acuracia, 1);//Abordagem 1 = Holdout

        arquivos.registraSaidaHoldout(redeH, neuroniosOcultos, taxaAprendizado, momentum, acuracia, testaRede(redeH, Holdout.conjTeste), redeH.erros_quadraticos_teste, redeH.erros_quadraticos_valid, redeH.erros_quadraticos_treino, numeroEpocas);

        MLP redeK = new MLP(neuroniosOcultos, taxaAprendizado, momentum);
        treinaRede(redeK, acuracia, 2);//Abordagem 1 = Holdout

        arquivos.registraSaidaKFOLD(redeK, neuroniosOcultos, taxaAprendizado, momentum, acuracia);
    }
}
