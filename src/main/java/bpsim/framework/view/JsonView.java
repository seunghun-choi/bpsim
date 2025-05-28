package bpsim.framework.view;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

public class JsonView extends AbstractView {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
    public void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	try{
    	
    	response.setCharacterEncoding("utf-8");
    	response.setContentType("application/json");
    	Writer out = response.getWriter();
    	String json = renderModel(model);
    	out.write(json);
    	out.close();
    	} catch(Throwable t){
    		t.printStackTrace();
    	}
	}
    
    private String renderModel(Map model) throws Exception{
    	if(model==null)
    		throw new Exception("Model is null.  I require Map, List, String, or Date.");
    	if(model.get("list")!=null)
    		return renderList((List)model.get("list"));
    	return renderMap(model);
    }
    
    
    private String renderMap(Map m){
    	Set keyset = m.keySet();
    	String ret = "{";
    	for(Object key : keyset){
    		ret += "\""+key+"\":"+renderObject(m.get(key))+",";
    	}
    	
    	return ret.substring(0,ret.length()-1)+"}";
    }
    
    private String renderList(List l){
    	String ret = "[";
    	for(Object o : l){
    		ret += renderObject(o)+",";
    	}
    	if(ret.length()==1) ret += ",";
    	return ret.substring(0,ret.length()-1)+"]";
    }

    
    private String renderObject(Object o){
    	if(o instanceof Map){
    		return renderMap((Map)o);
    	}
    	if(o instanceof String){
    		return "\""+((String)o).replace("\\", "\\\\").replace("\"", "\\\"")+"\"";
    	}
    	if(o instanceof List){
    		return renderList((List)o);
    	}
    	return "\""+(""+o).replace("\\", "\\\\").replace("\"", "\\\"")+"\"";
    }

}
