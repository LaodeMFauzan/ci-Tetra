package efsm.tuple.state;

public class State implements EFSMState {
    private String name;

    public State(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void onEntry() {

    }

    @Override
    public void onExit() {

    }
}
