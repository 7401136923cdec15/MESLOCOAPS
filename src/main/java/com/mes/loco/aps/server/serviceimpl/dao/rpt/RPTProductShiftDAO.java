package com.mes.loco.aps.server.serviceimpl.dao.rpt;

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
import com.mes.loco.aps.server.service.po.rpt.RPTProductShift;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class RPTProductShiftDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(RPTProductShiftDAO.class);

	private static RPTProductShiftDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wRPTProductShift
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, RPTProductShift wRPTProductShift, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wRPTProductShift == null)
				return 0;

			String wSQL = "";
			if (wRPTProductShift.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.rpt_productshift(ShiftDate,ShiftID,LineID,Finsh,Enter,WeekFinsh,"
								+ "WeekEnter,MonthFinsh,MonthEnter,YearFinsh,YearEnter,RealPlant,RealRepair) "
								+ "VALUES(:ShiftDate,:ShiftID,:LineID,:Finsh,:Enter,:WeekFinsh,:WeekEnter,"
								+ ":MonthFinsh,:MonthEnter,:YearFinsh,:YearEnter,:RealPlant,:RealRepair);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format("UPDATE {0}.rpt_productshift SET ShiftDate = :ShiftDate,ShiftID = :ShiftID,"
						+ "LineID = :LineID,Finsh = :Finsh,Enter = :Enter,WeekFinsh = :WeekFinsh,"
						+ "WeekEnter = :WeekEnter,MonthFinsh = :MonthFinsh,MonthEnter = :MonthEnter,"
						+ "YearFinsh = :YearFinsh,YearEnter = :YearEnter,RealPlant = :RealPlant,"
						+ "RealRepair = :RealRepair WHERE ID = :ID;", wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wRPTProductShift.ID);
			wParamMap.put("ShiftDate", wRPTProductShift.ShiftDate);
			wParamMap.put("ShiftID", wRPTProductShift.ShiftID);
			wParamMap.put("LineID", wRPTProductShift.LineID);
			wParamMap.put("Finsh", wRPTProductShift.Finsh);
			wParamMap.put("Enter", wRPTProductShift.Enter);
			wParamMap.put("WeekFinsh", wRPTProductShift.WeekFinsh);
			wParamMap.put("WeekEnter", wRPTProductShift.WeekEnter);
			wParamMap.put("MonthFinsh", wRPTProductShift.MonthFinsh);
			wParamMap.put("MonthEnter", wRPTProductShift.MonthEnter);
			wParamMap.put("YearFinsh", wRPTProductShift.YearFinsh);
			wParamMap.put("YearEnter", wRPTProductShift.YearEnter);
			wParamMap.put("RealPlant", wRPTProductShift.RealPlant);
			wParamMap.put("RealRepair", wRPTProductShift.RealRepair);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wRPTProductShift.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wRPTProductShift.setID(wResult);
			} else {
				wResult = wRPTProductShift.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<RPTProductShift> wList,
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
			for (RPTProductShift wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.rpt_productshift WHERE ID IN({0}) ;",
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
	public RPTProductShift SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		RPTProductShift wResult = new RPTProductShift();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<RPTProductShift> wList = SelectList(wLoginUser, wID, -1, -1, wErrorCode);
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
	public List<RPTProductShift> SelectList(BMSEmployee wLoginUser, int wID, int wShiftID, int wLineID,
			OutResult<Integer> wErrorCode) {
		List<RPTProductShift> wResultList = new ArrayList<RPTProductShift>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.rpt_productshift WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wShiftID <= 0 or :wShiftID = ShiftID ) "
					+ "and ( :wLineID <= 0 or :wLineID = LineID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wLineID", wLineID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	public List<RPTProductShift> SelectList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			int wIsMonthLastDay, OutResult<Integer> wErrorCode) {
		List<RPTProductShift> wResultList = new ArrayList<RPTProductShift>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.rpt_productshift WHERE  1=1  "
					+ "and ( :wIsMonthLastDay < 0 or :wIsMonthLastDay = IsMonthLastDay ) "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= ShiftDate) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= ShiftDate);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wIsMonthLastDay", wIsMonthLastDay);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private void SetValue(List<RPTProductShift> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				RPTProductShift wItem = new RPTProductShift();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ShiftDate = StringUtils.parseCalendar(wReader.get("ShiftDate"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.Finsh = StringUtils.parseInt(wReader.get("Finsh"));
				wItem.Enter = StringUtils.parseInt(wReader.get("Enter"));
				wItem.WeekFinsh = StringUtils.parseInt(wReader.get("WeekFinsh"));
				wItem.WeekEnter = StringUtils.parseInt(wReader.get("WeekEnter"));
				wItem.MonthFinsh = StringUtils.parseInt(wReader.get("MonthFinsh"));
				wItem.MonthEnter = StringUtils.parseInt(wReader.get("MonthEnter"));
				wItem.YearFinsh = StringUtils.parseInt(wReader.get("YearFinsh"));
				wItem.YearEnter = StringUtils.parseInt(wReader.get("YearEnter"));
				wItem.RealPlant = StringUtils.parseInt(wReader.get("RealPlant"));
				wItem.RealRepair = StringUtils.parseInt(wReader.get("RealRepair"));
				wItem.AvgRepairPeriod = StringUtils.parseDouble(wReader.get("AvgRepairPeriod"));
				wItem.SumRepairPeriod = StringUtils.parseDouble(wReader.get("SumRepairPeriod"));
				wItem.AvgTelegraphPeriod = StringUtils.parseDouble(wReader.get("AvgTelegraphPeriod"));
				wItem.SumTelegraphPeriod = StringUtils.parseDouble(wReader.get("SumTelegraphPeriod"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 添加报表数据
	 * 
	 * @param wLoginUser 登录信息
	 * @param wLine      修程
	 * @param wStartTime 开始时刻
	 * @param wEndTime   结束时刻
	 * @param wErrorCode 错误码
	 */
	public int RPT_AddData(BMSEmployee wLoginUser, int wLineID, int wShiftID, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"INSERT INTO {0}.rpt_productshift (ShiftDate,ShiftID,LineID,Finsh,Enter,WeekFinsh,WeekEnter,MonthFinsh,"
							+ "MonthEnter,YearFinsh,YearEnter,RealPlant,RealRepair,AvgRepairPeriod,"
							+ "SumRepairPeriod,AvgTelegraphPeriod,SumTelegraphPeriod,"
							+ "AvgMonthRepairPeriod,AvgMonthTelegraphPeriod,IsMonthLastDay,MonthOut,YearOut)"
							+ "VALUES (:wStartTime, :wShiftID,:wLineID, "
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(5,6,7,8) and LineID=:wLineID AND RealFinishDate between  :wStartTime and :wEndTime), "
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(3,4,5,6,7,8) and LineID=:wLineID AND RealReceiveDate between  :wStartTime and :wEndTime ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(5,6,7,8) and LineID=:wLineID AND RealFinishDate between  date_sub(:wStartTime,INTERVAL WEEKDAY(:wStartTime) + 0 DAY) and date_sub(:wStartTime,INTERVAL WEEKDAY(:wStartTime) - 7 DAY)),  "
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(3,4,5,6,7,8) and LineID=:wLineID AND RealReceiveDate between  date_sub(:wStartTime,INTERVAL WEEKDAY(:wStartTime) + 0 DAY) and date_sub(:wStartTime,INTERVAL WEEKDAY(:wStartTime) - 7 DAY) ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(5,6,7,8) and LineID=:wLineID AND RealFinishDate between   date_add(:wStartTime,interval -day(:wStartTime)+1 day) and date_add(last_day(:wStartTime),interval +1 day)),  "
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(3,4,5,6,7,8) and LineID=:wLineID AND RealReceiveDate between   date_add(:wStartTime,interval -day(:wStartTime)+1 day) and date_add(last_day(:wStartTime),interval +1 day) ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(5,6,7,8) and LineID=:wLineID AND RealFinishDate between str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(3,4,5,6,7,8) and LineID=:wLineID AND RealReceiveDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'') ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where LineID=:wLineID AND Status IN(3,4,5,6,7) ) , "
							+ "(select COUNT(ID) FROM {0}.oms_order where LineID=:wLineID AND Status IN(3,4) ) , "
							+ "(select avg(datediff( RealFinishDate,RealReceiveDate)) as AvgRepairPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ "	AND LineID=:wLineID  AND  RealFinishDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')), "
							+ "(select sum(datediff( RealFinishDate,RealReceiveDate)) as SumRepairPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ "	AND LineID=:wLineID  AND  RealFinishDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')),  "
							+ "(select avg(datediff( RealFinishDate,TelegraphTime)) as AvgTelegraphPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ "	AND LineID=:wLineID  AND  RealFinishDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')), "
							+ "(select sum(datediff( RealFinishDate,TelegraphTime)) as SumTelegraphPeriod FROM {0}.oms_order where Status IN(5,6,7,8)  "
							+ " AND LineID=:wLineID AND   RealFinishDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')), "
							+ "(select avg(datediff( RealFinishDate,RealReceiveDate)) as AvgMonthRepairPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ "	AND LineID=:wLineID  AND  RealFinishDate between date_add(:wStartTime,interval -day(:wStartTime)+1 day) and date_add(last_day(:wStartTime),interval +1 day)),"
							+ "(select avg(datediff( RealFinishDate,TelegraphTime)) as AvgMonthTelegraphPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ "	AND LineID=:wLineID  AND  RealFinishDate between date_add(:wStartTime,interval -day(:wStartTime)+1 day) and date_add(last_day(:wStartTime),interval +1 day)),"
							+ "1,"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(8) and LineID=:wLineID AND RealReceiveDate between   date_add(:wStartTime,interval -day(:wStartTime)+1 day) and date_add(last_day(:wStartTime),interval +1 day) ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(8) and LineID=:wLineID AND RealReceiveDate between str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d''))"
							+ "); ",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wShiftID", wShiftID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据时间段统计年最大ShiftID
	 * 
	 * @param wLoginUser
	 * @param wStartTime
	 * @param wEndTime
	 * @param wErrorCode
	 * @return
	 */
	public List<Integer> SelectShiftIDList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = MessageFormat
					.format("select * from {0}.rpt_productshift t inner join " + "(select t2.ydate,max(t2.ID) as MaxID "
							+ "from (select date_format(t1.ShiftDate,''%Y'') as ydate,t1.* "
							+ "from {0}.rpt_productshift t1 order by ydate asc,t1.ShiftDate desc) t2 "
							+ "group by t2.ydate) t3 on t.ID=t3.MaxID "
							+ "where :wStartTime <= ShiftDate and ShiftDate<=:wEndTime;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				if (wShiftID > 0) {
					wResult.add(wShiftID);
				}
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 将某月的IsMonthLastDay更新为0
	 * 
	 * @param wRPTProductShift
	 * @return
	 */
	public void UpdateIsMonthLastDay(BMSEmployee wLoginUser, int wShiftID, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = MessageFormat.format(
					"UPDATE {0}.rpt_productshift " + "SET IsMonthLastDay=0 "
							+ "WHERE ShiftID LIKE ''%{1}%'' AND ShiftID < {2} AND ID > 0;",
					wInstance.Result, String.valueOf(wShiftID).substring(0, 6), String.valueOf(wShiftID));

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
	}

	private RPTProductShiftDAO() {
		super();
	}

	public static RPTProductShiftDAO getInstance() {
		if (Instance == null)
			Instance = new RPTProductShiftDAO();
		return Instance;
	}

}
