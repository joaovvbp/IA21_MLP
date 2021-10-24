package Runner;

import MLP.MLP;
import Processamento.Exemplo;
import Processamento.Holdout;
import Processamento.ProcessamentoDeArquivo;

import java.util.ArrayList;
import java.util.List;

/*
* todo
* Passos a serem implementados
*
* separar isso em funcoes?
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

public class Main { // Refatorar, ou comecar do zero sem este esqueleto

    //Metodo para o processamento de dados e definicao de conjuntos
    public static void preparaDados(String local){
        //Processa o conjunto de dados
        ProcessamentoDeArquivo.processarDados(local);
        Holdout.holdout();
    }

    //Metodo para a etapa de treinamento
    public static double treinaRede(MLP rede){
        //Deve iterar em todas as entradas, calculando o erro, ajustando os pesos e passando para a prox entrada
        //Limitada pela funcao treshold

        double erro_geral = 0.0;
        //Passa por todos as entradas do conjunto de treinamento
        for (int i = 0; i < Holdout.conjTreinamento.size()/10; i++) {//CORRIGIR
            Double[] entrada = Holdout.conjTreinamento.get(i).vetorEntradas;
            int classe_esperada = Holdout.conjTreinamento.get(i).retornaRotulo();

            rede.forwardPropagation(entrada, rede);

            rede.saidas_da_rede.add(rede.converteSaida(rede.camadaSaida));

            int classe_obtida = rede.retornaRotulo(rede.converteSaida(rede.camadaSaida));

            for (int j = 0; j < rede.camadaSaida.tamanhoCamada; j++) {
                if (j == classe_esperada) {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 1);
                    System.out.println("Erro do neuronio de saida (" + j + ")[Esperado] = " + rede.camadaSaida.neuronios[j].ultimo_erro);
                } else {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 0);
                    System.out.println("Erro do neuronio de saida (" + j + ") = " + rede.camadaSaida.neuronios[j].ultimo_erro);
                }
            }

            for (int k = 0; k < rede.camadaOculta.tamanhoCamada; k++) {
                if (k == classe_esperada) {
                    rede.calculaErroNeuronioOculto(rede.camadaOculta.neuronios[k], rede.camadaOculta.neuronios[k].saida);
                    System.out.println("Erro do neuronio oculto (" + k + ")[Esperado] = " + rede.camadaOculta.neuronios[k].ultimo_erro);
                } else {
                    rede.calculaErroNeuronioOculto(rede.camadaOculta.neuronios[k], rede.camadaOculta.neuronios[k].saida);
                    System.out.println("Erro do neuronio oculto (" + k + ") = " + rede.camadaOculta.neuronios[k].ultimo_erro);
                }
            }

            System.out.println("\n\n");

            rede.ajustaPesosCamadaSaida(classe_esperada , classe_obtida);

            System.out.println("\n\n");

            rede.ajustaPesosCamadaOculta(entrada);
        }
        rede.calculaErroTotal(Holdout.conjTreinamento, rede);
        return erro_geral;
    }

    //Metodo para a etapa de verificacao
    public static void verificaRede(){

    }

    //Metodo para a etapa de testes
    public static void testaRede(){

    }

    public static void runner(int neuronios_ocultos, double taxa_aprendizado) {
        MLP rede = new MLP(neuronios_ocultos, taxa_aprendizado);

        preparaDados("src/main/resources/optdigits.csv");//Converter para um endereco relativo de acordo com a estrutura do projeto

        treinaRede(rede);

    }

    public static void main(String[] args) {
        runner(30,0.1);
    }
}
