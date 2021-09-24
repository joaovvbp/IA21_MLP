package MLP;

import java.util.Random;

public class Neuronio {
    Random random = new Random();
    protected double[] pesos;

    public Neuronio(int pesosRecebidos) {
        pesos = new double[pesosRecebidos];
    }

    public void inicializaPesos() {
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = random.nextDouble();
        }
    }

    public void normalizaPesos() {
        double somapesos = 0;
        for (double d : pesos) {
            somapesos += d;
        }
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = pesos[i]/somapesos;
        }
    }

}
