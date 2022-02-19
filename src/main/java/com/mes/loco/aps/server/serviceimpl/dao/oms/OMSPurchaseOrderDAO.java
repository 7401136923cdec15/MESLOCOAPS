package com.mes.loco.aps.server.serviceimpl.dao.oms;

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
import com.mes.loco.aps.server.service.po.oms.OMSPurchaseOrder;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class OMSPurchaseOrderDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(OMSPurchaseOrderDAO.class);

	private static OMSPurchaseOrderDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wOMSPurchaseOrder
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, OMSPurchaseOrder wOMSPurchaseOrder, OutResult<Integer> wErrorCode) {
		long wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOMSPurchaseOrder == null)
				return 0;

			String wSQL = "";
			if (wOMSPurchaseOrder.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.oms_purchaseorder(PlaceID,PartNo,MaterialID,SupplierID,FQTY,LinkmanID,ApplyTime,OrderTime,PlanReceiveDate,RequirementDate,RealPurchaseDur,RequireDur,Status,Remark,GoodRemark,PurchaserID,CreateID,CreateTime,EditID,EditTime) VALUES(:PlaceID,:PartNo,:MaterialID,:SupplierID,:FQTY,:LinkmanID,:ApplyTime,:OrderTime,:PlanReceiveDate,:RequirementDate,:RealPurchaseDur,:RequireDur,:Status,:Remark,:GoodRemark,:PurchaserID,:CreateID,:CreateTime,:EditID,:EditTime);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.oms_purchaseorder SET PlaceID = :PlaceID,PartNo = :PartNo,MaterialID = :MaterialID,SupplierID = :SupplierID,FQTY = :FQTY,LinkmanID = :LinkmanID,ApplyTime = :ApplyTime,OrderTime = :OrderTime,PlanReceiveDate = :PlanReceiveDate,RequirementDate = :RequirementDate,RealPurchaseDur = :RealPurchaseDur,RequireDur = :RequireDur,Status = :Status,Remark = :Remark,GoodRemark = :GoodRemark,PurchaserID = :PurchaserID,CreateID = :CreateID,CreateTime = :CreateTime,EditID = :EditID,EditTime = :EditTime WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wOMSPurchaseOrder.ID);
			wParamMap.put("PlaceID", wOMSPurchaseOrder.PlaceID);
			wParamMap.put("PartNo", wOMSPurchaseOrder.PartNo);
			wParamMap.put("MaterialID", wOMSPurchaseOrder.MaterialID);
			wParamMap.put("SupplierID", wOMSPurchaseOrder.SupplierID);
			wParamMap.put("FQTY", wOMSPurchaseOrder.FQTY);
			wParamMap.put("LinkmanID", wOMSPurchaseOrder.LinkmanID);
			wParamMap.put("ApplyTime", wOMSPurchaseOrder.ApplyTime);
			wParamMap.put("OrderTime", wOMSPurchaseOrder.OrderTime);
			wParamMap.put("PlanReceiveDate", wOMSPurchaseOrder.PlanReceiveDate);
			wParamMap.put("RequirementDate", wOMSPurchaseOrder.RequirementDate);
			wParamMap.put("RealPurchaseDur", wOMSPurchaseOrder.RealPurchaseDur);
			wParamMap.put("RequireDur", wOMSPurchaseOrder.RequireDur);
			wParamMap.put("Status", wOMSPurchaseOrder.Status);
			wParamMap.put("Remark", wOMSPurchaseOrder.Remark);
			wParamMap.put("GoodRemark", wOMSPurchaseOrder.GoodRemark);
			wParamMap.put("PurchaserID", wOMSPurchaseOrder.PurchaserID);
			wParamMap.put("CreateID", wOMSPurchaseOrder.CreateID);
			wParamMap.put("CreateTime", wOMSPurchaseOrder.CreateTime);
			wParamMap.put("EditID", wOMSPurchaseOrder.EditID);
			wParamMap.put("EditTime", wOMSPurchaseOrder.EditTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wOMSPurchaseOrder.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wOMSPurchaseOrder.setID(wResult);
			} else {
				wResult = wOMSPurchaseOrder.getID();
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
	public void DeleteList(BMSEmployee wLoginUser, List<OMSPurchaseOrder> wList, OutResult<Integer> wErrorCode) {
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return;
			}

			if (wList == null || wList.size() <= 0)
				return;

			List<String> wIDList = new ArrayList<String>();
			for (OMSPurchaseOrder wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.oms_purchaseorder WHERE ID IN({0}) ;",
					String.join(",", wIDList), wInstance.Result);
			this.ExecuteSqlTransaction(wSql);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
	}

	/**
	 * 查单条
	 * 
	 * @return
	 */
	public OMSPurchaseOrder SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		OMSPurchaseOrder wResult = new OMSPurchaseOrder();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<OMSPurchaseOrder> wList = SelectList(wLoginUser, wID, -1, "", -1, wErrorCode);
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
	public List<OMSPurchaseOrder> SelectList(BMSEmployee wLoginUser, long wID, int wPlaceID, String wPartNo,
			int wMaterialID, OutResult<Integer> wErrorCode) {
		List<OMSPurchaseOrder> wResultList = new ArrayList<OMSPurchaseOrder>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.oms_purchaseorder WHERE  1=1  and ( :wID <= 0 or :wID = ID ) and ( :wPlaceID <= 0 or :wPlaceID = PlaceID ) and ( :wPartNo is null or :wPartNo = '''' or :wPartNo = PartNo ) and ( :wMaterialID <= 0 or :wMaterialID = MaterialID );",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wPlaceID", wPlaceID);
			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wMaterialID", wMaterialID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				OMSPurchaseOrder wItem = new OMSPurchaseOrder();

				wItem.ID = StringUtils.parseLong(wReader.get("ID"));
				wItem.PlaceID = StringUtils.parseInt(wReader.get("PlaceID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.SupplierID = StringUtils.parseInt(wReader.get("SupplierID"));
				wItem.FQTY = StringUtils.parseInt(wReader.get("FQTY"));
				wItem.LinkmanID = StringUtils.parseInt(wReader.get("LinkmanID"));
				wItem.ApplyTime = StringUtils.parseCalendar(wReader.get("ApplyTime"));
				wItem.OrderTime = StringUtils.parseCalendar(wReader.get("OrderTime"));
				wItem.PlanReceiveDate = StringUtils.parseCalendar(wReader.get("PlanReceiveDate"));
				wItem.RequirementDate = StringUtils.parseCalendar(wReader.get("RequirementDate"));
				wItem.RealPurchaseDur = StringUtils.parseDouble(wReader.get("RealPurchaseDur"));
				wItem.RequireDur = StringUtils.parseDouble(wReader.get("RequireDur"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.Remark = StringUtils.parseString(wReader.get("Remark"));
				wItem.GoodRemark = StringUtils.parseString(wReader.get("GoodRemark"));
				wItem.PurchaserID = StringUtils.parseInt(wReader.get("PurchaserID"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditID = StringUtils.parseInt(wReader.get("EditID"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private OMSPurchaseOrderDAO() {
		super();
	}

	public static OMSPurchaseOrderDAO getInstance() {
		if (Instance == null)
			Instance = new OMSPurchaseOrderDAO();
		return Instance;
	}
}
