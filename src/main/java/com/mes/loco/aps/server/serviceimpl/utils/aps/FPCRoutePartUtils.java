package com.mes.loco.aps.server.serviceimpl.utils.aps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.cfg.CFGCalendar;
import com.mes.loco.aps.server.service.po.fmc.FMCTimeZone;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;

public class FPCRoutePartUtils {

	private List<FPCRoutePart> APSRoutePartList = new ArrayList<FPCRoutePart>();

	public FPCRoutePartUtils(List<FPCRoutePart> wRoutePartList) {
		APSRoutePartList = wRoutePartList;
		PrevRoutePartMap = APSRoutePartList.stream().collect(Collectors.groupingBy(p -> p.PrevPartID));
	}

	private List<List<Integer>> PartOrderRouteList = new ArrayList<List<Integer>>();

	private Map<Integer, List<FPCRoutePart>> PrevRoutePartMap = new HashMap<Integer, List<FPCRoutePart>>();

	/**
	 * 获取工位工艺路线扁平表
	 * 
	 * @return
	 */
	public List<List<Integer>> GetPartOrderRouteList() {
		if (PartOrderRouteList == null)
			PartOrderRouteList = new ArrayList<List<Integer>>();
		PartOrderRouteList.clear();

		List<FPCRoutePart> wStartList = APSRoutePartList.stream().filter(p -> p.PrevPartID <= 0)
				.collect(Collectors.toList());

		List<Integer> wMainList = null;
		for (FPCRoutePart wFPCRoutePart : wStartList) {
			wMainList = new ArrayList<Integer>();
			SetRoutePartStationList(wMainList, wFPCRoutePart.PartID);
			this.AddResult(PartOrderRouteList, wMainList);
		}
		APSRoutePartList.sort(Comparator.comparing(FPCRoutePart::getOrderID).thenComparing(q -> q.PrevPartID));

		for (FPCRoutePart wFPCRoutePart : APSRoutePartList) {
			if (wFPCRoutePart.NextPartIDMap != null && wFPCRoutePart.NextPartIDMap.size() > 0) {

				List<Integer> wTempList = null;
				for (String wPartIDString : wFPCRoutePart.NextPartIDMap.keySet()) {
					int wPartID = StringUtils.parseInt(wPartIDString);
					if (CheckRouteExist(wFPCRoutePart.PartID, wPartID))
						continue;

					List<List<Integer>> wNextPartIDSuffList = GetPartRouteSuffList(wPartID);

					if (RouteListTemp.size() <= 0) {
						// 首工位 在已算出的工艺路径中不存在 wFPCRoutePart.PartID->wPartID
						// wFPCRoutePart.PartID 是开始工位
						continue;
//						

					} else {
						for (List<Integer> wPrefix : RouteListTemp) {

							if (wNextPartIDSuffList.size() <= 0) {
								// 无任何路径包含此工位 配置不合理可以
								wTempList = CloneTool.CloneArray(wPrefix, Integer.class);
								wTempList.add(wFPCRoutePart.PartID);
								wTempList.add(wPartID);
								this.AddResult(PartOrderRouteList, wTempList);
							} else {
								for (List<Integer> wSuff : wNextPartIDSuffList) {
									wTempList = CloneTool.CloneArray(wPrefix, Integer.class);
									wTempList.add(wFPCRoutePart.PartID);
									wTempList.add(wPartID);
									wTempList.addAll(wSuff);
									this.AddResult(PartOrderRouteList, wTempList);
								}
							}
						}
					}
				}
			}
		}
		// 去重
		int wSize = PartOrderRouteList.size();
		if (wSize > 1) {

			for (int i = 0; i < wSize; i++) {
				if (i >= PartOrderRouteList.size())
					break;
				if (PartOrderRouteList.get(i) == null || PartOrderRouteList.get(i).size() <= 0
						|| IsExit(PartOrderRouteList, i)) {
					PartOrderRouteList.remove(i);
					i--;
				}
			}
		}
		return PartOrderRouteList;
	}

	/**
	 * 根据已排计划 获得未做的工位工艺路径列表
	 * 
	 * @param wDoneStationList
	 * @param wRoutePartIntegerList
	 * @return
	 */
	public static List<List<Integer>> GetUndoStationRouteList(BMSEmployee wLoginUser,
			List<APSTaskPart> wDoneStationList, Map<Integer, APSManuCapacity> wManuCapacityMap,
			List<List<Integer>> wRoutePartIntegerList, int wWrokDay, Map<Integer, APSTaskPart> wUndoOrUnStartTaskList,
			List<FMCTimeZone> wAllZoneList, Map<Integer, List<CFGCalendar>> wCalendarMap) {
		List<List<Integer>> wResult = new ArrayList<List<Integer>>();

		wDoneStationList = wDoneStationList.stream()
				.sorted(Comparator.comparing(APSTaskPart::getShiftPeriod)
						.thenComparing((o1, o2) -> o2.ShiftID - o1.ShiftID) // 班次倒序
						.thenComparing((o1, o2) -> o1.SubmitTime.compareTo(o2.SubmitTime)))
				.collect(Collectors.toList());

		// 将订单任务排序 让周计划排前面 且根据班次排列 然后根据提交时间排列
		APSManuCapacity wManuCapacity = null;
		// 确切做完的工位任务
		Map<Integer, List<APSTaskPart>> wMap = wDoneStationList.stream()
				.filter(p -> p.Active == 1
						&& (p.Status == APSTaskStatus.Done.getValue() || p.Status == APSTaskStatus.Aborted.getValue()))
				.collect(Collectors.groupingBy(p -> p.PartID));

		// 循环将周计划中预计完成的任务剔除出未完成任务
		// 然后根据周计划最大结束时间与月计划比对
		// 月计划在此时间之后的计划预计能完成 将此任务剔除出未完成任务
		APSTaskPart wCreateTaskPart = new APSTaskPart();
		// Load DB
		for (APSTaskPart wAPSTaskPart : wDoneStationList) {
			if (wAPSTaskPart.Active != 1 || (wAPSTaskPart.Status != APSTaskStatus.Issued.getValue()
					&& wAPSTaskPart.Status != APSTaskStatus.Started.getValue()))
				continue;

			if (wMap.containsKey(wAPSTaskPart.PartID))
				continue;

			if (!wManuCapacityMap.containsKey(wAPSTaskPart.PartID))
				continue;

			wManuCapacity = wManuCapacityMap.get(wAPSTaskPart.PartID);

			if (wCreateTaskPart.PartID != wAPSTaskPart.PartID) {
				if (wUndoOrUnStartTaskList.containsKey(wAPSTaskPart.PartID)) {

					wCreateTaskPart = wUndoOrUnStartTaskList.get(wAPSTaskPart.PartID);

					if (wCreateTaskPart.EndTime.compareTo(wAPSTaskPart.EndTime) < 0) {

						wCreateTaskPart.setEndTime(wAPSTaskPart.EndTime);
					}

				} else {
					wCreateTaskPart = new APSTaskPart();
					wCreateTaskPart.setPartID(wAPSTaskPart.PartID);
					wCreateTaskPart.setActive(1);
					wCreateTaskPart.setBOMNo(wAPSTaskPart.BOMNo);
					wCreateTaskPart.setLineName(wAPSTaskPart.LineName);
					wCreateTaskPart.setMaterialName(wAPSTaskPart.MaterialName);
					wCreateTaskPart.setMaterialNo(wAPSTaskPart.MaterialNo);
					wCreateTaskPart.setOrderNo(wAPSTaskPart.OrderNo);
					wCreateTaskPart.setPartName(wAPSTaskPart.PartName);
					wCreateTaskPart.setWorkShopName(wAPSTaskPart.WorkShopName);
					wCreateTaskPart.setLineID(wAPSTaskPart.LineID);
					wCreateTaskPart.setOrderID(wAPSTaskPart.OrderID);
					wCreateTaskPart.setPartNo(wAPSTaskPart.PartNo);
					wCreateTaskPart.setWorkHour(0.0);
					wCreateTaskPart.setCraftMinutes(0);
					wCreateTaskPart.setEndTime(wAPSTaskPart.EndTime);
					wUndoOrUnStartTaskList.put(wAPSTaskPart.PartID, wCreateTaskPart);
				}
			}
			if (wAPSTaskPart.ShiftPeriod == APSShiftPeriod.Week.getValue()) {
				long work = APSWorkTimeCalcUtilsBack.getInstance().CalcIntervalTime(wLoginUser, wCreateTaskPart.PartID,
						wWrokDay, wAPSTaskPart.StartTime, wAPSTaskPart.EndTime, wAllZoneList, wCalendarMap);

				if (((wCreateTaskPart.WorkHour * 60) + work) > (wManuCapacity.WorkHour * 60)) {
					if (!wMap.containsKey(wCreateTaskPart.PartID)) {
						wMap.put(wCreateTaskPart.PartID, StringUtils.parseList(new APSTaskPart[] { wAPSTaskPart }));
					}
					if (wUndoOrUnStartTaskList.containsKey(wCreateTaskPart.PartID))
						wUndoOrUnStartTaskList.remove(wCreateTaskPart.PartID);
				}

			} else if (wAPSTaskPart.ShiftPeriod == APSShiftPeriod.Month.getValue()) {
				if (wAPSTaskPart.EndTime.compareTo(wCreateTaskPart.EndTime) > 0) {
					long work = 0;
					if (wAPSTaskPart.StartTime.compareTo(wCreateTaskPart.EndTime) > 0)
						work = APSWorkTimeCalcUtilsBack.getInstance().CalcIntervalTime(wLoginUser,
								wCreateTaskPart.PartID, wWrokDay, wAPSTaskPart.StartTime, wAPSTaskPart.EndTime,
								wAllZoneList, wCalendarMap);
					else {
						work = APSWorkTimeCalcUtilsBack.getInstance().CalcIntervalTime(wLoginUser,
								wCreateTaskPart.PartID, wWrokDay, wCreateTaskPart.EndTime, wAPSTaskPart.EndTime,
								wAllZoneList, wCalendarMap);
					}
					if (((wCreateTaskPart.WorkHour * 60) + work) > (wManuCapacity.WorkHour * 60)) {
						if (!wMap.containsKey(wCreateTaskPart.PartID)) {
							wMap.put(wCreateTaskPart.PartID, StringUtils.parseList(new APSTaskPart[] { wAPSTaskPart }));
						}
						if (wUndoOrUnStartTaskList.containsKey(wCreateTaskPart.PartID))
							wUndoOrUnStartTaskList.remove(wCreateTaskPart.PartID);
					}
				}
			}

		}

		// 预计做完的工位任务

		for (List<Integer> list : wRoutePartIntegerList) {

			for (int i = 0; i < list.size(); i++) {
				if (wMap.containsKey(list.get(i))) {
					continue;
				}
				wResult.add(StringUtils.parseList(Arrays.copyOfRange(list.toArray(new Integer[0]), i, list.size())));
				break;
			}
		}
		// 去重
		int wSize = wResult.size();
		if (wSize > 1) {

			for (int i = 0; i < wSize; i++) {
				if (i >= wResult.size())
					break;
				if (wResult.get(i) == null || wResult.get(i).size() <= 0 || IsExit(wResult, i)) {
					wResult.remove(i);
					i--;
				}
			}
		}

		return wResult;
	}

	/**
	 * 根据已排计划 获得未排程的工位工艺路径列表
	 * 
	 * @param wDoneStationList
	 * @param wRoutePartIntegerList
	 * @return
	 */
	public static List<List<Integer>> GetUndoStationRouteList(BMSEmployee wLoginUser, Calendar wStartTime,
			List<APSTaskPart> wDoneStationList, List<List<Integer>> wRoutePartIntegerList) {
		List<List<Integer>> wResult = new ArrayList<List<Integer>>();
		try {
//			Map<Integer, List<APSTaskPart>> wMap = wDoneStationList.stream()
//					.filter(p -> p.StartTime.compareTo(wStartTime) < 0 && p.Active == 1
//							&& (p.Status == APSTaskStatus.Issued.getValue()
//									|| p.Status == APSTaskStatus.Started.getValue()
//									|| p.Status == APSTaskStatus.Suspend.getValue()
//									|| p.Status == APSTaskStatus.Done.getValue()
//									|| p.Status == APSTaskStatus.Aborted.getValue()))
//					.collect(Collectors.groupingBy(p -> p.PartID));

			Map<Integer, List<APSTaskPart>> wMap = wDoneStationList.stream()
					.filter(p -> p.Active == 1 && (p.Status == APSTaskStatus.Confirm.getValue()
							|| p.Status == APSTaskStatus.Started.getValue()
							|| p.Status == APSTaskStatus.Suspend.getValue() || p.Status == APSTaskStatus.Done.getValue()
							|| p.Status == APSTaskStatus.Aborted.getValue()))
					.collect(Collectors.groupingBy(p -> p.PartID));

			int wRepeatIndex = -1;
			// 循环工艺路线判断哪个任务没有任务

			for (List<Integer> list : wRoutePartIntegerList) {

				wRepeatIndex = -1;
				for (int i = 0; i < list.size(); i++) {
					if (wMap.containsKey(list.get(i))) {
						wRepeatIndex = i;
					}
				}

				wResult.add(StringUtils
						.parseList(Arrays.copyOfRange(list.toArray(new Integer[0]), wRepeatIndex + 1, list.size())));
			}
			// 去重
			int wSize = wResult.size();
			if (wSize > 1) {

				for (int i = 0; i < wSize; i++) {
					if (i >= wResult.size())
						break;
					if (wResult.get(i) == null || wResult.get(i).size() <= 0 || IsExit(wResult, i)) {
						wResult.remove(i);
						i--;
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return wResult;
	}

	private List<List<Integer>> RouteListTemp = new ArrayList<>();

	private boolean AddResult(List<List<Integer>> wPartOrderRouteList, List<Integer> wTempList) {
		if (IsExit(wPartOrderRouteList, wTempList)) {
			return false;
		}
//		System.out.println(wTempList);
		return wPartOrderRouteList.add(wTempList);
	}

	private boolean CheckRouteExist(Integer wPartID, Integer wNextPartID) {
		List<Integer> wRouteTemp = null;
		RouteListTemp.clear();
		for (List<Integer> wRoute : PartOrderRouteList) {

			if (wRoute == null || wRoute.size() <= 0)
				continue;

			wRouteTemp = new ArrayList<Integer>();
			for (int i = 0; i < wRoute.size(); i++) {

				if (wRoute.get(i) == wPartID) {
					wRouteTemp = StringUtils.parseList(Arrays.copyOfRange(wRoute.toArray(new Integer[0]), 0, i));

					if (wRoute.size() > (i - 2) && wRoute.get(i) == wNextPartID) {
						return true;
					}
				}

			}
			if (wRouteTemp.size() > 0)
				RouteListTemp.add(wRouteTemp);

		}

		// 去重
		int wSize = RouteListTemp.size();
		if (wSize > 1) {

			for (int i = 0; i < wSize; i++) {
				if (i >= RouteListTemp.size())
					break;
				if (RouteListTemp.get(i) == null || RouteListTemp.get(i).size() <= 0 || IsExit(RouteListTemp, i)) {
					RouteListTemp.remove(i);
					i--;
					wSize--;
				}
			}
		}
		return false;
	}

	private static boolean IsExit(List<List<Integer>> wAllList, int wIndex) {
		for (int i = 0; i < wAllList.size(); i++) {
			if (i == wIndex)
				continue;
			if (IsSameAs(wAllList.get(i), wAllList.get(wIndex)))
				return true;
		}
		return false;
	}

	private static boolean IsExit(List<List<Integer>> wAllList, List<Integer> wTempList) {
		for (int i = 0; i < wAllList.size(); i++) {

			if (IsSameAs(wAllList.get(i), wTempList))
				return true;
		}
		return false;
	}

	private static boolean IsSameAs(List<Integer> wList1, List<Integer> wList2) {

		if (wList1 != null && wList2 != null && wList1.size() >= wList2.size()) {
			for (int i = 0; i < wList2.size(); i++) {
				if (wList1.get(i).intValue() != wList2.get(i).intValue()) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	private List<List<Integer>> GetPartRouteSuffList(int wPartID) {
		List<List<Integer>> wResult = new ArrayList<List<Integer>>();

		List<Integer> wRouteTemp = null;
		for (List<Integer> wRoute : PartOrderRouteList) {

			if (wRoute == null || wRoute.size() <= 0)
				continue;
			wRouteTemp = new ArrayList<>();
			for (int i = 0; i < wRoute.size(); i++) {

				if (wRoute.get(i) != wPartID)

					continue;
				if (i == wRoute.size() - 1)
					continue;

				wRouteTemp = StringUtils
						.parseList(Arrays.copyOfRange(wRoute.toArray(new Integer[0]), i + 1, wRoute.size()));
			}
			if (wRouteTemp.size() > 0)
				wResult.add(wRouteTemp);
		}
		// 去重
		int wSize = wResult.size();
		if (wSize > 1) {

			for (int i = 0; i < wSize; i++) {
				if (i >= wResult.size())
					break;
				if (wResult.get(i) == null || wResult.get(i).size() <= 0 || IsExit(wResult, i)) {
					wResult.remove(i);
					i--;
					wSize--;
				}
			}
		}
		return wResult;
	}

	/**
	 * 根据PrevID生成工位顺序
	 * 
	 * @param wStationList
	 * @param wStationID
	 * @param wFPCRoutePartMap
	 */
	private void SetRoutePartStationList(List<Integer> wStationList, int wStationID) {

		wStationList.add(wStationID);
		if (PrevRoutePartMap.containsKey(wStationID)) {
			List<FPCRoutePart> wFPCRoutePartList = PrevRoutePartMap.get(wStationID);
			if (wFPCRoutePartList != null && wFPCRoutePartList.size() > 0) {

				List<Integer> wClone = CloneTool.CloneArray(wStationList, Integer.class);
				List<Integer> wClone_S = new ArrayList<Integer>();
				for (int i = 0; i < wFPCRoutePartList.size(); i++) {
					if (i > 0) {
						if (wStationList.contains(wFPCRoutePartList.get(i).getPartID()))
							continue;

						wClone_S = CloneTool.CloneArray(wClone, Integer.class);
						SetRoutePartStationList(wClone_S, wFPCRoutePartList.get(i).getPartID());
						this.AddResult(PartOrderRouteList, wClone_S);

					} else {
						SetRoutePartStationList(wStationList, wFPCRoutePartList.get(i).getPartID());
					}

				}
			}

		}

	}

}
