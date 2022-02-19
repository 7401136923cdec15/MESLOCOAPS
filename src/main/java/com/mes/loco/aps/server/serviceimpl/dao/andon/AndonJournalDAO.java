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
import com.mes.loco.aps.server.service.po.andon.AndonJournal;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class AndonJournalDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(AndonJournalDAO.class);

	private static AndonJournalDAO Instance = null;

	private AndonJournalDAO() {
		super();
	}

	public static AndonJournalDAO getInstance() {
		if (Instance == null)
			Instance = new AndonJournalDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wAndonJournal
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, AndonJournal wAndonJournal, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAndonJournal == null)
				return 0;

			String wSQL = "";
			if (wAndonJournal.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_andonjournal(CreateID,CreateTime,FQTYYearComplete_C5,FQTYMonthComplete_C5,FQTYPlant_C5,FQTYRepair_C5,FQTYYearComplete_C6,FQTYMonthComplete_C6,FQTYPlant_C6,FQTYRepair_C6,SignificantImpact) VALUES(:CreateID,:CreateTime,:FQTYYearComplete_C5,:FQTYMonthComplete_C5,:FQTYPlant_C5,:FQTYRepair_C5,:FQTYYearComplete_C6,:FQTYMonthComplete_C6,:FQTYPlant_C6,:FQTYRepair_C6,:SignificantImpact);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.aps_andonjournal SET CreateID = :CreateID,CreateTime = :CreateTime,FQTYYearComplete_C5 = :FQTYYearComplete_C5,FQTYMonthComplete_C5 = :FQTYMonthComplete_C5,FQTYPlant_C5 = :FQTYPlant_C5,FQTYRepair_C5 = :FQTYRepair_C5,FQTYYearComplete_C6 = :FQTYYearComplete_C6,FQTYMonthComplete_C6 = :FQTYMonthComplete_C6,FQTYPlant_C6 = :FQTYPlant_C6,FQTYRepair_C6 = :FQTYRepair_C6,SignificantImpact = :SignificantImpact WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAndonJournal.ID);
			wParamMap.put("CreateID", wAndonJournal.CreateID);
			wParamMap.put("CreateTime", wAndonJournal.CreateTime);
			wParamMap.put("FQTYYearComplete_C5", wAndonJournal.FQTYYearComplete_C5);
			wParamMap.put("FQTYMonthComplete_C5", wAndonJournal.FQTYMonthComplete_C5);
			wParamMap.put("FQTYPlant_C5", wAndonJournal.FQTYPlant_C5);
			wParamMap.put("FQTYRepair_C5", wAndonJournal.FQTYRepair_C5);
			wParamMap.put("FQTYYearComplete_C6", wAndonJournal.FQTYYearComplete_C6);
			wParamMap.put("FQTYMonthComplete_C6", wAndonJournal.FQTYMonthComplete_C6);
			wParamMap.put("FQTYPlant_C6", wAndonJournal.FQTYPlant_C6);
			wParamMap.put("FQTYRepair_C6", wAndonJournal.FQTYRepair_C6);
			wParamMap.put("SignificantImpact", wAndonJournal.SignificantImpact);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAndonJournal.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAndonJournal.setID(wResult);
			} else {
				wResult = wAndonJournal.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<AndonJournal> wList,
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
			for (AndonJournal wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_andonjournal WHERE ID IN({0}) ;",
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
	public AndonJournal SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		AndonJournal wResult = new AndonJournal();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<AndonJournal> wList = SelectList(wLoginUser, wID, -1, null, null, wErrorCode);
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
	public List<AndonJournal> SelectList(BMSEmployee wLoginUser, int wID, int wCreateID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<AndonJournal> wResultList = new ArrayList<AndonJournal>();
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

			String wSQL = MessageFormat.format("SELECT * FROM {0}.aps_andonjournal WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wCreateID <= 0 or :wCreateID = CreateID ) "
					+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  CreateTime ) "
					+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  CreateTime );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCreateID", wCreateID);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				AndonJournal wItem = new AndonJournal();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.FQTYYearComplete_C5 = StringUtils.parseInt(wReader.get("FQTYYearComplete_C5"));
				wItem.FQTYMonthComplete_C5 = StringUtils.parseInt(wReader.get("FQTYMonthComplete_C5"));
				wItem.FQTYPlant_C5 = StringUtils.parseInt(wReader.get("FQTYPlant_C5"));
				wItem.FQTYRepair_C5 = StringUtils.parseInt(wReader.get("FQTYRepair_C5"));
				wItem.FQTYYearComplete_C6 = StringUtils.parseInt(wReader.get("FQTYYearComplete_C6"));
				wItem.FQTYMonthComplete_C6 = StringUtils.parseInt(wReader.get("FQTYMonthComplete_C6"));
				wItem.FQTYPlant_C6 = StringUtils.parseInt(wReader.get("FQTYPlant_C6"));
				wItem.FQTYRepair_C6 = StringUtils.parseInt(wReader.get("FQTYRepair_C6"));
				wItem.SignificantImpact = StringUtils.parseString(wReader.get("SignificantImpact"));

				wItem.CreateName = APSConstans.GetBMSEmployeeName(wItem.CreateID);
				wItem.ItemList_C5 = AndonJournalItemDAO.getInstance().SelectList(wLoginUser, -1, wItem.ID, 1,
						wErrorCode);
				wItem.ItemList_C6 = AndonJournalItemDAO.getInstance().SelectList(wLoginUser, -1, wItem.ID, 2,
						wErrorCode);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 查询第一部分数据(报表日志)
	 */
	public AndonJournal QeuryFirstPart_Journal(BMSEmployee wLoginUser, Calendar wYearStartTime, Calendar wYearEndTime,
			Calendar wMonthStartTime, Calendar wMonthEndTime, OutResult<Integer> wErrorCode) {
		AndonJournal wResult = new AndonJournal();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format("select "
					+ "	(select count(*) from {0}.oms_order where LineID=1 and :wYearStartTime <= RealReceiveDate  and  RealReceiveDate <= :wYearEndTime and Status in (5,6,7,8)) FQTYYearComplete_C5,"
					+ "    (select count(*) from {0}.oms_order where LineID=1 and :wMonthStartTime <= RealFinishDate  and  RealFinishDate <= :wMonthEndTime and Status in (5,6,7,8)) FQTYMonthComplete_C5,"
					+ "    (select count(*) from {0}.oms_order where LineID=1 and Status in (3,4,5,6,7)) FQTYPlant_C5,"
					+ "    (select count(*) from {0}.oms_order where LineID=1 and Status in (4)) FQTYRepair_C5,"
					+ "    (select count(*) from {0}.oms_order where LineID=2 and :wYearStartTime <= RealReceiveDate  and  RealReceiveDate <= :wYearEndTime and Status in (5,6,7,8)) FQTYYearComplete_C6,"
					+ "    (select count(*) from {0}.oms_order where LineID=2 and :wMonthStartTime <= RealFinishDate  and  RealFinishDate <= :wMonthEndTime and Status in (5,6,7,8)) FQTYMonthComplete_C6,"
					+ "    (select count(*) from {0}.oms_order where LineID=2 and Status in (3,4,5,6,7)) FQTYPlant_C6,"
					+ "    (select count(*) from {0}.oms_order where LineID=2 and Status in (4)) FQTYRepair_C6;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wYearStartTime", wYearStartTime);
			wParamMap.put("wYearEndTime", wYearEndTime);
			wParamMap.put("wMonthStartTime", wMonthStartTime);
			wParamMap.put("wMonthEndTime", wMonthEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.FQTYYearComplete_C5 = StringUtils.parseInt(wReader.get("FQTYYearComplete_C5"));
				wResult.FQTYMonthComplete_C5 = StringUtils.parseInt(wReader.get("FQTYMonthComplete_C5"));
				wResult.FQTYPlant_C5 = StringUtils.parseInt(wReader.get("FQTYPlant_C5"));
				wResult.FQTYRepair_C5 = StringUtils.parseInt(wReader.get("FQTYRepair_C5"));
				wResult.FQTYYearComplete_C6 = StringUtils.parseInt(wReader.get("FQTYYearComplete_C6"));
				wResult.FQTYMonthComplete_C6 = StringUtils.parseInt(wReader.get("FQTYMonthComplete_C6"));
				wResult.FQTYPlant_C6 = StringUtils.parseInt(wReader.get("FQTYPlant_C6"));
				wResult.FQTYRepair_C6 = StringUtils.parseInt(wReader.get("FQTYRepair_C6"));
				wResult.SignificantImpact = StringUtils.parseString(wReader.get("SignificantImpact"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据 修程获取工位ID列表
	 */
	public List<Integer> QueryPartIDListByLineID(BMSEmployee wLoginUser, int wLineID, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select distinct PartID from {0}.aps_taskpart "
					+ "where LineID=:wLineID and ShiftPeriod=5 and Active=1;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wLineID", wLineID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				if (wPartID > 0) {
					wResult.add(wPartID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
