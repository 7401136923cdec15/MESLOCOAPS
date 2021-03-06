package com.mes.loco.aps.server.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.mes.loco.aps.server.service.OMSService;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.FPCPartTypes;
import com.mes.loco.aps.server.service.mesenum.IPTMode;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.OMSCheckType;
import com.mes.loco.aps.server.service.mesenum.OMSOrderStatus;
import com.mes.loco.aps.server.service.mesenum.SFCTurnOrderTaskStatus;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.excel.ExcelData;
import com.mes.loco.aps.server.service.po.excel.ExcelLineData;
import com.mes.loco.aps.server.service.po.excel.ExcelSheetData;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCRoute;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.loco.aps.server.service.po.ipt.IPTItem;
import com.mes.loco.aps.server.service.po.ipt.IPTStandard;
import com.mes.loco.aps.server.service.po.mss.MSSBOM;
import com.mes.loco.aps.server.service.po.mss.MSSBOMItem;
import com.mes.loco.aps.server.service.po.oms.OMSCheckMsg;
import com.mes.loco.aps.server.service.po.oms.OMSCommand;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.oms.OMSOutsourceOrder;
import com.mes.loco.aps.server.service.po.oms.OMSPurchaseOrder;
import com.mes.loco.aps.server.service.po.rsm.RSMTurnOrderTask;
import com.mes.loco.aps.server.service.po.sap.OrderItem;
import com.mes.loco.aps.server.service.utils.Configuration;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.FMCServiceImpl;
import com.mes.loco.aps.server.serviceimpl.OMSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.QMSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskPartDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskStepDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSCommandDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOutsourceOrderDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSPurchaseOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;
import com.mes.loco.aps.server.utils.Constants;
import com.mes.loco.aps.server.utils.aps.ExcelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OMSServiceImpl implements OMSService {
	private static Logger logger = LoggerFactory.getLogger(OMSServiceImpl.class);

	private static OMSService Instance;

	public static OMSService getInstance() {
		if (Instance == null)
			Instance = new OMSServiceImpl();
		return Instance;
	}

	public ServiceResult<OMSOrder> OMS_QueryOMSOrder(BMSEmployee wLoginUser, int wID) {
		ServiceResult<OMSOrder> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			wResult.Result = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSOrder>> OMS_QueryOMSOrderList(BMSEmployee wLoginUser, int wID, int wCommandID,
			String wOrderNo, int wLineID, int wProductID, int wBureauSectionID, String wPartNo, String wBOMNo,
			int wActive, List<Integer> wStateIDList) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<OMSOrder>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSOrderDAO.getInstance().SelectList(wLoginUser, wID, wCommandID, wOrderNo, wLineID,
					wProductID, wBureauSectionID, wPartNo, wBOMNo, wActive, wStateIDList, null, null, null, null,
					wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Long> OMS_UpdateOMSOrder(BMSEmployee wLoginUser, OMSOrder wOMSOrder) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			if (wOMSOrder.RouteID <= 0) {
				wOMSOrder.RouteID = APSConstans.GetFPCRoute(wOMSOrder.ProductID, wOMSOrder.LineID,
						wOMSOrder.CustomerID).ID;
			}

			// ????????????????????????
			if (StringUtils.isNotEmpty(wOMSOrder.PartNo)) {
				if (wOMSOrder.ID <= 0) {
					List<OMSOrder> wList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1,
							wOMSOrder.PartNo, "", 1, null, null, null, null, null, wErrorCode);
					if (wList.size() > 0) {
						wResult.FaultCode += "?????????????????????!";
						return wResult;
					}
				} else {
					List<OMSOrder> wList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1,
							wOMSOrder.PartNo, "", 1, null, null, null, null, null, wErrorCode);
					if (wList.size() > 0 && wList.stream().anyMatch(p -> p.ID != wOMSOrder.ID)) {
						wResult.FaultCode += "?????????????????????!";
						return wResult;
					}
				}
			}

			// ???????????????????????????????????????????????????????????????????????? ?????????WBS???+???.???+???????????????
			// ?????????ID?????????0
			if (wOMSOrder.ID > 0) {
				// ???????????????ID????????????
				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOMSOrder.ID, wErrorCode);
				// ???????????????????????????????????????
				if (!wOMSOrder.OrderNo.equals(wOrder.OrderNo)) {
					// ?????????????????????????????????????????????
					if (wOMSOrder.Status != OMSOrderStatus.HasOrder.getValue()
							&& wOMSOrder.Status != OMSOrderStatus.Default.getValue()) {
						wResult.FaultCode += "?????????????????????????????????????????????????????????????????????!";
						return wResult;
					}
					// ??????????????????????????????????????????????????????
					String wPatten = StringUtils.Format("^{0}\\.[0-9]{1}$", wOMSOrder.WBSNo, "{3,}");
					boolean wIsMatch = Pattern.matches(wPatten, wOMSOrder.OrderNo);
					if (!wIsMatch) {
						wResult.FaultCode += "???????????????WBS??????????????????!";
						return wResult;
					}
					// ??????????????????????????????
					List<OMSOrder> wList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, wOMSOrder.OrderNo,
							-1, -1, -1, "", "", 1, null, null, null, null, null, wErrorCode);
					if (wList.size() > 0) {
						wResult.FaultCode += "?????????WBS?????????!";
						return wResult;
					}
				}
			}

			// ???????????????
			CalcStopTimes(wOMSOrder);

			wResult.Result = Long.valueOf(OMSOrderDAO.getInstance().Update(wLoginUser, wOMSOrder, wErrorCode));

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ???????????????
	 */
	private Calendar GetDate(Calendar wTime) {
		Calendar wResult = Calendar.getInstance();
		try {
			int wYear = wTime.get(Calendar.YEAR);
			int wMonth = wTime.get(Calendar.MONTH);
			int wDate = wTime.get(Calendar.DATE);

			wResult.set(wYear, wMonth, wDate, 0, 0, 0);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ????????????
	 */
	private void CalcStopTimes(OMSOrder wOMSOrder) {
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);
			// ?????????????????????(??????????????????-??????????????????)
			if (wOMSOrder.RealFinishDate.compareTo(wBaseTime) > 0
					&& wOMSOrder.RealReceiveDate.compareTo(wBaseTime) > 0) {
				wOMSOrder.ActualRepairStopTimes = APSUtils.getInstance()
						.CalIntervalDays(GetDate(wOMSOrder.RealReceiveDate), GetDate(wOMSOrder.RealFinishDate));
			} else {
				wOMSOrder.ActualRepairStopTimes = 0;
			}
			// ?????????????????????(??????????????????-??????????????????)
			if (wOMSOrder.CompletionTelegramTime.compareTo(wBaseTime) > 0
					&& wOMSOrder.TelegraphRealTime.compareTo(wBaseTime) > 0) {
				wOMSOrder.TelegraphRepairStopTimes = APSUtils.getInstance().CalIntervalDays(
						GetDate(wOMSOrder.TelegraphRealTime), GetDate(wOMSOrder.CompletionTelegramTime));
			} else {
				wOMSOrder.TelegraphRepairStopTimes = 0;
			}
			// ???????????????(????????????-??????????????????)
			if (wOMSOrder.RealSendDate.compareTo(wBaseTime) > 0
					&& wOMSOrder.TelegraphRealTime.compareTo(wBaseTime) > 0) {
				wOMSOrder.InPlantStopTimes = APSUtils.getInstance()
						.CalIntervalDays(GetDate(wOMSOrder.TelegraphRealTime), GetDate(wOMSOrder.RealSendDate));
			} else {
				wOMSOrder.InPlantStopTimes = 0;
			}
			// ???????????????( (????????????-????????????) + ( ??????????????????-????????????) )
			if (wOMSOrder.ToSegmentTime.compareTo(wBaseTime) > 0 && wOMSOrder.RealSendDate.compareTo(wBaseTime) > 0
					&& wOMSOrder.TelegraphRealTime.compareTo(wBaseTime) > 0
					&& wOMSOrder.TimeAway.compareTo(wBaseTime) > 0) {
				wOMSOrder.OnTheWayStopTimes = APSUtils.getInstance().CalIntervalDays(GetDate(wOMSOrder.RealSendDate),
						GetDate(wOMSOrder.ToSegmentTime))
						+ APSUtils.getInstance().CalIntervalDays(GetDate(wOMSOrder.TimeAway),
								GetDate(wOMSOrder.TelegraphRealTime));
			} else {
				wOMSOrder.OnTheWayStopTimes = 0;
			}
			// ???????????????(????????????-??????????????????)
			if (wOMSOrder.RealSendDate.compareTo(wBaseTime) > 0
					&& wOMSOrder.CompletionTelegramTime.compareTo(wBaseTime) > 0) {
				wOMSOrder.PosterioriStopTimes = APSUtils.getInstance()
						.CalIntervalDays(GetDate(wOMSOrder.CompletionTelegramTime), GetDate(wOMSOrder.RealSendDate));
			} else {
				wOMSOrder.PosterioriStopTimes = 0;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public ServiceResult<Integer> OMS_ActiveOMSOrderList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			OMSOrderDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<OMSPurchaseOrder> OMS_QueryOMSPurchaseOrder(BMSEmployee wLoginUser, int wID) {
		ServiceResult<OMSPurchaseOrder> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSPurchaseOrderDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSPurchaseOrder>> OMS_QueryOMSPurchaseOrderList(BMSEmployee wLoginUser, long wID,
			int wPlaceID, String wPartNo, int wMaterialID) {
		ServiceResult<List<OMSPurchaseOrder>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSPurchaseOrderDAO.getInstance().SelectList(wLoginUser, wID, wPlaceID, wPartNo,
					wMaterialID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Long> OMS_UpdateOMSPurchaseOrder(BMSEmployee wLoginUser, OMSPurchaseOrder wOMSPurchaseOrder) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long
					.valueOf(OMSPurchaseOrderDAO.getInstance().Update(wLoginUser, wOMSPurchaseOrder, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<OMSOutsourceOrder> OMS_QueryOMSOutsourceOrder(BMSEmployee wLoginUser, int wID) {
		ServiceResult<OMSOutsourceOrder> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSOutsourceOrderDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSOutsourceOrder>> OMS_QueryOMSOutsourceOrderList(BMSEmployee wLoginUser, long wID,
			int wStationID, int wOrderID, String wPartNo, int wMaterialID) {
		ServiceResult<List<OMSOutsourceOrder>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSOutsourceOrderDAO.getInstance().SelectList(wLoginUser, wID, wStationID, wOrderID,
					wPartNo, wMaterialID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Long> OMS_UpdateOMSOutsourceOrder(BMSEmployee wLoginUser,
			OMSOutsourceOrder wOMSOutsourceOrder) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long
					.valueOf(OMSOutsourceOrderDAO.getInstance().Update(wLoginUser, wOMSOutsourceOrder, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<OMSCommand> OMS_QueryCommand(BMSEmployee wLoginUser, int wID) {
		ServiceResult<OMSCommand> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSCommandDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSCommand>> OMS_QueryCommandList(BMSEmployee wLoginUser, int wID, int wActive,
			int wFactoryID, int wBusinessUnitID) {
		ServiceResult<List<OMSCommand>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSCommandDAO.getInstance().SelectList(wLoginUser, wID, wActive, wFactoryID,
					wBusinessUnitID, "", wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Long> OMS_UpdateCommand(BMSEmployee wLoginUser, OMSCommand wOMSCommand) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			if (StringUtils.isEmpty(wOMSCommand.WBSNo)) {
				wResult.FaultCode += "?????????WBS????????????!";
				return wResult;
			}

			// ????????????WBS?????????????????????????????????????????????
			if (wOMSCommand.ID > 0) {
				// ????????????????????????????????????
				boolean wIsHasChild = OMSCommandDAO.getInstance().IsHasChild(wLoginUser, wOMSCommand.ID, wErrorCode);
				// ??????????????????ID??????0??????????????????ID???????????????
				OMSCommand wCommand = OMSCommandDAO.getInstance().SelectByID(wLoginUser, wOMSCommand.ID, wErrorCode);
				// ?????????????????????WBS?????????????????????
				if (!wCommand.WBSNo.equals(wOMSCommand.WBSNo) && wIsHasChild) {
					wResult.FaultCode += "?????????????????????????????????????????????????????????????????????WBS???!";
					return wResult;
				}
			}

			wResult.Result = Long.valueOf(OMSCommandDAO.getInstance().Update(wLoginUser, wOMSCommand, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> OMS_ActiveCommandList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			OMSCommandDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSOutsourceOrder>> OMS_QueryOMSOutsourceOrderList(BMSEmployee wLoginUser,
			List<Integer> wOrderIDList) {
		ServiceResult<List<OMSOutsourceOrder>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSOutsourceOrderDAO.getInstance().SelectList(wLoginUser, wOrderIDList, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSOrder>> OMS_QueryHistoryOrderList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wAPSShiftPeriod) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>();
		try {
			// ???????????????
			if (wAPSShiftPeriod == APSShiftPeriod.Month.getValue()) {
				List<Integer> wStateIDList = new ArrayList<Integer>(Arrays.asList(2, 3, 4));

				List<OMSOrder> wList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "",
						1, wStateIDList, null, null, null, null, wErrorCode);
				if (wList == null || wList.size() <= 0) {
					return wResult;
				}
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2010, 0, 1);
				wResult.Result = wList.stream().filter(
						p -> wStateIDList.stream().anyMatch(q -> q == p.Status) && StringUtils.isNotEmpty(p.WBSNo))
						.collect(Collectors.toList());
			} else if (wAPSShiftPeriod == APSShiftPeriod.Week.getValue()) {// ???????????????
				List<Integer> wStateList = new ArrayList<>(
						Arrays.asList(OMSOrderStatus.HasOrder.getValue(), OMSOrderStatus.ReceivedTelegraph.getValue()));
				wResult.Result.addAll(OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "",
						1, wStateList, wStartTime, wEndTime, null, null, wErrorCode));

				wStateList = new ArrayList<>(
						Arrays.asList(OMSOrderStatus.EnterFactoryed.getValue(), OMSOrderStatus.Repairing.getValue()));
				wResult.Result.addAll(OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "",
						1, wStateList, null, null, null, null, wErrorCode));

				wStateList = new ArrayList<>(
						Arrays.asList(OMSOrderStatus.FinishedWork.getValue(), OMSOrderStatus.CarSended.getValue()));
				wResult.Result.addAll(OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "",
						1, wStateList, null, null, wStartTime, wEndTime, wErrorCode));
				// ??????????????????????????????????????? ???????????????
				wResult.Result.removeIf(p -> p.Status != OMSOrderStatus.EnterFactoryed.getValue()
						&& p.Status != OMSOrderStatus.Repairing.getValue()
						&& p.Status != OMSOrderStatus.ReceivedTelegraph.getValue());
			}

			// ??????
			if (wResult.Result.size() > 0) {
				wResult.Result.sort(Comparator.comparing(OMSOrder::getStatus, Comparator.reverseOrder())
						.thenComparing(OMSOrder::getRealReceiveDate));
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSOrder>> OMS_QueryRFOrderList(BMSEmployee wLoginUser, int wCustomerID, int wLineID,
			int wProductID, String wPartNo, Calendar wStartTime, Calendar wEndTime, int wActive) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = OMSOrderDAO.getInstance().SelectList_RF(wLoginUser, wCustomerID, wLineID, wProductID,
					wPartNo, wActive, wStartTime, wEndTime, wErrorCode);

			// ???????????????
			if (wResult.Result.size() > 0) {
				List<OMSOrder> wStationData = OMSOrderDAO.getInstance().GetStationData(wLoginUser,
						wResult.Result.stream().map(p -> p.ID).collect(Collectors.toList()), wErrorCode);
				for (OMSOrder wOMSOrder : wResult.Result) {
					if (wStationData.stream().anyMatch(p -> p.ID == wOMSOrder.ID)) {
						OMSOrder wItem = wStationData.stream().filter(p -> p.ID == wOMSOrder.ID).findFirst().get();
						wOMSOrder.StationTotalSize = wItem.StationTotalSize;
						wOMSOrder.StationFinishSize = wItem.StationFinishSize;
					}
				}
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ????????????????????????
	 */
	public ServiceResult<Integer> OMS_ChangeRoute(BMSEmployee wLoginUser, OMSOrder wOrder, Integer wRouteID) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			// ??????????????????????????????(??????????????????????????????)true?????????????????????false
			List<APSTaskPart> wMaxPlanList = this.OMS_QueryMaxPlanList(wLoginUser, wOrder, false);
			// ???????????????????????????????????????????????????????????????????????????
			String wCheckResult = this.CheckIsAllCanFindSamePart(wLoginUser, wMaxPlanList, wRouteID);
			if (StringUtils.isNotEmpty(wCheckResult)) {
				wResult.FaultCode += wCheckResult;
				return wResult;
			}
			// ????????????????????????????????????????????????????????????
			wCheckResult = this.CheckIsAllHasNextPart(wLoginUser, wMaxPlanList, wRouteID);
			if (StringUtils.isNotEmpty(wCheckResult)) {
				wResult.FaultCode += wCheckResult;
				return wResult;
			}
			// ???????????????????????????????????????????????????????????????????????????
			if (wMaxPlanList.size() > 0) {
				wMaxPlanList = wMaxPlanList.stream().filter(p -> p.Status != APSTaskStatus.Done.getValue())
						.collect(Collectors.toList());
				wCheckResult = this.CheckIsAllNextPartIsNotStart(wLoginUser, wMaxPlanList, wOrder, wRouteID);
				if (StringUtils.isNotEmpty(wCheckResult)) {
					wResult.FaultCode += wCheckResult;
					return wResult;
				}
			}

			wOrder.RouteID = wRouteID.intValue();
			OMSOrderDAO.getInstance().Update(wLoginUser, wOrder, wErrorCode);

			List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, wRouteID.intValue()).List(FPCRoutePart.class);
			if (wRoutePartList == null || wRoutePartList.size() <= 0) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "?????????????????????????????????!";
				return wResult;
			}

			int wOldRouteID = APSConstans.GetFPCRoute(wOrder.ProductID, wOrder.LineID, wOrder.CustomerID).ID;

			List<FPCRoutePart> wOldRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, wOldRouteID).List(FPCRoutePart.class);
			if (wOldRoutePartList == null || wOldRoutePartList.size() <= 0) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "???????????????????????????????????????!";
				return wResult;
			}

			List<FPCRoutePart> wNotExistList = (List<FPCRoutePart>) wOldRoutePartList.stream()
					.filter(p -> !wRoutePartList.stream().anyMatch(q -> q.PartID == p.PartID))
					.collect(Collectors.toList());
			if (wNotExistList == null || wNotExistList.size() <= 0) {
				return wResult;
			}

			List<RSMTurnOrderTask> wTurnOrderList = QMSServiceImpl.getInstance()
					.RSM_QueryTurnOrderTaskList(wLoginUser, wOrder.ID, -1, -1, null).List(RSMTurnOrderTask.class);
			if (wTurnOrderList == null) {
				wTurnOrderList = new ArrayList<>();
			}

			for (FPCRoutePart wFPCRoutePart : wNotExistList) {
				if (!wTurnOrderList.stream().anyMatch(p -> (p.TargetStationID == wFPCRoutePart.PartID))) {
					List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID,
							-1, wOrder.LineID, wFPCRoutePart.PartID, 1, APSShiftPeriod.Week.getValue(), null, -1, null,
							null, wErrorCode);
					wTaskPartList.forEach(p -> {
						p.Active = 2;
					});
					for (APSTaskPart wAPSTaskPart : wTaskPartList) {
						APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
					}
					continue;
				}
				if (wTurnOrderList.stream().anyMatch(p -> (p.TargetStationID == wFPCRoutePart.PartID
						&& p.Status == SFCTurnOrderTaskStatus.Passed.getValue()))) {
					List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID,
							-1, wOrder.LineID, wFPCRoutePart.PartID, 1, APSShiftPeriod.Week.getValue(), null, -1, null,
							null, wErrorCode);
					for (APSTaskPart wAPSTaskPart : wTaskPartList) {
						List<APSTaskStep> wStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1,
								wAPSTaskPart.ID, -1, -1, -1, -1, -1, 1, null, null, null, null, wErrorCode);
						if (wStepList == null || wStepList.size() <= 0) {
							wAPSTaskPart.Active = 2;
							APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
						}
					}
					continue;
				}
				List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1,
						wOrder.LineID, wFPCRoutePart.PartID, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null,
						wErrorCode);
				wTaskPartList.forEach(p -> {
					p.Active = 2;
				});
				for (APSTaskPart wAPSTaskPart : wTaskPartList) {
					APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ????????????????????????????????????????????????????????????????????????
	 */
	private String CheckIsAllNextPartIsNotStart(BMSEmployee wLoginUser, List<APSTaskPart> wMaxPlanList, OMSOrder wOrder,
			Integer wRouteID) {
		String wResult = "";
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wMaxPlanList == null || wMaxPlanList.size() <= 0) {
				return wResult;
			}

			// ?????????????????????ID??????????????????????????????
//			List<FPCRoutePart> wRoutePartList = APSConstans.mFPCRoutePartList.stream()
//					.filter(p -> p.RouteID == wRouteID).collect(Collectors.toList());
			List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, wRouteID).List(FPCRoutePart.class);
			// ??????????????????????????????????????????????????????????????????
			for (APSTaskPart wAPSTaskPart : wMaxPlanList) {
				if (!wRoutePartList.stream().anyMatch(p -> p.PartID == wAPSTaskPart.PartID)) {
					continue;
				}
				FPCRoutePart wRoutePart = wRoutePartList.stream().filter(p -> p.PartID == wAPSTaskPart.PartID)
						.findFirst().get();
				// ?????????????????????????????????????????????(??????????????????????????????)
				List<Integer> wPartIDList = new ArrayList<Integer>();
				if (wRoutePart.NextPartIDMap != null && wRoutePart.NextPartIDMap.size() > 0) {
					wPartIDList.addAll(wRoutePart.NextPartIDMap.keySet().stream().map(p -> Integer.parseInt(p))
							.collect(Collectors.toList()));
				}
				if (wRoutePartList.stream().anyMatch(p -> p.PrevPartID == wRoutePart.PartID)) {
					wPartIDList.addAll(wRoutePartList.stream().filter(p -> p.PrevPartID == wRoutePart.PartID)
							.map(p -> p.PartID).collect(Collectors.toList()));
				}
				if (wPartIDList == null || wPartIDList.size() <= 0) {
					continue;
				}
				// ????????????????????????ID?????????????????????????????????
				List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectListByPartIDList(wLoginUser,
						wAPSTaskPart.OrderID, wPartIDList, wErrorCode);
				// ?????????????????????????????????????????????????????????
				// ??????????????????????????????
				if (wList.stream().anyMatch(p -> p.Status == APSTaskStatus.Started.getValue()
						|| p.Status == APSTaskStatus.Done.getValue())) {
					wResult = StringUtils.Format("????????????{0}??????????????????????????????????????????!", wAPSTaskPart.PartName);
					return wResult;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 */
	private String CheckIsAllHasNextPart(BMSEmployee wLoginUser, List<APSTaskPart> wMaxPlanList, Integer wRouteID) {
		String wResult = "";
		try {
			if (wMaxPlanList == null || wMaxPlanList.size() <= 0) {
				return wResult;
			}

			// ?????????????????????ID????????????????????????
//			List<FPCRoutePart> wRoutePartList = APSConstans.mFPCRoutePartList.stream()
//					.filter(p -> p.RouteID == wRouteID).collect(Collectors.toList());
			List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, wRouteID).List(FPCRoutePart.class);
			// ?????????????????????????????????????????????????????????????????????????????????
			for (APSTaskPart wAPSTaskPart : wMaxPlanList) {
				if (!wRoutePartList.stream().anyMatch(p -> p.PartID == wAPSTaskPart.PartID)) {
					continue;
				}

				FPCRoutePart wRoutePart = wRoutePartList.stream().filter(p -> p.PartID == wAPSTaskPart.PartID)
						.findFirst().get();
				// ???????????????????????????????????????
				// ?????????????????????????????????????????????
				if ((wRoutePart.NextPartIDMap == null || wRoutePart.NextPartIDMap.size() <= 0)
						&& !wRoutePartList.stream().anyMatch(p -> p.PrevPartID == wRoutePart.PartID)) {
					wResult = StringUtils.Format("????????????{0}????????????????????????????????????????????????!", wAPSTaskPart.PartName);
					return wResult;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ????????????????????????????????????????????????????????????????????????
	 */
	private String CheckIsAllCanFindSamePart(BMSEmployee wLoginUser, List<APSTaskPart> wMaxPlanList, Integer wRouteID) {
		String wResult = "";
		try {
			if (wMaxPlanList == null || wMaxPlanList.size() <= 0) {
				return wResult;
			}

			// ?????????????????????ID??????????????????????????????
//			List<FPCRoutePart> wRoutePartList = APSConstans.mFPCRoutePartList.stream()
//					.filter(p -> p.RouteID == wRouteID).collect(Collectors.toList());
			List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, wRouteID).List(FPCRoutePart.class);
			if (wRoutePartList.size() <= 0) {
				wResult = StringUtils.Format("????????????{0}??????????????????????????????????????????!", wMaxPlanList.get(0).PartName);
				return wResult;
			}
			// ?????????????????????????????????????????????????????????????????????????????????
			if (!wMaxPlanList.stream().allMatch(p -> wRoutePartList.stream().anyMatch(q -> p.PartID == q.PartID))) {
				APSTaskPart wTaskPart = wMaxPlanList.stream()
						.filter(p -> !wRoutePartList.stream().anyMatch(q -> p.PartID == q.PartID)).findFirst().get();
				wResult = StringUtils.Format("????????????{0}??????????????????????????????????????????!", wTaskPart.PartName);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ???????????????????????????(??????????????????????????????)
	 */
	@SuppressWarnings("unchecked")
	private List<APSTaskPart> OMS_QueryMaxPlanList(BMSEmployee wLoginUser, OMSOrder wOrder, boolean wIsDayPlan) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// ?????????????????????????????????
			if (wIsDayPlan) {
				// ?????????????????????
//				List<FPCRoutePart> wRoutePartList = APSConstans.mFPCRoutePartList.stream()
//						.filter(p -> p.RouteID == wOrder.RouteID).collect(Collectors.toList());
				List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
						.FPC_QueryRoutePartListByRouteID(wLoginUser, wOrder.RouteID).List(FPCRoutePart.class);
				// ?????????????????????????????????????????????????????????
				List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectWeekPlanByOrderID(wLoginUser, wOrder.ID,
						wErrorCode);
				// ???????????????????????????
				for (APSTaskPart wAPSTaskPart : wList) {
					// ?????????????????????????????????????????????
					List<Integer> wNextPartIDList = APSUtils.getInstance().GetNextPartIDList(wAPSTaskPart.PartID,
							wRoutePartList);
					// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
					if (wNextPartIDList.size() <= 0
							|| !wNextPartIDList.stream().anyMatch(p -> wList.stream().anyMatch(q -> q.PartID == p))) {
						wResult.add(wAPSTaskPart);
					}
				}
			}
			// ???????????????????????????
			else {
				APIResult wAPIResult = QMSServiceImpl.getInstance().RSM_QueryBOPDoneList(wLoginUser, wOrder.ID);
				if (wAPIResult == null) {
					return wResult;
				}

				List<Integer> wDoingList = wAPIResult.Custom("DoingList", List.class);
				if (wDoingList == null || wDoingList.size() <= 0) {
					return wResult;
				}

				List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1,
						-1, -1, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null, wErrorCode);
				if (wTaskPartList == null || wTaskPartList.size() <= 0) {
					return wResult;
				}

				wResult = wTaskPartList.stream().filter(p -> wDoingList.stream().anyMatch(q -> q == p.PartID))
						.collect(Collectors.toList());
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSOrder>> OMS_QueryConditionAll(BMSEmployee wLoginUser, int wProductID, int wLine,
			int wCustomerID, String wWBSNo, Calendar wStartTime, Calendar wEndTime, int wStatus) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			wResult.Result = OMSOrderDAO.getInstance().ConditionAll(wLoginUser, wProductID, wLine, wCustomerID, wWBSNo,
					wStartTime, wEndTime, wStatus, wErrorCode);

			// ??????????????????????????????????????????wbs?????????
			if (wResult.Result.size() > 0) {
				// ???????????????????????????????????????
				List<OMSOrder> wHasList = wResult.Result.stream().filter(p -> p.Status >= 3)
						.collect(Collectors.toList());
				List<OMSOrder> wNoList = wResult.Result.stream().filter(p -> p.Status < 3).collect(Collectors.toList());
				wHasList.sort(Comparator.comparing(OMSOrder::getRealReceiveDate).thenComparing(OMSOrder::getOrderNo,
						Comparator.reverseOrder()));
				wNoList.sort(Comparator.comparing(OMSOrder::getOrderNo).thenComparing(OMSOrder::getPlanReceiveDate));

				List<OMSOrder> wList = new ArrayList<OMSOrder>();
				wList.addAll(wHasList);
				wList.addAll(wNoList);
				wResult.Result = wList;

//				wResult.Result
//						.sort(Comparator.comparing(OMSOrder::getOrderNo).thenComparing(OMSOrder::getPlanReceiveDate));
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> OMS_SaveList(BMSEmployee wLoginUser, List<OMSOrder> wOrderList) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			if (wOrderList == null || wOrderList.size() <= 0) {
				wResult.FaultCode = String.valueOf(wResult.FaultCode) + "?????????????????????!";
				return wResult;
			}

			OMSCommand wOMSCommand = null;
			for (OMSOrder wOMSOrder : wOrderList) {

				List<OMSCommand> wCommandList = OMSCommandDAO.getInstance().SelectList(wLoginUser, -1, -1, -1,
						wOMSOrder.ProductID, wOMSOrder.WBSNo, wErrorCode);

				if (wCommandList == null || wCommandList.size() <= 0) {
					wOMSCommand = new OMSCommand();
					wOMSCommand.Active = 1;
					wOMSCommand.AuditorID = wLoginUser.ID;
					wOMSCommand.BusinessUnitID = wOMSOrder.ProductID;
					wOMSCommand.ContactCode = wOMSOrder.ContactCode;
					wOMSCommand.CreateTime = Calendar.getInstance();
					wOMSCommand.CreatorID = wLoginUser.ID;
					wOMSCommand.CustomerID = wOMSOrder.BureauSectionID;
					wOMSCommand.EditorID = wLoginUser.ID;
					wOMSCommand.FactoryID = wOMSOrder.LineID;
					wOMSCommand.EditTime = Calendar.getInstance();
					wOMSCommand.FQTYPlan = wOMSOrder.FQTYPlan;
					wOMSCommand.FQTYActual = wOMSOrder.FQTYActual;
					wOMSCommand.ID = 0;
					wOMSCommand.LinkManID = wOMSOrder.LinkManID;
					wOMSCommand.No = wOMSOrder.No;
					wOMSCommand.Status = 1;
					wOMSCommand.WBSNo = wOMSOrder.WBSNo;
					wOMSCommand.ID = (int) OMSCommandDAO.getInstance().Update(wLoginUser, wOMSCommand, wErrorCode);
				} else {
					wOMSCommand = wCommandList.get(0);
				}

				wOMSOrder.CommandID = wOMSCommand.ID;
				int wNewID = (int) OMSOrderDAO.getInstance().Update(wLoginUser, wOMSOrder, wErrorCode);
				if (wNewID <= 0) {
					break;
				}
			}
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<OMSOrder>> OMS_QueryOrderListByIDList(BMSEmployee wLoginUser,
			List<Integer> wOrderIDList) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			wResult.Result = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_ReceivedOrder(BMSEmployee wLoginUser, List<OMSOrder> wOrderList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wOrderList == null || wOrderList.size() <= 0) {
				wResult.FaultCode += "?????????????????????!";
				return wResult;
			}

			if (wOrderList.stream().anyMatch(p -> p.Status != OMSOrderStatus.Default.getValue()
					&& p.Status != OMSOrderStatus.HasOrder.getValue())) {
				wResult.FaultCode += StringUtils
						.Format("????????????{0}?????????????????????????????????????????????!",
								wOrderList.stream()
										.filter(p -> p.Status != OMSOrderStatus.Default.getValue()
												&& p.Status != OMSOrderStatus.HasOrder.getValue())
										.findFirst().get().OrderNo);
				return wResult;
			}

			for (OMSOrder wOrder : wOrderList) {
//				wOrder.TelegraphTime = Calendar.getInstance();
				wOrder.AuditorID = wLoginUser.ID;
				wOrder.AuditTime = Calendar.getInstance();
//				wOrder.TelegraphRealTime = Calendar.getInstance();
//				wOrder.TelegraphTime = Calendar.getInstance();
				wOrder.Status = OMSOrderStatus.ReceivedTelegraph.getValue();
				wResult.Result = (int) OMSOrderDAO.getInstance().Update(wLoginUser, wOrder, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSCheckMsg>> OMS_Check(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<List<OMSCheckMsg>> wResult = new ServiceResult<List<OMSCheckMsg>>();
		wResult.Result = new ArrayList<OMSCheckMsg>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode += "???????????????????????????!";
				return wResult;
			}
			// ???????????????BOP
			if (wOrder.RouteID <= 0) {
				OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.BOP.getValue(),
						StringUtils.Format("????????????{0}???????????????????????????BOP!", wOrder.OrderNo), OMSCheckType.BOP.getLable());
				wResult.Result.add(wOMSCheckMsg);
			}
			FPCRoute wRoute = FMCServiceImpl.getInstance().FPC_QueryRouteByID(wLoginUser, wOrder.RouteID)
					.Info(FPCRoute.class);
			if (wRoute == null || wRoute.ID <= 0) {
				OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.BOP.getValue(),
						StringUtils.Format("????????????{0}??????????????????BOP????????????!", wOrder.OrderNo), OMSCheckType.BOP.getLable());
				wResult.Result.add(wOMSCheckMsg);
			}
			// ?????????????????????
			if (wOrder.RouteID > 0) {
				List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
						.FPC_QueryRoutePartListByRouteID(wLoginUser, wOrder.RouteID).List(FPCRoutePart.class);
				if (wRoutePartList == null || wRoutePartList.size() <= 0) {
					OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.BOP.getValue(),
							StringUtils.Format("????????????{0}????????????????????????????????????!", wOrder.OrderNo), OMSCheckType.BOP.getLable());
					wResult.Result.add(wOMSCheckMsg);
				}
				// ?????????????????????
				List<FPCRoutePartPoint> wRoutePartPointList = FMCServiceImpl.getInstance()
						.FPC_QueryRoutePartPointListByRouteID(wLoginUser, wOrder.RouteID, -1)
						.List(FPCRoutePartPoint.class);
				if (wRoutePartPointList == null) {
					wRoutePartPointList = new ArrayList<FPCRoutePartPoint>();
				}
				if (wRoutePartList != null && wRoutePartList.size() > 0) {
					for (FPCRoutePart wFPCRoutePart : wRoutePartList) {
						if (!wRoutePartPointList.stream().anyMatch(p -> p.PartID == wFPCRoutePart.PartID)) {
							OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.BOP.getValue(),
									StringUtils.Format("????????????{0}???-???{1}?????????????????????????????????!", wOrder.OrderNo,
											APSConstans.GetFPCPartName(wFPCRoutePart.PartID)),
									OMSCheckType.BOP.getLable());
							wResult.Result.add(wOMSCheckMsg);
							continue;
						}
						// ???????????????
						List<FPCRoutePartPoint> wList = wRoutePartPointList.stream()
								.filter(p -> p.PartID == wFPCRoutePart.PartID).collect(Collectors.toList());
						IPTStandard wStandard = null;
						for (FPCRoutePartPoint wFPCRoutePartPoint : wList) {
							// ????????????????????????????????????????????????
							FPCPart wFPCPart = APSConstans.GetFPCPart(wFPCRoutePart.PartID);
							switch (FPCPartTypes.getEnumType(wFPCPart.PartType)) {
							case Product:// ????????????
								// ?????????????????????????????????????????????????????????????????????????????????
								wStandard = QMSServiceImpl.getInstance()
										.IPT_QueryStandardCurrent(wLoginUser, IPTMode.QTXJ.getValue(), wOrder.LineID,
												wFPCRoutePart.PartID, wFPCRoutePartPoint.PartPointID, wOrder.ProductNo,
												-1)
										.Info(IPTStandard.class);
								if (wStandard == null || wStandard.ID <= 0) {
									OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
											StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}??????????????????????????????!",
													wOrder.OrderNo, wOrder.ProductNo, wOrder.LineName,
													wFPCRoutePartPoint.PartName, wFPCRoutePartPoint.PartPointName),
											OMSCheckType.Standard.getLable());
									wResult.Result.add(wOMSCheckMsg);
								} else {
									// ??????????????????????????????
									List<IPTItem> wItemList = QMSServiceImpl.getInstance()
											.IPT_QueryItemList(wLoginUser, (int) wStandard.ID).List(IPTItem.class);
									if (wItemList == null || wItemList.size() <= 0) {
										OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
												StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}????????????????????????????????????!",
														wOrder.OrderNo, wOrder.ProductNo, wOrder.LineName,
														wFPCRoutePartPoint.PartName, wFPCRoutePartPoint.PartPointName),
												OMSCheckType.Standard.getLable());
										wResult.Result.add(wOMSCheckMsg);
									}
								}
								break;
							case PrevCheck:// ????????????
								wStandard = QMSServiceImpl.getInstance()
										.IPT_QueryStandardCurrent(wLoginUser, IPTMode.PreCheck.getValue(),
												wOrder.LineID, wFPCRoutePart.PartID, wFPCRoutePartPoint.PartPointID,
												wOrder.ProductNo, -1)
										.Info(IPTStandard.class);
								if (wStandard == null || wStandard.ID <= 0) {
									OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
											StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}????????????????????????!",
													wOrder.OrderNo, wOrder.ProductNo, wOrder.LineName,
													wFPCRoutePartPoint.PartName, wFPCRoutePartPoint.PartPointName),
											OMSCheckType.Standard.getLable());
									wResult.Result.add(wOMSCheckMsg);
								} else {
									// ??????????????????????????????
									List<IPTItem> wItemList = QMSServiceImpl.getInstance()
											.IPT_QueryItemList(wLoginUser, (int) wStandard.ID).List(IPTItem.class);
									if (wItemList == null || wItemList.size() <= 0) {
										OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
												StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}??????????????????????????????!",
														wOrder.OrderNo, wOrder.ProductNo, wOrder.LineName,
														wFPCRoutePartPoint.PartName, wFPCRoutePartPoint.PartPointName),
												OMSCheckType.Standard.getLable());
										wResult.Result.add(wOMSCheckMsg);
									}
								}

								wStandard = QMSServiceImpl.getInstance()
										.IPT_QueryStandardCurrent(wLoginUser, IPTMode.PeriodChange.getValue(),
												wOrder.LineID, wFPCRoutePart.PartID, wFPCRoutePartPoint.PartPointID,
												wOrder.ProductNo, wOrder.CustomerID)
										.Info(IPTStandard.class);
								if (wStandard == null || wStandard.ID <= 0) {
									OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
											StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}???-???{5}????????????????????????!",
													wOrder.OrderNo, wOrder.ProductNo, wOrder.LineName, wOrder.Customer,
													wFPCRoutePartPoint.PartName, wFPCRoutePartPoint.PartPointName),
											OMSCheckType.Standard.getLable());
									wResult.Result.add(wOMSCheckMsg);
								} else {
									// ??????????????????????????????
									List<IPTItem> wItemList = QMSServiceImpl.getInstance()
											.IPT_QueryItemList(wLoginUser, (int) wStandard.ID).List(IPTItem.class);
									if (wItemList == null || wItemList.size() <= 0) {
										OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
												StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}???-???{5}??????????????????????????????!",
														wOrder.OrderNo, wOrder.ProductNo, wOrder.LineName,
														wOrder.Customer, wFPCRoutePartPoint.PartName,
														wFPCRoutePartPoint.PartPointName),
												OMSCheckType.Standard.getLable());
										wResult.Result.add(wOMSCheckMsg);
									}
								}
								break;
							case QTFinally:// ????????????
								wStandard = QMSServiceImpl.getInstance()
										.IPT_QueryStandardCurrent(wLoginUser, IPTMode.Quality.getValue(), wOrder.LineID,
												wFPCRoutePart.PartID, wFPCRoutePartPoint.PartPointID, wOrder.ProductNo,
												-1)
										.Info(IPTStandard.class);
								if (wStandard == null || wStandard.ID <= 0) {
									OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
											StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}??????????????????!", wOrder.OrderNo,
													wOrder.ProductNo, wOrder.LineName, wFPCRoutePartPoint.PartName,
													wFPCRoutePartPoint.PartPointName),
											OMSCheckType.Standard.getLable());
									wResult.Result.add(wOMSCheckMsg);
								} else {
									// ??????????????????????????????
									List<IPTItem> wItemList = QMSServiceImpl.getInstance()
											.IPT_QueryItemList(wLoginUser, (int) wStandard.ID).List(IPTItem.class);
									if (wItemList == null || wItemList.size() <= 0) {
										OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
												StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}??????????????????????????????!",
														wOrder.OrderNo, wOrder.ProductNo, wOrder.LineName,
														wFPCRoutePartPoint.PartName, wFPCRoutePartPoint.PartPointName),
												OMSCheckType.Standard.getLable());
										wResult.Result.add(wOMSCheckMsg);
									}
								}
								break;
							case OutFactory:// ????????????
								wStandard = QMSServiceImpl.getInstance()
										.IPT_QueryStandardCurrent(wLoginUser, IPTMode.OutCheck.getValue(),
												wOrder.LineID, wFPCRoutePart.PartID, wFPCRoutePartPoint.PartPointID,
												wOrder.ProductNo, wOrder.CustomerID)
										.Info(IPTStandard.class);
								if (wStandard == null || wStandard.ID <= 0) {
									OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
											StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}??????????????????!", wOrder.OrderNo,
													wOrder.ProductNo, wOrder.LineName, wFPCRoutePartPoint.PartName,
													wFPCRoutePartPoint.PartPointName),
											OMSCheckType.Standard.getLable());
									wResult.Result.add(wOMSCheckMsg);
								} else {
									// ??????????????????????????????
									List<IPTItem> wItemList = QMSServiceImpl.getInstance()
											.IPT_QueryItemList(wLoginUser, (int) wStandard.ID).List(IPTItem.class);
									if (wItemList == null || wItemList.size() <= 0) {
										OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
												StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}??????????????????????????????!",
														wOrder.OrderNo, wOrder.ProductNo, wOrder.LineName,
														wFPCRoutePartPoint.PartName, wFPCRoutePartPoint.PartPointName),
												OMSCheckType.Standard.getLable());
										wResult.Result.add(wOMSCheckMsg);
									}
								}
								break;
							default:
								break;
							}
						}
					}
				}
			}
			// ???????????????BOM
			List<MSSBOM> wBOMList = WMSServiceImpl.getInstance()
					.MSS_QueryBOMAll(wLoginUser, "", "", -1, -1, wOrder.ProductID, -1).List(MSSBOM.class);
			if (wBOMList == null || wBOMList.size() <= 0) {
				OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(
						OMSCheckType.BOM.getValue(), StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}??????BOM??????!",
								wOrder.OrderNo, wOrder.Customer, wOrder.LineName, wOrder.ProductNo),
						OMSCheckType.BOM.getLable());
				wResult.Result.add(wOMSCheckMsg);
			} else {
				if (!wBOMList.stream().anyMatch(p -> p.ProductID == wOrder.ProductID
						&& p.CustomerID == wOrder.CustomerID && p.LineID == wOrder.LineID && p.IsStandard == 1)) {
					OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(
							OMSCheckType.BOM.getValue(), StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???????????????BOM!",
									wOrder.OrderNo, wOrder.Customer, wOrder.LineName, wOrder.ProductNo),
							OMSCheckType.BOM.getLable());
					wResult.Result.add(wOMSCheckMsg);
				} else {
					MSSBOM wBOM = wBOMList.stream().filter(p -> p.ProductID == wOrder.ProductID
							&& p.CustomerID == wOrder.CustomerID && p.LineID == wOrder.LineID && p.IsStandard == 1)
							.findFirst().get();
					List<MSSBOMItem> wList = WMSServiceImpl.getInstance()
							.MSS_QueryBOMItemAll(wLoginUser, wBOM.ID, -1, -1, -1, -1, -1, -1, -1, -1)
							.List(MSSBOMItem.class);
					if (wList == null || wList.size() <= 0) {
						OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.BOM.getValue(),
								StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}????????????BOM?????????BOM!", wOrder.OrderNo,
										wOrder.Customer, wOrder.LineName, wOrder.ProductNo),
								OMSCheckType.BOM.getLable());
						wResult.Result.add(wOMSCheckMsg);
					}
				}
			}
			// ??????????????????

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> ExportCheck(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<OMSCheckMsg> wCheckList = this.OMS_Check(wLoginUser, wOrderID).Result;
			if (wCheckList == null || wCheckList.size() <= 0) {
				wResult.FaultCode += "??????????????????????????????!";
				return wResult;
			}

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

			SimpleDateFormat wSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String wCurTime = wSimpleDateFormat.format(Calendar.getInstance().getTime());
			String wFileName = StringUtils.Format("{0}-{1}-????????????????????????.xls",
					new Object[] { wCurTime, wOrder.PartNo.replace("#", "-") });
			String wDirePath = StringUtils.Format("{0}static/export/",
					new Object[] { Constants.getConfigPath().replace("config/", "") });
			File wDirFile = new File(wDirePath);
			if (!wDirFile.exists()) {
				wDirFile.mkdirs();
			}
			String wFilePath = StringUtils.Format("{0}{1}", new Object[] { wDirePath, wFileName });
			File wNewFile = new File(wFilePath);
			wNewFile.createNewFile();

			FileOutputStream wFileOutputStream = new FileOutputStream(wNewFile);
			ExcelUtil.OMS_WriteCheckReport(wCheckList, wFileOutputStream, wOrder);
			String wUri = StringUtils.Format("/{0}/export/{1}",
					new Object[] { Configuration.readConfigString("project.name", "application"), wFileName });
			wResult.Result = wUri;
			wResult.setFaultCode(MESException.getEnumType((int) wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_CheckRouteBOM(BMSEmployee wLoginUser, List<OMSOrder> wOrderList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			if (wOrderList == null || wOrderList.size() <= 0) {
				return wResult;
			}

			for (OMSOrder wOMSOrder : wOrderList) {
				MSSBOM wBOM = WMSServiceImpl.getInstance().MSS_QueryBOM(wLoginUser, -1, "", wOMSOrder.RouteID)
						.Info(MSSBOM.class);
				if (wBOM == null || wBOM.ID <= 0) {
					wResult.FaultCode = StringUtils.Format("????????????{0}?????????????????????BOP?????????BOM!", wOMSOrder.OrderNo);
					return wResult;
				}
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_ClearCar(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode = "???????????????!";
				return wResult;
			}

			// ?????????????????????(?????????)
			OMSOrderDAO.getInstance().ClearCar(wLoginUser, wOrder);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);

			wOrder.Status = 8;
			if (wOrder.RealFinishDate.compareTo(wBaseTime) < 0) {
				wOrder.RealFinishDate = Calendar.getInstance();
			}
			wOrder.RealSendDate = Calendar.getInstance();

			OMSOrderDAO.getInstance().Update(wLoginUser, wOrder, wErrorCode);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSOrder>> OMS_QueryAll(BMSEmployee wLoginUser, List<Integer> wCustomerList,
			List<Integer> wLineList, Calendar wStartTime, Calendar wEndTime, List<Integer> wStatusList,
			List<Integer> wProductList, String wPartNo, List<Integer> wActiveList) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wCustomerList != null && wCustomerList.size() > 0) {
				wCustomerList.removeIf(p -> p == -1);
			}
			if (wLineList != null && wLineList.size() > 0) {
				wLineList.removeIf(p -> p == -1);
			}
			if (wStatusList != null && wStatusList.size() > 0) {
				wStatusList.removeIf(p -> p == -1);
			}
			if (wProductList != null && wProductList.size() > 0) {
				wProductList.removeIf(p -> p == -1);
			}
			if (wActiveList != null && wActiveList.size() > 0) {
				wActiveList.removeIf(p -> p == -1);
			}

			wResult.Result = OMSOrderDAO.getInstance().SelectList(wLoginUser, wCustomerList, wLineList, wStartTime,
					wEndTime, wStatusList, wProductList, wPartNo, wActiveList, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSOrder>> OMS_QueryInPlantCarList(BMSEmployee wLoginUser) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ?????????????????????????????????????????????
			List<String> wPartNoList = OMSOrderDAO.getInstance().OMS_QueryPartNoListInPlant(wLoginUser, wErrorCode);
			// ?????????????????????
			if (wPartNoList.size() <= 0) {
				return wResult;
			}
			for (String wPartNo : wPartNoList) {
				wPartNo = wPartNo.replace("[A]", "").replace("[B]", "");
			}
			// ?????????????????????????????????????????????????????????ID???????????????????????????????????????
			List<Integer> wIDList = OMSOrderDAO.getInstance().OMS_QueryInPlantCarList(wLoginUser, wPartNoList,
					wErrorCode);

			// ??????????????????????????????????????????????????????ID???????????????????????????????????????????????????
			List<Integer> wID1List = OMSOrderDAO.getInstance().OMS_QueryInPlantCarList_Repairing(wLoginUser,
					wPartNoList, wErrorCode);
			wIDList.addAll(wID1List);

			// ?????????????????????
			wResult.Result = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wIDList, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_ClearCarPro(BMSEmployee wLoginUser, List<Integer> wOrderIDList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			for (Integer wOrderID : wOrderIDList) {
				OMS_ClearCar(wLoginUser, wOrderID);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_HandleRepeatCommand(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ?????????????????????WBS
			List<String> wWBSNoList = OMSCommandDAO.getInstance().SelectRepeatWBSNoList(wLoginUser, wErrorCode);
			// ??????????????????
			for (String wWBS : wWBSNoList) {
				List<OMSCommand> wCommandList = OMSCommandDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, wWBS,
						wErrorCode);
				for (int i = 1; i < wCommandList.size(); i++) {
					// ????????????command
					OMSCommandDAO.getInstance().DeleteList(wLoginUser,
							new ArrayList<OMSCommand>(Arrays.asList(wCommandList.get(i))), wErrorCode);
					// ?????????????????????
					List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1,
							wCommandList.get(i).ID, "", -1, -1, -1, "", "", -1, null, null, null, null, null,
							wErrorCode);
					for (OMSOrder wOrder : wOrderList) {
						// ???????????????commandID
						wOrder.CommandID = wCommandList.get(0).ID;
						OMSOrderDAO.getInstance().Update(wLoginUser, wOrder, wErrorCode);
					}
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> OMS_SendToSAP(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				return wResult;
			}

			wResult.Result = SendToSAP(wLoginUser, wOrder);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * ?????????SAP
	 */
	private String SendToSAP(BMSEmployee wLoginUser, OMSOrder wOrder) {
		String wResult = "";
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

			wResult = (String) wCall.invoke(new Object[] { "02", wINPUT2 });// ????????????
			logger.info(wResult);
		} catch (Exception ex) {
			wResult = ex.toString();
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_DeleteByExcel(BMSEmployee wLoginUser, ExcelData wExcelData) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<String> wWBSList = new ArrayList<String>();
			for (ExcelSheetData wExcelSheetData : wExcelData.sheetData) {
				for (ExcelLineData wExcelLineData : wExcelSheetData.lineData) {
					String wWBSNo = wExcelLineData.colData.get(1);
					if (StringUtils.isNotEmpty(wWBSNo)) {
						wWBSList.add(wWBSNo);
					}
				}
			}

			wResult.Result = OMSOrderDAO.getInstance().DeleteByWBSNoList(wLoginUser, wWBSList, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSCheckMsg>> OMS_Check_V2(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<List<OMSCheckMsg>> wResult = new ServiceResult<List<OMSCheckMsg>>();
		wResult.Result = new ArrayList<OMSCheckMsg>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			// ?????????bop
			if (wOrder.RouteID <= 0) {
				OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.BOP.getValue(),
						StringUtils.Format("????????????{0}???????????????????????????BOP!", wOrder.PartNo), OMSCheckType.BOP.getLable());
				wResult.Result.add(wOMSCheckMsg);
			}
			// ?????????bom
			boolean wCheckResult = OMSOrderDAO.getInstance().IsSettedCurrentStandardBOM(wLoginUser, wOrder, wErrorCode);
			if (!wCheckResult) {
				OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.BOM.getValue(),
						StringUtils.Format("????????????{0}?????????????????????????????????BOM!", wOrder.PartNo), OMSCheckType.BOM.getLable());
				wResult.Result.add(wOMSCheckMsg);
			}
			// ???????????????
			List<FPCRoutePartPoint> wList = OMSOrderDAO.getInstance().SelectNoSettedStandardList(wLoginUser, wOrder,
					wErrorCode);
			for (FPCRoutePartPoint wFPCRoutePartPoint : wList) {
				OMSCheckMsg wOMSCheckMsg = new OMSCheckMsg(OMSCheckType.Standard.getValue(),
						StringUtils.Format("????????????{0}???-???{1}???-???{2}???-???{3}???-???{4}??????????????????????????????!", wOrder.PartNo,
								wOrder.ProductNo, wOrder.LineName, wFPCRoutePartPoint.PartName,
								wFPCRoutePartPoint.PartPointName),
						OMSCheckType.Standard.getLable());
				wResult.Result.add(wOMSCheckMsg);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_DisablePlan(BMSEmployee wLoginUser, String wOrders) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (StringUtils.isEmpty(wOrders)) {
				return wResult;
			}
			// ??????????????????
			List<Integer> wOrderIDList = StringUtils.parseIntList(wOrders.split(","));
			// ??????????????????
			OMSOrderDAO.getInstance().DisableAPSTaskStep(wLoginUser, wOrderIDList, wErrorCode);
			// ?????????????????????
			List<Integer> wSFCTaskStepIDList = OMSOrderDAO.getInstance().GetSFCTaskStepIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			if (wSFCTaskStepIDList.size() > 0) {
				OMSOrderDAO.getInstance().DisableSFCTaskStep(wLoginUser, wSFCTaskStepIDList, wErrorCode);
			}
			// ??????????????????
			OMSOrderDAO.getInstance().DisableSFCTaskIPT(wLoginUser, wOrderIDList, wErrorCode);
			// ?????????????????????
			List<Integer> wPGMessageIDList = OMSOrderDAO.getInstance().GetPGMessageIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			if (wPGMessageIDList.size() > 0) {
				OMSOrderDAO.getInstance().DisableMessageByIDList(wLoginUser, wPGMessageIDList, wErrorCode);
			}
			// ????????????????????????????????????????????????
			List<Integer> wIPTMessageIDList = OMSOrderDAO.getInstance().GetIPTMessageIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			if (wIPTMessageIDList.size() > 0) {
				OMSOrderDAO.getInstance().DisableMessageByIDList(wLoginUser, wIPTMessageIDList, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSOrder>> OMS_QuerySelfOrderList(BMSEmployee wLoginUser, int wOrderType) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ??????????????????????????????????????????ID??????
			List<Integer> wIDList = OMSOrderDAO.getInstance().SelectFinishedSelfOrderIDListByOrderType(wLoginUser,
					wOrderType, wErrorCode);
			// ???????????????ID????????????????????????
			wResult.Result = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wIDList, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_ReparingOrders(BMSEmployee wLoginUser, List<Integer> wOrderIDList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			for (int wOrderID : wOrderIDList) {
				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
				if (wOrder == null || wOrder.ID <= 0) {
					continue;
				}

				wOrder.Status = OMSOrderStatus.Repairing.getValue();
				wOrder.RealStartDate = Calendar.getInstance();
				OMSOrderDAO.getInstance().Update(wLoginUser, wOrder, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_SaveOrder(BMSEmployee wLoginUser, List<OMSOrder> wList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wERPIDList = wList.stream().map(p -> p.ERPID).collect(Collectors.toList());

			// ??????
			Collections.sort(wERPIDList);

			int wIndex = 0;
			for (OMSOrder omsOrder : wList) {
				omsOrder.ERPID = wERPIDList.get(wIndex);
				OMSOrderDAO.getInstance().Update(wLoginUser, omsOrder, wErrorCode);
				wIndex++;
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}
}
