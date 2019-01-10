package efsm.model;

import efsm.FSMStateMachine;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public interface FSMConcept {

    String getId();

    Object getProperty(String property);

    void addProperty(String name,Object value);

    public void attachStateMachine(FSMStateMachine stateMachine);
}
