package bpsim.framework.util;

import org.json.JSONArray;

public class EmptyJsonArray extends JSONArray{
	public Object get(int index){
		return this;
	}
}
