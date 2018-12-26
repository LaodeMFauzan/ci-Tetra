package efsm.model.impl;

import efsm.model.AbstractFSMState;
import efsm.model.FSMConcept;
import efsm.model.FSMEvent;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 26/12/18
 */

public final class FSMEndState extends AbstractFSMState {


    public FSMEndState() {
        super("_END_STATE_");
    }

    @Override
    public void onEntry(FSMConcept concept, FSMEvent event) {

    }

    @Override
    public void onExit(FSMConcept concept, FSMEvent event) {

    }
}
