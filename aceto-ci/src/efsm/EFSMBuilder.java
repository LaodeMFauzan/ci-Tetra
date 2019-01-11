package efsm;

import efsm.model.FSMConcept;
import efsm.model.FSMState;
import efsm.model.extension.EFSMState;
import efsm.model.extension.EFSMTransition;
import efsm.model.impl.DefaultFSMConcept;
import table.ActivityTableBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 29/12/18
 */

public class EFSMBuilder {
    private Set<String> inputSet,outputSet;
    private Set<FSMState> stateSet;
    private Set<EFSMTransition> transitionsSet;

    private HashMap<String,String> predicatesMap;
    private HashMap<String,String> branchMap;
    private HashMap<String,EFSMState> branchStateMap;

    private FSMStateMachine finiteStateMachine;
    private FSMConcept concept;

    int sequenceChar = 65;



    public EFSMBuilder() {
        initializeEFSM();
        try {
            setupEFSM(new File("out/activity-table.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FSMStateMachine getFiniteStateMachine() {
        return finiteStateMachine;
    }

    private void initializeEFSM(){
        ActivityTableBuilder.printToText(new File("data/sample-uc.txt"));

        stateSet = new LinkedHashSet<>();
        inputSet = new LinkedHashSet<>();
        outputSet = new LinkedHashSet<>();
        transitionsSet = new LinkedHashSet<>();

        predicatesMap = new HashMap<>();
        branchMap = new HashMap<>();
        branchStateMap = new HashMap<>();

        //Precondition for every use case
        //predicatesSet.add("cond0");
        //stateSet.add(new EFSMState("Start_State"));

        //Initialize
        concept = new DefaultFSMConcept();
        finiteStateMachine = new DefaultFSMStateMachine(concept);
    }

    private  void setupEFSM(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        String getNumberLetterFormat = "\\d[a-zA-Z](\\d)";
        Pattern pattern = Pattern.compile(getNumberLetterFormat);

        getBranch(lines,pattern);

        EFSMState previousState = null;
        String getPredicate = null;

        //COMPLETED(1) : read every line and get state of the efsm
        for(int i = 0; i < lines.size(); i++){

            Matcher matcher = pattern.matcher(lines.get(i));

            if(matcher.find()){
                getAllPredicate(i,lines);
                break;
            }

            if (i == 0){
                // get the start state -- think about it
                previousState = new EFSMState("Start_State");
                finiteStateMachine.addState(previousState);
                getPredicate = "cond0";
            } else {
                getPredicate = lines.get(i).split(" ")[4];

                if (!getPredicate.equals("null") ) {
                    getPredicate = getPredicate.substring(0, getPredicate.length() - 1);

                }
                else
                    getPredicate = null;
            }
            //Predicate set
            //predicatesSet.put(getPredicate);

            // Use the alphabet to name a state
            char stateName = (char) sequenceChar;
            EFSMState efsmState = constructState(stateName);
            sequenceChar++;

            //Input set and Output set
            boolean isInput =  isHaveInput(lines.get(i));

            //Transition set
            constructTransition(lines.get(i),isInput,getPredicate,efsmState,previousState);

            //Get the previous state
            previousState = efsmState;

            //COMPLETED(4) : do branching ?
            //Key to lookup for branch
            String indexExtension = lines.get(i).split(" ")[0]+"a1";
            if(branchMap.get(indexExtension) != null ){
                createBranch(indexExtension,isInput,previousState);
            }
        }
        concept.attachStateMachine(finiteStateMachine);
    }

    private void getAllPredicate(int index,List<String> lines) throws IOException {
        predicatesMap.put("cond0",ActivityTableBuilder.aCondition.get(0));
        for(int i = index; i<lines.size(); i++){
            String keyPredicate = lines.get(i).split(" ")[4];
            keyPredicate = keyPredicate.substring(0,keyPredicate.length()-1);
            String valuePredicateMap = lines.get(i).split("/")[1];
            predicatesMap.put(keyPredicate,valuePredicateMap);
        }
    }

    private boolean isHaveInput(String line){
        boolean isInput =  line.split(" ")[3].equalsIgnoreCase("System");
        if(isInput) {
            inputSet.add(line.split(" ")[1]);
        } else {
            outputSet.add(line.split(" ")[1]);
        }
        return isInput;
    }

    private void constructTransition(String line,boolean isInput,String predicate, EFSMState currentState,EFSMState previousState){
        EFSMTransition transition = new EFSMTransition(previousState,currentState);
        String[] splittedLine = line.split(" ");
        if(isInput){
            transition.setActionName(splittedLine[1]);
            //transition.setBlockName(null);
        } else {
            //transition.setActionName(null);
            transition.setBlockName(splittedLine[1]);
        }
        transition.setPredicateKey(predicate);

        transitionsSet.add(transition);

        finiteStateMachine.addOutGoingTransition(transition);
    }

    private EFSMState constructState(char stateName){
        //COMPLETED (2) : separate the construct state logic to this method
        EFSMState efsmState = new EFSMState(String.valueOf(stateName));
        stateSet.add(efsmState);
        finiteStateMachine.addState(efsmState);

        return efsmState;
    }

    private void getBranch(List<String> lines,Pattern pattern) {
        // Get the number letter number format(ex : 2a1) in variation and extension
        for(int i = 0; i < lines.size(); i++){
            Matcher matcher = pattern.matcher(lines.get(i));
            if(matcher.find()){
                String key = lines.get(i).split(" ")[0];
                String value = lines.get(i);
                //COMPLETED(3) : store the extension and variation
                branchMap.put(key,value);
            }
        }
    }

    private void createBranch(String indexExtension,boolean isInput,EFSMState previousState){
        boolean loopExtension = true;
        int indexNum = 1;
        int sequenceCharBranch = 97;
        char stateAlphabetIndex = (char) sequenceCharBranch;

        int notFound = 0;

        while(loopExtension){
            // 2a1 - 2a(n)
            if(branchMap.get(indexExtension) != null){
                String keyState = branchMap.get(indexExtension).split(" ")[1];
                EFSMState tempState = branchStateMap.get(keyState);
                EFSMState state;
                String predicate = branchMap.get(indexExtension).split(" ")[4];
                predicate = predicate.substring(0,predicate.length()-1);

                //COMPLETED (6) : abort state is only one state
                if(tempState != null){
                    state = branchStateMap.get(keyState);
                } else {
                    char branchStateName = (char) sequenceChar;
                    sequenceChar++;
                    state = constructState(branchStateName);
                    branchStateMap.put(keyState,state);
                }

                //COMPLETED (5) construct branch transition here
                constructTransition(branchMap.get(indexExtension),isInput,predicate,state,previousState);

                indexNum++;
                indexExtension = indexExtension.split("")[0]+stateAlphabetIndex+indexNum;
                if(notFound != 0)
                    notFound--;
                continue;
            }
            sequenceCharBranch++;
            indexNum = 1;
            stateAlphabetIndex = (char) sequenceCharBranch;
            indexExtension = indexExtension.split("")[0]+stateAlphabetIndex+indexNum;
            notFound++;

            if(notFound > 1) {
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        EFSMBuilder builder = new EFSMBuilder();
        File activityTableText = new File("out/activity-table.txt");
    }
}
