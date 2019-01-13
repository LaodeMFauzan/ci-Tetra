package efsm.internal;

import efsm.model.FSMEvent;
import efsm.model.FSMState;
import efsm.model.FSMTransition;
import efsm.model.impl.FSMEndState;
import efsm.model.impl.FSMStartState;

import java.util.*;

public class FSMStateGraph implements Iterable<FSMState> {

    private Map<String, FSMStateNode> stateNodes = new HashMap<>();

    public Map<String, FSMStateNode> getStateNodes() {
        return stateNodes;
    }

    public <S extends FSMState> boolean addState(S state){
        if (stateNodes.containsKey(state.getName())){
            return false;
        }
        FSMStateNode stateNode = new FSMStateNode(state);
        stateNodes.put(state.getName(), stateNode);
        return true;
    }

    public <T extends FSMTransition> boolean addOutGoingTransition(T transition){
        FSMState fromState = transition.getFrom();
        FSMState toState = transition.getTo();

        if (fromState instanceof FSMEndState || toState instanceof FSMStartState){
            return false;
        }

        FSMStateNode matchingFromStateNode = stateNodes.get(fromState.getName());
        FSMTransitionEdge transitionEdge = new FSMTransitionEdge(transition);

        return (matchingFromStateNode != null) && matchingFromStateNode.addOutgoingEdge(transitionEdge);
    }

    public FSMState getState(String name){
        return stateNodes.get(name).getWrappedState();
    }

    public FSMState getNextState(FSMState state, FSMEvent<?> fsmEvent){
        FSMStateNode matchingFromStateNode = stateNodes.get(state.getName());
        FSMState nextState = null;

        if (matchingFromStateNode != null){
            for (FSMTransitionEdge outGoingEdge : matchingFromStateNode.getOutgoingEdges()){
                if (outGoingEdge.matchesTriggerEventClass(fsmEvent.getClass())){
                    nextState = outGoingEdge.getToState();
                    break;
                }
            }
        }
        return nextState;
    }

    public int size(){
        return stateNodes.size();
    }

    @Override
    public Iterator<FSMState> iterator() {
        Collection<FSMStateNode> values = stateNodes.values();
        Collection<FSMState> states = new ArrayList<>(values.size());

        for (FSMStateNode stateNode : values) {
            states.add(stateNode.getWrappedState());
        }
        return states.iterator();
    }
}
