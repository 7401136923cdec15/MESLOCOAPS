package com.mes.loco.aps.server.serviceimpl.dao.mtc;

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
import com.mes.loco.aps.server.service.po.mtc.MTCTask;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class MTCTaskDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(MTCTaskDAO.class);

	private static MTCTaskDAO Instance = null;

	private MTCTaskDAO() {
		super();
	}

	public static MTCTaskDAO getInstance() {
		if (Instance == null)
			Instance = new MTCTaskDAO();
		return Instance;
	}

	/**
	 * 条件查询集合
	 */
	public List<MTCTask> SelectList(BMSEmployee wLoginUser, List<Integer> wIDList, int wFlowType, int wFlowID,
			int wPlaceID, int wTargetID, int wUpFlowID, List<Integer> wStatusList, List<Integer> wNotStatusList,
			int wShiftID, int wOrderID, int wType, int wCarTypeID, String wCarNumber, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<MTCTask> wResult = new ArrayList<MTCTask>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null || wStartTime.compareTo(wBaseTime) < 0)
				wStartTime = wBaseTime;
			if (wEndTime == null || wEndTime.compareTo(wBaseTime) < 0)
				wEndTime = wBaseTime;
			if (wStartTime.compareTo(wEndTime) > 0)
				return wResult;

			if (wStatusList == null)
				wStatusList = new ArrayList<Integer>();
			if (wIDList == null)
				wIDList = new ArrayList<Integer>();
			if (wNotStatusList == null)
				wNotStatusList = new ArrayList<Integer>();
			if (wCarNumber == null)
				wCarNumber = "";

			wIDList.removeIf(p -> p <= 0);
			wStatusList.removeIf(p -> p < 0);

			String wSQL = StringUtils.Format("SELECT * FROM {0}.mtc_task WHERE 1=1 "
					+ " and ( :wFlowType <= 0 or :wFlowType = FlowType )"
					+ " and ( :wFlowID <= 0 or :wFlowID = FlowID )" + " and ( :wPlaceID <= 0 or :wPlaceID = PlaceID )"
					+ " and ( :wOrderID <= 0 or :wOrderID = OrderID )" + " and ( :wType <= 0 or :wType = Type )"
					+ " and ( :wTargetID <= 0 or :wTargetID = TargetID )"
					+ " and (:wUpFlowID <=0 or UpFlowID= :wUpFlowID)"
					+ " and (:wCarTypeID <=0 or CarTypeID= :wCarTypeID)"
					+ " and (:wCarNumber = null or :wCarNumber='''' or PartNo= :wCarNumber)"
					+ " and ( :wStatus = '''' or Status in ({1}) ) and ( :wNotStatus = '''' or Status not in ({2}))"
					+ " and (:wID is null or :wID = '''' or ID in ({3}))"
					+ " and (:wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or CreateTime>= :wStartTime)"
					+ " and (:wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or CreateTime<= :wEndTime)",
					wInstance.Result, wStatusList.size() > 0 ? StringUtils.Join(",", wStatusList) : "0",
					wNotStatusList.size() > 0 ? StringUtils.Join(",", wNotStatusList) : "0",
					wIDList.size() > 0 ? StringUtils.Join(",", wIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", StringUtils.Join(",", wIDList));
			wParamMap.put("wFlowType", wFlowType);
			wParamMap.put("wFlowID", wFlowID);
			wParamMap.put("wPlaceID", wPlaceID);
			wParamMap.put("wTargetID", wTargetID);
			wParamMap.put("wStatus", StringUtils.Join(",", wStatusList));
			wParamMap.put("wNotStatus", StringUtils.Join(",", wNotStatusList));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wUpFlowID", wUpFlowID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wType", wType);
			wParamMap.put("wCarTypeID", wCarTypeID);
			wParamMap.put("wCarNumber", wCarNumber);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				MTCTask wMTCTask = new MTCTask();

				wMTCTask.ID = StringUtils.parseInt(wReader.get("ID"));
				wMTCTask.FlowType = StringUtils.parseInt(wReader.get("FlowType"));
				wMTCTask.FlowID = StringUtils.parseInt(wReader.get("FlowID"));
				wMTCTask.Code = StringUtils.parseString(wReader.get("Code"));
				wMTCTask.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wMTCTask.PlaceID = StringUtils.parseInt(wReader.get("PlaceID"));
				wMTCTask.InformShift = StringUtils.parseInt(wReader.get("InformShift"));
				wMTCTask.TargetID = StringUtils.parseInt(wReader.get("TargetID"));
				wMTCTask.TargetStockID = StringUtils.parseInt(wReader.get("TargetStockID"));

				wMTCTask.TargetSID = StringUtils.parseInt(wReader.get("TargetSID"));
				wMTCTask.TargetSStockID = StringUtils.parseInt(wReader.get("TargetSStockID"));

				wMTCTask.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wMTCTask.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wMTCTask.UpFlowID = StringUtils.parseInt(wReader.get("UpFlowID"));
				wMTCTask.FollowerID = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("FollowerID")).split(",|;"));
				wMTCTask.Status = StringUtils.parseInt(wReader.get("Status"));
				wMTCTask.StatusText = StringUtils.parseString(wReader.get("StatusText"));
				wMTCTask.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wMTCTask.ExpectedTime = StringUtils.parseCalendar(wReader.get("ExpectedTime"));
				wMTCTask.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wMTCTask.Type = StringUtils.parseInt(wReader.get("Type"));
				wMTCTask.CarTypeID = StringUtils.parseInt(wReader.get("CarTypeID"));
				wMTCTask.DepartmentID = StringUtils.parseInt(wReader.get("DepartmentID"));
				wMTCTask.AreaID = StringUtils.parseString(wReader.get("AreaID"));
				wMTCTask.IsPreMove = StringUtils.parseInt(wReader.get("IsPreMove"));

				wMTCTask.CarType = APSConstans.GetFPCProductName(wMTCTask.CarTypeID);

				wResult.add(wMTCTask);
			}
			if (wResult.size() <= 0)
				return wResult;
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

}
