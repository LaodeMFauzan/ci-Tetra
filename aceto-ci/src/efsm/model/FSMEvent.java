package efsm.model;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public interface FSMEvent<T> {

    T getSource();

    String getType();
}
