import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static ArrayList<String> aCondition = new ArrayList<>();

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
            if (i == 2){
                aCondition.add(headerTable[i]);
            }
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
        int countACond = 1;
        int linesScanned = 5;

        //TODO: Change this loop to while
        while(linesScanned < lines.size()){
            if(lines.get(linesScanned).contains("Main") ){
                isMainScenario = true;
                linesScanned++;
                continue;
            }
            else if(lines.get(linesScanned).contains("Extensions") ){
                isExtension = true;
                isMainScenario = false;
            }
            else if(lines.get(linesScanned).contains("Variations")){
                isVariation = true;
                isExtension = false;
            }
            String getNumberLetterFormat = "\\d[a-zA-Z](\\d)"; // Get the number letter number format in variation and extension
            Pattern pattern = Pattern.compile(getNumberLetterFormat);
            Matcher matcher = pattern.matcher(lines.get(linesScanned));

            if (!matcher.find() && (isExtension || isVariation)){
                linesScanned++;
                continue;
            }

            String number= getNumber(linesScanned,lines.get(linesScanned),isMainScenario);

            if (lines.get(linesScanned).contains("abort")){
                hasil.add(number+" abort "+null+" "+null+" "+aCondition.get(countACond));
                countACond++;
            } else if (lines.get(linesScanned).contains("go to step")) {
                hasil.add(number+" stepj");
            } else {
                String vb = getVBZ(lines.get(linesScanned));
                String nn = getNN(lines.get(linesScanned));
                String sender = getSender(lines.get(linesScanned));
                String receiver = getReceiver(lines.get(linesScanned));
                if(isVariation){
                    hasil.add(number+" "+vb+nn+" "+sender+" "+receiver+" "+aCondition.get(countACond));
                }
                else
                    hasil.add(number+" "+vb+nn+" "+sender+" "+receiver+" "+null);
            }
            linesScanned++;
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

   private static String getVBZ(String line){
        String result[] = line.split(" ");
        for (int i = 0; i < result.length; i++){
            if (result[i].contains("/VBZ")){
                return result[i].split("/")[0];
            }
        }
        return null;
   }

    private static String getNN(String line){
        String result[] = line.split(" ");
        boolean isGetVerb = false;
        for(int i = 0; i < result.length; i++){
            if (result[i].contains("/VBZ")){ //Get the noun after verb found
               isGetVerb = true;
            } else if(isGetVerb && !result[i].contains("NNP") && result[i].contains("/NN")){
                String firstChar = result[i].substring(0, 1).toUpperCase(); //Capitalize first letter to make it camelCase
                return firstChar+result[i].substring(1).split("/")[0];
            }
        }
        return null;
    }

    private static String getSender(String line){
        String result[] = line.split(" ");
        return result[1].split("/")[0];
    }

    private static String getReceiver(String line){
        String result[] = line.split(" ");
        int countNNP = 0;
        for (int i = 2; i < result.length; i++){ // first and second word are always number and sender,so we go to 3rd word
            if(result[i].contains("NNP") && i != 2){ //Special case for sender with two NNP
                countNNP++;
            }
            if (result[i].contains("submit") || result[i].contains("enter") || result[i].contains("gives")){
                return "System";
            } else if(result[i].contains("ask") || result[i].contains("provide") ) {
                return "Seller";
            } else if(countNNP > 0) {
                return result[i].split("/")[0];
            }
        }
        return null;
    }

    private static void getACondition(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        boolean isExtOrVar = false;
        int countACond = 1;
        for(int i = 5; i < lines.size(); i++){
            if (lines.get(i).contains("Extensions")){
                isExtOrVar = true;
            } else if(isExtOrVar){
                String getNumberLetterFormat = "\\d[a-zA-Z]"; // Get the number letter format in variation and extension
                Pattern pattern = Pattern.compile(getNumberLetterFormat);
                Matcher matcher = pattern.matcher(lines.get(i));

                String getNumberLetterNumberFormat = "\\d[a-zA-Z](\\d)";
                Pattern patternCheck = Pattern.compile(getNumberLetterNumberFormat);
                Matcher matcherCheck = patternCheck.matcher(lines.get(i));

                if(matcher.find() && !matcherCheck.find()){
                    aCondition.add("cond"+countACond+"/"+lines.get(i).split(getNumberLetterFormat)[1]);
                    countACond++;
                }
            }
        }
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
        String[] headerTable = getHeaderTable(input);
        getACondition(input);
        ArrayList<String> hasil = new ArrayList<>();
        hasil.addAll(Arrays.asList(headerTable));
        hasil.addAll(getActivityTable(taggedInput));

        for (String print:hasil){
            System.out.println(print);
        }
    }
}

