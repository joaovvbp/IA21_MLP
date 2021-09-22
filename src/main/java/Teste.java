import java.io.IOException;

public class Teste {

    public static void main(String[] args) throws IOException {
        LerDados.lerDados("src/main/resources/optdigits.csv");
        System.out.println(LerDados.vetores);

    }

}
