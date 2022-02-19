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
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPart;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPartTJ;
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
@RequestMapping("/api/RPTOrderPart")
public class RPTOrderPartController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(RPTOrderPartController.class);

	@Autowired
	RPTService wRPTService;

	/**
	 * 条件查询工位订单统计数据
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/All")
	public Object All(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wLineID = StringUtils.parseInt(request.getParameter("LineID"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<RPTOrderPart>> wServiceResult = wRPTService.RPT_QueryRPTOrderPartList(wLoginUser,
					wLineID, wStartTime, wEndTime);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "Tree", wServiceResult.CustomResult.get("Tree"));
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
	 * 全年订单工位统计数据
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/AllTJ")
	public Object AllTJ(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wYear = StringUtils.parseInt(request.getParameter("Year"));

			ServiceResult<Map<Integer, List<RPTOrderPartTJ>>> wServiceResult = wRPTService
					.RPT_QueryRPTOrderPartTJList(wLoginUser, wYear);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "Tree", wServiceResult.CustomResult.get("Tree"));
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
