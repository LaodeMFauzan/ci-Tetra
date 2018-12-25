package efsm.event;

import efsm.model.FSMEvent;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public interface FSMEventListener {

    public <E extends FSMEvent<?>> void onAssert(E fsmEvent);
}
