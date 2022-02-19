package com.mes.loco.aps.server.serviceimpl.dao.sch;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.sch.SCHSecondment;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sch.SCHSecondmentDAO;
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

public class SCHSecondmentDAO extends BaseDAO {
	private static Logger logger = LoggerFactory.getLogger(SCHSecondmentDAO.class);

	private static SCHSecondmentDAO Instance = null;

	public int Update(BMSEmployee wLoginUser, SCHSecondment wSCHSecondment, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wSCHSecondment == null) {
				return 0;
			}
			String wSQL = "";
			if (wSCHSecondment.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.sch_secondment(SendID,SendTime,SecondDepartmentID,IsOverArea,AreaID,SecondAuditID,SendAuditTime,BeSecondAuditID,BeSecondAuditTime,SecondPersonID,BeSecondDepartmentID,Status,ValidDate,IsExclude,ApplyValidDate) VALUES(:SendID,:SendTime,:SecondDepartmentID,:IsOverArea,:AreaID,:SecondAuditID,:SendAuditTime,:BeSecondAuditID,:BeSecondAuditTime,:SecondPersonID,:BeSecondDepartmentID,:Status,:ValidDate,:IsExclude,:ApplyValidDate);",
						new Object[] {

								wInstance.Result });
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.sch_secondment SET SendID = :SendID,SendTime = :SendTime,SecondDepartmentID = :SecondDepartmentID,IsOverArea = :IsOverArea,AreaID=:AreaID,SecondAuditID = :SecondAuditID,SendAuditTime = :SendAuditTime,BeSecondAuditID = :BeSecondAuditID,BeSecondAuditTime = :BeSecondAuditTime,SecondPersonID = :SecondPersonID,BeSecondDepartmentID=:BeSecondDepartmentID,Status = :Status,ValidDate=:ValidDate,IsExclude=:IsExclude,ApplyValidDate=:ApplyValidDate  WHERE ID = :ID;",
						new Object[] {

								wInstance.Result });
			}
			wSQL = DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("ID", Integer.valueOf(wSCHSecondment.ID));
			wParamMap.put("SendID", Integer.valueOf(wSCHSecondment.SendID));
			wParamMap.put("SendTime", wSCHSecondment.SendTime);
			wParamMap.put("SecondDepartmentID", Integer.valueOf(wSCHSecondment.SecondDepartmentID));
			wParamMap.put("IsOverArea", Integer.valueOf(wSCHSecondment.IsOverArea ? 1 : 0));
			wParamMap.put("AreaID", Integer.valueOf(wSCHSecondment.AreaID));
			wParamMap.put("SecondAuditID", Integer.valueOf(wSCHSecondment.SecondAuditID));
			wParamMap.put("SendAuditTime", wSCHSecondment.SendAuditTime);
			wParamMap.put("BeSecondAuditID", Integer.valueOf(wSCHSecondment.BeSecondAuditID));
			wParamMap.put("BeSecondAuditTime", wSCHSecondment.BeSecondAuditTime);
			wParamMap.put("SecondPersonID", Integer.valueOf(wSCHSecondment.SecondPersonID));
			wParamMap.put("BeSecondDepartmentID", Integer.valueOf(wSCHSecondment.BeSecondDepartmentID));
			wParamMap.put("Status", Integer.valueOf(wSCHSecondment.Status));
			wParamMap.put("ValidDate", wSCHSecondment.ValidDate);
			wParamMap.put("IsExclude", Integer.valueOf(wSCHSecondment.IsExclude));
			wParamMap.put("ApplyValidDate", wSCHSecondment.ApplyValidDate);

			GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(wParamMap);

			this.nameJdbcTemplate.update(wSQL, (SqlParameterSource) mapSqlParameterSource,
					(KeyHolder) generatedKeyHolder);

			if (wSCHSecondment.getID() <= 0) {
				wResult = generatedKeyHolder.getKey().intValue();
				wSCHSecondment.setID(wResult);
			} else {
				wResult = wSCHSecondment.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<SCHSecondment> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}
			List<String> wIDList = new ArrayList<>();
			for (SCHSecondment wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.sch_secondment WHERE ID IN({0}) ;",
					StringUtils.Join(",", wIDList), wInstance.Result);
			ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public SCHSecondment SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		SCHSecondment wResult = new SCHSecondment();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			List<SCHSecondment> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, -1, -1, null, null, null,
					wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<SCHSecondment> SelectList(BMSEmployee wLoginUser, int wID, int wSendID, int wSecondDepartmentID,
			int wSecondAuditID, int wBeSecondAuditID, int wSecondPersonID, int wBeSecondDepartmentID,
			List<Integer> wStateIDList, Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SCHSecondment> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<>();
			}
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0)
				wEndTime = wBaseTime;
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sch_secondment WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wSendID <= 0 or :wSendID = SendID ) "
					+ "and ( :wSecondDepartmentID <= 0 or :wSecondDepartmentID = SecondDepartmentID ) "
					+ "and ( :wSecondAuditID <= 0 or :wSecondAuditID = SecondAuditID ) "
					+ "and ( :wBeSecondAuditID <= 0 or :wBeSecondAuditID = BeSecondAuditID ) "
					+ "and ( :wSecondPersonID <= 0 or :wSecondPersonID = SecondPersonID ) "
					+ "and ( :wBeSecondDepartmentID <= 0 or :wBeSecondDepartmentID = BeSecondDepartmentID ) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= BeSecondAuditTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= SendTime) "
					+ "and ( :wStatus is null or :wStatus = '''' or Status in ({1}));", wInstance.Result,
					(wStateIDList.size() > 0) ? StringUtils.Join(",", wStateIDList) : "0");
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wID", Integer.valueOf(wID));
			wParamMap.put("wSendID", Integer.valueOf(wSendID));
			wParamMap.put("wSecondDepartmentID", Integer.valueOf(wSecondDepartmentID));
			wParamMap.put("wSecondAuditID", Integer.valueOf(wSecondAuditID));
			wParamMap.put("wBeSecondAuditID", Integer.valueOf(wBeSecondAuditID));
			wParamMap.put("wSecondPersonID", Integer.valueOf(wSecondPersonID));
			wParamMap.put("wBeSecondDepartmentID", Integer.valueOf(wBeSecondDepartmentID));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<SCHSecondment> SelectListByPersonIDList(BMSEmployee wLoginUser, List<Integer> wPersonIDList,
			OutResult<Integer> wErrorCode) {
		List<SCHSecondment> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wPersonIDList == null) {
				wPersonIDList = new ArrayList<>();
			}
			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.sch_secondment WHERE  1=1  and ( :wSecondPersonID is null or :wSecondPersonID = '''' or SecondPersonID in ({1}));",
					new Object[] {

							wInstance.Result,
							(wPersonIDList.size() > 0) ? StringUtils.Join(",", wPersonIDList) : "0" });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wSecondPersonID", StringUtils.Join(",", wPersonIDList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private void SetValue(List<SCHSecondment> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				SCHSecondment wItem = new SCHSecondment();

				wItem.ID = StringUtils.parseInt(wReader.get("ID")).intValue();
				wItem.SendID = StringUtils.parseInt(wReader.get("SendID")).intValue();
				wItem.SendTime = StringUtils.parseCalendar(wReader.get("SendTime"));
				wItem.SecondDepartmentID = StringUtils.parseInt(wReader.get("SecondDepartmentID")).intValue();
				wItem.IsOverArea = (StringUtils.parseInt(wReader.get("IsOverArea")).intValue() == 1);
				wItem.AreaID = StringUtils.parseInt(wReader.get("AreaID")).intValue();
				wItem.SecondAuditID = StringUtils.parseInt(wReader.get("SecondAuditID")).intValue();
				wItem.SendAuditTime = StringUtils.parseCalendar(wReader.get("SendAuditTime"));
				wItem.BeSecondAuditID = StringUtils.parseInt(wReader.get("BeSecondAuditID")).intValue();
				wItem.BeSecondAuditTime = StringUtils.parseCalendar(wReader.get("BeSecondAuditTime"));
				wItem.ValidDate = StringUtils.parseCalendar(wReader.get("ValidDate"));
				wItem.ApplyValidDate = StringUtils.parseCalendar(wReader.get("ApplyValidDate"));
				wItem.SecondPersonID = StringUtils.parseInt(wReader.get("SecondPersonID")).intValue();
				wItem.BeSecondDepartmentID = StringUtils.parseInt(wReader.get("BeSecondDepartmentID")).intValue();
				wItem.Status = StringUtils.parseInt(wReader.get("Status")).intValue();
				wItem.IsExclude = StringUtils.parseInt(wReader.get("IsExclude")).intValue();

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public static SCHSecondmentDAO getInstance() {
		if (Instance == null)
			Instance = new SCHSecondmentDAO();
		return Instance;
	}
}
