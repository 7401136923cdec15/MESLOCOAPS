package com.mes.loco.aps.server.serviceimpl.dao.sch;

import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class SCHWorkerDAO extends BaseDAO {
	private static SCHWorkerDAO Instance = null;

	private SCHWorkerDAO() {
		super();
	}

	public static SCHWorkerDAO getInstance() {
		if (Instance == null)
			Instance = new SCHWorkerDAO();
		return Instance;
	}

	// 岗位人员配置
//	private SCHWorker SCH_CheckWorker(int wCompanyID, int wLoginID, SCHWorker wWorker, OutResult<Integer> wErrorCode) {
//		SCHWorker wPositionDB = new SCHWorker();
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
//				if (wWorker.ID > 0) {
//					wSQLText = StringUtils.Format("Select * from {0}.sch_worker ", wInstance.Result)
//							+ " where ID!=:ID and WorkerID=:WorkerID and PositionID=:PositionID and TemplateID=:TemplateID";
//					wParms.clear();
//					wParms.put("ID", wWorker.ID);
//					wParms.put("WorkerID", wWorker.WorkerID);
//					wParms.put("PositionID", wWorker.PositionID);
//					wParms.put("TemplateID", wWorker.TemplateID);
//				} else {
//					wSQLText = StringUtils.Format("Select * from {0}.sch_worker ", wInstance.Result)
//							+ " where WorkerID=:WorkerID and PositionID=:PositionID and TemplateID=:TemplateID";
//					wParms.clear();
//					wParms.put("WorkerID", wWorker.WorkerID);
//					wParms.put("PositionID", wWorker.PositionID);
//					wParms.put("TemplateID", wWorker.TemplateID);
//				}
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					wPositionDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wPositionDB.WorkerID = StringUtils.parseInt(wSqlDataReader.get("WorkerID"));
//					wPositionDB.PositionID = StringUtils.parseInt(wSqlDataReader.get("PositionID"));
//					wPositionDB.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//					wPositionDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//					wPositionDB.EditorID = StringUtils.parseInt(wSqlDataReader.get("EditorID"));
//					wPositionDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//					wPositionDB.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//					wPositionDB.EditTime = StringUtils.parseCalendar(wSqlDataReader.get("EditTime"));
//					wPositionDB.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//					wPositionDB.Active = StringUtils.parseBoolean(wSqlDataReader.get("Active"));
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_CheckWorker", "Function Exception:" + ex.toString());
//		}
//		return wPositionDB;
//	}
//
//	public int SCH_AddWorker(int wCompanyID, int wLoginID, SCHWorker wWorker, OutResult<Integer> wErrorCode) {
//
//		int wID = 0;
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID, 110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				SCHWorker wPositionDB = this.SCH_CheckWorker(wCompanyID, wLoginID, wWorker, wErrorCode);
//				if (wPositionDB.ID > 0)
//					wErrorCode.set(MESException.Logic.getValue());
//			}
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("Insert Into {0}.sch_worker", wInstance.Result)
//						+ "(WorkerID,PositionID,CreatorID,EditorID,AuditorID,CreateTime,EditTime,AuditTime,Status,Active,Available,TemplateID) "
//						+ " Values(:WorkerID,:PositionID,:CreatorID,:EditorID,:AuditorID,:CreateTime,:EditTime,:AuditTime,:Status,:Active,:Available,:TemplateID);";
//				wParms.clear();
//
//				wParms.put("WorkerID", wWorker.WorkerID);
//				wParms.put("PositionID", wWorker.PositionID);
//				wParms.put("CreatorID", wLoginID);
//				wParms.put("EditorID", 0);
//				wParms.put("AuditorID", 0);
//
//				wParms.put("CreateTime", Calendar.getInstance());
//				wParms.put("EditTime", Calendar.getInstance());
//				wParms.put("AuditTime", Calendar.getInstance());
//				wParms.put("Status", wWorker.Status);
//				wParms.put("Active", 1);
//				wParms.put("Available", 1);
//				wParms.put("TemplateID", wWorker.TemplateID);
//
//				wSQLText = this.DMLChange(wSQLText);
//				KeyHolder keyHolder = new GeneratedKeyHolder();
//
//				SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//				nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//				wID = keyHolder.getKey().intValue();
//
//				SCHPositionDAO.getInstance().SCH_LoadConfiguration(wCompanyID, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_AddWorker", "Function Exception:" + ex.toString());
//		}
//		return wID;
//	}
//
//	public int SCH_SaveWorker(int wCompanyID, int wLoginID, SCHWorker wWorker, OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID, 110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				SCHWorker wPositionDB = this.SCH_CheckWorker(wCompanyID, wLoginID, wWorker, wErrorCode);
//				if (wPositionDB.ID > 0)
//					wErrorCode.set(MESException.Logic.getValue());
//			}
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_worker", wInstance.Result)
//						+ " set WorkerID=:WorkerID,PositionID=:PositionID,"
//						+ " EditorID=:EditorID,EditTime=:EditTime,Status=:Status where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wWorker.ID);
//				wParms.put("WorkerID", wWorker.WorkerID);
//				wParms.put("PositionID", wWorker.PositionID);
//				wParms.put("EditorID", wLoginID);
//				wParms.put("EditTime", Calendar.getInstance());
//				wParms.put("Status", wWorker.Status);
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//				SCHPositionDAO.getInstance().SCH_LoadConfiguration(wCompanyID, wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_SaveWorker", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public int SCH_DisableWorker(int wCompanyID, int wLoginID, SCHWorker wWorker, OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID, 110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_worker", wInstance.Result) + " set AuditorID=:AuditorID,"
//						+ " AuditTime=:AuditTime,Active=0 where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wWorker.ID);
//				wParms.put("AuditorID", wLoginID);
//				wParms.put("AuditTime", Calendar.getInstance());
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_DisableWorker", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public int SCH_ActiveWorker(int wCompanyID, int wLoginID, SCHWorker wWorker, OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID, 110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_worker", wInstance.Result) + " set AuditorID=:AuditorID,"
//						+ " AuditTime=:AuditTime,Active=1 where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wWorker.ID);
//
//				wParms.put("AuditorID", wLoginID);
//				wParms.put("AuditTime", Calendar.getInstance());
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_ActiveWorker", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public int SCH_AuditWorker(int wCompanyID, int wLoginID, SCHWorker wWorker, OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID, 110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_worker", wInstance.Result) + " set AuditorID=:AuditorID,"
//						+ "AuditTime=:AuditTime,Status=:Status where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wWorker.ID);
//
//				wParms.put("AuditorID", wLoginID);
//				wParms.put("AuditTime", Calendar.getInstance());
//				wParms.put("Status", BPMStatus.Audited);
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_AuditWorker", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public SCHWorker SCH_QueryWorkerByID(int wCompanyID, int wLoginID, int wID, OutResult<Integer> wErrorCode) {
//		SCHWorker wStationDB = new SCHWorker();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				// Step0:查询
//				if (wID > 0) {
//					Map<String, Object> wParms = new HashMap<String, Object>();
//					String wSQLText = "";
//
//					wSQLText = StringUtils.Format(
//							"Select t.*,p.WorkShopID,p.LineID from {0}.sch_worker t,{1}.bpm_position p ",
//							wInstance.Result, wInstance.Result) + " where t.ID=:ID and t.PositionID=p.ID";
//					wParms.clear();
//					wParms.put("ID", wID);
//					wSQLText = this.DMLChange(wSQLText);
//					List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//					for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//						wStationDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//						wStationDB.WorkerID = StringUtils.parseInt(wSqlDataReader.get("WorkerID"));
//						wStationDB.PositionID = StringUtils.parseInt(wSqlDataReader.get("PositionID"));
//						wStationDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//						wStationDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//						wStationDB.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//						wStationDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//						wStationDB.EditorID = StringUtils.parseInt(wSqlDataReader.get("EditorID"));
//						wStationDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//						wStationDB.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//						wStationDB.EditTime = StringUtils.parseCalendar(wSqlDataReader.get("EditTime"));
//						wStationDB.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//						wStationDB.Active = StringUtils.parseBoolean(wSqlDataReader.get("Active"));
//					}
//
//					if (wStationDB.ID > 0) {
//						wStationDB.Creator = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wStationDB.CreatorID);
//						wStationDB.Auditor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wStationDB.AuditorID);
//						wStationDB.Editor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wStationDB.EditorID);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryWorkerByID", "Function Exception:" + ex.toString());
//		}
//		return wStationDB;
//	}
//
//	public List<SCHWorker> SCH_QueryWorkerList(int wCompanyID, int wLoginID, int wLineID, int wWorkShopID,
//			int wModuleID, int wTemplateID, OutResult<Integer> wErrorCode) {
//		List<SCHWorker> wWorkerList = new ArrayList<SCHWorker>();
//		wErrorCode.set(0);
//		try {
//			List<SCHWorker> wTemplateList = SCHWorkerDAO.getInstance().SCH_LoadWorkerListByTemplate(wCompanyID,
//					wLoginID, wLineID, wWorkShopID, wModuleID, wTemplateID, wErrorCode);
//			if (wErrorCode.get() == 0) {
//
//				List<BPMPosition> wPositionList = BPMPositionDAO.getInstance().BPM_QueryPositionListByDB(wCompanyID,
//						wWorkShopID, wLineID, wModuleID, wErrorCode);
//
//				if (wErrorCode.get() == 0) {
//					for (BPMPosition wPosition : wPositionList) {
//						if (wPosition.Status != BPMStatus.Audited.getValue())
//							continue;
//
//						SCHWorker wWorker = new SCHWorker(wPosition);
//						wWorker.TemplateID = wTemplateID;
//						for (SCHWorker wWorkerDB : wTemplateList) {
//							if (wWorker.PositionID == wWorkerDB.PositionID && wWorker.WorkShopID == wWorkerDB.WorkShopID
//									&& wWorker.LineID == wWorkerDB.LineID) {
//								wWorker.ID = wWorkerDB.ID;
//								wWorker.WorkerID = wWorkerDB.WorkerID;
//								wWorker.Status = wWorkerDB.Status;
//								wWorker.Active = wWorkerDB.Active;
//								wWorker.CreatorID = wWorkerDB.CreatorID;
//								wWorker.CreateTime = wWorkerDB.CreateTime;
//								wWorker.EditorID = wWorkerDB.EditorID;
//								wWorker.EditTime = wWorkerDB.EditTime;
//							}
//						}
//						wWorkerList.add(wWorker);
//					}
//				}
//				wWorkerList = this.SCH_SetTextOfWorkerList(wCompanyID, wWorkerList, wErrorCode);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_QueryWorkerList", "Function Exception:" + ex.toString());
//			wErrorCode.set(MESException.Exception.getValue());
//		}
//		return wWorkerList;
//	}
//
//	public List<SCHWorker> SCH_LoadWorkerListByTemplate(int wCompanyID, int wLoginID, int wLineID, int wWorkShopID,
//			int wModuleID, int wTemplateID, OutResult<Integer> wErrorCode) {
//		List<SCHWorker> wTemplateList = new ArrayList<SCHWorker>();
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" and p.LineID={0}", wLineID);
//
//				if (wWorkShopID > 0)
//					wCondition = StringUtils.Format(" {0} and p.WorkShopID={1}", wCondition, wWorkShopID);
//
//				if (wTemplateID > 0)
//					wCondition = StringUtils.Format(" {0} and t.TemplateID={1}", wCondition, wTemplateID);
//
//				String wSQLText = StringUtils.Format(
//						"Select t.*,p.WorkShopID,p.LineID from {0}.sch_worker t,{1}.bpm_position p ", wInstance.Result,
//						wInstance.Result) + " where t.PositionID=p.ID" + wCondition;
//
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.clear();
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					SCHWorker wWorkerDB = new SCHWorker();
//
//					wWorkerDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wWorkerDB.WorkerID = StringUtils.parseInt(wSqlDataReader.get("WorkerID"));
//					wWorkerDB.PositionID = StringUtils.parseInt(wSqlDataReader.get("PositionID"));
//					wWorkerDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wWorkerDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//					wWorkerDB.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//					wWorkerDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//					wWorkerDB.EditorID = StringUtils.parseInt(wSqlDataReader.get("EditorID"));
//					wWorkerDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//					wWorkerDB.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//					wWorkerDB.EditTime = StringUtils.parseCalendar(wSqlDataReader.get("EditTime"));
//					wWorkerDB.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//					wWorkerDB.Active = StringUtils.parseBoolean(wSqlDataReader.get("Active"));
//					wWorkerDB.TemplateID = StringUtils.parseInt(wSqlDataReader.get("TemplateID"));
//					wTemplateList.add(wWorkerDB);
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_LoadWorkerListByTemplate: Query DB",
//					"Function Exception:" + ex.toString());
//		}
//		return wTemplateList;
//	}
//
//	public List<SCHWorker> SCH_SetTextOfWorkerList(int wCompanyID, List<SCHWorker> wWorkerList,
//			OutResult<Integer> wErrorCode) {
//		List<SCHWorker> wStationTextList = new ArrayList<SCHWorker>();
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			for (SCHWorker wWorkerDB : wWorkerList) {
//				wWorkerDB.Creator = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wWorkerDB.CreatorID, wEntryEmployee);
//				wWorkerDB.Auditor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wWorkerDB.AuditorID, wEntryEmployee);
//				wWorkerDB.Editor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wWorkerDB.EditorID, wEntryEmployee);
//				wWorkerDB.WorkerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wWorkerDB.WorkerID, wEntryEmployee);
//				FMCWorkShop wWorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopByID(wCompanyID,
//						wWorkerDB.WorkShopID, wFactoryModel);
//				if (wWorkShop.ID > 0) {
//					wWorkerDB.Factory = FMCFactoryDAO.getInstance().FMC_QueryFactoryNameByID(wCompanyID,
//							wWorkShop.FactoryID, wFactoryModel);
//					wWorkerDB.BusinessUnit = FMCFactoryDAO.getInstance().FMC_QueryBusinessUnitNameByID(wCompanyID,
//							wWorkShop.BusinessUnitID, wFactoryModel);
//				}
//				wWorkerDB.WorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopNameByID(wCompanyID,
//						wWorkerDB.WorkShopID, wFactoryModel);
//				wWorkerDB.Line = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wWorkerDB.LineID,
//						wFactoryModel);
//				wStationTextList.add(wWorkerDB);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_SetWorkerTextList", "Function Exception:" + ex.toString());
//		}
//		return wStationTextList;
//	}
//
//	public int SCH_SaveWorkerList(int wCompanyID, int wLoginID, List<SCHWorker> wWorkerList,
//			OutResult<Integer> wErrorCode) {
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID, 110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//				for (SCHWorker wWorker : wWorkerList) {
//					SCHWorker wWorkerDB = this.SCH_CheckWorker(wCompanyID, wLoginID, wWorker, wErrorCode);
//					if (wWorkerDB.ID > 0 || wWorker.WorkerID < 1) {
//						wErrorCode.set(MESException.Logic.getValue());
//						break;
//					}
//					if (wWorker.ID < 1) {
//						wSQLText = StringUtils.Format("Insert Into {0}.sch_worker", wInstance.Result)
//								+ "(WorkerID,PositionID,CreatorID,EditorID,AuditorID,CreateTime,EditTime,AuditTime,Status,Active,Available,TemplateID) "
//								+ " Values(:WorkerID,:PositionID,:CreatorID,:EditorID,:AuditorID,:CreateTime,:EditTime,:AuditTime,:Status,:Active,:Available,:TemplateID);";
//						wParms.clear();
//
//						wParms.put("WorkerID", wWorker.WorkerID);
//						wParms.put("PositionID", wWorker.PositionID);
//						wParms.put("CreatorID", wLoginID);
//						wParms.put("EditorID", 0);
//						wParms.put("AuditorID", 0);
//
//						wParms.put("CreateTime", Calendar.getInstance());
//						wParms.put("EditTime", Calendar.getInstance());
//						wParms.put("AuditTime", Calendar.getInstance());
//						wParms.put("Status", wWorker.Status);
//						wParms.put("Active", 1);
//						wParms.put("Available", 1);
//						wParms.put("TemplateID", wWorker.TemplateID);
//						wSQLText = this.DMLChange(wSQLText);
//						KeyHolder keyHolder = new GeneratedKeyHolder();
//
//						SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//						nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//						@SuppressWarnings("unused")
//						int wID = keyHolder.getKey().intValue();
//
//					} else {
//						wSQLText = StringUtils.Format("update {0}.sch_worker", wInstance.Result)
//								+ " set WorkerID=:WorkerID,PositionID=:PositionID,"
//								+ " EditorID=:EditorID,EditTime=:EditTime,Status=:Status where ID=:ID ";
//						wParms.clear();
//
//						wParms.put("ID", wWorker.ID);
//						wParms.put("WorkerID", wWorker.WorkerID);
//						wParms.put("PositionID", wWorker.PositionID);
//						wParms.put("EditorID", wLoginID);
//						wParms.put("EditTime", Calendar.getInstance());
//						wParms.put("Status", wWorker.Status);
//						wSQLText = this.DMLChange(wSQLText);
//						nameJdbcTemplate.update(wSQLText, wParms);
//					}
//					SCHPositionDAO.getInstance().SCH_LoadConfiguration(wCompanyID, wErrorCode);
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_SaveWorkerList", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public List<SCHWorker> SCH_QuerySubWorkerListByLoginID(int wCompanyID, int wLoginID, int wEventID,
//			OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//		List<SCHWorker> wWorkerList = new ArrayList<SCHWorker>();
//		try {
//			List<SCHPosition> wSubPositionList = SCHPositionDAO.getInstance()
//					.SFC_QuerySubPositionListByLoginID(wCompanyID, wLoginID, wEventID, wErrorCode);
//			for (SCHPosition wPosition : wSubPositionList) {
//				if (wPosition.WorkerID > 0) {
//					SCHWorker wWorker = new SCHWorker();
//					wWorker.WorkerID = wPosition.WorkerID;
//					wWorker.WorkShopID = wPosition.WorkShopID;
//					wWorker.LineID = wPosition.LineID;
//					wWorker.PositionID = wPosition.PositionID;
//					wWorker.FunctionID = wPosition.FunctionID;
//					wWorker.ModuleID = wPosition.ModuleID;
//					wWorker.CreateTime = Calendar.getInstance();
//					wWorker.AuditTime = Calendar.getInstance();
//					wWorker.EditTime = Calendar.getInstance();
//					boolean wNewWorker = true;
//					for (SCHWorker wSubWorker : wWorkerList) {
//						if (wSubWorker.WorkerID == wWorker.WorkerID) {
//							wNewWorker = false;
//							break;
//						}
//					}
//					if (wNewWorker)
//						wWorkerList.add(wWorker);
//				}
//			}
//			wWorkerList = this.SCH_SetTextOfWorkerList(wCompanyID, wWorkerList, wErrorCode);
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.Exception.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QuerySubWorkerListByLoginID",
//					"Function Exception:" + ex.toString());
//		}
//		return wWorkerList;
//	}
//
//	public List<SCHWorker> SCH_QueryWorkerByPositionID(int wCompanyID, int wLoginID, int wPositionID, int wShiftID,
//			OutResult<Integer> wErrorCode) {
//		List<SCHWorker> wPositionWorker = new ArrayList<SCHWorker>();
//
//		try {
//			List<Integer> wWorkerIDList = new ArrayList<Integer>();
//			List<SCHWorker> wWorkerListDB = SCHShiftDAO.getInstance().SCH_QueryWorkerListByShiftID(wCompanyID, wLoginID,
//					0, 0, 0, wShiftID, wErrorCode);
//			for (SCHWorker wWorkerDB : wWorkerListDB) {
//				if (wWorkerDB.PositionID == wPositionID) {
//					if (wWorkerIDList.contains(wWorkerDB.WorkerID))
//						continue;
//
//					wPositionWorker.add(wWorkerDB);
//					wWorkerIDList.add(wWorkerDB.WorkerID);
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.Exception.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryWorkerByPositionID",
//					"Function Exception:" + ex.toString());
//
//		}
//		return wPositionWorker;
//	}
//
//	public List<SCHWorker> SCH_QueryLeadWorkerByPositionID(int wCompanyID, int wLoginID, int wPositionID, int wShiftID,
//			OutResult<Integer> wErrorCode) {
//		List<SCHWorker> wLeadWorkerList = new ArrayList<>();
//
//		try {
//			// Step03: 流程模型
//			MESEntry wBPMModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BPMModel);
//			BPMPosition wPosition = BPMPositionDAO.getInstance().BPM_QueryPositionByID(wCompanyID, wPositionID,
//					wBPMModel);
//
//			wLeadWorkerList = this.SCH_QueryWorkerByPositionID(wCompanyID, wLoginID, wPosition.ParentID, wShiftID,
//					wErrorCode);
//			if (wLeadWorkerList == null || wLeadWorkerList.size() <= 0) {
//				BMSPosition wBMSPosition = BMSPositionDAO.getInstance().BMS_QueryPositionByID(wCompanyID, wLoginID,
//						wPositionID, wErrorCode);
//				wBMSPosition = BMSPositionDAO.getInstance().BMS_QueryPositionByID(wCompanyID, wLoginID,
//						wBMSPosition.ParentID, wErrorCode);
//
//				List<BMSEmployee> wBMSEmployeeList = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeList(wCompanyID,
//						wLoginID, 0, wBMSPosition.ID, 1, wErrorCode);
//				if (wBMSEmployeeList != null && wBMSEmployeeList.size() > 0) {
//
//					for (BMSEmployee wBMSEmployee : wBMSEmployeeList) {
//						SCHWorker wSCHWorker = new SCHWorker();
//						wSCHWorker.WorkerID = wBMSEmployee.ID;
//						wSCHWorker.WorkerName = wBMSEmployee.Name;
//						wLeadWorkerList.add(wSCHWorker);
//					}
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.Exception.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryLeadWorkerByPositionID",
//					"Function Exception:" + ex.toString());
//		}
//		return wLeadWorkerList;
//	}
}
