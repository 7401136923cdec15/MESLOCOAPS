package com.mes.loco.aps.server.serviceimpl.dao.rpt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPart;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPartTJ;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class RPTOrderPartDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(RPTOrderPartDAO.class);

	private static RPTOrderPartDAO Instance = null;

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<RPTOrderPart> SelectList(BMSEmployee wLoginUser, int wLineID, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<RPTOrderPart> wResultList = new ArrayList<RPTOrderPart>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wBaseTime;
			}

			String wSQL = StringUtils.Format(
					"SELECT t.OrderID,o.ProductID, t.PartNo,t.LineID, t.PartID, t1.FinishWorkTime as RealDate,t.EndTime as PlantDate,c.CustomerID, "
							+ " datediff(t1.FinishWorkTime,t.EndTime) as LaterDay  FROM  {0}.aps_taskpart t "
							+ " left join {0}.aps_taskpart t1 on t.OrderID=t1.OrderID AND t.PartID=t1.PartID AND t1.ShiftPeriod= :wWeekPlantPeriod  and t1.Active=1 "
							+ " inner join  {0}.oms_order o on t.OrderID=o.ID and  (( o.RealReceiveDate<=:wEndTime and o.RealFinishDate>:wStartTime and o.Status IN(5,6,7,8))"
							+ " or ( o.RealReceiveDate<=:wEndTime  and o.Status Not IN(5,6,7,8)))"
							+ " inner join {0}.oms_command  c  on c.ID=o.CommandID"
							+ " WHERE t.Active=1 and (:wLineID<=0 or t.LineID=:wLineID) and  t.ShiftPeriod= :wMonthPlantPeriod ; ",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wLineID", wLineID);

			wParamMap.put("wMonthPlantPeriod", APSShiftPeriod.Month.getValue());
			wParamMap.put("wWeekPlantPeriod", APSShiftPeriod.Week.getValue());

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				RPTOrderPart wItem = new RPTOrderPart();

				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.Line = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.CustomerName = APSConstans.GetCRMCustomerName(wItem.CustomerID);
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.PlantDate = StringUtils.parseCalendar(wReader.get("PlantDate"));
				wItem.RealDate = StringUtils.parseCalendar(wReader.get("RealDate"));
				wItem.LaterDay = StringUtils.parseInt(wReader.get("LaterDay"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public Map<Integer, List<RPTOrderPartTJ>> SelectList(BMSEmployee wLoginUser, int wYear,
			OutResult<Integer> wErrorCode) {
		Map<Integer, List<RPTOrderPartTJ>> wResultList = new HashMap<Integer, List<RPTOrderPartTJ>>();
		try {

			// 获取wYear 对应所有月份
			// KEY Start Value End
			Map<Calendar, Calendar> wDateSpan = new HashMap<Calendar, Calendar>();
			this.FillDateSpan(wDateSpan, wYear);

			List<RPTOrderPartTJ> wRPTOrderPartTJList = null;
			List<RPTOrderPart> wRPTOrderPartList = null;
			RPTOrderPartTJ wRPTOrderPartTJ = null;
			Map<Integer, List<RPTOrderPart>> wRPTOrderPartListMap = null;
			Map<Integer, List<RPTOrderPart>> wRPTOrderPartListMapPart = null;
			List<RPTOrderPart> wRPTOrderPartStream = null;
			for (Calendar wStartTime : wDateSpan.keySet()) {
				wRPTOrderPartList = this.SelectList(wLoginUser, 0, wStartTime, wDateSpan.get(wStartTime), wErrorCode);
				if (wRPTOrderPartList == null || wRPTOrderPartList.size() <= 0)
					continue;
				wRPTOrderPartTJList = new ArrayList<RPTOrderPartTJ>();

				wRPTOrderPartListMap = wRPTOrderPartList.stream().collect(Collectors.groupingBy(p -> p.LineID));

				for (int wLineID : wRPTOrderPartListMap.keySet()) {
					wRPTOrderPartListMapPart = wRPTOrderPartListMap.get(wLineID).stream()
							.collect(Collectors.groupingBy(p -> p.PartID));

					for (int wPartID : wRPTOrderPartListMapPart.keySet()) {

						wRPTOrderPartStream = wRPTOrderPartListMapPart.get(wPartID);

						wRPTOrderPartTJ = new RPTOrderPartTJ();

						wRPTOrderPartTJ.PlantCount = (int) wRPTOrderPartStream.stream()
								.filter(p -> p.PlantDate.compareTo(wStartTime) >= 0
										&& p.PlantDate.compareTo(wDateSpan.get(wStartTime)) < 0)
								.count();
						wRPTOrderPartTJ.FinshCount = (int) wRPTOrderPartStream.stream()
								.filter(p -> p.RealDate.compareTo(wStartTime) >= 0
										&& p.RealDate.compareTo(wDateSpan.get(wStartTime)) < 0)
								.count();
						wRPTOrderPartTJ.LaterCount = (int) wRPTOrderPartStream.stream()
								.filter(p -> p.PlantDate.compareTo(wStartTime) >= 0
										&& p.PlantDate.compareTo(wDateSpan.get(wStartTime)) < 0 && p.LaterDay > 0)
								.count();
						wRPTOrderPartTJ.LineID = wLineID;
						wRPTOrderPartTJ.LineName = APSConstans.GetFMCLineName(wRPTOrderPartTJ.LineID);
						wRPTOrderPartTJ.PartID = wPartID;
						wRPTOrderPartTJ.ShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wStartTime,
								APSShiftPeriod.Month, FMCShiftLevel.Default, 0); // 根据开始日期生产月ShiftID

						wRPTOrderPartTJList.add(wRPTOrderPartTJ);
					}
				}

				wResultList.put(wStartTime.get(Calendar.MONTH) + 1, wRPTOrderPartTJList);
			}

		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 填充一年的月份对应开始结束时刻
	 * 
	 * @param wDateSpan
	 * @param wYear
	 */
	private void FillDateSpan(Map<Calendar, Calendar> wDateSpan, int wYear) {
		try {
			if (wDateSpan == null) {
				return;
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-M");

			for (int i = 1; i <= 12; i++) {
				int wDays = getDaysOfMonth(wSDF.parse(StringUtils.Format("{0}-{1}", wYear, i)));
				Calendar wStartTime = Calendar.getInstance();
				wStartTime.set(wYear, i - 1, 1, 0, 0, 0);

				Calendar wEndTime = Calendar.getInstance();
				wEndTime.set(wYear, i - 1, wDays, 23, 59, 59);

				wDateSpan.put(wStartTime, wEndTime);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public static int getDaysOfMonth(Date wDate) {
		Calendar wCalendar = Calendar.getInstance();
		wCalendar.setTime(wDate);
		return wCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	private RPTOrderPartDAO() {
		super();
	}

	public static RPTOrderPartDAO getInstance() {
		if (Instance == null)
			Instance = new RPTOrderPartDAO();
		return Instance;
	}
}
