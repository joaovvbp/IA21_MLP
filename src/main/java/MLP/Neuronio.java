package MLP;

import java.util.Random;

import static java.lang.Math.exp;

public class Neuronio {
    Random random = new Random();
    public int ID = -1;
    public double saida;
    public double somaPonderada;
    public double ultimoErro;
    public double[] pesos;

    public Neuronio(int pesosRecebidos, int ID) {
        this.ID = ID;
        pesos = new double[pesosRecebidos];
    }

    public void inicializaPesos() {
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = random.nextDouble() * (1 + 1) - 1;
            //System.out.println(pesos[i]);
        }
    }

    public void normalizaPesos() {
        double somaPesos = 0;
        for (double d : pesos) {
            somaPesos += d;
        }
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = pesos[i] / somaPesos;
        }
    }

    public void somaPonderadaOculta(Double[] entrada) {
        somaPonderada = 0;
        normalizaPesos();
        for (int i = 0; i < entrada.length; i++) {
            somaPonderada += entrada[i] * pesos[i];
            //System.out.println("R(" + somaponderada + ")IN(" + entrada[i] + ")" + "* PESO(" + pesos[i] + ") ");
        }
        saida = sigmoide(somaPonderada);
    }

    public void somaPonderadaSaida(Camada camadaOculta) {
        somaPonderada = 0;
        normalizaPesos();
        for (int i = 0; i < camadaOculta.tamanhoCamada; i++) {
            somaPonderada += camadaOculta.neuronios[i].saida * pesos[i];
            //System.out.println("R(" + somaponderada + ")IN(" + camadaoculta.neuronios[i].saida + ")" + "* PESO(" + pesos[i] + ") ");
        }
        saida = sigmoide(somaPonderada);
//        System.out.println("Saida "+ID+": " + saida);
    }

    public static double sigmoide(double somaPonderadaDoNeuronio) {
        return 1 / (1 + exp(somaPonderadaDoNeuronio));
    }
}
