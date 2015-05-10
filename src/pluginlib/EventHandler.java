package pluginlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler {
	
	private static EventHandler instance;
	
	private final Map<Object, List<AbstractEvent>> map = new HashMap<Object, List<AbstractEvent>>();
	
	public static EventHandler getInstance() {
		return instance;
	}
	
	public void register(Object type, AbstractEvent event) {
		if (!map.containsKey(type)) {
			map.put(type, new ArrayList<AbstractEvent>());
		}
		
		List<AbstractEvent> list = map.get(type);
		
		list.add(event);
	}
	
	public void unregister(Object type, AbstractEvent event) {
		if (map.containsKey(type)) {
			map.get(type).remove(event);
		}
	}
	
	public List<AbstractEvent> getEvents(Object type) {
		return map.get(type);
	}
}
