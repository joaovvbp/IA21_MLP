package Runner;

import MLP.MLP;
import Processamento.Holdout;
import Processamento.ProcessamentoDeArquivo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

            rede.saidas_da_rede.add(rede.converteSaida(rede.camadaSaida));

            int classe_obtida = rede.retornaRotulo(rede.converteSaida(rede.camadaSaida));

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

            rede.ajustaPesosCamadaSaida(classe_esperada, classe_obtida);//TODO: Verificar

            rede.ajustaPesosCamadaOculta(entrada);//TODO: Verificar
        }
        erro_geral = rede.calculaErroTotal(Holdout.conjTreinamento, rede);//TODO: Verificar

        return erro_geral;
    }

    //Metodo para a etapa de verificacao
    public static void verificaRede() {

    }

    //Metodo para a etapa de testes
    public static void testaRede() {

    }

    public static void runner(int neuronios_ocultos, double taxa_aprendizado, double momentum) {
        MLP rede = new MLP(neuronios_ocultos, taxa_aprendizado);
        rede.momentum = momentum;

        preparaDados("src/main/resources/optdigits.csv");

        double erro_da_epoca = 999999;
        int i = 0;
        do {
            erro_da_epoca = treinaRede(rede);
            System.out.println("Erro da epoca " + i + " = " + erro_da_epoca);

            rede.saidas_da_rede.clear();
            i++;
        } while (erro_da_epoca > 0.5);
    }

    public static void main(String[] args) throws IOException {
        //TODO: Elaborar um loop para conseguir testar diferentes configuracoes de rede de forma automatica, registrando os dados num arquivo CSV
        runner(35, 0.05, 1.0);
    }
}
