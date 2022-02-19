package com.mes.loco.aps.server.serviceimpl.dao.sch;

import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class SCHTemplateDAO extends BaseDAO {
	private static SCHTemplateDAO Instance = null;

	private SCHTemplateDAO() {
		super();
	}

	public static SCHTemplateDAO getInstance() {
		if (Instance == null)
			Instance = new SCHTemplateDAO();
		return Instance;
	}

	// 岗位模板配置
//	private SCHTemplate SCH_CheckTemplateName(int wCompanyID, int wLoginID, SCHTemplate wTemplate,OutResult<Integer> wErrorCode) {
//		SCHTemplate wTemplateDB = new SCHTemplate();
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
//				if (wTemplate.ID > 0) {
//					wSQLText = StringUtils.Format("Select * from {0}.sch_template ", wInstance.Result)
//							+ " where ID!=:ID and WorkShopID=:WorkShopID and LineID=:LineID and ModuleID=:ModuleID and Name=:Name";
//					wParms.clear();
//					wParms.put("ID", wTemplate.ID);
//					wParms.put("WorkShopID", wTemplate.WorkShopID);
//					wParms.put("LineID", wTemplate.LineID);
//					wParms.put("ModuleID", wTemplate.ModuleID);
//					wParms.put("Name", wTemplate.Name);
//				} else {
//					wSQLText = StringUtils.Format("Select * from {0}.sch_template ", wInstance.Result)
//							+ " where WorkShopID=:WorkShopID and LineID=:LineID and ModuleID=:ModuleID and Name=:Name";
//					wParms.clear();
//					wParms.put("WorkShopID", wTemplate.WorkShopID);
//					wParms.put("LineID", wTemplate.LineID);
//					wParms.put("ModuleID", wTemplate.ModuleID);
//					wParms.put("Name", wTemplate.Name);
//				}
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					wTemplateDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wTemplateDB.Name = StringUtils.parseString(wSqlDataReader.get("Name"));
//					wTemplateDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//					wTemplateDB.EditorID = StringUtils.parseInt(wSqlDataReader.get("EditorID"));
//					wTemplateDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//					wTemplateDB.EditTime = StringUtils.parseCalendar(wSqlDataReader.get("EditTime"));
//					wTemplateDB.Active = StringUtils.parseBoolean(wSqlDataReader.get("Active"));
//					wTemplateDB.ModuleID = StringUtils.parseInt(wSqlDataReader.get("ModuleID"));
//					wTemplateDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//					wTemplateDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//				}
//
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_CheckTemplateName", "Function Exception:" + ex.toString());
//		}
//		return wTemplateDB;
//	}
//
//	public int SCH_AddTemplate(int wCompanyID, int wLoginID, SCHTemplate wTemplate,OutResult<Integer> wErrorCode) {
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
//				SCHTemplate wTemplateDB = this.SCH_CheckTemplateName(wCompanyID, wLoginID, wTemplate,wErrorCode);
//				if (wTemplateDB.ID > 0)
//					wErrorCode.set(MESException.Logic.getValue());
//			}
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("Insert Into {0}.sch_template", wInstance.Result)
//						+ "(Name,WorkShopID,LineID,ModuleID,CreatorID,EditorID,CreateTime,EditTime,Active) "
//						+ " Values(:Name,:WorkShopID,:LineID,:ModuleID,:CreatorID,:EditorID,:CreateTime,:EditTime,:Active);";
//				wParms.clear();
//
//				wParms.put("Name", wTemplate.Name);
//				wParms.put("WorkShopID", wTemplate.WorkShopID);
//				wParms.put("LineID", wTemplate.LineID);
//				wParms.put("ModuleID", wTemplate.ModuleID);
//
//				wParms.put("CreatorID", wLoginID);
//				wParms.put("EditorID", 0);
//
//				wParms.put("CreateTime", Calendar.getInstance());
//				wParms.put("EditTime", Calendar.getInstance());
//				wParms.put("Active", 1);
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
//			LoggerTool.SaveException("SCHService", "SCH_AddTemplate", "Function Exception:" + ex.toString());
//		}
//		return wID;
//	}
//
//	public int SCH_SaveTemplate(int wCompanyID, int wLoginID, SCHTemplate wTemplate,OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
//
//		try {
//			ServiceResult<String> wInstance = this.GetDataBaseName(wCompanyID, MESDBSource.Basic, wLoginID,
//					110001);
//
//			wErrorCode.set(wInstance.ErrorCode);
//
//			if (wErrorCode.Result == 0) {
//				SCHTemplate wTemplateDB = this.SCH_CheckTemplateName(wCompanyID, wLoginID, wTemplate,wErrorCode);
//				if (wTemplateDB.ID > 0)
//					wErrorCode.set(MESException.Logic.getValue());
//			}
//			if (wErrorCode.Result == 0) {
//				Map<String, Object> wParms = new HashMap<String, Object>();
//				String wSQLText = "";
//
//				wSQLText = StringUtils.Format("update {0}.sch_template", wInstance.Result)
//						+ " set Name=:Name,WorkShopID=:WorkShopID,LineID=:LineID,ModuleID=:ModuleID,"
//						+ " EditorID=:EditorID,EditTime=:EditTime where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wTemplate.ID);
//				wParms.put("Name", wTemplate.Name);
//				wParms.put("WorkShopID", wTemplate.WorkShopID);
//				wParms.put("LineID", wTemplate.LineID);
//				wParms.put("ModuleID", wTemplate.ModuleID);
//				wParms.put("EditorID", wLoginID);
//				wParms.put("EditTime", Calendar.getInstance());
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//				SCHPositionDAO.getInstance().SCH_LoadConfiguration(wCompanyID,wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_SaveTemplate", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int SCH_DisableTemplate(int wCompanyID, int wLoginID, SCHTemplate wTemplate,OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
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
//
//				wSQLText = StringUtils.Format("update {0}.sch_template", wInstance.Result)
//						+ " set EditorID=:EditorID," + " EditTime=:EditTime,Active=0 where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wTemplate.ID);
//				wParms.put("EditorID", wLoginID);
//				wParms.put("EditTime", Calendar.getInstance());
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_DisableTemplate", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public int SCH_ActiveTemplate(int wCompanyID, int wLoginID, SCHTemplate wTemplate,OutResult<Integer> wErrorCode) {
//		wErrorCode.set(0);
//		// 判断客户信息是否存在(中国：统一社会信用代码，国外:提醒是否有重复）
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
//
//				wSQLText = StringUtils.Format("update {0}.sch_template", wInstance.Result)
//						+ " set EditorID=:EditorID," + " EditTime=:EditTime,Active=1 where ID=:ID ";
//				wParms.clear();
//
//				wParms.put("ID", wTemplate.ID);
//
//				wParms.put("EditorID", wLoginID);
//				wParms.put("EditTime", Calendar.getInstance());
//				wSQLText = this.DMLChange(wSQLText);
//				nameJdbcTemplate.update(wSQLText, wParms);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_ActiveTemplate", "Function Exception:" + ex.toString());
//		}
//		return wErrorCode.Result;
//	}
//
//	public SCHTemplate SCH_QueryTemplateByID(int wCompanyID, int wLoginID, int wID,OutResult<Integer> wErrorCode) {
//		SCHTemplate wTemplateDB = new SCHTemplate();
//		wErrorCode.set(0);
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
//					wSQLText = StringUtils.Format("Select t.* from {0}.sch_template t ", wInstance.Result)
//							+ " where t.ID=:ID";
//					wParms.clear();
//					wParms.put("ID", wID);
//					wSQLText = this.DMLChange(wSQLText);
//					List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//					for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//						wTemplateDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//						wTemplateDB.Name = StringUtils.parseString(wSqlDataReader.get("Name"));
//						wTemplateDB.ModuleID = StringUtils.parseInt(wSqlDataReader.get("ModuleID"));
//						wTemplateDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//						wTemplateDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//						wTemplateDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//						wTemplateDB.EditorID = StringUtils.parseInt(wSqlDataReader.get("EditorID"));
//						wTemplateDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//						wTemplateDB.EditTime = StringUtils.parseCalendar(wSqlDataReader.get("EditTime"));
//						wTemplateDB.Active = StringUtils.parseBoolean(wSqlDataReader.get("Active"));
//					}
//
//					if (wTemplateDB.ID > 0) {
//						wTemplateDB.Creator = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wTemplateDB.CreatorID);
//						wTemplateDB.Editor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//								wTemplateDB.EditorID);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryTemplateByID", "Function Exception:" + ex.toString());
//		}
//		return wTemplateDB;
//	}
//
//	public List<SCHTemplate> SCH_QueryTemplateList(int wCompanyID, int wLoginID, int wLineID, int wWorkShopID,
//			int wModuleID,OutResult<Integer> wErrorCode) {
//		List<SCHTemplate> wTemplateList = new ArrayList<SCHTemplate>();
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
//					wCondition = StringUtils.Format(" where t.LineID={0}", wLineID);
//
//				if (wWorkShopID > 0) {
//					if (wCondition.length() < 1)
//						wCondition = StringUtils.Format(" where t.WorkShopID={0}", wWorkShopID);
//					else
//						wCondition = StringUtils.Format(" {0} and t.WorkShopID={1}", wCondition, wWorkShopID);
//				}
//				wSQLText = StringUtils.Format("Select t.*  from {0}.sch_template t ", wInstance.Result) + wCondition;
//				wParms.clear();
//				wSQLText = this.DMLChange(wSQLText);
//				List<Map<String, Object>> wQueryResultList = nameJdbcTemplate.queryForList(wSQLText, wParms);
//				for (Map<String, Object> wSqlDataReader : wQueryResultList) {
//					SCHTemplate wStationDB = new SCHTemplate();
//
//					wStationDB.ID = StringUtils.parseInt(wSqlDataReader.get("ID"));
//					wStationDB.Name = StringUtils.parseString(wSqlDataReader.get("Name"));
//					wStationDB.LineID = StringUtils.parseInt(wSqlDataReader.get("LineID"));
//					wStationDB.WorkShopID = StringUtils.parseInt(wSqlDataReader.get("WorkShopID"));
//					wStationDB.ModuleID = StringUtils.parseInt(wSqlDataReader.get("ModuleID"));
//					wStationDB.CreatorID = StringUtils.parseInt(wSqlDataReader.get("CreatorID"));
//					wStationDB.EditorID = StringUtils.parseInt(wSqlDataReader.get("EditorID"));
//					wStationDB.CreateTime = StringUtils.parseCalendar(wSqlDataReader.get("CreateTime"));
//					wStationDB.EditTime = StringUtils.parseCalendar(wSqlDataReader.get("EditTime"));
//					wStationDB.Active = StringUtils.parseBoolean(wSqlDataReader.get("Active"));
//
//					wTemplateList.add(wStationDB);
//				}
//				wTemplateList = this.SCH_SetTemplateTextList(wCompanyID, wTemplateList,wErrorCode);
//			}
//		} catch (Exception ex) {
//			wErrorCode.set(MESException.DBSQL.getValue());
//			LoggerTool.SaveException("SCHService", "SCH_QueryTemplateList: Query DB",
//					"Function Exception:" + ex.toString());
//		}
//		return wTemplateList;
//	}
//
//	private List<SCHTemplate> SCH_SetTemplateTextList(int wCompanyID, List<SCHTemplate> wTemplateList,OutResult<Integer> wErrorCode) {
//		List<SCHTemplate> wTemplateTextList = new ArrayList<SCHTemplate>();
//		try {
//			// Step01：人员姓名
//			MESEntry wEntryEmployee = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.BMSModel);
//			// Step02：工厂与事业部
//			MESEntry wFactoryModel = MESServer.MES_QueryEntryByMemory(wCompanyID, MESEntryEnum.FactoryModel);
//			for (SCHTemplate wTemplateDB : wTemplateList) {
//				wTemplateDB.Creator = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wTemplateDB.CreatorID, wEntryEmployee);
//				wTemplateDB.Editor = BMSEmployeeDAO.getInstance().BMS_QueryEmployeeNameByID(wCompanyID,
//						wTemplateDB.EditorID, wEntryEmployee);
//
//				FMCWorkShop wWorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopByID(wCompanyID,
//						wTemplateDB.WorkShopID, wFactoryModel);
//				if (wWorkShop.ID > 0) {
//					wTemplateDB.Factory = FMCFactoryDAO.getInstance().FMC_QueryFactoryNameByID(wCompanyID,
//							wWorkShop.FactoryID, wFactoryModel);
//					wTemplateDB.BusinessUnit = FMCFactoryDAO.getInstance().FMC_QueryBusinessUnitNameByID(wCompanyID,
//							wWorkShop.BusinessUnitID, wFactoryModel);
//				}
//				wTemplateDB.WorkShop = FMCFactoryDAO.getInstance().FMC_QueryWorkShopNameByID(wCompanyID,
//						wTemplateDB.WorkShopID, wFactoryModel);
//				wTemplateDB.Line = FMCLineDAO.getInstance().FMC_QueryLineNameByID(wCompanyID, wTemplateDB.LineID,
//						wFactoryModel);
//				wTemplateDB.ModuleName = BPMFunctionModule.getEnumType(wTemplateDB.ModuleID).getLable();
//				wTemplateTextList.add(wTemplateDB);
//			}
//		} catch (Exception ex) {
//			LoggerTool.SaveException("SCHService", "SCH_SetTemplateTextList", "Function Exception:" + ex.toString());
//		}
//		return wTemplateTextList;
//	}
}
