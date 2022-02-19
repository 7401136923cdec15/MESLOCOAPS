package com.mes.loco.aps.server.controller.aps;

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
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSInstallation;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
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
@RequestMapping("/api/APSInstallation")
public class APSInstallationController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(APSInstallationController.class);

	@Autowired
	APSService wAPSService;

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

			ServiceResult<APSInstallation> wServiceResult = wAPSService.APS_QueryInstallation(wLoginUser, wID);

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

			int wID = StringUtils.parseInt(request.getParameter("ID"));
			int wLineID = StringUtils.parseInt(request.getParameter("LineID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wMaterialID = StringUtils.parseInt(request.getParameter("MaterialID"));
			int wProductID = StringUtils.parseInt(request.getParameter("ProductID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSInstallation>> wServiceResult = wAPSService.APS_QueryInstallationList(wLoginUser, wID,
					wLineID, wPartID, wMaterialID, wProductID);

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

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			APSInstallation wAPSInstallation = CloneTool.Clone(wParam.get("data"), APSInstallation.class);
			if (wAPSInstallation == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			BMSEmployee wLoginUser = this.GetSession(request);

			if (wAPSInstallation.ID <= 0) {
				wAPSInstallation.CreatorID = wLoginUser.ID;
				wAPSInstallation.CreateTime = Calendar.getInstance();
			} else {
				wAPSInstallation.EditorID = wLoginUser.ID;
				wAPSInstallation.EditTime = Calendar.getInstance();
			}

			ServiceResult<Long> wServiceResult = wAPSService.APS_UpdateInstallation(wLoginUser, wAPSInstallation);

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
