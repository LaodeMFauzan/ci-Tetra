import java.io.*;
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

    public static BufferedReader readData(String fileName){
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            return br;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        MaxentTagger tagger = new MaxentTagger(MaxentTagger.DEFAULT_NLP_GROUP_MODEL_PATH);
        TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),("untokenizable=noneKeep"));

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out,"utf-8"));

        DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(readData("data/sample-uc.txt"));
        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);

        for (List<HasWord> sentence: documentPreprocessor){
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);

            pw.println(SentenceUtils.listToString(tSentence,false));
        }
        pw.close();
    }
}
