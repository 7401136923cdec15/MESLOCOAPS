package com.mes.loco.aps.server.serviceimpl.dao.mcs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.mcs.MCSLogInfo;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class MCSLogInfoDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(MCSLogInfoDAO.class);

	private static MCSLogInfoDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wMCSLogInfo
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, MCSLogInfo wMCSLogInfo, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wMCSLogInfo == null)
				return 0;

			String wSQL = "";
			if (wMCSLogInfo.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.mcs_loginfo(CustomerName,LineName,ProductNo,PartNo,VersionNo,FileName,FilePath,FileType,CreateTime,CreateTimeStr) VALUES(:CustomerName,:LineName,:ProductNo,:PartNo,:VersionNo,:FileName,:FilePath,:FileType,:CreateTime,:CreateTimeStr);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.mcs_loginfo SET CustomerName = :CustomerName,LineName = :LineName,ProductNo = :ProductNo,PartNo = :PartNo,VersionNo = :VersionNo,FileName = :FileName,FilePath = :FilePath,FileType = :FileType,CreateTime = :CreateTime,CreateTimeStr = :CreateTimeStr WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wMCSLogInfo.ID);
			wParamMap.put("CustomerName", wMCSLogInfo.CustomerName);
			wParamMap.put("LineName", wMCSLogInfo.LineName);
			wParamMap.put("ProductNo", wMCSLogInfo.ProductNo);
			wParamMap.put("PartNo", wMCSLogInfo.PartNo);
			wParamMap.put("VersionNo", wMCSLogInfo.VersionNo);
			wParamMap.put("FileName", wMCSLogInfo.FileName);
			wParamMap.put("FilePath", wMCSLogInfo.FilePath);
			wParamMap.put("FileType", wMCSLogInfo.FileType);
			wParamMap.put("CreateTime", wMCSLogInfo.CreateTime);
			wParamMap.put("CreateTimeStr", wMCSLogInfo.CreateTimeStr);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wMCSLogInfo.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wMCSLogInfo.setID(wResult);
			} else {
				wResult = wMCSLogInfo.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除集合
	 * 
	 * @param wList
	 */
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<MCSLogInfo> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (MCSLogInfo wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.mcs_loginfo WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查单条
	 * 
	 * @return
	 */
	public MCSLogInfo SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		MCSLogInfo wResult = new MCSLogInfo();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<MCSLogInfo> wList = SelectList(wLoginUser, wID, "", "", "", "", "", null, null, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<MCSLogInfo> SelectList(BMSEmployee wLoginUser, int wID, String wCustomerName, String wLineName,
			String wProductNo, String wFileName, String wFileType, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<MCSLogInfo> wResultList = new ArrayList<MCSLogInfo>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.mcs_loginfo WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) "
					+ "and ( :wCustomerName is null or :wCustomerName = '''' or :wCustomerName = CustomerName ) "
					+ "and ( :wLineName is null or :wLineName = '''' or :wLineName = LineName ) "
					+ "and ( :wProductNo is null or :wProductNo = '''' or :wProductNo = ProductNo ) "
					+ "and ( :wFileName is null or :wFileName = '''' or :wFileName = FileName ) "
					+ "and ( :wFileType is null or :wFileType = '''' or :wFileType = FileType ) "
					+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  CreateTime ) "
					+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  CreateTime );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCustomerName", wCustomerName);
			wParamMap.put("wLineName", wLineName);
			wParamMap.put("wProductNo", wProductNo);
			wParamMap.put("wFileName", wFileName);
			wParamMap.put("wFileType", wFileType);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				MCSLogInfo wItem = new MCSLogInfo();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.CustomerName = StringUtils.parseString(wReader.get("CustomerName"));
				wItem.LineName = StringUtils.parseString(wReader.get("LineName"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.VersionNo = StringUtils.parseString(wReader.get("VersionNo"));
				wItem.FileName = StringUtils.parseString(wReader.get("FileName"));
				wItem.FilePath = StringUtils.parseString(wReader.get("FilePath"));
				wItem.FileType = StringUtils.parseString(wReader.get("FileType"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.CreateTimeStr = StringUtils.parseString(wReader.get("CreateTimeStr"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private MCSLogInfoDAO() {
		super();
	}

	public static MCSLogInfoDAO getInstance() {
		if (Instance == null)
			Instance = new MCSLogInfoDAO();
		return Instance;
	}

	/**
	 * 通过BOMID查询传输日志ID
	 */
	public int MCS_QueryIDByBOMID(BMSEmployee wLoginUser, int wBOMID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID FROM {0}.mcs_loginfo where find_in_set(''{1}'',BOMID);",
					wInstance.Result, String.valueOf(wBOMID));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("ID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 通过BOPID查询传输日志ID
	 */
	public Integer MCS_QueryIDByBOPID(BMSEmployee wLoginUser, int wBOPID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID FROM {0}.mcs_loginfo where BOPID=:BOPID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("BOPID", wBOPID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("ID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
