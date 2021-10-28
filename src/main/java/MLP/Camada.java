package MLP;

public class Camada {
    public Neuronio[] neuronios;
    public int tamanhoCamada;

    public Camada(int tamanhoCamada, int tamanhoCamadaAnterior){
        this.tamanhoCamada = tamanhoCamada;
        neuronios = new Neuronio[tamanhoCamada];

        for(int j = 0; j < this.tamanhoCamada; j++) {
            neuronios[j] = new Neuronio(tamanhoCamadaAnterior, j);
            neuronios[j].inicializaPesos();
        }
    }
}
