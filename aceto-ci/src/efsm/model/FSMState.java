package efsm.model;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public interface FSMState {

    public String getName();

    public void onEntry(FSMConcept concept, FSMEvent event);

    public void onExit(FSMConcept concept, FSMEvent event);
}
