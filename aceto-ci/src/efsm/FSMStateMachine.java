package efsm;

import efsm.internal.FSMStateGraph;
import efsm.model.FSMState;
import efsm.model.FSMTransition;
import efsm.model.impl.FSMEndState;
import efsm.model.impl.FSMStartState;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 26/12/18
 */

public interface FSMStateMachine {

    FSMStartState getStartState();

    FSMEndState getEndState();

    <S extends FSMState> FSMStateMachine addState(S state);

    <S extends FSMState> FSMStateMachine addStates(S... states);

    <T extends FSMTransition> FSMStateMachine addOutGoingTransition(T transition);

    <T extends FSMTransition> FSMStateMachine addOutGoingTransitions(T... transitions);

    FSMState getCurrentState();

    FSMStateGraph getFsmStateGraph();
}
