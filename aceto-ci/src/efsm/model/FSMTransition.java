package efsm.model;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 26/12/18
 */

public interface FSMTransition {

    FSMState getFrom();

    FSMState getTo();

    Class<?> getTriggerEventClass();
}
