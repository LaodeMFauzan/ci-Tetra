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

    public static void readData(String fileName,TokenizerFactory<CoreLabel> ptbTokenizerFactory,MaxentTagger tagger) throws Exception {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out,"utf-8"));

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"utf-8"));


        DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(br);
        documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);

        for (List<HasWord> sentence : documentPreprocessor) {
            List<TaggedWord> tSentence = tagger.tagSentence(sentence);
            pw.println(SentenceUtils.listToString(tSentence, false));

        }
        pw.close();
    }

    public static void main(String[] args) throws Exception {
        MaxentTagger tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
        TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),("untokenizable=noneKeep"));

        readData("data/sample-uc.txt",ptbTokenizerFactory,tagger);

    }
}

