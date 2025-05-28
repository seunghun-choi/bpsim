package bpsim.module.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BpsimCommon {
	
	void insert(String queryId, Map args) throws SQLException;
	
	void update(String update, Map args) throws SQLException;
	
	void delete(String queryId, Map args) throws SQLException;
	
	int getListCount(String queryId, Map args) throws SQLException;
	
	int getDataCnt(String queryId, Map args) throws SQLException;
	
	Object getDataString(String queryId, Map args) throws SQLException;
	
	List getDataList(String queryId, Map params) throws SQLException;
	
	List getList(String queryId, Map params, int startIndex, int recordPerPage) throws SQLException;
	
	List getList(String queryId, Map params) throws SQLException;

	Map getObjectMap(String queryId, Map params) throws SQLException;
	
	List<Map> getObjectMapRow(String queryId, Map params) throws SQLException;
	
	String getXmlData(String queryId, Map params) throws SQLException;
	
	Object getObjectString(String queryId, Map params) throws SQLException;
	
	List getFileList(String queryId, Map params) throws SQLException;
	
	Map getFile(String queryId, Map params) throws SQLException;
	
	Map getAccessInfo(String queryId, Map params) throws SQLException;
		
	int insertData(String string, Map params) throws SQLException;
		
}
