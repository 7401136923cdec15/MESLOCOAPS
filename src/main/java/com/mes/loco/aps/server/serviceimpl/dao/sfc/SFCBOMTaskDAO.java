package com.mes.loco.aps.server.serviceimpl.dao.sfc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mes.loco.aps.server.service.mesenum.BFCMessageType;
import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.NCRLevel;
import com.mes.loco.aps.server.service.mesenum.SAPType;
import com.mes.loco.aps.server.service.mesenum.SFCBOMTaskResponsibility;
import com.mes.loco.aps.server.service.mesenum.SFCBOMTaskReviewComments;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bfc.BFCMessage;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.bpm.BPMTaskBase;
import com.mes.loco.aps.server.service.po.mss.MSSBOM;
import com.mes.loco.aps.server.service.po.mss.MSSMaterial;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.sfc.MESStatusDictionary;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTaskItem;
import com.mes.loco.aps.server.service.po.tcm.TCMMaterialChangeItems;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.CoreServiceImpl;
import com.mes.loco.aps.server.serviceimpl.WMSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.TaskBaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

/**
 * 偶换件不合格评审
 * 
 * @author ShrisJava
 *
 */
public class SFCBOMTaskDAO extends BaseDAO implements TaskBaseDAO {

	private static Logger logger = LoggerFactory.getLogger(SFCBOMTaskDAO.class);

	private static SFCBOMTaskDAO Instance = null;

	private SFCBOMTaskDAO() {
		super();
	}

	public static SFCBOMTaskDAO getInstance() {
		if (Instance == null)
			Instance = new SFCBOMTaskDAO();
		return Instance;
	}

	/**
	 * 添加或修改
	 * 
	 * @param wSFCBOMTask
	 * @return
	 */
	public SFCBOMTask Update(BMSEmployee wLoginUser, SFCBOMTask wSFCBOMTask, OutResult<Integer> wErrorCode) {
		SFCBOMTask wResult = new SFCBOMTask();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wSFCBOMTask == null)
				return wResult;

			if (wSFCBOMTask.FollowerID == null) {
				wSFCBOMTask.FollowerID = new ArrayList<Integer>();
			}

			String wSQL = "";
			if (wSFCBOMTask.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.sfc_bomtask(Code,FlowType,FlowID,UpFlowID,FollowerID,Status,"
						+ "StatusText,CreateTime,SubmitTime,OrderID,PartID,PartPointID,BOMID,"
						+ "BOMItemID,MaterialID,MaterialNumber,UnitID,Level,Disposal,ReviewComments,Responsibility,"
						+ "CraftsmanIDs,TechnicalEngineerIDs,SapType,SAPStatus,SAPStatusText,SRPartID,SRPartName,SRProductNo,ConfirmedLevels,ImageUrl,"
						+ "IsLGL,IsQualityLoss,QualityLossBig,QualityLossSmall) VALUES(:Code,:FlowType,:FlowID,"
						+ ":UpFlowID,:FollowerID,:Status,:StatusText,:CreateTime,:SubmitTime,:OrderID,"
						+ ":PartID,:PartPointID,:BOMID,:BOMItemID,:MaterialID,:MaterialNumber,:UnitID,:Level,"
						+ ":Disposal,:ReviewComments,:Responsibility,:CraftsmanIDs,:TechnicalEngineerIDs,:SapType,"
						+ ":SAPStatus,:SAPStatusText,:SRPartID,:SRPartName,:SRProductNo,:ConfirmedLevels,:ImageUrl,:IsLGL,:IsQualityLoss,:QualityLossBig,:QualityLossSmall);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format("UPDATE {0}.sfc_bomtask SET Code = :Code,FlowType = :FlowType,"
						+ "FlowID = :FlowID,UpFlowID = :UpFlowID,FollowerID = :FollowerID,"
						+ "Status = :Status,StatusText = :StatusText,CreateTime = :CreateTime,"
						+ "SubmitTime = now(),OrderID = :OrderID,PartID = :PartID,"
						+ "PartPointID = :PartPointID,BOMID = :BOMID,BOMItemID = :BOMItemID,"
						+ "MaterialID = :MaterialID,MaterialNumber = :MaterialNumber,UnitID = :UnitID,"
						+ "Level = :Level,Disposal=:Disposal,ReviewComments=:ReviewComments,"
						+ "Responsibility=:Responsibility,CraftsmanIDs=:CraftsmanIDs,"
						+ "TechnicalEngineerIDs=:TechnicalEngineerIDs,SapType=:SapType,"
						+ "SAPStatus=:SAPStatus,SAPStatusText=:SAPStatusText,SRPartID=:SRPartID,"
						+ "SRPartName=:SRPartName,SRProductNo=:SRProductNo,ConfirmedLevels=:ConfirmedLevels,ImageUrl=:ImageUrl,"
						+ "IsLGL=:IsLGL,IsQualityLoss=:IsQualityLoss,QualityLossBig=:QualityLossBig,QualityLossSmall=:QualityLossSmall WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wSFCBOMTask.ID);
			wParamMap.put("Code", wSFCBOMTask.Code);
			wParamMap.put("FlowType", wSFCBOMTask.FlowType);
			wParamMap.put("FlowID", wSFCBOMTask.FlowID);
			wParamMap.put("UpFlowID", wSFCBOMTask.UpFlowID);
			wParamMap.put("FollowerID", StringUtils.Join(",", wSFCBOMTask.FollowerID));
			wParamMap.put("Status", wSFCBOMTask.Status);
			wParamMap.put("StatusText", wSFCBOMTask.StatusText);
			wParamMap.put("CreateTime", wSFCBOMTask.CreateTime);
			wParamMap.put("SubmitTime", wSFCBOMTask.SubmitTime);
			wParamMap.put("OrderID", wSFCBOMTask.OrderID);
			wParamMap.put("PartID", wSFCBOMTask.PartID);
			wParamMap.put("PartPointID", wSFCBOMTask.PartPointID);
			wParamMap.put("BOMID", wSFCBOMTask.BOMID);
			wParamMap.put("BOMItemID", wSFCBOMTask.BOMItemID);
			wParamMap.put("MaterialID", wSFCBOMTask.MaterialID);
			wParamMap.put("MaterialNumber", wSFCBOMTask.MaterialNumber);
			wParamMap.put("UnitID", wSFCBOMTask.UnitID);
			wParamMap.put("Level", wSFCBOMTask.Level);
			wParamMap.put("Disposal", wSFCBOMTask.Disposal);
			wParamMap.put("ReviewComments", wSFCBOMTask.ReviewComments);
			wParamMap.put("Responsibility", wSFCBOMTask.Responsibility);
			wParamMap.put("CraftsmanIDs", wSFCBOMTask.CraftsmanIDs);
			wParamMap.put("TechnicalEngineerIDs", wSFCBOMTask.TechnicalEngineerIDs);
			wParamMap.put("SapType", wSFCBOMTask.SapType);
			wParamMap.put("SAPStatus", wSFCBOMTask.SAPStatus);
			wParamMap.put("SAPStatusText", wSFCBOMTask.SAPStatusText);
			wParamMap.put("SRPartID", wSFCBOMTask.SRPartID);
			wParamMap.put("SRPartName", wSFCBOMTask.SRPartName);
			wParamMap.put("SRProductNo", wSFCBOMTask.SRProductNo);
			wParamMap.put("ConfirmedLevels", wSFCBOMTask.ConfirmedLevels);
			wParamMap.put("ImageUrl", wSFCBOMTask.ImageUrl);
			wParamMap.put("IsLGL", wSFCBOMTask.IsLGL);
			wParamMap.put("IsQualityLoss", wSFCBOMTask.IsQualityLoss);
			wParamMap.put("QualityLossBig", wSFCBOMTask.QualityLossBig);
			wParamMap.put("QualityLossSmall", wSFCBOMTask.QualityLossSmall);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wSFCBOMTask.getID() <= 0) {
				wSFCBOMTask.setID(keyHolder.getKey().intValue());
			}
			wResult = wSFCBOMTask;
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	public List<SFCBOMTask> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID, String wFollowerID,
			int wOrderID, int wPartID, int wPartPointID, int wBOMID, int wLevel, List<Integer> wStateIDList,
			Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		return this.SelectList(wLoginUser, wID, wCode, wUpFlowID, wFollowerID, wOrderID, wPartID, wPartPointID, wBOMID,
				wLevel, wStateIDList, null, wStartTime, wEndTime, wErrorCode);
	}

	public List<SFCBOMTask> SelectList(BMSEmployee wLoginUser, int wOrderID, int wPartID, int wPartPointID, int wBOMID,
			int wLevel, List<Integer> wStateIDList, List<Integer> wNoStateIDList, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		return this.SelectList(wLoginUser, -1, "", -1, "", wOrderID, wPartID, wPartPointID, wBOMID, wLevel,
				wStateIDList, wNoStateIDList, wStartTime, wEndTime, wErrorCode);
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	private List<SFCBOMTask> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wUpFlowID,
			String wFollowerID, int wOrderID, int wPartID, int wPartPointID, int wBOMID, int wLevel,
			List<Integer> wStateIDList, List<Integer> wNoStateIDList, Calendar wStartTime, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		List<SFCBOMTask> wResultList = new ArrayList<SFCBOMTask>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wFollowerID == null) {
				wFollowerID = "";
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<Integer>();
			}
			wStateIDList.removeIf(p -> p < 0);
			if (wNoStateIDList == null) {
				wNoStateIDList = new ArrayList<Integer>();
			}
			wNoStateIDList.removeIf(p -> p < 0);

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sfc_bomtask WHERE  1=1  "
					+ " and ( :wID <= 0 or :wID = ID ) " + "and ( :wCode is null or :wCode = '''' or :wCode = Code ) "
					+ " and ( :wUpFlowID <= 0 or :wUpFlowID = UpFlowID ) "
					+ " and ( :wFollowerID = '''' or  find_in_set( :wFollowerID,replace(FollowerID,'';'','','') ) ) "
					+ " and ( :wOrderID <= 0 or :wOrderID = OrderID ) " + "and ( :wPartID <= 0 or :wPartID = PartID ) "
					+ " and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= SubmitTime) "
					+ " and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) "
					+ " and ( :wPartPointID <= 0 or :wPartPointID = PartPointID ) and ( :wBOMID <= 0 or :wBOMID = BOMID ) "
					+ " and ( :wLevel <= 0 or :wLevel = Level ) and ( :wStatus = '''' or Status in ({1}))"
					+ " and ( :wNoStatus = '''' or Status not in ({2}));", wInstance.Result,
					wStateIDList.size() > 0 ? StringUtils.Join(",", wStateIDList) : "0",
					wNoStateIDList.size() > 0 ? StringUtils.Join(",", wNoStateIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCode", wCode);
			wParamMap.put("wUpFlowID", wUpFlowID);
			wParamMap.put("wFollowerID", wFollowerID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wPartPointID", wPartPointID);
			wParamMap.put("wBOMID", wBOMID);
			wParamMap.put("wLevel", wLevel);
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));
			wParamMap.put("wNoStatus", StringUtils.Join(",", wNoStateIDList));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);
			SetValue(wLoginUser, wResultList, wQueryResult, wErrorCode);

			// ①子物料分等级返回
			for (SFCBOMTask wTask : wResultList) {
				if (wTask.SFCBOMTaskItemList == null || wTask.SFCBOMTaskItemList.size() <= 0) {
					continue;
				}

				wTask.SFCBOMTaskItemAList = wTask.SFCBOMTaskItemList.stream().filter(p -> p.Level == 1)
						.collect(Collectors.toList());
				wTask.SFCBOMTaskItemBList = wTask.SFCBOMTaskItemList.stream().filter(p -> p.Level == 2)
						.collect(Collectors.toList());
				wTask.SFCBOMTaskItemCList = wTask.SFCBOMTaskItemList.stream().filter(p -> p.Level == 3)
						.collect(Collectors.toList());
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 获取处理人名称(多人)
	 */
	private String GetNames(List<Integer> wIDList) {
		String wResult = "";
		try {
			if (wIDList == null || wIDList.size() <= 0) {
				return wResult;
			}

			List<String> wNames = new ArrayList<String>();
			wIDList.forEach(p -> {
				if (StringUtils.isNotEmpty(APSConstans.GetBMSEmployeeName(p))) {
					wNames.add(APSConstans.GetBMSEmployeeName(p));
				}
			});

			if (wNames.size() > 0) {
				wResult = StringUtils.Join(",", wNames);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * ID集合获取任务集合
	 */
	public List<SFCBOMTask> SelectList(BMSEmployee wLoginUser, List<Integer> wTaskIDList, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SFCBOMTask> wResultList = new ArrayList<SFCBOMTask>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wTaskIDList == null || wTaskIDList.size() <= 0) {
				wTaskIDList = new ArrayList<Integer>();
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sfc_bomtask WHERE  1=1  "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= SubmitTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) "
					+ "and ( :wIDs is null or :wIDs = '''' or ID in ({1}));", wInstance.Result,
					wTaskIDList.size() > 0 ? StringUtils.Join(",", wTaskIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wIDs", StringUtils.Join(",", wTaskIDList));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wLoginUser, wResultList, wQueryResult, wErrorCode);

			for (SFCBOMTask wSFCBOMTask : wResultList) {
				wSFCBOMTask.SFCBOMTaskItemList = SFCBOMTaskItemDAO.getInstance().SelectList(wLoginUser, -1,
						wSFCBOMTask.ID, wErrorCode);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * ID集合获取任务集合
	 */
	public List<SFCBOMTask> SelectListByCreateTime(BMSEmployee wLoginUser, List<Integer> wTaskIDList,
			Calendar wStartTime, Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SFCBOMTask> wResultList = new ArrayList<SFCBOMTask>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wTaskIDList == null || wTaskIDList.size() <= 0) {
				wTaskIDList = new ArrayList<Integer>();
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sfc_bomtask WHERE  1=1  "
					+ "and ( :wStartTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wStartTime <= CreateTime) "
					+ "and ( :wEndTime <= str_to_date(''2010-01-01'', ''%Y-%m-%d'') or :wEndTime >= CreateTime) "
					+ "and ( :wIDs is null or :wIDs = '''' or ID in ({1}));", wInstance.Result,
					wTaskIDList.size() > 0 ? StringUtils.Join(",", wTaskIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wIDs", StringUtils.Join(",", wTaskIDList));
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wLoginUser, wResultList, wQueryResult, wErrorCode);

			for (SFCBOMTask wSFCBOMTask : wResultList) {
				wSFCBOMTask.SFCBOMTaskItemList = SFCBOMTaskItemDAO.getInstance().SelectList(wLoginUser, -1,
						wSFCBOMTask.ID, wErrorCode);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 获取最新的编码
	 */
	public synchronized String GetNewCode(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			// 本月时间
//			int wYear = Calendar.getInstance().get(Calendar.YEAR);
//			int wMonth = Calendar.getInstance().get(Calendar.MONTH);
//			Calendar wSTime = Calendar.getInstance();
//			wSTime.set(wYear, wMonth, 1, 0, 0, 0);
//			Calendar wETime = Calendar.getInstance();
//			wETime.set(wYear, wMonth + 1, 1, 23, 59, 59);
//			wETime.add(Calendar.DATE, -1);

//			String wSQL = StringUtils.Format(
//					"select count(*)+1 as Number from {0}.sfc_bomtask where CreateTime > :wSTime and CreateTime < :wETime;",
//					wInstance.Result);

//			SELECT Code from iplantlocoaps.sfc_bomtask where ID in (SELECT Max(ID) FROM iplantlocoaps.sfc_bomtask);
			String wSQL = StringUtils.Format(
					"SELECT Code from {0}.sfc_bomtask where ID in (SELECT Max(ID) FROM {0}.sfc_bomtask);",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();
//			wParamMap.put("wSTime", wSTime);
//			wParamMap.put("wETime", wETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wNumber = 1;
			for (Map<String, Object> wReader : wQueryResult) {
//				if (wReader.containsKey("Number")) {
//					wNumber = StringUtils.parseInt(wReader.get("Number"));
//					break;
//				}
//				RP2021110001

				String wCode = StringUtils.parseString(wReader.get("Code"));
				// 截取流水号
				String wSerialNumber = wCode.substring(wCode.length() - 4);
				int wSN = StringUtils.parseInt(wSerialNumber);
				// 截取月份
				String wMonthStr = wCode.substring(wCode.length() - 6, wCode.length() - 4);
				int wMonth = StringUtils.parseInt(wMonthStr);
				if (Calendar.getInstance().get(Calendar.MONTH) != wMonth - 1) {
					wNumber = 1;
				} else {
					wNumber = wSN + 1;
				}
			}

			wResult = StringUtils.Format("RP{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
					String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1),
					String.format("%04d", wNumber));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 赋值
	 */
	private void SetValue(BMSEmployee wLoginUser, List<SFCBOMTask> wResultList, List<Map<String, Object>> wQueryResult,
			OutResult<Integer> wErrorCode) {
		try {

			Map<Integer, OMSOrder> wOrderMap = new HashMap<Integer, OMSOrder>();
			if (wQueryResult.size() > 0) {
				List<Integer> wOrderIDList = wQueryResult.stream().filter(p -> p.containsKey("OrderID"))
						.map(p -> StringUtils.parseInt(p.get("OrderID"))).distinct().collect(Collectors.toList());

				if (wOrderIDList.size() > 0) {
					wOrderMap = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wOrderIDList, wErrorCode)
							.stream().collect(Collectors.toMap(p -> p.ID, p -> p));
				}

			}
			OMSOrder wOMSOrder = null;

			for (Map<String, Object> wReader : wQueryResult) {
				SFCBOMTask wItem = new SFCBOMTask();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.FlowType = StringUtils.parseInt(wReader.get("FlowType"));
				wItem.FlowID = StringUtils.parseInt(wReader.get("FlowID"));
				wItem.UpFlowID = StringUtils.parseInt(wReader.get("UpFlowID"));
				wItem.FollowerID = StringUtils
						.parseIntList(StringUtils.parseString(wReader.get("FollowerID")).split(",|;"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.StatusText = StringUtils.parseString(wReader.get("StatusText"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.SubmitTime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartPointID = StringUtils.parseInt(wReader.get("PartPointID"));
				wItem.BOMID = StringUtils.parseInt(wReader.get("BOMID"));
				wItem.BOMItemID = StringUtils.parseInt(wReader.get("BOMItemID"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.MaterialNumber = StringUtils.parseDouble(wReader.get("MaterialNumber"));
				wItem.UnitID = StringUtils.parseInt(wReader.get("UnitID"));
				wItem.Level = StringUtils.parseInt(wReader.get("Level"));
				wItem.Disposal = StringUtils.parseString(wReader.get("Disposal"));
				wItem.CraftsmanIDs = StringUtils.parseString(wReader.get("CraftsmanIDs"));
				wItem.TechnicalEngineerIDs = StringUtils.parseString(wReader.get("TechnicalEngineerIDs"));
				wItem.ReviewComments = StringUtils.parseInt(wReader.get("ReviewComments"));
				wItem.Responsibility = StringUtils.parseInt(wReader.get("Responsibility"));
				wItem.SapType = StringUtils.parseInt(wReader.get("SapType"));

				wItem.SAPStatus = StringUtils.parseInt(wReader.get("SAPStatus"));
				wItem.SAPStatusText = StringUtils.parseString(wReader.get("SAPStatusText"));

				wItem.SRPartID = StringUtils.parseInt(wReader.get("SRPartID"));
				wItem.IsLGL = StringUtils.parseInt(wReader.get("IsLGL"));
				wItem.SRPartName = StringUtils.parseString(wReader.get("SRPartName"));
				wItem.SRProductNo = StringUtils.parseString(wReader.get("SRProductNo"));
				wItem.ConfirmedLevels = StringUtils.parseString(wReader.get("ConfirmedLevels"));
				wItem.ImageUrl = StringUtils.parseString(wReader.get("ImageUrl"));

				wItem.IsQualityLoss = StringUtils.parseInt(wReader.get("IsQualityLoss"));
				wItem.QualityLossBig = StringUtils.parseString(wReader.get("QualityLossBig"));
				wItem.QualityLossSmall = StringUtils.parseString(wReader.get("QualityLossSmall"));

				wItem.SapTypeName = SAPType.getEnumType(wItem.SapType).getLable();

				wItem.UpFlowName = APSConstans.GetBMSEmployeeName(wItem.UpFlowID);
				wItem.FollowerName = this.GetNames(wItem.FollowerID);

				wOMSOrder = wOrderMap.containsKey(wItem.OrderID) ? wOrderMap.get(wItem.OrderID) : new OMSOrder();

				wItem.WBSNo = wOMSOrder.WBSNo;
				wItem.ProductID = wOMSOrder.ProductID;
				wItem.ProductNo = wOMSOrder.ProductNo;
				wItem.CustomerID = wOMSOrder.CustomerID;
				wItem.CustomerCode = APSConstans.GetCRMCustomer(wItem.CustomerID).CustomerCode;
				wItem.CustomerName = wOMSOrder.Customer;
				wItem.PartNo = wOMSOrder.PartNo;
				wItem.LineID = wOMSOrder.LineID;
				wItem.LineName = wOMSOrder.LineName;
				wItem.RouteID = wOMSOrder.RouteID;

				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.PartCode = APSConstans.GetFPCPart(wItem.PartID).Code;
				wItem.PartPointName = APSConstans.GetFPCPartPointName(wItem.PartPointID);

				if (wItem.MaterialID > 0) {
					MSSMaterial wMaterial = new MSSMaterial();
					APIResult wAPIResult = WMSServiceImpl.getInstance().MSS_QueryMaterialByID(BaseDAO.SysAdmin,
							wItem.MaterialID);
					if (wAPIResult != null) {
						wMaterial = wAPIResult.Info(MSSMaterial.class);
					}

					wItem.MaterialNo = wMaterial.MaterialNo;
					wItem.MaterialName = wMaterial.MaterialName;
				}

				wItem.UnitText = APSConstans.GetCFGUnitName(wItem.UnitID);
				wItem.LevelName = NCRLevel.getEnumType(wItem.Level).getLable();
				wItem.CraftsmanNames = this.GetNames(StringUtils.parseIntList(wItem.CraftsmanIDs.split(",|;")));
				wItem.TechnicalEngineerNames = this
						.GetNames(StringUtils.parseIntList(wItem.TechnicalEngineerIDs.split(",|;")));

//				wItem.SFCBOMTaskItemList = SFCBOMTaskItemDAO.getInstance().SelectList(wLoginUser, -1, wItem.ID,
//						wErrorCode);

				wItem.LevelName = NCRLevel.getEnumType(wItem.Level).getLable();
				wItem.ResponsibilityName = SFCBOMTaskResponsibility.getEnumType(wItem.Responsibility).getLable();
				wItem.ReviewCommentsName = SFCBOMTaskReviewComments.getEnumType(wItem.ReviewComments).getLable();

				wResultList.add(wItem);
			}

			HandleStatusText(wResultList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 处理状态文本
	 */
	private void HandleStatusText(List<SFCBOMTask> wResultList) {
		try {
			Map<String, String> wMap = new HashMap<String, String>();

			// 查询状态字典
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<MESStatusDictionary> wList = MESStatusDictionaryDAO.getInstance().SelectList(BaseDAO.SysAdmin,
					new ArrayList<Integer>(Arrays.asList(8201)), wErrorCode);

			for (MESStatusDictionary wMESStatusDictionary : wList) {
				if (!wMap.containsKey(wMESStatusDictionary.Key)) {
					wMap.put(wMESStatusDictionary.Key, wMESStatusDictionary.Value);
				}
			}

			for (SFCBOMTask wSendNCRTask : wResultList) {
				if (wMap.containsKey(wSendNCRTask.StatusText)) {
					wSendNCRTask.StatusText = wMap.get(wSendNCRTask.StatusText);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public List<BPMTaskBase> BPM_GetUndoTaskList(BMSEmployee wLoginUser, int wResponsorID,
			OutResult<Integer> wErrorCode) {
		List<SFCBOMTask> wResult = new ArrayList<SFCBOMTask>();
		try {
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.OccasionNCR.getValue(), -1,
							BFCMessageType.Task.getValue(), 0, -1, null, null)
					.List(BFCMessage.class);
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.OccasionNCR.getValue(), -1, BFCMessageType.Task.getValue(), 1, -1, null, null)
					.List(BFCMessage.class));
			wMessageList.addAll(CoreServiceImpl.getInstance().BFC_GetMessageList(wLoginUser, wLoginUser.getID(),
					BPMEventModule.OccasionNCR.getValue(), -1, BFCMessageType.Task.getValue(), 2, -1, null, null)
					.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			// 所有未完成的任务
			Map<Integer, SFCBOMTask> wTaskMap = new HashMap<Integer, SFCBOMTask>();
			if (wTaskIDList != null && wTaskIDList.size() > 0) {
				List<SFCBOMTask> wMTCTaskListTemp = this.SelectList(wLoginUser, wTaskIDList, null, null, wErrorCode);

				wTaskMap = wMTCTaskListTemp.stream().collect(Collectors.toMap(p -> p.ID, p -> p, (o1, o2) -> o1));

			}
			SFCBOMTask wTaskTemp = null;
			for (BFCMessage wBFCMessage : wMessageList) {
				if (!wTaskMap.containsKey((int) wBFCMessage.getMessageID()))
					continue;

				wTaskTemp = CloneTool.Clone(wTaskMap.get((int) wBFCMessage.getMessageID()), SFCBOMTask.class);
				wTaskTemp.StepID = wBFCMessage.getStepID();
				wResult.add(wTaskTemp);
			}

			wResult.sort(Comparator.comparing(SFCBOMTask::getSubmitTime).reversed());
			// 剔除任务状态为0的任务（废弃任务）
			if (wResult != null && wResult.size() > 0) {
				wResult = wResult.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.Exception.getValue());
			logger.error(e.toString());
		}
		return new ArrayList<BPMTaskBase>(wResult);
	}

	@Override
	public List<BPMTaskBase> BPM_GetDoneTaskList(BMSEmployee wLoginUser, int wResponsorID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SFCBOMTask> wResult = new ArrayList<SFCBOMTask>();
		wErrorCode.set(0);
		try {
			List<SFCBOMTask> wTaskList = new ArrayList<SFCBOMTask>();
			// 获取所有任务消息
			List<BFCMessage> wMessageList = CoreServiceImpl.getInstance()
					.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.OccasionNCR.getValue(), -1,
							BFCMessageType.Task.getValue(), 3, -1, wStartTime, wEndTime)
					.List(BFCMessage.class);
			wMessageList
					.addAll(CoreServiceImpl.getInstance()
							.BFC_GetMessageList(wLoginUser, wLoginUser.getID(), BPMEventModule.OccasionNCR.getValue(),
									-1, BFCMessageType.Task.getValue(), 4, -1, wStartTime, wEndTime)
							.List(BFCMessage.class));

			List<Integer> wTaskIDList = wMessageList.stream().map(p -> (int) p.MessageID).distinct()
					.collect(Collectors.toList());

			if (wTaskIDList == null || wTaskIDList.size() <= 0) {
				return new ArrayList<BPMTaskBase>(wResult);
			}

			wTaskList = this.SelectList(wLoginUser, wTaskIDList, wStartTime, wEndTime, wErrorCode);

			wTaskList.sort(Comparator.comparing(SFCBOMTask::getSubmitTime).reversed());

			wResult = wTaskList;
			// 剔除任务状态为0的任务（废弃任务）
			if (wResult != null && wResult.size() > 0) {
				wResult = wResult.stream().filter(p -> p.Status != 0).collect(Collectors.toList());
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return new ArrayList<BPMTaskBase>(wResult);
	}

	@Override
	public List<BPMTaskBase> BPM_GetSendTaskList(BMSEmployee wLoginUser, int wResponsorID, Calendar wStartTime,
			Calendar wEndTime, OutResult<Integer> wErrorCode) {
		List<SFCBOMTask> wResult = new ArrayList<SFCBOMTask>();
		try {
			wResult = this.SelectList(wLoginUser, -1, "", wResponsorID, "", -1, -1, -1, -1, -1, null, wStartTime,
					wEndTime, wErrorCode);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return new ArrayList<BPMTaskBase>(wResult);
	}

	@Override
	public BPMTaskBase BPM_UpdateTask(BMSEmployee wLoginUser, BPMTaskBase wTask, OutResult<Integer> wErrorCode) {
		BPMTaskBase wResult = new BPMTaskBase();
		try {
			wResult = this.Update(wLoginUser, (SFCBOMTask) wTask, wErrorCode);

			// 保存bom子项
			if (((SFCBOMTask) wTask).SFCBOMTaskItemList != null && ((SFCBOMTask) wTask).SFCBOMTaskItemList.size() > 0) {
				// 删除减少的数据
				ExecutorService wES = Executors.newFixedThreadPool(1);
				wES.submit(() -> RemoveList(wLoginUser, (SFCBOMTask) wTask));
				wES.shutdown();

				for (SFCBOMTaskItem wSFCBOMTaskItem : ((SFCBOMTask) wTask).SFCBOMTaskItemList) {
					wSFCBOMTaskItem.SFCBOMTaskID = wResult.ID;
					SFCBOMTaskItemDAO.getInstance().Update(wLoginUser, wSFCBOMTaskItem, wErrorCode);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 删除减少的物料
	 */
	private void RemoveList(BMSEmployee wLoginUser, SFCBOMTask wTask) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			List<SFCBOMTaskItem> wList = SFCBOMTaskItemDAO.getInstance().SelectList(wLoginUser, -1, wTask.ID,
					wErrorCode);
			if (wList.size() > 0) {
				wList = wList.stream().filter(p -> !wTask.SFCBOMTaskItemList.stream().anyMatch(q -> q.ID == p.ID))
						.collect(Collectors.toList());
				if (wList.size() > 0) {
					SFCBOMTaskItemDAO.getInstance().DeleteList(wLoginUser, wList, wErrorCode);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public BPMTaskBase BPM_GetTaskInfo(BMSEmployee wLoginUser, int wTaskID, String wCode,
			OutResult<Integer> wErrorCode) {
		SFCBOMTask wResult = new SFCBOMTask();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<SFCBOMTask> wList = SelectList(wLoginUser, wTaskID, wCode, -1, "", -1, -1, -1, -1, -1, null, null,
					null, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);

			wResult.SFCBOMTaskItemList = SFCBOMTaskItemDAO.getInstance().SelectList(wLoginUser, -1, wResult.ID,
					wErrorCode);
			wResult.SFCBOMTaskItemAList = wResult.SFCBOMTaskItemList.stream().filter(p -> p.Level == 1)
					.collect(Collectors.toList());
			wResult.SFCBOMTaskItemBList = wResult.SFCBOMTaskItemList.stream().filter(p -> p.Level == 2)
					.collect(Collectors.toList());
			wResult.SFCBOMTaskItemCList = wResult.SFCBOMTaskItemList.stream().filter(p -> p.Level == 3)
					.collect(Collectors.toList());
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<Integer> GetCompletedListByTime(BMSEmployee wLoginUser, String wShiftID,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

//			String wSQL = StringUtils.Format("SELECT ID FROM {0}.sfc_bomtask where SubmitTime > :wStartTime "
//					+ "and SubmitTime < :wEndTime and ( Status=20 or ConfirmedLevels!='''') order by SubmitTime asc;",
//					wInstance.Result);

			String wSQL = StringUtils.Format("SELECT t2.ID FROM {0}.sfc_bomtask t2,{0}.sfc_bomtaskitem "
					+ "t1 WHERE t1.SFCBOMTaskID=t2.ID AND t1.ShiftID=:ShiftID AND (t2.Status=20 OR t2.ConfirmedLevels != '''') "
					+ "ORDER BY t2.SubmitTime ASC;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ShiftID", wShiftID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				wResult.add(wID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据物料号和物料编码查询偶换件评审单
	 */
	public List<Integer> SelectIDListByMaterial(BMSEmployee wLoginUser, String wMaterialName, String wMaterialNo,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select distinct t1.SFCBOMTaskID from {0}.sfc_bomtaskitem t1," + "{3}.mss_material t2 "
							+ "where t1.materialID=t2.ID "
							+ "and t2.MaterialNo like ''%{1}%'' and t2.MaterialName like ''%{2}%'';",
					wInstance.Result, wMaterialNo, wMaterialName, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wSFCBOMTaskID = StringUtils.parseInt(wReader.get("SFCBOMTaskID"));
				wResult.add(wSFCBOMTaskID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据sourceID获取标准BOMID
	 */
	public MSSBOM APS_QueryBOMID(BMSEmployee wLoginUser, int wSourceID, OutResult<Integer> wErrorCode) {
		MSSBOM wResult = new MSSBOM();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils
					.Format("SELECT t1.BOMID,t2.RouteID,t2.BOMName FROM {0}.mss_bomitem t1,{0}.mss_bom t2 "
							+ "where t1.BOMID=t2.ID and t1.ID=:ID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wSourceID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.ID = StringUtils.parseInt(wReader.get("BOMID"));
				wResult.RouteID = StringUtils.parseInt(wReader.get("RouteID"));
				wResult.BOMName = StringUtils.parseString(wReader.get("BOMName"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据SourceID获取变更数据
	 */
	public TCMMaterialChangeItems TCM_QueryChangeItems(BMSEmployee wLoginUser, int wSourceID,
			OutResult<Integer> wErrorCode) {
		TCMMaterialChangeItems wResult = new TCMMaterialChangeItems();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT BOMNo2 FROM {0}.tcm_materialchangeitems where id=:ID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wSourceID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.BOMNo2 = StringUtils.parseString(wReader.get("BOMNo2"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据BOMNo查询BOM
	 */
	public MSSBOM MSS_QueryIDByNo(BMSEmployee wLoginUser, String wBOMNO, OutResult<Integer> wErrorCode) {
		MSSBOM wResult = new MSSBOM();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID,RouteID,BOMName FROM {0}.mss_bom where BOMNo=:BOMNO;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("BOMNO", wBOMNO);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.ID = StringUtils.parseInt(wReader.get("ID"));
				wResult.RouteID = StringUtils.parseInt(wReader.get("RouteID"));
				wResult.BOMName = StringUtils.parseString(wReader.get("BOMName"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据班组ID获取工区ID
	 */
	public int GetAreaIDByClassID(BMSEmployee wLoginUser, int classID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT ID,Name FROM {0}.bms_department where ID in "
					+ "(SELECT ParentID FROM {0}.bms_department where ID=:ClassID);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ClassID", classID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wID = StringUtils.parseInt(wReader.get("ID"));
				String wName = StringUtils.parseString(wReader.get("Name"));
				if (wName.contains("工区")) {
					wResult = wID;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据日志ID查询已使用过的订单ID集合
	 */
	public List<Integer> SelectedUsedOrderIDList(BMSEmployee wLoginUser, int wLogID, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select OrderList from {0}.tcm_techchangenotice where ChangeLogID=:LogID "
					+ "and Status not in (0,21,22);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("LogID", wLogID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {

				String wOrders = StringUtils.parseString(wReader.get("OrderList"));
				if (StringUtils.isEmpty(wOrders)) {
					continue;
				}
				List<Integer> wIDList = StringUtils.parseIntList(wOrders.split(","));
				for (Integer wOrderID : wIDList) {
					if (wResult.stream().anyMatch(p -> p.intValue() == wOrderID.intValue())) {
						continue;
					}
					wResult.add(wOrderID);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取所有偶换件订单ID集合
	 */
	public List<Integer> GetOrderIDList(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT distinct OrderID FROM {0}.sfc_bomtask where status not in (0,22,21,23);", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wOrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wResult.add(wOrderID);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public void BPM_UpdateCode(BMSEmployee wLoginUser, SFCBOMTask wSFCBOMTask, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("Update {0}.sfc_bomtask set Code=:Code where ID = :ID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("Code", wSFCBOMTask.Code);
			wParamMap.put("ID", wSFCBOMTask.ID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void DeleteDefaultTask(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format(
					"Delete from {0}.sfc_bomtask where Status=0 and UpFlowID=:UpFlowID and ID>0;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("UpFlowID", wLoginUser.ID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void UpdateSubmitTime(BMSEmployee wLoginUser, SFCBOMTask wSFCBOMTask, Calendar wEndTime,
			OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("Update {0}.sfc_bomtask set SubmitTime=:SubmitTime where ID=:ID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("SubmitTime", wEndTime);
			wParamMap.put("ID", wSFCBOMTask.ID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 判断是否存在质量损失台车BOM
	 */
	public boolean IsExistQualityLoss(BMSEmployee wLoginUser, OMSOrder wOrder, OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("select count(*) number from {0}.aps_bomitem "
					+ "where BOMType=9 and OrderID=:OrderID and SAPStatus=1;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrder.ID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wNumber = StringUtils.parseInt(wReader.get("number"));
				if (wNumber > 0) {
					wResult = true;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 根据物料ID查询物料等级
	 */
	public int SFC_QueryLevelByMaterialID(BMSEmployee wLoginUser, int materialID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT BOMID FROM {0}.mss_material WHERE ID=:ID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", materialID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult = StringUtils.parseInt(wReader.get("BOMID"));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public void SFC_UpdateMaterialLevel(BMSEmployee wLoginUser, int level, int wMaterialID,
			OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			String wSQL = StringUtils.Format("UPDATE {0}.mss_material SET BOMID=:BOMID WHERE ID=:ID;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("BOMID", level);
			wParamMap.put("ID", wMaterialID);

			wSQL = this.DMLChange(wSQL);

			nameJdbcTemplate.update(wSQL, wParamMap);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
	
	
}
