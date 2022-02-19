package com.mes.loco.aps.server.serviceimpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.mes.loco.aps.server.service.AndonService;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.BFCMessageType;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.OMSOrderStatus;
import com.mes.loco.aps.server.service.mesenum.SFCTurnOrderTaskStatus;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.AndonConfig;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanAreaCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanClassCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanPartCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayReport01;
import com.mes.loco.aps.server.service.po.andon.AndonDayReportCar;
import com.mes.loco.aps.server.service.po.andon.AndonDayReportDate;
import com.mes.loco.aps.server.service.po.andon.AndonDayReportPart;
import com.mes.loco.aps.server.service.po.andon.AndonJournal;
import com.mes.loco.aps.server.service.po.andon.AndonJournalItem;
import com.mes.loco.aps.server.service.po.andon.AndonLocomotiveProductionStatus;
import com.mes.loco.aps.server.service.po.andon.AndonOrder;
import com.mes.loco.aps.server.service.po.andon.AndonScreen;
import com.mes.loco.aps.server.service.po.andon.AndonStationInfo;
import com.mes.loco.aps.server.service.po.andon.AndonStopStatistics;
import com.mes.loco.aps.server.service.po.andon.AndonTaskStep;
import com.mes.loco.aps.server.service.po.andon.AndonYearFinishSituation;
import com.mes.loco.aps.server.service.po.andon.FocasLGL;
import com.mes.loco.aps.server.service.po.andon.OrderTimeInfo;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepPlan;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bms.BMSWorkCharge;
import com.mes.loco.aps.server.service.po.excel.MyExcelSheet;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkCharge;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.loco.aps.server.service.po.lfs.LFSStoreHouse;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.rsm.RSMTurnOrderTask;
import com.mes.loco.aps.server.service.utils.CalendarUtil;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.andon.AndonConfigDAO;
import com.mes.loco.aps.server.serviceimpl.dao.andon.AndonDAO;
import com.mes.loco.aps.server.serviceimpl.dao.andon.AndonJournalDAO;
import com.mes.loco.aps.server.serviceimpl.dao.andon.AndonJournalItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskPartDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskStepDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;
import com.mes.loco.aps.server.utils.aps.ExcelUtil;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2019年12月27日12:46:35
 * @LastEditTime 2020-4-29 16:45:02
 *
 */
@Service
public class AndonServiceImpl implements AndonService {

	private static Logger logger = LoggerFactory.getLogger(AndonServiceImpl.class);

	public AndonServiceImpl() {
	}

	private static AndonService Instance;

	public static AndonService getInstance() {
		if (Instance == null)
			Instance = new AndonServiceImpl();
		return Instance;
	}

	@Override
	public ServiceResult<List<OrderTimeInfo>> Andon_QueryTrainOrderInfoList(BMSEmployee wLoginUser) {
		ServiceResult<List<OrderTimeInfo>> wResult = new ServiceResult<List<OrderTimeInfo>>();
		wResult.Result = new ArrayList<OrderTimeInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance()
					.SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "", 1, new ArrayList<Integer>(Arrays
							.asList(OMSOrderStatus.EnterFactoryed.getValue(), OMSOrderStatus.Repairing.getValue())),
							null, null, null, null, wErrorCode);
			if (wOrderList == null || wOrderList.size() <= 0) {
				return wResult;
			}

			// 库位列表
			List<LFSStoreHouse> wStoreHouseList = LFSServiceImpl.getInstance().LFS_QueryStoreHouseList(wLoginUser)
					.List(LFSStoreHouse.class);
			// 台位列表
			List<FMCWorkspace> wSpaceList = FMCServiceImpl.getInstance()
					.FMC_GetFMCWorkspaceList(wLoginUser, -1, -1, "", -1, 1).List(FMCWorkspace.class);
			// 工艺数据
//			List<FPCRoutePart> wFPCRoutePartList = APSConstans.mFPCRoutePartList;
//			List<FPCRoutePartPoint> wFPCRoutePartPointList = APSConstans.mFPCRoutePartPointList;
			List<FPCRoutePart> wFPCRoutePartList = new ArrayList<FPCRoutePart>();
			List<FPCRoutePartPoint> wFPCRoutePartPointList = new ArrayList<FPCRoutePartPoint>();

			// 验证数据是否有(防止服务重启报空指针异常)
			if (wStoreHouseList == null || wStoreHouseList.size() <= 0 || wSpaceList == null || wSpaceList.size() <= 0
					|| wFPCRoutePartList == null || wFPCRoutePartList.size() <= 0 || wFPCRoutePartPointList == null
					|| wFPCRoutePartPointList.size() <= 0) {
				return wResult;
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			OrderTimeInfo wOrderTimeInfo = null;
			ServiceResult<Integer> wNumberInfo = null;
			for (OMSOrder wOMSOrder : wOrderList) {
				List<AndonStationInfo> wStationInfoList = getStationInfo(wLoginUser, wOMSOrder, wFPCRoutePartList,
						wFPCRoutePartPointList);

				OrderTimeInfo wInfo = AndonDAO.getInstance().QueryStationProgress(wLoginUser, wOMSOrder.ID, wErrorCode);

				if (wStationInfoList == null || wStationInfoList.size() <= 0) {
					wOrderTimeInfo = new OrderTimeInfo();

					wOrderTimeInfo.OrderID = wOMSOrder.ID;
					// 配属局段
					wOrderTimeInfo.CustomerName = wOMSOrder.BureauSection;
					// 修程
					wOrderTimeInfo.LineName = wOMSOrder.LineName;
					// 台车编号
					wOrderTimeInfo.PartNo = wOMSOrder.PartNo;
					// 进厂时刻
					wOrderTimeInfo.InPlantTime = wOMSOrder.RealReceiveDate;
					// 进厂日期
					wOrderTimeInfo.RealReceiveDate = wSDF.format(wOMSOrder.RealReceiveDate.getTime());
					// 停时
					wOrderTimeInfo.StopTime = getDayInterval(wOMSOrder.RealReceiveDate.getTime(),
							Calendar.getInstance().getTime());
					// 台位
					wOrderTimeInfo.PlaceName = getPlaceName(wSpaceList, wOMSOrder);
					// 库位
					wOrderTimeInfo.StockName = getStockName(wStoreHouseList, wSpaceList, wOMSOrder);

					wNumberInfo = getNumberInfo(wLoginUser, wOMSOrder, 0);
					// 异常数
					wOrderTimeInfo.ExcNum = (int) wNumberInfo.CustomResult.get("FQTYException");
					// Ncr数
					wOrderTimeInfo.NcrNum = (int) wNumberInfo.CustomResult.get("FQTYNcr");
					// 返修数
					wOrderTimeInfo.ReworkNum = (int) wNumberInfo.CustomResult.get("FQTYRepair");

					// 完成进度
					wOrderTimeInfo.Progress = 0;
					// 剩余数
					wOrderTimeInfo.SurplusNum = 0;
					// 完成数
					wOrderTimeInfo.FinishNum = 0;
					// 工位
					wOrderTimeInfo.StationName = "";
					// 状态
					wOrderTimeInfo.Status = "";
					// 工位总数
					wOrderTimeInfo.STTotal = wInfo.STTotal;
					// 工位完成数
					wOrderTimeInfo.STFinish = wInfo.STFinish;

					wResult.Result.add(wOrderTimeInfo);
				} else {
					for (AndonStationInfo wAndonStationInfo : wStationInfoList) {
						wOrderTimeInfo = new OrderTimeInfo();

						wOrderTimeInfo.OrderID = wOMSOrder.ID;
						// 配属局段
						wOrderTimeInfo.CustomerName = wOMSOrder.BureauSection;
						// 修程
						wOrderTimeInfo.LineName = wOMSOrder.LineName;
						// 台车编号
						wOrderTimeInfo.PartNo = wOMSOrder.PartNo;
						// 进厂时刻
						wOrderTimeInfo.InPlantTime = wOMSOrder.RealReceiveDate;
						// 进厂日期
						wOrderTimeInfo.RealReceiveDate = wSDF.format(wOMSOrder.RealReceiveDate.getTime());
						// 停时
						wOrderTimeInfo.StopTime = getDayInterval(wOMSOrder.RealReceiveDate.getTime(),
								Calendar.getInstance().getTime());
						// 台位
						wOrderTimeInfo.PlaceName = getPlaceName(wSpaceList, wOMSOrder);
						// 库位
						wOrderTimeInfo.StockName = getStockName(wStoreHouseList, wSpaceList, wOMSOrder);

						wNumberInfo = getNumberInfo(wLoginUser, wOMSOrder, wAndonStationInfo.StationID);
						// 异常数
						wOrderTimeInfo.ExcNum = (int) wNumberInfo.CustomResult.get("FQTYException");
						// Ncr数
						wOrderTimeInfo.NcrNum = (int) wNumberInfo.CustomResult.get("FQTYNcr");
						// 返修数
						wOrderTimeInfo.ReworkNum = (int) wNumberInfo.CustomResult.get("FQTYRepair");

						// 完成进度
						wOrderTimeInfo.Progress = wAndonStationInfo.FinishProgress;
						// 剩余数
						wOrderTimeInfo.SurplusNum = wAndonStationInfo.FQTYLess;
						// 完成数
						wOrderTimeInfo.FinishNum = wAndonStationInfo.FQTYFinish;
						// 工位
						wOrderTimeInfo.StationName = wAndonStationInfo.StationName;
						// 状态
						wOrderTimeInfo.Status = wAndonStationInfo.Status;
						// 工位总数
						wOrderTimeInfo.STTotal = wInfo.STTotal;
						// 工位完成数
						wOrderTimeInfo.STFinish = wInfo.STFinish;

						wResult.Result.add(wOrderTimeInfo);
					}
				}
			}

			// 排序(进车时间≥异常数≥返修数)
			if (wResult != null && wResult.Result != null && wResult.Result.size() > 0) {
				// 根据订单号和工位去重
				// 多条件
				wResult.Result = wResult.Result.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
						() -> new TreeSet<>(Comparator.comparing(o -> o.getOrderID() + ";" + o.getStationName()))),
						ArrayList::new));
				// 排序
				wResult.Result.sort(
						Comparator.comparing(OrderTimeInfo::getInPlantTime).thenComparing(OrderTimeInfo::getPartNo)
								.thenComparing(OrderTimeInfo::getExcNum, Comparator.reverseOrder())
								.thenComparing(OrderTimeInfo::getReworkNum, Comparator.reverseOrder()));
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单、工位获取异常数、返修数、不合格评审数
	 * 
	 * @param wLoginUser
	 * @param wOMSOrder
	 * @return
	 */
	private ServiceResult<Integer> getNumberInfo(BMSEmployee wLoginUser, OMSOrder wOMSOrder, int wStationID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			int wFQTYException = 0;
			int wFQTYNcr = 0;
			int wFQTYRepair = 0;

			String wCarNumber = wOMSOrder.PartNo.split("#")[1];
			int wProductID = wOMSOrder.ProductID;

			wFQTYException = AndonDAO.getInstance().SelectExceptionCount(wLoginUser, wOMSOrder.PartNo,
					APSConstans.GetFPCPart(wStationID).Code, wErrorCode);
			wFQTYNcr = AndonDAO.getInstance().SelectNcrCount(wLoginUser, wProductID, wCarNumber, wStationID,
					wOMSOrder.RealReceiveDate, wErrorCode);
			wFQTYRepair = AndonDAO.getInstance().SelectRepairCount(wLoginUser, wOMSOrder.ID, wStationID,
					wOMSOrder.RealReceiveDate, wErrorCode);

			wResult.CustomResult.put("FQTYException", wFQTYException);
			wResult.CustomResult.put("FQTYNcr", wFQTYNcr);
			wResult.CustomResult.put("FQTYRepair", wFQTYRepair);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取工位信息、完成数等信息
	 * 
	 * @param wLoginUser
	 * @param wOMSOrder
	 * @return
	 */
	private List<AndonStationInfo> getStationInfo(BMSEmployee wLoginUser, OMSOrder wOMSOrder,
			List<FPCRoutePart> wFPCRoutePartList, List<FPCRoutePartPoint> wFPCRoutePartPointList) {
		List<AndonStationInfo> wResult = new ArrayList<AndonStationInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			int wFQTYLess = 0;
			int wFQTYFinish = 0;
			String wStationName = "";
			String wStatus = "";

			int wRouteID = wOMSOrder.RouteID;

			List<RSMTurnOrderTask> wTurnOrderList = QMSServiceImpl.getInstance()
					.RSM_QueryTurnOrderTaskList(wLoginUser, wOMSOrder.ID, -1, -1, null).List(RSMTurnOrderTask.class);

			if (wTurnOrderList != null && wTurnOrderList.size() > 0) {
				wResult = this.Andon_GetStationInfoList(wLoginUser, wTurnOrderList, wOMSOrder);
			} else {
				wStationName = APSConstans.GetFPCPartName(26);

				List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOMSOrder.ID,
						-1, -1, 26, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null, wErrorCode);
				if (wTaskPartList != null && wTaskPartList.size() > 0) {
					List<Integer> wStepIDList = Andon_GetPartPointIDList(wFPCRoutePartList, wFPCRoutePartPointList,
							wTaskPartList.get(0).PartID, wRouteID);
					List<APSTaskStep> wTaskStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1,
							wTaskPartList.get(0).ID, -1, -1, -1, -1, -1, 1, null, null, null, null, wErrorCode);
					wFQTYFinish = (int) wTaskStepList.stream().filter(p -> p.Status == APSTaskStatus.Done.getValue())
							.count();
					wFQTYLess = wStepIDList.size() - wFQTYFinish;

					if (wTaskPartList.get(0).Status == APSTaskStatus.Started.getValue()) {
						wStatus = "工位开工";
					} else if (wTaskPartList.get(0).Status == APSTaskStatus.Done.getValue()) {
						wStatus = "工位完工";
					}

					String wStationCode = APSConstans.GetFPCPart(wTaskPartList.get(0).PartID).Code;
					int wCount = AndonDAO.getInstance().SelectExceptionCount(wLoginUser, wStationCode, wOMSOrder.PartNo,
							wErrorCode);
					if (wCount > 0) {
						wStatus = "异常";
					}
				}

				AndonStationInfo wAndonStationInfo = new AndonStationInfo();
				wAndonStationInfo.FQTYFinish = wFQTYFinish;
				wAndonStationInfo.FQTYLess = wFQTYLess;
				wAndonStationInfo.StationName = wStationName;
				wAndonStationInfo.Status = wStatus;
				wAndonStationInfo.StationID = 26;
				wResult.add(wAndonStationInfo);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取当前工位的信息
	 */
	private List<AndonStationInfo> Andon_GetStationInfoList(BMSEmployee wLoginUser,
			List<RSMTurnOrderTask> wTurnOrderList, OMSOrder wOMSOrder) {
		List<AndonStationInfo> wResult = new ArrayList<AndonStationInfo>();
		try {
			if (wTurnOrderList == null || wTurnOrderList.size() <= 0) {
				return wResult;
			}

			// ①获取订单工艺路线
//			List<FPCRoutePart> wRoutePartList = APSConstans.mFPCRoutePartList.stream()
//					.filter(p -> p.RouteID == wOMSOrder.RouteID).collect(Collectors.toList());
			List<FPCRoutePart> wRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, wOMSOrder.RouteID).List(FPCRoutePart.class);
			if (wRoutePartList == null || wRoutePartList.size() <= 0) {
				return wResult;
			}
			// ②遍历转序单获取所有当前工位
			List<Integer> wCurPartIDList = new ArrayList<>();

			int wCurPartID = 0;
			for (RSMTurnOrderTask wRSMTurnOrderTask : wTurnOrderList) {
				if (wRSMTurnOrderTask.Status == SFCTurnOrderTaskStatus.Auditing.getValue()) {
					wCurPartID = wRSMTurnOrderTask.ApplyStationID;
				} else if (wRSMTurnOrderTask.Status == SFCTurnOrderTaskStatus.Passed.getValue()) {
					wCurPartID = wRSMTurnOrderTask.TargetStationID;
				}
				// ①获取当前工位的后续工位
				List<Integer> wPartIDList = APSUtils.getInstance().GetNextPartIDList(wCurPartID, wRoutePartList);
				if (wPartIDList == null || wPartIDList.size() <= 0) {
					wCurPartIDList.add(wCurPartID);
					continue;
				}
				// ②如果转序单中不存在后续工位的工位，添加
				if (wPartIDList.stream().allMatch(p -> !wTurnOrderList.stream().anyMatch(
						q -> q.ID != wRSMTurnOrderTask.ID && (q.ApplyStationID == p || q.TargetStationID == p)))) {
					wCurPartIDList.add(wCurPartID);
				}
			}

			if (wCurPartIDList.size() <= 0) {
				return wResult;
			}

			AndonStationInfo wAndonStationInfo;

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<APSTaskStep> wTaskStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wOMSOrder.ID, -1,
					-1, -1, -1, -1, -1, -1, 1, null, null, null, wCurPartIDList, wErrorCode);

			for (int wPartID : wCurPartIDList) {
				List<APSTaskStep> wTempList = wTaskStepList.stream().filter(p -> p.PartID == wPartID)
						.collect(Collectors.toList());
				wAndonStationInfo = new AndonStationInfo();
				wAndonStationInfo.StationID = wPartID;
				wAndonStationInfo.FQTYFinish = (int) wTempList.stream()
						.filter(p -> p.Status == APSTaskStatus.Done.getValue()).count();
				wAndonStationInfo.FQTYLess = wTempList.size() - wAndonStationInfo.FQTYFinish;
				wAndonStationInfo.StationName = APSConstans.GetFPCPartName(wPartID);
				wAndonStationInfo.Status = wTempList.stream().anyMatch(p -> p.Status != APSTaskStatus.Done.getValue())
						? "工位开工"
						: "工位完工";
				String wStationCode = APSConstans.GetFPCPart(wPartID).Code;
				int wCount = AndonDAO.getInstance().SelectExceptionCount(wLoginUser, wStationCode, wOMSOrder.PartNo,
						wErrorCode);
				if (wCount > 0) {
					wAndonStationInfo.Status = "异常";
				}
				wResult.add(wAndonStationInfo);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取库位
	 * 
	 * @param wStoreHouseList
	 * @param wSpaceList
	 * @param wOMSOrder
	 * @return
	 */
	private String getStockName(List<LFSStoreHouse> wStoreHouseList, List<FMCWorkspace> wSpaceList,
			OMSOrder wOMSOrder) {
		String wResult = "";
		try {
			Optional<FMCWorkspace> wOption = wSpaceList.stream().filter(p -> p.PartNo.equals(wOMSOrder.PartNo))
					.findFirst();

			String[] wStrs = wOMSOrder.PartNo.split("#");
			if (wStrs.length == 2 && !wOption.isPresent()) {
				String wPartNewNo = StringUtils.Format("{0}[A]#{1}", wStrs[0], wStrs[1]);

				wOption = wSpaceList.stream().filter(p -> p.PartNo.equals(wPartNewNo)).findFirst();
			}

			if (wOption.isPresent()) {
				int wStoreHouseID = wOption.get().ParentID;
				if (wStoreHouseID <= 0) {
					return wResult;
				}
				if (wStoreHouseList == null || wStoreHouseList.size() <= 0) {
					return wResult;
				}

				Optional<LFSStoreHouse> wHOption = wStoreHouseList.stream().filter(p -> p.ID == wStoreHouseID)
						.findFirst();
				if (wHOption.isPresent()) {
					wResult = wHOption.get().Name;
				} else {
					return wResult;
				}
			} else {
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/*
	 * 获取台位
	 */
	private String getPlaceName(List<FMCWorkspace> wSpaceList, OMSOrder wOMSOrder) {
		String wResult = "";
		try {
			if (wSpaceList == null || wSpaceList.size() <= 0) {
				return wResult;
			}

			Optional<FMCWorkspace> wOption = wSpaceList.stream().filter(p -> p.PartNo.equals(wOMSOrder.PartNo))
					.findFirst();
			if (wOption.isPresent()) {
				wResult = wOption.get().Name;
			} else {
				String[] wStrs = wOMSOrder.PartNo.split("#");
				if (wStrs.length != 2) {
					return wResult;
				}

				String wPartNewNo = StringUtils.Format("{0}[A]#{1}", wStrs[0], wStrs[1]);

				wOption = wSpaceList.stream().filter(p -> p.PartNo.equals(wPartNewNo)).findFirst();

				if (wOption.isPresent()) {
					wResult = wOption.get().Name;
				}

				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据修程、车型、工位获取工序ID集合
	 * 
	 * @param wFPCProductRouteList   产品工艺列表
	 * @param wFPCRoutePartList      工位工艺列表
	 * @param wFPCRoutePartPointList 工序工艺列表
	 * @param wPartID                工位ID
	 * @param wLineName              修程
	 * @param wProductNo             产品型号
	 * @return 工序ID集合
	 */
	private List<Integer> Andon_GetPartPointIDList(List<FPCRoutePart> wFPCRoutePartList,
			List<FPCRoutePartPoint> wFPCRoutePartPointList, int wPartID, int wRouteID) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
//			Optional<FPCProductRoute> wOption = wFPCProductRouteList.stream()
//					.filter(p -> p.Line.equals(wLineName) && p.ProductNo.equals(wProductNo)).findFirst();
//			if (!wOption.isPresent()) {
//				return wResult;
//			}
//
//			FPCProductRoute wFPCProductRoute = wOption.get();

			Optional<FPCRoutePart> wPartOption = wFPCRoutePartList.stream()
					.filter(p -> p.RouteID == wRouteID && p.PartID == wPartID).findFirst();
			if (!wPartOption.isPresent()) {
				return wResult;
			}

			FPCRoutePart wFPCRoutePart = wPartOption.get();

			List<FPCRoutePartPoint> wList = wFPCRoutePartPointList.stream()
					.filter(p -> p.PartID == wFPCRoutePart.PartID && p.RouteID == wRouteID)
					.collect(Collectors.toList());

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			wResult = wList.stream().map(p -> p.PartPointID).collect(Collectors.toList());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取日期间隔天数
	 * 
	 * @param wStart
	 * @param wEnd
	 * @return
	 */
	public Integer getDayInterval(Date wStart, Date wEnd) {
		int wResult = 0;
		try {
			final long wNd = 1000 * 24 * 60 * 60;
			Date wStarDay = new Date(wStart.getTime() - wStart.getTime() % wNd);
			Date wEndDay = new Date(wEnd.getTime() - wEnd.getTime() % wNd);
			wResult = (int) ((wEndDay.getTime() - wStarDay.getTime()) / wNd);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 占比计算保留小数的位数方法 转成百分数 当前数除以总数
	 * 
	 * @param wNum1 ,num2 num1/num2
	 * @return rate 保留2位小数的
	 */
	public String division(int wNum1, int wNum2) {
		String wRate = "0.00%";
		try {
			// 定义格式化起始位数
			String wFormat = "0.00";
			if (wNum2 != 0 && wNum1 != 0) {
				DecimalFormat wDec = new DecimalFormat(wFormat);
				wRate = wDec.format((double) wNum1 / wNum2 * 100) + "%";
				while (true) {
					if (wRate.equals(wFormat + "%")) {
						wFormat = wFormat + "0";
						DecimalFormat wDec1 = new DecimalFormat(wFormat);
						wRate = wDec1.format((double) wNum1 / wNum2 * 100) + "%";
					} else {
						break;
					}
				}
			} else if (wNum1 != 0 && wNum2 == 0) {
				wRate = "100%";
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wRate;
	}

	@Override
	public ServiceResult<List<OrderTimeInfo>> Andon_QueryTrainOrderInfoListNew(BMSEmployee wLoginUser) {
		ServiceResult<List<OrderTimeInfo>> wResult = new ServiceResult<List<OrderTimeInfo>>();
		wResult.Result = new ArrayList<OrderTimeInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①获取有周计划，维修中的订单ID集合
			List<Integer> wIDList = OMSOrderDAO.getInstance().SelectIDListByWeekPlan(wLoginUser, wErrorCode);
			// ①根据订单ID集合，查询维修中的订单
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wIDList, wErrorCode);

			if (wOrderList == null || wOrderList.size() <= 0) {
				return wResult;
			}

			// 获取车体工位集合
			List<Integer> wBodyStationIDList = AndonDAO.getInstance().Andon_GetBodyStationIDList(wLoginUser,
					wErrorCode);

			List<Integer> wStationIDList = new ArrayList<Integer>();
			for (OMSOrder wOrder : wOrderList) {
				wStationIDList = AndonDAO.getInstance().QueryCurrentStationList_V2(wLoginUser, wOrder.ID, wErrorCode);
				if (wStationIDList.size() <= 0) {
					wStationIDList = AndonDAO.getInstance().QueryFirstStationList(wLoginUser, wOrder.ID, wErrorCode);
				}
				for (int wStationID : wStationIDList) {
					// 不是车体工位直接继续
					if (!wBodyStationIDList.stream().anyMatch(p -> p == wStationID)) {
						continue;
					}
					OrderTimeInfo wInfo = AndonDAO.getInstance().QueryOrderTimeInfo(wLoginUser, wOrder, wStationID,
							wErrorCode);

					// 根据订单、工位ID查询落后，提前情况
					List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID,
							-1, -1, wStationID, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null, null,
							wErrorCode);
					if (wTaskPartList.size() > 0) {
						wInfo.PlanStartTime = wTaskPartList.get(0).StartTime;
						wInfo.PlanEndTime = wTaskPartList.get(0).EndTime;
						wInfo.PlanEndTime.add(Calendar.HOUR_OF_DAY, -12);

						wInfo.PlanSituation = CalendarUtil.getDayInterval(wTaskPartList.get(0).StartTime.getTime(),
								Calendar.getInstance().getTime());

						SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
						int wShiftID = Integer.parseInt(wSDF.format(wInfo.PlanStartTime.getTime()));
						int wTShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));

						switch (wInfo.Status) {
						case "未开工":
							if (wShiftID == wTShiftID) {
								wInfo.StationColor = 2;
							} else if (wShiftID > wTShiftID) {
								wInfo.StationColor = 3;
							} else {
								wInfo.StationColor = 2;
							}
							break;
						case "工位开工":
							if (wShiftID >= wTShiftID) {
								wInfo.StationColor = 1;
							} else {
								wInfo.StationColor = 2;
							}
							break;
						case "工位完工":
							wInfo.StationColor = 4;
							break;
						case "异常":
							wInfo.StationColor = 3;
							break;

						default:
							break;
						}

					}

					wResult.Result.add(wInfo);
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
	public ServiceResult<AndonScreen> Andon_QueryAndonScreen(BMSEmployee wLoginUser) {
		ServiceResult<AndonScreen> wResult = new ServiceResult<AndonScreen>();
		wResult.Result = new AndonScreen();
		try {
			wResult.Result = AndonDAO.Screen;

			if (wResult.Result == null) {
				wResult.Result = new AndonScreen();
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<AndonOrder> Andon_QueryPositionInfo(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<AndonOrder> wResult = new ServiceResult<AndonOrder>();
		wResult.Result = new AndonOrder();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode += "提示：参数错误!";
				return wResult;
			}

			List<Integer> wStationIDList = new ArrayList<Integer>();

			// 获取车体工位集合
			List<Integer> wBodyStationIDList = AndonDAO.getInstance().Andon_GetBodyStationIDList(wLoginUser,
					wErrorCode);

			wStationIDList = AndonDAO.getInstance().QueryCurrentStationList_V2(wLoginUser, wOrder.ID, wErrorCode);
			if (wStationIDList.size() <= 0) {
				wStationIDList = AndonDAO.getInstance().QueryFirstStationList(wLoginUser, wOrder.ID, wErrorCode);
			}

			wStationIDList = wStationIDList.stream()
					.filter(p -> wBodyStationIDList.stream().anyMatch(q -> q.intValue() == p.intValue()))
					.collect(Collectors.toList());

			List<OrderTimeInfo> wList = new ArrayList<OrderTimeInfo>();
			for (Integer wStationID : wStationIDList) {
				OrderTimeInfo wInfo = AndonDAO.getInstance().QueryOrderTimeInfo(wLoginUser, wOrder, wStationID,
						wErrorCode);
				wList.add(wInfo);
			}

			if (wList.size() > 0) {
				wResult.Result.OrderID = wOrderID;
				wResult.Result.PartNo = wOrder.PartNo;
				wResult.Result.PartName = String.join(",",
						wList.stream().map(p -> p.StationName).collect(Collectors.toList()));
				wResult.Result.PlaceName = wList.get(0).PlaceName;
				wResult.Result.StockName = wList.get(0).StockName;
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<AndonJournal> Andon_CreateJournal(BMSEmployee wLoginUser) {
		ServiceResult<AndonJournal> wResult = new ServiceResult<AndonJournal>();
		wResult.Result = new AndonJournal();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			int wYear = Calendar.getInstance().get(Calendar.YEAR);
			int wMonth = Calendar.getInstance().get(Calendar.MONTH);
			int wDate = Calendar.getInstance().get(Calendar.DATE);

			// ①今年开始时间
			Calendar wYearStartTime = Calendar.getInstance();
			wYearStartTime.set(wYear, 0, 1, 0, 0, 0);
			// ②今年结束时间
			Calendar wYearEndTime = Calendar.getInstance();
			wYearEndTime.set(wYear, 11, 31, 23, 59, 59);
			// ③本月开始时间
			Calendar wMonthStartTime = Calendar.getInstance();
			wMonthStartTime.set(wYear, wMonth, 1, 0, 0, 0);
			// ④本月结束时间
			Calendar wMonthEndTime = Calendar.getInstance();
			wMonthEndTime.set(wYear, wMonth + 1, 1, 23, 59, 59);
			wMonthEndTime.add(Calendar.DATE, -1);

			// ⑤查询第一部分报表日志
			wResult.Result = AndonJournalDAO.getInstance().QeuryFirstPart_Journal(wLoginUser, wYearStartTime,
					wYearEndTime, wMonthStartTime, wMonthEndTime, wErrorCode);
			// ⑥查询第三部分报表日志

			// ①次月开始时间
			Calendar wNextMonthStartTime = Calendar.getInstance();
			wNextMonthStartTime.set(wYear, wMonth + 1, 1, 0, 0, 0);
			// ②次月结束时间
			Calendar wNextMonthEndTime = Calendar.getInstance();
			wNextMonthEndTime.set(wYear, wMonth + 2, 1, 23, 59, 59);
			wNextMonthEndTime.add(Calendar.DATE, -1);
			// ③今日开始时间
			Calendar wTodayStartTime = Calendar.getInstance();
			wTodayStartTime.set(wYear, wMonth, wDate, 0, 0, 0);
			// ④今日结束时间
			Calendar wTodayEndTime = Calendar.getInstance();
			wTodayEndTime.set(wYear, wMonth, wDate, 23, 59, 59);

			// ①根据修程获取工位列表
			int wLineID = 1;
			List<Integer> wPartIDList = AndonJournalDAO.getInstance().QueryPartIDListByLineID(wLoginUser, wLineID,
					wErrorCode);
			// ②获取C5修的工位统计数据
			List<AndonJournalItem> wItemList_C5 = new ArrayList<AndonJournalItem>();
			for (Integer wPartID : wPartIDList) {
				AndonJournalItem wItem = AndonJournalItemDAO.getInstance().SelectItemByPartIDAndLineID(wLoginUser,
						wPartID, wLineID, wMonthStartTime, wMonthEndTime, wNextMonthStartTime, wNextMonthEndTime,
						wTodayStartTime, wTodayEndTime, wErrorCode);
				wItemList_C5.add(wItem);
			}
			// ③获取C6修的工位统计数据
			wLineID = 2;
			wPartIDList = AndonJournalDAO.getInstance().QueryPartIDListByLineID(wLoginUser, wLineID, wErrorCode);
			List<AndonJournalItem> wItemList_C6 = new ArrayList<AndonJournalItem>();
			for (Integer wPartID : wPartIDList) {
				AndonJournalItem wItem = AndonJournalItemDAO.getInstance().SelectItemByPartIDAndLineID(wLoginUser,
						wPartID, wLineID, wMonthStartTime, wMonthEndTime, wNextMonthStartTime, wNextMonthEndTime,
						wTodayStartTime, wTodayEndTime, wErrorCode);
				wItemList_C6.add(wItem);
			}
			// ④添加进返回结果
			wResult.Result.ItemList_C5 = wItemList_C5;
			wResult.Result.ItemList_C6 = wItemList_C6;

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> Andon_SubmitJournal(BMSEmployee wLoginUser, AndonJournal wAndonJournal) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①提交父项
			wAndonJournal.CreateID = wLoginUser.ID;
			wAndonJournal.CreateTime = Calendar.getInstance();
			wResult.Result = AndonJournalDAO.getInstance().Update(wLoginUser, wAndonJournal, wErrorCode);
			// ②提交子项
			for (AndonJournalItem wAndonJournalItem : wAndonJournal.ItemList_C5) {
				wAndonJournalItem.ID = 0;
				wAndonJournalItem.ParentID = wResult.Result;
				wAndonJournalItem.LineID = 1;
				AndonJournalItemDAO.getInstance().Update(wLoginUser, wAndonJournalItem, wErrorCode);
			}
			for (AndonJournalItem wAndonJournalItem : wAndonJournal.ItemList_C6) {
				wAndonJournalItem.ID = 0;
				wAndonJournalItem.ParentID = wResult.Result;
				wAndonJournalItem.LineID = 2;
				AndonJournalItemDAO.getInstance().Update(wLoginUser, wAndonJournalItem, wErrorCode);
			}

			// ①获取登陆者的上级
			BMSEmployee wUser = APSConstans.GetBMSEmployee(wLoginUser.ID);
			if (wUser.SuperiorID > 0) {
				// ②发送日志消息给此上级
				List<BFCMessage> wBFCMessageList = new ArrayList<>();
				BFCMessage wMessage = null;
				int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(),
						APSShiftPeriod.Day, FMCShiftLevel.Day, 0);
				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = wResult.Result;
				wMessage.Title = StringUtils.Format("{0} {1} {2}", BPMEventModule.ProductionLog.getLable(),
						wLoginUser.Name, String.valueOf(wShiftID));
				wMessage.MessageText = StringUtils.Format("【{0}】 {1}提交了今日日志，请查阅。",
						BPMEventModule.ProductionLog.getLable(), wLoginUser.Name);
				wMessage.ModuleID = BPMEventModule.ProductionLog.getValue();
				wMessage.ResponsorID = wUser.SuperiorID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Notify.getValue();
				wBFCMessageList.add(wMessage);

				CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<AndonJournal> Andon_QueryJournalInfo(BMSEmployee wLoginUser, int wID) {
		ServiceResult<AndonJournal> wResult = new ServiceResult<AndonJournal>();
		wResult.Result = new AndonJournal();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = AndonJournalDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<AndonJournal>> Andon_QueryJournalEmployeeAll(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<AndonJournal>> wResult = new ServiceResult<List<AndonJournal>>();
		wResult.Result = new ArrayList<AndonJournal>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = AndonJournalDAO.getInstance().SelectList(wLoginUser, -1, wLoginUser.ID, wStartTime,
					wEndTime, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> Andon_UpdateConfig(BMSEmployee wLoginUser, AndonConfig wAndonConfig) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = AndonConfigDAO.getInstance().Update(wLoginUser, wAndonConfig, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<AndonConfig>> Andon_QueryConfigList(BMSEmployee wLoginUser, int wID) {
		ServiceResult<List<AndonConfig>> wResult = new ServiceResult<List<AndonConfig>>();
		wResult.Result = new ArrayList<AndonConfig>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = AndonConfigDAO.getInstance().SelectList(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<AndonConfig> Andon_QueryConfig(BMSEmployee wLoginUser, int wID) {
		ServiceResult<AndonConfig> wResult = new ServiceResult<AndonConfig>();
		wResult.Result = new AndonConfig();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = AndonConfigDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<AndonLocomotiveProductionStatus> Andon_QueryProductionStatus(BMSEmployee wLoginUser) {
		ServiceResult<AndonLocomotiveProductionStatus> wResult = new ServiceResult<AndonLocomotiveProductionStatus>();
		wResult.Result = new AndonLocomotiveProductionStatus();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①查询看板配置
			List<AndonConfig> wConfigList = AndonConfigDAO.getInstance().SelectList(wLoginUser, -1, wErrorCode);
			if (wConfigList == null || wConfigList.size() <= 0) {
//				wResult.FaultCode += "提示：未查询到看板配置。";
				return wResult;
			}
			AndonConfig wConfig = wConfigList.get(0);
			// ②赋值停时
			AsignStopTimes(wLoginUser, wConfig, wResult.Result);
			// ③赋值当月过程返工件数
			AsignRepairs(wLoginUser, wConfig, wResult.Result);
			// ④赋值全年统计数据
			AsignStatisticalData(wLoginUser, wConfig, wResult.Result);
			// ⑤赋值视频地址和播放类型
			AsignUri(wLoginUser, wConfig, wResult.Result);
			// ⑥赋值工区生产进度
			AsignAreaProgress(wLoginUser, wConfig, wResult.Result);
			// ⑦赋值不合格单，异常情况
			AsingNcrAndExc(wLoginUser, wConfig, wResult.Result);
			// ⑧赋值零公里得分
			AsignGrade(wLoginUser, wConfig, wResult.Result);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 赋值零公里得分
	 */
	private void AsignGrade(BMSEmployee wLoginUser, AndonConfig wConfig, AndonLocomotiveProductionStatus result) {
		try {
			if (wConfig.GradeFlag == 1) {

				result.Grade_1 = wConfig.Grade_1;
				result.Grade_2 = wConfig.Grade_2;
				result.Grade_3 = wConfig.Grade_3;
				result.Grade_4 = wConfig.Grade_4;
				result.Grade_5 = wConfig.Grade_5;
				result.Grade_6 = wConfig.Grade_6;
				result.Grade_7 = wConfig.Grade_7;
				result.Grade_8 = wConfig.Grade_8;
				result.Grade_9 = wConfig.Grade_9;
				result.Grade_10 = wConfig.Grade_10;
				result.Grade_11 = wConfig.Grade_11;
				result.Grade_12 = wConfig.Grade_12;
				result.Grade_Quality = wConfig.Grade_Quality;
				result.GradeFlag = wConfig.GradeFlag;

				List<Integer> wList = new ArrayList<Integer>(Arrays.asList(result.Grade_1, result.Grade_2,
						result.Grade_3, result.Grade_4, result.Grade_5, result.Grade_6, result.Grade_7, result.Grade_8,
						result.Grade_9, result.Grade_10, result.Grade_11, result.Grade_12));
				int wFlag = -1;
				for (int i = wList.size() - 1; i >= 0; i--) {
					if (wList.get(i) > 0) {
						wFlag = i;
						break;
					}
				}
				result.GradeList = new ArrayList<Integer>();
				for (int i = 0; i <= wFlag; i++) {
					result.GradeList.add(wList.get(i));
				}

				return;
			} else {
				// ①调用focas接口获取数据
				Calendar wTime = Calendar.getInstance();
				int wYear = wTime.get(Calendar.YEAR);
				List<FocasLGL> wLGLList = MyHelperServiceImpl.getInstance().Focas_LGLMES(wLoginUser, wYear);
				// ②赋值result
				AsignResultLGL(wLGLList, result);
				// ③0数据处理
				result.Grade_Quality = wConfig.Grade_Quality;
				result.GradeFlag = 0;

				List<Integer> wList = new ArrayList<Integer>(Arrays.asList(result.Grade_1, result.Grade_2,
						result.Grade_3, result.Grade_4, result.Grade_5, result.Grade_6, result.Grade_7, result.Grade_8,
						result.Grade_9, result.Grade_10, result.Grade_11, result.Grade_12));
				int wFlag = -1;
				for (int i = wList.size() - 1; i >= 0; i--) {
					if (wList.get(i) > 0) {
						wFlag = i;
						break;
					}
				}
				result.GradeList = new ArrayList<Integer>();
				for (int i = 0; i <= wFlag; i++) {
					result.GradeList.add(wList.get(i));
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 零公里得分赋值(focas数据源)
	 */
	private void AsignResultLGL(List<FocasLGL> wLGLList, AndonLocomotiveProductionStatus result) {
		try {
			if (wLGLList == null || wLGLList.size() <= 0)
				return;
			// ①1月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("1")))
				result.Grade_1 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("1")).findFirst().get().lgl);
			// ②2月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("2")))
				result.Grade_2 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("2")).findFirst().get().lgl);
			// ③3月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("3")))
				result.Grade_3 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("3")).findFirst().get().lgl);
			// ④4月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("4")))
				result.Grade_4 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("4")).findFirst().get().lgl);
			// ⑤5月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("5")))
				result.Grade_5 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("5")).findFirst().get().lgl);
			// ⑥6月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("6")))
				result.Grade_6 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("6")).findFirst().get().lgl);
			// ⑦7月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("7")))
				result.Grade_7 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("7")).findFirst().get().lgl);
			// ⑧8月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("8")))
				result.Grade_8 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("8")).findFirst().get().lgl);
			// ⑨9月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("9")))
				result.Grade_9 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("9")).findFirst().get().lgl);
			// ⑩10月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("10")))
				result.Grade_10 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("10")).findFirst().get().lgl);
			// ⑩11月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("11")))
				result.Grade_11 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("11")).findFirst().get().lgl);
			// ⑩12月份
			if (wLGLList.stream().anyMatch(p -> p.month.equals("12")))
				result.Grade_12 = Integer
						.parseInt(wLGLList.stream().filter(p -> p.month.equals("12")).findFirst().get().lgl);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 赋值当月过程返工件数
	 */
	private void AsignRepairs(BMSEmployee wLoginUser, AndonConfig wConfig, AndonLocomotiveProductionStatus result) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			result.FGJS_One_ZB = wConfig.FGJS_One_ZB;
			result.FGJS_Two_ZB = wConfig.FGJS_Two_ZB;
			result.FGJS_Three_ZB = wConfig.FGJS_Three_ZB;
			result.FGJS_Four_ZB = wConfig.FGJS_Four_ZB;

			if (wConfig.RepairFlag == 1) {
				result.FGJS_One = wConfig.FGJS_One;
				result.FGJS_Two = wConfig.FGJS_Two;
				result.FGJS_Three = wConfig.FGJS_Three;
				result.FGJS_Four = wConfig.FGJS_Four;

				return;
			}

			int wYear = Calendar.getInstance().get(Calendar.YEAR);
			int wMonth = Calendar.getInstance().get(Calendar.MONTH);

			Calendar wStartMonth = Calendar.getInstance();
			wStartMonth.set(wYear, wMonth, 1, 0, 0, 0);

			Calendar wEndMonth = Calendar.getInstance();
			wEndMonth.set(wYear, wMonth + 1, 1, 0, 0, 0);

			int wAreaID = 45;
			int wNumber = AndonDAO.getInstance().Andon_AsignRepairs(wLoginUser, wStartMonth, wEndMonth, wAreaID,
					wErrorCode);
			result.FGJS_One = wNumber;

			wAreaID = 46;
			wNumber = AndonDAO.getInstance().Andon_AsignRepairs(wLoginUser, wStartMonth, wEndMonth, wAreaID,
					wErrorCode);
			result.FGJS_Two = wNumber;

			wAreaID = 47;
			wNumber = AndonDAO.getInstance().Andon_AsignRepairs(wLoginUser, wStartMonth, wEndMonth, wAreaID,
					wErrorCode);
			result.FGJS_Three = wNumber;

			wAreaID = 48;
			wNumber = AndonDAO.getInstance().Andon_AsignRepairs(wLoginUser, wStartMonth, wEndMonth, wAreaID,
					wErrorCode);
			result.FGJS_Four = wNumber;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 赋值全年统计数据
	 */
	private void AsignStatisticalData(BMSEmployee wLoginUser, AndonConfig wConfig,
			AndonLocomotiveProductionStatus result) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			result.JHJF_Month = wConfig.JHJF_Month;
			result.JHJF_Month_C5 = wConfig.JHJF_Month_C5;
			result.JHJF_Month_C6 = wConfig.JHJF_Month_C6;

			if (wConfig.PlanFlag == 1) {
				result.LJXJ_Year = wConfig.LJXJ_Year;
				result.LJXJ_Year_C5 = wConfig.LJXJ_Year_C5;
				result.LJXJ_Year_C6 = wConfig.LJXJ_Year_C6;
				result.SJJF_Month = wConfig.SJJF_Month;
				result.SJJF_Month_C5 = wConfig.SJJF_Month_C5;
				result.SJJF_Month_C6 = wConfig.SJJF_Month_C6;

				return;
			}

			int wYear = Calendar.getInstance().get(Calendar.YEAR);
			int wMonth = Calendar.getInstance().get(Calendar.MONTH);

			Calendar wStartYear = Calendar.getInstance();
			wStartYear.set(wYear, 0, 1, 0, 0, 0);

			Calendar wEndYear = Calendar.getInstance();
			wEndYear.set(wYear, 11, 31, 23, 59, 59);

			Calendar wStartMonth = Calendar.getInstance();
			wStartMonth.set(wYear, wMonth, 1, 0, 0, 0);

			Calendar wEndMonth = Calendar.getInstance();
			wEndMonth.set(wYear, wMonth + 1, 1, 0, 0, 0);

			AndonLocomotiveProductionStatus wItem = AndonDAO.getInstance().AsignStatisticalData(wLoginUser, wStartYear,
					wEndYear, wStartMonth, wEndMonth, wErrorCode);

			result.LJXJ_Year = wItem.LJXJ_Year;
			result.LJXJ_Year_C5 = wItem.LJXJ_Year_C5;
			result.LJXJ_Year_C6 = wItem.LJXJ_Year_C6;
			result.SJJF_Month = wItem.SJJF_Month;
			result.SJJF_Month_C5 = wItem.SJJF_Month_C5;
			result.SJJF_Month_C6 = wItem.SJJF_Month_C6;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 赋值视频地址和播放类型
	 */
	private void AsignUri(BMSEmployee wLoginUser, AndonConfig wConfig, AndonLocomotiveProductionStatus result) {
		try {
			result.Uri = wConfig.Uri;
			result.Type = wConfig.Type;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 赋值工区生产进度
	 */
	private void AsignAreaProgress(BMSEmployee wLoginUser, AndonConfig wConfig,
			AndonLocomotiveProductionStatus result) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wConfig.ProgressFlag == 1) {
				result.Done_One = wConfig.Done_One;
				result.NotDone_One = wConfig.NotDone_One;
				result.Done_Two = wConfig.Done_Two;
				result.NotDone_Two = wConfig.NotDone_Two;
				result.Done_Three = wConfig.Done_Three;
				result.NotDone_Three = wConfig.NotDone_Three;
				result.Done_Four = wConfig.Done_Four;
				result.NotDone_Four = wConfig.NotDone_Four;

				return;
			}

			AndonLocomotiveProductionStatus wItem = AndonDAO.getInstance().Andon_AsignAreaProgress(wLoginUser,
					wErrorCode);
			result.Done_One = wItem.Done_One;
			result.NotDone_One = wItem.NotDone_One;
			result.Done_Two = wItem.Done_Two;
			result.NotDone_Two = wItem.NotDone_Two;
			result.Done_Three = wItem.Done_Three;
			result.NotDone_Three = wItem.NotDone_Three;
			result.Done_Four = wItem.Done_Four;
			result.NotDone_Four = wItem.NotDone_Four;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 赋值不合格单，异常情况
	 */
	private void AsingNcrAndExc(BMSEmployee wLoginUser, AndonConfig wConfig, AndonLocomotiveProductionStatus result) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wConfig.NcrExcFlag == 1) {
				result.Total_Ncr = wConfig.Total_Ncr;
				result.Doing_Ncr = wConfig.Doing_Ncr;
				result.Total_Exc = wConfig.Total_Exc;
				result.Doing_Exc = wConfig.Doing_Exc;

				return;
			}

			int wYear = Calendar.getInstance().get(Calendar.YEAR);
			int wMonth = Calendar.getInstance().get(Calendar.MONTH);

			Calendar wStartMonth = Calendar.getInstance();
			wStartMonth.set(wYear, wMonth, 1, 0, 0, 0);

			Calendar wEndMonth = Calendar.getInstance();
			wEndMonth.set(wYear, wMonth + 1, 1, 0, 0, 0);

			AndonLocomotiveProductionStatus wItem = AndonDAO.getInstance().Andon_AsingNcrAndExc(wLoginUser, wStartMonth,
					wEndMonth, wErrorCode);
			result.Total_Ncr = wItem.Total_Ncr;
			result.Doing_Ncr = wItem.Doing_Ncr;
			result.Total_Exc = wItem.Total_Exc;
			result.Doing_Exc = wItem.Doing_Exc;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 赋值停时
	 */
	private void AsignStopTimes(BMSEmployee wLoginUser, AndonConfig wConfig, AndonLocomotiveProductionStatus result) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			result.YHTS_C6_ZB = wConfig.YHTS_C6_ZB;
			result.YHTS_C5_ZB = wConfig.YHTS_C5_ZB;
			result.JCTS_C6_ZB = wConfig.JCTS_C6_ZB;
			result.JCTS_C5_ZB = wConfig.JCTS_C5_ZB;

			if (wConfig.StopFlag == 1) {
				result.YHTS_C6 = wConfig.YHTS_C6;
				result.YHTS_C5 = wConfig.YHTS_C5;
				result.JCTS_C6 = wConfig.JCTS_C6;
				result.JCTS_C5 = wConfig.JCTS_C5;

				return;
			}

			int wYear = Calendar.getInstance().get(Calendar.YEAR);

			Calendar wStartYear = Calendar.getInstance();
			wStartYear.set(wYear, 0, 1, 0, 0, 0);

			Calendar wEndYear = Calendar.getInstance();
			wEndYear.set(wYear, 11, 31, 23, 59, 59);

			AndonLocomotiveProductionStatus wItem = AndonDAO.getInstance().Andon_AsignStopTimes(wLoginUser, wStartYear,
					wEndYear, wErrorCode);
			result.YHTS_C6 = wItem.YHTS_C6;
			result.YHTS_C5 = wItem.YHTS_C5;
			result.JCTS_C6 = wItem.JCTS_C6;
			result.JCTS_C5 = wItem.JCTS_C5;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<List<AndonDayPlanPartCashingRate>> Andon_QueryPartDayPlanRate(BMSEmployee wLoginUser,
			Calendar wSTime, Calendar wETime, String wPartIDs, String wOrderIDs) {
		ServiceResult<List<AndonDayPlanPartCashingRate>> wResult = new ServiceResult<List<AndonDayPlanPartCashingRate>>();
		wResult.Result = new ArrayList<AndonDayPlanPartCashingRate>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wOrderIDList = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(wOrderIDs) && !wOrderIDs.equals("-1")) {
				String[] wStrs = wOrderIDs.split(",");
				for (String wStr : wStrs) {
					wOrderIDList.add(StringUtils.parseInt(wStr));
				}
			}
			List<Integer> wPartIDParamList = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(wPartIDs) && !wPartIDs.equals("-1")) {
				String[] wStrs = wPartIDs.split(",");
				for (String wStr : wStrs) {
					wPartIDParamList.add(StringUtils.parseInt(wStr));
				}
			}

			// ①查询已排好序的工位id集合
			List<Integer> wSortedPartIDList = AndonDAO.getInstance().Andon_QuerySortedPartIDList(wLoginUser,
					wErrorCode);
			// ①获取工位集合
			List<Integer> wPartIDList = AndonDAO.getInstance().Andon_QueryPartDayPlanIDList(wLoginUser, wErrorCode);
			// ②遍历工位，获取日计划数
			for (int wPartID : wSortedPartIDList) {
				if (!wPartIDList.stream().anyMatch(p -> p == wPartID)) {
					continue;
				}
				AndonDayPlanPartCashingRate wRate = AndonDAO.getInstance().Andon_QueryPartDayPlan(wLoginUser, wPartID,
						wOrderIDList, wSTime, wETime, wErrorCode);
				if (wRate.TotalNumber > 0) {
					wResult.Result.add(wRate);
				}
			}

			if (wPartIDParamList.size() > 0) {
				wResult.Result = wResult.Result.stream()
						.filter(p -> wPartIDParamList.stream().anyMatch(q -> q == p.PartID))
						.collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<AndonDayPlanClassCashingRate>> Andon_QueryClassDayPlanRate(BMSEmployee wLoginUser,
			Calendar wSTime, Calendar wETime, String wClassIDs, String wOrderIDs) {
		ServiceResult<List<AndonDayPlanClassCashingRate>> wResult = new ServiceResult<List<AndonDayPlanClassCashingRate>>();
		wResult.Result = new ArrayList<AndonDayPlanClassCashingRate>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wOrderIDList = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(wOrderIDs) && !wOrderIDs.equals("-1")) {
				String[] wStrs = wOrderIDs.split(",");
				for (String wStr : wStrs) {
					wOrderIDList.add(StringUtils.parseInt(wStr));
				}
			}
			List<Integer> wClassIDParamList = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(wClassIDs) && !wClassIDs.equals("-1")) {
				String[] wStrs = wClassIDs.split(",");
				for (String wStr : wStrs) {
					wClassIDParamList.add(StringUtils.parseInt(wStr));
				}
			}

			// ①获取激活的班组工位列表
			List<BMSWorkCharge> wList = CoreServiceImpl.getInstance().FMC_QueryWorkChargeList(wLoginUser)
					.List(BMSWorkCharge.class);
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}
			wList = wList.stream().filter(p -> p.Active == 1).collect(Collectors.toList());
			// ②提取班组
			List<Integer> wClassIDList = wList.stream().map(p -> p.ClassID).distinct().collect(Collectors.toList());
			// ③遍历获取工位列表，根据工位日计划兑现率
			for (int wClassID : wClassIDList) {
				List<BMSWorkCharge> wPartChargeList = wList.stream().filter(p -> p.ClassID == wClassID)
						.collect(Collectors.toList());

				int wFSize = 0;
				int wTSize = 0;

				for (BMSWorkCharge wItem : wPartChargeList) {
					AndonDayPlanPartCashingRate wRate = AndonDAO.getInstance().Andon_QueryPartDayPlan(wLoginUser,
							wItem.StationID, wOrderIDList, wSTime, wETime, wErrorCode);
					wFSize += wRate.FinishNumber;
					wTSize += wRate.TotalNumber;
				}

//				double wAvF = (double) wFSize / wPartChargeList.size();
//				double wAvT = (double) wTSize / wPartChargeList.size();
				double wAv = 0.0;
				if (wTSize > 0) {
					wAv = new BigDecimal((double) wFSize / wTSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}

				String wClassName = APSConstans.GetBMSDepartmentName(wClassID);

				if (wTSize <= 0) {
					continue;
				}
				AndonDayPlanClassCashingRate wRateNew = new AndonDayPlanClassCashingRate(wClassID, wClassName, wFSize,
						wTSize, wAv * 100);
				wResult.Result.add(wRateNew);
			}

			// 筛选工区
			if (wClassIDParamList.size() > 0) {
				wResult.Result = wResult.Result.stream()
						.filter(p -> wClassIDParamList.stream().anyMatch(q -> q == p.ClassID))
						.collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<AndonDayPlanAreaCashingRate>> Andon_QueryAreaDayPlanRate(BMSEmployee wLoginUser,
			Calendar wSTime, Calendar wETime, String wAreaIDs, String wOrderIDs) {
		ServiceResult<List<AndonDayPlanAreaCashingRate>> wResult = new ServiceResult<List<AndonDayPlanAreaCashingRate>>();
		wResult.Result = new ArrayList<AndonDayPlanAreaCashingRate>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wOrderIDList = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(wOrderIDs) && !wOrderIDs.equals("-1")) {
				String[] wStrs = wOrderIDs.split(",");
				for (String wStr : wStrs) {
					wOrderIDList.add(StringUtils.parseInt(wStr));
				}
			}
			List<Integer> wAreaIDParamList = new ArrayList<Integer>();
			if (StringUtils.isNotEmpty(wAreaIDs) && !wAreaIDs.equals("-1")) {
				String[] wStrs = wAreaIDs.split(",");
				for (String wStr : wStrs) {
					wAreaIDParamList.add(StringUtils.parseInt(wStr));
				}
			}

			// ①获取激活的班组工位列表
			List<LFSWorkAreaStation> wList = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.filter(p -> p.Active == 1 && p.WorkAreaID > 0).collect(Collectors.toList());
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}
			// ②提取工区
			List<Integer> wAreaIDList = wList.stream().map(p -> p.WorkAreaID).distinct().collect(Collectors.toList());
			Collections.sort(wAreaIDList);

			// ③遍历获取工位列表，根据工位日计划兑现率
			for (int wAreaID : wAreaIDList) {
				List<LFSWorkAreaStation> wWSList = wList.stream().filter(p -> p.WorkAreaID == wAreaID)
						.collect(Collectors.toList());

				int wFSize = 0;
				int wTSize = 0;

				for (LFSWorkAreaStation wItem : wWSList) {
					AndonDayPlanPartCashingRate wRate = AndonDAO.getInstance().Andon_QueryPartDayPlan(wLoginUser,
							wItem.StationID, wOrderIDList, wSTime, wETime, wErrorCode);
					wFSize += wRate.FinishNumber;
					wTSize += wRate.TotalNumber;
				}

//				double wAvF = (double) wFSize / wWSList.size();
//				double wAvT = (double) wTSize / wWSList.size();
				double wAv = 0.0;
				if (wTSize > 0) {
					wAv = new BigDecimal((double) wFSize / wTSize).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}

				String wClassName = APSConstans.GetBMSDepartmentName(wAreaID);

				AndonDayPlanAreaCashingRate wRateNew = new AndonDayPlanAreaCashingRate(wAreaID, wClassName, wFSize,
						wTSize, wAv * 100);
				wResult.Result.add(wRateNew);
			}

			// 筛选工区
			if (wAreaIDParamList.size() > 0) {
				wResult.Result = wResult.Result.stream()
						.filter(p -> wAreaIDParamList.stream().anyMatch(q -> q == p.AreaID))
						.collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<AndonDayPlanAreaCashingRate>> Andon_QueryAreaMonthPlanRate(BMSEmployee wLoginUser,
			Calendar wSDate, Calendar wEDate, String wAreaIDs, String wOrderIDs) {
		ServiceResult<List<AndonDayPlanAreaCashingRate>> wResult = new ServiceResult<List<AndonDayPlanAreaCashingRate>>();
		wResult.Result = new ArrayList<AndonDayPlanAreaCashingRate>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wSDate.compareTo(wEDate) > 0) {
				return wResult;
			}

			Calendar wSTime = wSDate;
			wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);

			Calendar wETime = wEDate;
			wETime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 23, 59, 59);

			List<AndonDayPlanAreaCashingRate> wAllList = new ArrayList<AndonDayPlanAreaCashingRate>();
			List<AndonDayPlanAreaCashingRate> wList = Andon_QueryAreaDayPlanRate(wLoginUser, wSTime, wETime, wAreaIDs,
					wOrderIDs).Result;
			wAllList.addAll(wList);

			// ①提取工区
			List<Integer> wAreaIDList = wAllList.stream().map(p -> p.AreaID).distinct().collect(Collectors.toList());
			// ②遍历求平均数
			for (int wAreaID : wAreaIDList) {
				List<AndonDayPlanAreaCashingRate> wTempList = wAllList.stream()
						.filter(p -> p.TotalNumber > 0 && p.AreaID == wAreaID).collect(Collectors.toList());
				if (wTempList.size() > 0) {

					double wT = 0;
					for (AndonDayPlanAreaCashingRate wAndonDayPlanAreaCashingRate : wTempList) {
						wT += wAndonDayPlanAreaCashingRate.Rate;
					}
					double wAv = new BigDecimal((double) wT / wTempList.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
							.doubleValue();

					AndonDayPlanAreaCashingRate wRate = new AndonDayPlanAreaCashingRate(wAreaID,
							wTempList.get(0).AreaName, 0, 0, wAv);
					wResult.Result.add(wRate);
				} else {
					String wName = wAllList.stream().filter(p -> p.AreaID == wAreaID).findFirst().get().AreaName;
					AndonDayPlanAreaCashingRate wRate = new AndonDayPlanAreaCashingRate(wAreaID, wName, 0, 0, 0);
					wResult.Result.add(wRate);
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
	public ServiceResult<List<AndonDayPlanClassCashingRate>> Andon_QueryClassMonthPlanRate(BMSEmployee wLoginUser,
			Calendar wSDate, Calendar wEDate, String wClassIDs, String wOrderIDs) {
		ServiceResult<List<AndonDayPlanClassCashingRate>> wResult = new ServiceResult<List<AndonDayPlanClassCashingRate>>();
		wResult.Result = new ArrayList<AndonDayPlanClassCashingRate>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wSDate.compareTo(wEDate) > 0) {
				return wResult;
			}

			Calendar wSTime = wSDate;
			wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);

			Calendar wETime = wEDate;
			wETime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 23, 59, 59);

			List<AndonDayPlanClassCashingRate> wAllList = new ArrayList<AndonDayPlanClassCashingRate>();
			List<AndonDayPlanClassCashingRate> wList = Andon_QueryClassDayPlanRate(wLoginUser, wSTime, wETime,
					wClassIDs, wOrderIDs).Result;
			wAllList.addAll(wList);

			// ①提取班组
			List<Integer> wClassIDList = wAllList.stream().map(p -> p.ClassID).distinct().collect(Collectors.toList());
			// ②遍历求平均数
			for (int wClassID : wClassIDList) {
				List<AndonDayPlanClassCashingRate> wTempList = wAllList.stream()
						.filter(p -> p.TotalNumber > 0 && p.ClassID == wClassID).collect(Collectors.toList());
				if (wTempList.size() > 0) {

					double wT = 0;
					for (AndonDayPlanClassCashingRate wAndonDayPlanAreaCashingRate : wTempList) {
						wT += wAndonDayPlanAreaCashingRate.Rate;
					}
					double wAv = new BigDecimal((double) wT / wTempList.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
							.doubleValue();

					AndonDayPlanClassCashingRate wRate = new AndonDayPlanClassCashingRate(wClassID,
							wTempList.get(0).ClassName, 0, 0, wAv);
					wResult.Result.add(wRate);
				} else {
					String wName = wAllList.stream().filter(p -> p.ClassID == wClassID).findFirst().get().ClassName;
					AndonDayPlanClassCashingRate wRate = new AndonDayPlanClassCashingRate(wClassID, wName, 0, 0, 0);
					wResult.Result.add(wRate);
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
	public ServiceResult<List<AndonDayPlanPartCashingRate>> Andon_QueryPartMonthPlanRate(BMSEmployee wLoginUser,
			Calendar wSDate, Calendar wEDate, String wPartIDs, String wOrderIDs) {
		ServiceResult<List<AndonDayPlanPartCashingRate>> wResult = new ServiceResult<List<AndonDayPlanPartCashingRate>>();
		wResult.Result = new ArrayList<AndonDayPlanPartCashingRate>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wSDate.compareTo(wEDate) > 0) {
				return wResult;
			}

			Calendar wSTime = wSDate;
			wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);

			Calendar wETime = wEDate;
			wETime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 23, 59, 59);

			List<AndonDayPlanPartCashingRate> wAllList = new ArrayList<AndonDayPlanPartCashingRate>();
			List<AndonDayPlanPartCashingRate> wList = Andon_QueryPartDayPlanRate(wLoginUser, wSTime, wETime, wPartIDs,
					wOrderIDs).Result;
			wAllList.addAll(wList);

			// ①提取工位
			List<Integer> wPartIDList = wAllList.stream().map(p -> p.PartID).distinct().collect(Collectors.toList());
			// ②遍历求平均数
			for (int wPartID : wPartIDList) {
				List<AndonDayPlanPartCashingRate> wTempList = wAllList.stream()
						.filter(p -> p.TotalNumber > 0 && p.PartID == wPartID).collect(Collectors.toList());
				if (wTempList.size() > 0) {

					double wT = 0;
					for (AndonDayPlanPartCashingRate wAndonDayPlanAreaCashingRate : wTempList) {
						wT += wAndonDayPlanAreaCashingRate.Rate;
					}
					double wAv = new BigDecimal((double) wT / wTempList.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
							.doubleValue();

					AndonDayPlanPartCashingRate wRate = new AndonDayPlanPartCashingRate(wPartID,
							wTempList.get(0).PartName, 0, 0, wAv);
					wResult.Result.add(wRate);
				} else {
					String wName = wAllList.stream().filter(p -> p.PartID == wPartID).findFirst().get().PartName;
					AndonDayPlanPartCashingRate wRate = new AndonDayPlanPartCashingRate(wPartID, wName, 0, 0, 0);
					wResult.Result.add(wRate);
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
	public ServiceResult<String> Andon_ExportTrainStatus(BMSEmployee wLoginUser) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		try {
			AndonScreen wScreen = AndonServiceImpl.getInstance().Andon_QueryAndonScreen(wLoginUser).Result;
			// 检修概况
			MyExcelSheet wMyExcelSheet = GetMyExcelSheet(wScreen);
			// 在厂台车列表
			MyExcelSheet wMyExcelSheet1 = GetwMyExcelSheet1(wScreen);

			List<MyExcelSheet> wMyExcelSheetList = new ArrayList<MyExcelSheet>(
					Arrays.asList(wMyExcelSheet, wMyExcelSheet1));

			wResult.Result = ExcelUtil.ExportData(wMyExcelSheetList, "广州电力机车MES系统生产状态监控");
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取在厂台车状态列表
	 */
	private MyExcelSheet GetwMyExcelSheet1(AndonScreen wScreen) {
		MyExcelSheet wResult = new MyExcelSheet();
		try {
			wResult.SheetName = "在厂台车列表";
			wResult.HeaderList = new ArrayList<String>(Arrays.asList("车号", "配属局段", "修程", "进厂日期", "停时", "库位", "台位", "工位",
					"计划完工日期", "提前/落后(天)", "完成数", "剩余数", "完成进度", "返修", "不合格评审", "异常", "状态"));
			wResult.TitleName = "在厂台车列表";
			wResult.DataList = new ArrayList<List<String>>();

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd");

			for (OrderTimeInfo wOrderTimeInfo : wScreen.TrainItemList) {
				List<String> wList = new ArrayList<String>();

				wList.add(wOrderTimeInfo.PartNo);
				wList.add(wOrderTimeInfo.CustomerName);
				wList.add(wOrderTimeInfo.LineName);
				wList.add(wSDF.format(wOrderTimeInfo.InPlantTime.getTime()));
				wList.add(String.valueOf(wOrderTimeInfo.StopTime));
				wList.add(wOrderTimeInfo.StockName);
				wList.add(wOrderTimeInfo.PlaceName);
				wList.add(wOrderTimeInfo.StationName + "(" + wSDF.format(wOrderTimeInfo.PlanStartTime.getTime()) + ")");
				wList.add(wSDF.format(wOrderTimeInfo.PlanEndTime.getTime()));
				wList.add(String.valueOf(wOrderTimeInfo.PlanSituation));
				wList.add(String.valueOf(wOrderTimeInfo.FinishNum));
				wList.add(String.valueOf(wOrderTimeInfo.SurplusNum));
				if (wOrderTimeInfo.SurplusNum + wOrderTimeInfo.FinishNum <= 0) {
					wList.add("0%");
				} else {
					double wRate = new BigDecimal(
							(double) wOrderTimeInfo.FinishNum / (wOrderTimeInfo.FinishNum + wOrderTimeInfo.SurplusNum))
									.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					wList.add(String.valueOf((int) (wRate * 100)) + "%");
				}
				wList.add(String.valueOf(wOrderTimeInfo.ReworkNum));
				wList.add(String.valueOf(wOrderTimeInfo.NcrNum));
				wList.add(String.valueOf(wOrderTimeInfo.ExcNum));
				wList.add(wOrderTimeInfo.Status);

				wResult.DataList.add(wList);

			}

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取检修概况Excel数据
	 */
	private MyExcelSheet GetMyExcelSheet(AndonScreen wScreen) {
		MyExcelSheet wResult = new MyExcelSheet();
		try {
			wResult.SheetName = "检修概况";
			wResult.HeaderList = new ArrayList<String>(Arrays.asList("全年累计修竣", "C5修累计修竣", "C6修累计修竣", "本月竣工数", "在厂机车",
					"在修机车", "待完工任务", "今日新增异常", "不合格评审数", "返修数"));
			wResult.TitleName = "检修概况";
			wResult.DataList = new ArrayList<List<String>>();
			List<String> wList = new ArrayList<String>(Arrays.asList(String.valueOf(wScreen.YearReport.AnnualNum),
					String.valueOf(wScreen.YearReport.AnnualC5Num), String.valueOf(wScreen.YearReport.AnnualC6Num),
					String.valueOf(wScreen.YearReport.ThisMonthNum), String.valueOf(wScreen.YearReport.LocoCar),
					String.valueOf(wScreen.YearReport.RepairCar), String.valueOf(wScreen.YearReport.stationTask),
					String.valueOf(wScreen.YearReport.ExcTask), String.valueOf(wScreen.YearReport.ncrTask),
					String.valueOf(wScreen.YearReport.repairTask)));
			wResult.DataList.add(wList);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> Andon_ExportAreaRate(BMSEmployee wLoginUser, List<AndonDayPlanAreaCashingRate> wList) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		try {
			MyExcelSheet wMyExcelSheet = GetAreaRateExcelSheet(wList);

			List<MyExcelSheet> wMyExcelSheetList = new ArrayList<MyExcelSheet>(Arrays.asList(wMyExcelSheet));

			wResult.Result = ExcelUtil.ExportData(wMyExcelSheetList, "工区兑现率");
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取工区兑现率sheet
	 */
	private MyExcelSheet GetAreaRateExcelSheet(List<AndonDayPlanAreaCashingRate> wList) {
		MyExcelSheet wResult = new MyExcelSheet();
		try {
			wResult.SheetName = "工区兑现率";
			wResult.TitleName = "工区兑现率";
			wResult.HeaderList = wList.stream().map(p -> p.AreaName).distinct().collect(Collectors.toList());
			wResult.DataList = new ArrayList<List<String>>();
			List<String> wValueList = new ArrayList<String>();
			for (AndonDayPlanAreaCashingRate wAndonDayPlanAreaCashingRate : wList) {
				wValueList.add(String.valueOf(wAndonDayPlanAreaCashingRate.Rate * 100) + "%");
			}
			wResult.DataList.add(wValueList);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> Andon_ExportClassRate(BMSEmployee wLoginUser,
			List<AndonDayPlanClassCashingRate> wList) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		try {
			MyExcelSheet wMyExcelSheet = GetClassRateExcelSheet(wList);

			List<MyExcelSheet> wMyExcelSheetList = new ArrayList<MyExcelSheet>(Arrays.asList(wMyExcelSheet));

			wResult.Result = ExcelUtil.ExportData(wMyExcelSheetList, "班组兑现率");
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private MyExcelSheet GetClassRateExcelSheet(List<AndonDayPlanClassCashingRate> wList) {
		MyExcelSheet wResult = new MyExcelSheet();
		try {
			wResult.SheetName = "班组兑现率";
			wResult.TitleName = "班组兑现率";
			wResult.HeaderList = wList.stream().map(p -> p.ClassName).distinct().collect(Collectors.toList());
			wResult.DataList = new ArrayList<List<String>>();
			List<String> wValueList = new ArrayList<String>();
			for (AndonDayPlanClassCashingRate wAndonDayPlanAreaCashingRate : wList) {
				wValueList.add(String.valueOf(wAndonDayPlanAreaCashingRate.Rate * 100) + "%");
			}
			wResult.DataList.add(wValueList);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> Andon_ExportPartRate(BMSEmployee wLoginUser, List<AndonDayPlanPartCashingRate> wList) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		try {
			MyExcelSheet wMyExcelSheet = GetPartRateExcelSheet(wList);

			List<MyExcelSheet> wMyExcelSheetList = new ArrayList<MyExcelSheet>(Arrays.asList(wMyExcelSheet));

			wResult.Result = ExcelUtil.ExportData(wMyExcelSheetList, "工位兑现率");
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private MyExcelSheet GetPartRateExcelSheet(List<AndonDayPlanPartCashingRate> wList) {
		MyExcelSheet wResult = new MyExcelSheet();
		try {
			wResult.SheetName = "工位兑现率";
			wResult.TitleName = "工位兑现率";
			wResult.HeaderList = wList.stream().map(p -> p.PartName).distinct().collect(Collectors.toList());
			wResult.DataList = new ArrayList<List<String>>();
			List<String> wValueList = new ArrayList<String>();
			for (AndonDayPlanPartCashingRate wAndonDayPlanAreaCashingRate : wList) {
				wValueList.add(String.valueOf(wAndonDayPlanAreaCashingRate.Rate * 100) + "%");
			}
			wResult.DataList.add(wValueList);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FPCPart>> APS_QueryPartList(BMSEmployee wLoginUser, String wAreaID, String wClassID) {
		ServiceResult<List<FPCPart>> wResult = new ServiceResult<List<FPCPart>>();
		wResult.Result = new ArrayList<FPCPart>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①工区筛选工位
			List<Integer> wAreaPartIDList = null;
			if (StringUtils.isNotEmpty(wAreaID) && !wAreaID.equals("-1")) {
				List<Integer> wAreaIDList = new ArrayList<Integer>();
				String[] wStrs = wAreaID.split(",");
				for (String wStr : wStrs) {
					wAreaIDList.add(StringUtils.parseInt(wStr));
				}

				wAreaPartIDList = AndonDAO.getInstance().Andon_QueryAreaPartIDList(wLoginUser, wAreaIDList, wErrorCode);
			}

			// ②班组筛选工位
			List<Integer> wClassPartIDList = null;
			if (StringUtils.isNotEmpty(wClassID) && !wClassID.equals("-1")) {
				List<Integer> wClassIDList = new ArrayList<Integer>();
				String[] wStrs = wClassID.split(",");
				for (String wStr : wStrs) {
					wClassIDList.add(StringUtils.parseInt(wStr));
				}

				wClassPartIDList = AndonDAO.getInstance().Andon_QueryClassPartIDList(wLoginUser, wClassIDList,
						wErrorCode);
			}

			// ③取交集
			if (wAreaPartIDList != null && wClassPartIDList != null) {
				List<Integer> wCloneList = wClassPartIDList;
				wAreaPartIDList = wAreaPartIDList.stream().filter(p -> wCloneList.stream().anyMatch(q -> q == p))
						.collect(Collectors.toList());
				for (int wPartID : wAreaPartIDList) {
					FPCPart wPart = APSConstans.GetFPCPart(wPartID);
					if (wPart.ID > 0) {
						wResult.Result.add(wPart);
					}
				}
			} else {
				if (wAreaPartIDList != null && wAreaPartIDList.size() > 0) {
					for (int wPartID : wAreaPartIDList) {
						FPCPart wPart = APSConstans.GetFPCPart(wPartID);
						if (wPart.ID > 0) {
							wResult.Result.add(wPart);
						}
					}
				} else {
					for (int wPartID : wClassPartIDList) {
						FPCPart wPart = APSConstans.GetFPCPart(wPartID);
						if (wPart.ID > 0) {
							wResult.Result.add(wPart);
						}
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
	public ServiceResult<Integer> Andon_ProductionDaily2(BMSEmployee wLoginUser, int wYear) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①解析年度时间
			Calendar wSTime = Calendar.getInstance();
			wSTime.set(wYear, 0, 1, 0, 0, 0);
			Calendar wETime = Calendar.getInstance();
			wETime.set(wYear, 11, 31, 23, 59, 59);
			// ②根据时间查询实际到厂时间在当年的订单ID集合
			List<Integer> wIDList = OMSOrderDAO.getInstance().SelectListByYearTime(wLoginUser, wSTime, wETime,
					wErrorCode);
			// ③根据订单ID集合获取订单列表
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wIDList, wErrorCode);
			// 移除广铁的订单
			wOrderList.removeIf(p -> p.CustomerID == 43);
			// ④C5统计
			List<OMSOrder> wC5List = wOrderList.stream().filter(p -> p.LineID == 1).collect(Collectors.toList());

			int wMonth = Calendar.getInstance().get(Calendar.MONTH);

			Calendar wMSTime = Calendar.getInstance();
			wMSTime.set(wYear, wMonth, 1, 0, 0, 0);

			Calendar wMETime = Calendar.getInstance();
			wMETime.set(wYear, wMonth + 1, 1, 0, 0, 0);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);

			int bYJG = (int) wC5List.stream()
					.filter(p -> p.RealFinishDate.compareTo(wMSTime) >= 0 && p.RealFinishDate.compareTo(wMETime) <= 0)
					.count();
			int lJJG = (int) wC5List.stream().filter(p -> p.RealFinishDate.compareTo(wBaseTime) >= 0).count();
			double dBJXTS = 0;
			double sJJXTS = 0;
			double zCTS = 0;
			double zTTS = 0;
			double yHTS = 0;
			for (OMSOrder omsOrder : wC5List) {
				dBJXTS += omsOrder.TelegraphRepairStopTimes;
				sJJXTS += omsOrder.ActualRepairStopTimes;
				zCTS += omsOrder.InPlantStopTimes;
				zTTS += omsOrder.OnTheWayStopTimes;
				yHTS += omsOrder.PosterioriStopTimes;
			}

			if (dBJXTS > 0) {
				dBJXTS = new BigDecimal((double) dBJXTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
			if (sJJXTS > 0) {
				sJJXTS = new BigDecimal((double) sJJXTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
			if (zCTS > 0) {
				zCTS = new BigDecimal((double) zCTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
			if (zTTS > 0) {
				zTTS = new BigDecimal((double) zTTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
			if (yHTS > 0) {
				yHTS = new BigDecimal((double) yHTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}

			AndonStopStatistics wC5Total = new AndonStopStatistics(1, "C5", wC5List.size(), bYJG, lJJG, dBJXTS, sJJXTS,
					zCTS, zTTS, yHTS);
			wResult.CustomResult.put("C5Total", wC5Total);
			// ⑤C5订单
			wResult.CustomResult.put("C5", wC5List);
			// ⑥C6统计
			List<OMSOrder> wC6List = wOrderList.stream().filter(p -> p.LineID == 2).collect(Collectors.toList());

			bYJG = (int) wC6List.stream()
					.filter(p -> p.RealFinishDate.compareTo(wMSTime) >= 0 && p.RealFinishDate.compareTo(wMETime) <= 0)
					.count();
			lJJG = (int) wC6List.stream().filter(p -> p.RealFinishDate.compareTo(wBaseTime) >= 0).count();
			dBJXTS = 0;
			sJJXTS = 0;
			zCTS = 0;
			zTTS = 0;
			yHTS = 0;
			for (OMSOrder omsOrder : wC6List) {
				dBJXTS += omsOrder.TelegraphRepairStopTimes;
				sJJXTS += omsOrder.ActualRepairStopTimes;
				zCTS += omsOrder.InPlantStopTimes;
				zTTS += omsOrder.OnTheWayStopTimes;
				yHTS += omsOrder.PosterioriStopTimes;
			}

			if (dBJXTS > 0) {
				dBJXTS = new BigDecimal((double) dBJXTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
			if (sJJXTS > 0) {
				sJJXTS = new BigDecimal((double) sJJXTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
			if (zCTS > 0) {
				zCTS = new BigDecimal((double) zCTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
			if (zTTS > 0) {
				zTTS = new BigDecimal((double) zTTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
			if (yHTS > 0) {
				yHTS = new BigDecimal((double) yHTS / wC5List.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}

			AndonStopStatistics wC6Total = new AndonStopStatistics(2, "C6", wC6List.size(), bYJG, lJJG, dBJXTS, sJJXTS,
					zCTS, zTTS, yHTS);
			wResult.CustomResult.put("C6Total", wC6Total);
			// ⑦C6订单
			wResult.CustomResult.put("C6", wC6List);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> Andon_ProductionDaily3(BMSEmployee wLoginUser, int wYear) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①获取年度时间
			Calendar wSTime = Calendar.getInstance();
			wSTime.set(wYear, 0, 1, 0, 0, 0);
			Calendar wETime = Calendar.getInstance();
			wETime.set(wYear, 11, 31, 23, 59, 59);
			// ②根据时间和状态查询订单集合
			List<Integer> wStatusList = new ArrayList<Integer>(Arrays.asList(5, 6, 7, 8));
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectList(wLoginUser, null, null, wSTime, wETime,
					wStatusList, null, "", new ArrayList<Integer>(Arrays.asList(1)), wErrorCode);
			// ③筛选局段和机型
			List<OMSOrder> wList = wOrderList.stream().collect(Collectors.collectingAndThen(
					Collectors.toCollection(() -> new TreeSet<>(Comparator
							.comparing(o -> o.getLineID() + ";" + o.getBureauSectionID() + ";" + o.getProductID()))),
					ArrayList::new));
			// ④统计数据
			List<AndonYearFinishSituation> wTList = new ArrayList<AndonYearFinishSituation>();
			for (OMSOrder omsOrder : wList) {

				// ①1月
				Calendar wSMTime1 = Calendar.getInstance();
				wSMTime1.set(wYear, 0, 1, 0, 0, 0);
				Calendar wEMTime1 = Calendar.getInstance();
				wEMTime1.set(wYear, 1, 1, 0, 0, 0);
				int month1 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime1) >= 0
								&& p.RealFinishDate.compareTo(wEMTime1) < 0)
						.count();
				// ②2月
				Calendar wSMTime2 = Calendar.getInstance();
				wSMTime2.set(wYear, 1, 1, 0, 0, 0);
				Calendar wEMTime2 = Calendar.getInstance();
				wEMTime2.set(wYear, 2, 1, 0, 0, 0);
				int month2 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime2) >= 0
								&& p.RealFinishDate.compareTo(wEMTime2) < 0)
						.count();
				// ③3月
				Calendar wSMTime3 = Calendar.getInstance();
				wSMTime3.set(wYear, 2, 1, 0, 0, 0);
				Calendar wEMTime3 = Calendar.getInstance();
				wEMTime3.set(wYear, 3, 1, 0, 0, 0);
				int month3 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime3) >= 0
								&& p.RealFinishDate.compareTo(wEMTime3) < 0)
						.count();
				// ④4月
				Calendar wSMTime4 = Calendar.getInstance();
				wSMTime4.set(wYear, 3, 1, 0, 0, 0);
				Calendar wEMTime4 = Calendar.getInstance();
				wEMTime4.set(wYear, 4, 1, 0, 0, 0);
				int month4 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime4) >= 0
								&& p.RealFinishDate.compareTo(wEMTime4) < 0)
						.count();
				// ⑤5月
				Calendar wSMTime5 = Calendar.getInstance();
				wSMTime5.set(wYear, 4, 1, 0, 0, 0);
				Calendar wEMTime5 = Calendar.getInstance();
				wEMTime5.set(wYear, 5, 1, 0, 0, 0);
				int month5 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime5) >= 0
								&& p.RealFinishDate.compareTo(wEMTime5) < 0)
						.count();
				// ⑥6月
				Calendar wSMTime6 = Calendar.getInstance();
				wSMTime6.set(wYear, 5, 1, 0, 0, 0);
				Calendar wEMTime6 = Calendar.getInstance();
				wEMTime6.set(wYear, 6, 1, 0, 0, 0);
				int month6 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime6) >= 0
								&& p.RealFinishDate.compareTo(wEMTime6) < 0)
						.count();
				// ⑦7月
				Calendar wSMTime7 = Calendar.getInstance();
				wSMTime7.set(wYear, 6, 1, 0, 0, 0);
				Calendar wEMTime7 = Calendar.getInstance();
				wEMTime7.set(wYear, 7, 1, 0, 0, 0);
				int month7 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime7) >= 0
								&& p.RealFinishDate.compareTo(wEMTime7) < 0)
						.count();
				// ⑧8月
				Calendar wSMTime8 = Calendar.getInstance();
				wSMTime8.set(wYear, 7, 1, 0, 0, 0);
				Calendar wEMTime8 = Calendar.getInstance();
				wEMTime8.set(wYear, 8, 1, 0, 0, 0);
				int month8 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime8) >= 0
								&& p.RealFinishDate.compareTo(wEMTime8) < 0)
						.count();
				// ⑨9月
				Calendar wSMTime9 = Calendar.getInstance();
				wSMTime9.set(wYear, 8, 1, 0, 0, 0);
				Calendar wEMTime9 = Calendar.getInstance();
				wEMTime9.set(wYear, 9, 1, 0, 0, 0);
				int month9 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime9) >= 0
								&& p.RealFinishDate.compareTo(wEMTime9) < 0)
						.count();
				// ⑩10月
				Calendar wSMTime10 = Calendar.getInstance();
				wSMTime10.set(wYear, 9, 1, 0, 0, 0);
				Calendar wEMTime10 = Calendar.getInstance();
				wEMTime10.set(wYear, 10, 1, 0, 0, 0);
				int month10 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime10) >= 0
								&& p.RealFinishDate.compareTo(wEMTime10) < 0)
						.count();
				// ①11月
				Calendar wSMTime11 = Calendar.getInstance();
				wSMTime11.set(wYear, 10, 1, 0, 0, 0);
				Calendar wEMTime11 = Calendar.getInstance();
				wEMTime11.set(wYear, 11, 1, 0, 0, 0);
				int month11 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime11) >= 0
								&& p.RealFinishDate.compareTo(wEMTime11) < 0)
						.count();
				// ②12月
				Calendar wSMTime12 = Calendar.getInstance();
				wSMTime12.set(wYear, 11, 1, 0, 0, 0);
				Calendar wEMTime12 = Calendar.getInstance();
				wEMTime12.set(wYear, 11, 31, 23, 59, 59);
				int month12 = (int) wOrderList.stream()
						.filter(p -> p.LineID == omsOrder.LineID && p.CustomerID == omsOrder.CustomerID
								&& p.ProductID == omsOrder.ProductID && p.RealFinishDate.compareTo(wSMTime12) >= 0
								&& p.RealFinishDate.compareTo(wEMTime12) < 0)
						.count();

				int total = month1 + month2 + month3 + month4 + month5 + month6 + month7 + month8 + month9 + month10
						+ month11 + month12;

				AndonYearFinishSituation wItem = new AndonYearFinishSituation(0, omsOrder.LineID, omsOrder.LineName,
						omsOrder.CustomerID, omsOrder.Customer, total, month1, month2, month3, month4, month5, month6,
						month7, month8, month9, month10, month11, month12);
				wItem.ProductNo = omsOrder.ProductNo;
				wTList.add(wItem);
			}
			// ⑤C5
			List<AndonYearFinishSituation> wC5List = wTList.stream().filter(p -> p.LineID == 1)
					.collect(Collectors.toList());
			wC5List.sort(Comparator.comparing(AndonYearFinishSituation::getCustomerID));
			wResult.CustomResult.put("C5", wC5List);
			// ⑥C6
			List<AndonYearFinishSituation> wC6List = wTList.stream().filter(p -> p.LineID == 2)
					.collect(Collectors.toList());
			wC6List.sort(Comparator.comparing(AndonYearFinishSituation::getCustomerID));
			wResult.CustomResult.put("C6", wC6List);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<AndonDayReport01> Andon_ProductionDaily1(BMSEmployee wLoginUser, int wYear, int wMonth) {
		ServiceResult<AndonDayReport01> wResult = new ServiceResult<AndonDayReport01>();
		wResult.Result = new AndonDayReport01();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①周计划
			List<Integer> wOrderIDList = APSTaskPartDAO.getInstance().SelectAndonList(wLoginUser, wYear, wMonth,
					wErrorCode);
			List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectListByOrderIDList(wLoginUser,
					APSShiftPeriod.Week.getValue(), null, wOrderIDList, wErrorCode);

			// ①获取统计数据
			wResult.Result.TJData = AndonDAO.getInstance().Andon_QueryTJData(wLoginUser, wYear, wMonth, wErrorCode);
			// ②获取工位数据
			wResult.Result.PartData = GetPartData(wLoginUser, wYear, wMonth, wList);
			// ①订单查询
//			List<Integer> wOrderIDList = wList.stream().map(p -> p.OrderID).distinct().collect(Collectors.toList());
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			// ③获取C5在修车数据
			List<OMSOrder> wC5List = wOrderList.stream().filter(p -> p.LineID == 1 && p.Status == 4)
					.collect(Collectors.toList());
			int wLineID = 1;
			wResult.Result.C5RepairingList = GetRepairingList(wLoginUser, wYear, wC5List, wList, wLineID, wErrorCode);
			// ④获取C6在修车数据
			List<OMSOrder> wC6List = wOrderList.stream().filter(p -> p.LineID == 2 && p.Status == 4)
					.collect(Collectors.toList());
			wLineID = 2;
			wResult.Result.C6RepairingList = GetRepairingList(wLoginUser, wYear, wC6List, wList, wLineID, wErrorCode);
			// ⑤获取待回送机车数据
			List<OMSOrder> wBackListC5 = wOrderList.stream()
					.filter(p -> p.LineID == 1 && (p.Status == 5 || p.Status == 6 || p.Status == 7))
					.collect(Collectors.toList());
			List<AndonDayReportCar> wC5BackList = GetRepairingList(wLoginUser, wYear, wBackListC5, wList, 1,
					wErrorCode);
			List<OMSOrder> wBackListC6 = wOrderList.stream()
					.filter(p -> p.LineID == 2 && (p.Status == 5 || p.Status == 6 || p.Status == 7))
					.collect(Collectors.toList());
			List<AndonDayReportCar> wC6BackList = GetRepairingList(wLoginUser, wYear, wBackListC6, wList, 2,
					wErrorCode);
			wResult.Result.C5ToBackList = wC5BackList;
			wResult.Result.C6ToBackList = wC6BackList;

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取维修中的数据
	 */
	private List<AndonDayReportCar> GetRepairingList(BMSEmployee wLoginUser, int wYear, List<OMSOrder> wC5List,
			List<APSTaskPart> wList, int wLineID, OutResult<Integer> wErrorCode) {
		List<AndonDayReportCar> wResult = new ArrayList<AndonDayReportCar>();
		try {
			for (OMSOrder wOMSOrder : wC5List) {
				int stopTime = 0;
				if (wOMSOrder.Status == 4) {
					stopTime = CalendarUtil.getDayInterval(wOMSOrder.RealReceiveDate.getTime(),
							Calendar.getInstance().getTime());
				} else {
					stopTime = CalendarUtil.getDayInterval(wOMSOrder.RealFinishDate.getTime(),
							Calendar.getInstance().getTime());
				}

				List<AndonDayReportDate> planList = GetPlanReportDateList(wLoginUser, wOMSOrder, wLineID, wList);
				List<AndonDayReportDate> realList = GetRealReportDateList(wLoginUser, wOMSOrder, wLineID, wList);
				String remark = GetRemark(wLoginUser, wOMSOrder, wLineID, wList);

				AndonDayReportCar wItem = new AndonDayReportCar(wOMSOrder.ProductNo, wOMSOrder.PartNo.split("#")[1],
						wOMSOrder.LineName, wOMSOrder.Customer, planList, realList, stopTime, remark);
				wResult.add(wItem);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取备注
	 */
	private String GetRemark(BMSEmployee wLoginUser, OMSOrder wOMSOrder, int wLineID, List<APSTaskPart> wList) {
		String wResult = "";
		try {
			List<Integer> wPartIDList = null;
			if (wLineID == 1) {
				wPartIDList = new ArrayList<Integer>(
						Arrays.asList(103, 28, 64, 27, 53, 11, 22, 21, 20, 43, 5, 2, 163, 59, 107, 26));
			} else if (wLineID == 2) {
				wPartIDList = new ArrayList<Integer>(Arrays.asList(103, 28, 64, 27, 53, 11, 114, 135, 112, 111, 115, 43,
						5, 161, 160, 159, 163, 59, 107, 26));
			}
			for (int wPartID : wPartIDList) {
				if (wList.stream().anyMatch(p -> p.OrderID == wOMSOrder.ID && p.LineID == wLineID && p.PartID == wPartID
						&& p.Status == 5)) {
					wResult = wList.stream().filter(p -> p.OrderID == wOMSOrder.ID && p.LineID == wLineID
							&& p.PartID == wPartID && p.Status == 5).findFirst().get().PartName;
					return wResult;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取实际日期
	 */
	private List<AndonDayReportDate> GetRealReportDateList(BMSEmployee wLoginUser, OMSOrder wOMSOrder, int wLineID,
			List<APSTaskPart> wList) {
		List<AndonDayReportDate> wResult = new ArrayList<AndonDayReportDate>();

		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
//			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
//			int wPShiftID = Integer.parseInt(wSDF.format(wOMSOrder.TelegraphRealTime.getTime()));
//			int wRShiftID = Integer.parseInt(wSDF.format(wOMSOrder.RealReceiveDate.getTime()));
//			int wColor = 0;
//			if (wRShiftID > wPShiftID) {
//				wColor = 3;
//			} else {
//				wColor = 2;
//			}

			if (wLineID == 1) {
				// ①入厂
				AndonDayReportDate wDate = new AndonDayReportDate(wOMSOrder.RealReceiveDate, 2);
				wResult.add(wDate);
				// ②机车预检
				int wPartID = 26;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ③机车一级解体
				wPartID = 107;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ④机车外部清洁
				wPartID = 59;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑤转向架解体
				wPartID = 163;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑥机车二级解体
				wPartID = 2;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑦车体检修
				wPartID = 5;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑧车体油漆
				wPartID = 43;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// 上台位
				Calendar wDateTime = AndonDAO.getInstance().Andon_SelectPlaceDate(wLoginUser, wOMSOrder, wErrorCode);
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2010, 0, 1, 0, 0, 0);
				if (wDateTime.compareTo(wBaseTime) > 0) {
					wDate = new AndonDayReportDate(wDateTime, 2);
				} else {
					wDate = new AndonDayReportDate(wDateTime, 0);
				}
				wResult.add(wDate);
				// ⑨电气检修
				wPartID = 20;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑩机制检修
				wPartID = 0;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ①设备吊装
				wPartID = 21;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ②线管连接
				wPartID = 22;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ③转向架总成
				wPartID = 11;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ④机车总成
				wPartID = 53;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑤低压试验
				wPartID = 27;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑥高压试验
				wPartID = 64;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑦试运
				wPartID = 28;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑧交验
				wPartID = 103;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
			} else if (wLineID == 2) {
				// ①入厂
				AndonDayReportDate wDate = new AndonDayReportDate(wOMSOrder.RealReceiveDate, 2);
				wResult.add(wDate);
				// ②机车预检
				int wPartID = 26;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ③机车一级解体
				wPartID = 107;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ④机车外部清洁
				wPartID = 59;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑤转向架解体
				wPartID = 163;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑥机车二级解体1
				wPartID = 159;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑦机车二级解体2
				wPartID = 160;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑧机车二级解体3
				wPartID = 161;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑨车体检修
				wPartID = 5;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑩车体油漆
				wPartID = 43;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// 上台位
				Calendar wDateTime = AndonDAO.getInstance().Andon_SelectPlaceDate(wLoginUser, wOMSOrder, wErrorCode);
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2010, 0, 1, 0, 0, 0);
				if (wDateTime.compareTo(wBaseTime) > 0) {
					wDate = new AndonDayReportDate(wDateTime, 2);
				} else {
					wDate = new AndonDayReportDate(wDateTime, 0);
				}
				wResult.add(wDate);
				// ①组装1
				wPartID = 115;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ②组装2
				wPartID = 111;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ③组装3
				wPartID = 112;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ④组装4
				wPartID = 113;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑤组装5
				wPartID = 114;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑥转向架总成
				wPartID = 11;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑦机车总成
				wPartID = 53;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑧低压试验
				wPartID = 27;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑨高压试验
				wPartID = 64;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ⑩试运
				wPartID = 28;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
				// ①交验
				wPartID = 103;
				AddRealTime(wOMSOrder, wList, wResult, wPartID);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取计划日期
	 */
	private List<AndonDayReportDate> GetPlanReportDateList(BMSEmployee wLoginUser, OMSOrder wOMSOrder, int wLineID,
			List<APSTaskPart> wList) {
		List<AndonDayReportDate> wResult = new ArrayList<AndonDayReportDate>();
		try {
			if (wLineID == 1) {
				// ①入厂
				AndonDayReportDate wDate = new AndonDayReportDate(wOMSOrder.TelegraphRealTime, 1);
				wResult.add(wDate);
				// ②机车预检
				int wPartID = 26;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ③机车一级解体
				wPartID = 107;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ④机车外部清洁
				wPartID = 59;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑤转向架解体
				wPartID = 163;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑥机车二级解体
				wPartID = 2;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑦车体检修
				wPartID = 5;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑧车体油漆
				wPartID = 43;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);

				// 上台位
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2000, 0, 1, 0, 0, 0);
				wDate = new AndonDayReportDate(wBaseTime, 1);
				wResult.add(wDate);

				// ⑨电气检修
				wPartID = 20;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑩机制检修
				wPartID = 0;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ①设备吊装
				wPartID = 21;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ②线管连接
				wPartID = 22;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ③转向架总成
				wPartID = 11;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ④机车总成
				wPartID = 53;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑤低压试验
				wPartID = 27;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑥高压试验
				wPartID = 64;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑦试运
				wPartID = 28;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑧交验
				wPartID = 103;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
			} else if (wLineID == 2) {
				// ①入厂
				AndonDayReportDate wDate = new AndonDayReportDate(wOMSOrder.RealReceiveDate, 1);
				wResult.add(wDate);
				// ②机车预检
				int wPartID = 26;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ③机车一级解体
				wPartID = 107;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ④机车外部清洁
				wPartID = 59;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑤转向架解体
				wPartID = 163;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑥机车二级解体1
				wPartID = 159;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑦机车二级解体2
				wPartID = 160;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑧机车二级解体3
				wPartID = 161;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑨车体检修
				wPartID = 5;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑩车体油漆
				wPartID = 43;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// 上台位
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2000, 0, 1, 0, 0, 0);
				wDate = new AndonDayReportDate(wBaseTime, 1);
				wResult.add(wDate);
				// ①组装1
				wPartID = 115;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ②组装2
				wPartID = 111;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ③组装3
				wPartID = 112;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ④组装4
				wPartID = 113;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑤组装5
				wPartID = 114;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑥转向架总成
				wPartID = 11;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑦机车总成
				wPartID = 53;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑧低压试验
				wPartID = 27;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑨高压试验
				wPartID = 64;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ⑩试运
				wPartID = 28;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
				// ①交验
				wPartID = 103;
				AddPlanTime(wOMSOrder, wList, wResult, wPartID);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 添加计划时间
	 */
	private void AddPlanTime(OMSOrder wOMSOrder, List<APSTaskPart> wList, List<AndonDayReportDate> wResult,
			int wPartID) {
		try {
			if (wList.stream().anyMatch(p -> p.OrderID == wOMSOrder.ID && p.PartID == wPartID)) {
				APSTaskPart apsTaskPart = wList.stream().filter(p -> p.OrderID == wOMSOrder.ID && p.PartID == wPartID)
						.findFirst().get();
				AndonDayReportDate wDateTime = new AndonDayReportDate(apsTaskPart.EndTime, 1);
				wResult.add(wDateTime);
			} else {
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2000, 0, 1, 0, 0, 0);

				AndonDayReportDate wDateTime = new AndonDayReportDate(wBaseTime, 0);
				wResult.add(wDateTime);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 添加实际时间
	 */
	private void AddRealTime(OMSOrder wOMSOrder, List<APSTaskPart> wList, List<AndonDayReportDate> wResult,
			int wPartID) {
		try {
			if (wList.stream().anyMatch(p -> p.OrderID == wOMSOrder.ID && p.PartID == wPartID)) {
				APSTaskPart apsTaskPart = wList.stream().filter(p -> p.OrderID == wOMSOrder.ID && p.PartID == wPartID)
						.findFirst().get();

				if (apsTaskPart.Status == 5) {

					Calendar wEndTime = (Calendar) apsTaskPart.EndTime.clone();
					wEndTime.add(Calendar.HOUR_OF_DAY, -12);

					SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
					int wPShiftID = Integer.parseInt(wSDF.format(wEndTime.getTime()));
					int wRShiftID = Integer.parseInt(wSDF.format(apsTaskPart.FinishWorkTime.getTime()));

					if (wRShiftID > wPShiftID) {
						AndonDayReportDate wDateTime = new AndonDayReportDate(apsTaskPart.FinishWorkTime, 3);
						wResult.add(wDateTime);
					} else {
						AndonDayReportDate wDateTime = new AndonDayReportDate(apsTaskPart.FinishWorkTime, 2);
						wResult.add(wDateTime);
					}
				} else {
					AndonDayReportDate wDateTime = new AndonDayReportDate(apsTaskPart.FinishWorkTime, 0);
					wResult.add(wDateTime);
				}
			} else {
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2000, 0, 1, 0, 0, 0);

				AndonDayReportDate wDateTime = new AndonDayReportDate(wBaseTime, 0);
				wResult.add(wDateTime);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取工位数据
	 */
	private AndonDayReportPart GetPartData(BMSEmployee wLoginUser, int wYear, int wMonth, List<APSTaskPart> wList) {
		AndonDayReportPart wResult = new AndonDayReportPart();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①C5
			// ①入厂
			int wLineID = 1;
			int wRC = AndonDAO.getInstance().Andon_SelectRC(wLoginUser, wYear, wMonth, wLineID, wErrorCode);
			wResult.C5Plan.add(0);
			wResult.C5Real.add(wRC);
			wResult.C5Out.add(0);
			// ②机车预检
			int wPartID = 26;
			AddData(wResult, wList, wLineID, wPartID);
			// ③机车一级解体
			wPartID = 107;
			AddData(wResult, wList, wLineID, wPartID);
			// ④机车外部清洁
			wPartID = 59;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑤转向架解体
			wPartID = 163;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑥机车二级解体
			wPartID = 2;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑦车体检修
			wPartID = 5;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑧车体油漆
			wPartID = 43;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑨上台位

			List<Integer> wOrderIDList = wList.stream().filter(p -> p.LineID == 1).map(p -> p.OrderID).distinct()
					.collect(Collectors.toList());
			int wNumber = AndonDAO.getInstance().Andon_SelectPlaceNumber(wLoginUser, wOrderIDList, wErrorCode);

			wResult.C5Plan.add(0);
			wResult.C5Real.add(wNumber);
			wResult.C5Out.add(0);
			// ⑩电气检修
			wPartID = 20;
			AddData(wResult, wList, wLineID, wPartID);
			// ①机制检修
			wResult.C5Plan.add(0);
			wResult.C5Real.add(0);
			wResult.C5Out.add(0);
			// ②设备吊装
			wPartID = 21;
			AddData(wResult, wList, wLineID, wPartID);
			// ③线管连接
			wPartID = 22;
			AddData(wResult, wList, wLineID, wPartID);
			// ④转向架总成
			wPartID = 11;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑤机车总成
			wPartID = 53;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑥低压试验
			wPartID = 27;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑦高压试验
			wPartID = 64;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑧试运
			wPartID = 28;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑨交验
			wPartID = 103;
			AddData(wResult, wList, wLineID, wPartID);
			// ②C6
			// ①入厂
			wLineID = 2;
			wRC = AndonDAO.getInstance().Andon_SelectRC(wLoginUser, wYear, wMonth, wLineID, wErrorCode);
			wResult.C6Plan.add(0);
			wResult.C6Real.add(wRC);
			wResult.C6Out.add(0);
			// ②机车预检
			wPartID = 26;
			AddData(wResult, wList, wLineID, wPartID);
			// ③机车一级解体
			wPartID = 107;
			AddData(wResult, wList, wLineID, wPartID);
			// ④机车外部清洁
			wPartID = 59;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑤转向架解体
			wPartID = 163;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑥机车二级解体1
			wPartID = 159;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑦机车二级解体2
			wPartID = 160;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑧机车二级解体3
			wPartID = 161;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑨车体检修
			wPartID = 5;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑩车体油漆
			wPartID = 43;
			AddData(wResult, wList, wLineID, wPartID);
			// ①上台位
			wOrderIDList = wList.stream().filter(p -> p.LineID == 2).map(p -> p.OrderID).distinct()
					.collect(Collectors.toList());
			wNumber = AndonDAO.getInstance().Andon_SelectPlaceNumber(wLoginUser, wOrderIDList, wErrorCode);

			wResult.C6Plan.add(0);
			wResult.C6Real.add(wNumber);
			wResult.C6Out.add(0);
			// ②组装1
			wPartID = 115;
			AddData(wResult, wList, wLineID, wPartID);
			// ③组装2
			wPartID = 111;
			AddData(wResult, wList, wLineID, wPartID);
			// ④组装3
			wPartID = 112;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑤组装4
			wPartID = 113;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑥组长5
			wPartID = 114;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑦转向架总成
			wPartID = 11;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑧机车总成
			wPartID = 53;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑨低压试验
			wPartID = 27;
			AddData(wResult, wList, wLineID, wPartID);
			// ⑩高压试验
			wPartID = 64;
			AddData(wResult, wList, wLineID, wPartID);
			// ①试运
			wPartID = 28;
			AddData(wResult, wList, wLineID, wPartID);
			// ②交验
			wPartID = 103;
			AddData(wResult, wList, wLineID, wPartID);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 添加数据
	 */
	private void AddData(AndonDayReportPart wResult, List<APSTaskPart> wList, int wLineID, int wPartID) {
		try {
			int wPlanNumber;
			int wRealNum;
			int wOut;
			wPlanNumber = (int) wList.stream().filter(p -> p.LineID == wLineID && p.PartID == wPartID)
					.map(p -> p.OrderID).distinct().count();
			wRealNum = (int) wList.stream().filter(p -> p.LineID == wLineID && p.PartID == wPartID && p.Status == 5)
					.map(p -> p.OrderID).distinct().count();
			wOut = wPlanNumber - wRealNum < 0 ? 0 : wPlanNumber - wRealNum;
			if (wLineID == 1) {
				wResult.C5Plan.add(wPlanNumber);
				wResult.C5Real.add(wRealNum);
				wResult.C5Out.add(wOut);
			} else {
				wResult.C6Plan.add(wPlanNumber);
				wResult.C6Real.add(wRealNum);
				wResult.C6Out.add(wOut);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<String> Andon_ExportProductionPlan(BMSEmployee wLoginUser) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①周计划
			List<OMSOrder> wOrderList = OMSServiceImpl.getInstance()
					.OMS_QueryOMSOrderList(wLoginUser, -1, -1, "", -1, -1, -1, "", "", 1,
							StringUtils.parseListArgs(OMSOrderStatus.ReceivedTelegraph.getValue(),
									OMSOrderStatus.EnterFactoryed.getValue(), OMSOrderStatus.Repairing.getValue(),
									OMSOrderStatus.FinishedWork.getValue(), OMSOrderStatus.ToOutChcek.getValue(),
									OMSOrderStatus.ToOutConfirm.getValue()))
					.getResult();

			// 移除实际进厂时间在2021-8-29 00:00:00之前的订单
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2021, 7, 29, 0, 0, 0);
			wOrderList.removeIf(p -> p.RealReceiveDate.compareTo(wBaseTime) < 0);

			wOrderList.removeIf(p -> p.Active != 1 || p.ID <= 0);
			if (wOrderList.size() <= 0) {
				wResult.FaultCode += "无在修订单";
				return wResult;
			}

			// 订单排序
			wOrderList.sort(Comparator.comparing(OMSOrder::getERPID).thenComparing(OMSOrder::getRealReceiveDate));

			List<Integer> wOrderIDListTemp = wOrderList.stream().map(p -> p.ID).distinct().collect(Collectors.toList());

			List<APSTaskPart> wTaskPartList = APSServiceImpl.getInstance()
					.APS_QueryTaskPartList(wLoginUser, APSShiftPeriod.Week.getValue(), null, wOrderIDListTemp)
					.getResult();

			if (wTaskPartList == null || wTaskPartList.size() <= 0) {
				wResult.FaultCode += "无在修任务";
				return wResult;
			}

			// 工序计划排序
			wTaskPartList = OrderTaskPart(wTaskPartList, wOrderList);

			// 剔除没有任务的订单
			List<APSTaskPart> wAPSTaskPartClone = wTaskPartList;
			wOrderList.removeIf(p -> !wAPSTaskPartClone.stream().anyMatch(q -> q.OrderID == p.ID));

			List<Integer> wOrderIDList = wTaskPartList.stream().map(p -> p.OrderID).distinct()
					.collect(Collectors.toList());
			// ①主计划pagenumber=1
			List<LFSWorkAreaStation> wWSList = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.filter(p -> p.Active == 1).collect(Collectors.toList());
			wWSList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));

			// 排序
			for (APSTaskPart wAPSTaskPart : wTaskPartList) {
				if (wWSList.stream().anyMatch(p -> p.StationID == wAPSTaskPart.PartID)) {
					wAPSTaskPart.PartOrder = wWSList.stream().filter(p -> p.StationID == wAPSTaskPart.PartID)
							.findFirst().get().OrderNum;
				}
			}
			wTaskPartList.sort(Comparator.comparing(APSTaskPart::getPartOrder));

			List<Integer> wMainPartIDList = wWSList.stream().filter(p -> p.PageNumber == 1).map(p -> p.StationID)
					.distinct().collect(Collectors.toList());

			List<Integer> wMainPartIDListFilter = wTaskPartList.stream()
					.filter(p -> wMainPartIDList.stream().anyMatch(q -> q == p.PartID)).map(p -> p.PartID).distinct()
					.collect(Collectors.toList());

			// ②部件pagenumber=2
			List<Integer> wPartPartIDList = wWSList.stream().filter(p -> p.PageNumber == 4).map(p -> p.StationID)
					.distinct().collect(Collectors.toList());

			List<Integer> wPartPartIDListFilter = wTaskPartList.stream()
					.filter(p -> wPartPartIDList.stream().anyMatch(q -> q == p.PartID)).map(p -> p.PartID).distinct()
					.collect(Collectors.toList());

			// ③驱动pagenumber=3
			List<Integer> wDrivePartIDList = wWSList.stream().filter(p -> p.PageNumber == 2).map(p -> p.StationID)
					.distinct().collect(Collectors.toList());
			List<Integer> wDrivePartIDListFilter = wTaskPartList.stream()
					.filter(p -> wDrivePartIDList.stream().anyMatch(q -> q == p.PartID)).map(p -> p.PartID).distinct()
					.collect(Collectors.toList());

			// ④变压器pagenumber=4
			List<Integer> wTransformerPartIDList = wWSList.stream().filter(p -> p.PageNumber == 3).map(p -> p.StationID)
					.distinct().collect(Collectors.toList());
			List<Integer> wTransformerPartIDListFilter = wTaskPartList.stream()
					.filter(p -> wTransformerPartIDList.stream().anyMatch(q -> q == p.PartID)).map(p -> p.PartID)
					.distinct().collect(Collectors.toList());

			// ②工序计划
			List<Integer> wStepPartList = new ArrayList<Integer>();
			wStepPartList.addAll(wPartPartIDList);
			wStepPartList.addAll(wDrivePartIDList);
			wStepPartList.addAll(wTransformerPartIDList);
			List<APSTaskStepPlan> wStepPlanList = APSServiceImpl.getInstance().APS_QueryStepPlanCompare(wLoginUser,
					StringUtils.Join(",", wOrderIDList), StringUtils.Join(",", wStepPartList),
					APSShiftPeriod.Week.getValue()).Result;

			// 工序计划排序
			wStepPlanList = OrderTaskStepPlan(wStepPlanList, wOrderList);

			// ③导出
			wResult.Result = ExcelUtil.ExportProductPlan(wLoginUser, wTaskPartList, wStepPlanList,
					wMainPartIDListFilter, wPartPartIDListFilter, wDrivePartIDListFilter, wTransformerPartIDListFilter,
					wOrderList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private List<APSTaskPart> OrderTaskPart(List<APSTaskPart> wTaskPartList, List<OMSOrder> wOrderList) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			if (wTaskPartList == null || wTaskPartList.size() <= 0) {
				return wResult;
			}

			for (OMSOrder wOMSOrder : wOrderList) {
				List<APSTaskPart> wList = wTaskPartList.stream().filter(p -> p.OrderID == wOMSOrder.ID)
						.collect(Collectors.toList());
				if (wList != null && wList.size() > 0) {
					wResult.addAll(wList);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private List<APSTaskStepPlan> OrderTaskStepPlan(List<APSTaskStepPlan> wStepPlanList, List<OMSOrder> wOrderList) {
		List<APSTaskStepPlan> wResult = new ArrayList<APSTaskStepPlan>();
		try {
			if (wStepPlanList == null || wStepPlanList.size() <= 0) {
				return wResult;
			}

			for (OMSOrder wOMSOrder : wOrderList) {
				List<APSTaskStepPlan> wList = wStepPlanList.stream().filter(p -> p.OrderID == wOMSOrder.ID)
						.collect(Collectors.toList());
				if (wList != null && wList.size() > 0) {
					wResult.addAll(wList);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<AndonTaskStep>> Andon_QueryAreaStepDetails(BMSEmployee wLoginUser, int wStatus,
			int wWorkAreaID, Calendar wStartTime, Calendar wEndTime, int wOrderID, int wPartID, int wStepID) {
		ServiceResult<List<AndonTaskStep>> wResult = new ServiceResult<List<AndonTaskStep>>();
		wResult.Result = new ArrayList<AndonTaskStep>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wEndTime.set(Calendar.HOUR_OF_DAY, 23);
			wEndTime.set(Calendar.MINUTE, 59);
			wEndTime.set(Calendar.SECOND, 59);

			List<AndonTaskStep> wList = AndonDAO.getInstance().SelectAreaStepDetails(wLoginUser, wStatus, wWorkAreaID,
					wStartTime, wEndTime, wErrorCode);

			// 班组工位
			List<FMCWorkCharge> wWorkCharegeList = APSConstans.GetFMCWorkChargeList();
			for (AndonTaskStep wAndonTaskStep : wList) {
				List<FMCWorkCharge> wItemList = wWorkCharegeList.stream()
						.filter(p -> p.StationID == wAndonTaskStep.PartID).collect(Collectors.toList());
				if (wItemList != null && wItemList.size() > 0) {
					List<String> wNames = wItemList.stream().map(p -> p.ClassName).collect(Collectors.toList());
					wAndonTaskStep.ClassNames = StringUtils.Join(",", wNames);
				}
			}

			List<Integer> wOrderIDList = wList.stream().map(p -> p.OrderID).distinct().collect(Collectors.toList());
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			wOrderList.sort(Comparator.comparing(OMSOrder::getERPID).thenComparing(OMSOrder::getRealReceiveDate));
			for (OMSOrder omsOrder : wOrderList) {
				List<AndonTaskStep> wTempList = wList.stream().filter(p -> p.OrderID == omsOrder.ID)
						.collect(Collectors.toList());
				wResult.Result.addAll(wTempList);
			}
			// 工位排序
			Map<Integer, Integer> wOrderMap = AndonDAO.getInstance().Andon_QueryPartOrderMap(wLoginUser,
					wOrderList.get(0).RouteID, wErrorCode);
			for (AndonTaskStep wAndonTaskStep : wResult.Result) {
				if (wOrderMap.containsKey(wAndonTaskStep.PartID)) {
					wAndonTaskStep.OrderNum = wOrderMap.get(wAndonTaskStep.PartID);
				}
			}
			wResult.Result
					.sort(Comparator.comparing(AndonTaskStep::getOrderNum).thenComparing(AndonTaskStep::getPartID));

			// 条件筛选
			if (wOrderID > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.OrderID == wOrderID)
						.collect(Collectors.toList());
			}
			if (wPartID > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.PartID == wPartID).collect(Collectors.toList());
			}
			if (wStepID > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.StepID == wStepID).collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}
}
