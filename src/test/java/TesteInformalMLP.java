import MLP.MLP;

public class TesteInformalMLP {
    static double[] entrada = new double[]{0,0,5,13,9,1,0,0,0,0,13,15,10,15,5,0,0,3,15,2,0,11,8,0,0,4,12,0,0,8,8,0,0,5,8,0,0,9,8,0,0,4,11,0,1,12,7,0,0,2,14,5,10,12,0,0,0,0,6,13,10,0,0,0};

    public static void somaPonderadaTest(){
        MLP rede1 = new MLP(3, 0.1);
        rede1.somaPonderada(entrada,  rede1.camadaOculta.neuronios[0]);
    }

    public static void main(String[] args) {
        somaPonderadaTest();
    }
}
