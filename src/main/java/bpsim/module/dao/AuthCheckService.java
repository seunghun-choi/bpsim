package bpsim.module.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ibatis.sqlmap.client.SqlMapClient;

import bpsim.framework.dao.AbstractDao;

public class AuthCheckService extends AbstractDao implements BpsimCommon{
	
	public AuthCheckService(HttpServletRequest request)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("bpsim/config/servlet/applicationContext.xml");
		SqlMapClient client  = (SqlMapClient)context.getBean("sqlMapClientBase");
		setSqlMap(client);
	}	
	
	protected void setSqlMap(SqlMapClient sqlMapClient){
		setSqlMapClient(sqlMapClient);
	}
	
	public void insert(String queryId, Map args) throws SQLException {
		regist(queryId, args);
		
	}
	
	public void update(String update, Map args) throws SQLException {
		modify(update, args);
	}
	
	public void delete(String queryId, Map args) throws SQLException {
		remove(queryId, args);
	}	
	
	public int getListCount(String queryId, Map args) throws SQLException {
		return getCount(queryId, args);
	}
	
	public int getDataCnt(String queryId, Map args) throws SQLException {
		return getCount(queryId, args);
	}
	
	public Object getDataString(String queryId, Map args) throws SQLException {
		return getValue(queryId, args);
	}
	
	public List getDataList(String queryId, Map args) throws SQLException {
		return getList(queryId, args);
	}
	
	public List getList(String queryId, Map args, int startIndex, int recordPerPage) throws SQLException {
		return getPageList(queryId, args, startIndex, recordPerPage);
	}
	
	public List getList(String queryId, Map args) throws SQLException {
		return super.getList(queryId, args);
	}
	
	public Map getObjectMap(String queryId, Map args) throws SQLException {
		return getMap(queryId, args);
	}
	
	public String getXmlData(String queryId, Map args) throws SQLException {
		return getXml(queryId, args);
	}
	
	public Object getObjectString(String queryId, Map args) throws SQLException {
		return getValue(queryId, args);
	}
	
	public List getFileList(String queryId, Map args) throws SQLException {
		return super.getList(queryId, args);
	}
	
	public Map getFile(String queryId, Map args) throws SQLException {
		return getMap(queryId, args);
	}

	@Override
	public List<Map> getObjectMapRow(String queryId, Map params) throws SQLException {
		return null;
	}

	@Override
	public Map getAccessInfo(String queryId, Map params) throws SQLException {
		return null;
	}

	@Override
	public int insertData(String string, Map params) throws SQLException {
		return 0;
	}
}
