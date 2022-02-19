package com.mes.loco.aps.server.serviceimpl.dao.aps;

import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class APSTaskDAO extends BaseDAO {

	private static APSTaskDAO Instance = null;

	private APSTaskDAO() {
		super();
	}

	public static APSTaskDAO getInstance() {
		if (Instance == null)
			Instance = new APSTaskDAO();
		return Instance;
	}

	// APS任务管理
	// 接口函数
//	public int APS_SaveTaskLine(int wCompanyID, int wLoginID, APSTaskLine wTaskLine, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		int wID = 0;
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					500301);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				if (wTaskLine.ID < 1) {
//					String wSQLText = StringUtils.Format("Insert Into {0}.aps_taskline", wInstance.Result)
//							+ "(OrderID,LineID,FQTYShift,FQTYParts,FQTYDone,ShiftID,PlanerID,SubmitTime,"
//							+ " WorkHour,SessionTime,Status,Active,FQTYGood,FQTYBad,TaskText,AuditorID,AuditTime,StartTime,EndTime)  Values(:OrderID,:LineID,:FQTYShift,:FQTYParts,:FQTYDone,:ShiftID,"
//							+ " :PlanerID,:SubmitTime,:WorkHour,:SessionTime,:Status,:Active,:FQTYGood,:FQTYBad,:TaskText,:AuditorID,:AuditTime,:StartTime,:EndTime);";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//					wParms.clear();
//					wParms.put("OrderID", wTaskLine.OrderID);
//					wParms.put("LineID", wTaskLine.LineID);
//
//					wParms.put("FQTYShift", wTaskLine.FQTYShift);
//					wParms.put("FQTYParts", wTaskLine.FQTYParts);
//					wParms.put("FQTYDone", wTaskLine.FQTYDone);
//					wParms.put("WorkHour", wTaskLine.WorkHour);
//
//					wParms.put("Status", APSTaskStatus.Saved.getValue());
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wParms.put("PlanerID", wLoginID);
//					wParms.put("SubmitTime", Calendar.getInstance());
//					wParms.put("SessionTime", Calendar.getInstance());
//					wParms.put("Active", 0);
//
//					wParms.put("FQTYGood", wTaskLine.FQTYGood);
//					wParms.put("FQTYBad", wTaskLine.FQTYBad);
//					wParms.put("TaskText", wTaskLine.TaskText);
//					wParms.put("AuditorID", wLoginID);
//					wParms.put("AuditTime", Calendar.getInstance());
//					wParms.put("StartTime", wTaskLine.StartTime);
//					wParms.put("EndTime", wTaskLine.EndTime);
//
//					wSQLText = this.DMLChange(wSQLText);
//					KeyHolder keyHolder = new GeneratedKeyHolder();
//
//					SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//					nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//					wID = keyHolder.getKey().intValue();
//				} else {
//					String wSQLText = StringUtils.Format("Update {0}.aps_taskline", wInstance.Result)
//							+ " set FQTYShift=:FQTYShift,PlanerID=:PlanerID " + " where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("FQTYShift", wTaskLine.FQTYShift);
//					wParms.put("PlanerID", wLoginID);
//
//					wParms.put("ID", wTaskLine.ID);
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wID = wTaskLine.ID;
//				}
//				wTaskLine.ErrorCode = MESException.Default.getValue();
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("APSService", "APS_SaveTaskLine", "Function Exception:" + ex.toString());
//		}
//		return wID;
//	}
//
//	public int APS_RemoveTaskLineByID(int wCompanyID, int wLoginID, int wID, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//				int wShiftID = SFCTaskDAO.DayShiftID;
//
//				APSTaskLine wTaskLine = this.APS_QueryTaskLineByID(wCompanyID, wLoginID, wID, wErrorCode);
//				if (wTaskLine.ID > 1 && wTaskLine.ShiftID >= wShiftID
//						&& wTaskLine.Status == APSTaskStatus.Saved.getValue()) {
//					String wSQLText = StringUtils.Format("Delete from {0}.aps_taskline", wInstance.Result)
//							+ " where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//					wParms.clear();
//					wParms.put("ID", wTaskLine.ID);
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Delete from {0}.aps_taskpart", wInstance.Result)
//							+ " where TaskLineID=:TaskLineID and ShiftID=:ShiftID";
//					wParms.clear();
//					wParms.put("ID", wTaskLine.ID);
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Delete from {0}.aps_taskstep", wInstance.Result)
//							+ " where TaskLineID=:TaskLineID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("TaskPartID", wTaskLine.ID);
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Delete from {0}.wms_material", wInstance.Result)
//							+ " where TaskLineID=:TaskLineID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("TaskPartID", wTaskLine.ID);
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Delete from {0}.wms_materiallocation", wInstance.Result)
//							+ " where TaskLineID=:TaskLineID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("TaskPartID", wTaskLine.ID);
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//				}
//			}
//		} catch (Exception ex) {
//
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_RemoveTaskLine", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int APS_RemoveTaskLineByShiftID(int wCompanyID, int wLoginID, int wLineID, int wShiftID,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//				int wShiftIDC = SFCTaskDAO.DayShiftID;
//
//				List<APSTaskLine> wTaskLineList = this.APS_QueryTaskLineListByShiftID(wCompanyID, wLoginID, wLineID,
//						wShiftID, wErrorCode);
//				for (APSTaskLine wTaskLine : wTaskLineList) {
//					if (wTaskLine.ID > 1 && wTaskLine.ShiftID >= wShiftIDC
//							&& wTaskLine.Status == APSTaskStatus.Saved.getValue()) {
//						String wSQLText = StringUtils.Format("Delete from {0}.aps_taskline", wInstance.Result)
//								+ " where ID=:ID and ShiftID=:ShiftID";
//						Map<String, Object> wParms = new HashMap<String, Object>();
//
//						wParms.put("ID", wTaskLine.ID);
//						wParms.put("ShiftID", wTaskLine.ShiftID);
//						wSQLText = this.DMLChange(wSQLText);
//						nameJdbcTemplate.update(wSQLText, wParms);
//
//						wSQLText = StringUtils.Format("Delete from {0}.aps_taskpart", wInstance.Result)
//								+ " where TaskLineID=:TaskLineID and ShiftID=:ShiftID";
//						wParms.clear();
//
//						wParms.put("ID", wTaskLine.ID);
//						wParms.put("ShiftID", wTaskLine.ShiftID);
//						wSQLText = this.DMLChange(wSQLText);
//						nameJdbcTemplate.update(wSQLText, wParms);
//
//						wSQLText = StringUtils.Format("Delete from {0}.aps_taskstep", wInstance.Result)
//								+ " where TaskLineID=:TaskLineID and ShiftID=:ShiftID";
//						wParms.clear();
//
//						wParms.put("TaskPartID", wTaskLine.ID);
//						wParms.put("ShiftID", wTaskLine.ShiftID);
//						wSQLText = this.DMLChange(wSQLText);
//						nameJdbcTemplate.update(wSQLText, wParms);
//
//						wSQLText = StringUtils.Format("Delete from {0}.wms_material", wInstance.Result)
//								+ " where TaskLineID=:TaskLineID and ShiftID=:ShiftID";
//						wParms.clear();
//
//						wParms.put("TaskPartID", wTaskLine.ID);
//						wParms.put("ShiftID", wTaskLine.ShiftID);
//						wSQLText = this.DMLChange(wSQLText);
//						nameJdbcTemplate.update(wSQLText, wParms);
//
//						wSQLText = StringUtils.Format("Delete from {0}.wms_materiallocation", wInstance.Result)
//								+ " where TaskLineID=:TaskLineID and ShiftID=:ShiftID";
//						wParms.clear();
//
//						wParms.put("TaskPartID", wTaskLine.ID);
//						wParms.put("ShiftID", wTaskLine.ShiftID);
//						wSQLText = this.DMLChange(wSQLText);
//						nameJdbcTemplate.update(wSQLText, wParms);
//					}
//				}
//			}
//		} catch (Exception ex) {
//
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_RemoveTaskLine", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int APS_AssignTaskLineByID(int wCompanyID, int wLoginID, int wID, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				APSTaskLine wTaskLine = this.APS_QueryTaskLineByID(wCompanyID, wLoginID, wID, wErrorCode);
//				if (wTaskLine.ID > 0 && wTaskLine.Status == APSTaskStatus.Saved.getValue()) {
//					String wSQLText = StringUtils.Format(
//							"Update {0}.aps_taskline Set Status=:Status,AuditorID=:AuditorID,AuditTime=:AuditTime",
//							wInstance.Result) + " where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("ID", wTaskLine.ID);
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wParms.put("Status", APSTaskStatus.Issued.getValue());
//					wParms.put("AuditorID", wLoginID);
//					wParms.put("AuditTime", Calendar.getInstance());
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Update {0}.aps_taskpart Set Status=:Status", wInstance.Result)
//							+ " where TaskLineID=:TaskLineID ";
//					wParms.clear();
//
//					wParms.put("TaskLineID", wTaskLine.ID);
//					wParms.put("Status", APSTaskStatus.Issued.getValue());
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Update {0}.aps_taskstep Set Status=:Status", wInstance.Result)
//							+ " where TaskLineID=:TaskLineID ";
//					wParms.clear();
//
//					wParms.put("TaskLineID", wTaskLine.ID);
//					wParms.put("Status", APSTaskStatus.Issued.getValue());
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					SFCTaskDAO.getInstance().SFC_ResetActiveTaskList(wCompanyID, wErrorCode);
//				}
//			}
//		} catch (Exception ex) {
//
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_AssignTaskLineByID", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int APS_ReverseAssignTaskLineByID(int wCompanyID, int wLoginID, int wID, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				APSTaskLine wTaskLine = this.APS_QueryTaskLineByID(wCompanyID, wLoginID, wID, wErrorCode);
//				if (wTaskLine.ID > 0 && wTaskLine.Status == APSTaskStatus.Issued.getValue()) {
//					String wSQLText = StringUtils.Format(
//							"Update {0}.aps_taskline Set Status=:Status,AuditorID=:AuditorID,AuditTime=:AuditTime",
//							wInstance.Result) + " where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("ID", wTaskLine.ID);
//					wParms.put("ShiftID", wTaskLine.ShiftID);
//					wParms.put("Status", APSTaskStatus.Saved.getValue());
//					wParms.put("AuditorID", wLoginID);
//					wParms.put("AuditTime", Calendar.getInstance());
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Update {0}.aps_taskpart Set Status=:Status", wInstance.Result)
//							+ " where TaskLineID=:TaskLineID ";
//					wParms.clear();
//
//					wParms.put("TaskLineID", wTaskLine.ID);
//					wParms.put("Status", APSTaskStatus.Saved.getValue());
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Update {0}.aps_taskstep Set Status=:Status", wInstance.Result)
//							+ " where TaskLineID=:TaskLineID ";
//					wParms.clear();
//
//					wParms.put("TaskLineID", wTaskLine.ID);
//					wParms.put("Status", APSTaskStatus.Saved.getValue());
//					nameJdbcTemplate.update(wSQLText, wParms);
//				}
//			}
//		} catch (Exception ex) {
//
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_ReverseAssignTaskLineByID",
//					"Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int APS_AssignTaskLineByShiftID(int wCompanyID, int wLoginID, int wLineID, int wShiftID,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				List<APSTaskLine> wTaskLineList = this.APS_QueryTaskLineListByShiftID(wCompanyID, wLoginID, wLineID,
//						wShiftID, wErrorCode);
//				for (APSTaskLine wTaskLine : wTaskLineList) {
//					if (wTaskLine.ID > 0 && wTaskLine.Status == APSTaskStatus.Saved.getValue()) {
//						String wSQLText = StringUtils.Format(
//								"Update {0}.aps_taskline Set Status=:Status,AuditorID=:AuditorID,AuditTime=:AuditTime",
//								wInstance.Result) + " where ID=:ID and ShiftID=:ShiftID";
//						Map<String, Object> wParms = new HashMap<String, Object>();
//
//						wParms.put("ID", wTaskLine.ID);
//						wParms.put("ShiftID", wTaskLine.ShiftID);
//						wParms.put("Status", APSTaskStatus.Issued.getValue());
//						wParms.put("AuditorID", wLoginID);
//						wParms.put("AuditTime", Calendar.getInstance());
//						nameJdbcTemplate.update(wSQLText, wParms);
//
//						wSQLText = StringUtils.Format("Update {0}.aps_taskpart Set Status=:Status", wInstance.Result)
//								+ " where TaskLineID=:TaskLineID ";
//						wParms.clear();
//
//						wParms.put("TaskLineID", wTaskLine.ID);
//						wParms.put("Status", APSTaskStatus.Issued.getValue());
//						nameJdbcTemplate.update(wSQLText, wParms);
//
//						wSQLText = StringUtils.Format("Update {0}.aps_taskstep Set Status=:Status", wInstance.Result)
//								+ " where TaskLineID=:TaskLineID ";
//						wParms.clear();
//
//						wParms.put("TaskLineID", wTaskLine.ID);
//						wParms.put("Status", APSTaskStatus.Issued.getValue());
//						nameJdbcTemplate.update(wSQLText, wParms);
//					}
//				}
//				SFCTaskDAO.getInstance().SFC_ResetActiveTaskList(wCompanyID, wErrorCode);
//			}
//		} catch (Exception ex) {
//
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_AssignTaskLineByShiftID",
//					"Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int APS_SaveTaskPart(int wCompanyID, int wLoginID, APSTaskPart wTaskPart, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		int wID = 0;
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					500301);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				if (wTaskPart.ID < 1) {
//					String wSQLText = StringUtils.Format("Insert Into {0}.aps_taskpart", wInstance.Result)
//							+ "(OrderID,TaskLineID,LineID,PartID,FQTYShift,FQTYParts,FQTYDone,ShiftID,PlanerID,"
//							+ " SubmitTime,WorkHour,StartTime,EndTime,Status,Active,FQTYGood,FQTYBad,TaskText,PartOrderID,ShiftDate)  Values(:OrderID,:TaskLineID,:LineID,:PartID,:FQTYShift,:FQTYParts,"
//							+ " :FQTYDone,:ShiftID,:PlanerID,:SubmitTime,:WorkHour,:StartTime,:EndTime,:Status,:Active,:FQTYGood,:FQTYBad,:TaskText,:PartOrderID,:ShiftDate);";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("OrderID", wTaskPart.OrderID);
//					wParms.put("TaskLineID", wTaskPart.TaskLineID);
//					wParms.put("LineID", wTaskPart.LineID);
//					wParms.put("PartID", wTaskPart.PartID);
//
//					wParms.put("FQTYShift", wTaskPart.FQTYShift);
//					wParms.put("FQTYParts", wTaskPart.FQTYParts);
//					wParms.put("FQTYDone", wTaskPart.FQTYDone);
//					wParms.put("WorkHour", wTaskPart.WorkHour);
//
//					wParms.put("Status", 0);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wParms.put("PlanerID", wLoginID);
//					wParms.put("SubmitTime", Calendar.getInstance());
//					wParms.put("StartTime", Calendar.getInstance());
//					wParms.put("EndTime", Calendar.getInstance());
//					wParms.put("Active", 0);
//
//					wParms.put("FQTYGood", wTaskPart.FQTYGood);
//					wParms.put("FQTYBad", wTaskPart.FQTYBad);
//					wParms.put("TaskText", wTaskPart.TaskText);
//					wParms.put("PartOrderID", wTaskPart.PartOrderID);
//					wParms.put("ShiftDate", wTaskPart.ShiftDate);
//					wSQLText = this.DMLChange(wSQLText);
//					KeyHolder keyHolder = new GeneratedKeyHolder();
//
//					SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//					nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//					wID = keyHolder.getKey().intValue();
//
//				} else {
//					String wSQLText = StringUtils.Format("Update {0}.aps_taskpart", wInstance.Result)
//							+ " set FQTYShift=:FQTYShift,PlanerID=:PlanerID " + " where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("FQTYShift", wTaskPart.FQTYShift);
//					wParms.put("PlanerID", wLoginID);
//
//					wParms.put("ID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wID = wTaskPart.ID;
//				}
//				wTaskPart.ErrorCode = MESException.Default.getValue();
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_SaveTaskPart", "Function Exception:" + ex.toString());
//		}
//		return wID;
//	}
//
//	public int APS_SaveTaskStep(int wCompanyID, int wLoginID, APSTaskStep wTaskStep, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		int wID = 0;
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					500301);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				if (wTaskStep.ID < 1) {
//					String wSQLText = StringUtils.Format("Insert Into {0}.aps_taskstep", wInstance.Result)
//							+ "(OrderID,TaskLineID,TaskPartID,LineID,PartID,PartPointID,FQTYShift,FQTYParts,FQTYDone,ShiftID,PlanerID,"
//							+ " SubmitTime,WorkHour,StartTime,EndTime,Status,Active,TaskText,StepOrderID,ShiftDate)  Values(:OrderID,:TaskLineID,:TaskPartID,:LineID,:PartID,:PartPointID,:FQTYShift,:FQTYParts,:FQTYDone,:ShiftID,"
//							+ " :PlanerID,:SubmitTime,:WorkHour,:StartTime,:EndTime,:Status,:Active,:TaskText,:StepOrderID,:ShiftDate);";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("ID", wTaskStep.ID);
//					wParms.put("OrderID", wTaskStep.OrderID);
//					wParms.put("TaskLineID", wTaskStep.TaskLineID);
//					wParms.put("TaskPartID", wTaskStep.TaskPartID);
//					wParms.put("LineID", wTaskStep.LineID);
//					wParms.put("PartID", wTaskStep.PartID);
//					wParms.put("PartPointID", wTaskStep.PartPointID);
//
//					wParms.put("FQTYShift", wTaskStep.FQTYShift);
//					wParms.put("FQTYParts", wTaskStep.FQTYParts);
//					wParms.put("FQTYDone", wTaskStep.FQTYDone);
//					wParms.put("WorkHour", wTaskStep.WorkHour);
//
//					wParms.put("Status", 0);
//					wParms.put("ShiftID", wTaskStep.ShiftID);
//					wParms.put("PlanerID", wLoginID);
//					wParms.put("SubmitTime", Calendar.getInstance());
//					wParms.put("StartTime", Calendar.getInstance());
//					wParms.put("EndTime", Calendar.getInstance());
//					wParms.put("Active", 0);
//
//					wParms.put("TaskText", wTaskStep.TaskText);
//					wParms.put("StepOrderID", wTaskStep.StepOrderID);
//					wParms.put("ShiftDate", wTaskStep.ShiftDate);
//					wSQLText = this.DMLChange(wSQLText);
//					KeyHolder keyHolder = new GeneratedKeyHolder();
//
//					SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//					nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//					wID = keyHolder.getKey().intValue();
//
//				} else {
//					String wSQLText = StringUtils.Format("Update {0}.aps_taskstep", wInstance.Result)
//							+ " set FQTYShift=:FQTYShift,PlanerID=:PlanerID " + " where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("FQTYShift", wTaskStep.FQTYShift);
//					wParms.put("PlanerID", wLoginID);
//
//					wParms.put("ID", wTaskStep.ID);
//					wParms.put("ShiftID", wTaskStep.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//				}
//				wTaskStep.ErrorCode = MESException.Default.getValue();
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_SaveTaskStep", "Function Exception:" + ex.toString());
//		}
//		return wID;
//	}
//
//	public int APS_SaveMaterial(int wCompanyID, int wLoginID, APSMaterial wMaterial, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		int wID = 0;
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					500301);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				if (wMaterial.ID < 1) {
//					String wSQLText = StringUtils.Format("Insert Into {0}.aps_material", wInstance.Result)
//							+ "(OrderID,TaskLineID,TaskPartID,TaskStepID,MaterialID,LineID,PartID,PartPointID,FQTYShift,FQTYDemand,FQTYOnFactory,"
//							+ " FQTYMargin,ShiftID,Status,PlanerID,SubmitTime,SessionTime,Active)  Values(:OrderID,:TaskLineID,:TaskPartID,:TaskStepID,:MaterialID,:LineID,:PartID,:PartPointID,:FQTYShift,:FQTYDemand,"
//							+ " :FQTYOnFactory,:FQTYMargin,:ShiftID,:Status,:PlanerID,:SubmitTime,:SessionTime,:Active);";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("OrderID", wMaterial.OrderID);
//					wParms.put("TaskLineID", wMaterial.TaskLineID);
//					wParms.put("TaskPartID", wMaterial.TaskPartID);
//					wParms.put("TaskStepID", wMaterial.TaskStepID);
//					wParms.put("MaterialID", wMaterial.MaterialID);
//					wParms.put("LineID", wMaterial.LineID);
//					wParms.put("PartID", wMaterial.PartID);
//					wParms.put("PartPointID", wMaterial.PartPointID);
//					wParms.put("ShiftID", wMaterial.ShiftID);
//					wParms.put("FQTYShift", wMaterial.FQTYShift);
//					wParms.put("FQTYDemand", wMaterial.FQTYDemand);
//					wParms.put("FQTYOnFactory", wMaterial.FQTYOnFactory);
//					wParms.put("FQTYMargin", wMaterial.FQTYMargin);
//
//					wParms.put("Status", 1);
//					wParms.put("PlanerID", wLoginID);
//					wParms.put("SubmitTime", Calendar.getInstance());
//					wParms.put("SessionTime", Calendar.getInstance());
//					wParms.put("Active", 0);
//					wSQLText = this.DMLChange(wSQLText);
//					KeyHolder keyHolder = new GeneratedKeyHolder();
//
//					SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//					nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//					wID = keyHolder.getKey().intValue();
//
//				} else {
//					String wSQLText = StringUtils.Format("Update {0}.aps_material", wInstance.Result)
//							+ " set FQTYShift=:FQTYShift,PlanerID=:PlanerID " + " where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("FQTYShift", wMaterial.FQTYShift);
//					wParms.put("PlanerID", wLoginID);
//
//					wParms.put("ID", wMaterial.ID);
//					wParms.put("ShiftID", wMaterial.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_SaveMaterial", "Function Exception:" + ex.toString());
//		}
//		return wID;
//	}
//
//	public int APS_RemoveTaskPart(int wCompanyID, int wLoginID, APSTaskPart wTaskPart, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//				int wShiftID = SFCTaskDAO.DayShiftID;
//
//				if (wTaskPart.ID > 1 && wTaskPart.ShiftID >= wShiftID) {
//					String wSQLText = StringUtils.Format("Delete from {0}.aps_taskpart", wInstance.Result)
//							+ " where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//					wParms.clear();
//					wParms.put("ID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Delete from {0}.aps_taskpartPoint", wInstance.Result)
//							+ " where TaskPartID=:TaskPartID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("TaskPartID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Delete from {0}.wms_material", wInstance.Result)
//							+ " where TaskPartID=:TaskPartID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("TaskPartID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Delete from {0}.wms_materiallocation", wInstance.Result)
//							+ " where TaskPartID=:TaskPartID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("TaskPartID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Delete from {0}.aps_scheduleworker", wInstance.Result)
//							+ " where TaskPartID=:TaskPartID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("TaskPartID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("delete from {0}.dms_taskipt", wInstance.Result)
//							+ " where ShiftID=:ShiftID and TaskPartID=:TaskPartID";
//					wParms.clear();
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wParms.put("TaskPartID", wTaskPart.ID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//				}
//			}
//		} catch (Exception ex) {
//
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_RemoveTaskPart", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int APS_DisableTaskPart(int wCompanyID, int wLoginID, APSTaskPart wTaskPart, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//				int wShiftID = SFCTaskDAO.DayShiftID;
//
//				if (wTaskPart.ID > 1 && wTaskPart.ShiftID == wShiftID) {
//					String wSQLText = StringUtils.Format("Update {0}.aps_taskpart set Active=0", wInstance.Result)
//							+ "  where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("ID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Update {0}.aps_taskpartPoint Set Active=0", wInstance.Result)
//							+ " where TaskPartID=:TaskPartID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("ID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Update {0}.wms_material Set Active=0", wInstance.Result)
//							+ " where TaskPartID=:TaskPartID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("ID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//				}
//			}
//		} catch (Exception ex) {
//
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_DisableTaskPart", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int APS_ActiveTaskPart(int wCompanyID, int wLoginID, APSTaskPart wTaskPart, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//				int wShiftID = SFCTaskDAO.DayShiftID;
//
//				if (wTaskPart.ID > 1 && wTaskPart.ShiftID == wShiftID) {
//					String wSQLText = StringUtils.Format("Update {0}.aps_taskpart set Active=1", wInstance.Result)
//							+ "  where ID=:ID and ShiftID=:ShiftID";
//					Map<String, Object> wParms = new HashMap<String, Object>();
//
//					wParms.put("ID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Update {0}.aps_taskpartPoint Set Active=1", wInstance.Result)
//							+ " where TaskPartID=:TaskPartID and ShiftID=:ShiftID";
//					wParms.clear();
//					wParms.put("ID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//
//					wSQLText = StringUtils.Format("Update {0}.wms_material Set Active=1", wInstance.Result)
//							+ " where TaskPartID=:TaskPartID and ShiftID=:ShiftID";
//					wParms.clear();
//
//					wParms.put("ID", wTaskPart.ID);
//					wParms.put("ShiftID", wTaskPart.ShiftID);
//					wSQLText = this.DMLChange(wSQLText);
//					nameJdbcTemplate.update(wSQLText, wParms);
//				}
//			}
//		} catch (Exception ex) {
//
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_ActiveTaskPart", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public List<APSTaskLine> APS_QueryTaskLineListByOrderID(int wCompanyID, int wLoginID, int wOrderID,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskLine> wTaskLineList = new ArrayList<APSTaskLine>();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				String wSQLText = StringUtils.Format(
//						"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskline t,{1}.oms_mesorder O",
//						wInstance.Result, wInstance.Result)
//						+ " where t.OrderID=O.ID and t.OrderID=:OrderID Order By t.ID ";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.put("OrderID", wOrderID);
//
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					APSTaskLine wTaskLine = new APSTaskLine();
//					wTaskLine.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskLine.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskLine.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//					wTaskLine.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskLine.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//					wTaskLine.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskLine.FQTYGood = StringUtils.parseInt(wSqlDataReader.get("FQTYGood"));
//					wTaskLine.FQTYBad = StringUtils.parseInt(wSqlDataReader.get("FQTYBad"));
//					wTaskLine.WorkHour = StringUtils.parseInt(wSqlDataReader.get("WorkHour"));
//					wTaskLine.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//					wTaskLine.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskLine.PlanerID = StringUtils.parseInt(wSqlDataReader.get("PlanerID"));
//					wTaskLine.Shifts = StringUtils.parseFloat(wSqlDataReader.get("Shifts"));
//					wTaskLine.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskLine.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//					wTaskLine.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//					wTaskLine.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//					wTaskLine.SubmitTime = StringUtils.parseCalendar(wSqlDataReader.get("SubmitTime"));
//					wTaskLine.SessionTime = StringUtils.parseCalendar(wSqlDataReader.get("SessionTime"));
//
//					// 辅助属性（订单属性）
//					wTaskLine.OrderNo = StringUtils.parseString(wSqlDataReader.get("OrderNo"));
//					wTaskLine.ProductNo = StringUtils.parseString(wSqlDataReader.get("ProductNo"));
//					wTaskLine.MaterialNo = StringUtils.parseString(wSqlDataReader.get("MaterialNo"));
//					wTaskLine.MaterialName = StringUtils.parseString(wSqlDataReader.get("MaterialName"));
//					wTaskLine.FQTY = StringUtils.parseInt(wSqlDataReader.get("FQTY"));
//
//					wTaskLine.BOMNo = StringUtils.parseString(wSqlDataReader.get("BomNO"));
//					wTaskLine.Priority = StringUtils.parseInt(wSqlDataReader.get("Priority"));
//					wTaskLineList.add(wTaskLine);
//				}
//
//				wTaskLineList = this.APS_SetTaskLineListText(wCompanyID, wTaskLineList, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineListByOrderID",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskLineList;
//	}
//
//	public List<APSTaskLine> APS_QueryTaskLineListByShiftID(int wCompanyID, int wLoginID, int wLineID, int wShiftID,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskLine> wTaskLineList = new ArrayList<APSTaskLine>();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" {0} and t.LineID={1}", wCondition, wLineID);
//
//				String wSQLText = StringUtils.Format(
//						"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskline t,{1}.oms_mesorder O",
//						wInstance.Result, wInstance.Result) + " where t.OrderID=O.ID and t.ShiftID=:ShiftID "
//						+ wCondition + " Order By t.ID ";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.put("ShiftID", wShiftID);
//
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					APSTaskLine wTaskLine = new APSTaskLine();
//					wTaskLine.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskLine.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskLine.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//					wTaskLine.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskLine.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//					wTaskLine.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskLine.FQTYGood = StringUtils.parseInt(wSqlDataReader.get("FQTYGood"));
//					wTaskLine.FQTYBad = StringUtils.parseInt(wSqlDataReader.get("FQTYBad"));
//					wTaskLine.WorkHour = StringUtils.parseInt(wSqlDataReader.get("WorkHour"));
//					wTaskLine.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//					wTaskLine.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskLine.PlanerID = StringUtils.parseInt(wSqlDataReader.get("PlanerID"));
//					wTaskLine.Shifts = StringUtils.parseFloat(wSqlDataReader.get("Shifts"));
//					wTaskLine.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskLine.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//					wTaskLine.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//					wTaskLine.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//					wTaskLine.SubmitTime = StringUtils.parseCalendar(wSqlDataReader.get("SubmitTime"));
//					wTaskLine.SessionTime = StringUtils.parseCalendar(wSqlDataReader.get("SessionTime"));
//
//					// 辅助属性（订单属性）
//					wTaskLine.OrderNo = StringUtils.parseString(wSqlDataReader.get("OrderNo"));
//					wTaskLine.ProductNo = StringUtils.parseString(wSqlDataReader.get("ProductNo"));
//					wTaskLine.MaterialNo = StringUtils.parseString(wSqlDataReader.get("MaterialNo"));
//					wTaskLine.MaterialName = StringUtils.parseString(wSqlDataReader.get("MaterialName"));
//					wTaskLine.FQTY = StringUtils.parseInt(wSqlDataReader.get("FQTY"));
//
//					wTaskLine.BOMNo = StringUtils.parseString(wSqlDataReader.get("BomNO"));
//					wTaskLine.Priority = StringUtils.parseInt(wSqlDataReader.get("Priority"));
//					wTaskLineList.add(wTaskLine);
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineListByShiftID Step01:Query TaskPartList",
//					"Function Exception:" + ex.toString());
//		}
//		try {
//			List<APSTaskLine> wRangedLineList = new ArrayList<APSTaskLine>();
//			for (APSTaskLine wTaskLine : wTaskLineList) {
//				boolean wRangedLine = BMSRoleDAO.getInstance().BMS_CheckRangeByAuthorityID(wCompanyID, wLoginID,
//						BMSRange.Line, wTaskLine.LineID, wErrorCode);
//				if (wRangedLine)
//					wRangedLineList.add(wTaskLine);
//			}
//			wTaskLineList = this.APS_SetTaskLineListText(wCompanyID, wRangedLineList, wErrorCode);
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.Exception.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineListByShiftID Step02:Fill TaskPartPoint",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskLineList;
//	}
//
//	public List<APSTaskPart> APS_QueryTaskPartListByShiftID(int wCompanyID, int wLoginID, int wLineID, int wShiftID,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskPart> wTaskPartList = new ArrayList<APSTaskPart>();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" {0} and t.LineID={1}", wCondition, wLineID);
//
//				String wSQLText = StringUtils.Format(
//						"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskpart t,{1}.oms_mesorder O",
//						wInstance.Result, wInstance.Result) + " where t.OrderID=O.ID and t.ShiftID=:ShiftID "
//						+ wCondition + " Order By t.TaskLineID ";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.put("ShiftID", wShiftID);
//
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					APSTaskPart wTaskPart = new APSTaskPart();
//					wTaskPart.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskPart.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskPart.TaskLineID = StringUtils.parseInt(wSqlDataReader.get("TaskLineID"));
//					wTaskPart.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wTaskPart.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//
//					wTaskPart.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskPart.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//					wTaskPart.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskPart.FQTYGood = StringUtils.parseInt(wSqlDataReader.get("FQTYGood"));
//					wTaskPart.FQTYBad = StringUtils.parseInt(wSqlDataReader.get("FQTYBad"));
//					wTaskPart.WorkHour = StringUtils.parseInt(wSqlDataReader.get("WorkHour"));
//					wTaskPart.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskPart.PlanerID = StringUtils.parseInt(wSqlDataReader.get("PlanerID"));
//					wTaskPart.PartOrderID = StringUtils.parseInt(wSqlDataReader.get("PartOrderID"));
//
//					wTaskPart.SubmitTime = StringUtils.parseCalendar(wSqlDataReader.get("SubmitTime"));
//					wTaskPart.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskPart.ShiftDate = StringUtils.parseCalendar(wSqlDataReader.get("ShiftDate"));
//
//					// 辅助属性（订单属性）
//					wTaskPart.OrderNo = StringUtils.parseString(wSqlDataReader.get("OrderNo"));
//					wTaskPart.ProductNo = StringUtils.parseString(wSqlDataReader.get("ProductNo"));
//					wTaskPart.MaterialNo = StringUtils.parseString(wSqlDataReader.get("MaterialNo"));
//					wTaskPart.MaterialName = StringUtils.parseString(wSqlDataReader.get("MaterialName"));
//
//					wTaskPart.BOMNo = StringUtils.parseString(wSqlDataReader.get("BomNO"));
//					wTaskPart.Priority = StringUtils.parseInt(wSqlDataReader.get("Priority"));
//
//					wTaskPartList.add(wTaskPart);
//				}
//
//				wTaskPartList = this.APS_SetTaskPartListText(wCompanyID, wTaskPartList, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_QueryTaskPartListByShiftID Step01:Query TaskPartList",
//					"Function Exception:" + ex.toString());
//		}
//		try {
//			List<APSTaskStep> wTaskStepList = this.APS_QueryTaskStepListByShiftID(wCompanyID, wLoginID, wLineID,
//					wShiftID, wErrorCode);
//			for (APSTaskPart wTaskPart : wTaskPartList) {
//				BMSEmployee wEmployee = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeByID(wCompanyID, 0,
//						wTaskPart.PlanerID, wErrorCode);
//				wTaskPart.PlanerName = wEmployee.Operator;
//				for (APSTaskStep wTaskStep : wTaskStepList) {
//					if (wTaskStep.TaskPartID == wTaskPart.ID && wTaskStep.OrderID == wTaskPart.OrderID) {
//						wTaskStep.OrderNo = wTaskPart.OrderNo;
//						wTaskStep.ProductNo = wTaskPart.ProductNo;
//						wTaskStep.PartName = wTaskPart.PartName;
//						wTaskStep.MaterialNo = wTaskPart.MaterialNo;
//						wTaskStep.MaterialName = wTaskPart.MaterialName;
//						wTaskStep.PlanerID = wTaskPart.PlanerID;
//						wTaskStep.PlanerName = wTaskPart.PlanerName;
//
//						wTaskPart.TaskStepList.add(wTaskStep);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.Exception.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskPartListByShiftID Step02:Fill TaskPartPoint",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskPartList;
//	}
//
//	public List<APSTaskStep> APS_QueryTaskStepListByShiftID(int wCompanyID, int wLoginID, int wLineID, int wShiftID,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskStep> wTaskStepList = new ArrayList<APSTaskStep>();
//		ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//		if (wInstance.Result.length() < 1)
//			return wTaskStepList;
//
//		try {
//
//			String wCondition = "";
//			if (wLineID > 0)
//				wCondition = StringUtils.Format(" {0} and t.LineID={1}", wCondition, wLineID);
//
//			String wSQLText = StringUtils.Format(
//					"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskstep t,{1}.oms_mesorder O",
//					wInstance.Result, wInstance.Result) + " where t.OrderID=O.ID and t.ShiftID=:ShiftID" + wCondition
//					+ " Order By t.TaskPartID ";
//			Map<String, Object> wParms = new HashMap<String, Object>();
//			wParms.put("ShiftID", wShiftID);
//
//			List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//			for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//				APSTaskStep wTaskStep = new APSTaskStep();
//				wTaskStep.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//				wTaskStep.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//				wTaskStep.TaskPartID = StringUtils.parseInt(wSqlDataReader.get("TaskPartID"));
//				wTaskStep.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//				wTaskStep.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//				wTaskStep.PartPointID = StringUtils.parseInt(wSqlDataReader.get("PartPointID"));
//				wTaskStep.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//				wTaskStep.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//
//				wTaskStep.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//				wTaskStep.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//				wTaskStep.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//				wTaskStep.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//				wTaskStep.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//
//				wTaskStep.StepOrderID = StringUtils.parseInt(wSqlDataReader.get("StepOrderID"));
//				wTaskStep.TaskText = StringUtils.parseString(wSqlDataReader.get("TaskText"));
//
//				wTaskStep.ShiftDate = StringUtils.parseCalendar(wSqlDataReader.get("ShiftDate"));
//				wTaskStepList.add(wTaskStep);
//			}
//
//			wTaskStepList = this.APS_SetTaskStepListText(wCompanyID, wTaskStepList, wErrorCode);
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_QueryTaskStepListByShiftID",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskStepList;
//	}
//
//	// S001
//	private List<APSTaskStep> APS_SetTaskStepListText(int wCompanyID, List<APSTaskStep> wTaskStepList,
//			OutResult<Integer> wErrorCode) {
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			// Step03：工艺模型
//			MESEntry wRouteModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.RouteModel);
//
//			for (APSTaskStep wTaskStep : wTaskStepList) {
//				wTaskStep.PlanerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wTaskStep.PlanerID, wEntryEmployee);
//				wTaskStep.LineName = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wTaskStep.LineID,
//						wFactoryModel);
//				wTaskStep.PartName = FPCPartDAO.getInstance().FPC_QueryPartNameByID(wCompanyID, wTaskStep.PartID,
//						wRouteModel);
//				wTaskStep.PartPointName = FPCPartDAO.getInstance().FPC_QueryPartPointNameByID(wCompanyID,
//						wTaskStep.PartPointID, wRouteModel);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("APSService", "APS_SetTaskPartPointListText",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskStepList;
//	}
//
//	private APSTaskStep APS_SetTaskStepText(int wCompanyID, APSTaskStep wTaskStep, OutResult<Integer> wErrorCode) {
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			// Step03：工艺模型
//			MESEntry wRouteModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.RouteModel);
//
//			wTaskStep.PlanerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//					wTaskStep.PlanerID, wEntryEmployee);
//			wTaskStep.LineName = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wTaskStep.LineID,
//					wFactoryModel);
//			wTaskStep.PartName = FPCPartDAO.getInstance().FPC_QueryPartNameByID(wCompanyID, wTaskStep.PartID,
//					wRouteModel);
//			wTaskStep.PartPointName = FPCPartDAO.getInstance().FPC_QueryPartNameByID(wCompanyID, wTaskStep.PartPointID,
//					wRouteModel);
//		} catch (Exception ex) {
//			LoggerTool.SaveException("APSService", "APS_SetTaskPartPointText", "Function Exception:" + ex.toString());
//		}
//		return wTaskStep;
//	}
//
//	private APSTaskPart APS_SetTaskPartText(int wCompanyID, APSTaskPart wTaskPart, OutResult<Integer> wErrorCode) {
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			// Step03：工艺模型
//			MESEntry wRouteModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.RouteModel);
//
//			wTaskPart.PlanerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//					wTaskPart.PlanerID, wEntryEmployee);
//			wTaskPart.LineName = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wTaskPart.LineID,
//					wFactoryModel);
//			wTaskPart.PartName = FPCPartDAO.getInstance().FPC_QueryPartNameByID(wCompanyID, wTaskPart.PartID,
//					wRouteModel);
//		} catch (Exception ex) {
//			LoggerTool.SaveException("APSService", "APS_SetTaskPartText", "Function Exception:" + ex.toString());
//		}
//		return wTaskPart;
//	}
//
//	public List<APSTaskLine> APS_SetTaskLineListText(int wCompanyID, List<APSTaskLine> wTaskLineList,
//			OutResult<Integer> wErrorCode) {
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			// Step03：工艺模型
//			@SuppressWarnings("unused")
//			MESEntry wRouteModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.RouteModel);
//			for (APSTaskLine wTaskLine : wTaskLineList) {
//				wTaskLine.PlanerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wTaskLine.PlanerID, wEntryEmployee);
//				wTaskLine.LineName = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wTaskLine.LineID,
//						wFactoryModel);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("APSService", "APS_SetTaskLineListText", "Function Exception:" + ex.toString());
//		}
//		return wTaskLineList;
//	}
//
//	private APSTaskLine APS_SetTaskLineText(int wCompanyID, APSTaskLine wTaskLine, OutResult<Integer> wErrorCode) {
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			// Step03：工艺模型
//			@SuppressWarnings("unused")
//			MESEntry wRouteModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.RouteModel);
//			wTaskLine.PlanerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//					wTaskLine.PlanerID, wEntryEmployee);
//			wTaskLine.LineName = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wTaskLine.LineID,
//					wFactoryModel);
//		} catch (Exception ex) {
//			LoggerTool.SaveException("APSService", "APS_SetTaskLineText", "Function Exception:" + ex.toString());
//		}
//		return wTaskLine;
//	}
//
//	private List<APSTaskPart> APS_SetTaskPartListText(int wCompanyID, List<APSTaskPart> wTaskPartList,
//			OutResult<Integer> wErrorCode) {
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			// Step03：工艺模型
//			MESEntry wRouteModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.RouteModel);
//			for (APSTaskPart wTaskPart : wTaskPartList) {
//				wTaskPart.PlanerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wTaskPart.PlanerID, wEntryEmployee);
//				wTaskPart.LineName = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wTaskPart.LineID,
//						wFactoryModel);
//				wTaskPart.PartName = FPCPartDAO.getInstance().FPC_QueryPartNameByID(wCompanyID, wTaskPart.PartID,
//						wRouteModel);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("APSService", "APS_SetTaskPartListText", "Function Exception:" + ex.toString());
//		}
//		return wTaskPartList;
//	}
//
//	public APSTaskLine APS_QueryTaskLineByID(int wCompanyID, int wLoginID, int wID, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		APSTaskLine wTaskLine = new APSTaskLine();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				String wSQLText = StringUtils.Format(
//						"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskline t,{1}.oms_mesorder O",
//						wInstance.Result, wInstance.Result) + " where t.OrderID=O.ID and t.ID=:ID ";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.clear();
//				wParms.put("ID", wID);
//
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					wTaskLine.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskLine.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskLine.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//					wTaskLine.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskLine.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//					wTaskLine.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskLine.FQTYGood = StringUtils.parseInt(wSqlDataReader.get("FQTYGood"));
//					wTaskLine.FQTYBad = StringUtils.parseInt(wSqlDataReader.get("FQTYBad"));
//					wTaskLine.WorkHour = StringUtils.parseInt(wSqlDataReader.get("WorkHour"));
//					wTaskLine.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//					wTaskLine.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskLine.PlanerID = StringUtils.parseInt(wSqlDataReader.get("PlanerID"));
//					wTaskLine.Shifts = StringUtils.parseFloat(wSqlDataReader.get("Shifts"));
//					wTaskLine.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskLine.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//					wTaskLine.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//					wTaskLine.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//					wTaskLine.SubmitTime = StringUtils.parseCalendar(wSqlDataReader.get("SubmitTime"));
//					wTaskLine.SessionTime = StringUtils.parseCalendar(wSqlDataReader.get("SessionTime"));
//
//					// 辅助属性（订单属性）
//					wTaskLine.OrderNo = StringUtils.parseString(wSqlDataReader.get("OrderNo"));
//					wTaskLine.ProductNo = StringUtils.parseString(wSqlDataReader.get("ProductNo"));
//					wTaskLine.MaterialNo = StringUtils.parseString(wSqlDataReader.get("MaterialNo"));
//					wTaskLine.MaterialName = StringUtils.parseString(wSqlDataReader.get("MaterialName"));
//					wTaskLine.FQTY = StringUtils.parseInt(wSqlDataReader.get("FQTY"));
//
//					wTaskLine.BOMNo = StringUtils.parseString(wSqlDataReader.get("BomNO"));
//					wTaskLine.Priority = StringUtils.parseInt(wSqlDataReader.get("Priority"));
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineListByStatus Step01:Query TaskPartList",
//					"Function Exception:" + ex.toString());
//		}
//		try {
//			if (wTaskLine.ID > 0) {
//				wTaskLine.TaskPartList = this.APS_QueryTaskPartListByTaskLineID(wCompanyID, wTaskLine.ID, wErrorCode);
//				List<APSTaskStep> wTaskStepList = this.APS_QueryTaskStepListByTaskPartID(wCompanyID, wTaskLine.ID, 0,
//						wErrorCode);
//				wTaskStepList = this.APS_SetTaskStepListText(wCompanyID, wTaskStepList, wErrorCode);
//				for (APSTaskStep wTaskStep : wTaskStepList) {
//					for (APSTaskPart wTaskPart : wTaskLine.TaskPartList) {
//						if (wTaskPart.ID == wTaskStep.TaskPartID) {
//							wTaskPart.TaskStepList.add(wTaskStep);
//							break;
//						}
//					}
//				}
//				wTaskLine.TaskPartList = this.APS_SetTaskPartListText(wCompanyID, wTaskLine.TaskPartList, wErrorCode);
//				wTaskLine = this.APS_SetTaskLineText(wCompanyID, wTaskLine, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.Logic.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineByID", "Function Exception:" + ex.toString());
//		}
//		return wTaskLine;
//	}
//
//	public List<APSTaskPart> APS_QueryTaskPartListByTaskLineID(int wCompanyID, int wTaskLineID,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskPart> wTaskPartList = new ArrayList<APSTaskPart>();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				String wSQLText = StringUtils.Format(
//						"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskpart t,{1}.oms_mesorder O",
//						wInstance.Result, wInstance.Result)
//						+ " where t.OrderID=O.ID and t.TaskLineID=:TaskLineID Order By t.ID,t.PartOrderID ";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.put("TaskLineID", wTaskLineID);
//
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					APSTaskPart wTaskPart = new APSTaskPart();
//					wTaskPart.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskPart.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskPart.TaskLineID = StringUtils.parseInt(wSqlDataReader.get("TaskLineID"));
//					wTaskPart.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wTaskPart.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//
//					wTaskPart.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskPart.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//					wTaskPart.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskPart.FQTYGood = StringUtils.parseInt(wSqlDataReader.get("FQTYGood"));
//					wTaskPart.FQTYBad = StringUtils.parseInt(wSqlDataReader.get("FQTYBad"));
//					wTaskPart.WorkHour = StringUtils.parseInt(wSqlDataReader.get("WorkHour"));
//
//					wTaskPart.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskPart.PlanerID = StringUtils.parseInt(wSqlDataReader.get("PlanerID"));
//					wTaskPart.PartOrderID = StringUtils.parseInt(wSqlDataReader.get("PartOrderID"));
//
//					wTaskPart.SubmitTime = StringUtils.parseCalendar(wSqlDataReader.get("SubmitTime"));
//					wTaskPart.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskPart.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//					wTaskPart.ShiftDate = StringUtils.parseCalendar(wSqlDataReader.get("ShiftDate"));
//
//					// 辅助属性（订单属性）
//					wTaskPart.OrderNo = StringUtils.parseString(wSqlDataReader.get("OrderNo"));
//					wTaskPart.ProductNo = StringUtils.parseString(wSqlDataReader.get("ProductNo"));
//					wTaskPart.MaterialNo = StringUtils.parseString(wSqlDataReader.get("MaterialNo"));
//					wTaskPart.MaterialName = StringUtils.parseString(wSqlDataReader.get("MaterialName"));
//
//					wTaskPart.BOMNo = StringUtils.parseString(wSqlDataReader.get("BomNO"));
//					wTaskPart.Priority = StringUtils.parseInt(wSqlDataReader.get("Priority"));
//					wTaskPartList.add(wTaskPart);
//				}
//
//				wTaskPartList = this.APS_SetTaskPartListText(wCompanyID, wTaskPartList, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskPartListByTaskLineID",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskPartList;
//	}
//
//	public List<APSTaskStep> APS_QueryTaskStepListByTaskPartID(int wCompanyID, int wTaskLineID, int wTaskPartID,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskStep> wTaskStepList = new ArrayList<APSTaskStep>();
//		ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//		try {
//			String wSQLText = "";
//			Map<String, Object> wParms = new HashMap<String, Object>();
//			if (wTaskPartID > 0) {
//				wSQLText = StringUtils.Format("Select * from {0}.aps_taskstep t", wInstance.Result)
//						+ " where t.TaskPartID=:TaskPartID Order By t.TaskPartID ";
//
//				wParms.put("TaskPartID", wTaskPartID);
//			} else {
//				wSQLText = StringUtils.Format("Select * from {0}.aps_taskstep t", wInstance.Result)
//						+ " where t.TaskLineID=:TaskLineID Order By t.TaskPartID ";
//				wParms.put("TaskLineID", wTaskLineID);
//			}
//			List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//			for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//				APSTaskStep wTaskStep = new APSTaskStep();
//				wTaskStep.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//				wTaskStep.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//				wTaskStep.TaskLineID = StringUtils.parseInt(wSqlDataReader.get("TaskLineID"));
//				wTaskStep.TaskPartID = StringUtils.parseInt(wSqlDataReader.get("TaskPartID"));
//				wTaskStep.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//				wTaskStep.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//				wTaskStep.PartPointID = StringUtils.parseInt(wSqlDataReader.get("PartPointID"));
//				wTaskStep.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//				wTaskStep.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//
//				wTaskStep.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//				wTaskStep.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//				wTaskStep.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//				wTaskStep.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//				wTaskStep.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//
//				wTaskStep.StepOrderID = StringUtils.parseInt(wSqlDataReader.get("StepOrderID"));
//				wTaskStep.TaskText = StringUtils.parseString(wSqlDataReader.get("TaskText"));
//				wTaskStep.ShiftDate = StringUtils.parseCalendar(wSqlDataReader.get("ShiftDate"));
//				wTaskStepList.add(wTaskStep);
//			}
//
//			this.APS_SetTaskStepListText(wCompanyID, wTaskStepList, wErrorCode);
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskStepListByTaskPartID",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskStepList;
//	}
//
//	public APSTaskStep APS_QueryTaskStepByID(int wCompanyID, int wLogin, int wID, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		APSTaskStep wTaskStep = new APSTaskStep();
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//				String wSQLText = StringUtils.Format("Select t.* from {0}.aps_taskstep t", wInstance.Result)
//						+ " where t.ID=:ID ";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.put("ID", wID);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					wTaskStep.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskStep.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskStep.TaskLineID = StringUtils.parseInt(wSqlDataReader.get("TaskLineID"));
//					wTaskStep.TaskPartID = StringUtils.parseInt(wSqlDataReader.get("TaskPartID"));
//
//					wTaskStep.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//					wTaskStep.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//					wTaskStep.PartPointID = StringUtils.parseInt(wSqlDataReader.get("PartPointID"));
//					wTaskStep.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskStep.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//
//					wTaskStep.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskStep.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskStep.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskStep.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//					wTaskStep.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//
//					wTaskStep.StepOrderID = StringUtils.parseInt(wSqlDataReader.get("StepOrderID"));
//					wTaskStep.TaskText = StringUtils.parseString(wSqlDataReader.get("TaskText"));
//					wTaskStep.ShiftDate = StringUtils.parseCalendar(wSqlDataReader.get("ShiftDate"));
//				}
//				if (wTaskStep.ID > 0)
//					wTaskStep = this.APS_SetTaskStepText(wCompanyID, wTaskStep, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskStepByID Step01: Query DB",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskStep;
//	}
//
//	public APSTaskPart APS_QueryTaskPartByID(int wCompanyID, int wLogin, int wID, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		APSTaskPart wTaskPart = new APSTaskPart();
//		ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//		try {
//
//			String wSQLText = StringUtils.Format("Select t.* from {0}.aps_taskpart t", wInstance.Result)
//					+ " where t.ID=:ID ";
//			Map<String, Object> wParms = new HashMap<String, Object>();
//			wParms.put("ID", wID);
//			List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//			for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//				wTaskPart.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//				wTaskPart.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//				wTaskPart.TaskLineID = StringUtils.parseInt(wSqlDataReader.get("TaskLineID"));
//				wTaskPart.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//				wTaskPart.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//				wTaskPart.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//				wTaskPart.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//
//				wTaskPart.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//				wTaskPart.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//				wTaskPart.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//				wTaskPart.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//				wTaskPart.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//
//				wTaskPart.PartOrderID = StringUtils.parseInt(wSqlDataReader.get("PartOrderID"));
//				wTaskPart.TaskText = StringUtils.parseString(wSqlDataReader.get("TaskText"));
//				wTaskPart.ShiftDate = StringUtils.parseCalendar(wSqlDataReader.get("ShiftDate"));
//			}
//			if (wTaskPart.ID > 0) {
//				wTaskPart.TaskStepList = this.APS_QueryTaskStepListByTaskPartID(wCompanyID, 0, wTaskPart.ID,
//						wErrorCode);
//				wTaskPart = this.APS_SetTaskPartText(wCompanyID, wTaskPart, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskPartByID Step01: Query DB",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskPart;
//	}
//
//	public List<APSTaskLine> APS_QueryTaskLineListByStatus(int wCompanyID, int wLoginID, int wLineID, int wStatus,
//			OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskLine> wTaskLineList = new ArrayList<APSTaskLine>();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" {0} and t.LineID={1}", wCondition, wLineID);
//
//				String wSQLText = StringUtils.Format(
//						"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskline t,{1}.oms_mesorder O",
//						wInstance.Result, wInstance.Result) + " where t.OrderID=O.ID and t.Status=:Status " + wCondition
//						+ " Order By t.ID ";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.put("Status", wStatus);
//
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					APSTaskLine wTaskLine = new APSTaskLine();
//					wTaskLine.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskLine.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskLine.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//					wTaskLine.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskLine.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//					wTaskLine.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskLine.FQTYGood = StringUtils.parseInt(wSqlDataReader.get("FQTYGood"));
//					wTaskLine.FQTYBad = StringUtils.parseInt(wSqlDataReader.get("FQTYBad"));
//					wTaskLine.WorkHour = StringUtils.parseInt(wSqlDataReader.get("WorkHour"));
//					wTaskLine.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//					wTaskLine.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskLine.PlanerID = StringUtils.parseInt(wSqlDataReader.get("PlanerID"));
//					wTaskLine.Shifts = StringUtils.parseFloat(wSqlDataReader.get("Shifts"));
//					wTaskLine.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskLine.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//					wTaskLine.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//					wTaskLine.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//					wTaskLine.SubmitTime = StringUtils.parseCalendar(wSqlDataReader.get("SubmitTime"));
//					wTaskLine.SessionTime = StringUtils.parseCalendar(wSqlDataReader.get("SessionTime"));
//
//					// 辅助属性（订单属性）
//					wTaskLine.OrderNo = StringUtils.parseString(wSqlDataReader.get("OrderNo"));
//					wTaskLine.ProductNo = StringUtils.parseString(wSqlDataReader.get("ProductNo"));
//					wTaskLine.MaterialNo = StringUtils.parseString(wSqlDataReader.get("MaterialNo"));
//					wTaskLine.MaterialName = StringUtils.parseString(wSqlDataReader.get("MaterialName"));
//					wTaskLine.FQTY = StringUtils.parseInt(wSqlDataReader.get("FQTY"));
//
//					wTaskLine.BOMNo = StringUtils.parseString(wSqlDataReader.get("BomNO"));
//					wTaskLine.Priority = StringUtils.parseInt(wSqlDataReader.get("Priority"));
//					wTaskLineList.add(wTaskLine);
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineListByStatus Step01:Query TaskPartList",
//					"Function Exception:" + ex.toString());
//		}
//		try {
//			List<APSTaskLine> wRangedLineList = new ArrayList<APSTaskLine>();
//			for (APSTaskLine wTaskLine : wTaskLineList) {
//				boolean wRangedLine = BMSRoleDAO.getInstance().BMS_CheckRangeByAuthorityID(wCompanyID, wLoginID,
//						BMSRange.Line, wTaskLine.LineID, wErrorCode);
//				if (wRangedLine)
//					wRangedLineList.add(wTaskLine);
//			}
//			wTaskLineList = this.APS_SetTaskLineListText(wCompanyID, wRangedLineList, wErrorCode);
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.Exception.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineListByStatus",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskLineList;
//	}
//
//	// 报表服务
//	public List<APSTaskLine> APS_QueryTaskLineListByTime(int wCompanyID, int wLoginID, int wLineID, Calendar wStartTime,
//			Calendar wEndTime, int wStatus, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskLine> wTaskLineList = new ArrayList<APSTaskLine>();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" {0} and t.LineID={1}", wCondition, wLineID);
//				String wSQLText = "";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				if (wStatus > 0) {
//					wSQLText = StringUtils.Format(
//							"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskline t,{1}.oms_mesorder O",
//							wInstance.Result, wInstance.Result)
//							+ " where t.OrderID=O.ID and t.Status=:Status and t.StartTime between :StartTime and :EndTime "
//							+ wCondition + " Order By t.ID ";
//					wParms.clear();
//					wParms.put("Status", wStatus);
//				} else {
//					wSQLText = StringUtils.Format(
//							"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskline t,{1}.oms_mesorder O",
//							wInstance.Result, wInstance.Result)
//							+ " where t.OrderID=O.ID and t.StartTime between :StartTime and :EndTime " + wCondition
//							+ " Order By t.ID ";
//					wParms.clear();
//				}
//
//				wParms.put("StartTime", wStartTime);
//				wParms.put("EndTime", wEndTime);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					APSTaskLine wTaskLine = new APSTaskLine();
//					wTaskLine.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskLine.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskLine.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//					wTaskLine.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskLine.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//					wTaskLine.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskLine.FQTYGood = StringUtils.parseInt(wSqlDataReader.get("FQTYGood"));
//					wTaskLine.FQTYBad = StringUtils.parseInt(wSqlDataReader.get("FQTYBad"));
//					wTaskLine.WorkHour = StringUtils.parseInt(wSqlDataReader.get("WorkHour"));
//					wTaskLine.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//					wTaskLine.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskLine.PlanerID = StringUtils.parseInt(wSqlDataReader.get("PlanerID"));
//					wTaskLine.Shifts = StringUtils.parseFloat(wSqlDataReader.get("Shifts"));
//					wTaskLine.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskLine.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//					wTaskLine.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//					wTaskLine.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//					wTaskLine.SubmitTime = StringUtils.parseCalendar(wSqlDataReader.get("SubmitTime"));
//					wTaskLine.SessionTime = StringUtils.parseCalendar(wSqlDataReader.get("SessionTime"));
//
//					// 辅助属性（订单属性）
//					wTaskLine.OrderNo = StringUtils.parseString(wSqlDataReader.get("OrderNo"));
//					wTaskLine.ProductNo = StringUtils.parseString(wSqlDataReader.get("ProductNo"));
//					wTaskLine.MaterialNo = StringUtils.parseString(wSqlDataReader.get("MaterialNo"));
//					wTaskLine.MaterialName = StringUtils.parseString(wSqlDataReader.get("MaterialName"));
//					wTaskLine.FQTY = StringUtils.parseInt(wSqlDataReader.get("FQTY"));
//
//					wTaskLine.BOMNo = StringUtils.parseString(wSqlDataReader.get("BomNO"));
//					wTaskLine.Priority = StringUtils.parseInt(wSqlDataReader.get("Priority"));
//					wTaskLineList.add(wTaskLine);
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineListByTime Step01:Query TaskPartList",
//					"Function Exception:" + ex.toString());
//		}
//		try {
//			List<APSTaskLine> wRangedLineList = new ArrayList<APSTaskLine>();
//			for (APSTaskLine wTaskLine : wTaskLineList) {
//				boolean wRangedLine = BMSRoleDAO.getInstance().BMS_CheckRangeByAuthorityID(wCompanyID, wLoginID,
//						BMSRange.Line, wTaskLine.LineID, wErrorCode);
//				if (wRangedLine)
//					wRangedLineList.add(wTaskLine);
//			}
//			wTaskLineList = this.APS_SetTaskLineListText(wCompanyID, wRangedLineList, wErrorCode);
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.Exception.getValue());
//			LoggerTool.SaveException("APSService", "APS_QueryTaskLineListByTime",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskLineList;
//	}
//
//	public List<APSTaskPart> APS_QueryTaskPartListByTime(int wCompanyID, int wLoginID, int wLineID, int wPartID,
//			Calendar wStartTime, Calendar wEndTime, int wStatus, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskPart> wTaskPartList = new ArrayList<APSTaskPart>();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.Result == 0) {
//
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" {0} and t.LineID={1}", wCondition, wLineID);
//
//				if (wPartID > 0)
//					wCondition = StringUtils.Format(" {0} and t.PartID={1}", wCondition, wPartID);
//				String wSQLText = "";
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				if (wStatus > 0) {
//					wSQLText = StringUtils.Format(
//							"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskpart t,{1}.oms_mesorder O",
//							wInstance.Result, wInstance.Result)
//							+ " where t.OrderID=O.ID and t.Status=:Status and t.ShiftDate between :StartTime and :EndTime"
//							+ wCondition + " Order By t.TaskLineID ";
//
//					wParms.put("Status", wStatus);
//				} else {
//					wSQLText = StringUtils.Format(
//							"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskpart t,{1}.oms_mesorder O",
//							wInstance.Result, wInstance.Result)
//							+ " where t.OrderID=O.ID and t.ShiftDate between :StartTime and :EndTime" + wCondition
//							+ " Order By t.TaskLineID ";
//				}
//				wParms.put("StartTime", wStartTime);
//				wParms.put("EndTime", wEndTime);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					APSTaskPart wTaskPart = new APSTaskPart();
//					wTaskPart.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTaskPart.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//					wTaskPart.TaskLineID = StringUtils.parseInt(wSqlDataReader.get("TaskLineID"));
//					wTaskPart.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wTaskPart.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//
//					wTaskPart.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//					wTaskPart.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//					wTaskPart.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//					wTaskPart.FQTYGood = StringUtils.parseInt(wSqlDataReader.get("FQTYGood"));
//					wTaskPart.FQTYBad = StringUtils.parseInt(wSqlDataReader.get("FQTYBad"));
//					wTaskPart.WorkHour = StringUtils.parseInt(wSqlDataReader.get("WorkHour"));
//					wTaskPart.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wTaskPart.PlanerID = StringUtils.parseInt(wSqlDataReader.get("PlanerID"));
//					wTaskPart.PartOrderID = StringUtils.parseInt(wSqlDataReader.get("PartOrderID"));
//
//					wTaskPart.SubmitTime = StringUtils.parseCalendar(wSqlDataReader.get("SubmitTime"));
//					wTaskPart.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//					wTaskPart.ShiftDate = StringUtils.parseCalendar(wSqlDataReader.get("ShiftDate"));
//
//					// 辅助属性（订单属性）
//					wTaskPart.OrderNo = StringUtils.parseString(wSqlDataReader.get("OrderNo"));
//					wTaskPart.ProductNo = StringUtils.parseString(wSqlDataReader.get("ProductNo"));
//					wTaskPart.MaterialNo = StringUtils.parseString(wSqlDataReader.get("MaterialNo"));
//					wTaskPart.MaterialName = StringUtils.parseString(wSqlDataReader.get("MaterialName"));
//
//					wTaskPart.BOMNo = StringUtils.parseString(wSqlDataReader.get("BomNO"));
//					wTaskPart.Priority = StringUtils.parseInt(wSqlDataReader.get("Priority"));
//
//					wTaskPartList.add(wTaskPart);
//				}
//
//				wTaskPartList = this.APS_SetTaskPartListText(wCompanyID, wTaskPartList, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_QueryTaskPartListByTime",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskPartList;
//	}
//
//	public List<APSTaskStep> APS_QueryTaskStepListByTime(int wCompanyID, int wLoginID, int wLineID, int wPartID,
//			Calendar wStartTime, Calendar wEndTime, int wStatus, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		List<APSTaskStep> wTaskStepList = new ArrayList<APSTaskStep>();
//		ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//		if (wInstance.Result.length() < 1)
//			return wTaskStepList;
//
//		try {
//
//			String wCondition = "";
//			if (wLineID > 0)
//				wCondition = StringUtils.Format(" {0} and t.LineID={1}", wCondition, wLineID);
//
//			if (wPartID > 0)
//				wCondition = StringUtils.Format(" {0} and t.PartID={1}", wCondition, wPartID);
//			String wSQLText = "";
//			Map<String, Object> wParms = new HashMap<String, Object>();
//			if (wStatus > 0) {
//				wSQLText = StringUtils.Format(
//						"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskstep t,{1}.oms_mesorder O",
//						wInstance.Result, wInstance.Result)
//						+ " where t.OrderID=O.ID and t.Status=:Status and t.ShiftDate between :StartTime and :EndTime"
//						+ wCondition + " Order By t.TaskPartID ";
//
//				wParms.put("Status", wStatus);
//			} else {
//				wSQLText = StringUtils.Format(
//						"Select O.OrderNo,O.ProductNo,O.MaterialNo,O.MaterialName,O.FQTY,O.BOMNo,O.Priority,t.* from {0}.aps_taskstep t,{1}.oms_mesorder O",
//						wInstance.Result, wInstance.Result)
//						+ " where t.OrderID=O.ID and t.ShiftDate between :StartTime and :EndTime" + wCondition
//						+ " Order By t.TaskPartID ";
//
//			}
//			wParms.put("StartTime", wStartTime);
//			wParms.put("EndTime", wEndTime);
//			List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//			for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//				APSTaskStep wTaskStep = new APSTaskStep();
//				wTaskStep.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//				wTaskStep.OrderID = StringUtils.parseInt(wSqlDataReader.get("OrderID"));
//				wTaskStep.TaskPartID = StringUtils.parseInt(wSqlDataReader.get("TaskPartID"));
//				wTaskStep.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//
//				wTaskStep.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//				wTaskStep.PartPointID = StringUtils.parseInt(wSqlDataReader.get("PartPointID"));
//				wTaskStep.FQTYShift = StringUtils.parseInt(wSqlDataReader.get("FQTYShift"));
//				wTaskStep.FQTYParts = StringUtils.parseInt(wSqlDataReader.get("FQTYParts"));
//
//				wTaskStep.FQTYDone = StringUtils.parseInt(wSqlDataReader.get("FQTYDone"));
//				wTaskStep.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//				wTaskStep.StartTime = StringUtils.parseCalendar(wSqlDataReader.get("StartTime"));
//				wTaskStep.EndTime = StringUtils.parseCalendar(wSqlDataReader.get("EndTime"));
//				wTaskStep.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//
//				wTaskStep.StepOrderID = StringUtils.parseInt(wSqlDataReader.get("StepOrderID"));
//				wTaskStep.TaskText = StringUtils.parseString(wSqlDataReader.get("TaskText"));
//
//				wTaskStep.ShiftDate = StringUtils.parseCalendar(wSqlDataReader.get("ShiftDate"));
//				wTaskStepList.add(wTaskStep);
//			}
//
//			wTaskStepList = this.APS_SetTaskStepListText(wCompanyID, wTaskStepList, wErrorCode);
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//
//			LoggerTool.SaveException("APSService", "APS_QueryTaskStepListByTime",
//					"Function Exception:" + ex.toString());
//		}
//		return wTaskStepList;
//	}

}
