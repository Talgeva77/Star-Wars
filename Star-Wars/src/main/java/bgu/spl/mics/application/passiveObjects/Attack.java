package bgu.spl.mics.application.passiveObjects;

import java.util.List;



public class Attack {
    final List<Integer> serials;
    final int duration;

    public Attack(List<Integer> serialNumbers, int duration) {
        this.serials = serialNumbers;
        this.duration = duration;
    }

    public int getDuration(){
        return duration;
    }
    public List<Integer> getSerials(){
        return serials;
    }
}
