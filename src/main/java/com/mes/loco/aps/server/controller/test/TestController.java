package com.mes.loco.aps.server.controller.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.mes.loco.aps.server.controller.BaseController;
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.mesenum.SFCOutSourceType;
import com.mes.loco.aps.server.service.mesenum.SFCReplaceType;
import com.mes.loco.aps.server.service.mesenum.WMSOrderType;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSBOMItem;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersion;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersionBPM;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiHisTask;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiTask;
import com.mes.loco.aps.server.service.po.mrp.MRPMaterialPlan;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.sap.APSBomData;
import com.mes.loco.aps.server.service.po.sap.HEAD;
import com.mes.loco.aps.server.service.po.sap.INPUT2;
import com.mes.loco.aps.server.service.po.sap.ITEM;
import com.mes.loco.aps.server.service.po.sap.OrderItem;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTaskItem;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemand;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemandItem;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.DesUtil;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.BPMServiceImpl;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSBOMItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSSchedulingVersionBPMDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSSchedulingVersionDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskPartDAO;
import com.mes.loco.aps.server.serviceimpl.dao.mrp.MRPMaterialPlanDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.dao.wms.WMSPickDemandDAO;
import com.mes.loco.aps.server.serviceimpl.dao.wms.WMSPickDemandItemDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * ???????????????
 * 
 * @author PengYouWang
 * @CreateTime 2020-4-2 16:57:38
 * @LastEditTime 2020-4-2 16:57:41
 */
@RestController
@RequestMapping("/api/Test")
public class TestController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	APSService wAPSService;

	/**
	 * ??????MES?????????????????????SAP
	 */
	@GetMapping("/TestOrderChange")
	public Object TestOrderChange(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			String wRst = OrderChangeTest(wLoginUser, wOrderID);

			ServiceResult<Integer> wServiceResult = new ServiceResult<Integer>();
			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wRst);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/Test")
	public Object Test(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			testAndon(wLoginUser);

			ServiceResult<Integer> wServiceResult = new ServiceResult<Integer>();
			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	private void testAndon(BMSEmployee wLoginUser) {
		try {

//			List<WMSLinePartLL> wHeaderList = new ArrayList<WMSLinePartLL>();
//
//			WMSLinePartLL wWMSLinePartLL = new WMSLinePartLL();
//			wWMSLinePartLL.details = new ArrayList<WMSLLDetail>();
//			wWMSLinePartLL.details.add(new WMSLLDetail());
//			wWMSLinePartLL.details.add(new WMSLLDetail());
//
//			wHeaderList.add(wWMSLinePartLL);
//			wHeaderList.add(wWMSLinePartLL);
//
//			WMSLinePartLLs wWMSLinePartLLs = new WMSLinePartLLs(wHeaderList);
//
//			WMSReturn wReturn = MyHelperServiceImpl.getInstance().WMS_PostLL(wWMSLinePartLLs);
//			System.out.println(wReturn);

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<APSSchedulingVersionBPM> wList = APSSchedulingVersionBPMDAO.getInstance().SelectList(wLoginUser, -1,
					"WP2021090006", -1, -1, null, "", -1, -1, null, null, null, wErrorCode);
			if (wList.size() > 0) {
				APSSchedulingVersion wScheduling = APSSchedulingVersionDAO.getInstance().SelectByVersionNo(wLoginUser,
						wList.get(0).VersionNo, wErrorCode);
				if (wScheduling != null && wScheduling.ID > 0) {
					wScheduling.APSTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
							wScheduling.TaskPartIDList, wErrorCode);
					// ????????????????????????
					APS_TriggerMRP(wLoginUser, wList.get(0), wScheduling);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ????????????????????????
	 */
	private void APS_TriggerMRP(BMSEmployee wLoginUser, APSSchedulingVersionBPM wAPSSchedulingVersionBPM,
			APSSchedulingVersion wScheduling) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// ?????????????????????
			for (APSTaskPart wAPSTaskPart : wScheduling.APSTaskPartList) {
				// ???????????????????????????????????????????????????(???????????????????????????)
				MRPMaterialPlanDAO.getInstance().DisableMainPlan(wLoginUser, wAPSTaskPart.OrderID, wAPSTaskPart.PartID,
						wErrorCode);
				if (wErrorCode.Result != 0) {
					logger.error("????????????????????????????????????????????????,??????????????????!");
					return;
				}
				// ???????????????bom(???????????????????????????)
				List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser,
						wAPSTaskPart.OrderID, "", "", -1, -1, -1, wAPSTaskPart.PartID, -1, -1, "", -1, -1, -1, null, -1,
						-1, -1, wErrorCode);
				if (wList == null || wList.size() <= 0) {
					continue;
				}
				// ???????????????????????????
				for (APSBOMItem apsbomItem : wList) {
					MRPMaterialPlan wMRPMaterialPlan = new MRPMaterialPlan(0, apsbomItem.ProductID, apsbomItem.LineID,
							apsbomItem.CustomerID, apsbomItem.OrderID, apsbomItem.PartNo, apsbomItem.PartID,
							apsbomItem.PartPointID, apsbomItem.MaterialID, apsbomItem.MaterialName,
							apsbomItem.MaterialNo, 1, apsbomItem.Number, wAPSTaskPart.StartTime,
							wAPSSchedulingVersionBPM.Code, 1, Calendar.getInstance(), wLoginUser.ID,
							apsbomItem.ReplaceType, apsbomItem.OutsourceType, 1, apsbomItem.WBSNo,
							apsbomItem.AssessmentType);
					MRPMaterialPlanDAO.getInstance().Update(wLoginUser, wMRPMaterialPlan, wErrorCode);
				}
				// ????????????????????????
				TriggerMaterialDistributionSheet(wLoginUser, wList, wAPSTaskPart);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ?????????????????????
	 * 
	 * @param wLoginUser   ????????????
	 * @param wList        ?????????????????????????????????bom
	 * @param wAPSTaskPart ????????????
	 */
	private void TriggerMaterialDistributionSheet(BMSEmployee wLoginUser, List<APSBOMItem> wList,
			APSTaskPart wAPSTaskPart) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOMSOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wList.get(0).OrderID, wErrorCode);

			// ????????????????????????
			List<WMSPickDemand> wExsitList = WMSPickDemandDAO.getInstance().SelectList(wLoginUser, -1,
					String.valueOf(WMSOrderType.LineOrder.getValue()), "", wList.get(0).ProductID, wList.get(0).LineID,
					wList.get(0).CustomerID, wList.get(0).OrderID, wList.get(0).PartID, -1, null, null, null,
					wErrorCode);
			if (wExsitList.size() > 0) {
				// ???????????????????????????
				return;
			}
			// ????????????????????????
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);

			String wCode = WMSPickDemandDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			Calendar wTime = wAPSTaskPart.StartTime;

			Calendar expectTime1 = Calendar.getInstance();
			expectTime1.set(wTime.get(Calendar.YEAR), wTime.get(Calendar.MONTH), wTime.get(Calendar.DATE), 0, 0, 0);

			Calendar expectTime2 = Calendar.getInstance();
			expectTime2.set(wTime.get(Calendar.YEAR), wTime.get(Calendar.MONTH), wTime.get(Calendar.DATE), 12, 0, 0);

			String monitorNo = "";
			String monitor = "";
			BMSEmployee wUser = WMSPickDemandDAO.getInstance().GetMonitorByPart(wLoginUser, wList.get(0).PartID,
					wErrorCode);
			if (StringUtils.isNotEmpty(wUser.LoginID)) {
				monitorNo = wUser.LoginID;
				monitor = wUser.Name;
			}

			WMSPickDemand wWMSPickDemand = new WMSPickDemand(0, "1900",
					String.valueOf(WMSOrderType.LineOrder.getValue()), wCode, expectTime1, expectTime2, monitorNo,
					monitor, wList.get(0).ProductID, wList.get(0).ProductNo, wList.get(0).LineID, wList.get(0).LineName,
					wList.get(0).CustomerID, APSConstans.GetCRMCustomer(wList.get(0).CustomerID).CustomerName,
					APSConstans.GetCRMCustomer(wList.get(0).CustomerID).CustomerCode, wList.get(0).OrderID,
					wList.get(0).PartNo, wList.get(0).PartID, APSConstans.GetFPCPartName(wList.get(0).PartID),
					APSConstans.GetFPCPart(wList.get(0).PartID).Code, "", "", wBaseTime, "", "", wBaseTime, 1,
					wLoginUser.ID, wLoginUser.Name, Calendar.getInstance(), wList.get(0).WBSNo);
			int wDemandID = WMSPickDemandDAO.getInstance().Update(wLoginUser, wWMSPickDemand, wErrorCode);
			if (wDemandID <= 0) {
				return;
			}
			// ??????????????????????????????
			int wIndex = 1;
			for (APSBOMItem wMSSBOMItem : wList) {
				WMSPickDemandItem wWMSPickDemandItem = new WMSPickDemandItem(0, wDemandID, wMSSBOMItem.MaterialID,
						wMSSBOMItem.MaterialNo, wMSSBOMItem.MaterialName, wMSSBOMItem.Number, wOMSOrder.OrderNo,
						wMSSBOMItem.PartPointID,
						APSConstans.GetFPCPartPoint(wMSSBOMItem.PartPointID).Code.replace("PS-", ""),
						APSConstans.GetFPCPartPointName(wMSSBOMItem.PartPointID), String.valueOf(wIndex), "",
						wMSSBOMItem.ReplaceType, SFCReplaceType.getEnumType(wMSSBOMItem.ReplaceType).getLable(),
						wMSSBOMItem.OutsourceType, SFCOutSourceType.getEnumType(wMSSBOMItem.OutsourceType).getLable(),
						"", "");
				WMSPickDemandItemDAO.getInstance().Update(wLoginUser, wWMSPickDemandItem, wErrorCode);
				wIndex++;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????????????????????????????
	 */
	@SuppressWarnings("unused")
	private void CopyProcess(BMSEmployee wLoginUser, List<SFCBOMTaskItem> wList, SFCBOMTask wTask,
			OutResult<Integer> wErrorCode) {
		try {
			// ?????????????????????
			List<BPMActivitiHisTask> wHisList = BPMServiceImpl.getInstance()
					.BPM_GetHistoryInstanceByID(wLoginUser, wTask.FlowID).List(BPMActivitiHisTask.class);
			wHisList.removeIf(p -> p.Status == 0);
			// ?????????

			BMSEmployee wUser = APSConstans.GetBMSEmployee(wTask.UpFlowID);
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			APIResult wAPiResult = CoreServiceImpl.getInstance().QMS_StartInstance(wUser, "8201");
			List<BPMActivitiTask> wBPMActivitiTask = wAPiResult.List(BPMActivitiTask.class);
			SFCBOMTask wData = wAPiResult.Custom("data", SFCBOMTask.class);

			SFCBOMTask wCloneItem = CloneTool.Clone(wTask, SFCBOMTask.class);

			wCloneItem.SFCBOMTaskItemList = wList;

			wCloneItem.ID = wData.ID;
			wCloneItem.Code = wData.Code;
			wCloneItem.Status = 14;
			wCloneItem.FlowID = wData.FlowID;

			wCloneItem.AreaCharge = wHisList.get(1).Assignee;

			wCloneItem = CoreServiceImpl.getInstance()
					.QMS_CompleteInstance(wUser, wCloneItem, wBPMActivitiTask.get(0).ID).Info(SFCBOMTask.class);

			// ?????????1
			wUser = APSConstans.GetBMSEmployee(Integer.parseInt(wHisList.get(1).Assignee));
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			wCloneItem.Status = 25;

			List<BPMActivitiHisTask> wNewTaskList = BPMServiceImpl.getInstance()
					.BPM_GetHistoryInstanceByID(wLoginUser, wCloneItem.FlowID).List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			wCloneItem = CoreServiceImpl.getInstance().QMS_CompleteInstance(wUser, wCloneItem, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);

			// ?????????2
			wUser = APSConstans.GetBMSEmployee(Integer.parseInt(wHisList.get(2).Assignee));
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			wCloneItem.Status = 1;

			wNewTaskList = BPMServiceImpl.getInstance().BPM_GetHistoryInstanceByID(wLoginUser, wCloneItem.FlowID)
					.List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			wCloneItem = CoreServiceImpl.getInstance().QMS_CompleteInstance(wUser, wCloneItem, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);

			// ?????????3
			wUser = APSConstans.GetBMSEmployee(Integer.parseInt(wHisList.get(3).Assignee));
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			wCloneItem.Status = 50;

			wNewTaskList = BPMServiceImpl.getInstance().BPM_GetHistoryInstanceByID(wLoginUser, wCloneItem.FlowID)
					.List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			wCloneItem = CoreServiceImpl.getInstance().QMS_CompleteInstance(wUser, wCloneItem, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ?????????SAP
	 */
	@SuppressWarnings("unused")
	private void SendToSAP(BMSEmployee wLoginUser, OMSOrder wOrder) {
		try {
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");

			List<OrderItem> wItemList = new ArrayList<OrderItem>();

			OrderItem wItem = new OrderItem();
			wItem.PSPNR = wOrder.OrderNo;
			wItem.ZCHEX = wOrder.ProductNo;
			wItem.ZTCH = wOrder.PartNo.split("#")[1];
			wItem.ZRC_DATE = wSDF.format(wOrder.RealReceiveDate.getTime());
			wItem.ZWG_DATE = wSDF.format(wOrder.RealFinishDate.getTime());
			wItemList.add(wItem);

			String wINPUT2 = JSON.toJSONString(wItemList);

			String wEndPoint = APSUtils.getInstance().GetSAPEndpoint();
			org.apache.axis.client.Service wService = new org.apache.axis.client.Service();
			Call wCall = (Call) wService.createCall();

			wCall.setUsername(APSUtils.getInstance().GetSAPUser());
			wCall.setPassword(APSUtils.getInstance().GetSAPPassword());

			wCall.setTargetEndpointAddress(wEndPoint);
			wCall.setOperationName(new QName("urn:sap-com:document:sap:rfc:functions", "ZIF_MES_INPUT_001"));// ??????????????????
			wCall.addParameter("INPUT1", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.addParameter("INPUT2", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.setReturnType(XMLType.XSD_STRING); // ??????????????????String

			String wReturn = (String) wCall.invoke(new Object[] { "02", wINPUT2 });// ????????????
			logger.info(wReturn);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????BOM????????????
	 */
	@SuppressWarnings("unused")
	private void APSBomChangeTest(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, 170, "", "", -1, -1,
					-1, -1, -1, -1, "", -1, -1, -1, null, -1, -1, -1, wErrorCode);
			wList = wList.stream().filter(p -> p.ID == 170274).collect(Collectors.toList());

			// ???????????????BOM???SAP
			String wJson = ChangeToJson(wLoginUser, wList, "U");
			if (StringUtils.isNotEmpty(wJson)) {
				SendToSAP(wLoginUser, wJson);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????BOM????????????
	 */
	@SuppressWarnings("unused")
	private void APSBomCreateTest(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, 161, "", "", -1, -1,
					-1, -1, -1, -1, "", -1, -1, -1, null, -1, -1, -1, wErrorCode);
			wList = wList.stream().filter(p -> p.OrderNum <= 6).collect(Collectors.toList());
			wList.get(0).PartPointName = "??????001";
			wList.get(1).PartPointName = "??????002";
			wList.get(2).PartPointName = "??????003";
			wList.get(3).PartPointName = "??????004";
			wList.forEach(p -> p.WBSNo = "GQ-A603.P.LX.004");

			// ???????????????BOM???SAP
			String wJson = ChangeToJson(wLoginUser, wList, "I");
			if (StringUtils.isNotEmpty(wJson)) {
				SendToSAP(wLoginUser, wJson);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ??????BOM????????????
	 */
	@SuppressWarnings("unused")
	private void APSBomDeleteTest(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, 161, "", "", -1, -1,
					-1, -1, -1, -1, "", -1, -1, -1, null, -1, -1, -1, wErrorCode);
			wList = wList.stream().filter(p -> p.OrderNum == 6 || p.OrderNum == 8).collect(Collectors.toList());
			wList.get(0).DeleteID = "X";

			// ???????????????BOM???SAP
			String wJson = ChangeToJson(wLoginUser, wList, "U");
			if (StringUtils.isNotEmpty(wJson)) {
				SendToSAP(wLoginUser, wJson);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ?????????BOM?????????SAP
	 */
	private void SendToSAP(BMSEmployee wLoginUser, String wJson) {
		try {
			String wEndPoint = "http://10.200.10.12:8000/sap/bc/srt/rfc/sap/zif_mes_ws/120/zif_mes_ws/zif_mes_ws";
			org.apache.axis.client.Service wService = new org.apache.axis.client.Service();
			Call wCall = (Call) wService.createCall();

			wCall.setUsername("sapdev");
			wCall.setPassword("senchu2020");

			wCall.setTargetEndpointAddress(wEndPoint);
			wCall.setOperationName(new QName("urn:sap-com:document:sap:rfc:functions", "ZIF_MES_INPUT_001"));// ??????????????????
			wCall.addParameter("INPUT1", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.addParameter("INPUT2", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.setReturnType(XMLType.XSD_STRING); // ??????????????????String

			String wReturn = (String) wCall.invoke(new Object[] { "01", wJson });// ????????????
			logger.info(wReturn);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * ?????????bom?????????SAP?????????json?????????
	 */
	private String ChangeToJson(BMSEmployee wLoginUser, List<APSBOMItem> wList, String wFlag) {
		String wResult = "";
		try {
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			APSBomData wAPSBomData = new APSBomData();
			wAPSBomData.INPUT1 = "01";

			INPUT2 wINPUT2 = new INPUT2();
			wINPUT2.MODE = wFlag;
			wINPUT2.HEAD = GetHead(wLoginUser, wList.get(0));
			wINPUT2.ITEM = GetITEM(wLoginUser, wList);
			wAPSBomData.INPUT2 = wINPUT2;

			wResult = JSON.toJSONString(wAPSBomData.INPUT2);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ????????????
	 */
	private List<ITEM> GetITEM(BMSEmployee wLoginUser, List<APSBOMItem> wList) {
		List<ITEM> wResult = new ArrayList<ITEM>();
		try {
			SimpleDateFormat wDateFormat = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat wTimeFormat = new SimpleDateFormat("HHmmss");

			for (APSBOMItem wAPSBOMItem : wList) {
				ITEM wItem = new ITEM();

				wItem.AEDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
				wItem.AENAM = wLoginUser.LoginID;
				wItem.AETIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
				wItem.BWTAR = wAPSBOMItem.AssessmentType;
				wItem.ERDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
				wItem.ERNAM = wLoginUser.LoginID;
				wItem.ERTIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
				wItem.GRPNR = "";
				wItem.ID = wAPSBOMItem.OrderID;
				wItem.ITEMID = wAPSBOMItem.OrderNum;
				wItem.KTEXT = wAPSBOMItem.TextCode;
				wItem.KTTXT = StringUtils.Format("{0}  {1}", APSConstans.GetFPCPart(wAPSBOMItem.PartID).Code,
						wAPSBOMItem.PartPointName);
				wItem.MATNR = wAPSBOMItem.MaterialNo;
				wItem.MEINS = wAPSBOMItem.UnitText;
				wItem.MENGE = String.valueOf(wAPSBOMItem.Number);
				wItem.RGEKZ = "";
				wItem.USR00 = APSConstans.GetFPCPart(wAPSBOMItem.PartID).Code;
				wItem.XFJJ = wAPSBOMItem.RepairCoreIdentification;
				wItem.ZBHOH = String.valueOf(wAPSBOMItem.ReplaceType);
				wItem.ZCJXC = wAPSBOMItem.DisassyType > 0 ? "X" : "";
				wItem.ZDELE = wAPSBOMItem.DeleteID;
				wItem.ZDRHH = wAPSBOMItem.DingrongGroup;
				wItem.ZFLAGFJ = wAPSBOMItem.AccessoryLogo;
				wItem.ZFLAGJXJ = wAPSBOMItem.RepairPartClass;
				wItem.ZFLAGKGL = wAPSBOMItem.CustomerMaterial > 0 ? "X" : "";
				wItem.ZLGORT = String.valueOf(wAPSBOMItem.StockID);
				wItem.ZLLSL = String.valueOf(wAPSBOMItem.PickingQuantity);
				wItem.ZREMARK = wAPSBOMItem.Remark;
				wItem.ZSCXC = wAPSBOMItem.OverLine > 0 ? "X" : "";
				wItem.ZSFHH = wAPSBOMItem.PartChange > 0 ? "X" : "";
				wItem.ZWTDW = wAPSBOMItem.DisassyType > 0 ? "X" : "";
				wItem.ZYCYZ = wAPSBOMItem.OriginalType > 0 ? "X" : "";
				wItem.ZZDHL = String.valueOf(wAPSBOMItem.EvenExchangeRate);
				wItem.ZZFC = wAPSBOMItem.ReceiveDepart;
				wItem.ZZLDL = wAPSBOMItem.QTType <= 0 ? ""
						: wAPSBOMItem.QTType == 1 ? "001"
								: wAPSBOMItem.QTType == 2 ? "002"
										: wAPSBOMItem.QTType == 3 ? "003" : wAPSBOMItem.QTType == 4 ? "004" : "";
				wItem.ZZLXL = wAPSBOMItem.QTItemType <= 0 ? ""
						: wAPSBOMItem.QTItemType == 1 ? "01"
								: wAPSBOMItem.QTItemType == 2 ? "02"
										: wAPSBOMItem.QTItemType == 3 ? "03"
												: wAPSBOMItem.QTItemType == 4 ? "04"
														: wAPSBOMItem.QTItemType == 5 ? "05" : "";
				wItem.ZZZBZ = wAPSBOMItem.Remark;
				wItem.ZZZWW = wAPSBOMItem.OutsourceType <= 0 ? "" : String.valueOf(wAPSBOMItem.OutsourceType);

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ????????????
	 */
	private HEAD GetHead(BMSEmployee wLoginUser, APSBOMItem wAPSBOMItem) {
		HEAD wResult = new HEAD();
		try {
			SimpleDateFormat wDateFormat = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat wTimeFormat = new SimpleDateFormat("HHmmss");

			wResult.AEDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
			wResult.AENAM = wLoginUser.LoginID;
			wResult.AETIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
			wResult.ARBPL = wAPSBOMItem.WorkCenter;
			wResult.ERDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
			wResult.ERNAM = wLoginUser.LoginID;
			wResult.ERTIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
			wResult.ID = wAPSBOMItem.OrderID;
			wResult.KTEXT = wAPSBOMItem.TextCode;
			wResult.MAT_PSPNR = wAPSBOMItem.WBSNo;
			wResult.WERKS = String.valueOf(wAPSBOMItem.FactoryID);
			wResult.ZCHEX = wAPSBOMItem.ProductNo;
			wResult.ZDELE = "";
			wResult.ZJDXX = wAPSBOMItem.CustomerCode;
			wResult.ZSCLX = String.valueOf(wAPSBOMItem.BOMType);
			wResult.ZXIUC = wAPSBOMItem.LineName.replace("C", "");
			wResult.ZZFC = wAPSBOMItem.ReceiveDepart;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ????????????????????????
	 */
	private String OrderChangeTest(BMSEmployee wLoginUser, int wOrderID) {
		String wResult = "";
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");

			List<OrderItem> wItemList = new ArrayList<OrderItem>();

			OrderItem wItem = new OrderItem();
			wItem.PSPNR = wOrder.OrderNo;
			wItem.ZCHEX = wOrder.ProductNo;
			wItem.ZTCH = wOrder.PartNo.split("#")[1];
			wItem.ZRC_DATE = wSDF.format(wOrder.RealReceiveDate.getTime());
			wItem.ZWG_DATE = "";
			wItemList.add(wItem);

			String wINPUT2 = JSON.toJSONString(wItemList);

			String wEndPoint = APSUtils.getInstance().GetSAPEndpoint();
			org.apache.axis.client.Service wService = new org.apache.axis.client.Service();
			Call wCall = (Call) wService.createCall();

			wCall.setUsername(APSUtils.getInstance().GetSAPUser());
			wCall.setPassword(APSUtils.getInstance().GetSAPPassword());

			wCall.setTargetEndpointAddress(wEndPoint);
			wCall.setOperationName(new QName("urn:sap-com:document:sap:rfc:functions", "ZIF_MES_INPUT_001"));// ??????????????????
			wCall.addParameter("INPUT1", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.addParameter("INPUT2", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.setReturnType(XMLType.XSD_STRING); // ??????????????????String

			wResult = (String) wCall.invoke(new Object[] { "02", wINPUT2 });// ????????????
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = ex.toString();
		}
		return wResult;
	}
}
