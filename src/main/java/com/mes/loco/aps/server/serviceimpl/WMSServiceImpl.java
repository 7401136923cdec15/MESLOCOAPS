package com.mes.loco.aps.server.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.mes.loco.aps.server.service.WMSService;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.SFCOutSourceType;
import com.mes.loco.aps.server.service.mesenum.SFCReplaceType;
import com.mes.loco.aps.server.service.mesenum.WMSOrderType;
import com.mes.loco.aps.server.service.mesenum.WMSPickDemandStatus;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSBOMItem;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.mss.MSSBOM;
import com.mes.loco.aps.server.service.po.mss.MSSBOMItem;
import com.mes.loco.aps.server.service.po.mss.MSSMaterial;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.wms.WMSLLDetail;
import com.mes.loco.aps.server.service.po.wms.WMSLinePartLL;
import com.mes.loco.aps.server.service.po.wms.WMSLinePartLLs;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemand;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemandItem;
import com.mes.loco.aps.server.service.po.wms.WMSReturn;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskPartDAO;
import com.mes.loco.aps.server.serviceimpl.dao.mss.MSSBOMItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.dao.wms.WMSPickDemandDAO;
import com.mes.loco.aps.server.serviceimpl.dao.wms.WMSPickDemandItemDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.utils.RemoteInvokeUtils;

@Service
public class WMSServiceImpl implements WMSService {

	private static Logger logger = LoggerFactory.getLogger(WMSServiceImpl.class);

	public WMSServiceImpl() {
		super();
	}

	private static WMSService Instance;

	public static WMSService getInstance() {
		if (Instance == null)
			Instance = new WMSServiceImpl();
		return Instance;
	}

	@Override
	public APIResult MSS_QueryBOM(BMSEmployee wLoginUser, int wBOMID, String wBOMNo, int wRouteID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("bom_no", wBOMNo);
			wParms.put("bom_id", wBOMID);
			wParms.put("RouteID", wRouteID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Bom/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryBOMAll(BMSEmployee wLoginUser, String wName, String wBOMNo, int wWorkShopID, int wBOMType,
			int wProductID, int wStatus) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("bom_no", wBOMNo);
			wParms.put("bom_name", wBOMNo);
			wParms.put("workshop_id", wWorkShopID);
			wParms.put("type_id", wBOMType);
			wParms.put("ProductID", wProductID);
			wParms.put("status", wStatus);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, StringUtils
					.Format("api/Bom/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryUnitList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Unit/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryMaterialList(BMSEmployee wLoginUser, String wMaterialNo) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("material_no", wMaterialNo);
			wParms.put("material_name", "");
			wParms.put("type_id", -1);
			wParms.put("status", -1);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Material/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_SaveBOM(BMSEmployee wLoginUser, MSSBOM wMSSBOM) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wMSSBOM);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Bom/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_SaveBOMItem(BMSEmployee wLoginUser, MSSBOMItem wMSSBOMItem) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wMSSBOMItem);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/BomItem/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_SaveMaterial(BMSEmployee wLoginUser, MSSMaterial wMSSMaterial) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wMSSMaterial);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Material/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryBOMItemAll(BMSEmployee wLoginUser, int wBOMID, int wLineID, int wProductID,
			int wCustomerID, int wPlaceID, int wPartPointID, int wBOMType, int wReplaceType, int wRouteID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();

			wParms.put("bom_id", Integer.valueOf(wBOMID));
			wParms.put("RouteID", wRouteID);
			wParms.put("PlaceID", Integer.valueOf(-1));
			wParms.put("LineID", Integer.valueOf(-1));
			wParms.put("ProductID", Integer.valueOf(-1));
			wParms.put("CustomerID", Integer.valueOf(-1));
			wParms.put("ReplaceType", Integer.valueOf(-1));
			wParms.put("BOMType", Integer.valueOf(-1));
			wParms.put("PartPointID", Integer.valueOf(-1));
			wParms.put("OutSourceType", Integer.valueOf(-1));
			wParms.put("IsList", Integer.valueOf(-1));

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/BomItem/All?cadv_ao={0}&cade_po={1}",
							new Object[] { wLoginUser.getLoginName(), wLoginUser.getPassword() }),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryBOMItemByID(BMSEmployee wLoginUser, int wBOMItemID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("id", wBOMItemID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/BomItem/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult MSS_QueryMaterialByID(BMSEmployee wLoginUser, int wMaterialID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("material_id", wMaterialID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Material/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult APS_SaveBOMItemList(BMSEmployee wLoginUser, List<APSBOMItem> wAPSBOMItemList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wAPSBOMItemList);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/APSBOM/Create?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult APS_OrderStart(BMSEmployee wLoginUser, int wLineID, int wProductID, int wCustomerID, int wOrderID,
			String wWBSNo, String wPartNo) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("LineID", wLineID);
			wParms.put("ProductID", wProductID);
			wParms.put("CustomerID", wCustomerID);
			wParms.put("OrderID", wOrderID);
			wParms.put("WBSNo", wWBSNo);
			wParms.put("PartNo", wPartNo);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/APSBOM/OrderStart?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public APIResult APS_BOMItemUpdate(BMSEmployee wLoginUser, APSBOMItem wAPSBOMItem) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wAPSBOMItem);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/APSBOM/Update?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public APIResult MSS_ConfigAll(BMSEmployee wLoginUser, String wPartConfigNo, String wPartConfigName, int wActive,
			String wProductNo, int wCustomerID, int wLineID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<>();
			wParms.put("PartConfigNo", wPartConfigNo);
			wParms.put("PartConfigName", wPartConfigName);
			wParms.put("Active", Integer.valueOf(wActive));
			wParms.put("ProductNo", wProductNo);
			wParms.put("CustomerID", Integer.valueOf(wCustomerID));
			wParms.put("LineID", Integer.valueOf(wLineID));

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/MSSPart/ConfigAll?cadv_ao={0}&cade_po={1}",
							new Object[] { wLoginUser.getLoginName(), wLoginUser.getPassword() }),
					wParms, HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<WMSPickDemand>> WMS_QueryPickDemandList(BMSEmployee wLoginUser, String wOrderType,
			String wDemandNo, int wProductID, int wLineID, int wCustomerID, int wOrderID, int wPartID, int wStatus,
			String wMaterial, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<WMSPickDemand>> wResult = new ServiceResult<List<WMSPickDemand>>();
		wResult.Result = new ArrayList<WMSPickDemand>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wIDList = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(wMaterial)) {
				wIDList = WMSPickDemandItemDAO.getInstance().SelectDemandList(wLoginUser, wMaterial, wErrorCode);
			}

			wResult.Result = WMSPickDemandDAO.getInstance().SelectList(wLoginUser, -1, wOrderType, wDemandNo,
					wProductID, wLineID, wCustomerID, wOrderID, wPartID, wStatus, wStartTime, wEndTime, wIDList,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<WMSPickDemand> WMS_QueryPickDemand(BMSEmployee wLoginUser, int wDemandID) {
		ServiceResult<WMSPickDemand> wResult = new ServiceResult<WMSPickDemand>();
		wResult.Result = new WMSPickDemand();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = WMSPickDemandDAO.getInstance().SelectByID(wLoginUser, wDemandID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> WMS_TriggerPickDemandTask(BMSEmployee wLoginUser, int wOrderID, int wPartID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ????????????
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			// ????????????????????????
			List<WMSPickDemand> wExsitList = WMSPickDemandDAO.getInstance().SelectList(wLoginUser, -1,
					String.valueOf(WMSOrderType.LineOrder.getValue()), "", wOrder.ProductID, wOrder.LineID,
					wOrder.CustomerID, wOrder.ID, wPartID, -1, null, null, null, wErrorCode);
			if (wExsitList.size() > 0) {
				wResult.FaultCode += "???????????????????????????????????????????????????!";
				return wResult;
			}

			// ????????????????????????
			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1, -1,
					wPartID, 1, APSShiftPeriod.Week.getValue(), new ArrayList<Integer>(Arrays.asList(2, 4, 5)), -1,
					null, null, wErrorCode);
			if (wTaskPartList == null || wTaskPartList.size() <= 0) {
				wResult.FaultCode += "????????????????????????????????????????????????";
				return wResult;
			}
			// ???????????????BOM
			int wBOMID = MSSBOMItemDAO.getInstance().QueryBOMID(wLoginUser, wOrderID, wErrorCode);
			if (wBOMID <= 0) {
				wResult.FaultCode += "?????????????????????????????????BOM";
				return wResult;
			}
			List<MSSBOMItem> wItemList = MSSBOMItemDAO.getInstance().SelectList(wLoginUser, wBOMID, -1, wPartID,
					wErrorCode);
			if (wItemList == null || wItemList.size() <= 0) {
				wResult.FaultCode += "????????????????????????????????????BOM";
				return wResult;
			}

			// ?????????????????????
			wItemList = wItemList.stream().filter(p -> p.ReplaceType == 1 || p.OutsourceType == 1)
					.collect(Collectors.toList());

			// ????????????????????????
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);

			String wCode = WMSPickDemandDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			Calendar wTime = wTaskPartList.get(0).StartTime;

			Calendar expectTime1 = Calendar.getInstance();
			expectTime1.set(wTime.get(Calendar.YEAR), wTime.get(Calendar.MONTH), wTime.get(Calendar.DATE), 0, 0, 0);

			Calendar expectTime2 = Calendar.getInstance();
			expectTime2.set(wTime.get(Calendar.YEAR), wTime.get(Calendar.MONTH), wTime.get(Calendar.DATE), 12, 0, 0);

			String monitorNo = "";
			String monitor = "";
			BMSEmployee wUser = WMSPickDemandDAO.getInstance().GetMonitorByPart(wLoginUser, wPartID, wErrorCode);
			if (StringUtils.isNotEmpty(wUser.LoginID)) {
				monitorNo = wUser.LoginID;
				monitor = wUser.Name;
			}

			WMSPickDemand wWMSPickDemand = new WMSPickDemand(0, "1900",
					String.valueOf(WMSOrderType.LineOrder.getValue()), wCode, expectTime1, expectTime2, monitorNo,
					monitor, wOrder.ProductID, wOrder.ProductNo, wOrder.LineID, wOrder.LineName, wOrder.CustomerID,
					wOrder.Customer, APSConstans.GetCRMCustomer(wOrder.CustomerID).CustomerCode, wOrder.ID,
					wOrder.PartNo, wPartID, APSConstans.GetFPCPartName(wPartID), APSConstans.GetFPCPart(wPartID).Code,
					"", "", wBaseTime, "", "", wBaseTime, 1, wLoginUser.ID, wLoginUser.Name, Calendar.getInstance(),
					wOrder.OrderNo);
			int wDemandID = WMSPickDemandDAO.getInstance().Update(wLoginUser, wWMSPickDemand, wErrorCode);
			if (wDemandID <= 0) {
				wResult.FaultCode += "????????????????????????????????????????????????!";
				return wResult;
			}
			// ??????????????????????????????
			int wIndex = 1;
			for (MSSBOMItem wMSSBOMItem : wItemList) {
				WMSPickDemandItem wWMSPickDemandItem = new WMSPickDemandItem(0, wDemandID, wMSSBOMItem.MaterialID,
						wMSSBOMItem.MaterialNo, wMSSBOMItem.MaterialName, wMSSBOMItem.MaterialNumber, wOrder.OrderNo,
						wMSSBOMItem.PartPointID,
						APSConstans.GetFPCPartPoint(wMSSBOMItem.PartPointID).Code.replace("PS-", ""),
						APSConstans.GetFPCPartPointName(wMSSBOMItem.PartPointID), String.valueOf(wIndex), "",
						wMSSBOMItem.ReplaceType, SFCReplaceType.getEnumType(wMSSBOMItem.ReplaceType).getLable(),
						wMSSBOMItem.OutsourceType, SFCOutSourceType.getEnumType(wMSSBOMItem.OutsourceType).getLable(),
						"", "");
				WMSPickDemandItemDAO.getInstance().Update(wLoginUser, wWMSPickDemandItem, wErrorCode);
				wIndex++;
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> WMS_ManualPush(BMSEmployee wLoginUser, int wDemandID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			WMSPickDemand wPickDemand = WMSPickDemandDAO.getInstance().SelectByID(wLoginUser, wDemandID, wErrorCode);

			List<WMSLinePartLL> wHeaderList = new ArrayList<WMSLinePartLL>();
			WMSLinePartLL wWMSLinePartLL = GetWMSLinePartLL(wPickDemand);
			wHeaderList.add(wWMSLinePartLL);
			WMSLinePartLLs wWMSLinePartLLs = new WMSLinePartLLs(wHeaderList);
			WMSReturn wReturn = MyHelperServiceImpl.getInstance().WMS_PostLL(wWMSLinePartLLs);
			if (wReturn.returnFlag.equals("1")) {
				wPickDemand.Status = WMSPickDemandStatus.Sended.getValue();
				wPickDemand.SendStatus = 1;
				wPickDemand.SendDesc = "????????????";
			} else {
				wPickDemand.SendStatus = 2;

				if (StringUtils.isEmpty(wReturn.returnDesc))
					wPickDemand.SendDesc = "????????????";
				else
					wPickDemand.SendDesc = wReturn.returnDesc;

				wResult.FaultCode += wReturn.returnDesc;
			}
			WMSPickDemandDAO.getInstance().Update(wLoginUser, wPickDemand, wErrorCode);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ???MES??????????????????WMS?????????
	 */
	private WMSLinePartLL GetWMSLinePartLL(WMSPickDemand wPickDemand) {
		WMSLinePartLL wResult = new WMSLinePartLL();
		try {
			wResult.customerId = wPickDemand.Factory;
			wResult.orderType = wPickDemand.OrderType;
			wResult.docNo = wPickDemand.DemandNo;
			wResult.expectedShipmentTime1 = wPickDemand.ExpectTime1Text;
			wResult.requiredDeliveryTime = wPickDemand.ExpectTime2Text;
			wResult.consigneeId = wPickDemand.MonitorNo;
			wResult.consigneeName = wPickDemand.Monitor;
			wResult.hedi01 = wPickDemand.ProductNo;
			wResult.hedi02 = wPickDemand.LineName;
			wResult.hedi03 = wPickDemand.CustomerCode;
			wResult.hedi04 = wPickDemand.PartNo;
			wResult.hedi05 = wPickDemand.PartName;
			wResult.hedi06 = wPickDemand.PartCode;
			wResult.hedi07 = wPickDemand.WBSNo;
			wResult.details = new ArrayList<WMSLLDetail>();
			for (WMSPickDemandItem wWMSPickDemandItem : wPickDemand.ItemList) {
				WMSLLDetail wWMSLLDetail = new WMSLLDetail();
				wWMSLLDetail.sku = wWMSPickDemandItem.MaterialNo;
				wWMSLLDetail.qtyOrdered = StringUtils.parseString(wWMSPickDemandItem.FQTY);
				wWMSLLDetail.lotAtt08 = wPickDemand.WBSNo;
				wWMSLLDetail.lotAtt11 = wWMSPickDemandItem.AssessmentType;
				wWMSLLDetail.dedi04 = wWMSPickDemandItem.PartPointCode;
				wWMSLLDetail.dedi05 = wWMSPickDemandItem.PartPointName;
				wWMSLLDetail.dedi06 = wWMSPickDemandItem.RowNo;
				wWMSLLDetail.userDefine1 = wWMSPickDemandItem.GroupFlag;
				wWMSLLDetail.userDefine2 = wWMSPickDemandItem.KittingFlag;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
