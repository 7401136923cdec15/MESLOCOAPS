package com.mes.loco.aps.server.serviceimpl.dao.sfc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.sfc.SFCRepairItem;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class SFCRepairItemDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(SFCRepairItemDAO.class);

	private static SFCRepairItemDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wSFCRepairItem
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, SFCRepairItem wSFCRepairItem, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wSFCRepairItem == null)
				return 0;

			String wSQL = "";
			if (wSFCRepairItem.getID() <= 0) {
				wSQL = StringUtils.Format("INSERT INTO {0}.sfc_repairitem(Code,Name,Detail,PartID,TechnicianIDs,"
						+ "TechnicianID,Result,Remark,TaskID,ItemTaskID) VALUES(:Code,:Name,:Detail,:PartID,:TechnicianIDs,"
						+ ":TechnicianID,:Result,:Remark,:TaskID,:ItemTaskID);", wInstance.Result);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.sfc_repairitem SET Code = :Code,Name = :Name,Detail = :Detail,"
								+ "PartID = :PartID,TechnicianIDs = :TechnicianIDs,TechnicianID = :TechnicianID,"
								+ "Result = :Result,Remark = :Remark,ItemTaskID=:ItemTaskID WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wSFCRepairItem.ID);
			wParamMap.put("Code", wSFCRepairItem.Code);
			wParamMap.put("Name", wSFCRepairItem.Name);
			wParamMap.put("Detail", wSFCRepairItem.Detail);
			wParamMap.put("PartID", wSFCRepairItem.PartID);
			wParamMap.put("TechnicianIDs", wSFCRepairItem.TechnicianIDs);
			wParamMap.put("TechnicianID", wSFCRepairItem.TechnicianID);
			wParamMap.put("Result", wSFCRepairItem.Result);
			wParamMap.put("Remark", wSFCRepairItem.Remark);
			wParamMap.put("TaskID", wSFCRepairItem.TaskID);
			wParamMap.put("ItemTaskID", wSFCRepairItem.ItemTaskID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wSFCRepairItem.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wSFCRepairItem.setID(wResult);
			} else {
				wResult = wSFCRepairItem.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除集合
	 * 
	 * @param wList
	 */
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<SFCRepairItem> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (SFCRepairItem wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.sfc_repairitem WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查单条
	 * 
	 * @return
	 */
	public SFCRepairItem SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		SFCRepairItem wResult = new SFCRepairItem();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<SFCRepairItem> wList = SelectList(wLoginUser, wID, "", -1, -1, -1, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<SFCRepairItem> SelectList(BMSEmployee wLoginUser, int wID, String wCode, int wPartID, int wResult,
			int wTaskID, OutResult<Integer> wErrorCode) {
		List<SFCRepairItem> wResultList = new ArrayList<SFCRepairItem>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.sfc_repairitem WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wTaskID <= 0 or :wTaskID = TaskID ) "
					+ "and ( :wCode is null or :wCode = '''' or :wCode = Code ) "
					+ "and ( :wPartID <= 0 or :wPartID = PartID ) " + "and ( :wResult <= 0 or :wResult = Result );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wCode", wCode);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wResult", wResult);
			wParamMap.put("wTaskID", wTaskID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<SFCRepairItem> SelectListByIDList(BMSEmployee wLoginUser, List<Integer> wIDList,
			OutResult<Integer> wErrorCode) {
		List<SFCRepairItem> wResultList = new ArrayList<SFCRepairItem>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wIDList == null || wIDList.size() <= 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.sfc_repairitem WHERE  1=1  "
							+ "and ( :wIDs is null or :wIDs = '''' or ID in ({1}));",
					wInstance.Result, wIDList.size() > 0 ? StringUtils.Join(",", wIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wIDList", StringUtils.Join(",", wIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SetValue(wResultList, wQueryResult);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 赋值
	 */
	private void SetValue(List<SFCRepairItem> wResultList, List<Map<String, Object>> wQueryResult) {
		try {
			for (Map<String, Object> wReader : wQueryResult) {
				SFCRepairItem wItem = new SFCRepairItem();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.TaskID = StringUtils.parseInt(wReader.get("TaskID"));
				wItem.ItemTaskID = StringUtils.parseInt(wReader.get("ItemTaskID"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.Name = StringUtils.parseString(wReader.get("Name"));
				wItem.Detail = StringUtils.parseString(wReader.get("Detail"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.TechnicianIDs = StringUtils.parseString(wReader.get("TechnicianIDs"));
				wItem.TechnicianID = StringUtils.parseInt(wReader.get("TechnicianID"));
				wItem.Result = StringUtils.parseInt(wReader.get("Result"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));

				wItem.TechnicianName = APSConstans.GetBMSEmployeeName(wItem.TechnicianID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取最新的编码
	 */
	public String GetNewCode(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			// 本月时间
			int wYear = Calendar.getInstance().get(Calendar.YEAR);
			int wMonth = Calendar.getInstance().get(Calendar.MONTH);
			Calendar wSTime = Calendar.getInstance();
			wSTime.set(wYear, wMonth, 1, 0, 0, 0);
			Calendar wETime = Calendar.getInstance();
			wETime.set(wYear, wMonth + 1, 1, 23, 59, 59);
			wETime.add(Calendar.DATE, -1);

			String wSQL = StringUtils.Format(
					"select count(*)+1 as Number from {0}.sfc_repairitem where CreateTime > :wSTime and CreateTime < :wETime;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();
			wParamMap.put("wSTime", wSTime);
			wParamMap.put("wETime", wETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wNumber = 0;
			for (Map<String, Object> wReader : wQueryResult) {
				if (wReader.containsKey("Number")) {
					wNumber = StringUtils.parseInt(wReader.get("Number"));
					break;
				}
			}

			wResult = StringUtils.Format("RI{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
					String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1),
					String.format("%04d", wNumber));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private SFCRepairItemDAO() {
		super();
	}

	public static SFCRepairItemDAO getInstance() {
		if (Instance == null)
			Instance = new SFCRepairItemDAO();
		return Instance;
	}
}
