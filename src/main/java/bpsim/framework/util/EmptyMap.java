package bpsim.framework.util;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class EmptyMap implements Map {

	public int size() {
		return 0;
	}

	public boolean isEmpty() {
		return true;
	}

	public boolean containsKey(Object key) {
		return false;
	}

	public boolean containsValue(Object value) {
		return false;
	}

	public Object get(Object key) {
		return "";
	}

	public Object put(Object key, Object value) {
		return null;
	}

	public Object remove(Object key) {
		return null;
	}

	public void putAll(Map m) {
	}

	public void clear() {
	}

	public Set keySet() {
		return Collections.EMPTY_SET;
	}

	public Collection values() {
		return Collections.EMPTY_SET;
	}

	public Set entrySet() {
		return Collections.EMPTY_SET;
	}


}
