package MLP;

public class Camada {
    public Neuronio[] neuronios;
    public int tamanho;

    public Camada(int tamanhoCamada, int tamanhoCamadaAnterior){
        tamanho = tamanhoCamada;
        neuronios = new Neuronio[tamanhoCamada];

        for(int j = 0; j < tamanho; j++) {
            neuronios[j] = new Neuronio(tamanhoCamadaAnterior);
        }
    }
}
