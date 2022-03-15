package com.mes.loco.aps.server.controller.sfc;

import java.util.Calendar;
import java.util.HashMap;
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
import com.mes.loco.aps.server.service.SFCService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTaskItem;
import com.mes.loco.aps.server.service.po.sfc.SFCStationPerson;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 偶换件不合格评审(流程引擎版)
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-15 12:58:02
 * @LastEditTime 2020-7-15 12:58:06
 */
@RestController
@RequestMapping("/api/SFCBOMTask")
public class SFCBOMTaskController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(SFCBOMTaskController.class);

	@Autowired
	SFCService wSFCService;

	/**
	 * 查历史记录
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

			// ①获取参数
			int wID = StringUtils.parseInt(request.getParameter("ID"));
			String wCode = StringUtils.parseString(request.getParameter("Code"));
			int wUpFlowID = StringUtils.parseInt(request.getParameter("UpFlowID"));
			int wFollowerID = StringUtils.parseInt(request.getParameter("FollowerID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wPartPointID = StringUtils.parseInt(request.getParameter("PartPointID"));
			int wBOMID = StringUtils.parseInt(request.getParameter("BOMID"));
			int wLevel = StringUtils.parseInt(request.getParameter("Level"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<SFCBOMTask>> wServiceResult = wSFCService.SFC_QueryBOMTaskHistory(wLoginUser, wID, wCode,
					wUpFlowID, wFollowerID, wOrderID, wPartID, wPartPointID, wBOMID, wLevel, wStartTime, wEndTime);

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

			ServiceResult<List<BPMTaskBase>> wServiceResult = wSFCService.SFC_QueryBOMTaskEmployeeAll(wLoginUser,
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
	 * 获取待办、已办、发起 偶换件不合格评审任务
	 */
	@GetMapping("/EmployeeAllNew")
	public Object EmployeeAllNew(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wLevel = StringUtils.parseInt(request.getParameter("Level"));
			int wStatus = StringUtils.parseInt(request.getParameter("Status"));

			ServiceResult<List<SFCBOMTask>> wServiceResult = wSFCService.SFC_QueryBOMTaskEmployeeAllNew(wLoginUser,
					wStartTime, wEndTime, wOrderID, wPartID, wLevel, wStatus);

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
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wID = StringUtils.parseInt(request.getParameter("ID"));

			ServiceResult<SFCBOMTask> wServiceResult = wSFCService.SFC_GetBOMTask(wLoginUser, wID);

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
	 * 工位人员获取(工区主管、工艺师)
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/StationPerson")
	public Object StationPerson(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wStationID = StringUtils.parseInt(request.getParameter("StationID"));

			ServiceResult<SFCStationPerson> wServiceResult = wSFCService.SFC_QueryStationPerson(wLoginUser, wStationID);

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
	 * 获取待办、已办、发起 偶换件不合格评审任务
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

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wLevel = StringUtils.parseInt(request.getParameter("Level"));
			int wStatus = StringUtils.parseInt(request.getParameter("Status"));

			String wMaterialNo = StringUtils.parseString(request.getParameter("MaterialNo"));
			String wMaterialName = StringUtils.parseString(request.getParameter("MaterialName"));

			int wIsLGL = StringUtils.parseInt(request.getParameter("IsLGL"));
			int wIsQualityLoss = StringUtils.parseInt(request.getParameter("IsQualityLoss"));

			ServiceResult<List<SFCBOMTask>> wServiceResult = wSFCService.SFC_QueryBOMTaskList(wLoginUser, wOrderID,
					wPartID, wLevel, wStatus, wStartTime, wEndTime, wMaterialNo, wMaterialName, wIsLGL, wIsQualityLoss);

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
	 * 获取登陆者所在工位列表
	 */
	@GetMapping("/StationList")
	public Object StationList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<List<FPCPart>> wServiceResult = wSFCService.SFC_QueryLoginStationList(wLoginUser);

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
	 * 获取工位检验员
	 */
	@GetMapping("/PartCheckerList")
	public Object PartCheckerList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));

			ServiceResult<List<BMSEmployee>> wServiceResult = wSFCService.SFC_QueryPartCheckerList(wLoginUser, wPartID);

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
	 * 获取工艺师列表
	 */
	@GetMapping("/TechnicianList")
	public Object TechnicianList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<List<BMSEmployee>> wServiceResult = wSFCService.SFC_QueryTechnicianList(wLoginUser);

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
	 * 导出Excel(指定日期的已确认的物料清单)
	 */
	@GetMapping("/Export")
	public Object Export(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			Calendar wDate = StringUtils.parseCalendar(request.getParameter("Date"));

			ServiceResult<String> wServiceResult = wSFCService.SFC_ExportBOMTaskList(wLoginUser, wDate);

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
	 * 导出Excel(按照订单)
	 */
	@GetMapping("/ExportByOrder")
	public Object ExportByOrder(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			if (wOrderID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			ServiceResult<String> wServiceResult = wSFCService.SFC_ExportBOMTaskListByOrder(wLoginUser, wOrderID);

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
	 * 更新单据
	 * 
	 * @return
	 */
	@PostMapping("/Update")
	public Object Update(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			SFCBOMTask wData = CloneTool.Clone(wParam.get("data"), SFCBOMTask.class);

			ServiceResult<SFCBOMTask> wServiceResult = wSFCService.SFC_UpdateBOMTask(wLoginUser, wData);

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
	 * 定额问题
	 */
	@GetMapping("/UpdateQuota")
	public Object UpdateQuota(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_UpdateQuota(wLoginUser);

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
	 * 根据日志ID获取可用车辆列表
	 */
	@GetMapping("/OrderListByLogID")
	public Object OrderListByLogID(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wLogID = StringUtils.parseInt(request.getParameter("LogID"));

			if (wLogID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：参数错误!");
			}

			ServiceResult<List<OMSOrder>> wServiceResult = wSFCService.SFC_QueryOrderListByLogID(wLoginUser, wLogID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "Used", wServiceResult.CustomResult.get("Used"));
				this.SetResult(wResult, "Less", wServiceResult.CustomResult.get("Less"));
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
	 * 修改评估类型
	 */
	@PostMapping("/UpdateAssessType")
	public Object UpdateAssessType(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			List<SFCBOMTaskItem> wDataList = CloneTool.CloneArray(wParam.get("data"), SFCBOMTaskItem.class);

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_UpdateAssessType(wLoginUser, wDataList);

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
	 * 获取所有偶换件发起的订单列表
	 */
	@GetMapping("/OrderList")
	public Object OrderList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<List<OMSOrder>> wServiceResult = wSFCService.SFC_QueryOrderList(wLoginUser);

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
	 * 单号重置(内部重置)
	 */
	@GetMapping("/CodeReset")
	public Object CodeReset(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_BOMTaskCodeReset(wLoginUser);

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
	 * 更新任务更新时间
	 */
	@GetMapping("/UpdateSubmitTime")
	public Object UpdateSubmitTime(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_UpdateBOMTaskSubmitTime(wLoginUser);

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
	 * 委外处理(是否互换件√上)
	 */
	@GetMapping("/OutsourcingProcess")
	public Object OutsourcingProcess(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wSFCBomTaskID = StringUtils.parseInt(request.getParameter("SFCBomTaskID"));

			if (wSFCBomTaskID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_OutsourcingProcess(wLoginUser, wSFCBomTaskID);

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
