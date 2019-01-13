package efsm.internal;

import efsm.model.FSMState;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public class FSMStateNode {

    private FSMState wrappedState;

    private Set<FSMTransitionEdge> outgoingTransitionEdges;

    FSMStateNode(FSMState wrappedState){
        this.wrappedState = wrappedState;
        this.outgoingTransitionEdges = new HashSet<>();
    }

    boolean addOutgoingEdge(FSMTransitionEdge fsmTransitionEdge){
//        for (FSMTransitionEdge outGoingTransitionEdge : outgoingTransitionEdges){
//            if (outGoingTransitionEdge.matchesTriggerEventClass(fsmTransitionEdge)){
//                throw new IllegalArgumentException("Transition with this event type already exist");
//            }
//        }
        return outgoingTransitionEdges.add(fsmTransitionEdge);
    }

    FSMState getWrappedState(){
        return wrappedState;
    }

    public Collection<FSMTransitionEdge> getOutgoingEdges(){
        return Collections.unmodifiableCollection(outgoingTransitionEdges);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        FSMStateNode that = (FSMStateNode) o;

        return !wrappedState.equals(that.wrappedState);
    }

    @Override
    public int hashCode() {
        return wrappedState.hashCode();
    }
}
