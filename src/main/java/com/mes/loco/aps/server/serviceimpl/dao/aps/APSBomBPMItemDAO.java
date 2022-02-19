package com.mes.loco.aps.server.serviceimpl.dao.aps;

import java.text.MessageFormat;
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
import com.mes.loco.aps.server.service.po.aps.APSBomBPMItem;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

public class APSBomBPMItemDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(APSBomBPMItemDAO.class);

	private static APSBomBPMItemDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAPSBomBPMItem
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, APSBomBPMItem wAPSBomBPMItem, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAPSBomBPMItem == null)
				return 0;

			String wSQL = "";
			if (wAPSBomBPMItem.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.aps_bombpmitem(APSBomBPMID,APSBomItemID,BOMType,FactoryID,WBSNo,OrderID,PartNo,LineID,ProductID,CustomerID,PartID,PartPointID,MaterialID,MaterialNo,Number,UnitID,ReplaceType,OutsourceType,OriginalType,DisassyType,OverLine,PartChange,ReceiveDepart,StockID,QTType,QTItemType,CustomerMaterial,AuditorID,AuditTime,EditorID,EditTime,Status,RelaDemandNo,TextCode,WorkCenter,DeleteID,SubRelaDemandNo,AssessmentType,AccessoryLogo,RepairPartClass,Remark,DingrongGroup,RepairCoreIdentification,PickingQuantity,EvenExchangeRate,Client,OrderNum,SourceType,SourceID,DifferenceItem,OverQuota,SAPStatus,SAPMsg,NewNumber,NewReplaceType,NewOutsourceType,NewPartChange,NewQTType,NewQTItemType,NewAssessmentType,NewRemark,Type) VALUES(:APSBomBPMID,:APSBomItemID,:BOMType,:FactoryID,:WBSNo,:OrderID,:PartNo,:LineID,:ProductID,:CustomerID,:PartID,:PartPointID,:MaterialID,:MaterialNo,:Number,:UnitID,:ReplaceType,:OutsourceType,:OriginalType,:DisassyType,:OverLine,:PartChange,:ReceiveDepart,:StockID,:QTType,:QTItemType,:CustomerMaterial,:AuditorID,:AuditTime,:EditorID,:EditTime,:Status,:RelaDemandNo,:TextCode,:WorkCenter,:DeleteID,:SubRelaDemandNo,:AssessmentType,:AccessoryLogo,:RepairPartClass,:Remark,:DingrongGroup,:RepairCoreIdentification,:PickingQuantity,:EvenExchangeRate,:Client,:OrderNum,:SourceType,:SourceID,:DifferenceItem,:OverQuota,:SAPStatus,:SAPMsg,:NewNumber,:NewReplaceType,:NewOutsourceType,:NewPartChange,:NewQTType,:NewQTItemType,:NewAssessmentType,:NewRemark,:Type);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.aps_bombpmitem SET APSBomBPMID = :APSBomBPMID,APSBomItemID = :APSBomItemID,BOMType = :BOMType,FactoryID = :FactoryID,WBSNo = :WBSNo,OrderID = :OrderID,PartNo = :PartNo,LineID = :LineID,ProductID = :ProductID,CustomerID = :CustomerID,PartID = :PartID,PartPointID = :PartPointID,MaterialID = :MaterialID,MaterialNo = :MaterialNo,Number = :Number,UnitID = :UnitID,ReplaceType = :ReplaceType,OutsourceType = :OutsourceType,OriginalType = :OriginalType,DisassyType = :DisassyType,OverLine = :OverLine,PartChange = :PartChange,ReceiveDepart = :ReceiveDepart,StockID = :StockID,QTType = :QTType,QTItemType = :QTItemType,CustomerMaterial = :CustomerMaterial,AuditorID = :AuditorID,AuditTime = :AuditTime,EditorID = :EditorID,EditTime = :EditTime,Status = :Status,RelaDemandNo = :RelaDemandNo,TextCode = :TextCode,WorkCenter = :WorkCenter,DeleteID = :DeleteID,SubRelaDemandNo = :SubRelaDemandNo,AssessmentType = :AssessmentType,AccessoryLogo = :AccessoryLogo,RepairPartClass = :RepairPartClass,Remark = :Remark,DingrongGroup = :DingrongGroup,RepairCoreIdentification = :RepairCoreIdentification,PickingQuantity = :PickingQuantity,EvenExchangeRate = :EvenExchangeRate,Client = :Client,OrderNum = :OrderNum,SourceType = :SourceType,SourceID = :SourceID,DifferenceItem = :DifferenceItem,OverQuota = :OverQuota,SAPStatus = :SAPStatus,SAPMsg = :SAPMsg,NewNumber = :NewNumber,NewReplaceType = :NewReplaceType,NewOutsourceType = :NewOutsourceType,NewPartChange = :NewPartChange,NewQTType = :NewQTType,NewQTItemType = :NewQTItemType,NewAssessmentType = :NewAssessmentType,NewRemark = :NewRemark,Type=:Type WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAPSBomBPMItem.ID);
			wParamMap.put("APSBomBPMID", wAPSBomBPMItem.APSBomBPMID);
			wParamMap.put("APSBomItemID", wAPSBomBPMItem.APSBomItemID);
			wParamMap.put("BOMType", wAPSBomBPMItem.BOMType);
			wParamMap.put("FactoryID", wAPSBomBPMItem.FactoryID);
			wParamMap.put("WBSNo", wAPSBomBPMItem.WBSNo);
			wParamMap.put("OrderID", wAPSBomBPMItem.OrderID);
			wParamMap.put("PartNo", wAPSBomBPMItem.PartNo);
			wParamMap.put("LineID", wAPSBomBPMItem.LineID);
			wParamMap.put("ProductID", wAPSBomBPMItem.ProductID);
			wParamMap.put("CustomerID", wAPSBomBPMItem.CustomerID);
			wParamMap.put("PartID", wAPSBomBPMItem.PartID);
			wParamMap.put("PartPointID", wAPSBomBPMItem.PartPointID);
			wParamMap.put("MaterialID", wAPSBomBPMItem.MaterialID);
			wParamMap.put("MaterialNo", wAPSBomBPMItem.MaterialNo);
			wParamMap.put("Number", wAPSBomBPMItem.Number);
			wParamMap.put("UnitID", wAPSBomBPMItem.UnitID);
			wParamMap.put("ReplaceType", wAPSBomBPMItem.ReplaceType);
			wParamMap.put("OutsourceType", wAPSBomBPMItem.OutsourceType);
			wParamMap.put("OriginalType", wAPSBomBPMItem.OriginalType);
			wParamMap.put("DisassyType", wAPSBomBPMItem.DisassyType);
			wParamMap.put("OverLine", wAPSBomBPMItem.OverLine);
			wParamMap.put("PartChange", wAPSBomBPMItem.PartChange);
			wParamMap.put("ReceiveDepart", wAPSBomBPMItem.ReceiveDepart);
			wParamMap.put("StockID", wAPSBomBPMItem.StockID);
			wParamMap.put("QTType", wAPSBomBPMItem.QTType);
			wParamMap.put("QTItemType", wAPSBomBPMItem.QTItemType);
			wParamMap.put("CustomerMaterial", wAPSBomBPMItem.CustomerMaterial);
			wParamMap.put("AuditorID", wAPSBomBPMItem.AuditorID);
			wParamMap.put("AuditTime", wAPSBomBPMItem.AuditTime);
			wParamMap.put("EditorID", wAPSBomBPMItem.EditorID);
			wParamMap.put("EditTime", wAPSBomBPMItem.EditTime);
			wParamMap.put("Status", wAPSBomBPMItem.Status);
			wParamMap.put("RelaDemandNo", wAPSBomBPMItem.RelaDemandNo);
			wParamMap.put("TextCode", wAPSBomBPMItem.TextCode);
			wParamMap.put("WorkCenter", wAPSBomBPMItem.WorkCenter);
			wParamMap.put("DeleteID", wAPSBomBPMItem.DeleteID);
			wParamMap.put("SubRelaDemandNo", wAPSBomBPMItem.SubRelaDemandNo);
			wParamMap.put("AssessmentType", wAPSBomBPMItem.AssessmentType);
			wParamMap.put("AccessoryLogo", wAPSBomBPMItem.AccessoryLogo);
			wParamMap.put("RepairPartClass", wAPSBomBPMItem.RepairPartClass);
			wParamMap.put("Remark", wAPSBomBPMItem.Remark);
			wParamMap.put("DingrongGroup", wAPSBomBPMItem.DingrongGroup);
			wParamMap.put("RepairCoreIdentification", wAPSBomBPMItem.RepairCoreIdentification);
			wParamMap.put("PickingQuantity", wAPSBomBPMItem.PickingQuantity);
			wParamMap.put("EvenExchangeRate", wAPSBomBPMItem.EvenExchangeRate);
			wParamMap.put("Client", wAPSBomBPMItem.Client);
			wParamMap.put("OrderNum", wAPSBomBPMItem.OrderNum);
			wParamMap.put("SourceType", wAPSBomBPMItem.SourceType);
			wParamMap.put("SourceID", wAPSBomBPMItem.SourceID);
			wParamMap.put("DifferenceItem", wAPSBomBPMItem.DifferenceItem);
			wParamMap.put("OverQuota", wAPSBomBPMItem.OverQuota);
			wParamMap.put("SAPStatus", wAPSBomBPMItem.SAPStatus);
			wParamMap.put("SAPMsg", wAPSBomBPMItem.SAPMsg);
			wParamMap.put("NewNumber", wAPSBomBPMItem.NewNumber);
			wParamMap.put("NewReplaceType", wAPSBomBPMItem.NewReplaceType);
			wParamMap.put("NewOutsourceType", wAPSBomBPMItem.NewOutsourceType);
			wParamMap.put("NewPartChange", wAPSBomBPMItem.NewPartChange);
			wParamMap.put("NewQTType", wAPSBomBPMItem.NewQTType);
			wParamMap.put("NewQTItemType", wAPSBomBPMItem.NewQTItemType);
			wParamMap.put("NewAssessmentType", wAPSBomBPMItem.NewAssessmentType);
			wParamMap.put("NewRemark", wAPSBomBPMItem.NewRemark);
			wParamMap.put("Type", wAPSBomBPMItem.Type);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAPSBomBPMItem.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAPSBomBPMItem.setID(wResult);
			} else {
				wResult = wAPSBomBPMItem.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<APSBomBPMItem> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (APSBomBPMItem wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.aps_bombpmitem WHERE ID IN({0}) ;",
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
	public APSBomBPMItem SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		APSBomBPMItem wResult = new APSBomBPMItem();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<APSBomBPMItem> wList = SelectList(wLoginUser, wID, -1, -1, wErrorCode);
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
	public List<APSBomBPMItem> SelectList(BMSEmployee wLoginUser, int wID, int wAPSBomBPMID, int wAPSBomItemID,
			OutResult<Integer> wErrorCode) {
		List<APSBomBPMItem> wResultList = new ArrayList<APSBomBPMItem>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format(
					"SELECT t1.*,t2.MaterialName FROM {0}.aps_bombpmitem t1,{0}.mss_material t2 WHERE t1.MaterialID=t2.ID and  1=1  "
							+ "and ( :wID <= 0 or :wID = t1.ID ) "
							+ "and ( :wAPSBomBPMID <= 0 or :wAPSBomBPMID = t1.APSBomBPMID ) "
							+ "and ( :wAPSBomItemID <= 0 or :wAPSBomItemID = t1.APSBomItemID );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wAPSBomBPMID", wAPSBomBPMID);
			wParamMap.put("wAPSBomItemID", wAPSBomItemID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				APSBomBPMItem wItem = new APSBomBPMItem();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.APSBomBPMID = StringUtils.parseInt(wReader.get("APSBomBPMID"));
				wItem.APSBomItemID = StringUtils.parseInt(wReader.get("APSBomItemID"));
				wItem.BOMType = StringUtils.parseInt(wReader.get("BOMType"));
				wItem.FactoryID = StringUtils.parseInt(wReader.get("FactoryID"));
				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartPointID = StringUtils.parseInt(wReader.get("PartPointID"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.MaterialNo = StringUtils.parseString(wReader.get("MaterialNo"));
				wItem.Number = StringUtils.parseDouble(wReader.get("Number"));
				wItem.UnitID = StringUtils.parseInt(wReader.get("UnitID"));
				wItem.ReplaceType = StringUtils.parseInt(wReader.get("ReplaceType"));
				wItem.OutsourceType = StringUtils.parseInt(wReader.get("OutsourceType"));
				wItem.OriginalType = StringUtils.parseInt(wReader.get("OriginalType"));
				wItem.DisassyType = StringUtils.parseInt(wReader.get("DisassyType"));
				wItem.OverLine = StringUtils.parseInt(wReader.get("OverLine"));
				wItem.PartChange = StringUtils.parseInt(wReader.get("PartChange"));
				wItem.ReceiveDepart = StringUtils.parseString(wReader.get("ReceiveDepart"));
				wItem.StockID = StringUtils.parseInt(wReader.get("StockID"));
				wItem.QTType = StringUtils.parseInt(wReader.get("QTType"));
				wItem.QTItemType = StringUtils.parseInt(wReader.get("QTItemType"));
				wItem.CustomerMaterial = StringUtils.parseInt(wReader.get("CustomerMaterial"));
				wItem.AuditorID = StringUtils.parseInt(wReader.get("AuditorID"));
				wItem.AuditTime = StringUtils.parseCalendar(wReader.get("AuditTime"));
				wItem.EditorID = StringUtils.parseInt(wReader.get("EditorID"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.RelaDemandNo = StringUtils.parseString(wReader.get("RelaDemandNo"));
				wItem.TextCode = StringUtils.parseString(wReader.get("TextCode"));
				wItem.WorkCenter = StringUtils.parseString(wReader.get("WorkCenter"));
				wItem.DeleteID = StringUtils.parseString(wReader.get("DeleteID"));
				wItem.SubRelaDemandNo = StringUtils.parseString(wReader.get("SubRelaDemandNo"));
				wItem.AssessmentType = StringUtils.parseString(wReader.get("AssessmentType"));
				wItem.AccessoryLogo = StringUtils.parseString(wReader.get("AccessoryLogo"));
				wItem.RepairPartClass = StringUtils.parseString(wReader.get("RepairPartClass"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.DingrongGroup = StringUtils.parseString(wReader.get("DingrongGroup"));
				wItem.RepairCoreIdentification = StringUtils.parseString(wReader.get("RepairCoreIdentification"));
				wItem.PickingQuantity = StringUtils.parseInt(wReader.get("PickingQuantity"));
				wItem.EvenExchangeRate = StringUtils.parseDouble(wReader.get("EvenExchangeRate"));
				wItem.Client = StringUtils.parseString(wReader.get("Client"));
				wItem.OrderNum = StringUtils.parseInt(wReader.get("OrderNum"));
				wItem.SourceType = StringUtils.parseInt(wReader.get("SourceType"));
				wItem.SourceID = StringUtils.parseInt(wReader.get("SourceID"));
				wItem.DifferenceItem = StringUtils.parseInt(wReader.get("DifferenceItem"));
				wItem.OverQuota = StringUtils.parseInt(wReader.get("OverQuota"));
				wItem.SAPStatus = StringUtils.parseInt(wReader.get("SAPStatus"));
				wItem.SAPMsg = StringUtils.parseString(wReader.get("SAPMsg"));
				wItem.NewNumber = StringUtils.parseDouble(wReader.get("NewNumber"));
				wItem.NewReplaceType = StringUtils.parseInt(wReader.get("NewReplaceType"));
				wItem.NewOutsourceType = StringUtils.parseInt(wReader.get("NewOutsourceType"));
				wItem.NewPartChange = StringUtils.parseInt(wReader.get("NewPartChange"));
				wItem.NewQTType = StringUtils.parseInt(wReader.get("NewQTType"));
				wItem.NewQTItemType = StringUtils.parseInt(wReader.get("NewQTItemType"));
				wItem.NewAssessmentType = StringUtils.parseString(wReader.get("NewAssessmentType"));
				wItem.NewRemark = StringUtils.parseString(wReader.get("NewRemark"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));

				wItem.CustomerCode = APSConstans.GetCRMCustomer(wItem.CustomerID).CustomerCode;
				wItem.CustomerName = APSConstans.GetCRMCustomerName(wItem.CustomerID);
				wItem.LineName = APSConstans.GetFMCLineName(wItem.LineID);
				wItem.MaterialName = StringUtils.parseString(wReader.get("MaterialName"));
				wItem.PartName = APSConstans.GetFPCPartName(wItem.PartID);
				wItem.PartPointName = APSConstans.GetFPCPartPointName(wItem.PartPointID);

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private APSBomBPMItemDAO() {
		super();
	}

	public static APSBomBPMItemDAO getInstance() {
		if (Instance == null)
			Instance = new APSBomBPMItemDAO();
		return Instance;
	}

	public List<Integer> GetIDListByMaterial(BMSEmployee wLoginUser, String wMaterialNo, String wMaterialName,
			OutResult<Integer> wErrorCode) {
		List<Integer> wResultList = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			if (wMaterialNo == null)
				wMaterialNo = "";
			if (wMaterialName == null)
				wMaterialName = "";

			String wSQL = MessageFormat.format(
					"SELECT distinct t1.APSBomBPMID FROM {0}.aps_bombpmitem t1,{0}.mss_material t2 WHERE t1.MaterialID=t2.ID and  1=1  "
							+ "and t2.MaterialName like ''%{1}%'' " + "and t2.MaterialNo like ''%{2}%'' ;",
					wInstance.Result, wMaterialName, wMaterialNo);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wAPSBomBPMID = StringUtils.parseInt(wReader.get("APSBomBPMID"));
				wResultList.add(wAPSBomBPMID);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}
}
