package bgu.spl.mics;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.RebelsWonBroadcast;
import bgu.spl.mics.application.services.R2D2Microservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    MicroService m1;
    MicroService m2;
    MessageBus MB = MessageBusImpl.getInstance();
    Event e;
    Broadcast b;

    @BeforeEach
    void setUp() {
        e = new DeactivationEvent();
        b = new RebelsWonBroadcast();
        m1 = new R2D2Microservice(60);
        m2 = new R2D2Microservice(600);
    }

    @AfterEach
    void tearDown() {
        MB.unregister(m1);
        MB.unregister(m2);
    }

    @Test
    void subscribeEvent() throws InterruptedException {
        MB.register(m1);
        MB.subscribeEvent(DeactivationEvent.class,m1);
        MB.sendEvent(e);
        Message mess1 = MB.awaitMessage(m1);
        assertNotNull(mess1);
    }

    @Test
    void subscribeBroadcast() throws InterruptedException {
        MB.register(m1);
        MB.subscribeBroadcast(b.getClass(),m1);
        MB.sendBroadcast(b);
        Message mess1 = MB.awaitMessage(m1);
        assertNotNull(mess1);
    }

    @Test
    void complete() throws InterruptedException {
        MB.register(m1);
        MB.subscribeEvent(DeactivationEvent.class, m1);
        Future <String> f = MB.sendEvent(e);
        assertFalse(f.isDone());
        MB.awaitMessage(m1);
        MB.complete(e,f);
        assertTrue(f.isDone());
    }

    @Test
    void sendBroadcast() throws InterruptedException {
        MB.register(m1);
        MB.register(m2);
        MB.subscribeBroadcast(b.getClass(),m1);
        MB.subscribeBroadcast(b.getClass(),m2);
        MB.sendBroadcast(b);
        Message mm1 =MB.awaitMessage(m1);
        Message mm2 =MB.awaitMessage(m2);
        assertNotNull(mm1);
        assertEquals(mm1,mm2);

    }

    @Test
    void sendEvent() throws InterruptedException {
        MB.register(m1);
        MB.subscribeBroadcast(b.getClass(),m1);
        MB.sendBroadcast(b);
        Message mess1 = MB.awaitMessage(m1);
        assertNotNull(mess1);
    }

    @Test
    void awaitMessage()throws InterruptedException {
        MB.register(m1);
        MB.subscribeBroadcast(b.getClass(),m1);
        MB.sendBroadcast(b);
        Message mes =MB.awaitMessage(m1);
        assertTrue(mes.equals(b));
    }
}