import MLP.MLP;
import MLP.Neuronio;
import Processamento.Exemplo;
import Processamento.Holdout;
import Processamento.KFoldCrossValidation;
import Processamento.ProcessamentoDeArquivo;

import java.util.ArrayList;
import java.util.List;

public class TesteInformalMLP {

    public static void propagacaoTest() {
        ProcessamentoDeArquivo.processarDados("src/main/resources/optdigits.csv");
        Holdout.holdout();

        Double[] entrada = Holdout.conjTreinamento.get(0).vetorEntradas;
        int classe_esperada = Holdout.conjTreinamento.get(0).retornaRotulo();

        MLP rede1 = new MLP(30, 0.1);
        int classe_obtida = -1;
        rede1.forwardPropagation(entrada, rede1);

        int[] vetor_saida_esperado = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        vetor_saida_esperado[classe_esperada] = 1;

        int[] vetor_saida_obtido = rede1.converteSaida(rede1.camadaSaida);

        rede1.saidasDaRede.add(vetor_saida_obtido);

        System.out.println();
        for (int i = 0; i < vetor_saida_obtido.length; i++) {
            if (vetor_saida_obtido[i] == 1) {
                System.out.println("Classe obtida = (" + i + ")");
                classe_obtida = i;
            }
        }
        System.out.println();

        System.out.println("Classe esperada = (" + classe_esperada + ")");

        System.out.println();

        for (int i = 0; i < rede1.camadaSaida.tamanhoCamada; i++) {
            if (i == classe_esperada) {
                rede1.calculaErroNeuronioSaida(rede1.camadaSaida.neuronios[i], rede1.camadaSaida.neuronios[i].saida, 1);
                System.out.println("Erro do neuronio de saida (" + i + ")[Esperado] = " + rede1.camadaSaida.neuronios[i].ultimoErro);
            } else {
                rede1.calculaErroNeuronioSaida(rede1.camadaSaida.neuronios[i], rede1.camadaSaida.neuronios[i].saida, 0);
                System.out.println("Erro do neuronio de saida (" + i + ") = " + rede1.camadaSaida.neuronios[i].ultimoErro);
            }
        }

        System.out.println();

        for (int i = 0; i < rede1.camadaOculta.tamanhoCamada; i++) {
            if (i == classe_esperada) {
                rede1.calculaErroNeuronioOculto(rede1.camadaOculta.neuronios[i], rede1.camadaOculta.neuronios[i].saida);
                System.out.println("Erro do neuronio oculto (" + i + ")[Esperado] = " + rede1.camadaOculta.neuronios[i].ultimoErro);
            } else {
                rede1.calculaErroNeuronioOculto(rede1.camadaOculta.neuronios[i], rede1.camadaOculta.neuronios[i].saida);
                System.out.println("Erro do neuronio oculto (" + i + ") = " + rede1.camadaOculta.neuronios[i].ultimoErro);
            }
        }

        List<Exemplo> conjTreinamentoTest = new ArrayList<>();
        Exemplo teste = Holdout.conjTreinamento.get(0);
        conjTreinamentoTest.add(teste);

        rede1.calculaErroTotal(conjTreinamentoTest, rede1);

        System.out.println("\n\n");

        rede1.ajustaPesosCamadaSaida(classe_esperada , classe_obtida);

        System.out.println("\n\n");

        rede1.ajustaPesosCamadaOculta(entrada);

    }

    public static void main(String[] args) {
        propagacaoTest();
    }
}
