package com.mes.loco.aps.server.serviceimpl.dao.sch;

import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class SCHPositionDAO extends BaseDAO {
	private static SCHPositionDAO Instance = null;

	private SCHPositionDAO() {
		super();
	}

	public static SCHPositionDAO getInstance() {
		if (Instance == null)
			Instance = new SCHPositionDAO();
		return Instance;
	}

	// wSqlDataReader\[(\"\w+\")\] wSqlDataReader.get($1)
	// 工位岗位配置
//	private SCHPosition SCH_CheckPosition(int wCompanyID, int wLoginID, SCHPosition wPosition,
//			OutResult<Integer> wErrorCode) {
//		SCHPosition wPositionDB = new SCHPosition();
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				// Step0:查询
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//				if (wPosition.ID > 0) {
//					wSQLText = StringUtils.Format("Select * from {0}.sch_position ", wInstance.Result)
//							+ " where ID!=:ID and LineID=:LineID and PartID=:PartID and PartPointID=:PartPointID and  DeviceID=:DeviceID "
//							+ " and PositionLevel=:PositionLevel and WorkShopID=:WorkShopID and StationID=:StationID and PositionID=:PositionID";
//					wParms.clear();
//					wParms.put("ID", wPosition.ID);
//					wParms.put("LineID", wPosition.LineID);
//					wParms.put("PartID", wPosition.PartID);
//					wParms.put("PartPointID", wPosition.PartPointID);
//					wParms.put("DeviceID", wPosition.DeviceID);
//
//					wParms.put("PositionLevel", wPosition.PositionLevel);
//					wParms.put("WorkShopID", wPosition.WorkShopID);
//					wParms.put("StationID", wPosition.StationID);
//					wParms.put("PositionID", wPosition.PositionID);
//				} else {
//					wSQLText = StringUtils.Format("Select * from {0}.sch_position ", wInstance.Result)
//							+ " where LineID=:LineID and PartID=:PartID and PartPointID=:PartPointID and  DeviceID=:DeviceID and PositionLevel=:PositionLevel"
//							+ " and WorkShopID=:WorkShopID and StationID=:StationID and PositionID=:PositionID";
//					wParms.clear();
//					wParms.put("LineID", wPosition.LineID);
//					wParms.put("PartID", wPosition.PartID);
//					wParms.put("PartPointID", wPosition.PartPointID);
//					wParms.put("DeviceID", wPosition.DeviceID);
//					wParms.put("PositionLevel", wPosition.PositionLevel);
//
//					wParms.put("WorkShopID", wPosition.WorkShopID);
//					wParms.put("StationID", wPosition.StationID);
//					wParms.put("PositionID", wPosition.PositionID);
//				}
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					wPositionDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wPositionDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wPositionDB.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//					wPositionDB.PartPointID = StringUtils.parseInt(wSqlDataReader.get("PartPointID"));
//					wPositionDB.DeviceID = StringUtils.parseInt(wSqlDataReader.get("DeviceID"));
//					wPositionDB.PositionLevel = StringUtils.parseInt(wSqlDataReader.get("PositionLevel"));
//					wPositionDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//					wPositionDB.StationID = StringUtils.parseInt(wSqlDataReader.get("StationID"));
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
//			LoggerTool.SaveException("SCHService", "SCH_CheckPosition", "Function Exception:" + ex.toString());
//		}
//		return wPositionDB;
//	}
//
//	public int SCH_AddPosition(int wCompanyID, int wLoginID, SCHPosition wPosition, OutResult<Integer> wErrorCode) {
//
//		int wID = 0;
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				SCHPosition wPositionDB = this.SCH_CheckPosition(wCompanyID, wLoginID, wPosition, wErrorCode);
//				if (wPositionDB.ID > 0)
//					wErrorCode.set(MESException.Logic.getValue());
//			}
//			if (wErrorCode.get() == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("Insert Into {0}.sch_position", wInstance.Result)
//						+ "(WorkShopID,LineID,PartID,PartPointID,DeviceID,StationID,PositionLevel,PositionID,CreatorID,EditorID,AuditorID,CreateTime,EditTime,AuditTime,Status,Active,Available) "
//						+ " Values(:WorkShopID,:LineID,:PartID,:PartPointID,:DeviceID,:StationID,:PositionLevel,:PositionID,:CreatorID,:EditorID,:AuditorID,:CreateTime,:EditTime,:AuditTime,:Status,:Active,:Available);";
//				wParms.clear();
//
//				wParms.put("LineID", wPosition.LineID);
//				wParms.put("PartID", wPosition.PartID);
//				wParms.put("PartPointID", wPosition.PartPointID);
//				wParms.put("DeviceID", wPosition.DeviceID);
//				wParms.put("PositionLevel", wPosition.PositionLevel);
//				wParms.put("WorkShopID", wPosition.WorkShopID);
//				wParms.put("StationID", wPosition.StationID);
//				wParms.put("PositionID", wPosition.PositionID);
//				wParms.put("CreatorID", wLoginID);
//				wParms.put("EditorID", 0);
//				wParms.put("AuditorID", 0);
//
//				wParms.put("CreateTime", Calendar.getInstance());
//				wParms.put("EditTime", Calendar.getInstance());
//				wParms.put("AuditTime", Calendar.getInstance());
//				wParms.put("Status", wPosition.Status);
//				wParms.put("Active", 1);
//				wParms.put("Available", 1);
//
//				wSQLText = this.DMLChange(wSQLText);
//				KeyHolder keyHolder = new GeneratedKeyHolder();
//
//				SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParms);
//				nameJdbcTemplate.update(wSQLText, wSqlParameterSource, keyHolder);
//
//				wID = keyHolder.getKey().intValue();
//
//				SCHPositionDAO.getInstance().SCH_LoadConfiguration(wCompanyID,wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_AddPosition", "Function Exception:" + ex.toString());
//		}
//		return wID;
//	}
//
//	public int SCH_SavePosition(int wCompanyID, int wLoginID, SCHPosition wPosition, OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				SCHPosition wPositionDB = this.SCH_CheckPosition(wCompanyID, wLoginID, wPosition, wErrorCode);
//				if (wPositionDB.ID > 0)
//					wErrorCode.set(MESException.Logic.getValue());
//			}
//			if (wErrorCode.get() == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_position", wInstance.Result)
//						+ " set WorkShopID=:WorkShopID,LineID=:LineID,PartID=:PartID,PartPointID=:PartPointID,DeviceID=:DeviceID,StationID=:StationID,"
//						+ " PositionLevel=:PositionLevel,PositionID=:PositionID,EditorID=:EditorID,EditTime=:EditTime,Status=:Status where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wPosition.ID);
//				wParms.put("LineID", wPosition.LineID);
//				wParms.put("PartID", wPosition.PartID);
//				wParms.put("PartPointID", wPosition.PartPointID);
//				wParms.put("DeviceID", wPosition.DeviceID);
//				wParms.put("PositionLevel", wPosition.PositionLevel);
//				wParms.put("WorkShopID", wPosition.WorkShopID);
//				wParms.put("StationID", wPosition.StationID);
//				wParms.put("PositionID", wPosition.PositionID);
//				wParms.put("EditorID", wLoginID);
//				wParms.put("EditTime", Calendar.getInstance());
//				wParms.put("Status", wPosition.Status);
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//				SCHPositionDAO.getInstance().SCH_LoadConfiguration(wCompanyID,wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_SavePosition", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public int SCH_DisablePosition(int wCompanyID, int wLoginID, SCHPosition wPosition, OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_position", wInstance.Result)
//						+ " set AuditorID=:AuditorID," + " AuditTime=:AuditTime,Active=0 where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wPosition.ID);
//				wParms.put("AuditorID", wLoginID);
//				wParms.put("AuditTime", Calendar.getInstance());
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_DisablePosition", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public int SCH_ActivePosition(int wCompanyID, int wLoginID, SCHPosition wPosition, OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_position", wInstance.Result)
//						+ " set AuditorID=:AuditorID," + " AuditTime=:AuditTime,Active=1 where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wPosition.ID);
//
//				wParms.put("AuditorID", wLoginID);
//				wParms.put("AuditTime", Calendar.getInstance());
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_ActivePosition", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public int SCH_AuditPosition(int wCompanyID, int wLoginID, SCHPosition wPosition, OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_position", wInstance.Result)
//						+ " set AuditorID=:AuditorID," + "AuditTime=:AuditTime,Status=:Status where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wPosition.ID);
//
//				wParms.put("AuditorID", wLoginID);
//				wParms.put("AuditTime", Calendar.getInstance());
//				wParms.put("Status", wPosition.Status);
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_AuditPosition", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.get();
//	}
//
//	public SCHPosition SCH_QueryPositionByID(int wCompanyID, int wLoginID, int wID, OutResult<Integer> wErrorCode) {
//		SCHPosition wPositionDB = new SCHPosition();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				// Step0:查询
//				if (wID > 0) {
//					Map<String, Object> wParms = new HashMap<String, Object>();
//					String wSQLText = "";
//
//					wSQLText = StringUtils.Format("Select t.* from {0}.sch_positiont where t.ID=:ID ",
//							wInstance.Result);
//					wParms.clear();
//					wParms.put("ID", wID);
//					wSQLText = this.DMLChange(wSQLText);
//					List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//					for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//						wPositionDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//						wPositionDB.StationID = StringUtils.parseInt(wSqlDataReader.get("StationID"));
//						wPositionDB.PositionID = StringUtils.parseInt(wSqlDataReader.get("PositionID"));
//						wPositionDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//						wPositionDB.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//						wPositionDB.PartPointID = StringUtils.parseInt(wSqlDataReader.get("PartPointID"));
//						wPositionDB.DeviceID = StringUtils.parseInt(wSqlDataReader.get("DeviceID"));
//						wPositionDB.PositionLevel = StringUtils.parseInt(wSqlDataReader.get("PositionLevel"));
//						wPositionDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//						// wStationDB.DeviceID = StringUtils.parseInt(wSqlDataReader.get("DeviceID"));
//						wPositionDB.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//						wPositionDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//						wPositionDB.EditorID = StringUtils.parseInt(wSqlDataReader.get("EditorID"));
//						wPositionDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//						wPositionDB.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//						wPositionDB.EditTime = StringUtils.parseCalendar(wSqlDataReader.get("EditTime"));
//						wPositionDB.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//						wPositionDB.Active = StringUtils.parseBoolean(wSqlDataReader.get("Active"));
//					}
//
//					if (wPositionDB.ID > 0) {
//						// Step01：人员姓名
//						MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//						// Step02：工厂与事业部
//						MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID,
//								MESEntryEnum.FactoryModel);
//						// Step03: 流程模型
//						MESEntry wBPMModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BPMModel);
//						// Step04：工艺模型
//						MESEntry wRouteModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.RouteModel);
//
//						wPositionDB.Creator = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wPositionDB.CreatorID, wEntryEmployee);
//						wPositionDB.Auditor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wPositionDB.AuditorID, wEntryEmployee);
//						wPositionDB.Editor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wPositionDB.EditorID, wEntryEmployee);
//						wPositionDB.WorkerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wPositionDB.WorkerID, wEntryEmployee);
//						FMCWorkShop wWorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopByID(wCompanyID,
//								wPositionDB.WorkShopID, wFactoryModel);
//						if (wWorkShop.ID > 0) {
//							wPositionDB.Factory = FMCFactoryDAO.getInstance().FMC_QueryFactoryNameByID(wCompanyID,
//									wWorkShop.FactoryID, wFactoryModel);
//							wPositionDB.BusinessUnit = FMCFactoryDAO.getInstance()
//									.FMC_QueryBusinessUnitNameByID(wCompanyID, wWorkShop.BusinessUnitID, wFactoryModel);
//						}
//						wPositionDB.WorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopNameByID(wCompanyID,
//								wPositionDB.WorkShopID, wFactoryModel);
//						wPositionDB.Line = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID,
//								wPositionDB.LineID, wFactoryModel);
//						wPositionDB.StationName = FMCStationDAO.getInstance().FMC_QueryStationNameByID(wCompanyID,
//								wPositionDB.StationID, wFactoryModel);
//
//						wPositionDB.PartName = FPCPartDAO.getInstance().FPC_QueryPartNameByID(wCompanyID,
//								wPositionDB.PartID, wRouteModel);
//						wPositionDB.PartPointName = FPCPartDAO.getInstance().FPC_QueryPartPointNameByID(wCompanyID,
//								wPositionDB.PartPointID, wRouteModel);
//
//						if (wPositionDB.PositionID > 0) {
//							BPMPosition wPosition = BPMPositionDAO.getInstance().BPM_QueryPositionByID(wCompanyID,
//									wPositionDB.PositionID, wBPMModel);
//							wPositionDB.FunctionID = wPosition.FunctionID;
//							wPositionDB.PositionName = wPosition.Name;
//							BPMFunction wFunction = BPMFunctionDAO.getInstance().BPM_QueryFunctionByID(wCompanyID,
//									wPositionDB.FunctionID, wBPMModel);
//							wPositionDB.FunctionName = wFunction.Name;
//							wPositionDB.ModuleID = wFunction.ModuleID;
//							wPositionDB.ModuleName = BPMFunctionModule.getEnumType(wFunction.ModuleID).getLable();
//						}
//					}
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryPositionByID", "Function Exception:" + ex.toString());
//
//		}
//		return wPositionDB;
//	}
//
//	public List<SCHPosition> SCH_QueryPositionListByLoginID(int wCompanyID, int wLoginID,
//			OutResult<Integer> wErrorCode) {
//		List<SCHPosition> wPositionList = new ArrayList<SCHPosition>();
//
//		try {
//			MESEntry wSCHModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.SCHModel);
//			for (SCHPosition wSCHPosition : wSCHModel.SCHModel.PositionList) {
//				if (wSCHPosition.WorkerID == wLoginID) {
//					wPositionList.add(wSCHPosition);
//				}
//			}
//			wPositionList = this.SCH_SetPositionTextList(wCompanyID, wPositionList, wErrorCode);
//			wPositionList = wPositionList.stream().sorted(Comparator.comparing(SCHPosition::getLineID)
//					.thenComparing(p -> p.getModuleID()).thenComparing(p -> p.getStationID()))
//					.collect(Collectors.toList());
//			// wPositionList = wPositionList.OrderBy(p => p.LineID).ThenBy(p =>
//			// p.ModuleID).ThenBy(p => p.StationID).ToList();
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_QueryPositionListByLoginID: Set Text",
//					"Function Exception:" + ex.toString());
//			wErrorCode.set(MESException.Exception.getValue());
//		}
//		return wPositionList;
//	}
//
//	public List<SCHPosition> SCH_QueryPositionListByShiftID(int wCompanyID, int wLoginID, int wLineID, int wWorkShopID,
//			int wPositionLevel, int wShiftID, boolean wFillShift, OutResult<Integer> wErrorCode) {
//		List<SCHPosition> wPositionList = new ArrayList<SCHPosition>();
//
//		try {
//			wPositionList = SCHPositionDAO.getInstance().SCH_LoadPositionListByDB(wCompanyID, wLoginID, wLineID,
//					wWorkShopID, SCHPositionLevel.getEnumType(wPositionLevel), wErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				List<SCHWorker> wTemplateList = SCHWorkerDAO.getInstance().SCH_LoadWorkerListByTemplate(wCompanyID, 0,
//						0, 0, 0, 0, wErrorCode);
//				if (wErrorCode.get() == 0) {
//					List<SCHWorker> wShiftWorkerList = new ArrayList<SCHWorker>();
//					if (wFillShift)
//						wShiftWorkerList = SCHShiftDAO.getInstance().SCH_QueryWorkerListByShiftID(wCompanyID, wLoginID,
//								wLineID, wWorkShopID, 0, wShiftID,wErrorCode);
//
//					for (SCHPosition wPositionDB : wPositionList) {
//						if (wPositionDB.Status != BPMStatus.Audited.getValue())
//							continue;
//
//						for (SCHWorker wWorkerDB : wShiftWorkerList) {
//							if (wPositionDB.PositionID == wWorkerDB.PositionID && wWorkerDB.WorkerID > 0) {
//								wPositionDB.OnShift = true;
//								wPositionDB.WorkerID = wWorkerDB.WorkerID;
//								break;
//							}
//						}
//						if (wPositionDB.WorkerID < 1) {
//							for (SCHWorker wWorkerDB : wTemplateList) {
//								if (wPositionDB.PositionID == wWorkerDB.PositionID) {
//									wPositionDB.OnShift = false;
//									wPositionDB.WorkerID = wWorkerDB.WorkerID;
//									break;
//								}
//							}
//						}
//					}
//					wPositionList = this.SCH_SetPositionTextList(wCompanyID, wPositionList, wErrorCode);
//					wPositionList = wPositionList
//							.stream().sorted(Comparator.comparing(SCHPosition::getLineID)
//									.thenComparing(p -> p.getModuleID()).thenComparing(p -> p.getStationID()))
//							.collect(Collectors.toList());
//					// wPositionList = wPositionList.OrderBy(p => p.LineID).ThenBy(p =>
//					// p.ModuleID).ThenBy(p => p.StationID).ToList();
//				}
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_QueryPositionList: Set Text",
//					"Function Exception:" + ex.toString());
//			wErrorCode.set(MESException.Exception.getValue());
//		}
//		return wPositionList;
//	}
//
//	public List<SCHPosition> SCH_QueryPositionListByDeviceID(int wCompanyID, int wLoginID, int wDeviceID,
//			OutResult<Integer> wErrorCode) {
//
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//		List<SCHPosition> wDevicePositionList = new ArrayList<SCHPosition>();
//		try {
//			List<SCHPosition> wPositionList = SCHPositionDAO.getInstance().SCH_LoadPositionListByDB(wCompanyID,
//					wLoginID, 0, 0, SCHPositionLevel.Device, wErrorCode);
//			wPositionList = this.SCH_SetPositionTextList(wCompanyID, wPositionList, wErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				List<SCHWorker> wTemplateList = SCHWorkerDAO.getInstance().SCH_LoadWorkerListByTemplate(wCompanyID, 0,
//						0, 0, BPMFunctionModule.Equipment.getValue(), 0, wErrorCode);
//				if (wErrorCode.get() == 0) {
//					List<SCHWorker> wShiftWorkerList = SCHShiftDAO.getInstance().SCH_QueryWorkerListByShiftID(
//							wCompanyID, wLoginID, 0, 0, BPMFunctionModule.Equipment.getValue(), SFCTaskDAO.ShiftID,wErrorCode);
//					for (SCHPosition wPositionDB : wPositionList) {
//						if (wPositionDB.Status != BPMStatus.Audited.getValue())
//							continue;
//
//						if (wPositionDB.ModuleID != BPMFunctionModule.Equipment.getValue())
//							continue;
//
//						if (wPositionDB.StationID != wDeviceID)
//							continue;
//
//						for (SCHWorker wWorkerDB : wShiftWorkerList) {
//							if (wPositionDB.PositionID == wWorkerDB.PositionID && wWorkerDB.WorkerID > 0) {
//								wPositionDB.OnShift = true;
//								wPositionDB.WorkerID = wWorkerDB.WorkerID;
//								break;
//							}
//						}
//						if (wPositionDB.WorkerID < 1) {
//							for (SCHWorker wWorkerDB : wTemplateList) {
//								if (wPositionDB.PositionID == wWorkerDB.PositionID) {
//									wPositionDB.OnShift = false;
//									wPositionDB.WorkerID = wWorkerDB.WorkerID;
//									break;
//								}
//							}
//						}
//						wDevicePositionList.add(wPositionDB);
//					}
//
//					wDevicePositionList = wDevicePositionList
//							.stream().sorted(Comparator.comparing(SCHPosition::getLineID)
//									.thenComparing(p -> p.getModuleID()).thenComparing(p -> p.getStationID()))
//							.collect(Collectors.toList());
//					// wDevicePositionList = wDevicePositionList.OrderBy(p => p.LineID).ThenBy(p =>
//					// p.ModuleID).ThenBy(p => p.StationID).ToList();
//				}
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_QueryPositionListByDeviceID",
//					"Function Exception:" + ex.toString());
//		}
//		return wDevicePositionList;
//	}
//
//	private List<SCHPosition> SCH_SetPositionTextList(int wCompanyID, List<SCHPosition> wStationList,
//			OutResult<Integer> wErrorCode) {
//		List<SCHPosition> wStationTextList = new ArrayList<SCHPosition>();
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			// Step03: 流程模型
//			MESEntry wBPMModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BPMModel);
//			// Step04：工艺模型
//			MESEntry wRouteModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.RouteModel);
//
//			for (SCHPosition wPositionDB : wStationList) {
//				wPositionDB.Creator = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wPositionDB.CreatorID, wEntryEmployee);
//				wPositionDB.Auditor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wPositionDB.AuditorID, wEntryEmployee);
//				wPositionDB.Editor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wPositionDB.EditorID, wEntryEmployee);
//				wPositionDB.WorkerName = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wPositionDB.WorkerID, wEntryEmployee);
//				FMCWorkShop wWorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopByID(wCompanyID,
//						wPositionDB.WorkShopID, wFactoryModel);
//				if (wWorkShop.ID > 0) {
//					wPositionDB.Factory = FMCFactoryDAO.getInstance().FMC_QueryFactoryNameByID(wCompanyID,
//							wWorkShop.FactoryID, wFactoryModel);
//					wPositionDB.BusinessUnit = FMCFactoryDAO.getInstance().FMC_QueryBusinessUnitNameByID(wCompanyID,
//							wWorkShop.BusinessUnitID, wFactoryModel);
//				}
//				wPositionDB.WorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopNameByID(wCompanyID,
//						wPositionDB.WorkShopID, wFactoryModel);
//				wPositionDB.Line = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wPositionDB.LineID,
//						wFactoryModel);
//				wPositionDB.StationName = FMCStationDAO.getInstance().FMC_QueryStationNameByID(wCompanyID,
//						wPositionDB.StationID, wFactoryModel);
//
//				wPositionDB.PartName = FPCPartDAO.getInstance().FPC_QueryPartNameByID(wCompanyID, wPositionDB.PartID,
//						wRouteModel);
//				wPositionDB.PartPointName = FPCPartDAO.getInstance().FPC_QueryPartPointNameByID(wCompanyID,
//						wPositionDB.PartPointID, wRouteModel);
//
//				if (wPositionDB.PositionID > 0) {
//					BPMPosition wPosition = BPMPositionDAO.getInstance().BPM_QueryPositionByID(wCompanyID,
//							wPositionDB.PositionID, wBPMModel);
//					wPositionDB.FunctionID = wPosition.FunctionID;
//					wPositionDB.PositionName = wPosition.Name;
//					BPMFunction wFunction = BPMFunctionDAO.getInstance().BPM_QueryFunctionByID(wCompanyID,
//							wPositionDB.FunctionID, wBPMModel);
//					wPositionDB.FunctionName = wFunction.Name;
//					wPositionDB.ModuleID = wFunction.ModuleID;
//					wPositionDB.ModuleName = BPMFunctionModule.getEnumType(wFunction.ModuleID).getLable();
//				}
//				wStationTextList.add(wPositionDB);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_SetPositionTextList", "Function Exception:" + ex.toString());
//		}
//		return wStationTextList;
//	}
//
//	// 静态函数
//	// 公共函数
//	public int SCH_LoadConfiguration(int wCompanyID, OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		try {
//			MESServer.MES_LoadEntryByDB(wCompanyID, MESEntryEnum.SCHModel,wErrorCode);
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_LoadConfiguration", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public SCHModel SCH_LoadSCHModel(int wCompanyID, OutResult<Integer> wErrorCode) {
//		SCHModel wSCHModel = new SCHModel();
//		try {
//			wSCHModel.PositionList = SCHPositionDAO.getInstance().SCH_LoadPositionWorkerListByShiftID(wCompanyID,
//					SFCTaskDAO.ShiftID, wErrorCode);
//			wSCHModel.ErrorCode = wErrorCode.get();
//		} catch (Exception ex) {
//			wSCHModel.ErrorCode = MESException.Exception.getValue();
//			LoggerTool.SaveException("SCHService", "SCH_LoadSCHModel ", "Function Exception:" + ex.toString());
//		}
//		return wSCHModel;
//	}
//
//	public List<SCHPosition> SCH_LoadPositionListByDB(int wCompanyID, int wLoginID, int wLineID, int wWorkShopID,
//			SCHPositionLevel wPositionLevel, OutResult<Integer> wErrorCode) {
//		List<SCHPosition> wPositionList = new ArrayList<SCHPosition>();
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.get() == 0) {
//				// Step0:查询
//				String wCondition = "";
//				if (wLineID > 0)
//					wCondition = StringUtils.Format(" where t.LineID={0}", wLineID);
//
//				if (wWorkShopID > 0) {
//					if (wCondition.length() > 1)
//						wCondition = StringUtils.Format(" {0} and t.WorkShopID={1}", wCondition, wWorkShopID);
//					else
//						wCondition = StringUtils.Format(" where t.WorkShopID={0}", wWorkShopID);
//				}
//				if (wWorkShopID > 0) {
//					if (wCondition.length() > 1)
//						wCondition = StringUtils.Format(" {0} and t.WorkShopID={1}", wCondition, wWorkShopID);
//					else
//						wCondition = StringUtils.Format(" where t.WorkShopID={0}", wWorkShopID);
//				}
//				if (wPositionLevel != SCHPositionLevel.Default) {
//					if (wCondition.length() > 1)
//						wCondition = StringUtils.Format(" {0} and t.PositionLevel={1}", wCondition,
//								wPositionLevel.getValue());
//					else
//						wCondition = StringUtils.Format(" where t.PositionLevel={0}", wPositionLevel.getValue());
//				}
//				String wSQLText = StringUtils.Format("Select t.* from {0}.sch_position t ", wInstance.Result)
//						+ wCondition;
//
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				wParms.clear();
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					SCHPosition wPositionDB = new SCHPosition();
//					wPositionDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wPositionDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wPositionDB.PartID = StringUtils.parseInt(wSqlDataReader.get("PartID"));
//					wPositionDB.PartPointID = StringUtils.parseInt(wSqlDataReader.get("PartPointID"));
//					wPositionDB.DeviceID = StringUtils.parseInt(wSqlDataReader.get("DeviceID"));
//					wPositionDB.PositionLevel = StringUtils.parseInt(wSqlDataReader.get("PositionLevel"));
//					wPositionDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//					wPositionDB.StationID = StringUtils.parseInt(wSqlDataReader.get("StationID"));
//					wPositionDB.PositionID = StringUtils.parseInt(wSqlDataReader.get("PositionID"));
//					wPositionDB.AuditorID = StringUtils.parseInt(wSqlDataReader.get("AuditorID"));
//					wPositionDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//					wPositionDB.EditorID = StringUtils.parseInt(wSqlDataReader.get("EditorID"));
//					wPositionDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//					wPositionDB.AuditTime = StringUtils.parseCalendar(wSqlDataReader.get("AuditTime"));
//					wPositionDB.EditTime = StringUtils.parseCalendar(wSqlDataReader.get("EditTime"));
//					wPositionDB.Status = StringUtils.parseInt(wSqlDataReader.get("Status"));
//					wPositionDB.Active = StringUtils.parseBoolean(wSqlDataReader.get("Active"));
//					wPositionList.add(wPositionDB);
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_LoadPositionListByDB: Query DB",
//					"Function Exception:" + ex.toString());
//		}
//		return wPositionList;
//	}
//
//	public List<SCHPosition> SCH_LoadPositionWorkerListByShiftID(int wCompanyID, int wShiftID,
//			OutResult<Integer> wErrorCode) {
//
//		List<SCHPosition> wPositionList = new ArrayList<SCHPosition>();
//
//		List<SCHWorker> wShiftWorkerList = new ArrayList<SCHWorker>();
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic);
//			wErrorCode.set(wInstance.ErrorCode);
//			if (wErrorCode.get() == 0) {
//				String wSQLText = StringUtils.Format(
//						"Select t.*,p.WorkShopID,p.LineID from {0}.sch_shiftworker t,{1}.bpm_position p ",
//						wInstance.Result, wInstance.Result) + " where t.PositionID=p.ID and t.ShiftID=:ShiftID";
//
//				Map<String, Object> wParms = new HashMap<String, Object>();
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
//					wShiftWorkerList.add(wWorkerDB);
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryShiftWorkerList: Query DB",
//					"Function Exception:" + ex.toString());
//		}
//		try {
//			if (wErrorCode.get() == 0) {
//				List<SCHWorker> wTemplateList = SCHWorkerDAO.getInstance()
//						.SCH_LoadWorkerListByTemplate(wCompanyID, 0, 0, 0, 0, 0,wErrorCode);
//				
//				if (wErrorCode.get() == 0) {
//					List<SCHPosition> wPositionDBList = SCHPositionDAO.getInstance()
//							.SCH_LoadPositionListByDB(wCompanyID, 0, 0, 0, SCHPositionLevel.Default, wErrorCode);
//
//					for (SCHPosition wPositionDB : wPositionDBList) {
//						if (wPositionDB.Status != BPMStatus.Audited.getValue())
//							continue;
//
//						for (SCHWorker wWorkerDB : wShiftWorkerList) {
//							if (wPositionDB.PositionID == wWorkerDB.PositionID) {
//								wPositionDB.WorkerID = wWorkerDB.WorkerID;
//							}
//						}
//						if (wPositionDB.WorkerID < 1) {
//							for (SCHWorker wWorkerDB : wTemplateList) {
//								if (wPositionDB.PositionID == wWorkerDB.PositionID) {
//									wPositionDB.WorkerID = wWorkerDB.WorkerID;
//								}
//							}
//						}
//						wPositionList.add(wPositionDB);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_LoadPositionWorkerListByShiftID: Set Text",
//					"Function Exception:" + ex.toString());
//			wErrorCode.set(MESException.Exception.getValue());
//		}
//		return wPositionList;
//	}
//
//	public List<SCHPosition> SFC_QueryPositionListByLoginID(int wCompanyID, int wLoginID, int wEventID,
//			boolean wIncludeSub, OutResult<Integer> wErrorCode) {
//
//		List<SCHPosition> wSCHPositionList = new ArrayList<SCHPosition>();
//		try {
//			// Step01:通过工作流获取EventID对应的岗位职能--岗位列表
//			List<BPMPosition> wBPMPositionList = BPMPositionDAO.getInstance().BPM_QueryPositionListByEventID(wCompanyID,
//					wEventID, wErrorCode);
//
//			// Step02:通过wLoginID获取当班值班岗位列表-----有效值班岗位
//			MESEntry wSCHModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.SCHModel);
//
//			for (SCHPosition wSCHPosition : wSCHModel.SCHModel.PositionList) {
//				if (wSCHPosition.WorkerID == wLoginID) {
//					for (BPMPosition wBPMPosition : wBPMPositionList) {
//						if (wBPMPosition.ID == wSCHPosition.PositionID) {
//							wSCHPositionList.add(wSCHPosition);
//						}
//					}
//				}
//			}
//			// Step03:获取下级岗位列表
//			if (wIncludeSub) {
//				List<SCHPosition> wSubPositionList = SCHPositionDAO.getInstance()
//						.SFC_QuerySubPositionListByLoginID(wCompanyID, wLoginID, wEventID, wErrorCode);
//				for (SCHPosition wSubPosition : wSubPositionList) {
//					boolean wIsExistPosition = false;
//					for (SCHPosition wPosition : wSCHPositionList) {
//						if (wPosition.PositionID == wSubPosition.PositionID && wPosition.LineID == wSubPosition.LineID
//								&& wPosition.PartID == wSubPosition.PartID
//								&& wPosition.WorkShopID == wSubPosition.WorkShopID
//								&& wPosition.StationID == wSubPosition.StationID) {
//							wIsExistPosition = true;
//							break;
//						}
//					}
//					if (!wIsExistPosition)
//						wSCHPositionList.add(wSubPosition);
//				}
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SFCService", "SFC_QueryPositionListByLoginID",
//					"Function Exception:" + ex.toString());
//		}
//		return wSCHPositionList;
//	}
//
//	public List<SCHPosition> SFC_QuerySubPositionListByLoginID(int wCompanyID, int wLoginID, int wEventID,
//			OutResult<Integer> wErrorCode) {
//
//		List<SCHPosition> wSubPositionList = new ArrayList<SCHPosition>();
//		try {
//			// Step01:通过工作流获取EventID对应的岗位职能--岗位列表
//			List<BPMPosition> wEventPositionList = BPMPositionDAO.getInstance()
//					.BPM_QueryPositionListByEventID(wCompanyID, wEventID, wErrorCode);
//			// Step02:获取本人的岗位列表
//			MESEntry wSCHModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.SCHModel);
//			List<SCHPosition> wLoginPositionList = new ArrayList<SCHPosition>();
//			for (SCHPosition wItemPosition : wSCHModel.SCHModel.PositionList) {
//				if (wItemPosition.WorkerID == wLoginID) {
//					wLoginPositionList.add(wItemPosition);
//				}
//			}
//			// Step03:获取下级的岗位列表
//			MESEntry wBPMEntry = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BPMModel);
//			List<BPMPosition> wBPMSubPositionList = new ArrayList<BPMPosition>();
//			for (SCHPosition wItemPosition : wLoginPositionList) {
//				List<BPMPosition> wBPMPositionList = BPMPositionDAO.getInstance().BPM_QuerySubPositionListByID(
//						wCompanyID, wItemPosition.PositionID, wBPMEntry.BPMModel.PositionList);
//				if (wBPMPositionList.size() > 0)
//					wBPMSubPositionList.addAll(wBPMPositionList);
//
//			}
//			// Step04:通过岗位上下级关系当班值班岗位列表
//			List<SCHPosition> wSubLoginPositionList = new ArrayList<SCHPosition>();
//			for (SCHPosition wSCHPosition : wSCHModel.SCHModel.PositionList) {
//				for (BPMPosition wBPMPosition : wBPMSubPositionList) {
//					if (wBPMPosition.ID == wSCHPosition.PositionID) {
//						wSubLoginPositionList.add(wSCHPosition);
//					}
//				}
//			}
//			// Step05:通过wLoginID获取当班值班岗位列表-----有效值班岗位
//			for (SCHPosition wSCHPosition : wSubLoginPositionList) {
//				for (BPMPosition wBPMPosition : wEventPositionList) {
//					if (wBPMPosition.ID == wSCHPosition.PositionID) {
//						wSubPositionList.add(wSCHPosition);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SFCService", "SFC_QuerySubPositionListByLoginID",
//					"Function Exception:" + ex.toString());
//		}
//		return wSubPositionList;
//	}
//
//	public List<SCHPosition> SFC_QueryPositionListByStationID(int wCompanyID, int wStationID, int wEventID,
//			OutResult<Integer> wErrorCode) {
//
//		List<SCHPosition> wSCHPositionList = new ArrayList<SCHPosition>();
//		try {
//			// Step01:通过工作流获取EventID对应的岗位职能--岗位列表
//			List<BPMPosition> wBPMPositionList = BPMPositionDAO.getInstance().BPM_QueryPositionListByEventID(wCompanyID,
//					wEventID, wErrorCode);
//
//			// Step02:通过wLoginID获取当班值班岗位列表-----有效值班岗位
//			MESEntry wSCHModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.SCHModel);
//
//			for (SCHPosition wSCHPosition : wSCHModel.SCHModel.PositionList) {
//				for (BPMPosition wBPMPosition : wBPMPositionList) {
//					if (wBPMPosition.ID == wSCHPosition.PositionID && wSCHPosition.StationID == wStationID) {
//						wSCHPositionList.add(wSCHPosition);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SFCService", "SFC_QueryPositionListByStationID",
//					"Function Exception:" + ex.toString());
//		}
//		return wSCHPositionList;
//	}
//
//	public List<SCHPosition> SFC_QueryPositionListByShiftID(int wCompanyID, int wShiftID, int wStationID, int wEventID,
//			OutResult<Integer> wErrorCode) {
//
//		List<SCHPosition> wSCHPositionList = new ArrayList<SCHPosition>();
//		try {
//			// Step01:通过工作流获取EventID对应的岗位职能--岗位列表
//			List<BPMPosition> wBPMPositionList = BPMPositionDAO.getInstance().BPM_QueryPositionListByEventID(wCompanyID,
//					wEventID, wErrorCode);
//
//			// Step02:通过wLoginID获取当班值班岗位列表-----有效值班岗位
//			List<SCHPosition> wShiftPositionList = new ArrayList<SCHPosition>();
//			if (wShiftID == SFCTaskDAO.DayShiftID) {
//				MESEntry wSCHModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.SCHModel);
//				wShiftPositionList.addAll(wSCHModel.SCHModel.PositionList);
//			} else {
//				wShiftPositionList = SCHPositionDAO.getInstance().SCH_LoadPositionWorkerListByShiftID(wCompanyID,
//						wShiftID, wErrorCode);
//			}
//			for (SCHPosition wSCHPosition : wShiftPositionList) {
//				for (BPMPosition wBPMPosition : wBPMPositionList) {
//					if (wBPMPosition.ID == wSCHPosition.PositionID) {
//						if (wStationID > 0) {
//							if (wSCHPosition.StationID == wStationID)
//								wSCHPositionList.add(wSCHPosition);
//						} else {
//							wSCHPositionList.add(wSCHPosition);
//						}
//					}
//				}
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SFCService", "SFC_QueryPositionListByShiftID",
//					"Function Exception:" + ex.toString());
//		}
//		return wSCHPositionList;
//	}
}
