package efsm.model.impl;

import efsm.FSMStateMachine;
import efsm.model.FSMConcept;

import java.util.Properties;
import java.util.UUID;

/**
 * Created by Intellij IDEA
 * User : fauzan
 * Date : 26/12/18
 */

public class DefaultFSMConcept implements FSMConcept {

    private String id;

    private Properties properties;

    private FSMStateMachine stateMachine;

    public DefaultFSMConcept(){
        this(UUID.randomUUID().toString());
    }

    public DefaultFSMConcept(String id){
        this.id = id;
        properties = new Properties();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getProperty(String property) {
        if(property == null){
            throw new IllegalArgumentException("Property name cannot be null");
        }
        return properties.get(property);
    }

    @Override
    public void addProperty(String name, Object value) {
        if(name == null){
            throw new IllegalArgumentException("Property name cannot be null");
        }
        properties.put(name,value);
    }

    public void attachStateMachine(FSMStateMachine fsmStateMachine){
        this.stateMachine = fsmStateMachine;
    }
}
