import MLP.MLP;
import MLP.Neuronio;
import Processamento.Holdout;
import Processamento.KFoldCrossValidation;
import Processamento.ProcessamentoDeArquivo;

public class TesteInformalMLP {
    static double[] entrada1 = new double[]{0, 0, 6, 12, 14, 5, 0, 0, 0, 4, 16, 11, 8, 15, 6, 0, 0, 4, 16, 7, 2, 12, 6, 0, 0, 0, 13, 15, 15, 13, 2, 0, 0, 0, 1, 16, 16, 6, 0, 0, 0, 0, 8, 15, 14, 15, 1, 0, 0, 3, 16, 10, 10, 16, 1, 0, 0, 1, 12, 16, 14, 5, 0, 0};//8

    public static void somaPonderadaTest(double[] entrada) {
        MLP rede1 = new MLP(3, 0.1);
        for (int i = 0; i < rede1.camadaOculta.tamanhoCamada; i++) {
            rede1.camadaOculta.neuronios[i].somaPonderadaOculta(entrada);
        }
        for (int i = 0; i < rede1.camadaSaida.tamanhoCamada; i++) {
            rede1.camadaSaida.neuronios[i].somaPonderadaSaida(rede1.camadaOculta);
        }
    }

    public static void propagacaoTest(double[] entrada, int classe_esperada) {
        MLP rede1 = new MLP(30, 0.1);
        int classe_obtida = -1;
        rede1.forwardPropagation(entrada, rede1);

        int[] vetor_saida_esperado = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        vetor_saida_esperado[classe_esperada] = 1;

        int[] vetor_saida_obtido = rede1.converteSaida(rede1.camadaSaida);

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
                //System.out.println("Erro do neuronio de saida (" + i + ")[Esperado] = " + rede1.camadaSaida.neuronios[i].ultimo_erro);
            } else {
                rede1.calculaErroNeuronioSaida(rede1.camadaSaida.neuronios[i], rede1.camadaSaida.neuronios[i].saida, 0);
                //System.out.println("Erro do neuronio de saida (" + i + ") = " + rede1.camadaSaida.neuronios[i].ultimo_erro);
            }
        }

        //System.out.println();

        for (int i = 0; i < rede1.camadaOculta.tamanhoCamada; i++) {
            if (i == classe_esperada) {
                rede1.calculaErroNeuronioOculto(rede1.camadaOculta.neuronios[i], rede1.camadaOculta.neuronios[i].saida);
                //System.out.println("Erro do neuronio oculto (" + i + ")[Esperado] = " + rede1.camadaSaida.neuronios[i].ultimo_erro);
            } else {
                rede1.calculaErroNeuronioOculto(rede1.camadaOculta.neuronios[i], rede1.camadaOculta.neuronios[i].saida);
                //System.out.println("Erro do neuronio oculto (" + i + ") = " + rede1.camadaSaida.neuronios[i].ultimo_erro);
            }
        }

        rede1.ajustaPesosCamadaSaida(classe_esperada , classe_obtida);

        rede1.ajustaPesosCamadaOculta(entrada);

    }

    public static void main(String[] args) {
        propagacaoTest(entrada1, 8);
    }
}
