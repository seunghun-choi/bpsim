package bpsim.framework.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Dao {
	
	List getList(String qry, Map args) throws SQLException;
	
	List getPageList(String qry, Map args, int startIndex, int recordPerPage) throws SQLException;
	
	Map getMap(String qry, Map args) throws SQLException;
	
	int getCount(String qry, Map args) throws SQLException;
	
	Object getValue(String qry, Map args) throws SQLException;
	
	Object regist(String qry, Map args) throws SQLException;
	
	Object modify(String qry, Map args) throws SQLException;
	
	Object remove(String qry, Map args) throws SQLException;	
	
	String getXml(String qry, Map args) throws SQLException;	
	
	/**
	 * 트랜잭션의 이용 - com/a2m/module/업무/../model/*Service.java 에 적용
	 * 
	 * 1) 트랜잭션 어노테이션용 클래스 Import
	 * import org.springframework.transaction.annotation.Propagation;
	 * import org.springframework.transaction.annotation.Transactional;
	 * 
	 * 2) 트랜잭션을 사용할 메소드에 어노테이션 적용
	 * @Transactional(propagation=Propagation.REQUIRED)
	 */	

}
