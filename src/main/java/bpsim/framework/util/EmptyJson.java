package bpsim.framework.util;

import org.json.JSONObject;

public class EmptyJson extends JSONObject{
	public String get(String key){
		return "";
	}
}
