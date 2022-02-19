package com.mes.loco.aps.server.serviceimpl.dao.andon;

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
import com.mes.loco.aps.server.service.po.andon.AndonJournalItem;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class AndonJournalItemDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(AndonJournalItemDAO.class);

	private static AndonJournalItemDAO Instance = null;

	private AndonJournalItemDAO() {
		super();
	}

	public static AndonJournalItemDAO getInstance() {
		if (Instance == null)
			Instance = new AndonJournalItemDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wAndonJournalItem
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, AndonJournalItem wAndonJournalItem, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAndonJournalItem == null)
				return 0;

			String wSQL = "";
			if (wAndonJournalItem.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_andonjournalitem(ParentID,LineID,PartID,FQTYPlan,FQTYReal"
								+ ",FQTYMonthTask,FQTYDayPlan,FQTYDayComplete) VALUES(:ParentID,:LineID,:PartID,"
								+ ":FQTYPlan,:FQTYReal,:FQTYMonthTask,:FQTYDayPlan,:FQTYDayComplete);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format("UPDATE {0}.aps_andonjournalitem SET ParentID = :ParentID,LineID = :LineID,"
						+ "PartID = :PartID,FQTYPlan = :FQTYPlan,FQTYReal = :FQTYReal,FQTYMonthTask = :FQTYMonthTask,"
						+ "FQTYDayPlan = :FQTYDayPlan,FQTYDayComplete = :FQTYDayComplete WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAndonJournalItem.ID);
			wParamMap.put("ParentID", wAndonJournalItem.ParentID);
			wParamMap.put("LineID", wAndonJournalItem.LineID);
			wParamMap.put("PartID", wAndonJournalItem.PartID);
			wParamMap.put("FQTYPlan", wAndonJournalItem.FQTYPlan);
			wParamMap.put("FQTYReal", wAndonJournalItem.FQTYReal);
			wParamMap.put("FQTYMonthTask", wAndonJournalItem.FQTYMonthTask);
			wParamMap.put("FQTYDayPlan", wAndonJournalItem.FQTYDayPlan);
			wParamMap.put("FQTYDayComplete", wAndonJournalItem.FQTYDayComplete);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAndonJournalItem.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAndonJournalItem.setID(wResult);
			} else {
				wResult = wAndonJournalItem.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<AndonJournalItem> wList,
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
			for (AndonJournalItem wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_andonjournalitem WHERE ID IN({0}) ;",
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
	public AndonJournalItem SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		AndonJournalItem wResult = new AndonJournalItem();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<AndonJournalItem> wList = SelectList(wLoginUser, wID, -1, -1, wErrorCode);
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
	public List<AndonJournalItem> SelectList(BMSEmployee wLoginUser, int wID, int wParentID, int wLineID,
			OutResult<Integer> wErrorCode) {
		List<AndonJournalItem> wResultList = new ArrayList<AndonJournalItem>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.aps_andonjournalitem WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wParentID <= 0 or :wParentID = ParentID ) "
					+ "and ( :wLineID <= 0 or :wLineID = LineID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wParentID", wParentID);
			wParamMap.put("wLineID", wLineID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				AndonJournalItem wItem = new AndonJournalItem();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ParentID = StringUtils.parseInt(wReader.get("ParentID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.FQTYPlan = StringUtils.parseInt(wReader.get("FQTYPlan"));
				wItem.FQTYReal = StringUtils.parseInt(wReader.get("FQTYReal"));
				wItem.FQTYMonthTask = StringUtils.parseInt(wReader.get("FQTYMonthTask"));
				wItem.FQTYDayPlan = StringUtils.parseInt(wReader.get("FQTYDayPlan"));
				wItem.FQTYDayComplete = StringUtils.parseInt(wReader.get("FQTYDayComplete"));

				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 根据工位和修程获取子项
	 */
	public AndonJournalItem SelectItemByPartIDAndLineID(BMSEmployee wLoginUser, Integer wPartID, int wLineID,
			Calendar wMonthStartTime, Calendar wMonthEndTime, Calendar wNextMonthStartTime, Calendar wNextMonthEndTime,
			Calendar wTodayStartTime, Calendar wTodayEndTime, OutResult<Integer> wErrorCode) {
		AndonJournalItem wResult = new AndonJournalItem();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select "
					+ "	((select count(distinct OrderID) from {0}.aps_taskpart where ShiftPeriod=5 and Active=1 and Status !=5 and StartTime < :wMonthStartTime and LineID=:wLineID and PartID=:wPartID)+(select count(distinct OrderID) from {0}.aps_taskpart where ShiftPeriod=5 and Active=1 and StartTime > :wMonthStartTime and StartTime < :wMonthEndTime and LineID=:wLineID and PartID=:wPartID)) FQTYPlan,"
					+ "    ((select count(distinct OrderID) from {0}.aps_taskpart where ShiftPeriod=5 and Active=1 and Status !=5 and StartTime < :wMonthStartTime and LineID=:wLineID and PartID=:wPartID)+(select count(distinct OrderID) from {0}.aps_taskpart where ShiftPeriod=5 and Active=1 and StartTime > :wMonthStartTime and StartTime < :wMonthEndTime and LineID=:wLineID and PartID=:wPartID)) FQTYReal,"
					+ "    (select count(distinct OrderID) from {0}.aps_taskpart where ShiftPeriod=5 and Active=1 and Status=5 and  StartTime > :wNextMonthStartTime and StartTime < :wNextMonthEndTime and FinishWorkTime > :wMonthStartTime and FinishWorkTime < :wMonthEndTime and LineID=:wLineID and PartID=:wPartID) FQTYMonthTask,"
					+ "    ((select count(distinct OrderID) from {0}.aps_taskpart where ShiftPeriod=5 and Active=1 and Status!=5 and StartTime < :wTodayStartTime and LineID=:wLineID and PartID=:wPartID)+(select count(distinct OrderID) from {0}.aps_taskpart where ShiftPeriod=5 and Active=1 and StartTime > :wTodayStartTime and StartTime < :wTodayEndTime and LineID=:wLineID and PartID=:wPartID)) FQTYDayPlan,"
					+ "    (select count(distinct OrderID) from {0}.aps_taskpart where ShiftPeriod=5 and Active=1 and Status=5 and FinishWorkTime > :wTodayStartTime and FinishWorkTime < :wTodayEndTime and LineID=:wLineID and PartID=:wPartID) FQTYDayComplete;"
					+ "", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wMonthStartTime", wMonthStartTime);
			wParamMap.put("wMonthEndTime", wMonthEndTime);
			wParamMap.put("wNextMonthStartTime", wNextMonthStartTime);
			wParamMap.put("wNextMonthEndTime", wNextMonthEndTime);
			wParamMap.put("wTodayStartTime", wTodayStartTime);
			wParamMap.put("wTodayEndTime", wTodayEndTime);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wLineID", wLineID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.LineID = wLineID;
				wResult.PartID = wPartID;

				wResult.FQTYPlan = StringUtils.parseInt(wReader.get("FQTYPlan"));
				wResult.FQTYReal = StringUtils.parseInt(wReader.get("FQTYReal"));
				wResult.FQTYMonthTask = StringUtils.parseInt(wReader.get("FQTYMonthTask"));
				wResult.FQTYDayPlan = StringUtils.parseInt(wReader.get("FQTYDayPlan"));
				wResult.FQTYDayComplete = StringUtils.parseInt(wReader.get("FQTYDayComplete"));

				wResult.PartName = APSConstans.GetFPCPartName(wPartID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
