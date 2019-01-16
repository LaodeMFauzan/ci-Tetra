package efsm;

import efsm.internal.FSMStateGraph;
import efsm.internal.FSMStateNode;
import efsm.internal.FSMTransitionEdge;
import efsm.model.FSMState;

import java.util.*;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 11/01/19
 */

public class TestCaseBuilder {

    private EFSMBuilder efsmObject;
    private Set<String> testCaseList;
    private FSMStateMachine finiteStateMachine;

    public TestCaseBuilder(EFSMBuilder efsmObject) {
        this.efsmObject = efsmObject;
        this.finiteStateMachine = efsmObject.getFiniteStateMachine();

        generateTestCase();
    }

    private Set<String> generateTestCase(){
        try{
            testCaseList = new LinkedHashSet<>();
            Stack<FSMStateNode> stateStack = new Stack<>();
            FSMStateGraph fsmStateGraph = finiteStateMachine.getFsmStateGraph();

            Map<String, FSMStateNode> stateNodesMap = fsmStateGraph.getStateNodes();
            stateStack.push(stateNodesMap.get("Start_State"));

            FSMState next = null;
            String testCase = "Start ->";
            String prevTestCase = testCase;
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
                    prevTestCase = testCase;
                    stateStack.push(stateNodesMap.get(next.getName()));
                } else if(stateStack.peek().getOutgoingEdges().size() > 1){
                    //getBranchTestCase(stateStack,testCaseList,testCase);
                    // if n-1 break loop
                    int index = 0;
                    for(FSMTransitionEdge edges : stateStack.peek().getOutgoingEdges()){
                        testCase = prevTestCase;
                        index++;
                        stateStack.push(stateNodesMap.get(edges.getWrappedTransition().getTo().getName()));
                        if (edges.getWrappedTransition().getActionName() != null){
                            testCase += edges.getWrappedTransition().getActionName()+"?->";
                        } else {
                            testCase += edges.getWrappedTransition().getBlockName()+"!->";
                        }
                        if(index == stateStack.peek().getOutgoingEdges().size()-1){
                            break;
                        }
                        testCaseList.add(testCase); // this is the problem
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
