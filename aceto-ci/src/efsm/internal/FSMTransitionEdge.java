package efsm.internal;

import efsm.model.FSMState;
import efsm.model.FSMTransition;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public class FSMTransitionEdge {

    private FSMTransition wrappedTransition;

    public FSMTransitionEdge(FSMTransition wrappedTransition) {
        this.wrappedTransition = wrappedTransition;
    }

    public FSMTransition getWrappedTransition() {
        return wrappedTransition;
    }

    FSMState getFromState(){
        return wrappedTransition.getFrom();
    }

    FSMState getToState(){
        return wrappedTransition.getTo();
    }

    boolean matchesTriggerEventClass(Class<?> trigerEventClass){
        return wrappedTransition.getTriggerEventClass().equals(trigerEventClass);
    }

    boolean matchesTriggerEventClass(FSMTransitionEdge anotherfTransitionEdge){
        return wrappedTransition.getTriggerEventClass().equals(anotherfTransitionEdge.wrappedTransition.getTriggerEventClass());
    }
}
