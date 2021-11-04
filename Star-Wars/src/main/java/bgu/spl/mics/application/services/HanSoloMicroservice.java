package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.RebelsWonBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import java.util.List;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvents}.
 */
public class HanSoloMicroservice extends MicroService {
    private Ewoks ewoks;
    private Diary d;
    public HanSoloMicroservice() {
        super("Han");
        ewoks = Ewoks.getInstance();
        d = Diary.getInstance();
    }

    @Override
    protected void initialize()  {
        Callback<AttackEvent> attackEventCallback = c -> {
            d.incrementTotalAttacks();
            int duration = c.getAttack().getDuration();
            List<Integer> SerialEwoks = c.getAttack().getSerials();
            ewoks.acquire(SerialEwoks);
            Thread.sleep(duration);
            ewoks.release(SerialEwoks);
            super.complete(c,true);
            d.setHanSoloFinish(System.currentTimeMillis());
        };
        subscribeEvent(AttackEvent.class, attackEventCallback);
        Callback<RebelsWonBroadcast> rebelsWonBroadcastCallback = c -> {
            terminate();
            d.setHanSoloTerminate(System.currentTimeMillis());
        };
        subscribeBroadcast(RebelsWonBroadcast.class, rebelsWonBroadcastCallback);
    }
}
