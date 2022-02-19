package com.mes.loco.aps.server.controller.rpt;

import java.util.Calendar;
import java.util.List;

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
import com.mes.loco.aps.server.service.po.rpt.RPTCustomerShift;
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
@RequestMapping("/api/RPTCustomerShift")
public class RPTCustomerShiftController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(RPTCustomerShiftController.class);

	@Autowired
	RPTService wRPTService;

	/**
	 * 条件查询局段统计数据
	 * 
	 * @param request
	 * @return
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

			ServiceResult<List<RPTCustomerShift>> wServiceResult = wRPTService.RPT_QueryCustomerShiftList(wLoginUser,
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

	/**
	 * 条件查询单条局段统计数据
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/Info")
	public Object Info(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wCustomerID = StringUtils.parseInt(request.getParameter("CustomerID"));
			Calendar wShiftDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));
			int wShiftPeriod = StringUtils.parseInt(request.getParameter("ShiftPeriod"));
			APSShiftPeriod wAPSShiftPeriod = APSShiftPeriod.getEnumType(wShiftPeriod);

			ServiceResult<RPTCustomerShift> wServiceResult = wRPTService.RPT_QueryCustomerShiftInfo(wLoginUser,
					wCustomerID, wShiftDate, wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
