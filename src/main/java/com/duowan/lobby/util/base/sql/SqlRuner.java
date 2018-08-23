package com.duowan.lobby.util.base.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * sql的执行类
 * 
 * @author tanjh 2013-6-20
 */
public class SqlRuner {
	private static Logger log = LoggerFactory.getLogger(SqlRuner.class);

	protected Connection connection;
	protected Statement statement;
	protected ResultSet resultSet;
	protected DataSource dataSource;
	protected Object args;

	public SqlRuner(DataSource dataSource, Object args) {
		super();
		this.dataSource = dataSource;
		this.args = args;
	}

	/**
	 * 注意里面用到 connection，statement，resultSet要指向此类的
	 */
	protected void execute() throws Exception {
		throw new RuntimeException("没有重载这个方法");
	}

	public final void runVoid() throws Exception {
		try {
			execute();
		} catch (Exception e) {
			throw e;
		} finally {
			cleanUp(statement, connection, resultSet, dataSource);
			statement = null;
			connection = null;
			resultSet = null;
		}
	}

	/**
	 * 注意里面用到 connection，statement，resultSet要指向此类的
	 */
	protected <T> T executeReturnObj(Class<T> t) throws Exception {
		throw new RuntimeException("没有重载这个方法");
	}

	public final <T> T runReturnObj(Class<T> t) throws Exception {
		try {
			return executeReturnObj(t);
		} catch (Exception e) {
			throw e;
		} finally {
			cleanUp(statement, connection, resultSet, dataSource);
			statement = null;
			connection = null;
			resultSet = null;
		}
	}

	/**
	 * 注意里面用到 connection，statement，resultSet要指向此类的
	 */
	protected <T> List<T> executeReturnList(Class<T> t) throws Exception {
		throw new RuntimeException("没有重载这个方法");
	}

	public final <T> List<T> runReturnList(Class<T> t) throws Exception {
		try {
			return runReturnList(t);
		} catch (Exception e) {
			throw e;
		} finally {
			cleanUp(statement, connection, resultSet, dataSource);
			statement = null;
			connection = null;
			resultSet = null;
		}
	}

	private void cleanUp(Statement st, Connection conn, ResultSet rs, DataSource dataSource) {
		try {
			if (st != null && st instanceof ParameterDisposer) {
				((ParameterDisposer) st).cleanupParameters();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		JdbcUtils.closeResultSet(rs);
		JdbcUtils.closeStatement(st);
		DataSourceUtils.releaseConnection(conn, dataSource);
	}

}
