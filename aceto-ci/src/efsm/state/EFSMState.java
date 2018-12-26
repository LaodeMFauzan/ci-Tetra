package efsm.state;

public interface EFSMState {
    public String getName();

    public void onEntry();

    public void onExit();
}