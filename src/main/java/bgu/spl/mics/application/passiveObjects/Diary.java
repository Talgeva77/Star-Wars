package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/** Passive data-object representing a Diary - in which the flow of the battle is recorded.
 */
public class Diary {
    AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;
    public Diary(){
        totalAttacks = new AtomicInteger(0);
    }
    private static class Diaryholder{
        private static Diary instance = new Diary();
    }
    public static  Diary getInstance(){
        return Diary.Diaryholder.instance;
    }
    public int getTotalAttacks(){ return totalAttacks.get();}
    public long getHanSoloFinish(){ return  HanSoloFinish;}
    public long getR2D2Deactivate(){return R2D2Deactivate;}
    public long getLeiaTerminate(){return LeiaTerminate;}
    public long getHanSoloTerminate(){return HanSoloTerminate;}
    public long getC3POFinish(){return C3POFinish;}
    public long getC3POTerminate(){return C3POTerminate;}
    public long getR2D2Terminate(){return R2D2Terminate;}
    public long getLandoTerminate(){return LandoTerminate;}
    public void setHanSoloFinish(long HanSoloFinish){this.HanSoloFinish=HanSoloFinish;}
    public void setC3POFinish(long C3POFinish){this.C3POFinish=C3POFinish;}
    public void setR2D2Deactivate(long R2D2Deactivate){this.R2D2Deactivate=R2D2Deactivate;}
    public void setLeiaTerminate(long LeiaTerminate){this.LeiaTerminate=LeiaTerminate;}
    public void setHanSoloTerminate(long HanSoloTerminate){this.HanSoloTerminate=HanSoloTerminate;}
    public void setC3POTerminate(long C3POTerminate){this.C3POTerminate=C3POTerminate;}
    public void setR2D2Terminate(long R2D2Terminate){this.R2D2Terminate=R2D2Terminate;}
    public void setLandoTerminate(long LandoTerminate){this.LandoTerminate=LandoTerminate;}
    public void incrementTotalAttacks(){totalAttacks.incrementAndGet();}



}
