package Execucao;

import MLP.MLP;
import Dados.Exemplo;
import Dados.Holdout;
import Dados.KFold;
import Dados.ProcessaDados;

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

    public static String calculaErroVerdairo(MLP rede) {
        double e = calculaErro(Holdout.conjTeste, rede);
        double se = Math.sqrt(e * (1 - e) / Holdout.conjTeste.size());

        double lower = e - (1.96 * se);
        double upper = e + (1.96 * se);

        return (lower + " < e < " + upper);
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

    public static int treinaRede(MLP rede, double acuracia, int abordagem) {
        int n_epocas = 0;

        switch (abordagem){
            case(1)://Holdout
                do {
                    rede.treinaRedeHoldout(rede);

                    rede.erros_quadraticos_teste.add(new double[]{n_epocas, rede.erro_final_teste});
                    rede.erros_quadraticos_valid.add(new double[]{n_epocas, rede.erro_final_valid});
                    rede.erros_quadraticos_treino.add(new double[]{n_epocas, rede.erro_final_treino});

                    n_epocas++;
                } while (validaRede(rede, Holdout.conjValidacao) < acuracia);

                testaRede(rede, Holdout.conjTeste);
                return n_epocas;

            case(2)://K-fold
                //TODO: Desenvolver uma funçao para treino utilizando o K-FOLD, basta se basear na funcao de treino com holdout.
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (j != i){
                            KFold.folds.get(j);//Conjunto para treino
                        }
                    }
                    KFold.folds.get(i);//Conjunto para testes
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
        //TODO: Gravar a rede em um arquivo após convergir
        //TODO: Implementar a etapa de testes e verificação
        String prefixo_local = "src/main/resources/teste_fixo";

        preparaDados("src/main/resources/optdigits.dat");

        int n_ocultos = 30;
        double t_aprendizado = 0.05;
        double momentum = 0.9;
        double acuracia = 0.95;
        int abordagem = 1;

        Arquivos arquivos = new Arquivos(prefixo_local);
        MLP rede = new MLP(n_ocultos, t_aprendizado, momentum);

        int n_epocas = treinaRede(rede, acuracia, abordagem);

        //Registra os dados em arquivos CSV e um único arquivo de saída .txt
        //É possível alterar o endereço e manter os arquivos alterando o endereço prefixo_local utilizado no construtor da classe Arquivos
         arquivos.limpaArquivos();

         //TODO: Desenvolver uma funçao de registro de saida individual para o K-FOLD
        arquivos.registraSaidaKFOLD(rede, n_ocultos, t_aprendizado, momentum, acuracia, testaRede(rede, Holdout.conjTeste), rede.erros_quadraticos_teste, rede.erros_quadraticos_valid, rede.erros_quadraticos_treino, n_epocas - 1);
    }
}
