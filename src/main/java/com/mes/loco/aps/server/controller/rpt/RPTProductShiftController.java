package com.mes.loco.aps.server.controller.rpt;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mes.loco.aps.server.controller.BaseController;
import com.mes.loco.aps.server.service.RPTService;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.rpt.RPTDataDetails;
import com.mes.loco.aps.server.service.po.rpt.RPTMonthData;
import com.mes.loco.aps.server.service.po.rpt.RPTProductShift;
import com.mes.loco.aps.server.service.po.rpt.RPTQuarterData;
import com.mes.loco.aps.server.service.po.rpt.RPTYearNumberTrend;
import com.mes.loco.aps.server.service.po.rpt.RPTYearProductShift;
import com.mes.loco.aps.server.service.po.rpt.RPTYearStopTrend;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 报表控制器
 * 
 * @author PengYouWang
 * @CreateTime 2020-3-10 12:42:54
 * @LastEditTime 2020-3-10 12:42:59
 */
@RestController
@RequestMapping("/api/RPTProductShift")
public class RPTProductShiftController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(RPTProductShiftController.class);

	@Autowired
	RPTService wRPTService;

	/**
	 * 条件查询修程统计数据
	 */
	@GetMapping("/All")
	public Object All(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wShiftDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));
			int wShiftPeriod = StringUtils.parseInt(request.getParameter("ShiftPeriod"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1);
			if (wShiftPeriod > 0 && wShiftDate != null && wShiftDate.compareTo(wBaseTime) > 0) {
				APSShiftPeriod wAPSShiftPeriod = APSShiftPeriod.getEnumType(wShiftPeriod);
				switch (wAPSShiftPeriod) {
				case Month:
					wStartTime = Calendar.getInstance();
					wStartTime.set(Calendar.YEAR, wShiftDate.get(Calendar.YEAR));
					wStartTime.set(Calendar.MONTH, wShiftDate.get(Calendar.MONTH));
					wStartTime.set(Calendar.DATE, 1);

					wEndTime = this.getLastDayOfMonth(wShiftDate);
					wEndTime.add(Calendar.DATE, 1);
					break;
				case Week:
					wStartTime = this.getFirstDayOfWeek(wShiftDate);
					wEndTime = this.getLastOfWeek(wShiftDate);
					wEndTime.add(Calendar.DATE, 1);
					break;
				case Day:
					wStartTime = wShiftDate;
					wStartTime.set(Calendar.HOUR_OF_DAY, 0);
					wStartTime.set(Calendar.MINUTE, 0);
					wStartTime.set(Calendar.SECOND, 0);

					wEndTime = wShiftDate;
					wEndTime.set(Calendar.HOUR_OF_DAY, 23);
					wEndTime.set(Calendar.MINUTE, 59);
					wEndTime.set(Calendar.SECOND, 59);
				default:
					break;
				}
			}

			ServiceResult<List<RPTProductShift>> wServiceResult = wRPTService.RPT_QueryProductShiftList(wLoginUser,
					wStartTime, wEndTime);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取日期所在月最后一天
	 * 
	 * @param wDate
	 * @return
	 */
	private Calendar getLastDayOfMonth(Calendar wDate) {
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

	private Calendar getFirstDayOfWeek(Calendar wDate) {
		Calendar wResult = Calendar.getInstance();
		try {
			// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
			int wDayWeek = wDate.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			if (1 == wDayWeek) {
				wDate.add(Calendar.DAY_OF_MONTH, -1);
			}
			wDate.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
			int wDay = wDate.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			wDate.add(Calendar.DATE, wDate.getFirstDayOfWeek() - wDay);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
			wResult = wDate;
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
	private Calendar getLastOfWeek(Calendar wDate) {
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

	@GetMapping("/Info")
	public Object Info(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wLineID = StringUtils.parseInt(request.getParameter("LineID"));
			Calendar wShiftDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));
			int wShiftPeriod = StringUtils.parseInt(request.getParameter("ShiftPeriod"));
			APSShiftPeriod wAPSShiftPeriod = APSShiftPeriod.getEnumType(wShiftPeriod);

			ServiceResult<List<RPTProductShift>> wServiceResult = wRPTService.RPT_QueryProductShiftInfo(wLoginUser,
					wLineID, wShiftDate, wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 根据时间段获取年修程统计数据
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/YearData")
	public Object YearData(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<RPTYearProductShift>> wServiceResult = wRPTService
					.RPT_QueryYearProductShiftList(wLoginUser, wStartTime, wEndTime);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取季度完成数
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/QuarterData")
	public Object QuarterData(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wYear = StringUtils.parseInt(request.getParameter("Year"));

			ServiceResult<List<RPTQuarterData>> wServiceResult = wRPTService.RPT_QueryQuarterData(wLoginUser, wYear);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				// 月份数据
				ServiceResult<List<RPTMonthData>> wMonthResult = wRPTService.RPT_QueryMonthData(wLoginUser, wYear);
				this.SetResult(wResult, "MonthList", wMonthResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取统计数据详情
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/DataDetails")
	public Object DataDetails(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 年份
			int wYear = StringUtils.parseInt(request.getParameter("Year"));
			// 查询ID(季度或月份)
			int wQID = StringUtils.parseInt(request.getParameter("QID"));
			// 查询类别 1季度 2月份
			int wQType = StringUtils.parseInt(request.getParameter("QType"));
			// 是否统计0
			boolean wShowZero = StringUtils.parseBoolean(request.getParameter("ShowZero"));

			ServiceResult<List<RPTDataDetails>> wServiceResult = wRPTService.RPT_QueryDataDetails(wLoginUser, wYear,
					wQID, wQType, wShowZero);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "Details", wServiceResult.CustomResult.get("Details"));
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 年度机型统计数据
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/YearProduct")
	public Object YearProduct(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 年份
			int wYear = StringUtils.parseInt(request.getParameter("Year"));

			ServiceResult<List<RPTDataDetails>> wServiceResult = wRPTService.RPT_QueryYearProduct(wLoginUser, wYear);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 机车进出厂数量趋势图
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/YearNumberTrend")
	public Object YearNumberTrend(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 年份
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<RPTYearNumberTrend> wServiceResult = wRPTService.RPT_QueryYearNumberTrend(wLoginUser,
					wStartTime, wEndTime);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 机车平均停时趋势图
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/YearStopTrend")
	public Object YearStopTrend(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 年份
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<RPTYearStopTrend> wServiceResult = wRPTService.RPT_QueryYearStopTrend(wLoginUser, wStartTime,
					wEndTime);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
