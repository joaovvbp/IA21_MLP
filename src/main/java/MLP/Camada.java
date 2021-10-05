package MLP;

public class Camada {
    public Neuronio[] neuronios;
    //public double[] valores;
    public int tamanhoCamada;

    public Camada(int tamanhoCamada, int tamanhoCamadaAnterior){
        this.tamanhoCamada = tamanhoCamada;
        neuronios = new Neuronio[tamanhoCamada];
        //valores = new double[neuronios.length];

        for(int j = 0; j < this.tamanhoCamada; j++) {
            neuronios[j] = new Neuronio(tamanhoCamadaAnterior, j);
            //System.out.println("ID: "+neuronios[j].ID);
            neuronios[j].inicializaPesos();
        }
    }
}
