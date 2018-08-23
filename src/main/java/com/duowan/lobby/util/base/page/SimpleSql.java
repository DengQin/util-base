package com.duowan.lobby.util.base.page;

public class SimpleSql {
	private String select = "";
	private String from = "";
	private String where = "";
	private String order = "";

	public SimpleSql() {
		super();
	}

	public SimpleSql(String sql) {
		int from_index = sql.indexOf(" from ");
		select = sql.substring(0, from_index);
		int where_index = sql.indexOf(" where ", from_index);
		int order_index = sql.indexOf(" order ", from_index);
		if (where_index > 0) {
			from = sql.substring(from_index, where_index);
			if (order_index >= 0) {
				where = sql.substring(where_index, order_index);
				order = sql.substring(order_index);
			} else {
				where = sql.substring(where_index);
			}
		} else if (order_index > 0) {
			from = sql.substring(from_index, order_index);
			order = sql.substring(order_index);
		} else {
			from = sql.substring(from_index);
		}
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
