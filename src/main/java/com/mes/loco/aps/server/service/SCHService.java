package com.mes.loco.aps.server.service;

import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSDepartment;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.sch.SCHSecondment;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentApply;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentBPM;

import java.util.Calendar;
import java.util.List;

public interface SCHService {
	ServiceResult<SCHSecondment> SCH_QuerySecondment(BMSEmployee paramBMSEmployee, int paramInt);

	ServiceResult<List<SCHSecondment>> SCH_QuerySecondmentList(BMSEmployee paramBMSEmployee, int paramInt1,
			int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, List<Integer> paramList,
			Calendar paramCalendar1, Calendar paramCalendar2);

	ServiceResult<Integer> SCH_UpdateSecondment(BMSEmployee paramBMSEmployee, SCHSecondment paramSCHSecondment);

	ServiceResult<List<BMSEmployee>> SCH_QueryCanSelectList(BMSEmployee paramBMSEmployee, int paramInt);

	ServiceResult<List<SCHSecondment>> SCH_QuerySecondmentListByTagTypes(BMSEmployee paramBMSEmployee, int paramInt);

	ServiceResult<List<BMSEmployee>> SCH_QueryEmoloyeeListByClassList(BMSEmployee paramBMSEmployee,
			List<BMSDepartment> paramList);

	ServiceResult<List<BFCMessage>> SFC_GetMessaegList(BMSEmployee paramBMSEmployee, SCHSecondment paramSCHSecondment);

	ServiceResult<Integer> SCH_SendMessageToRelaPersons(BMSEmployee paramBMSEmployee, List<SCHSecondment> paramList);

	ServiceResult<Integer> SFH_DeadlineSecondment(BMSEmployee paramBMSEmployee, int paramInt);

	/**
	 * 借调也需要你写接口了，先界面要分为（借调发起 借调审批 ），还可以通过时间查询
	 * 
	 */
	ServiceResult<List<SCHSecondment>> SCH_QueryAll(BMSEmployee wLoginUser, int wTagType, Calendar wStartTime,
			Calendar wEndTime, int wClassID);

	/**
	 * 查询默认状态的借调单
	 */
	ServiceResult<SCHSecondmentBPM> SCH_QueryDefaultSecondementBPM(BMSEmployee wLoginUser, int wEventID);

	/**
	 * 创建借调单
	 */
	ServiceResult<SCHSecondmentBPM> SCH_CreateSecondementBPM(BMSEmployee wLoginUser, BPMEventModule wEventID);

	/**
	 * 提交借调单
	 */
	ServiceResult<SCHSecondmentBPM> SCH_SubmitSecondementBPM(BMSEmployee wLoginUser, SCHSecondmentBPM wData);

	/**
	 * 查询单条借调单
	 */
	ServiceResult<SCHSecondmentBPM> SCH_GetSecondementBPM(BMSEmployee wLoginUser, int wID);

	/**
	 * 用人员拿任务
	 */
	ServiceResult<List<SCHSecondmentBPM>> SFC_QuerySecondementBPMEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime, int wType);

	/**
	 * 查询借调历史
	 */
	ServiceResult<List<SCHSecondmentBPM>> SCH_QuerySecondmentBPMHistory(BMSEmployee wLoginUser, int wID, String wCode,
			int wUpFlowID, int wFollowerID, int wPersonID, int wType, Calendar wStartTime, Calendar wEndTime);

	/**
	 * 查询默认状态的单据
	 */
	ServiceResult<SCHSecondmentApply> SCH_QueryDefaultSecondmentApply(BMSEmployee wLoginUser, int wEventID);

	/**
	 * 创建单据
	 */
	ServiceResult<SCHSecondmentApply> SCH_CreateSecondmentApply(BMSEmployee wLoginUser, BPMEventModule wEventID);

	/**
	 * 提交单据
	 */
	ServiceResult<SCHSecondmentApply> SCH_SubmitSecondmentApply(BMSEmployee wLoginUser, SCHSecondmentApply wData);

	/**
	 * 查询单条单据
	 */
	ServiceResult<SCHSecondmentApply> SCH_GetSecondmentApply(BMSEmployee wLoginUser, int wID);

	/**
	 * 用人员拿任务
	 */
	ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime, int wType);

	/**
	 * 查询单据历史
	 */
	ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyHistory(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wPersonID, int wType, Calendar wStartTime, Calendar wEndTime);

	/**
	 * 获取人员信息
	 */
	ServiceResult<Integer> SCH_QueryPersonInfo(BMSEmployee wLoginUser);

	/**
	 * 获取人员信息
	 */
	ServiceResult<Integer> SCH_QueryPersonInfo(BMSEmployee wLoginUser, int wPerson);

	/**
	 * 根据工区ID获取工区下班组列表
	 */
	ServiceResult<List<BMSDepartment>> SCH_QueryAreaClassList(BMSEmployee wLoginUser, int wAreaID);

	/**
	 * 根据班组ID获取班组成员集合
	 */
	ServiceResult<List<BMSEmployee>> SFC_QueryEmployeePartList(BMSEmployee wLoginUser, int wClassID);

	/**
	 * 终止借调
	 */
	ServiceResult<Integer> SCH_DeadLine(BMSEmployee wLoginUser, SCHSecondmentApply wData);

	ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyHistoryAll(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wPersonID, int wType, Calendar wStartTime, Calendar wEndTime);

	ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyEmployeeAllPro(BMSEmployee wLoginUser,
			int wTagTypes, Calendar wStartTime, Calendar wEndTime, int wType);

	ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyEmployeeAllNew(BMSEmployee wLoginUser,
			Calendar wStartTime, Calendar wEndTime, int wType, int wStatus);
	
	ServiceResult<List<SCHSecondmentApply>> SCH_QuerySecondmentApplyList(BMSEmployee wLoginUser, int wType, int wStatus,
			Calendar wStartTime, Calendar wEndTime);
}
