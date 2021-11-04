package bgu.spl.mics.application.passiveObjects;


import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private List<Ewok> members = new LinkedList<>() ;

    public Ewoks() {

    }

    private static class Ewoksholder {
        private static Ewoks instance = new Ewoks();
    }

    public static Ewoks getInstance() {
        return Ewoks.Ewoksholder.instance;
    }

    public void setMembers(int quantity) {
        for (int i = 1; i <= quantity; i++)
            members.add(new Ewok(i));
    }

    public synchronized void acquire(List<Integer> AttackTeam) throws InterruptedException {
        for (Integer s : AttackTeam) {
            for (Ewok e : members) {
                if (e.getSerialNumber() == s)
                    e.acquire();
            }
        }
    }

    public void release(List<Integer> AttackTeam) {
        for (Integer s : AttackTeam) {
            for (Ewok e : members) {
                if (e.getSerialNumber() == s)
                    e.release();
            }
        }
    }

}




