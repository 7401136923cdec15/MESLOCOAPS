package com.mes.loco.aps.server.serviceimpl.utils.aps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.APSMsgTypes;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.aps.APSMessage;
import com.mes.loco.aps.server.service.po.aps.APSShiftDuration;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSWorkHour;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.cfg.CFGCalendar;
import com.mes.loco.aps.server.service.po.fmc.FMCTimeZone;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.APSServiceImpl;
import com.mes.loco.aps.server.shristool.LoggerTool;

public class APSWorkTimeCalcUtils {

	private static Logger logger = LoggerFactory.getLogger(APSWorkTimeCalcUtils.class);

	public APSWorkTimeCalcUtils() {
	}

	private static APSWorkTimeCalcUtils Instance;

	public static APSWorkTimeCalcUtils getInstance() {
		if (Instance == null)
			Instance = new APSWorkTimeCalcUtils();
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
	public Calendar CalcTaskEndTime(BMSEmployee wLoginUser, int wPartID, Calendar wStartTime, Double wWorkMinus,
			Double wTechMinus, int wWorkDay, List<FMCTimeZone> wAllZoneList, List<CFGCalendar> wCalendarList,
			int wLimitMinus) {
		Calendar wResult = (Calendar) wStartTime.clone();
//		Double _wWorkMinus = wWorkMinus;
		try {

//			if (wPartID == 110) {
//				System.out.println("S--==--" + StringUtils.parseCalendarToString(wStartTime, "yyyy-MM-dd HH:mm"));
//				if (_wWorkMinus == 0) {
//					System.out.println("t");
//				}
//			}

			long wStartMillis = System.currentTimeMillis();
			List<CFGCalendar> wHolidayList = wCalendarList;

			APSWorkHour wWorkHour = APSServiceImpl.getInstance().APS_QueryWorkHour(wLoginUser).Result;
			if (wWorkHour == null) {
				wWorkHour = new APSWorkHour();
			}

			int wMaxWorkHour = wWorkHour.MaxWorkHour;
			int wMinWorkHour = wWorkHour.MinWorkHour;
			// 已12点为基准点
			int wMiddleWorkHour = wWorkHour.MiddleWorkHour;

			int wTempHour = 0;
			Calendar wTempTime = Calendar.getInstance();
			for (FMCTimeZone wFMCTimeZone : wAllZoneList) {

				wTempTime = (Calendar) wFMCTimeZone.StartTime.clone();

				if (!wFMCTimeZone.isIdleOrWork())
					continue;

				wTempHour = wFMCTimeZone.getStartTime().get(Calendar.HOUR_OF_DAY);
				if (wTempHour < wMinWorkHour)
					wMinWorkHour = wTempHour;

				wTempTime.add(Calendar.MINUTE, wFMCTimeZone.getMinutes());
				wTempHour = wTempTime.get(Calendar.HOUR_OF_DAY);
				if (wTempHour > wMaxWorkHour)
					wMaxWorkHour = wTempHour;
			}

			wResult = this.CalcEndTime(wStartTime, wWorkMinus, wMiddleWorkHour, wMinWorkHour, wMaxWorkHour,
					wHolidayList);

			// 加工艺时长
			wResult = this.CalcEndTime(wResult, wTechMinus, wMiddleWorkHour, wMinWorkHour, wMaxWorkHour, null);

			long wEndMillis = System.currentTimeMillis();
			int wCallMS = (int) (wEndMillis - wStartMillis);

//			if (wPartID == 110) {
//				System.out.println(StringUtils.parseCalendarToString(wStartTime, "yyyy-MM-dd HH:mm") + "--==--"
//						+ StringUtils.parseCalendarToString(wResult, "yyyy-MM-dd HH:mm") + "--==--" + _wWorkMinus);
//			}
			LoggerTool.MonitorFunction("APSWorkTimeCalcUtils", "CalcTaskEndTime", wCallMS);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private Calendar CalcEndTime(Calendar wStartTime, Double wWorkMinus, int wMiddleWorkHour, int wMinWorkHour,
			int wMaxWorkHour, List<CFGCalendar> wHolidayList) {
		Calendar wResult = (Calendar) wStartTime.clone();
		try {
			if (wWorkMinus <= 0)
				return wResult;

			if (wStartTime.get(Calendar.HOUR_OF_DAY) > wMiddleWorkHour) {
				wStartTime.add(Calendar.DATE, 1);
				wStartTime.set(Calendar.HOUR_OF_DAY, wMinWorkHour);
				wStartTime.set(Calendar.MINUTE, 0);

			} else if (wStartTime.get(Calendar.HOUR_OF_DAY) <= wMinWorkHour) {

				wStartTime.set(Calendar.HOUR_OF_DAY, wMinWorkHour);
				wStartTime.set(Calendar.MINUTE, 0);

			} else {
				wStartTime.set(Calendar.HOUR_OF_DAY, wMiddleWorkHour);
				wStartTime.set(Calendar.MINUTE, 0);
			}
			wResult = (Calendar) wStartTime.clone();

			boolean wIsEdit = false;
			while (wWorkMinus > 0) {

				if (wHolidayList != null && wHolidayList.size() > 0) {
					if (IsTodayRest(wResult, wHolidayList)) {
						if (!wIsEdit) {
							wStartTime.add(Calendar.DATE, 1);
							wStartTime.set(Calendar.HOUR_OF_DAY, wMinWorkHour);
							wStartTime.set(Calendar.MINUTE, 0);
						}
						wResult.add(Calendar.DATE, 1);
						wResult.set(Calendar.HOUR_OF_DAY, wMinWorkHour);
						wResult.set(Calendar.MINUTE, 0);
						continue;
					}
				}
				wIsEdit = true;

				if (wWorkMinus < 1) {
					if (wStartTime.get(Calendar.HOUR_OF_DAY) < wMiddleWorkHour) {
//						wResult.add(Calendar.DATE, 1);
						wResult.set(Calendar.HOUR_OF_DAY, wMiddleWorkHour);
						wResult.set(Calendar.MINUTE, 0);
						// 上午
					} else {
						// 下午
//						wResult.add(Calendar.DATE, 1);
						wResult.set(Calendar.HOUR_OF_DAY, wMaxWorkHour);
						wResult.set(Calendar.MINUTE, 0);
					}

				} else if (wWorkMinus == 1) {
					if (wStartTime.get(Calendar.HOUR_OF_DAY) < wMiddleWorkHour) {
						wResult.add(Calendar.DATE, 1);
//						wResult.set(Calendar.HOUR_OF_DAY, wMaxWorkHour);
						wResult.set(Calendar.MINUTE, 0);
					} else {
						// 下午
						wResult.add(Calendar.DATE, 1);
						// 12 -12 无需操作
					}

				} else {
					wResult.add(Calendar.DATE, 1);
				}
				wWorkMinus -= 1;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 计算时间区间最大工作时长 单位天
	 * 
	 * @param wLoginUser
	 * @param wStartTime
	 * @param wEndTime
	 * @param wWorkDayID 作息ID
	 * @return
	 */
	public Double CalcIntervalTime(BMSEmployee wLoginUser, int wPartID, int wWorkDayID, Calendar wStartTime,
			Calendar wEndTime, List<FMCTimeZone> wAllZoneList, Map<Integer, List<CFGCalendar>> wCalendarMap) {
		Double wResult = 0.0;
		try {
			long wStartMillis = System.currentTimeMillis();
			if (wStartTime.compareTo(wEndTime) >= 0)
				return wResult;

			if (wCalendarMap == null || !wCalendarMap.containsKey(wPartID))
				return wResult;

			List<CFGCalendar> wHolidayList = wCalendarMap.get(wPartID);
			// 开始计算
			Double wTotalMinus = 0.0;

			int wMaxWorkHour = 17;

			int wMinWorkHour = 8;

			// 已12点为基准点
			int wMiddleWorkHour = 12;

			for (FMCTimeZone wFMCTimeZone : wAllZoneList) {
				if (!wFMCTimeZone.isIdleOrWork())
					continue;

				if (wFMCTimeZone.getStartTime().get(Calendar.HOUR_OF_DAY) < wMinWorkHour)
					wMinWorkHour = wFMCTimeZone.getStartTime().get(Calendar.HOUR_OF_DAY);

				wFMCTimeZone.StartTime.add(Calendar.MINUTE, wFMCTimeZone.getMinutes());
				if (wFMCTimeZone.getStartTime().get(Calendar.HOUR_OF_DAY) > wMaxWorkHour)
					wMinWorkHour = wFMCTimeZone.getStartTime().get(Calendar.HOUR_OF_DAY);

			}

			// 处理开始时间
			wStartTime = (Calendar) wStartTime.clone();
			if (wStartTime.get(Calendar.HOUR_OF_DAY) > wMiddleWorkHour) {
				wStartTime.add(Calendar.DATE, 1);
				wStartTime.set(Calendar.HOUR_OF_DAY, wMinWorkHour);
				wStartTime.set(Calendar.MINUTE, 0);

			} else if (wStartTime.get(Calendar.HOUR_OF_DAY) <= wMinWorkHour) {

				wStartTime.set(Calendar.HOUR_OF_DAY, wMinWorkHour);
				wStartTime.set(Calendar.MINUTE, 0);

			} else {
				wStartTime.set(Calendar.HOUR_OF_DAY, wMiddleWorkHour);
				wStartTime.set(Calendar.MINUTE, 0);
			}

			// 处理结束时间
			wEndTime = (Calendar) wEndTime.clone();
			if (wEndTime.get(Calendar.HOUR_OF_DAY) > wMiddleWorkHour) {

				wEndTime.set(Calendar.HOUR_OF_DAY, wMaxWorkHour);
				wEndTime.set(Calendar.MINUTE, 0);

			} else if (wEndTime.get(Calendar.HOUR_OF_DAY) <= wMinWorkHour) {
				wEndTime.set(Calendar.DAY_OF_MONTH, -1);
				wEndTime.set(Calendar.HOUR_OF_DAY, wMaxWorkHour);
				wEndTime.set(Calendar.MINUTE, 0);

			} else if (wEndTime.get(Calendar.HOUR_OF_DAY) < wMiddleWorkHour) {
				wEndTime.set(Calendar.HOUR_OF_DAY, wMiddleWorkHour);
				wEndTime.set(Calendar.MINUTE, 0);
			}

			// 开始计算
			while (wStartTime.compareTo(wEndTime) < 0) {
				if (IsTodayRest(wStartTime, wHolidayList)) {
					wStartTime.add(Calendar.DATE, 1);
					wStartTime.set(Calendar.HOUR_OF_DAY, wMinWorkHour);
					wStartTime.set(Calendar.MINUTE, 0);
					continue;
				}
				if (wStartTime.get(Calendar.DAY_OF_MONTH) == wEndTime.get(Calendar.DAY_OF_MONTH)) {

					if (wEndTime.get(Calendar.HOUR_OF_DAY) >= wMaxWorkHour
							&& wStartTime.get(Calendar.HOUR_OF_DAY) <= wMinWorkHour) {
						wTotalMinus += 1;
					} else {
						wTotalMinus += 0.5;
					}
					break;
				}
				wStartTime.add(Calendar.DATE, 1);
				wTotalMinus += 1;
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

			List<APSTaskPart> wTaskPartList = wBuilderTaskList.stream()
					.filter(p -> p.PartID == wManuCapacity.PartID && p.LineID == wManuCapacity.LineID)
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
					wAllTimeList.add((Calendar) wItem.StartTime.clone());
				}
				if (wItem.EndTime.compareTo(wStartime) > 0) {
					wAllTimeList.add((Calendar) wItem.EndTime.clone());
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
			wResult = wResult.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.naturalOrder())).collect(
					Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

			// 处理连续的空闲时间段
			wResult = HandleResult(wResult);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 处理空闲的时间段
	 */
	private Map<Calendar, Calendar> HandleResult(Map<Calendar, Calendar> wResult) {
		try {
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMddHHmmss");

			List<APSShiftDuration> wDurationList = new ArrayList<APSShiftDuration>();
			int wIndex = 1;
			for (Calendar wSTime : wResult.keySet()) {
				Calendar wETime = wResult.get(wSTime);
				APSShiftDuration wAPSShiftDuration = new APSShiftDuration();
				wAPSShiftDuration.ShiftS = Long.parseLong(wSDF.format(wSTime.getTime()));
				wAPSShiftDuration.ShiftE = Long.parseLong(wSDF.format(wETime.getTime()));
				wAPSShiftDuration.DateS = wSTime;
				wAPSShiftDuration.DateE = wETime;
				wAPSShiftDuration.ID = wIndex++;
				wDurationList.add(wAPSShiftDuration);
			}

			// ①找到所有连续的时间段
			StringBuffer wSB = new StringBuffer();
			for (int i = 0; i < wDurationList.size(); i++) {
				if (i < wDurationList.size() - 1) {
					if (wDurationList.get(i).DateE == wDurationList.get(i + 1).DateS) {
						wSB.append(StringUtils.Format("{0},", String.valueOf(wDurationList.get(i).ID)));
					} else {
						wSB.append(StringUtils.Format("{0};", String.valueOf(wDurationList.get(i).ID)));
					}
				} else {
					wSB.append(StringUtils.Format("{0}", String.valueOf(wDurationList.get(i).ID)));
				}
			}

			List<List<APSShiftDuration>> wListList = new ArrayList<List<APSShiftDuration>>();
			String[] wStrs = wSB.toString().split(";");
			for (String wStr : wStrs) {
				List<APSShiftDuration> wList = new ArrayList<APSShiftDuration>();
				String[] wItems = wStr.split(",");
				for (String wItem : wItems) {
					int wID = Integer.parseInt(wItem);
					wList.add(wDurationList.stream().filter(p -> p.ID == wID).findFirst().get());
				}
				wListList.add(wList);
			}
			// ②遍历连续的时间段，取其中的最小的StartTime和最大的EndTime作为一个新的时间段添加到集合中去
			wResult = new HashMap<Calendar, Calendar>();
			for (List<APSShiftDuration> wItemList : wListList) {
				Calendar wMinDate = wItemList.stream().min(Comparator.comparing(APSShiftDuration::getDateS))
						.get().DateS;
				Calendar wMaxDate = wItemList.stream().max(Comparator.comparing(APSShiftDuration::getDateE))
						.get().DateE;
				wResult.put(wMinDate, wMaxDate);
			}

			// 排序
			wResult = wResult.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.naturalOrder())).collect(
					Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

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
}
