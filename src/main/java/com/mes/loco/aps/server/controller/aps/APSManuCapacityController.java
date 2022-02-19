package com.mes.loco.aps.server.controller.aps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.mes.loco.aps.server.service.CoreService;
import com.mes.loco.aps.server.service.mesenum.FPCPartTypes;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
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
@RequestMapping("/api/APSManuCapacity")
public class APSManuCapacityController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(APSManuCapacityController.class);

	@Autowired
	APSService wAPSService;

	@Autowired
	CoreService wCoreService;

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

			ServiceResult<APSManuCapacity> wServiceResult = wAPSService.APS_QueryManuCapacity(wLoginUser, wID);

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
			int wActive = StringUtils.parseInt(request.getParameter("Active"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSManuCapacity>> wServiceResult = wAPSService.APS_QueryManuCapacityList(wLoginUser, wID,
					wLineID, wPartID, wActive);

			List<FPCPart> wFPCPartList = wCoreService.FPC_QueryPartList(wLoginUser).List(FPCPart.class);

			List<APSManuCapacity> wOtherList = new ArrayList<APSManuCapacity>();
			for (FPCPart wFPCPart : wFPCPartList) {
				if (wFPCPart.Active != 1 || (wFPCPart.PartType != FPCPartTypes.Product.getValue()
						&& wFPCPart.PartType != FPCPartTypes.PrevCheck.getValue()))
					continue;

				APSManuCapacity wAPSManuCapacity = new APSManuCapacity();
				wAPSManuCapacity.Active = 1;
				wAPSManuCapacity.CreateTime = Calendar.getInstance();
				wAPSManuCapacity.CreatorID = wLoginUser.ID;
				wAPSManuCapacity.EditTime = Calendar.getInstance();
				wAPSManuCapacity.LineID = wLineID;
				wAPSManuCapacity.PartID = wFPCPart.ID;
				wOtherList.add(wAPSManuCapacity);
			}
			wOtherList = wOtherList.stream().filter(
					p -> !wServiceResult.Result.stream().anyMatch(q -> q.LineID == p.LineID && q.PartID == p.PartID))
					.collect(Collectors.toList());
			wServiceResult.Result.addAll(wOtherList);

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

			APSManuCapacity wCapacity = CloneTool.Clone(wParam.get("data"), APSManuCapacity.class);
			if (wCapacity == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			BMSEmployee wLoginUser = this.GetSession(request);

			if (wCapacity.ID <= 0) {
				wCapacity.CreatorID = wLoginUser.ID;
				wCapacity.CreateTime = Calendar.getInstance();
			} else {
				wCapacity.EditorID = wLoginUser.ID;
				wCapacity.EditTime = Calendar.getInstance();
			}

			ServiceResult<Long> wServiceResult = wAPSService.APS_UpdateManuCapacity(wLoginUser, wCapacity);

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
	 * 批量激活或禁用
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Active")
	public Object Active(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("Active"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");
			int wActive = StringUtils.parseInt(wParam.get("Active"));

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			List<APSManuCapacity> wList = CloneTool.CloneArray(wParam.get("data"), APSManuCapacity.class);
			if (wList == null || wList.size() <= 0)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			List<Integer> wIDList = wList.stream().map(p -> p.getID()).collect(Collectors.toList());

			wAPSService.APS_ActiveManuCapacityList(wLoginUser, wIDList, wActive);

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
