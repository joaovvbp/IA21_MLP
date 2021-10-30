package MLP;

import java.util.Random;

import static java.lang.Math.exp;

public class Neuronio {
    Random random = new Random();
    public int ID = -1; //TODO: Verificar a necessidade disto

    public double saida;
    public double soma_ponderada;//Em outras palavras, a entrada "Geral" de um Neuronio

    public double[] pesos;

    public double ultimo_erro;
    public double[] ultimo_ajuste;

    public Neuronio(int pesosRecebidos, int ID) {
        this.ID = ID;
        pesos = new double[pesosRecebidos];
        ultimo_ajuste = new double[pesosRecebidos];
    }

    public void inicializaPesos() {
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = random.nextDouble() * (1 + 1) - 1;
        }
        /*
        double somapesos = 0;
        for (double peso : pesos) {
            somapesos += peso;
        }
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = pesos[i] / somapesos;
        }
         */
    }

    public void normalizaPesos() {
        /*
        double somapesos = 0;
        for (double peso : pesos) {
            somapesos += peso;
        }
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = pesos[i] / somapesos;
        }

         */
    }

    public void somaPonderadaOculta(Double[] entrada) {
        soma_ponderada = 0;
        for (int i = 0; i < entrada.length; i++) {
            soma_ponderada += entrada[i] * pesos[i];
        }
        saida = sigmoide(soma_ponderada);
    }

    public void somaPonderadaSaida(Camada camadaoculta) {
        soma_ponderada = 0;
        for (int i = 0; i < camadaoculta.tamanhoCamada; i++) {
            soma_ponderada += camadaoculta.neuronios[i].saida * pesos[i];
        }
        saida = sigmoide(soma_ponderada);
    }

    public static double sigmoide(double somaponderadaDoNeuronio) {
        return 1 / (1 + exp((-1 * somaponderadaDoNeuronio)));
    }
}
