package efsm;

import efsm.event.FSMEventListener;
import efsm.event.FSMEventRegistry;
import efsm.internal.FSMStateGraph;
import efsm.model.FSMConcept;
import efsm.model.FSMEvent;
import efsm.model.FSMState;
import efsm.model.FSMTransition;
import efsm.model.impl.FSMEndState;
import efsm.model.impl.FSMStartState;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 27/12/18
 */

public class DefaultFSMStateMachine implements FSMStateMachine,FSMEventListener{

    private FSMConcept concept;

    private FSMStateGraph stateGraph;

    private FSMState currentState;

    public DefaultFSMStateMachine(FSMConcept concept) {
        this.concept = concept;
        initialize();
    }

    void initialize(){
//        FSMStartState startState = new FSMStartState();
//        FSMEndState endState = new FSMEndState();
        stateGraph = new FSMStateGraph();
//        stateGraph.addState(startState);
//        stateGraph.addState(endState);
//
//        currentState = startState;

        FSMEventRegistry.INSTANCE.addEventListener(this);
    }


    @Override
    public <E extends FSMEvent<?>> void onAssert(E fsmEvent) {
        FSMState nextState = stateGraph.getNextState(currentState,fsmEvent);
        if (nextState != null) {
            FSMState previousState = currentState;
            //Make this next state current state
            currentState = nextState;
            //Execute exit action of previous state and entry of next state
            previousState.onExit(concept, fsmEvent);
            currentState.onEntry(concept,fsmEvent);
        }
    }

    @Override
    public FSMStartState getStartState() {
        return (FSMStartState) stateGraph.getState("_Start_State_");
    }

    @Override
    public FSMEndState getEndState() {
        return (FSMEndState) stateGraph.getState("_End_State_");
    }

    @Override
    public <S extends FSMState> FSMStateMachine addState(S state) {
        if (state == null){
            throw new IllegalArgumentException("State can't be null");
        }
        if (state.getName() == null){
            throw new IllegalArgumentException("State name can't be null");
        }
        stateGraph.addState(state);
        return this;
    }

    @Override
    public <S extends FSMState> FSMStateMachine addStates(S... states) {
        for (S state : states){
            addState(state);
        }
        return this;
    }

    @Override
    public <T extends FSMTransition> FSMStateMachine addOutGoingTransition(T transition) {
       if (transition == null){
           throw new IllegalArgumentException("Transition can't be null");
       }
       if (transition.getFrom() == null || transition.getTo() == null){
           throw new IllegalArgumentException("Transition cannot be from or to null");
       }
       stateGraph.addOutGoingTransition(transition);
       return this;
    }

    @Override
    public <T extends FSMTransition> FSMStateMachine addOutGoingTransitions(T... transitions) {
        for (T transition : transitions){
            addOutGoingTransition(transition);
        }
        return this;
    }

    @Override
    public FSMState getCurrentState() {
        return currentState;
    }
}
