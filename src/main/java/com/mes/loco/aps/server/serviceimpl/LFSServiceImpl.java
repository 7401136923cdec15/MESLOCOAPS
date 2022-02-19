package com.mes.loco.aps.server.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.mes.loco.aps.server.service.LFSService;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RemoteInvokeUtils;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2019年12月27日12:44:48
 * @LastEditTime 2020-4-2 16:48:39
 *
 */

@Service
public class LFSServiceImpl implements LFSService {
	private static Logger logger = LoggerFactory.getLogger(LFSServiceImpl.class);

	public LFSServiceImpl() {
	}

	private static LFSService Instance;

	public static LFSService getInstance() {
		if (Instance == null)
			Instance = new LFSServiceImpl();
		return Instance;
	} 
	
	
	@Override
	public APIResult LFS_QueryWorkAreaStationList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {

			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", -1);
			wParms.put("WorkAreaID", -1);
			wParms.put("StationID", -1);
			wParms.put("Active", 1);

			String wUri = StringUtils.Format("api/LFS/WorkAreaAll?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}
	 
	@Override
	public APIResult LFS_QueryAreaDepartmentList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", -1);
			wParms.put("WorkAreaID", -1);
			wParms.put("DepartmentID", -1);
			wParms.put("Active", 1);

			String wUri = StringUtils.Format("api/LFSAreaDepartment/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}
	
	
	@Override  
	public APIResult LFS_QueryWorkAreaCheckerList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", -1);
			wParms.put("WorkAreaID", -1);
			wParms.put("CheckerID", -1);
			wParms.put("Active", 1);

			String wUri = StringUtils.Format("api/LFSWorkAreaChecker/All?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	@Override
	public APIResult LFS_QueryAreaDepartment(BMSEmployee wLoginUser, int wID, String wName) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", wID);
			wParms.put("Name", wName); 

			String wUri = StringUtils.Format("api/LFSAreaDepartment/Info?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}


	@Override
	public APIResult LFS_QueryStoreHouseList(BMSEmployee wLoginUser) {
		APIResult wResult = new APIResult();
		try {
			Map<String, Object> wParms = new HashMap<String, Object>();
			wParms.put("ID", -1);
			wParms.put("Active", -1); 

			String wUri = StringUtils.Format("api/LFS/StoreHouseAll?cadv_ao={0}&cade_po={1}&company_id={2}",
					wLoginUser.LoginName, wLoginUser.Password, wLoginUser.CompanyID);
			wResult = RemoteInvokeUtils.getInstance().HttpInvokeAPI(ServerUrl, ServerName, wUri, wParms,
					HttpMethod.GET);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
