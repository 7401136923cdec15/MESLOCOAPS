package com.mes.loco.aps.server.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mes.loco.aps.server.service.MyHelperService;
import com.mes.loco.aps.server.service.mesenum.SFCTurnOrderTaskStatus;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.FocasLGL;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fmc.FMCLine;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkCharge;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCPartPoint;
import com.mes.loco.aps.server.service.po.fpc.FPCProduct;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.rsm.RSMTurnOrderTask;
import com.mes.loco.aps.server.service.po.wms.WMSLinePartLLs;
import com.mes.loco.aps.server.service.po.wms.WMSReturn;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.utils.RemoteInvokeUtils;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-4-1 13:53:36
 * @LastEditTime 2020-4-1 13:53:40
 *
 */
@Service
public class MyHelperServiceImpl implements MyHelperService {

	private static Logger logger = LoggerFactory.getLogger(MyHelperServiceImpl.class);

	public MyHelperServiceImpl() {
	}

	private static MyHelperService Instance;

	public static MyHelperService getInstance() {
		if (Instance == null)
			Instance = new MyHelperServiceImpl();
		return Instance;
	}

	@Override
	public ServiceResult<Map<String, FMCLine>> FMC_QueryLineNameMap(BMSEmployee wLoginUser) {
		ServiceResult<Map<String, FMCLine>> wResult = new ServiceResult<Map<String, FMCLine>>();
		try {
			wResult.Result = new HashMap<String, FMCLine>();

			List<FMCLine> wLineList = CoreServiceImpl.getInstance().FMC_QueryLineList(wLoginUser).List(FMCLine.class);
			if (wLineList == null || wLineList.size() <= 0)
				return wResult;

			for (FMCLine wFMCLine : wLineList) {
				if (wResult.Result.containsKey(wFMCLine.Name))
					continue;
				wResult.Result.put(wFMCLine.Name, wFMCLine);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<String, FPCPart>> FPC_QueryPartNameMap(BMSEmployee wLoginUser) {
		ServiceResult<Map<String, FPCPart>> wResult = new ServiceResult<Map<String, FPCPart>>();
		try {
			wResult.Result = new HashMap<String, FPCPart>();

			List<FPCPart> wList = CoreServiceImpl.getInstance().FPC_QueryPartList(wLoginUser).List(FPCPart.class);
			if (wList == null || wList.size() <= 0)
				return wResult;

			for (FPCPart wFPCPart : wList) {
				if (wResult.Result.containsKey(wFPCPart.Name))
					continue;
				wResult.Result.put(wFPCPart.Name, wFPCPart);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<String, FPCPartPoint>> FPC_QueryPartPointNameMap(BMSEmployee wLoginUser) {
		ServiceResult<Map<String, FPCPartPoint>> wResult = new ServiceResult<Map<String, FPCPartPoint>>();
		try {
			List<FPCPartPoint> wList = CoreServiceImpl.getInstance().FPC_QueryPartPointList(wLoginUser)
					.List(FPCPartPoint.class);
			if (wList == null || wList.size() <= 0)
				return wResult;

			for (FPCPartPoint wFPCPartPoint : wList) {
				if (wResult.Result.containsKey(wFPCPartPoint.Name))
					continue;
				wResult.Result.put(wFPCPartPoint.Name, wFPCPartPoint);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<Integer, FMCWorkCharge>> FMC_QueryWorkChargeMap(BMSEmployee wLoginUser) {
		ServiceResult<Map<Integer, FMCWorkCharge>> wResult = new ServiceResult<Map<Integer, FMCWorkCharge>>();
		try {
			wResult.Result = new HashMap<Integer, FMCWorkCharge>();

			List<FMCWorkCharge> wList = CoreServiceImpl.getInstance().FMC_QueryWorkChargeList(wLoginUser)
					.List(FMCWorkCharge.class);
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			for (FMCWorkCharge wFMCWorkCharge : wList) {
				if (wResult.Result.containsKey(wFMCWorkCharge.StationID)) {
					continue;
				}
				wResult.Result.put(wFMCWorkCharge.StationID, wFMCWorkCharge);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<String, FPCProduct>> FPC_QueryProductNameMap(BMSEmployee wLoginUser) {
		ServiceResult<Map<String, FPCProduct>> wResult = new ServiceResult<Map<String, FPCProduct>>();
		try {
			wResult.Result = new HashMap<String, FPCProduct>();

			APIResult wApiResult = CoreServiceImpl.getInstance().FPC_QueryProductList(wLoginUser, -1, -1);
			List<FPCProduct> wFPCProductList = wApiResult.List(FPCProduct.class);
			if (wFPCProductList == null || wFPCProductList.size() <= 0)
				return wResult;

			wFPCProductList = wFPCProductList.stream().filter(p -> p.Active == 1).collect(Collectors.toList());

			for (FPCProduct wFPCProduct : wFPCProductList) {
				if (!wResult.Result.containsKey(wFPCProduct.ProductNo)) {
					wResult.Result.put(wFPCProduct.ProductNo, wFPCProduct);
				}
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<Integer, List<Integer>>> LFS_QueryWorkAreaCheckListMap(BMSEmployee wLoginUser) {
		ServiceResult<Map<Integer, List<Integer>>> wResult = new ServiceResult<Map<Integer, List<Integer>>>();
		try {
			wResult.Result = new HashMap<Integer, List<Integer>>();

			List<LFSWorkAreaChecker> wList = LFSServiceImpl.getInstance().LFS_QueryWorkAreaCheckerList(wLoginUser)
					.List(LFSWorkAreaChecker.class);
			if (wList == null || wList.size() <= 0)
				return wResult;

			List<LFSWorkAreaChecker> wTempList = null;
			List<Integer> wIDList = null;
			for (LFSWorkAreaChecker wLFSWorkAreaChecker : wList) {
				if (wResult.Result.containsKey(wLFSWorkAreaChecker.WorkAreaID))
					continue;

				wTempList = wList.stream().filter(p -> p.WorkAreaID == wLFSWorkAreaChecker.WorkAreaID)
						.collect(Collectors.toList());
				if (wTempList != null && wTempList.size() > 0) {
					wIDList = new ArrayList<Integer>();
					for (LFSWorkAreaChecker wItem : wTempList) {
						wIDList.addAll(wItem.CheckerIDList);
					}
					wResult.Result.put(wLFSWorkAreaChecker.WorkAreaID, wIDList);
				}
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<Integer>> FPC_QueryPreStationIDList(BMSEmployee wLoginUser, int wOrderID,
			int wStationID) {
		ServiceResult<List<Integer>> wResult = new ServiceResult<List<Integer>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>();
			wResult.Result = new ArrayList<Integer>();
			OMSOrder wOMSOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOMSOrder == null || wOMSOrder.ID <= 0) {
				return wResult;
			}

			int wRouteID = 0;
			if (APSConstans.GetFPCRoute(wOMSOrder.ProductID, wOMSOrder.LineID, wOMSOrder.CustomerID).ID > 0) {
				wRouteID = APSConstans.GetFPCRoute(wOMSOrder.ProductID, wOMSOrder.LineID, wOMSOrder.CustomerID).ID;
			}
			if (wRouteID <= 0) {
				return wResult;
			}

//			APIResult wAPIResult = CoreServiceImpl.getInstance().FPC_QueryProductRouteList(wLoginUser);
//			List<FPCProductRoute> wProductRouteList = wAPIResult.List(FPCProductRoute.class);
//			Optional<FPCProductRoute> wOption = wProductRouteList.stream()
//					.filter(p -> p.ProductID == wOMSOrder.ProductID && p.Line.equals(wOMSOrder.LineName)).findFirst();

//			if (!wOption.isPresent()) {
//				return wResult;
//			}

//			FPCProductRoute wProductRoute = wOption.get();
			List<FPCRoutePart> wPartList = CoreServiceImpl.getInstance().FPC_QueryRoutePartList(wLoginUser, wRouteID)
					.List(FPCRoutePart.class);

			if (wPartList == null || wPartList.size() <= 0) {
				return wResult;
			}
			// 直接上级工位
			Optional<FPCRoutePart> wRoutePart = wPartList.stream().filter(p -> p.PartID == wStationID).findFirst();
			if (wRoutePart.isPresent()) {
				wResult.Result.add(wRoutePart.get().PrevPartID);
			}
			// 间接上级工位
			List<FPCRoutePart> wTempList = wPartList.stream()
					.filter(p -> p.NextPartIDMap.containsKey(String.valueOf(wStationID))).collect(Collectors.toList());
			if (wTempList.size() > 0) {
				wResult.Result.addAll(wTempList.stream().map(p -> p.PartID).collect(Collectors.toList()));
			}

			// 去除为0的工位ID
			if (wResult.Result.size() > 0) {
				wResult.Result.removeIf(p -> p <= 0);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Boolean> SFC_JudgeTaskStepIsCanDo(BMSEmployee wLoginUser, APSTaskStep wAPSTaskStep) {
		ServiceResult<Boolean> wResult = new ServiceResult<Boolean>();
		try {
			// 查询上工位ID集合
			List<Integer> wPreStationIDList = this.FPC_QueryPreStationIDList(wLoginUser, wAPSTaskStep.OrderID,
					wAPSTaskStep.PartID).Result;
			if (wPreStationIDList.size() <= 0) {
				wResult.Result = true;
			} else {
				List<RSMTurnOrderTask> wTempList = QMSServiceImpl.getInstance()
						.RSM_QueryTurnOrderTaskList(wLoginUser, wAPSTaskStep.OrderID, -1, wAPSTaskStep.PartID,
								new ArrayList<Integer>(Arrays.asList(SFCTurnOrderTaskStatus.Passed.getValue())))
						.List(RSMTurnOrderTask.class);
				if (wTempList == null || wTempList.size() <= 0) {
					wResult.Result = false;
				} else {
					if (wPreStationIDList.stream()
							.allMatch(p -> wTempList.stream().anyMatch(q -> q.ApplyStationID == p))) {
						wResult.Result = true;
					} else {
						wResult.Result = false;
					}
				}
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Boolean> WDW_IsSendRepair(BMSEmployee wAdminUser, int taskID, int iPTItemID) {
		ServiceResult<Boolean> wResult = new ServiceResult<Boolean>();
		try {
			Map<String, Integer> wMap = WDWServiceImpl.getInstance().WDW_SpecialItemAll_Repair(wAdminUser, taskID)
					.Info(Map.class);
			if (wMap == null || wMap.size() <= 0) {
				wResult.Result = false;
			} else if (wMap.containsKey(String.valueOf(iPTItemID))) {
				wResult.Result = true;
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public List<FocasLGL> Focas_LGLMES(BMSEmployee wLoginUser, int wYear) {
		List<FocasLGL> wResult = new ArrayList<FocasLGL>();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("y", wYear);

			String wUrl = StringUtils.Format(
					"http://10.200.10.19:8080/Portal/Statistics/LGLMES?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);

			@SuppressWarnings("unchecked")
			Map<String, Object> wMap = RemoteInvokeUtils.getInstance().HttpInvoke(wUrl, wParms, HttpMethod.GET,
					HashMap.class);
			wResult = CloneTool.CloneArray(wMap.get("data"), FocasLGL.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WMSReturn WMS_PostLL(WMSLinePartLLs wWMSLinePartLLs) {
		WMSReturn wResult = new WMSReturn();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("xmldata", wWMSLinePartLLs);

			String wUrl = "http://10.200.10.32:19192/datahub/gzloco/FluxWmsJsonApi/?client_customerid=FLUXWMSGJCMES&messageid=OUB_02";

			Map<String, Object> wMap = RemoteInvokeUtils.getInstance().HttpInvoke(wUrl, wParms, HttpMethod.POST,
					HashMap.class);
			wResult = CloneTool.Clone(((Map<String, Object>) wMap.get("Response")).get("return"), WMSReturn.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void WMS_TestSend() {
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			Map<String, String> wCustom = new HashMap<String, String>();
			wCustom.put("appId", "83e90e28cc6");
			wCustom.put("secretKey", "ua3Bhy9pv3ee9jfJSwNgjVVstvLzbrb8");
			wCustom.put("appUrl", "/MESCore/my_app/contain.html?username=012100090015");
			wParms.put("custom", wCustom);
			wParms.put("userAccountList", "012100090015");
			wParms.put("title", "通知标题3");
			wParms.put("content", "通知文本3");
			wParms.put("appName", "com.crrcgzgs.portal");
			wParms.put("insideAppName", "mes");
			wParms.put("type", 1);

			logger.info(JSON.toJSONString(wParms));

			String wUrl = "http://10.200.3.7:8081/gxhspush/platform/platFormJsonPushMsg.act";

			Map<String, Object> wMap = RemoteInvokeUtils.getInstance().HttpInvoke(wUrl, wParms, HttpMethod.POST,
					HashMap.class);

			logger.info(JSON.toJSONString(wMap));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
}
