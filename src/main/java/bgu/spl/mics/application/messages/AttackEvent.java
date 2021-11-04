package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private Attack e;

    public AttackEvent (Attack e){
        this.e = e;
    }

    public Attack getAttack(){
        return e;
    }
}
