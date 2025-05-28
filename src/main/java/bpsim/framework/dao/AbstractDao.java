package bpsim.framework.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.event.RowHandler;

import bpsim.framework.util.XmlRowHandler;

public class AbstractDao extends SqlMapClientDaoSupport implements Dao{
	
	public List getList(String qry, Map args) throws SQLException {
		return getSqlMapClientTemplate().queryForList(qry, args);
	}
	
	public List getPageList(String qry, Map args, int startIndex, int recordPerPage) throws SQLException {
		return getSqlMapClientTemplate().queryForList(qry, args, startIndex, recordPerPage);
	}

	public int getCount(String qry, Map args) throws SQLException {
		return (Integer)getSqlMapClientTemplate().queryForObject(qry, args);
	}	
	
	public Map getMap(String qry, Map args) throws SQLException {
		return (Map)getSqlMapClientTemplate().queryForObject(qry, args);
	}

	public Object getValue(String qry, Map args) throws SQLException {
		return getSqlMapClientTemplate().queryForObject(qry, args);
	}

	public Object regist(String qry, Map args) throws SQLException {
		return getSqlMapClientTemplate().insert(qry, args);
	}

	public Object modify(String qry, Map args) throws SQLException {
		return getSqlMapClientTemplate().update(qry, args);
	}

	public Object remove(String qry, Map args) throws SQLException {
		return getSqlMapClientTemplate().delete(qry, args);
	}
	
	public String getXml(String queryId, Map args) throws SQLException {
		String dataList	= null;
		try {
			RowHandler xmlRowHandle = new XmlRowHandler("response"); 
			getSqlMapClientTemplate().queryWithRowHandler(queryId, args, xmlRowHandle);
			dataList = ((XmlRowHandler)xmlRowHandle).toString();
		}
		catch(Exception e) {}
		
		return dataList;
	}
	
}
