package com.mes.loco.aps.server.serviceimpl.utils.aps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.APSMsgTypes;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.aps.APSMessage;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.cfg.CFGCalendar;
import com.mes.loco.aps.server.service.po.fmc.FMCTimeZone;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.shristool.LoggerTool;

public class APSWorkTimeCalcUtilsBack {

	private static Logger logger = LoggerFactory.getLogger(APSWorkTimeCalcUtilsBack.class);

	public APSWorkTimeCalcUtilsBack() {
	}

	private static APSWorkTimeCalcUtilsBack Instance;

	public static APSWorkTimeCalcUtilsBack getInstance() {
		if (Instance == null)
			Instance = new APSWorkTimeCalcUtilsBack();
		return Instance;
	}

	/**
	 * 根据开始时间 工作时长 工艺时长 计算结束时刻 已修改完成
	 * 
	 * @param wLoginUser
	 * @param wStartTime
	 * @param wWorkMinus
	 * @param wTechMinus
	 * @return
	 */
	public Calendar CalcTaskEndTime(BMSEmployee wLoginUser, int wPartID, Calendar wStartTime, long wWorkMinus,
			long wTechMinus, int wWorkDay, List<FMCTimeZone> wAllZoneList, Map<Integer, List<CFGCalendar>> wCalendarMap,
			int wLimitMinus) {
		Calendar wResult = (Calendar) wStartTime.clone();
//		long _wWorkMinus = wWorkMinus;
		try {

//			if (wPartID == 20) {
//				System.out.println("S--==--" + StringUtils.parseCalendarToString(wStartTime, "yyyy-MM-dd HH:mm"));
//				if (_wWorkMinus == 0) {
//					System.out.println("t");
//				}
//			}

			long wStartMillis = System.currentTimeMillis();
			List<CFGCalendar> wHolidayList = null;
			if (wCalendarMap == null || !wCalendarMap.containsKey(wPartID)) {
				wHolidayList = new ArrayList<CFGCalendar>();
			}
			wHolidayList = wCalendarMap.get(wPartID);

			Map<Calendar, Integer> wWorkMap = wAllZoneList.stream().filter(p -> p.isIdleOrWork())
					.collect(Collectors.toMap(FMCTimeZone::getStartTime, p -> p.Minutes));

			List<Calendar> wCaSList = wWorkMap.keySet().stream().sorted((o1, o2) -> o1.compareTo(o2))
					.collect(Collectors.toList());

			Calendar wMaxWorkTime = Calendar.getInstance();
			wMaxWorkTime.set(2000, 0, 1, 0, 0, 0);
			if (wCaSList.size() < 1) {
				wResult.add(Calendar.MINUTE, (int) wWorkMinus);
				return wResult;
			} else {
				wMaxWorkTime.set(2000, 0, 1, wCaSList.get(wCaSList.size() - 1).get(Calendar.HOUR_OF_DAY),
						wCaSList.get(wCaSList.size() - 1).get(Calendar.MINUTE),
						wCaSList.get(wCaSList.size() - 1).get(Calendar.SECOND));
				wMaxWorkTime.add(Calendar.MINUTE, wWorkMap.get(wCaSList.get(wCaSList.size() - 1)));
			}

			// 开始计算
			boolean wIsEdit = false;
			while (wWorkMinus > 0) {
				if (IsTodayRest(wResult, wHolidayList)) {
					if (!wIsEdit) {
						wStartTime.add(Calendar.DATE, 1);
						wStartTime.set(Calendar.HOUR_OF_DAY, 0);
						wStartTime.set(Calendar.MINUTE, 0);
					}
					wResult.add(Calendar.DATE, 1);
					wResult.set(Calendar.HOUR_OF_DAY, 0);
					wResult.set(Calendar.MINUTE, 0);
					continue;
				}
				wMaxWorkTime.set(Calendar.YEAR, wResult.get(Calendar.YEAR));
				wMaxWorkTime.set(Calendar.MONTH, wResult.get(Calendar.MONTH));
				wMaxWorkTime.set(Calendar.DAY_OF_MONTH, wResult.get(Calendar.DAY_OF_MONTH));

				if (wMaxWorkTime.compareTo(wResult) < 0) {
					wResult.add(Calendar.DATE, 1);
					wResult.set(Calendar.HOUR_OF_DAY, 0);
					wResult.set(Calendar.MINUTE, 0);
					wResult.set(Calendar.MINUTE, 0);
					continue;
				}
				for (Calendar wZoneS : wCaSList) {
					Calendar wTodayZoneS = (Calendar) wZoneS.clone();
					int wBeUseMin = wWorkMap.get(wZoneS);

					wTodayZoneS.set(Calendar.YEAR, wResult.get(Calendar.YEAR));
					wTodayZoneS.set(Calendar.MONTH, wResult.get(Calendar.MONTH));
					wTodayZoneS.set(Calendar.DAY_OF_MONTH, wResult.get(Calendar.DAY_OF_MONTH));

					Calendar wTodayZoneE = (Calendar) wTodayZoneS.clone();
					wTodayZoneE.add(Calendar.MINUTE, wBeUseMin);

					if (!wIsEdit) {
						if (wTodayZoneS.compareTo(wStartTime) > 0) {
							wStartTime.set(Calendar.YEAR, wTodayZoneS.get(Calendar.YEAR));
							wStartTime.set(Calendar.MONTH, wTodayZoneS.get(Calendar.MONTH));
							wStartTime.set(Calendar.DAY_OF_MONTH, wTodayZoneS.get(Calendar.DAY_OF_MONTH));
							wStartTime.set(Calendar.HOUR_OF_DAY, wTodayZoneS.get(Calendar.HOUR_OF_DAY));
							wStartTime.set(Calendar.MINUTE, wTodayZoneS.get(Calendar.MINUTE));
							wStartTime.set(Calendar.SECOND, wTodayZoneS.get(Calendar.SECOND));
						}
						wIsEdit = true;
					}

					if (wTodayZoneE.compareTo(wResult) < 0) {
						continue;
					}

					if (wTodayZoneS.compareTo(wResult) > 0) {
						wResult = (Calendar) wTodayZoneS.clone();
					} else {
						wBeUseMin -= (int) ((wResult.getTimeInMillis() - wTodayZoneS.getTimeInMillis()) / 60000);
					}

					if (wBeUseMin >= wWorkMinus) {
						wResult.add(Calendar.MINUTE, (int) wWorkMinus);
						wWorkMinus = 0;
						break;
					} else {
						wResult.add(Calendar.MINUTE, (int) wBeUseMin);
						wWorkMinus -= wBeUseMin;
					}
				}

				if (wWorkMinus > 0) {
					// 加一天
					if (wWorkMinus > wLimitMinus) {
						wResult.add(Calendar.DATE, 1);
						wResult.set(Calendar.HOUR_OF_DAY, 0);
						wResult.set(Calendar.MINUTE, 0);
						wResult.set(Calendar.MINUTE, 0);
					} else {
						wResult.add(Calendar.MINUTE, (int) wWorkMinus);
						break;
					}
				}
			}

			// 加工艺时长
			wResult.add(Calendar.MINUTE, (int) wTechMinus);

			long wEndMillis = System.currentTimeMillis();
			int wCallMS = (int) (wEndMillis - wStartMillis);

//			if (wPartID == 20) {
//				System.out.println(StringUtils.parseCalendarToString(wStartTime, "yyyy-MM-dd HH:mm") + "--==--"
//						+ StringUtils.parseCalendarToString(wResult, "yyyy-MM-dd HH:mm") + "--==--" + _wWorkMinus);
//			}
			LoggerTool.MonitorFunction("APSWorkTimeCalcUtils", "CalcTaskEndTime", wCallMS);
		} catch (Exception ex) {
			logger.error(ex.toString());
			ex.printStackTrace();
		}
		return wResult;
	}

	/**
	 * 计算时间区间最大工作时长 单位分钟
	 * 
	 * @param wLoginUser
	 * @param wStartTime
	 * @param wEndTime
	 * @param wWorkDayID 作息ID
	 * @return
	 */
	public long CalcIntervalTime(BMSEmployee wLoginUser, int wPartID, int wWorkDayID, Calendar wStartTime,
			Calendar wEndTime, List<FMCTimeZone> wAllZoneList, Map<Integer, List<CFGCalendar>> wCalendarMap) {
		long wResult = 0L;
		try {
			long wStartMillis = System.currentTimeMillis();
			if (wStartTime.compareTo(wEndTime) >= 0)
				return wResult;

			if (wCalendarMap == null || !wCalendarMap.containsKey(wPartID))
				return wResult;
			List<CFGCalendar> wHolidayList = wCalendarMap.get(wPartID);
			// 开始计算
			int wTotalMinus = 0;

			label: while (wStartTime.compareTo(wEndTime) < 0) {
				if (IsTodayRest(wStartTime, wHolidayList)) {
					wStartTime.add(Calendar.DATE, 1);
					wStartTime.set(Calendar.HOUR, 0);
					wStartTime.set(Calendar.MINUTE, 0);
				} else {
					// 获取StartTime的年、月、日
					int wStartYear = wStartTime.get(Calendar.YEAR);
					int wStartMonth = wStartTime.get(Calendar.MONTH);
					int wStartDate = wStartTime.get(Calendar.DATE);
					for (FMCTimeZone wTimeZone : wAllZoneList) {
						// 获取TimeZone的时、分
						int wTimeZoneHour = wTimeZone.StartTime.get(Calendar.HOUR_OF_DAY);
						int wTimeZoneMinute = wTimeZone.StartTime.get(Calendar.MINUTE);
						// 到了第二天
						if (wTimeZoneHour < wAllZoneList.get(0).StartTime.get(Calendar.HOUR_OF_DAY)) {
							wStartDate++;
						}
						// 构造开始时间点
						Calendar wStartPoint = Calendar.getInstance();
						wStartPoint.set(wStartYear, wStartMonth, wStartDate, wTimeZoneHour, wTimeZoneMinute, 0);
						// 构造结束时间点
						Calendar wEndPoint = Calendar.getInstance();
						wEndPoint.set(wStartYear, wStartMonth, wStartDate, wTimeZoneHour, wTimeZoneMinute, 0);
						wEndPoint.add(Calendar.MINUTE, wTimeZone.Minutes);
						if (wEndTime.compareTo(wStartPoint) < 0) {
							break label;
						} else if (wEndTime.compareTo(wEndPoint) < 0) {
							if (!wTimeZone.isIdleOrWork()) {
								break label;
							} else {
								long wTimeOne = wEndTime.getTimeInMillis();
								long wTimeTwo = wStartPoint.getTimeInMillis();
								long wMinutes = (wTimeOne - wTimeTwo) / (1000 * 60);
								wTotalMinus += wMinutes;
								break label;
							}
						} else {
							if (wTimeZone.isIdleOrWork())
								wTotalMinus += wTimeZone.Minutes;
						}
					}
					wStartTime.add(Calendar.DATE, 1);
					wStartTime.set(Calendar.HOUR, 0);
					wStartTime.set(Calendar.MINUTE, 0);
				}
			}
			wResult = wTotalMinus;
//			System.out.println("最大工作时长:" + wResult);

			long wEndMillis = System.currentTimeMillis();
			int wCallMS = (int) (wEndMillis - wStartMillis);
			LoggerTool.MonitorFunction("APSWorkTimeCalcUtils", "CalcIntervalTime", wCallMS);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<APSMessage> CheckBuildTask(List<APSTaskPart> wBuilderTaskList,
			List<APSManuCapacity> wManuCapacityList) {
		List<APSMessage> wResult = new ArrayList<APSMessage>();

		for (APSManuCapacity wManuCapacity : wManuCapacityList) {

			List<APSTaskPart> wTaskPartList = wBuilderTaskList.stream().filter(p -> p.PartID == wManuCapacity.PartID)
					.collect(Collectors.toList());

			wResult.addAll(CheckBuildTask(wTaskPartList, wManuCapacity.FQTY));
		}

		return wResult;
	}

	private List<APSMessage> CheckBuildTask(List<APSTaskPart> wBuilderTaskList, double wFQTY) {
		List<APSMessage> wResult = new ArrayList<APSMessage>();

		APSTaskPart wTask1 = null;
		APSTaskPart wTask2 = null;
		List<APSTaskPart> wReList = null;
		APSMessage wMsg = null;
		for (int i = 0; i < wBuilderTaskList.size(); i++) {

			wTask1 = wBuilderTaskList.get(i);

			wReList = new ArrayList<APSTaskPart>();

			wReList.add(wTask1);

			if (wTask1.EndTime.compareTo(wTask1.StartTime) <= 0) {
				continue;
			}

			for (int j = 0; j < wBuilderTaskList.size(); j++) {
				if (i == j) {
					continue;
				}

				wTask2 = wBuilderTaskList.get(j);
				if (wTask2.EndTime.compareTo(wTask2.StartTime) <= 0) {
					continue;
				}

				if (wTask2.StartTime.compareTo(wTask1.EndTime) >= 0
						|| wTask2.EndTime.compareTo(wTask1.StartTime) <= 0) {
					continue;
				}
				wReList.add(wTask2);
			}
			if (wReList.size() > wFQTY) {

				for (APSTaskPart wTaskPart : wReList) {
					wMsg = new APSMessage();

					wMsg.ID = 0;
					wMsg.OrderID = wTaskPart.OrderID;
					wMsg.LineID = wTaskPart.LineID;
					wMsg.WorkShopID = wTaskPart.WorkShopID;
					wMsg.PartID = wTaskPart.PartID;
					wMsg.ProductNo = wTaskPart.ProductNo;
					wMsg.Type = APSMsgTypes.Station.getValue();
					wMsg.MessageText = StringUtils.Format("工位：{0}  加工最大负荷{1} 现负荷{2}", wTaskPart.PartName, wFQTY,
							wReList.size());
					wResult.add(wMsg);
				}

			}

		}
		return wResult;
	}

	/**
	 * 计算空闲时间区间
	 * 
	 * @param wBuilderTaskList
	 * @param wFQTY            最大同时占用数
	 * @return 空闲时间区间
	 */
	public Map<Calendar, Calendar> CalcFreeTimeInterval(List<APSTaskPart> wBuilderTaskList, double wFQTY,
			Calendar wStartime) {
		Map<Calendar, Calendar> wResult = new HashMap<Calendar, Calendar>();
		try {
			if (wFQTY <= 0)
				wFQTY = 1;
			long wStartMillis = System.currentTimeMillis();
			// 最大空闲时刻
			Calendar wMaxFreeTime = Calendar.getInstance();
			wMaxFreeTime.add(Calendar.YEAR, 100);

			if (wBuilderTaskList == null || wBuilderTaskList.size() <= 0) {
				wResult.put(wStartime, wMaxFreeTime);
				return wResult;
			}

			List<Calendar> wAllTimeList = new ArrayList<Calendar>();
			wAllTimeList.add(wStartime);
			for (APSTaskPart wItem : wBuilderTaskList) {
				if (wItem.StartTime.compareTo(wStartime) > 0) {
					wAllTimeList.add(wItem.StartTime);
				}
				if (wItem.EndTime.compareTo(wStartime) > 0) {
					wAllTimeList.add(wItem.EndTime);
				}
			}
			wAllTimeList.sort((a, b) -> a.compareTo(b));

			// 得到所有的时间段
			Map<Calendar, Calendar> wAllDuration = new HashMap<Calendar, Calendar>();
			for (int i = 0; i < wAllTimeList.size(); i++) {
				if (i < wAllTimeList.size() - 1) {
					wAllDuration.put(wAllTimeList.get(i), wAllTimeList.get(i + 1));
				} else {
					wAllDuration.put(wAllTimeList.get(i), wMaxFreeTime);
				}
			}

			// 遍历获取空闲时间段
			for (Calendar wItem : wAllDuration.keySet()) {
				if (wBuilderTaskList.stream().filter(
						p -> p.StartTime.compareTo(wItem) <= 0 && p.EndTime.compareTo(wAllDuration.get(wItem)) >= 0)
						.collect(Collectors.toList()).size() < wFQTY) {
					wResult.put(wItem, wAllDuration.get(wItem));
				}
			}
			long wEndMillis = System.currentTimeMillis();
			int wCallMS = (int) (wEndMillis - wStartMillis);
			LoggerTool.MonitorFunction("APSWorkTimeCalcUtils", "CalcFreeTimeInterval", wCallMS);
			// 排序
//			wResult = new HashMap<Calendar, Calendar>(wResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

//	/**
//	 * 获取作息列表
//	 */
//	private List<FMCTimeZone> GetTimeZoneListAndHolidayList(BMSEmployee wLoginUser, int wWorkDayID) {
//		List<FMCTimeZone> wResult = new ArrayList<FMCTimeZone>();
//		try {
//			// 获取作息
//			List<FMCWorkDay> wWorkDayList = CoreServiceImpl.getInstance().FMC_QueryWorkDayLists(wLoginUser);
//			if (wWorkDayList == null || wWorkDayList.size() <= 0)
//				return wResult;
//			// 班次列表
//			Optional<FMCWorkDay> wDayOption = wWorkDayList.stream().filter(p -> p.ID == wWorkDayID).findFirst();
//			if (!wDayOption.isPresent())
//				return wResult;
//			FMCWorkDay wWorkDay = wDayOption.get();
//			List<FMCShift> wShiftList = CoreServiceImpl.getInstance().FMC_QueryShiftList(wLoginUser, wWorkDay.ID);
//			if (wShiftList == null || wShiftList.size() <= 0)
//				return wResult;
//			// 时间作息列表
//			for (FMCShift wItem : wShiftList) {
//				List<FMCTimeZone> wTempList = CoreServiceImpl.getInstance().FMC_QueryTimeZoneList(wLoginUser, wItem.ID);
//				if (wTempList == null || wTempList.size() <= 0)
//					continue;
//				wResult.addAll(wTempList);
//			}
//			// 处理时区问题
//			wResult.forEach(p -> p.StartTime = UtcToLocal(p.StartTime));
//		} catch (Exception ex) {
//			logger.error(ex.toString());
//		}
//		return wResult;
//	}

//	/**
//	 * 获取休息日列表
//	 */
//	private List<CFGCalendar> GetHolidayList(BMSEmployee wLoginUser, int wWorkDayID, int wPartID) {
//		List<CFGCalendar> wResult = new ArrayList<CFGCalendar>();
//		try {
//			// 获取工位所在工区
//			List<LFSWorkAreaStation> wAreaList = CoreServiceImpl.getInstance().LFS_QueryWorkAreaStationList(wLoginUser);
//			if (wAreaList == null || wAreaList.size() <= 0)
//				return wResult;
//			Optional<LFSWorkAreaStation> wOption = wAreaList.stream().filter(p -> p.StationID == wPartID).findFirst();
//			if (!wOption.isPresent())
//				return wResult;
//
//			int wYear = Calendar.getInstance().get(Calendar.YEAR);
//			wResult = CoreServiceImpl.getInstance().CFG_QueryCalendarList(wLoginUser, wYear, wOption.get().WorkAreaID);
//
//			// 处理时区问题
//			wResult.forEach(p -> p.HolidayDate = UtcToLocal(p.HolidayDate));
//		} catch (Exception ex) {
//			logger.error(ex.toString());
//		}
//		return wResult;
//	}

	/**
	 * 判断今日是否是休息日
	 * 
	 * @param wToday
	 * @param wHolidayDateList
	 * @return
	 */
	private boolean IsTodayRest(Calendar wToday, List<CFGCalendar> wHolidayDateList) {
		boolean wResult = false;
		try {
			if (wHolidayDateList == null || wHolidayDateList.size() == 0)
				return wResult;
			wResult = wHolidayDateList.stream()
					.anyMatch(p -> p.HolidayDate.get(Calendar.YEAR) == wToday.get(Calendar.YEAR)
							&& p.HolidayDate.get(Calendar.MONTH) == wToday.get(Calendar.MONTH)
							&& p.HolidayDate.get(Calendar.DATE) == wToday.get(Calendar.DATE));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

//	/**
//	 * UTC时间转本地时间
//	 * 
//	 */
//	private Calendar UtcToLocal(Calendar wUTCCalendar) {
//		Calendar wResult = Calendar.getInstance();
//		try {
//			Date wDate = UtcToLocal(wUTCCalendar.getTime());
//			wResult.setTime(wDate);
//		} catch (Exception ex) {
//			logger.error(ex.toString());
//		}
//		return wResult;
//	}

//	/**
//	 * UTC时间转本地时间
//	 * 
//	 */
//	private Date UtcToLocal(Date wUtcDate) {
//		Date wLocalDate = new Date();
//		try {
//			long wUtcTimeInMillis = wUtcDate.getTime();
//			Calendar wCalendar = Calendar.getInstance();
//			wCalendar.setTimeInMillis(wUtcTimeInMillis);
//			/** 取得时间偏移量 */
//			int wZoneOffset = wCalendar.get(java.util.Calendar.ZONE_OFFSET);
//			/** 取得夏令时差 */
//			int wDstOffset = wCalendar.get(java.util.Calendar.DST_OFFSET);
//			/** 从本地时间里扣除这些差量，即可以取得UTC时间 */
//			wCalendar.add(java.util.Calendar.MILLISECOND, +(wZoneOffset + wDstOffset));
//			/** 取得的时间就是UTC标准时间 */
//			wLocalDate = new Date(wCalendar.getTimeInMillis());
//		} catch (Exception ex) {
//			logger.error(ex.toString());
//		}
//		return wLocalDate;
//	}
}
