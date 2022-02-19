package com.mes.loco.aps.server.serviceimpl.dao.sch;

import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class SCHShiftDAO extends BaseDAO {
	private static SCHShiftDAO Instance = null;

	private SCHShiftDAO() {
		super();
	}

	public static SCHShiftDAO getInstance() {
		if (Instance == null)
			Instance = new SCHShiftDAO();
		return Instance;
	}

	// 排班管理
//	private SCHShift SCH_CheckShift(int wCompanyID, SCHShift wShift, OutResult<Integer> wErrorCode) {
//		SCHShift wShiftDB = new SCHShift();
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				// Step0:查询
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//				wSQLText = StringUtils.Format("Select * from {0}.sch_shiftlist ", wInstance.Result)
//						+ " where ShiftID=:ShiftID and WorkShopID=:WorkShopID and LineID=:LineID and ModuleID=:ModuleID";
//				wParms.clear();
//				wParms.put("ShiftID", wShift.ShiftID);
//				wParms.put("WorkShopID", wShift.WorkShopID);
//				wParms.put("LineID", wShift.LineID);
//				wParms.put("ModuleID", wShift.ModuleID);
//
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					wShiftDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wShiftDB.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wShiftDB.ModuleID = StringUtils.parseInt(wSqlDataReader.get("ModuleID"));
//					wShiftDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wShiftDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//					wShiftDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//					wShiftDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_CheckWorker", "Function Exception:" + ex.toString());
//		}
//		return wShiftDB;
//	}
//
//	private int SCH_SaveShift(int wCompanyID, int wLoginID, SCHShift wShift, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		int wID = 0;
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				SCHShift wShiftDB = this.SCH_CheckShift(wCompanyID, wShift, wErrorCode);
//				if (wErrorCode.Result == 0) {
//					Map<String, Object> wParms = new HashMap<String, Object>();
//					String wSQLText = "";
//
//					if (wShiftDB.ID < 1) {
//						wSQLText = StringUtils.Format("Insert Into {0}.sch_shiftlist", wInstance.Result)
//								+ "(ShiftID,WorkShopID,LineID,ModuleID,CreatorID,CreateTime) "
//								+ " Values(:ShiftID,:WorkShopID,:LineID,:ModuleID,:CreatorID,:CreateTime);";
//						wParms.clear();
//
//						wParms.put("ShiftID", wShift.ShiftID);
//						wParms.put("WorkShopID", wShift.WorkShopID);
//						wParms.put("LineID", wShift.LineID);
//						wParms.put("ModuleID", wShift.ModuleID);
//						wParms.put("CreatorID", wLoginID);
//						wParms.put("CreateTime", Calendar.getInstance());
//
//						wSQLText = this.DMLChange(wSQLText);
//						KeyHolder keyHolder = new GeneratedKeyHolder();
//
//						SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//						nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//						wID = keyHolder.getKey().intValue();
//
//					} else {
//						wSQLText = StringUtils.Format("Update {0}.sch_shiftlist", wInstance.Result)
//								+ " set CreatorID=:CreatorID,CreateTime=:CreateTime  "
//								+ " where ShiftID=:ShiftID and WorkShopID=:WorkShopID and LineID=:LineID and ModuleID=:ModuleID";
//						wParms.clear();
//
//						wParms.put("ShiftID", wShift.ShiftID);
//						wParms.put("WorkShopID", wShift.WorkShopID);
//						wParms.put("LineID", wShift.LineID);
//						wParms.put("ModuleID", wShift.ModuleID);
//						wParms.put("CreatorID", wLoginID);
//						wParms.put("CreateTime", Calendar.getInstance());
//						wSQLText = this.DMLChange(wSQLText);
//						nameJdbcTemplate.update(wSQLText, wParms);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_SaveShift", "Function Exception:" + ex.toString());
//		}
//		return wID;
//	}
//
//	private List<SCHShift> SCH_SetShiftTextList(int wCompanyID, List<SCHShift> wShiftList,
//			OutResult<Integer> wErrorCode) {
//		List<SCHShift> wShiftTextList = new ArrayList<SCHShift>();
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			for (SCHShift wShiftDB : wShiftList) {
//				wShiftDB.Creator = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wShiftDB.CreatorID, wEntryEmployee);
//				FMCWorkShop wWorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopByID(wCompanyID,
//						wShiftDB.WorkShopID, wFactoryModel);
//				if (wWorkShop.ID > 0) {
//					wShiftDB.Factory = FMCFactoryDAO.getInstance().FMC_QueryFactoryNameByID(wCompanyID,
//							wWorkShop.FactoryID, wFactoryModel);
//					wShiftDB.BusinessUnit = FMCFactoryDAO.getInstance().FMC_QueryBusinessUnitNameByID(wCompanyID,
//							wWorkShop.BusinessUnitID, wFactoryModel);
//				}
//				wShiftDB.WorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopNameByID(wCompanyID,
//						wShiftDB.WorkShopID, wFactoryModel);
//				wShiftDB.Line = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wShiftDB.LineID,
//						wFactoryModel);
//				wShiftDB.ModuleName = BPMFunctionModule.getEnumType(wShiftDB.ModuleID).getLable();
//				wShiftTextList.add(wShiftDB);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_SetShiftTextList", "Function Exception:" + ex.toString());
//		}
//		return wShiftTextList;
//	}
//
//	public List<SCHShift> SCH_QueryShiftList(int wCompanyID, int wLoginID, int wLineID, int wWorkShopID, int wModuleID,
//			int wShiftID, OutResult<Integer> wErrorCode) {
//		List<SCHShift> wShiftList = new ArrayList<SCHShift>();
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				// Step0:查询
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" and t.LineID={0}", wLineID);
//
//				if (wWorkShopID > 0)
//					wCondition = StringUtils.Format(" {0} and t.WorkShopID={1}", wCondition, wWorkShopID);
//
//				if (wModuleID > 0)
//					wCondition = StringUtils.Format(" {0} and t.ModuleID={1}", wCondition, wModuleID);
//
//				wSQLText = StringUtils.Format("Select t.* from {0}.sch_shiftlist t ", wInstance.Result)
//						+ " where t.ShiftID>=:ShiftID" + wCondition;
//				wParms.clear();
//				wParms.put("ShiftID", wShiftID);
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					SCHShift wShiftDB = new SCHShift();
//
//					wShiftDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wShiftDB.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wShiftDB.ModuleID = StringUtils.parseInt(wSqlDataReader.get("ModuleID"));
//					wShiftDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wShiftDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//					wShiftDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//					wShiftDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//					wShiftList.add(wShiftDB);
//				}
//				wShiftList = this.SCH_SetShiftTextList(wCompanyID, wShiftList, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryShiftList: Query DB",
//					"Function Exception:" + ex.toString());
//		}
//		return wShiftList;
//	}
//
//	public List<SCHWorker> SCH_QueryWorkerListByShiftID(int wCompanyID, int wLoginID, int wLineID, int wWorkShopID,
//			int wModuleID, int wShiftID, OutResult<Integer> wErrorCode) {
//		List<SCHWorker> wWorkerList = new ArrayList<SCHWorker>();
//		List<SCHWorker> wWorkerDBList = new ArrayList<SCHWorker>();
//		wErrorCode.set(0);
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				// Step0:查询
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" and p.LineID={0}", wLineID);
//
//				if (wWorkShopID > 0)
//					wCondition = StringUtils.Format(" {0} and p.WorkShopID={1}", wCondition, wWorkShopID);
//
//				wSQLText = StringUtils.Format(
//						"Select t.*,p.WorkShopID,p.LineID from {0}.sch_shiftworker t,{1}.bpm_position p ",
//						wInstance.Result, wInstance.Result) + " where t.PositionID=p.ID and t.ShiftID=:ShiftID"
//						+ wCondition;
//				wParms.clear();
//				wParms.put("ShiftID", wShiftID);
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					SCHWorker wWorkerDB = new SCHWorker();
//
//					wWorkerDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wWorkerDB.WorkerID = StringUtils.parseInt(wSqlDataReader.get("WorkerID"));
//					wWorkerDB.PositionID = StringUtils.parseInt(wSqlDataReader.get("PositionID"));
//					wWorkerDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//					wWorkerDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//					wWorkerDB.ShiftID = StringUtils.parseInt(wSqlDataReader.get("ShiftID"));
//					wWorkerDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wWorkerDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//					wWorkerDBList.add(wWorkerDB);
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryShiftWorkerList: Query DB",
//					"Function Exception:" + ex.toString());
//		}
//		try {
//			List<BPMPosition> wPositionList = BPMPositionDAO.getInstance().BPM_QueryPositionListByDB(wCompanyID,
//					wWorkShopID, wLineID, wModuleID, wErrorCode);
//			if (wErrorCode.Result == 0) {
//				for (BPMPosition wPosition : wPositionList) {
//					if (wPosition.Status != BPMStatus.Audited.getValue())
//						continue;
//
//					SCHWorker wWorker = new SCHWorker(wPosition);
//					wWorker.ShiftID = wShiftID;
//					for (SCHWorker wWorkerDB : wWorkerDBList) {
//						if (wWorker.PositionID == wWorkerDB.PositionID) {
//							wWorker.ID = wWorkerDB.ID;
//							wWorker.WorkerID = wWorkerDB.WorkerID;
//							wWorker.Status = wWorkerDB.Status;
//							wWorker.Active = wWorkerDB.Active;
//							wWorker.ShiftID = wWorkerDB.ShiftID;
//							wWorker.CreatorID = wWorkerDB.CreatorID;
//							wWorker.CreateTime = wWorkerDB.CreateTime;
//						}
//					}
//					wWorkerList.add(wWorker);
//				}
//			}
//			wWorkerList = SCHWorkerDAO.getInstance().SCH_SetTextOfWorkerList(wCompanyID, wWorkerList, wErrorCode);
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_QueryShiftWorkerList: Set Text",
//					"Function Exception:" + ex.toString());
//			wErrorCode.set(MESException.Exception.getValue());
//		}
//		return wWorkerList;
//	}
//
//	public int SCH_SaveShiftWorkerList(int wCompanyID, int wLoginID, List<SCHWorker> wWorkerList, int wLineID,
//			int wWorkShopID, int wModuleID, int wShiftID, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//
//		List<SCHWorker> wWorkerNewList = new ArrayList<SCHWorker>();
//		try {
//			// Step01：剔除已存储的排班记录
//			List<SCHWorker> wWorkerListDB = this.SCH_QueryWorkerListByShiftID(wCompanyID, wLoginID, wLineID,
//					wWorkShopID, wModuleID, wShiftID, wErrorCode);
//			for (SCHWorker wWorker : wWorkerList) {
//				boolean wExist = false;
//				for (SCHWorker wWorkerDB : wWorkerListDB) {
//					if (wWorkerDB.PositionID == wWorker.PositionID && wWorkerDB.ModuleID == wWorker.ModuleID
//							&& wWorkerDB.WorkerID == wWorker.WorkerID && wWorkerDB.WorkShopID == wWorker.WorkShopID) {
//						wExist = true;
//						break;
//					}
//				}
//				if (!wExist)
//					wWorkerNewList.add(wWorker);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_SaveShiftWorkerList:Remove Repeat Line",
//					"Function Exception:" + ex.toString());
//		}
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//				for (SCHWorker wWorker : wWorkerNewList) {
//					// Step02：增加或更新排班记录
//					if (wWorker.ID < 1) {
//						wSQLText = StringUtils.Format("Insert Into {0}.sch_shiftworker", wInstance.Result)
//								+ "(WorkerID,PositionID,ShiftID,CreatorID,CreateTime) "
//								+ " Values(:WorkerID,:PositionID,:ShiftID,:CreatorID,:CreateTime);";
//						wParms.clear();
//
//						wParms.put("WorkerID", wWorker.WorkerID);
//						wParms.put("PositionID", wWorker.PositionID);
//						wParms.put("ShiftID", wWorker.ShiftID);
//						wParms.put("CreatorID", wLoginID);
//						wParms.put("CreateTime", Calendar.getInstance());
//
//						KeyHolder keyHolder = new GeneratedKeyHolder();
//
//						SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//						nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//						@SuppressWarnings("unused")
//						int wID = keyHolder.getKey().intValue();
//
//					} else {
//						wSQLText = StringUtils.Format("update {0}.sch_shiftworker", wInstance.Result)
//								+ " set WorkerID=:WorkerID,PositionID=:PositionID,"
//								+ " CreatorID=:CreatorID,CreateTime=:CreateTime where ID=:ID ";
//						wParms.clear();
//
//						wParms.put("ID", wWorker.ID);
//						wParms.put("WorkerID", wWorker.WorkerID);
//						wParms.put("PositionID", wWorker.PositionID);
//						wParms.put("CreatorID", wLoginID);
//						wParms.put("CreateTime", Calendar.getInstance());
//						wSQLText = this.DMLChange(wSQLText);
//						nameJdbcTemplate.update(wSQLText, wParms);
//					}
//				}
//				if (wWorkerNewList.size() > 0) {
//					// Step03：保存排班日志信息
//					SCHShift wShift = new SCHShift(wLineID, wWorkShopID, wModuleID, wShiftID);
//					this.SCH_SaveShift(wCompanyID, wLoginID, wShift, wErrorCode);
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_SaveShiftWorkerList", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
}
