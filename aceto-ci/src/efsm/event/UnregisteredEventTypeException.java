package efsm.event;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public class UnregisteredEventTypeException extends Exception {

    public UnregisteredEventTypeException(String s) {
        super(s);
    }
}
