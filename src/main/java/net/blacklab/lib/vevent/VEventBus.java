package net.blacklab.lib.vevent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class VEventBus {
	
	private Map<Class<? extends IVEvent>, Map<Object, Method>> listenerMethods;
	
	public static VEventBus instance = new VEventBus();

	private VEventBus() {
		listenerMethods = new HashMap<Class<? extends IVEvent>, Map<Object,Method>>();
	}
	
	public void registerListener(Object object) {
		// Register LMRE Event Listener
		for (Method method: object.getClass().getMethods()) {
			if (method.isAnnotationPresent(SubscribeVEvent.class)) {
				Class[] paramType = method.getParameterTypes();
				if (paramType.length == 1 && IVEvent.class.isAssignableFrom(paramType[0])) {
					register(paramType[0], object, method);
				}
			}
		}
	}
	
	private void register(Class<? extends IVEvent> eventClass, Object listenerObj, Method method) {
		if (!listenerMethods.containsKey(eventClass)) {
			listenerMethods.put(eventClass, new HashMap<Object,Method>());
		}
		Map<Object,Method> nMap = listenerMethods.get(eventClass);
		if (nMap.containsKey(listenerObj)) {
			throw new IllegalStateException(String.format("Duplicate of IVEvent handler method: %s.%s", listenerObj, method));
		}
		nMap.put(listenerObj, method);
	}
	
	/**
	 * Dispatch event. Returns true if the event is canceled.
	 * @param event
	 * @return
	 */
	public boolean post(IVEvent event) {
		Map<Object,Method> nMap = listenerMethods.get(event.getClass());
		if (nMap != null) {
			for (Entry<Object,Method> entry: nMap.entrySet()) {
				try {
					entry.getValue().invoke(entry.getKey(), event);
					return event instanceof IVEventCancelable ? ((IVEventCancelable) event).isCanceled() : false;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
