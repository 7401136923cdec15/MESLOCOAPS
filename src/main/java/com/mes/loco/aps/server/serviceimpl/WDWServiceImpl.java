package com.mes.loco.aps.server.serviceimpl;

import com.mes.loco.aps.server.service.WDWService;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.WDWServiceImpl;
import com.mes.loco.aps.server.utils.RemoteInvokeUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class WDWServiceImpl implements WDWService {
	private static Logger logger = LoggerFactory.getLogger(WDWServiceImpl.class);

	private static WDWService Instance;

	public static WDWService getInstance() {
		if (Instance == null)
			Instance = new WDWServiceImpl();
		return Instance;
	}

	public APIResult WDW_SpecialItemAll_Repair(BMSEmployee wAdminUser, int taskID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("SpecialTaskID", Integer.valueOf(taskID));

			String wUri = StringUtils.Format("api/RRO/SpecialItemAll?cadv_ao={0}&cade_po={1}",
					new Object[] { wAdminUser.LoginName, wAdminUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public APIResult RRO_QueryItemTaskList(BMSEmployee wLoginUser, int wCarType, int wLineID, int wPartID, int wLimit) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("CarType", Integer.valueOf(wCarType));
			wParms.put("LineID", Integer.valueOf(wLineID));
			wParms.put("PartID", Integer.valueOf(wPartID));
			wParms.put("Limit", Integer.valueOf(wLimit));

			String wUri = StringUtils.Format("api/RRO/QueryItemTaskList?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult WDW_BindCarToPlant(BMSEmployee wLoginUser, String wPartNo) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("PartNo", wPartNo);

			String wUri = StringUtils.Format("api/MTC/SavePartNo?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult RRO_QueryNotFinishItemList(BMSEmployee wLoginUser, int wOrderID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("OrderID", wOrderID);

			String wUri = StringUtils.Format("api/RRO/QueryItemByOrderID?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult RRO_QueryItemTaskList(BMSEmployee wLoginUser, List<Integer> wIDList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("IDList", wIDList);

			String wUri = StringUtils.Format("api/RRO/QueryItemTaskList?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MTC_QuerySectionInfoList(BMSEmployee wLoginUser, int wProductID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("ProductID", wProductID);

			String wUri = StringUtils.Format("api/MTCSectionInfo/SectionList?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MTC_QueryEmployeeAllWeb(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			int wProductID, int wStatus) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("StartTime", wStartTime);
			wParms.put("EndTime", wEndTime);
			wParms.put("ProductID", wProductID);
			wParms.put("Status", wStatus);

			String wUri = StringUtils.Format("api/MTC/EmployeeAllWeb?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
