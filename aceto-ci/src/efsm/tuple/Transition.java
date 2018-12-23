package efsm.tuple;

import efsm.tuple.state.State;

public class Transition {
    private State sourceState;
    private State destinationState;
    private String action;
    private String predicate;
    private String block;
}
