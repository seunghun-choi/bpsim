package bpsim.framework.util;

public class AttributeEncoder {
	public static String encodeAttribute(String s){
		return s.replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
	}
}
