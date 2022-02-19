package com.mes.loco.aps.server.serviceimpl.dao.tcm;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.tcm.TCMMaterialChangeItems;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class TCMMaterialChangeItemsDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(TCMMaterialChangeItemsDAO.class);

	private static TCMMaterialChangeItemsDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wTCMMaterialChangeItems
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, TCMMaterialChangeItems wTCMMaterialChangeItems,
			OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wTCMMaterialChangeItems == null)
				return 0;

			String wSQL = "";
			if (wTCMMaterialChangeItems.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.tcm_materialchangeitems(ChangeLogID,ChangeType,BOMID,MaterialID,MaterialNo,"
								+ "MaterialName,TypeID,MaterialUnit,MaterialUnitRatio,DeviceNo,Author,Auditor,"
								+ "EditTime,AuditTime,LossRatio,Active,PartPointID,PartPointName,ParentID,GradeID,"
								+ "UnitID,PlaceID,BOMType,MaterialNumber,ProductQD,Remark,ReplaceType,ReplaceRatio,"
								+ "OutsourceType,OriginalType,DisassyType,OrderNum,BOMNo1,BOMNo2,RouteNo1,"
								+ "RouteNo2,NewPartID,Methods,Annex,RouteID1,RouteID2,OldMaterialNumber) "
								+ "VALUES(:ChangeLogID,:ChangeType,"
								+ ":BOMID,:MaterialID,:MaterialNo,:MaterialName,:TypeID,:MaterialUnit,:MaterialUnitRatio,"
								+ ":DeviceNo,:Author,:Auditor,:EditTime,:AuditTime,:LossRatio,:Active,:PartPointID,"
								+ ":PartPointName,:ParentID,:GradeID,:UnitID,:PlaceID,:BOMType,:MaterialNumber,:ProductQD,"
								+ ":Remark,:ReplaceType,:ReplaceRatio,:OutsourceType,:OriginalType,:DisassyType,"
								+ ":OrderNum,:BOMNo1,:BOMNo2,:RouteNo1,:RouteNo2,:NewPartID,:Methods,:Annex,"
								+ ":RouteID1,:RouteID2,:OldMaterialNumber);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.tcm_materialchangeitems SET ChangeLogID = :ChangeLogID,ChangeType = :ChangeType,"
								+ "BOMID = :BOMID,MaterialID = :MaterialID,MaterialNo = :MaterialNo,"
								+ "MaterialName = :MaterialName,TypeID = :TypeID,MaterialUnit = :MaterialUnit,"
								+ "MaterialUnitRatio = :MaterialUnitRatio,DeviceNo = :DeviceNo,Author = :Author,"
								+ "Auditor = :Auditor,EditTime = :EditTime,AuditTime = :AuditTime,LossRatio = :LossRatio,"
								+ "Active = :Active,PartPointID = :PartPointID,PartPointName = :PartPointName,"
								+ "ParentID = :ParentID,GradeID = :GradeID,UnitID = :UnitID,PlaceID = :PlaceID,"
								+ "BOMType = :BOMType,MaterialNumber = :MaterialNumber,ProductQD = :ProductQD,"
								+ "Remark = :Remark,ReplaceType = :ReplaceType,ReplaceRatio = :ReplaceRatio,"
								+ "OutsourceType = :OutsourceType,OriginalType = :OriginalType,DisassyType = :DisassyType,"
								+ "OrderNum = :OrderNum,BOMNo1=:BOMNo1,BOMNo2=:BOMNo2,"
								+ "RouteNo1=:RouteNo1,RouteNo2=:RouteNo2,NewPartID=:NewPartID,"
								+ "Methods=:Methods,Annex=:Annex,RouteID1=:RouteID1,"
								+ "RouteID2=:RouteID2,OldMaterialNumber=:OldMaterialNumber WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wTCMMaterialChangeItems.ID);
			wParamMap.put("ChangeLogID", wTCMMaterialChangeItems.ChangeLogID);
			wParamMap.put("ChangeType", wTCMMaterialChangeItems.ChangeType);
			wParamMap.put("BOMID", wTCMMaterialChangeItems.BOMID);
			wParamMap.put("MaterialID", wTCMMaterialChangeItems.MaterialID);
			wParamMap.put("MaterialNo", wTCMMaterialChangeItems.MaterialNo);
			wParamMap.put("MaterialName", wTCMMaterialChangeItems.MaterialName);
			wParamMap.put("TypeID", wTCMMaterialChangeItems.TypeID);
			wParamMap.put("MaterialUnit", wTCMMaterialChangeItems.MaterialUnit);
			wParamMap.put("MaterialUnitRatio", wTCMMaterialChangeItems.MaterialUnitRatio);
			wParamMap.put("DeviceNo", wTCMMaterialChangeItems.DeviceNo);
			wParamMap.put("Author", wTCMMaterialChangeItems.Author);
			wParamMap.put("Auditor", wTCMMaterialChangeItems.Auditor);
			wParamMap.put("EditTime", wTCMMaterialChangeItems.EditTime);
			wParamMap.put("AuditTime", wTCMMaterialChangeItems.AuditTime);
			wParamMap.put("LossRatio", wTCMMaterialChangeItems.LossRatio);
			wParamMap.put("Active", wTCMMaterialChangeItems.Active);
			wParamMap.put("PartPointID", wTCMMaterialChangeItems.PartPointID);
			wParamMap.put("PartPointName", wTCMMaterialChangeItems.PartPointName);
			wParamMap.put("ParentID", wTCMMaterialChangeItems.ParentID);
			wParamMap.put("GradeID", wTCMMaterialChangeItems.GradeID);
			wParamMap.put("UnitID", wTCMMaterialChangeItems.UnitID);
			wParamMap.put("PlaceID", wTCMMaterialChangeItems.PlaceID);
			wParamMap.put("BOMType", wTCMMaterialChangeItems.BOMType);
			wParamMap.put("MaterialNumber", wTCMMaterialChangeItems.MaterialNumber);
			wParamMap.put("ProductQD", wTCMMaterialChangeItems.ProductQD);
			wParamMap.put("Remark", wTCMMaterialChangeItems.Remark);
			wParamMap.put("ReplaceType", wTCMMaterialChangeItems.ReplaceType);
			wParamMap.put("ReplaceRatio", wTCMMaterialChangeItems.ReplaceRatio);
			wParamMap.put("OutsourceType", wTCMMaterialChangeItems.OutsourceType);
			wParamMap.put("OriginalType", wTCMMaterialChangeItems.OriginalType);
			wParamMap.put("DisassyType", wTCMMaterialChangeItems.DisassyType);
			wParamMap.put("OrderNum", wTCMMaterialChangeItems.OrderNum);
			wParamMap.put("BOMNo1", wTCMMaterialChangeItems.BOMNo1);
			wParamMap.put("BOMNo2", wTCMMaterialChangeItems.BOMNo2);
			wParamMap.put("RouteNo1", wTCMMaterialChangeItems.RouteNo1);
			wParamMap.put("RouteNo2", wTCMMaterialChangeItems.RouteNo2);
			wParamMap.put("NewPartID", wTCMMaterialChangeItems.NewPartID);
			wParamMap.put("Methods", wTCMMaterialChangeItems.Methods);
			wParamMap.put("Annex", wTCMMaterialChangeItems.Annex);
			wParamMap.put("RouteID1", wTCMMaterialChangeItems.RouteID1);
			wParamMap.put("RouteID2", wTCMMaterialChangeItems.RouteID2);
			wParamMap.put("OldMaterialNumber", wTCMMaterialChangeItems.OldMaterialNumber);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wTCMMaterialChangeItems.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wTCMMaterialChangeItems.setID(wResult);
			} else {
				wResult = wTCMMaterialChangeItems.getID();
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 批量插入
	 */
	public void InsertList(BMSEmployee wLoginUser, List<TCMMaterialChangeItems> wTCMMaterialChangeItemsList,
			OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			if (wTCMMaterialChangeItemsList == null || wTCMMaterialChangeItemsList.size() <= 0) {
				return;
			}

			String wSQL = "";
			wSQL = MessageFormat.format(
					"INSERT INTO {0}.tcm_materialchangeitems(ChangeLogID,ChangeType,BOMID,MaterialID,MaterialNo,MaterialName,"
							+ "TypeID,MaterialUnit,MaterialUnitRatio,DeviceNo,Author,Auditor,EditTime,AuditTime,"
							+ "LossRatio,Active,PartPointID,PartPointName,ParentID,GradeID,UnitID,PlaceID,"
							+ "BOMType,MaterialNumber,ProductQD,Remark,ReplaceType,ReplaceRatio,OutsourceType,"
							+ "OriginalType,DisassyType,OrderNum,BOMNo1,BOMNo2,RouteNo1,RouteNo2,"
							+ "NewPartID,Methods,Annex,RouteID1,RouteID2,OldMaterialNumber) VALUES {1};",
					wInstance.Result, GetValuesSQL(wTCMMaterialChangeItemsList));

			wSQL = this.DMLChange(wSQL);

			this.ExecuteSqlTransaction(wSQL);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 获取批量插入SQL
	 */
	private String GetValuesSQL(List<TCMMaterialChangeItems> wTCMMaterialChangeItemsList) {
		String wResult = "";
		try {
			if (wTCMMaterialChangeItemsList == null || wTCMMaterialChangeItemsList.size() <= 0) {
				return wResult;
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			List<String> wList = new ArrayList<String>();
			for (TCMMaterialChangeItems wTCMMaterialChangeItems : wTCMMaterialChangeItemsList) {
				String wItem = StringUtils.Format(
						"({0},{1},{2},{3},''{4}'',''{5}'',{6},{7},{8},''{9}'',''{10}'',''{11}'',''{12}'',''{13}'',{14},{15},"
								+ "{16},''{17}'',{18},{19},{20},{21},{22},{23},''{24}'',''{25}'',"
								+ "{26},{27},{28},{29},{30},{31},''{32}'',''{33}'',''{34}'',''{35}'',{36},''{37}'',''{38}'',{39},{40},{41})",
						String.valueOf(wTCMMaterialChangeItems.ChangeLogID),
						String.valueOf(wTCMMaterialChangeItems.ChangeType),
						String.valueOf(wTCMMaterialChangeItems.BOMID),
						String.valueOf(wTCMMaterialChangeItems.MaterialID), wTCMMaterialChangeItems.MaterialNo,
						wTCMMaterialChangeItems.MaterialName, String.valueOf(wTCMMaterialChangeItems.TypeID),
						String.valueOf(wTCMMaterialChangeItems.MaterialUnit),
						String.valueOf(wTCMMaterialChangeItems.MaterialUnitRatio), wTCMMaterialChangeItems.DeviceNo,
						wTCMMaterialChangeItems.Author, wTCMMaterialChangeItems.Auditor,
						wSDF.format(wTCMMaterialChangeItems.EditTime.getTime()),
						wSDF.format(wTCMMaterialChangeItems.AuditTime.getTime()),
						String.valueOf(wTCMMaterialChangeItems.LossRatio),
						String.valueOf(wTCMMaterialChangeItems.Active),
						String.valueOf(wTCMMaterialChangeItems.PartPointID), wTCMMaterialChangeItems.PartPointName,
						String.valueOf(wTCMMaterialChangeItems.ParentID),
						String.valueOf(wTCMMaterialChangeItems.GradeID), String.valueOf(wTCMMaterialChangeItems.UnitID),
						String.valueOf(wTCMMaterialChangeItems.PlaceID),
						String.valueOf(wTCMMaterialChangeItems.BOMType),
						String.valueOf(wTCMMaterialChangeItems.MaterialNumber), wTCMMaterialChangeItems.ProductQD,
						wTCMMaterialChangeItems.Remark, String.valueOf(wTCMMaterialChangeItems.ReplaceType),
						String.valueOf(wTCMMaterialChangeItems.ReplaceRatio),
						String.valueOf(wTCMMaterialChangeItems.OutsourceType),
						String.valueOf(wTCMMaterialChangeItems.OriginalType),
						String.valueOf(wTCMMaterialChangeItems.DisassyType),
						String.valueOf(wTCMMaterialChangeItems.OrderNum), wTCMMaterialChangeItems.BOMNo1,
						wTCMMaterialChangeItems.BOMNo2, wTCMMaterialChangeItems.RouteNo1,
						wTCMMaterialChangeItems.RouteNo2, wTCMMaterialChangeItems.NewPartID,
						wTCMMaterialChangeItems.Methods, wTCMMaterialChangeItems.Annex,
						wTCMMaterialChangeItems.RouteID1, wTCMMaterialChangeItems.RouteID2,
						wTCMMaterialChangeItems.OldMaterialNumber);
				wList.add(wItem);
			}
			wResult = StringUtils.Join(",", wList);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 删除集合
	 * 
	 * @param wList
	 */
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<TCMMaterialChangeItems> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (TCMMaterialChangeItems wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.tcm_materialchangeitems WHERE ID IN({0}) ;",
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
	 */
	public TCMMaterialChangeItems SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		TCMMaterialChangeItems wResult = new TCMMaterialChangeItems();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<TCMMaterialChangeItems> wList = SelectList(wLoginUser, wID, -1, -1, wErrorCode);
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
	 */
	public List<TCMMaterialChangeItems> SelectList(BMSEmployee wLoginUser, int wID, int wChangeLogID, int wChangeType,
			OutResult<Integer> wErrorCode) {
		List<TCMMaterialChangeItems> wResultList = new ArrayList<TCMMaterialChangeItems>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.tcm_materialchangeitems WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) and ( :wChangeLogID <= 0 or :wChangeLogID = ChangeLogID ) "
					+ "and ( :wChangeType <= 0 or :wChangeType = ChangeType );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wChangeLogID", wChangeLogID);
			wParamMap.put("wChangeType", wChangeType);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				TCMMaterialChangeItems wItem = new TCMMaterialChangeItems();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ChangeLogID = StringUtils.parseInt(wReader.get("ChangeLogID"));
				wItem.ChangeType = StringUtils.parseInt(wReader.get("ChangeType"));
				wItem.BOMID = StringUtils.parseInt(wReader.get("BOMID"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.MaterialName = StringUtils.parseString(wReader.get("MaterialName"));
				wItem.TypeID = StringUtils.parseInt(wReader.get("TypeID"));
				wItem.MaterialUnit = StringUtils.parseFloat(wReader.get("MaterialUnit"));
				wItem.MaterialUnitRatio = StringUtils.parseFloat(wReader.get("MaterialUnitRatio"));
				wItem.DeviceNo = StringUtils.parseString(wReader.get("DeviceNo"));
				wItem.Author = StringUtils.parseString(wReader.get("Author"));
				wItem.Auditor = StringUtils.parseString(wReader.get("Auditor"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.LossRatio = StringUtils.parseFloat(wReader.get("LossRatio"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.PartPointID = StringUtils.parseInt(wReader.get("PartPointID"));
				wItem.PartPointName = StringUtils.parseString(wReader.get("PartPointName"));
				wItem.ParentID = StringUtils.parseInt(wReader.get("ParentID"));
				wItem.GradeID = StringUtils.parseInt(wReader.get("GradeID"));
				wItem.UnitID = StringUtils.parseInt(wReader.get("UnitID"));
				wItem.PlaceID = StringUtils.parseInt(wReader.get("PlaceID"));
				wItem.BOMType = StringUtils.parseInt(wReader.get("BOMType"));
				wItem.MaterialNumber = StringUtils.parseDouble(wReader.get("MaterialNumber"));
				wItem.ProductQD = StringUtils.parseString(wReader.get("ProductQD"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.ReplaceType = StringUtils.parseInt(wReader.get("ReplaceType"));
				wItem.ReplaceRatio = StringUtils.parseFloat(wReader.get("ReplaceRatio"));
				wItem.OutsourceType = StringUtils.parseInt(wReader.get("OutsourceType"));
				wItem.OriginalType = StringUtils.parseInt(wReader.get("OriginalType"));
				wItem.DisassyType = StringUtils.parseInt(wReader.get("DisassyType"));
				wItem.OrderNum = StringUtils.parseInt(wReader.get("OrderNum"));
				wItem.BOMNo1 = StringUtils.parseString(wReader.get("BOMNo1"));
				wItem.BOMNo2 = StringUtils.parseString(wReader.get("BOMNo2"));
				wItem.RouteNo1 = StringUtils.parseString(wReader.get("RouteNo1"));
				wItem.RouteNo2 = StringUtils.parseString(wReader.get("RouteNo2"));
				wItem.NewPartID = StringUtils.parseInt(wReader.get("NewPartID"));
				wItem.Methods = StringUtils.parseString(wReader.get("Methods"));
				wItem.Annex = StringUtils.parseString(wReader.get("Annex"));
				wItem.RouteID1 = StringUtils.parseInt(wReader.get("RouteID1"));
				wItem.RouteID2 = StringUtils.parseInt(wReader.get("RouteID2"));
				wItem.OldMaterialNumber = StringUtils.parseDouble(wReader.get("OldMaterialNumber"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 查询条数
	 */
	public int SelectCount(BMSEmployee wLoginUser, int wChangeLogID, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = MessageFormat.format(
					"SELECT count(*) as Number FROM {0}.tcm_materialchangeitems WHERE  :wChangeLogID = ChangeLogID ;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wChangeLogID", wChangeLogID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wNumber = StringUtils.parseInt(wReader.get("Number"));
				wResult = wNumber;
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 批量激活或禁用
	 */
	public ServiceResult<Integer> Active(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wIDList == null || wIDList.size() <= 0)
				return wResult;
			if (wActive != 0 && wActive != 1)
				return wResult;
			for (Integer wItem : wIDList) {
				TCMMaterialChangeItems wTCMMaterialChangeItems = SelectByID(wLoginUser, wItem, wErrorCode);
				if (wTCMMaterialChangeItems == null || wTCMMaterialChangeItems.ID <= 0)
					continue;
				wTCMMaterialChangeItems.Active = wActive;
				long wID = Update(wLoginUser, wTCMMaterialChangeItems, wErrorCode);
				if (wID <= 0)
					break;
			}
		} catch (Exception e) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(e.toString());
		}
		return wResult;
	}

	private TCMMaterialChangeItemsDAO() {
		super();
	}

	public static TCMMaterialChangeItemsDAO getInstance() {
		if (Instance == null)
			Instance = new TCMMaterialChangeItemsDAO();
		return Instance;
	}

	public List<String> GetPartNoList(BMSEmployee wLoginUser, List<Integer> wOrderList, OutResult<Integer> wErrorCode) {
		List<String> wResult = new ArrayList<String>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOrderList == null || wOrderList.size() <= 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT PartNo FROM {0}.oms_order where ID in ({1});", wInstance.Result,
					StringUtils.Join(",", wOrderList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.add(StringUtils.parseString(wReader.get("PartNo")));
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	public boolean JudgeIsStepFinish(BMSEmployee wLoginUser, Integer wOrderID, int wPartID, Integer wStepID,
			OutResult<Integer> wErrorCode) {
		boolean wResult = false;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format("SELECT Status FROM {0}.aps_taskstep where "
					+ "OrderID=:wOrderID and PartID=:wPartID and StepID=:wStepID;", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wStepID", wStepID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				if (StringUtils.parseInt(wReader.get("Status")) == 5) {
					return true;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
