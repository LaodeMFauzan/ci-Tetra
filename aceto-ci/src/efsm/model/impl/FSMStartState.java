package efsm.model.impl;

import efsm.model.AbstractFSMState;
import efsm.model.FSMConcept;
import efsm.model.FSMEvent;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 26/12/18
 */

public final class FSMStartState extends AbstractFSMState {

    public FSMStartState() {
        super("_Start_State_");
    }

    @Override
    public void onEntry(FSMConcept concept, FSMEvent event) {

    }

    @Override
    public void onExit(FSMConcept concept, FSMEvent event) {

    }
}
