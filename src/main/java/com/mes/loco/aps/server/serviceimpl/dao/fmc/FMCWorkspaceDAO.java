package com.mes.loco.aps.server.serviceimpl.dao.fmc;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.mesenum.MESDBSource;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class FMCWorkspaceDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(FMCWorkspaceDAO.class);

	private static FMCWorkspaceDAO Instance = null;

	/**
	 * 查单条
	 * 
	 * @return
	 */
	public FMCWorkspace SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		FMCWorkspace wResult = new FMCWorkspace();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<FMCWorkspace> wList = SelectList(wLoginUser, wID, wErrorCode);
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
	public List<FMCWorkspace> SelectList(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		List<FMCWorkspace> wResultList = new ArrayList<FMCWorkspace>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.Basic,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format(
					"SELECT * FROM {0}.fmc_workspace WHERE  1=1  and ( :wID <= 0 or :wID = ID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				FMCWorkspace wItem = new FMCWorkspace();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.Name = StringUtils.parseString(wReader.get("Name"));
				wItem.Code = StringUtils.parseString(wReader.get("Code"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.PlaceType = StringUtils.parseInt(wReader.get("PlaceType"));
				wItem.CreatorID = StringUtils.parseInt(wReader.get("CreatorID"));
				wItem.CreateTime = StringUtils.parseCalendar(wReader.get("CreateTime"));
				wItem.EditorID = StringUtils.parseInt(wReader.get("EditorID"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartNo = StringUtils.parseString(wReader.get("PartNo"));
				wItem.Active = StringUtils.parseInt(wReader.get("Active"));
				wItem.OrderID = StringUtils.parseInt(wReader.get("OrderID"));
				wItem.ParentID = StringUtils.parseInt(wReader.get("ParentID"));
				wItem.Length = (double) StringUtils.parseInt(wReader.get("Length"));
				wItem.Status = StringUtils.parseInt(wReader.get("Status"));
				wItem.AlowTransType = StringUtils.parseInt(wReader.get("AlowTransType"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private FMCWorkspaceDAO() {
		super();
	}

	public static FMCWorkspaceDAO getInstance() {
		if (Instance == null)
			Instance = new FMCWorkspaceDAO();
		return Instance;
	}
}
