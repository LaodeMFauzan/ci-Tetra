package efsm;

import efsm.internal.FSMStateGraph;
import efsm.internal.FSMStateNode;
import efsm.internal.FSMTransitionEdge;
import efsm.model.FSMState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
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
        Stack<FSMStateNode> stateStack = new Stack<>();
        FSMStateGraph fsmStateGraph = finiteStateMachine.getFsmStateGraph();

        Map<String, FSMStateNode> stateNodes = fsmStateGraph.getStateNodes();
        stateStack.push(stateNodes.get("Start_State"));

        FSMState next = null;
        while(!stateStack.isEmpty()){
            Collection<FSMTransitionEdge> outgoingEdges = stateStack.pop().getOutgoingEdges();
             for(FSMTransitionEdge edges : outgoingEdges){
                System.out.println("Input Action : "+edges.getWrappedTransition().getActionName());
                next = edges.getWrappedTransition().getTo();
            }
            stateStack.push(stateNodes.get(next.getName()));
        }
        return this.testCaseList;
    }

    public static void main(String[] args) {
        TestCaseBuilder caseBuilder = new TestCaseBuilder(new EFSMBuilder());

    }

}
