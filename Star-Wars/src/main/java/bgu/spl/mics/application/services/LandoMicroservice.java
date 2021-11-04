package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.RebelsWonBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;
    private Diary d;
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
        d = Diary.getInstance();
    }

    @Override
    protected void initialize()  {
        Callback<BombDestroyerEvent> bombDestroyerEventCallback = c -> {
            Thread.sleep(duration);
            sendBroadcast(new RebelsWonBroadcast());
        };
        subscribeEvent(BombDestroyerEvent.class, bombDestroyerEventCallback);
        Callback<RebelsWonBroadcast> rebelsWonBroadcastCallback = c -> {
            terminate();
            d.setLandoTerminate(System.currentTimeMillis());
        };
        subscribeBroadcast(RebelsWonBroadcast.class, rebelsWonBroadcastCallback);
    }
}
