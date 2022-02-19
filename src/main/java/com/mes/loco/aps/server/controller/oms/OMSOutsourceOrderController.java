package com.mes.loco.aps.server.controller.oms;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mes.loco.aps.server.controller.BaseController;
import com.mes.loco.aps.server.service.OMSService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.oms.OMSOutsourceOrder;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2019年12月26日22:46:00
 * @LastEditTime 2019年12月27日16:39:20
 */
@RestController
@RequestMapping("/api/OMSOutsourceOrder")
public class OMSOutsourceOrderController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(OMSOutsourceOrderController.class);

	@Autowired
	OMSService wOMSService;

	/**
	 * 查单条
	 */
	@GetMapping("/Info")
	public Object Info(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));

			ServiceResult<OMSOutsourceOrder> wServiceResult = wOMSService.OMS_QueryOMSOutsourceOrder(wLoginUser, wID);

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

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/All")
	public Object All(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			long wID = StringUtils.parseLong(request.getParameter("ID"));
			int wStationID = StringUtils.parseInt(request.getParameter("StationID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			String wPartNo = StringUtils.parseString(request.getParameter("PartNo"));
			int wMaterialID = StringUtils.parseInt(request.getParameter("MaterialID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<OMSOutsourceOrder>> wServiceResult = wOMSService
					.OMS_QueryOMSOutsourceOrderList(wLoginUser, wID, wStationID, wOrderID, wPartNo, wMaterialID);

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
	 * 新增或更新
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Update")
	public Object Update(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			OMSOutsourceOrder wOMSOutsourceOrder = CloneTool.Clone(wParam.get("data"), OMSOutsourceOrder.class);
			if (wOMSOutsourceOrder == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			if (wOMSOutsourceOrder.ID <= 0) {
				wOMSOutsourceOrder.CreateID = wLoginUser.ID;
				wOMSOutsourceOrder.CreateTime = Calendar.getInstance();
			} else {
				wOMSOutsourceOrder.EditID = wLoginUser.ID;
				wOMSOutsourceOrder.EditTime = Calendar.getInstance();
			}

			ServiceResult<Long> wServiceResult = wOMSService.OMS_UpdateOMSOutsourceOrder(wLoginUser,
					wOMSOutsourceOrder);

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
