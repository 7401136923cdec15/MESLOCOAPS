package com.mes.loco.aps.server.serviceimpl.dao.sfc;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.SFCSequentialInfoType;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.sfc.SFCOrderForm;
import com.mes.loco.aps.server.service.po.sfc.SFCSequentialInfo;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCOrderFormDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import java.text.MessageFormat;
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

public class SFCOrderFormDAO extends BaseDAO {
	private static Logger logger = LoggerFactory.getLogger(SFCOrderFormDAO.class);

	private static SFCOrderFormDAO Instance = null;

	public static SFCOrderFormDAO getInstance() {
		if (Instance == null)
			Instance = new SFCOrderFormDAO();
		return Instance;
	}

	public int Update(BMSEmployee wLoginUser, SFCOrderForm wSFCOrderForm, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wSFCOrderForm == null) {
				return 0;
			}
			String wSQL = "";
			if (wSFCOrderForm.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.sfc_orderform(OrderID,OrderNo,PartNo,CreateID,CreateTime,"
								+ "ConfirmID,ConfirmTime,Status,Type,ImagePathList,Remark) "
								+ "VALUES(:OrderID,:OrderNo,:PartNo,:CreateID,:CreateTime,:ConfirmID,"
								+ ":ConfirmTime,:Status,:Type,:ImagePathList,:Remark);",
						new Object[] { wInstance.Result });
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.sfc_orderform SET OrderID = :OrderID,OrderNo = :OrderNo,"
								+ "PartNo = :PartNo,CreateID = :CreateID,CreateTime = :CreateTime,"
								+ "ConfirmID = :ConfirmID,ConfirmTime = :ConfirmTime,Status = :Status,"
								+ "Type = :Type,ImagePathList=:ImagePathList,Remark=:Remark WHERE ID = :ID;",
						new Object[] { wInstance.Result });
			}
			wSQL = DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("ID", Integer.valueOf(wSFCOrderForm.ID));
			wParamMap.put("OrderID", Integer.valueOf(wSFCOrderForm.OrderID));
			wParamMap.put("OrderNo", wSFCOrderForm.OrderNo);
			wParamMap.put("PartNo", wSFCOrderForm.PartNo);
			wParamMap.put("CreateID", Integer.valueOf(wSFCOrderForm.CreateID));
			wParamMap.put("CreateTime", wSFCOrderForm.CreateTime);
			wParamMap.put("ConfirmID", Integer.valueOf(wSFCOrderForm.ConfirmID));
			wParamMap.put("ConfirmTime", wSFCOrderForm.ConfirmTime);
			wParamMap.put("Status", Integer.valueOf(wSFCOrderForm.Status));
			wParamMap.put("Type", Integer.valueOf(wSFCOrderForm.Type));
			wParamMap.put("ImagePathList", StringUtils.Join(";", wSFCOrderForm.ImagePathList));
			wParamMap.put("Remark", wSFCOrderForm.Remark);

			GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(wParamMap);

			this.nameJdbcTemplate.update(wSQL, (SqlParameterSource) mapSqlParameterSource,
					(KeyHolder) generatedKeyHolder);

			if (wSFCOrderForm.getID() <= 0) {
				wResult = generatedKeyHolder.getKey().intValue();
				wSFCOrderForm.setID(wResult);
			} else {
				wResult = wSFCOrderForm.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<SFCOrderForm> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<>(Integer.valueOf(0));
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0) {
				return wResult;
			}
			List<String> wIDList = new ArrayList<>();
			for (SFCOrderForm wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.sfc_orderform WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	public SFCOrderForm SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		SFCOrderForm wResult = new SFCOrderForm();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			List<SFCOrderForm> wList = SelectList(wLoginUser, wID, -1, "", -1, null, wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
		} catch (Exception e) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(e.toString());
		}
		return wResult;
	}

	public List<SFCOrderForm> SelectList(BMSEmployee wLoginUser, int wID, int wOrderID, String wPartNo, int wType,
			List<Integer> wStateIDList, OutResult<Integer> wErrorCode) {
		List<SFCOrderForm> wResultList = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResultList;
			}

			if (wStateIDList == null) {
				wStateIDList = new ArrayList<>();
			}

			String wSQL = MessageFormat.format(
					"SELECT * FROM {0}.sfc_orderform WHERE  1=1  " + "and ( :wID <= 0 or :wID = ID ) "
							+ "and ( :wOrderID <= 0 or :wOrderID = OrderID ) "
							+ "and ( :wPartNo is null or :wPartNo = '''' or :wPartNo = PartNo ) "
							+ "and ( :wType <= 0 or :wType = Type ) "
							+ "and ( :wStatus is null or :wStatus = '''' or Status in ({1}));",
					new Object[] {

							wInstance.Result, (wStateIDList.size() > 0) ? StringUtils.Join(",", wStateIDList) : "0" });
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wID", Integer.valueOf(wID));
			wParamMap.put("wOrderID", Integer.valueOf(wOrderID));
			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wType", Integer.valueOf(wType));
			wParamMap.put("wStatus", StringUtils.Join(",", wStateIDList));

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				SFCOrderForm wItem = new SFCOrderForm();

				wItem.ID = StringUtils.parseInt(wReader.get("ID")).intValue();
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID")).intValue();
				wItem.OrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID")).intValue();
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.ConfirmID = StringUtils.parseInt(wReader.get("ConfirmID")).intValue();
				wItem.ConfirmTime = StringUtils.parseCalendar(wReader.get("ConfirmTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status")).intValue();
				wItem.Type = StringUtils.parseInt(wReader.get("Type")).intValue();
				wItem.ImagePathList = StringUtils
						.parseList(StringUtils.parseString(wReader.get("ImagePathList")).split(";"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));

				wItem.Creator = APSConstans.GetBMSEmployeeName(wItem.CreateID);
				wItem.ConfirmName = APSConstans.GetBMSEmployeeName(wItem.ConfirmID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 查询已收电报电子履历
	 */
	public List<SFCSequentialInfo> SelectTelegraphInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format("SELECT ID as OrderID,OrderNo,PartNo,AuditorID,TelegraphRealTime "
					+ "FROM {0}.oms_order where ID=:wOrderID and Status>=2;", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("AuditorID"));
				Calendar wTime = StringUtils.parseCalendar(wReader.get("TelegraphRealTime"));

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.Telegraph.getValue(),
						SFCSequentialInfoType.Telegraph.getLable(), "", 0, "/", 0, "/", wTime, wTime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询进厂确认电子履历
	 */
	public List<SFCSequentialInfo> SelectInPlantInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT t1.OrderID,t2.OrderNo,t2.PartNo,t1.ConfirmID,t1.ConfirmTime "
							+ "FROM {0}.sfc_orderform t1,{0}.oms_order t2 "
							+ "where t1.OrderID=t2.ID and OrderID=:wOrderID and t1.Type=3 and t1.Status=2;",
					wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("ConfirmID"));
				Calendar wTime = StringUtils.parseCalendar(wReader.get("ConfirmTime"));

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.Inplant.getValue(),
						SFCSequentialInfoType.Inplant.getLable(), "", 0, "/", 0, "/", wTime, wTime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询移车电子履历
	 */
	public List<SFCSequentialInfo> SelectMoveCarInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance1.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance2 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance2.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT t1.OrderID,t2.OrderNo,t2.PartNo,t1.UpFlowID,t1.CreateTime,t1.SubmitTime,"
							+ "t3.Name as SName,t4.Name as TName FROM {2}.mtc_task t1,{0}.oms_order t2,"
							+ "{1}.fmc_workspace t3,{1}.fmc_workspace t4 where t1.TargetSID=t4.ID "
							+ "and t1.PlaceID=t3.ID and t1.OrderID=t2.ID and t1.OrderID=:wOrderID and t1.Status=5;",
					wInstance.Result, wInstance1.Result, wInstance2.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wSName = StringUtils.parseString(wReader.get("SName"));
				String wTName = StringUtils.parseString(wReader.get("TName"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("UpFlowID"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				String wText = StringUtils.Format("【{0}】->【{1}】", wSName, wTName);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.MoveCar.getValue(),
						SFCSequentialInfoType.MoveCar.getLable(), wText, 0, "/", 0, wText, wSTime, wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询开工打卡电子履历
	 */
	public List<SFCSequentialInfo> SelectClockInInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance1.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format("SELECT t3.OrderID,t4.OrderNo,t4.PartNo,t1.OperatorID,t1.LoginTime,"
					+ "t3.PartID,t3.StepID FROM {1}.sfc_loginstation t1," + "{0}.sfc_taskstep t2,{0}.aps_taskstep t3,"
					+ "{0}.oms_order t4 where t1.SFCTaskStepID=t2.ID and t2.TaskStepID=t3.ID "
					+ "and t3.OrderID=t4.ID and t4.ID=:wOrderID and t1.Type=1;", wInstance.Result, wInstance1.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("OperatorID"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("LoginTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("LoginTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wStepID = StringUtils.parseInt(wReader.get("StepID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.ClockIn.getValue(),
						SFCSequentialInfoType.ClockIn.getLable(), "", wPartID, wPartName, wStepID, wStepName, wSTime,
						wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询完工打卡电子履历
	 */
	public List<SFCSequentialInfo> SelectClockOutInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance1.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format("SELECT t3.OrderID,t4.OrderNo,t4.PartNo,t1.OperatorID,t1.LoginTime,"
					+ "t3.PartID,t3.StepID FROM {1}.sfc_loginstation t1," + "{0}.sfc_taskstep t2,{0}.aps_taskstep t3,"
					+ "{0}.oms_order t4 where t1.SFCTaskStepID=t2.ID and t2.TaskStepID=t3.ID "
					+ "and t3.OrderID=t4.ID and t4.ID=:wOrderID and t1.Type=2;", wInstance.Result, wInstance1.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("OperatorID"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("LoginTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("LoginTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wStepID = StringUtils.parseInt(wReader.get("StepID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.ClockOut.getValue(),
						SFCSequentialInfoType.ClockOut.getLable(), "", wPartID, wPartName, wStepID, wStepName, wSTime,
						wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询预检电子履历
	 */
	public List<SFCSequentialInfo> SelectPreCheckInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat
					.format("SELECT OrderID,OrderNo,PartNo,OperatorList,StartTime,EndTime,StationID as "
							+ "PartID,PartPointID as StepID FROM {0}.sfc_taskipt where OrderID=:wOrderID "
							+ "and TaskType=14 and Status=2;", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOperatorStr = StringUtils.parseString(wReader.get("OperatorList"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("EndTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wStepID = StringUtils.parseInt(wReader.get("StepID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (StringUtils.isNotEmpty(wOperatorStr)) {
					List<String> wNames = new ArrayList<String>();
					String[] wIDs = wOperatorStr.split(",|;");
					for (String wID : wIDs) {
						wOperatorList.add(Integer.parseInt(wID));
						String wName = APSConstans.GetBMSEmployeeName(Integer.parseInt(wID));
						wNames.add(wName);
					}
					wOperatorName = StringUtils.Join(",", wNames);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.PreCheck.getValue(),
						SFCSequentialInfoType.PreCheck.getLable(), "", wPartID, wPartName, wStepID, wStepName, wSTime,
						wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询自检电子履历
	 */
	public List<SFCSequentialInfo> SelectSelfCheckInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat
					.format("SELECT OrderID,OrderNo,PartNo,OperatorList,StartTime,EndTime,StationID as "
							+ "PartID,PartPointID as StepID FROM {0}.sfc_taskipt where OrderID=:wOrderID "
							+ "and TaskType=6 and Status=2 and OperatorList !='''';", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOperatorStr = StringUtils.parseString(wReader.get("OperatorList"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("EndTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wStepID = StringUtils.parseInt(wReader.get("StepID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (StringUtils.isNotEmpty(wOperatorStr)) {
					List<String> wNames = new ArrayList<String>();
					String[] wIDs = wOperatorStr.split(",|;");
					for (String wID : wIDs) {
						wOperatorList.add(Integer.parseInt(wID));
						String wName = APSConstans.GetBMSEmployeeName(Integer.parseInt(wID));
						wNames.add(wName);
					}
					wOperatorName = StringUtils.Join(",", wNames);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.SelfCheck.getValue(),
						SFCSequentialInfoType.SelfCheck.getLable(), "", wPartID, wPartName, wStepID, wStepName, wSTime,
						wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询互检电子履历
	 */
	public List<SFCSequentialInfo> SelectMutalCheckInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat
					.format("SELECT OrderID,OrderNo,PartNo,OperatorList,StartTime,EndTime,StationID as "
							+ "PartID,PartPointID as StepID FROM {0}.sfc_taskipt where OrderID=:wOrderID "
							+ "and TaskType=12 and Status=2 and OperatorList !='''';", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOperatorStr = StringUtils.parseString(wReader.get("OperatorList"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("EndTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wStepID = StringUtils.parseInt(wReader.get("StepID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (StringUtils.isNotEmpty(wOperatorStr)) {
					List<String> wNames = new ArrayList<String>();
					String[] wIDs = wOperatorStr.split(",|;");
					for (String wID : wIDs) {
						wOperatorList.add(Integer.parseInt(wID));
						String wName = APSConstans.GetBMSEmployeeName(Integer.parseInt(wID));
						wNames.add(wName);
					}
					wOperatorName = StringUtils.Join(",", wNames);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.MutualCheck.getValue(),
						SFCSequentialInfoType.MutualCheck.getLable(), "", wPartID, wPartName, wStepID, wStepName,
						wSTime, wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询专检电子履历
	 */
	public List<SFCSequentialInfo> SelectSpecialCheckInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat
					.format("SELECT OrderID,OrderNo,PartNo,OperatorList,StartTime,EndTime,StationID as "
							+ "PartID,PartPointID as StepID FROM {0}.sfc_taskipt where OrderID=:wOrderID "
							+ "and TaskType=13 and Status=2 and OperatorList !='''';", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOperatorStr = StringUtils.parseString(wReader.get("OperatorList"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("EndTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wStepID = StringUtils.parseInt(wReader.get("StepID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (StringUtils.isNotEmpty(wOperatorStr)) {
					List<String> wNames = new ArrayList<String>();
					String[] wIDs = wOperatorStr.split(",|;");
					for (String wID : wIDs) {
						wOperatorList.add(Integer.parseInt(wID));
						String wName = APSConstans.GetBMSEmployeeName(Integer.parseInt(wID));
						wNames.add(wName);
					}
					wOperatorName = StringUtils.Join(",", wNames);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.SpecialCheck.getValue(),
						SFCSequentialInfoType.SpecialCheck.getLable(), "", wPartID, wPartName, wStepID, wStepName,
						wSTime, wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询终检电子履历
	 */
	public List<SFCSequentialInfo> SelectFinalCheckInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat
					.format("SELECT OrderID,OrderNo,PartNo,OperatorList,StartTime,EndTime,StationID as "
							+ "PartID,PartPointID as StepID FROM {0}.sfc_taskipt where OrderID=:wOrderID "
							+ "and TaskType=15 and Status=2 and OperatorList !='''';", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOperatorStr = StringUtils.parseString(wReader.get("OperatorList"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("EndTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wStepID = StringUtils.parseInt(wReader.get("StepID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (StringUtils.isNotEmpty(wOperatorStr)) {
					List<String> wNames = new ArrayList<String>();
					String[] wIDs = wOperatorStr.split(",|;");
					for (String wID : wIDs) {
						wOperatorList.add(Integer.parseInt(wID));
						String wName = APSConstans.GetBMSEmployeeName(Integer.parseInt(wID));
						wNames.add(wName);
					}
					wOperatorName = StringUtils.Join(",", wNames);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.FinalCheck.getValue(),
						SFCSequentialInfoType.FinalCheck.getLable(), "", wPartID, wPartName, wStepID, wStepName, wSTime,
						wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询出厂检电子履历
	 */
	public List<SFCSequentialInfo> SelectOutCheckInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat
					.format("SELECT OrderID,OrderNo,PartNo,OperatorList,StartTime,EndTime,StationID as "
							+ "PartID,PartPointID as StepID FROM {0}.sfc_taskipt where OrderID=:wOrderID "
							+ "and TaskType=16 and Status=2 and OperatorList !='''';", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOperatorStr = StringUtils.parseString(wReader.get("OperatorList"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("StartTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("EndTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				int wStepID = StringUtils.parseInt(wReader.get("StepID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (StringUtils.isNotEmpty(wOperatorStr)) {
					List<String> wNames = new ArrayList<String>();
					String[] wIDs = wOperatorStr.split(",|;");
					for (String wID : wIDs) {
						wOperatorList.add(Integer.parseInt(wID));
						String wName = APSConstans.GetBMSEmployeeName(Integer.parseInt(wID));
						wNames.add(wName);
					}
					wOperatorName = StringUtils.Join(",", wNames);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.OutCheck.getValue(),
						SFCSequentialInfoType.OutCheck.getLable(), "", wPartID, wPartName, wStepID, wStepName, wSTime,
						wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询竣工确认电子履历
	 */
	public List<SFCSequentialInfo> SelectFinishConfirmInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT t1.OrderID,t2.OrderNo,t2.PartNo,t1.ConfirmID,t1.ConfirmTime "
							+ "FROM {0}.sfc_orderform t1,{0}.oms_order t2 "
							+ "where t1.OrderID=t2.ID and OrderID=:wOrderID and t1.Type=1 and t1.Status=2;",
					wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("ConfirmID"));
				Calendar wTime = StringUtils.parseCalendar(wReader.get("ConfirmTime"));

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.CompleteConfirm.getValue(),
						SFCSequentialInfoType.CompleteConfirm.getLable(), "/", 0, "/", 0, "/", wTime, wTime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询出厂申请电子履历
	 */
	public List<SFCSequentialInfo> SelectOutApplyInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format("SELECT t1.OrderID,t2.OrderNo,t2.PartNo,t1.CreateID,t1.CreateTime "
					+ "FROM {0}.sfc_orderform t1,{0}.oms_order t2 "
					+ "where t1.OrderID=t2.ID and OrderID=:wOrderID and t1.Type=2;", wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("CreateID"));
				Calendar wTime = StringUtils.parseCalendar(wReader.get("CreateTime"));

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.OutApply.getValue(),
						SFCSequentialInfoType.OutApply.getLable(), "/", 0, "/", 0, "/", wTime, wTime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询出厂确认电子履历
	 */
	public List<SFCSequentialInfo> SelectOutConfirmInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT t1.OrderID,t2.OrderNo,t2.PartNo,t1.ConfirmID,t1.ConfirmTime "
							+ "FROM {0}.sfc_orderform t1,{0}.oms_order t2 "
							+ "where t1.OrderID=t2.ID and OrderID=:wOrderID and t1.Type=2 and t1.Status=2;",
					wInstance.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("ConfirmID"));
				Calendar wTime = StringUtils.parseCalendar(wReader.get("ConfirmTime"));

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.OutPlant.getValue(),
						SFCSequentialInfoType.OutPlant.getLable(), "/", 0, "/", 0, "/", wTime, wTime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询不合格评审电子履历
	 */
	public List<SFCSequentialInfo> SelectNCRInfo(BMSEmployee wLoginUser, int wOrderID, OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance1.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT t1.OrderID,t2.OrderNo,t2.PartNo,t1.UpFlowID,t1.CreateTime,t1.SubmitTime,"
							+ "t1.StationID as PartID,t1.DescribeInfo as Text "
							+ "FROM {1}.ncr_task t1,{0}.oms_order t2 "
							+ "where t1.OrderID=t2.ID and  t1.OrderID=:wOrderID and t1.Status=12;",
					wInstance.Result, wInstance1.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wText = StringUtils.parseString(wReader.get("Text"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("UpFlowID"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.NCR.getValue(), SFCSequentialInfoType.NCR.getLable(),
						wText, wPartID, wPartName, 0, wText, wSTime, wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询返修电子履历
	 */
	public List<SFCSequentialInfo> SelectRepairInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.WDW,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance1.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT t1.OrderID,t2.OrderNo,t2.PartNo,t1.UpFlowID,t1.CreateTime,t1.SubmitTime,"
							+ "t1.StationID,t1.Content as Text " + "FROM {1}.rro_repairitem t1,{0}.oms_order t2 "
							+ "where t1.OrderID=t2.ID and  t1.OrderID=:wOrderID and t1.Status=25;",
					wInstance.Result, wInstance1.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wText = StringUtils.parseString(wReader.get("Text"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("UpFlowID"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				int wPartID = StringUtils.parseInt(wReader.get("StationID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.Repair.getValue(), SFCSequentialInfoType.Repair.getLable(),
						wText, wPartID, wPartName, 0, wText, wSTime, wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询异常电子履历
	 */
	public List<SFCSequentialInfo> SelectExceptionInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance1.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance2 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance2.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT t2.ID as OrderID,t2.OrderNo,t2.PartNo,"
							+ "t1.UpFlowID,t1.CreateTime,t1.SubmitTime,t4.ID as PartID,t1.Comment as Text "
							+ "FROM {2}.exc_calltaskbpm t1,{0}.oms_order t2,"
							+ "{2}.exc_station t3,{1}.fpc_part t4 where t4.Code=t3.StationNo "
							+ "and t1.PartNo=t2.PartNo and t2.ID=52 and t3.ID=t1.StationID " + "and t1.Status=20;",
					wInstance.Result, wInstance1.Result, wInstance2.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wText = StringUtils.parseString(wReader.get("Text"));
				int wOpeartorID = StringUtils.parseInt(wReader.get("UpFlowID"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("SubmitTime"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (wOpeartorID > 0) {
					wOperatorList.add(wOpeartorID);
					wOperatorName = APSConstans.GetBMSEmployeeName(wOpeartorID);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.Exception.getValue(),
						SFCSequentialInfoType.Exception.getLable(), wText, wPartID, wPartName, 0, wText, wSTime,
						wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 查询预检问题项电子履历
	 */
	public List<SFCSequentialInfo> SelectProblemInfo(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		List<SFCSequentialInfo> wResult = new ArrayList<>();
		try {
			ServiceResult<String> wInstance = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance1.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance2 = GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.EXC,
					wLoginUser.getID(), 0);
			wErrorCode.set(Integer.valueOf(wInstance2.ErrorCode));
			if (((Integer) wErrorCode.Result).intValue() != 0) {
				return wResult;
			}

			String wSQL = MessageFormat
					.format("SELECT t2.ID as OrderID,t2.OrderNo,t2.PartNo,t3.OperatorList,t1.PreCheckTime,t4.Text,"
							+ "t3.StationID,t3.PartPointID FROM {2}.ipt_precheckproblem t1,"
							+ "{1}.oms_order t2,{0}.sfc_taskipt t3,{2}.ipt_itemrecord t4 "
							+ "where t4.ID=t1.IPTItemID and t3.ID=t1.IPTPreCheckTaskID and t1.OrderID=t2.ID "
							+ "and t1.OrderID=:wOrderID;", wInstance.Result, wInstance1.Result, wInstance2.Result);
			Map<String, Object> wParamMap = new HashMap<>();

			wParamMap.put("wOrderID", wOrderID);

			wSQL = DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = this.nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				String wOrderNo = StringUtils.parseString(wReader.get("OrderNo"));
				String wPartNo = StringUtils.parseString(wReader.get("PartNo"));
				String wOperatorStr = StringUtils.parseString(wReader.get("OperatorList"));
				Calendar wSTime = StringUtils.parseCalendar(wReader.get("PreCheckTime"));
				Calendar wETime = StringUtils.parseCalendar(wReader.get("PreCheckTime"));
				int wPartID = StringUtils.parseInt(wReader.get("StationID"));
				int wStepID = StringUtils.parseInt(wReader.get("PartPointID"));
				String wPartName = APSConstans.GetFPCPartName(wPartID);
				String wStepName = APSConstans.GetFPCPartPointName(wStepID);

				List<Integer> wOperatorList = new ArrayList<Integer>();
				String wOperatorName = "";
				if (StringUtils.isNotEmpty(wOperatorStr)) {
					List<String> wNames = new ArrayList<String>();
					String[] wIDs = wOperatorStr.split(",|;");
					for (String wID : wIDs) {
						wOperatorList.add(Integer.parseInt(wID));
						String wName = APSConstans.GetBMSEmployeeName(Integer.parseInt(wID));
						wNames.add(wName);
					}
					wOperatorName = StringUtils.Join(",", wNames);
				}

				SFCSequentialInfo wInfo = new SFCSequentialInfo(0, wOrderID, wOrderNo, wPartNo, wOperatorList,
						wOperatorName, SFCSequentialInfoType.ProblemCheck.getValue(),
						SFCSequentialInfoType.ProblemCheck.getLable(), "", wPartID, wPartName, wStepID, wStepName,
						wSTime, wETime);
				wResult.add(wInfo);
			}
		} catch (Exception ex) {
			wErrorCode.set(Integer.valueOf(MESException.DBSQL.getValue()));
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取工位顺序
	 */
	public Map<Integer, Integer> SelectPartOrderMap(BMSEmployee wLoginUser, int wOrderID,
			OutResult<Integer> wErrorCode) {
		Map<Integer, Integer> wResult = new HashMap<Integer, Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			ServiceResult<String> wInstance1 = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance1.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					" select PartID,OrderID from {0}.fpc_routepart where routeid "
							+ "in ( select RouteID from {1}.oms_order where ID=:OrderID) order by OrderID asc;",
					wInstance.Result, wInstance1.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("OrderID", wOrderID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {

				int wOrder = StringUtils.parseInt(wReader.get("OrderID"));
				int wPartID = StringUtils.parseInt(wReader.get("PartID"));

				wResult.put(wPartID, wOrder);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
