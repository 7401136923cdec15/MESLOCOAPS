package com.mes.loco.aps.server.serviceimpl.utils.aps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.OMSOrderPriority;
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
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.shristool.LoggerTool;

/**
 * 自动排程工具类
 * 
 * @author Femi.Yang
 *
 */
public class APSAutoUtilsBack {
	private static Logger logger = LoggerFactory.getLogger(APSAutoUtilsBack.class);

	public APSAutoUtilsBack() {
	}

	private static APSAutoUtilsBack Instance;

	public static APSAutoUtilsBack getInstance() {
		if (Instance == null)
			Instance = new APSAutoUtilsBack();
		return Instance;
	}

	/**
	 * 设置订单优先级
	 * 
	 * @param wLoginUser
	 * @param wOrderList
	 * @param wOrderPriorityList
	 * @param wLineOrders
	 * @param wCustomerOrders
	 * @return
	 */
	public List<OMSOrder> APS_SetOrderPriority(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			List<OMSOrderPriority> wOrderPriorityList, List<Integer> wLineOrders, List<Integer> wCustomerOrders,
			List<Integer> wProductIDs) {
		try {
			// 局段顺序
			// 修程顺序
			if (wOrderList == null || wOrderList.size() <= 0)
				return wOrderList;

			if (wOrderPriorityList == null || wOrderPriorityList.size() <= 0)
				return wOrderList;

			for (int i = wOrderPriorityList.size() - 1; i >= 0; i--) {
				OMSOrderPriority wOrderPriority = wOrderPriorityList.get(i);
				switch (wOrderPriority) {
				case Default:
					break;
				case RealReceiveDate:

					wOrderList
							.sort(Comparator.comparing((OMSOrder p) -> p.Status).reversed().thenComparing((o1, o2) -> {
								return o1.PlanReceiveDate.compareTo(o2.PlanReceiveDate);
							}));
					break;
				case Line:
					if (wLineOrders == null || wLineOrders.size() <= 0)
						break;
					wOrderList.sort((o1, o2) -> {
						int wIndex1 = wLineOrders.indexOf(o1.getLineID());
						int wIndex2 = wLineOrders.indexOf(o2.getLineID());
						if (wIndex1 == -1) {
							wIndex1 = wLineOrders.size();
						}
						if (wIndex2 == -1) {
							wIndex2 = wLineOrders.size();
						}
						return wIndex1 - wIndex2;
					});

					break;
				case PlanFinishDate:
					wOrderList.sort((o1, o2) -> o1.PlanFinishDate.compareTo(o2.PlanFinishDate));
					break;
				case BureauSection:
					if (wCustomerOrders == null || wCustomerOrders.size() <= 0)
						break;
					wOrderList.sort((o1, o2) -> {

						int wIndex1 = wCustomerOrders.indexOf(o1.getBureauSectionID());
						int wIndex2 = wCustomerOrders.indexOf(o2.getBureauSectionID());
						if (wIndex1 == -1) {
							wIndex1 = wCustomerOrders.size();
						}
						if (wIndex2 == -1) {
							wIndex2 = wCustomerOrders.size();
						}
						return wIndex1 - wIndex2;
					});
					break;
				case ProductNo:
					if (wProductIDs == null || wProductIDs.size() <= 0)
						break;
					wOrderList.sort((o1, o2) -> {
						int wIndex1 = wProductIDs.indexOf(o1.getProductID());
						int wIndex2 = wProductIDs.indexOf(o2.getProductID());
						if (wIndex1 == -1) {
							wIndex1 = wProductIDs.size();
						}
						if (wIndex2 == -1) {
							wIndex2 = wProductIDs.size();
						}
						return wIndex1 - wIndex2;
					});
					break;
				default:
					break;
				}
			}

			int wPriority = wOrderList.size();
			for (OMSOrder wOMSOrder : wOrderList) {
				wOMSOrder.Priority = wPriority;
				wPriority--;
			}

			// 订单优先级只与当前需要比较的订单列表中有效 并不存储
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wOrderList;
	}

	/**
	 * ==自动排程
	 * 
	 * @param wLoginUser
	 * @param wOrderList
	 * @param wOrderPartIssuedList ==订单列表中的订单 已经 下达/开工/暂停 的工位计划 周 月 排月计划时 包含月计划
	 *                             排周计划/滚动计划时只包含周计划
	 * @param wOutsourceOrderList  ==委外订单列表
	 * @param StartTime            ==排程开始时间
	 * @param wEndTime             ==排程结束时间
	 * @param wRoutePartList       ==工位工艺路径 key:OrderID Value:
	 * @param wManuCapacityList    ==工位加工能力明细
	 * @param wAPSInstallationList ==工位安装明细
	 * @param wAPSDismantlingList  ==工位拆解明细
	 * @param wErrorCode           ==返回错误码
	 * @param wMessageList         ==返回冲突消息
	 * @return
	 */
	public List<APSTaskPart> APS_AutoTaskPart(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			APSShiftPeriod wShiftPeriod, List<APSTaskPart> wOrderPartIssuedList,
			List<OMSOutsourceOrder> wOutsourceOrderList, Calendar wStartTime, Calendar wEndTime,
			Map<Integer, List<FPCRoutePart>> wRoutePartList, List<APSManuCapacity> wManuCapacityList,
			List<APSInstallation> wAPSInstallationList, List<APSDismantling> wAPSDismantlingList,
			List<APSMessage> wMessageList, int wWorkDay, List<FMCTimeZone> wAllZoneList,
			Map<Integer, List<CFGCalendar>> wCalendarMap, int wLimitMinutes, OutResult<Integer> wErrorCode) {

		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {

			// 安装拆解只为算出安装工位是否能正常排程
			// 考虑当前订单上次排程情况 并需要多次时间转序
			// 考虑所排工位剩余容量
			// 一单单排
			wStartTime = StringUtils.FormatCalendar(wStartTime, "yyyy-MM-dd");
			wEndTime = StringUtils.FormatCalendar(wEndTime, "yyyy-MM-dd");

			Calendar wTempStartTime = (Calendar) wStartTime.clone();
			// 生成开始时间车辆预计占用工位列表

			if (wShiftPeriod == APSShiftPeriod.Week) {
				wOrderPartIssuedList = wOrderPartIssuedList.stream()
						.filter(p -> p.ShiftPeriod == APSShiftPeriod.Week.getValue()).collect(Collectors.toList());
			}

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

			List<APSTaskPart> wOrderTaskPartList = null;
			Map<Integer, APSManuCapacity> wManuCapacityMap = null;
			APSManuCapacity wManuCapacity = null;

			List<FPCRoutePart> wRoutePartOrder = null;
			List<OMSOutsourceOrder> wOutsourceList = null;
			for (OMSOrder wOrder : wOrderList) {
				long wStartMillis = System.currentTimeMillis();
				wOrderTaskPartList = new ArrayList<APSTaskPart>();

				List<List<Integer>> wRoutePartIntegerList = new ArrayList<List<Integer>>();

				if (!wPartRouteListLineMap.containsKey(wOrder.getID()))
					continue;

				if (!wRoutePartList.containsKey(wOrder.getID()))
					continue;

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

				// 根据初始工位列表&工艺路径列表&已做工位列表 获取剩余工艺路径列表
//				List<List<Integer>> wPartRouteList = FPCRoutePartUtils.GetUndoStationRouteList(wLoginUser,
//						wDoneStationList, wManuCapacityMap, wRoutePartIntegerList, wWorkDay, wUndoOrUnStartTaskList,
//						wAllZoneList, wCalendarMap);

				// 根据初始工位列表&工艺路径列表&已排程工位列表 获取剩余未排程工艺路径列表
				List<List<Integer>> wPartRouteList = FPCRoutePartUtils.GetUndoStationRouteList(wLoginUser,
						(Calendar) wStartTime.clone(), wDoneStationList, wRoutePartIntegerList);

				Calendar wOrderStartTime = (Calendar) wStartTime.clone(); // 需要考虑当天是否上班 以及考虑班次工时问题 确定开始时间

				// 预计进厂日期
				if ((wOrder.Status == OMSOrderStatus.HasOrder.getValue()
						|| wOrder.Status == OMSOrderStatus.ReceivedTelegraph.getValue())
						&& wOrderStartTime.compareTo(wOrder.PlanReceiveDate) < 0) {
					wOrderStartTime = (Calendar) wOrder.PlanReceiveDate.clone();
				}

				for (List<Integer> wIntegerList : wPartRouteList) {
					wOrderStartTime = (Calendar) wStartTime.clone();
					int i = 0;
					for (Integer wInteger : wIntegerList) {
						i++;
						if (wManuCapacityMap.containsKey(wInteger)) {
							wManuCapacity = wManuCapacityMap.get(wInteger);
						} else {
							wManuCapacity = new APSManuCapacity();
							wManuCapacity.setPartID(wInteger);
						}

						Optional<APSTaskPart> wOptional = null;
						APSTaskPart wAPSTaskPart = null;

						if (i == 1 && wUndoOrUnStartTaskList.containsKey(wInteger)) {

							wAPSTaskPart = wUndoOrUnStartTaskList.get(wInteger);
						} else {
							wAPSTaskPart = new APSTaskPart();
						}

						if (wAPSTaskPart.PartID <= 0) {

							// 判断是否有已排任务 有则获取已排任务
							// 是否为合并路径工位点 若是 则获取已排任务 判断其开始时间大小 并修改此开始时间为 比较大的值 相应修改其后已排任务的开始结束时间
							wOptional = wOrderTaskPartList.stream().filter(p -> p.PartID == wInteger).findFirst();
							// 若已排过此工位任务就需要判断是否是合并工位 若是需要比对开始时间并修改
							if (wOptional.isPresent()) {
								wAPSTaskPart = wOptional.get();
								// 是否为合并路径工位点
								if (!CheckMergeRoutePart(wRoutePartOrder, wInteger)) {
									// 否 不做任何处理
									continue;
								}
								// 是判断是否与此时间有不同
								if (wOrderStartTime.compareTo(wAPSTaskPart.StartTime) <= 0) {
									// 时间比之前小 也不做任何处理
									wOrderStartTime = (Calendar) wAPSTaskPart.EndTime.clone();
									continue;
								}
								// 时间比之前大 修改其开始和结束时间 并影响其后所有工位任务
								wAPSTaskPart.StartTime = (Calendar) wOrderStartTime.clone();
								EditTaskTime(wLoginUser, wAPSTaskPart, wShiftPeriod, wRoutePartOrder, wOutsourceList,
										(Calendar) wOrderStartTime.clone(), wOrderTaskPartList, wResult, wManuCapacity,
										wOrderPartIssuedList, wManuCapacityMap, wAPSInstallationList,
										wAPSDismantlingList, wWorkDay, true, wMessageList, wAllZoneList, wCalendarMap,
										wLimitMinutes);

								wOrderStartTime = (Calendar) wAPSTaskPart.EndTime.clone();
								continue;

							} else {
								wAPSTaskPart = new APSTaskPart(wLoginUser, wManuCapacity, wOrder,
										wShiftPeriod.getValue());
								wAPSTaskPart.StartTime = (Calendar) wOrderStartTime.clone();
							}
						}

						this.EditTaskTime(wLoginUser, wAPSTaskPart, wShiftPeriod, wRoutePartOrder, wOutsourceList,
								(Calendar) wOrderStartTime.clone(), wOrderTaskPartList, wResult, wManuCapacity,
								wOrderPartIssuedList, wManuCapacityMap, wAPSInstallationList, wAPSDismantlingList,
								wWorkDay, false, wMessageList, wAllZoneList, wCalendarMap, wLimitMinutes);

						if (wAPSTaskPart.ID > 0) {
							wAPSTaskPart.ID = 0;
							if (wAPSTaskPart.StartTime.compareTo(wStartTime) < 0)
								wAPSTaskPart.StartTime = (Calendar) wStartTime.clone();
							wAPSTaskPart.ShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID,
									(Calendar) wStartTime.clone(), wShiftPeriod, FMCShiftLevel.Default, 0);
						}
						wOrderTaskPartList.add(wAPSTaskPart);
						wOrderStartTime = (Calendar) wAPSTaskPart.EndTime.clone();

					}

				}
				long wEndMillis = System.currentTimeMillis();
				int wCallMS = (int) (wEndMillis - wStartMillis);
				LoggerTool.MonitorFunction("排程", "Step__" + wOrder.ID, wCallMS);
				wResult.addAll(wOrderTaskPartList);
			}

			/*
			 * 避免时间超过 删除原因：避免工位任务没有出来 Calendar wEndTimeMax = (Calendar) wEndTime.clone();
			 * wEndTimeMax.add(Calendar.DAY_OF_MONTH, 1); wResult =
			 * wResult.stream().filter(p -> p.StartTime.compareTo(wEndTimeMax) <
			 * 0).collect(Collectors.toList());
			 * 
			 * wResult.forEach(p -> { if (p.EndTime.compareTo(wEndTimeMax) > 0) p.EndTime =
			 * (Calendar) wEndTimeMax.clone(); });
			 */

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 是否是合并工位
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
	 * 
	 * @param wTaskPart
	 * @param wStartime
	 * @param wBuilderTaskList
	 * @param wStationAllTimeList
	 * @param wManuCapacity
	 * @param wManuCapacityMap
	 * @param wAPSInstallationList
	 * @param wAPSDismantlingList
	 * @param wIsMergeNext
	 * @return
	 */
	private void EditTaskTime(BMSEmployee wLoginUser, APSTaskPart wTaskPart, APSShiftPeriod wShiftPeriod,
			List<FPCRoutePart> wRoutePartList, List<OMSOutsourceOrder> wOutsourceList, Calendar wStartime,
			List<APSTaskPart> wBuilderTaskList, List<APSTaskPart> wStationAllTimeList, APSManuCapacity wManuCapacity,
			List<APSTaskPart> wOrderPartIssuedList, Map<Integer, APSManuCapacity> wManuCapacityMap,
			List<APSInstallation> wAPSInstallationList, List<APSDismantling> wAPSDismantlingList, int wWorkDay,
			boolean wIsMergeNext, List<APSMessage> wAPSMessageList, List<FMCTimeZone> wAllZoneList,
			Map<Integer, List<CFGCalendar>> wCalendarMap, int wLimitMinutes) {

		// 根据前面订单排程结果获取需要排的工位占用时间段 wResult
		List<APSTaskPart> wStationTimeList = wStationAllTimeList.stream()
				.filter(p -> p.PartID == wTaskPart.PartID && p.OrderID != wTaskPart.OrderID)
				.collect(Collectors.toList());

		Calendar wBaseTime = Calendar.getInstance();
		wBaseTime.set(2000, 1, 1);

		long wWorkMinus = (long) (wManuCapacity.WorkHour * 60);
		long wTechMinus = (long) (wManuCapacity.Period * 60);

		if (wTaskPart.WorkHour > 0) {
			wWorkMinus -= (wTaskPart.WorkHour * 60);

//			if (wTaskPart.PartNo == "") {
//				System.out.println("");
//			}

			Calendar wWorkEndTime = APSWorkTimeCalcUtilsBack.getInstance().CalcTaskEndTime(wLoginUser, wTaskPart.PartID,
					wStartime, wWorkMinus, 0, wWorkDay, wAllZoneList, wCalendarMap, 0);
			wTechMinus = (wStartime.getTimeInMillis() - wWorkEndTime.getTimeInMillis()) / 60000;
		}

		if (wShiftPeriod == APSShiftPeriod.Week) {
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

					Calendar wInstalStartTime = (Calendar) wStartime.clone();
					APSTaskPart wPrevTaskPart = null;

					// region ==获取其工位任务
					Optional<APSTaskPart> wTaskPartOptional = wBuilderTaskList.stream()
							.filter(p -> p.PartID == wDismantling.PartID && p.LineID == wDismantling.LineID)
							.findFirst();

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

					// 判断此物料是否是必须的
					switch (APSInstallCheckMode.getEnumType(wInstallation.InstallCheckMode)) {
					case Default:
						break;
					case CheckChange:
					case CheckChangePrev:

						// 判断结束时长
						if (wInstalStartTime.compareTo(wStartime) > 0) {
							wStartime = wInstalStartTime;
						}
						break;
					case OnlyCheck:
						// 判断结束时长
						if (wInstalStartTime.compareTo(wStartime) > 0) {
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
							wAPSMessageList.add(wAPSMessage);
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

		// region ==根据其加工能力判断获取大于此开始时间的工位空闲加工周期 并修改开始时间和结束时间

		// step3 == 根据工位占用详情 获取其在最小开始时间后的空闲时间段
		Map<Calendar, Calendar> wFreeTimeInterval = APSWorkTimeCalcUtilsBack.getInstance()
				.CalcFreeTimeInterval(wStationTimeList, wManuCapacity.getFQTY(), wStartime);

		// 由于设置了100 \\年时间为结束区间 所以不可能没有空闲时间匹配

		// step4 == 循环遍历空闲时间段 并检查时间段是否满足加工工时和工艺工时需求
		for (Calendar wFreeTimeS : wFreeTimeInterval.keySet()) {
			Calendar wFreeTimeE = wFreeTimeInterval.get(wFreeTimeS);

			if (wTaskPart.getPartID() > 0 && wFreeTimeS.compareTo(wStartime) > 0) {
				wWorkMinus = (long) (wManuCapacity.WorkHour * 60);
				wTechMinus = (long) (wManuCapacity.Period * 60);
			}
			Calendar wTaskEndTime = APSWorkTimeCalcUtilsBack.getInstance().CalcTaskEndTime(wLoginUser, wTaskPart.PartID,
					wFreeTimeS, wWorkMinus, wTechMinus, wWorkDay, wAllZoneList, wCalendarMap, 0);

			if (wTaskEndTime.compareTo(wFreeTimeE) <= 0) {
				wTaskPart.StartTime = wFreeTimeS;
				wTaskPart.EndTime = (Calendar) wTaskEndTime.clone();
				break;
			}
		}

		// endRegion

		if (wIsMergeNext) {
			// 获取NextList
			List<APSTaskPart> wNextList = GetNextTaskPartList(wBuilderTaskList, wTaskPart.PartID, wRoutePartList);
			for (APSTaskPart apsTaskPart : wNextList) {
				APSManuCapacity manuCapacity = null;
				if (wManuCapacityMap.containsKey(apsTaskPart.PartID))
					manuCapacity = wManuCapacityMap.get(apsTaskPart.PartID);
				else
					manuCapacity = new APSManuCapacity();
				EditTaskTime(wLoginUser, apsTaskPart, wShiftPeriod, wRoutePartList, wOutsourceList,
						(Calendar) wTaskPart.EndTime.clone(), wBuilderTaskList, wStationAllTimeList, manuCapacity,
						wOrderPartIssuedList, wManuCapacityMap, wAPSInstallationList, wAPSDismantlingList, wWorkDay,
						wIsMergeNext, wAPSMessageList, wAllZoneList, wCalendarMap, wLimitMinutes);
			}

		}

	}

	/**
	 * 根据工艺路径获取其下到工位任务列表
	 * 
	 * @param wBuilderTaskList
	 * @param wRoutePartList
	 * @return
	 */
	private List<APSTaskPart> GetNextTaskPartList(List<APSTaskPart> wBuilderTaskList, int wPartID,
			List<FPCRoutePart> wRoutePartList) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();

		List<Integer> wPartIDList = new ArrayList<Integer>();
		for (FPCRoutePart wFPCRoutePart : wRoutePartList) {

			if (wFPCRoutePart.PrevPartID == wPartID) {
				wPartIDList.add(wFPCRoutePart.PartID);
			}
			if (wFPCRoutePart.PartID == wPartID) {
				wPartIDList
						.addAll(StringUtils.parseIntList(wFPCRoutePart.NextPartIDMap.keySet().toArray(new String[0])));
			}

		}

		wResult = wBuilderTaskList.stream().filter(p -> wPartIDList.contains(p.PartID)).collect(Collectors.toList());
		return wResult;
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
