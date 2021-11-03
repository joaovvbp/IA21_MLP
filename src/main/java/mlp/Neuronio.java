package mlp;

import java.util.Random;

import static java.lang.Math.exp;

public class Neuronio {
    Random random = new Random();
    public int Id; //Usado para se referenciar ao neurônio quando não se tem acesso ao seu indíce no vetor da camada diretamente
    public double saida;
    public double somaPonderada;//Em outras palavras, a entrada "Geral" de um Neuronio
    public double[] pesos;
    public double ultimoErro;//Usado no cálculo do erro da camada oculta
    public double[] ultimoAjuste;//Usado no cálculo do termo momentum

    //Construtor do neuronio, inicializa os pesos de forma aleatória e define seu ID
    public Neuronio(int pesosRecebidos, int ID) {
        this.Id = ID;
        pesos = new double[pesosRecebidos];
        ultimoAjuste = new double[pesosRecebidos];
    }

    //Inicializa os pesos com valores aleatórios entre -1 e 1
    public void inicializaPesos() {
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = random.nextDouble() * (1 + 1) - 1;
        }
    }

    public void normalizaPesos() {
        double somaPesos = 0;
        for (double peso : pesos) {
            somaPesos += peso;
        }
        for (int i = 0; i < pesos.length; i++) {
            if (somaPesos != 0) pesos[i] = pesos[i] / somaPesos;
        }
    }

    //Calcula a soma ponderada e a saída utilizando a função de ativação sigmoide
    public void propagaOculta(Double[] entrada) {
        somaPonderada = 0;
        for (int i = 0; i < entrada.length; i++) {
            somaPonderada += entrada[i] * pesos[i];
        }
        saida = sigmoide(somaPonderada);
    }

    //Calcula a soma ponderada e a saída utilizando a função de ativação sigmoide
    public void propagaSaida(Camada camadaoculta) {
        somaPonderada = 0;
        for (int i = 0; i < camadaoculta.tamanhoCamada; i++) {
            somaPonderada += camadaoculta.neuronios[i].saida * pesos[i];
        }
        saida = sigmoide(somaPonderada);
    }

    //Função de ativação do neurônio
    public static double sigmoide(double somaponderadaDoNeuronio) {
        return 1 / (1 + exp((-1 * somaponderadaDoNeuronio)));
    }
}
