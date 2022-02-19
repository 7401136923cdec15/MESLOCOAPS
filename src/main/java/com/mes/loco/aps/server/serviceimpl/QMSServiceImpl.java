package com.mes.loco.aps.server.serviceimpl;

import com.mes.loco.aps.server.service.QMSService;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.ipt.IPTPreCheckProblem;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.QMSServiceImpl;
import com.mes.loco.aps.server.utils.RemoteInvokeUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class QMSServiceImpl implements QMSService {
	private static Logger logger = LoggerFactory.getLogger(QMSServiceImpl.class);

	private static QMSService Instance;

	public static QMSService getInstance() {
		if (Instance == null)
			Instance = new QMSServiceImpl();
		return Instance;
	}

	public APIResult RSM_QueryTurnOrderTaskList(BMSEmployee wLoginUser, int wOrderID, int wApplyStationID,
			int wTargetStationID, List<Integer> wStateIDList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("OrderID", Integer.valueOf(wOrderID));
			wParms.put("ApplyStationID", Integer.valueOf(wApplyStationID));
			wParms.put("TargetStationID", Integer.valueOf(wTargetStationID));
			wParms.put("StateIDList", wStateIDList);
			String wUri = StringUtils.Format("api/RSMTurnOrderTask/All?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public APIResult IPT_QueryPreCheckProblemByID(BMSEmployee wLoginUser, int wProblemID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("ID", Integer.valueOf(wProblemID));
			String wUri = StringUtils.Format("api/IPTPreCheckProblem/Info?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public APIResult IPT_UpdatePreCheckProblem(BMSEmployee wLoginUser, IPTPreCheckProblem wIPTPreCheckProblem) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("data", wIPTPreCheckProblem);
			String wUri = StringUtils.Format("api/IPTPreCheckProblem/Update?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public APIResult IPT_QueryPreCheckReportList(BMSEmployee wLoginUser, int wOrderID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("OrderID", Integer.valueOf(wOrderID));
			String wUri = StringUtils.Format("api/IPTPreCheckReport/All?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public APIResult SFC_QueryTaskIPTList(BMSEmployee wLoginUser, int wOrderID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("OrderID", Integer.valueOf(wOrderID));
			String wUri = StringUtils.Format("api/SFCTaskIPT/All?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public APIResult IPT_QueryValueList(BMSEmployee wLoginUser, int wSFCTaskIPTID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("TaskID", Integer.valueOf(wSFCTaskIPTID));
			String wUri = StringUtils.Format("api/IPTStandard/IPTValue?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public APIResult IPT_QueryStandardCurrent(BMSEmployee wLoginUser, int wITPMode, int wLineID, int wPartID,
			int wPartPointID, String wProductNo, int wCustomID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();

			wParms.put("ITPMode", wITPMode);
			wParms.put("LineID", wLineID);
			wParms.put("PartID", wPartID);
			wParms.put("PartPointID", wPartPointID);
			wParms.put("ProductNo", wProductNo);
			wParms.put("CustomID", wCustomID);

			String wUri = StringUtils.Format("api/IPTStandard/Current?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public APIResult IPT_QueryItemList(BMSEmployee wLoginUser, int wStandardID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();

			wParms.put("StandardID", wStandardID);

			String wUri = StringUtils.Format("api/IPTStandard/ItemListByStandard?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
	
	@Override
	public APIResult RSM_QueryBOPDoneList(BMSEmployee wLoginUser, int wOrderID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();

			wParms.put("OrderID", wOrderID);

			String wUri = StringUtils.Format("api/RSMTurnOrderTask/BOPDoneList?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
	
	@Override
	public APIResult SFC_QueryChangeOrderID(BMSEmployee wLoginUser, int wOrderID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();

			wParms.put("OrderID", wOrderID);

			String wUri = StringUtils.Format("api/SFCBogiesChangeBPM/QueryChangeOrderID?cadv_ao={0}&cade_po={1}",
					new Object[] { wLoginUser.LoginName, wLoginUser.Password });
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
