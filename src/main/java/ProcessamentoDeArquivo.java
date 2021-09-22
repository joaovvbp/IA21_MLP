import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessamentoDeArquivo {

    static List<Integer[]> vetores = new ArrayList<>();

    static void lerDados(String nome) throws IOException {

        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader(nome));

        while ((row = csvReader.readLine()) != null) {
            Integer[] entradaInteger = new Integer[65];
            String[] data = row.split(",");
            for (int i = 0; i < data.length; i++) {
                entradaInteger[i] = Integer.parseInt(data[i]);
            }
            vetores.add(entradaInteger);
        }
        csvReader.close();
        System.out.println("Tamanho: "+vetores.size());
    }


}