package com.mes.loco.aps.server.serviceimpl.dao.sfc;

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
import com.mes.loco.aps.server.service.po.sfc.SFCDefaultPerson;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.shristool.LoggerTool;

/**
 * 工位工序默认人员
 * 
 * @author ShrisJava
 *
 */
public class SFCDefaultPersonDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(SFCDefaultPersonDAO.class);

	private static SFCDefaultPersonDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wSFCDefaultPerson
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, SFCDefaultPerson wSFCDefaultPerson, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			long wStartMillis = System.currentTimeMillis();
			
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wSFCDefaultPerson == null)
				return 0;

			String wSQL = "";
			if (wSFCDefaultPerson.getID() <= 0) {
				wSQL = MessageFormat
						.format("INSERT INTO {0}.sfc_defaultperson(ProductID,LineID,PartID,PartPointID,PersonIDList) "
								+ "VALUES(:ProductID,:LineID,:PartID,:PartPointID,:PersonIDList);", wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.sfc_defaultperson SET ProductID=:ProductID, LineID = :LineID,PartID = :PartID,"
								+ "PartPointID = :PartPointID,PersonIDList = :PersonIDList WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wSFCDefaultPerson.ID);
			wParamMap.put("ProductID", wSFCDefaultPerson.ProductID);
			wParamMap.put("LineID", wSFCDefaultPerson.LineID);
			wParamMap.put("PartID", wSFCDefaultPerson.PartID);
			wParamMap.put("PartPointID", wSFCDefaultPerson.PartPointID);
			wParamMap.put("PersonIDList", StringUtils.Join(";", wSFCDefaultPerson.PersonIDList));

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wSFCDefaultPerson.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wSFCDefaultPerson.setID(wResult);
			} else {
				wResult = wSFCDefaultPerson.getID();
			}
			
			long wEndMillis = System.currentTimeMillis();
			int wCallMS = (int) (wEndMillis - wStartMillis);
			LoggerTool.MonitorFunction("派工测试", "Step_Update默认人员", wCallMS);
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<SFCDefaultPerson> wList,
			OutResult<Integer> wErrorCode) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wList == null || wList.size() <= 0)
				return wResult;

			List<String> wIDList = new ArrayList<String>();
			for (SFCDefaultPerson wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.sfc_defaultperson WHERE ID IN({0}) ;",
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
	public SFCDefaultPerson SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		SFCDefaultPerson wResult = new SFCDefaultPerson();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<SFCDefaultPerson> wList = SelectList(wLoginUser, wID, -1, -1, -1, -1, wErrorCode);
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
	public List<SFCDefaultPerson> SelectList(BMSEmployee wLoginUser, int wID, int wProductID, int wLineID, int wPartID,
			int wPartPointID, OutResult<Integer> wErrorCode) {
		List<SFCDefaultPerson> wResultList = new ArrayList<SFCDefaultPerson>();
		try {
			long wStartMillis = System.currentTimeMillis();
			
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser, MESDBSource.APS, 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format("SELECT * FROM {0}.sfc_defaultperson WHERE  1=1  "
					+ "and ( :wID <= 0 or :wID = ID ) " + "and ( :wProductID <= 0 or :wProductID = ProductID ) "
					+ "and ( :wLineID <= 0 or :wLineID = LineID ) " + "and ( :wPartID <= 0 or :wPartID = PartID ) "
					+ "and ( :wPartPointID <= 0 or :wPartPointID = PartPointID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);
			wParamMap.put("wProductID", wProductID);
			wParamMap.put("wLineID", wLineID);
			wParamMap.put("wPartID", wPartID);
			wParamMap.put("wPartPointID", wPartPointID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				SFCDefaultPerson wItem = new SFCDefaultPerson();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.ProductID = StringUtils.parseInt(wReader.get("ProductID"));
				wItem.LineID = StringUtils.parseInt(wReader.get("LineID"));
				wItem.PartID = StringUtils.parseInt(wReader.get("PartID"));
				wItem.PartPointID = StringUtils.parseInt(wReader.get("PartPointID"));
				wItem.PersonIDList = StringUtils
						.parseIntList((StringUtils.parseString(wReader.get("PersonIDList")).split(";")));

				wResultList.add(wItem);
			}
			
			long wEndMillis = System.currentTimeMillis();
			int wCallMS = (int) (wEndMillis - wStartMillis);
			LoggerTool.MonitorFunction("派工测试", "Step_SelectList", wCallMS);
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private SFCDefaultPersonDAO() {
		super();
	}

	public static SFCDefaultPersonDAO getInstance() {
		if (Instance == null)
			Instance = new SFCDefaultPersonDAO();
		return Instance;
	}
}
