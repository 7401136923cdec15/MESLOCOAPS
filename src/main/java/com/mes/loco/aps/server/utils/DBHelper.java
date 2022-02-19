package com.mes.loco.aps.server.utils;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mes.loco.aps.server.service.mesenum.DBEnumType;
import com.mes.loco.aps.server.service.utils.StringUtils;

public class DBHelper {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(DBHelper.class);

	private static DataSource MySqlDataSource = null;

	private static DataSource GetMySqlDataSource() {
		if (MySqlDataSource == null)
			MySqlDataSource = new ComboPooledDataSource("Mysql_dataSource");

		return MySqlDataSource;
	}

	private static DataSource AccessDataSource = null;

	private static DataSource GetAccessDataSource() {
		if (AccessDataSource == null)
			AccessDataSource = new ComboPooledDataSource("Access_dataSource");

		return AccessDataSource;
	}

	private static DataSource OracleDataSource = null;

	private static DataSource GetOracleDataSource() {
		if (OracleDataSource == null)
			OracleDataSource = new ComboPooledDataSource("Oracle_dataSource");

		return OracleDataSource;
	}

	private static DataSource SQLServerDataSource = null;

	private static DataSource GetSQLServerDataSource() {
		if (SQLServerDataSource == null)
			SQLServerDataSource = new ComboPooledDataSource("SQLServer_dataSource");

		return SQLServerDataSource;
	}

	private static DBEnumType DBType = DBEnumType
			.getEnumType(StringUtils.parseInt(Configuration.readConfigString("mes.server.sql.type", "config/config")));

	public static NamedParameterJdbcTemplate getTemplate() {

		NamedParameterJdbcTemplate jdbcTemplate = null;

		switch (DBType) {
		case Default:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetMySqlDataSource());
			break;
		case Access:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetAccessDataSource());
			break;
		case MySQL:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetMySqlDataSource());
			break;
		case Oracle:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetOracleDataSource());
			break;
		case SQLServer:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetSQLServerDataSource());
			break;
		default:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetMySqlDataSource());
			break;
		}
		return jdbcTemplate;
	}

	public static NamedParameterJdbcTemplate getTemplate(DBEnumType wDBEnumType) {
		NamedParameterJdbcTemplate jdbcTemplate = null;

		switch (wDBEnumType) {
		case Default:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetMySqlDataSource());
			break;
		case Access:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetAccessDataSource());
			break;
		case MySQL:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetMySqlDataSource());
			break;
		case Oracle:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetOracleDataSource());
			break;
		case SQLServer:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetSQLServerDataSource());
			break;
		default:
			jdbcTemplate = new NamedParameterJdbcTemplate(GetMySqlDataSource());
			break;
		}
		return jdbcTemplate;
	}

}
