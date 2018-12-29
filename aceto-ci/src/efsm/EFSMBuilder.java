package efsm;

import efsm.model.FSMConcept;
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
    private ArrayList<String> inputSet,OutputSet;

    public static void setupEFSM(File file) throws IOException {
        FSMConcept concept = new DefaultFSMConcept();
        FSMStateMachine finiteStateMachine = new DefaultFSMStateMachine(concept);

        Path path = Paths.get(file.getPath());
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        //TODO(1) : read every line and get state of the efsm
        for(int i = 0; i < lines.size(); i++){

        }

    }
}
