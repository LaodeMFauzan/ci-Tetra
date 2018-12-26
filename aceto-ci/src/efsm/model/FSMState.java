package efsm.model;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public interface FSMState {

    String getName();

    void onEntry(FSMConcept concept, FSMEvent event);

    void onExit(FSMConcept concept, FSMEvent event);
}
