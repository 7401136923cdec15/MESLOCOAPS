package com.mes.loco.aps.server.serviceimpl.dao.rpt;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.rpt.RPTCustomerShift;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class RPTCustomerShiftDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(RPTCustomerShiftDAO.class);

	private static RPTCustomerShiftDAO Instance = null;

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<RPTCustomerShift> SelectList(BMSEmployee wLoginUser, int wID, int wShiftID, int wCustomerID,
			OutResult<Integer> wErrorCode) {
		List<RPTCustomerShift> wResultList = new ArrayList<RPTCustomerShift>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.rpt_customershift WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wShiftID <= 0 or :wShiftID = ShiftID ) "
					+ "and ( :wCustomerID <= 0 or :wCustomerID = CustomerID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wShiftID", wShiftID);
			wParamMap.put("wCustomerID", wCustomerID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private void SetValue(List<RPTCustomerShift> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				RPTCustomerShift wItem = new RPTCustomerShift();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ShiftDate = StringUtils.parseCalendar(wReader.get("ShiftDate"));
				wItem.ShiftID = StringUtils.parseInt(wReader.get("ShiftID"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
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

	public List<RPTCustomerShift> SelectList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<RPTCustomerShift> wResultList = new ArrayList<RPTCustomerShift>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.rpt_customershift WHERE  1=1  "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= ShiftDate) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= ShiftDate);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
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
	public int RPT_AddData(BMSEmployee wLoginUser, int wCustomerID, int wShiftID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"INSERT INTO {0}.rpt_customershift (ShiftDate,ShiftID,CustomerID,Finsh,Enter,WeekFinsh,WeekEnter,MonthFinsh,"
							+ "MonthEnter,YearFinsh,YearEnter,RealPlant,RealRepair,AvgRepairPeriod,SumRepairPeriod,AvgTelegraphPeriod,SumTelegraphPeriod)"
							+ "VALUES (:wStartTime, :wShiftID,:wCustomerID, "
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(5,6,7,8) and BureauSectionID=:wCustomerID AND RealFinishDate between  :wStartTime and :wEndTime), "
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(3,4,5,6,7,8) and BureauSectionID=:wCustomerID AND RealReceiveDate between  :wStartTime and :wEndTime ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(5,6,7,8) and BureauSectionID=:wCustomerID AND RealFinishDate between  date_sub(:wStartTime,INTERVAL WEEKDAY(:wStartTime) + 0 DAY) and date_sub(:wStartTime,INTERVAL WEEKDAY(:wStartTime) - 7 DAY)),  "
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(3,4,5,6,7,8) and BureauSectionID=:wCustomerID AND RealReceiveDate between  date_sub(:wStartTime,INTERVAL WEEKDAY(:wStartTime) + 0 DAY) and date_sub(:wStartTime,INTERVAL WEEKDAY(:wStartTime) - 7 DAY) ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(5,6,7,8) and BureauSectionID=:wCustomerID AND RealFinishDate between   date_add(:wStartTime,interval -day(:wStartTime)+1 day) and date_add(last_day(:wStartTime),interval +1 day)),  "
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(3,4,5,6,7,8) and BureauSectionID=:wCustomerID AND RealReceiveDate between   date_add(:wStartTime,interval -day(:wStartTime)+1 day) and date_add(last_day(:wStartTime),interval +1 day) ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(5,6,7,8) and BureauSectionID=:wCustomerID AND RealFinishDate between str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')),"
							+ "(select COUNT(ID) FROM {0}.oms_order where Status IN(3,4,5,6,7,8) and BureauSectionID=:wCustomerID AND RealReceiveDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'') ),"
							+ "(select COUNT(ID) FROM {0}.oms_order where BureauSectionID=:wCustomerID AND Status IN(3,4,5,6,7) ) , "
							+ "(select COUNT(ID) FROM {0}.oms_order where BureauSectionID=:wCustomerID AND Status IN(3,4) ) , "
							+ "(select avg(datediff( RealFinishDate,RealReceiveDate)) as AvgRepairPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ "	AND BureauSectionID=:wCustomerID AND   RealFinishDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')),"
							+ "(select sum(datediff( RealFinishDate,RealReceiveDate)) as SumRepairPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ "	AND BureauSectionID=:wCustomerID AND   RealFinishDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')),"
							+ "(select avg(datediff( RealFinishDate,TelegraphTime)) as AvgTelegraphPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ "	AND BureauSectionID=:wCustomerID AND   RealFinishDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d'')),"
							+ "(select sum(datediff( RealFinishDate,TelegraphTime)) as SumTelegraphPeriod FROM {0}.oms_order where Status IN(5,6,7,8) "
							+ " AND BureauSectionID=:wCustomerID AND   RealFinishDate between  str_to_date( concat(year(:wStartTime),''-01-01''),''%Y-%m-%d'') and str_to_date( concat(year(:wStartTime)+1,''-01-01''),''%Y-%m-%d''))); ",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wCustomerID", wCustomerID);
			wParamMap.put("wShiftID", wShiftID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private RPTCustomerShiftDAO() {
		super();
	}

	public static RPTCustomerShiftDAO getInstance() {
		if (Instance == null)
			Instance = new RPTCustomerShiftDAO();
		return Instance;
	}

}
