package pluginlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler<T> {
		
	private final Map<Object, List<T>> map = new HashMap<Object, List<T>>();
	
	public void register(Object type, T event) {
		if (!map.containsKey(type)) {
			map.put(type, new ArrayList<T>());
		}
		
		List<T> list = map.get(type);
		
		list.add(event);
	}
	
	public void unregister(Object type, T event) {
		if (map.containsKey(type)) {
			map.get(type).remove(event);
		}
	}
	
	public List<T> getEvents(Object type) {
		return map.get(type);
	}
}
