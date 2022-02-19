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
import com.mes.loco.aps.server.service.po.oms.OMSOutsourceOrder;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

/**
 * 委外修部件
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 09:43:10
 * @LastEditTime 2020-4-17 15:19:54
 *
 */
public class OMSOutsourceOrderDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(OMSOutsourceOrderDAO.class);

	private static OMSOutsourceOrderDAO Instance = null;

	/**
	 * 权限码
	 */
	private static int AccessCode = 501300;

	/**
	 * 添加或修改
	 * 
	 * @param wOMSOutsourceOrder
	 * @return
	 */
	public long Update(BMSEmployee wLoginUser, OMSOutsourceOrder wOMSOutsourceOrder, OutResult<Integer> wErrorCode) {
		long wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOMSOutsourceOrder == null)
				return 0;

			String wSQL = "";
			if (wOMSOutsourceOrder.getID() <= 0) {
				wSQL = StringUtils.Format(
						"INSERT INTO {0}.oms_outsourceorder(StationID,OrderID,PartNo,MaterialID,ComponentName,SupplierID,FQTY,LinkmanID,SupplierAddress,ApplyTime,SendRepairTime,ReceiveRepairTime,FinishRepairTime,PlanReceiveDate,RequirementDate,RealPurchaseDur,RequireDur,Status,Remark,GoodRemark,PurchaserID,CreateID,CreateTime,EditID,EditTime,AtualReceiveDate) VALUES(:StationID,:OrderID,:PartNo,:MaterialID,:ComponentName,:SupplierID,:FQTY,:LinkmanID,:SupplierAddress,:ApplyTime,:SendRepairTime,:ReceiveRepairTime,:FinishRepairTime,:PlanReceiveDate,:RequirementDate,:RealPurchaseDur,:RequireDur,:Status,:Remark,:GoodRemark,:PurchaserID,:CreateID,:CreateTime,:EditID,:EditTime,:AtualReceiveDate);",
						wInstance.Result);
			} else {
				wSQL = StringUtils.Format(
						"UPDATE {0}.oms_outsourceorder SET StationID = :StationID,OrderID = :OrderID,PartNo = :PartNo,MaterialID = :MaterialID,ComponentName = :ComponentName,SupplierID = :SupplierID,FQTY = :FQTY,LinkmanID = :LinkmanID,SupplierAddress = :SupplierAddress,ApplyTime = :ApplyTime,SendRepairTime = :SendRepairTime,ReceiveRepairTime = :ReceiveRepairTime,FinishRepairTime = :FinishRepairTime,PlanReceiveDate = :PlanReceiveDate,RequirementDate = :RequirementDate,RealPurchaseDur = :RealPurchaseDur,RequireDur = :RequireDur,Status = :Status,Remark = :Remark,GoodRemark = :GoodRemark,PurchaserID = :PurchaserID,CreateID = :CreateID,CreateTime = :CreateTime,EditID = :EditID,EditTime = :EditTime,AtualReceiveDate = :AtualReceiveDate WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wOMSOutsourceOrder.ID);
			wParamMap.put("StationID", wOMSOutsourceOrder.StationID);
			wParamMap.put("OrderID", wOMSOutsourceOrder.OrderID);
			wParamMap.put("PartNo", wOMSOutsourceOrder.PartNo);
			wParamMap.put("MaterialID", wOMSOutsourceOrder.MaterialID);
			wParamMap.put("ComponentName", wOMSOutsourceOrder.ComponentName);
			wParamMap.put("SupplierID", wOMSOutsourceOrder.SupplierID);
			wParamMap.put("FQTY", wOMSOutsourceOrder.FQTY);
			wParamMap.put("LinkmanID", wOMSOutsourceOrder.LinkmanID);
			wParamMap.put("SupplierAddress", wOMSOutsourceOrder.SupplierAddress);
			wParamMap.put("ApplyTime", wOMSOutsourceOrder.ApplyTime);
			wParamMap.put("SendRepairTime", wOMSOutsourceOrder.SendRepairTime);
			wParamMap.put("ReceiveRepairTime", wOMSOutsourceOrder.ReceiveRepairTime);
			wParamMap.put("FinishRepairTime", wOMSOutsourceOrder.FinishRepairTime);
			wParamMap.put("PlanReceiveDate", wOMSOutsourceOrder.PlanReceiveDate);
			wParamMap.put("RequirementDate", wOMSOutsourceOrder.RequirementDate);
			wParamMap.put("RealPurchaseDur", wOMSOutsourceOrder.RealPurchaseDur);
			wParamMap.put("RequireDur", wOMSOutsourceOrder.RequireDur);
			wParamMap.put("Status", wOMSOutsourceOrder.Status);
			wParamMap.put("Remark", wOMSOutsourceOrder.Remark);
			wParamMap.put("GoodRemark", wOMSOutsourceOrder.GoodRemark);
			wParamMap.put("PurchaserID", wOMSOutsourceOrder.PurchaserID);
			wParamMap.put("CreateID", wOMSOutsourceOrder.CreateID);
			wParamMap.put("CreateTime", wOMSOutsourceOrder.CreateTime);
			wParamMap.put("EditID", wOMSOutsourceOrder.EditID);
			wParamMap.put("EditTime", wOMSOutsourceOrder.EditTime);
			wParamMap.put("AtualReceiveDate", wOMSOutsourceOrder.AtualReceiveDate);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wOMSOutsourceOrder.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wOMSOutsourceOrder.setID(wResult);
			} else {
				wResult = wOMSOutsourceOrder.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<OMSOutsourceOrder> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), AccessCode);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (OMSOutsourceOrder wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = StringUtils.Format("delete from {1}.oms_outsourceorder WHERE ID IN({0}) ;",
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
	public OMSOutsourceOrder SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		OMSOutsourceOrder wResult = new OMSOutsourceOrder();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<OMSOutsourceOrder> wList = SelectList(wLoginUser, wID, -1, -1, "", -1, wErrorCode);
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
	public List<OMSOutsourceOrder> SelectList(BMSEmployee wLoginUser, List<Integer> wOrderIDList,
			OutResult<Integer> wErrorCode) {
		List<OMSOutsourceOrder> wResult = new ArrayList<OMSOutsourceOrder>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wOrderIDList == null || wOrderIDList.size() <= 0)
				return wResult;

			String wSQL = StringUtils.Format(
					"SELECT * FROM {0}.oms_outsourceorder WHERE  1=1  " + "and ( OrderID in ({1}) ); ",
					wInstance.Result, StringUtils.Join(",", wOrderIDList));

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				OMSOutsourceOrder wItem = new OMSOutsourceOrder();

				wItem.ID = StringUtils.parseLong(wReader.get("ID"));
				wItem.StationID = StringUtils.parseInt(wReader.get("StationID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.ComponentName = StringUtils.parseString(wReader.get("ComponentName"));
				wItem.SupplierID = StringUtils.parseInt(wReader.get("SupplierID"));
				wItem.FQTY = StringUtils.parseInt(wReader.get("FQTY"));
				wItem.LinkmanID = StringUtils.parseInt(wReader.get("LinkmanID"));
				wItem.SupplierAddress = StringUtils.parseString(wReader.get("SupplierAddress"));
				wItem.ApplyTime = StringUtils.parseCalendar(wReader.get("ApplyTime"));
				wItem.SendRepairTime = StringUtils.parseCalendar(wReader.get("SendRepairTime"));
				wItem.ReceiveRepairTime = StringUtils.parseCalendar(wReader.get("ReceiveRepairTime"));
				wItem.FinishRepairTime = StringUtils.parseCalendar(wReader.get("FinishRepairTime"));
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
				wItem.AtualReceiveDate = StringUtils.parseCalendar(wReader.get("AtualReceiveDate"));

				wResult.add(wItem);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 条件查询集合
	 * 
	 * @return
	 */
	public List<OMSOutsourceOrder> SelectList(BMSEmployee wLoginUser, long wID, int wStationID, int wOrderID,
			String wPartNo, int wMaterialID, OutResult<Integer> wErrorCode) {
		List<OMSOutsourceOrder> wResultList = new ArrayList<OMSOutsourceOrder>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = StringUtils.Format("SELECT * FROM {0}.oms_outsourceorder WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wStationID <= 0 or :wStationID = StationID ) "
					+ "and ( :wOrderID <= 0 or :wOrderID = OrderID ) "
					+ "and ( :wPartNo is null or :wPartNo = '''' or :wPartNo = PartNo ) "
					+ "and ( :wMaterialID <= 0 or :wMaterialID = MaterialID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wStationID", wStationID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartNo", wPartNo);
			wParamMap.put("wMaterialID", wMaterialID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				OMSOutsourceOrder wItem = new OMSOutsourceOrder();

				wItem.ID = StringUtils.parseLong(wReader.get("ID"));
				wItem.StationID = StringUtils.parseInt(wReader.get("StationID"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.MaterialID = StringUtils.parseInt(wReader.get("MaterialID"));
				wItem.ComponentName = StringUtils.parseString(wReader.get("ComponentName"));
				wItem.SupplierID = StringUtils.parseInt(wReader.get("SupplierID"));
				wItem.FQTY = StringUtils.parseInt(wReader.get("FQTY"));
				wItem.LinkmanID = StringUtils.parseInt(wReader.get("LinkmanID"));
				wItem.SupplierAddress = StringUtils.parseString(wReader.get("SupplierAddress"));
				wItem.ApplyTime = StringUtils.parseCalendar(wReader.get("ApplyTime"));
				wItem.SendRepairTime = StringUtils.parseCalendar(wReader.get("SendRepairTime"));
				wItem.ReceiveRepairTime = StringUtils.parseCalendar(wReader.get("ReceiveRepairTime"));
				wItem.FinishRepairTime = StringUtils.parseCalendar(wReader.get("FinishRepairTime"));
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
				wItem.AtualReceiveDate = StringUtils.parseCalendar(wReader.get("AtualReceiveDate"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private OMSOutsourceOrderDAO() {
		super();
	}

	public static OMSOutsourceOrderDAO getInstance() {
		if (Instance == null)
			Instance = new OMSOutsourceOrderDAO();
		return Instance;
	}
}
