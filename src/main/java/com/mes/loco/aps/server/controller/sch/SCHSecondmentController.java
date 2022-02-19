package com.mes.loco.aps.server.controller.sch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import com.mes.loco.aps.server.service.CoreService;
import com.mes.loco.aps.server.service.LFSService;
import com.mes.loco.aps.server.service.SCHService;
import com.mes.loco.aps.server.service.mesenum.BMSDepartmentType;
import com.mes.loco.aps.server.service.mesenum.SCHSecondStatus;
import com.mes.loco.aps.server.service.mesenum.TagTypes;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSDepartment;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.loco.aps.server.service.po.sch.SCHSecondment;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.LFSServiceImpl;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 借调控制器
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-15 14:51:27
 * @LastEditTime 2020-7-15 14:51:31
 */
@RestController
@RequestMapping("/api/SCHSecondment")
public class SCHSecondmentController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(SCHSecondmentController.class);

	@Autowired
	SCHService wSCHService;

	@Autowired
	CoreService wCoreService;

	@Autowired
	LFSService wLFSService;

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

			ServiceResult<SCHSecondment> wServiceResult = wSCHService.SCH_QuerySecondment(wLoginUser, wID);

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
	 * 获取某班组可借调的人员列表
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/SecondEmployeeList")
	public Object SecondEmployeeList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wDepartmentID = StringUtils.parseInt(request.getParameter("DepartmentID"));
			if (wDepartmentID <= 0)
				wDepartmentID = StringUtils.parseInt(request.getParameter("AreaDepartmentID"));

			ServiceResult<List<BMSEmployee>> wServiceResult = wSCHService.SCH_QueryCanSelectList(wLoginUser,
					wDepartmentID);
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

			BMSEmployee wLoginUser = this.GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));
			int wSecondAuditID = StringUtils.parseInt(request.getParameter("SecondAuditID"));
			int wBeSecondAuditID = StringUtils.parseInt(request.getParameter("BeSecondAuditID"));
			int wSecondDepartmentID = StringUtils.parseInt(request.getParameter("SecondDepartmentID"));
			int wSecondPersonID = StringUtils.parseInt(request.getParameter("SecondPersonID"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<SCHSecondment>> wServiceResult = wSCHService.SCH_QuerySecondmentList(wLoginUser, wID, -1,
					wSecondDepartmentID, wSecondAuditID, wBeSecondAuditID, wSecondPersonID, null, wStartTime, wEndTime);

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
	 * 标签拿借调单(1：待处理任务)2：发起的任务)
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/EmployeeAll")
	public Object EmployeeAll(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wTagTypes = StringUtils.parseInt(request.getParameter("TagTypes"));

			ServiceResult<List<SCHSecondment>> wServiceResult = wSCHService
					.SCH_QuerySecondmentListByTagTypes(wLoginUser, wTagTypes);

			ServiceResult<List<SCHSecondment>> wToDoRst = this.wSCHService.SCH_QuerySecondmentListByTagTypes(wLoginUser,
					TagTypes.Dispatcher.getValue());
			ServiceResult<List<SCHSecondment>> wDoneRst = this.wSCHService.SCH_QuerySecondmentListByTagTypes(wLoginUser,
					TagTypes.Applicant.getValue());

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				List<BMSDepartment> wList = GetClassList(wLoginUser);
				SetResult(wResult, "ClassList", wList);
				ServiceResult<List<BMSEmployee>> wUserSrt = wSCHService.SCH_QueryEmoloyeeListByClassList(wLoginUser,
						wList);
				SetResult(wResult, "EmployeeList", wUserSrt.Result);

				SetResult(wResult, "ToDoList", wToDoRst.Result);
				SetResult(wResult, "DoneList", wDoneRst.Result);
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
	 * 创建借调单(主要是判断权限用)(无参)
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Create")
	public Object Create(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wAreaID = this.GetAreaID(wLoginUser);
			if (wAreaID <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
				return wResult;
			}

			SCHSecondment wSCHSecondment = new SCHSecondment();
			wSCHSecondment.SendID = wLoginUser.ID;
			wSCHSecondment.SendTime = Calendar.getInstance();

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wSCHSecondment);

		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 新增借调单(本工区提交时使用(跨区))
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
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			SCHSecondment wSCHSecondment = CloneTool.Clone(wParam.get("data"), SCHSecondment.class);
			if (wSCHSecondment == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			if (wSCHSecondment.ID == 0) {
				wSCHSecondment.SendTime = Calendar.getInstance();
				wSCHSecondment.Status = SCHSecondStatus.ToOtherSecond.getValue();
				wSCHSecondment.SecondAuditID = wLoginUser.ID;
				wSCHSecondment.SendAuditTime = Calendar.getInstance();
				wSCHSecondment.ApplyValidDate = wSCHSecondment.ValidDate;
			}

			ServiceResult<Integer> wServiceResult = wSCHService.SCH_UpdateSecondment(wLoginUser, wSCHSecondment);

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
	 * 工区主管获取可选工区列表(无参)
	 */
	@GetMapping("/AreaList")
	public Object AreaList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			// 部门列表
			List<BMSDepartment> wDepartmentList = wCoreService.BMS_QueryDepartmentList(wLoginUser, 0, 0)
					.List(BMSDepartment.class);
			if (wDepartmentList == null || wDepartmentList.size() <= 0)
				return GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

			List<LFSWorkAreaChecker> wList = wLFSService.LFS_QueryWorkAreaCheckerList(wLoginUser)
					.List(LFSWorkAreaChecker.class);
			if (wList.stream().anyMatch(
					p -> p.LeaderIDList != null && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				int wAreaID = wList.stream().filter(
						p -> p.LeaderIDList != null && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))
						.findFirst().get().WorkAreaID;
				wDepartmentList = wDepartmentList.stream().filter(p -> p.Type == 2 && p.ID != wAreaID)
						.collect(Collectors.toList());
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wDepartmentList, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", new ArrayList<BMSDepartment>(), null);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 批量保存(本工区主管选多人时使用)
	 */
	@PostMapping("/SaveList")
	public Object SaveList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("SCHSecondment") || !wParam.containsKey("PersonIDList"))
				return GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

			SCHSecondment wSCHSecondment = CloneTool.Clone(wParam.get("SCHSecondment"), SCHSecondment.class);

			List<Integer> wPersonIDList = CloneTool.CloneArray(wParam.get("PersonIDList"), Integer.class);
			if (wPersonIDList == null || wPersonIDList.size() <= 0)
				return GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

			int wAreaID = GetAreaID(wLoginUser);

			// 人员列表
			List<BMSEmployee> wEmployeeList = wCoreService.BMS_GetEmployeeAll(wLoginUser, 0, 0, 1)
					.List(BMSEmployee.class);
			if (wEmployeeList == null || wEmployeeList.size() <= 0)
				return GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

			SCHSecondment wTemp = null;
			List<BFCMessage> wMessageList = new ArrayList<BFCMessage>();
			for (Integer wPersonID : wPersonIDList) {
				wTemp = new SCHSecondment();

				wTemp.SendID = wLoginUser.ID;
				wTemp.SendTime = Calendar.getInstance();
				wTemp.SecondDepartmentID = wSCHSecondment.SecondDepartmentID;
				wTemp.IsOverArea = false;
				wTemp.SecondAuditID = wLoginUser.ID;
				wTemp.SendAuditTime = Calendar.getInstance();
				wTemp.SecondPersonID = wPersonID;

				wTemp.BeSecondAuditID = wLoginUser.ID;
				wTemp.BeSecondAuditTime = Calendar.getInstance();
				wTemp.AreaID = wAreaID;

				wTemp.BeSecondDepartmentID = wSCHSecondment.BeSecondDepartmentID;

				wTemp.Status = SCHSecondStatus.Seconded.getValue();
				wTemp.ValidDate = wSCHSecondment.ValidDate;
				wTemp.ApplyValidDate = wSCHSecondment.ApplyValidDate;
				wTemp.IsExclude = wSCHSecondment.IsExclude;

				ServiceResult<Integer> wRst = wSCHService.SCH_UpdateSecondment(wLoginUser, wTemp);
				if (wRst == null || wRst.Result == null || wRst.Result <= 0)
					break;

				wTemp.ID = wRst.Result;

				// 发送通知消息给借调班班长、被借调班班长、被借调人员
				wMessageList.addAll(wSCHService.SFC_GetMessaegList(wLoginUser, wTemp).Result);
			}

			wCoreService.BFC_UpdateMessageList(wLoginUser, wMessageList);

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	private int GetAreaID(BMSEmployee wLoginUser) {
		int wResult = 0;
		try {
			List<LFSWorkAreaChecker> wList = wLFSService.LFS_QueryWorkAreaCheckerList(wLoginUser)
					.List(LFSWorkAreaChecker.class);
			if (wList != null && wList.size() > 0 && wList.stream().anyMatch(
					p -> p.LeaderIDList != null && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				wResult = wList.stream().filter(p -> p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))
						.findFirst().get().WorkAreaID;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 批量保存(相关工区主管选多人时使用)
	 */
	@PostMapping("/UpdateList")
	public Object UpdateList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("SCHSecondment") || !wParam.containsKey("PersonIDList"))
				return GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

			SCHSecondment wSCHSecondment = CloneTool.Clone(wParam.get("SCHSecondment"), SCHSecondment.class);

			List<Integer> wPersonIDList = CloneTool.CloneArray(wParam.get("PersonIDList"), Integer.class);
			if (wPersonIDList == null || wPersonIDList.size() <= 0)
				return GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

			// 人员列表
			List<BMSEmployee> wEmployeeList = wCoreService.BMS_GetEmployeeAll(wLoginUser, 0, 0, 1)
					.List(BMSEmployee.class);
			if (wEmployeeList == null || wEmployeeList.size() <= 0)
				return GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

			wSCHSecondment.IsOverArea = true;
			wSCHSecondment.SecondPersonID = wPersonIDList.get(0);
			wSCHSecondment.Status = SCHSecondStatus.Seconded.getValue();
			wSCHSecondment.SecondAuditID = wSCHSecondment.SendID;
			wSCHSecondment.BeSecondAuditID = wLoginUser.ID;
			wSCHSecondment.BeSecondAuditTime = Calendar.getInstance();
			wSCHSecondment.ApplyValidDate = wSCHSecondment.ValidDate;

			ServiceResult<Integer> wRst = wSCHService.SCH_UpdateSecondment(wLoginUser, wSCHSecondment);
			if (wRst == null || wRst.Result == null || wRst.Result <= 0)
				return GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

			SCHSecondment wTemp = null;
			List<SCHSecondment> wMessageSourceList = new ArrayList<SCHSecondment>();
			wMessageSourceList.add(wSCHSecondment);
			if (wPersonIDList.size() > 1) {
				for (int i = 1; i < wPersonIDList.size(); i++) {
					wTemp = CloneTool.Clone(wSCHSecondment, SCHSecondment.class);

					wTemp.ID = 0;
					wTemp.SecondPersonID = wPersonIDList.get(i);

					ServiceResult<Integer> wRsts = wSCHService.SCH_UpdateSecondment(wLoginUser, wTemp);
					if (wRsts == null || wRsts.Result == null || wRsts.Result <= 0)
						return GetResult(RetCode.SERVER_CODE_ERR, wRsts.FaultCode);
					wTemp.ID = wRsts.Result;

					wMessageSourceList.add(wTemp);
				}
			}

			// 责任工区主管处理借调单后，发送通知消息给相关人员
			wSCHService.SCH_SendMessageToRelaPersons(wLoginUser, wMessageSourceList);

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取登陆者所在工区的所有班组列表
	 * 
	 * @param wLoginUser
	 * @param wRstDepartmentList
	 */
	private List<BMSDepartment> GetClassList(BMSEmployee wLoginUser) {
		List<BMSDepartment> wResult = new ArrayList<BMSDepartment>();
		try {
			List<LFSWorkAreaChecker> wCheckerList = LFSServiceImpl.getInstance()
					.LFS_QueryWorkAreaCheckerList(wLoginUser).List(LFSWorkAreaChecker.class);
			if (wCheckerList == null || wCheckerList.size() <= 0) {
				return wResult;
			}

			if (wCheckerList.stream().anyMatch(
					p -> p.LeaderIDList != null && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				int wWorkAreaID = wCheckerList.stream().filter(
						p -> p.LeaderIDList != null && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))
						.findFirst().get().WorkAreaID;
				if (wWorkAreaID > 0) {
					List<BMSDepartment> wDepartmentList = GetClassListByAreaID(wWorkAreaID,
							wCoreService.BMS_QueryDepartmentList(wLoginUser, 0, 0).List(BMSDepartment.class));
					if (wDepartmentList != null && wDepartmentList.size() > 0) {
						wResult.addAll(wDepartmentList);
					}
				}
			} else {
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据工区ID获取所有该工区下所有的班组列表
	 * 
	 * @param wWorkAreaID
	 * @return
	 */
	private List<BMSDepartment> GetClassListByAreaID(int wWorkAreaID, List<BMSDepartment> wAllList) {
		List<BMSDepartment> wResult = new ArrayList<BMSDepartment>();
		try {
			List<BMSDepartment> wList = FindChildList(wAllList, wWorkAreaID);
			if (wList != null && wList.size() > 0) {
				for (BMSDepartment wBMSDepartment : wList) {
					if (wBMSDepartment.Type == BMSDepartmentType.Class.getValue()) {
						wResult.add(wBMSDepartment);
					}
					wResult.addAll(GetClassListByAreaID(wBMSDepartment.ID, wAllList));
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private List<BMSDepartment> FindChildList(List<BMSDepartment> wAllList, int wDepartmentID) {
		List<BMSDepartment> wResult = new ArrayList<BMSDepartment>();
		try {
			wResult = wAllList.stream().filter(p -> p.ParentID == wDepartmentID).collect(Collectors.toList());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@PostMapping({ "/Deadline" })
	public Object Deadline(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wSCHSecondmentID = StringUtils.parseInt(wParam.get("SCHSecondmentID")).intValue();

			ServiceResult<Integer> wServiceResult = this.wSCHService.SFH_DeadlineSecondment(wLoginUser,
					wSCHSecondmentID);

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
	 * 借调也需要你写接口了，先界面要分为（借调发起 借调审批 ），还可以通过时间查询
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/QueryAll")
	public Object QueryAll(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wTagType = StringUtils.parseInt(request.getParameter("TagType"));//2 发起  //4 审批
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wClassID = StringUtils.parseInt(request.getParameter("ClassID"));

			ServiceResult<List<SCHSecondment>> wServiceResult = wSCHService.SCH_QueryAll(wLoginUser, wTagType,
					wStartTime, wEndTime, wClassID);

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
