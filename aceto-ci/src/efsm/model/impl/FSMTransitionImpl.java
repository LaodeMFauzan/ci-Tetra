package efsm.model.impl;

import efsm.model.FSMState;
import efsm.model.FSMTransition;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 26/12/18
 */

public class FSMTransitionImpl implements FSMTransition {

    private FSMState fromState;

    private FSMState toState;

    private Class<?> triggerEventClass;

    @Override
    public FSMState getFrom() {
        return fromState;
    }

    @Override
    public FSMState getTo() {
        return toState;
    }

    @Override
    public Class<?> getTriggerEventClass() {
        return triggerEventClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FSMTransitionImpl that = (FSMTransitionImpl) o;

        return fromState.equals(that.fromState) && toState.equals(that.toState) && triggerEventClass.equals(that.triggerEventClass);
    }

    @Override
    public int hashCode() {
        int result = fromState.hashCode();
        result = 31 * result + toState.hashCode();
        result = 31 * triggerEventClass.hashCode();
        return result;
    }
}
