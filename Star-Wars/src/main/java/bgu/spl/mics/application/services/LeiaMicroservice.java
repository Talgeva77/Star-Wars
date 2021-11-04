package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.RebelsWonBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import sun.font.TrueTypeFont;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private int counterAttacks;
	private Diary d;
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		d = Diary.getInstance();
    }

    @Override
    protected void initialize() throws InterruptedException {
        Future [] futs = new Future[attacks.length];
        Future f = new Future();
        while (counterAttacks< attacks.length){
            try {
                futs[counterAttacks] = sendEvent(new AttackEvent((attacks[counterAttacks])));
                counterAttacks = counterAttacks + 1;
            }catch (IllegalArgumentException e){
                Thread.sleep(60);
            }
        }
        for(int j=0; j<futs.length; j++) {
            if (futs[j] != null)
                futs[j].get();
        }
        try {
             f = sendEvent(new DeactivationEvent());
        }catch (IllegalArgumentException e){
            Thread.sleep(60);
        }
        f.get();
        sendEvent(new BombDestroyerEvent());
        Callback<RebelsWonBroadcast> rebelsWonBroadcastCallback = c -> {
            terminate();
            d.setLeiaTerminate(System.currentTimeMillis());
        };
        subscribeBroadcast(RebelsWonBroadcast.class, rebelsWonBroadcastCallback);
    }
}
