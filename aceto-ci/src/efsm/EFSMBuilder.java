package efsm;

import efsm.model.FSMConcept;
import efsm.model.FSMState;
import efsm.model.extension.EFSMState;
import efsm.model.impl.DefaultFSMConcept;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 29/12/18
 */

public class EFSMBuilder {
    private ArrayList<String> inputSet,outputSet;
    private ArrayList<FSMState> stateSet;

    private void initializeEFSM(){
        stateSet = new ArrayList<>();
        inputSet = new ArrayList<>();
        outputSet = new ArrayList<>();
    }

    private  void setupEFSM(File file) throws IOException {
        initializeEFSM();
        FSMConcept concept = new DefaultFSMConcept();
        FSMStateMachine finiteStateMachine = new DefaultFSMStateMachine(concept);

        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);


        int sequenceChar = 65;
        int countAbort = 0;
        //TODO(1) : read every line and get state of the efsm
        for(int i = 0; i < lines.size(); i++){
            boolean isAbort = lines.get(i).split(" ")[1].equalsIgnoreCase("abort");
            if(countAbort > 0 && isAbort ){
                continue;
            }
            else if(isAbort){
                countAbort++;
            }
            char stateName = (char) sequenceChar; // Use the alphabet to name a state
            FSMState efsmState = new EFSMState(String.valueOf(stateName));
            stateSet.add(efsmState);
            finiteStateMachine.addState(efsmState);
            sequenceChar++;

            //Input Set
            boolean isInput =  lines.get(i).split(" ")[3].equalsIgnoreCase("System");
            if(isInput) {
                inputSet.add(lines.get(i).split(" ")[1]);
            } else {
                outputSet.add(lines.get(i).split(" ")[1]);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        EFSMBuilder builder = new EFSMBuilder();
        File activityTableText = new File("out/activity-table.txt");

        builder.setupEFSM(activityTableText);

    }
}
