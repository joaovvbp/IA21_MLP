package MLP;

import java.util.Random;

import static java.lang.Math.exp;

public class Neuronio {
    Random random = new Random();
    public int ID = -1;
    public double saida;
    public double soma_ponderada;
    public double ultimo_erro;
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
        double somapesos = 0;
        for (double d : pesos) {
            somapesos += d;
        }
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = pesos[i] / somapesos;
        }
    }

    public void somaPonderadaOculta(Double[] entrada) {
        soma_ponderada = 0;
        normalizaPesos();
        for (int i = 0; i < entrada.length; i++) {
            soma_ponderada += entrada[i] * pesos[i];
            //System.out.println("R(" + somaponderada + ")IN(" + entrada[i] + ")" + "* PESO(" + pesos[i] + ") ");
        }
        saida = sigmoide(soma_ponderada);
    }

    public void somaPonderadaSaida(Camada camadaoculta) {
        soma_ponderada = 0;
        normalizaPesos();
        for (int i = 0; i < camadaoculta.tamanhoCamada; i++) {
            soma_ponderada += camadaoculta.neuronios[i].saida * pesos[i];
            //System.out.println("R(" + somaponderada + ")IN(" + camadaoculta.neuronios[i].saida + ")" + "* PESO(" + pesos[i] + ") ");
        }
        saida = sigmoide(soma_ponderada);
        System.out.println("Saida "+ID+": " + saida);
    }

    public static double sigmoide(double somaponderadaDoNeuronio) {
        return 1 / (1 + exp(somaponderadaDoNeuronio));
    }
}
