package com.mes.loco.aps.server.serviceimpl.dao.wms;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.mesenum.WMSOrderType;
import com.mes.loco.aps.server.service.mesenum.WMSPickDemandStatus;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemand;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class WMSPickDemandDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(WMSPickDemandDAO.class);

	private static WMSPickDemandDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wWMSPickDemand
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, WMSPickDemand wWMSPickDemand, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wWMSPickDemand == null)
				return 0;

			String wSQL = "";
			if (wWMSPickDemand.getID() <= 0) {
				wSQL = MessageFormat
						.format("INSERT INTO {0}.wms_pickdemand(Factory,OrderType,DemandNo,ExpectTime1,ExpectTime2,"
								+ "MonitorNo,Monitor,ProductID,ProductNo,LineID,LineName,CustomerID,CustomerName,"
								+ "CustomerCode,OrderID,PartNo,PartID,PartName,PartCode,DeliveryMan,DeliveryNo,DeliveryTime,"
								+ "ReceiveMan,ReceiveNo,ReceiveTime,CreateID,Creator,CreateTime,Status,SendStatus,"
								+ "SendDesc,WBSNo,SorterID,Sorter,SorterNo,SorterTime,DeliveryID,ReceiveID) "
								+ "VALUES(:Factory,:OrderType,:DemandNo,:ExpectTime1,:ExpectTime2,:MonitorNo,"
								+ ":Monitor,:ProductID,:ProductNo,:LineID,:LineName,:CustomerID,:CustomerName,"
								+ ":CustomerCode,:OrderID,:PartNo,:PartID,:PartName,:PartCode,:DeliveryMan,:DeliveryNo,"
								+ ":DeliveryTime,:ReceiveMan,:ReceiveNo,:ReceiveTime,:CreateID,:Creator,:CreateTime,:Status,"
								+ ":SendStatus,:SendDesc,:WBSNo,:SorterID,:Sorter,:SorterNo,:SorterTime,"
								+ ":DeliveryID,:ReceiveID);", wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.wms_pickdemand SET Factory = :Factory,OrderType = :OrderType,DemandNo = :DemandNo,"
								+ "ExpectTime1 = :ExpectTime1,ExpectTime2 = :ExpectTime2,MonitorNo = :MonitorNo,"
								+ "Monitor = :Monitor,ProductID = :ProductID,ProductNo = :ProductNo,LineID = :LineID,"
								+ "LineName = :LineName,CustomerID = :CustomerID,CustomerName = :CustomerName,"
								+ "CustomerCode = :CustomerCode,OrderID=:OrderID,PartNo = :PartNo,PartID = :PartID,PartName = :PartName,"
								+ "PartCode = :PartCode,DeliveryMan = :DeliveryMan,DeliveryNo = :DeliveryNo,"
								+ "DeliveryTime = :DeliveryTime,ReceiveMan = :ReceiveMan,ReceiveNo = :ReceiveNo,"
								+ "ReceiveTime = :ReceiveTime,CreateID = :CreateID,Creator = :Creator,CreateTime = :CreateTime,"
								+ "Status = :Status,SendStatus=:SendStatus,SendDesc=:SendDesc,WBSNo=:WBSNo,SorterID=:SorterID,"
								+ "Sorter=:Sorter,SorterNo=:SorterNo,SorterTime=:SorterTime,DeliveryID=:DeliveryID,"
								+ "ReceiveID=:ReceiveID WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wWMSPickDemand.ID);
			wParamMap.put("Factory", wWMSPickDemand.Factory);
			wParamMap.put("OrderType", wWMSPickDemand.OrderType);
			wParamMap.put("DemandNo", wWMSPickDemand.DemandNo);
			wParamMap.put("ExpectTime1", wWMSPickDemand.ExpectTime1);
			wParamMap.put("ExpectTime2", wWMSPickDemand.ExpectTime2);
			wParamMap.put("MonitorNo", wWMSPickDemand.MonitorNo);
			wParamMap.put("Monitor", wWMSPickDemand.Monitor);
			wParamMap.put("ProductID", wWMSPickDemand.ProductID);
			wParamMap.put("ProductNo", wWMSPickDemand.ProductNo);
			wParamMap.put("LineID", wWMSPickDemand.LineID);
			wParamMap.put("LineName", wWMSPickDemand.LineName);
			wParamMap.put("CustomerID", wWMSPickDemand.CustomerID);
			wParamMap.put("CustomerName", wWMSPickDemand.CustomerName);
			wParamMap.put("CustomerCode", wWMSPickDemand.CustomerCode);
			wParamMap.put("OrderID", wWMSPickDemand.OrderID);
			wParamMap.put("PartNo", wWMSPickDemand.PartNo);
			wParamMap.put("PartID", wWMSPickDemand.PartID);
			wParamMap.put("PartName", wWMSPickDemand.PartName);
			wParamMap.put("PartCode", wWMSPickDemand.PartCode);
			wParamMap.put("DeliveryMan", wWMSPickDemand.DeliveryMan);
			wParamMap.put("DeliveryNo", wWMSPickDemand.DeliveryNo);
			wParamMap.put("DeliveryTime", wWMSPickDemand.DeliveryTime);
			wParamMap.put("ReceiveMan", wWMSPickDemand.ReceiveMan);
			wParamMap.put("ReceiveNo", wWMSPickDemand.ReceiveNo);
			wParamMap.put("ReceiveTime", wWMSPickDemand.ReceiveTime);
			wParamMap.put("CreateID", wWMSPickDemand.CreateID);
			wParamMap.put("Creator", wWMSPickDemand.Creator);
			wParamMap.put("CreateTime", wWMSPickDemand.CreateTime);
			wParamMap.put("Status", wWMSPickDemand.Status);
			wParamMap.put("SendStatus", wWMSPickDemand.SendStatus);
			wParamMap.put("SendDesc", wWMSPickDemand.SendDesc);
			wParamMap.put("WBSNo", wWMSPickDemand.WBSNo);
			wParamMap.put("SorterID", wWMSPickDemand.SorterID);
			wParamMap.put("Sorter", wWMSPickDemand.Sorter);
			wParamMap.put("SorterNo", wWMSPickDemand.SorterNo);
			wParamMap.put("SorterTime", wWMSPickDemand.SorterTime);
			wParamMap.put("DeliveryID", wWMSPickDemand.DeliveryID);
			wParamMap.put("ReceiveID", wWMSPickDemand.ReceiveID);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wWMSPickDemand.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wWMSPickDemand.setID(wResult);
			} else {
				wResult = wWMSPickDemand.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<WMSPickDemand> wList,
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
			for (WMSPickDemand wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.wms_pickdemand WHERE ID IN({0}) ;",
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
	public WMSPickDemand SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		WMSPickDemand wResult = new WMSPickDemand();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<WMSPickDemand> wList = SelectList(wLoginUser, wID, "", "", -1, -1, -1, -1, -1, -1, null, null, null,
					wErrorCode);
			if (wList == null || wList.size() != 1)
				return wResult;
			wResult = wList.get(0);
			wResult.ItemList = WMSPickDemandItemDAO.getInstance().SelectList(wLoginUser, -1, wResult.ID, -1, "", -1,
					wErrorCode);
			wResult.ItemList.forEach(p -> {
				p.DemandNo = wList.get(0).DemandNo;
				p.ProductNo = wList.get(0).ProductNo;
				p.LineName = wList.get(0).LineName;
				p.CustomerName = wList.get(0).CustomerName;
				p.PartName = wList.get(0).PartName;
				p.PartNo = wList.get(0).PartNo;
			});
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
	public List<WMSPickDemand> SelectList(BMSEmployee wLoginUser, int wID, String wOrderType, String wDemandNo,
			int wProductID, int wLineID, int wCustomerID, int wOrderID, int wPartID, int wStatus, Calendar wStartTime,
			Calendar wEndTime, List<Integer> wIDList, OutResult<Integer> wErrorCode) {
		List<WMSPickDemand> wResultList = new ArrayList<WMSPickDemand>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1, 0, 0, 0);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) > 0) {
				return wResultList;
			}

			if (wIDList == null) {
				wIDList = new ArrayList<Integer>();
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.wms_pickdemand WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) "
					+ "and ( :wOrderType is null or :wOrderType = '''' or :wOrderType = OrderType ) "
					+ "and ( :wDemandNo is null or :wDemandNo = '''' or DemandNo like ''%{1}%'' ) "
					+ "and ( :wStartTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wStartTime <=  CreateTime ) "
					+ "and ( :wEndTime <=str_to_date(''2010-01-01'', ''%Y-%m-%d'')  or :wEndTime >=  CreateTime ) "
					+ "and ( :wProductID <= 0 or :wProductID = ProductID ) "
					+ "and ( :wLineID <= 0 or :wLineID = LineID ) "
					+ "and ( :wCustomerID <= 0 or :wCustomerID = CustomerID ) "
					+ "and ( :wIDs is null or :wIDs = '''' or ID in ({2})) "
					+ "and ( :wOrderID <= 0 or :wOrderID = OrderID ) " + "and ( :wPartID <= 0 or :wPartID = PartID ) "
					+ "and ( :wStatus <= 0 or :wStatus = Status );", wInstance.Result, wDemandNo,
					wIDList.size() > 0 ? StringUtils.Join(",", wIDList) : "0");

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wOrderType", wOrderType);
			wParamMap.put("wDemandNo", wDemandNo);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wCustomerID", wCustomerID);
			wParamMap.put("wOrderID", wOrderID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wStatus", wStatus);
			wParamMap.put("wStartTime", wStartTime);
			wParamMap.put("wEndTime", wEndTime);
			wParamMap.put("wIDs", StringUtils.Join(",", wIDList));

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat wDateSDF = new SimpleDateFormat("yyyy-MM-dd");

			Calendar wBaseTime1 = Calendar.getInstance();
			wBaseTime1.set(2010, 0, 1, 0, 0, 0);

			for (Map<String, Object> wReader : wQueryResult) {
				WMSPickDemand wItem = new WMSPickDemand();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Factory = StringUtils.parseString(wReader.get("Factory"));
				wItem.OrderType = StringUtils.parseString(wReader.get("OrderType"));
				wItem.DemandNo = StringUtils.parseString(wReader.get("DemandNo"));
				wItem.ExpectTime1 = StringUtils.parseCalendar(wReader.get("ExpectTime1"));
				wItem.ExpectTime2 = StringUtils.parseCalendar(wReader.get("ExpectTime2"));
				wItem.MonitorNo = StringUtils.parseString(wReader.get("MonitorNo"));
				wItem.Monitor = StringUtils.parseString(wReader.get("Monitor"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.ProductNo = StringUtils.parseString(wReader.get("ProductNo"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.LineName = StringUtils.parseString(wReader.get("LineName"));
				wItem.CustomerID = StringUtils.parseInt(wReader.get("CustomerID"));
				wItem.CustomerName = StringUtils.parseString(wReader.get("CustomerName"));
				wItem.CustomerCode = StringUtils.parseString(wReader.get("CustomerCode"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartName = StringUtils.parseString(wReader.get("PartName"));
				wItem.PartCode = StringUtils.parseString(wReader.get("PartCode"));
				wItem.DeliveryMan = StringUtils.parseString(wReader.get("DeliveryMan"));
				wItem.DeliveryNo = StringUtils.parseString(wReader.get("DeliveryNo"));
				wItem.DeliveryTime = StringUtils.parseCalendar(wReader.get("DeliveryTime"));
				wItem.ReceiveMan = StringUtils.parseString(wReader.get("ReceiveMan"));
				wItem.ReceiveNo = StringUtils.parseString(wReader.get("ReceiveNo"));
				wItem.ReceiveTime = StringUtils.parseCalendar(wReader.get("ReceiveTime"));
				wItem.CreateID = StringUtils.parseInt(wReader.get("CreateID"));
				wItem.Creator = StringUtils.parseString(wReader.get("Creator"));
				if (wItem.CreateID == -100)
					wItem.Creator = "MES系统";

				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.SendStatus = StringUtils.parseInt(wReader.get("SendStatus"));
				wItem.SendDesc = StringUtils.parseString(wReader.get("SendDesc"));
				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));

				wItem.SorterID = StringUtils.parseInt(wReader.get("SorterID"));
				wItem.Sorter = StringUtils.parseString(wReader.get("Sorter"));
				wItem.SorterNo = StringUtils.parseString(wReader.get("SorterNo"));
				wItem.SorterTime = StringUtils.parseCalendar(wReader.get("SorterTime"));
				wItem.ReceiveID = StringUtils.parseInt(wReader.get("ReceiveID"));
				wItem.WBSNo = StringUtils.parseString(wReader.get("WBSNo"));
				wItem.SorterTimeText = wItem.SorterTime.compareTo(wBaseTime1) > 0
						? wSDF.format(wItem.SorterTime.getTime())
						: "";

				// 辅助属性
				wItem.ExpectTime1Text = wDateSDF.format(wItem.ExpectTime1.getTime());
				wItem.DeliveryTimeText = wItem.DeliveryTime.compareTo(wBaseTime1) > 0
						? wSDF.format(wItem.DeliveryTime.getTime())
						: "";
				wItem.OrderTypeText = WMSOrderType.getEnumType(StringUtils.parseInt(wItem.OrderType)).getLable();
				wItem.ReceiveTimeText = wItem.ReceiveTime.compareTo(wBaseTime1) > 0
						? wSDF.format(wItem.ReceiveTime.getTime())
						: "";
				wItem.CreateTimeText = wSDF.format(wItem.CreateTime.getTime());
				wItem.StatusText = WMSPickDemandStatus.getEnumType(wItem.Status).getLable();
				wItem.SendStatusText = wItem.SendStatus == 1 ? "成功" : wItem.SendStatus == 2 ? "失败" : "暂未推送";

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	/**
	 * 获取最新的编码
	 */
	public String GetNewCode(BMSEmployee wLoginUser, OutResult<Integer> wErrorCode) {
		String wResult = "";
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.Basic, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			// 本月时间
			int wYear = Calendar.getInstance().get(Calendar.YEAR);
			int wMonth = Calendar.getInstance().get(Calendar.MONTH);
			Calendar wSTime = Calendar.getInstance();
			wSTime.set(wYear, wMonth, 1, 0, 0, 0);
			Calendar wETime = Calendar.getInstance();
			wETime.set(wYear, wMonth + 1, 1, 23, 59, 59);
			wETime.add(Calendar.DATE, -1);

			String wSQL = StringUtils.Format(
					"select count(*)+1 as Number from {0}.wms_pickdemand where CreateTime > :wSTime and CreateTime < :wETime;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();
			wParamMap.put("wSTime", wSTime);
			wParamMap.put("wETime", wETime);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			int wNumber = 0;
			for (Map<String, Object> wReader : wQueryResult) {
				if (wReader.containsKey("Number")) {
					wNumber = StringUtils.parseInt(wReader.get("Number"));
					break;
				}
			}

			wResult = StringUtils.Format("LSPD{0}{1}{2}", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
					String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1),
					String.format("%04d", wNumber));
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	private WMSPickDemandDAO() {
		super();
	}

	public static WMSPickDemandDAO getInstance() {
		if (Instance == null)
			Instance = new WMSPickDemandDAO();
		return Instance;
	}

	public BMSEmployee GetMonitorByPart(BMSEmployee wLoginUser, int wPartID, OutResult<Integer> wErrorCode) {
		BMSEmployee wResult = new BMSEmployee();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"select t1.Name,t1.LoginID from iplantmes.mbs_user t1,iplantmes.bms_position t2 "
							+ "where t1.DepartmentID in (SELECT ClassID FROM iplantmes.bms_workcharge "
							+ "where StationID=:StationID and Active=1) and t1.Position=t2.ID and t2.DutyID=1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("StationID", wPartID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				wResult.Name = StringUtils.parseString(wReader.get("Name"));
				wResult.LoginID = StringUtils.parseString(wReader.get("LoginID"));
				return wResult;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取满足条件的自动推送ID集合
	 */
	public List<Integer> SelectAutoList(BMSEmployee wLoginUser, Calendar wToday, OutResult<Integer> wErrorCode) {
		List<Integer> wResult = new ArrayList<Integer>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			String wSQL = StringUtils.Format(
					"SELECT ID FROM {0}.wms_pickdemand " + "where Status=1 and :wToday>=ExpectTime1;",
					wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wToday", wToday);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				int wItem = StringUtils.parseInt(wReader.get("ID"));
				wResult.add(wItem);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
