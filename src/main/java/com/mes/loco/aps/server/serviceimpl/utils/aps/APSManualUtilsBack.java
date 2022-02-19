package com.mes.loco.aps.server.serviceimpl.utils.aps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.APSInstallCheckMode;
import com.mes.loco.aps.server.service.mesenum.APSMsgTypes;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.OMSOrderStatus;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.aps.APSDismantling;
import com.mes.loco.aps.server.service.po.aps.APSInstallation;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.aps.APSMessage;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.cfg.CFGCalendar;
import com.mes.loco.aps.server.service.po.fmc.FMCTimeZone;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.oms.OMSOutsourceOrder;
import com.mes.loco.aps.server.service.utils.StringUtils;

public class APSManualUtilsBack {
	private static Logger logger = LoggerFactory.getLogger(APSManualUtilsBack.class);

	public APSManualUtilsBack() {
	}

	private static APSManualUtilsBack Instance;

	public static APSManualUtilsBack getInstance() {
		if (Instance == null)
			Instance = new APSManualUtilsBack();
		return Instance;
	}

	/**
	 * ==手动排程
	 * 
	 * @param wLoginUser
	 * @param wOrderList
	 * @param wCheckTaskList       ==手动修改后的任务列表
	 * @param wOrderPartIssuedList ==订单列表中的订单 已经 下达/开工/暂停 的工位计划 周 月 排月计划时
	 *                             既包含周计划也包含月计划 排周计划/滚动计划时只包含周计划
	 * @param wOutsourceOrderList  ==委外订单列表
	 * @param StartTime            ==排程开始时间
	 * @param wEndTime             ==排程结束时间
	 * @param wRoutePartList       ==工位工艺路径 Key:OrderID Value:
	 * @param wManuCapacityList    ==工位加工能力明细
	 * @param wAPSInstallationList ==工位安装明细
	 * @param wAPSDismantlingList  ==工位拆解明细
	 * @param wErrorCode           ==返回错误码
	 * @param wMessageList         ==返回冲突消息
	 * @return
	 */
	public List<APSMessage> APS_CheckTaskPart(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			List<APSTaskPart> wCheckTaskList, APSShiftPeriod wShiftPeriod, List<APSTaskPart> wOrderPartIssuedList,
			List<OMSOutsourceOrder> wOutsourceOrderList, Calendar wStartTime, Calendar wEndTime,
			Map<Integer, List<FPCRoutePart>> wRoutePartList, List<APSManuCapacity> wManuCapacityList,
			List<APSInstallation> wAPSInstallationList, List<APSDismantling> wAPSDismantlingList, int wWorkDay,
			List<FMCTimeZone> wAllZoneList, Map<Integer, List<CFGCalendar>> wCalendarMap,
			OutResult<Integer> wErrorCode) {

		List<APSMessage> wResult = new ArrayList<APSMessage>();
		try {
			// 安装拆解只为算出安装工位是否能正常排程
			// 考虑当前订单上次排程情况 并需要多次时间转序
			// 考虑所排工位剩余容量
			// 一单单排
			wStartTime = StringUtils.FormatCalendar(wStartTime, "yyyy-MM-dd");
			wEndTime = StringUtils.FormatCalendar(wEndTime, "yyyy-MM-dd");

			Calendar wTempStartTime = (Calendar) wStartTime.clone();
			// 生成开始时间车辆预计占用工位列表

			wOrderPartIssuedList.removeIf(p -> p.EndTime.compareTo(p.StartTime) <= 0);

			wCheckTaskList.removeIf(p -> p.EndTime.compareTo(p.StartTime) <= 0);

			// 获取任务结束时间不大于开始时间的所有任务 也就是他们是预计已完成的工位任务
			Map<Integer, List<APSTaskPart>> wAPSTaskPartDoneListMap = wOrderPartIssuedList.stream()
					.filter(p -> p.StartTime.compareTo(wTempStartTime) <= 0 && p.Active == 1
							&& (p.Status == APSTaskStatus.Issued.getValue()
									|| p.Status == APSTaskStatus.Started.getValue()
									|| p.Status == APSTaskStatus.Suspend.getValue()
									|| p.Status == APSTaskStatus.Done.getValue()
									|| p.Status == APSTaskStatus.Aborted.getValue()))
					.sorted((o1, o2) -> o2.StartTime.compareTo(o1.StartTime))
					.collect(Collectors.groupingBy(p -> p.OrderID));

			// 将工艺路径转换从工艺路径列表
			Map<Integer, List<List<Integer>>> wPartRouteListLineMap = new HashMap<Integer, List<List<Integer>>>();
			for (Integer wOrderID : wRoutePartList.keySet()) {

				FPCRoutePartUtils wFPCRoutePartUtils = new FPCRoutePartUtils(wRoutePartList.get(wOrderID));
				wPartRouteListLineMap.put(wOrderID, wFPCRoutePartUtils.GetPartOrderRouteList());
			}

			Map<Integer, APSManuCapacity> wManuCapacityMap = null;

			APSManuCapacity wManuCapacity = null;

			List<OMSOutsourceOrder> wOutsourceList = null;

			List<FPCRoutePart> wRoutePartOrder = null;

			List<String> wMsgStringList = null;

			APSMessage wMsg = null;

			Map<Integer, List<APSTaskPart>> wTaskPartOrderMap = wCheckTaskList.stream()
					.collect(Collectors.groupingBy(p -> p.OrderID));

			// 根据已排计划检查其工位加工负荷
			wResult.addAll(APSWorkTimeCalcUtilsBack.getInstance().CheckBuildTask(wCheckTaskList, wManuCapacityList));

			// 检查工位计划中物料是否满足
			wResult.addAll(CheckTaskPartMaterial(wCheckTaskList, wOrderPartIssuedList, wOutsourceList,
					wAPSInstallationList, wAPSDismantlingList));

			// 根据未做首工位检查对应工位计划加工工时是否满足
			List<APSTaskPart> wCheckOrderTaskList = null;
			for (OMSOrder wOrder : wOrderList) {

				List<List<Integer>> wRoutePartIntegerList = new ArrayList<List<Integer>>();

				if (!wPartRouteListLineMap.containsKey(wOrder.getID()))
					continue;

				if (!wRoutePartList.containsKey(wOrder.getID()))
					continue;
				if (!wTaskPartOrderMap.containsKey(wOrder.ID))
					continue;

				wCheckOrderTaskList = wTaskPartOrderMap.get(wOrder.ID);
				// 检查预计进厂日期
				if (wOrder.Status == OMSOrderStatus.HasOrder.getValue()
						|| wOrder.Status == OMSOrderStatus.ReceivedTelegraph.getValue()) {
					wResult.addAll(CheckOrderPlantDate(wCheckOrderTaskList, wOrder));
				}

				wManuCapacityMap = wManuCapacityList.stream().filter(p -> p.LineID == wOrder.LineID).collect(
						Collectors.toMap(APSManuCapacity::getPartID, Function.identity(), (key1, key2) -> key2));

				wOutsourceList = wOutsourceOrderList.stream().filter(p -> p.OrderID == wOrder.getID())
						.collect(Collectors.toList());

				wRoutePartOrder = wRoutePartList.get(wOrder.getID());

				wRoutePartIntegerList = wPartRouteListLineMap.get(wOrder.getID());

				// 获取此订单已做工位列表
				List<APSTaskPart> wDoneStationList = new ArrayList<APSTaskPart>();
				if (wAPSTaskPartDoneListMap.containsKey(wOrder.getID()))
					wDoneStationList = wAPSTaskPartDoneListMap.get(wOrder.getID());

				Map<Integer, APSTaskPart> wUndoOrUnStartTaskList = new HashMap<Integer, APSTaskPart>();

//				// 根据初始工位列表&工艺路径列表&已做工位列表 获取剩余工艺路径列表
//				List<List<Integer>> wPartRouteList = FPCRoutePartUtils.GetUndoStationRouteList(wLoginUser,
//						wDoneStationList, wManuCapacityMap, wRoutePartIntegerList, wWorkDay, wUndoOrUnStartTaskList,
//						wAllZoneList, wCalendarMap);
//				
				// 根据初始工位列表&工艺路径列表&已排程工位列表 获取剩余未排程工艺路径列表
				List<List<Integer>> wPartRouteList = FPCRoutePartUtils.GetUndoStationRouteList(wLoginUser,
						(Calendar) wStartTime.clone(), wDoneStationList, wRoutePartIntegerList);

				Optional<APSTaskPart> wOptional = null;
				APSTaskPart wUndoAPSTaskPart = null;

				APSTaskPart wBuildAPSTaskPart = null;

				// 循环剩余工艺路径首工位的加工工时是否满足
				for (Integer wInteger : wPartRouteList.stream().map(p -> p.get(0)).distinct()
						.collect(Collectors.toList())) {

					if (!wManuCapacityMap.containsKey(wInteger))
						continue;

					wManuCapacity = wManuCapacityMap.get(wInteger);

					if (wUndoOrUnStartTaskList.containsKey(wInteger))
						wUndoAPSTaskPart = wUndoOrUnStartTaskList.get(wInteger);
					else {
						wUndoAPSTaskPart = new APSTaskPart();
					}

					wOptional = wCheckOrderTaskList.stream().filter(p -> p.PartID == wInteger).findFirst();
					if (!wOptional.isPresent()) {
						continue;
						// 未排计划 不管
					}
					wBuildAPSTaskPart = wOptional.get();

					long wMinus = APSWorkTimeCalcUtilsBack.getInstance().CalcIntervalTime(wLoginUser, wManuCapacity.PartID,
							wWorkDay, (Calendar)wBuildAPSTaskPart.StartTime.clone(), (Calendar)wBuildAPSTaskPart.EndTime.clone(), wAllZoneList,
							wCalendarMap);
					if ((wUndoAPSTaskPart.WorkHour * 60) + wMinus < (wManuCapacity.WorkHour * 60)) {
						wMsg = new APSMessage();
						wMsg.ID = 0;
						wMsg.OrderID = wBuildAPSTaskPart.OrderID;
						wMsg.LineID = wBuildAPSTaskPart.LineID;
						wMsg.WorkShopID = wBuildAPSTaskPart.WorkShopID;
						wMsg.PartID = wBuildAPSTaskPart.PartID;
						wMsg.ProductNo = wBuildAPSTaskPart.ProductNo;
						wMsg.Type = APSMsgTypes.Work.getValue();
						wMsg.MessageText = StringUtils.Format("工位【{0}】计划工时({1})小于标准工时({2})", wBuildAPSTaskPart.PartName,
								(wUndoAPSTaskPart.WorkHour + (wMinus / 60)), wManuCapacity.WorkHour);

						wResult.add(wMsg);
					}

					if (CheckMergeRoutePart(wRoutePartOrder, wInteger)) {
						List<FPCRoutePart> wPrevRoutePartList = GetPrevRoutePartList(wCheckOrderTaskList, wInteger,
								wRoutePartOrder);
						if (wPrevRoutePartList.size() > 0) {

							wMsgStringList = new ArrayList<String>();

							for (FPCRoutePart wFPCRoutePart : wPrevRoutePartList) {
								wOptional = wCheckOrderTaskList.stream().filter(p -> p.PartID == wFPCRoutePart.PartID)
										.findFirst();

								if (!wOptional.isPresent()) {
									wMsgStringList.add(StringUtils.Format("前置工位【{0}】未排产", wFPCRoutePart.Name));
								} else if (wOptional.get().EndTime.compareTo(wBuildAPSTaskPart.StartTime) > 0) {
									wMsgStringList.add(StringUtils.Format("前置工位【{0}】未完成 ", wFPCRoutePart.Name));
								}
							}
							if (wMsgStringList.size() > 0) {
								wMsg = new APSMessage();
								wMsg.ID = 0;
								wMsg.OrderID = wBuildAPSTaskPart.OrderID;
								wMsg.LineID = wBuildAPSTaskPart.LineID;
								wMsg.WorkShopID = wBuildAPSTaskPart.WorkShopID;
								wMsg.PartID = wBuildAPSTaskPart.PartID;
								wMsg.ProductNo = wBuildAPSTaskPart.ProductNo;
								wMsg.Type = APSMsgTypes.Tech.getValue();
								wMsg.MessageText = StringUtils.Join("\n", wMsgStringList);
								wResult.add(wMsg);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	private List<APSMessage> CheckOrderPlantDate(List<APSTaskPart> wCheckTaskList, OMSOrder wOrder) {
		List<APSMessage> wResult = new ArrayList<APSMessage>();
		APSMessage wMsg = null;
		for (APSTaskPart wAPSTaskPart : wCheckTaskList) {
			if (wOrder.PlanReceiveDate.compareTo(wAPSTaskPart.StartTime) < 0)
				continue;
			wMsg = new APSMessage();
			wMsg.ID = 0;
			wMsg.OrderID = wAPSTaskPart.OrderID;
			wMsg.LineID = wAPSTaskPart.LineID;
			wMsg.WorkShopID = wAPSTaskPart.WorkShopID;
			wMsg.PartID = wAPSTaskPart.PartID;
			wMsg.ProductNo = wAPSTaskPart.ProductNo;
			wMsg.Type = APSMsgTypes.Receive.getValue();
			wMsg.MessageText = StringUtils.Format("预计台车进厂时间【0】，计划开始时台车未进厂",
					StringUtils.parseCalendarToString(wOrder.getPlanReceiveDate(), "yyyy-MM-dd"));
			wResult.add(wMsg);
		}

		return wResult;
	}

	/**
	 * 检查委外物料是否齐全
	 * 
	 * @param wCheckTaskList
	 * @param wOrderPartIssuedList
	 * @param wOutsourceList
	 * @param wAPSInstallationList
	 * @param wAPSDismantlingList
	 * @return
	 */
	private List<APSMessage> CheckTaskPartMaterial(List<APSTaskPart> wCheckTaskList,
			List<APSTaskPart> wOrderPartIssuedList, List<OMSOutsourceOrder> wOutsourceList,
			List<APSInstallation> wAPSInstallationList, List<APSDismantling> wAPSDismantlingList) {

		List<APSMessage> wResult = new ArrayList<APSMessage>();

		Calendar wBaseTime = Calendar.getInstance();
		wBaseTime.set(2000, 1, 1);

		Calendar wInstalStartTime = null;
		for (APSTaskPart wTaskPart : wCheckTaskList) {

			// step1 == 根据其安装拆解 得到最小开始时间
			// step2 == 根据委外订单 得到最快可接收物料时刻
			List<APSInstallation> wOrderInstallationList = wAPSInstallationList.stream()
					.filter(p -> p.LineID == wTaskPart.LineID && p.PartID == wTaskPart.PartID)
					.collect(Collectors.toList());
			if (wOrderInstallationList != null && wOrderInstallationList.size() > 0) {
				// 获取其对应物料的拆解工序 拆解一般都是委外的拆解

				for (APSDismantling wDismantling : wAPSDismantlingList) {
					if (wDismantling.LineID != wTaskPart.LineID)
						continue;
					Optional<APSInstallation> wInstallationOptional = wOrderInstallationList.stream()
							.filter(q -> q.MaterialID == wDismantling.MaterialID).findFirst();

					if (!wInstallationOptional.isPresent()) {
						continue;
					}
					wInstalStartTime = (Calendar) wTaskPart.StartTime.clone();
					APSTaskPart wPrevTaskPart = null;

					// region ==获取其工位任务
					Optional<APSTaskPart> wTaskPartOptional = wCheckTaskList.stream()
							.filter(p -> p.PartID == wDismantling.PartID && p.LineID == wDismantling.LineID
									&& p.OrderID == wTaskPart.OrderID)
							.sorted((o1, o2) -> o2.StartTime.compareTo(o1.StartTime)).findFirst();

					if (wTaskPartOptional.isPresent()) {
						wPrevTaskPart = wTaskPartOptional.get();
					} else {
						// 已做 获取其订单已排产且有效工位任务 获取其结束时长
						wPrevTaskPart = GetHistroyTaskPart(wOrderPartIssuedList, wTaskPart.OrderID, wTaskPart.LineID,
								wTaskPart.PartID);

					}
					if (wPrevTaskPart.PartID <= 0) {
						wPrevTaskPart.EndTime = (Calendar) wBaseTime.clone();
					}

					wInstalStartTime = (Calendar) wPrevTaskPart.EndTime.clone();

					wInstalStartTime.add(Calendar.MINUTE, (int) (wDismantling.getProPurchaseDur() * 60));

					// endRegion

					// region ==根据委外订单获取最快可接收物料时刻

					Calendar wOutsourceReceiveTime = (Calendar) wBaseTime.clone();

					List<OMSOutsourceOrder> wOutsourceListM = wOutsourceList.stream()
							.filter(p -> p.OrderID == wTaskPart.OrderID && p.MaterialID == wDismantling.MaterialID)
							.collect(Collectors.toList());

					for (int i = 0; i < wOutsourceListM.size(); i++) {

						Calendar wCalendar = (wOutsourceListM.get(i - 1).AtualReceiveDate.compareTo(wBaseTime) > 0
								? wOutsourceListM.get(i - 1).AtualReceiveDate
								: wOutsourceListM.get(i - 1).PlanReceiveDate);

						if (wOutsourceReceiveTime.compareTo(wCalendar) < 0)
							wOutsourceReceiveTime = (Calendar) wCalendar.clone();

					}
					// endRegion
					if (wInstalStartTime.compareTo(wOutsourceReceiveTime) < 0)
						wInstalStartTime = wOutsourceReceiveTime;

					APSInstallation wInstallation = wInstallationOptional.get();
					switch (APSInstallCheckMode.getEnumType(wInstallation.InstallCheckMode)) {
					case Default:
						break;
					case CheckChange:
					case CheckChangePrev:
					case OnlyCheck:
						// 判断结束时长
						if (wInstalStartTime.compareTo(wTaskPart.StartTime) > 0) {
							APSMessage wAPSMessage = new APSMessage();

							wAPSMessage.ID = 0;
							wAPSMessage.OrderID = wTaskPart.OrderID;
							wAPSMessage.LineID = wTaskPart.LineID;
							wAPSMessage.WorkShopID = wTaskPart.WorkShopID;
							wAPSMessage.PartID = wTaskPart.PartID;
							wAPSMessage.ProductNo = wTaskPart.ProductNo;
							wAPSMessage.Type = APSMsgTypes.Material.getValue();
							wAPSMessage.MessageText = StringUtils.Format("物料：{0} 编号：{1} 的物料到厂时间为{2}",
									wInstallation.MaterialName, wInstallation.MaterialNo,
									StringUtils.FormatCalendar(wInstalStartTime, "yyyy-MM-dd HH:mm"));
							wResult.add(wAPSMessage);
						}

						break;
					case Uncheck:
						break;
					default:
						break;
					}

				}

			}

		}
		return wResult;

	}

	/**
	 * 根据工艺路径获取其上到工位路径列表
	 * 
	 * @param wBuilderTaskList
	 * @param wRoutePartList
	 * @return
	 */
	private List<FPCRoutePart> GetPrevRoutePartList(List<APSTaskPart> wBuilderTaskList, int wPartID,
			List<FPCRoutePart> wRoutePartList) {

		List<FPCRoutePart> wResult = new ArrayList<FPCRoutePart>();

		List<Integer> wPartIDList = new ArrayList<Integer>();
		for (FPCRoutePart wFPCRoutePart : wRoutePartList) {

			if (wFPCRoutePart.PartID == wPartID && wFPCRoutePart.PrevPartID > 0) {
				if (!wPartIDList.contains(wFPCRoutePart.PrevPartID))
					wPartIDList.add(wFPCRoutePart.PrevPartID);
			}
			if (wFPCRoutePart.NextPartIDMap == null || wFPCRoutePart.NextPartIDMap.size() <= 0
					|| !wFPCRoutePart.NextPartIDMap.containsKey(String.valueOf(wPartID)))
				continue;
			if (!wPartIDList.contains(wFPCRoutePart.PartID) && wFPCRoutePart.PartID > 0)
				wPartIDList.add(wFPCRoutePart.PartID);

		}

		wResult = wRoutePartList.stream().filter(p -> wPartIDList.contains(p.PartID)).collect(Collectors.toList());
		return wResult;
	}

	/**
	 * 是否是合并工位 需要考虑
	 * 
	 * @param wRoutePartList
	 * @param wPartID
	 * @return
	 */
	private boolean CheckMergeRoutePart(List<FPCRoutePart> wRoutePartList, Integer wPartID) {

		List<Integer> wPartIDList = new ArrayList<Integer>();

		for (FPCRoutePart wFPCRoutePart : wRoutePartList) {
			if (wPartID == wFPCRoutePart.PartID && wFPCRoutePart.PrevPartID > 0) {

				wPartIDList.add(wFPCRoutePart.PrevPartID);
				continue;
			}

			if (wFPCRoutePart.NextPartIDMap == null || wFPCRoutePart.NextPartIDMap.size() <= 0) {
				continue;
			}
			for (String wPartIDString : wFPCRoutePart.NextPartIDMap.keySet()) {
				if (wPartID == StringUtils.parseInt(wPartIDString) && !wPartIDList.contains(wFPCRoutePart.PartID)) {
					wPartIDList.add(wFPCRoutePart.PartID);
				}
			}
		}
		if (wPartIDList.size() > 1)
			return true;

		return false;
	}

	/**
	 * 获取此订单此工位历史已排任务 获取其订单已排产且有效工位任务
	 * 
	 * @param wLoginUser
	 * @param wOrderID
	 * @param wPartID
	 * @return
	 */
	private APSTaskPart GetHistroyTaskPart(List<APSTaskPart> wTaskPartList, int wOrderID, int wLineID, int wPartID) {
		APSTaskPart wResult = new APSTaskPart();
		for (APSTaskPart p : wTaskPartList) {
			if (p.OrderID == wOrderID && p.LineID == wLineID && p.PartID == wPartID && p.Active == 1) {
				if (wResult.ID <= 0) {
					wResult = p;
				}
			}
		}

		return wResult;
	}

}
