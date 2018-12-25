package efsm.model;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 25/12/18
 */

public interface FSMConcept {

    public String getId();

    public Object getProperty(String property);

    public void addProperty(String name,Object value);

}
