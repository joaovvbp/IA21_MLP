import MLP.MLP;

public class TesteInformalMLP {
    static double[] entrada1 = new double[]{0, 0, 5, 13, 9, 1, 0, 0, 0, 0, 13, 15, 10, 15, 5, 0, 0, 3, 15, 2, 0, 11, 8, 0, 0, 4, 12, 0, 0, 8, 8, 0, 0, 5, 8, 0, 0, 9, 8, 0, 0, 4, 11, 0, 1, 12, 7, 0, 0, 2, 14, 5, 10, 12, 0, 0, 0, 0, 6, 13, 10, 0, 0, 0};
    static double[] entrada2 = new double[]{0, 0, 11, 12, 0, 0, 0, 0, 0, 2, 16, 16, 16, 13, 0, 0, 0, 3, 16, 12, 10, 14, 0, 0, 0, 1, 16, 1, 12, 15, 0, 0, 0, 0, 13, 16, 9, 15, 2, 0, 0, 0, 0, 3, 0, 9, 11, 0, 0, 0, 0, 0, 9, 15, 4, 0, 0, 0, 9, 12, 13, 3, 0, 0};
    static double[] entrada3 = new double[]{};

    public static void somaPonderadaTest(double[] entrada) {
        MLP rede1 = new MLP(3, 0.1);
        for (int i = 0; i < rede1.camadaOculta.tamanho; i++) {
            rede1.camadaOculta.neuronios[i].somaPonderadaOculta(entrada);
        }
        for (int i = 0; i < rede1.camadaSaida.tamanho; i++) {
            rede1.camadaSaida.neuronios[i].somaPonderadaSaida(rede1.camadaOculta);
        }
    }

    public static void propagacaoTest(double[] entrada) {
        MLP rede1 = new MLP(3, 0.1);
        rede1.forwardPropagation(entrada, rede1);
    }

    public static void main(String[] args) {
        propagacaoTest(entrada2);
    }
}
