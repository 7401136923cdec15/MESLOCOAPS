package com.mes.loco.aps.server.controller.wms;

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
import com.mes.loco.aps.server.service.WMSService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemand;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 产线工位领料需求控制器
 * 
 * @author PengYouWang
 * @CreateTime 2022-1-6 11:31:11
 */
@RestController
@RequestMapping("/api/WMSPickDemand")
public class WMSPickDemandController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(WMSPickDemandController.class);

	@Autowired
	WMSService wWMSService;

	/**
	 * 条件查询产线工位领料需求
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

			String wOrderType = StringUtils.parseString(request.getParameter("OrderType"));
			String wDemandNo = StringUtils.parseString(request.getParameter("DemandNo"));
			int wProductID = StringUtils.parseInt(request.getParameter("ProductID"));
			int wLineID = StringUtils.parseInt(request.getParameter("LineID"));
			int wCustomerID = StringUtils.parseInt(request.getParameter("CustomerID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			String wMaterial = StringUtils.parseString(request.getParameter("Material"));
			int wStatus = StringUtils.parseInt(request.getParameter("Status"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<WMSPickDemand>> wServiceResult = wWMSService.WMS_QueryPickDemandList(wLoginUser,
					wOrderType, wDemandNo, wProductID, wLineID, wCustomerID, wOrderID, wPartID, wStatus, wMaterial,
					wStartTime, wEndTime);

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
	 * 查询领料需求单
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

			// 获取参数
			int wDemandID = StringUtils.parseInt(request.getParameter("DemandID"));

			if (wDemandID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：参数错误，DemandID不能小于或等于0!");
			}

			ServiceResult<WMSPickDemand> wServiceResult = wWMSService.WMS_QueryPickDemand(wLoginUser, wDemandID);

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
	 * 手动触发领料需求
	 */
	@GetMapping("/TriggerTask")
	public Object TriggerTask(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));

			if (wOrderID <= 0 || wPartID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			ServiceResult<Integer> wServiceResult = wWMSService.WMS_TriggerPickDemandTask(wLoginUser, wOrderID,
					wPartID);

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
	 * 手动推送领料需求
	 */
	@GetMapping("/ManualPush")
	public Object ManualPush(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wDemandID = StringUtils.parseInt(request.getParameter("DemandID"));

			if (wDemandID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			ServiceResult<Integer> wServiceResult = wWMSService.WMS_ManualPush(wLoginUser, wDemandID);

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
