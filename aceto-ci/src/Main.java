import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public static  String getBodyTable(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        String hasil = "";
        for (int i = 5; i<lines.size(); i++){
            if (lines.get(i).contains("abort")){
                hasil = i+" abort";
            } else if (lines.get(i).contains("go to step")) {
                hasil = i +" stepj";
            } else {
                hasil = i + " ";
            }
        }
        return null;
    }

    public static String getFirstVB(String line){

        return null;
    }

    public static String getNN(String line){

        return null;
    }

    public static String getSender(String line){

        return null;
    }

    public String getReceiver(String line){
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

//        for (String header:getHeaderTable(input)){
//            System.out.println(header);
//        }
        //getBodyTable(taggedtext);
    }
}

