package com.mes.loco.aps.server.controller.aps;

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
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSBomBPM;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 台车BOM审批(流程引擎版)
 * 
 * @author PengYouWang
 * @CreateTime 2021-11-15 13:53:47
 */
@RestController
@RequestMapping("/api/APSBomBPM")
public class APSBomBPMController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(APSBomBPMController.class);

	@Autowired
	APSService wAPSService;

	/**
	 * 查历史记录
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/History")
	public Object History(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			String wCode = StringUtils.parseString(request.getParameter("Code"));
			int wUpFlowID = StringUtils.parseInt(request.getParameter("UpFlowID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wLineID = StringUtils.parseInt(request.getParameter("wLineID"));
			int wProductID = StringUtils.parseInt(request.getParameter("wProductID"));
			int wCustomerID = StringUtils.parseInt(request.getParameter("wCustomerID"));
			int wType = StringUtils.parseInt(request.getParameter("wType"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<APSBomBPM>> wServiceResult = wAPSService.APS_QueryBomBPMHistory(wLoginUser, wUpFlowID,
					wCode, wUpFlowID, wOrderID, wLineID, wProductID, wCustomerID, wType, wStartTime, wEndTime);

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
	 * 人员获取任务
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/EmployeeAll")
	public Object EmployeeAll(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wTagTypes = StringUtils.parseInt(request.getParameter("TagTypes"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<BPMTaskBase>> wServiceResult = wAPSService.APS_QueryBomBPMEmployeeAll(wLoginUser,
					wTagTypes, wStartTime, wEndTime);

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
	 * 人员获取任务
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/EmployeeAllWeb")
	public Object EmployeeAllWeb(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			String wCode = StringUtils.parseString(request.getParameter("Code"));
			int wUpFlowID = StringUtils.parseInt(request.getParameter("UpFlowID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wLineID = StringUtils.parseInt(request.getParameter("wLineID"));
			int wProductID = StringUtils.parseInt(request.getParameter("wProductID"));
			int wCustomerID = StringUtils.parseInt(request.getParameter("wCustomerID"));
			int wType = StringUtils.parseInt(request.getParameter("wType"));

			int wStatus = StringUtils.parseInt(request.getParameter("Status"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			String wMaterialName = StringUtils.parseString(request.getParameter("MaterialName"));
			String wMaterialNo = StringUtils.parseString(request.getParameter("MaterialNo"));

			ServiceResult<List<APSBomBPM>> wServiceResult = wAPSService.APS_QueryBomBPMEmployeeAllWeb(wLoginUser, wCode,
					wUpFlowID, wOrderID, wLineID, wProductID, wCustomerID, wType, wStatus, wStartTime, wEndTime,
					wMaterialName, wMaterialNo);

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
	 * 查单条
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/Info")
	public Object Info(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wID = StringUtils.parseInt(request.getParameter("ID"));

			ServiceResult<APSBomBPM> wServiceResult = wAPSService.APS_GetBomBPM(wLoginUser, wID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
				this.SetResult(wResult, "Notice", wServiceResult.CustomResult.get("Notice"));
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
	 * 根据工位找班组长
	 */
	@GetMapping("/UserByParts")
	public Object UserByParts(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			String wPartIDs = StringUtils.parseString(request.getParameter("PartIDs"));

			ServiceResult<List<BMSEmployee>> wServiceResult = wAPSService.APS_QueryUserByParts(wLoginUser, wPartIDs);

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
