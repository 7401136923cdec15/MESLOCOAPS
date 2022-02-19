package com.mes.loco.aps.server.serviceimpl.utils.aps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.BMSDepartmentType;
import com.mes.loco.aps.server.service.mesenum.FPCPartTypes;
import com.mes.loco.aps.server.service.po.bms.BMSDepartment;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bms.BMSPosition;
import com.mes.loco.aps.server.service.po.cfg.CFGCalendar;
import com.mes.loco.aps.server.service.po.fmc.FMCLine;
import com.mes.loco.aps.server.service.po.fmc.FMCLineUnit;
import com.mes.loco.aps.server.service.po.fmc.FMCShift;
import com.mes.loco.aps.server.service.po.fmc.FMCTimeZone;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkDay;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.utils.Configuration;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.FMCServiceImpl;

/**
 * 排程服务工具类
 * 
 * @author PengYouWang
 * @CreateTime 2020-1-9 15:19:23
 * @LastEditTime 2020-1-9 15:19:26
 *
 */
public class APSUtils {
	private static Logger logger = LoggerFactory.getLogger(APSUtils.class);

	public APSUtils() {
	}

	private static APSUtils Instance;

	public static APSUtils getInstance() {
		if (Instance == null)
			Instance = new APSUtils();
		return Instance;
	}

	/**
	 * 修程、休息日map
	 * 
	 * @param wLoginUser
	 * @return
	 */
	public Map<Integer, List<CFGCalendar>> GetCalendarMap(BMSEmployee wLoginUser) {
		Map<Integer, List<CFGCalendar>> wResult = new HashMap<Integer, List<CFGCalendar>>();
		try {
			// 修程列表
			List<FMCLine> wLineList = CoreServiceImpl.getInstance().FMC_QueryLineList(wLoginUser).List(FMCLine.class);
			if (wLineList == null || wLineList.size() <= 0) {
				return wResult;
			}

			int wYear = Calendar.getInstance().get(Calendar.YEAR);

			for (FMCLine wFMCLine : wLineList) {
				List<CFGCalendar> wList = CoreServiceImpl.getInstance()
						.CFG_QueryCalendarList(wLoginUser, wYear, wFMCLine.WorkShopID).List(CFGCalendar.class);
				if (wList == null || wList.size() <= 0)
					continue;
				wList = wList.stream().filter(p -> p.Active == 1).collect(Collectors.toList());
				// 处理时区问题
				wList.forEach(p -> p.HolidayDate = UtcToLocal(p.HolidayDate));
				if (!wResult.containsKey(wFMCLine.ID))
					wResult.put(wFMCLine.ID, wList);
			}

			// 获取所有工位工区关系
//			List<LFSWorkAreaStation> wAreaList = LFSServiceImpl.getInstance().LFS_QueryWorkAreaStationList(wLoginUser)
//					.List(LFSWorkAreaStation.class);
//			for (LFSWorkAreaStation wItem : wAreaList) {
//				if (!wResult.containsKey(wItem.StationID) && wAreaMap.containsKey(wItem.WorkAreaID)) {
//					wResult.put(wItem.StationID, wAreaMap.get(wItem.WorkAreaID));
//				}
//			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取作息列表
	 * 
	 * @param wLoginUser
	 * @param wWorkDayID
	 * @return
	 */
	public List<FMCTimeZone> GetTimeZoneListAndHolidayList(BMSEmployee wLoginUser, int wWorkDayID) {
		List<FMCTimeZone> wResult = new ArrayList<FMCTimeZone>();
		try {
			// 获取作息
			List<FMCWorkDay> wWorkDayList = CoreServiceImpl.getInstance().FMC_QueryWorkDayLists(wLoginUser)
					.List(FMCWorkDay.class);
			if (wWorkDayList == null || wWorkDayList.size() <= 0)
				return wResult;
			// 班次列表
			Optional<FMCWorkDay> wDayOption = wWorkDayList.stream().filter(p -> p.ID == wWorkDayID).findFirst();
			if (!wDayOption.isPresent())
				return wResult;
			FMCWorkDay wWorkDay = wDayOption.get();
			List<FMCShift> wShiftList = CoreServiceImpl.getInstance().FMC_QueryShiftList(wLoginUser, wWorkDay.ID)
					.List(FMCShift.class);
			if (wShiftList == null || wShiftList.size() <= 0)
				return wResult;
			// 时间作息列表
			for (FMCShift wItem : wShiftList) {
				List<FMCTimeZone> wTempList = CoreServiceImpl.getInstance().FMC_QueryTimeZoneList(wLoginUser, wItem.ID)
						.List(FMCTimeZone.class);
				if (wTempList == null || wTempList.size() <= 0)
					continue;
				wResult.addAll(wTempList);
			}
			// 处理时区问题
			wResult.forEach(p -> p.StartTime = UtcToLocal(p.StartTime));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * UTC时间转本地时间
	 * 
	 */
	public Calendar UtcToLocal(Calendar wUTCCalendar) {
		Calendar wResult = Calendar.getInstance();
		try {
			Date wDate = UtcToLocal(wUTCCalendar.getTime());
			wResult.setTime(wDate);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * UTC时间转本地时间
	 * 
	 */
	public Date UtcToLocal(Date wUtcDate) {
		Date wLocalDate = new Date();
		try {
			long wUtcTimeInMillis = wUtcDate.getTime();
			Calendar wCalendar = Calendar.getInstance();
			wCalendar.setTimeInMillis(wUtcTimeInMillis);
			/** 取得时间偏移量 */
			int wZoneOffset = wCalendar.get(java.util.Calendar.ZONE_OFFSET);
			/** 取得夏令时差 */
			int wDstOffset = wCalendar.get(java.util.Calendar.DST_OFFSET);
			/** 从本地时间里扣除这些差量，即可以取得UTC时间 */
			wCalendar.add(java.util.Calendar.MILLISECOND, +(wZoneOffset + wDstOffset));
			/** 取得的时间就是UTC标准时间 */
			wLocalDate = new Date(wCalendar.getTimeInMillis());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wLocalDate;
	}

	/**
	 * 根据班组ID获取班组长
	 * 
	 * @param wLoginUser
	 * @param secondDepartmentID
	 * @return
	 */
	public BMSEmployee APS_GetMonitorByDepartmentID(BMSEmployee wLoginUser, int wClassID) {
		BMSEmployee wResult = new BMSEmployee();
		try {
			BMSDepartment wDepartment = APSConstans.GetBMSDepartment(wClassID);
			if (wDepartment.Type != BMSDepartmentType.Class.getValue()) {
				return wResult;
			}

			List<BMSEmployee> wList = APSConstans.GetBMSEmployeeList().values().stream()
					.filter(p -> p.DepartmentID == wClassID).collect(Collectors.toList());

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			BMSPosition wPosition;
			for (BMSEmployee wBMSEmployee : wList) {
				wPosition = APSConstans.GetBMSPosition(wBMSEmployee.Position);
				if (wPosition != null && wPosition.ID > 0 && wPosition.DutyID == 1) {
					return wBMSEmployee;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据工区ID获取工区主管
	 * 
	 * @param wLoginUser
	 * @param wAreaID
	 * @return
	 */
	public List<BMSEmployee> APS_GetDirectorByAreaID(BMSEmployee wLoginUser, int wAreaID) {
		List<BMSEmployee> wResult = new ArrayList<BMSEmployee>();
		try {
			Optional<LFSWorkAreaChecker> wOption = APSConstans.GetLFSWorkAreaCheckerList().stream()
					.filter(p -> p.WorkAreaID == wAreaID).findFirst();
			if (wOption.isPresent()) {
				LFSWorkAreaChecker wCheker = wOption.get();
				BMSEmployee wEmployee = null;
				for (Integer wLeaderID : wCheker.LeaderIDList) {
					wEmployee = APSConstans.GetBMSEmployee(wLeaderID);
					if (wEmployee != null && wEmployee.ID > 0) {
						wResult.add(wEmployee);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据订单获取工位列表
	 * 
	 * @param wLoginUser           登录信息
	 * @param wOMSOrder            订单
	 * @param wFPCProductRouteList 车型工艺路线
	 * @param wFPCRoutePartList    工艺工位
	 * @return
	 */
	public List<Integer> APS_GetPartIDListByOrder(BMSEmployee wLoginUser, OMSOrder wOMSOrder,
			List<FPCRoutePart> wFPCRoutePartList) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			// 输入验证
			if (wOMSOrder == null || wOMSOrder.ID <= 0 || wFPCRoutePartList == null || wFPCRoutePartList.size() <= 0) {
				return wResult;
			}
			// 获取唯一车型工艺路线
			if (APSConstans.GetFPCRoute(wOMSOrder.ProductID, wOMSOrder.LineID, wOMSOrder.CustomerID).ID <= 0) {
				return wResult;
			}
//			Optional<FPCRoute> wProductOption = wFPCProductRouteList.stream()
//					.filter(p -> p.Line.equals(wOMSOrder.LineName) && p.ProductID == wOMSOrder.ProductID).findFirst();
//			if (!wProductOption.isPresent()) {
//				return wResult;
//			}
//			FPCProductRoute wFPCProductRoute = wProductOption.get();
			// 根据RouteID获取工艺工位列表
			List<FPCRoutePart> wRoutePartList = wFPCRoutePartList.stream().filter(p -> p.RouteID == APSConstans
					.GetFPCRoute(wOMSOrder.ProductID, wOMSOrder.LineID, wOMSOrder.CustomerID).ID)
					.collect(Collectors.toList());
			if (wRoutePartList == null || wRoutePartList.size() <= 0) {
				return wResult;
			}
			// 根据工艺工位列表获取工位ID集合
			wResult = wRoutePartList.stream().map(p -> p.PartID).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据修程、工位查询工序列表
	 * 
	 * @return
	 */
	public List<Integer> FMC_QueryStepIDList(BMSEmployee wLoginUser, int wLineID, int wPartID, int wProductID) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			List<FMCLineUnit> wLineUnitList = FMCServiceImpl.getInstance()
					.FMC_QueryLineUnitListByLineID(wLoginUser, wLineID, -1, wProductID, false).List(FMCLineUnit.class);
			if (wLineUnitList == null || wLineUnitList.size() <= 0) {
				return wResult;
			}

			// 找到此工位的所有工序
			wLineUnitList = wLineUnitList.stream().filter(p -> p.LevelID == 3 && p.ParentUnitID == wPartID)
					.collect(Collectors.toList());
			if (wLineUnitList == null || wLineUnitList.size() <= 0) {
				return wResult;
			}

			wResult = wLineUnitList.stream().map(p -> p.UnitID).collect(Collectors.toList());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<Integer> GetYJStationIDList(BMSEmployee wLoginUser) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			List<FPCPart> wPartList = FMCServiceImpl.getInstance()
					.FPC_QueryPartList(wLoginUser, -1, -1, -1, FPCPartTypes.PrevCheck.getValue()).List(FPCPart.class);

			if (wPartList == null || wPartList.size() <= 0) {
				return wResult;
			} else {
				wResult = wPartList.stream().map(p -> p.ID).collect(Collectors.toList());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取日期所在月最后一天
	 * 
	 * @param wDate
	 * @return
	 */
	public Calendar getLastDayOfMonth(Calendar wDate) {
		Calendar wResult = Calendar.getInstance();
		try {
			wResult = wDate;
			int wLast = wResult.getActualMaximum(Calendar.DAY_OF_MONTH);
			wResult.set(Calendar.DAY_OF_MONTH, wLast);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取指定日期所在周最后一天
	 * 
	 * @param dataStr
	 * @param dateFormat
	 * @param resultDateFormat
	 * @return
	 */
	public Calendar getLastOfWeek(Calendar wDate) {
		Calendar wResult = Calendar.getInstance();
		try {
			int wD = 0;
			if (wDate.get(Calendar.DAY_OF_WEEK) == 1) {
				wD = -6;
			} else {
				wD = 2 - wDate.get(Calendar.DAY_OF_WEEK);
			}
			wDate.add(Calendar.DAY_OF_WEEK, wD);
			wDate.add(Calendar.DAY_OF_WEEK, 6);
			wResult = wDate;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据年 月 获取对应的月份 天数
	 */
	public int getDaysByYearMonth(int wYear, int wMonth) {
		Calendar wDate = Calendar.getInstance();
		wDate.set(Calendar.YEAR, wYear);
		wDate.set(Calendar.MONTH, wMonth - 1);
		wDate.set(Calendar.DATE, 1);
		wDate.roll(Calendar.DATE, -1);
		int wMaxDate = wDate.get(Calendar.DATE);
		return wMaxDate;
	}

	/**
	 * 计算开始时间和结束时间之间的间隔小时
	 * 
	 * @param wStartTime 开始时间
	 * @param wEndTime   结束时间
	 * @return 间隔小时
	 */
	public double CalIntervalHour(Calendar wStartTime, Calendar wEndTime) {
		double wResult = 0.0;
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) >= 0) {
				return wResult;
			}

			long wStart = wStartTime.getTime().getTime();
			long wEnd = wEndTime.getTime().getTime();
			long wMinutes = (wEnd - wStart) / (1000 * 60);
			double wHour = (double) wMinutes / 60;
			wResult = formatDouble(wHour);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 计算开始时间和结束时间之间的间隔天数
	 * 
	 * @param wStartTime 开始时间
	 * @param wEndTime   结束时间
	 * @return 间隔天数
	 */
	public int CalIntervalDays(Calendar wStartTime, Calendar wEndTime) {
		int wResult = 0;
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) >= 0) {
				return wResult;
			}

			long wStart = wStartTime.getTime().getTime();
			long wEnd = wEndTime.getTime().getTime();
			long wMinutes = (wEnd - wStart) / (1000 * 60);
			wResult = (int) (wMinutes / 60 / 24);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 四舍五入保留两位小数
	 * 
	 * @param wNumber
	 * @return
	 */
	public double formatDouble(double wNumber) {
		double wResult = 0.0;
		try {
			// 如果不需要四舍五入，可以使用RoundingMode.DOWN
			BigDecimal wBG = new BigDecimal(wNumber).setScale(2, RoundingMode.UP);
			wResult = wBG.doubleValue();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取所有后续工位集合
	 */
	public List<Integer> GetNextPartIDList(int wPartID, List<FPCRoutePart> wRoutePartList) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			if (wRoutePartList == null || wRoutePartList.size() <= 0) {
				return wResult;
			}

			if (!wRoutePartList.stream().anyMatch(p -> p.PartID == wPartID)) {
				return wResult;
			}
			// ①从集合中找到此PartID的RoutePart
			FPCRoutePart wRoutePart = wRoutePartList.stream().filter(p -> p.PartID == wPartID).findFirst().get();
			// ②根据nextPart找
			if (wRoutePart.NextPartIDMap != null && wRoutePart.NextPartIDMap.size() > 0) {
				for (String wKey : wRoutePart.NextPartIDMap.keySet()) {
					int wTempID = Integer.parseInt(wKey);
					if (wTempID <= 0) {
						continue;
					}
					if (wResult.stream().anyMatch(p -> p.intValue() == wTempID)) {
						continue;
					}
					wResult.add(wTempID);
					List<Integer> wList = GetNextPartIDList(wTempID, wRoutePartList);
					if (wList != null && wList.size() > 0) {
						wResult.addAll(wList);
					}
				}
			}
			// ③根据Prev找
			List<FPCRoutePart> wRouteList = wRoutePartList.stream().filter(p -> p.PrevPartID == wPartID)
					.collect(Collectors.toList());
			if (wRouteList != null && wRouteList.size() > 0) {
				for (FPCRoutePart wTempRoute : wRouteList) {
					if (wResult.stream().anyMatch(p -> p.intValue() == wTempRoute.PartID)) {
						continue;
					}

					wResult.add(wTempRoute.PartID);
					List<Integer> wList = GetNextPartIDList(wTempRoute.PartID, wRoutePartList);
					if (wList != null && wList.size() > 0) {
						wResult.addAll(wList);
					}
				}
			}

			// 去除不合理的数据
			if (wResult.size() > 0) {
				wResult.removeIf(p -> p <= 0);

				wResult = wResult.stream().distinct().collect(Collectors.toList());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取SAP终结点地址
	 */
	public String GetSAPEndpoint() {
		String wResult = "";
		try {
			String wSapIp = Configuration.readConfigString("sapip", "config/config");
			if (StringUtils.isEmpty(wSapIp))
				wResult = "http://10.200.10.12:8000/sap/bc/srt/rfc/sap/zif_mes_ws/120/zif_mes_ws/zif_mes_ws";
			else
				wResult = wSapIp;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取SAP用户名
	 */
	public String GetSAPUser() {
		String wResult = "";
		try {
			String wSapIp = Configuration.readConfigString("sapuser", "config/config");
			if (StringUtils.isEmpty(wSapIp))
				wResult = "sapdev";
			else
				wResult = wSapIp;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取SAP密码
	 */
	public String GetSAPPassword() {
		String wResult = "";
		try {
			String wSapIp = Configuration.readConfigString("sappassword", "config/config");
			if (StringUtils.isEmpty(wSapIp))
				wResult = "senchu2020";
			else
				wResult = wSapIp;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
