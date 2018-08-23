package com.duowan.lobby.util.base.page;

import java.util.Map;

public class MysqlDialect implements Dialect {

	public String buildQueryPageSQL(String sql,Map<String, Object> paramMap,int start,int pageSize) {
		paramMap.put("start", start);
		paramMap.put("pageSize", pageSize);
		return sql + " limit :start,:pageSize ";
	}
}
