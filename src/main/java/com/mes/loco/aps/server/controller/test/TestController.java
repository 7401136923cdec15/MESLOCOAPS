package com.mes.loco.aps.server.controller.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSBOMItem;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiHisTask;
import com.mes.loco.aps.server.service.po.bpm.BPMActivitiTask;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.sap.APSBomData;
import com.mes.loco.aps.server.service.po.sap.HEAD;
import com.mes.loco.aps.server.service.po.sap.INPUT2;
import com.mes.loco.aps.server.service.po.sap.ITEM;
import com.mes.loco.aps.server.service.po.sap.OrderItem;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTaskItem;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.DesUtil;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.BPMServiceImpl;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.MyHelperServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSBOMItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 测试控制器
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
	 * 测试MES订单变更推送给SAP
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
	 * 接口测试
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
			// 台车BOM创建测试
//			APSBomCreateTest(wLoginUser);

			// 台车BOM变更测试
//			APSBomChangeTest(wLoginUser);

			// 生产订单变更测试
//			OrderChangeTest(wLoginUser, 1615);

//			APSBomDeleteTest(wLoginUser);

//			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
//			APSBOMItem wItem = APSBOMItemDAO.getInstance().APS_QueryBOMItemByID(wLoginUser, 184147, wErrorCode);
//			APSServiceImpl.getInstance().APS_UpdateBOMItem(wLoginUser, wItem);

//			OMSServiceImpl.getInstance().OMS_DisablePlan(wLoginUser, "151");

//			APSServiceImpl.getInstance().APS_BOMTaskToSAP(wLoginUser, 166);
//			APSServiceImpl.getInstance().APS_BOMTaskToSAP(wLoginUser, 207);

//			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
//			SFCBOMTask wInfo = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_GetTaskInfo(wLoginUser, 166, "",
//					wErrorCode);
//
//			CopyProcess(wLoginUser, new ArrayList<SFCBOMTaskItem>(), wInfo, wErrorCode);

//			ServiceResult<AndonLocomotiveProductionStatus> wRst = AndonServiceImpl.getInstance()
//					.Andon_QueryProductionStatus(wLoginUser);
//			System.out.println(wRst);

//			ServiceResult<Integer> wRsl = APSServiceImpl.getInstance().APS_SendToSAPPro(wLoginUser, 1805, 0);
//			System.out.println(wRsl);

//			ServiceResult<Integer> wRSst = APSServiceImpl.getInstance().APS_ChangeLogAddToSAP(wLoginUser, 5);
//			System.out.println(wRSst.toString());

			// 479、1618
			// 479、1619

//			APSServiceImpl.getInstance().APS_AddAPSBOMToSAP(wLoginUser, 481, 1615);

			// 测试能否取消删除标记
//			APSServiceImpl.getInstance().APS_CancelDeleteX(wLoginUser, 271830);
			// 245112、243368

//			int wBOMID = 542;
//			List<Integer> wBOMItemIDList = new ArrayList<Integer>(Arrays.asList(1325538, 1325539, 1325540));
//			List<Integer> wOrderIDList = new ArrayList<Integer>(Arrays.asList(1654, 1655));
//			APSServiceImpl.getInstance().SendSingleMaterial(wLoginUser, wBOMID, wBOMItemIDList, wOrderIDList);

//			int wOrderID = 1619;
//			int wAPSBOMID = 245112;
//
//			APSServiceImpl.getInstance().APS_ChangeNumber(wLoginUser, wOrderID, wAPSBOMID);

//			ServiceResult<List<AndonDayPlanAreaCashingRate>> wRst = AndonServiceImpl.getInstance()
//					.Andon_QueryAreaDayPlanRate(wLoginUser);
//			for (AndonDayPlanAreaCashingRate wItem : wRst.Result) {
//				System.out.println(wItem.toString());
//			}

//			ServiceResult<Integer> wRst = APSServiceImpl.getInstance().APS_QueryBOMID(wLoginUser, 31572, 4, "CS");
//			System.out.println(wRst.Result);

//			ServiceResult<AndonLocomotiveProductionStatus> wRst = AndonServiceImpl.getInstance()
//					.Andon_QueryProductionStatus(wLoginUser);
//			System.out.println(wRst);

//			ServiceResult<SFCStationPerson> wRst = SFCServiceImpl.getInstance().SFC_QueryStationPerson(wLoginUser, 139);
//			System.out.println(wRst.Result);

//			ServiceResult<Integer> wRst = SFCServiceImpl.getInstance().SFC_UpdateQuota(wLoginUser);
//			System.out.println(wRst);

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

			MyHelperServiceImpl.getInstance().WMS_TestSend();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 复制单据流程到评级后
	 */
	@SuppressWarnings("unused")
	private void CopyProcess(BMSEmployee wLoginUser, List<SFCBOMTaskItem> wList, SFCBOMTask wTask,
			OutResult<Integer> wErrorCode) {
		try {
			// ①查询历史记录
			List<BPMActivitiHisTask> wHisList = BPMServiceImpl.getInstance()
					.BPM_GetHistoryInstanceByID(wLoginUser, wTask.FlowID).List(BPMActivitiHisTask.class);
			wHisList.removeIf(p -> p.Status == 0);
			// ①发起

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

			// ①处理1
			wUser = APSConstans.GetBMSEmployee(Integer.parseInt(wHisList.get(1).Assignee));
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			wCloneItem.Status = 25;

			List<BPMActivitiHisTask> wNewTaskList = BPMServiceImpl.getInstance()
					.BPM_GetHistoryInstanceByID(wLoginUser, wCloneItem.FlowID).List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			wCloneItem = CoreServiceImpl.getInstance().QMS_CompleteInstance(wUser, wCloneItem, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);

			// ①处理2
			wUser = APSConstans.GetBMSEmployee(Integer.parseInt(wHisList.get(2).Assignee));
			wUser.LoginName = DesUtil.encrypt(wUser.LoginName, BaseDAO.appSecret);
			wUser.Password = DesUtil.encrypt("123456", BaseDAO.appSecret);

			wCloneItem.Status = 1;

			wNewTaskList = BPMServiceImpl.getInstance().BPM_GetHistoryInstanceByID(wLoginUser, wCloneItem.FlowID)
					.List(BPMActivitiHisTask.class);
			wNewTaskList = wNewTaskList.stream().filter(p -> p.Status == 0).collect(Collectors.toList());

			wCloneItem = CoreServiceImpl.getInstance().QMS_CompleteInstance(wUser, wCloneItem, wNewTaskList.get(0).ID)
					.Info(SFCBOMTask.class);

			// ①处理3
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
	 * 推送给SAP
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
			wCall.setOperationName(new QName("urn:sap-com:document:sap:rfc:functions", "ZIF_MES_INPUT_001"));// 调用的方法名
			wCall.addParameter("INPUT1", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.addParameter("INPUT2", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.setReturnType(XMLType.XSD_STRING); // 返回值类型：String

			String wReturn = (String) wCall.invoke(new Object[] { "02", wINPUT2 });// 远程调用
			logger.info(wReturn);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 台车BOM变更测试
	 */
	@SuppressWarnings("unused")
	private void APSBomChangeTest(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, 170, "", "", -1, -1,
					-1, -1, -1, -1, "", -1, -1, -1, null, -1, -1, -1, wErrorCode);
			wList = wList.stream().filter(p -> p.ID == 170274).collect(Collectors.toList());

			// ①推送台车BOM给SAP
			String wJson = ChangeToJson(wLoginUser, wList, "U");
			if (StringUtils.isNotEmpty(wJson)) {
				SendToSAP(wLoginUser, wJson);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 台车BOM创建测试
	 */
	@SuppressWarnings("unused")
	private void APSBomCreateTest(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, 161, "", "", -1, -1,
					-1, -1, -1, -1, "", -1, -1, -1, null, -1, -1, -1, wErrorCode);
			wList = wList.stream().filter(p -> p.OrderNum <= 6).collect(Collectors.toList());
			wList.get(0).PartPointName = "测试001";
			wList.get(1).PartPointName = "测试002";
			wList.get(2).PartPointName = "测试003";
			wList.get(3).PartPointName = "测试004";
			wList.forEach(p -> p.WBSNo = "GQ-A603.P.LX.004");

			// ①推送台车BOM给SAP
			String wJson = ChangeToJson(wLoginUser, wList, "I");
			if (StringUtils.isNotEmpty(wJson)) {
				SendToSAP(wLoginUser, wJson);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 台车BOM删除测试
	 */
	@SuppressWarnings("unused")
	private void APSBomDeleteTest(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, 161, "", "", -1, -1,
					-1, -1, -1, -1, "", -1, -1, -1, null, -1, -1, -1, wErrorCode);
			wList = wList.stream().filter(p -> p.OrderNum == 6 || p.OrderNum == 8).collect(Collectors.toList());
			wList.get(0).DeleteID = "X";

			// ①推送台车BOM给SAP
			String wJson = ChangeToJson(wLoginUser, wList, "U");
			if (StringUtils.isNotEmpty(wJson)) {
				SendToSAP(wLoginUser, wJson);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 将台车BOM发送给SAP
	 */
	private void SendToSAP(BMSEmployee wLoginUser, String wJson) {
		try {
			String wEndPoint = "http://10.200.10.12:8000/sap/bc/srt/rfc/sap/zif_mes_ws/120/zif_mes_ws/zif_mes_ws";
			org.apache.axis.client.Service wService = new org.apache.axis.client.Service();
			Call wCall = (Call) wService.createCall();

			wCall.setUsername("sapdev");
			wCall.setPassword("senchu2020");

			wCall.setTargetEndpointAddress(wEndPoint);
			wCall.setOperationName(new QName("urn:sap-com:document:sap:rfc:functions", "ZIF_MES_INPUT_001"));// 调用的方法名
			wCall.addParameter("INPUT1", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.addParameter("INPUT2", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.setReturnType(XMLType.XSD_STRING); // 返回值类型：String

			String wReturn = (String) wCall.invoke(new Object[] { "01", wJson });// 远程调用
			logger.info(wReturn);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 将台车bom转换为SAP需要的json字符串
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
	 * 获取明细
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
	 * 获取表头
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
	 * 生产订单变更测试
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
			wCall.setOperationName(new QName("urn:sap-com:document:sap:rfc:functions", "ZIF_MES_INPUT_001"));// 调用的方法名
			wCall.addParameter("INPUT1", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.addParameter("INPUT2", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.setReturnType(XMLType.XSD_STRING); // 返回值类型：String

			wResult = (String) wCall.invoke(new Object[] { "02", wINPUT2 });// 远程调用
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = ex.toString();
		}
		return wResult;
	}
}
