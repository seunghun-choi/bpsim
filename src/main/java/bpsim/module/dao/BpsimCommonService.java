package bpsim.module.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ibatis.sqlmap.client.SqlMapClient;

import bpsim.framework.dao.AbstractDao;

@Component("bpsimCommonService")
public class BpsimCommonService extends AbstractDao implements BpsimCommon{
	
	@Resource(name="sqlMapClientBase")
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
	
	public List<Map> getObjectMapRow(String queryId, Map args) throws SQLException {
		return getList(queryId, args);
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

	public Map getAccessInfo(String queryId, Map args) throws SQLException {
		return getMap(queryId, args);
	}
	
	public int insertData(String queryId, Map args) throws SQLException {
		return (int) regist( queryId,  args);
	}
}
