package com.mes.loco.aps.server.service;

import java.util.Calendar;
import java.util.List;

import com.mes.loco.aps.server.service.mesenum.QRTypes;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.bfc.BFCAuditAction;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.dms.DMSLedgerStatus;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.utils.Configuration;

public interface CoreService {
	static String ServerUrl = Configuration.readConfigString("core.server.url", "config/config");

	static String ServerName = Configuration.readConfigString("core.server.project.name", "config/config");

	APIResult BMS_LoginEmployee(String wLoginName, String wPassword, String wToken, long wMac, int wnetJS);

	APIResult BMS_LogoutEmployee(BMSEmployee wLoginUse);

	APIResult BMS_GetEmployeeAll(BMSEmployee wLoginUser, int wDepartmentID, int wPosition, int wActive);

	APIResult BMS_QueryEmployeeByID(BMSEmployee wLoginUser, int wID);

	APIResult BMS_CheckPowerByAuthorityID(int wCompanyID, int wUserID, int wFunctionID, int wRangeID, int wTypeID);

	APIResult SFC_QueryShiftID(BMSEmployee wLoginUser, int Shifts);

	APIResult FMC_QueryWorkspace(BMSEmployee wLoginUser, int wID, String wCode);

	APIResult FMC_GetFMCWorkspaceList(BMSEmployee wLoginUser, int wProductID, int wPartID, String wPartNo,
			int wPlaceType, int wActive);

	APIResult SCH_QueryLeadWorkerByPositionID(BMSEmployee wLoginUser, int wPositionID, int wShiftID);

	APIResult SCH_QueryWorkerByPositionID(BMSEmployee wLoginUser, int wPositionID, int wShiftID);

	APIResult BFC_GetQRCode(BMSEmployee wLoginUser, QRTypes wQRTypes, long wQRCodeID);

	APIResult BFC_GetQRType(BMSEmployee wLoginUser, String wQRCode);

	APIResult FMC_SaveFMCWorkspace(BMSEmployee wBMSEmployee, FMCWorkspace wFMCWorkspace);

	APIResult DMS_GetDeviceLedgerList(BMSEmployee wBMSEmployee, int wBusinessUnitID, int wBaseID, int wFactoryID,
			int wWorkShopID, int wLineID, int wModelID, DMSLedgerStatus wDMSLedgerStatus);

	APIResult FPC_QueryProductList(BMSEmployee wBMSEmployee, int wBusinessUnitID, int wProductTypeID);

	APIResult FPC_GetPartByID(BMSEmployee wLoginEmployee, long wID);

	APIResult BMS_QueryPositionList(BMSEmployee wLoginUser, int wCompanyID);

	APIResult BMS_QueryDepartmentList(BMSEmployee wLoginUser, int wCompanyID, int wLoginID);

	APIResult FMC_QueryLineUnitList(BMSEmployee wLoginUser, int wLineID, int wProductID);

	APIResult CFG_QueryCalendarList(BMSEmployee wLoginUser, int wYear, int wWorkShopID);

	APIResult FMC_QueryWorkDayLists(BMSEmployee wLoginUser);

	APIResult FMC_QueryShiftList(BMSEmployee wLoginUser, int wFactoryID);

	APIResult FMC_QueryTimeZoneList(BMSEmployee wLoginUser, int wShiftID);

	APIResult FPC_QueryRouteList(BMSEmployee wLoginUser);

	APIResult FPC_QueryRoutePartList(BMSEmployee wLoginUser, int wRouteID);

	APIResult FMC_QueryLineList(BMSEmployee wLoginUser);

	APIResult FPC_QueryPartList(BMSEmployee wLoginUser);

	APIResult FPC_QueryPartPointList(BMSEmployee wLoginUser);

	APIResult FPC_QueryProductRouteList(BMSEmployee wLoginUser);

	APIResult FMC_QueryWorkShopList(BMSEmployee wLoginUser);

	APIResult FMC_QueryActiveWorkDay(BMSEmployee wLoginUser, int wActive);

	APIResult FMC_QueryWorkChargeList(BMSEmployee wLoginUser);

	APIResult CRM_QueryCustomerList(BMSEmployee wLoginUser);

	APIResult FPC_QueryProductList(BMSEmployee wLoginUser);

	APIResult BFC_GetMessageList(BMSEmployee wLoginUser, int wResponsorID, int wModuleID, int wType, int wActive,
			int wShiftID, Calendar wStartTime, Calendar wEndTime);

	APIResult BFC_UpdateMessageList(BMSEmployee wLoginUser, List<BFCMessage> wBFCMessageList);

	APIResult BFC_SendMessageList(BMSEmployee wLoginUser, List<BFCMessage> wBFCMessageList);

	APIResult BMS_FunctionRangeAll(BMSEmployee wLoginUser, int wOperatorID, int wFunctionID);

	APIResult BMS_FunctionAll(BMSEmployee wLoginUser, int wOperatorID);

	APIResult BMS_UserAllByFunction(BMSEmployee wLoginUser, int wFunctionID);

	APIResult BFC_UpdateAction(BMSEmployee wLoginUser, BFCAuditAction wBFCAuditAction, String wTitle);

	APIResult BFC_CurrentConfig(BMSEmployee wLoginUser, int wModuleID, int wTaskID, int wUserID);

	APIResult BFC_ActionAll(BMSEmployee wLoginUser, int wModuleID, int wTaskID);

	APIResult BFC_GetMessageList(BMSEmployee wLoginUser, int wResponsorID, int wModuleID, int wMessageID, int wType,
			int wActive, int wShiftID, Calendar wStartTime, Calendar wEndTime);

	APIResult BFC_GetMessageList(BMSEmployee wLoginUser, int wResponsorID, int wModuleID, List<Integer> wMessageID,
			int wType, int wActive);

	APIResult CFG_QueryUnitList(BMSEmployee wLoginUser);

	APIResult QMS_StartInstance(BMSEmployee wLoginUser, String processDefinitionKey);

	APIResult QMS_CompleteInstance(BMSEmployee wLoginUser, BPMTaskBase wBPMTaskBase, String wTaskID);
	
	APIResult BMS_UserAllByRoleID(BMSEmployee wLoginUser, int wRoleID);
}
