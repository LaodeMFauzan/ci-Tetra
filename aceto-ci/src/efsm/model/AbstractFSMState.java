package efsm.model;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 26/12/18
 */

public abstract class AbstractFSMState implements FSMState {

    protected String name;

    protected AbstractFSMState(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        AbstractFSMState that = (AbstractFSMState) o;

        return !name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Name = '").append(name);
        return sb.toString();
    }
}
