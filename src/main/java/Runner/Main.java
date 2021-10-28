package Runner;

import MLP.MLP;
import Processamento.Holdout;
import Processamento.ProcessamentoDeArquivo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
/*
 * TODO: Passos a serem implementados
 *
 * 1 - Preparar os conjuntos de treinamento, verificacao e teste
 * 2 - Rodar as epocas ate a rede convergir
 * 3 - Verificar
 * 4 - Testar
 *
 * Notas:
 * - Lembrar de desenvolver outra Metodo implementando o momentum
 * - Verificar o codigo procurando simplificar e conferir se todas as funcoes estao de acordo com o esperado
 */

public class Main {

    //Metodo para o processamento de dados e definicao de conjuntos
    public static void preparaDados(String local) {
        //Processa o conjunto de dados
        ProcessamentoDeArquivo.processarDados(local);
        Holdout.holdout();
    }

    //Metodo para a etapa de treinamento
    public static double treinaRede(MLP rede) {
        //Deve iterar em todas as entradas, calculando o erro, ajustando os pesos e passando para a prox entrada
        //Limitada pela funcao treshold

        double erro_geral = 0.0;
        //Passa por todos as entradas do conjunto de treinamento
        for (int i = 0; i < Holdout.conjTreinamento.size(); i++) {//CORRIGIR
            Double[] entrada = Holdout.conjTreinamento.get(i).vetorEntradas;
            int classe_esperada = Holdout.conjTreinamento.get(i).retornaRotulo();

            rede.forwardPropagation(entrada, rede);

            for (int j = 0; j < rede.camadaSaida.tamanhoCamada; j++) {
                if (j == classe_esperada) {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 1);//TODO: Verificar
                } else {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 0);
                }
            }

            for (int k = 0; k < rede.camadaOculta.tamanhoCamada; k++) {
                if (k == classe_esperada) {
                    rede.calculaErroNeuronioOculto(rede.camadaOculta.neuronios[k], rede.camadaOculta.neuronios[k].saida);//TODO: Verificar
                } else {
                    rede.calculaErroNeuronioOculto(rede.camadaOculta.neuronios[k], rede.camadaOculta.neuronios[k].saida);
                }
            }

            rede.ajustaPesosCamadaSaida();//TODO: Verificar

            rede.ajustaPesosCamadaOculta(entrada);//TODO: Verificar

        }
        erro_geral = rede.calculaErroTotal(Holdout.conjTreinamento);//TODO: Verificar

        return erro_geral;
    }

    //Metodo para a etapa de verificacao
    public static void verificaRede() {

    }

    //Metodo para a etapa de testes
    public static void testaRede(MLP rede) {
        for (int i = 0; i < Holdout.conjTeste.size(); i++) {
            Double[] entrada = Holdout.conjTeste.get(i).vetorEntradas;
            rede.forwardPropagation(entrada, rede);
            System.out.println("Esperado: "+Holdout.conjTeste.get(i).retornaRotulo());

            int[] saida = rede.converteSaida(rede.camadaSaida);
            int j = 0;
            for (; j < 10; j++) if(saida[j] == 1) break;
            System.out.println("Obtido: " +saida[j]);
        }
    }

    public static void runner(int neuronios_ocultos, double taxa_aprendizado, double momentum, int num_epocas) throws IOException {
        Random random = new Random();
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/test_data_ID"+random.nextInt(100)+".txt", true));
        MLP rede = new MLP(neuronios_ocultos, taxa_aprendizado);
        rede.momentum = momentum;

        preparaDados("src/main/resources/optdigits.csv");

        writer.append("Tamanho do conjunto de treinamento: ").append(String.valueOf(Holdout.conjTreinamento.size())).append("\n");
        writer.append("Configs da rede: ").append(String.valueOf(neuronios_ocultos)).append(", ").append(String.valueOf(taxa_aprendizado)).append(", ").append(String.valueOf(momentum)).append(", ").append(String.valueOf(num_epocas)).append("\n\n");

        double erro_da_epoca = -1;
        int i = 0;
        while (i <= num_epocas){
            erro_da_epoca = treinaRede(rede);

            if(i % 100 == 0){
                System.out.println("Epoca["+i+"] = "+erro_da_epoca);
                writer.append("epocas[").append(String.valueOf(i)).append("] = ").append(String.valueOf(erro_da_epoca)).append(";\n");
            }

            i++;
        }

        testaRede(rede);

        writer.close();
    }

    public static void main(String[] args) throws IOException {
        //TODO: Elaborar um loop para conseguir testar diferentes configuracoes de rede de forma automatica, registrando os dados num arquivo CSV
        //TODO: A rede nao converge, pode ser um problema no calculo e ajuste dos erros (independentes do erro geral)

        //TODO: Não encontrei divergencias entre as implementações dos métodos e as funções apresentadas pela professora, tudo está de acordo e parece funcionar corretamente
        //Conferi o cálculo de erro, ajuste dos pesos e a normalização e tudo parecia fazer sentido, vou tentar rodar a rede com essas configurações por mais épocas e ver se observo algo
        //Não consigo explicar os comportamentos que tenho observado (O mesmo erro por várias épocas, até de repente variar e voltar a repetir) (Apesar de que no geral se observa uma redução)
        //A primeira época ter um erro extremamente baixo, por algum motivo.
        runner(20, 0.0001, 0.001, 10000);
    }
}
