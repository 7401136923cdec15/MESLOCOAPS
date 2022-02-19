package com.mes.loco.aps.server.serviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.mes.loco.aps.server.service.CoreService;
import com.mes.loco.aps.server.service.mesenum.QRTypes;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.bfc.BFCAuditAction;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.dms.DMSLedgerStatus;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.sch.SCHWorker;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.utils.RemoteInvokeUtils;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-3-31 19:01:59
 * @LastEditTime 2020-1-8 10:29:23
 *
 */
@Service
public class CoreServiceImpl implements CoreService {
	private static Logger logger = LoggerFactory.getLogger(CoreServiceImpl.class);

	public CoreServiceImpl() {
	}

	private static CoreService Instance;

	public static CoreService getInstance() {
		if (Instance == null)
			Instance = new CoreServiceImpl();
		return Instance;
	}

	@Override
	public APIResult BMS_LoginEmployee(String wLoginName, String wPassword, String wToken, long wMac, int wnetJS) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("user_id", wLoginName);
			wParms.put("passWord", wPassword);
			wParms.put("token", wToken);
			wParms.put("PhoneMac", wMac);
			wParms.put("netJS", wMac);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, "api/User/Login", wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_GetEmployeeAll(BMSEmployee wLoginUser, int wDepartmentID, int wPosition, int wActive) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("active", wActive);
			wParms.put("DepartmentID", wDepartmentID);
			wParms.put("Position", wPosition);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/User/All?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_QueryEmployeeByID(BMSEmployee wLoginUser, int wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("user_info", wID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/User/Info?cadv_ao={0}&cade_po={1}", wLoginUser.getLoginName(),
							wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_CheckPowerByAuthorityID(int wCompanyID, int wUserID, int wFunctionID, int wRangeID,
			int wTypeID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("AuthortyID", wFunctionID);
			wParms.put("RangeID", wRangeID);
			wParms.put("TypeID", wTypeID);
			wParms.put("CompanyID", wCompanyID);
			wParms.put("UserID", wUserID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, "api/Role/Check", wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult SFC_QueryShiftID(BMSEmployee wLoginUser, int Shifts) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			String wUri = StringUtils.Format("api/SCHShift/CurrentShiftID?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkspace(BMSEmployee wLoginUser, int wID, String wCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("Code", wCode);
			String wUri = StringUtils.Format("api/FMCWorkspace/Info?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult SCH_QueryLeadWorkerByPositionID(BMSEmployee wLoginUser, int wPositionID, int wShiftID) {
		APIResult wResult = new APIResult();
		try {
			// 自己构造APIResult
			APIResult wResultList = BMS_GetEmployeeAll(wLoginUser, -1, wLoginUser.Manager, 1);
			List<BMSEmployee> wList = wResultList.List(BMSEmployee.class);
			List<SCHWorker> wWorkerList = new ArrayList<SCHWorker>();
			if (wList != null && wList.size() > 0) {
				for (BMSEmployee wItem : wList) {
					SCHWorker wSCHWorker = new SCHWorker();
					wSCHWorker.WorkerID = wItem.ID;
					wSCHWorker.WorkerName = wItem.Name;
					wWorkerList.add(wSCHWorker);
				}
			}
			Map<String, Object> wReturnObject = new HashMap<String, Object>();
			wReturnObject.put("msg", "");
			wReturnObject.put("list", wWorkerList);
			wReturnObject.put("info", null);
			Map<String, Object> wResultData = new HashMap<String, Object>();
			wResultData.put("resultCode", 1000);
			wResultData.put("returnObject", wReturnObject);
			// 转换
			wResult = CloneTool.Clone(wResultData, APIResult.class);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult SCH_QueryWorkerByPositionID(BMSEmployee wLoginUser, int wPositionID, int wShiftID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("PositionID", wPositionID);
			wParms.put("ShiftID", wShiftID);
			String wUri = StringUtils.Format("api/SCHWorker/PositionLeader?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_GetQRCode(BMSEmployee wLoginUser, QRTypes wQRType, long wQRCodeID) {
		APIResult wResult = new APIResult();
		try {
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_GetFMCWorkspaceList(BMSEmployee wLoginUser, int wProductID, int wPartID, String wPartNo,
			int wPlaceType, int wActive) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ProductID", wProductID);
			wParms.put("PartID", wPartID);
			wParms.put("PartNo", wPartNo);
			wParms.put("PlaceType", wPlaceType);
			wParms.put("Active", wActive);
			String wUri = StringUtils.Format("api/FMCWorkspace/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_SaveFMCWorkspace(BMSEmployee wBMSEmployee, FMCWorkspace wFMCWorkspace) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wFMCWorkspace);
			String wUri = StringUtils.Format("api/FMCWorkspace/Update?cadv_ao={0}&cade_po={1}&company_id={2}",
					wBMSEmployee.LoginName, wBMSEmployee.Password, wBMSEmployee.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult DMS_GetDeviceLedgerList(BMSEmployee wBMSEmployee, int wBusinessUnitID, int wBaseID, int wFactoryID,
			int wWorkShopID, int wLineID, int wModelID, DMSLedgerStatus wDMSLedgerStatus) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ModelID", wModelID);
			wParms.put("WorkShopID", wWorkShopID);
			wParms.put("LineID", wLineID);
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("BaseID", wBaseID);
			wParms.put("FactoryID", wFactoryID);
			wParms.put("Status", wDMSLedgerStatus.getValue());
			String wUri = StringUtils.Format("api/DeviceLedger/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wBMSEmployee.LoginName, wBMSEmployee.Password, wBMSEmployee.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductList(BMSEmployee wBMSEmployee, int wBusinessUnitID, int wProductTypeID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", wBusinessUnitID);
			wParms.put("ProductTypeID", wProductTypeID);
			wParms.put("OAGetType", -1);
			String wUri = StringUtils.Format("api/FPCProduct/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wBMSEmployee.LoginName, wBMSEmployee.Password, wBMSEmployee.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_GetQRType(BMSEmployee wLoginUser, String wQRCode) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("QRCode", wQRCode);
			String wUri = StringUtils.Format("api/BFCQR/QRType?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据ID获取工位
	 * 
	 * @param wLoginUser
	 * @param wID
	 * @return
	 */
	@Override
	public APIResult FPC_GetPartByID(BMSEmployee wLoginUser, long wID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			String wUri = StringUtils.Format("api/FPCPartPoint/Info?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取岗位列表
	 * 
	 * @param wLoginUser
	 * @param wCompanyID
	 * @return
	 */
	public APIResult BMS_QueryPositionList(BMSEmployee wLoginUser, int wCompanyID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			String wUri = StringUtils.Format("api/Department/AllPosition?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取部门列表
	 * 
	 * @param wLoginUser
	 * @param wCompanyID
	 * @param wLoginID
	 * @return
	 */
	public APIResult BMS_QueryDepartmentList(BMSEmployee wLoginUser, int wCompanyID, int wLoginID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			String wUri = StringUtils.Format("api/Department/AllDepartment?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryLineUnitList(BMSEmployee wLoginUser, int wLineID, int wProductID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("LineID", wLineID);
			wParms.put("ProductID", wProductID);
			wParms.put("ID", -1);
			String wUri = StringUtils.Format("api/FMCLineUnit/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult CFG_QueryCalendarList(BMSEmployee wLoginUser, int wYear, int wWorkShopID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("year", wYear);
			wParms.put("WorkShopID", wWorkShopID);
			String wUri = StringUtils.Format("api/Holiday/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkDayLists(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			String wUri = StringUtils.Format("api/FMCWorkDay/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryShiftList(BMSEmployee wLoginUser, int wFactoryID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", wFactoryID);

			String wUri = StringUtils.Format("api/FMCShift/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryTimeZoneList(BMSEmployee wLoginUser, int wShiftID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ShiftID", wShiftID);

			String wUri = StringUtils.Format("api/FMCTimeZone/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRouteList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", -1);
			wParms.put("BusinessUnitID", -1);
			wParms.put("ProductTypeID", -1);
			wParms.put("OAGetType", -1);

			String wUri = StringUtils.Format("api/FPCRoute/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryRoutePartList(BMSEmployee wLoginUser, int wRouteID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("RouteID", wRouteID);

			String wUri = StringUtils.Format("api/FPCRoutePart/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryLineList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", 0);
			wParms.put("BusinessUnitID", 0);
			wParms.put("WorkShopID", 0);

			String wUri = StringUtils.Format("api/FMCLine/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryPartList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", 0);
			wParms.put("BusinessUnitID", 0);

			String wUri = StringUtils.Format("api/FPCPart/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryPartPointList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", 0);
			wParms.put("BusinessUnitID", 0);
			wParms.put("ProductTypeID", 0);

			String wUri = StringUtils.Format("api/FPCPartPoint/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductRouteList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("RouteID", 0);
			wParms.put("ProductID", 0);

			String wUri = StringUtils.Format("api/FPCProductRoute/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkShopList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FactoryID", 0);
			wParms.put("BusinessUnitID", 0);

			String wUri = StringUtils.Format("api/FMCWorkShop/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryActiveWorkDay(BMSEmployee wLoginUser, int wActive) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", 0);
			wParms.put("Active", wActive);

			String wUri = StringUtils.Format("api/FMCWorkDay/Info?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FMC_QueryWorkChargeList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("StationID", 0);
			wParms.put("Active", 1);
			wParms.put("ClassID", 0);

			String wUri = StringUtils.Format("api/WorkCharge/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult CRM_QueryCustomerList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("active", 2);

			String wUri = StringUtils.Format("api/Customer/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult FPC_QueryProductList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("BusinessUnitID", -1);
			wParms.put("ProductTypeID", -1);
			wParms.put("OAGetType", -1);
			String wUri = StringUtils.Format("api/FPCProduct/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_GetMessageList(BMSEmployee wLoginUser, int wResponsorID, int wModuleID, int wType, int wActive,
			int wShiftID, Calendar wStartTime, Calendar wEndTime) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ResponsorID", wResponsorID);
			wParms.put("ModuleID", wModuleID);
			wParms.put("ShiftID", wShiftID);
			wParms.put("Type", wType);
			wParms.put("Active", wActive);
			wParms.put("StartTime", wStartTime);
			wParms.put("EndTime", wEndTime);
			String wUri = StringUtils.Format("api/HomePage/MsgAll?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_GetMessageList(BMSEmployee wLoginUser, int wResponsorID, int wModuleID, int wMessageID,
			int wType, int wActive, int wShiftID, Calendar wStartTime, Calendar wEndTime) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ResponsorID", wResponsorID);
			wParms.put("ModuleID", wModuleID);
			wParms.put("ShiftID", wShiftID);
			wParms.put("MessageID", wMessageID);
			wParms.put("Type", wType);
			wParms.put("Active", wActive);
			wParms.put("StairtTime", wStartTime);
			wParms.put("EndTime", wEndTime);
			String wUri = StringUtils.Format("/api/HomePage/MsgAll?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_GetMessageList(BMSEmployee wLoginUser, int wResponsorID, int wModuleID,
			List<Integer> wMessageID, int wType, int wActive) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ResponsorID", wResponsorID);
			wParms.put("ModuleID", wModuleID);
			wParms.put("ShiftID", -1);
			wParms.put("MessageID", StringUtils.Join(",", wMessageID));
			wParms.put("Type", wType);
			wParms.put("Active", wActive);
			String wUri = StringUtils.Format("/api/HomePage/MsgAll?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_UpdateMessageList(BMSEmployee wLoginUser, List<BFCMessage> wBFCMessageList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wBFCMessageList);
			wParms.put("Send", 0);
			String wUri = StringUtils.Format("api/HomePage/MsgUpdate?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_SendMessageList(BMSEmployee wLoginUser, List<BFCMessage> wBFCMessageList) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wBFCMessageList);
			wParms.put("Send", 1);
			String wUri = StringUtils.Format("api/HomePage/MsgUpdate?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_FunctionRangeAll(BMSEmployee wLoginUser, int wOperatorID, int wFunctionID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("OperatorID", wOperatorID);
			wParms.put("FunctionID", wFunctionID);
			String wUri = StringUtils.Format("api/Role/FunctionRangeAll?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_FunctionAll(BMSEmployee wLoginUser, int wOperatorID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("OperatorID", wOperatorID);
			String wUri = StringUtils.Format("api/Role/FunctionAll?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_UserAllByFunction(BMSEmployee wLoginUser, int wFunctionID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("FunctionID", wFunctionID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName,
					StringUtils.Format("api/Role/UserAllByFunctionID?cadv_ao={0}&cade_po={1}",
							wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_UpdateAction(BMSEmployee wLoginUser, BFCAuditAction wBFCAuditAction, String wTitle) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("data", wBFCAuditAction);
			wParms.put("Title", wTitle);
			String wUri = StringUtils.Format("api/BFCAudit/UpdateAction?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_CurrentConfig(BMSEmployee wLoginUser, int wModuleID, int wTaskID, int wUserID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ModuleID", wModuleID);
			wParms.put("TaskID", wTaskID);
			wParms.put("UserID", wUserID);
			String wUri = StringUtils.Format("api/BFCAudit/CurrentConfig?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BFC_ActionAll(BMSEmployee wLoginUser, int wModuleID, int wTaskID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ModuleID", wModuleID);
			wParms.put("TaskID", wTaskID);
			String wUri = StringUtils.Format("api/BFCAudit/ActionAll?cadv_ao={0}&cade_po={1}", wLoginUser.LoginName,
					wLoginUser.Password);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult CFG_QueryUnitList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			String wUri = StringUtils.Format("api/Unit/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult QMS_StartInstance(BMSEmployee wLoginUser, String processDefinitionKey) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("processDefinitionKey", processDefinitionKey);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, "MESLOCOAPS",
					StringUtils.Format("api/Runtime/startProcessByProcessDefinitionKey?cadv_ao={0}&cade_po={1}",
							wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult QMS_CompleteInstance(BMSEmployee wLoginUser, BPMTaskBase wBPMTaskBase, String wTaskID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			wParms.put("data", wBPMTaskBase);
			wParms.put("TaskID", wTaskID);

			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, "MESLOCOAPS",
					StringUtils.Format("api/Runtime/CompleteMyPersonalTask?cadv_ao={0}&cade_po={1}",
							wLoginUser.getLoginName(), wLoginUser.getPassword()),
					wParms, HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_LogoutEmployee(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();

			String wUri = StringUtils.Format("api/User/Logout?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.POST);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult BMS_UserAllByRoleID(BMSEmployee wLoginUser, int wRoleID) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("role_id", wRoleID);

			wResult = RemoteInvokeUtils.getInstance()
					.HttpInvokeAPI(ServerUrl, ServerName,
							StringUtils.Format("api/Role/UserAll?cadv_ao={0}&cade_po={1}",
									BaseDAO.SysAdmin.getLoginName(), BaseDAO.SysAdmin.getPassword()),
							wParms, HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
