package Execucao;

import MLP.MLP;
import Dados.Exemplo;
import Dados.Holdout;
import Dados.KFold;
import Dados.ProcessaDados;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Main {
    static Random random = new Random();//Usado na nomeacao dos arquivos

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

    //Metodo para a etapa de testes
    public static int[][] testaRede(MLP rede, List<Exemplo> conjunto) throws IOException {
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

    public static void treinaRede(MLP rede, double acuracia, List<Exemplo> conjunto_treino, List<Exemplo> conjunto_validacao ) throws IOException {
        double erro_da_epoca;
        int i = 0;

        do {
            erro_da_epoca = rede.treinaRede(rede);
            rede.erros_quadraticos.add(new double[]{i, erro_da_epoca});
            i++;
        } while (validaRede(rede, conjunto_validacao) < acuracia);

        testaRede(rede, Holdout.conjTeste);
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

        int n_ocultos = 25;
        double t_aprendizado = 0.1;
        double momentum = 0.9;
        double acuracia = 0.95;

        Arquivos arquivos = new Arquivos(prefixo_local);
        MLP rede = new MLP(n_ocultos, t_aprendizado, momentum);

        treinaRede(rede, acuracia, Holdout.conjTreinamento, Holdout.conjValidacao);

        arquivos.limpaArquivos();
        arquivos.registraErroQuadratico(rede.erros_quadraticos);
        arquivos.registraMatrizConfusao(testaRede(rede, Holdout.conjTeste));
        arquivos.registraRede(rede);
    }
}
