import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Main {

    public static File processedByParser(String fileName,TokenizerFactory<CoreLabel> ptbTokenizerFactory,MaxentTagger tagger) throws Exception {
        File output = new File("out/tagged-uc.txt");
        PrintStream ps = new PrintStream(output);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"utf-8"));

        DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(br);
        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);

        for (List<HasWord> sentence : documentPreprocessor) {
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);
            String outputString = SentenceUtils.listToString(tSentence,false)+"\n";
            ps.write(outputString.getBytes());
        }

        ps.close();
        //System.out.println("Tagged text has been made");

        return output;
    }

    private static String[] getHeaderTable(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] headerTable = new String[5]; // header table contains 5 field

        for(int i = 0; i < headerTable.length; i++){
            String readLine = br.readLine();
            headerTable[i] = readLine.substring(readLine.indexOf(":") + 1);
        }

        return headerTable;
    }

    public static ArrayList<String> getActivityTable(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        ArrayList<String> hasil = new ArrayList<>();
        boolean isMainScenario = false;
        boolean isExtension = false;
        boolean isVariation = false;
        for (int i = 0; i < lines.size(); i++){ // Ganti loop ini jadi while TODO: Change this loop to while
            if(lines.get(i).contains("Main") ){
                isMainScenario = true;
                continue;
            }
            else if(lines.get(i).contains("Extensions") ){
                isExtension = true;
                isMainScenario = false;
            }
            else if(lines.get(i).contains("Variations")){
                isVariation = true;
                isExtension = false;
            }
            String getNumberLetterFormat = "\\d[a-zA-Z](\\d)"; // Get the number letter format in variation and extension
            Pattern pattern = Pattern.compile(getNumberLetterFormat);
            Matcher matcher = pattern.matcher(lines.get(i));

            if (!matcher.find() && (isExtension || isVariation)){
                continue;
            }

            String number= getNumber(i,lines.get(i),isMainScenario);


                if (lines.get(i).contains("abort")){
                    hasil.add(number+" abort");
                } else if (lines.get(i).contains("go to step")) {
                    hasil.add(number+" stepj");
                } else {
                    String vb = getVBZ(lines.get(i));
                    String nn = getNN(lines.get(i));
                    String sender = getSender(lines.get(i));
                    String receiver = getReceiver(lines.get(i));
                    String aCondition = getACondition(lines.get(i));
                    hasil.add(number+" "+vb+" "+nn+" "+sender+" "+receiver+" "+aCondition);
                }
            }

        return hasil;
    }

    private static String getNumber(int i,String line,boolean isMainScenario){
        if (isMainScenario){
            int hasil = i-5;
            return String.valueOf(hasil);
        } else {
            String result[] = line.split(" ");
            return result[0].split("/")[0];
        }
    }

   public static String getVBZ(String line){
        String result[] = line.split(" ");
        for (int i = 0; i < result.length; i++){
            if (result[i].contains("/VBZ")){
                result[i] = result[i].split("/")[0];
                return result[i];
            }
        }
        return null;
   }

    public static String getNN(String line){
        return null;
    }

    public static String getSender(String line){
        String result[] = line.split(" ");
        return result[1].split("/")[0];
    }

    public static String getReceiver(String line){
        return null;
    }

    public static String getACondition(String line){
        return null;
    }

    public static String getActivityTable(){
        return null;
    }

    public static void main(String[] args) throws Exception {
        MaxentTagger tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
        TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),("untokenizable=noneKeep"));

        File input = new File("data/sample-uc.txt");
        File taggedInput = processedByParser(input.getPath(),ptbTokenizerFactory,tagger);

//        Path path = Paths.get(taggedInput.getPath());
//        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
//        for(int i = 0; i < lines.size(); i++){
//            System.out.println(lines.get(i));
//        }
        ArrayList<String> hasil = getActivityTable(taggedInput);
        for (String print:hasil){
            System.out.println(print);
        }
    }
}

