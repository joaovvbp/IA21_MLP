package Runner;

import MLP.MLP;
import Processamento.Exemplo;
import Processamento.Holdout;
import Processamento.ProcessamentoDeArquivo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 1);
                } else {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 0);
                }
            }

            for (int k = 0; k < rede.camadaOculta.tamanhoCamada; k++) {
                if (k == classe_esperada) {
                    rede.calculaErroNeuronioOculto(rede.camadaOculta.neuronios[k], rede.camadaOculta.neuronios[k].saida);
                } else {
                    rede.calculaErroNeuronioOculto(rede.camadaOculta.neuronios[k], rede.camadaOculta.neuronios[k].saida);
                }
            }

            rede.ajustaPesosCamadaSaidaCM(classe_esperada, classe_obtida);

            rede.ajustaPesosCamadaOcultaCM(entrada);
        }
        erro_geral = rede.calculaErroTotal(Holdout.conjTreinamento, rede);

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

    public static void geraCSV() throws IOException {
        //TODO: Gerar um CSV com atributos da rede
        //Pode ser necessário usar o stringbuilder
        //Armazenar 10 entradas, uma por linha, com todos os dados necessários
        //Armazenar todos os pesos gerados aleatoriamente para ser inicializados como vetor: { 1, 2, 3, n-1, n};
        //Este ultimo passo depende da quantidade de neuronios na intermediaria, acho que vou fazer com 30
        //Não sei o que mais pode ser necessário manter estático
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/test_data.txt", true));

        //Preparando os dados para escrita
        preparaDados("src/main/resources/optdigits.csv");

        for (int i = 0; i < 10; i++) {//Guardando as entradas
            Double[] entrada = Holdout.conjTreinamento.get(i).vetorEntradas;
            writer.append("double[] entrada"+i+" = new double[]{");
            for (int j = 0; j < entrada.length; j++) {
                if (j == entrada.length - 1) {
                    writer.append(entrada[i].toString()+"};");
                }
                else{
                    writer.append(entrada[i].toString()+", ");
                }
            }
            writer.append("\n");
        }

        writer.append("\n");

        //Criando uma rede
        MLP rede = new MLP(30, 0.001){};

        writer.append("\nOculta: \n");

        for (int i = 0; i < rede.camadaOculta.neuronios.length; i++) {
            writer.append("rede.camadaOculta.neuronios["+i+"].pesos = new double[]{");
            for (int j = 0; j <  rede.camadaOculta.neuronios[i].pesos.length; j++) {
                if (j == rede.camadaOculta.neuronios[i].pesos.length - 1) {
                    writer.append(rede.camadaOculta.neuronios[i].pesos[j]+"};");
                }
                else{
                    writer.append(rede.camadaOculta.neuronios[i].pesos[j]+", ");
                }
            }
            writer.append("\n");
        }

        writer.append("\nSaida: \n");

        for (int i = 0; i < rede.camadaSaida.neuronios.length; i++) {
            writer.append("rede.camadaSaida.neuronios["+i+"].pesos = new double[]{");
            for (int j = 0; j <  rede.camadaSaida.neuronios[i].pesos.length; j++) {
                if (j == rede.camadaSaida.neuronios[i].pesos.length - 1) {
                    writer.append(rede.camadaSaida.neuronios[i].pesos[j]+"};");
                }
                else{
                    writer.append(rede.camadaSaida.neuronios[i].pesos[j]+", ");
                }
            }
            writer.append("\n");
        }

        writer.append("\ndouble taxa_de_aprendizado = "+rede.taxaDeAprendizado+";");

        writer.close();
    }

    public static void main(String[] args) throws IOException {
        //TODO: Elaborar um loop para conseguir testar diferentes configuracoes de rede de forma automatica, registrando os dados num arquivo CSV
//        runner(35, 0.05, 1.0);
        geraCSV();
    }
}
