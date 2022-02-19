package com.mes.loco.aps.server.serviceimpl.utils.aps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.po.BPMResource;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersion;
import com.mes.loco.aps.server.service.po.bms.BMSDepartment;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bms.BMSPosition;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.cfg.CFGUnit;
import com.mes.loco.aps.server.service.po.crm.CRMCustomer;
import com.mes.loco.aps.server.service.po.fmc.FMCLine;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkCharge;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkShop;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCPartPoint;
import com.mes.loco.aps.server.service.po.fpc.FPCProduct;
import com.mes.loco.aps.server.service.po.fpc.FPCRoute;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaChecker;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.loco.aps.server.service.po.sfc.SFCAttemptRun;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.LFSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.fpc.FPCRouteDAO;

/**
 * QMS全局缓存数据类
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-4-2 12:42:40
 * @LastEditTime 2020-4-11 21:58:24
 *
 */
public class APSConstans {

	private static Logger logger = LoggerFactory.getLogger(APSConstans.class);

	public static int YJStationID = 26;

	/**
	 * 车间全局数据
	 */
	private static Calendar RefreshWorkShopTime = Calendar.getInstance();

	private static Map<Integer, FMCWorkShop> FMCWorkShopDic = new HashMap<Integer, FMCWorkShop>();

	public static synchronized Map<Integer, FMCWorkShop> GetFMCWorkShopList() {
		if (FMCWorkShopDic == null || FMCWorkShopDic.size() <= 0
				|| RefreshWorkShopTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCWorkShop> wFMCWorkShopList = CoreServiceImpl.getInstance().FMC_QueryWorkShopList(BaseDAO.SysAdmin)
					.List(FMCWorkShop.class);
			if (wFMCWorkShopList != null && wFMCWorkShopList.size() > 0) {
				FMCWorkShopDic = wFMCWorkShopList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshWorkShopTime = Calendar.getInstance();
			RefreshWorkShopTime.add(Calendar.MINUTE, 3);
		}
		return FMCWorkShopDic;
	}

	public static String GetFMCWorkShopName(int wID) {
		String wResult = "";
		if (APSConstans.GetFMCWorkShopList().containsKey(wID)) {
			if (APSConstans.GetFMCWorkShopList().get(wID) != null) {
				wResult = APSConstans.GetFMCWorkShopList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCWorkShop GetFMCWorkShop(int wID) {
		FMCWorkShop wResult = new FMCWorkShop();
		if (APSConstans.GetFMCWorkShopList().containsKey(wID)) {
			if (APSConstans.GetFMCWorkShopList().get(wID) != null) {
				wResult = APSConstans.GetFMCWorkShopList().get(wID);
			}
		}
		return wResult;
	}

	/**
	 * 产线全局数据
	 */
	private static Calendar RefreshLineTime = Calendar.getInstance();

	private static Map<Integer, FMCLine> FMCLineDic = new HashMap<Integer, FMCLine>();

	public static synchronized Map<Integer, FMCLine> GetFMCLineList() {
		if (FMCLineDic == null || FMCLineDic.size() <= 0 || RefreshLineTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCLine> wFMCLineList = CoreServiceImpl.getInstance().FMC_QueryLineList(BaseDAO.SysAdmin)
					.List(FMCLine.class);
			if (wFMCLineList != null && wFMCLineList.size() > 0) {
				FMCLineDic = wFMCLineList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshLineTime = Calendar.getInstance();
			RefreshLineTime.add(Calendar.MINUTE, 3);
		}
		return FMCLineDic;
	}

	public static String GetFMCLineName(int wID) {
		String wResult = "";
		if (APSConstans.GetFMCLineList().containsKey(wID)) {
			if (APSConstans.GetFMCLineList().get(wID) != null) {
				wResult = APSConstans.GetFMCLineList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FMCLine GetFMCLine(int wID) {
		FMCLine wResult = new FMCLine();
		if (APSConstans.GetFMCLineList().containsKey(wID)) {
			if (APSConstans.GetFMCLineList().get(wID) != null) {
				wResult = APSConstans.GetFMCLineList().get(wID);
			}
		}
		return wResult;
	}

	/**
	 * 工位全局数据
	 */
	private static Calendar RefreshPartTime = Calendar.getInstance();

	private static Map<Integer, FPCPart> FPCPartDic = new HashMap<Integer, FPCPart>();

	public static synchronized Map<Integer, FPCPart> GetFPCPartList() {
		if (FPCPartDic == null || FPCPartDic.size() <= 0 || RefreshPartTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCPart> wFPCPartList = CoreServiceImpl.getInstance().FPC_QueryPartList(BaseDAO.SysAdmin)
					.List(FPCPart.class);
			if (wFPCPartList != null && wFPCPartList.size() > 0) {
				FPCPartDic = wFPCPartList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshPartTime = Calendar.getInstance();
			RefreshPartTime.add(Calendar.MINUTE, 3);
		}
		return FPCPartDic;
	}

	public static String GetFPCPartName(int wID) {
		String wResult = "";
		if (APSConstans.GetFPCPartList().containsKey(wID)) {
			if (APSConstans.GetFPCPartList().get(wID) != null) {
				wResult = APSConstans.GetFPCPartList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FPCPart GetFPCPart(int wID) {
		FPCPart wResult = new FPCPart();
		if (APSConstans.GetFPCPartList().containsKey(wID)) {
			if (APSConstans.GetFPCPartList().get(wID) != null) {
				wResult = APSConstans.GetFPCPartList().get(wID);
			}
		}
		return wResult;
	}

	public static int GetPartIDByCode(String wCode) {
		for (FPCPart wFPCPart : APSConstans.GetFPCPartList().values()) {
			if (wFPCPart.Code.equals(wCode))
				return wFPCPart.ID;
		}
		return 0;
	}

	/**
	 * 工序全局数据
	 */
	private static Calendar RefreshPartPointTime = Calendar.getInstance();

	private static Map<Integer, FPCPartPoint> FPCPartPointDic = new HashMap<Integer, FPCPartPoint>();

	public static synchronized Map<Integer, FPCPartPoint> GetFPCPartPointList() {
		if (FPCPartPointDic == null || FPCPartPointDic.size() <= 0
				|| RefreshPartPointTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCPartPoint> wFPCPartPointList = CoreServiceImpl.getInstance()
					.FPC_QueryPartPointList(BaseDAO.SysAdmin).List(FPCPartPoint.class);
			if (wFPCPartPointList != null && wFPCPartPointList.size() > 0) {
				FPCPartPointDic = wFPCPartPointList.stream()
						.collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshPartPointTime = Calendar.getInstance();
			RefreshPartPointTime.add(Calendar.MINUTE, 3);
		}
		return FPCPartPointDic;
	}

	public static String GetFPCPartPointName(int wID) {
		String wResult = "";
		if (APSConstans.GetFPCPartPointList().containsKey(wID)) {
			if (APSConstans.GetFPCPartPointList().get(wID) != null) {
				wResult = APSConstans.GetFPCPartPointList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static FPCPartPoint GetFPCPartPoint(int wID) {
		FPCPartPoint wResult = new FPCPartPoint();
		if (APSConstans.GetFPCPartPointList().containsKey(wID)) {
			if (APSConstans.GetFPCPartPointList().get(wID) != null) {
				wResult = APSConstans.GetFPCPartPointList().get(wID);
			}
		}
		return wResult;
	}

	public static int GetPartPointIDByName(String wName) {
		for (FPCPartPoint wFPCPartPoint : APSConstans.GetFPCPartPointList().values()) {
			if (wFPCPartPoint.Name.equals(wName))
				return wFPCPartPoint.ID;
		}
		return 0;
	}

	/**
	 * 产品型号全局数据
	 */
	private static Calendar RefreshProductTime = Calendar.getInstance();

	private static Map<Integer, FPCProduct> FPCProductDic = new HashMap<Integer, FPCProduct>();

	public static synchronized Map<Integer, FPCProduct> GetFPCProductList() {
		if (FPCProductDic == null || FPCProductDic.size() <= 0
				|| RefreshProductTime.compareTo(Calendar.getInstance()) < 0) {
			List<FPCProduct> wFPCProductList = CoreServiceImpl.getInstance().FPC_QueryProductList(BaseDAO.SysAdmin)
					.List(FPCProduct.class);
			if (wFPCProductList != null && wFPCProductList.size() > 0) {
				FPCProductDic = wFPCProductList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshProductTime = Calendar.getInstance();
			RefreshProductTime.add(Calendar.MINUTE, 3);
		}
		return FPCProductDic;
	}

	public static String GetFPCProductName(int wID) {
		String wResult = "";
		if (APSConstans.GetFPCProductList().containsKey(wID)) {
			if (APSConstans.GetFPCProductList().get(wID) != null) {
				wResult = APSConstans.GetFPCProductList().get(wID).getProductNo();
			}
		}
		return wResult;
	}

	public static FPCProduct GetFPCProduct(int wID) {
		FPCProduct wResult = new FPCProduct();
		if (APSConstans.GetFPCProductList().containsKey(wID)) {
			if (APSConstans.GetFPCProductList().get(wID) != null) {
				wResult = APSConstans.GetFPCProductList().get(wID);
			}
		}
		return wResult;
	}

	public static int GetFPCProducID(String wProductNo) {
		int wResult = 0;
		for (int wProductID : APSConstans.GetFPCProductList().keySet()) {
			if (APSConstans.GetFPCProductList().get(wProductID).ProductNo.equals(wProductNo)) {
				return wProductID;
			}
		}
		return wResult;
	}

	/**
	 * 部门全局数据
	 */
	private static Calendar RefreshDepartmentTime = Calendar.getInstance();

	private static Map<Integer, BMSDepartment> BMSDepartmentDic = new HashMap<Integer, BMSDepartment>();

	public static synchronized Map<Integer, BMSDepartment> GetBMSDepartmentList() {
		if (BMSDepartmentDic == null || BMSDepartmentDic.size() <= 0
				|| RefreshDepartmentTime.compareTo(Calendar.getInstance()) < 0) {
			List<BMSDepartment> wBMSDepartmentList = CoreServiceImpl.getInstance()
					.BMS_QueryDepartmentList(BaseDAO.SysAdmin, 0, 0).List(BMSDepartment.class);
			if (wBMSDepartmentList != null && wBMSDepartmentList.size() > 0) {
				BMSDepartmentDic = wBMSDepartmentList.stream()
						.collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshDepartmentTime = Calendar.getInstance();
			RefreshDepartmentTime.add(Calendar.MINUTE, 3);
		}
		return BMSDepartmentDic;
	}

	public static String GetBMSDepartmentName(int wID) {
		String wResult = "";
		if (APSConstans.GetBMSDepartmentList().containsKey(wID)) {
			if (APSConstans.GetBMSDepartmentList().get(wID) != null) {
				wResult = APSConstans.GetBMSDepartmentList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static BMSDepartment GetBMSDepartment(int wID) {
		BMSDepartment wResult = new BMSDepartment();
		if (APSConstans.GetBMSDepartmentList().containsKey(wID)) {
			if (APSConstans.GetBMSDepartmentList().get(wID) != null) {
				wResult = APSConstans.GetBMSDepartmentList().get(wID);
			}
		}
		return wResult;
	}

	/**
	 * 人员全局数据
	 */
	private static Calendar RefreshEmployeeTime = Calendar.getInstance();

	private static Map<Integer, BMSEmployee> BMSEmployeeDic = new HashMap<Integer, BMSEmployee>();

	public static synchronized Map<Integer, BMSEmployee> GetBMSEmployeeList() {
		if (BMSEmployeeDic == null || BMSEmployeeDic.size() <= 0
				|| RefreshEmployeeTime.compareTo(Calendar.getInstance()) < 0) {
			List<BMSEmployee> wBMSEmployeeList = CoreServiceImpl.getInstance()
					.BMS_GetEmployeeAll(BaseDAO.SysAdmin, -1, -1, 1).List(BMSEmployee.class);
			if (wBMSEmployeeList != null && wBMSEmployeeList.size() > 0) {
				BMSEmployeeDic = wBMSEmployeeList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshEmployeeTime = Calendar.getInstance();
			RefreshEmployeeTime.add(Calendar.MINUTE, 3);
		}
		return BMSEmployeeDic;
	}

	public static String GetBMSEmployeeName(int wID) {
		String wResult = "";
		if (APSConstans.GetBMSEmployeeList().containsKey(wID)) {
			if (APSConstans.GetBMSEmployeeList().get(wID) != null) {
				wResult = APSConstans.GetBMSEmployeeList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static BMSEmployee GetBMSEmployee(int wID) {
		BMSEmployee wResult = new BMSEmployee();
		if (APSConstans.GetBMSEmployeeList().containsKey(wID)) {
			if (APSConstans.GetBMSEmployeeList().get(wID) != null) {
				wResult = APSConstans.GetBMSEmployeeList().get(wID);
			}
		}
		return wResult;
	}

	public static String GetBMSEmployeeName(List<Integer> wIDList) {
		String wResult = "";
		if (wIDList == null || wIDList.size() <= 0)
			return wResult;

		List<String> wNames = new ArrayList<String>();
		for (Integer integer : wIDList) {
			if (integer <= 0)
				continue;

			if (APSConstans.GetBMSEmployeeList().containsKey(integer)) {
				if (APSConstans.GetBMSEmployeeList().get(integer) != null) {
					wNames.add(APSConstans.GetBMSEmployeeList().get(integer).getName());
				}
			}

		}
		wResult = StringUtils.Join(",", wNames);

		return wResult;
	}

	/**
	 * 岗位全局数据
	 */
	private static Calendar RefreshPositionTime = Calendar.getInstance();

	private static Map<Integer, BMSPosition> BMSPositionDic = new HashMap<Integer, BMSPosition>();

	public static synchronized Map<Integer, BMSPosition> GetBMSPositionList() {
		if (BMSPositionDic == null || BMSPositionDic.size() <= 0
				|| RefreshPositionTime.compareTo(Calendar.getInstance()) < 0) {
			List<BMSPosition> wBMSPositionList = CoreServiceImpl.getInstance()
					.BMS_QueryPositionList(BaseDAO.SysAdmin, 0).List(BMSPosition.class);
			if (wBMSPositionList != null && wBMSPositionList.size() > 0) {
				BMSPositionDic = wBMSPositionList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshPositionTime = Calendar.getInstance();
			RefreshPositionTime.add(Calendar.MINUTE, 3);
		}
		return BMSPositionDic;
	}

	public static String GetBMSPositionName(int wID) {
		String wResult = "";
		if (APSConstans.GetBMSPositionList().containsKey(wID)) {
			if (APSConstans.GetBMSPositionList().get(wID) != null) {
				wResult = APSConstans.GetBMSPositionList().get(wID).getName();
			}
		}
		return wResult;
	}

	public static BMSPosition GetBMSPosition(int wID) {
		BMSPosition wResult = new BMSPosition();
		if (APSConstans.GetBMSPositionList().containsKey(wID)) {
			if (APSConstans.GetBMSPositionList().get(wID) != null) {
				wResult = APSConstans.GetBMSPositionList().get(wID);
			}
		}
		return wResult;
	}

	/**
	 * 工位班组全局数据
	 */
	private static Calendar RefreshWorkChargeTime = Calendar.getInstance();

	private static List<FMCWorkCharge> FMCWorkChargeList = new ArrayList<FMCWorkCharge>();

	public static synchronized List<FMCWorkCharge> GetFMCWorkChargeList() {
		if (FMCWorkChargeList == null || FMCWorkChargeList.size() <= 0
				|| RefreshWorkChargeTime.compareTo(Calendar.getInstance()) < 0) {
			List<FMCWorkCharge> wFMCWorkChargeList = CoreServiceImpl.getInstance()
					.FMC_QueryWorkChargeList(BaseDAO.SysAdmin).List(FMCWorkCharge.class);
			RefreshWorkChargeTime = Calendar.getInstance();
			RefreshWorkChargeTime.add(Calendar.MINUTE, 3);
			FMCWorkChargeList = wFMCWorkChargeList;
		}
		return FMCWorkChargeList;
	}

	public static int GetClassID(int wStationID) {
		int wResult = 0;
		if (APSConstans.GetFMCWorkChargeList().stream().anyMatch(p -> p.StationID == wStationID)) {
			wResult = APSConstans.GetFMCWorkChargeList().stream().filter(p -> p.StationID == wStationID).findFirst()
					.get().ClassID;
		}
		return wResult;
	}

	public static List<Integer> GetStationIDList(int wClassID) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			if (APSConstans.GetFMCWorkChargeList().stream().anyMatch(p -> p.ClassID == wClassID)) {
				List<FMCWorkCharge> wList = APSConstans.GetFMCWorkChargeList().stream()
						.filter(p -> p.ClassID == wClassID).collect(Collectors.toList());
				for (FMCWorkCharge wFMCWorkCharge : wList) {
					wResult.add(wFMCWorkCharge.StationID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 局段全局数据
	 */
	private static Calendar RefreshCustomerTime = Calendar.getInstance();

	private static Map<Integer, CRMCustomer> CRMCustomerDic = new HashMap<Integer, CRMCustomer>();

	public static synchronized Map<Integer, CRMCustomer> GetCRMCustomerList() {
		if (CRMCustomerDic == null || CRMCustomerDic.size() <= 0
				|| RefreshCustomerTime.compareTo(Calendar.getInstance()) < 0) {
			List<CRMCustomer> wCRMCustomerList = CoreServiceImpl.getInstance().CRM_QueryCustomerList(BaseDAO.SysAdmin)
					.List(CRMCustomer.class);
			if (wCRMCustomerList != null && wCRMCustomerList.size() > 0) {
				CRMCustomerDic = wCRMCustomerList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshCustomerTime = Calendar.getInstance();
			RefreshCustomerTime.add(Calendar.MINUTE, 3);
		}
		return CRMCustomerDic;
	}

	public static String GetCRMCustomerName(int wID) {
		String wResult = "";
		if (APSConstans.GetCRMCustomerList().containsKey(wID)) {
			if (APSConstans.GetCRMCustomerList().get(wID) != null) {
				wResult = APSConstans.GetCRMCustomerList().get(wID).getCustomerName();
			}
		}
		return wResult;
	}

	public static CRMCustomer GetCRMCustomer(int wID) {
		CRMCustomer wResult = new CRMCustomer();
		if (APSConstans.GetCRMCustomerList().containsKey(wID)) {
			if (APSConstans.GetCRMCustomerList().get(wID) != null) {
				wResult = APSConstans.GetCRMCustomerList().get(wID);
			}
		}
		return wResult;
	}

	/**
	 * 工区检验员全局数据
	 */
	private static Calendar RefreshWorkAreaCheckerTime = Calendar.getInstance();

	private static List<LFSWorkAreaChecker> LFSWorkAreaCheckerList = new ArrayList<LFSWorkAreaChecker>();

	public static synchronized List<LFSWorkAreaChecker> GetLFSWorkAreaCheckerList() {
		if (LFSWorkAreaCheckerList == null || LFSWorkAreaCheckerList.size() <= 0
				|| RefreshWorkAreaCheckerTime.compareTo(Calendar.getInstance()) < 0) {
			List<LFSWorkAreaChecker> wLFSWorkAreaCheckerList = LFSServiceImpl.getInstance()
					.LFS_QueryWorkAreaCheckerList(BaseDAO.SysAdmin).List(LFSWorkAreaChecker.class);
			RefreshWorkAreaCheckerTime = Calendar.getInstance();
			RefreshWorkAreaCheckerTime.add(Calendar.MINUTE, 3);
			LFSWorkAreaCheckerList = wLFSWorkAreaCheckerList;
		}
		return LFSWorkAreaCheckerList;
	}

	public static int LFS_GetWorkAreaID(int wCheckerID) {
		int wResult = 0;
		if (APSConstans.GetLFSWorkAreaCheckerList().stream()
				.anyMatch(p -> p.CheckerIDList.stream().anyMatch(q -> q == wCheckerID))) {
			wResult = APSConstans.GetLFSWorkAreaCheckerList().stream()
					.filter(p -> p.CheckerIDList.stream().anyMatch(q -> q == wCheckerID)).findFirst().get().WorkAreaID;
		}
		return wResult;
	}

	public static List<Integer> LFS_GetCheckerIDList(int wWorkAreaID) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			if (APSConstans.GetLFSWorkAreaCheckerList().stream().anyMatch(p -> p.WorkAreaID == wWorkAreaID)) {
				List<LFSWorkAreaChecker> wList = APSConstans.GetLFSWorkAreaCheckerList().stream()
						.filter(p -> p.WorkAreaID == wWorkAreaID).collect(Collectors.toList());
				for (LFSWorkAreaChecker wLFSWorkAreaChecker : wList) {
					wResult.addAll(wLFSWorkAreaChecker.CheckerIDList);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	// region 工区工位全局数据
	private static Calendar RefreshWorkAreaStationTime = Calendar.getInstance();

	private static Map<Integer, LFSWorkAreaStation> LFSWorkAreaStationDic = new HashMap<Integer, LFSWorkAreaStation>();

	public static synchronized Map<Integer, LFSWorkAreaStation> GetLFSWorkAreaStationList() {
		if (LFSWorkAreaStationDic == null || LFSWorkAreaStationDic.size() <= 0
				|| RefreshWorkAreaStationTime.compareTo(Calendar.getInstance()) < 0) {
			List<LFSWorkAreaStation> wLFSWorkAreaStationList = LFSServiceImpl.getInstance()
					.LFS_QueryWorkAreaStationList(BaseDAO.SysAdmin).List(LFSWorkAreaStation.class);
			if (wLFSWorkAreaStationList != null && wLFSWorkAreaStationList.size() > 0) {
				LFSWorkAreaStationDic = wLFSWorkAreaStationList.stream()
						.collect(Collectors.toMap(p -> p.StationID, p -> p, (o1, o2) -> o1));
			}
			RefreshWorkAreaStationTime = Calendar.getInstance();
			RefreshWorkAreaStationTime.add(Calendar.MINUTE, 3);
		}
		return LFSWorkAreaStationDic;
	}

	public static LFSWorkAreaStation GetLFSWorkAreaStation(int wID) {
		LFSWorkAreaStation wResult = new LFSWorkAreaStation();
		if (APSConstans.GetLFSWorkAreaStationList().containsKey(wID)) {
			if (APSConstans.GetLFSWorkAreaStationList().get(wID) != null) {
				wResult = APSConstans.GetLFSWorkAreaStationList().get(wID);
			}
		}
		return wResult;
	}
	// endRegion

	/**
	 * 工艺路线全局数据
	 */
	private static Calendar RefreshRouteTime = Calendar.getInstance();

	private static List<FPCRoute> FPCRouteList = new ArrayList<FPCRoute>();

	public static int mShiftID = 0;

	public static int mDayPlanShiftID = 0;

	public static synchronized List<FPCRoute> GetFPCRouteList() {
		if (FPCRouteList == null || FPCRouteList.size() <= 0
				|| RefreshRouteTime.compareTo(Calendar.getInstance()) < 0) {
//			List<FPCRoute> wFPCRouteList = FMCServiceImpl.getInstance().FPC_QueryRouteList(BaseDAO.SysAdmin, -1, -1, -1)
//					.List(FPCRoute.class);
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<FPCRoute> wFPCRouteList = FPCRouteDAO.getInstance().SelectList(BaseDAO.SysAdmin, -1, -1, -1, -1,
					wErrorCode);
			if (wFPCRouteList != null && wFPCRouteList.size() > 0) {
				wFPCRouteList = wFPCRouteList.stream().filter(p -> p.Active == 1).collect(Collectors.toList());
			}
			RefreshRouteTime = Calendar.getInstance();
			RefreshRouteTime.add(Calendar.MINUTE, 3);
			FPCRouteList = wFPCRouteList;
		}
		return FPCRouteList;
	}

	public static FPCRoute GetFPCRoute(int wProductID, int wLineID, int wCustomerID) {
		FPCRoute wResult = new FPCRoute();
		try {
			if (APSConstans.GetFPCRouteList().stream().anyMatch(p -> p.ProductID == wProductID && p.LineID == wLineID
					&& p.CustomerID == wCustomerID && p.IsStandard == 1)) {
				return APSConstans.GetFPCRouteList().stream().filter(p -> p.ProductID == wProductID
						&& p.LineID == wLineID && p.CustomerID == wCustomerID && p.IsStandard == 1).findFirst().get();
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

//	// endRegion
//	public static List<FPCRoute> mFPCRouteList = new ArrayList<FPCRoute>();
//	// 全局工艺工位数据
//	public static List<FPCRoutePart> mFPCRoutePartList = new ArrayList<FPCRoutePart>();
//	// 全局工艺工序数据
//	public static List<FPCRoutePartPoint> mFPCRoutePartPointList = new ArrayList<FPCRoutePartPoint>();

	// region 单位全局数据
	private static Calendar RefreshUnitTime = Calendar.getInstance();

	private static Map<Integer, CFGUnit> CFGUnitDic = new HashMap<Integer, CFGUnit>();

	public static synchronized Map<Integer, CFGUnit> GetCFGUnitList() {
		if (CFGUnitDic == null || CFGUnitDic.size() <= 0 || RefreshUnitTime.compareTo(Calendar.getInstance()) < 0) {
			List<CFGUnit> wCFGUnitList = CoreServiceImpl.getInstance().CFG_QueryUnitList(BaseDAO.SysAdmin)
					.List(CFGUnit.class);
			if (wCFGUnitList != null && wCFGUnitList.size() > 0) {
				CFGUnitDic = wCFGUnitList.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));
			}
			RefreshUnitTime = Calendar.getInstance();
			RefreshUnitTime.add(Calendar.MINUTE, 3);
		}
		return CFGUnitDic;
	}

	public static CFGUnit GetCFGUnit(int wID) {
		if (APSConstans.GetCFGUnitList().containsKey(wID)) {
			if (APSConstans.GetCFGUnitList().get(wID) != null) {
				return APSConstans.GetCFGUnitList().get(wID);
			}
		}
		return new CFGUnit();
	}

	public static CFGUnit GetCFGUnit(String wUnit) {
		for (CFGUnit wCFGUnit : APSConstans.GetCFGUnitList().values()) {
			if (wCFGUnit.Name.equals(wUnit))
				return wCFGUnit;
		}
		return new CFGUnit();
	}

	public static String GetCFGUnitName(int wID) {
		String wResult = "";
		if (APSConstans.GetCFGUnitList().containsKey(wID)) {
			if (APSConstans.GetCFGUnitList().get(wID) != null) {
				wResult = APSConstans.GetCFGUnitList().get(wID).getName();
			}
		}
		return wResult;
	}
	// endRegion

	public synchronized static void SetBizTask(BPMTaskBase wBPMTaskBase) {
		try {
			switch (BPMEventModule.getEnumType(wBPMTaskBase.FlowType)) {
			case AttemptRun:
				SFCAttemptRunTaskResource.add((SFCAttemptRun) wBPMTaskBase);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	public static BPMResource<SFCAttemptRun> SFCAttemptRunTaskResource = new BPMResource<SFCAttemptRun>();

	/**
	 * 需要触发质量日计划和生产日计划的排程版本
	 */
	public static APSSchedulingVersion mAPSSchedulingVersion = null;

	/**
	 * 获取名称
	 */
	public static String GetName(String personID) {
		String wResult = "";
		try {
			List<String> wNames = new ArrayList<String>();
			String[] wIDs = personID.split(",");
			for (String wID : wIDs) {
				Integer wUserID = StringUtils.parseInt(wID);

				String wName = APSConstans.GetBMSEmployeeName(wUserID);
				if (StringUtils.isNotEmpty(wName)) {
					wNames.add(wName);
				}
			}
			wResult = StringUtils.Join(",", wNames);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 自动创建台车BOM
	 */
//	public static List<Integer> mIBomOrderList = new ArrayList<Integer>();

	/**
	 * 自动更新台车BOM
	 */
//	public static List<Integer> mUBomOrderList = new ArrayList<Integer>();

	/**
	 * 自动更新台车订单
	 */
//	public static List<OMSOrder> mUOrderList = new ArrayList<OMSOrder>();
}
