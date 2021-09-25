package MLP;

public class Camada {
    public Neuronio[] neuronios;
    //public double[] valores;
    public int tamanho;

    public Camada(int tamanhoCamada, int tamanhoCamadaAnterior){
        tamanho = tamanhoCamada;
        neuronios = new Neuronio[tamanhoCamada];
        //valores = new double[neuronios.length];

        for(int j = 0; j < tamanho; j++) {
            neuronios[j] = new Neuronio(tamanhoCamadaAnterior);
            neuronios[j].inicializaPesos();
        }
    }
}
