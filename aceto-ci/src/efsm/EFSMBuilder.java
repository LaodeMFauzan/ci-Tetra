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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    FSMStateMachine finiteStateMachine;

    private void initializeEFSM(){
        stateSet = new LinkedHashSet<>();
        inputSet = new LinkedHashSet<>();
        outputSet = new LinkedHashSet<>();
        predicatesSet = new LinkedHashSet<>();
        transitionsSet = new LinkedHashSet<>();

        //Precondition for every use case
        predicatesSet.add("cond0");
        //stateSet.add(new EFSMState("Start_State"));

        //Initialize
        FSMConcept concept = new DefaultFSMConcept();
        finiteStateMachine = new DefaultFSMStateMachine(concept);
    }

    private  void setupEFSM(File file) throws IOException {
        initializeEFSM();

        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        EFSMState previousState = null;
        String getPredicate = null;
        int sequenceChar = 65;

        //COMPLETED(1) : read every line and get state of the efsm
        for(int i = 0; i < lines.size(); i++){
            if (i == 0){
                // get the start state -- think about it
                previousState = new EFSMState("Start_State");
                getPredicate = "cond0";
            } else {
                getPredicate = lines.get(i).split(" ")[4];

                if (!getPredicate.equals("null") )
                    getPredicate =  getPredicate.substring(0,getPredicate.length()-1);
                else
                    getPredicate = null;
            }

            // Use the alphabet to name a state
            char stateName = (char) sequenceChar;
            EFSMState efsmState = constructState(stateName);
            sequenceChar++;

            //Input set and Output set
            boolean isInput =  lines.get(i).split(" ")[3].equalsIgnoreCase("System");
            if(isInput) {
                inputSet.add(lines.get(i).split(" ")[1]);
            } else {
                outputSet.add(lines.get(i).split(" ")[1]);
            }

            //Predicate set
            predicatesSet.add(getPredicate);

            //Transition set
            constructTransition(lines.get(i),isInput,getPredicate,efsmState,previousState);

            //Get the previous state
            previousState = efsmState;
        }
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

    public static void main(String[] args) throws IOException {
        EFSMBuilder builder = new EFSMBuilder();
        File activityTableText = new File("out/activity-table.txt");

        builder.setupEFSM(activityTableText);

    }
}
