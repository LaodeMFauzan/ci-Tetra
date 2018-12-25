package efsm.event;

import efsm.model.FSMEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public class FSMEventRegistry {

    public static final FSMEventRegistry INSTANCE = new FSMEventRegistry();

    private Set<String> eventTypes = new HashSet<>();

    private Collection<FSMEventListener> eventListeners = new ArrayList<>();

    private FSMEventRegistry(){}

    public boolean registerEventType(String eventType){
        if (eventType == null || eventType.isEmpty()){
            throw new IllegalArgumentException("Event type parameter cannot be null or empty");
        }
        return eventTypes.add(eventType);
    }

    public <L extends  FSMEventListener> void addEventListener(L eventListener){
        eventListeners.add(eventListener);
    }

    public <E extends FSMEvent<?>> void assertEvent(E fsmEvent) throws UnregisteredEventTypeException {
        if (!eventTypes.contains(fsmEvent.getType())){
            throw new UnregisteredEventTypeException("Event type " + fsmEvent.getType() + " not registered");
        }
        for (FSMEventListener eventListener : eventListeners){
            eventListener.onAssert(fsmEvent);
        }
    }

}
