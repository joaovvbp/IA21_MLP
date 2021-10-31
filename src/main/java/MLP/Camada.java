package MLP;

public class Camada {
    public Neuronio[] neuronios;//Representa os neurônios da camada
    public int tamanhoCamada;

    public Camada(int tamanhoCamada, int tamanhoCamadaAnterior) {
        this.tamanhoCamada = tamanhoCamada;
        neuronios = new Neuronio[tamanhoCamada];

        for (int j = 0; j < this.tamanhoCamada; j++) {//Inicializa todos os neurônios com pesos aleatórios (entre -1 e 1)
            neuronios[j] = new Neuronio(tamanhoCamadaAnterior, j);
            neuronios[j].inicializaPesos();
        }
    }
}
