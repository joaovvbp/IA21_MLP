package MLP;

import java.util.Random;

import static java.lang.Math.exp;

public class Neuronio {
    Random random = new Random();
    public double saida;
    public double somaponderada;
    public double[] pesos;

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
            pesos[i] = pesos[i] / somapesos;
        }
    }

    public void somaPonderadaOculta(double[] entrada) {
        somaponderada = 0;
        normalizaPesos();
        for (int i = 0; i < entrada.length; i++) {
            somaponderada += entrada[i] * pesos[i];
            //System.out.println("R(" + somaponderada + ")IN(" + entrada[i] + ")" + "* PESO(" + pesos[i] + ") ");
        }
        saida = sigmoide(somaponderada);
    }

    public void somaPonderadaSaida(Camada camadaoculta) {
        somaponderada = 0;
        normalizaPesos();
        for (int i = 0; i < camadaoculta.tamanho; i++) {
            somaponderada += camadaoculta.neuronios[i].saida * pesos[i];
            //System.out.println("R(" + somaponderada + ")IN(" + camadaoculta.neuronios[i].saida + ")" + "* PESO(" + pesos[i] + ") ");
        }
        saida = sigmoide(somaponderada);
        System.out.println("Saida: " + saida);
    }

    public static double sigmoide(double somaponderadaDoNeuronio) {
        return 1 / (1 + exp(somaponderadaDoNeuronio));
    }
}
