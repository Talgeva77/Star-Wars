package bgu.spl.mics.application.passiveObjects;


/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 */
public class Ewok {
	int serialNumber;
	boolean available;

	public Ewok(int serialNumber){
	    this.serialNumber = serialNumber;
	    available = true;
    }
  
    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        available = false;
    }

    /**
     * release an Ewok
     */
    public synchronized void release() {
        available = true;
        notifyAll();
    }
    public int getSerialNumber () {return serialNumber;}

}
