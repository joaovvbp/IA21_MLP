package Execucao;

import MLP.MLP;
import Dados.Exemplo;
import Dados.Holdout;
import Dados.KFold;
import Dados.ProcessaDados;

import java.io.IOException;
import java.util.Arrays;
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
        double somatorio_erro = -1;
        for (Exemplo exemplo : conjunto) {
            rede.forwardPropagation(exemplo.vetorEntradas);
            if (exemplo.retornaRotulo() != rede.retornaRotulo(rede.converteSaida(rede.camadaSaida))) {
                somatorio_erro += 1;
            }
        }
        return somatorio_erro / conjunto.size();
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
        int[][] matriz_confusao = new int[10][10];

        for (Exemplo exemplo : conjunto) {
            rede.forwardPropagation(exemplo.vetorEntradas);
            int[] saida = rede.converteSaida(rede.camadaSaida);

            int esperado = exemplo.retornaRotulo();
            int obtido = rede.retornaRotulo(saida);

            matriz_confusao[esperado][obtido] += 1;
        }

        return matriz_confusao;
    }

    public static double[] calculaMediaKFolds(MLP rede) {
        double somatorio_upper = 0;
        double somatorio_lower = 0;
        for (int i = 0; i < rede.erros_verdadeiros_folds.size(); i++) {
            double[] erro_verdadeiro = rede.erros_verdadeiros_folds.get(i);
            somatorio_upper += erro_verdadeiro[0];
            somatorio_lower += erro_verdadeiro[1];
        }
        return new double[]{somatorio_upper/rede.erros_verdadeiros_folds.size(), somatorio_lower/rede.erros_verdadeiros_folds.size()};
    }

    public static int treinaRede(MLP rede, double acuracia, int abordagem) throws IOException {
        int n_epocas = 0;
        String prefixo_local = "src/main/resources/teste_fixo";
        Arquivos arquivos = new Arquivos(prefixo_local);

        switch (abordagem) {
            case (1)://Holdout
                do {
                    rede.treinaRedeHoldout(rede);

                    rede.erros_quadraticos_teste.add(new double[]{n_epocas, rede.erro_final_teste});
                    rede.erros_quadraticos_valid.add(new double[]{n_epocas, rede.erro_final_valid});
                    rede.erros_quadraticos_treino.add(new double[]{n_epocas, rede.erro_final_treino});

                    n_epocas++;
                } while (validaRede(rede, Holdout.conjValidacao) < acuracia);

                testaRede(rede, Holdout.conjTeste);
                return n_epocas;

            case (2)://K-fold
                //TODO: Desenvolver uma funçao para treino utilizando o K-FOLD, basta se basear na funcao de treino com holdout.
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (j != i) {
                            rede.treinaRedeKFold(rede, KFold.folds.get(j));
                        }
                    }
                    rede.erros_verdadeiros_folds.add(calculaErroVerdairo(rede, KFold.folds.get(i)));
                }
                return 0;
        }
        return -1;//Se alcançou este trecho, algo deu errado
    }

    //Metodo para a etapa de verificacao
    public static double validaRede(MLP rede, List<Exemplo> conjunto) {
        return calculaAcuracia(calculaErro(conjunto, rede));
    }

    public static void main(String[] args) throws IOException {
        preparaDados("src/main/resources/optdigits.dat");

        //Parametros da rede
        int n_ocultos = 20;
        double t_aprendizado = 1.0;
        double momentum = 0.9;
        double acuracia = 0.95;

        Arquivos arquivos = new Arquivos("src/main/resources/relatorio");
        arquivos.limpaArquivos();

        MLP rede_h = new MLP(n_ocultos, t_aprendizado, momentum);
        int n_epocas = treinaRede(rede_h, acuracia, 1);//Abordagem 1 = Holdout

        arquivos.registraSaidaHoldout(rede_h, n_ocultos, t_aprendizado, momentum, acuracia, testaRede(rede_h, Holdout.conjTeste), rede_h.erros_quadraticos_teste, rede_h.erros_quadraticos_valid, rede_h.erros_quadraticos_treino, n_epocas);

        MLP rede_k = new MLP(n_ocultos, t_aprendizado, momentum);
        treinaRede(rede_k, acuracia, 2);//Abordagem 1 = Holdout

        arquivos.registraSaidaKFOLD(rede_k, n_ocultos, t_aprendizado, momentum, acuracia);
    }
}
