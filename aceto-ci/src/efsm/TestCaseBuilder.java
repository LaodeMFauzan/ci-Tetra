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
        try{
            testCaseList = new ArrayList<>();
            Stack<FSMStateNode> stateStack = new Stack<>();
            FSMStateGraph fsmStateGraph = finiteStateMachine.getFsmStateGraph();

            Map<String, FSMStateNode> stateNodesMap = fsmStateGraph.getStateNodes();
            stateStack.push(stateNodesMap.get("Start_State"));

            FSMState next = null;
            String testCase = "Start ->";
            while(!stateStack.isEmpty()){
                if (stateStack.peek().getOutgoingEdges().size() == 1){
                    for(FSMTransitionEdge edges : stateStack.pop().getOutgoingEdges()){
                        if (edges.getWrappedTransition().getActionName() != null){
                            testCase += edges.getWrappedTransition().getActionName()+"?->";
                        } else {
                            testCase += edges.getWrappedTransition().getBlockName()+"!->";
                        }
                        next = edges.getWrappedTransition().getTo();
                    }
                    stateStack.push(stateNodesMap.get(next.getName()));
                } else if(stateStack.peek().getOutgoingEdges().size() > 1){
                    //getBranchTestCase(stateStack,testCaseList,testCase);
                    for(FSMTransitionEdge edges : stateStack.peek().getOutgoingEdges()){
                        stateStack.push(stateNodesMap.get(edges.getWrappedTransition().getTo().getName()));
                    }
                } else {
                    testCaseList.add(testCase);
                    stateStack.pop();
                }
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return this.testCaseList;
    }

    private void getBranchTestCase(Stack<FSMStateNode> stateStack,ArrayList<String> testCaseList,String testCase) {
        Collection<FSMTransitionEdge> outgoingEdges = stateStack.pop().getOutgoingEdges();

    }

    public static void main(String[] args) {
        TestCaseBuilder caseBuilder = new TestCaseBuilder(new EFSMBuilder());

    }

}
