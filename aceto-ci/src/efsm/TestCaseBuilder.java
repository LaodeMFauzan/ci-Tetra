package efsm;

import efsm.internal.FSMStateGraph;
import efsm.model.FSMState;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 11/01/19
 */

public class TestCaseBuilder {

    private EFSMBuilder efsmObject;
    private ArrayList<String> testCaseList;
    private FSMStateMachine finiteStateMachine;

    public TestCaseBuilder(EFSMBuilder efsmObject) {
        this.efsmObject = efsmObject;
        this.finiteStateMachine = efsmObject.getFiniteStateMachine();

        generateTestCase();
    }

    private ArrayList<String> generateTestCase(){
        testCaseList = new ArrayList<>();
        Stack<FSMState> stateStack = new Stack<>();
        FSMStateGraph fsmStateGraph = finiteStateMachine.getFsmStateGraph();

        stateStack.push(fsmStateGraph.getState("Start_State"));



        return this.testCaseList;
    }

    public static void main(String[] args) {
        TestCaseBuilder caseBuilder = new TestCaseBuilder(new EFSMBuilder());

    }

}
