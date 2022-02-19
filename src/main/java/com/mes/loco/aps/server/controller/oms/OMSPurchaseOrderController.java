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
import com.mes.loco.aps.server.service.po.oms.OMSPurchaseOrder;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-5-22 20:28:30
 * @LastEditTime 2020-5-22 20:28:35
 */
@RestController
@RequestMapping("/api/OMSPurchaseOrder")
public class OMSPurchaseOrderController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(OMSPurchaseOrderController.class);

	@Autowired
	OMSService wOMSService;

	/**
	 * 查单条
	 * 
	 * @param request
	 * @return
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

			ServiceResult<OMSPurchaseOrder> wServiceResult = wOMSService.OMS_QueryOMSPurchaseOrder(wLoginUser, wID);

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
			int wPlaceID = StringUtils.parseInt(request.getParameter("PlaceID"));
			String wPartNo = StringUtils.parseString(request.getParameter("PartNo"));
			int wMaterialID = StringUtils.parseInt(request.getParameter("MaterialID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<OMSPurchaseOrder>> wServiceResult = wOMSService.OMS_QueryOMSPurchaseOrderList(wLoginUser,
					wID, wPlaceID, wPartNo, wMaterialID);

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

			OMSPurchaseOrder wOMSPurchaseOrder = CloneTool.Clone(wParam.get("data"), OMSPurchaseOrder.class);
			if (wOMSPurchaseOrder == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			if (wOMSPurchaseOrder.ID <= 0) {
				wOMSPurchaseOrder.CreateID = wLoginUser.ID;
				wOMSPurchaseOrder.CreateTime = Calendar.getInstance();
			} else {
				wOMSPurchaseOrder.EditID = wLoginUser.ID;
				wOMSPurchaseOrder.EditTime = Calendar.getInstance();
			}

			ServiceResult<Long> wServiceResult = wOMSService.OMS_UpdateOMSPurchaseOrder(wLoginUser, wOMSPurchaseOrder);

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
