package efsm;

import efsm.internal.FSMStateGraph;
import efsm.internal.FSMStateNode;
import efsm.internal.FSMTransitionEdge;
import efsm.model.FSMState;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

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
                      testCase = getActivityName(testCase,edges);
                        next = edges.getWrappedTransition().getTo();
                    }
                    prevTestCase = testCase;
                    stateStack.push(stateNodesMap.get(next.getName()));
                } else if(stateStack.peek().getOutgoingEdges().size() > 1){
                    testCase = getBranchTestCase(stateNodesMap,stateStack,testCaseList,prevTestCase);
                } else {
                    testCase += " end";
                    testCaseList.add(testCase);
                    stateStack.pop();
                }
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return this.testCaseList;
    }

    private String getBranchTestCase(Map<String, FSMStateNode> stateNodesMap,Stack<FSMStateNode> stateStack,Set<String> testCaseList,String testCase) {
        for(FSMTransitionEdge edges : stateStack.pop().getOutgoingEdges()){
            String branchTestcase = testCase;
            stateStack.push(stateNodesMap.get(edges.getWrappedTransition().getTo().getName()));
            branchTestcase = getActivityName(branchTestcase,edges);
            if(stateStack.peek().getOutgoingEdges().size() > 0){
                return getActivityName(testCase,edges);
            }
            branchTestcase += " end";
            testCaseList.add(branchTestcase);
            stateStack.pop();
        }
        return null;
    }

    private String getActivityName(String testCase,FSMTransitionEdge edges){
        if (edges.getWrappedTransition().getActionName() != null){
            testCase += edges.getWrappedTransition().getActionName()+"?->";
        } else {
            testCase += edges.getWrappedTransition().getBlockName()+"!->";
        }
        return testCase;
    }

    public static void main(String[] args) {
        TestCaseBuilder caseBuilder = new TestCaseBuilder(new EFSMBuilder());
        for (String testCase : caseBuilder.testCaseList){
            System.out.println(testCase);
        }
    }

}
