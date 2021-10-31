package MLP;

import java.util.Random;

import static java.lang.Math.exp;

public class Neuronio {
    Random random = new Random();
    //TODO: Econtrar uma forma de se livrar disso, é meio gambiarra
    public int ID; //Usado para se referenciar ao neurônio quando não se tem acesso ao seu indíce no vetor da camada diretamente

    public double saida;
    public double soma_ponderada;//Em outras palavras, a entrada "Geral" de um Neuronio

    public double[] pesos;

    public double ultimo_erro;//Usado no cálculo do erro da camada oculta
    public double[] ultimo_ajuste;//Usado no cálculo do termo momentum

    //Construtor do neuronio, inicializa os pesos de forma aleatória e define seu ID
    public Neuronio(int pesosRecebidos, int ID) {
        this.ID = ID;
        pesos = new double[pesosRecebidos];
        ultimo_ajuste = new double[pesosRecebidos];
    }

    //Inicializa os pesos com valores aleatórios entre -1 e 1
    public void inicializaPesos() {
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = random.nextDouble() * (1 + 1) - 1;
        }
    }

    public void normalizaPesos() {
        double somapesos = 0;
        for (double peso : pesos) {
            somapesos += peso;
        }
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = pesos[i] / somapesos;
        }
    }

    //Calcula a soma ponderada e a saída utilizando a função de ativação sigmoide
    public void propagaOculta(Double[] entrada) {
        soma_ponderada = 0;
        for (int i = 0; i < entrada.length; i++) {
            soma_ponderada += entrada[i] * pesos[i];
        }
        saida = sigmoide(soma_ponderada);
    }

    //Calcula a soma ponderada e a saída utilizando a função de ativação sigmoide
    public void propagaSaida(Camada camadaoculta) {
        soma_ponderada = 0;
        for (int i = 0; i < camadaoculta.tamanhoCamada; i++) {
            soma_ponderada += camadaoculta.neuronios[i].saida * pesos[i];
        }
        saida = sigmoide(soma_ponderada);
    }

    //Função de ativação do neurônio
    public static double sigmoide(double somaponderadaDoNeuronio) {
        return 1 / (1 + exp((-1 * somaponderadaDoNeuronio)));
    }
}
