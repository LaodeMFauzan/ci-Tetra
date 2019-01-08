package efsm;

import efsm.model.FSMConcept;
import efsm.model.FSMState;
import efsm.model.extension.EFSMState;
import efsm.model.extension.EFSMTransition;
import efsm.model.impl.DefaultFSMConcept;

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
    private Set<String> predicatesSet;
    private Set<EFSMTransition> transitionsSet;

    private HashMap<String,String> branchMap;

    FSMStateMachine finiteStateMachine;

    public EFSMBuilder() {
        initializeEFSM();
    }

    private void initializeEFSM(){
        stateSet = new LinkedHashSet<>();
        inputSet = new LinkedHashSet<>();
        outputSet = new LinkedHashSet<>();
        predicatesSet = new LinkedHashSet<>();
        transitionsSet = new LinkedHashSet<>();
        branchMap = new HashMap<>();

        //Precondition for every use case
        predicatesSet.add("cond0");
        //stateSet.add(new EFSMState("Start_State"));

        //Initialize
        FSMConcept concept = new DefaultFSMConcept();
        finiteStateMachine = new DefaultFSMStateMachine(concept);
    }

    private  void setupEFSM(File file) throws IOException {
        getBranch(file);
        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        EFSMState previousState = null;
        String getPredicate = null;
        int sequenceChar = 65;
        boolean isHaveBranch = false;

        //COMPLETED(1) : read every line and get state of the efsm
        for(int i = 0; i < lines.size(); i++){
            String getNumberLetterFormat = "\\d[a-zA-Z](\\d)";
            Pattern pattern = Pattern.compile(getNumberLetterFormat);
            Matcher matcher = pattern.matcher(lines.get(i));

            if(matcher.find()){
                break;
            }

            if (i == 0){
                // get the start state -- think about it
                previousState = new EFSMState("Start_State");
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
            predicatesSet.add(getPredicate);

            // Use the alphabet to name a state
            char stateName = (char) sequenceChar;
            EFSMState efsmState = constructState(stateName);
            sequenceChar++;

            //Input set and Output set
            boolean isInput =  isHaveInput(lines.get(i));

            //TODO(4) : do branching ?
            //Key to lookup for branch
            String indexExtension = lines.get(i).split(" ")[0]+"a1";
            if(branchMap.get(indexExtension) != null ){
                createBranch(indexExtension,getPredicate,previousState);
            }

            //Transition set
            constructTransition(lines.get(i),isInput,getPredicate,efsmState,previousState);

            //Get the previous state
            previousState = efsmState;
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
    }

    private EFSMState constructState(char stateName){
        //COMPLETED (2) : separate the construct state logic to this method
        EFSMState efsmState = new EFSMState(String.valueOf(stateName));
        stateSet.add(efsmState);
        finiteStateMachine.addState(efsmState);

        return efsmState;
    }

    private void getBranch(File file) throws IOException {
        // Get the number letter number format(ex : 2a1) in variation and extension
        String getNumberLetterFormat = "\\d[a-zA-Z](\\d)";
        Pattern pattern = Pattern.compile(getNumberLetterFormat);

        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

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

    private void createBranch(String indexExtension,String predicate,EFSMState previousState){
        boolean loopExtension = true;
        int indexNum = 1;
        int sequenceChar = 97;
        char stateName = (char) sequenceChar;

        while(loopExtension){
            if(branchMap.get(indexExtension) != null){
                String activity = branchMap.get(indexExtension).split(" ")[1];
                //TODO (5) construct transition here

                sequenceChar++;
                indexNum++;
                indexExtension = indexExtension.split("")[0]+stateName+indexNum;
            } else {
                loopExtension = false;
            }
        }
        boolean loopVariation = true;
        while (loopVariation){

        }
    }

    public static void main(String[] args) throws IOException {
        EFSMBuilder builder = new EFSMBuilder();
        File activityTableText = new File("out/activity-table.txt");

        builder.setupEFSM(activityTableText);
    }
}