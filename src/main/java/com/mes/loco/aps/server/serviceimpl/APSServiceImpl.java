package com.mes.loco.aps.server.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.mesenum.APSBOMSourceType;
import com.mes.loco.aps.server.service.mesenum.APSBomBPMStatus;
import com.mes.loco.aps.server.service.mesenum.APSOperateType;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.APSTaskBPMStatus;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.APSTaskStepCancelLogBPMStatus;
import com.mes.loco.aps.server.service.mesenum.BFCMessageType;
import com.mes.loco.aps.server.service.mesenum.BMSDepartmentType;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.BPMStatus;
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.FPCPartTypes;
import com.mes.loco.aps.server.service.mesenum.LFSStationType;
import com.mes.loco.aps.server.service.mesenum.MBSRoleTree;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.MRPMaterialPlanStatus;
import com.mes.loco.aps.server.service.mesenum.OMSOrderPriority;
import com.mes.loco.aps.server.service.mesenum.OMSOrderStatus;
import com.mes.loco.aps.server.service.mesenum.SFCOrderFormType;
import com.mes.loco.aps.server.service.mesenum.SFCOutSourceType;
import com.mes.loco.aps.server.service.mesenum.SFCReplaceType;
import com.mes.loco.aps.server.service.mesenum.SFCTaskStepType;
import com.mes.loco.aps.server.service.mesenum.SFCTaskType;
import com.mes.loco.aps.server.service.mesenum.SFCTurnOrderTaskStatus;
import com.mes.loco.aps.server.service.mesenum.TCMChangeType;
import com.mes.loco.aps.server.service.mesenum.TagTypes;
import com.mes.loco.aps.server.service.mesenum.TaskQueryType;
import com.mes.loco.aps.server.service.mesenum.WMSOrderType;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.AndonTime;
import com.mes.loco.aps.server.service.po.aps.APSAuditConfig;
import com.mes.loco.aps.server.service.po.aps.APSBOMItem;
import com.mes.loco.aps.server.service.po.aps.APSBOMLog;
import com.mes.loco.aps.server.service.po.aps.APSBomBPM;
import com.mes.loco.aps.server.service.po.aps.APSBomBPMItem;
import com.mes.loco.aps.server.service.po.aps.APSCondition;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAudit;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAuditBPM;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAuditItemBPM;
import com.mes.loco.aps.server.service.po.aps.APSDismantling;
import com.mes.loco.aps.server.service.po.aps.APSInstallation;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacityStep;
import com.mes.loco.aps.server.service.po.aps.APSMessage;
import com.mes.loco.aps.server.service.po.aps.APSPartNoDetails;
import com.mes.loco.aps.server.service.po.aps.APSPartsLog;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersion;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersionBPM;
import com.mes.loco.aps.server.service.po.aps.APSStepPlanParam;
import com.mes.loco.aps.server.service.po.aps.APSTaskLine;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSTaskPartDetails;
import com.mes.loco.aps.server.service.po.aps.APSTaskRemark;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepCancelLog;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepCancelLogBPM;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepDetails;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepInfo;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepPlan;
import com.mes.loco.aps.server.service.po.aps.APSTriggerPart;
import com.mes.loco.aps.server.service.po.aps.APSWorkAreaDetails;
import com.mes.loco.aps.server.service.po.aps.APSWorkHour;
import com.mes.loco.aps.server.service.po.bfc.BFCAuditAction;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSDepartment;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bms.BMSProductClassPart;
import com.mes.loco.aps.server.service.po.bms.BMSRoleItem;
import com.mes.loco.aps.server.service.po.bms.BMSWorkChargeItem;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.cfg.CFGCalendar;
import com.mes.loco.aps.server.service.po.excel.ExcelData;
import com.mes.loco.aps.server.service.po.excel.ExcelLineData;
import com.mes.loco.aps.server.service.po.excel.ExcelSheetData;
import com.mes.loco.aps.server.service.po.fmc.FMCLine;
import com.mes.loco.aps.server.service.po.fmc.FMCTimeZone;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkCharge;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.fpc.FPCCapacityStepTemp;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCPartPoint;
import com.mes.loco.aps.server.service.po.fpc.FPCPartTemp;
import com.mes.loco.aps.server.service.po.fpc.FPCProduct;
import com.mes.loco.aps.server.service.po.fpc.FPCRoute;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePartPoint;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePartTemp;
import com.mes.loco.aps.server.service.po.fpc.StepPlanTableTemp;
import com.mes.loco.aps.server.service.po.ipt.IPTValue;
import com.mes.loco.aps.server.service.po.lfs.LFSStoreHouse;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.loco.aps.server.service.po.mcs.MCSLogInfo;
import com.mes.loco.aps.server.service.po.mrp.MRPMaterialPlan;
import com.mes.loco.aps.server.service.po.mss.MSSBOM;
import com.mes.loco.aps.server.service.po.mss.MSSBOMItem;
import com.mes.loco.aps.server.service.po.mss.MSSMaterial;
import com.mes.loco.aps.server.service.po.mtc.MTCSectionInfo;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.oms.OMSOutsourceOrder;
import com.mes.loco.aps.server.service.po.rsm.RSMTurnOrderTask;
import com.mes.loco.aps.server.service.po.sap.APSBomData;
import com.mes.loco.aps.server.service.po.sap.HEAD;
import com.mes.loco.aps.server.service.po.sap.INPUT2;
import com.mes.loco.aps.server.service.po.sap.ITEM;
import com.mes.loco.aps.server.service.po.sap.OrderItem;
import com.mes.loco.aps.server.service.po.sap.SAPResult;
import com.mes.loco.aps.server.service.po.sch.SCHSecondmentApply;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTaskItem;
import com.mes.loco.aps.server.service.po.sfc.SFCOrderForm;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskIPT;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStep;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepInfo;
import com.mes.loco.aps.server.service.po.tcm.TCMMaterialChangeItems;
import com.mes.loco.aps.server.service.po.tcm.TCMMaterialChangeLog;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemand;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemandItem;
import com.mes.loco.aps.server.service.utils.CalendarUtil;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.Configuration;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.service.utils.XmlTool;
import com.mes.loco.aps.server.serviceimpl.APSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.FMCServiceImpl;
import com.mes.loco.aps.server.serviceimpl.LFSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.MyHelperServiceImpl;
import com.mes.loco.aps.server.serviceimpl.OMSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.QMSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.SFCServiceImpl;
import com.mes.loco.aps.server.serviceimpl.WDWServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.andon.AndonDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSAuditConfigDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSBOMItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSBOMLogDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSBomBPMDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSBomBPMItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSConditionDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSDayPlanAuditBPMDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSDayPlanAuditDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSDismantlingDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSInstallationDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSManuCapacityDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSManuCapacityStepDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSMessageDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSPartsLogDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSSchedulingVersionBPMDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSSchedulingVersionDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskLineDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskPartDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskStepCancelLogBPMDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskStepCancelLogDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskStepDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTaskStepPlanDAO;
import com.mes.loco.aps.server.serviceimpl.dao.aps.APSTriggerPartDAO;
import com.mes.loco.aps.server.serviceimpl.dao.bms.BMSWorkChargeItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.fpc.FPCRouteDAO;
import com.mes.loco.aps.server.serviceimpl.dao.mcs.MCSLogInfoDAO;
import com.mes.loco.aps.server.serviceimpl.dao.mrp.MRPMaterialPlanDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sch.SCHSecondmentApplyDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCBOMTaskDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCBOMTaskItemDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCOrderFormDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCTaskStepDAO;
import com.mes.loco.aps.server.serviceimpl.dao.tcm.TCMMaterialChangeLogDAO;
import com.mes.loco.aps.server.serviceimpl.dao.wms.WMSPickDemandDAO;
import com.mes.loco.aps.server.serviceimpl.dao.wms.WMSPickDemandItemDAO;
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSAutoUtils;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSManualUtils;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;
import com.mes.loco.aps.server.shristool.CalendarDAO;
import com.mes.loco.aps.server.utils.Constants;
import com.mes.loco.aps.server.utils.RetCode;
import com.mes.loco.aps.server.utils.aps.ExcelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class APSServiceImpl implements APSService {
	private static Logger logger = LoggerFactory.getLogger(APSServiceImpl.class);

	private static APSService Instance;

	public static APSService getInstance() {
		if (Instance == null)
			Instance = new APSServiceImpl();
		return Instance;
	}

	public ServiceResult<APSManuCapacity> APS_QueryManuCapacity(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSManuCapacity> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSManuCapacityDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryManuCapacity", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSManuCapacity>> APS_QueryManuCapacityList(BMSEmployee wLoginUser, int wID, int wLineID,
			int wPartID, int wActive) {
		ServiceResult<List<APSManuCapacity>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSManuCapacityDAO.getInstance().SelectList(wLoginUser, wID, wLineID, wPartID, wActive,
					wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryManuCapacityList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Long> APS_UpdateManuCapacity(BMSEmployee wLoginUser, APSManuCapacity wAPSManuCapacity) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long
					.valueOf(APSManuCapacityDAO.getInstance().Update(wLoginUser, wAPSManuCapacity, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateManuCapacity", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_ActiveManuCapacityList(BMSEmployee wLoginUser, List<Integer> wIDList,
			int wActive) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			APSManuCapacityDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_ActiveManuCapacityList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<APSDismantling> APS_QueryDismantling(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSDismantling> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSDismantlingDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryDismantling", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSDismantling>> APS_QueryDismantlingList(BMSEmployee wLoginUser, int wID, int wLineID,
			int wPartID, int wProductID, int wMaterialID) {
		ServiceResult<List<APSDismantling>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSDismantlingDAO.getInstance().SelectList(wLoginUser, wID, wLineID, wPartID, wProductID,
					wMaterialID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryDismantlingList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Long> APS_UpdateDismantling(BMSEmployee wLoginUser, APSDismantling wAPSDismantling) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long
					.valueOf(APSDismantlingDAO.getInstance().Update(wLoginUser, wAPSDismantling, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateDismantling", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<APSPartsLog> APS_QueryPartsLog(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSPartsLog> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSPartsLogDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryPartsLog", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSPartsLog>> APS_QueryPartsLogList(BMSEmployee wLoginUser, int wID, int wProductID,
			String wPartNo) {
		ServiceResult<List<APSPartsLog>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSPartsLogDAO.getInstance().SelectList(wLoginUser, wID, wProductID, wPartNo, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryPartsLogList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Long> APS_UpdatePartsLog(BMSEmployee wLoginUser, APSPartsLog wAPSPartsLog) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long.valueOf(APSPartsLogDAO.getInstance().Update(wLoginUser, wAPSPartsLog, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdatePartsLog", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<APSInstallation> APS_QueryInstallation(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSInstallation> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSInstallationDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryInstallation", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSInstallation>> APS_QueryInstallationList(BMSEmployee wLoginUser, int wID, int wLineID,
			int wPartID, int wMaterialID, int wProductID) {
		ServiceResult<List<APSInstallation>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSInstallationDAO.getInstance().SelectList(wLoginUser, wID, wLineID, wPartID, wMaterialID,
					wProductID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryInstallationList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Long> APS_UpdateInstallation(BMSEmployee wLoginUser, APSInstallation wAPSInstallation) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long
					.valueOf(APSInstallationDAO.getInstance().Update(wLoginUser, wAPSInstallation, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateInstallation", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<APSTaskPart> APS_QueryTaskPart(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSTaskPart> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSTaskPartDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskPart", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSTaskPart>> APS_QueryTaskPartList(BMSEmployee wLoginUser, int wID, int wOrderID,
			int wTaskLineID, int wLineID, int wPartID, int wActive) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			wResult.Result = APSTaskPartDAO.getInstance().SelectList(wLoginUser, wID, wOrderID, wTaskLineID, wLineID,
					wPartID, wActive, -1, null, -1, null, null, wErrorCode);

			// 处理延迟天数
			if (wResult.Result.size() > 0) {
				wResult.Result.forEach(p -> {
					p.EndTime.add(Calendar.DATE, -1);
					Calendar wEDate = (Calendar) p.FinishWorkTime.clone();
					Calendar wSDate = (Calendar) p.EndTime.clone();
					p.DelayHours = getDateDays(wEDate, wSDate);
				});
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskPartList", e.toString() }));
		}
		return wResult;
	}

	/**
	 * 获取间隔天数
	 * 
	 * @param 时间1
	 * @param 时间2
	 * @return
	 */
	private double getDateDays(Calendar wSDate, Calendar wEDate) {
		double wResult = 0;
		try {
			wResult = (double) ((wEDate.getTime().getTime() - wSDate.getTime().getTime() + 1000000)
					/ (3600 * 24 * 1000));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public ServiceResult<Long> APS_UpdateTaskPart(BMSEmployee wLoginUser, APSTaskPart wAPSTaskPart) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long.valueOf(APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateTaskPart", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_ActiveTaskPartList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			APSTaskPartDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_ActiveTaskPartList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<APSTaskLine> APS_QueryTaskLine(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSTaskLine> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSTaskLineDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskLine", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSTaskLine>> APS_QueryTaskLineList(BMSEmployee wLoginUser, int wID, int wOrderID,
			int wWorkShopID, int wLineID, int wActive) {
		ServiceResult<List<APSTaskLine>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSTaskLineDAO.getInstance().SelectList(wLoginUser, wID, wOrderID, wWorkShopID, wLineID,
					wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskLineList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Long> APS_UpdateTaskLine(BMSEmployee wLoginUser, APSTaskLine wAPSTaskLine) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long.valueOf(APSTaskLineDAO.getInstance().Update(wLoginUser, wAPSTaskLine, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateTaskLine", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_ActiveTaskLineList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			APSTaskLineDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_ActiveTaskLineList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<OMSOrder>> APS_SetOrderPriority(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			List<OMSOrderPriority> wOrderPriorityList, List<Integer> wLineOrders, List<Integer> wCustomerOrders,
			List<Integer> wProductIDs) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<>();
		try {
			wResult.Result = APSAutoUtils.getInstance().APS_SetOrderPriority(wLoginUser, wOrderList, wOrderPriorityList,
					wLineOrders, wCustomerOrders, wProductIDs);
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_SetOrderPriority", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSTaskPart>> APS_AutoTaskPart(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			APSShiftPeriod wShiftPeriod, List<APSTaskPart> wOrderPartIssuedList,
			List<OMSOutsourceOrder> wOutsourceOrderList, Calendar wStartTime, Calendar wEndTime,
			Map<Integer, List<FPCRoutePart>> wRoutePartList, List<APSManuCapacity> wManuCapacityList,
			List<APSInstallation> wAPSInstallationList, List<APSDismantling> wAPSDismantlingList,
			List<APSMessage> wMessageList, int wWorkDay, int wLimitMinutes, List<APSTaskPart> wMutualTaskList) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = new ArrayList<>();

			List<FMCTimeZone> wAllZoneList = new ArrayList<>();
			Map<Integer, List<CFGCalendar>> wCalendarMap = new HashMap<>();

			wAllZoneList = APSUtils.getInstance().GetTimeZoneListAndHolidayList(wLoginUser, wWorkDay);

			wCalendarMap = APSUtils.getInstance().GetCalendarMap(wLoginUser);

			wResult.Result = APSAutoUtils.getInstance().APS_AutoTaskPart(wLoginUser, wOrderList, wShiftPeriod,
					wOrderPartIssuedList, wOutsourceOrderList, wStartTime, wEndTime, wRoutePartList, wManuCapacityList,
					wAPSInstallationList, wAPSDismantlingList, wMessageList, wWorkDay, wAllZoneList, wCalendarMap,
					wLimitMinutes, wMutualTaskList, wErrorCode);

			if (wResult.Result != null && (wResult.Result).size() > 0 && wShiftPeriod == APSShiftPeriod.Week) {
				List<APSTaskPart> wRemoveList = new ArrayList<>();
				for (APSTaskPart wAPSTaskPart : wResult.Result) {
					List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1,
							wAPSTaskPart.OrderID, -1, wAPSTaskPart.LineID, wAPSTaskPart.PartID, 1,
							wShiftPeriod.getValue(),
							new ArrayList<>(Arrays.asList(Integer.valueOf(APSTaskStatus.Confirm.getValue()),
									Integer.valueOf(APSTaskStatus.Started.getValue()),
									Integer.valueOf(APSTaskStatus.Done.getValue()))),
							-1, null, null, wErrorCode);
					if (wList != null && wList.size() > 0) {
						wRemoveList.add(wAPSTaskPart);
					}
				}
				wResult.Result.removeIf(p -> wRemoveList.contains(p));
			}

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_AutoTaskPart", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSMessage>> APS_CheckTaskPart(BMSEmployee wLoginUser, List<OMSOrder> wOrderList,
			List<APSTaskPart> wCheckTaskList, APSShiftPeriod wShiftPeriod, List<APSTaskPart> wOrderPartIssuedList,
			List<OMSOutsourceOrder> wOutsourceOrderList, Calendar wStartTime, Calendar wEndTime,
			Map<Integer, List<FPCRoutePart>> wRoutePartList, List<APSManuCapacity> wManuCapacityList,
			List<APSInstallation> wAPSInstallationList, List<APSDismantling> wAPSDismantlingList, int wWorkDay) {
		ServiceResult<List<APSMessage>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			List<FMCTimeZone> wAllZoneList = new ArrayList<>();
			Map<Integer, List<CFGCalendar>> wCalendarMap = new HashMap<>();

			wAllZoneList = APSUtils.getInstance().GetTimeZoneListAndHolidayList(wLoginUser, wWorkDay);

			wCalendarMap = APSUtils.getInstance().GetCalendarMap(wLoginUser);

			wResult.Result = APSManualUtils.getInstance().APS_CheckTaskPart(wLoginUser, wOrderList, wCheckTaskList,
					wShiftPeriod, wOrderPartIssuedList, wOutsourceOrderList, wStartTime, wEndTime, wRoutePartList,
					wManuCapacityList, wAPSInstallationList, wAPSDismantlingList, wWorkDay, wAllZoneList, wCalendarMap,
					wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_CheckTaskPart", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<APSMessage> APS_QueryMessage(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSMessage> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSMessageDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryMessage", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSMessage>> APS_QueryMessageList(BMSEmployee wLoginUser, int wID, int wOrderID) {
		ServiceResult<List<APSMessage>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSMessageDAO.getInstance().SelectList(wLoginUser, wID, wOrderID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryMessageList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Long> APS_UpdateMessage(BMSEmployee wLoginUser, APSMessage wAPSMessage) {
		ServiceResult<Long> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Long.valueOf(APSMessageDAO.getInstance().Update(wLoginUser, wAPSMessage, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateMessage", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSTaskPart>> APS_QueryTaskPartList(BMSEmployee wLoginUser, int wShiftPeriod,
			List<Integer> wStatusIDList, List<Integer> wOrderIDList) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSTaskPartDAO.getInstance().SelectListByOrderIDList(wLoginUser, wShiftPeriod,
					wStatusIDList, wOrderIDList, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskPartList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSTaskStep>> APS_QueryTaskStepByShiftDate(BMSEmployee wLoginUser, Calendar wShiftDate,
			int wWork) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			FMCShiftLevel wShiftLevel = MESServer.MES_QueryShiftLevel(wLoginUser, 0);
			int wShiftID = MESServer.MES_QueryShiftID(0, wShiftDate, APSShiftPeriod.Day, wShiftLevel, 0);

			APIResult wApiResult = LFSServiceImpl.getInstance().LFS_QueryWorkAreaStationList(wLoginUser);
			List<LFSWorkAreaStation> wWorkAreaStationList = wApiResult.List(LFSWorkAreaStation.class);
			List<Integer> wPartIDList = new ArrayList<>();
			for (LFSWorkAreaStation wWorkAreaStation : wWorkAreaStationList) {
				if (wPartIDList.contains(Integer.valueOf(wWorkAreaStation.StationID)))
					continue;
				wPartIDList.add(Integer.valueOf(wWorkAreaStation.StationID));
			}

			List<APSTaskStep> wList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, -1, -1,
					-1, wShiftID, 1, null, null, null, null, wErrorCode);
			if (wList != null && wList.size() > 0) {
				wList = wList.stream().filter(p -> wPartIDList.stream().anyMatch(q -> q == p.PartID))
						.collect(Collectors.toList());
			}
			wResult.Result = wList;
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskStepByShiftDate", e.toString() }));
		}

		return wResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<List<APSTaskStep>> APS_CreateTaskStepByShiftDate(BMSEmployee wLoginUser, Calendar wShiftDate) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>();

			Calendar wShiftEnd = CalendarUtil.GetDate(wShiftDate);
			wShiftEnd.set(Calendar.HOUR_OF_DAY, 23);
			wShiftEnd.set(Calendar.MINUTE, 59);
			wShiftEnd.set(Calendar.SECOND, 59);

			// 班次
			int wShiftID = MESServer.MES_QueryShiftID(0, wShiftDate, APSShiftPeriod.Day, FMCShiftLevel.Day, 0);

			// 查询当天包括之前未完成（下达的,激活的,开始的）的周计划wShiftDate
			List<APSTaskPart> wAPSTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1,
					1, APSShiftPeriod.Week.getValue(),
					new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Issued.getValue(),
							APSTaskStatus.Confirm.getValue(), APSTaskStatus.Started.getValue())),
					-1, null, wShiftEnd, wErrorCode);
			if (wAPSTaskPartList == null || wAPSTaskPartList.size() <= 0)
				return wResult;

			// 将相同订单、相同工位的工位计划剔除
			wAPSTaskPartList = this.RemoveSameTaskPart(wAPSTaskPartList);

			// 获取对应所有已存在的工序任务
			List<APSTaskStep> wAPSTaskStepList = new ArrayList<APSTaskStep>();
			for (APSTaskPart wItem : wAPSTaskPartList) {
				wAPSTaskStepList.addAll(APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, wItem.ID, -1,
						-1, -1, -1, -1, 1, null, null, null, null, wErrorCode));
			}

			// 根据工位对应的所有工序生成所有未做完的工序任务 今天已排的工序任务从已排任务中获取 待排计划状态为0 已排计划中有保存和下达状态
			List<APSTaskStep> wAPSTaskStepUndoList = new ArrayList<APSTaskStep>();
			// 工艺数据
			List<FPCRoutePart> wFPCRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(BaseDAO.SysAdmin, 0).List(FPCRoutePart.class);
			List<FPCRoutePartPoint> wFPCRoutePartPointList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartPointListByRouteID(BaseDAO.SysAdmin, 0, 0).List(FPCRoutePartPoint.class);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);

			List<Integer> wPartPointIDList = null;
			Optional<APSTaskStep> wOption = null;

			// 根据权限返回数据
			// 工区人员
			List<LFSWorkAreaChecker> wAreaCheckerList = APSConstans.GetLFSWorkAreaCheckerList();
			if (wAreaCheckerList != null && wAreaCheckerList.stream().anyMatch(
					p -> p.LeaderIDList != null && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				// 工区工位
				List<LFSWorkAreaStation> wAreaStationList = APSConstans.GetLFSWorkAreaStationList().values().stream()
						.collect(Collectors.toList());
				int wAreaID = wAreaCheckerList.stream().filter(
						p -> p.LeaderIDList != null && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))
						.findFirst().get().WorkAreaID;
				List<LFSWorkAreaStation> wTempList = wAreaStationList.stream().filter(p -> p.WorkAreaID == wAreaID)
						.collect(Collectors.toList());
				if (wTempList.size() > 0) {
					wAPSTaskPartList = wAPSTaskPartList.stream()
							.filter(p -> wTempList.stream().anyMatch(q -> q.StationID == p.PartID))
							.collect(Collectors.toList());
				} else {
					wAPSTaskPartList = new ArrayList<APSTaskPart>();
				}
			} else {
				wAPSTaskPartList = new ArrayList<APSTaskPart>();
			}

			int wRouteID = 0;
			List<Integer> wOrderIDList = wAPSTaskPartList.stream().map(p -> p.OrderID).distinct()
					.collect(Collectors.toList());

			List<OMSOrder> wOMSOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			Map<Integer, OMSOrder> wOMSOrderMap = wOMSOrderList.stream().collect(Collectors.toMap(p -> p.ID, p -> p));
			OMSOrder wOrder = null;
			for (APSTaskPart wItem : wAPSTaskPartList) {
				wOrder = null;
				if (wOMSOrderMap.containsKey(wItem.OrderID)) {
					wOrder = wOMSOrderMap.get(wItem.OrderID);
				}
				if (wOrder != null && wOrder.ID > 0) {
					wRouteID = wOrder.RouteID;
				}

				if (wRouteID <= 0) {
					wResult.FaultCode += StringUtils.Format("提示：【{0}】该订单工艺BOP缺失!", wOrder.OrderNo);
					return wResult;
				}
				// 获取工序ID集合
				wPartPointIDList = APS_GetPartPointIDList(wFPCRoutePartList, wFPCRoutePartPointList, wItem.PartID,
						wRouteID);
				if (wPartPointIDList == null || wPartPointIDList.size() <= 0) {
					continue;
				}

				for (int wPartPointID : wPartPointIDList) {
					wOption = wAPSTaskStepList.stream()
							.filter(p -> p.TaskPartID == wItem.ID && p.StepID == wPartPointID).findFirst();
					if (wOption.isPresent()) {
						wAPSTaskStepUndoList.add(wOption.get());
					} else {
						APSTaskStep wAPSTaskStep = new APSTaskStep();

						wAPSTaskStep.Active = 1;
						wAPSTaskStep.ID = 0;
						wAPSTaskStep.LineID = wItem.LineID;
						wAPSTaskStep.MaterialNo = wItem.MaterialNo;
						wAPSTaskStep.OrderID = wItem.OrderID;
						wAPSTaskStep.PartID = wItem.PartID;
						wAPSTaskStep.PartNo = wItem.PartNo;
						wAPSTaskStep.PlanerID = wLoginUser.ID;
						wAPSTaskStep.ProductNo = wItem.ProductNo;
						wAPSTaskStep.ShiftID = wShiftID;
						wAPSTaskStep.Status = APSTaskStatus.Saved.getValue();
						wAPSTaskStep.StepID = wPartPointID;
						wAPSTaskStep.TaskPartID = wItem.ID;
						wAPSTaskStep.TaskLineID = wItem.TaskLineID;
						wAPSTaskStep.TaskText = wItem.TaskText;
						wAPSTaskStep.WorkHour = 0;
						wAPSTaskStep.WorkShopID = wItem.WorkShopID;
						wAPSTaskStep.StartTime = wBaseTime;
						wAPSTaskStep.EndTime = wBaseTime;
						wAPSTaskStep.ReadyTime = wBaseTime;
						wAPSTaskStep.CreateTime = Calendar.getInstance();

						wAPSTaskStep.ID = APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);

						wAPSTaskStepUndoList.add(wAPSTaskStep);
					}
				}
			}

			ServiceResult<List<OMSOrder>> wOrderResult = this.GetOrderListAndTaskPartListByTaskStepList(wLoginUser,
					wAPSTaskStepUndoList, wFPCRoutePartList, wFPCRoutePartPointList);
			wResult.CustomResult.put("OrderList", wOrderResult.Result);
			wResult.Result = wAPSTaskStepUndoList;

			// 处理制定的数据，只要未排的
			List<APSTaskPartDetails> wToDoList = new ArrayList<APSTaskPartDetails>();
			List<APSTaskPartDetails> wList = (List<APSTaskPartDetails>) wOrderResult.CustomResult.get("TaskPartList");
			for (APSTaskPartDetails wAPSTaskPartDetails : wList) {
				if (wResult.Result.stream().anyMatch(p -> p.TaskPartID == wAPSTaskPartDetails.APSTaskPart.ID
						&& p.Status == APSTaskStatus.Saved.getValue())) {
					wToDoList.add(wAPSTaskPartDetails);
				}
			}

			wResult.CustomResult.put("TaskPartList", wToDoList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception ex) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_CreateTaskStepByShiftDate",
					ex.toString()));
		}
		return wResult;
	}

	/**
	 * 将相同订单、相同工位的工位计划去重
	 * 
	 * @param wAPSTaskPartList
	 */
	private List<APSTaskPart> RemoveSameTaskPart(List<APSTaskPart> wAPSTaskPartList) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			if (wAPSTaskPartList == null || wAPSTaskPartList.size() <= 0) {
				return wResult;
			}

			List<APSTaskPart> wList = null;
			Optional<APSTaskPart> wOption = null;
			for (APSTaskPart wAPSTaskPart : wAPSTaskPartList) {
				wList = wAPSTaskPartList.stream()
						.filter(p -> p.OrderID == wAPSTaskPart.OrderID && p.PartID == wAPSTaskPart.PartID)
						.collect(Collectors.toList());
				if (wList != null && wList.size() == 1) {
					wResult.add(wAPSTaskPart);
				} else if (wList != null && wList.size() > 1) {
					wOption = wList.stream().max((a, b) -> a.SubmitTime.compareTo(b.SubmitTime));
					if (wOption.isPresent()) {
						wResult.add(wOption.get());
					}
				}
			}
			// 去重
			wResult = new ArrayList<APSTaskPart>(wResult.stream()
					.collect(Collectors.toMap(APSTaskPart::getID, account -> account, (k1, k2) -> k2)).values());
		} catch (Exception ex) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "RemoveSameTaskPart", ex.toString()));
		}
		return wResult;
	}

	/**
	 * 根据修程、车型、工位获取工序ID集合
	 * 
	 * @param wFPCProductRouteList   产品工艺列表
	 * @param wFPCRoutePartList      工位工艺列表
	 * @param wFPCRoutePartPointList 工序工艺列表
	 * @param wPartID                工位ID
	 * @param wLineName              修程
	 * @param wProductNo             产品型号
	 * @return 工序ID集合
	 */
	private List<Integer> APS_GetPartPointIDList(List<FPCRoutePart> wFPCRoutePartList,
			List<FPCRoutePartPoint> wFPCRoutePartPointList, int wPartID, int wRouteID) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {

			Optional<FPCRoutePart> wPartOption = wFPCRoutePartList.stream()
					.filter(p -> p.RouteID == wRouteID && p.PartID == wPartID).findFirst();
			if (!wPartOption.isPresent()) {
				return wResult;
			}

			FPCRoutePart wFPCRoutePart = wPartOption.get();

			List<FPCRoutePartPoint> wList = wFPCRoutePartPointList.stream()
					.filter(p -> p.PartID == wFPCRoutePart.PartID && p.RouteID == wRouteID)
					.collect(Collectors.toList());

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			wResult = wList.stream().map(p -> p.PartPointID).collect(Collectors.toList());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<Map<String, Object>>> APS_QueryTableInfoList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wAPSShiftPeriod) {
		ServiceResult<List<Map<String, Object>>> wResult = new ServiceResult<List<Map<String, Object>>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// 生成shiftID
			FMCShiftLevel wShiftLevel = MESServer.MES_QueryShiftLevel(wLoginUser, 0);
			int wShiftID = MESServer.MES_QueryShiftID(0, wStartTime, APSShiftPeriod.getEnumType(wAPSShiftPeriod),
					wShiftLevel, 0);
			// 工区工位列表
			APIResult wApiResult = LFSServiceImpl.getInstance().LFS_QueryWorkAreaStationList(wLoginUser);
			List<LFSWorkAreaStation> wWorkAreaStationList = wApiResult.List(LFSWorkAreaStation.class);
			// 根据ShiftID查询任务
			List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectListByShiftID(wLoginUser, wShiftID,
					wErrorCode);
			// 获取最大的SubmitTime
			Calendar wMaxSubmitTime = wList.stream().max(Comparator.comparing(APSTaskPart::getSubmitTime))
					.get().SubmitTime;
			// 月计划任务集合
			wList = wList.stream().filter(p -> p.SubmitTime.compareTo(wMaxSubmitTime) == 0)
					.collect(Collectors.toList());
			// 月订单集合
			List<OMSOrder> wOrderList = OMSServiceImpl.getInstance().OMS_QueryHistoryOrderList(wLoginUser, wStartTime,
					wEndTime, APSShiftPeriod.Week.getValue()).Result;
			// 生成表头数据
			List<FPCPart> wPartList = CoreServiceImpl.getInstance().FPC_QueryPartList(wLoginUser).List(FPCPart.class);
			int wFlag = 0;
			Map<String, Object> wHeadMap = new HashMap<String, Object>();
			wHeadMap.put(String.valueOf(wFlag++), "序号");
			wHeadMap.put(String.valueOf(wFlag++), "车型");
			wHeadMap.put(String.valueOf(wFlag++), "车号");
			wHeadMap.put(String.valueOf(wFlag++), "配属");
			wHeadMap.put(String.valueOf(wFlag++), "修程");
			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wHeadMap.put(String.valueOf(wFlag++), wOption.get().Name);
				}
			}
			wHeadMap.put(String.valueOf(wFlag++), "停时");
			wHeadMap.put(String.valueOf(wFlag++), "备注");
			wResult.Result.add(wHeadMap);
			// 获取表格数据
			wResult.Result.addAll(ChangeToTable(wOrderList, wList, wWorkAreaStationList).Result);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(
					StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_QueryTableInfoList", e.toString()));
		}
		return wResult;
	}

	/**
	 * 
	 * @param wOrderList       月度订单
	 * @param wAPSTaskPartList 月计划列表
	 * @param wStationList     工区工位列表（gantt中左边显示的工位列表）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<List<Map<String, Object>>> ChangeToTable(List<OMSOrder> wOrderList,
			List<APSTaskPart> wAPSTaskPartList, List<LFSWorkAreaStation> wStationList) {
		ServiceResult<List<Map<String, Object>>> wResult = new ServiceResult<List<Map<String, Object>>>();
		try {
			if (wOrderList == null || wAPSTaskPartList == null || wStationList == null) {
				wResult.FaultCode += "参数不合法 转换Table失败！";
				return wResult;
			}
			wResult.Result = new ArrayList<Map<String, Object>>();
			// 排序
			wStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			// 查询月计划
			Map<Integer, List<APSTaskPart>> wAPSTaskPartMap = wAPSTaskPartList.stream()
					.collect(Collectors.groupingBy(APSTaskPart::getOrderID));
			Map<Integer, APSTaskPart> wTaskMap = null;
			Map<String, Object> wMap = null;
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			for (OMSOrder wOMSOrder : wOrderList) {
				wMap = CloneTool.Clone(wOMSOrder, Map.class);
				if (wAPSTaskPartMap.containsKey(wOMSOrder.ID)) {
					wTaskMap = wAPSTaskPartMap.get(wOMSOrder.ID).stream()
							.collect(Collectors.toMap(p -> p.PartID, p -> p, (k1, k2) -> k1));
				} else {
					wTaskMap = new HashMap<Integer, APSTaskPart>();
				}

				for (LFSWorkAreaStation wStation : wStationList) {

					if (!wTaskMap.containsKey(wStation.StationID)) {
						wMap.put("Station_" + wStation.StationID, wBaseTime);
						wMap.put("Status_" + wStation.StationID, APSTaskStatus.Default.getValue());
						continue;
					}

					APSTaskPart apsTaskPart = wTaskMap.get(wStation.StationID);

					if (apsTaskPart.Status == 5) {
						wMap.put("Status_" + wStation.StationID, 5);
					} else {
						SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
						int wRShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
						Calendar wCloneEndTime = (Calendar) apsTaskPart.EndTime.clone();
						wCloneEndTime.add(Calendar.HOUR_OF_DAY, -12);

						int wPShiftID = Integer.parseInt(wSDF.format(wCloneEndTime.getTime()));
						int wSShiftID = Integer.parseInt(wSDF.format(apsTaskPart.StartTime.getTime()));

						if (apsTaskPart.Status == 2) {
							wMap.put("Status_" + wStation.StationID, 2);
						} else {
							if (wRShiftID > wPShiftID) {
								wMap.put("Status_" + wStation.StationID, 6);
							} else {
								if (apsTaskPart.Status == 2 && wRShiftID >= wSShiftID) {
									wMap.put("Status_" + wStation.StationID, 4);
								} else {
									wMap.put("Status_" + wStation.StationID, apsTaskPart.getStatus());
								}
							}
						}
					}

					if (apsTaskPart.getStatus() == APSTaskStatus.Done.getValue()) {
						wMap.put("Station_" + wStation.StationID, apsTaskPart.FinishWorkTime);
						continue;
					}

//					if (apsTaskPart.getStatus() == APSTaskStatus.Started.getValue()) {
//						wMap.put("Station_" + wStation.StationID, apsTaskPart.StartWorkTime);
//						continue;
//					}

					wMap.put("Station_" + wStation.StationID, apsTaskPart.EndTime);
				}
				wResult.Result.add(wMap);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@SuppressWarnings("unchecked")
	public ServiceResult<List<Map<String, Object>>> ChangeToTableByEndTime(List<OMSOrder> wOrderList,
			List<APSTaskPart> wAPSTaskPartList, List<LFSWorkAreaStation> wStationList) {
		ServiceResult<List<Map<String, Object>>> wResult = new ServiceResult<>();
		try {
			wResult.Result = new ArrayList<>();

			wStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));

			Map<Integer, List<APSTaskPart>> wAPSTaskPartMap = (Map<Integer, List<APSTaskPart>>) wAPSTaskPartList
					.stream().collect(Collectors.groupingBy(APSTaskPart::getOrderID));
			Map<Integer, APSTaskPart> wTaskMap = null;
			Map<String, Object> wMap = null;
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 1, 1);
			for (OMSOrder wOMSOrder : wOrderList) {
				wMap = CloneTool.Clone(wOMSOrder, Map.class);
				if (wAPSTaskPartMap.containsKey(Integer.valueOf(wOMSOrder.ID))) {
					wTaskMap = (Map<Integer, APSTaskPart>) (wAPSTaskPartMap.get(Integer.valueOf(wOMSOrder.ID))).stream()
							.collect(Collectors.toMap(p -> Integer.valueOf(p.PartID), p -> p, (k1, k2) -> k1));
				} else {
					wTaskMap = new HashMap<>();
				}

				for (LFSWorkAreaStation wStation : wStationList) {

					if (wTaskMap.containsKey(Integer.valueOf(wStation.StationID))) {
						APSTaskPart wTaskPart = (APSTaskPart) wTaskMap.get(Integer.valueOf(wStation.StationID));
						if (wTaskPart.Status == 5) {
							wMap.put("Station_" + wStation.StationID, wTaskPart.FinishWorkTime);
						} else {
							wMap.put("Station_" + wStation.StationID, wTaskPart.EndTime);
						}
					} else {
						wMap.put("Station_" + wStation.StationID, wBaseTime);
					}

					// 赋值状态
					if (wTaskMap.containsKey(wStation.StationID)) {
						APSTaskPart apsTaskPart = wTaskMap.get(wStation.StationID);

						if (apsTaskPart.Status == 5) {
							wMap.put("Status_" + wStation.StationID, 5);
						} else {
							SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
							int wRShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));

							Calendar wTime = (Calendar) apsTaskPart.EndTime.clone();
							wTime.add(Calendar.HOUR_OF_DAY, -12);

							int wPShiftID = Integer.parseInt(wSDF.format(wTime.getTime()));
							int wSShiftID = Integer.parseInt(wSDF.format(apsTaskPart.StartTime.getTime()));

							if (apsTaskPart.Status == 2) {
								wMap.put("Status_" + wStation.StationID, 2);
							} else {
								if (wRShiftID > wPShiftID) {
									wMap.put("Status_" + wStation.StationID, 6);
								} else {
									if (apsTaskPart.Status == 2 && wRShiftID >= wSShiftID) {
										wMap.put("Status_" + wStation.StationID, 4);
									} else {
										wMap.put("Status_" + wStation.StationID, apsTaskPart.getStatus());
									}
								}

							}
						}
					} else {
						wMap.put("Status_" + wStation.StationID, 0);
					}
				}
				((List<Map<String, Object>>) wResult.Result).add(wMap);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public ServiceResult<APSTaskStep> APS_QueryTaskStep(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSTaskStep> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSTaskStepDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskStep", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSTaskStep>> APS_QueryTaskStepList(BMSEmployee wLoginUser, int wID, int wOrderID,
			int wTaskLineID, int wTaskPartID, int wWorkShopID, int wLineID, int wPartID, int wStepID, int wShiftID,
			int wActive, List<Integer> wStateIDList) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSTaskStepDAO.getInstance().SelectList(wLoginUser, wID, wOrderID, wTaskLineID,
					wTaskPartID, wWorkShopID, wLineID, wPartID, wStepID, wShiftID, wActive, wStateIDList, null, null,
					null, wErrorCode);

			// 查询工时
			List<APSTaskStep> wUpdateList = new ArrayList<APSTaskStep>();

			Map<Integer, OMSOrder> wOrderMap = new HashMap<Integer, OMSOrder>();
			if (wResult.Result.size() > 0) {
				List<Integer> wOrderIDList = wResult.Result.stream().filter(p -> p.WorkHour > 0 && p.OrderID > 0)
						.map(p -> p.OrderID).distinct().collect(Collectors.toList());

				if (wOrderIDList.size() > 0) {
					wOrderMap = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode)
							.stream().collect(Collectors.toMap(p -> p.ID, p -> p));
				}

			}

			for (APSTaskStep wAPSTaskStep : wResult.Result) {
				if (wAPSTaskStep.WorkHour > 0) {
					continue;
				}

				if (!wOrderMap.containsKey(wAPSTaskStep.OrderID))
					continue;
				OMSOrder wOrder = wOrderMap.get(wAPSTaskStep.OrderID);
				if (wOrder == null || wOrder.ID <= 0)
					continue;

				double wWorkHour = APSTaskStepDAO.getInstance().GetActualPeriod(wLoginUser, wOrder.RouteID,
						wAPSTaskStep.PartID, wAPSTaskStep.StepID, wErrorCode);
				if (wWorkHour > 0) {
					wAPSTaskStep.WorkHour = wWorkHour;
					wUpdateList.add(wAPSTaskStep);
				}

			}
			// 异步更新
			for (APSTaskStep wAPSTaskStep : wUpdateList) {
				APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
			}

			wResult.Result.removeIf(p -> p.Active == 2);

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskStepList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_UpdateTaskStep(BMSEmployee wLoginUser, APSTaskStep wAPSTaskStep) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Integer.valueOf(APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateTaskStep", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_ActiveTaskStepList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			APSTaskStepDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_ActiveTaskStepList", e.toString() }));
		}
		return wResult;
	}

	/**
	 * 查询历史排程结果
	 */
	@Override
	public ServiceResult<List<APSTaskPart>> APS_QueryHistoryTaskPartList(BMSEmployee wLoginUser, int wAPSShiftPeriod,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			FMCShiftLevel wShiftLevel = MESServer.MES_QueryShiftLevel(wLoginUser, 0);
			int wShiftID = MESServer.MES_QueryShiftID(0, wStartTime, APSShiftPeriod.getEnumType(wAPSShiftPeriod),
					wShiftLevel, 0);
			List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, 1,
					wAPSShiftPeriod, null, wShiftID, null, null, wErrorCode);
			if (wList == null || wList.size() <= 0)
				return wResult;
			APSTaskPart wMaxItem = wList.stream().max(Comparator.comparing(APSTaskPart::getSubmitTime)).get();
			Calendar wMaxSubmitTime = wMaxItem.SubmitTime;
			wList = wList.stream().filter(p -> p.SubmitTime.compareTo(wMaxSubmitTime) == 0)
					.collect(Collectors.toList());
			wResult.Result = wList;
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_QueryHistoryTaskPartList",
					e.toString()));
		}
		return wResult;
	}

	public ServiceResult<List<APSMessage>> APS_QueryMessageListBySubmitTime(BMSEmployee wLoginUser,
			Calendar wSubmitTime) {
		ServiceResult<List<APSMessage>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSMessageDAO.getInstance().SelectListBySubmitTime(wLoginUser, wSubmitTime, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryMessageListBySubmitTime", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<APSCondition> APS_QueryCondition(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSCondition> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSConditionDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryCondition", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSCondition>> APS_QueryConditionList(BMSEmployee wLoginUser, int wID, int wShiftPeriod) {
		ServiceResult<List<APSCondition>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSConditionDAO.getInstance().SelectList(wLoginUser, wID, wShiftPeriod, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryConditionList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_UpdateCondition(BMSEmployee wLoginUser, APSCondition wAPSCondition) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Integer
					.valueOf(APSConditionDAO.getInstance().Update(wLoginUser, wAPSCondition, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateCondition", e.toString() }));
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryTaskStepListByMonitorID(BMSEmployee wLoginUser, Calendar wTaskDate,
			int wMonitorID) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①将wTaskDate转换为一天的开始时间和结束时间
			Calendar wStartTime = (Calendar) wTaskDate.clone();
			wStartTime.set(Calendar.HOUR_OF_DAY, 0);
			wStartTime.set(Calendar.MINUTE, 0);
			wStartTime.set(Calendar.SECOND, 0);
			Calendar wEndTime = (Calendar) wTaskDate.clone();
			wEndTime.set(Calendar.HOUR_OF_DAY, 23);
			wEndTime.set(Calendar.MINUTE, 59);
			wEndTime.set(Calendar.SECOND, 59);
			// ②获取登录者的工位列表
			List<Integer> wPartIDList = null;
			if (CoreServiceImpl.getInstance()
					.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 501002, 0, 0)
					.Info(Boolean.class)) {
				wPartIDList = new ArrayList<Integer>();
			} else {
				// 工位ID集合
				wPartIDList = this.GetPartIDListByLoginUser(wLoginUser);
				if (wPartIDList == null || wPartIDList.size() <= 0) {
					return wResult;
				}
			}
			// ③通过时间段和工位列表查询工位任务集合
			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByTime(wLoginUser, wPartIDList,
					wStartTime, wEndTime, wErrorCode);
			// ④通过工位任务集合查询工序任务集合
			if (wTaskPartList.size() > 0) {
				wResult.Result = APSTaskStepDAO.getInstance().SelectListByTaskPartIDList(wLoginUser,
						wTaskPartList.stream().map(p -> p.ID).collect(Collectors.toList()), wErrorCode);
			}

			wResult.Result.removeIf(p -> p.Status == APSTaskStatus.Saved.getValue());

			// 终检任务、出厂检任务(未完成的，今日完成的)
			wResult.Result.addAll(APSTaskStepDAO.getInstance().SelectQTList(wLoginUser, null, null,
					new ArrayList<Integer>(
							Arrays.asList(APSTaskStatus.Issued.getValue(), APSTaskStatus.Started.getValue())),
					wPartIDList, wErrorCode));
			wResult.Result.addAll(APSTaskStepDAO.getInstance().SelectQTList(wLoginUser, wStartTime, wEndTime,
					new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Done.getValue())), wPartIDList, wErrorCode));
			// 去重
			wResult.Result = new ArrayList<APSTaskStep>(wResult.Result.stream()
					.collect(Collectors.toMap(APSTaskStep::getID, account -> account, (k1, k2) -> k2)).values());

			if (wResult.Result.size() <= 0) {
				return wResult;
			}

			/**
			 * 默认人员返回(没派工记录时默认返回)
			 */
			Map<Integer, List<Integer>> wPersonMap = SFCServiceImpl.getInstance().SFC_QueryStepPersonMap(wLoginUser,
					wResult.Result).Result;

			List<Integer> wAPSTaskstepIDList = wResult.Result.stream().map(p -> p.ID).collect(Collectors.toList());
			List<SFCTaskStep> wList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser,
					wAPSTaskstepIDList, SFCTaskStepType.Step.getValue(), wErrorCode);

			for (APSTaskStep wAPSTaskStep : wResult.Result) {
				List<SFCTaskStep> wTempList = wList.stream().filter(p -> p.TaskStepID == wAPSTaskStep.ID)
						.collect(Collectors.toList());

				if (wAPSTaskStep.OperatorList != null && wAPSTaskStep.OperatorList.size() > 0) {
					wAPSTaskStep.Operators = GetUserNames(wAPSTaskStep.OperatorList);
					continue;
				}

				if (wTempList != null && wTempList.size() > 0) {
					wAPSTaskStep.OperatorList = wTempList.stream().map(p -> p.OperatorID).collect(Collectors.toList());
					continue;
				}

				if (!wPersonMap.containsKey(wAPSTaskStep.ID)) {
					continue;
				}
				wAPSTaskStep.OperatorList = wPersonMap.get(wAPSTaskStep.ID);
				wAPSTaskStep.Operators = GetUserNames(wPersonMap.get(wAPSTaskStep.ID));
			}

			// ①获取工序中所有不在此班组的人员列表
			int wMonitorClassID = APSConstans.GetBMSEmployee(wMonitorID).DepartmentID;
			List<Integer> wNotInClassPersonIDList = new ArrayList<Integer>();
			for (APSTaskStep wAPSTaskStep : wResult.Result) {
				if (wAPSTaskStep.IsDispatched) {
					continue;
				}
				for (Integer wPersonID : wAPSTaskStep.OperatorList) {
					if (APSConstans.GetBMSEmployee(wPersonID).DepartmentID != wMonitorClassID) {
						if (!wNotInClassPersonIDList.stream().anyMatch(p -> p == wPersonID)) {
							wNotInClassPersonIDList.add(wPersonID);
						}
					}
				}
			}
			// ②根据这些人员获取借调单列表
			List<SCHSecondmentApply> wSecondList = new ArrayList<SCHSecondmentApply>();
			if (wNotInClassPersonIDList.size() > 0) {
				wSecondList = SCHSecondmentApplyDAO.getInstance().SelectListByPersonIDList(wLoginUser,
						wNotInClassPersonIDList, wErrorCode);
			}

			// ③判断此人是否有借调到本班组且未到期的借调单
			List<Integer> wNeedRemovePersonIDList = new ArrayList<Integer>();
			for (Integer wPersonID : wNotInClassPersonIDList) {
				// ④若没有，加到需要移除的集合里
				if (!wSecondList.stream()
						.anyMatch(p -> p.PersonID.contains(String.valueOf(wPersonID)) && p.NewClassID == wMonitorClassID
								&& Calendar.getInstance().compareTo(p.StartTime) >= 0
								&& Calendar.getInstance().compareTo(p.EndTime) <= 0)) {
					wNeedRemovePersonIDList.add(wPersonID);
				}
			}
			// ⑤遍历工序列表，移除，未派工且在移除集合里的人
			for (APSTaskStep wAPSTaskStep : wResult.Result) {
				if (wAPSTaskStep.IsDispatched) {
					continue;
				}

				if (wAPSTaskStep.OperatorList == null || wAPSTaskStep.OperatorList.size() <= 0) {
					continue;
				}

				wAPSTaskStep.OperatorList.removeIf(p -> wNeedRemovePersonIDList.stream().anyMatch(q -> q == p));
				wAPSTaskStep.Operators = GetUserNames(wAPSTaskStep.OperatorList);
			}
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_QueryTaskStepListByMonitorID",
					e.toString()));
		}
		return wResult;
	}

	private String GetUserNames(List<Integer> wList) {
		String wResult = "";
		try {
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			List<String> wNameList = new ArrayList<>();

			for (Integer wUserID : wList) {
				String wTemp = APSConstans.GetBMSEmployeeName(wUserID.intValue());
				if (StringUtils.isNotEmpty(wTemp)) {
					wNameList.add(wTemp);
				}
			}

			if (wNameList.size() > 0) {
				wResult = StringUtils.Join(",", wNameList);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取登录者所管辖的工位列表
	 * 
	 * @param wLoginUser
	 * @return
	 */
	private List<Integer> GetPartIDListByLoginUser(BMSEmployee wLoginUser) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			// 判断此人是否是班组长
			if (APSConstans.GetBMSDepartment(wLoginUser.DepartmentID).Type != BMSDepartmentType.Class.getValue()
					|| APSConstans.GetBMSPosition(wLoginUser.Position).DutyID != 1) {
				return wResult;
			}

			int wClassID = wLoginUser.DepartmentID;

			// 班组工位列表
			List<FMCWorkCharge> wList1 = CoreServiceImpl.getInstance().FMC_QueryWorkChargeList(wLoginUser)
					.List(FMCWorkCharge.class);
			if (wList1 == null) {
				return wResult;
			}

			wList1 = wList1.stream().filter(p -> p.ClassID == wClassID && p.Active == 1).collect(Collectors.toList());
			if (wList1 == null) {
				return wResult;
			}

			wResult = wList1.stream().map(p -> p.StationID).collect(Collectors.toList());
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSSchedulingVersion>> APS_QueryAuditTaskList(BMSEmployee wLoginUser, int wAPSShiftPeriod,
			Calendar wShiftDate, int wTagType) {
		ServiceResult<List<APSSchedulingVersion>> wResult = new ServiceResult<List<APSSchedulingVersion>>();
		wResult.Result = new ArrayList<APSSchedulingVersion>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSSchedulingVersion> wSchedulingList = APSSchedulingVersionDAO.getInstance().SelectList(wLoginUser,
					-1, wAPSShiftPeriod, null,
					new ArrayList<Integer>(
							Arrays.asList(APSTaskStatus.Saved.getValue(), APSTaskStatus.Submit.getValue(),
									APSTaskStatus.ToAudit.getValue(), APSTaskStatus.Audited.getValue())),
					wErrorCode);

			int wModuleID = 0;
			switch (APSShiftPeriod.getEnumType(wAPSShiftPeriod)) {
			case Month:
				wModuleID = BPMEventModule.SCMonthAudit.getValue();
				break;
			case Week:
				wModuleID = BPMEventModule.SCWeekAudit.getValue();
				break;
			default:
				break;
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wShiftDate, APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			List<BFCMessage> wMessageList = null;
			switch (TagTypes.getEnumType(wTagType)) {
			case Dispatcher:// 待办
				// 待提交的任务列表
				if (CoreServiceImpl.getInstance().BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID,
						MBSRoleTree.PlanSubmit.getValue(), 0, 0).Info(Boolean.class)) {
					wResult.Result.addAll(wSchedulingList.stream()
							.filter(p -> p.Status == APSTaskStatus.Saved.getValue()).collect(Collectors.toList()));
				}
				// 若是已提交的任务，则根据当前登录者的权限和审批配置判断该任务此人是否有权限拿
				int wModuleTempID = wModuleID;
				for (APSSchedulingVersion wAPSSchedulingVersion : wSchedulingList) {
					wMessageList = CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.ID,
							wModuleID, BFCMessageType.Task.getValue(), 0, wShiftID, null, null).List(BFCMessage.class);
					if (wMessageList != null && wMessageList.size() > 0
							&& wMessageList.stream().anyMatch(p -> p.Active == 0 && p.ModuleID == wModuleTempID
									&& p.MessageID == wAPSSchedulingVersion.ID)) {
						wResult.Result.add(wAPSSchedulingVersion);
					}
				}
				break;
			case Applicant:// 已办
				List<BFCAuditAction> wActionList = null;
				for (APSSchedulingVersion wAPSSchedulingVersion : wSchedulingList) {
					wActionList = CoreServiceImpl.getInstance()
							.BFC_ActionAll(wLoginUser, wModuleID, wAPSSchedulingVersion.ID).List(BFCAuditAction.class);
					if (wActionList != null && wActionList.size() > 0
							&& wActionList.stream().anyMatch(p -> p.AuditorID == wLoginUser.ID)) {
						wResult.Result.add(wAPSSchedulingVersion);
					}
				}
				break;
			default:
				break;
			}
			// 去重
			if (wResult.Result != null && wResult.Result.size() > 0) {
				wResult.Result = new ArrayList<APSSchedulingVersion>(wResult.Result.stream()
						.collect(Collectors.toMap(APSSchedulingVersion::getID, account -> account, (k1, k2) -> k2))
						.values());
				for (APSSchedulingVersion wAPSSchedulingVersion : wResult.Result) {
					wAPSSchedulingVersion.APSTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
							wAPSSchedulingVersion.TaskPartIDList, wErrorCode);
				}
			}
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(
					StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_QueryAuditTaskList", e.toString()));
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<Integer> APS_AuditOperate(BMSEmployee wLoginUser,
			APSSchedulingVersion wAPSSchedulingVersion, int wOperateType, int wAPSShiftPeriod) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			if (wLoginUser.getID() < 0 && wLoginUser.getID() != BaseDAO.SysAdmin.ID) {
				wResult.FaultCode += "用户不存在";
				return wResult;
			}

			switch (APSOperateType.getEnumType(wOperateType)) {
			case Submit:// 提交
			case Audit:// 审批
			case Reject:// 驳回
				// 更新任务状态
				APSSchedulingVersionDAO.getInstance().Audit(wLoginUser, wAPSSchedulingVersion, wOperateType,
						wErrorCode);
				if (wErrorCode.Result != 0) {
					wResult.FaultCode += MESException.getEnumType(wErrorCode.Result).getLable();
					return wResult;
				}
				APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSSchedulingVersion.TaskPartIDList,
						wAPSSchedulingVersion.Status, wErrorCode);
				break;
			case Issued:// 下达
				int wFunctionID = 0;
				switch (APSShiftPeriod.getEnumType(wAPSShiftPeriod)) {
				case Month:
					wFunctionID = 82000001;
					break;
				case Week:
					wFunctionID = 82000002;
					break;
				default:
					break;
				}
				// 【周排程】权限控制
				if (!CoreServiceImpl.getInstance()
						.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, wFunctionID, 0, 0)
						.Info(Boolean.class)) {
					wResult.FaultCode += RetCode.SERVER_CODE_UNROLE;
					return wResult;
				}
				if (wAPSSchedulingVersion.APSTaskPartList.stream()
						.anyMatch(p -> p.Status != APSTaskStatus.Audited.getValue())) {
					wResult.FaultCode = StringUtils.Format("提示：无法下达状态为【{0}】的周计划",
							APSTaskStatus.getEnumType(wAPSSchedulingVersion.APSTaskPartList.get(0).Status).getLable());
					return wResult;
				}

				wAPSSchedulingVersion.Status = APSTaskStatus.Issued.getValue();
				APSSchedulingVersionDAO.getInstance().Update(wLoginUser, wAPSSchedulingVersion, wErrorCode);
				APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSSchedulingVersion.TaskPartIDList,
						wAPSSchedulingVersion.Status, wErrorCode);

				if (wAPSShiftPeriod == APSShiftPeriod.Week.getValue()) {
					ExecutorService wES = Executors.newFixedThreadPool(1);
					wES.submit(() -> APS_TriggerDayPlans(wLoginUser, wAPSSchedulingVersion));
					wES.shutdown();
					// 触发线程日计划
//					APSConstans.mAPSSchedulingVersion = wAPSSchedulingVersion;
				}
				break;
			default:
				break;
			}
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode = e.toString();
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_AuditOperate", e.toString()));
		}
		return wResult;
	}

	/**
	 * 制定所有的日计划
	 */
	private void MakingAllTaskStep(BMSEmployee wLoginUser, APSSchedulingVersion wAPSSchedulingVersion) {
		try {
			if (wAPSSchedulingVersion == null || wAPSSchedulingVersion.APSTaskPartList == null
					|| wAPSSchedulingVersion.APSTaskPartList.size() <= 0) {
				return;
			}

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①获取所有工位任务
			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
					wAPSSchedulingVersion.TaskPartIDList, wErrorCode);
			if (wTaskPartList == null || wTaskPartList.size() <= 0) {
				return;
			}
			// ②获取所有订单
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser,
					wTaskPartList.stream().map(p -> p.OrderID).distinct().collect(Collectors.toList()), wErrorCode);
			if (wOrderList == null || wOrderList.size() <= 0) {
				return;
			}
			// ①根据订单ID集合获取工序计划集合
			List<APSTaskStepPlan> wStepPlanList = APSTaskStepPlanDAO.getInstance().SelectList(wLoginUser,
					wOrderList.stream().map(p -> p.ID).distinct().collect(Collectors.toList()), wErrorCode);
			// ③遍历工位任务，获取工艺工序数据
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			for (APSTaskPart wAPSTaskPart : wTaskPartList) {
				if (!wOrderList.stream().anyMatch(p -> p.ID == wAPSTaskPart.OrderID)) {
					continue;
				}

				OMSOrder wOrder = wOrderList.stream().filter(p -> p.ID == wAPSTaskPart.OrderID).findFirst().get();

				List<FPCRoutePartPoint> wRoutePartPointList = FMCServiceImpl.getInstance()
						.FPC_QueryRoutePartPointListByRouteID(wLoginUser, wOrder.RouteID, wAPSTaskPart.PartID)
						.List(FPCRoutePartPoint.class);

				if (wRoutePartPointList == null || wRoutePartPointList.size() <= 0) {
					continue;
				}

				// ④遍历工艺工序数据，判断是否存在日计划，若存在修改，否则新增
				APSTaskStep wAPSTaskStep = null;
				for (FPCRoutePartPoint wFPCRoutePartPoint : wRoutePartPointList) {
					List<APSTaskStep> wList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1, -1,
							-1, -1, wAPSTaskPart.PartID, wFPCRoutePartPoint.PartPointID, -1, -1, null, null, null, null,
							wErrorCode);
					if (wList != null && wList.size() > 0) {
						wAPSTaskStep = wList.get(0);
						wAPSTaskStep.TaskPartID = wAPSTaskPart.ID;
						if (wStepPlanList.stream().anyMatch(p -> p.OrderID == wAPSTaskPart.OrderID
								&& p.PartID == wAPSTaskPart.PartID && p.StepID == wFPCRoutePartPoint.PartPointID)) {
							APSTaskStepPlan wStepPlan = wStepPlanList.stream()
									.filter(p -> p.OrderID == wAPSTaskPart.OrderID && p.PartID == wAPSTaskPart.PartID
											&& p.StepID == wFPCRoutePartPoint.PartPointID)
									.findFirst().get();
							wAPSTaskStep.PlanStartTime = wStepPlan.StartTime;
							wAPSTaskStep.PlanEndTime = wStepPlan.EndTime;
						} else {
							wAPSTaskStep.PlanStartTime = wAPSTaskPart.StartTime;
							wAPSTaskStep.PlanEndTime = wAPSTaskPart.EndTime;
						}

						wAPSTaskStep.ReadyTime = Calendar.getInstance();

						wAPSTaskStep.ShiftID = wShiftID;

					} else {
						wAPSTaskStep = new APSTaskStep();

						wAPSTaskStep.Active = 1;
						wAPSTaskStep.ID = 0;
						wAPSTaskStep.LineID = wAPSTaskPart.LineID;
						wAPSTaskStep.MaterialNo = wAPSTaskPart.MaterialNo;
						wAPSTaskStep.OrderID = wAPSTaskPart.OrderID;
						wAPSTaskStep.PartID = wAPSTaskPart.PartID;
						wAPSTaskStep.PartNo = wAPSTaskPart.PartNo;
						wAPSTaskStep.PlanerID = wLoginUser.ID;
						wAPSTaskStep.ProductNo = wAPSTaskPart.ProductNo;
						wAPSTaskStep.ShiftID = wShiftID;
						wAPSTaskStep.Status = APSTaskStatus.Saved.getValue();
						wAPSTaskStep.StepID = wFPCRoutePartPoint.PartPointID;
						wAPSTaskStep.TaskPartID = wAPSTaskPart.ID;
						wAPSTaskStep.TaskLineID = wAPSTaskPart.TaskLineID;
						wAPSTaskStep.TaskText = wAPSTaskPart.TaskText;
						wAPSTaskStep.WorkHour = 0;
						wAPSTaskStep.WorkShopID = wAPSTaskPart.WorkShopID;
						wAPSTaskStep.StartTime = wBaseTime;
						wAPSTaskStep.EndTime = wBaseTime;
						wAPSTaskStep.ReadyTime = wBaseTime;
						wAPSTaskStep.CreateTime = Calendar.getInstance();
						if (wStepPlanList.stream().anyMatch(p -> p.OrderID == wAPSTaskPart.OrderID
								&& p.PartID == wAPSTaskPart.PartID && p.StepID == wFPCRoutePartPoint.PartPointID)) {
							APSTaskStepPlan wStepPlan = wStepPlanList.stream()
									.filter(p -> p.OrderID == wAPSTaskPart.OrderID && p.PartID == wAPSTaskPart.PartID
											&& p.StepID == wFPCRoutePartPoint.PartPointID)
									.findFirst().get();
							wAPSTaskStep.PlanStartTime = wStepPlan.StartTime;
							wAPSTaskStep.PlanEndTime = wStepPlan.EndTime;
						} else {
							wAPSTaskStep.PlanStartTime = wAPSTaskPart.StartTime;
							wAPSTaskStep.PlanEndTime = wAPSTaskPart.EndTime;
						}
					}

					// 判断是否禁用此日计划
					if (wAPSTaskPart.Active == 2) {
						wAPSTaskStep.Active = 2;
					}

					APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 触发质量日计划
	 */
	private void TriggerQualityDayPlans(BMSEmployee wLoginUser, APSSchedulingVersion wAPSSchedulingVersion) {
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			if (wAPSSchedulingVersion.APSTaskPartList == null || wAPSSchedulingVersion.APSTaskPartList.size() <= 0) {
				return;
			}

			List<FPCPart> wQPartList = FMCServiceImpl.getInstance()
					.FPC_QueryPartList(wLoginUser, -1, -1, -1, FPCPartTypes.QTFinally.getValue()).List(FPCPart.class);
			if (wQPartList == null || wQPartList.size() <= 0) {
				return;
			}

			for (APSTaskPart wAPSTaskPart : wAPSSchedulingVersion.APSTaskPartList) {
				List<FPCPart> wTempPartList = (List<FPCPart>) wQPartList.stream()
						.filter(p -> (p.QTPartID == wAPSTaskPart.PartID)).collect(Collectors.toList());
				if (wTempPartList == null || wTempPartList.size() <= 0) {
					continue;
				}

				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wAPSTaskPart.OrderID, wErrorCode);
				if (wOrder == null || wOrder.ID <= 0) {
					continue;
				}
				for (FPCPart wFPCPart : wTempPartList) {
					List<Integer> wStepIDList = APSUtils.getInstance().FMC_QueryStepIDList(wLoginUser, wOrder.LineID,
							wFPCPart.ID, wOrder.ProductID);
					if (wStepIDList == null || wStepIDList.size() <= 0) {
						continue;
					}

					List<APSTaskStep> wList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1, -1,
							-1, -1, wFPCPart.ID, -1, -1, 1, null, null, null, null, wErrorCode);
					if (wList != null && wList.size() > 0) {
						continue;
					}

					CreateQualityDayPlans(wLoginUser, wStepIDList, wFPCPart, wOrder);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	private void CreateQualityDayPlans(BMSEmployee wAdminUser, List<Integer> wStepIDList, FPCPart wFPCPart,
			OMSOrder wOrder) {
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

		try {
			int wShiftID = MESServer.MES_QueryShiftID(wAdminUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			for (Integer wStepID : wStepIDList) {
				APSTaskStep wAPSTaskStep = new APSTaskStep();

				wAPSTaskStep.ID = 0;
				wAPSTaskStep.Active = 1;
				wAPSTaskStep.CreateTime = Calendar.getInstance();
				wAPSTaskStep.LineID = wOrder.LineID;
				wAPSTaskStep.OrderID = wOrder.ID;
				wAPSTaskStep.PartID = wFPCPart.ID;
				wAPSTaskStep.ProductNo = wOrder.ProductNo;
				wAPSTaskStep.ShiftID = wShiftID;
				wAPSTaskStep.Status = APSTaskStatus.Issued.getValue();
				wAPSTaskStep.StepID = wStepID.intValue();
				wAPSTaskStep.IsDispatched = false;
				wAPSTaskStep.PartNo = wOrder.PartNo;
				wAPSTaskStep.ReadyTime = Calendar.getInstance();

				int wNewID = APSTaskStepDAO.getInstance().Update(wAdminUser, wAPSTaskStep, wErrorCode);
				if (wNewID <= 0) {
					break;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public ServiceResult<APSAuditConfig> APS_QueryAuditConfig(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSAuditConfig> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSAuditConfigDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryAuditConfig", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<List<APSAuditConfig>> APS_QueryAuditConfigList(BMSEmployee wLoginUser, int wID,
			int wAPSShiftPeriod) {
		ServiceResult<List<APSAuditConfig>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSAuditConfigDAO.getInstance().SelectList(wLoginUser, wID, wAPSShiftPeriod, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryAuditConfigList", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_UpdateAuditConfig(BMSEmployee wLoginUser, APSAuditConfig wAPSAuditConfig) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = Integer
					.valueOf(APSAuditConfigDAO.getInstance().Update(wLoginUser, wAPSAuditConfig, wErrorCode));
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_UpdateAuditConfig", e.toString() }));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_DeleteAuditConfigList(BMSEmployee wLoginUser,
			List<APSAuditConfig> wAPSAuditConfigList) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult = APSAuditConfigDAO.getInstance().DeleteList(wLoginUser, wAPSAuditConfigList, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_DeleteAuditConfigList", e.toString() }));
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FPCProduct>> APS_PlanProductList(BMSEmployee wLoginUser, int wShiftPeriod) {
		ServiceResult<List<FPCProduct>> wResult = new ServiceResult<List<FPCProduct>>();
		try {
			wResult.Result = new ArrayList<FPCProduct>();
			ServiceResult<List<APSTaskPart>> wRst = APS_QueryHistoryTaskPartList(wLoginUser, wShiftPeriod,
					Calendar.getInstance(), null);
			if (wRst == null || wRst.Result == null || wRst.Result.size() <= 0)
				return wResult;

			// 产品型号映射
			Map<String, FPCProduct> wProductMap = MyHelperServiceImpl.getInstance()
					.FPC_QueryProductNameMap(wLoginUser).Result;
			if (wProductMap == null || wProductMap.size() <= 0)
				return wResult;

			for (APSTaskPart wAPSTaskPart : wRst.Result) {
				String[] wCars = wAPSTaskPart.PartNo.split("#");
				if (wCars.length != 2)
					continue;
				if (wProductMap.containsKey(wCars[0]))
					wResult.Result.add(wProductMap.get(wCars[0]));
			}

			// 去重
			if (wResult.Result.size() > 0) {
				wResult.Result = new ArrayList<FPCProduct>(wResult.Result.stream()
						.collect(Collectors.toMap(FPCProduct::getID, account -> account, (k1, k2) -> k2)).values());
			}
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_PlanProductList", e.toString()));
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_AdjustHour(BMSEmployee wLoginUser, List<APSTaskStep> wAPSTaskStepList,
			double wHour) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			for (APSTaskStep wAPSTaskStep : wAPSTaskStepList) {
				wAPSTaskStep.WorkHour = wHour;
				APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
			}
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_AdjustHour", e.toString() }));
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryAuditStepTaskList(BMSEmployee wLoginUser, int wTagType,
			Calendar wShiftDate) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			int wModuleID = BPMEventModule.SCDayAudit.getValue();

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wShiftDate, APSShiftPeriod.Day,
					FMCShiftLevel.Default, 0);

			List<APSTaskStep> wHisTaskList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, -1,
					-1, -1, -1, 1,
					new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Saved.getValue(),
							APSTaskStatus.ToAudit.getValue(), APSTaskStatus.Audited.getValue())),
					null, null, null, wErrorCode);

			List<BFCMessage> wMessageList = null;
			switch (TagTypes.getEnumType(wTagType)) {
			case Dispatcher:// 待办
				// 若是已提交的任务，则根据当前登录者的权限和审批配置判断该任务此人是否有权限拿
				for (APSTaskStep wAPSTaskStep : wHisTaskList) {
					wMessageList = CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.ID,
							wModuleID, BFCMessageType.Task.getValue(), 0, wShiftID, null, null).List(BFCMessage.class);
					if (wMessageList != null && wMessageList.size() > 0
							&& wMessageList.stream().anyMatch(p -> p.MessageID == wAPSTaskStep.ID)) {
						wResult.Result.add(wAPSTaskStep);
					}
				}
				break;
			case Applicant:// 已办
				List<BFCAuditAction> wActionList = null;
				for (APSTaskStep wAPSTaskStep : wHisTaskList) {
					wActionList = CoreServiceImpl.getInstance().BFC_ActionAll(wLoginUser, wModuleID, wAPSTaskStep.ID)
							.List(BFCAuditAction.class);
					if (wActionList != null && wActionList.size() > 0
							&& wActionList.stream().anyMatch(p -> p.AuditorID == wLoginUser.ID)) {
						wResult.Result.add(wAPSTaskStep);
					}
				}
				break;
			default:
				break;
			}
			// 去重
			if (wResult.Result != null && wResult.Result.size() > 0) {
				wResult.Result = new ArrayList<APSTaskStep>(wResult.Result.stream()
						.collect(Collectors.toMap(APSTaskStep::getID, account -> account, (k1, k2) -> k2)).values());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(
					StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_QueryAuditStepTaskList", e.toString()));
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_AuditStepOperate(BMSEmployee wLoginUser, List<APSTaskStep> wTaskList,
			int wOperateType) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			List<Integer> wPartIDList = null;
			Map<Integer, List<Integer>> wAreaPartIDMap = null;
			switch (APSOperateType.getEnumType(wOperateType)) {
			case Submit:
				if (wTaskList.stream().anyMatch(p -> p.Status == APSTaskStatus.Saved.getValue()
						&& (p.Remark == null || StringUtils.isEmpty(p.Remark.Remark)))) {
					wResult.FaultCode += "提示：未提交的计划需要填写备注!";
					return wResult;
				}

				// 保存备注
				SaveRemarks(wLoginUser, wTaskList);

				List<APSTaskStep> wAuditList = wTaskList.stream()
						.filter(p -> p.Status == APSTaskStatus.Submit.getValue()).collect(Collectors.toList());

				if (wAuditList.size() <= 0) {
					wResult.FaultCode += "提示：提交的数据中必须包含要审批的数据!";
					return wResult;
				}

				// 获取工区
				Optional<LFSWorkAreaStation> wOption = APSConstans.GetLFSWorkAreaStationList().values().stream()
						.filter(p -> p.StationID == wAuditList.get(0).PartID).findFirst();
				if (!wOption.isPresent() || wOption.get().WorkAreaID <= 0) {
					wResult.FaultCode += "提示：工区未找到!";
					return wResult;
				}
				int wAreaID = wOption.get().WorkAreaID;
				// 根据工区和ShiftID查询日计划审批数据
				Calendar wBaseTime = Calendar.getInstance();
				wBaseTime.set(2000, 0, 1);
				List<APSDayPlanAudit> wDayAuditList = APSDayPlanAuditDAO.getInstance().SelectList(wLoginUser, -1,
						wAreaID, wShiftID, null, wErrorCode);
				if (wDayAuditList == null || wDayAuditList.size() <= 0) {
					// 关闭过期的审批单
					APSDayPlanAuditDAO.getInstance().CloseAreaAudit(wLoginUser, wAreaID, wErrorCode);
					// 创建新的审批单
					APSDayPlanAudit wAPSDayPlanAudit = new APSDayPlanAudit(0, wAreaID, "", wShiftID, wLoginUser.ID, "",
							Calendar.getInstance(), 0, "", wBaseTime, BPMStatus.Save.getValue());
					wAPSDayPlanAudit.ID = APSDayPlanAuditDAO.getInstance().Update(wLoginUser, wAPSDayPlanAudit,
							wErrorCode);
					APSDayPlanAuditDAO.getInstance().Audit(wLoginUser, wAPSDayPlanAudit, wOperateType, wErrorCode);
					if (wErrorCode.Result == 0) {
						for (APSTaskStep wAPSTaskStep : wAuditList) {
							wAPSTaskStep.ShiftID = wShiftID;
							wAPSTaskStep.Status = APSTaskStatus.ToAudit.getValue();
							APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
						}
					}
				} else if (wDayAuditList.stream().anyMatch(p -> p.Status == BPMStatus.Save.getValue())) {
					APSDayPlanAudit wAPSDayPlanAudit = wDayAuditList.stream()
							.filter(p -> p.Status == BPMStatus.Save.getValue()).findFirst().get();
					APSDayPlanAuditDAO.getInstance().Audit(wLoginUser, wAPSDayPlanAudit, wOperateType, wErrorCode);
					if (wErrorCode.Result == 0) {
						for (APSTaskStep wAPSTaskStep : wAuditList) {
							wAPSTaskStep.ShiftID = wShiftID;
							wAPSTaskStep.Status = APSTaskStatus.ToAudit.getValue();
							APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
						}
					}
				}
//				else if (wDayAuditList.stream().anyMatch(
//						p -> p.Status != BPMStatus.Audited.getValue() && p.Status != BPMStatus.Save.getValue())) {
//					wResult.FaultCode += "提示：本工区今日日计划已制定，请审批完成后再试!";
//					return wResult;
//				} 

				else {
					String wReSubmitStr = Configuration.readConfigString("resubmit", "config/config");
					int wReSubmit = Integer.parseInt(wReSubmitStr);
					if (wReSubmit != 1) {
						wResult.FaultCode += "提示：本工区今日日计划已制定，无法再次制定!";
						return wResult;
					}
					// 重新制定日计划
					APSDayPlanAudit wAPSDayPlanAudit = new APSDayPlanAudit(0, wAreaID, "", wShiftID, wLoginUser.ID, "",
							Calendar.getInstance(), 0, "", wBaseTime, BPMStatus.Save.getValue());
					wAPSDayPlanAudit.ID = APSDayPlanAuditDAO.getInstance().Update(wLoginUser, wAPSDayPlanAudit,
							wErrorCode);
					APSDayPlanAuditDAO.getInstance().Audit(wLoginUser, wAPSDayPlanAudit, wOperateType, wErrorCode);
					if (wErrorCode.Result == 0) {
						for (APSTaskStep wAPSTaskStep : wAuditList) {
							wAPSTaskStep.Status = APSTaskStatus.ToAudit.getValue();
							wAPSTaskStep.ShiftID = wShiftID;
							APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
						}
					}
				}

				// 将工位计划状态改为“已确认”
				List<Integer> wTaskPartIDList = wAuditList.stream()
						.filter(p -> p.Status == APSTaskStatus.ToAudit.getValue()).map(p -> p.TaskPartID).distinct()
						.collect(Collectors.toList());
				if (wTaskPartIDList.size() > 0) {
					List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
							wTaskPartIDList, wErrorCode);
					for (APSTaskPart wAPSTaskPart : wTaskPartList) {
						wAPSTaskPart.Status = APSTaskStatus.Confirm.getValue();
						APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
					}
				}
				break;
			case Audit:
				wPartIDList = wTaskList.stream().map(p -> p.PartID).distinct().collect(Collectors.toList());

				wAreaPartIDMap = new HashMap<Integer, List<Integer>>();
				for (Integer wPartID : wPartIDList) {
					int wID = GetAreaIDByPartID(wLoginUser, wPartID);
					if (wID > 0) {
						if (wAreaPartIDMap.containsKey(wID)) {
							List<Integer> wIDList = wAreaPartIDMap.get(wID);
							if (!wIDList.stream().anyMatch(p -> p == wPartID)) {
								wIDList.add(wPartID);
							}
						} else {
							List<Integer> wIDList = new ArrayList<Integer>();
							wIDList.add(wPartID);
							wAreaPartIDMap.put(wID, wIDList);
						}
					}
				}

				// 遍历工区映射，审批日计划
				for (Integer wAreaItemID : wAreaPartIDMap.keySet()) {
					// 查找日计划配置
					List<APSDayPlanAudit> wDayPlanAuditList = APSDayPlanAuditDAO.getInstance().SelectList(wLoginUser,
							-1, wAreaItemID, -1, new ArrayList<Integer>(Arrays.asList(BPMStatus.ToAudit.getValue())),
							wErrorCode);
					if (wDayPlanAuditList == null || wDayPlanAuditList.size() <= 0) {
						continue;
					}
					APSDayPlanAudit wAPSDayPlanAudit = wDayPlanAuditList.get(wDayPlanAuditList.size() - 1);
					APSDayPlanAuditDAO.getInstance().Audit(wLoginUser, wAPSDayPlanAudit, wOperateType, wErrorCode);
					if (wAPSDayPlanAudit.Status == BPMStatus.Audited.getValue()) {
						Map<Integer, List<Integer>> wTempMap = wAreaPartIDMap;
						// 查找工序任务
						List<APSTaskStep> wStepTaskList = wTaskList.stream()
								.filter(p -> wTempMap.get(wAreaItemID).stream().anyMatch(q -> q == p.PartID))
								.collect(Collectors.toList());
						// 修改工序任务状态
						wStepTaskList.forEach(p -> p.Status = APSTaskStatus.Audited.getValue());
						for (APSTaskStep wAPSTaskStep : wStepTaskList) {
							APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
						}
						// 关闭待审批的消息
						CloseMessage(wLoginUser, wAPSDayPlanAudit.ID);
					}
				}
				break;
			case Cancel:// 撤回
			case Reject:// 驳回
				wPartIDList = wTaskList.stream().map(p -> p.PartID).distinct().collect(Collectors.toList());

				wAreaPartIDMap = new HashMap<Integer, List<Integer>>();
				for (Integer wPartID : wPartIDList) {
					int wID = GetAreaIDByPartID(wLoginUser, wPartID);
					if (wID > 0) {
						if (wAreaPartIDMap.containsKey(wID)) {
							List<Integer> wIDList = wAreaPartIDMap.get(wID);
							if (!wIDList.stream().anyMatch(p -> p == wPartID)) {
								wIDList.add(wPartID);
							}
						} else {
							List<Integer> wIDList = new ArrayList<Integer>();
							wIDList.add(wPartID);
							wAreaPartIDMap.put(wID, wIDList);
						}
					}
				}

				// 遍历工区映射，审批日计划
				for (Integer wAreaItemID : wAreaPartIDMap.keySet()) {
					// 查找日计划配置
					List<APSDayPlanAudit> wDayPlanAuditList = APSDayPlanAuditDAO.getInstance().SelectList(wLoginUser,
							-1, wAreaItemID, -1, new ArrayList<Integer>(Arrays.asList(BPMStatus.ToAudit.getValue())),
							wErrorCode);
					if (wDayPlanAuditList == null) {
						continue;
					}
					APSDayPlanAudit wAPSDayPlanAudit = wDayPlanAuditList.get(wDayPlanAuditList.size() - 1);
					APSDayPlanAuditDAO.getInstance().Audit(wLoginUser, wAPSDayPlanAudit, wOperateType, wErrorCode);
					if (wErrorCode.Result == 0 && wAPSDayPlanAudit.Status == BPMStatus.Save.getValue()) {
						Map<Integer, List<Integer>> wTempMap = wAreaPartIDMap;
						// 查找工序任务
						List<APSTaskStep> wStepTaskList = wTaskList.stream()
								.filter(p -> wTempMap.get(wAreaItemID).stream().anyMatch(q -> q == p.PartID))
								.collect(Collectors.toList());
						// 修改工序任务状态
						wStepTaskList.forEach(p -> p.Status = APSTaskStatus.Saved.getValue());
						for (APSTaskStep wAPSTaskStep : wStepTaskList) {
							APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
						}

						// 将工位任务状态改为“下达”
						wTaskPartIDList = wStepTaskList.stream().map(p -> p.TaskPartID).collect(Collectors.toList());
						List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
								wTaskPartIDList, wErrorCode);
						for (APSTaskPart wAPSTaskPart : wTaskPartList) {
							wAPSTaskPart.Status = APSTaskStatus.Issued.getValue();
							APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
						}
					}
				}

				break;
			case Issued:// 下达
				// 【日计划下达】权限控制
				if (!CoreServiceImpl.getInstance()
						.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 82000003, 0, 0)
						.Info(Boolean.class)) {
					wResult.FaultCode = RetCode.SERVER_CODE_UNROLE;
					return wResult;
				}
				for (APSTaskStep wAPSTaskStep : wTaskList) {
					if (wAPSTaskStep.Status != APSTaskStatus.Audited.getValue()
							&& wAPSTaskStep.Status != APSTaskStatus.Aborted.getValue()) {
						wResult.FaultCode = "请选择已审批或已终止的数据!";
						return wResult;
					}
					wAPSTaskStep.ReadyTime = Calendar.getInstance();
					wAPSTaskStep.Status = APSTaskStatus.Issued.getValue();
					APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
				}
				// 工区主管下达日计划要发送通知消息给日计划中所有的工位对应的班组长
				SendMessageToMonitor(wLoginUser, wTaskList);
				break;
			default:
				wResult.FaultCode += "未知操作!!!";
				return wResult;
			}
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_AuditStepOperate", e.toString()));
		}
		return wResult;
	}

	/**
	 * 关闭日计划审批消息
	 */
	private void CloseMessage(BMSEmployee wLoginUser, int wDayPlanAuditID) {
		try {
			List<BFCMessage> wList = CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, -1,
					BPMEventModule.SCDayAudit.getValue(), wDayPlanAuditID, -1, 0, -1, null, null)
					.List(BFCMessage.class);
			wList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, -1,
					BPMEventModule.SCDayAudit.getValue(), wDayPlanAuditID, -1, 1, -1, null, null)
					.List(BFCMessage.class));
			wList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, -1,
					BPMEventModule.SCDayAudit.getValue(), wDayPlanAuditID, -1, 2, -1, null, null)
					.List(BFCMessage.class));
			if (wList == null || wList.size() <= 0) {
				return;
			}

			wList.forEach(p -> p.Active = 4);
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 保存备注
	 * 
	 * @param wLoginUser
	 * @param wTaskList
	 */
	private void SaveRemarks(BMSEmployee wLoginUser, List<APSTaskStep> wTaskList) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<APSTaskStep> wNotList = wTaskList.stream().filter(p -> p.Status == APSTaskStatus.Saved.getValue())
					.collect(Collectors.toList());
			if (wNotList == null || wNotList.size() <= 0) {
				return;
			}

			for (APSTaskStep wAPSTaskStep : wNotList) {
				wAPSTaskStep.Remark.SubmitID = wLoginUser.ID;
				wAPSTaskStep.RemarkList.add(wAPSTaskStep.Remark);
				APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 根据工位ID获取工区
	 * 
	 * @param wLoginUser
	 * @param wPartID
	 * @return
	 */
	private int GetAreaIDByPartID(BMSEmployee wLoginUser, int wPartID) {
		int wResult = 0;
		try {
			Optional<LFSWorkAreaStation> wOption = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.filter(p -> p.StationID == wPartID).findFirst();
			if (!wOption.isPresent() || wOption.get().WorkAreaID <= 0) {
				return wResult;
			}
			wResult = wOption.get().WorkAreaID;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private void SendMessageToMonitor(BMSEmployee wLoginUser, List<APSTaskStep> wTaskList) {
		try {
			List<Integer> wPartIDList = (List<Integer>) wTaskList.stream().map(p -> Integer.valueOf(p.PartID))
					.distinct().collect(Collectors.toList());

			Map<Integer, List<Integer>> wAreaPartIDMap = new HashMap<>();
			for (Integer wPartID : wPartIDList) {
				int wID = GetAreaIDByPartID(wLoginUser, wPartID.intValue());
				if (wID > 0) {
					if (wAreaPartIDMap.containsKey(wID)) {
						List<Integer> wIDList = wAreaPartIDMap.get(wID);
						if (!wIDList.stream().anyMatch(p -> p == wPartID)) {
							wIDList.add(wPartID);
						}
					} else {
						List<Integer> wIDList = new ArrayList<Integer>();
						wIDList.add(wPartID);
						wAreaPartIDMap.put(wID, wIDList);
					}
				}
			}

			List<BFCMessage> wMessageList = new ArrayList<>();
			BFCMessage wMessage = null;

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			for (Integer wAreaID : wAreaPartIDMap.keySet()) {
				List<Integer> wTempPartIDList = wAreaPartIDMap.get(wAreaID);
				for (Integer wPartID : wTempPartIDList) {
					// 找到班组长
					Optional<FMCWorkCharge> wOption = APSConstans.GetFMCWorkChargeList().stream()
							.filter(p -> p.StationID == wPartID).findFirst();
					if (!wOption.isPresent()) {
						continue;
					}
					List<BMSEmployee> wMonitorList = APSConstans.GetBMSEmployeeList().values().stream()
							.filter(p -> p.DepartmentID == wOption.get().ClassID
									&& APSConstans.GetBMSPosition(p.Position).DutyID == 1)
							.collect(Collectors.toList());
					for (BMSEmployee wBMSEmployee : wMonitorList) {
						wMessage = new BFCMessage();
						wMessage.Active = 0;
						wMessage.CompanyID = 0;
						wMessage.CreateTime = Calendar.getInstance();
						wMessage.EditTime = Calendar.getInstance();
						wMessage.ID = 0L;
						wMessage.MessageID = wAreaID.intValue();
						wMessage.Title = StringUtils.Format("日计划 {0}", new Object[] { Integer.valueOf(wShiftID) });
						wMessage.MessageText = StringUtils.Format("【{0}】 {1}下达了【{2}】日计划",
								new Object[] { BPMEventModule.SCDayAudit.getLable(), wLoginUser.Name,
										APSConstans.GetBMSDepartmentName(wAreaID.intValue()) });
						wMessage.ModuleID = BPMEventModule.SCDayAudit.getValue();
						wMessage.ResponsorID = wBMSEmployee.ID;
						wMessage.ShiftID = wShiftID;
						wMessage.StationID = 0L;
						wMessage.Type = BFCMessageType.Notify.getValue();
						wMessageList.add(wMessage);
					}
				}
			}

			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public ServiceResult<List<APSTaskPart>> APS_QueryTaskPartList(BMSEmployee wLoginUser, int wShiftID,
			int wShiftPeriod) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, 1, wShiftPeriod,
					null, wShiftID, null, null, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskPartList", e.toString() }));
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_SaveTaskPartList(BMSEmployee wLoginUser, Calendar wStartTime, Calendar wEndTime,
			List<APSTaskPart> wTaskPartList, List<APSMessage> wMessageList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			if (wTaskPartList.size() <= 0) {
				return wResult;
			}

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 去除开始时间大于排程的结束时间的任务
			Calendar wEndTimeC = (Calendar) wEndTime.clone();
			wEndTimeC.set(Calendar.HOUR_OF_DAY, 23);
			wEndTimeC.set(Calendar.MINUTE, 59);
			wEndTimeC.set(Calendar.SECOND, 59);

//			wTaskPartList.removeIf(p -> p.StartTime.compareTo(wEndTimeC) > 0);
			// 保存到数据库(同步提交时间)
			Calendar wSubmitTime = Calendar.getInstance();
			wMessageList.forEach(p -> p.SubmitTime = wSubmitTime);
			wTaskPartList.forEach(p -> p.SubmitTime = wSubmitTime);

			if (wTaskPartList.size() <= 0) {
				return wResult;
			}

			// 生成ShiftID
			int wShiftPeriod = wTaskPartList.get(0).ShiftPeriod;
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, (Calendar) wStartTime.clone(),
					APSShiftPeriod.getEnumType(wShiftPeriod), FMCShiftLevel.Default, 0);
			wTaskPartList.forEach(p -> p.ShiftID = wShiftID);

			for (APSMessage wItem : wMessageList) {
				this.APS_UpdateMessage(wLoginUser, wItem);
			}
			for (APSTaskPart wItem : wTaskPartList) {
				if (wItem.ShiftPeriod == APSShiftPeriod.Week.getValue()) {
					// 禁用同订单、同工位数据
					List<APSTaskPart> wOldList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wItem.OrderID,
							-1, -1, wItem.PartID, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null, wErrorCode);
					if (wOldList != null && wOldList.size() > 0) {
						wOldList.forEach(p -> p.Active = 2);
						for (APSTaskPart wAPSTaskPart : wOldList) {
							APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
						}
					}
				}
				wItem.Active = 1;
				wItem.Status = APSTaskStatus.Saved.getValue();
				ServiceResult<Long> wIDResult = this.APS_UpdateTaskPart(wLoginUser, wItem);
				if (wIDResult.Result > 0) {
					wItem.ID = wIDResult.Result.intValue();
					if (wItem.StepPlanList != null && wItem.StepPlanList.size() > 0) {
						/**
						 * 保存工序排程计划
						 */
						ExecutorService wES = Executors.newFixedThreadPool(1);
						wES.submit(() -> SaveStepPlanList(wLoginUser, wItem));
						wES.shutdown();
					}
				}
			}

			if (wTaskPartList.size() <= 0) {
				return wResult;
			}
			// 保存排程版本
			List<APSSchedulingVersion> wHistory = APSSchedulingVersionDAO.getInstance().SelectList(wLoginUser, -1,
					wTaskPartList.get(0).ShiftPeriod, null, null, wErrorCode);
			int wSerialNumber = 1;
			if (wHistory.size() > 0) {
				String wNo = wHistory.stream().max(Comparator.comparing(APSSchedulingVersion::getCreateTime))
						.get().VersionNo;
				wSerialNumber = Integer.parseInt(wNo.split("-")[2]) + 1;
			}

			DecimalFormat wDecimalFormat = new DecimalFormat("0000");
			String wFormat = wDecimalFormat.format(wSerialNumber);

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			String wCurTime = wSDF.format(Calendar.getInstance().getTime());

			// 删除之前的版本
			List<APSSchedulingVersion> wHis = APSSchedulingVersionDAO.getInstance().SelectList(wLoginUser, -1,
					wShiftPeriod, null, new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Saved.getValue())),
					wErrorCode);
			APSSchedulingVersionDAO.getInstance().DeleteList(wLoginUser, wHis, wErrorCode);
//			for (APSSchedulingVersion wItem : wHis) {
////				APSTaskPartDAO.getInstance().DeleteIDList(wLoginUser, wItem.TaskPartIDList, wErrorCode);
//			}

			APSSchedulingVersion wAPSSchedulingVersion = new APSSchedulingVersion();
			wAPSSchedulingVersion.APSShiftPeriod = wTaskPartList.get(0).ShiftPeriod;
			wAPSSchedulingVersion.CreateID = wLoginUser.ID;
			wAPSSchedulingVersion.CreateTime = Calendar.getInstance();
			wAPSSchedulingVersion.EndTime = wEndTime;
			wAPSSchedulingVersion.ID = 0;
			wAPSSchedulingVersion.StartTime = wStartTime;
			wAPSSchedulingVersion.Status = APSTaskStatus.Saved.getValue();
			wAPSSchedulingVersion.TaskPartIDList = wTaskPartList.stream().map(p -> p.ID).collect(Collectors.toList());
			wAPSSchedulingVersion.VersionNo = StringUtils.Format("{0}-{1}-{2}",
					wTaskPartList.get(0).ShiftPeriod == APSShiftPeriod.Month.getValue() ? "M" : "W", wCurTime, wFormat);
			APSSchedulingVersionDAO.getInstance().Update(wLoginUser, wAPSSchedulingVersion, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_SaveTaskPartList", e.toString()));
		}
		return wResult;
	}

	/**
	 * 保存工序排程计划
	 */
	private void SaveStepPlanList(BMSEmployee wLoginUser, APSTaskPart wItem) {
		try {
			if (wItem.ID <= 0) {
				return;
			}

			/**
			 * 根据TaskPartID删除工序排程计划
			 */
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			APSTaskStepPlanDAO.getInstance().DeleteByTaskPartID(wLoginUser, wItem.ID, wErrorCode);

			/**
			 * 遍历保存工序计划
			 */
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
			for (APSTaskPart wAPSTaskPart : wItem.StepPlanList) {
				APSTaskStepPlan wPlan = new APSTaskStepPlan(0, wItem.OrderID, wItem.PartNo, wItem.ID, wItem.LineID,
						wItem.PartID, wAPSTaskPart.PartID, Calendar.getInstance(), Calendar.getInstance(), wShiftID,
						wAPSTaskPart.StartTime, wAPSTaskPart.EndTime, 1, 1, "", wItem.ProductNo, "", "", "",
						wLoginUser.ID, "", wAPSTaskPart.TaskText, "");
				APSTaskStepPlanDAO.getInstance().Update(wLoginUser, wPlan, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryLeaderTask(BMSEmployee wLoginUser, Calendar wShiftDate) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 工区工位
			List<LFSWorkAreaStation> wAreaStationList = LFSServiceImpl.getInstance()
					.LFS_QueryWorkAreaStationList(wLoginUser).List(LFSWorkAreaStation.class);
			// 工区人员
			List<LFSWorkAreaChecker> wAreaPersonList = LFSServiceImpl.getInstance()
					.LFS_QueryWorkAreaCheckerList(wLoginUser).List(LFSWorkAreaChecker.class);
			if (wAreaStationList == null || wAreaStationList.size() <= 0 || wAreaPersonList == null
					|| wAreaPersonList.size() <= 0) {
				return wResult;
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wShiftDate, APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			if (!wAreaPersonList.stream().anyMatch(p -> p.LeaderIDList != null && p.LeaderIDList.size() > 0
					&& p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				return wResult;
			}

			LFSWorkAreaChecker wAreaPerson = wAreaPersonList.stream().filter(p -> p.LeaderIDList != null
					&& p.LeaderIDList.size() > 0 && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))
					.findFirst().get();
			wAreaStationList = wAreaStationList.stream().filter(p -> p.WorkAreaID == wAreaPerson.WorkAreaID)
					.collect(Collectors.toList());

			List<APSTaskStep> wTaskList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, -1,
					-1, -1, wShiftID, 1,
					new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Audited.getValue(),
							APSTaskStatus.Issued.getValue(), APSTaskStatus.Aborted.getValue())),
					null, null, null, wErrorCode);
			if (wTaskList == null || wTaskList.size() <= 0) {
				return wResult;
			}

			for (APSTaskStep wAPSTaskStep : wTaskList) {
				if (wAreaStationList != null && wAreaStationList.size() > 0
						&& wAreaStationList.stream().anyMatch(p -> p.StationID == wAPSTaskStep.PartID)) {
					wResult.Result.add(wAPSTaskStep);
				}
			}

			// 去重
			if (wResult.Result != null && wResult.Result.size() > 0) {
				wResult.Result = new ArrayList<APSTaskStep>(wResult.Result.stream()
						.collect(Collectors.toMap(APSTaskStep::getID, account -> account, (k1, k2) -> k2)).values());
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskPart>> APS_QueryRFTaskPartList(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 查询是否转向架互换了
			Integer wChangeOrderID = QMSServiceImpl.getInstance().SFC_QueryChangeOrderID(wLoginUser, wOrderID)
					.Info(Integer.class);
			if (wChangeOrderID > 0) {
				// ①获取工区工位基础数据
				List<LFSWorkAreaStation> wWSList = LFSServiceImpl.getInstance().LFS_QueryWorkAreaStationList(wLoginUser)
						.List(LFSWorkAreaStation.class);
				wWSList = wWSList.stream().filter(p -> p.Active == 1).collect(Collectors.toList());
				// ②获取车体和整车的工位列表
				List<Integer> wBodyPartIDList = wWSList.stream()
						.filter(p -> p.StationType == LFSStationType.WholeTrain.getValue()
								|| p.StationType == LFSStationType.Body.getValue())
						.map(p -> p.StationID).collect(Collectors.toList());
				// ③获取转向架的工位列表
				List<Integer> wBogiesPartIDList = wWSList.stream()
						.filter(p -> p.StationType == LFSStationType.Bogies.getValue()).map(p -> p.StationID)
						.collect(Collectors.toList());
				// ④查询车体和整车的工位任务
				wResult.Result.addAll(APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1, -1, -1, 1,
						APSShiftPeriod.Week.getValue(), null, -1, null, null, wBodyPartIDList, wErrorCode));
				// ⑤查询互换转向架的工位任务
				wResult.Result.addAll(APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wChangeOrderID, -1, -1,
						-1, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null, wBogiesPartIDList, wErrorCode));
			} else {
				wResult.Result = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1, -1, -1, 1,
						APSShiftPeriod.Week.getValue(), null, -1, null, null, wErrorCode);
			}

			if (wResult.Result == null || wResult.Result.size() <= 0) {
				return wResult;
			}

			OMSOrder wOMSOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wResult.Result.get(0).OrderID,
					wErrorCode);
			if (wOMSOrder == null || wOMSOrder.ID <= 0) {
				return wResult;
			}

			// 班组工位
			List<FMCWorkCharge> wWorkChargeList = APSConstans.GetFMCWorkChargeList();
			if (wWorkChargeList == null) {
				wWorkChargeList = new ArrayList<FMCWorkCharge>();
			}

			Optional<FMCWorkCharge> wOption = null;
			for (APSTaskPart wAPSTaskPart : wResult.Result) {
				wAPSTaskPart.CustomerID = wOMSOrder.BureauSectionID;
				wAPSTaskPart.CustomerName = wOMSOrder.BureauSection;
				wAPSTaskPart.WBSNO = wOMSOrder.WBSNo;
				wAPSTaskPart.OrderNo = wOMSOrder.OrderNo;
				wOption = wWorkChargeList.stream().filter(p -> p.Active == 1 && p.StationID == wAPSTaskPart.PartID)
						.findFirst();
				if (wOption.isPresent()) {
					wAPSTaskPart.ClassID = wOption.get().ClassID;
					wAPSTaskPart.ClassName = APSConstans.GetBMSDepartmentName(wAPSTaskPart.ClassID);
				}

				wAPSTaskPart.EndTime.add(Calendar.DATE, -1);
				Calendar wEDate = (Calendar) wAPSTaskPart.FinishWorkTime.clone();
				Calendar wSDate = (Calendar) wAPSTaskPart.EndTime.clone();
				wAPSTaskPart.DelayHours = getDateDays(wEDate, wSDate);
			}

			// 查询工序进度
			List<Integer> TaskPartIDList = wResult.Result.stream().map(p -> p.ID).collect(Collectors.toList());

			List<APSTaskPart> wWSList = APSTaskPartDAO.getInstance().SelectListProgress(wLoginUser, TaskPartIDList,
					wErrorCode);
			for (APSTaskPart wItem : wResult.Result) {
				if (wWSList.stream().anyMatch(p -> p.ID == wItem.ID)) {
					APSTaskPart wTaskPat = wWSList.stream().filter(p -> p.ID == wItem.ID).findFirst().get();

					wItem.PointSize = wTaskPat.PointSize;
					wItem.PointFinishSize = wTaskPat.PointFinishSize;
				}
			}

			// 排序
			wResult.Result = SortPartList(wLoginUser, wResult.Result, wOMSOrder.ID);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@SuppressWarnings("unused")
	private void UpdateTaskStepStatus() {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<Integer> wTaskStepIDList = APSTaskStepDAO.getInstance()
					.SelectNotRealFinishedTaskStepIDList(BaseDAO.SysAdmin, wErrorCode);
			for (Integer wTaskStepID : wTaskStepIDList) {
				APSTaskStep wTaskStep = APSTaskStepDAO.getInstance().SelectByID(BaseDAO.SysAdmin, wTaskStepID,
						wErrorCode);
				if (wTaskStep != null && wTaskStep.ID > 0) {
					wTaskStep.Status = 4;
					APSTaskStepDAO.getInstance().Update(BaseDAO.SysAdmin, wTaskStep, wErrorCode);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 排序
	 */
	private List<APSTaskPart> SortPartList(BMSEmployee wLoginUser, List<APSTaskPart> wList, int wOrderID) {
		List<APSTaskPart> wResult = wList;
		try {
			// ①获取工位顺序字典
			Map<Integer, Integer> wPartMap = APSTaskPartDAO.getInstance().SelectPartOrderMap(wLoginUser, wOrderID);
			// ②顺序赋值
			for (APSTaskPart wAPSTaskPart : wResult) {
				if (wPartMap.containsKey(wAPSTaskPart.PartID)) {
					wAPSTaskPart.PartOrder = wPartMap.get(wAPSTaskPart.PartID);
				} else {
					wAPSTaskPart.PartOrder = 100;
				}
			}
			// ③排序
			wResult.sort(Comparator.comparing(APSTaskPart::getPartOrder));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public ServiceResult<APSWorkHour> APS_QueryWorkHour(BMSEmployee wLoginUser) {
		ServiceResult<APSWorkHour> wResult = new ServiceResult<>();
		wResult.Result = new APSWorkHour();
		try {
			wResult.Result = XmlTool.ReadXml(String.valueOf(Constants.getConfigPath()) + "APSWorkHour.xml");
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> APS_SaveWorkHour(BMSEmployee wLoginUser, int wMinWorkHour, int wMiddleWorkHour,
			int wMaxWorkHour) {
		ServiceResult<Integer> wResult = new ServiceResult<>();
		try {
			APSWorkHour wAPSWorkHour = new APSWorkHour();
			wAPSWorkHour.MaxWorkHour = wMaxWorkHour;
			wAPSWorkHour.MiddleWorkHour = wMiddleWorkHour;
			wAPSWorkHour.MinWorkHour = wMinWorkHour;

			XmlTool.SaveXml(String.valueOf(Constants.getConfigPath()) + "APSWorkHour.xml", wAPSWorkHour);
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Boolean> APS_CheckIsExist(BMSEmployee wLoginUser, Calendar wStartTime,
			APSShiftPeriod wShiftPeriod) {
		ServiceResult<Boolean> wResult = new ServiceResult<Boolean>();
		wResult.Result = false;
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<APSSchedulingVersionBPM> wList = APSSchedulingVersionBPMDAO.getInstance().SelectList(wLoginUser, -1,
					"", -1, -1, "", "", wShiftPeriod.getValue(), -1, new ArrayList<Integer>(Arrays.asList(2, 3, 4, 5)),
					null, null, wErrorCode);
//			List<APSSchedulingVersion> wList = APSSchedulingVersionDAO.getInstance().SelectList(wLoginUser, -1,
//					wShiftPeriod.getValue(), wStartTime,
//					new ArrayList<Integer>(
//							Arrays.asList(APSTaskStatus.Saved.getValue(), APSTaskStatus.Submit.getValue(),
//									APSTaskStatus.ToAudit.getValue(), APSTaskStatus.Audited.getValue())),
//					wErrorCode);
			if (wList != null && wList.size() > 0) {
				wResult.Result = true;
			}
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_QueryTodayToDoTaskPartList(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSTaskPartDAO.getInstance().SelectTodayToDoList(wLoginUser, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<APSSchedulingVersion>> APS_QueryHistoryVersionList(BMSEmployee wLoginUser,
			int wAPSShiftPeriod, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<APSSchedulingVersion>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
			wResult.Result = APSSchedulingVersionDAO.getInstance().SelectListByTime(wLoginUser, wAPSShiftPeriod,
					wStartTime, wEndTime, wErrorCode);
			for (APSSchedulingVersion wAPSSchedulingVersion : wResult.Result) {
				wAPSSchedulingVersion.APSTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
						wAPSSchedulingVersion.TaskPartIDList, wErrorCode);
				// 工位排序
				wAPSSchedulingVersion.APSTaskPartList = OrderPart(wAPSSchedulingVersion.APSTaskPartList);
//				if (wAPSSchedulingVersion.APSTaskPartList != null && wAPSSchedulingVersion.APSTaskPartList.size() > 0) {
//					wAPSSchedulingVersion.APSTaskPartList.forEach(p -> {
//					});
//					wAPSSchedulingVersion.APSTaskPartList.forEach(p -> {
//					});
//				}
				wAPSSchedulingVersion.OMSOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser,
						wAPSSchedulingVersion.APSTaskPartList.stream().map(p -> p.OrderID).distinct()
								.collect(Collectors.toList()),
						wErrorCode);
			}
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FMCWorkspace>> APS_QueryPartNoStatus(BMSEmployee wLoginUser, List<FMCWorkspace> wList) {
		ServiceResult<List<FMCWorkspace>> wResult = new ServiceResult<List<FMCWorkspace>>();
		wResult.Result = wList;
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			OMSOrder wOrder = null;
			List<OMSOrder> wOrderList = null;
			List<RSMTurnOrderTask> wTurnOrdeList = null;
			List<APSTaskPart> wTaskPartList = null;
			for (FMCWorkspace wFMCWorkspace : wList) {
				wFMCWorkspace.PartNo = wFMCWorkspace.PartNo.replace("[A]", "").replace("[B]", "");
				wOrderList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1,
						wFMCWorkspace.PartNo, "", 1, null, null, null, null, null, wErrorCode);
				if (wOrderList == null || wOrderList.size() <= 0) {
					continue;
				}
				wOrder = wOrderList.get(0);
				wTurnOrdeList = QMSServiceImpl.getInstance()
						.RSM_QueryTurnOrderTaskList(wLoginUser, wOrder.ID, -1, -1, null).List(RSMTurnOrderTask.class);
				if (wTurnOrdeList == null || wTurnOrdeList.size() <= 0) {
					wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1, -1,
							APSConstans.YJStationID, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null,
							wErrorCode);
					if (wTaskPartList != null && wTaskPartList.size() > 0
							&& wTaskPartList.stream().anyMatch(p -> p.Status == APSTaskStatus.Issued.getValue())) {
						wFMCWorkspace.Status = 3;
					} else if (wTaskPartList != null && wTaskPartList.size() > 0
							&& wTaskPartList.stream().anyMatch(p -> p.Status == APSTaskStatus.Started.getValue())) {
						wFMCWorkspace.Status = 4;
					} else if (wTaskPartList != null && wTaskPartList.size() > 0
							&& wTaskPartList.stream().anyMatch(p -> p.Status == APSTaskStatus.Done.getValue())) {
						wFMCWorkspace.Status = 5;
					}
				} else {
					Calendar wApplyTime = wTurnOrdeList.stream()
							.max(Comparator.comparing(RSMTurnOrderTask::getApplyTime)).get().ApplyTime;
					wTurnOrdeList = wTurnOrdeList.stream().filter(p -> p.ApplyTime.compareTo(wApplyTime) == 0)
							.collect(Collectors.toList());
					for (RSMTurnOrderTask wRSMTurnOrderTask : wTurnOrdeList) {
						if (wRSMTurnOrderTask.Status == SFCTurnOrderTaskStatus.Auditing.getValue()) {
							wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1, -1,
									wRSMTurnOrderTask.ApplyStationID, 1, APSShiftPeriod.Week.getValue(), null, -1, null,
									null, wErrorCode);
							if (wTaskPartList != null && wTaskPartList.size() > 0 && wTaskPartList.stream()
									.anyMatch(p -> p.Status == APSTaskStatus.Issued.getValue())) {
								wFMCWorkspace.Status = 3;
							} else if (wTaskPartList != null && wTaskPartList.size() > 0 && wTaskPartList.stream()
									.anyMatch(p -> p.Status == APSTaskStatus.Started.getValue())) {
								wFMCWorkspace.Status = 4;
							} else if (wTaskPartList != null && wTaskPartList.size() > 0 && wTaskPartList.stream()
									.anyMatch(p -> p.Status == APSTaskStatus.Done.getValue())) {
								wFMCWorkspace.Status = 5;
							}
						} else if (wRSMTurnOrderTask.Status == SFCTurnOrderTaskStatus.Passed.getValue()) {
							wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1, -1,
									wRSMTurnOrderTask.TargetStationID, 1, APSShiftPeriod.Week.getValue(), null, -1,
									null, null, wErrorCode);
							if (wTaskPartList != null && wTaskPartList.size() > 0 && wTaskPartList.stream()
									.anyMatch(p -> p.Status == APSTaskStatus.Issued.getValue())) {
								wFMCWorkspace.Status = 3;
							} else if (wTaskPartList != null && wTaskPartList.size() > 0 && wTaskPartList.stream()
									.anyMatch(p -> p.Status == APSTaskStatus.Started.getValue())) {
								wFMCWorkspace.Status = 4;
							} else if (wTaskPartList != null && wTaskPartList.size() > 0 && wTaskPartList.stream()
									.anyMatch(p -> p.Status == APSTaskStatus.Done.getValue())) {
								wFMCWorkspace.Status = 5;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 自动补齐工序同时返回订单列表和工位列表
	 */
	private ServiceResult<List<OMSOrder>> GetOrderListAndTaskPartListWithWholeTaskStep(BMSEmployee wLoginUser,
			List<APSTaskStep> wStepTaskList, List<FPCRoutePart> wFPCRoutePartList,
			List<FPCRoutePartPoint> wFPCRoutePartPointList) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wStepTaskList == null || wStepTaskList.size() <= 0) {
				wResult.CustomResult.put("TaskPartList", new ArrayList<APSTaskPartDetails>());
				wResult.CustomResult.put("TaskStepList", new ArrayList<APSTaskStep>());
				return wResult;
			}

			// 根据订单ID集合获取订单列表
			List<Integer> wOrderIDList = wStepTaskList.stream().map(p -> p.OrderID).distinct()
					.collect(Collectors.toList());
			wResult.Result = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);

			// 根据工位任务ID集合获取所有工位任务
			List<Integer> wTaskPartIDList = wStepTaskList.stream().map(p -> p.TaskPartID).distinct()
					.collect(Collectors.toList());
			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
					wTaskPartIDList, wErrorCode);
			wTaskPartList = wTaskPartList.stream().filter(p -> p.Active == 1).collect(Collectors.toList());

			List<APSTaskStep> wAllStepList = new ArrayList<APSTaskStep>();

			List<Integer> wPartPointIDList = null;
			List<APSTaskPartDetails> wDetailsList = new ArrayList<APSTaskPartDetails>();
			APSTaskPartDetails wAPSTaskPartDetails = null;
			List<APSTaskStep> wTaskStepList = null;

			List<LFSWorkAreaStation> wAreaStationList = LFSServiceImpl.getInstance()
					.LFS_QueryWorkAreaStationList(wLoginUser).List(LFSWorkAreaStation.class);

			int wRouteID = 0;

			List<APSTaskStep> wAllTaskStepList = new ArrayList<APSTaskStep>();
			if (wTaskPartList.size() > 0) {
				wAllTaskStepList = APSTaskStepDAO.getInstance().SelectListByTaskPartIDList(wLoginUser,
						wTaskPartList.stream().map(p -> p.ID).collect(Collectors.toList()), wErrorCode);
			}

			// 基本时间
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);

			for (APSTaskPart wAPSTaskPart : wTaskPartList) {
				OMSOrder wOrder = wResult.Result.stream().filter(p -> p.ID == wAPSTaskPart.OrderID).findFirst().get();
				if (wOrder != null && wOrder.ID > 0) {
					wRouteID = wOrder.RouteID;
					if (wAPSTaskPart.RouteID > 0) {
						wRouteID = wAPSTaskPart.RouteID;
					}
				}
				// 工艺工序列表
				wPartPointIDList = APS_GetPartPointIDList(wFPCRoutePartList, wFPCRoutePartPointList,
						wAPSTaskPart.PartID, wRouteID);
				// 工序任务列表
				wTaskStepList = wAllTaskStepList.stream().filter(p -> p.TaskPartID == wAPSTaskPart.ID)
						.collect(Collectors.toList());

				List<APSTaskStep> wTempList = wTaskStepList;
				List<Integer> wNotSaveList = wPartPointIDList.stream()
						.filter(p -> !wTempList.stream().anyMatch(q -> q.StepID == p)).collect(Collectors.toList());

				if (wNotSaveList != null && wNotSaveList.size() > 0) {
					for (int wNewStepID : wNotSaveList) {
						APSTaskStep wAPSTaskStep = new APSTaskStep();

						wAPSTaskStep.Active = 1;
						wAPSTaskStep.ID = 0;
						wAPSTaskStep.LineID = wAPSTaskPart.LineID;
						wAPSTaskStep.MaterialNo = wAPSTaskPart.MaterialNo;
						wAPSTaskStep.OrderID = wAPSTaskPart.OrderID;
						wAPSTaskStep.PartID = wAPSTaskPart.PartID;
						wAPSTaskStep.PartNo = wAPSTaskPart.PartNo;
						wAPSTaskStep.PlanerID = wLoginUser.ID;
						wAPSTaskStep.ProductNo = wAPSTaskPart.ProductNo;
						wAPSTaskStep.ShiftID = 0;
						wAPSTaskStep.Status = APSTaskStatus.Saved.getValue();
						wAPSTaskStep.StepID = wNewStepID;
						wAPSTaskStep.TaskPartID = wAPSTaskPart.ID;
						wAPSTaskStep.TaskLineID = wAPSTaskPart.TaskLineID;
						wAPSTaskStep.TaskText = wAPSTaskPart.TaskText;
						wAPSTaskStep.WorkHour = 0;
						wAPSTaskStep.WorkShopID = wAPSTaskPart.WorkShopID;
						wAPSTaskStep.StartTime = wBaseTime;
						wAPSTaskStep.EndTime = wBaseTime;
						wAPSTaskStep.ReadyTime = wBaseTime;
						wAPSTaskStep.CreateTime = Calendar.getInstance();

						wAPSTaskStep.ID = APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
						wTaskStepList.add(wAPSTaskStep);
					}
				}

				// 工位详情
				wAPSTaskPartDetails = new APSTaskPartDetails();
				wAPSTaskPartDetails.APSTaskPart = wAPSTaskPart;
				wAPSTaskPartDetails.OrderNum = wAPSTaskPart.ID;
				wAPSTaskPartDetails.PartID = wAPSTaskPart.PartID;
				wAPSTaskPartDetails.ShiftDate = wAPSTaskPart.StartTime;
				wAPSTaskPartDetails.PartNo = wOrder.PartNo;
				wAPSTaskPartDetails.OrderID = wOrder.ID;
				// 工序总数
				wAPSTaskPartDetails.StepSize = wPartPointIDList.size();
				// 工序完成数
				wAPSTaskPartDetails.StepFinish = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Done.getValue()).count();
				// 工序在制数
				wAPSTaskPartDetails.StepMaking = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Submit.getValue()
								|| p.Status == APSTaskStatus.ToAudit.getValue()
								|| p.Status == APSTaskStatus.Audited.getValue())
						.count();
				// 工序已排数
				wAPSTaskPartDetails.StepSchedule = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Issued.getValue()
								|| p.Status == APSTaskStatus.Started.getValue()
								|| p.Status == APSTaskStatus.Done.getValue())
						.count();

				if (wAreaStationList != null && wAreaStationList.size() > 0 && wAreaStationList.stream()
						.anyMatch(p -> p.StationID == wAPSTaskPart.PartID && p.WorkAreaID > 0)) {
					wAPSTaskPartDetails.AreaID = wAreaStationList.stream()
							.filter(p -> p.StationID == wAPSTaskPart.PartID && p.WorkAreaID > 0).findFirst()
							.get().WorkAreaID;
					wAPSTaskPartDetails.AreaName = APSConstans.GetBMSDepartmentName(wAPSTaskPartDetails.AreaID);
				}

				wDetailsList.add(wAPSTaskPartDetails);
				wAllStepList.addAll(wTaskStepList);
			}

			// 排序
			if (wDetailsList.size() > 0) {
				wDetailsList.sort(Comparator.comparing(APSTaskPartDetails::getPartNo)
						.thenComparing(APSTaskPartDetails::getAreaID).thenComparing(APSTaskPartDetails::getOrderNum)
						.thenComparing(APSTaskPartDetails::getShiftDate));
			}

			wResult.CustomResult.put("TaskPartList", wDetailsList);
			wResult.CustomResult.put("TaskStepList", wAllStepList);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private ServiceResult<List<OMSOrder>> GetOrderListAndTaskPartListByTaskStepList(BMSEmployee wLoginUser,
			List<APSTaskStep> wStepTaskList, List<FPCRoutePart> wFPCRoutePartList,
			List<FPCRoutePartPoint> wFPCRoutePartPointList) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		try {
			if (wStepTaskList == null || wStepTaskList.size() <= 0) {
				wResult.CustomResult.put("TaskPartList", new ArrayList<APSTaskPartDetails>());
				return wResult;
			}
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wOrderIDList = wStepTaskList.stream().map(p -> p.OrderID).distinct()
					.collect(Collectors.toList());
			List<Integer> wTaskPartIDList = wStepTaskList.stream().map(p -> p.TaskPartID).distinct()
					.collect(Collectors.toList());

			wResult.Result = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);
			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
					wTaskPartIDList, wErrorCode);

			List<Integer> wPartPointIDList = null;

			List<APSTaskPartDetails> wDetailsList = new ArrayList<APSTaskPartDetails>();
			APSTaskPartDetails wAPSTaskPartDetails = null;
			List<APSTaskStep> wTaskStepList = null;
			List<LFSWorkAreaStation> wAreaStationList = LFSServiceImpl.getInstance()
					.LFS_QueryWorkAreaStationList(wLoginUser).List(LFSWorkAreaStation.class);

			// 所有工序任务列表
			List<APSTaskStep> wAllTaskStepList = APSTaskStepDAO.getInstance().SelectListByTaskPartIDList(wLoginUser,
					wStepTaskList.stream().map(p -> p.TaskPartID).distinct().collect(Collectors.toList()), wErrorCode);

			int wRouteID = 0;
			for (APSTaskPart wAPSTaskPart : wTaskPartList) {
				OMSOrder wOrder = wResult.Result.stream().filter(p -> p.ID == wAPSTaskPart.OrderID).findFirst().get();
				if (wOrder != null && wOrder.ID > 0) {
					wRouteID = wOrder.RouteID;
				}
				// 工艺工序列表
				wPartPointIDList = APS_GetPartPointIDList(wFPCRoutePartList, wFPCRoutePartPointList,
						wAPSTaskPart.PartID, wRouteID);
				// 工序任务列表
				wTaskStepList = wAllTaskStepList.stream().filter(p -> p.TaskPartID == wAPSTaskPart.ID)
						.collect(Collectors.toList());
				// 工位详情
				wAPSTaskPartDetails = new APSTaskPartDetails();
				wAPSTaskPartDetails.APSTaskPart = wAPSTaskPart;
				// 工序总数
				wAPSTaskPartDetails.StepSize = wPartPointIDList.size();
				// 工序完成数
				wAPSTaskPartDetails.StepFinish = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Done.getValue()).count();
				// 工序在制数
				wAPSTaskPartDetails.StepMaking = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Submit.getValue()
								|| p.Status == APSTaskStatus.ToAudit.getValue()
								|| p.Status == APSTaskStatus.Audited.getValue())
						.count();
				// 工序已排数
				wAPSTaskPartDetails.StepSchedule = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Issued.getValue()
								|| p.Status == APSTaskStatus.Started.getValue()
								|| p.Status == APSTaskStatus.Done.getValue())
						.count();

				if (wAreaStationList != null && wAreaStationList.size() > 0 && wAreaStationList.stream()
						.anyMatch(p -> p.StationID == wAPSTaskPart.PartID && p.WorkAreaID > 0)) {
					wAPSTaskPartDetails.AreaID = wAreaStationList.stream()
							.filter(p -> p.StationID == wAPSTaskPart.PartID && p.WorkAreaID > 0).findFirst()
							.get().WorkAreaID;
				}

				wDetailsList.add(wAPSTaskPartDetails);
			}
			wResult.CustomResult.put("TaskPartList", wDetailsList);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取待竣工确认的订单列表
	 */
	@Override
	public ServiceResult<List<OMSOrder>> APS_QueryCompleteOrderList(BMSEmployee wLoginUser) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①获取所有维修中的订单列表
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "",
					1, new ArrayList<Integer>(Arrays.asList(OMSOrderStatus.Repairing.getValue())), null, null, null,
					null, wErrorCode);
			if (wOrderList == null || wOrderList.size() <= 0) {
				return wResult;
			}
			wResult.Result = wOrderList;
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_CompleteConfirm(BMSEmployee wLoginUser, int wOrderID, List<String> wImagePathList,
			String wRemark) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode += RetCode.SERVER_RST_ERROR_OUT;
				return wResult;
			}

			if (wOrder.Status != OMSOrderStatus.Repairing.getValue()) {
				wResult.FaultCode += "提示：请选择状态为“维修中”的订单!";
				return wResult;
			}

//			List<FPCProductRoute> wFPCProductRouteList = FMCServiceImpl.getInstance()
//					.FPC_QueryProductRouteList(wLoginUser, 0, 0, 0).List(FPCProductRoute.class);
			List<FPCRoutePart> wFPCRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(wLoginUser, 0).List(FPCRoutePart.class);
			List<FPCRoutePartPoint> wFPCRoutePartPointList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartPointListByRouteID(wLoginUser, 0, 0).List(FPCRoutePartPoint.class);

			List<Integer> wPartIDList = APSUtils.getInstance().APS_GetPartIDListByOrder(wLoginUser, wOrder,
					wFPCRoutePartList);
			if (wPartIDList == null || wPartIDList.size() <= 0) {
				wResult.FaultCode += "提示：该订单暂时未设置工艺路线!";
				return wResult;
			}

			int wRouteID = wOrder.RouteID;

			// 终检工位列表
			List<FPCPart> wQTFinals = FMCServiceImpl.getInstance()
					.FPC_QueryPartList(wLoginUser, -1, -1, -1, FPCPartTypes.QTFinally.getValue()).List(FPCPart.class);

			for (int wPartID : wPartIDList) {
				// ③根据订单、工位获取工位任务
				List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, -1,
						wOrder.LineID, wPartID, 1, APSShiftPeriod.Week.getValue(), null, -1, null, null, wErrorCode);
				if (wTaskPartList == null || wTaskPartList.size() <= 0) {
					wResult.FaultCode += StringUtils.Format("提示：【{0}】工位任务未下达!", APSConstans.GetFPCPart(wPartID).Name);
					return wResult;
				}
				// ④根据订单、工位获取所有工序ID集合
				List<Integer> wPartPointIDList = APS_GetPartPointIDList(wFPCRoutePartList, wFPCRoutePartPointList,
						wPartID, wRouteID);
				if (wPartPointIDList == null || wPartPointIDList.size() <= 0) {
					wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下无工序!", APSConstans.GetFPCPart(wPartID).Name);
					return wResult;
				}
				// ⑤根据工位任务获取所有工序任务列表
				List<APSTaskStep> wTaskStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1,
						wTaskPartList.get(0).ID, -1, -1, -1, -1, -1, 1, null, null, null, null, wErrorCode);
				if (wTaskStepList == null || wTaskStepList.size() <= 0) {
					wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下无工序任务!", APSConstans.GetFPCPart(wPartID).Name);
					return wResult;
				}
				// ⑥判断是否所有工序都有工序任务，且所有工序任务状态都为完工，若成立，则取，若不成立，则弃。
				if (!wPartPointIDList.stream().allMatch(p -> wTaskStepList.stream()
						.anyMatch(q -> p == q.StepID && q.Status == APSTaskStatus.Done.getValue()))) {
					Integer wStepID = wPartPointIDList.stream()
							.filter(p -> !(wTaskStepList.stream()
									.anyMatch(q -> p == q.StepID && q.Status == APSTaskStatus.Done.getValue())))
							.findFirst().get();
					wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下【{1}】工序未完工!",
							APSConstans.GetFPCPart(wPartID).Name, APSConstans.GetFPCPartPointName(wStepID));
					return wResult;
				}
				// ⑦判断此工位是否有质量工位触发，且该工位的工序任务全部完工
				if (wQTFinals != null && wQTFinals.size() > 0
						&& wQTFinals.stream().anyMatch(p -> p.QTPartID == wPartID)) {
					List<FPCPart> wTempPartList = wQTFinals.stream().filter(p -> p.QTPartID == wPartID)
							.collect(Collectors.toList());
					for (FPCPart wItem : wTempPartList) {
						// 获取该工位下所有工序
						List<Integer> wQTStepIDList = APSUtils.getInstance().FMC_QueryStepIDList(wLoginUser,
								wOrder.LineID, wItem.ID, wOrder.ProductID);
						if (wQTStepIDList == null || wQTStepIDList.size() <= 0) {
							wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下无工序!",
									APSConstans.GetFPCPartName(wItem.ID));
							return wResult;
						}
						for (int wStepID : wQTStepIDList) {
							List<APSTaskStep> wQTStepTaskList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1,
									wOrder.ID, -1, -1, -1, wOrder.LineID, wItem.ID, wStepID, -1, 1, null, null, null,
									null, wErrorCode);
							if (wQTStepTaskList == null || wQTStepIDList.size() <= 0) {
								wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下【{1}】工序未生成日计划!",
										APSConstans.GetFPCPartName(wItem.ID));
								return wResult;
							}
							if (wQTStepTaskList.stream().anyMatch(p -> p.Status != APSTaskStatus.Done.getValue())) {
								wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下【{1}】工序未完工!",
										APSConstans.GetFPCPartName(wItem.ID), APSConstans.GetFPCPartPointName(wStepID));
								return wResult;
							}

							// 判断所有不合格项是否发起不合格评审
							String wCheckResult = this.IsSendRepair(wLoginUser, wOrder, wItem.ID, wStepID);
							if (StringUtils.isNotEmpty(wCheckResult)) {
								wResult.FaultCode += wCheckResult;
								return wResult;
							}
						}
					}
				}
			}

			// ⑧检查该订单的不合格评审单是否全部完成
			int wNCRNumber = AndonDAO.getInstance().SelectNcrCountNF(wLoginUser, wOrder.ID, wErrorCode);
			if (wNCRNumber > 0) {
				wResult.FaultCode += "提示：该订单有未完成的不合格评审单!";
				return wResult;
			}
			// ⑨检查该订单的返修单是否全部完成
			int wRepairNumber = AndonDAO.getInstance().SelectRepairCountNF(wLoginUser, wOrder.ID, wErrorCode);
			if (wRepairNumber > 0) {
				wResult.FaultCode += "提示：该订单有未完成的返修单!";
				return wResult;
			}

			// ⑩生成竣工确认单，且将订单状态改为已完工
			List<SFCOrderForm> wOrderList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, "", 1,
					null, wErrorCode);
			if (wOrderList != null && wOrderList.size() > 0) {
				SFCOrderForm wSFCOrderForm = wOrderList.get(0);
				wSFCOrderForm.ConfirmID = wLoginUser.ID;
				wSFCOrderForm.ConfirmTime = Calendar.getInstance();
				wSFCOrderForm.Status = 2;
				if (wImagePathList != null && wImagePathList.size() > 0) {
					wSFCOrderForm.ImagePathList = wImagePathList;
				}
				if (StringUtils.isNotEmpty(wRemark)) {
					wSFCOrderForm.Remark = wRemark;
				}
				SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);

				wOrder.Status = OMSOrderStatus.FinishedWork.getValue();
				wOrder.RealFinishDate = Calendar.getInstance();
				OMSOrderDAO.getInstance().Update(BaseDAO.SysAdmin, wOrder, wErrorCode);
			} else {
				wResult.FaultCode += "提示：该订单的竣工确认单缺失!";
				return wResult;
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 判断某订单的终检工位、工序任务不合格检验项是否发起返修
	 * 
	 * @param wLoginUser
	 * @param wOrder
	 * @param wPartID
	 * @param wStepID
	 * @return
	 */
	private String IsSendRepair(BMSEmployee wLoginUser, OMSOrder wOrder, int wPartID, int wStepID) {
		String wResult = "";
		try {
			List<SFCTaskIPT> wIPTList = QMSServiceImpl.getInstance().SFC_QueryTaskIPTList(wLoginUser, wOrder.ID)
					.List(SFCTaskIPT.class);
			if (wIPTList == null || wIPTList.size() <= 0) {
				return wResult;
			}
			List<SFCTaskIPT> wTaskIPTList = wIPTList.stream().filter(
					p -> p.PartID == wPartID && p.PartPointID == wStepID && p.TaskType == SFCTaskType.Final.getValue())
					.collect(Collectors.toList());
			if (wTaskIPTList == null || wTaskIPTList.size() <= 0) {
				return wResult;
			}

			for (SFCTaskIPT wSFCTaskIPT : wTaskIPTList) {
				List<IPTValue> wValueList = QMSServiceImpl.getInstance().IPT_QueryValueList(wLoginUser, wSFCTaskIPT.ID)
						.List(IPTValue.class);
				if (wValueList == null || wValueList.size() <= 0) {
					continue;
				}
				List<IPTValue> wNotQualityValues = wValueList.stream().filter(p -> p.Result == 2)
						.collect(Collectors.toList());
				if (wNotQualityValues == null || wNotQualityValues.size() <= 0) {
					continue;
				}

				for (IPTValue wIPTValue : wNotQualityValues) {
					if (!MyHelperServiceImpl.getInstance().WDW_IsSendRepair(wLoginUser, wSFCTaskIPT.ID,
							(int) wIPTValue.IPTItemID).Result) {
						wResult = StringUtils.Format("提示：【{0}】工序中有未发起返修的不合格项!", wSFCTaskIPT.PartPointName);
						return wResult;
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSOrder>> APS_QueryOutApplyOrderList(BMSEmployee wLoginUser) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance()
					.SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "", 1,
							new ArrayList<Integer>(Arrays.asList(OMSOrderStatus.FinishedWork.getValue(),
									OMSOrderStatus.ToOutChcek.getValue(), OMSOrderStatus.ToOutConfirm.getValue(),
									OMSOrderStatus.CarSended.getValue())),
							null, null, null, null, wErrorCode);
			if (wOrderList == null || wOrderList.size() <= 0) {
				wResult.CustomResult.put("OrderMap", new HashMap<Integer, Boolean>());
				return wResult;
			}

			Map<Integer, SFCOrderForm> wMap = new HashMap<Integer, SFCOrderForm>();
			for (OMSOrder wOMSOrder : wOrderList) {
				List<SFCOrderForm> wList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOMSOrder.ID, "", 2,
						null, wErrorCode);
				if (wList == null || wList.size() <= 0) {
					continue;
				} else {
					wMap.put(wOMSOrder.ID, wList.get(0));
				}
			}

			wResult.Result = wOrderList;
			wResult.CustomResult.put("OrderMap", wMap);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_OutApply(BMSEmployee wLoginUser, int wOrderID, List<String> wImagePathList,
			String wRemark) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode += RetCode.SERVER_RST_ERROR_OUT;
				return wResult;
			}

			// ①判断是否是非线上车
			boolean wFlag = OMSOrderDAO.getInstance().IsOffLineBoarding(wLoginUser, wOrderID, wErrorCode);
			if (wFlag) {
				if (!IsInPlant(wLoginUser, wOrder)) {
					wResult.FaultCode += StringUtils.Format("提示：【{0}】该车不在厂线上，无法出厂!", wOrder.PartNo);
					return wResult;
				}

				OutPlantOffLine(wLoginUser, wOrderID, wImagePathList, wRemark, wOrder);

				return wResult;
			}

			// ⑧检查该订单的不合格评审单是否全部完成
			int wNCRNumber = AndonDAO.getInstance().SelectNcrCountNF(wLoginUser, wOrder.ID, wErrorCode);
			if (wNCRNumber > 0) {
				wResult.FaultCode += "提示：该订单有未完成的不合格评审单!";
				return wResult;
			}
			// ⑨检查该订单的返修单是否全部完成
			int wRepairNumber = AndonDAO.getInstance().SelectRepairCountNF(wLoginUser, wOrder.ID, wErrorCode);
			if (wRepairNumber > 0) {
				wResult.FaultCode += "提示：该订单有未完成的返修单!";
				return wResult;
			}

			List<SFCOrderForm> wFormList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, "", 2,
					null, wErrorCode);
			if (wFormList != null && wFormList.size() > 0) {
				wResult.FaultCode += "提示：该订单已申请出厂!";
				return wResult;
			}

			// 触发普查工位的日计划
			List<FPCPart> wOutFPartList = FMCServiceImpl.getInstance()
					.FPC_QueryPartList(wLoginUser, -1, -1, -1, FPCPartTypes.OutFactory.getValue()).List(FPCPart.class);
			if (wOutFPartList == null || wOutFPartList.size() <= 0) {
				wResult.FaultCode += "提示：普查工位未设置!";
				return wResult;
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			for (FPCPart wFPCPart : wOutFPartList) {
				List<Integer> wStepIDList = APSUtils.getInstance().FMC_QueryStepIDList(wLoginUser, wOrder.LineID,
						wFPCPart.ID, wOrder.ProductID);
				if (wStepIDList == null || wStepIDList.size() <= 0) {
					wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下无工序!", wFPCPart.Name);
					return wResult;
				}

				APSTaskStep wAPSTaskStep;
				for (Integer wStepID : wStepIDList) {
					wAPSTaskStep = new APSTaskStep();
					wAPSTaskStep.ID = 0;
					wAPSTaskStep.Active = 1;
					wAPSTaskStep.CreateTime = Calendar.getInstance();
					wAPSTaskStep.LineID = wOrder.LineID;
					wAPSTaskStep.OrderID = wOrder.ID;
					wAPSTaskStep.PartID = wFPCPart.ID;
					wAPSTaskStep.PartNo = wOrder.PartNo;
					wAPSTaskStep.ProductNo = wOrder.ProductNo;
					wAPSTaskStep.ShiftID = wShiftID;
					wAPSTaskStep.Status = APSTaskStatus.Issued.getValue();
					wAPSTaskStep.StepID = wStepID;
					wAPSTaskStep.IsDispatched = false;
					APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
				}
			}

			// 生成出厂申请单
			SFCOrderForm wSFCOrderForm = new SFCOrderForm();
			wSFCOrderForm.CreateID = wLoginUser.ID;
			wSFCOrderForm.CreateTime = Calendar.getInstance();
			wSFCOrderForm.ID = 0;
			wSFCOrderForm.Type = 2;
			wSFCOrderForm.OrderID = wOrder.ID;
			wSFCOrderForm.OrderNo = wOrder.OrderNo;
			wSFCOrderForm.PartNo = wOrder.PartNo;
			wSFCOrderForm.Status = 1;
			if (wImagePathList != null && wImagePathList.size() > 0) {
				wSFCOrderForm.ImagePathList = wImagePathList;
			}
			if (StringUtils.isNotEmpty(wRemark)) {
				wSFCOrderForm.Remark = wRemark;
			}
			wResult.Result = SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);

			// 修改订单的状态为待出厂普查
			wOrder.Status = OMSOrderStatus.ToOutChcek.getValue();
			OMSOrderDAO.getInstance().Update(BaseDAO.SysAdmin, wOrder, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 非线上车直接出厂
	 */
	private void OutPlantOffLine(BMSEmployee wLoginUser, int wOrderID, List<String> wImagePathList, String wRemark,
			OMSOrder wOrder) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 生成出厂申请单
			SFCOrderForm wSFCOrderForm = new SFCOrderForm();

			wSFCOrderForm.CreateID = wLoginUser.ID;
			wSFCOrderForm.CreateTime = Calendar.getInstance();
			wSFCOrderForm.ID = 0;
			wSFCOrderForm.Type = 2;
			wSFCOrderForm.OrderID = wOrder.ID;
			wSFCOrderForm.OrderNo = wOrder.OrderNo;
			wSFCOrderForm.PartNo = wOrder.PartNo;
			wSFCOrderForm.Status = 2;
			wSFCOrderForm.ConfirmID = wLoginUser.ID;
			wSFCOrderForm.ConfirmTime = Calendar.getInstance();
			if (wImagePathList != null && wImagePathList.size() > 0) {
				wSFCOrderForm.ImagePathList = wImagePathList;
			}
			if (StringUtils.isNotEmpty(wRemark)) {
				wSFCOrderForm.Remark = wRemark;
			}

			SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);

			// 修改订单的状态为待出厂普查
			wOrder.Status = OMSOrderStatus.CarSended.getValue();
			wOrder.RealSendDate = Calendar.getInstance();
			OMSOrderDAO.getInstance().Update(BaseDAO.SysAdmin, wOrder, wErrorCode);

			// 清掉台位上的车(待出厂)
			OMSOrderDAO.getInstance().ClearCar(wLoginUser, wOrder);

			// 推送订单到SAP
			String wFlag = Configuration.readConfigString("isSendSAP", "config/config");
			if (wFlag.equals("1")) {
				SendToSAP(wLoginUser, wOrder);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<Integer> APS_OutConfirm(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode += RetCode.SERVER_RST_ERROR_OUT;
				return wResult;
			}

			// 判断车号是否在厂线上
//			if (!IsInPlant(wLoginUser, wOrder)) {
//				wResult.FaultCode += StringUtils.Format("提示：【{0}】该车不在厂线上，无法出厂!", wOrder.PartNo);
//				return wResult;
//			}
//
//			// ⑧检查该订单的不合格评审单是否全部完成
//			int wNCRNumber = AndonDAO.getInstance().SelectNcrCountNF(wLoginUser, wOrder.ID, wErrorCode);
//			if (wNCRNumber > 0) {
//				wResult.FaultCode += "提示：该订单有未完成的不合格评审单!";
//				return wResult;
//			}
//			// ⑨检查该订单的返修单是否全部完成
//			int wRepairNumber = AndonDAO.getInstance().SelectRepairCountNF(wLoginUser, wOrder.ID, wErrorCode);
//			if (wRepairNumber > 0) {
//				wResult.FaultCode += "提示：该订单有未完成的返修单!";
//				return wResult;
//			}

			List<SFCOrderForm> wFormList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, "", 2,
					null, wErrorCode);
			if (wFormList == null || wFormList.size() <= 0) {
				wResult.FaultCode += "提示：该订单的出厂申请单缺失!";
				return wResult;
			}

			// 触发普查工位的日计划
//			List<FPCPart> wOutFPartList = FMCServiceImpl.getInstance()
//					.FPC_QueryPartList(wLoginUser, -1, -1, -1, FPCPartTypes.OutFactory.getValue()).List(FPCPart.class);
//			if (wOutFPartList == null || wOutFPartList.size() <= 0) {
//				wResult.FaultCode += "提示：普查工位未设置!";
//				return wResult;
//			}
//
//			for (FPCPart wFPCPart : wOutFPartList) {
//				List<Integer> wStepIDList = APSUtils.getInstance().FMC_QueryStepIDList(wLoginUser, wOrder.LineID,
//						wFPCPart.ID, wOrder.ProductID);
//				if (wStepIDList == null || wStepIDList.size() <= 0) {
//					wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下无工序!", wFPCPart.Name);
//					return wResult;
//				}
//
//				for (int wStepID : wStepIDList) {
//					List<APSTaskStep> wStepTaskList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID,
//							-1, -1, -1, wOrder.LineID, wFPCPart.ID, wStepID, -1, 1, null, null, null, null, wErrorCode);
//					if (wStepTaskList == null || wStepTaskList.size() <= 0) {
//						wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下【{1}】工序任务未生成!", wFPCPart.Name,
//								APSConstans.GetFPCPartPointName(wStepID));
//						return wResult;
//					}
//					if (wStepTaskList.stream().anyMatch(p -> p.Status != APSTaskStatus.Done.getValue())) {
//						wResult.FaultCode += StringUtils.Format("提示：【{0}】工位下【{1}】工序任务未完成!", wFPCPart.Name,
//								APSConstans.GetFPCPartPointName(wStepID));
//						return wResult;
//					}
//				}
//			}

			// 修改出厂申请单的状态
			SFCOrderForm wSFCOrderForm = wFormList.get(0);
			wSFCOrderForm.ConfirmID = wLoginUser.ID;
			wSFCOrderForm.ConfirmTime = Calendar.getInstance();
			wSFCOrderForm.Status = 2;
			SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);
			// 修改订单的状态
			wOrder.RealSendDate = Calendar.getInstance();
			wOrder.Status = OMSOrderStatus.CarSended.getValue();
			OMSOrderDAO.getInstance().Update(BaseDAO.SysAdmin, wOrder, wErrorCode);

			// 清掉台位上的车(待出厂)
			OMSOrderDAO.getInstance().ClearCar(wLoginUser, wOrder);

			// 推送订单到SAP
			String wFlag = Configuration.readConfigString("isSendSAP", "config/config");
			if (wFlag.equals("1")) {
				SendToSAP(wLoginUser, wOrder);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 推送给SAP
	 */
	private void SendToSAP(BMSEmployee wLoginUser, OMSOrder wOrder) {
		try {
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");

			Calendar wTime = Calendar.getInstance();
			wTime.set(2010, 0, 1);

			List<OrderItem> wItemList = new ArrayList<OrderItem>();

			OrderItem wItem = new OrderItem();
			wItem.PSPNR = wOrder.OrderNo;
			wItem.ZCHEX = wOrder.ProductNo;
			wItem.ZTCH = wOrder.PartNo.split("#")[1];
			wItem.ZRC_DATE = wSDF.format(wOrder.RealReceiveDate.getTime());

			if (wOrder.RealFinishDate.compareTo(wTime) <= 0) {
				wItem.ZWG_DATE = "";
			} else {
				wItem.ZWG_DATE = wSDF.format(wOrder.RealFinishDate.getTime());
			}

			wItemList.add(wItem);

			String wINPUT2 = JSON.toJSONString(wItemList);

			String wEndPoint = APSUtils.getInstance().GetSAPEndpoint();
			org.apache.axis.client.Service wService = new org.apache.axis.client.Service();
			Call wCall = (Call) wService.createCall();

			wCall.setUsername(APSUtils.getInstance().GetSAPUser());
			wCall.setPassword(APSUtils.getInstance().GetSAPPassword());

			wCall.setTargetEndpointAddress(wEndPoint);
			wCall.setOperationName(new QName("urn:sap-com:document:sap:rfc:functions", "ZIF_MES_INPUT_001"));// 调用的方法名
			wCall.addParameter("INPUT1", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.addParameter("INPUT2", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.setReturnType(XMLType.XSD_STRING); // 返回值类型：String

			String wReturn = (String) wCall.invoke(new Object[] { "02", wINPUT2 });// 远程调用
			logger.info(wReturn);

			CalendarDAO.getInstance().WriteLogFile(wINPUT2 + "::" + wReturn, "台车WBS", wOrder);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 判断车是否在厂线上
	 * 
	 * @param wLoginUser
	 * @param wOrder
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean IsInPlant(BMSEmployee wLoginUser, OMSOrder wOrder) {
		boolean wResult = false;
		try {
			List<FMCWorkspace> wList = FMCServiceImpl.getInstance()
					.FMC_GetFMCWorkspaceList(wLoginUser, -1, -1, "", -1, 1).List(FMCWorkspace.class);
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			if (!wList.stream().anyMatch(p -> p.PartNo.equals(wOrder.PartNo))) {
				return wResult;
			}

			FMCWorkspace wWorkSpace = wList.stream().filter(p -> p.PartNo.equals(wOrder.PartNo)).findFirst().get();
			List<LFSStoreHouse> wHouseList = LFSServiceImpl.getInstance().LFS_QueryStoreHouseList(wLoginUser)
					.List(LFSStoreHouse.class);
			if (wHouseList == null || wHouseList.size() <= 0) {
				return wResult;
			}

			if (!wHouseList.stream().anyMatch(p -> p.ID == wWorkSpace.ParentID)) {
				return wResult;
			}

			LFSStoreHouse wHouse = wHouseList.stream().filter(p -> p.ID == wWorkSpace.ParentID).findFirst().get();

			if (wHouse.Type == 2) {
				wResult = true;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_AutoSendMessageToApplyOutPlant(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 每日8-9点开始提示
			Calendar wTStart = Calendar.getInstance();
			wTStart.set(Calendar.HOUR_OF_DAY, 8);
			wTStart.set(Calendar.MINUTE, 0);
			wTStart.set(Calendar.SECOND, 0);
			Calendar wTEnd = Calendar.getInstance();
			wTEnd.set(Calendar.HOUR_OF_DAY, 9);
			wTEnd.set(Calendar.MINUTE, 0);
			wTEnd.set(Calendar.SECOND, 0);
			if (!(Calendar.getInstance().compareTo(wTStart) > 0 && Calendar.getInstance().compareTo(wTEnd) < 0)) {
				return wResult;
			}
			// ①查询出已完工订单列表
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "",
					1, new ArrayList<Integer>(Arrays.asList(OMSOrderStatus.FinishedWork.getValue())), null, null, null,
					null, wErrorCode);
			if (wOrderList == null || wOrderList.size() <= 0) {
				return wResult;
			}

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			// ②遍历订单查询该订单是否已发起出厂申请，若没有，查消息列表，若没有发送消息提示，发送消息提示给所有有权限申请出厂的人
			List<BFCMessage> wMessageList = null;
//			List<BMSRoleItem> wRoleItemList = null;
			List<BFCMessage> wNewList = new ArrayList<BFCMessage>();
			BFCMessage wBFCMessage = null;
			for (OMSOrder wOMSOrder : wOrderList) {
				wMessageList = CoreServiceImpl.getInstance()
						.BFC_GetMessageList(wLoginUser, -1, BPMEventModule.OutPlantApply.getValue(),
								BFCMessageType.Notify.getValue(), -1, wShiftID, null, null)
						.List(BFCMessage.class);
				if (wMessageList != null && wMessageList.size() > 0
						&& wMessageList.stream().anyMatch(p -> p.MessageID == wOMSOrder.ID)) {
					continue;
				}
				// ③根据出厂申请权限码获取人员列表
//				wRoleItemList = CoreServiceImpl.getInstance().BMS_UserAllByFunction(wLoginUser, 500904)
//						.List(BMSRoleItem.class);
//				if (wRoleItemList == null || wRoleItemList.size() <= 0) {
//					continue;
//				}

				List<Integer> wUserIDList = GetNoticeUesrList(wLoginUser);

				for (int wUserID : wUserIDList) {
					wBFCMessage = new BFCMessage();
					wBFCMessage.Active = 0;
					wBFCMessage.CompanyID = wLoginUser.CompanyID;
					wBFCMessage.CreateTime = Calendar.getInstance();
					wBFCMessage.EditTime = Calendar.getInstance();
					wBFCMessage.ID = 0;
					wBFCMessage.MessageID = wOMSOrder.ID;
					wBFCMessage.MessageText = StringUtils.Format("提示：【{0}-{1}】该订单已竣工确认，可发起出厂申请!", wOMSOrder.OrderNo,
							wOMSOrder.PartNo);
					wBFCMessage.ModuleID = BPMEventModule.OutPlantApply.getValue();
					wBFCMessage.ResponsorID = wUserID;
					wBFCMessage.ShiftID = wShiftID;
					wBFCMessage.Title = StringUtils.Format("提示：【{0}-{1}】出厂申请通知!", wOMSOrder.OrderNo, wOMSOrder.PartNo);
					wBFCMessage.Type = BFCMessageType.Notify.getValue();
					wNewList.add(wBFCMessage);
				}
			}
			// ④发送通知消息
			if (wNewList != null && wNewList.size() > 0) {
				CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wNewList);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据权限获取出厂申请通知人列表
	 */
	private List<Integer> GetNoticeUesrList(BMSEmployee wLoginUser) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			// ①计划员
			List<BMSRoleItem> wUList = CoreServiceImpl.getInstance().BMS_UserAllByRoleID(wLoginUser, 1)
					.List(BMSRoleItem.class);
			List<Integer> wList = wUList.stream().map(p -> p.FunctionID).distinct().collect(Collectors.toList());
			if (wList != null && wList.size() > 0) {
				wResult.addAll(wList);
			}
			// ②工区主管
			List<BMSRoleItem> wUList1 = CoreServiceImpl.getInstance().BMS_UserAllByRoleID(wLoginUser, 3)
					.List(BMSRoleItem.class);
			List<Integer> wList1 = wUList1.stream().map(p -> p.FunctionID).distinct().collect(Collectors.toList());
			if (wList1 != null && wList1.size() > 0) {
				wResult.addAll(wList1);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSOrder>> OMS_QueryInPlantList(BMSEmployee wLoginUser, int wType) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<OMSOrder> wList = new ArrayList<OMSOrder>();

			switch (wType) {
			case 1:
				wList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "", 1,
						StringUtils.parseListArgs(OMSOrderStatus.EnterFactoryed.getValue(),
								OMSOrderStatus.Repairing.getValue(), OMSOrderStatus.FinishedWork.getValue(),
								OMSOrderStatus.ToOutChcek.getValue(), OMSOrderStatus.ToOutConfirm.getValue()),
						null, null, null, null, wErrorCode);

				wList.sort((o1, o2) -> o2.RealReceiveDate.compareTo(o1.RealReceiveDate));
				break;
			default:
				wList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", -1, -1, -1, "", "", 1,
						StringUtils.parseListArgs(OMSOrderStatus.ReceivedTelegraph.getValue()), null, null, null, null,
						wErrorCode);

				wList.sort((o1, o2) -> o2.PlanReceiveDate.compareTo(o1.PlanReceiveDate));
				break;
			}

			if (wList == null || wList.size() <= 0) {
				wResult.CustomResult.put("OrderMap", new HashMap<Integer, SFCOrderForm>());
				return wResult;
			}

			Map<Integer, SFCOrderForm> wOrderMap = new HashMap<Integer, SFCOrderForm>();
			for (OMSOrder wOMSOrder : wList) {
				List<SFCOrderForm> wFormList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOMSOrder.ID,
						"", SFCOrderFormType.InConfirm.getValue(), null, wErrorCode);
				if (wFormList == null || wFormList.size() <= 0) {
					continue;
				}
				wOrderMap.put(wOMSOrder.ID, wFormList.get(0));
			}
			wResult.Result = wList;
			wResult.CustomResult.put("OrderMap", wOrderMap);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_InPlantConfirm(BMSEmployee wLoginUser, int wOrderID, List<String> wImagePathList,
			String wRemark) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode += RetCode.SERVER_RST_ERROR_OUT;
				return wResult;
			}

			if (wOrder.Status != OMSOrderStatus.ReceivedTelegraph.getValue()) {
				wResult.FaultCode += "提示：请选择状态为“已收电报”的订单!";
				return wResult;
			}

			// 创建进厂确认单
			SFCOrderForm wSFCOrderForm = new SFCOrderForm(0, wOrderID, wOrder.OrderNo, wOrder.PartNo, wLoginUser.ID,
					wLoginUser.Name, Calendar.getInstance(), wLoginUser.ID, wLoginUser.Name, Calendar.getInstance(), 2,
					SFCOrderFormType.InConfirm.getValue(), wOrder.LineName, wOrder.ProductNo, wOrder.BureauSection);
			if (wImagePathList != null && wImagePathList.size() > 0) {
				wSFCOrderForm.ImagePathList = wImagePathList;
			}
			if (StringUtils.isNotEmpty(wRemark)) {
				wSFCOrderForm.Remark = wRemark;
			}
			wResult.Result = SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);
			// 修改订单状态为已进场
			wOrder.Status = OMSOrderStatus.EnterFactoryed.getValue();
			wOrder.RealReceiveDate = Calendar.getInstance();
			OMSOrderDAO.getInstance().Update(wLoginUser, wOrder, wErrorCode);

			// 将车绑定到厂线上(分多节情况)
			List<MTCSectionInfo> wSectionList = WDWServiceImpl.getInstance()
					.MTC_QuerySectionInfoList(wLoginUser, wOrder.ProductID).List(MTCSectionInfo.class);
			if (wSectionList != null && wSectionList.size() > 1) {
				for (MTCSectionInfo wSectionInfo : wSectionList) {
					String wPNo = StringUtils.Format("{0}-{1}", wOrder.PartNo, wSectionInfo.Name);

					WDWServiceImpl.getInstance().WDW_BindCarToPlant(wLoginUser, wPNo);
				}
			} else {
				WDWServiceImpl.getInstance().WDW_BindCarToPlant(wLoginUser, wOrder.PartNo);
			}

			// 创建并推送台车BOM
//			APSConstans.mIBomOrderList = new ArrayList<Integer>(Arrays.asList(wOrderID));

			ExecutorService wES = Executors.newFixedThreadPool(1);
			wES.submit(() -> APSServiceImpl.getInstance().APS_CreateBomItemByOrder(BaseDAO.SysAdmin, wOrderID));
			wES.shutdown();

			// 创建并推送订单
//			APSConstans.mUOrderList = new ArrayList<OMSOrder>(Arrays.asList(wOrder));

//			ExecutorService wES1 = Executors.newFixedThreadPool(1);
//			wES1.submit(() -> SendToSAP(BaseDAO.SysAdmin, wOrder));
//			wES1.shutdown();

			// 修改订单的工艺路线为最新的
			ExecutorService wES2 = Executors.newFixedThreadPool(1);
			wES2.submit(() -> UpdateRoute(wLoginUser, new ArrayList<OMSOrder>(Arrays.asList(wOrder))));
			wES2.shutdown();
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 工位返修频率提示
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Integer> APS_StationRepairFrequencyTip(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 每日8-9点开始提示
			Calendar wTStart = Calendar.getInstance();
			wTStart.set(Calendar.HOUR_OF_DAY, 8);
			wTStart.set(Calendar.MINUTE, 0);
			wTStart.set(Calendar.SECOND, 0);
			Calendar wTEnd = Calendar.getInstance();
			wTEnd.set(Calendar.HOUR_OF_DAY, 9);
			wTEnd.set(Calendar.MINUTE, 0);
			wTEnd.set(Calendar.SECOND, 0);
			if (!(Calendar.getInstance().compareTo(wTStart) > 0 && Calendar.getInstance().compareTo(wTEnd) < 0)) {
				return wResult;
			}
			// ①查询出所有下达的、开工的且激活的工位计划
			List<APSTaskPart> wAllToDoList = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1, 1,
					APSShiftPeriod.Week.getValue(),
					new ArrayList<Integer>(
							Arrays.asList(APSTaskStatus.Issued.getValue(), APSTaskStatus.Started.getValue())),
					-1, null, null, wErrorCode);
			if (wAllToDoList.size() <= 0) {
				return wResult;
			}
			// ②去除开始时间大于明日的工位计划
			Calendar wTomTime = Calendar.getInstance();
			wTomTime.add(Calendar.DATE, 1);
			wTomTime.set(Calendar.HOUR_OF_DAY, 23);
			wTomTime.set(Calendar.MINUTE, 59);
			wTomTime.set(Calendar.SECOND, 59);
			wAllToDoList.removeIf(p -> p.StartTime.compareTo(wTomTime) > 0);
			// ③去除重复的工位计划
			wAllToDoList = new ArrayList<APSTaskPart>(wAllToDoList.stream()
					.collect(Collectors.toMap(APSTaskPart::getID, account -> account, (k1, k2) -> k2)).values());
			List<BFCMessage> wMessageList = new ArrayList<BFCMessage>();
			BFCMessage wMessage = null;
			// APS
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			// ④遍历工位计划，通过工位、车型、修程获取返修频率
			for (APSTaskPart wAPSTaskPart : wAllToDoList) {
				// 判断此工位任务今日是否已发送了消息，若发送了消息，continue
				List<BFCMessage> wTempMessageList = CoreServiceImpl.getInstance()
						.BFC_GetMessageList(wLoginUser, -1, BPMEventModule.StationTip.getValue(),
								BFCMessageType.Notify.getValue(), -1, wShiftID, null, null)
						.List(BFCMessage.class);
				if (wTempMessageList != null && wTempMessageList.size() > 0
						&& wTempMessageList.stream().anyMatch(p -> p.MessageID == wAPSTaskPart.ID)) {
					continue;
				}

				Map<String, Integer> wRepairMap = WDWServiceImpl.getInstance().RRO_QueryItemTaskList(wLoginUser,
						APSConstans.GetFPCProducID(wAPSTaskPart.ProductNo), wAPSTaskPart.LineID, wAPSTaskPart.PartID, 5)
						.Info(Map.class);
				if (wRepairMap == null || wRepairMap.size() <= 0) {
					continue;
				}
				// ⑤根据工位获取工区
				if (APSConstans.GetLFSWorkAreaStationList().values().stream()
						.anyMatch(p -> p.StationID == wAPSTaskPart.PartID)) {
					int wAreaID = APSConstans.GetLFSWorkAreaStationList().values().stream()
							.filter(p -> p.StationID == wAPSTaskPart.PartID).findFirst().get().WorkAreaID;
					if (wAreaID <= 0) {
						continue;
					}
					// ⑥根据工区获取工区主管
					if (APSConstans.GetLFSWorkAreaCheckerList().stream().anyMatch(
							p -> p.WorkAreaID == wAreaID && p.LeaderIDList != null && p.LeaderIDList.size() > 0)) {
						List<Integer> wLeaderList = new ArrayList<Integer>();
						List<LFSWorkAreaChecker> wLFSCheckerList = APSConstans
								.GetLFSWorkAreaCheckerList().stream().filter(p -> p.WorkAreaID == wAreaID
										&& p.LeaderIDList != null && p.LeaderIDList.size() > 0)
								.collect(Collectors.toList());
						for (LFSWorkAreaChecker wLFSWorkAreaChecker : wLFSCheckerList) {
							wLeaderList.addAll(wLFSWorkAreaChecker.LeaderIDList);
						}
						wLeaderList = wLeaderList.stream().distinct().collect(Collectors.toList());
						if (wLeaderList == null || wLeaderList.size() <= 0) {
							continue;
						}
						// ⑦创建发送给工区主管的通知消息
						for (Integer wLeaderID : wLeaderList) {
							wMessage = new BFCMessage();
							wMessage.Active = 0;
							wMessage.CompanyID = 0;
							wMessage.CreateTime = Calendar.getInstance();
							wMessage.EditTime = Calendar.getInstance();
							wMessage.ID = 0;
							wMessage.MessageID = wAPSTaskPart.ID;
							wMessage.Title = StringUtils.Format("【{0}】【{1}】工位 以往返修项频率提示 ",
									BPMEventModule.StationTip.getLable(),
									APSConstans.GetFPCPartName(wAPSTaskPart.PartID));
							wMessage.MessageText = this.GetMessageText(wRepairMap, wAPSTaskPart);
							wMessage.ModuleID = BPMEventModule.StationTip.getValue();
							wMessage.ResponsorID = wLeaderID;
							wMessage.ShiftID = wShiftID;
							wMessage.StationID = wAPSTaskPart.PartID;
							wMessage.Type = BFCMessageType.Notify.getValue();
							wMessageList.add(wMessage);
						}
					}
				}
			}
			// ⑧批量发送通知消息
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wMessageList);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private String GetMessageText(Map<String, Integer> wRepairMap, APSTaskPart wAPSTaskPart) {
		String wResult = "";
		try {
			if (wRepairMap == null || wRepairMap.size() <= 0) {
				wRepairMap = new HashMap<>();
			}

			StringBuffer wSB = new StringBuffer();
			wSB.append(StringUtils.Format("【{0}】-【{1}】-【{2}】以往返修项频率：",
					new Object[] { wAPSTaskPart.LineName, wAPSTaskPart.ProductNo, wAPSTaskPart.PartName }));
			for (String wContent : wRepairMap.keySet()) {
				if (StringUtils.isEmpty(wContent)) {
					continue;
				}
				wSB.append(StringUtils.Format("返修项：【{0}】--频次：【{1}】    ",
						new Object[] { wContent, wRepairMap.get(wContent) }));
			}
			wResult = wSB.toString();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public ServiceResult<List<APSTaskPart>> APS_QueryCompareList(BMSEmployee wLoginUser, List<Integer> wOrderIDList) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<>();
		wResult.Result = new ArrayList<>();
		OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));
		try {
			if (wOrderIDList == null || wOrderIDList.size() <= 0) {
				wResult.FaultCode += "提示：订单不能为空!";
				return wResult;
			}

			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByOrderIDList(wLoginUser,
					APSShiftPeriod.Week.getValue(), null, wOrderIDList, wErrorCode);

			List<APSTaskPart> wMonthTaskPartList = APSTaskPartDAO.getInstance().SelectListByOrderIDList(wLoginUser,
					APSShiftPeriod.Month.getValue(), null, wOrderIDList, wErrorCode);

			for (APSTaskPart apsTaskPart : wTaskPartList) {
				if (apsTaskPart.Active != 1)
					continue;
				for (APSTaskPart wMonthTaskPart : wMonthTaskPartList) {
					if (wMonthTaskPart.Active != 1)
						continue;
					if (apsTaskPart.OrderID != wMonthTaskPart.OrderID || apsTaskPart.PartID != wMonthTaskPart.PartID) {
						continue;
					}

					apsTaskPart.StartTime = wMonthTaskPart.StartTime;
					apsTaskPart.EndTime = wMonthTaskPart.EndTime;
				}
			}

			Map<Integer, List<FPCRoutePart>> wRoutePartListMap = new HashMap<>();

			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			List<FPCRoutePart> wPartList = null;
			for (OMSOrder wOMSOrder : wOrderList) {

				if (wRoutePartListMap.containsKey(wOMSOrder.RouteID))
					continue;
				wPartList = CoreServiceImpl.getInstance().FPC_QueryRoutePartList(wLoginUser, wOMSOrder.RouteID)
						.List(FPCRoutePart.class);
				if (wPartList != null && wPartList.size() > 0) {
					wRoutePartListMap.put(wOMSOrder.RouteID, wPartList);
				}
			}

			wResult.CustomResult.put("LineID", wOrderList.get(0).LineID);

			int wFlag = 1;
			int wOrderFlag = 1;
			List<APSTaskPart> wTreeList = new ArrayList<>();

			List<FPCRoutePart> wRoutePartList = new ArrayList<>();
			for (int i : wRoutePartListMap.keySet()) {
				wRoutePartList.addAll(wRoutePartListMap.get(i));
			}

			List<LFSWorkAreaStation> wWorkAreaStationList = (List<LFSWorkAreaStation>) APSConstans
					.GetLFSWorkAreaStationList().values().stream().collect(Collectors.toList());
			wWorkAreaStationList.removeIf(p -> !wRoutePartList.stream().anyMatch(q -> q.PartID == p.StationID));
			wWorkAreaStationList.removeIf(p -> !wTaskPartList.stream().anyMatch(q -> q.PartID == p.StationID));

			wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));

			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {

				APSTaskPart wAPSTaskPart = new APSTaskPart();
				wAPSTaskPart.PartID = wItem.StationID;
				wAPSTaskPart.PartOrder = wOrderFlag++;
				wAPSTaskPart.PartName = APSConstans.GetFPCPartName(wItem.StationID);

				wAPSTaskPart.UniqueID = wFlag++;
				wTreeList.add(wAPSTaskPart);

				for (FMCLine wLine : APSConstans.GetFMCLineList().values()) {
					if (wLine.Active != 1)
						continue;
					APSTaskPart wLinePart = new APSTaskPart();
					wLinePart.UniqueID = wFlag++;
					wLinePart.LineID = wLine.ID;
					wLinePart.LineName = wLine.Name;
					wAPSTaskPart.TaskPartList.add(wLinePart);

					wLinePart.TaskPartList = wTaskPartList.stream()
							.filter(p -> (p.PartID == wItem.StationID && p.LineID == wLine.ID))
							.collect(Collectors.toList());
					if (wLinePart.TaskPartList != null && wLinePart.TaskPartList.size() > 0) {
						for (APSTaskPart wItemPart : wLinePart.TaskPartList) {
							wItemPart.UniqueID = wFlag++;
							wItemPart.PartOrder = wAPSTaskPart.PartOrder;
						}
						continue;
					}
					wLinePart.TaskPartList = new ArrayList<>();
					wLinePart.TaskPartList.add(new APSTaskPart());
				}
			}

			wResult.CustomResult.put("TreeList", wTreeList);
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			Calendar wStartTime = wBaseTime;
			Calendar wEndTime = wBaseTime;

			if (wTaskPartList != null && wTaskPartList.size() > 0) {
				wStartTime = ((APSTaskPart) wTaskPartList.stream().min(Comparator.comparing(APSTaskPart::getStartTime))
						.get()).StartTime;
				Calendar wTime1 = ((APSTaskPart) wTaskPartList.stream()
						.max(Comparator.comparing(APSTaskPart::getEndTime)).get()).getEndTime();
				Calendar wTime2 = ((APSTaskPart) wTaskPartList.stream()
						.max(Comparator.comparing(APSTaskPart::getFinishWorkTime)).get()).getEndTime();
				if (wTime1.compareTo(wTime2) > 0) {
					wEndTime = wTime1;
				} else {
					wEndTime = wTime2;
				}
			}
			wResult.CustomResult.put("StartTime", wStartTime);
			wResult.CustomResult.put("EndTime", wEndTime);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode = String.valueOf(wResult.FaultCode) + e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> APS_CheckIsMakedMonthPlan(BMSEmployee wLoginUser, List<OMSOrder> wOrderList) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wOrderList == null || wOrderList.size() <= 0) {
				return wResult;
			}

			wResult.Result = APSTaskPartDAO.getInstance().APS_CheckIsMakedMonthPlan(wLoginUser,
					wOrderList.stream().map(p -> p.ID).collect(Collectors.toList()), wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> APS_CheckIsAreaLeader(BMSEmployee wLoginUser) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		try {
			if (!APSConstans.GetLFSWorkAreaCheckerList().stream().anyMatch(p -> p.LeaderIDList != null
					&& p.LeaderIDList.size() > 0 && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				wResult.Result = "提示：您不是工区主管，无法制定日计划!";
				return wResult;
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryTaskStepListByTime(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ②获取登录者的工位列表
			List<Integer> wPartIDList = null;
			if (CoreServiceImpl.getInstance()
					.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 501002, 0, 0)
					.Info(Boolean.class)) {
				wPartIDList = new ArrayList<Integer>();
			} else {
				// 工位ID集合
				wPartIDList = this.GetPartIDListByLoginUser(wLoginUser);
				if (wPartIDList == null || wPartIDList.size() <= 0) {
					return wResult;
				}
			}

			// ⑤出厂检和终检任务
			wResult.Result.addAll(APSTaskStepDAO.getInstance().SelectQTList(wLoginUser, null, null,
					new ArrayList<Integer>(
							Arrays.asList(APSTaskStatus.Issued.getValue(), APSTaskStatus.Started.getValue())),
					wPartIDList, wErrorCode));
			wResult.Result.addAll(APSTaskStepDAO.getInstance().SelectQTList(wLoginUser, wStartTime, wEndTime,
					new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Done.getValue())), wPartIDList, wErrorCode));

			// ③通过时间段和工位列表查询工位任务集合
			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByTime(wLoginUser, wPartIDList,
					wStartTime, wEndTime, wErrorCode);
			if (wTaskPartList != null && wTaskPartList.size() > 0) {
				// ④通过工位任务集合查询工序任务集合
				wResult.Result.addAll(APSTaskStepDAO.getInstance().SelectListByTaskPartIDList(wLoginUser,
						wTaskPartList.stream().map(p -> p.ID).collect(Collectors.toList()), wErrorCode));
			}

			if (wResult.Result.size() <= 0) {
				return wResult;
			}

			// ⑥去重
			wResult.Result = new ArrayList<APSTaskStep>(wResult.Result.stream()
					.collect(Collectors.toMap(APSTaskStep::getID, account -> account, (k1, k2) -> k2)).values());

			/**
			 * 默认人员返回(没派工记录时默认返回)
			 */
			List<SFCTaskStep> wList = null;
			Map<Integer, List<Integer>> wPersonMap = SFCServiceImpl.getInstance().SFC_QueryStepPersonMap(wLoginUser,
					wResult.Result).Result;

			List<Integer> wAPSTaskstepIDList = wResult.Result.stream().map(p -> p.ID).collect(Collectors.toList());
			wList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser, wAPSTaskstepIDList,
					SFCTaskStepType.Step.getValue(), wErrorCode);

			for (APSTaskStep wAPSTaskStep : wResult.Result) {
				List<SFCTaskStep> wTempList = wList.stream().filter(p -> p.TaskStepID == wAPSTaskStep.ID)
						.collect(Collectors.toList());

				if (wAPSTaskStep.OperatorList != null && wAPSTaskStep.OperatorList.size() > 0) {
					wAPSTaskStep.Operators = GetUserNames(wAPSTaskStep.OperatorList);
					continue;
				}

				if (wTempList != null && wTempList.size() > 0) {
					wAPSTaskStep.OperatorList = wTempList.stream().map(p -> p.OperatorID).collect(Collectors.toList());
					continue;
				}

				if (!wPersonMap.containsKey(wAPSTaskStep.ID)) {
					continue;
				}
				wAPSTaskStep.OperatorList = wPersonMap.get(wAPSTaskStep.ID);
				wAPSTaskStep.Operators = GetUserNames(wPersonMap.get(wAPSTaskStep.ID));
			}

			if (wResult.Result.size() > 0) {
				wResult.Result.removeIf(p -> p.Active != 1);
			}
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}", "APSServiceImpl", "APS_QueryTaskStepListByMonitorID",
					e.toString()));
		}
		return wResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryTaskStepByConditions(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wAreaID, int wLineID, int wPartID) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<APSTaskStep> wStepTaskList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, -1,
					wLineID, wPartID, -1, -1, 1, null, wStartTime, wEndTime, null, wErrorCode);

			// 工区工位
			if (wAreaID > 0) {
				List<LFSWorkAreaStation> wAreaStationList = APSConstans.GetLFSWorkAreaStationList().values().stream()
						.filter(p -> p.WorkAreaID == wAreaID).collect(Collectors.toList());
				wStepTaskList = wStepTaskList.stream()
						.filter(p -> wAreaStationList.stream().anyMatch(q -> q.StationID == p.PartID))
						.collect(Collectors.toList());
			}

			// 工艺数据
			List<FPCRoutePart> wFPCRoutePartList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartListByRouteID(BaseDAO.SysAdmin, 0).List(FPCRoutePart.class);
			List<FPCRoutePartPoint> wFPCRoutePartPointList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartPointListByRouteID(BaseDAO.SysAdmin, 0, 0).List(FPCRoutePartPoint.class);

			ServiceResult<List<OMSOrder>> wOrderResult = this.GetOrderListAndTaskPartListWithWholeTaskStep(wLoginUser,
					wStepTaskList, wFPCRoutePartList, wFPCRoutePartPointList);

			wResult.Result = (List<APSTaskStep>) wOrderResult.CustomResult.get("TaskStepList");
			wResult.CustomResult.put("OrderList", wOrderResult.Result);
			wResult.CustomResult.put("TaskPartList", wOrderResult.CustomResult.get("TaskPartList"));
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCTaskStepInfo>> APS_QuerySFCTaskStepInfoList(BMSEmployee wLoginUser,
			List<APSTaskStep> wList) {
		ServiceResult<List<SFCTaskStepInfo>> wResult = new ServiceResult<List<SFCTaskStepInfo>>();
		wResult.Result = new ArrayList<SFCTaskStepInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}
			// 去重
			List<APSTaskStep> wDistinctList = new ArrayList<APSTaskStep>(wList.stream()
					.collect(Collectors.toMap(APSTaskStep::getID, account -> account, (k1, k2) -> k2)).values());
			// 工区工位列表
			List<LFSWorkAreaStation> wLFSWorkAreaStationList = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.collect(Collectors.toList());

			SFCTaskStepInfo wSFCTaskStepInfo = null;
			for (APSTaskStep wAPSTaskStep : wDistinctList) {
				wSFCTaskStepInfo = new SFCTaskStepInfo();
				wSFCTaskStepInfo.OrderID = wAPSTaskStep.OrderID;
				wSFCTaskStepInfo.OrderNo = wAPSTaskStep.OrderNo;
				wSFCTaskStepInfo.PartNo = wAPSTaskStep.PartNo;
				wSFCTaskStepInfo.StationID = wAPSTaskStep.PartID;
				wSFCTaskStepInfo.StationName = wAPSTaskStep.PartName;
				wSFCTaskStepInfo.ToDispatch = (int) wList.stream().filter(
						p -> (p.OrderID == wAPSTaskStep.OrderID && p.PartID == wAPSTaskStep.PartID && !p.IsDispatched))
						.count();
				wSFCTaskStepInfo.Dispatched = (int) wList.stream().filter(
						p -> (p.OrderID == wAPSTaskStep.OrderID && p.PartID == wAPSTaskStep.PartID && p.IsDispatched))
						.count();
				wSFCTaskStepInfo.Status = wSFCTaskStepInfo.ToDispatch == 0 ? 1 : 0;
				if (wLFSWorkAreaStationList != null && wLFSWorkAreaStationList.size() > 0
						&& wLFSWorkAreaStationList.stream().anyMatch(p -> p.StationID == wAPSTaskStep.PartID)) {
					wSFCTaskStepInfo.OrderNum = wLFSWorkAreaStationList.stream()
							.filter(p -> p.StationID == wAPSTaskStep.PartID).findFirst().get().OrderNum;
				}
				if (!wResult.Result.stream()
						.anyMatch(p -> p.OrderID == wAPSTaskStep.OrderID && p.StationID == wAPSTaskStep.PartID)) {
					wResult.Result.add(wSFCTaskStepInfo);
				}
			}

			// 排序1、状态升序2、车号升序3、顺序升序
			if (wResult.Result.size() > 0) {
				// 排序
				wResult.Result.sort(Comparator.comparing(SFCTaskStepInfo::getStatus)
						.thenComparing(SFCTaskStepInfo::getPartNo).thenComparing(SFCTaskStepInfo::getOrderNum));
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSDayPlanAuditBPM> APS_SubmitDayPlanAuditBPMTask(BMSEmployee wLoginUser,
			APSDayPlanAuditBPM wData) {
		ServiceResult<APSDayPlanAuditBPM> wResult = new ServiceResult<APSDayPlanAuditBPM>();
		wResult.Result = new APSDayPlanAuditBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①判断输入数据的正确性
			if (wData == null || wData.ID <= 0) {
				return wResult;
			}
			// ②查询出所有子项

			List<APSTaskStep> wList = null;
			if (wData.AuditItemBPMList != null && wData.AuditItemBPMList.size() > 0) {
				wList = APSTaskStepDAO.getInstance().SelectListByIDList(wLoginUser, wData.AuditItemBPMList.stream()
						.map(p -> p.APSTaskStepID).distinct().collect(Collectors.toList()), wErrorCode);
			}

			// ②若数据状态为“提交”，修改子项的状态为“待审批”，修改单据的状态为“待审批”
			if (wList != null && wList.size() > 0 && wData.Status == 1) {
				wList.forEach(p -> {
					if (wData.AuditItemBPMList.stream().anyMatch(q -> q.Status == 1 && q.APSTaskStepID == p.ID)) {
						p.Status = APSTaskBPMStatus.ToAudit.getValue();
					} else {
						APSDayPlanAuditItemBPM wAuditBPM = wData.AuditItemBPMList.stream()
								.filter(q -> q.Status == 0 && q.APSTaskStepID == p.ID).findFirst().get();
						p.Remark = new APSTaskRemark();
						p.Remark.Remark = wAuditBPM.Remark;
						p.Remark.SubimtTime = Calendar.getInstance();
						p.RemarkList.add(p.Remark);
					}
					APSTaskStepDAO.getInstance().Update(wLoginUser, p, wErrorCode);
				});
			}
			// ③若数据状态为“已驳回”，修改子项的状态为“保存”，修改单据的状态“保存”
			else if (wList != null && wList.size() > 0 && wData.Status == 22) {
				wList.forEach(p -> {
					if (wData.AuditItemBPMList.stream().anyMatch(q -> q.Status == 1 && q.APSTaskStepID == p.ID)) {
						p.Status = APSTaskBPMStatus.Saved.getValue();
						APSTaskStepDAO.getInstance().Update(wLoginUser, p, wErrorCode);
					}
				});
				wData.StatusText = APSTaskBPMStatus.Rejected.getLable();
			}
			// 撤销
			else if (wList != null && wList.size() > 0 && wData.Status == 21) {
				wList.forEach(p -> {
					if (wData.AuditItemBPMList.stream().anyMatch(q -> q.Status == 1 && q.APSTaskStepID == p.ID)) {
						p.Status = APSTaskBPMStatus.Saved.getValue();
						APSTaskStepDAO.getInstance().Update(wLoginUser, p, wErrorCode);
					}
				});
				wData.StatusText = "已撤销";
			}
			// ⑤若数据状态为“已审批”，修改子项的状态为“已审批”，修改单据的状态为“已审批”
			else if (wList != null && wList.size() > 0 && wData.Status == 2) {
				wList.forEach(p -> {
					if (wData.AuditItemBPMList.stream().anyMatch(q -> q.Status == 1 && q.APSTaskStepID == p.ID)) {
						p.Status = APSTaskBPMStatus.Audited.getValue();
						APSTaskStepDAO.getInstance().Update(wLoginUser, p, wErrorCode);
					}
				});
			} else if (wList != null && wList.size() > 0 && wData.Status == 3) {
				wList.forEach(p -> {
					if (wData.AuditItemBPMList.stream().anyMatch(q -> q.Status == 1 && q.APSTaskStepID == p.ID)) {
						p.Status = APSTaskStatus.Issued.getValue();
						p.ReadyTime = Calendar.getInstance();
						APSTaskStepDAO.getInstance().Update(wLoginUser, p, wErrorCode);
					}
				});
				wData.StatusText = APSTaskBPMStatus.Issued.getLable();
			}

			if (wData.Status == 20) {
				wData.StatusText = "已下达";
			}

			// ⑥保存单据
			wResult.Result = (APSDayPlanAuditBPM) APSDayPlanAuditBPMDAO.getInstance().BPM_UpdateTask(wLoginUser, wData,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSDayPlanAuditBPM> APS_QueryDefaultDayPlanAuditBPMTask(BMSEmployee wLoginUser, int wEventID) {
		ServiceResult<APSDayPlanAuditBPM> wResult = new ServiceResult<APSDayPlanAuditBPM>();
		wResult.Result = new APSDayPlanAuditBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSDayPlanAuditBPM> wList = APSDayPlanAuditBPMDAO.getInstance().SelectList(wLoginUser, -1, "",
					wLoginUser.ID, null, -1, -1,
					new ArrayList<Integer>(Arrays.asList(APSTaskBPMStatus.Default.getValue())), null, null, wErrorCode);
			if (wList != null && wList.size() > 0) {
				wResult.Result = wList.get(0);
				wResult.Result.CreateTime = Calendar.getInstance();
				wResult.Result.SubmitTime = Calendar.getInstance();
				int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(),
						APSShiftPeriod.Day, FMCShiftLevel.Day, 0);
				wResult.Result.ShiftDate = Calendar.getInstance();
				wResult.Result.ShiftID = wShiftID;

				wResult.Result.AreaID = APSConstans.GetLFSWorkAreaCheckerList().stream()
						.filter(p -> p.LeaderIDList != null && p.LeaderIDList.size() > 0
								&& p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))
						.findFirst().get().WorkAreaID;
				wResult.Result.AreaName = APSConstans.GetBMSDepartmentName(wResult.Result.AreaID);
				wResult.Result.UpFlowName = APSConstans.GetBMSEmployeeName(wLoginUser.ID);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<APSDayPlanAuditBPM> APS_CreateDayPlanAuditBPMTask(BMSEmployee wLoginUser,
			BPMEventModule wEventID) {
		ServiceResult<APSDayPlanAuditBPM> wResult = new ServiceResult<APSDayPlanAuditBPM>();
		wResult.Result = new APSDayPlanAuditBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			APSDayPlanAuditBPM wAPSDayPlanAuditBPM = new APSDayPlanAuditBPM();
			wAPSDayPlanAuditBPM.Code = APSDayPlanAuditBPMDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			wAPSDayPlanAuditBPM.ShiftID = wShiftID;
			wAPSDayPlanAuditBPM.ShiftDate = Calendar.getInstance();
			wAPSDayPlanAuditBPM.ID = 0;
			wAPSDayPlanAuditBPM.FlowType = BPMEventModule.SCDayAudit.getValue();
			wAPSDayPlanAuditBPM.UpFlowID = wLoginUser.ID;
			wAPSDayPlanAuditBPM.Status = APSTaskBPMStatus.Default.getValue();
			wAPSDayPlanAuditBPM.StatusText = "";
			wAPSDayPlanAuditBPM.CreateTime = Calendar.getInstance();
			wAPSDayPlanAuditBPM.SubmitTime = Calendar.getInstance();

			// 赋值AreaID
			if (APSConstans.GetLFSWorkAreaCheckerList().stream().anyMatch(p -> p.LeaderIDList != null
					&& p.LeaderIDList.size() > 0 && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				wAPSDayPlanAuditBPM.AreaID = APSConstans.GetLFSWorkAreaCheckerList().stream()
						.filter(p -> p.LeaderIDList != null && p.LeaderIDList.size() > 0
								&& p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))
						.findFirst().get().WorkAreaID;
				wAPSDayPlanAuditBPM.AreaName = APSConstans.GetBMSDepartmentName(wAPSDayPlanAuditBPM.AreaID);
				wAPSDayPlanAuditBPM.UpFlowName = APSConstans.GetBMSEmployeeName(wLoginUser.ID);
			}

			// 限制审批单的创建
			List<APSDayPlanAuditBPM> wList = APSDayPlanAuditBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, "",
					wAPSDayPlanAuditBPM.AreaID, -1, new ArrayList<Integer>(Arrays.asList(1, 2, 3)), null, null,
					wErrorCode);
			if (wList.size() > 0) {
				wResult.FaultCode += "提示：该工区有未完成的审批单，请完成后再次制定!";
				return wResult;
			}

			wResult.Result = (APSDayPlanAuditBPM) APSDayPlanAuditBPMDAO.getInstance().BPM_UpdateTask(wLoginUser,
					wAPSDayPlanAuditBPM, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSDayPlanAuditBPM> APS_GetDayPlanAuditBPMTask(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSDayPlanAuditBPM> wResult = new ServiceResult<APSDayPlanAuditBPM>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = (APSDayPlanAuditBPM) APSDayPlanAuditBPMDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wID,
					"", wErrorCode);
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSDayPlanAuditBPM>> APS_QueryDayPlanAuditBPMHistory(BMSEmployee wLoginUser, int wID,
			String wCode, int wUpFlowID, int wFollowerID, int wAreaID, int wShiftID, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<APSDayPlanAuditBPM>> wResult = new ServiceResult<List<APSDayPlanAuditBPM>>();
		wResult.Result = new ArrayList<APSDayPlanAuditBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSDayPlanAuditBPMDAO.getInstance().SelectList(wLoginUser, wID, wCode, wUpFlowID,
					String.valueOf(wFollowerID), wAreaID, wShiftID, null, wStartTime, wEndTime, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BPMTaskBase>> APS_QueryDayPlanAuditBPMEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (TagTypes.getEnumType(wTagTypes)) {
			case Applicant:// 2发起
				wResult.Result = APSDayPlanAuditBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			case Dispatcher:// 1待做
				wResult.Result = APSDayPlanAuditBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				break;
			case Approver:// 4已做
				wResult.Result = APSDayPlanAuditBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryDayPlanAuditItemDetails(BMSEmployee wLoginUser,
			int wAPSDayPlanAuditBPMID) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			APSDayPlanAuditBPM wItem = (APSDayPlanAuditBPM) APSDayPlanAuditBPMDAO.getInstance()
					.BPM_GetTaskInfo(wLoginUser, wAPSDayPlanAuditBPMID, "", wErrorCode);
			if (wItem == null || wItem.ID <= 0 || wItem.AuditItemBPMList == null
					|| wItem.AuditItemBPMList.size() <= 0) {

				wResult.CustomResult.put("Info", wItem);
				wResult.CustomResult.put("PartList", new ArrayList<APSTaskPartDetails>());

				return wResult;
			}

			wResult.Result = APSTaskStepDAO.getInstance().SelectListByIDList(wLoginUser,
					wItem.AuditItemBPMList.stream().map(p -> p.APSTaskStepID).distinct().collect(Collectors.toList()),
					wErrorCode);

			// 提取订单
			List<Integer> wOrderIDList = wResult.Result.stream().map(p -> p.OrderID).distinct()
					.collect(Collectors.toList());
			List<OMSOrder> wOList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);
			// 订单排序
//			wOList.sort(Comparator.comparing(OMSOrder::getRealReceiveDate));
			wOList.sort(Comparator.comparing(OMSOrder::getERPID).thenComparing(OMSOrder::getRealReceiveDate));
			// 结果排序
			wResult.Result = OrderResultTaskStep(wOList, wResult.Result);

			// 提取工位信息
			List<APSTaskPartDetails> wDetailsList = new ArrayList<APSTaskPartDetails>();
			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
					wResult.Result.stream().map(p -> p.TaskPartID).distinct().collect(Collectors.toList()), wErrorCode);

			// 工位任务排序
			wTaskPartList = OrderTaskPartList(wOList, wTaskPartList);

			APSTaskPartDetails wAPSTaskPartDetails;
			for (APSTaskPart wAPSTaskPart : wTaskPartList) {
				// 工位详情
				wAPSTaskPartDetails = new APSTaskPartDetails();
				wAPSTaskPartDetails.APSTaskPart = wAPSTaskPart;

				List<APSTaskStep> wTaskStepList = wResult.Result.stream().filter(p -> p.TaskPartID == wAPSTaskPart.ID)
						.collect(Collectors.toList());

				// 工序总数
				wAPSTaskPartDetails.StepSize = wTaskStepList.size();
				// 工序完成数
				wAPSTaskPartDetails.StepFinish = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Done.getValue()).count();
				// 工序在制数
				wAPSTaskPartDetails.StepMaking = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Submit.getValue()
								|| p.Status == APSTaskStatus.ToAudit.getValue()
								|| p.Status == APSTaskStatus.Audited.getValue())
						.count();
				// 工序已排数
				wAPSTaskPartDetails.StepSchedule = (int) wTaskStepList.stream()
						.filter(p -> p.Status == APSTaskStatus.Issued.getValue()
								|| p.Status == APSTaskStatus.Started.getValue()
								|| p.Status == APSTaskStatus.Done.getValue())
						.count();

				if (APSConstans.GetLFSWorkAreaStationList().values().stream()
						.anyMatch(p -> p.StationID == wAPSTaskPart.PartID && p.Active == 1)) {
					wAPSTaskPartDetails.AreaID = APSConstans.GetLFSWorkAreaStationList().values().stream()
							.filter(p -> p.StationID == wAPSTaskPart.PartID && p.Active == 1).findFirst()
							.get().WorkAreaID;
					wAPSTaskPartDetails.AreaName = APSConstans.GetBMSDepartmentName(wAPSTaskPartDetails.AreaID);
				}

				wDetailsList.add(wAPSTaskPartDetails);
			}

			wResult.CustomResult.put("Info", wItem);
			wResult.CustomResult.put("PartList", wDetailsList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 排序
	 */
	private List<APSTaskPart> OrderTaskPartList(List<OMSOrder> wOList, List<APSTaskPart> wTaskPartList) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			if (wTaskPartList == null || wTaskPartList.size() <= 0) {
				return wResult;
			}

			for (OMSOrder wOMSOrder : wOList) {
				List<APSTaskPart> wList = wTaskPartList.stream().filter(p -> p.OrderID == wOMSOrder.ID)
						.collect(Collectors.toList());
				if (wList.size() > 0) {
					wResult.addAll(wList);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 排序
	 */
	private List<APSTaskStep> OrderResultTaskStep(List<OMSOrder> wOList, List<APSTaskStep> wAPSTaskStepList) {
		List<APSTaskStep> wResult = new ArrayList<APSTaskStep>();
		try {
			if (wAPSTaskStepList == null || wAPSTaskStepList.size() <= 0) {
				return wResult;
			}

			for (OMSOrder wOMSOrder : wOList) {
				List<APSTaskStep> wList = wAPSTaskStepList.stream().filter(p -> p.OrderID == wOMSOrder.ID)
						.collect(Collectors.toList());
				if (wList.size() > 0) {
					wResult.addAll(wList);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FPCRoute>> ASP_QueryRouteList(BMSEmployee wLoginUser, List<Integer> wRouteIDList,
			Map<Integer, List<FPCRoutePart>> wRouteMap) {
		ServiceResult<List<FPCRoute>> wResult = new ServiceResult<List<FPCRoute>>();
		wResult.Result = new ArrayList<FPCRoute>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wRouteIDList == null || wRouteIDList.size() <= 0) {
				return wResult;
			}

			for (Integer wRouteID : wRouteIDList) {
//				FPCRoute wRoute = FMCServiceImpl.getInstance().FPC_QueryRouteByID(wLoginUser, wRouteID)
//						.Info(FPCRoute.class);
				FPCRoute wRoute = FPCRouteDAO.getInstance().SelectByID(wLoginUser, wRouteID, wErrorCode);
				if (wRouteMap.containsKey(wRouteID)) {
					wRoute.PartList = wRouteMap.get(wRouteID);
				} else {
					wRoute.PartList = FMCServiceImpl.getInstance().FPC_QueryRoutePartListByRouteID(wLoginUser, wRouteID)
							.List(FPCRoutePart.class);
				}
				wResult.Result.add(wRoute);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> Export(BMSEmployee wLoginUser, String wVersionNo, String wSuffix, int wTaskPartID,
			int wAPSShiftPeriod) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (StringUtils.isEmpty(wVersionNo)) {
				wVersionNo = APSSchedulingVersionDAO.getInstance().GetVersonNo(wLoginUser, wTaskPartID, wErrorCode);
			}

			if (StringUtils.isEmpty(wVersionNo)) {
				wResult.FaultCode += "提示：参数错误!";
				return wResult;
			}

			APSSchedulingVersion wVersion = APSSchedulingVersionDAO.getInstance().SelectByVersionNo(wLoginUser,
					wVersionNo, wErrorCode);

			int wYear = Calendar.getInstance().get(Calendar.YEAR);

			SimpleDateFormat wSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String wCurTime = wSimpleDateFormat.format(Calendar.getInstance().getTime());
			String wType = wAPSShiftPeriod == APSShiftPeriod.Month.getValue() ? "月" : "周";
			String wFileName = StringUtils.Format("{0}-{1}-{2}滚动计划.xls", String.valueOf(wYear), wCurTime, wType);
			String wDirePath = StringUtils.Format("{0}static/export/",
					new Object[] { Constants.getConfigPath().replace("config/", "") });
			File wDirFile = new File(wDirePath);
			if (!wDirFile.exists()) {
				wDirFile.mkdirs();
			}
			String wFilePath = StringUtils.Format("{0}{1}", new Object[] { wDirePath, wFileName });
			File wNewFile = new File(wFilePath);
			wNewFile.createNewFile();
			List<List<String>> wSourceList = this.APS_GetSourceList(wLoginUser, wVersion, wSuffix);

			FileOutputStream wFileOutputStream = new FileOutputStream(wNewFile);
			ExcelUtil.APS_WriteScrollPlan(wSourceList, wFileOutputStream, wType);
			String wUri = StringUtils.Format("/{0}/export/{1}",
					Configuration.readConfigString("project.name", "application"), wFileName);
			wResult.Result = wUri;

			wResult.setFaultCode(MESException.getEnumType((int) wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取滚动计划数据源
	 */
	private List<List<String>> APS_GetSourceList(BMSEmployee wLoginUser, APSSchedulingVersion wVersion,
			String wSuffix) {
		List<List<String>> wResult = new ArrayList<List<String>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// ①构造表头数据
			List<Integer> wShiftIDList = this.GetShiftIDs(wVersion);
			List<String> wHearderList = this.GetHeaderList(wVersion);
			wResult.add(wHearderList);
			// ②获取工位计划列表
			List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
					wVersion.TaskPartIDList, null, wErrorCode);
			// ④根据工区工位表，排序工位
			List<LFSWorkAreaStation> wASList = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.filter(p -> p.Active == 1).collect(Collectors.toList());
			wASList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));

			// 获取工艺路线列表
			List<Integer> wOrderIDList = wTaskPartList.stream().map(p -> p.OrderID).collect(Collectors.toList());
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList,
					wErrorCode);
			List<Integer> wRouteIDList = wOrderList.stream().map(p -> p.RouteID).distinct()
					.collect(Collectors.toList());

//			List<FPCRoutePart> wRoutePartList = APSConstans.mFPCRoutePartList.stream()
//					.filter(p -> wRouteIDList.stream().anyMatch(q -> q == p.RouteID)).collect(Collectors.toList());
			List<FPCRoutePart> wRoutePartList = new ArrayList<FPCRoutePart>();
			for (Integer wRouteID : wRouteIDList) {
				List<FPCRoutePart> wRPList = FMCServiceImpl.getInstance()
						.FPC_QueryRoutePartListByRouteID(wLoginUser, wRouteID).List(FPCRoutePart.class);
				wRoutePartList.addAll(wRPList);
			}

			wASList.removeIf(p -> !wRoutePartList.stream().anyMatch(q -> q.PartID == p.StationID));
			// ⑤遍历排序好的工位数据，创建行数据
			int wNum = 1;
			for (LFSWorkAreaStation wAS : wASList) {
				List<String> wValueList = new ArrayList<String>();
				// ①添加序号
				wValueList.add(String.valueOf(wNum));
				// ②添加工位名称
				wValueList.add(APSConstans.GetFPCPartName(wAS.StationID));
				wResult.add(wValueList);
				for (int wShiftID : wShiftIDList) {
					// ③根据工位和ShiftID获取车辆名称
					String wCars = this.GetCars(wShiftID, wAS, wTaskPartList);
					if (StringUtils.isNotEmpty(wSuffix)) {
						wCars = wCars.replace(wSuffix, "");
					}
					// ④添加车辆
					wValueList.add(wCars);
				}
				wNum++;
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据工位和ShiftID获取车辆名称
	 */
	private String GetCars(int wShiftID, LFSWorkAreaStation wAS, List<APSTaskPart> wTaskPartList) {
		String wResult = "";
		try {
			List<APSTaskPart> wList = wTaskPartList.stream().filter(p -> p.PartID == wAS.StationID)
					.collect(Collectors.toList());

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			List<String> wCars = new ArrayList<String>();
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");

			for (APSTaskPart wAPSTaskPart : wList) {
				Calendar wS = (Calendar) wAPSTaskPart.StartTime.clone();
				Calendar wE = (Calendar) wAPSTaskPart.EndTime.clone();
				int wShiftS = Integer.parseInt(wSDF.format(wS.getTime()));
				int wShiftE = Integer.parseInt(wSDF.format(wE.getTime()));

				if (wShiftE > wShiftS) {
					wE.add(Calendar.DATE, -1);
					wShiftE = Integer.parseInt(wSDF.format(wE.getTime()));
				}

				if (wShiftID >= wShiftS && wShiftID <= wShiftE
						&& !wCars.stream().anyMatch(p -> p.equals(wAPSTaskPart.PartNo))) {
					wCars.add(wAPSTaskPart.PartNo);
				}
			}
			if (wCars.size() > 0) {
				wResult = StringUtils.Join(",", wCars);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据排程版本获取滚动计划表头
	 * 
	 * @param wVersion
	 * @return
	 */
	private List<String> GetHeaderList(APSSchedulingVersion wVersion) {
		List<String> wResult = new ArrayList<String>();
		try {
			wResult.add("序号");
			wResult.add("工位");

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat wSDF1 = new SimpleDateFormat("MM月dd日");

			Calendar wS = (Calendar) wVersion.StartTime.clone();
			Calendar wE = (Calendar) wVersion.EndTime.clone();

			while (Integer.parseInt(wSDF.format(wS.getTime())) <= Integer.parseInt(wSDF.format(wE.getTime()))) {
				wResult.add(wSDF1.format(wS.getTime()));
				wS.add(Calendar.DATE, 1);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据排程时间获取班次集合
	 */
	private List<Integer> GetShiftIDs(APSSchedulingVersion wVersion) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");

			Calendar wS = (Calendar) wVersion.StartTime.clone();
			Calendar wE = (Calendar) wVersion.EndTime.clone();

			while (Integer.parseInt(wSDF.format(wS.getTime())) <= Integer.parseInt(wSDF.format(wE.getTime()))) {
				wResult.add(Integer.parseInt(wSDF.format(wS.getTime())));
				wS.add(Calendar.DATE, 1);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskPart>> APS_QueryTaskPartList(BMSEmployee wLoginUser, int wOrderID,
			Calendar wStartTime, Calendar wEndTime, int wAPSShiftPeriod) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1, -1, -1, 1,
					wAPSShiftPeriod, null, -1, wStartTime, wEndTime, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSSchedulingVersionBPM> APS_QueryDefaultSchedulingVersionBPM(BMSEmployee wLoginUser,
			int wEventID) {
		ServiceResult<APSSchedulingVersionBPM> wResult = new ServiceResult<APSSchedulingVersionBPM>();
		wResult.Result = new APSSchedulingVersionBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			int wAPSShiftPeriod = 0;
			switch (BPMEventModule.getEnumType(wEventID)) {
			case SCMonthAudit:
				wAPSShiftPeriod = APSShiftPeriod.Month.getValue();
				break;
			case SCWeekAudit:
				wAPSShiftPeriod = APSShiftPeriod.Week.getValue();
				break;
			default:
				break;
			}

			List<APSSchedulingVersionBPM> wList = APSSchedulingVersionBPMDAO.getInstance().SelectList(wLoginUser, -1,
					"", -1, wLoginUser.ID, "", "", wAPSShiftPeriod, -1,
					new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Default.getValue())), null, null, wErrorCode);
			if (wList != null && wList.size() > 0) {
				wResult.Result = wList.get(0);
				wResult.Result.CreateTime = Calendar.getInstance();
				wResult.Result.SubmitTime = Calendar.getInstance();
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<APSSchedulingVersionBPM> APS_CreateSchedulingVersionBPM(BMSEmployee wLoginUser,
			BPMEventModule wEventID) {
		ServiceResult<APSSchedulingVersionBPM> wResult = new ServiceResult<APSSchedulingVersionBPM>();
		wResult.Result = new APSSchedulingVersionBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			int wAPSShiftPeriod = 0;
			switch (wEventID) {
			case SCMonthAudit:
				wAPSShiftPeriod = APSShiftPeriod.Month.getValue();
				break;
			case SCWeekAudit:
				wAPSShiftPeriod = APSShiftPeriod.Week.getValue();
				break;
			default:
				break;
			}

			wResult.Result.APSShiftPeriod = wAPSShiftPeriod;
			wResult.Result.Code = APSSchedulingVersionBPMDAO.getInstance().GetNewCode(wLoginUser, wErrorCode,
					APSShiftPeriod.getEnumType(wAPSShiftPeriod));
			wResult.Result.CreateTime = Calendar.getInstance();
			wResult.Result.FlowType = wEventID.getValue();
			wResult.Result.ID = 0;
			wResult.Result.Status = APSTaskStatus.Default.getValue();
			wResult.Result.StatusText = "";
			wResult.Result.SubmitTime = Calendar.getInstance();
			wResult.Result.UpFlowID = wLoginUser.ID;
			wResult.Result.VersionNo = wResult.Result.Code;

			wResult.Result.UpFlowName = wLoginUser.Name;

			wResult.Result = (APSSchedulingVersionBPM) APSSchedulingVersionBPMDAO.getInstance()
					.BPM_UpdateTask(wLoginUser, wResult.Result, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;

	}

	@Override
	public synchronized ServiceResult<APSSchedulingVersionBPM> APS_SubmitSchedulingVersionBPM(BMSEmployee wLoginUser,
			APSSchedulingVersionBPM wData) {
		ServiceResult<APSSchedulingVersionBPM> wResult = new ServiceResult<APSSchedulingVersionBPM>();
		wResult.Result = new APSSchedulingVersionBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 提交
			List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser, wData.TaskPartIDList,
					wErrorCode);

			if (wData.Status == 2 && wList.stream().anyMatch(p -> p.Status != 1)) {
				wResult.FaultCode += "提示：请提交未排的数据!";
				return wResult;
			}

			if (wData.Status == 2) {
				for (APSTaskPart wAPSTaskPart : wList) {
					wAPSTaskPart.Status = APSTaskStatus.ToAudit.getValue();
					APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
				}
			} else if (wData.Status == 22) {
				for (APSTaskPart wAPSTaskPart : wList) {
					wAPSTaskPart.Status = APSTaskStatus.Saved.getValue();
					APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
				}
			} else if (wData.Status == 1) {
				for (APSTaskPart wAPSTaskPart : wList) {
					wAPSTaskPart.Status = APSTaskStatus.Saved.getValue();
					APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
				}
			} else if (wData.Status == 5) {
				for (APSTaskPart wAPSTaskPart : wList) {
					wAPSTaskPart.Status = APSTaskStatus.Audited.getValue();
					APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
				}
			} else if (wData.Status == 20) {
				for (APSTaskPart wAPSTaskPart : wList) {
					wAPSTaskPart.Status = APSTaskStatus.Issued.getValue();

					// 根据订单ID和工位ID判断是否是不做的工位
					boolean wCheckFlag = APSTaskPartDAO.getInstance().CheckIsNotDo(wLoginUser, wAPSTaskPart.OrderID,
							wAPSTaskPart.PartID, wErrorCode);
					if (wCheckFlag) {
						wAPSTaskPart.Active = 2;
					}

					APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
				}

				APSSchedulingVersion wScheduling = APSSchedulingVersionDAO.getInstance().SelectByVersionNo(wLoginUser,
						wData.VersionNo, wErrorCode);
				if (wScheduling != null && wScheduling.ID > 0) {
					wScheduling.Status = APSTaskStatus.Issued.getValue();
					APSSchedulingVersionDAO.getInstance().Update(wLoginUser, wScheduling, wErrorCode);

					if (wData.APSShiftPeriod == APSShiftPeriod.Week.getValue()) {
						wScheduling.APSTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
								wScheduling.TaskPartIDList, wErrorCode);

						// 触发日计划
						ExecutorService wES = Executors.newFixedThreadPool(1);
						wES.submit(() -> APS_TriggerDayPlans(wLoginUser, wScheduling));
						wES.shutdown();

						String materialManage = Configuration.readConfigString("materialManage", "config/config");
						if (materialManage.equals("1")) {
							// 触发物料需求计划
							ExecutorService wES1 = Executors.newFixedThreadPool(1);
							wES1.submit(() -> APS_TriggerMRP(wLoginUser, wData, wScheduling));
							wES1.shutdown();
						}
					}
				}
			} else if (wData.Status == 21) {
				wData.StatusText = "已驳回";
				for (APSTaskPart wAPSTaskPart : wList) {
					wAPSTaskPart.Status = APSTaskStatus.Saved.getValue();
					APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
				}
			}

			if (wData.Status == 20) {
				wData.StatusText = "已下达";
			}

			wResult.Result = (APSSchedulingVersionBPM) APSSchedulingVersionBPMDAO.getInstance()
					.BPM_UpdateTask(wLoginUser, wData, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 触发物料需求计划
	 */
	private void APS_TriggerMRP(BMSEmployee wLoginUser, APSSchedulingVersionBPM wAPSSchedulingVersionBPM,
			APSSchedulingVersion wScheduling) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			String wMRPMaterialPlanDays = Configuration.readConfigString("mrpmaterialplandays", "config/config");
			int wPlanDays = StringUtils.parseInt(wMRPMaterialPlanDays);
			// ①遍历工位计划
			for (APSTaskPart wAPSTaskPart : wScheduling.APSTaskPartList) {
				// ②禁用该订单、该工位的物料需求计划(必换件、委外必修件)
				MRPMaterialPlanDAO.getInstance().DisableMainPlan(wLoginUser, wAPSTaskPart.OrderID, wAPSTaskPart.PartID,
						wErrorCode);
				if (wErrorCode.Result != 0) {
					logger.error("禁用必换和必修件物料需求计划失败,建议检查程序!");
					return;
				}
				// ③查询台车bom(必换件、委外必修件)
				List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser,
						wAPSTaskPart.OrderID, "", "", -1, -1, -1, wAPSTaskPart.PartID, -1, -1, "", -1, -1, -1, null, -1,
						-1, -1, wErrorCode);
				if (wList == null || wList.size() <= 0) {
					continue;
				}
				// ④创建物料需求计划
				List<MRPMaterialPlan> wPlanList = new ArrayList<MRPMaterialPlan>();
				for (APSBOMItem apsbomItem : wList) {
					MRPMaterialPlan wMRPMaterialPlan = new MRPMaterialPlan(0, apsbomItem.ProductID, apsbomItem.LineID,
							apsbomItem.CustomerID, apsbomItem.OrderID, apsbomItem.PartNo, apsbomItem.PartID,
							apsbomItem.PartPointID, apsbomItem.MaterialID, apsbomItem.MaterialName,
							apsbomItem.MaterialNo, 1, apsbomItem.Number, wAPSTaskPart.StartTime,
							wAPSSchedulingVersionBPM.Code, 1, Calendar.getInstance(), wLoginUser.ID,
							apsbomItem.ReplaceType, apsbomItem.OutsourceType, 1, apsbomItem.WBSNo,
							apsbomItem.AssessmentType);
					MRPMaterialPlanDAO.getInstance().Update(wLoginUser, wMRPMaterialPlan, wErrorCode);
					wPlanList.add(wMRPMaterialPlan);
				}

				Calendar wSTime = (Calendar) wAPSTaskPart.StartTime.clone();
				wSTime.add(Calendar.DATE, -wPlanDays);
				wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);

				Calendar wTTime = Calendar.getInstance();
				wTTime.set(wTTime.get(Calendar.YEAR), wTTime.get(Calendar.MONTH), wTTime.get(Calendar.DATE), 0, 0, 0);

				if (wTTime.compareTo(wSTime) >= 0 && wPlanList.size() > 0) {
					// ⑤触发物料配送单
					TriggerMaterialDistributionSheet(BaseDAO.SysAdmin, wPlanList,
							MRPMaterialPlanStatus.SystemSubmit.getValue());
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 触发物料配送单
	 * 
	 * @param wLoginUser   登录信息
	 * @param wList        必换件、委外必修件台车bom
	 * @param wAPSTaskPart 工位计划
	 */
	private void TriggerMaterialDistributionSheet(BMSEmployee wLoginUser, List<MRPMaterialPlan> wList,
			int wMRPMaterialPlanStatus) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOMSOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wList.get(0).OrderID, wErrorCode);

			// 判断需求是否已提
			List<WMSPickDemand> wExsitList = WMSPickDemandDAO.getInstance().SelectList(wLoginUser, -1,
					String.valueOf(WMSOrderType.LineOrder.getValue()), "", wList.get(0).ProductID, wList.get(0).LineID,
					wList.get(0).CustomerID, wList.get(0).OrderID, wList.get(0).PartID, -1, null, null, null,
					wErrorCode);
			if (wExsitList.size() > 0) {
				// 取消计划，或不指定
				return;
			}
			// ③创建领料需求单
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);

			String wCode = WMSPickDemandDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			Calendar wTime = wList.get(0).DemandDate;

			Calendar expectTime1 = Calendar.getInstance();
			expectTime1.set(wTime.get(Calendar.YEAR), wTime.get(Calendar.MONTH), wTime.get(Calendar.DATE), 0, 0, 0);

			Calendar expectTime2 = Calendar.getInstance();
			expectTime2.set(wTime.get(Calendar.YEAR), wTime.get(Calendar.MONTH), wTime.get(Calendar.DATE), 12, 0, 0);

			String monitorNo = "";
			String monitor = "";
			BMSEmployee wUser = WMSPickDemandDAO.getInstance().GetMonitorByPart(wLoginUser, wList.get(0).PartID,
					wErrorCode);
			if (StringUtils.isNotEmpty(wUser.LoginID)) {
				monitorNo = wUser.LoginID;
				monitor = wUser.Name;
			}

			WMSPickDemand wWMSPickDemand = new WMSPickDemand(0, "1900",
					String.valueOf(WMSOrderType.LineOrder.getValue()), wCode, expectTime1, expectTime2, monitorNo,
					monitor, wList.get(0).ProductID, wList.get(0).ProductNo, wList.get(0).LineID, wList.get(0).LineName,
					wList.get(0).CustomerID, APSConstans.GetCRMCustomer(wList.get(0).CustomerID).CustomerName,
					APSConstans.GetCRMCustomer(wList.get(0).CustomerID).CustomerCode, wList.get(0).OrderID,
					wList.get(0).PartNo, wList.get(0).PartID, APSConstans.GetFPCPartName(wList.get(0).PartID),
					APSConstans.GetFPCPart(wList.get(0).PartID).Code, "", "", wBaseTime, "", "", wBaseTime, 1,
					wLoginUser.ID, wLoginUser.Name, Calendar.getInstance(), wList.get(0).WBSNo);
			int wDemandID = WMSPickDemandDAO.getInstance().Update(wLoginUser, wWMSPickDemand, wErrorCode);
			if (wDemandID <= 0) {
				return;
			}
			// ④创建领料需求单明细
			int wIndex = 1;
			for (MRPMaterialPlan wMSSBOMItem : wList) {
				WMSPickDemandItem wWMSPickDemandItem = new WMSPickDemandItem(0, wDemandID, wMSSBOMItem.MaterialID,
						wMSSBOMItem.MaterialNo, wMSSBOMItem.MaterialName, wMSSBOMItem.FQTY, wOMSOrder.OrderNo,
						wMSSBOMItem.StepID, APSConstans.GetFPCPartPoint(wMSSBOMItem.StepID).Code.replace("PS-", ""),
						APSConstans.GetFPCPartPointName(wMSSBOMItem.StepID), String.valueOf(wIndex), "",
						wMSSBOMItem.ReplaceType, SFCReplaceType.getEnumType(wMSSBOMItem.ReplaceType).getLable(),
						wMSSBOMItem.OutSourceType, SFCOutSourceType.getEnumType(wMSSBOMItem.OutSourceType).getLable(),
						wMSSBOMItem.AssessmentType, "");
				WMSPickDemandItemDAO.getInstance().Update(wLoginUser, wWMSPickDemandItem, wErrorCode);
				wIndex++;
			}
			// ⑤关联物料需求计划
			List<MRPMaterialPlan> wPList = wList.stream()
					.filter(p -> p.OrderID == wList.get(0).OrderID && p.PartID == wList.get(0).PartID && p.Active == 1)
					.collect(Collectors.toList());
			for (MRPMaterialPlan mrpMaterialPlan : wPList) {
				mrpMaterialPlan.Status = wMRPMaterialPlanStatus;
				mrpMaterialPlan.EditID = wLoginUser.ID;
				mrpMaterialPlan.EditTime = Calendar.getInstance();
				mrpMaterialPlan.WMSPickDemandID = wDemandID;
				MRPMaterialPlanDAO.getInstance().Update(wLoginUser, mrpMaterialPlan, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<APSSchedulingVersionBPM> APS_GetSchedulingVersionBPM(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSSchedulingVersionBPM> wResult = new ServiceResult<APSSchedulingVersionBPM>();
		wResult.Result = new APSSchedulingVersionBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = (APSSchedulingVersionBPM) APSSchedulingVersionBPMDAO.getInstance()
					.BPM_GetTaskInfo(wLoginUser, wID, "", wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSSchedulingVersionBPM>> APS_QuerySchedulingVersionBPMHistory(BMSEmployee wLoginUser,
			int wID, String wCode, int wUpFlowID, int wFollowerID, int wAPSShiftPeriod, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<APSSchedulingVersionBPM>> wResult = new ServiceResult<List<APSSchedulingVersionBPM>>();
		wResult.Result = new ArrayList<APSSchedulingVersionBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			String wFollower = "";
			if (wFollowerID > 0) {
				wFollower = String.valueOf(wFollowerID);
			}
			wResult.Result = APSSchedulingVersionBPMDAO.getInstance().SelectList(wLoginUser, wID, wCode, -1, wUpFlowID,
					wFollower, "", wAPSShiftPeriod, -1, null, wStartTime, wEndTime, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BPMTaskBase>> APS_QuerySchedulingVersionBPMEmployeeAll(BMSEmployee wLoginUser,
			int wAPSShiftPeriod, int wTagTypes, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			int wEventID = 0;
			switch (APSShiftPeriod.getEnumType(wAPSShiftPeriod)) {
			case Month:
				wEventID = BPMEventModule.SCMonthAudit.getValue();
				break;
			case Week:
				wEventID = BPMEventModule.SCWeekAudit.getValue();
				break;
			default:
				break;
			}

			switch (TagTypes.getEnumType(wTagTypes)) {
			case Applicant:// 2发起
				wResult.Result = APSSchedulingVersionBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			case Dispatcher:// 1待做
				wResult.Result = APSSchedulingVersionBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				break;
			case Approver:// 4已做
				wResult.Result = APSSchedulingVersionBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			default:
				break;
			}

			int wEventC = wEventID;
			wResult.Result = wResult.Result.stream().filter(p -> p.FlowType == wEventC && p.Status != 0)
					.collect(Collectors.toList());

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskPart>> APS_QuerySchedulingVersionItemDetails(BMSEmployee wLoginUser,
			int wAPSSchedulingVersionBPMID) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wAPSSchedulingVersionBPMID <= 0) {
				return wResult;
			}

			APSSchedulingVersionBPM wTask = (APSSchedulingVersionBPM) APSSchedulingVersionBPMDAO.getInstance()
					.BPM_GetTaskInfo(wLoginUser, wAPSSchedulingVersionBPMID, "", wErrorCode);

			if (wTask == null || wTask.ID <= 0 || wTask.TaskPartIDList == null || wTask.TaskPartIDList.size() <= 0) {
				return wResult;
			}

			wResult.Result = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser, wTask.TaskPartIDList,
					wErrorCode);

			wResult.CustomResult.put("Info", wTask);

			List<OMSOrder> wOMSOrderList = new ArrayList<OMSOrder>();
			if (wResult.Result.size() > 0) {
				List<Integer> wOrderIDList = wResult.Result.stream().map(p -> p.OrderID).distinct()
						.collect(Collectors.toList());
				wOMSOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);
			}
			wResult.CustomResult.put("OMSOrderList", wOMSOrderList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSDayPlanAudit> APS_QueryDayPlanAudit(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSDayPlanAudit> wResult = new ServiceResult<APSDayPlanAudit>();
		wResult.Result = new APSDayPlanAudit();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = APSDayPlanAuditDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

//	@Override
//	public ServiceResult<List<APSPartNoDetails>> APS_QueryPartNoDetails(BMSEmployee wLoginUser, Calendar wStartTime,
//			Calendar wEndTime) {
//		ServiceResult<List<APSPartNoDetails>> wResult = new ServiceResult<List<APSPartNoDetails>>();
//		wResult.Result = new ArrayList<APSPartNoDetails>();
//		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
//		try {
//			ServiceResult<List<APSTaskStep>> wServiceResult = this.APS_QueryTaskStepByTime(wLoginUser, wStartTime,
//					wEndTime, false);
//
//			if (wServiceResult.Result == null || wServiceResult.Result.size() <= 0) {
//				return wResult;
//			}
//
//			List<APSTaskPartDetails> wAPSTaskPartDetailsList = (List<APSTaskPartDetails>) wServiceResult.CustomResult
//					.get("TaskPartList");
//			List<String> wList = wAPSTaskPartDetailsList.stream().map(p -> p.PartNo).distinct()
//					.collect(Collectors.toList());
//			APSPartNoDetails wAPSPartNoDetails;
//			for (String wPartNo : wList) {
//				wAPSPartNoDetails = new APSPartNoDetails();
//				wAPSPartNoDetails.PartNo = wPartNo;
//
//				List<APSTaskPartDetails> wTaskPartList = wAPSTaskPartDetailsList.stream()
//						.filter(p -> p.PartNo.equals(wPartNo)).collect(Collectors.toList());
//				for (APSTaskPartDetails wAPSTaskPartDetails : wTaskPartList) {
//					wAPSPartNoDetails.OrderID = wAPSTaskPartDetails.OrderID;
//					wAPSPartNoDetails.StepFinish += wAPSTaskPartDetails.StepFinish;
//					wAPSPartNoDetails.StepMaking += wAPSTaskPartDetails.StepMaking;
//					wAPSPartNoDetails.StepSchedule += wAPSTaskPartDetails.StepSchedule;
//					wAPSPartNoDetails.StepSize += wAPSTaskPartDetails.StepSize;
//				}
//				wResult.Result.add(wAPSPartNoDetails);
//			}
//
//			if (wResult.Result.size() > 0) {
//				// 排序
//				wResult.Result.sort(Comparator.comparing(APSPartNoDetails::getPartNo));
//			}
//
//			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
//		} catch (Exception e) {
//			wResult.FaultCode += e.toString();
//			logger.error(e.toString());
//		}
//		return wResult;
//	}

//	@Override
//	public ServiceResult<List<APSWorkAreaDetails>> APS_QueryAreaDetails(BMSEmployee wLoginUser, Calendar wStartTime,
//			Calendar wEndTime, int wOrderID) {
//		ServiceResult<List<APSWorkAreaDetails>> wResult = new ServiceResult<List<APSWorkAreaDetails>>();
//		wResult.Result = new ArrayList<APSWorkAreaDetails>();
//		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
//		try {
//
//			ServiceResult<List<APSTaskStep>> wServiceResult = this.APS_QueryTaskStepByTime(wLoginUser, wStartTime,
//					wEndTime, false);
//
//			if (wServiceResult.Result == null || wServiceResult.Result.size() <= 0) {
//				return wResult;
//			}
//
//			List<APSTaskPartDetails> wAPSTaskPartDetailsList = (List<APSTaskPartDetails>) wServiceResult.CustomResult
//					.get("TaskPartList");
//			wAPSTaskPartDetailsList = wAPSTaskPartDetailsList.stream().filter(p -> p.OrderID == wOrderID)
//					.collect(Collectors.toList());
//			List<Integer> wAreaIDList = wAPSTaskPartDetailsList.stream().map(p -> p.AreaID).distinct()
//					.collect(Collectors.toList());
//			APSWorkAreaDetails wAPSWorkAreaDetails;
//			for (int wAreaID : wAreaIDList) {
//				wAPSWorkAreaDetails = new APSWorkAreaDetails();
//
//				List<APSTaskPartDetails> wTaskPartList = wAPSTaskPartDetailsList.stream()
//						.filter(p -> p.AreaID == wAreaID).collect(Collectors.toList());
//
//				for (APSTaskPartDetails wAPSTaskPartDetails : wTaskPartList) {
//					wAPSWorkAreaDetails.OrderID = wAPSTaskPartDetails.OrderID;
//					wAPSWorkAreaDetails.PartNo = wAPSTaskPartDetails.PartNo;
//					wAPSWorkAreaDetails.AreaID = wAPSTaskPartDetails.AreaID;
//					wAPSWorkAreaDetails.AreaName = wAPSTaskPartDetails.AreaName;
//					wAPSWorkAreaDetails.StepFinish += wAPSTaskPartDetails.StepFinish;
//					wAPSWorkAreaDetails.StepMaking += wAPSTaskPartDetails.StepMaking;
//					wAPSWorkAreaDetails.StepSchedule += wAPSTaskPartDetails.StepSchedule;
//					wAPSWorkAreaDetails.StepSize += wAPSTaskPartDetails.StepSize;
//				}
//				wResult.Result.add(wAPSWorkAreaDetails);
//			}
//
//			if (wResult.Result.size() > 0) {
//				// 排序
//				wResult.Result.sort(Comparator.comparing(APSWorkAreaDetails::getAreaName));
//			}
//
//			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
//		} catch (Exception e) {
//			wResult.FaultCode += e.toString();
//			logger.error(e.toString());
//		}
//		return wResult;
//	}

	@Override
	public ServiceResult<Integer> APS_CheckIsAuditor(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		wResult.FaultCode = "";
		try {
			if (!APSConstans.GetLFSWorkAreaCheckerList().stream().anyMatch(p -> p.LeaderIDList != null
					&& p.LeaderIDList.size() > 0 && p.LeaderIDList.stream().anyMatch(q -> q == wLoginUser.ID))) {
				wResult.FaultCode += "提示：登录者不是工区主管!";
				return wResult;
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_ReSetTaskStep(BMSEmployee wLoginUser, int wAPSTaskStepID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSTaskStepDAO.getInstance().ReSet(wLoginUser, wAPSTaskStepID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_BatchInPlantConfirm(BMSEmployee wLoginUser, List<OMSOrder> wDataList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wDataList.stream().anyMatch(p -> p.Status != OMSOrderStatus.ReceivedTelegraph.getValue())) {
				wResult.FaultCode += "提示：请选择状态为“已收电报”的订单!";
				return wResult;
			}

			for (OMSOrder wOrder : wDataList) {
				// 创建进厂确认单
				SFCOrderForm wSFCOrderForm = new SFCOrderForm(0, wOrder.ID, wOrder.OrderNo, wOrder.PartNo,
						wLoginUser.ID, wLoginUser.Name, Calendar.getInstance(), wLoginUser.ID, wLoginUser.Name,
						Calendar.getInstance(), 2, SFCOrderFormType.InConfirm.getValue(), wOrder.LineName,
						wOrder.ProductNo, wOrder.BureauSection);
				wResult.Result = SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);
				// 修改订单状态为已进场
				wOrder.Status = OMSOrderStatus.EnterFactoryed.getValue();
				wOrder.RealReceiveDate = Calendar.getInstance();

				// 获取最新的ERPID(排序字段)
				wOrder.ERPID = OMSOrderDAO.getInstance().GetMaxERPID(wLoginUser, wErrorCode);

				OMSOrderDAO.getInstance().Update(wLoginUser, wOrder, wErrorCode);

				// “广州铁路轨道装备有限公司”局段的车辆不绑定到看板上
				if (wOrder.CustomerID == 43) {
					continue;
				}

				// 将车绑定到厂线上(分多节情况)
				List<MTCSectionInfo> wSectionList = WDWServiceImpl.getInstance()
						.MTC_QuerySectionInfoList(wLoginUser, wOrder.ProductID).List(MTCSectionInfo.class);
				if (wSectionList != null && wSectionList.size() > 1) {
					for (MTCSectionInfo wSectionInfo : wSectionList) {
						String wPNo = StringUtils.Format("{0}-{1}", wOrder.PartNo, wSectionInfo.Name);

						WDWServiceImpl.getInstance().WDW_BindCarToPlant(wLoginUser, wPNo);
					}
				} else {
					WDWServiceImpl.getInstance().WDW_BindCarToPlant(wLoginUser, wOrder.PartNo);
				}
			}

//			String wFlag = Configuration.readConfigString("isSendSAP", "config/config");
//			if (wFlag.equals("1")) {
//				for (OMSOrder wOMSOrder : wDataList) {
//					ExecutorService wES = Executors.newFixedThreadPool(1);
//					wES.submit(() -> SendToSAP(BaseDAO.SysAdmin, wOMSOrder));
//					wES.shutdown();
//				}
//			}

			// 创建并推送台车BOM
			for (OMSOrder omsOrder : wDataList) {
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> APSServiceImpl.getInstance().APS_CreateBomItemByOrder(BaseDAO.SysAdmin, omsOrder.ID));
				wES.shutdown();
			}

			// 创建并推送台车BOM
//			APSConstans.mIBomOrderList = wDataList.stream().map(p -> p.ID).collect(Collectors.toList());
			// 创建并推送台车订单
//			APSConstans.mUOrderList = wDataList;

			// 修改订单的工艺路线为最新的
			ExecutorService wES = Executors.newFixedThreadPool(1);
			wES.submit(() -> UpdateRoute(wLoginUser, wDataList));
			wES.shutdown();

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 修改订单的工艺路线为最新的
	 */
	private void UpdateRoute(BMSEmployee wLoginUser, List<OMSOrder> wDataList) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			for (OMSOrder omsOrder : wDataList) {
				int wRouteID = OMSOrderDAO.getInstance().GetRouteIDByOrderID(wLoginUser, omsOrder.ID, wErrorCode);
				if (wRouteID <= 0) {
					continue;
				}
				omsOrder.RouteID = wRouteID;
				OMSOrderDAO.getInstance().Update(wLoginUser, omsOrder, wErrorCode);

				// 触发预检和机车一级解体任务
				TriggerPreCheckTask(wLoginUser, omsOrder);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 触发预检和机车一级解体任务
	 */
	private void TriggerPreCheckTask(BMSEmployee wLoginUser, OMSOrder omsOrder) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSTriggerPart> wList = APSTriggerPartDAO.getInstance().SelectList(wLoginUser, -1, omsOrder.ProductID,
					omsOrder.LineID, -1, 1, wErrorCode);
			if (wList == null || wList.size() <= 0) {
				return;
			}

			Calendar wStartTime = Calendar.getInstance();
			wStartTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
					Calendar.getInstance().get(Calendar.DATE), 8, 0, 0);
			Calendar wEndTime = (Calendar) wStartTime.clone();
			wEndTime.add(Calendar.HOUR_OF_DAY, 12);

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
			int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);

			// 触发工位任务
			for (APSTriggerPart apsTriggerPart : wList) {
				APSTaskPart wAPSTaskPart = new APSTaskPart(0, omsOrder.ID, omsOrder.PartNo, 0, omsOrder.LineID,
						apsTriggerPart.PartID, wShiftID, wLoginUser.ID, APSShiftPeriod.Week.getValue(),
						Calendar.getInstance(), wStartTime, wEndTime, omsOrder.OrderNo, 0, wLoginUser.Name, 1, 2,
						Calendar.getInstance(), omsOrder.CustomerID, omsOrder.OrderNo, omsOrder.RouteID, wLoginUser.ID,
						wLoginUser.Name);
				wAPSTaskPart.FinishWorkTime = wBaseTime;
				wAPSTaskPart.StartWorkTime = wBaseTime;
				wAPSTaskPart.ProductNo = omsOrder.ProductNo;
				wAPSTaskPart.ID = (int) APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
				if (wAPSTaskPart.ID <= 0) {
					continue;
				}
				// 触发工序任务
				List<FPCRoutePartPoint> wRoutePartPointList = FMCServiceImpl.getInstance()
						.FPC_QueryRoutePartPointListByRouteID(wLoginUser, omsOrder.RouteID, apsTriggerPart.PartID)
						.List(FPCRoutePartPoint.class);
				for (FPCRoutePartPoint wFPCRoutePartPoint : wRoutePartPointList) {
					APSTaskStep wAPSTaskStep = new APSTaskStep();

					wAPSTaskStep.ID = 0;
					wAPSTaskStep.Active = 1;
					wAPSTaskStep.LineID = wAPSTaskPart.LineID;
					wAPSTaskStep.MaterialNo = wAPSTaskPart.MaterialNo;
					wAPSTaskStep.OrderID = wAPSTaskPart.OrderID;
					wAPSTaskStep.PartID = wAPSTaskPart.PartID;
					wAPSTaskStep.PartNo = wAPSTaskPart.PartNo;
					wAPSTaskStep.PlanerID = wLoginUser.ID;
					wAPSTaskStep.ProductNo = wAPSTaskPart.ProductNo;
					wAPSTaskStep.ShiftID = wShiftID;
					wAPSTaskStep.Status = APSTaskStatus.Issued.getValue();
					wAPSTaskStep.StepID = wFPCRoutePartPoint.PartPointID;
					wAPSTaskStep.TaskPartID = wAPSTaskPart.ID;
					wAPSTaskStep.TaskLineID = wAPSTaskPart.TaskLineID;
					wAPSTaskStep.TaskText = wAPSTaskPart.TaskText;
					wAPSTaskStep.WorkHour = 0;
					wAPSTaskStep.WorkShopID = wAPSTaskPart.WorkShopID;
					wAPSTaskStep.StartTime = wBaseTime;
					wAPSTaskStep.EndTime = wBaseTime;
					wAPSTaskStep.ReadyTime = wBaseTime;
					wAPSTaskStep.CreateTime = Calendar.getInstance();
					wAPSTaskStep.PlanStartTime = wStartTime;
					wAPSTaskStep.PlanEndTime = wEndTime;

					APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<List<APSTaskPart>> APS_QueryWorkHourList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wOrderID) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSTaskPartDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1, -1, -1, 1,
					APSShiftPeriod.Week.getValue(), null, -1, wStartTime, wEndTime, wErrorCode);

			if (APSConstans.GetBMSDepartment(wLoginUser.DepartmentID).Type == BMSDepartmentType.Class.getValue()) {
				List<FMCWorkCharge> wChargeList = APSConstans.GetFMCWorkChargeList().stream()
						.filter(p -> p.Active == 1 && p.ClassID == wLoginUser.DepartmentID)
						.collect(Collectors.toList());
				wResult.Result = wResult.Result.stream()
						.filter(p -> wChargeList.stream().anyMatch(q -> q.StationID == p.PartID))
						.collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_Delete(BMSEmployee wLoginUser, int wOrderID, int wPartID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<APSTaskStep> wList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1, -1, -1, -1,
					wPartID, -1, -1, -1, null, null, null, null, wErrorCode);

			List<APSTaskStep> wOneList = new ArrayList<APSTaskStep>();
			for (APSTaskStep wAPSTaskStep : wList) {
				if (!wOneList.stream().anyMatch(p -> p.StepID == wAPSTaskStep.StepID)) {
					if (wList.stream().anyMatch(p -> p.StepID == wAPSTaskStep.StepID && p.Status != 1)) {
						wOneList.add(wList.stream().filter(p -> p.StepID == wAPSTaskStep.StepID && p.Status != 1)
								.findFirst().get());
					} else {
						wOneList.add(wAPSTaskStep);
					}
				}
			}

			List<APSTaskStep> wRepeatList = wList.stream().filter(p -> !wOneList.stream().anyMatch(q -> q.ID == p.ID))
					.collect(Collectors.toList());
			if (wRepeatList.size() > 0) {
				APSTaskStepDAO.getInstance().DeleteList(wLoginUser, wRepeatList, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<SFCTaskStepInfo>> APS_GetCarStationList(BMSEmployee wLoginUser, Calendar wTaskDate,
			int wWorkAreaID) {
		ServiceResult<List<SFCTaskStepInfo>> wResult = new ServiceResult<List<SFCTaskStepInfo>>();
		wResult.Result = new ArrayList<SFCTaskStepInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①将wTaskDate转换为一天的开始时间和结束时间
			Calendar wStartTime = (Calendar) wTaskDate.clone();
			wStartTime.set(Calendar.HOUR_OF_DAY, 0);
			wStartTime.set(Calendar.MINUTE, 0);
			wStartTime.set(Calendar.SECOND, 0);
			Calendar wEndTime = (Calendar) wTaskDate.clone();
			wEndTime.set(Calendar.HOUR_OF_DAY, 23);
			wEndTime.set(Calendar.MINUTE, 59);
			wEndTime.set(Calendar.SECOND, 59);
			// ①获取工位任务列表
			boolean wIsAll = false;
			if (CoreServiceImpl.getInstance()
					.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 501002, 0, 0)
					.Info(Boolean.class)) {
				wIsAll = true;
			}
			List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectDispatchList(wLoginUser, wStartTime, wEndTime,
					wIsAll, wErrorCode);

			// 有所有权限的人，提供工区筛选
			if (wIsAll && wWorkAreaID > 0) {
				// ①获取工区工位
				List<LFSWorkAreaStation> wWSList = APSConstans.GetLFSWorkAreaStationList().values().stream()
						.collect(Collectors.toList());
				// ②筛选工位任务
				if (wWSList != null && wWSList.size() > 0) {
					wList = wList.stream()
							.filter(p -> wWSList.stream().anyMatch(
									q -> q.Active == 1 && q.WorkAreaID == wWorkAreaID && q.StationID == p.PartID))
							.collect(Collectors.toList());
				}
			} else {
				// 筛选车型、班组、工位
				List<BMSProductClassPart> wPCPList = BMSWorkChargeItemDAO.getInstance()
						.GetProductClassPartList(wLoginUser, wLoginUser.DepartmentID, wErrorCode);
				if (wPCPList.size() > 0) {
					wList = wList.stream()
							.filter(p -> wPCPList.stream()
									.anyMatch(q -> q.ProductNo.equals(p.ProductNo) && q.PartID == p.PartID))
							.collect(Collectors.toList());
				}
			}

			// ②转换为工位信息列表
			for (APSTaskPart wAPSTaskPart : wList) {
				// 判断日计划是否已下达
				if (!APSTaskPartDAO.getInstance().IsIssueDayPlan(wLoginUser, wAPSTaskPart.ID, wErrorCode)) {
					continue;
				}

				SFCTaskStepInfo wInfo = APSTaskPartDAO.getInstance().SelectToDoAndDone(wLoginUser, wAPSTaskPart.ID,
						wErrorCode);
				SFCTaskStepInfo wSFCTaskStepInfo = new SFCTaskStepInfo();
				wSFCTaskStepInfo.Dispatched = wInfo.Dispatched;
				wSFCTaskStepInfo.OrderID = wAPSTaskPart.OrderID;
				wSFCTaskStepInfo.OrderNo = wAPSTaskPart.OrderNo;
				wSFCTaskStepInfo.PartNo = wAPSTaskPart.PartNo;
				wSFCTaskStepInfo.StationID = wAPSTaskPart.PartID;
				wSFCTaskStepInfo.StationName = wAPSTaskPart.PartName;
				wSFCTaskStepInfo.ToDispatch = wInfo.ToDispatch;
				wSFCTaskStepInfo.Status = wInfo.ToDispatch == 0 ? 1 : 0;
				wResult.Result.add(wSFCTaskStepInfo);
			}
			// ①获取质量工位数据
			List<APSTaskStep> wTaskStepList = APSTaskStepDAO.getInstance().SelectFinalAndOutList(wLoginUser, wStartTime,
					wEndTime, wErrorCode);
			// ②根据订单和工位去重
			wTaskStepList = wTaskStepList.stream()
					.collect(Collectors.toMap(p -> p.getOrderID() + "_" + p.getPartID(), p -> p, (o1, o2) -> o1))
					.values().stream().collect(Collectors.toList());

			// ③遍历获取数据
			for (APSTaskStep wAPSTaskStep : wTaskStepList) {
				List<APSTaskStep> wAllTaskStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1,
						wAPSTaskStep.OrderID, -1, -1, -1, -1, wAPSTaskStep.PartID, -1, -1, 1, null, null, null, null,
						wErrorCode);
				SFCTaskStepInfo wSFCTaskStepInfo = new SFCTaskStepInfo();
				wSFCTaskStepInfo.Dispatched = (int) wAllTaskStepList.stream().filter(p -> p.IsDispatched).count();
				wSFCTaskStepInfo.OrderID = wAPSTaskStep.OrderID;
				wSFCTaskStepInfo.OrderNo = wAPSTaskStep.OrderNo;
				wSFCTaskStepInfo.PartNo = wAPSTaskStep.PartNo;
				wSFCTaskStepInfo.StationID = wAPSTaskStep.PartID;
				wSFCTaskStepInfo.StationName = wAPSTaskStep.PartName;
				wSFCTaskStepInfo.ToDispatch = (int) wAllTaskStepList.stream().filter(p -> !p.IsDispatched).count();
				wSFCTaskStepInfo.Status = wSFCTaskStepInfo.ToDispatch == 0 ? 1 : 0;
				wResult.Result.add(wSFCTaskStepInfo);
			}
			// ④去重
			wResult.Result = wResult.Result.stream()
					.collect(Collectors.collectingAndThen(Collectors.toCollection(
							() -> new TreeSet<>(Comparator.comparing(o -> o.getPartNo() + ";" + o.getStationName()))),
							ArrayList::new));

			// ⑤工区赋值
			if (wResult.Result.size() > 0) {
				List<LFSWorkAreaStation> wWorkStationList = APSConstans.GetLFSWorkAreaStationList().values().stream()
						.collect(Collectors.toList());
				wWorkStationList = wWorkStationList.stream().filter(p -> p.Active == 1 && p.WorkAreaID > 0)
						.collect(Collectors.toList());
				for (SFCTaskStepInfo wSFCTaskStepInfo : wResult.Result) {
					if (!wWorkStationList.stream().anyMatch(p -> p.StationID == wSFCTaskStepInfo.StationID)) {
						continue;
					}

					LFSWorkAreaStation wWorkArea = wWorkStationList.stream()
							.filter(p -> p.StationID == wSFCTaskStepInfo.StationID).findFirst().get();
					wSFCTaskStepInfo.AreaID = wWorkArea.WorkAreaID;
					wSFCTaskStepInfo.AreaName = APSConstans.GetBMSDepartmentName(wWorkArea.WorkAreaID);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryTaskStepListByOrder(BMSEmployee wLoginUser, Calendar wTaskDate,
			int wOrderID, int wPartID, String wPartNo) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ③通过时间段和工位列表查询工位任务集合
			wResult.Result = APSTaskStepDAO.getInstance().SelectListByOrder(wLoginUser, wOrderID, wPartID, wPartNo,
					wErrorCode);
//			wResult.Result.removeIf(p -> p.Status == APSTaskStatus.Saved.getValue());
			// 去重
			wResult.Result = new ArrayList<APSTaskStep>(wResult.Result.stream()
					.collect(Collectors.toMap(APSTaskStep::getID, account -> account, (k1, k2) -> k2)).values());

			if (wResult.Result.size() <= 0) {
				return wResult;
			}

			/**
			 * 默认人员返回(没派工记录时默认返回)
			 */
			Map<Integer, List<Integer>> wPersonMap = SFCServiceImpl.getInstance().SFC_QueryStepPersonMap(wLoginUser,
					wResult.Result).Result;

			List<Integer> wAPSTaskstepIDList = wResult.Result.stream().map(p -> p.ID).collect(Collectors.toList());
			List<SFCTaskStep> wList = SFCTaskStepDAO.getInstance().SelectListByTaskStepIDList(wLoginUser,
					wAPSTaskstepIDList, SFCTaskStepType.Step.getValue(), wErrorCode);

			// 查询自检、互检、专检单的状态
			List<SFCTaskIPT> wSFCTaskIPTList = SFCTaskStepDAO.getInstance().GetCheckTaskStatusList(wLoginUser,
					wAPSTaskstepIDList, wErrorCode);

			for (APSTaskStep wAPSTaskStep : wResult.Result) {
				// 自检互检专检状态
				if (wSFCTaskIPTList.stream().anyMatch(p -> p.TaskStepID == wAPSTaskStep.ID && p.TaskType == 6)) {
					wAPSTaskStep.SelfStatus = wSFCTaskIPTList.stream()
							.filter(p -> p.TaskStepID == wAPSTaskStep.ID && p.TaskType == 6).findFirst().get().Status;
				}
				if (wSFCTaskIPTList.stream().anyMatch(p -> p.TaskStepID == wAPSTaskStep.ID && p.TaskType == 12)) {
					wAPSTaskStep.MutualStatus = wSFCTaskIPTList.stream()
							.filter(p -> p.TaskStepID == wAPSTaskStep.ID && p.TaskType == 12).findFirst().get().Status;
				}
				if (wSFCTaskIPTList.stream().anyMatch(p -> p.TaskStepID == wAPSTaskStep.ID && p.TaskType == 13)) {
					wAPSTaskStep.SpecialStatus = wSFCTaskIPTList.stream()
							.filter(p -> p.TaskStepID == wAPSTaskStep.ID && p.TaskType == 13).findFirst().get().Status;
				}

				List<SFCTaskStep> wTempList = wList.stream().filter(p -> p.TaskStepID == wAPSTaskStep.ID)
						.collect(Collectors.toList());

				if (wAPSTaskStep.OperatorList != null && wAPSTaskStep.OperatorList.size() > 0) {
					wAPSTaskStep.Operators = GetUserNames(wAPSTaskStep.OperatorList);
					continue;
				}

				if (wTempList != null && wTempList.size() > 0) {
					wAPSTaskStep.OperatorList = wTempList.stream().map(p -> p.OperatorID).collect(Collectors.toList());
					continue;
				}

				if (!wPersonMap.containsKey(wAPSTaskStep.ID)) {
					continue;
				}
				wAPSTaskStep.OperatorList = wPersonMap.get(wAPSTaskStep.ID);
				wAPSTaskStep.Operators = GetUserNames(wPersonMap.get(wAPSTaskStep.ID));

			}

			// ①获取工序中所有不在此班组的人员列表
			int wMonitorClassID = APSConstans.GetBMSEmployee(wLoginUser.ID).DepartmentID;
			List<Integer> wNotInClassPersonIDList = new ArrayList<Integer>();
			for (APSTaskStep wAPSTaskStep : wResult.Result) {
				if (wAPSTaskStep.IsDispatched) {
					continue;
				}
				for (Integer wPersonID : wAPSTaskStep.OperatorList) {
					if (APSConstans.GetBMSEmployee(wPersonID).DepartmentID != wMonitorClassID) {
						if (!wNotInClassPersonIDList.stream().anyMatch(p -> p == wPersonID)) {
							wNotInClassPersonIDList.add(wPersonID);
						}
					}
				}
			}
			// ②根据这些人员获取借调单列表
			List<SCHSecondmentApply> wSecondList = new ArrayList<SCHSecondmentApply>();
			if (wNotInClassPersonIDList.size() > 0) {
				wSecondList = SCHSecondmentApplyDAO.getInstance().SelectListByPersonIDList(wLoginUser,
						wNotInClassPersonIDList, wErrorCode);
			}

			// ③判断此人是否有借调到本班组且未到期的借调单
			List<Integer> wNeedRemovePersonIDList = new ArrayList<Integer>();
			for (Integer wPersonID : wNotInClassPersonIDList) {
				// ④若没有，加到需要移除的集合里
				if (!wSecondList.stream()
						.anyMatch(p -> p.PersonID.contains(String.valueOf(wPersonID)) && p.NewClassID == wMonitorClassID
								&& Calendar.getInstance().compareTo(p.StartTime) >= 0
								&& Calendar.getInstance().compareTo(p.EndTime) <= 0)) {
					wNeedRemovePersonIDList.add(wPersonID);
				}
			}
			// ⑤遍历工序列表，移除，未派工且在移除集合里的人
			for (APSTaskStep wAPSTaskStep : wResult.Result) {
				if (wAPSTaskStep.IsDispatched) {
					continue;
				}

				if (wAPSTaskStep.OperatorList == null || wAPSTaskStep.OperatorList.size() <= 0) {
					continue;
				}

				wAPSTaskStep.OperatorList.removeIf(p -> wNeedRemovePersonIDList.stream().anyMatch(q -> q == p));
				wAPSTaskStep.Operators = GetUserNames(wAPSTaskStep.OperatorList);
			}

			// 筛选激活的数据
			wResult.Result = wResult.Result.stream().filter(p -> p.Active == 1).collect(Collectors.toList());

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskPart>> APS_QueryMoreTaskPartList(BMSEmployee wLoginUser,
			List<Integer> wOrderIDList, Calendar wStartTime, Calendar wEndTime, int wAPSShiftPeriod) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<>(Integer.valueOf(0));

			wResult.Result = APSTaskPartDAO.getInstance().SelectListByOrderListAndTime(wLoginUser, wOrderIDList,
					wStartTime, wEndTime, wAPSShiftPeriod, wErrorCode);

			// 工位排序
			wResult.Result = OrderPart(wResult.Result);

			wResult.setFaultCode(MESException.getEnumType(((Integer) wErrorCode.Result).intValue()).getLable());
		} catch (Exception e) {
			logger.error(StringUtils.Format("{0} {1} ex：{2}",
					new Object[] { "APSServiceImpl", "APS_QueryTaskPartList", e.toString() }));
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStepInfo>> APS_QueryTaskStepInfo(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, List<Integer> wClassIDList, List<Integer> wPartIDList, List<Integer> wStatusList,
			int wIsDisPatched) {
		ServiceResult<List<APSTaskStepInfo>> wResult = new ServiceResult<List<APSTaskStepInfo>>();
		wResult.Result = new ArrayList<APSTaskStepInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 1, 1);
			if (wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime.set(3000, 1, 1);
			}

			if (wPartIDList == null || wPartIDList.size() <= 0
					|| (wPartIDList.size() == 1 && wPartIDList.get(0) == -1)) {
				wPartIDList = APSServiceImpl.getInstance().APS_QueryStationList(wLoginUser).Result.stream()
						.map(p -> p.ID).collect(Collectors.toList());
			}
			if (wClassIDList == null || wClassIDList.size() <= 0
					|| (wClassIDList.size() == 1 && wClassIDList.get(0) == -1)) {
				wClassIDList = APSServiceImpl.getInstance().APS_QueryClassList(wLoginUser).Result.stream()
						.map(p -> p.ID).collect(Collectors.toList());
			}
			if (wStatusList == null || wStatusList.size() <= 0
					|| (wStatusList.size() == 1 && wStatusList.get(0) == -1)) {
				wStatusList = new ArrayList<Integer>();
			}

			wResult.Result = APSTaskStepDAO.getInstance().QueryAPSTaskStepInfoList(wLoginUser, wStartTime, wEndTime,
					wClassIDList, wPartIDList, wStatusList, wIsDisPatched, wErrorCode);

			List<LFSWorkAreaStation> wList = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.filter(p -> p.Active == 1).collect(Collectors.toList());
			for (APSTaskStepInfo wAPSTaskStepInfo : wResult.Result) {
				if (wList.stream().anyMatch(p -> p.StationID == wAPSTaskStepInfo.PartID)) {
					LFSWorkAreaStation wItem = wList.stream().filter(p -> p.StationID == wAPSTaskStepInfo.PartID)
							.findFirst().get();
					wAPSTaskStepInfo.AreaID = wItem.WorkAreaID;
					wAPSTaskStepInfo.Area = APSConstans.GetBMSDepartmentName(wItem.WorkAreaID);
				}
			}

			// 排序
			if (wResult.Result.size() > 0) {
				wResult.Result
						.sort(Comparator.comparing(APSTaskStepInfo::getPartNo).thenComparing(APSTaskStepInfo::getID));
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BMSDepartment>> APS_QueryClassList(BMSEmployee wLoginUser) {
		ServiceResult<List<BMSDepartment>> wResult = new ServiceResult<List<BMSDepartment>>();
		wResult.Result = new ArrayList<BMSDepartment>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<BMSDepartment> wList = APSConstans.GetBMSDepartmentList().values().stream()
					.filter(p -> p.Active == 1 && p.Type == 3).collect(Collectors.toList());
			if (wList.stream().anyMatch(p -> p.ID == wLoginUser.DepartmentID)) {
				wResult.Result = wList.stream().filter(p -> p.ID == wLoginUser.DepartmentID)
						.collect(Collectors.toList());
				wResult.CustomResult.put("IsAllClass", false);
			} else {
				wResult.Result = wList;
				wResult.CustomResult.put("IsAllClass", true);
			}

			ServiceResult<List<FPCPart>> wSR = APSServiceImpl.getInstance().APS_QueryStationList(wLoginUser);
			boolean wIsAllPart = (boolean) wSR.Get("IsAllPart");
			wResult.CustomResult.put("IsAllPart", wIsAllPart);
			wResult.CustomResult.put("PartList", wSR.Result);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FPCPart>> APS_QueryStationList(BMSEmployee wLoginUser) {
		ServiceResult<List<FPCPart>> wResult = new ServiceResult<List<FPCPart>>();
		wResult.Result = new ArrayList<FPCPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<FMCWorkCharge> wList = APSConstans.GetFMCWorkChargeList().stream()
					.filter(p -> p.ClassID == wLoginUser.DepartmentID).collect(Collectors.toList());
			if (wList.size() > 0) {
				for (FMCWorkCharge wFMCWorkCharge : wList) {
					wResult.Result.add(APSConstans.GetFPCPart(wFMCWorkCharge.StationID));
				}
				wResult.CustomResult.put("IsAllPart", false);
			} else {
				wResult.Result = APSConstans.GetFPCPartList().values().stream().filter(p -> p.Active == 1)
						.collect(Collectors.toList());
				wResult.CustomResult.put("IsAllPart", true);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStepDetails>> APS_QueryTransInfo(BMSEmployee wLoginUser, int wAPSTaskPartID) {
		ServiceResult<List<APSTaskStepDetails>> wResult = new ServiceResult<List<APSTaskStepDetails>>();
		wResult.Result = new ArrayList<APSTaskStepDetails>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSTaskStepDAO.getInstance().SelectAPSTaskStepDetails(wLoginUser, wAPSTaskPartID,
					wErrorCode);

			wResult.CustomResult.put("APSTaskPart",
					APSTaskPartDAO.getInstance().SelectByID(wLoginUser, wAPSTaskPartID, wErrorCode));

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_CreateTaskStepByShiftDateNew(BMSEmployee wLoginUser,
			Calendar wShiftDate) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			Calendar wShiftEnd = CalendarUtil.GetDate(wShiftDate);
			wShiftEnd.set(Calendar.HOUR_OF_DAY, 23);
			wShiftEnd.set(Calendar.MINUTE, 59);
			wShiftEnd.set(Calendar.SECOND, 59);

			// 工位详情
			List<APSTaskPartDetails> wAPSTaskPartDetailsList = APSTaskPartDAO.getInstance()
					.SelectAPSTaskPartDetailsList(wLoginUser, wShiftEnd, wErrorCode);
			// 工序详情
			wResult.Result = APSTaskStepDAO.getInstance().SelectAPSTaskStepList(wLoginUser, wShiftEnd, wErrorCode);
			// 筛选排了计划的工序计划
			wResult.Result = wResult.Result.stream().filter(p -> wShiftEnd.compareTo(p.PlanStartTime) > 0)
					.collect(Collectors.toList());
			// 订单详情
			List<OMSOrder> wOrderList = new ArrayList<OMSOrder>();
			if (wAPSTaskPartDetailsList.size() > 0) {
				wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser,
						wAPSTaskPartDetailsList.stream().map(p -> p.OrderID).collect(Collectors.toList()), wErrorCode);
			}
			// 重新赋值工位任务
			if (wAPSTaskPartDetailsList.size() > 0) {
				List<APSTaskPart> wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
						wAPSTaskPartDetailsList.stream().map(p -> p.APSTaskPart.ID).collect(Collectors.toList()),
						wErrorCode);
				for (APSTaskPartDetails wAPSTaskPartDetails : wAPSTaskPartDetailsList) {
					if (wTaskPartList.stream().anyMatch(p -> p.ID == wAPSTaskPartDetails.APSTaskPart.ID)) {
						wAPSTaskPartDetails.APSTaskPart = wTaskPartList.stream()
								.filter(p -> p.ID == wAPSTaskPartDetails.APSTaskPart.ID).findFirst().get();
					}
				}
			}
			// 剔除没有工序的工位
			wAPSTaskPartDetailsList.removeIf(
					p -> !wResult.Result.stream().anyMatch(q -> q.OrderID == p.OrderID && q.PartID == p.PartID));
			// 返回数据
			wResult.CustomResult.put("OrderList", wOrderList);
			wResult.CustomResult.put("TaskPartList", wAPSTaskPartDetailsList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSPartNoDetails>> APS_QueryPartNoDetailsNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wWorkAreaID) {
		ServiceResult<List<APSPartNoDetails>> wResult = new ServiceResult<List<APSPartNoDetails>>();
		wResult.Result = new ArrayList<APSPartNoDetails>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 今日时间
			Calendar wTodaySTime = Calendar.getInstance();
			wTodaySTime.set(Calendar.HOUR_OF_DAY, 0);
			wTodaySTime.set(Calendar.MINUTE, 0);
			wTodaySTime.set(Calendar.SECOND, 0);
			Calendar wTodayETime = Calendar.getInstance();
			wTodayETime.set(Calendar.HOUR_OF_DAY, 23);
			wTodayETime.set(Calendar.MINUTE, 59);
			wTodayETime.set(Calendar.SECOND, 59);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);
			if (wStartTime.compareTo(wBaseTime) < 0) {
				wStartTime = wTodaySTime;
			}
			if (wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wTodayETime;
			}

			wResult.Result = APSTaskStepDAO.getInstance().SelectAPSPartNoDetailsList(wLoginUser, wStartTime, wEndTime,
					wWorkAreaID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSWorkAreaDetails>> APS_QueryAreaDetailsNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wOrderID, int wWorkAreaID) {
		ServiceResult<List<APSWorkAreaDetails>> wResult = new ServiceResult<List<APSWorkAreaDetails>>();
		wResult.Result = new ArrayList<APSWorkAreaDetails>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 今日时间
			Calendar wTodaySTime = Calendar.getInstance();
			wTodaySTime.set(Calendar.HOUR_OF_DAY, 0);
			wTodaySTime.set(Calendar.MINUTE, 0);
			wTodaySTime.set(Calendar.SECOND, 0);
			Calendar wTodayETime = Calendar.getInstance();
			wTodayETime.set(Calendar.HOUR_OF_DAY, 23);
			wTodayETime.set(Calendar.MINUTE, 59);
			wTodayETime.set(Calendar.SECOND, 59);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);
			if (wStartTime.compareTo(wBaseTime) < 0) {
				wStartTime = wTodaySTime;
			}
			if (wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wTodayETime;
			}

			wResult.Result = APSTaskStepDAO.getInstance().SelectAPSWorkAreaDetailsList(wLoginUser, wStartTime, wEndTime,
					wOrderID, wErrorCode);
			if (wWorkAreaID > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.AreaID == wWorkAreaID)
						.collect(Collectors.toList());
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryTaskStepByTimeNew(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wAreaID, int wOrderID) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 今日时间
			Calendar wTodaySTime = Calendar.getInstance();
			wTodaySTime.set(Calendar.HOUR_OF_DAY, 0);
			wTodaySTime.set(Calendar.MINUTE, 0);
			wTodaySTime.set(Calendar.SECOND, 0);
			Calendar wTodayETime = Calendar.getInstance();
			wTodayETime.set(Calendar.HOUR_OF_DAY, 23);
			wTodayETime.set(Calendar.MINUTE, 59);
			wTodayETime.set(Calendar.SECOND, 59);

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2010, 0, 1, 0, 0, 0);
			if (wStartTime.compareTo(wBaseTime) < 0) {
				wStartTime = wTodaySTime;
			}
			if (wEndTime.compareTo(wBaseTime) < 0) {
				wEndTime = wTodayETime;
			}

			wResult.Result = APSTaskStepDAO.getInstance().SelectQueryTaskStep(wLoginUser, wStartTime, wEndTime, wAreaID,
					wOrderID, wErrorCode);

			List<APSTaskPartDetails> wAPSTaskPartDetailsList = new ArrayList<APSTaskPartDetails>();
			List<Integer> wTaskPartIDList = wResult.Result.stream().map(p -> p.TaskPartID).distinct()
					.collect(Collectors.toList());

			List<APSTaskPart> wTaskPartList = new ArrayList<APSTaskPart>();
			if (wTaskPartIDList.size() > 0) {
				wTaskPartList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser, wTaskPartIDList,
						wErrorCode);
			}

			for (Integer wTaskPartID : wTaskPartIDList) {
				APSTaskPartDetails wAPSTaskPartDetails = new APSTaskPartDetails();
				if (wTaskPartList.stream().anyMatch(p -> p.ID == wTaskPartID)) {
					wAPSTaskPartDetails.APSTaskPart = wTaskPartList.stream().filter(p -> p.ID == wTaskPartID)
							.findFirst().get();
					APSTaskStep wTempStep = wResult.Result.stream().filter(p -> p.TaskPartID == wTaskPartID).findFirst()
							.get();
					wAPSTaskPartDetails.AreaID = wTempStep.AreaID;
					wAPSTaskPartDetails.AreaName = wTempStep.AreaName;
					wAPSTaskPartDetails.OrderID = wTempStep.OrderID;
					wAPSTaskPartDetails.OrderNum = wTempStep.OrderNum;
					wAPSTaskPartDetails.PartID = wTempStep.PartID;
					wAPSTaskPartDetails.PartNo = wTempStep.PartNo;
					wAPSTaskPartDetails.ShiftDate = wAPSTaskPartDetails.APSTaskPart.ShiftDate;

					wAPSTaskPartDetails.StepSize = (int) wResult.Result.stream()
							.filter(p -> p.TaskPartID == wTaskPartID).count();
					wAPSTaskPartDetails.StepFinish = (int) wResult.Result.stream()
							.filter(p -> p.TaskPartID == wTaskPartID && p.Status == 5).count();
					wAPSTaskPartDetails.StepSchedule = (int) wResult.Result.stream().filter(
							p -> p.TaskPartID == wTaskPartID && (p.Status == 2 || p.Status == 4 || p.Status == 5))
							.count();
					wAPSTaskPartDetails.StepMaking = (int) wResult.Result.stream().filter(
							p -> p.TaskPartID == wTaskPartID && (p.Status == 8 || p.Status == 9 || p.Status == 10))
							.count();

					wAPSTaskPartDetailsList.add(wAPSTaskPartDetails);
				}
			}

			List<OMSOrder> wOrderList = new ArrayList<OMSOrder>();
			if (wResult.Result.size() > 0) {
				wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser,
						wResult.Result.stream().map(p -> p.OrderID).distinct().collect(Collectors.toList()),
						wErrorCode);
			}

			// 排序
			wAPSTaskPartDetailsList.sort(Comparator.comparing(APSTaskPartDetails::getOrderNum));

			wResult.CustomResult.put("OrderList", wOrderList);
			wResult.CustomResult.put("TaskPartList", wAPSTaskPartDetailsList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FMCWorkspace>> APS_QueryPartNoStatusNew(BMSEmployee wLoginUser,
			List<FMCWorkspace> wList) {
		ServiceResult<List<FMCWorkspace>> wResult = new ServiceResult<List<FMCWorkspace>>();
		wResult.Result = new ArrayList<FMCWorkspace>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			Map<Integer, String> wWorkspaceCarMap = AndonDAO.getInstance().QueryWorkspaceCarMap(wLoginUser, wErrorCode);
			List<Integer> wOrderIDList = new ArrayList<Integer>();
			List<String> wPartNoList = new ArrayList<String>();
			for (Integer wSpaceID : wWorkspaceCarMap.keySet()) {
				String wPartNo = wWorkspaceCarMap.get(wSpaceID).replace("[A]", "").replace("[B]", "");
				if (wPartNo.contains("-")) {
					wPartNo = wPartNo.split("-")[0];
				}
				wPartNoList.add(wPartNo);
			}

			wOrderIDList = OMSOrderDAO.getInstance().SelectIDList(wLoginUser, wPartNoList, wErrorCode);

			if (wOrderIDList.size() > 0) {
				Map<String, Integer> wStatusMap = AndonDAO.getInstance().QueryCarStatusMapNew(wLoginUser,
						StringUtils.Join(",", wOrderIDList), wErrorCode);
				for (FMCWorkspace wFMCWorkspace : wList) {
					if (StringUtils.isEmpty(wFMCWorkspace.PartNo)) {
						continue;
					}
					String wPartNo = wFMCWorkspace.PartNo.replace("[A]", "").replace("[B]", "");
					if (wPartNo.contains("-")) {
						wPartNo = wPartNo.split("-")[0];
//						wPartNo = StringUtils.Format("{0}#{1}", wPartNo.split("_")[0],
//								wPartNo.split("_")[1].split("#")[1]);
					}
					if (wStatusMap.containsKey(wPartNo)) {
						wFMCWorkspace.Status = wStatusMap.get(wPartNo);
					}
				}
			}

			wResult.Result = wList;

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void APS_TriggerDayPlans(BMSEmployee wAdminUser) {
		try {
			if (APSConstans.mAPSSchedulingVersion == null) {
				return;
			}

			// 触发质量日计划
			TriggerQualityDayPlans(wAdminUser, APSConstans.mAPSSchedulingVersion);
			// 制定所有的日计划
			MakingAllTaskStep(wAdminUser, APSConstans.mAPSSchedulingVersion);
			// 置为空
			APSConstans.mAPSSchedulingVersion = null;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public void APS_TriggerDayPlans(BMSEmployee wAdminUser, APSSchedulingVersion wAPSSchedulingVersion) {
		try {
			// 触发质量日计划
			TriggerQualityDayPlans(wAdminUser, wAPSSchedulingVersion);
			// 制定所有的日计划
			MakingAllTaskStep(wAdminUser, wAPSSchedulingVersion);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<List<APSTaskStep>> APS_QueryOrderTimeInfoList(BMSEmployee wLoginUser, int wOrderID,
			String wStationName) {
		ServiceResult<List<APSTaskStep>> wResult = new ServiceResult<List<APSTaskStep>>();
		wResult.Result = new ArrayList<APSTaskStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSTaskStepDAO.getInstance().APS_QueryOrderTimeInfoList(wLoginUser, wOrderID, wStationName,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void APS_ReMakingDayPlan(BMSEmployee wAdminUser) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			int wShiftID = MESServer.MES_QueryShiftID(wAdminUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			SimpleDateFormat wSDF = new SimpleDateFormat("HH");
			int wHour = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));

			if (wShiftID != APSConstans.mDayPlanShiftID && wHour >= 1 && wHour <= 2) {
				APSConstans.mDayPlanShiftID = wShiftID;
				// ①将shiftID小于当日的，状态为下达的日计划状态改为保存
				List<APSTaskStep> wList = APSTaskStepDAO.getInstance().SelectListByShiftID(wAdminUser, wShiftID,
						wErrorCode);
				// ①去除质量日计划
				wList.removeIf(p -> APSConstans.GetFPCPart(p.PartID).PartType == 3
						|| APSConstans.GetFPCPart(p.PartID).PartType == 4);

				for (APSTaskStep wAPSTaskStep : wList) {
					// ②将这些日计划的派工属性改为0
					wAPSTaskStep.IsDispatched = false;
					// ③将这些日计划的派工人员清空
					wAPSTaskStep.OperatorList = new ArrayList<Integer>();
					// ④将这些日计划的状态改为保存
					wAPSTaskStep.Status = APSTaskStatus.Saved.getValue();
					// ⑤提交数据
					APSTaskStepDAO.getInstance().Update(wAdminUser, wAPSTaskStep, wErrorCode);
					// ④将这些日计划的派工任务删除
					List<SFCTaskStep> wSFCTaskStepList = SFCTaskStepDAO.getInstance().SelectList(wAdminUser, -1,
							wAPSTaskStep.ID, -1, -1, -1, null, -1, wErrorCode);
					if (wSFCTaskStepList.size() > 0) {
						SFCTaskStepDAO.getInstance().DeleteList(wAdminUser, wSFCTaskStepList, wErrorCode);
					}
					// ⑤将这些日计划的派工消息删除
					SFCTaskStepDAO.getInstance().DeleteMessage(wAdminUser, wSFCTaskStepList, wErrorCode);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override

	public ServiceResult<List<APSBOMItem>> APS_QueryBOMItemList(BMSEmployee wLoginUser, int wOrderID, String wWBSNo,
			String wPartNo, int wLineID, int wProductID, int wCustomerID, int wPartID, int wPartPointID,
			int wMaterialID, String wMaterialNo, int wBOMType, int wReplaceType, int wOutsourceType,
			List<Integer> wStatus, int wDifferenceItem, int wOverQuota, int wSourceType) {
		ServiceResult<List<APSBOMItem>> wResult = new ServiceResult<List<APSBOMItem>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wErrorCode.set(0);

			wResult.Result = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, wOrderID, wWBSNo, wPartNo,
					wLineID, wProductID, wCustomerID, wPartID, wPartPointID, wMaterialID, wMaterialNo, wBOMType,
					wReplaceType, wOutsourceType, wStatus, wDifferenceItem, wOverQuota, wSourceType, wErrorCode);

			wResult.Result.removeIf(p -> p.DeleteID.equals("X"));

			wResult.FaultCode += MESException.getEnumType(wErrorCode.Result).getLable();

		} catch (Exception e) {

			logger.error(e.toString());

		}

		return wResult;

	}

	@Override

	public ServiceResult<APSBOMItem> APS_QueryBOMItemByID(BMSEmployee wLoginUser, int wID) {

		ServiceResult<APSBOMItem> wResult = new ServiceResult<APSBOMItem>();

		try {

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wErrorCode.set(0);

			wResult.Result = APSBOMItemDAO.getInstance().APS_QueryBOMItemByID(wLoginUser, wID, wErrorCode);

			wResult.FaultCode += MESException.getEnumType(wErrorCode.Result).getLable();

		} catch (Exception e) {

			logger.error(e.toString());

		}

		return wResult;

	}

	@Override

	public ServiceResult<Integer> APS_UpdateBOMItem(BMSEmployee wLoginUser, APSBOMItem wBOMItem) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wErrorCode.set(0);

			// 判断是否为差异项
			wBOMItem.DifferenceItem = APSBOMItemDAO.getInstance().JudgeIsDifferenceItem(wLoginUser, wBOMItem,
					wErrorCode);

			// 修改评估类型
			if (wBOMItem.SourceType != APSBOMSourceType.SFCBOMTask.getValue()
					&& wBOMItem.SourceType != APSBOMSourceType.BOMAudit.getValue()) {
				if (wBOMItem.ReplaceType == 1) {
					wBOMItem.AssessmentType = "常规新件";
				} else if (wBOMItem.OutsourceType == 1 || wBOMItem.OutsourceType == 2) {
					wBOMItem.AssessmentType = "修复旧件";
				} else if (wBOMItem.PartChange > 0) {
					wBOMItem.AssessmentType = "修复旧件";
				} else if (wBOMItem.OutsourceType == 3 || wBOMItem.OutsourceType == 4) {
					wBOMItem.AssessmentType = "可用旧件";
				}
			}

			// 如果来源于标准BOM变更，如果是必换件或委外必修件 ，判断MES系统中是否存在相同的物料，存在，直接返回
			if (wBOMItem.SourceType == APSBOMSourceType.BOMChange.getValue()) {
				if (wBOMItem.ReplaceType == 1 || wBOMItem.OutsourceType == 1) {
					List<APSBOMItem> wExsitList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser,
							wBOMItem.OrderID, "", "", -1, -1, -1, wBOMItem.PartID, wBOMItem.PartPointID,
							wBOMItem.MaterialID, "", -1, wBOMItem.ReplaceType, wBOMItem.OutsourceType, null, -1, -1, -1,
							wErrorCode);
					if (wExsitList.size() > 0) {
						return wResult;
					}
				}
			}

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wBOMItem.OrderID, wErrorCode);

			List<APSBOMItem> wList = new ArrayList<APSBOMItem>(Arrays.asList(wBOMItem));

			// 验证台车BOM
			String wMsg = CheckAPSBOM(wLoginUser, wList, wOrder);

			if (StringUtils.isNotEmpty(wMsg)) {

				List<BFCMessage> wBFCMessageList = new ArrayList<>();
				BFCMessage wMessage = null;
				int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(),
						APSShiftPeriod.Day, FMCShiftLevel.Day, 0);
				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = 0;
				wMessage.Title = StringUtils.Format("【台车BOM】提示", BPMEventModule.StationTip.getLable());
				wMessage.MessageText = wMsg;
				wMessage.ModuleID = BPMEventModule.StationTip.getValue();
				wMessage.ResponsorID = StringUtils.parseInt(Configuration.readConfigString("bomUser", "config/config"));
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Notify.getValue();
				wBFCMessageList.add(wMessage);

				CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);

				// 返回提示信息
				wResult.FaultCode = wMsg;
				return wResult;
			}

			wBOMItem.ID = APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wBOMItem, wErrorCode);

			wBOMItem = APSBOMItemDAO.getInstance().APS_QueryBOMItemByID(wLoginUser, wBOMItem.ID, wErrorCode);

			wList = new ArrayList<APSBOMItem>(Arrays.asList(wBOMItem));

			String wSendSAP = Configuration.readConfigString("isSendSAP", "config/config");

			if (wSendSAP.equals("1")) {
				// ①推送台车BOM给SAP
				String wJson = ChangeToJson(wLoginUser, wList, "U");
				List<Integer> wAPSBOMIDList = wList.stream().map(p -> p.ID).distinct().collect(Collectors.toList());
				if (StringUtils.isNotEmpty(wJson)) {
					SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 0, wAPSBOMIDList);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSBOMItem>> APS_CreateBOMItem(BMSEmployee wLoginUser, int wRouteID,
			List<APSBOMItem> wBOMItemList) {
		ServiceResult<List<APSBOMItem>> wResult = new ServiceResult<List<APSBOMItem>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wBOMItemList == null || wBOMItemList.size() <= 0) {
				return wResult;
			}

			APSBOMItem wAPSBOMItem = wBOMItemList.get(0);

			List<MSSBOMItem> wMSSBOMItemList = WMSServiceImpl.getInstance().MSS_QueryBOMItemAll(wLoginUser, -1,
					wAPSBOMItem.LineID, wAPSBOMItem.ProductID, wAPSBOMItem.CustomerID, -1, -1, -1, -1, wRouteID)
					.List(MSSBOMItem.class);

			List<APSBOMItem> wResultList = new ArrayList<APSBOMItem>();

			APSBOMItem wBOMItem = null;

			for (MSSBOMItem wMSSBOMItem : wMSSBOMItemList) {

				if (wMSSBOMItem.Active != 1)
					continue;

				if (wMSSBOMItem.ReplaceType == 1 || wMSSBOMItem.OutsourceType == 1) {

					wBOMItem = new APSBOMItem(wMSSBOMItem, wAPSBOMItem.LineID, wAPSBOMItem.ProductID,
							wAPSBOMItem.CustomerID, wAPSBOMItem.OrderID, wAPSBOMItem.WBSNo, wAPSBOMItem.PartNo);

					wBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
					wBOMItem.SourceID = wMSSBOMItem.ID;

					wResultList.add(wBOMItem);
				}
			}

			wResultList.addAll(wBOMItemList);

			APSBOMItemDAO.getInstance().APS_CreateBOMItem(wLoginUser, wResultList, wErrorCode);

			wResult.Result = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, wAPSBOMItem.getOrderID(),

					wAPSBOMItem.getWBSNo(), wAPSBOMItem.getPartNo(), wAPSBOMItem.getLineID(),

					wAPSBOMItem.getProductID(), wAPSBOMItem.getCustomerID(), 0, 0, 0, "", 0, 0, 0, null, -1, -1, -1,
					wErrorCode);

			wResult.FaultCode += MESException.getEnumType(wErrorCode.Result).getLable();

		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSBOMItem>> APS_CreateBOMItem(BMSEmployee wLoginUser, int wRouteID, int wLineID,
			int wProductID, int wCustomerID, int wOrderID, String wWBSNo, String wPartNo) {
		ServiceResult<List<APSBOMItem>> wResult = new ServiceResult<List<APSBOMItem>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<MSSBOMItem> wMSSBOMItemList = WMSServiceImpl.getInstance()
					.MSS_QueryBOMItemAll(wLoginUser, -1, wLineID, wProductID, wCustomerID, -1, -1, -1, -1, wRouteID)
					.List(MSSBOMItem.class);

//			List<MSSBOMItem> wMSSBOMItemList = MSSMaterialDAO.getInstance().MSS_QueryBOMItemList(wLoginUser, -1,
//
//					wRouteID, wLineID, 0, wCustomerID, wProductID, -1, -1, -1, -1, true, wErrorCode);

			List<APSBOMItem> wResultList = new ArrayList<APSBOMItem>();

			APSBOMItem wBOMItem = null;

			for (MSSBOMItem wMSSBOMItem : wMSSBOMItemList) {
				if (wMSSBOMItem.Active != 1)
					continue;

				if (wMSSBOMItem.ReplaceType == 1 || wMSSBOMItem.OutsourceType == 1) {
					wBOMItem = new APSBOMItem(wMSSBOMItem, wLineID, wProductID, wCustomerID, wOrderID, wWBSNo, wPartNo);

					wBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
					wBOMItem.SourceID = wMSSBOMItem.ID;

					wResultList.add(wBOMItem);
				}
			}

			APSBOMItemDAO.getInstance().APS_CreateBOMItem(wLoginUser, wResultList, wErrorCode);

			wResult.Result = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, wOrderID, wWBSNo, wPartNo,
					wLineID, wProductID, wCustomerID, 0, 0, 0, "", 0, 0, 0, null, -1, -1, -1, wErrorCode);

			wResult.FaultCode += MESException.getEnumType(wErrorCode.Result).getLable();
		} catch (Exception e) {
			logger.error(e.toString());
		}

		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_DeleteBOMItem(BMSEmployee wLoginUser, APSBOMItem wBOMItem) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 根据订单ID，工位、工序、物料ID 查询物料
			APSBOMItem wItem = APSBOMItemDAO.getInstance().APS_SelectItem(wLoginUser, wBOMItem.OrderID, wBOMItem.PartID,
					wBOMItem.PartPointID, wBOMItem.MaterialID, wBOMItem.ReplaceType, wBOMItem.OutsourceType,
					wErrorCode);

			if (wItem != null && wItem.ID > 0) {
				// 标准bom删除
				wItem.DeleteID = "X";
				wItem.QTType = wBOMItem.QTType;
				wResult.Result = APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wItem, wErrorCode);

				// 推送给SAP(更新)
				if (wBOMItem.OrderID > 0) {

					ExecutorService wES = Executors.newFixedThreadPool(1);
					wES.submit(() -> APSServiceImpl.getInstance().APS_UpdateBomItemByBOMItem(BaseDAO.SysAdmin,
							wResult.Result));
					wES.shutdown();

//					APSServiceImpl.getInstance().APS_UpdateBomItemByBOMItem(BaseDAO.SysAdmin, wBOMItemID);
//					wDeleteList.add(wBOMItemID);

//					APSConstans.mUBomOrderList.add(wResult.Result);
				}
			}

			wResult.FaultCode += MESException.getEnumType(wErrorCode.Result).getLable();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_CreateBomItemByOrder(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

			int wBOMID = APSBOMItemDAO.getInstance().GetCurrentStandardBOMID(wLoginUser, wOrder.ProductID,
					wOrder.LineID, wOrder.CustomerID, wErrorCode);

			if (wOrder.RouteID <= 0) {
				wBOMID = 0;
			}

			List<MSSBOMItem> wMSSBOMItemList = null;
			if (wBOMID > 0) {
				wMSSBOMItemList = WMSServiceImpl.getInstance()
						.MSS_QueryBOMItemAll(wLoginUser, wBOMID, -1, -1, -1, -1, -1, -1, -1, -1).List(MSSBOMItem.class);
			}

			if (wMSSBOMItemList == null || wMSSBOMItemList.size() <= 0) {

				// 返回提示信息
				wResult.FaultCode = StringUtils.Format("提示：【{0}】该车辆无当前标准BOM，创建台车BOM失败!", wOrder.PartNo);

				// 发送创建台车bom的消息
				SendAPSBOMMessage(wLoginUser, wResult.FaultCode, wOrder);

				return wResult;
			}

			List<APSBOMItem> wResultList = new ArrayList<APSBOMItem>();

			APSBOMItem wBOMItem = null;

			for (MSSBOMItem wMSSBOMItem : wMSSBOMItemList) {

				if (wMSSBOMItem.Active == 2)
					continue;

//				if (wMSSBOMItem.Active != 1)
//					continue;

//				if (wMSSBOMItem.ReplaceType == 1 || (wMSSBOMItem.ReplaceType != 2 && wMSSBOMItem.OutsourceType == 1)) {
				if (wMSSBOMItem.ReplaceType == 1 || wMSSBOMItem.OutsourceType == 1) {

					wBOMItem = new APSBOMItem(wMSSBOMItem, wOrder.LineID, wOrder.ProductID, wOrder.CustomerID,
							wOrder.ID, wOrder.OrderNo, wOrder.PartNo);

					wBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
					wBOMItem.SourceID = wMSSBOMItem.ID;
					wBOMItem.AuditorID = wLoginUser.ID;
					wBOMItem.EditorID = wLoginUser.ID;
					wBOMItem.AuditTime = Calendar.getInstance();
					wBOMItem.EditTime = Calendar.getInstance();

					wResultList.add(wBOMItem);
				}
			}

			// 验证台车BOM
			String wMsg = CheckAPSBOM(wLoginUser, wResultList, wOrder);
			if (StringUtils.isNotEmpty(wMsg)) {

				// 发送创建台车bom的消息
				SendAPSBOMMessage(wLoginUser, wMsg, wOrder);

				// 返回提示信息
				wResult.FaultCode = wMsg;
				return wResult;
			}

			// 重新编号
			int wFlag = 1;
			for (APSBOMItem wAPSBOMItem : wResultList) {
				wAPSBOMItem.OrderNum = wFlag++;
			}

			// 评估类型赋值
			wResultList.forEach(p -> {
				if (p.OutsourceType == 1) {
					p.AssessmentType = "修复旧件";
				}
			});

			APSBOMItemDAO.getInstance().APS_CreateBOMItem(wLoginUser, wResultList, wErrorCode);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, wOrderID, "", "", -1,
					-1, -1, -1, -1, -1, "", -1, -1, -1, null, -1, -1, -1, wErrorCode);

			String wSendSAP = Configuration.readConfigString("isSendSAP", "config/config");
			if (wSendSAP.equals("1")) {
				// ①推送台车BOM给SAP
				String wJson = ChangeToJson(wLoginUser, wList, "I");
				if (StringUtils.isNotEmpty(wJson)) {
					SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 1,
							wList.stream().map(p -> p.ID).collect(Collectors.toList()));
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 发送消息通知(创建台车bom成功与否)
	 */
	private void SendAPSBOMMessage(BMSEmployee wLoginUser, String wMsg, OMSOrder wOrder) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			int wStatus = 0;
			if (wMsg.contains("失败") || wMsg.contains("E")) {
				wStatus = 2;
			} else if (wMsg.contains("成功")) {
				wStatus = 1;

				List<APSBOMLog> wHisList = APSBOMLogDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1, wOrder.ID,
						2, null, null, wErrorCode);
				for (APSBOMLog wAPSBOMLog : wHisList) {
					wAPSBOMLog.Status = 3;
					APSBOMLogDAO.getInstance().Update(wLoginUser, wAPSBOMLog, wErrorCode);
				}
			}

			APSBOMLog wLog = new APSBOMLog(0, wOrder.ProductID, wOrder.ProductNo, wOrder.LineID, wOrder.LineName,
					wOrder.CustomerID, wOrder.Customer, wOrder.ID, wOrder.PartNo, wOrder.OrderNo, wStatus, wMsg,
					Calendar.getInstance());
			int wMessageID = APSBOMLogDAO.getInstance().Update(wLoginUser, wLog, wErrorCode);

			// 此处推送消息，告知为何创建台车bom失败
			List<BFCMessage> wBFCMessageList = new ArrayList<>();
			BFCMessage wMessage = null;
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			String wBomUser = Configuration.readConfigString("bomUser", "config/config");
			String[] wUserList = wBomUser.split(",");
			for (String wUserIDStr : wUserList) {
				int wUserID = StringUtils.parseInt(wUserIDStr);

				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = 0;
				wMessage.Title = StringUtils.Format("{0} {1}", BPMEventModule.APSBOMCreateNotify.getLable(), wShiftID);

				if (wStatus == 1) {
					wMessage.MessageText = StringUtils.Format("【{0}】{1}", wOrder.PartNo, wMsg);
				} else if (wStatus == 2) {
					wMessage.MessageText = StringUtils.Format("【{0}】创建台车BOM失败，请查看失败详情，并及时处理。", wOrder.PartNo);
				} else {
					wMessage.MessageText = StringUtils.Format("【{0}】{1}", wOrder.PartNo, wMsg);
				}

				wMessage.ModuleID = BPMEventModule.APSBOMCreateNotify.getValue();
				wMessage.MessageID = wMessageID;
				wMessage.ResponsorID = wUserID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Notify.getValue();
				wBFCMessageList.add(wMessage);
			}

			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<Integer> APS_UpdateBomItemByBOMItem(BMSEmployee wLoginUser, int wBomItemID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			APSBOMItem wItem = APSBOMItemDAO.getInstance().APS_QueryBOMItemByID(wLoginUser, wBomItemID, wErrorCode);

			if (wItem == null || wItem.ID <= 0) {
				return wResult;
			}

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wItem.OrderID, wErrorCode);

			List<APSBOMItem> wList = new ArrayList<APSBOMItem>(Arrays.asList(wItem));

			// 验证台车BOM
			String wMsg = CheckAPSBOM(wLoginUser, wList, wOrder);

			if (StringUtils.isNotEmpty(wMsg)) {

				List<BFCMessage> wBFCMessageList = new ArrayList<>();
				BFCMessage wMessage = null;
				int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(),
						APSShiftPeriod.Day, FMCShiftLevel.Day, 0);
				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = 0;
				wMessage.Title = StringUtils.Format("【台车BOM】提示", BPMEventModule.StationTip.getLable());
				wMessage.MessageText = wMsg;
				wMessage.ModuleID = BPMEventModule.StationTip.getValue();
				wMessage.ResponsorID = StringUtils.parseInt(Configuration.readConfigString("bomUser", "config/config"));
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Notify.getValue();
				wBFCMessageList.add(wMessage);

				CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);

				// 返回提示信息
				wResult.FaultCode = wMsg;
				return wResult;
			}

			String wSendSAP = Configuration.readConfigString("isSendSAP", "config/config");

			if (wSendSAP.equals("1")) {
				// ①推送台车BOM给SAP
				String wJson = ChangeToJson(wLoginUser, wList, "U");
				List<Integer> wAPSBOMIDList = wList.stream().map(p -> p.ID).distinct().collect(Collectors.toList());
				if (StringUtils.isNotEmpty(wJson)) {
					SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 0, wAPSBOMIDList);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 将台车BOM发送给SAP
	 */
	private String SendToSAP(BMSEmployee wLoginUser, String wJson, String wWBSNo, int wSFCBOMTaskID, OMSOrder wOrder,
			int wMode, List<Integer> wAPSBOMIDList) {
		String wResult = "";
		try {
			String wEndPoint = APSUtils.getInstance().GetSAPEndpoint();
			org.apache.axis.client.Service wService = new org.apache.axis.client.Service();
			Call wCall = (Call) wService.createCall();

			wCall.setUsername(APSUtils.getInstance().GetSAPUser());
			wCall.setPassword(APSUtils.getInstance().GetSAPPassword());

			wCall.setTargetEndpointAddress(wEndPoint);
			wCall.setOperationName(new QName("urn:sap-com:document:sap:rfc:functions", "ZIF_MES_INPUT_001"));// 调用的方法名
			wCall.addParameter("INPUT1", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.addParameter("INPUT2", XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			wCall.setReturnType(XMLType.XSD_STRING); // 返回值类型：String

			String wReturn = (String) wCall.invoke(new Object[] { "01", wJson });// 远程调用
			logger.info(wWBSNo + "::" + wReturn);

			CalendarDAO.getInstance().WriteLogFile(wJson + "::" + wReturn, "台车bom", wOrder);

			wResult = wReturn;

			if (wSFCBOMTaskID > 0) {
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> UpdateSFCBOMTaskSAPStatus(wLoginUser, wReturn, wSFCBOMTaskID));
				wES.shutdown();
			}

			if (wMode == 1) {
				// 发送创建台车bom的消息
				SendAPSBOMMessage(wLoginUser, wResult, wOrder);
			}

			if (wAPSBOMIDList != null && wAPSBOMIDList.size() > 0) {
				String wMsg = wResult;
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> UpdateAPSBOMMsg(wLoginUser, wAPSBOMIDList, wMsg));
				wES.shutdown();
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 更新台车BOM推送状态
	 */
	private void UpdateAPSBOMMsg(BMSEmployee wLoginUser, List<Integer> wAPSBOMIDList, String wResult) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wResult.contains("E")) {
				for (int wID : wAPSBOMIDList) {
					APSBOMItem wItem = APSBOMItemDAO.getInstance().APS_QueryBOMItemByID(wLoginUser, wID, wErrorCode);
					if (wItem == null || wItem.ID <= 0) {
						continue;
					}
					wItem.SAPStatus = 2;
					wItem.SAPMsg = wResult;
					APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wItem, wErrorCode);
				}
			} else if (wResult.contains("成功")) {
				for (int wID : wAPSBOMIDList) {
					APSBOMItem wItem = APSBOMItemDAO.getInstance().APS_QueryBOMItemByID(wLoginUser, wID, wErrorCode);
					if (wItem == null || wItem.ID <= 0) {
						continue;
					}
					wItem.SAPStatus = 1;
					wItem.SAPMsg = "";
					APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wItem, wErrorCode);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 将台车bom转换为SAP需要的json字符串
	 */
	private String ChangeToJson(BMSEmployee wLoginUser, List<APSBOMItem> wList, String wFlag) {
		String wResult = "";
		try {
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			APSBomData wAPSBomData = new APSBomData();
			wAPSBomData.INPUT1 = "01";

			INPUT2 wINPUT2 = new INPUT2();
			wINPUT2.MODE = wFlag;
			wINPUT2.HEAD = GetHead(wLoginUser, wList.get(0));
			wINPUT2.ITEM = GetITEM(wLoginUser, wList);
			wAPSBomData.INPUT2 = wINPUT2;

			wResult = JSON.toJSONString(wAPSBomData.INPUT2);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取明细
	 */
	private List<ITEM> GetITEM(BMSEmployee wLoginUser, List<APSBOMItem> wList) {
		List<ITEM> wResult = new ArrayList<ITEM>();
		try {
			SimpleDateFormat wDateFormat = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat wTimeFormat = new SimpleDateFormat("HHmmss");

			for (APSBOMItem wAPSBOMItem : wList) {
				ITEM wItem = new ITEM();

				wItem.AEDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
				wItem.AENAM = "RFC_USER";
				wItem.AETIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
				wItem.BWTAR = wAPSBOMItem.AssessmentType;
				wItem.ERDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
				wItem.ERNAM = "RFC_USER";
				wItem.ERTIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
				wItem.GRPNR = "";
				wItem.ID = wAPSBOMItem.OrderID;
				wItem.ITEMID = wAPSBOMItem.OrderNum;
				wItem.KTEXT = wAPSBOMItem.TextCode;
				wItem.KTTXT = StringUtils.Format("{0}  {1}", APSConstans.GetFPCPart(wAPSBOMItem.PartID).Code,
						wAPSBOMItem.PartPointName);
				wItem.MATNR = wAPSBOMItem.MaterialNo;
				wItem.MEINS = wAPSBOMItem.UnitText;
				wItem.MENGE = String.valueOf(wAPSBOMItem.Number);
				wItem.RGEKZ = "";
				wItem.USR00 = APSConstans.GetFPCPart(wAPSBOMItem.PartID).Code;
				wItem.XFJJ = wAPSBOMItem.RepairCoreIdentification;
				wItem.ZBHOH = wAPSBOMItem.ReplaceType <= 0 ? "" : String.valueOf(wAPSBOMItem.ReplaceType);
				wItem.ZCJXC = wAPSBOMItem.DisassyType > 0 ? "X" : "";
				wItem.ZDELE = wAPSBOMItem.DeleteID;
				wItem.ZDRHH = wAPSBOMItem.DingrongGroup;
				wItem.ZFLAGFJ = wAPSBOMItem.AccessoryLogo;
				wItem.ZFLAGJXJ = wAPSBOMItem.RepairPartClass;
				wItem.ZFLAGKGL = wAPSBOMItem.CustomerMaterial > 0 ? "X" : "";
				wItem.ZLGORT = String.valueOf(wAPSBOMItem.StockID);
				wItem.ZLLSL = String.valueOf(wAPSBOMItem.PickingQuantity);
				wItem.ZREMARK = wAPSBOMItem.Remark;
				wItem.ZSCXC = wAPSBOMItem.OverLine > 0 ? "X" : "";
				wItem.ZSFHH = wAPSBOMItem.PartChange > 0 ? "X" : "";
				wItem.ZWTDW = wAPSBOMItem.DisassyType > 0 ? "X" : "";
				wItem.ZYCYZ = wAPSBOMItem.OriginalType > 0 ? "X" : "";
				wItem.ZZDHL = String.valueOf(wAPSBOMItem.EvenExchangeRate);
				wItem.ZZFC = wAPSBOMItem.ReceiveDepart;
				wItem.ZZLDL = wAPSBOMItem.QTType <= 0 ? ""
						: wAPSBOMItem.QTType == 1 ? "001"
								: wAPSBOMItem.QTType == 2 ? "002"
										: wAPSBOMItem.QTType == 3 ? "003" : wAPSBOMItem.QTType == 4 ? "004" : "";
				wItem.ZZLXL = wAPSBOMItem.QTItemType <= 0 ? ""
						: wAPSBOMItem.QTItemType == 1 ? "01"
								: wAPSBOMItem.QTItemType == 2 ? "02"
										: wAPSBOMItem.QTItemType == 3 ? "03"
												: wAPSBOMItem.QTItemType == 4 ? "04"
														: wAPSBOMItem.QTItemType == 5 ? "05" : "";
				wItem.ZZZBZ = wAPSBOMItem.Remark;
				wItem.ZZZWW = wAPSBOMItem.OutsourceType <= 0 ? "" : String.valueOf(wAPSBOMItem.OutsourceType);

				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

//	/**
//	 * 获取明细
//	 */
//	private List<ITEM> GetITEM(BMSEmployee wLoginUser, List<APSBOMItem> wList) {
//		List<ITEM> wResult = new ArrayList<ITEM>();
//		try {
//			SimpleDateFormat wDateFormat = new SimpleDateFormat("yyyyMMdd");
//			SimpleDateFormat wTimeFormat = new SimpleDateFormat("HHmmss");
//
//			for (APSBOMItem wAPSBOMItem : wList) {
//				ITEM wItem = new ITEM();
//
//				wItem.AEDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
//				wItem.AENAM = wLoginUser.LoginID;
//				wItem.AETIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
//				wItem.BWTAR = wAPSBOMItem.AssessmentType;
//				wItem.ERDAT = wDateFormat.format(wAPSBOMItem.AuditTime.getTime());
//				wItem.ERNAM = wLoginUser.LoginID;
//				wItem.ERTIM = wTimeFormat.format(wAPSBOMItem.AuditTime.getTime());
//				wItem.GRPNR = wAPSBOMItem.WBSNo;
//				wItem.ID = wAPSBOMItem.OrderID;
//				wItem.ITEMID = wAPSBOMItem.OrderNum;
//				wItem.KTEXT = wAPSBOMItem.TextCode;
//				wItem.KTTXT = StringUtils.Format("{0} {1}", APSConstans.GetFPCPartPoint(wAPSBOMItem.PartPointID).Code,
//						wAPSBOMItem.PartPointName);
//				wItem.MATNR = wAPSBOMItem.MaterialNo;
//				wItem.MEINS = wAPSBOMItem.UnitText;
//				wItem.MENGE = String.valueOf(wAPSBOMItem.Number);
//				wItem.RGEKZ = "";
//				wItem.USR00 = APSConstans.GetFPCPart(wAPSBOMItem.PartID).Code;
//				wItem.XFJJ = wAPSBOMItem.RepairCoreIdentification;
//				wItem.ZBHOH = String.valueOf(wAPSBOMItem.ReplaceType);
//				wItem.ZCJXC = String.valueOf(wAPSBOMItem.DisassyType);
//				wItem.ZDELE = "";
//				wItem.ZDRHH = wAPSBOMItem.DingrongGroup;
//				wItem.ZFLAGFJ = wAPSBOMItem.AccessoryLogo;
//				wItem.ZFLAGJXJ = wAPSBOMItem.RepairPartClass;
//				wItem.ZFLAGKGL = String.valueOf(wAPSBOMItem.CustomerMaterial);
//				wItem.ZLGORT = String.valueOf(wAPSBOMItem.StockID);
//				wItem.ZLLSL = String.valueOf(wAPSBOMItem.PickingQuantity);
//				wItem.ZREMARK = wAPSBOMItem.Remark;
//				wItem.ZSCXC = String.valueOf(wAPSBOMItem.OverLine);
//				wItem.ZSFHH = String.valueOf(wAPSBOMItem.PartChange);
//				wItem.ZWTDW = String.valueOf(wAPSBOMItem.DisassyType);
//				wItem.ZYCYZ = String.valueOf(wAPSBOMItem.OriginalType);
//				wItem.ZZDHL = String.valueOf(wAPSBOMItem.EvenExchangeRate);
//				wItem.ZZFC = wAPSBOMItem.ReceiveDepart;
//				wItem.ZZLDL = String.valueOf(wAPSBOMItem.QTType);
//				wItem.ZZLXL = String.valueOf(wAPSBOMItem.QTItemType);
//				wItem.ZZZBZ = wAPSBOMItem.Remark;
//				wItem.ZZZWW = String.valueOf(wAPSBOMItem.OutsourceType);
//
//				wResult.add(wItem);
//			}
//		} catch (Exception ex) {
//			logger.error(ex.toString());
//		}
//		return wResult;
//	}

	/**
	 * 获取表头
	 */
	private HEAD GetHead(BMSEmployee wLoginUser, APSBOMItem wAPSBOMItem) {
		HEAD wResult = new HEAD();
		try {
			SimpleDateFormat wDateFormat = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat wTimeFormat = new SimpleDateFormat("HHmmss");

			wResult.AEDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
			wResult.AENAM = "RFC_USER";
			wResult.AETIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
			wResult.ARBPL = wAPSBOMItem.WorkCenter;
			wResult.ERDAT = wDateFormat.format(wAPSBOMItem.EditTime.getTime());
			wResult.ERNAM = "RFC_USER";
			wResult.ERTIM = wTimeFormat.format(wAPSBOMItem.EditTime.getTime());
			wResult.ID = wAPSBOMItem.OrderID;
			wResult.KTEXT = wAPSBOMItem.TextCode;
			wResult.MAT_PSPNR = wAPSBOMItem.WBSNo;
			wResult.WERKS = String.valueOf(wAPSBOMItem.FactoryID);
			wResult.ZCHEX = wAPSBOMItem.ProductNo;
			wResult.ZDELE = "";
			wResult.ZJDXX = wAPSBOMItem.CustomerCode;
			wResult.ZSCLX = String.valueOf(wAPSBOMItem.BOMType);
			wResult.ZXIUC = wAPSBOMItem.LineName.replace("C", "");
			wResult.ZZFC = wAPSBOMItem.ReceiveDepart;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 检查台车BOM
	 */
	private String CheckAPSBOM(BMSEmployee wLoginUser, List<APSBOMItem> wMSSBOMItemList, OMSOrder wOrder) {
		String wResult = "";
		try {
			// ①BOM类型必须是在生产类型表(ZPPSCLX-ZSCLX中维护过的值)，目前只有3个值：1新造 2检修 9计划外;
			if (wMSSBOMItemList.stream().anyMatch(p -> p.BOMType != 1 && p.BOMType != 2 && p.BOMType != 9)) {
				wResult = StringUtils.Format(
						"提示：【{0}】创建台车BOM失败。BOM类型必须是在生产类型表(ZPPSCLX-ZSCLX中维护过的值)，目前只有3个值：1新造 2检修 9计划外;", wOrder.PartNo);
				return wResult;
			}
			// ②车型数据必须是在车型配置表(TCJ1T-PRATX)中维护过的值，例：HXD1C;
			if (wMSSBOMItemList.stream().anyMatch(p -> p.ProductID <= 0)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。车型数据必须是在车型配置表(TCJ1T-PRATX)中维护过的值，例：HXD1C",
						wOrder.PartNo);
				return wResult;
			}
			// ③修程：当BOM类型为2时，修程只允许5/6，当BOM类型为1时修程只允许为空白；当BOM类型为9时，允许5/6、空白。
			if (wMSSBOMItemList.stream().anyMatch(
					p -> (p.BOMType == 2 && p.LineID != 1 && p.LineID != 2) || (p.BOMType == 1 && p.LineID != 0)
							|| (p.BOMType == 9 && p.LineID != 1 && p.LineID != 2 && p.LineID != 0))) {
				wResult = StringUtils.Format(
						"提示：【{0}】创建台车BOM失败。当BOM类型为2时，修程只允许5/6，当BOM类型为1时修程只允许为空白；当BOM类型为9时，允许5/6、空白。", wOrder.PartNo);
				return wResult;
			}
			// ④局段信息：必须是在局段表(ZPMJD-ZJDXX)中维护过的值，例：NN(南宁)；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.CustomerID <= 0)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。局段信息：必须是在局段表(ZPMJD-ZJDXX)中维护过的值，例：NN(南宁)；",
						wOrder.PartNo);
				return wResult;
			}
			// ⑤工厂：检查工厂表(T001W-WERKS),目前只有1900；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.FactoryID != 1900)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。工厂：检查工厂表(T001W-WERKS),目前只有1900；", wOrder.PartNo);
				return wResult;
			}
			// ⑥WBS元素：第9位必须是P，必须已经在项目中维护过的WBS(PRPS-PSPNR);
			if (wMSSBOMItemList.stream().anyMatch(p -> p.WBSNo.length() < 9 || p.WBSNo.charAt(8) != 'P')) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。WBS元素：第9位必须是P，必须已经在项目中维护过的WBS(PRPS-PSPNR);",
						wOrder.PartNo);
				return wResult;
			}
			// ⑦工位：必须在工位表清单中(ZPPTW-USR00)已经维护；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.PartID <= 0)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。工位：必须在工位表清单中(ZPPTW-USR00)已经维护；", wOrder.PartNo);
				return wResult;
			}
			// ⑧工序：工序描述必须是在ZPM1011工序清单中维护；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.PartPointID <= 0)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。工序：工序描述必须是在ZPM1011工序清单中维护；", wOrder.PartNo);
				return wResult;
			}
			// ⑨物料编码：和工厂一起检查表(MARA-MEINS)
			if (wMSSBOMItemList.stream().anyMatch(p -> p.MaterialID <= 0)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。物料编码：和工厂一起检查表(MARA-MEINS)。", wOrder.PartNo);
				return wResult;
			}
			// ⑩物料名称：不检查；
			// ①单位：检查与物料的基本单位一致(MARA-MEINS)；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.UnitID <= 0)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。单位：检查与物料的基本单位一致(MARA-MEINS)；", wOrder.PartNo);
				return wResult;
			}
			// ②评估类型：只允许的值：修复旧件、可用旧件、常规新件、高价互换件；
			if (wMSSBOMItemList.stream()
					.anyMatch(p -> !p.AssessmentType.equals("修复旧件") && !p.AssessmentType.equals("可用旧件")
							&& !p.AssessmentType.equals("常规新件") && !p.AssessmentType.equals("高价互换件"))) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。评估类型：只允许的值：修复旧件、可用旧件、常规新件、高价互换件", wOrder.PartNo);
				return wResult;
			}
			// ③是否互换件：只允许X或空白；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.PartChange != 0 && p.PartChange != 1)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。是否互换件：只允许X或空白；", wOrder.PartNo);
				return wResult;
			}
			// ④是否超修程：只允许X或空白；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.OverLine != 0 && p.OverLine != 1)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。是否超修程：只允许X或空白；", wOrder.PartNo);
				return wResult;
			}
			// ⑤领料部门：只允许0001；
			if (wMSSBOMItemList.stream().anyMatch(p -> !p.ReceiveDepart.equals("0001"))) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。领料部门：只允许0001；", wOrder.PartNo);
				return wResult;
			}
			// ⑥仓库号：BOM类型为1时固定值1100，BOM类型为2时固定值1200，BOM类型为9时，允许1100和1200；
			if (wMSSBOMItemList.stream()
					.anyMatch(p -> (p.BOMType == 1 && p.StockID != 1100) || (p.BOMType == 2 && p.StockID != 1200)
							|| (p.BOMType == 9 && p.StockID != 1100 && p.StockID != 1200))) {
				wResult = StringUtils.Format(
						"提示：【{0}】创建台车BOM失败。仓库号：BOM类型为1时固定值1100，BOM类型为2时固定值1200，BOM类型为9时，允许1100和1200；", wOrder.PartNo);
				return wResult;
			}
			// ⑦客供料与原拆原装要求，两个字段不能同时有值；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.CustomerMaterial > 0 && p.OriginalType > 0)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。客供料与原拆原装要求，两个字段不能同时有值；", wOrder.PartNo);
				return wResult;
			}
			// ⑧台车BOM中的组件用量不能为0，总数量不能大于标准BOM的用量；
			if (wMSSBOMItemList.stream().anyMatch(p -> p.Number <= 0)) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。台车BOM中的组件用量不能为0，总数量不能大于标准BOM的用量；", wOrder.PartNo);
				return wResult;
			}
			// ⑨台车BOM中的行项目必须在标准BOM中能够找到；
			// ⑩已完工车辆不允许更改台车BOM。台车WBS标识完工时，对应的台车BOM保存时报错“已完工车辆不允许更改WBS_BOM”.
			if (wOrder.Status == 8) {
				wResult = StringUtils.Format("提示：【{0}】创建台车BOM失败。已完工车辆不允许更改WBS_BOM", wOrder.PartNo);
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<OMSOrder>> APS_QueryOrderBOMList(BMSEmployee wLoginUser, List<Integer> wCustomerList,
			List<Integer> wLineList, List<Integer> wStatusList, List<Integer> wProductList, String wPartNo,
			List<Integer> wActiveList, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<OMSOrder>> wResult = new ServiceResult<List<OMSOrder>>();
		wResult.Result = new ArrayList<OMSOrder>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			boolean wIsAll = false;
			if (wStatusList.stream().anyMatch(p -> p == -1)) {
				wIsAll = true;
			}

			if (wCustomerList != null && wCustomerList.size() > 0) {
				wCustomerList.removeIf(p -> p == -1);
			}
			if (wLineList != null && wLineList.size() > 0) {
				wLineList.removeIf(p -> p == -1);
			}
			if (wStatusList != null && wStatusList.size() > 0) {
				wStatusList.removeIf(p -> p == -1);
			}
			if (wProductList != null && wProductList.size() > 0) {
				wProductList.removeIf(p -> p == -1);
			}
			if (wActiveList != null && wActiveList.size() > 0) {
				wActiveList.removeIf(p -> p == -1);
			}

			List<OMSOrder> wList = OMSOrderDAO.getInstance().SelectList(wLoginUser, wCustomerList, wLineList,
					wStartTime, wEndTime, wStatusList, wProductList, wPartNo, wActiveList, wErrorCode);

			if (wIsAll) {
				wList = wList.stream().filter(p -> p.Status != 1 && p.Status != 2).collect(Collectors.toList());
			}

			if (wList != null && wList.size() > 0) {
				for (OMSOrder wOMSOrder : wList) {
					int wCount = APSBOMItemDAO.getInstance().APS_QueryBOMItemCount(wLoginUser, wOMSOrder.ID,
							wErrorCode);
					if (wCount > 0) {
						wOMSOrder.IsCreateAPSBom = true;
					} else {
						wOMSOrder.IsCreateAPSBom = false;
					}
				}
				wResult.Result = wList;
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized void APS_AutoGenerateConfirmForm(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wOrderIDList = OMSOrderDAO.getInstance()
					.OMS_QueryNotGenerateConfirmFormOrderIDList(wLoginUser, wErrorCode);
			if (wOrderIDList == null || wOrderIDList.size() <= 0) {
				return;
			}

			for (Integer wOrderID : wOrderIDList) {
				SFCOrderForm wSFCOrderForm = new SFCOrderForm();

				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

				wSFCOrderForm.CreateID = wLoginUser.ID;
				wSFCOrderForm.CreateTime = Calendar.getInstance();
				wSFCOrderForm.ID = 0;
				wSFCOrderForm.Type = SFCOrderFormType.CompleteConfirm.getValue();
				wSFCOrderForm.OrderID = wOrderID;
				wSFCOrderForm.OrderNo = wOrder.OrderNo;
				wSFCOrderForm.PartNo = wOrder.PartNo;
				wSFCOrderForm.Status = 1;

				SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public synchronized void APS_AutoFinishTaskPart(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSTaskPart> wList = APSTaskPartDAO.getInstance().APS_QueryFinishedTaskPartList(wLoginUser,
					wErrorCode);
			if (wList == null || wList.size() <= 0) {
				return;
			}

			for (APSTaskPart wAPSTaskPart : wList) {
				wAPSTaskPart.Status = APSTaskStatus.Done.getValue();
				wAPSTaskPart.FinishWorkTime = Calendar.getInstance();

				APSTaskPartDAO.getInstance().Update(wLoginUser, wAPSTaskPart, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public List<AndonTime> ASP_QueryAndonTimeList(BMSEmployee wLoginUser, List<Integer> wOrderIDList) {
		List<AndonTime> wResult = new ArrayList<AndonTime>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult = APSTaskPartDAO.getInstance().APS_QueryAndonTime(wLoginUser, wOrderIDList, wErrorCode);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSBOMItem>> APS_QueryQueryDeleteBomItemList(BMSEmployee wLoginUser, int wOrderID,
			int wPartID, String wMaterialNos) {
		ServiceResult<List<APSBOMItem>> wResult = new ServiceResult<List<APSBOMItem>>();
		wResult.Result = new ArrayList<APSBOMItem>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (StringUtils.isEmpty(wMaterialNos)) {
				return wResult;
			}

			String[] wList = wMaterialNos.split(",");
			for (String wMaterialNo : wList) {
				wResult.Result.addAll(APSBOMItemDAO.getInstance().APS_QueryBOMItemList(wLoginUser, wOrderID, "", "", -1,
						-1, -1, wPartID, -1, -1, wMaterialNo, -1, -1, -1, null, -1, -1, -1, wErrorCode));
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_BOMTaskToSAP(BMSEmployee wLoginUser, int wBOMTaskID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			SFCBOMTask wTask = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wBOMTaskID, "",
					wErrorCode);

			OMSOrder wOMSOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wTask.OrderID, wErrorCode);

			// ①推送物料到SAP
			if (wTask.MaterialID > 0) {
				APSBOMItem wAPSBOMItem = CreateAPSBOMItem(wLoginUser, wTask, wErrorCode, wOMSOrder);
				APSServiceImpl.getInstance().APS_UpdateBOMItem(wLoginUser, wAPSBOMItem);
			}

			// ①推送物料到SAP(批量子表数据)
			if (wTask.SFCBOMTaskItemList.size() > 0) {
				List<SFCBOMTaskItem> wItemList = wTask.SFCBOMTaskItemList.stream().filter(p -> p.Status != 2)
						.collect(Collectors.toList());

				if (wItemList.size() <= 0) {
					return wResult;
				}

				// 禁用之前的台车BOM
				DisableAPSBOM(wLoginUser, wItemList, wTask);

				// 推送给MES和SAP
				SynchronizedToSap(wLoginUser, wTask, wErrorCode, wItemList, wBOMTaskID);
			}
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 打上删除标记，台车BOM
	 */
	private void DisableAPSBOM(BMSEmployee wLoginUser, List<SFCBOMTaskItem> wItemList, SFCBOMTask wTask) {
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			for (SFCBOMTaskItem wSFCBOMTaskItem : wItemList) {
				APSBOMItemDAO.getInstance().DeleteBOMX(wLoginUser, wTask.PartID, wTask.StepID,
						wSFCBOMTaskItem.MaterialID, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	private void SynchronizedToSap(BMSEmployee wLoginUser, SFCBOMTask wTask, OutResult<Integer> wErrorCode,
			List<SFCBOMTaskItem> wItemList, int wSFCBOMTaskID) {
		try {
			OMSOrder wOMSOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wTask.OrderID, wErrorCode);

			List<APSBOMItem> wList = CreateAPSBOMItemList(wLoginUser, wTask, wErrorCode, wOMSOrder, wItemList);

			if (wList.size() > 0) {
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> APSServiceImpl.getInstance().APS_UpdateBOMItemList(wLoginUser, wList, wSFCBOMTaskID));
				wES.shutdown();
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 创建台车bom集合
	 */
	private List<APSBOMItem> CreateAPSBOMItemList(BMSEmployee wLoginUser, SFCBOMTask wTask,
			OutResult<Integer> wErrorCode, OMSOrder wOMSOrder, List<SFCBOMTaskItem> wItemList) {
		List<APSBOMItem> wResult = new ArrayList<APSBOMItem>();
		try {
			if (wItemList == null || wItemList.size() <= 0) {
				return wResult;
			}

			for (SFCBOMTaskItem wSFCBOMTaskItem : wItemList) {
				APSBOMItem wAPSBOMItem = null;

				int wOutsourceType = 0;
				if (wSFCBOMTaskItem.BOMItemID > 0) {
					// 提交到Core偶换件
					MSSBOMItem wItem = WMSServiceImpl.getInstance()
							.MSS_QueryBOMItemByID(wLoginUser, wSFCBOMTaskItem.BOMItemID).Info(MSSBOMItem.class);
					wOutsourceType = wItem.OutsourceType;
				}
				wAPSBOMItem = new APSBOMItem();
				wAPSBOMItem.ID = 0;
				wAPSBOMItem.BOMType = 2;
				wAPSBOMItem.FactoryID = 1900;
				wAPSBOMItem.WBSNo = wOMSOrder.OrderNo;
				wAPSBOMItem.OrderID = wOMSOrder.ID;
				wAPSBOMItem.PartNo = wOMSOrder.PartNo;
				wAPSBOMItem.LineID = wOMSOrder.LineID;
				wAPSBOMItem.ProductID = wOMSOrder.ProductID;
				wAPSBOMItem.CustomerID = wOMSOrder.CustomerID;
				wAPSBOMItem.PartID = wTask.PartID;
				wAPSBOMItem.PartPointID = wTask.PartPointID;
				wAPSBOMItem.MaterialID = wSFCBOMTaskItem.MaterialID;
				wAPSBOMItem.MaterialNo = wSFCBOMTaskItem.MaterialNo;
				wAPSBOMItem.Number = wSFCBOMTaskItem.MaterialNumber;
				wAPSBOMItem.UnitID = APSBOMItemDAO.getInstance().GetUnitID(wLoginUser, wSFCBOMTaskItem.MaterialID,
						wErrorCode);
				wAPSBOMItem.ReplaceType = 2;
				wAPSBOMItem.OutsourceType = wOutsourceType;
				// 若是委外偶修件，需设置为互换件
				if (wOutsourceType == 2) {
					wAPSBOMItem.PartChange = 1;
				}
				wAPSBOMItem.StockID = 1200;
				wAPSBOMItem.Status = 1;

				if (wSFCBOMTaskItem.SapType == 1) {
					wAPSBOMItem.AssessmentType = "常规新件";
				} else if (wSFCBOMTaskItem.SapType == 2) {
					wAPSBOMItem.AssessmentType = "修复旧件";
				} else if (wSFCBOMTaskItem.SapType == 3) {
					wAPSBOMItem.AssessmentType = "可用旧件";
				} else if (wSFCBOMTaskItem.SapType == 4) {
					wAPSBOMItem.AssessmentType = "高价互换件";
				}

				wAPSBOMItem.SourceType = APSBOMSourceType.SFCBOMTask.getValue();
				wAPSBOMItem.SourceID = wSFCBOMTaskItem.ID;
				wAPSBOMItem.AuditTime = Calendar.getInstance();
				wAPSBOMItem.EditTime = Calendar.getInstance();

				// ①加入质量损失
				if (StringUtils.isNotEmpty(wSFCBOMTaskItem.QualityLossBig)
						|| StringUtils.isNotEmpty(wSFCBOMTaskItem.QualityLossSmall)) {
					wAPSBOMItem.BOMType = 9;
					wAPSBOMItem.QTType = GetQTType(wSFCBOMTaskItem.QualityLossBig);
					wAPSBOMItem.QTItemType = GetQTItemType(wSFCBOMTaskItem.QualityLossSmall);
				}

				if (wAPSBOMItem != null)
					wResult.add(wAPSBOMItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取质量损失小类
	 */
	private int GetQTItemType(String qualityLossSmall) {
		int wResult = 0;
		try {
			switch (qualityLossSmall) {
			case "01":
				wResult = 1;
				break;
			case "02":
				wResult = 2;
				break;
			case "03":
				wResult = 3;
				break;
			case "04":
				wResult = 4;
				break;
			case "05":
				wResult = 5;
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取质量损失大类
	 */
	private int GetQTType(String qualityLossBig) {
		int wResult = 0;
		try {
			switch (qualityLossBig) {
			case "001":
				wResult = 1;
				break;
			case "002":
				wResult = 2;
				break;
			case "003":
				wResult = 3;
				break;
			case "004":
				wResult = 4;
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private APSBOMItem CreateAPSBOMItem(BMSEmployee wLoginUser, SFCBOMTask wTask, OutResult<Integer> wErrorCode,
			OMSOrder wOMSOrder) {
		APSBOMItem wAPSBOMItem = null;
		try {
			if (wTask.BOMItemID > 0) {
				// 提交到Core偶换件
				MSSBOMItem wItem = WMSServiceImpl.getInstance().MSS_QueryBOMItemByID(wLoginUser, wTask.BOMItemID)
						.Info(MSSBOMItem.class);
				wItem.MaterialNumber = wTask.MaterialNumber;
				wAPSBOMItem = new APSBOMItem(wItem, wOMSOrder.LineID, wOMSOrder.ProductID, wOMSOrder.BureauSectionID,
						wOMSOrder.ID, wOMSOrder.OrderNo, wOMSOrder.PartNo);
			} else {
				wAPSBOMItem = new APSBOMItem();
				wAPSBOMItem.ID = 0;
				wAPSBOMItem.BOMType = 2;
				wAPSBOMItem.FactoryID = 1900;
				wAPSBOMItem.WBSNo = wOMSOrder.OrderNo;
				wAPSBOMItem.OrderID = wOMSOrder.ID;
				wAPSBOMItem.PartNo = wOMSOrder.PartNo;
				wAPSBOMItem.LineID = wOMSOrder.LineID;
				wAPSBOMItem.ProductID = wOMSOrder.ProductID;
				wAPSBOMItem.CustomerID = wOMSOrder.CustomerID;
				wAPSBOMItem.PartID = wTask.PartID;
				wAPSBOMItem.PartPointID = wTask.PartPointID;
				wAPSBOMItem.MaterialID = wTask.MaterialID;
				wAPSBOMItem.MaterialNo = wTask.MaterialNo;
				wAPSBOMItem.Number = wTask.MaterialNumber;
				wAPSBOMItem.UnitID = APSBOMItemDAO.getInstance().GetUnitID(wLoginUser, wTask.MaterialID, wErrorCode);
				wAPSBOMItem.ReplaceType = 2;
				wAPSBOMItem.StockID = 1200;
				wAPSBOMItem.Status = 1;
			}

			if (wTask.SapType == 1) {
				wAPSBOMItem.AssessmentType = "常规新件";
			} else if (wTask.SapType == 2) {
				wAPSBOMItem.AssessmentType = "修复旧件";
			}
			wAPSBOMItem.SourceType = APSBOMSourceType.SFCBOMTask.getValue();
			wAPSBOMItem.SourceID = wTask.ID;
			wAPSBOMItem.AuditTime = Calendar.getInstance();
			wAPSBOMItem.EditTime = Calendar.getInstance();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wAPSBOMItem;
	}

	@Override
	public ServiceResult<List<APSDayPlanAuditBPM>> APS_QueryDayPlanAuditBPMEmployeeAllWeb(BMSEmployee wLoginUser,
			Calendar wStartTime, Calendar wEndTime, int wStatus, int wAreaID) {
		ServiceResult<List<APSDayPlanAuditBPM>> wResult = new ServiceResult<List<APSDayPlanAuditBPM>>();
		wResult.Result = new ArrayList<APSDayPlanAuditBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (wStatus) {
			case 1:
				wResult.Result.addAll(APSDayPlanAuditBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, "", -1, -1,
						new ArrayList<Integer>(Arrays.asList(20, 21, 22)), wStartTime, wEndTime, wErrorCode));
				break;
			case 0:
				wResult.Result.addAll(APSDayPlanAuditBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, "", -1, -1,
						new ArrayList<Integer>(Arrays.asList(1, 2)), wStartTime, wEndTime, wErrorCode));
				break;
			default:
				wResult.Result.addAll(APSDayPlanAuditBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, "", -1, -1,
						new ArrayList<Integer>(Arrays.asList(1, 2, 3, 20, 21, 22)), wStartTime, wEndTime, wErrorCode));
				break;
			}
			wResult.Result.sort((o1, o2) -> o2.SubmitTime.compareTo(o1.SubmitTime));

			if (wAreaID > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.AreaID == wAreaID).collect(Collectors.toList());
			}

			if (wResult.Result.size() <= 0 || wStatus == 1) {
				return wResult;
			}

			// 待办数据处理
			List<BPMTaskBase> wBaseList = APSDayPlanAuditBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser,
					wLoginUser.getID(), wErrorCode);
			if (wBaseList == null || wBaseList.size() <= 0) {
				return wResult;
			}
			for (BPMTaskBase wTaskBase : wBaseList) {
				if (!(wTaskBase instanceof APSDayPlanAuditBPM))
					continue;
				APSDayPlanAuditBPM wIPTStandardBPM = (APSDayPlanAuditBPM) wTaskBase;
				wIPTStandardBPM.TagTypes = TaskQueryType.ToHandle.getValue();
				for (int i = 0; i < wResult.Result.size(); i++) {
					if (wResult.Result.get(i).ID == wIPTStandardBPM.ID)
						wResult.Result.set(i, wIPTStandardBPM);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> APS_SendMoreToSAP(BMSEmployee wLoginUser, List<MSSBOMItem> wDataList) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		wResult.Result = "";
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().GetListByBOMID(wLoginUser, wDataList.get(0).BOMID,
					wErrorCode);

			for (OMSOrder wOrder : wOrderList) {
				List<APSBOMItem> wResultList = new ArrayList<APSBOMItem>();

				APSBOMItem wBOMItem = null;

				for (MSSBOMItem wMSSBOMItem : wDataList) {

					if (wMSSBOMItem.Active != 1)
						continue;

					if (wMSSBOMItem.ReplaceType == 1
							|| (wMSSBOMItem.ReplaceType != 2 && wMSSBOMItem.OutsourceType == 1)) {

						wBOMItem = new APSBOMItem(wMSSBOMItem, wOrder.LineID, wOrder.ProductID, wOrder.BureauSectionID,
								wOrder.ID, wOrder.OrderNo, wOrder.PartNo);

						wBOMItem.ProductNo = wOrder.ProductNo;
						wBOMItem.LineName = wOrder.LineName;
						wBOMItem.CustomerCode = APSConstans.GetCRMCustomer(wOrder.BureauSectionID).CustomerCode;
						wBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
						wBOMItem.SourceID = wMSSBOMItem.ID;
						wBOMItem.AuditorID = wLoginUser.ID;
						wBOMItem.EditorID = wLoginUser.ID;
						wBOMItem.AuditTime = Calendar.getInstance();
						wBOMItem.EditTime = Calendar.getInstance();

						wResultList.add(wBOMItem);
					}
				}

				if (wResultList.size() <= 0) {
					wResult.FaultCode += "提示：无必换件或委外必修件!";
					return wResult;
				}

				// 验证台车BOM
				String wMsg = CheckAPSBOM(wLoginUser, wResultList, wOrder);
				if (StringUtils.isNotEmpty(wMsg)) {

					List<BFCMessage> wBFCMessageList = new ArrayList<>();
					BFCMessage wMessage = null;
					int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(),
							APSShiftPeriod.Day, FMCShiftLevel.Day, 0);
					// 发送任务消息到人员
					wMessage = new BFCMessage();
					wMessage.Active = 0;
					wMessage.CompanyID = 0;
					wMessage.CreateTime = Calendar.getInstance();
					wMessage.EditTime = Calendar.getInstance();
					wMessage.ID = 0;
					wMessage.MessageID = 0;
					wMessage.Title = StringUtils.Format("【台车BOM】提示", BPMEventModule.StationTip.getLable());
					wMessage.MessageText = wMsg;
					wMessage.ModuleID = BPMEventModule.StationTip.getValue();
					wMessage.ResponsorID = StringUtils
							.parseInt(Configuration.readConfigString("bomUser", "config/config"));
					wMessage.ShiftID = wShiftID;
					wMessage.StationID = 0;
					wMessage.Type = BFCMessageType.Notify.getValue();
					wBFCMessageList.add(wMessage);

					CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);

					// 返回提示信息
					wResult.FaultCode = wMsg;
					return wResult;
				}

				// 重新编号保存
				for (APSBOMItem wAPSBOMItem : wResultList) {
					wAPSBOMItem.OrderNum = APSBOMItemDAO.getInstance().APS_QueryOrdrNumber(wLoginUser,
							wAPSBOMItem.OrderID, wErrorCode);
					wAPSBOMItem.ID = APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wAPSBOMItem, wErrorCode);
				}

				// 评估类型赋值
				wResultList.forEach(p -> {
					if (p.OutsourceType == 1) {
						p.AssessmentType = "修复旧件";
					}
				});

				String wReturnMsg = "";
				String wSendSAP = Configuration.readConfigString("isSendSAP", "config/config");
				if (wSendSAP.equals("1")) {
					// ①推送台车BOM给SAP
					String wJson = ChangeToJson(wLoginUser, wResultList, "U");
					if (StringUtils.isNotEmpty(wJson)) {
						wReturnMsg = SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 0, null);
					}
				}
				wResult.Result += StringUtils.Format("【{0}】{1}。", wOrder.PartNo, wReturnMsg);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> IPT_ImportAPSBOM(BMSEmployee wLoginUser, ExcelData wExcelData,
			String wOriginalFileName, int wOrderID) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

			List<APSBOMItem> wList = new ArrayList<APSBOMItem>();

			int wIndex = 1;
			for (ExcelSheetData wExcelSheetData : wExcelData.sheetData) {
				for (int i = 2; i < wExcelSheetData.lineData.size(); i++) {
					APSBOMItem wAPSBOMItem = new APSBOMItem();

					wAPSBOMItem.ID = 0;
					wAPSBOMItem.BOMType = 2;
					wAPSBOMItem.FactoryID = 1900;
					wAPSBOMItem.WBSNo = wOrder.OrderNo;
					wAPSBOMItem.OrderID = wOrder.ID;
					wAPSBOMItem.PartNo = wOrder.PartNo;
					wAPSBOMItem.LineID = wOrder.LineID;
					wAPSBOMItem.ProductID = wOrder.ProductID;
					wAPSBOMItem.CustomerID = wOrder.CustomerID;
					wAPSBOMItem.PartID = APSConstans.GetPartIDByCode(wExcelSheetData.lineData.get(i).colData.get(4));

					String wStepName = wExcelSheetData.lineData.get(i).colData.get(38).split("  ")[1];

					wAPSBOMItem.PartPointID = APSConstans.GetPartPointIDByName(wStepName);
					wAPSBOMItem.MaterialID = GetMaterialID(wLoginUser, wExcelSheetData.lineData.get(i).colData.get(7));
					wAPSBOMItem.MaterialNo = wExcelSheetData.lineData.get(i).colData.get(7);
					wAPSBOMItem.Number = StringUtils.parseDouble(wExcelSheetData.lineData.get(i).colData.get(12));
					wAPSBOMItem.UnitID = APSConstans.GetCFGUnit(wExcelSheetData.lineData.get(i).colData.get(13)).ID;
					wAPSBOMItem.ReplaceType = StringUtils.parseInt(wExcelSheetData.lineData.get(i).colData.get(24));
					wAPSBOMItem.OutsourceType = StringUtils.parseInt(wExcelSheetData.lineData.get(i).colData.get(27));
					wAPSBOMItem.OriginalType = 0;
					wAPSBOMItem.DisassyType = 0;
					wAPSBOMItem.OverLine = 0;
					wAPSBOMItem.PartChange = 0;
					wAPSBOMItem.ReceiveDepart = "0001";
					wAPSBOMItem.StockID = 1200;
					wAPSBOMItem.QTType = 0;
					wAPSBOMItem.QTItemType = 0;
					wAPSBOMItem.CustomerMaterial = 0;
					wAPSBOMItem.AuditorID = 0;
					wAPSBOMItem.AuditTime = Calendar.getInstance();
					wAPSBOMItem.EditorID = 0;
					wAPSBOMItem.EditTime = Calendar.getInstance();
					wAPSBOMItem.Status = 1;
					wAPSBOMItem.RelaDemandNo = wExcelSheetData.lineData.get(i).colData.get(0);
					wAPSBOMItem.TextCode = wExcelSheetData.lineData.get(i).colData.get(34);
					wAPSBOMItem.WorkCenter = "";
					wAPSBOMItem.DeleteID = wExcelSheetData.lineData.get(i).colData.get(33);
					wAPSBOMItem.SubRelaDemandNo = wExcelSheetData.lineData.get(i).colData.get(1);
					wAPSBOMItem.AssessmentType = wExcelSheetData.lineData.get(i).colData.get(6);
					wAPSBOMItem.AccessoryLogo = "";
					wAPSBOMItem.RepairPartClass = "";
					wAPSBOMItem.Remark = wExcelSheetData.lineData.get(i).colData.get(31);
					wAPSBOMItem.DingrongGroup = "";
					wAPSBOMItem.RepairCoreIdentification = "";
					wAPSBOMItem.PickingQuantity = 0;
					wAPSBOMItem.EvenExchangeRate = 0;
					wAPSBOMItem.Client = "";
					wAPSBOMItem.OrderNum = wIndex++;
					wAPSBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
					wAPSBOMItem.SourceID = 0;
					wAPSBOMItem.DifferenceItem = 0;
					wAPSBOMItem.OverQuota = 0;

					wList.add(wAPSBOMItem);
				}
			}

			APSBOMItemDAO.getInstance().APS_CreateBOMItem(wLoginUser, wList, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取物料ID
	 * 
	 * @return
	 */
	private int GetMaterialID(BMSEmployee wLoginUser, String wNo) {
		int wResult = 0;
		try {
			if (StringUtils.isEmpty(wNo)) {
				return wResult;
			}

			List<MSSMaterial> wList = WMSServiceImpl.getInstance().MSS_QueryMaterialList(wLoginUser, wNo)
					.List(MSSMaterial.class);
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}

			wResult = wList.get(0).ID;
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_BOMItemUpdateProperty(BMSEmployee wBMSEmployee, APSBOMItem wBOMItem) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 根据订单ID，工位、工序、物料ID 查询物料
			APSBOMItem wItem = APSBOMItemDAO.getInstance().APS_SelectItem(wBMSEmployee, wBOMItem.OrderID,
					wBOMItem.PartID, wBOMItem.PartPointID, wBOMItem.MaterialID, wBOMItem.ReplaceType,
					wBOMItem.OutsourceType, wErrorCode);
			if (wItem != null && wItem.ID > 0) {
				wItem.Number = wBOMItem.Number;
				wResult.Result = APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wBMSEmployee, wItem, wErrorCode);

				// 推送给SAP(更新)
				if (wBOMItem.OrderID > 0) {
					ExecutorService wES = Executors.newFixedThreadPool(1);
					wES.submit(() -> APSServiceImpl.getInstance().APS_UpdateBomItemByBOMItem(BaseDAO.SysAdmin,
							wResult.Result));
					wES.shutdown();
//					APSConstans.mUBomOrderList.add(wResult.Result);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_CompleteConfirm_V2(BMSEmployee wLoginUser, int wOrderID,
			List<String> wImagePathList, String wRemark) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

			if (wOrder.Status != OMSOrderStatus.Repairing.getValue()) {
				wResult.FaultCode += "提示：请选择状态为“维修中”的订单!";
				return wResult;
			}

			// ①判断非线上车，竣工确认
			boolean wCheckResult = OMSOrderDAO.getInstance().IsOffLineBoarding(wLoginUser, wOrderID, wErrorCode);
			if (wCheckResult) {
				SFCOrderForm wSFCOrderForm = new SFCOrderForm();

				wSFCOrderForm.ID = 0;
				wSFCOrderForm.CreateID = wLoginUser.ID;
				wSFCOrderForm.Creator = wLoginUser.Name;
				wSFCOrderForm.OrderID = wOrderID;
				wSFCOrderForm.OrderNo = wOrder.OrderNo;
				wSFCOrderForm.PartNo = wOrder.PartNo;
				wSFCOrderForm.ProductNo = wOrder.ProductNo;
				wSFCOrderForm.Type = SFCOrderFormType.CompleteConfirm.getValue();

				wSFCOrderForm.CreateTime = Calendar.getInstance();
				wSFCOrderForm.ConfirmID = wLoginUser.ID;
				wSFCOrderForm.ConfirmTime = Calendar.getInstance();
				wSFCOrderForm.Status = 2;

				if (wImagePathList != null && wImagePathList.size() > 0) {
					wSFCOrderForm.ImagePathList = wImagePathList;
				}

				if (StringUtils.isNotEmpty(wRemark)) {
					wSFCOrderForm.Remark = wRemark;
				}

				SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);

				wOrder.Status = OMSOrderStatus.FinishedWork.getValue();
				wOrder.RealFinishDate = Calendar.getInstance();
				OMSOrderDAO.getInstance().Update(BaseDAO.SysAdmin, wOrder, wErrorCode);

				return wResult;
			}

			// ⑩生成竣工确认单，且将订单状态改为已完工
			List<SFCOrderForm> wOrderList = SFCOrderFormDAO.getInstance().SelectList(wLoginUser, -1, wOrder.ID, "", 1,
					null, wErrorCode);
			if (wOrderList == null || wOrderList.size() <= 0) {
				wResult.FaultCode += "提示：该订单未完成终检，无法进行竣工确认!";
				return wResult;
			}

			// ⑧检查该订单的不合格评审单是否全部完成
			int wNCRNumber = AndonDAO.getInstance().SelectNcrCountNF(wLoginUser, wOrder.ID, wErrorCode);
			if (wNCRNumber > 0) {
				wResult.FaultCode += "提示：该订单有未完成的不合格评审单!";
				return wResult;
			}
			// ⑨检查该订单的返修单是否全部完成
			int wRepairNumber = AndonDAO.getInstance().SelectRepairCountNF(wLoginUser, wOrder.ID, wErrorCode);
			if (wRepairNumber > 0) {
				wResult.FaultCode += "提示：该订单有未完成的返修单!";
				return wResult;
			}

			APSTaskPart wTaskPart = AndonDAO.getInstance().SelectNotFinishTaskPart(wLoginUser, wOrder.ID, wErrorCode);
			if (wTaskPart != null && wTaskPart.ID > 0) {
				wResult.FaultCode += StringUtils.Format("提示：【{0}】工位未完工，无法竣工确认!", wTaskPart.PartName);
				return wResult;
			}

			if (wOrderList != null && wOrderList.size() > 0) {
				SFCOrderForm wSFCOrderForm = wOrderList.get(0);
				wSFCOrderForm.ConfirmID = wLoginUser.ID;
				wSFCOrderForm.ConfirmTime = Calendar.getInstance();
				wSFCOrderForm.Status = 2;
				if (wImagePathList != null && wImagePathList.size() > 0) {
					wSFCOrderForm.ImagePathList = wImagePathList;
				}
				if (StringUtils.isNotEmpty(wRemark)) {
					wSFCOrderForm.Remark = wRemark;
				}
				SFCOrderFormDAO.getInstance().Update(wLoginUser, wSFCOrderForm, wErrorCode);

				wOrder.Status = OMSOrderStatus.FinishedWork.getValue();
				wOrder.RealFinishDate = Calendar.getInstance();
				OMSOrderDAO.getInstance().Update(BaseDAO.SysAdmin, wOrder, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_UpdateBOMItemList(BMSEmployee wLoginUser, List<APSBOMItem> wDataList,
			int wSFCBOMTaskID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 处理数据
			for (APSBOMItem wBOMItem : wDataList) {
				// 判断是否为差异项
				wBOMItem.DifferenceItem = APSBOMItemDAO.getInstance().JudgeIsDifferenceItem(wLoginUser, wBOMItem,
						wErrorCode);
				// 判断是否超定额
//				wBOMItem.OverQuota = APSBOMItemDAO.getInstance().JudgeIsOverQuota(wLoginUser, wBOMItem, wErrorCode);

				if (wBOMItem.SourceType != APSBOMSourceType.SFCBOMTask.getValue()) {
					if (wBOMItem.ReplaceType == 1 || wBOMItem.ReplaceType == 2) {
						wBOMItem.AssessmentType = "常规新件";
					}
					if (wBOMItem.OutsourceType == 1 || wBOMItem.OutsourceType == 2) {
						wBOMItem.AssessmentType = "修复旧件";
					}
					if (wBOMItem.PartChange > 0) {
						wBOMItem.AssessmentType = "修复旧件";
					}
					if (wBOMItem.ReplaceType != 1 && (wBOMItem.OutsourceType == 3 || wBOMItem.OutsourceType == 4)) {
						wBOMItem.AssessmentType = "可用旧件";
					}
				}
			}

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wDataList.get(0).OrderID, wErrorCode);

			// 验证台车BOM
			String wMsg = CheckAPSBOM(wLoginUser, wDataList, wOrder);

			if (StringUtils.isNotEmpty(wMsg)) {

				List<BFCMessage> wBFCMessageList = new ArrayList<>();
				BFCMessage wMessage = null;
				int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(),
						APSShiftPeriod.Day, FMCShiftLevel.Day, 0);
				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = 0;
				wMessage.Title = StringUtils.Format("【台车BOM】提示", BPMEventModule.StationTip.getLable());
				wMessage.MessageText = wMsg;
				wMessage.ModuleID = BPMEventModule.StationTip.getValue();
				wMessage.ResponsorID = StringUtils.parseInt(Configuration.readConfigString("bomUser", "config/config"));
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Notify.getValue();
				wBFCMessageList.add(wMessage);

				CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);

				// 维护SAP推送状态
				if (wSFCBOMTaskID > 0) {
					ExecutorService wES = Executors.newFixedThreadPool(1);
					wES.submit(() -> UpdateSFCBOMTaskSAPStatus(wLoginUser, wMsg, wSFCBOMTaskID));
					wES.shutdown();
				}

				// 返回提示信息
				wResult.FaultCode = wMsg;
				return wResult;
			}

			// 保存到MES系统数据库
			for (APSBOMItem wBOMItem : wDataList) {
				wBOMItem.ID = APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wBOMItem, wErrorCode);
				wBOMItem.ProductNo = wOrder.ProductNo;
				wBOMItem.LineName = wOrder.LineName;
				wBOMItem.CustomerCode = APSConstans.GetCRMCustomer(wBOMItem.CustomerID).CustomerCode;
				wBOMItem.PartPointName = APSConstans.GetFPCPartPointName(wBOMItem.PartPointID);
			}

			String wSendSAP = Configuration.readConfigString("isSendSAP", "config/config");

			if (wSendSAP.equals("1")) {
				// ①质量损失的数据分开发送
				List<APSBOMItem> wQualityLossList = wDataList.stream().filter(p -> p.BOMType == 9)
						.collect(Collectors.toList());
				List<APSBOMItem> wNormalList = wDataList.stream().filter(p -> p.BOMType != 9)
						.collect(Collectors.toList());

				if (wQualityLossList != null && wQualityLossList.size() > 0) {
					// 判断是否已经有质量损失，若没有，用I创建，若有，用U更新
					boolean wIsExist = SFCBOMTaskDAO.getInstance().IsExistQualityLoss(wLoginUser, wOrder, wErrorCode);
					String wUpChar = "U";
					if (!wIsExist) {
						wUpChar = "I";
					}

					// ①推送台车BOM给SAP
					String wJson = ChangeToJson(wLoginUser, wQualityLossList, wUpChar);
					if (StringUtils.isNotEmpty(wJson)) {
						SendToSAP(wLoginUser, wJson, wOrder.OrderNo, wSFCBOMTaskID, wOrder, 0, null);
					}
				}

				if (wNormalList != null && wNormalList.size() > 0) {
					// ①推送台车BOM给SAP
					String wJson = ChangeToJson(wLoginUser, wNormalList, "U");
					if (StringUtils.isNotEmpty(wJson)) {
						SendToSAP(wLoginUser, wJson, wOrder.OrderNo, wSFCBOMTaskID, wOrder, 0, null);
					}
				}

				// ①推送台车BOM给SAP
//				String wJson = ChangeToJson(wLoginUser, wDataList, "U");
//				if (StringUtils.isNotEmpty(wJson)) {
//					SendToSAP(wLoginUser, wJson, wOrder.OrderNo, wSFCBOMTaskID, wOrder, 0);
//				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 维护偶换件的推送SAP状态
	 */
	private void UpdateSFCBOMTaskSAPStatus(BMSEmployee wLoginUser, String wMsg, int wSFCBOMTaskID) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			SFCBOMTask wTask = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wSFCBOMTaskID, "",
					wErrorCode);
			if (StringUtils.isEmpty(wMsg)) {
			} else if (wMsg.contains("失败") || wMsg.contains("E")) {
				wTask.SAPStatus = 2;
				wTask.SAPStatusText = wMsg;
				SFCBOMTaskDAO.getInstance().Update(wLoginUser, wTask, wErrorCode);

				// ①发送通知消息给黄刚，通知他创建工序。
				SendMessageToTechToCreateStep(wLoginUser, wTask.PartNo, wMsg, wTask.ID);
			} else if (wMsg.contains("S")) {
				wTask.SAPStatus = 1;
				wTask.SAPStatusText = "推送成功";
				SFCBOMTaskDAO.getInstance().Update(wLoginUser, wTask, wErrorCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 发送通知消息给工艺(黄刚),通知他及时创建工序
	 */
	private void SendMessageToTechToCreateStep(BMSEmployee wLoginUser, String partNo, String wMsg, int wTaskID) {
		try {
			String wUsers = Configuration.readConfigString("bomUser", "config/config");
			String[] wStrs = wUsers.split(",");

			List<BFCMessage> wBFCMessageList = new ArrayList<>();
			BFCMessage wMessage = null;
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			for (String wUserStr : wStrs) {
				int wUserID = Integer.parseInt(wUserStr);
				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = wTaskID;
				wMessage.Title = StringUtils.Format("{0} 创建失败", BPMEventModule.APSBOMError.getLable());
				wMessage.MessageText = StringUtils.Format("【{0}】{1}", partNo, wMsg);
				wMessage.ModuleID = BPMEventModule.APSBOMError.getValue();
				wMessage.ResponsorID = wUserID;
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Notify.getValue();
				wBFCMessageList.add(wMessage);
			}
			CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<List<APSTaskPart>> APS_QueryMonthPlanByMonth(BMSEmployee wLoginUser, int wYear, int wMonth) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①开始时间
			Calendar wStartTime = Calendar.getInstance();
			wStartTime.set(wYear, wMonth - 1, 1, 0, 0, 0);
			// ②结束时间
			Calendar wEndTime = Calendar.getInstance();
			wEndTime.set(wYear, wMonth, 1, 0, 0, 0);

			wResult.Result = APSTaskPartDAO.getInstance().APS_QueryMonthPlanByMonth(wLoginUser, wStartTime, wEndTime,
					wErrorCode);

			List<OMSOrder> wOMSOrderList = new ArrayList<OMSOrder>();
			if (wResult.Result.size() > 0) {
				List<Integer> wOrderIDList = wResult.Result.stream().map(p -> p.OrderID).distinct()
						.collect(Collectors.toList());
				wOMSOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);
			}
			wResult.CustomResult.put("OMSOrderList", wOMSOrderList);

			// 多条件去重
			wResult.Result = wResult.Result.stream()
					.collect(Collectors.collectingAndThen(Collectors.toCollection(
							() -> new TreeSet<>(Comparator.comparing(o -> o.getOrderID() + ";" + o.getPartID()))),
							ArrayList::new));

			// 工位排序
			wResult.Result = OrderPart(wResult.Result);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 工位排序
	 */
	private List<APSTaskPart> OrderPart(List<APSTaskPart> wList) {
		List<APSTaskPart> wResult = wList;
		try {
			List<LFSWorkAreaStation> wWSList = APSConstans.GetLFSWorkAreaStationList().values().stream()
					.collect(Collectors.toList());
			for (APSTaskPart wAPSTaskPart : wList) {
				if (wWSList.stream().anyMatch(p -> p.StationID == wAPSTaskPart.PartID)) {
					wAPSTaskPart.PartOrder = wWSList.stream().filter(p -> p.StationID == wAPSTaskPart.PartID)
							.findFirst().get().OrderNum;
				} else {
					wAPSTaskPart.PartOrder = 0;
				}
			}
			// 排序
			wResult.sort(Comparator.comparing(APSTaskPart::getPartOrder));
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskPart>> APS_QueryWeekPlanByWeek(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSTaskPartDAO.getInstance().APS_QueryWeekPlanByWeek(wLoginUser, wStartTime, wEndTime,
					wErrorCode);

			List<OMSOrder> wOMSOrderList = new ArrayList<OMSOrder>();
			if (wResult.Result.size() > 0) {
				List<Integer> wOrderIDList = wResult.Result.stream().map(p -> p.OrderID).distinct()
						.collect(Collectors.toList());
				wOMSOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);
			}
			wResult.CustomResult.put("OMSOrderList", wOMSOrderList);

			// 多条件去重
			wResult.Result = wResult.Result.stream()
					.collect(Collectors.collectingAndThen(Collectors.toCollection(
							() -> new TreeSet<>(Comparator.comparing(o -> o.getOrderID() + ";" + o.getPartID()))),
							ArrayList::new));

			// 工位排序
			wResult.Result = OrderPart(wResult.Result);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_SendToSAPPro(BMSEmployee wLoginUser, int wOrderID, int wSFCBOMTaskID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

			int wBOMID = APSBOMItemDAO.getInstance().GetCurrentStandardBOMID(wLoginUser, wOrder.ProductID,
					wOrder.LineID, wOrder.CustomerID, wErrorCode);

			if (wOrder.RouteID <= 0) {
				wBOMID = 0;
			}

			List<MSSBOMItem> wMSSBOMItemList = null;
			if (wBOMID > 0) {
				wMSSBOMItemList = WMSServiceImpl.getInstance()
						.MSS_QueryBOMItemAll(wLoginUser, wBOMID, -1, -1, -1, -1, -1, -1, -1, -1).List(MSSBOMItem.class);
			}

			if (wMSSBOMItemList == null || wMSSBOMItemList.size() <= 0) {

				// 返回提示信息
				wResult.FaultCode = StringUtils.Format("提示：【{0}】该车辆无当前标准BOM，创建台车BOM失败!", wOrder.PartNo);

				// 此处推送消息，告知为何创建台车bom失败
				List<BFCMessage> wBFCMessageList = new ArrayList<>();
				BFCMessage wMessage = null;
				int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(),
						APSShiftPeriod.Day, FMCShiftLevel.Day, 0);
				// 发送任务消息到人员
				wMessage = new BFCMessage();
				wMessage.Active = 0;
				wMessage.CompanyID = 0;
				wMessage.CreateTime = Calendar.getInstance();
				wMessage.EditTime = Calendar.getInstance();
				wMessage.ID = 0;
				wMessage.MessageID = 0;
				wMessage.Title = StringUtils.Format("【台车BOM】提示", BPMEventModule.StationTip.getLable());
				wMessage.MessageText = wResult.FaultCode;
				wMessage.ModuleID = BPMEventModule.StationTip.getValue();
				wMessage.ResponsorID = StringUtils.parseInt(Configuration.readConfigString("bomUser", "config/config"));
				wMessage.ShiftID = wShiftID;
				wMessage.StationID = 0;
				wMessage.Type = BFCMessageType.Notify.getValue();
				wBFCMessageList.add(wMessage);

				CoreServiceImpl.getInstance().BFC_UpdateMessageList(wLoginUser, wBFCMessageList);

				return wResult;
			}

			List<APSBOMItem> wResultList = new ArrayList<APSBOMItem>();

			APSBOMItem wBOMItem = null;

			for (MSSBOMItem wMSSBOMItem : wMSSBOMItemList) {

				if (wMSSBOMItem.Active != 1)
					continue;

				if (wMSSBOMItem.ReplaceType == 2 && wMSSBOMItem.OutsourceType == 1) {

					wBOMItem = new APSBOMItem(wMSSBOMItem, wOrder.LineID, wOrder.ProductID, wOrder.CustomerID,
							wOrder.ID, wOrder.OrderNo, wOrder.PartNo);

					wBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
					wBOMItem.SourceID = wMSSBOMItem.ID;
					wBOMItem.AuditorID = wLoginUser.ID;
					wBOMItem.EditorID = wLoginUser.ID;
					wBOMItem.AuditTime = Calendar.getInstance();
					wBOMItem.EditTime = Calendar.getInstance();

					wResultList.add(wBOMItem);
				}
			}

			// 评估类型赋值
			wResultList.forEach(p -> {
				if (p.OutsourceType == 1) {
					p.AssessmentType = "修复旧件";
				}
			});

			APSServiceImpl.getInstance().APS_UpdateBOMItemList(wLoginUser, wResultList, wSFCBOMTaskID);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_ChangeLogAddToSAP(BMSEmployee wLoginUser, int wLogID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			TCMMaterialChangeLog wLog = TCMMaterialChangeLogDAO.getInstance().SelectByID(wLoginUser, wLogID,
					wErrorCode);
			List<TCMMaterialChangeItems> wList = wLog.ItemList.stream()
					.filter(p -> p.ChangeType == TCMChangeType.MaterialInsert.getValue()
							&& (p.ReplaceType == 1 || p.OutsourceType == 1))
					.collect(Collectors.toList());
			if (wList.size() > 0) {
				List<MSSBOMItem> wBOMItemList = CloneTool.CloneArray(wList, MSSBOMItem.class);

				String[] wStrs = wLog.OrderIDList.split(",");
				for (String wOrderStr : wStrs) {
					int wOrderID = StringUtils.parseInt(wOrderStr);
					if (wOrderID <= 0) {
						continue;
					}

					OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

					List<APSBOMItem> wResultList = new ArrayList<APSBOMItem>();

					APSBOMItem wBOMItem = null;

					for (MSSBOMItem wMSSBOMItem : wBOMItemList) {

						if (wMSSBOMItem.Active != 1)
							continue;

						if (wMSSBOMItem.ReplaceType == 1 || wMSSBOMItem.OutsourceType == 1) {

							wBOMItem = new APSBOMItem(wMSSBOMItem, wOrder.LineID, wOrder.ProductID, wOrder.CustomerID,
									wOrder.ID, wOrder.OrderNo, wOrder.PartNo);

							wBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
							wBOMItem.SourceID = wMSSBOMItem.ID;
							wBOMItem.AuditorID = wLoginUser.ID;
							wBOMItem.EditorID = wLoginUser.ID;
							wBOMItem.AuditTime = Calendar.getInstance();
							wBOMItem.EditTime = Calendar.getInstance();

							wResultList.add(wBOMItem);
						}
					}

					// 评估类型赋值
					wResultList.forEach(p -> {
						if (p.OutsourceType == 1) {
							p.AssessmentType = "修复旧件";
						}
					});

					APSServiceImpl.getInstance().APS_UpdateBOMItemList(wLoginUser, wResultList, 0);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<MCSLogInfo>> MCS_QueryLogList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<MCSLogInfo>> wResult = new ServiceResult<List<MCSLogInfo>>();
		wResult.Result = new ArrayList<MCSLogInfo>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = MCSLogInfoDAO.getInstance().SelectList(wLoginUser, -1, "", "", "", "", "", wStartTime,
					wEndTime, wErrorCode);

			if (wResult.Result.size() > 0) {
				// 排序
				wResult.Result.sort(Comparator.comparing(MCSLogInfo::getCreateTime, Comparator.reverseOrder()));
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSSchedulingVersionBPM>> APS_QuerySchedulingVersionBPMEmployeeAllWeb(
			BMSEmployee wLoginUser, int wAPSShiftPeriod, int wStatus, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<APSSchedulingVersionBPM>> wResult = new ServiceResult<List<APSSchedulingVersionBPM>>();
		wResult.Result = new ArrayList<APSSchedulingVersionBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (wStatus) {
			case 1:
				wResult.Result.addAll(APSSchedulingVersionBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1,
						"", "", wAPSShiftPeriod, -1, new ArrayList<Integer>(Arrays.asList(20)), wStartTime, wEndTime,
						wErrorCode));
				break;
			case 0:
				wResult.Result.addAll(APSSchedulingVersionBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1,
						"", "", wAPSShiftPeriod, -1, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5)), null, null,
						wErrorCode));
				break;
			default:
				wResult.Result.addAll(APSSchedulingVersionBPMDAO.getInstance().SelectList(wLoginUser, -1, "", -1, -1,
						"", "", wAPSShiftPeriod, -1, null, wStartTime, wEndTime, wErrorCode));
				break;
			}
			wResult.Result.sort((o1, o2) -> o2.SubmitTime.compareTo(o1.SubmitTime));

			if (wResult.Result.size() <= 0 || wStatus == 1) {
				return wResult;
			}
			List<BPMTaskBase> wBaseList = APSSchedulingVersionBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser,
					wLoginUser.ID, wErrorCode);

			if (wBaseList == null || wBaseList.size() <= 0) {
				return wResult;
			}
			for (BPMTaskBase wTaskBase : wBaseList) {
				if (!(wTaskBase instanceof APSSchedulingVersionBPM))
					continue;
				APSSchedulingVersionBPM wAPSSchedulingVersionBPM = (APSSchedulingVersionBPM) wTaskBase;
				wAPSSchedulingVersionBPM.TagTypes = TaskQueryType.ToHandle.getValue();
				for (int i = 0; i < wResult.Result.size(); i++) {
					if (wResult.Result.get(i).ID == wAPSSchedulingVersionBPM.ID)
						wResult.Result.set(i, wAPSSchedulingVersionBPM);
				}
			}

		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<String> APS_CheckSchedulingVersionBPM(BMSEmployee wLoginUser, APSSchedulingVersionBPM wTask) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 提交
			if (wTask.Status == 2) {
				List<APSTaskPart> wList = APSTaskPartDAO.getInstance().SelectListByIDList(wLoginUser,
						wTask.TaskPartIDList, wErrorCode);
				if (wList.stream().anyMatch(p -> p.Status != 1)) {
					wResult.Result += "提示：请提交未排的数据!";
					return wResult;
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSBOMLog>> APS_QueryBOMLogList(BMSEmployee wLoginUser, int wOrderID, int wLineID,
			int wProductID, int wCustomerID, int wStatus, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<APSBOMLog>> wResult = new ServiceResult<List<APSBOMLog>>();
		wResult.Result = new ArrayList<APSBOMLog>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSBOMLogDAO.getInstance().SelectList(wLoginUser, -1, wProductID, wLineID, wCustomerID,
					wOrderID, wStatus, wStartTime, wEndTime, wErrorCode);

			// 时间降序排序
			wResult.Result.sort(Comparator.comparing(APSBOMLog::getCreateTime, Comparator.reverseOrder()));

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSBOMLog> APS_QueryBOMLogByID(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSBOMLog> wResult = new ServiceResult<APSBOMLog>();
		wResult.Result = new APSBOMLog();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = APSBOMLogDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			if ((wResult.Result.Status == 2 || wResult.Result.Status == 3)
					&& wResult.Result.Msg.contains("请用事务码ZPM1011配置标准工序")) {
				SAPResult wSAPResult = JSON.parseObject(wResult.Result.Msg, SAPResult.class);
				String[] wStrs = wSAPResult.msg.split("\\|");
				for (String wStr : wStrs) {
					if (wResult.Result.ErrorList.stream().anyMatch(p -> p.equals(wStr))) {
						continue;
					}
					wResult.Result.ErrorList.add(wStr);
				}

				// 如果错误列表有值，转换赋值
				if (wResult.Result.ErrorList.size() > 0) {
					wResult.Result.Msg = StringUtils.Join("\r\n", wResult.Result.ErrorList);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSManuCapacityStep> APS_QueryManuCapacityStep(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSManuCapacityStep> wResult = new ServiceResult<APSManuCapacityStep>();
		wResult.Result = new APSManuCapacityStep();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = APSManuCapacityStepDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSManuCapacityStep>> APS_QueryManuCapacityStepList(BMSEmployee wLoginUser, int wLineID,
			int wPartID, int wStatus, int wActive) {
		ServiceResult<List<APSManuCapacityStep>> wResult = new ServiceResult<List<APSManuCapacityStep>>();
		wResult.Result = new ArrayList<APSManuCapacityStep>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			List<APSManuCapacityStep> wList = APSManuCapacityStepDAO.getInstance().SelectList(wLoginUser, -1, wLineID,
					wPartID, -1, wStatus, wActive, wErrorCode);

			// ①根据工位、修程获取产线单元明细，工序列表
			List<Integer> wStepList = APSManuCapacityStepDAO.getInstance().SelectLineUnitStepList(wLoginUser, wLineID,
					wPartID, wErrorCode);
			// ②遍历赋值
			APSManuCapacityStep wAPSManuCapacityStep;
			for (Integer wStepID : wStepList) {
				if (wList.stream().anyMatch(p -> p.StepID == wStepID)) {
					wAPSManuCapacityStep = wList.stream().filter(p -> p.StepID == wStepID).findFirst().get();
				} else {
					wAPSManuCapacityStep = new APSManuCapacityStep();
					wAPSManuCapacityStep.ID = 0;
					wAPSManuCapacityStep.LineID = wLineID;
					wAPSManuCapacityStep.LineName = APSConstans.GetFMCLineName(wLineID);
					wAPSManuCapacityStep.PartID = wPartID;
					wAPSManuCapacityStep.PartName = APSConstans.GetFPCPartName(wPartID);
					wAPSManuCapacityStep.StepID = wStepID;
					wAPSManuCapacityStep.StepName = APSConstans.GetFPCPartPointName(wStepID);
				}
				wResult.Result.add(wAPSManuCapacityStep);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_UpdateManuCapacityStep(BMSEmployee wLoginUser, APSManuCapacityStep wCapacity) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = APSManuCapacityStepDAO.getInstance().Update(wLoginUser, wCapacity, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void APS_ActiveManuCapacityStepList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			APSManuCapacityStepDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public List<FPCPartTemp> mFPCPartListTemp = new ArrayList<FPCPartTemp>();

	@Override
	public ServiceResult<List<APSTaskPart>> APS_AutoSchedulingStep(BMSEmployee wLoginUser, int wOrderID,
			APSShiftPeriod wShiftPeriod, Calendar wStartTime, Calendar wEndTime, int wWorkDay, int wLimitMinutes,
			List<APSTaskPart> wMutualTaskList, int wPartID) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			// 1、获取排程参数
			// ①工位任务列表
			List<APSTaskPart> wOrderPartIssuedList = new ArrayList<APSTaskPart>();
			// ②委外订单列表
			List<OMSOutsourceOrder> wOutsourceOrderList = new ArrayList<OMSOutsourceOrder>();
			// ③工位工时列表
			List<APSManuCapacity> wManuCapacityList = getManuCapacityList(wLoginUser, wOrder, wPartID);
			// ④获取工艺路径
			Map<Integer, List<FPCRoutePart>> wRoutePartListMap = getRoutePartListMap(wLoginUser, wOrder, wPartID);
			// ⑤产线列表
			List<FMCLine> wLineList = APSConstans.GetFMCLineList().values().stream().collect(Collectors.toList());
			wLineList.removeIf(p -> p.Active != 1);
			// ⑥获取安装明细列表
			List<APSInstallation> wAPSInstallationList = new ArrayList<APSInstallation>();
			// ⑦获取拆解明细列表
			List<APSDismantling> wAPSDismantlingList = new ArrayList<APSDismantling>();
			// ⑧冲突消息
			List<APSMessage> wMessageList = new ArrayList<APSMessage>();
			// ⑨订单列表
			List<OMSOrder> wOrderList = new ArrayList<OMSOrder>(Arrays.asList(wOrder));
			// 2、调用自动排程接口
			wResult = APSServiceImpl.getInstance().APS_AutoTaskPart(wLoginUser, wOrderList, wShiftPeriod,
					wOrderPartIssuedList, wOutsourceOrderList, wStartTime, wEndTime, wRoutePartListMap,
					wManuCapacityList, wAPSInstallationList, wAPSDismantlingList, wMessageList, wWorkDay, wLimitMinutes,
					wMutualTaskList);
			// 3、返回数据处理
			// ①赋值WBS号
			if (wResult.Result.size() > 0) {
				wResult.Result.forEach(p -> {
					if (StringUtils.isEmpty(p.PartNo)) {
						p.PartNo = wOrderList.stream().filter(q -> q.ID == p.OrderID).findFirst().get().OrderNo;
					}
				});
			}
			// ②赋值工艺路线
			if (wResult.Result != null && wResult.Result.size() > 0) {
				wResult.Result.forEach(
						p -> p.RouteID = wOrderList.stream().filter(q -> q.ID == p.OrderID).findFirst().get().RouteID);
			}
			// ③甘特图Tip生成
			for (APSTaskPart wItem : wResult.Result) {
				Optional<APSManuCapacity> wOption = wManuCapacityList.stream()
						.filter(p -> p.LineID == wItem.LineID && p.PartID == wItem.PartID).findFirst();
				if (wOption.isPresent()) {
					APSManuCapacity wCapacity = wOption.get();
					String wTaskText = StringUtils.Format("工位容量：{0}\n排程周期(天)：{1}", wCapacity.FQTY, wCapacity.WorkHour);
					wItem.TaskText = wTaskText;
				}
			}
			// ④处理月排程结尾堆积问题
			Calendar wMiddleTime = wEndTime;

			Calendar wLastTime = (Calendar) wEndTime.clone();
			wLastTime.add(Calendar.DATE, 1);

			if (wShiftPeriod == APSShiftPeriod.Month && wResult.Result != null && wResult.Result.size() > 0) {
				List<APSTaskPart> wTempList = wResult.Result.stream().filter(p -> p.StartTime.compareTo(wEndTime) > 0)
						.collect(Collectors.toList());
				if (wTempList != null && wTempList.size() > 0) {
					APSTaskPart wMinItem = wTempList.stream().min(Comparator.comparing(APSTaskPart::getEndTime)).get();
					wMiddleTime = wMinItem.EndTime;

					wResult.Result.forEach(p -> {
						if (p.StartTime.compareTo(wEndTime) > 0) {
							double wHour = CalIntervalHour(wEndTime, p.StartTime);
							p.StartTime = wEndTime;
							p.EndTime.add(Calendar.HOUR_OF_DAY, (int) wHour * -1);
						}
					});
				}
			}
			// 4、其他返回参数构造
			// ①StartTime
			wResult.CustomResult.put("StartTime", wStartTime);
			// ②MiddleTime
			wResult.CustomResult.put("MiddleTime", wMiddleTime);
			// ③RouteList
//			List<Integer> wRouteIDList = wOrderList.stream().map(p -> p.RouteID).distinct()
//					.collect(Collectors.toList());
//			List<FPCRoute> wRouteList = APSServiceImpl.getInstance().ASP_QueryRouteList(wLoginUser, wRouteIDList,
//					wPartID).Result;
			List<FPCRoute> wRouteList = new ArrayList<FPCRoute>();
			wResult.CustomResult.put("RouteList", wRouteList);
			// ④Msg
			// 产线列表
			if (wLineList == null || wLineList.size() <= 0)
				return wResult;
			// 工位列表
			List<FPCPart> wPartList = null;
//			if (mFPCPartListTemp.stream().anyMatch(p -> p.RouteID == wOrder.RouteID && p.PartID == wPartID)) {
//				wPartList = mFPCPartListTemp.stream().filter(p -> p.RouteID == wOrder.RouteID && p.PartID == wPartID)
//						.findFirst().get().List;
//			} else {
			List<FPCPart> wPList = APSTaskPartDAO.getInstance().SelectStepPart(wLoginUser, wOrderID, wPartID,
					wErrorCode);

			FPCPartTemp wTemp = new FPCPartTemp();
			wTemp.RouteID = wOrder.RouteID;
			wTemp.PartID = wPartID;
			wTemp.List = wPList;
			mFPCPartListTemp.add(wTemp);

			wPartList = wPList;
//			}

			// list排序
			for (APSTaskPart wAPSTaskPart : wResult.Result) {
				if (!wPartList.stream().anyMatch(p -> p.ID == wAPSTaskPart.PartID)) {
					continue;
				}

				int wOID = wPartList.stream().filter(p -> p.ID == wAPSTaskPart.PartID).findFirst().get().FactoryID;
				wAPSTaskPart.TaskLineID = wOID;
			}
			// 排序
			wResult.Result.sort(Comparator.comparing(APSTaskPart::getTaskLineID));

			if (wPartList == null || wPartList.size() <= 0)
				return wResult;

			int wFlag = 1;
			int wOrderFlag = 1;
			List<APSTaskPart> wTreeList = new ArrayList<APSTaskPart>();
			List<LFSWorkAreaStation> wWorkAreaStationList = getLFSWorkAreaStationList(wPartList);
			if (wWorkAreaStationList == null || wWorkAreaStationList.size() <= 0) {
				return wResult;
			}

			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				// 工位
				APSTaskPart wAPSTaskPart = new APSTaskPart();
				wAPSTaskPart.PartID = wItem.StationID;
				wAPSTaskPart.PartOrder = wOrderFlag++;
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wAPSTaskPart.PartName = wOption.get().Name;
				}
				wAPSTaskPart.UniqueID = wFlag++;
				wTreeList.add(wAPSTaskPart);

				for (FMCLine wLine : wLineList) {
					// 产线
					APSTaskPart wLinePart = new APSTaskPart();
					wLinePart.UniqueID = wFlag++;
					wLinePart.LineID = wLine.ID;
					wLinePart.LineName = wLine.Name;
					wAPSTaskPart.TaskPartList.add(wLinePart);

					// 工位任务
					wLinePart.TaskPartList = wResult.Result.stream()
							.filter(p -> p.PartID == wItem.StationID && p.LineID == wLine.ID)
							.collect(Collectors.toList());
					if (wLinePart.TaskPartList != null && wLinePart.TaskPartList.size() > 0) {
						for (APSTaskPart wItemPart : wLinePart.TaskPartList) {
							wItemPart.UniqueID = wFlag++;
							wItemPart.PartOrder = wAPSTaskPart.PartOrder;

							// 为冲突消息设置唯一标识
							if (wMessageList == null || wMessageList.size() <= 0)
								continue;
							Optional<APSMessage> wMessageOption = wMessageList.stream()
									.filter(p -> p.LineID == wItemPart.LineID && p.PartID == wItemPart.PartID
											&& p.OrderID == wItemPart.OrderID)
									.findFirst();
							if (wMessageOption.isPresent()) {
								APSMessage wMessage = wMessageOption.get();
								wMessage.UniqueID = wItemPart.UniqueID;
								wItemPart.APSMessage = wMessage.MessageText;
							}
						}
					} else {
						wLinePart.TaskPartList = new ArrayList<APSTaskPart>();
						wLinePart.TaskPartList.add(new APSTaskPart());
					}
				}
			}
			wResult.CustomResult.put("Msg", wMessageList);
			// ⑤TreeList
			wResult.CustomResult.put("TreeList", wTreeList);
			// ⑥TableInfoList
			List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
			Map<String, Object> wHeadMap = new HashMap<String, Object>();
			List<FPCPart> wStationList = new ArrayList<FPCPart>();
			// 排序
			if (wWorkAreaStationList != null && wWorkAreaStationList.size() > 0) {
				wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			}
			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wHeadMap.put("Station_" + String.valueOf(wItem.StationID), wOption.get().Name);
					wStationList.add(wOption.get());
				}
			}
			wTableMap.add(wHeadMap);
			wTableMap.addAll(APSServiceImpl.getInstance().ChangeToTable(wOrderList, wResult.Result,
					wWorkAreaStationList).Result);
			wResult.CustomResult.put("TableInfoList", wTableMap);
			// ⑦OrderColumn
			wResult.CustomResult.put("OrderColumn", wStationList);
			// ⑧MaxTime
			// 最大时间
			Calendar wMaxTime = wResult.Result.stream().max(Comparator.comparing(APSTaskPart::getEndTime))
					.get().EndTime;
			wResult.CustomResult.put("MaxTime", wMaxTime);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<FPCRoutePartTemp> mFPCRoutePartTempList = new ArrayList<FPCRoutePartTemp>();

	/**
	 * 获取工序工艺路径
	 */
	private Map<Integer, List<FPCRoutePart>> getRoutePartListMap(BMSEmployee wLoginUser, OMSOrder wOrder, int wPartID) {
		Map<Integer, List<FPCRoutePart>> wResult = new HashMap<Integer, List<FPCRoutePart>>();
		try {
			List<FPCRoutePartPoint> wList = null;
			if (mFPCRoutePartTempList != null && mFPCRoutePartTempList.stream()
					.anyMatch(p -> p.RouteID == wOrder.RouteID && p.PartID == wPartID)) {
				wList = mFPCRoutePartTempList.stream().filter(p -> p.RouteID == wOrder.RouteID && p.PartID == wPartID)
						.findFirst().get().List;
			} else {
				wList = FMCServiceImpl.getInstance()
						.FPC_QueryRoutePartPointListByRouteID(wLoginUser, wOrder.RouteID, wPartID)
						.List(FPCRoutePartPoint.class);
				FPCRoutePartTemp wTemp = new FPCRoutePartTemp();
				wTemp.RouteID = wOrder.RouteID;
				wTemp.PartID = wPartID;
				wTemp.List = wList;
				mFPCRoutePartTempList.add(wTemp);
			}

			List<FPCRoutePart> wRoutePartList = new ArrayList<FPCRoutePart>();
			for (FPCRoutePartPoint wFPCRoutePartPoint : wList) {
				FPCRoutePart wItem = CloneTool.Clone(wFPCRoutePartPoint, FPCRoutePart.class);
				wItem.PrevPartID = wFPCRoutePartPoint.PrevStepID;
				wItem.NextPartIDMap = wFPCRoutePartPoint.NextStepIDMap;
				wItem.PartID = wFPCRoutePartPoint.PartPointID;
				wRoutePartList.add(wItem);
			}
			wResult.put(wOrder.ID, wRoutePartList);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<FPCCapacityStepTemp> mFPCCapacityStepTempList = new ArrayList<FPCCapacityStepTemp>();

	/**
	 * 获取工序工时
	 */
	private List<APSManuCapacity> getManuCapacityList(BMSEmployee wLoginUser, OMSOrder wOrder, int wPartID) {
		List<APSManuCapacity> wResult = new ArrayList<APSManuCapacity>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSManuCapacityStep> wList = null;

			if (mFPCCapacityStepTempList != null && mFPCCapacityStepTempList.stream()
					.anyMatch(p -> p.RouteID == wOrder.RouteID && p.PartID == wPartID)) {
				wList = mFPCCapacityStepTempList.stream()
						.filter(p -> p.RouteID == wOrder.RouteID && p.PartID == wPartID).findFirst().get().List;
			} else {
				wList = APSManuCapacityStepDAO.getInstance().SelectList(wLoginUser, -1, wOrder.LineID, wPartID, -1, -1,
						1, wErrorCode);
				FPCCapacityStepTemp wTemp = new FPCCapacityStepTemp();
				wTemp.RouteID = wOrder.RouteID;
				wTemp.PartID = wPartID;
				wTemp.List = wList;
				mFPCCapacityStepTempList.add(wTemp);
			}

			for (APSManuCapacityStep wAPSManuCapacityStep : wList) {
				APSManuCapacity wItem = CloneTool.Clone(wAPSManuCapacityStep, APSManuCapacity.class);
				wItem.PartID = wAPSManuCapacityStep.StepID;
				wItem.PartName = wAPSManuCapacityStep.StepName;
				wResult.add(wItem);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 工位转化为工区工位
	 */
	private List<LFSWorkAreaStation> getLFSWorkAreaStationList(List<FPCPart> wPartList) {
		List<LFSWorkAreaStation> wResult = new ArrayList<LFSWorkAreaStation>();
		try {
			for (FPCPart wFPCPart : wPartList) {
				LFSWorkAreaStation wItem = new LFSWorkAreaStation();
				wItem.StationID = wFPCPart.ID;
				wItem.StationName = wFPCPart.Name;
				wResult.add(wItem);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 计算开始时间和结束时间之间的间隔小时
	 * 
	 * @param wStartTime 开始时间
	 * @param wEndTime   结束时间
	 * @return 间隔小时
	 */
	public double CalIntervalHour(Calendar wStartTime, Calendar wEndTime) {
		double wResult = 0.0;
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) >= 0) {
				return wResult;
			}

			long wStart = wStartTime.getTime().getTime();
			long wEnd = wEndTime.getTime().getTime();
			long wMinutes = (wEnd - wStart) / (1000 * 60);
			double wHour = (double) wMinutes / 60;
			wResult = formatDouble(wHour);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 四舍五入保留两位小数
	 * 
	 * @param wNumber
	 * @return
	 */
	public double formatDouble(double wNumber) {
		double wResult = 0.0;
		try {
			// 如果不需要四舍五入，可以使用RoundingMode.DOWN
			BigDecimal wBG = new BigDecimal(wNumber).setScale(2, RoundingMode.UP);
			wResult = wBG.doubleValue();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_BomItemPropertyChange(BMSEmployee wLoginUser, int wID, String wDeleteID,
			double wNumber) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①根据ID查询台车bom
			APSBOMItem wBomItem = APSBOMItemDAO.getInstance().APS_QueryBOMItemByID(wLoginUser, wID, wErrorCode);
			if (wBomItem == null || wBomItem.ID <= 0) {
				wResult.FaultCode += "提示：此ID不存在!";
				return wResult;
			}

			if (StringUtils.isNotEmpty(wDeleteID)) {
				wBomItem.DeleteID = wDeleteID;
			}

			if (wNumber > 0) {
				wBomItem.Number = wNumber;
			}

			// ③更新MES台车Bom
			APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wBomItem, wErrorCode);

			// ③构造推送json
			String wJson = ChangeToJson(wLoginUser, new ArrayList<APSBOMItem>(Arrays.asList(wBomItem)), "U");
			// ④推送数据
			if (StringUtils.isNotEmpty(wJson)) {
				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wBomItem.OrderID, wErrorCode);

				String wReturnMsg = SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 0,
						new ArrayList<Integer>(Arrays.asList(wID)));
				if (wReturnMsg.contains("失败") || wReturnMsg.contains("E")) {
					wResult.FaultCode += wReturnMsg;
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_AssessTypeChange(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①查询出偶换件委外必修件评估类型为常规新件的偶换件ID集合
			List<Integer> wIDList = APSBOMItemDAO.getInstance().SelectAssessErrorIDList(wLoginUser, wErrorCode);
			if (wIDList == null || wIDList.size() <= 0) {
				wResult.FaultCode += "提示：没有偶换件委外必须件评估类型为常规新件的台车bom";
				return wResult;
			}
			// ②根据偶换件ID集合获取偶换件集合
			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().SelectByIDList(wLoginUser, wIDList, wErrorCode);
			if (wList == null || wList.size() <= 0) {
				wResult.FaultCode += "提示：没有偶换件委外必须件评估类型为常规新件的台车bom";
				return wResult;
			}
			// ③提取订单集合
			List<Integer> wOrderIDList = wList.stream().map(p -> p.OrderID).distinct().collect(Collectors.toList());
			// ④遍历订单，修改属性，并且推送台车bom到SAP
			for (Integer wOrderID : wOrderIDList) {
				List<APSBOMItem> wOrderBomList = wList.stream().filter(p -> p.OrderID == wOrderID)
						.collect(Collectors.toList());

				for (APSBOMItem wAPSBOMItem : wOrderBomList) {
					wAPSBOMItem.DeleteID = "X";
					APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wAPSBOMItem, wErrorCode);
				}

				List<APSBOMItem> wDeleteList = CloneTool.CloneArray(wOrderBomList, APSBOMItem.class);

				wOrderBomList.forEach(p -> p.AssessmentType = "修复旧件");

				List<APSBOMItem> wInsertList = new ArrayList<APSBOMItem>();
				for (APSBOMItem wAPSBOMItem : wOrderBomList) {
					wAPSBOMItem.ID = 0;
					wAPSBOMItem.DeleteID = "";
					wAPSBOMItem.ID = APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wAPSBOMItem, wErrorCode);
					wInsertList.add(wAPSBOMItem);
				}

				wDeleteList.addAll(wInsertList);

				// ③构造推送json
				String wJson = ChangeToJson(wLoginUser, wDeleteList, "U");
				// ④推送数据
				if (StringUtils.isNotEmpty(wJson)) {
					OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

					String wReturnMsg = SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 0, null);
					if (wReturnMsg.contains("失败") || wReturnMsg.contains("E")) {
						wResult.FaultCode += wReturnMsg;
					}
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStepPlan>> APS_QueryStepPlanList(BMSEmployee wLoginUser, String wOrderIDs,
			String wPartIDs, int wAPSShfitPeriod) {
		ServiceResult<List<APSTaskStepPlan>> wResult = new ServiceResult<List<APSTaskStepPlan>>();
		wResult.Result = new ArrayList<APSTaskStepPlan>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			String[] wOrders = wOrderIDs.split(",");

			wResult.Result = APSTaskStepPlanDAO.getInstance().SelectList(wLoginUser, wOrderIDs, wPartIDs,
					wAPSShfitPeriod, wErrorCode);

			List<Integer> wOrderIDList = new ArrayList<Integer>();
			String[] wStrs = wOrders;
			for (String wStr : wStrs) {
				int wID = StringUtils.parseInt(wStr);
				wOrderIDList.add(wID);
			}

			List<Integer> wPartIDList = new ArrayList<Integer>();
			wStrs = wPartIDs.split(",");
			for (String wStr : wStrs) {
				int wID = StringUtils.parseInt(wStr);
				wPartIDList.add(wID);
			}

			List<StepPlanTableTemp> wTableInfoMap = new ArrayList<StepPlanTableTemp>();
			List<StepPlanTableTemp> wOrderColumnMap = new ArrayList<StepPlanTableTemp>();

			List<FPCPartPoint> wStepList = APSConstans.GetFPCPartPointList().values().stream()
					.collect(Collectors.toList());

			// 订单排序
			List<OMSOrder> wOList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);
//			wOList.sort(Comparator.comparing(OMSOrder::getRealReceiveDate));
			wOList.sort(Comparator.comparing(OMSOrder::getERPID).thenComparing(OMSOrder::getRealReceiveDate));
			// 结果排序
			wResult.Result = OrderResult(wOList, wResult.Result);

			for (int wOrderID : wOrderIDList) {
				for (int wPartID : wPartIDList) {
					// TableInfo
					StepPlanTableTemp wTableInfo = new StepPlanTableTemp();
					wTableInfo.OrderID = wOrderID;
					wTableInfo.PartID = wPartID;

					List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
					Map<String, Object> wHeadMap = new HashMap<String, Object>();
					List<FPCPart> wStationList = new ArrayList<FPCPart>();

					List<LFSWorkAreaStation> wWorkAreaStationList = new ArrayList<LFSWorkAreaStation>();

					List<APSTaskStepPlan> wTempList = wResult.Result.stream()
							.filter(p -> p.OrderID == wOrderID && p.PartID == wPartID).collect(Collectors.toList());

					// 工序排序
					wTempList = OrderTempList(wTempList);

					for (APSTaskStepPlan wItem : wTempList) {
						FPCPart wPart = new FPCPart();
						if (wStepList.stream().anyMatch(p -> p.ID == wItem.StepID)) {
							FPCPartPoint wStep = wStepList.stream().filter(p -> p.ID == wItem.StepID).findFirst().get();
							wPart.ID = wStep.ID;
							wPart.Name = wStep.Name;
							wPart.FactoryID = wItem.OrderNum;
						}
						wHeadMap.put("Station_" + String.valueOf(wItem.StepID), wPart.Name);
						wStationList.add(wPart);

						LFSWorkAreaStation wLFSWorkAreaStation = new LFSWorkAreaStation();
						wLFSWorkAreaStation.StationID = wPart.ID;
						wLFSWorkAreaStation.StationName = wPart.Name;
						wWorkAreaStationList.add(wLFSWorkAreaStation);
					}

					List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser,
							new ArrayList<Integer>(Arrays.asList(wOrderID)), wErrorCode);

					List<APSTaskPart> wTaskPartList = new ArrayList<APSTaskPart>();
					for (APSTaskStepPlan wAPSTaskStepPlan : wResult.Result) {
						APSTaskPart wAPSTaskPart = new APSTaskPart();
						wAPSTaskPart.OrderID = wAPSTaskStepPlan.OrderID;
						wAPSTaskPart.PartID = wAPSTaskStepPlan.StepID;
						wAPSTaskPart.StartTime = wAPSTaskStepPlan.StartTime;
						wAPSTaskPart.EndTime = wAPSTaskStepPlan.EndTime;
						wTaskPartList.add(wAPSTaskPart);
					}

					wTableMap.add(wHeadMap);
					wTableMap.addAll(APSServiceImpl.getInstance().ChangeToTableByEndTime(wOrderList, wTaskPartList,
							wWorkAreaStationList).Result);

//					wStationList = new ArrayList<FPCPart>(wStationList.stream()
//							.collect(Collectors.toMap(FPCPart::getID, account -> account, (k1, k2) -> k2)).values());
					List<FPCPart> wNRList = new ArrayList<FPCPart>();
					for (FPCPart wFPCPart : wStationList) {
						if (!wNRList.stream().anyMatch(p -> p.ID == wFPCPart.ID)) {
							wNRList.add(wFPCPart);
						}
					}

					wTableInfo.Data = wTableMap;
					wTableInfoMap.add(wTableInfo);
					// OrderColumn
					StepPlanTableTemp wOrderColumn = new StepPlanTableTemp();
					wOrderColumn.OrderID = wOrderID;
					wOrderColumn.PartID = wPartID;
					wOrderColumn.Data = wNRList;
					wOrderColumnMap.add(wOrderColumn);
				}
			}

			// 表格数据返回
			wResult.CustomResult.put("TableInfoList", wTableInfoMap);
			wResult.CustomResult.put("OrderColumn", wOrderColumnMap);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 工序排序
	 */
	private List<APSTaskStepPlan> OrderTempList(List<APSTaskStepPlan> wTempList) {
		List<APSTaskStepPlan> wResult = wTempList;
		try {
			if (wTempList == null || wTempList.size() <= 0) {
				return wResult;
			}
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			// 查询是否设置了手动排序，若设置了，修改FactoryID
			List<FPCPart> wOList = APSManuCapacityStepDAO.getInstance().SelectListByOrder(BaseDAO.SysAdmin,
					wTempList.get(0).OrderID, wTempList.get(0).PartID, wErrorCode);
			if (wOList.stream().anyMatch(p -> p.FactoryID > 0)) {
				for (APSTaskStepPlan wAPSTaskStepPlan : wTempList) {
					if (!wOList.stream().anyMatch(p -> p.ID == wAPSTaskStepPlan.StepID)) {
						continue;
					}
					wAPSTaskStepPlan.OrderNum = wOList.stream().filter(p -> p.ID == wAPSTaskStepPlan.StepID).findFirst()
							.get().FactoryID;
				}
			}
			// 排序
			wResult.sort(Comparator.comparing(APSTaskStepPlan::getOrderNum));
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_AddAPSBOMToSAP(BMSEmployee wLoginUser, int wBOMID, int wOrderID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①查询新版标准BOM子项集合
			List<MSSBOMItem> wMSSBOMItemList = null;
			if (wBOMID > 0) {
				wMSSBOMItemList = WMSServiceImpl.getInstance()
						.MSS_QueryBOMItemAll(wLoginUser, wBOMID, -1, -1, -1, -1, -1, -1, -1, -1).List(MSSBOMItem.class);
			}
			if (wMSSBOMItemList == null || wMSSBOMItemList.size() <= 0) {
				wResult.FaultCode += "提示：查询标准BOM失败";
				return wResult;
			}
			// ②筛选出必换件、必修件
			wMSSBOMItemList = wMSSBOMItemList.stream().filter(p -> p.ReplaceType == 1 || p.OutsourceType == 1)
					.collect(Collectors.toList());
			// ③遍历查询订单是否存在，若不存在，加入待推送集合
			List<MSSBOMItem> wToSendList = new ArrayList<MSSBOMItem>();
			for (MSSBOMItem wMSSBOMItem : wMSSBOMItemList) {
				boolean wCheckResult = APSBOMItemDAO.getInstance().IsExist(wLoginUser, wOrderID, wMSSBOMItem.PlaceID,
						wMSSBOMItem.PartPointID, wMSSBOMItem.MaterialID, wMSSBOMItem.ReplaceType,
						wMSSBOMItem.OutsourceType, wErrorCode);
				if (!wCheckResult) {
					wToSendList.add(wMSSBOMItem);
				}
			}
			// ④推送给SAP
			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

			List<APSBOMItem> wResultList = new ArrayList<APSBOMItem>();

			APSBOMItem wBOMItem = null;

			for (MSSBOMItem wMSSBOMItem : wToSendList) {

				wBOMItem = new APSBOMItem(wMSSBOMItem, wOrder.LineID, wOrder.ProductID, wOrder.CustomerID, wOrder.ID,
						wOrder.OrderNo, wOrder.PartNo);

				wBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
				wBOMItem.SourceID = wMSSBOMItem.ID;
				wBOMItem.AuditorID = wLoginUser.ID;
				wBOMItem.EditorID = wLoginUser.ID;
				wBOMItem.AuditTime = Calendar.getInstance();
				wBOMItem.EditTime = Calendar.getInstance();

				wResultList.add(wBOMItem);
			}

			// 验证台车BOM
			String wMsg = CheckAPSBOM(wLoginUser, wResultList, wOrder);
			if (StringUtils.isNotEmpty(wMsg)) {

				// 发送创建台车bom的消息
				SendAPSBOMMessage(wLoginUser, wMsg, wOrder);

				// 返回提示信息
				wResult.FaultCode = wMsg;
				return wResult;
			}

			// 评估类型赋值
			wResultList.forEach(p -> {
				if (p.OutsourceType == 1) {
					p.AssessmentType = "修复旧件";
				}
			});

			for (APSBOMItem wItem : wResultList) {
				wItem.ID = APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wItem, wErrorCode);
				wItem.ProductNo = wOrder.ProductNo;
				wItem.LineName = wOrder.LineName;
				wItem.CustomerCode = APSConstans.GetCRMCustomer(wItem.CustomerID).CustomerCode;
				wItem.PartPointName = APSConstans.GetFPCPartPointName(wItem.PartPointID);
			}

			String wSendSAP = Configuration.readConfigString("isSendSAP", "config/config");

			if (wSendSAP.equals("1")) {
				// ①推送台车BOM给SAP
				String wJson = ChangeToJson(wLoginUser, wResultList, "U");
				if (StringUtils.isNotEmpty(wJson)) {
					SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 0,
							wResultList.stream().map(p -> p.ID).collect(Collectors.toList()));
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_SaveStepPlan(BMSEmployee wLoginUser, List<APSTaskPart> wStepPlanList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wStepPlanList == null || wStepPlanList.size() <= 0) {
				return wResult;
			}

			List<Integer> wOrderIDList = wStepPlanList.stream().map(p -> p.OrderID).distinct()
					.collect(Collectors.toList());
			List<Integer> wPartIDList = wStepPlanList.stream().map(p -> p.RealPartID).distinct()
					.collect(Collectors.toList());

			for (int wOrderID : wOrderIDList) {
				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

				for (int wPartID : wPartIDList) {

					List<APSTaskPart> wToSaveList = wStepPlanList.stream()
							.filter(p -> p.OrderID == wOrderID && p.RealPartID == wPartID).collect(Collectors.toList());
					if (wToSaveList == null || wToSaveList.size() <= 0) {
						continue;
					}

					// ①转换为工序计划结构
					/**
					 * 遍历保存工序计划
					 */
					SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");
					int wShiftID = Integer.parseInt(wSDF.format(Calendar.getInstance().getTime()));
					List<APSTaskStepPlan> wList = new ArrayList<APSTaskStepPlan>();
					for (APSTaskPart wAPSTaskPart : wToSaveList) {
						APSTaskStepPlan wPlan = new APSTaskStepPlan(0, wAPSTaskPart.OrderID, wOrder.PartNo, 0,
								wOrder.LineID, wPartID, wAPSTaskPart.PartID, Calendar.getInstance(),
								Calendar.getInstance(), wShiftID, wAPSTaskPart.StartTime, wAPSTaskPart.EndTime, 1, 1,
								"", wOrder.ProductNo, "", "", "", wLoginUser.ID, "", wAPSTaskPart.TaskText, "");
						wList.add(wPlan);
					}
					List<Integer> wStepIDList = wToSaveList.stream().map(p -> p.PartID).distinct()
							.collect(Collectors.toList());
					// ②查询存在的工序计划
					List<APSTaskStepPlan> wEList = APSTaskStepPlanDAO.getInstance().SelectList(wLoginUser, wOrder.ID,
							wPartID, wStepIDList, wStepPlanList.get(0).ShiftPeriod, wErrorCode);
					// ③删除存在的工序计划
					if (wEList.size() > 0) {
						APSTaskStepPlanDAO.getInstance().DeleteList(wLoginUser, wEList, wErrorCode);
					}
					// ④遍历保存工序计划
					for (APSTaskStepPlan wAPSTaskStepPlan : wList) {
						wAPSTaskStepPlan.APSShiftPeriod = wStepPlanList.get(0).ShiftPeriod;
						APSTaskStepPlanDAO.getInstance().Update(wLoginUser, wAPSTaskStepPlan, wErrorCode);
					}
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<FPCRoute>> ASP_QueryRouteList(BMSEmployee wLoginUser, List<Integer> wRouteIDList,
			int wPartID) {
		ServiceResult<List<FPCRoute>> wResult = new ServiceResult<List<FPCRoute>>();
		wResult.Result = new ArrayList<FPCRoute>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			if (wRouteIDList == null || wRouteIDList.size() <= 0) {
				return wResult;
			}

			for (Integer wRouteID : wRouteIDList) {
				FPCRoute wRoute = FMCServiceImpl.getInstance().FPC_QueryRouteByID(wLoginUser, wRouteID)
						.Info(FPCRoute.class);

				List<FPCRoutePartPoint> wList = FMCServiceImpl.getInstance()
						.FPC_QueryRoutePartPointListByRouteID(wLoginUser, wRouteID, wPartID)
						.List(FPCRoutePartPoint.class);

				List<FPCRoutePart> wRoutePartList = new ArrayList<FPCRoutePart>();
				for (FPCRoutePartPoint wFPCRoutePartPoint : wList) {
					FPCRoutePart wFPCRoutePart = new FPCRoutePart();

					wFPCRoutePart.ID = wFPCRoutePartPoint.ID;
					wFPCRoutePart.RouteID = wFPCRoutePartPoint.RouteID;
					wFPCRoutePart.PartID = wFPCRoutePartPoint.PartPointID;
					wFPCRoutePart.PrevPartID = wFPCRoutePartPoint.PrevStepID;
					wFPCRoutePart.NextPartIDMap = wFPCRoutePartPoint.NextStepIDMap;

					wRoutePartList.add(wFPCRoutePart);
				}

				wRoute.PartList = wRoutePartList;

				wResult.Result.add(wRoute);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void APS_CancelDeleteX(BMSEmployee wLoginUser, int wBOMItemID) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().SelectByIDList(wLoginUser,
					new ArrayList<Integer>(Arrays.asList(wBOMItemID)), wErrorCode);

			for (APSBOMItem wAPSBOMItem : wList) {
				wAPSBOMItem.DeleteID = "";
				wAPSBOMItem.EditTime = Calendar.getInstance();

				APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, wAPSBOMItem, wErrorCode);
			}

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wList.get(0).OrderID, wErrorCode);

			String wJson = ChangeToJson(wLoginUser, wList, "U");
			if (StringUtils.isNotEmpty(wJson)) {
				SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 0, null);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public void SendSingleMaterial(BMSEmployee wLoginUser, int wBOMID, List<Integer> wBOMItemIDList,
			List<Integer> wOrderIDList) {
		try {
			List<MSSBOMItem> wMSSBOMItemList = null;
			if (wBOMID > 0) {
				wMSSBOMItemList = WMSServiceImpl.getInstance()
						.MSS_QueryBOMItemAll(wLoginUser, wBOMID, -1, -1, -1, -1, -1, -1, -1, -1).List(MSSBOMItem.class);
				wMSSBOMItemList = wMSSBOMItemList.stream().filter(p -> wBOMItemIDList.stream().anyMatch(q -> q == p.ID))
						.collect(Collectors.toList());
			}

			if (wMSSBOMItemList == null || wMSSBOMItemList.size() <= 0) {
				return;
			}

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			for (int wOrderID : wOrderIDList) {
				OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);

				List<APSBOMItem> wResultList = new ArrayList<APSBOMItem>();

				APSBOMItem wBOMItem = null;

				for (MSSBOMItem wMSSBOMItem : wMSSBOMItemList) {

					if (wMSSBOMItem.ReplaceType == 1 || wMSSBOMItem.OutsourceType == 1) {

						wBOMItem = new APSBOMItem(wMSSBOMItem, wOrder.LineID, wOrder.ProductID, wOrder.CustomerID,
								wOrder.ID, wOrder.OrderNo, wOrder.PartNo);

						wBOMItem.SourceType = APSBOMSourceType.StandardBOM.getValue();
						wBOMItem.SourceID = wMSSBOMItem.ID;
						wBOMItem.AuditorID = wLoginUser.ID;
						wBOMItem.EditorID = wLoginUser.ID;
						wBOMItem.AuditTime = Calendar.getInstance();
						wBOMItem.EditTime = Calendar.getInstance();

						wResultList.add(wBOMItem);
					}
				}

				// 评估类型赋值
				wResultList.forEach(p -> {
					if (p.OutsourceType == 1) {
						p.AssessmentType = "修复旧件";
					}
				});

				APSServiceImpl.getInstance().APS_UpdateBOMItemList(wLoginUser, wResultList, 0);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<String> APS_DeleteListByImport(BMSEmployee wLoginUser, ExcelData result, int wOrderID) {
		ServiceResult<String> wResult = new ServiceResult<String>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①解析出ID集合
			List<Integer> wIDList = new ArrayList<Integer>();

			ExcelSheetData wSheet = result.sheetData.get(0);

			int wIndex = 0;
			for (ExcelLineData wExcelLineData : wSheet.lineData) {
				if (wIndex == 0) {
					wIndex++;
					continue;
				}
				wIDList.add(StringUtils.parseInt(wExcelLineData.colData.get(0).replace(".0", "")));
			}
			// ②添加删除标记
			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().SelectByIDList(wLoginUser, wIDList, wErrorCode);
			for (APSBOMItem apsbomItem : wList) {
				apsbomItem.DeleteID = "X";
				APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, apsbomItem, wErrorCode);
			}

			APSServiceImpl.getInstance().APS_UpdateBOMItemList(wLoginUser, wList, 0);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void APS_ChangeNumber(BMSEmployee wLoginUser, int wOrderID, int wAPSBOMID) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wIDList = new ArrayList<Integer>(Arrays.asList(wAPSBOMID));
			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().SelectByIDList(wLoginUser, wIDList, wErrorCode);

			for (APSBOMItem apsbomItem : wList) {
				apsbomItem.Number = 2.0;
				APSBOMItemDAO.getInstance().APS_UpdateBOMItem(wLoginUser, apsbomItem, wErrorCode);
			}

			APSServiceImpl.getInstance().APS_UpdateBOMItemList(wLoginUser, wList, 0);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<List<APSTaskPart>> APS_StepPlanMore(BMSEmployee wLoginUser, List<APSStepPlanParam> wList) {
		ServiceResult<List<APSTaskPart>> wResult = new ServiceResult<List<APSTaskPart>>();
		wResult.Result = new ArrayList<APSTaskPart>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			mFPCCapacityStepTempList = new ArrayList<FPCCapacityStepTemp>();
			mFPCRoutePartTempList = new ArrayList<FPCRoutePartTemp>();
			mFPCPartListTemp = new ArrayList<FPCPartTemp>();

			List<StepPlanTableTemp> wTableInfoMap = new ArrayList<StepPlanTableTemp>();
			List<StepPlanTableTemp> wOrderColumnMap = new ArrayList<StepPlanTableTemp>();
			for (APSStepPlanParam wAPSStepPlanParam : wList) {
				ServiceResult<List<APSTaskPart>> wRst = APSServiceImpl.getInstance().APS_AutoSchedulingStep(wLoginUser,
						wAPSStepPlanParam.OrderID, APSShiftPeriod.getEnumType(wAPSStepPlanParam.APSShiftPeriod),
						wAPSStepPlanParam.StartTime, wAPSStepPlanParam.EndTime, wAPSStepPlanParam.WorkDay,
						wAPSStepPlanParam.LimitMinutes, wAPSStepPlanParam.MutualTaskList, wAPSStepPlanParam.PartID);
				wRst.Result.forEach(p -> p.RealPartID = wAPSStepPlanParam.PartID);

				Object wTableInfoList = wRst.CustomResult.get("TableInfoList");
				Object wOrderColumn = wRst.CustomResult.get("OrderColumn");

				StepPlanTableTemp wTableInfoTemp = new StepPlanTableTemp();
				wTableInfoTemp.OrderID = wAPSStepPlanParam.OrderID;
				wTableInfoTemp.PartID = wAPSStepPlanParam.PartID;
				wTableInfoTemp.Data = wTableInfoList;
				wTableInfoMap.add(wTableInfoTemp);

				StepPlanTableTemp wOrderColumnTemp = new StepPlanTableTemp();
				wOrderColumnTemp.OrderID = wAPSStepPlanParam.OrderID;
				wOrderColumnTemp.PartID = wAPSStepPlanParam.PartID;
				wOrderColumnTemp.Data = wOrderColumn;
				wOrderColumnMap.add(wOrderColumnTemp);

				wResult.Result.addAll(wRst.Result);
			}

			String wOrderIDs = StringUtils.Join(",", wList.stream().map(p -> p.OrderID).collect(Collectors.toList()));
			String wPartIDs = StringUtils.Join(",", wList.stream().map(p -> p.PartID).collect(Collectors.toList()));

			List<FPCRoutePartPoint> wRouteList = FPCRouteDAO.getInstance().SelectRoutePartPointList(wLoginUser,
					wOrderIDs, wPartIDs, wErrorCode);

			wResult.CustomResult.put("TableInfoListMap", wTableInfoMap);
			wResult.CustomResult.put("OrderColumnMap", wOrderColumnMap);
			wResult.CustomResult.put("RouteList", wRouteList);

			mFPCCapacityStepTempList.clear();
			mFPCRoutePartTempList.clear();
			mFPCPartListTemp.clear();

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_QueryBOMID(BMSEmployee wLoginUser, int wSourceID, int wType,
			String wCustomerCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wType == APSBOMSourceType.StandardBOM.getValue()) {
				MSSBOM wBOM = SFCBOMTaskDAO.getInstance().APS_QueryBOMID(wLoginUser, wSourceID, wErrorCode);
				wResult.CustomResult.put("RouteID", wBOM.RouteID);
				wResult.CustomResult.put("BomID", wBOM.ID);
				wResult.CustomResult.put("BomName", wBOM.BOMName);
			} else if (wType == APSBOMSourceType.BOMChange.getValue()) {
				TCMMaterialChangeItems wItems = SFCBOMTaskDAO.getInstance().TCM_QueryChangeItems(wLoginUser, wSourceID,
						wErrorCode);
				if (StringUtils.isNotEmpty(wItems.BOMNo2)) {
					String[] wStrs = wItems.BOMNo2.split(",");
					for (String wStr : wStrs) {
						if (!wStr.contains(wCustomerCode)) {
							continue;
						}

						MSSBOM wBOM = SFCBOMTaskDAO.getInstance().MSS_QueryIDByNo(wLoginUser, wStr, wErrorCode);
						wResult.CustomResult.put("RouteID", wBOM.RouteID);
						wResult.CustomResult.put("BomID", wBOM.ID);
						wResult.CustomResult.put("BomName", wBOM.BOMName);
					}
				} else {
					wResult.CustomResult.put("RouteID", 0);
					wResult.CustomResult.put("BomID", 0);
					wResult.CustomResult.put("BomName", "");
				}
			} else if (wType == APSBOMSourceType.SFCBOMTask.getValue()) {
				int wSFCBOMTaskID = SFCBOMTaskItemDAO.getInstance().SelectByID(wLoginUser, wSourceID,
						wErrorCode).SFCBOMTaskID;
				wResult.CustomResult.put("RouteID", 0);
				wResult.CustomResult.put("BomID", wSFCBOMTaskID);
				wResult.CustomResult.put("BomName", "");
			} else {
				wResult.CustomResult.put("RouteID", 0);
				wResult.CustomResult.put("BomID", 0);
				wResult.CustomResult.put("BomName", "");
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStepPlan>> APS_QueryStepPlanCompare(BMSEmployee wLoginUser, String wOrderIDs,
			String wPartIDs, int wAPSShiftPeriod) {
		ServiceResult<List<APSTaskStepPlan>> wResult = new ServiceResult<List<APSTaskStepPlan>>();
		wResult.Result = new ArrayList<APSTaskStepPlan>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			String[] wOrders = wOrderIDs.split(",");

			// 根据订单、工位查询实际的工序计划
			List<APSTaskStep> wRealTaskStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, wOrderIDs,
					wPartIDs, wErrorCode);

			wResult.Result = APSTaskStepPlanDAO.getInstance().SelectList(wLoginUser, wOrderIDs, wPartIDs,
					wAPSShiftPeriod, wErrorCode);

			// 查询周计划
			List<APSTaskPart> wTaskPartList1 = APSTaskPartDAO.getInstance().SelectListByOrdersAndPartIDs(wLoginUser,
					wOrderIDs, wPartIDs, wErrorCode);
			wResult.Result.removeIf(
					p -> !wTaskPartList1.stream().anyMatch(q -> q.OrderID == p.OrderID && p.PartID == q.PartID));

			// 赋值真实的时间
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			for (APSTaskStepPlan wAPSTaskStepPlan : wResult.Result) {
				if (wRealTaskStepList.stream().anyMatch(p -> p.OrderID == wAPSTaskStepPlan.OrderID
						&& p.PartID == wAPSTaskStepPlan.PartID && p.StepID == wAPSTaskStepPlan.StepID)) {
					APSTaskStep wTaskStep = wRealTaskStepList
							.stream().filter(p -> p.OrderID == wAPSTaskStepPlan.OrderID
									&& p.PartID == wAPSTaskStepPlan.PartID && p.StepID == wAPSTaskStepPlan.StepID)
							.findFirst().get();
					wAPSTaskStepPlan.RealStartTime = wTaskStep.StartTime;
					wAPSTaskStepPlan.RealEndTime = wTaskStep.EndTime;
					wAPSTaskStepPlan.Status = wTaskStep.Status;
				} else {
					wAPSTaskStepPlan.RealStartTime = wBaseTime;
					wAPSTaskStepPlan.RealEndTime = wBaseTime;
				}
			}

			List<Integer> wOrderIDList = new ArrayList<Integer>();
			String[] wStrs = wOrders;
			for (String wStr : wStrs) {
				int wID = StringUtils.parseInt(wStr);
				wOrderIDList.add(wID);
			}

			List<Integer> wPartIDList = new ArrayList<Integer>();
			wStrs = wPartIDs.split(",");
			for (String wStr : wStrs) {
				int wID = StringUtils.parseInt(wStr);
				wPartIDList.add(wID);
			}

			List<StepPlanTableTemp> wTableInfoMap = new ArrayList<StepPlanTableTemp>();
			List<StepPlanTableTemp> wOrderColumnMap = new ArrayList<StepPlanTableTemp>();

			List<FPCPartPoint> wStepList = APSConstans.GetFPCPartPointList().values().stream()
					.collect(Collectors.toList());

			// 订单排序
			List<OMSOrder> wOList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode);
//			wOList.sort(Comparator.comparing(OMSOrder::getRealReceiveDate));
			wOList.sort(Comparator.comparing(OMSOrder::getERPID).thenComparing(OMSOrder::getRealReceiveDate));
			// 结果排序
			wResult.Result = OrderResult(wOList, wResult.Result);

			for (int wOrderID : wOrderIDList) {
				for (int wPartID : wPartIDList) {
					// TableInfo
					StepPlanTableTemp wTableInfo = new StepPlanTableTemp();
					wTableInfo.OrderID = wOrderID;
					wTableInfo.PartID = wPartID;

					List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
					Map<String, Object> wHeadMap = new HashMap<String, Object>();
					List<FPCPart> wStationList = new ArrayList<FPCPart>();

					List<LFSWorkAreaStation> wWorkAreaStationList = new ArrayList<LFSWorkAreaStation>();

					List<APSTaskStepPlan> wTempList = wResult.Result.stream()
							.filter(p -> p.OrderID == wOrderID && p.PartID == wPartID).collect(Collectors.toList());

					// 工序排序
					wTempList = OrderTempList(wTempList);

					for (APSTaskStepPlan wItem : wTempList) {
						FPCPart wPart = new FPCPart();
						if (wStepList.stream().anyMatch(p -> p.ID == wItem.StepID)) {
							FPCPartPoint wStep = wStepList.stream().filter(p -> p.ID == wItem.StepID).findFirst().get();
							wPart.ID = wStep.ID;
							wPart.Name = wStep.Name;
							wPart.FactoryID = wItem.OrderNum;
						}
						wHeadMap.put("Station_" + String.valueOf(wItem.StepID), wPart.Name);
						wStationList.add(wPart);

						LFSWorkAreaStation wLFSWorkAreaStation = new LFSWorkAreaStation();
						wLFSWorkAreaStation.StationID = wPart.ID;
						wLFSWorkAreaStation.StationName = wPart.Name;
						wWorkAreaStationList.add(wLFSWorkAreaStation);
					}

					List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser,
							new ArrayList<Integer>(Arrays.asList(wOrderID)), wErrorCode);

					List<APSTaskPart> wTaskPartList = new ArrayList<APSTaskPart>();
					for (APSTaskStepPlan wAPSTaskStepPlan : wResult.Result) {
						APSTaskPart wAPSTaskPart = new APSTaskPart();
						wAPSTaskPart.OrderID = wAPSTaskStepPlan.OrderID;
						wAPSTaskPart.PartID = wAPSTaskStepPlan.StepID;
						wAPSTaskPart.StartTime = wAPSTaskStepPlan.StartTime;
						wAPSTaskPart.EndTime = wAPSTaskStepPlan.EndTime;
						wAPSTaskPart.Status = wAPSTaskStepPlan.Status;
						wAPSTaskPart.FinishWorkTime = wAPSTaskStepPlan.RealEndTime;
						wTaskPartList.add(wAPSTaskPart);
					}

					wTableMap.add(wHeadMap);
					wTableMap.addAll(APSServiceImpl.getInstance().ChangeToTableByEndTime(wOrderList, wTaskPartList,
							wWorkAreaStationList).Result);

					List<FPCPart> wNRList = new ArrayList<FPCPart>();
					for (FPCPart wFPCPart : wStationList) {
						if (!wNRList.stream().anyMatch(p -> p.ID == wFPCPart.ID)) {
							wNRList.add(wFPCPart);
						}
					}

					wTableInfo.Data = wTableMap;
					wTableInfoMap.add(wTableInfo);
					// OrderColumn
					StepPlanTableTemp wOrderColumn = new StepPlanTableTemp();
					wOrderColumn.OrderID = wOrderID;
					wOrderColumn.PartID = wPartID;
					wOrderColumn.Data = wNRList;
					wOrderColumnMap.add(wOrderColumn);
				}
			}

			// 表格数据返回
			wResult.CustomResult.put("TableInfoList", wTableInfoMap);
			wResult.CustomResult.put("OrderColumn", wOrderColumnMap);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 按照订单的顺序排序
	 */
	private List<APSTaskStepPlan> OrderResult(List<OMSOrder> wOList, List<APSTaskStepPlan> wAPSTaskStepPlanList) {
		List<APSTaskStepPlan> wResult = new ArrayList<APSTaskStepPlan>();
		try {
			if (wAPSTaskStepPlanList == null || wAPSTaskStepPlanList.size() <= 0) {
				return wResult;
			}

			for (OMSOrder wOMSOrder : wOList) {
				List<APSTaskStepPlan> wList = wAPSTaskStepPlanList.stream().filter(p -> p.OrderID == wOMSOrder.ID)
						.collect(Collectors.toList());
				if (wList != null && wList.size() > 0) {
					wResult.addAll(wList);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_SendSingleMaterial(BMSEmployee wLoginUser, int wBOMID, String wOrders,
			String wItemIDs) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<Integer> wOrderIDList = new ArrayList<Integer>();
			String[] wStrs = wOrders.split(",");
			for (String wStr : wStrs) {
				int wID = StringUtils.parseInt(wStr);
				wOrderIDList.add(wID);
			}

			List<Integer> wBOMItemIDList = new ArrayList<Integer>();
			wStrs = wItemIDs.split(",");
			for (String wStr : wStrs) {
				int wID = StringUtils.parseInt(wStr);
				wBOMItemIDList.add(wID);
			}

			APSServiceImpl.getInstance().SendSingleMaterial(wLoginUser, wBOMID, wBOMItemIDList, wOrderIDList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> MCS_QueryIDByBOMID(BMSEmployee wLoginUser, int wBOMID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = MCSLogInfoDAO.getInstance().MCS_QueryIDByBOMID(wLoginUser, wBOMID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> MCS_QueryIDByBOPID(BMSEmployee wLoginUser, int wBOPID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = MCSLogInfoDAO.getInstance().MCS_QueryIDByBOPID(wLoginUser, wBOPID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<MCSLogInfo> MCS_QueryLogInfo(BMSEmployee wLoginUser, int wID) {
		ServiceResult<MCSLogInfo> wResult = new ServiceResult<MCSLogInfo>();
		wResult.Result = new MCSLogInfo();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = MCSLogInfoDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_ReSendBOMByIDList(BMSEmployee wLoginUser, List<Integer> wIDList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBOMItem> wList = APSBOMItemDAO.getInstance().SelectByIDList(wLoginUser, wIDList, wErrorCode);

			String wSendSAP = Configuration.readConfigString("isSendSAP", "config/config");

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wList.get(0).OrderID, wErrorCode);

			if (wSendSAP.equals("1")) {
				// ①推送台车BOM给SAP
				String wJson = ChangeToJson(wLoginUser, wList, "U");
				List<Integer> wAPSBOMIDList = wList.stream().map(p -> p.ID).distinct().collect(Collectors.toList());
				if (StringUtils.isNotEmpty(wJson)) {
					SendToSAP(wLoginUser, wJson, wOrder.OrderNo, 0, wOrder, 0, wAPSBOMIDList);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> OMS_SynchronizePartNo(BMSEmployee wLoginUser, int wOrderID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wOrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0) {
				wResult.FaultCode += "提示：订单不存在!";
				return wResult;
			}

			SendToSAP(BaseDAO.SysAdmin, wOrder);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_SynchronizedStepPlan(BMSEmployee wLoginUser, int wPartID, String wOrders) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			String[] wStrs = wOrders.split(",");
			for (String wStr : wStrs) {
				int wOrderID = StringUtils.parseInt(wStr);

				List<APSTaskStep> wTaskStepList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, -1,
						-1, -1, -1, wPartID, -1, -1, 1, null, null, null, null, wErrorCode);
				List<APSTaskStepPlan> wPlanList = APSTaskStepPlanDAO.getInstance().SelectList(wLoginUser, -1, wOrderID,
						-1, -1, wPartID, -1, -1, -1, -1, APSShiftPeriod.Week.getValue(), wErrorCode);
				// ①查找删除的工序
				List<APSTaskStepPlan> wDeleteList = wPlanList.stream()
						.filter(p -> !wTaskStepList.stream().anyMatch(q -> q.StepID == p.StepID))
						.collect(Collectors.toList());
				APSTaskStepPlanDAO.getInstance().DeleteList(wLoginUser, wDeleteList, wErrorCode);
				// ②查找新增的工序
				List<APSTaskStep> wAddedList = wTaskStepList.stream()
						.filter(p -> !wPlanList.stream().anyMatch(q -> q.StepID == p.StepID))
						.collect(Collectors.toList());
				for (APSTaskStep wAPSTaskStep : wAddedList) {
					APSTaskStepPlan wPlan = wPlanList.get(0);
					wPlan.ID = 0;
					wPlan.StepID = wAPSTaskStep.StepID;
					wPlan.StartTime = wAPSTaskStep.PlanStartTime;
					wPlan.EndTime = wAPSTaskStep.PlanEndTime;
					APSTaskStepPlanDAO.getInstance().Update(wLoginUser, wPlan, wErrorCode);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BMSWorkChargeItem>> BMS_QueryWorkChargeItemList(BMSEmployee wLoginUser,
			int wWorkChargeID) {
		ServiceResult<List<BMSWorkChargeItem>> wResult = new ServiceResult<List<BMSWorkChargeItem>>();
		wResult.Result = new ArrayList<BMSWorkChargeItem>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = BMSWorkChargeItemDAO.getInstance().SelectList(wLoginUser, -1, wWorkChargeID, -1,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> BMS_DeleteWorkChargeItemList(BMSEmployee wLoginUser, List<BMSWorkChargeItem> wList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			BMSWorkChargeItemDAO.getInstance().DeleteList(wLoginUser, wList, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> BMS_SaveWorkChargeItemList(BMSEmployee wLoginUser, List<BMSWorkChargeItem> wList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			for (BMSWorkChargeItem bmsWorkChargeItem : wList) {
				List<BMSWorkChargeItem> wHisList = BMSWorkChargeItemDAO.getInstance().SelectList(wLoginUser, -1,
						bmsWorkChargeItem.WorkChargeID, bmsWorkChargeItem.ProductID, wErrorCode);
				if (wHisList.size() > 0) {
					continue;
				}
				bmsWorkChargeItem.ID = 0;
				if (bmsWorkChargeItem.WorkChargeID <= 0 || bmsWorkChargeItem.ProductID <= 0) {
					continue;
				}
				BMSWorkChargeItemDAO.getInstance().Update(wLoginUser, bmsWorkChargeItem, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStepCancelLog>> APS_QueryTaskStepCancelLogList(BMSEmployee wLoginUser,
			int wOrderID, int wPartID, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<APSTaskStepCancelLog>> wResult = new ServiceResult<List<APSTaskStepCancelLog>>();
		wResult.Result = new ArrayList<APSTaskStepCancelLog>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = APSTaskStepCancelLogDAO.getInstance().SelectList(wLoginUser, -1, wOrderID, wPartID, -1, -1,
					wStartTime, wEndTime, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_SaveStepCancelLog(BMSEmployee wLoginUser, List<APSTaskStep> wList,
			int wCancelType, String wCancelTypeName, String wRemark) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			if (wList == null || wList.size() <= 0) {
				wResult.FaultCode += "提示：参数错误!";
				return wResult;
			}

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①修改工序任务状态
			for (APSTaskStep wAPSTaskStep : wList) {
				wAPSTaskStep.Status = 5;
				wAPSTaskStep.StartTime = Calendar.getInstance();
				wAPSTaskStep.EndTime = Calendar.getInstance();
				APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
				// 删除派工记录
				List<SFCTaskStep> wSFCTaskStepList = SFCTaskStepDAO.getInstance().SelectList(wLoginUser, -1,
						wAPSTaskStep.ID, -1, -1, -1, null, -1, wErrorCode);
				SFCTaskStepDAO.getInstance().DeleteList(wLoginUser, wSFCTaskStepList, wErrorCode);
				// 禁用派工消息
				for (SFCTaskStep wSFCTaskStep : wSFCTaskStepList) {
					SFCTaskStepDAO.getInstance().DisableDispatchMessage(wLoginUser, wSFCTaskStep.ID, wErrorCode);
				}
			}

			// ②生成工序任务取消记录
			List<String> wStepNameList = wList.stream().map(p -> p.StepName).distinct().collect(Collectors.toList());
			List<Integer> wStepIDList = wList.stream().map(p -> p.StepID).distinct().collect(Collectors.toList());

			APSTaskStepCancelLog wLog = new APSTaskStepCancelLog(0, wList.get(0).OrderID, wList.get(0).PartNo,
					wList.get(0).PartID, wList.get(0).PartName, StringUtils.Join(",", wStepIDList),
					StringUtils.Join(",", wStepNameList), wCancelType, wCancelTypeName, wRemark, wLoginUser.ID,
					wLoginUser.Name, Calendar.getInstance());
			APSTaskStepCancelLogDAO.getInstance().Update(wLoginUser, wLog, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSTaskStepCancelLog> APS_QueryTaskStepCancelLogInfo(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSTaskStepCancelLog> wResult = new ServiceResult<APSTaskStepCancelLog>();
		wResult.Result = new APSTaskStepCancelLog();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = APSTaskStepCancelLogDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_CancelTaskStepPlan(BMSEmployee wLoginUser, List<APSTaskStep> wDataList) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wDataList == null || wDataList.size() <= 0) {
				return wResult;
			}

			for (APSTaskStep wAPSTaskStep : wDataList) {
				// ②撤销当前工序
				wAPSTaskStep.Status = 1;
				APSTaskStepDAO.getInstance().Update(wLoginUser, wAPSTaskStep, wErrorCode);
				// ①获取后续工序ID集合
				List<Integer> wStepIDList = GetNextStepIDList(wLoginUser, wAPSTaskStep);
				// ③查询后续工序
				for (int wStepID : wStepIDList) {
					List<APSTaskStep> wList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1,
							wAPSTaskStep.OrderID, -1, wAPSTaskStep.TaskPartID, -1, wAPSTaskStep.LineID,
							wAPSTaskStep.PartID, wStepID, -1, 1, null, null, null, null, wErrorCode);
					// ④遍历撤销后续工序
					for (APSTaskStep apsTaskStep : wList) {
						apsTaskStep.Status = 1;
						APSTaskStepDAO.getInstance().Update(wLoginUser, apsTaskStep, wErrorCode);
					}
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取后续工序
	 */
	private List<Integer> GetNextStepIDList(BMSEmployee wLoginUser, APSTaskStep wAPSTaskStep) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			OMSOrder wOrder = OMSOrderDAO.getInstance().SelectByID(wLoginUser, wAPSTaskStep.OrderID, wErrorCode);
			if (wOrder == null || wOrder.ID <= 0 || wOrder.RouteID <= 0) {
				return wResult;
			}

			List<FPCRoutePartPoint> wRoutePartPointList = FMCServiceImpl.getInstance()
					.FPC_QueryRoutePartPointListByRouteID(wLoginUser, wOrder.RouteID, wAPSTaskStep.PartID)
					.List(FPCRoutePartPoint.class);
			wResult = GetNextStepID(wAPSTaskStep.StepID, wRoutePartPointList);
			// 去重
			wResult = wResult.stream().distinct().collect(Collectors.toList());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 递归获取后续工序
	 */
	private List<Integer> GetNextStepID(int wStepID, List<FPCRoutePartPoint> wRoutePartPointList) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			// ①前工序等于自己
			if (wRoutePartPointList.stream().anyMatch(p -> p.PrevStepID == wStepID)) {
				List<Integer> wPreveList = wRoutePartPointList.stream().filter(p -> p.PrevStepID == wStepID)
						.map(p -> p.PartPointID).distinct().collect(Collectors.toList());
				for (Integer wItemID : wPreveList) {
					if (!wResult.stream().anyMatch(p -> p.intValue() == wItemID.intValue())) {
						wResult.add(wItemID);
						// ③递归
						List<Integer> wList = GetNextStepID(wItemID, wRoutePartPointList);
						for (Integer wID : wList) {
							if (!wResult.stream().anyMatch(p -> p.intValue() == wID.intValue())) {
								wResult.add(wID);
							}
						}
					}
				}
			}
			// ②后工序map
			if (wRoutePartPointList.stream().anyMatch(p -> p.PartPointID == wStepID)) {
				FPCRoutePartPoint wRoutePartPoint = wRoutePartPointList.stream().filter(p -> p.PartPointID == wStepID)
						.findFirst().get();
				for (String wItem : wRoutePartPoint.NextStepIDMap.keySet()) {
					Integer wIntItem = StringUtils.parseInt(wItem);
					if (wIntItem > 0 && !wResult.stream().anyMatch(p -> p.intValue() == wIntItem.intValue())) {
						wResult.add(wIntItem);
						// ③递归
						List<Integer> wList = GetNextStepID(wIntItem, wRoutePartPointList);
						for (Integer wID : wList) {
							if (!wResult.stream().anyMatch(p -> p.intValue() == wID.intValue())) {
								wResult.add(wID);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_ActiveTaskPart(BMSEmployee wLoginUser, int wAPSSchedulingVersionBPMID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			APSSchedulingVersionBPM wTask = (APSSchedulingVersionBPM) APSSchedulingVersionBPMDAO.getInstance()
					.BPM_GetTaskInfo(wLoginUser, wAPSSchedulingVersionBPMID, "", wErrorCode);
			for (int wTaskPartID : wTask.TaskPartIDList) {
				APSTaskPart wTaskPart = APSTaskPartDAO.getInstance().SelectByID(wLoginUser, wTaskPartID, wErrorCode);
				if (wTaskPart.Active == 2) {
					wTaskPart.Active = 1;
					APSTaskPartDAO.getInstance().Update(wLoginUser, wTaskPart, wErrorCode);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSBomBPM> APS_QueryDefaultBomBPM(BMSEmployee wLoginUser, int wEventID) {
		ServiceResult<APSBomBPM> wResult = new ServiceResult<APSBomBPM>();
		wResult.Result = new APSBomBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSBomBPM> wList = APSBomBPMDAO.getInstance().SelectList(wLoginUser, -1, "", wLoginUser.ID, -1, -1, -1,
					-1, -1, null, null, new ArrayList<Integer>(Arrays.asList(0)), wErrorCode);
			if (wList.size() > 0) {
				APSBomBPMDAO.getInstance().DeleteList(wLoginUser, wList, wErrorCode);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<APSBomBPM> APS_CreateBomBPM(BMSEmployee wLoginUser, BPMEventModule wEventID) {
		ServiceResult<APSBomBPM> wResult = new ServiceResult<APSBomBPM>();
		wResult.Result = new APSBomBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// 删除状态为0的单据
			APSBomBPMDAO.getInstance().DeleteNoUseTask(wLoginUser, wErrorCode);

			wResult.Result.Code = APSBomBPMDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			wResult.Result.UpFlowID = wLoginUser.ID;
			wResult.Result.UpFlowName = wLoginUser.Name;
			wResult.Result.CreateTime = Calendar.getInstance();
			wResult.Result.SubmitTime = Calendar.getInstance();
			wResult.Result.ID = 0;
			wResult.Result.Status = APSBomBPMStatus.Default.getValue();
			wResult.Result.StatusText = "";
			wResult.Result.FlowType = wEventID.getValue();

			wResult.Result = (APSBomBPM) APSBomBPMDAO.getInstance().BPM_UpdateTask(wLoginUser, wResult.Result,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSBomBPM> APS_SubmitBomBPM(BMSEmployee wLoginUser, APSBomBPM wData) {
		ServiceResult<APSBomBPM> wResult = new ServiceResult<APSBomBPM>();
		wResult.Result = new APSBomBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wData.Status == 20) {
				wData.StatusText = "已完成";
			}

			if (wData.Status == 2) {
				// 删除子项列表
				List<APSBomBPMItem> wSubList = APSBomBPMItemDAO.getInstance().SelectList(wLoginUser, -1, wData.ID, 0,
						wErrorCode);
				if (wSubList.size() > 0) {
					APSBomBPMItemDAO.getInstance().DeleteList(wLoginUser, wSubList, wErrorCode);
				}

				wData.APSBomBPMItemList.forEach(p -> p.ID = 0);
			}

			wResult.Result = (APSBomBPM) APSBomBPMDAO.getInstance().BPM_UpdateTask(wLoginUser, wData, wErrorCode);

			// 推送物料
			if (wData.Status == 20) {

				if (wData.Type > 0) {
					switch (wData.Type) {
					case 1:// 新增
						APSBomBPMAdd(wLoginUser, wData, wErrorCode);
						break;
					case 2:// 修改
						APSBomBPMUpdate(wLoginUser, wData, wErrorCode);
						break;
					case 3:// 删除
						APSBomBPMDelete(wLoginUser, wData, wErrorCode);
						break;
					default:
						break;
					}
				} else {
					List<APSBomBPMItem> wList = APSBomBPMItemDAO.getInstance().SelectList(wLoginUser, -1, wData.ID, -1,
							wErrorCode);
					if (wList == null || wList.size() <= 0) {
						return wResult;
					}
					// 新增
					List<APSBomBPMItem> wAddList = wList.stream().filter(p -> p.Type == 1).collect(Collectors.toList());
					if (wAddList != null && wAddList.size() > 0) {
						APSBomBPMAdd(wLoginUser, wAddList, wErrorCode);
					}
					// 修改
					List<APSBomBPMItem> wUpdateList = wList.stream().filter(p -> p.Type == 2)
							.collect(Collectors.toList());
					if (wUpdateList != null && wUpdateList.size() > 0) {
						APSBomBPMUpdate(wLoginUser, wUpdateList, wErrorCode);
					}
					// 删除
					List<APSBomBPMItem> wDeleteList = wList.stream().filter(p -> p.Type == 3)
							.collect(Collectors.toList());
					if (wDeleteList != null && wDeleteList.size() > 0) {
						APSBomBPMDelete(wLoginUser, wDeleteList, wErrorCode);
					}
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 新增台车BOM审批推送
	 */
	private void APSBomBPMAdd(BMSEmployee wLoginUser, APSBomBPM wData, OutResult<Integer> wErrorCode) {
		try {
			// ①查询子项
			List<APSBomBPMItem> wList = APSBomBPMItemDAO.getInstance().SelectList(wLoginUser, -1, wData.ID, -1,
					wErrorCode);
			if (wList == null || wList.size() <= 0) {
				return;
			}
			// ②遍历新增
			for (APSBomBPMItem wAPSBomBPMItem : wList) {
				APSBOMItem wItem = CloneTool.Clone(wAPSBomBPMItem, APSBOMItem.class);
				wItem.ID = 0;
				wItem.SourceID = wData.ID;
				wItem.SourceType = APSBOMSourceType.BOMAudit.getValue();
				APSServiceImpl.getInstance().APS_UpdateBOMItem(wLoginUser, wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 新增台车BOM审批推送
	 */
	private void APSBomBPMAdd(BMSEmployee wLoginUser, List<APSBomBPMItem> wList, OutResult<Integer> wErrorCode) {
		try {
			// ②遍历新增
			for (APSBomBPMItem wAPSBomBPMItem : wList) {
				APSBOMItem wItem = CloneTool.Clone(wAPSBomBPMItem, APSBOMItem.class);
				wItem.ID = 0;
				wItem.SourceID = wAPSBomBPMItem.APSBomBPMID;
				wItem.SourceType = APSBOMSourceType.BOMAudit.getValue();
				APSServiceImpl.getInstance().APS_UpdateBOMItem(wLoginUser, wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 修改台车bom审批推送
	 */
	private void APSBomBPMUpdate(BMSEmployee wLoginUser, APSBomBPM wData, OutResult<Integer> wErrorCode) {
		try {
			// ①查询子项
			List<APSBomBPMItem> wList = APSBomBPMItemDAO.getInstance().SelectList(wLoginUser, -1, wData.ID, -1,
					wErrorCode);
			if (wList == null || wList.size() <= 0) {
				return;
			}
			// ②遍历修改
			for (APSBomBPMItem wAPSBomBPMItem : wList) {

				APSServiceImpl.getInstance().APS_BomItemPropertyChange(wLoginUser, wAPSBomBPMItem.APSBomItemID, "X", 0);

				APSBOMItem wItem = CloneTool.Clone(wAPSBomBPMItem, APSBOMItem.class);
				wItem.ID = 0;
				wItem.Number = wAPSBomBPMItem.NewNumber;
				wItem.ReplaceType = wAPSBomBPMItem.NewReplaceType;
				wItem.OutsourceType = wAPSBomBPMItem.NewOutsourceType;
				wItem.PartChange = wAPSBomBPMItem.NewPartChange;
				wItem.QTType = wAPSBomBPMItem.NewQTType;
				wItem.QTItemType = wAPSBomBPMItem.NewQTItemType;
				wItem.AssessmentType = wAPSBomBPMItem.NewAssessmentType;
				wItem.Remark = wAPSBomBPMItem.NewRemark;
				wItem.SourceID = wData.ID;
				wItem.SourceType = APSBOMSourceType.BOMAudit.getValue();

				APSServiceImpl.getInstance().APS_UpdateBOMItem(wLoginUser, wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 修改台车bom审批推送
	 */
	private void APSBomBPMUpdate(BMSEmployee wLoginUser, List<APSBomBPMItem> wList, OutResult<Integer> wErrorCode) {
		try {
			// ②遍历修改
			for (APSBomBPMItem wAPSBomBPMItem : wList) {

				APSServiceImpl.getInstance().APS_BomItemPropertyChange(wLoginUser, wAPSBomBPMItem.APSBomItemID, "X", 0);

				APSBOMItem wItem = CloneTool.Clone(wAPSBomBPMItem, APSBOMItem.class);
				wItem.ID = 0;
				wItem.Number = wAPSBomBPMItem.NewNumber;
				wItem.ReplaceType = wAPSBomBPMItem.NewReplaceType;
				wItem.OutsourceType = wAPSBomBPMItem.NewOutsourceType;
				wItem.PartChange = wAPSBomBPMItem.NewPartChange;
				wItem.QTType = wAPSBomBPMItem.NewQTType;
				wItem.QTItemType = wAPSBomBPMItem.NewQTItemType;
				wItem.AssessmentType = wAPSBomBPMItem.NewAssessmentType;
				wItem.Remark = wAPSBomBPMItem.NewRemark;
				wItem.SourceID = wAPSBomBPMItem.APSBomBPMID;
				wItem.SourceType = APSBOMSourceType.BOMAudit.getValue();

				APSServiceImpl.getInstance().APS_UpdateBOMItem(wLoginUser, wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 删除台车BOM审批推送
	 */
	private void APSBomBPMDelete(BMSEmployee wLoginUser, APSBomBPM wData, OutResult<Integer> wErrorCode) {
		try {
			// ①查询子项
			List<APSBomBPMItem> wList = APSBomBPMItemDAO.getInstance().SelectList(wLoginUser, -1, wData.ID, -1,
					wErrorCode);
			if (wList == null || wList.size() <= 0) {
				return;
			}
			// ②遍历删除
			for (APSBomBPMItem wAPSBomBPMItem : wList) {
				APSServiceImpl.getInstance().APS_BomItemPropertyChange(wLoginUser, wAPSBomBPMItem.APSBomItemID, "X", 0);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 删除台车BOM审批推送
	 */
	private void APSBomBPMDelete(BMSEmployee wLoginUser, List<APSBomBPMItem> wList, OutResult<Integer> wErrorCode) {
		try {
			// ②遍历删除
			for (APSBomBPMItem wAPSBomBPMItem : wList) {
				APSServiceImpl.getInstance().APS_BomItemPropertyChange(wLoginUser, wAPSBomBPMItem.APSBomItemID, "X", 0);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<APSBomBPM> APS_GetBomBPM(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSBomBPM> wResult = new ServiceResult<APSBomBPM>();
		wResult.Result = new APSBomBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = (APSBomBPM) APSBomBPMDAO.getInstance().BPM_GetTaskInfo(wLoginUser, wID, "", wErrorCode);

			// 查询通知信息
			List<BFCMessage> wList = APSBomBPMDAO.getInstance().SelectNoticeMessage(wLoginUser, wResult.Result.ID,
					wErrorCode);
			wResult.CustomResult.put("Notice", wList);

			wResult.Result.APSBomBPMItemList = APSBomBPMItemDAO.getInstance().SelectList(wLoginUser, -1,
					wResult.Result.ID, -1, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BPMTaskBase>> APS_QueryBomBPMEmployeeAll(BMSEmployee wLoginUser, int wTagTypes,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (TagTypes.getEnumType(wTagTypes)) {
			case Applicant:// 2发起
				wResult.Result = APSBomBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID, wStartTime,
						wEndTime, wErrorCode);
				break;
			case Dispatcher:// 1待做
				wResult.Result = APSBomBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID, wErrorCode);
				break;
			case Approver:// 4已做
				wResult.Result = APSBomBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID, wStartTime,
						wEndTime, wErrorCode);
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}

			// 排序
			wResult.Result.sort(Comparator.comparing(BPMTaskBase::getCreateTime, Comparator.reverseOrder()));

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSBomBPM>> APS_QueryBomBPMHistory(BMSEmployee wLoginUser, int wID, String wCode,
			int wUpFlowID, int wOrderID, int wLineID, int wProductID, int wCustomerID, int wType, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<APSBomBPM>> wResult = new ServiceResult<List<APSBomBPM>>();
		wResult.Result = new ArrayList<APSBomBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSBomBPMDAO.getInstance().SelectList(wLoginUser, -1, wCode, wUpFlowID, wType, wOrderID,
					wLineID, wProductID, wCustomerID, wStartTime, wEndTime, null, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSBomBPM>> APS_QueryBomBPMEmployeeAllWeb(BMSEmployee wLoginUser, String wCode,
			int wUpFlowID, int wOrderID, int wLineID, int wProductID, int wCustomerID, int wType, int wStatus,
			Calendar wStartTime, Calendar wEndTime, String wMaterialName, String wMaterialNo) {
		ServiceResult<List<APSBomBPM>> wResult = new ServiceResult<List<APSBomBPM>>();
		wResult.Result = new ArrayList<APSBomBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (wStatus) {
			case 1:
				wResult.Result.addAll(APSBomBPMDAO.getInstance().SelectList(wLoginUser, -1, wCode, wUpFlowID, wType,
						wOrderID, wLineID, wProductID, wCustomerID, wStartTime, wEndTime,
						new ArrayList<Integer>(Arrays.asList(20, 21, 22)), wErrorCode));
				break;
			case 0:
				wResult.Result.addAll(APSBomBPMDAO.getInstance().SelectList(wLoginUser, -1, wCode, wUpFlowID, wType,
						wOrderID, wLineID, wProductID, wCustomerID, wStartTime, wEndTime,
						new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5)), wErrorCode));
				break;
			default:
				wResult.Result.addAll(APSBomBPMDAO.getInstance().SelectList(wLoginUser, -1, wCode, wUpFlowID, wType,
						wOrderID, wLineID, wProductID, wCustomerID, wStartTime, wEndTime,
						new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 20, 21, 22)), wErrorCode));
				break;
			}
			wResult.Result.sort((o1, o2) -> o2.SubmitTime.compareTo(o1.SubmitTime));

			// 筛选物料
			if (StringUtils.isNotEmpty(wMaterialNo) || StringUtils.isNotEmpty(wMaterialName)) {
				List<Integer> wTaskIDList = APSBomBPMItemDAO.getInstance().GetIDListByMaterial(wLoginUser, wMaterialNo,
						wMaterialName, wErrorCode);
				wResult.Result = wResult.Result.stream()
						.filter(p -> wTaskIDList.stream().anyMatch(q -> q.intValue() == p.ID))
						.collect(Collectors.toList());
			}

			if (wResult.Result.size() <= 0 || wStatus == 1) {
				return wResult;
			}

			// 待办数据处理
			List<BPMTaskBase> wBaseList = APSBomBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.getID(),
					wErrorCode);
			if (wBaseList == null || wBaseList.size() <= 0) {
				return wResult;
			}
			for (BPMTaskBase wTaskBase : wBaseList) {
				if (!(wTaskBase instanceof APSBomBPM))
					continue;
				APSBomBPM wIPTStandardBPM = (APSBomBPM) wTaskBase;
				wIPTStandardBPM.TagTypes = TaskQueryType.ToHandle.getValue();
				for (int i = 0; i < wResult.Result.size(); i++) {
					if (wResult.Result.get(i).ID == wIPTStandardBPM.ID)
						wResult.Result.set(i, wIPTStandardBPM);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> APS_AutoSynchronizedBOM(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①找到待同步车型、修程、局段
			List<FPCRoute> wFPCRouteList = FPCRouteDAO.getInstance().GetToSynchronizedList(wLoginUser, wErrorCode);
			// ②通过车型、修程、局段获取已进场、维修中且推送成功到ERP过的车辆
			for (FPCRoute wFPCRoute : wFPCRouteList) {
				List<Integer> wOrderIDList = FPCRouteDAO.getInstance().GetMyOrderIDList(wLoginUser, wFPCRoute,
						wErrorCode);
				if (wOrderIDList == null || wOrderIDList.size() <= 0) {
					continue;
				}
				// ③通过车型修程局段获取最新的BOMID
				int wMaxBOMID = FPCRouteDAO.getInstance().GetMaxBOMID(wLoginUser, wFPCRoute, wErrorCode);
				if (wMaxBOMID <= 0) {
					continue;
				}
				// ④对比推送
				for (int wOrderID : wOrderIDList) {
					APSServiceImpl.getInstance().APS_AddAPSBOMToSAP(wLoginUser, wMaxBOMID, wOrderID);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void APS_HandleBOMOnTime() {
		try {
			Calendar wSTime = Calendar.getInstance();
			wSTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
					Calendar.getInstance().get(Calendar.DATE), 2, 0, 0);
			Calendar wETime = Calendar.getInstance();
			wETime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
					Calendar.getInstance().get(Calendar.DATE), 2, 0, 30);

			// 同步台车BOM到ERP
			if (Calendar.getInstance().compareTo(wSTime) > 0 && Calendar.getInstance().compareTo(wETime) < 0) {
				APSServiceImpl.getInstance().APS_AutoSynchronizedBOM(BaseDAO.SysAdmin);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public synchronized ServiceResult<APSTaskStepCancelLogBPM> APS_QueryDefaultTaskStepCancelLogBPM(
			BMSEmployee wLoginUser, int wEventID) {
		ServiceResult<APSTaskStepCancelLogBPM> wResult = new ServiceResult<APSTaskStepCancelLogBPM>();
		wResult.Result = new APSTaskStepCancelLogBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<APSTaskStepCancelLogBPM> wList = APSTaskStepCancelLogBPMDAO.getInstance().SelectList(wLoginUser, -1,
					"", wLoginUser.ID, -1, -1, null, null, new ArrayList<Integer>(Arrays.asList(0)), -1, wErrorCode);
			if (wList.size() > 0) {
				wResult.Result = wList.get(0);
				wResult.Result.CreateTime = Calendar.getInstance();
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public synchronized ServiceResult<APSTaskStepCancelLogBPM> APS_CreateTaskStepCancelLogBPM(BMSEmployee wLoginUser,
			BPMEventModule wEventID) {
		ServiceResult<APSTaskStepCancelLogBPM> wResult = new ServiceResult<APSTaskStepCancelLogBPM>();
		wResult.Result = new APSTaskStepCancelLogBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result.Code = APSTaskStepCancelLogBPMDAO.getInstance().GetNewCode(wLoginUser, wErrorCode);
			wResult.Result.UpFlowID = wLoginUser.ID;
			wResult.Result.UpFlowName = wLoginUser.Name;
			wResult.Result.CreateTime = Calendar.getInstance();
			wResult.Result.SubmitTime = Calendar.getInstance();
			wResult.Result.ID = 0;
			wResult.Result.Status = APSTaskStepCancelLogBPMStatus.Default.getValue();
			wResult.Result.StatusText = "";
			wResult.Result.FlowType = wEventID.getValue();

			wResult.Result = (APSTaskStepCancelLogBPM) APSTaskStepCancelLogBPMDAO.getInstance()
					.BPM_UpdateTask(wLoginUser, wResult.Result, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<APSTaskStepCancelLogBPM> APS_SubmitTaskStepCancelLogBPM(BMSEmployee wLoginUser,
			APSTaskStepCancelLogBPM wData) {
		ServiceResult<APSTaskStepCancelLogBPM> wResult = new ServiceResult<APSTaskStepCancelLogBPM>();
		wResult.Result = new APSTaskStepCancelLogBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wData.Status == 20) {
				wData.StatusText = "已完成";
				// 取消工序计划
				TaskStepCancel(wLoginUser, wData, wErrorCode);
			}

			wResult.Result = (APSTaskStepCancelLogBPM) APSTaskStepCancelLogBPMDAO.getInstance()
					.BPM_UpdateTask(wLoginUser, wData, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	private void TaskStepCancel(BMSEmployee wLoginUser, APSTaskStepCancelLogBPM wData, OutResult<Integer> wErrorCode) {
		try {
			if (StringUtils.isEmpty(wData.StepIDs)) {
				return;
			}

			String[] wStrs = wData.StepIDs.split(",");
			List<APSTaskStep> wList = new ArrayList<APSTaskStep>();
			for (String wStr : wStrs) {
				int wStepID = StringUtils.parseInt(wStr);
				if (wStepID <= 0) {
					continue;
				}

				List<APSTaskStep> wItemList = APSTaskStepDAO.getInstance().SelectList(wLoginUser, -1, wData.OrderID, -1,
						-1, -1, -1, wData.PartID, wStepID, -1, 1, null, null, null, null, wErrorCode);
				if (wItemList != null && wItemList.size() > 0) {
					wList.addAll(wItemList);
				}
			}

			APSServiceImpl.getInstance().APS_SaveStepCancelLog(wLoginUser, wList, wData.CancelType, "",
					wData.DescribeInfo);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public ServiceResult<APSTaskStepCancelLogBPM> APS_GetTaskStepCancelLogBPM(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSTaskStepCancelLogBPM> wResult = new ServiceResult<APSTaskStepCancelLogBPM>();
		wResult.Result = new APSTaskStepCancelLogBPM();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = (APSTaskStepCancelLogBPM) APSTaskStepCancelLogBPMDAO.getInstance()
					.BPM_GetTaskInfo(wLoginUser, wID, "", wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BPMTaskBase>> APS_QueryTaskStepCancelLogBPMEmployeeAll(BMSEmployee wLoginUser,
			int wTagTypes, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<BPMTaskBase>> wResult = new ServiceResult<List<BPMTaskBase>>();
		wResult.Result = new ArrayList<BPMTaskBase>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (TagTypes.getEnumType(wTagTypes)) {
			case Applicant:// 2发起
				wResult.Result = APSTaskStepCancelLogBPMDAO.getInstance().BPM_GetSendTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			case Dispatcher:// 1待做
				wResult.Result = APSTaskStepCancelLogBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser, wLoginUser.ID,
						wErrorCode);
				break;
			case Approver:// 4已做
				wResult.Result = APSTaskStepCancelLogBPMDAO.getInstance().BPM_GetDoneTaskList(wLoginUser, wLoginUser.ID,
						wStartTime, wEndTime, wErrorCode);
				break;
			default:
				break;
			}

			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}

			// 排序
			wResult.Result.sort(Comparator.comparing(BPMTaskBase::getCreateTime, Comparator.reverseOrder()));

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStepCancelLogBPM>> APS_QueryTaskStepCancelLogBPMHistory(BMSEmployee wLoginUser,
			int wID, String wCode, int wUpFlowID, int wOrderID, int wPartID, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<APSTaskStepCancelLogBPM>> wResult = new ServiceResult<List<APSTaskStepCancelLogBPM>>();
		wResult.Result = new ArrayList<APSTaskStepCancelLogBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = APSTaskStepCancelLogBPMDAO.getInstance().SelectList(wLoginUser, wUpFlowID, wCode,
					wUpFlowID, wOrderID, wPartID, wStartTime, wEndTime, null, -1, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<APSTaskStepCancelLogBPM>> APS_QueryTaskStepCancelLogBPMEmployeeAllWeb(
			BMSEmployee wLoginUser, String wCode, int wUpFlowID, int wOrderID, int wPartID, int wStatus,
			Calendar wStartTime, Calendar wEndTime, int wCancelType) {
		ServiceResult<List<APSTaskStepCancelLogBPM>> wResult = new ServiceResult<List<APSTaskStepCancelLogBPM>>();
		wResult.Result = new ArrayList<APSTaskStepCancelLogBPM>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			switch (wStatus) {
			case 1:
				wResult.Result.addAll(APSTaskStepCancelLogBPMDAO.getInstance().SelectList(wLoginUser, -1, "", wUpFlowID,
						wOrderID, wPartID, wStartTime, wEndTime, new ArrayList<Integer>(Arrays.asList(20, 21, 22)),
						wCancelType, wErrorCode));
				break;
			case 0:
				wResult.Result.addAll(APSTaskStepCancelLogBPMDAO.getInstance().SelectList(wLoginUser, -1, "", wUpFlowID,
						wOrderID, wPartID, wStartTime, wEndTime, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4)),
						wCancelType, wErrorCode));
				break;
			default:
				wResult.Result.addAll(APSTaskStepCancelLogBPMDAO.getInstance().SelectList(wLoginUser, -1, "", wUpFlowID,
						wOrderID, wPartID, wStartTime, wEndTime,
						new ArrayList<Integer>(Arrays.asList(1, 2, 3, 20, 21, 22)), wCancelType, wErrorCode));
				break;
			}
			wResult.Result.sort((o1, o2) -> o2.SubmitTime.compareTo(o1.SubmitTime));

			if (wResult.Result.size() <= 0 || wStatus == 1) {
				return wResult;
			}

			// 待办数据处理
			List<BPMTaskBase> wBaseList = APSTaskStepCancelLogBPMDAO.getInstance().BPM_GetUndoTaskList(wLoginUser,
					wLoginUser.getID(), wErrorCode);
			if (wBaseList == null || wBaseList.size() <= 0) {
				return wResult;
			}
			for (BPMTaskBase wTaskBase : wBaseList) {
				if (!(wTaskBase instanceof APSTaskStepCancelLogBPM))
					continue;
				APSTaskStepCancelLogBPM wIPTStandardBPM = (APSTaskStepCancelLogBPM) wTaskBase;
				wIPTStandardBPM.TagTypes = TaskQueryType.ToHandle.getValue();
				for (int i = 0; i < wResult.Result.size(); i++) {
					if (wResult.Result.get(i).ID == wIPTStandardBPM.ID)
						wResult.Result.set(i, wIPTStandardBPM);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public void APS_BOMTaskToSAP(BMSEmployee wLoginUser, List<SFCBOMTaskItem> wDataList) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			SFCBOMTask wTask = (SFCBOMTask) SFCBOMTaskDAO.getInstance().BPM_GetTaskInfo(wLoginUser,
					wDataList.get(0).SFCBOMTaskID, "", wErrorCode);

			// 禁用之前的台车BOM
			DisableAPSBOM(wLoginUser, wDataList, wTask);

			// 推送给MES和SAP
			SynchronizedToSap(wLoginUser, wTask, wErrorCode, wDataList, wTask.ID);
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * ID查询
	 */
	@Override
	public ServiceResult<APSTriggerPart> APS_QueryTriggerPart(BMSEmployee wLoginUser, int wID) {
		ServiceResult<APSTriggerPart> wResult = new ServiceResult<APSTriggerPart>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = APSTriggerPartDAO.getInstance().SelectByID(wLoginUser, wID, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询
	 */
	@Override
	public ServiceResult<List<APSTriggerPart>> APS_QueryTriggerPartList(BMSEmployee wLoginUser, int wID, int wProductID,
			int wLineID, int wCustomerID, int wActive) {
		ServiceResult<List<APSTriggerPart>> wResult = new ServiceResult<List<APSTriggerPart>>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wResult.Result = APSTriggerPartDAO.getInstance().SelectList(wLoginUser, wID, wProductID, wLineID,
					wCustomerID, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 更新或修改
	 */
	@Override
	public ServiceResult<Integer> APS_UpdateTriggerPart(BMSEmployee wLoginUser, APSTriggerPart wAPSTriggerPart) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (wAPSTriggerPart.ID <= 0) {
				List<APSTriggerPart> wList = APSTriggerPartDAO.getInstance().SelectList(wLoginUser, -1,
						wAPSTriggerPart.ProductID, wAPSTriggerPart.LineID, -1, -1, wErrorCode);
				if (wList.stream().anyMatch(p -> p.PartID == wAPSTriggerPart.PartID)) {
					wResult.FaultCode += "提示：该工位已添加!";
					return wResult;
				}
			}

			wResult.Result = APSTriggerPartDAO.getInstance().Update(wLoginUser, wAPSTriggerPart, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 批量激活或禁用
	 */
	@Override
	public ServiceResult<Integer> APS_ActiveTriggerPartList(BMSEmployee wLoginUser, List<Integer> wIDList,
			int wActive) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			APSTriggerPartDAO.getInstance().Active(wLoginUser, wIDList, wActive, wErrorCode);
			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<BMSEmployee>> APS_QueryUserByParts(BMSEmployee wLoginUser, String wPartIDs) {
		ServiceResult<List<BMSEmployee>> wResult = new ServiceResult<List<BMSEmployee>>();
		wResult.Result = new ArrayList<BMSEmployee>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			if (StringUtils.isEmpty(wPartIDs)) {
				return wResult;
			}

			List<Integer> wUserIDList = APSBomBPMDAO.getInstance().GetUserByParts(wLoginUser, wPartIDs, wErrorCode);
			for (Integer wUserID : wUserIDList) {
				BMSEmployee wUser = APSConstans.GetBMSEmployee(wUserID);
				if (wUser.ID > 0)
					wResult.Result.add(wUser);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<MRPMaterialPlan>> MRP_QueryMaterialPlanList(BMSEmployee wLoginUser, int wProductID,
			int wLineID, int wCustomerID, int wOrderID, int wPartID, int wStepID, String wMaterial, int wMaterialType,
			int wReplaceType, int wOutSourceType, Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<MRPMaterialPlan>> wResult = new ServiceResult<List<MRPMaterialPlan>>();
		wResult.Result = new ArrayList<MRPMaterialPlan>();
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			wResult.Result = MRPMaterialPlanDAO.getInstance().SelectList(wLoginUser, -1, wProductID, wLineID,
					wCustomerID, wOrderID, wPartID, wStepID, -1, "", "", wMaterialType, "", 1, wReplaceType,
					wOutSourceType, wStartTime, wEndTime, null, null, wMaterial, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> MRP_HandTrigger(BMSEmployee wLoginUser, int wOrderID, int wPartID) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<MRPMaterialPlan> wList = MRPMaterialPlanDAO.getInstance().SelectList(wLoginUser, -1, -1, -1, -1,
					wOrderID, wPartID, -1, -1, "", "", -1, "", 1, -1, -1, null, null, null, null, "", wErrorCode);
			if (wList.stream().anyMatch(p -> p.Status != MRPMaterialPlanStatus.Save.getValue())) {
				wResult.FaultCode += "提示：当前工位的物料配送单已生成!";
				return wResult;
			}

			this.TriggerMaterialDistributionSheet(wLoginUser, wList, MRPMaterialPlanStatus.HandSubmit.getValue());

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 自动将物料需求计划生成物料配送单
	 */
	@Override
	public void APS_AutoCreateDeliveryOrder(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			// ①查询状态为保存的物料需求计划
			String wMRPMaterialPlanDays = Configuration.readConfigString("mrpmaterialplandays", "config/config");
			int wPlanDays = StringUtils.parseInt(wMRPMaterialPlanDays);
			Calendar wToday = Calendar.getInstance();
			wToday.add(Calendar.DATE, wPlanDays);
			wToday.set(Calendar.HOUR_OF_DAY, 23);
			wToday.set(Calendar.MINUTE, 59);
			wToday.set(Calendar.SECOND, 59);

			List<MRPMaterialPlan> wList = MRPMaterialPlanDAO.getInstance().SelectAutoList(wLoginUser, wToday,
					wErrorCode);
			// ②遍历，判断是否满足自动推送时间
			for (MRPMaterialPlan wMRPMaterialPlan : wList) {
				List<MRPMaterialPlan> wItemList = MRPMaterialPlanDAO.getInstance().SelectList(wLoginUser, -1, -1, -1,
						-1, wMRPMaterialPlan.OrderID, wMRPMaterialPlan.PartID, -1, -1, "", "", -1, "", 1, -1, -1, null,
						null, null, null, "", wErrorCode);
				// ③若满足，自动推送
				this.TriggerMaterialDistributionSheet(wLoginUser, wItemList,
						MRPMaterialPlanStatus.SystemSubmit.getValue());
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * 自动推送物料配送单至WMS
	 */
	@Override
	public void APS_AutoSendDemandList(BMSEmployee wLoginUser) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			String wSendToWMS = Configuration.readConfigString("sendtowms", "config/config");
			if (!wSendToWMS.equals("1")) {
				return;
			}

			// ①查询状态为保存的物料需求计划
			String wWMSPickDemandDays = Configuration.readConfigString("wmspickdemanddays", "config/config");
			int wPlanDays = StringUtils.parseInt(wWMSPickDemandDays);

			Calendar wToday = Calendar.getInstance();
			wToday.add(Calendar.DATE, wPlanDays);
			wToday.set(Calendar.HOUR_OF_DAY, 23);
			wToday.set(Calendar.MINUTE, 59);
			wToday.set(Calendar.SECOND, 59);

			List<Integer> wList = WMSPickDemandDAO.getInstance().SelectAutoList(wLoginUser, wToday, wErrorCode);
			// ②遍历，判断是否满足自动推送时间
			for (int wWMSPickDemandID : wList) {
				// ③若满足，自动推送
				WMSServiceImpl.getInstance().WMS_ManualPush(wLoginUser, wWMSPickDemandID);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
}

/*
 * Location: C:\Users\Shris\Desktop\新建文件夹
 * (5)\MESLOCOAPS.zip!\WEB-INF\classes\com\mes\loco\aps\server\serviceimpl\
 * APSServiceImpl.class Java compiler version: 8 (52.0) JD-Core Version: 1.1.2
 */