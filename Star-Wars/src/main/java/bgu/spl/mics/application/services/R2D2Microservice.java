package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.RebelsWonBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 */
public class R2D2Microservice extends MicroService {
    private long duration;
    private Diary d;
    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
        d = Diary.getInstance();
    }
    @Override
    protected void initialize()  {
        Callback<DeactivationEvent> deactivationEventCallback = c -> {
            Thread.sleep(duration);
            super.complete(c,true);
            d.setR2D2Deactivate(System.currentTimeMillis());
        };
        subscribeEvent(DeactivationEvent.class, deactivationEventCallback);
        Callback<RebelsWonBroadcast> rebelsWonBroadcastCallback = c -> {
            terminate();
            d.setR2D2Terminate(System.currentTimeMillis());
        };
        subscribeBroadcast(RebelsWonBroadcast.class, rebelsWonBroadcastCallback);
    }
}
