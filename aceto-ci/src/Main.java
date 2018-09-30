import java.io.BufferedReader;
import java.io.FileReader;
import edu.stanford.nlp.coref.data.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;

public class Main {

    public static void readData(String fileName){
        String line = "";
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            while((line = br.readLine()) != null){
                System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readData("data/sample-uc.txt");
    }
}
