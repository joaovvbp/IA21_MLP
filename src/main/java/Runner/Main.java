package Runner;

import MLP.MLP;
import Processamento.Exemplo;
import Processamento.Holdout;
import Processamento.ProcessamentoDeArquivo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
        rede.erro_geral = 0.0;
        //Passa por todos as entradas do conjunto de treinamento
        for (int i = 0; i < Holdout.conjTreinamento.size(); i++) {//CORRIGIR
            int classe_esperada = Holdout.conjTreinamento.get(i).retornaRotulo();

            rede.forwardPropagation(Holdout.conjTreinamento.get(i).vetorEntradas, rede);

            for (int j = 0; j < rede.camadaSaida.tamanhoCamada; j++) {
                if (j == classe_esperada) {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 1);//TODO: Verificar
                } else {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 0);
                }
            }

            for (int k = 0; k < rede.camadaOculta.tamanhoCamada; k++) {
                rede.calculaErroNeuronioOculto(rede.camadaOculta.neuronios[k], rede.camadaOculta.neuronios[k].saida);
            }

            rede.ajustaPesosCamadaOculta(Holdout.conjTreinamento.get(i).vetorEntradas);//TODO: Verificar

            rede.ajustaPesosCamadaSaida();//TODO: Verificar

            rede.calculaErroTotal(Holdout.conjTreinamento.get(i));//TODO: Verificar
            rede.erros_exemplo = 0;
        }

        return (0.5) * (rede.erro_geral);
    }

    //Metodo para a etapa de verificacao
    public static void verificaRede() {

    }

    //Metodo para a etapa de testes
    public static void testaRede(MLP rede, String local, double erro_total) throws IOException {
        //Matriz de confusao
        BufferedWriter writer = new BufferedWriter(new FileWriter(local, true));

        int matriz_confusao[][] = new int[10][10];

        for (int i = 0; i < Holdout.conjTeste.size(); i++) {
            Double[] entrada = Holdout.conjTeste.get(i).vetorEntradas;

            rede.forwardPropagation(entrada, rede);
            int[] saida = rede.converteSaida(rede.camadaSaida);

            int esperado = Holdout.conjTeste.get(i).retornaRotulo();
            int obtido = rede.retornaRotulo(saida);

            matriz_confusao[esperado][obtido] += 1;
        }

//        for (int i = 0; i < 10; i++) { //Imprime ou armazena como um array
//            for (int j = 0; j < 10; j++) {
//                if (i == 0 && j == 0) {
//                    System.out.print("{{ " + matriz_confusao[i][j] + ", ");
//                    writer.append("{{ " + matriz_confusao[i][j] + ", ");
//                } else if (i != 0 && j == 0) {
//                    System.out.print("{ " + matriz_confusao[i][j] + ", ");
//                    writer.append("{ " + matriz_confusao[i][j] + ", ");
//                } else if (i==9 && j == 9) {
//                    System.out.print(matriz_confusao[i][j] + "}}\n");
//                    writer.append(+matriz_confusao[i][j] + "}}\n");
//                } else if (j == 9) {
//                    System.out.print(matriz_confusao[i][j] + "}\n");
//                    writer.append(+matriz_confusao[i][j] + "}\n");
//                } else {
//                    System.out.print(matriz_confusao[i][j] + ", ");
//                    writer.append(matriz_confusao[i][j] + ", ");
//                }
//            }
//        }

        for (int i = 0; i < 10; i++) { //Imprime ou armazena como csv
            if (i == 0) {
                System.out.print((int) erro_total + ", ");
                writer.append((int) erro_total + ", ");
                for (int j = 0; j < 10; j++) {
                    if (j == 9) {
                        System.out.print(j);
                        writer.append(j + "");
                    } else {
                        System.out.print(j + ", ");
                        writer.append(j + ", ");
                    }
                }
                System.out.println();
                writer.append("\n");
            }
            System.out.print(i + ", ");
            writer.append(i + ", ");
            for (int j = 0; j < 10; j++) {
                //System.out.print(j +", ");
                if (j == 9) {
                    System.out.print(matriz_confusao[i][j]);
                    writer.append(matriz_confusao[i][j] + "");
                } else {
                    System.out.print(matriz_confusao[i][j] + ", ");
                    writer.append(matriz_confusao[i][j] + ", ");
                }
            }
            System.out.println();
            writer.append("\n");
        }
        writer.append("\n");
        writer.close();
    }

    public static void runner(int neuronios_ocultos, double taxa_aprendizado, double momentum, int num_epocas, List<Exemplo> conjTreinamento) throws IOException {
        Random random = new Random();
        MLP rede = new MLP(neuronios_ocultos, taxa_aprendizado);
        rede.momentum = momentum;

        String local = "src/main/resources/test_data_ID" + random.nextInt(10000) + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(local, true));

        writer.append("Tamanho do conjunto de treinamento: ").append(String.valueOf(conjTreinamento.size())).append("\n");

        double erro_da_epoca = 0;
        int i = 0;
        do {
            erro_da_epoca = treinaRede(rede);

            System.out.println("E[" + i + "] = " + erro_da_epoca);
            writer.append("E[").append(String.valueOf(i)).append("] = ").append(String.valueOf(erro_da_epoca)).append(";\n");

            i++;
        } while (i < num_epocas);

        testaRede(rede, local, erro_da_epoca);
        System.out.println(neuronios_ocultos + ", " + taxa_aprendizado + ", " + momentum + ", " + num_epocas);
        writer.append(String.valueOf(neuronios_ocultos)).append(", ").append(String.valueOf(taxa_aprendizado)).append(", ").append(String.valueOf(momentum)).append(", ").append(String.valueOf(num_epocas)).append("\n\n");

        writer.close();
    }

    public static void main(String[] args) throws IOException {
        //TODO: Elaborar um loop para conseguir testar diferentes configuracoes de rede de forma automatica, registrando os dados num arquivo CSV
        //TODO: A rede nao converge, pode ser um problema no calculo e ajuste dos erros (independentes do erro geral)

        //TODO: Não encontrei divergencias entre as implementações dos métodos e as funções apresentadas pela professora, tudo está de acordo e parece funcionar corretamente
        //Conferi o cálculo de erro, ajuste dos pesos e a normalização e tudo parecia fazer sentido, vou tentar rodar a rede com essas configurações por mais épocas e ver se observo algo

        double[] taxas = new double[]{1.0E-1,1.0E-2,1.0E-3,1.0E-4};
        int[] neuronios = new int[]{10,15,20,25,30,35,40,45,50,55,60,65};
        double[] momentum = new double[]{0.1,0.2,0.4,0.6,0.8};

        preparaDados("src/main/resources/optdigits.csv");

        for (int i = 0; i < taxas.length; i++) {
            for (int j = 0; j < neuronios.length; j++) {
                for (int k = 0; k < momentum.length; k++) {
                    runner(neuronios[j], taxas[i], momentum[k], 300, Holdout.conjTreinamento);
                }
            }
        }

    }
}
