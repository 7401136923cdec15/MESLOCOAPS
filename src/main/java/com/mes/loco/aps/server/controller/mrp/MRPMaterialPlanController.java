package com.mes.loco.aps.server.controller.mrp;

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
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.mrp.MRPMaterialPlan;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 物料需求计划控制器
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-3-10 13:32:09
 */
@RestController
@RequestMapping("/api/MRPMaterialPlan")
public class MRPMaterialPlanController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(MRPMaterialPlanController.class);

	@Autowired
	APSService wAPSService;

	/**
	 * 条件查询配料需求计划
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

			// 获取参数
			int wProductID = StringUtils.parseInt(request.getParameter("ProductID"));
			int wLineID = StringUtils.parseInt(request.getParameter("LineID"));
			int wCustomerID = StringUtils.parseInt(request.getParameter("CustomerID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wStepID = StringUtils.parseInt(request.getParameter("StepID"));
			String wMaterial = StringUtils.parseString(request.getParameter("Material"));
			int wMaterialType = StringUtils.parseInt(request.getParameter("MaterialType"));
			int wReplaceType = StringUtils.parseInt(request.getParameter("ReplaceType"));
			int wOutSourceType = StringUtils.parseInt(request.getParameter("OutSourceType"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<MRPMaterialPlan>> wServiceResult = wAPSService.MRP_QueryMaterialPlanList(wLoginUser,
					wProductID, wLineID, wCustomerID, wOrderID, wPartID, wStepID, wMaterial, wMaterialType,
					wReplaceType, wOutSourceType, wStartTime, wEndTime);

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
	 * 手动触发物料配送单
	 */
	@GetMapping("/HandTrigger")
	public Object HandTrigger(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));

			if (wOrderID <= 0 || wPartID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			ServiceResult<Integer> wServiceResult = wAPSService.MRP_HandTrigger(wLoginUser, wOrderID, wPartID);

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
}
