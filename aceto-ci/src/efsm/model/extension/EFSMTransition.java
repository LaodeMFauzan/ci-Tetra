package efsm.model.extension;

import efsm.model.FSMState;
import efsm.model.FSMTransition;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 29/12/18
 */

public class EFSMTransition implements FSMTransition {

    private EFSMState sourceState;
    private EFSMState destinationState;
    private Class<?> triggerClassEvent;

    //Extension of FSM
    private String actionName;
    private String predicateKey;
    private String blockName;

    public EFSMTransition() {
    }

    public EFSMTransition(EFSMState sourceState, EFSMState destinationState) {
        this.sourceState = sourceState;
        this.destinationState = destinationState;
    }

    public EFSMTransition(EFSMState sourceState, EFSMState destinationState, String actionName, String predicateKey, String blockName) {
        this.sourceState = sourceState;
        this.destinationState = destinationState;
        this.actionName = actionName;
        this.predicateKey = predicateKey;
        this.blockName = blockName;
    }

    @Override
    public FSMState getFrom() {
        return sourceState;
    }

    @Override
    public FSMState getTo() {
        return destinationState;
    }

    @Override
    public Class<?> getTriggerEventClass() {
        return triggerClassEvent;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getPredicateKey() {
        return predicateKey;
    }

    public void setPredicateKey(String predicateKey) {
        this.predicateKey = predicateKey;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
}
