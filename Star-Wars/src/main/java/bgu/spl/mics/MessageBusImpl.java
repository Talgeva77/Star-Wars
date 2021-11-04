package bgu.spl.mics;
import java.util.concurrent.*;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> HashMapMsgToMS; //Hash Map of Messages with Queue of MicroServices in any of the values
	private ConcurrentHashMap< MicroService, LinkedBlockingQueue<Message>> HashMapMStoMsg;//Hash Map of MicroServices with Queue of Messages in any of the values
	private ConcurrentHashMap<Event, Future> HashMapEventToFuture;// Hash Map of events that includes all the future values of the events
	private static class MessageBusImplholder{//Creating Singleton
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl(){
		HashMapMsgToMS = new ConcurrentHashMap<>();
		HashMapMStoMsg = new ConcurrentHashMap<>();
		HashMapEventToFuture = new ConcurrentHashMap<>();
	}

	public static  MessageBusImpl getInstance(){
		return MessageBusImplholder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		HashMapMsgToMS.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		HashMapMsgToMS.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		HashMapMsgToMS.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		HashMapMsgToMS.get(type).add(m);
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		HashMapEventToFuture.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (HashMapMsgToMS.get(b.getClass()) != null){
			for (MicroService ms : HashMapMsgToMS.get(b.getClass())){//Sending the broadcast to message to all the MicroServices that Subscribed to this type of broadcast
				if (HashMapMStoMsg.get(ms) != null)
					HashMapMStoMsg.get(ms).add(b);
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		MicroService ms;
		if (!HashMapMsgToMS.containsKey(e.getClass()))//We check if the event is exist
			throw new IllegalArgumentException();
		synchronized (e.getClass()){// We dont want that an event will get the queue meanwhile the queue was changed by other thread
			if (HashMapMsgToMS.get(e.getClass()).isEmpty())//We check if someone subscribed himself to the event
				throw new IllegalArgumentException();
			ms = HashMapMsgToMS.get(e.getClass()).poll();
			HashMapMsgToMS.get(e.getClass()).add(ms);
		}
		synchronized (ms){// We dont want that a MicroService will get the queue meanwhile the queue was changed by other MicroService thread
			if(HashMapMStoMsg.keySet().contains(ms)) {
				Future <T> fut = new Future<>();
				HashMapEventToFuture.put(e,fut);
				HashMapMStoMsg.get(ms).add(e);
				return fut;
			}
		}
		return null;
	}

	@Override
	public void register(MicroService m) { HashMapMStoMsg.put(m, new LinkedBlockingQueue<>()); }

	@Override
	public void unregister(MicroService m) {
		HashMapMStoMsg.remove(m);
		for(Class<? extends Message> mes: HashMapMsgToMS.keySet()){
			if (HashMapMsgToMS.get(mes).contains(m))
				HashMapMsgToMS.get(mes).remove(m);
		}
	}

	@Override
	public Message awaitMessage(MicroService m) {
		Message message = null;
		try {
			message = HashMapMStoMsg.get(m).take();
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}
		return message;
	}
}
