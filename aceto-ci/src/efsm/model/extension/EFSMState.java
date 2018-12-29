package efsm.model.extension;

import efsm.event.FSMEventRegistry;
import efsm.event.UnregisteredEventTypeException;
import efsm.model.AbstractFSMState;
import efsm.model.FSMConcept;
import efsm.model.FSMEvent;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 29/12/18
 */

public class EFSMState extends AbstractFSMState {

    private FSMEvent eventToTrigger;

    public EFSMState(String name) {
        super(name);
    }

    public EFSMState(String name, FSMEvent eventToTrigger) {
        super(name);
        this.eventToTrigger = eventToTrigger;
    }

    @Override
    public void onEntry(FSMConcept concept, FSMEvent event) {
        System.out.printf("Entry state executed for state [%s] caused by event [%s]", name, event.getSource());
        System.out.println();
        //Assert new event to get SM going
        try {
            FSMEventRegistry.INSTANCE.assertEvent(eventToTrigger);
        } catch (UnregisteredEventTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onExit(FSMConcept concept, FSMEvent event) {
        System.out.printf("Exit state executed for state [%s] caused by event [%s]", name, event.getSource());
        System.out.println();
    }
}
