package com.mes.loco.aps.server.serviceimpl.dao.andon;

import java.math.BigDecimal;
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
import com.mes.loco.aps.server.service.po.andon.AndonConfig;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;

public class AndonConfigDAO extends BaseDAO {

	private static Logger logger = LoggerFactory.getLogger(AndonConfigDAO.class);

	private static AndonConfigDAO Instance = null;

	/**
	 * 添加或修改
	 * 
	 * @param wAndonConfig
	 * @return
	 */
	public int Update(BMSEmployee wLoginUser, AndonConfig wAndonConfig, OutResult<Integer> wErrorCode) {
		int wResult = 0;
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			if (wAndonConfig == null)
				return 0;

			String wSQL = "";
			if (wAndonConfig.getID() <= 0) {
				wSQL = MessageFormat.format(
						"INSERT INTO {0}.andon_config(YHTS_C6_ZB,YHTS_C5_ZB,JCTS_C6_ZB,JCTS_C5_ZB,FGJS_One_ZB,FGJS_Two_ZB,FGJS_Three_ZB,FGJS_Four_ZB,JHJF_Month,JHJF_Month_C6,JHJF_Month_C5,Uri,Type,EditID,Editor,EditTime,YHTS_C6,YHTS_C5,JCTS_C6,JCTS_C5,StopFlag,FGJS_One,FGJS_Two,FGJS_Three,FGJS_Four,RepairFlag,LJXJ_Year,LJXJ_Year_C6,LJXJ_Year_C5,SJJF_Month,SJJF_Month_C6,SJJF_Month_C5,PlanFlag,NotDone_One,Done_One,NotDone_Two,Done_Two,NotDone_Three,Done_Three,NotDone_Four,Done_Four,ProgressFlag,Total_Ncr,Doing_Ncr,Total_Exc,Doing_Exc,NcrExcFlag,Grade_1,Grade_2,Grade_3,Grade_4,Grade_5,Grade_6,Grade_7,Grade_8,Grade_9,Grade_10,Grade_11,Grade_12,Grade_Quality,GradeFlag,RotationTime) VALUES(:YHTS_C6_ZB,:YHTS_C5_ZB,:JCTS_C6_ZB,:JCTS_C5_ZB,:FGJS_One_ZB,:FGJS_Two_ZB,:FGJS_Three_ZB,:FGJS_Four_ZB,:JHJF_Month,:JHJF_Month_C6,:JHJF_Month_C5,:Uri,:Type,:EditID,:Editor,:EditTime,:YHTS_C6,:YHTS_C5,:JCTS_C6,:JCTS_C5,:StopFlag,:FGJS_One,:FGJS_Two,:FGJS_Three,:FGJS_Four,:RepairFlag,:LJXJ_Year,:LJXJ_Year_C6,:LJXJ_Year_C5,:SJJF_Month,:SJJF_Month_C6,:SJJF_Month_C5,:PlanFlag,:NotDone_One,:Done_One,:NotDone_Two,:Done_Two,:NotDone_Three,:Done_Three,:NotDone_Four,:Done_Four,:ProgressFlag,:Total_Ncr,:Doing_Ncr,:Total_Exc,:Doing_Exc,:NcrExcFlag,:Grade_1,:Grade_2,:Grade_3,:Grade_4,:Grade_5,:Grade_6,:Grade_7,:Grade_8,:Grade_9,:Grade_10,:Grade_11,:Grade_12,:Grade_Quality,:GradeFlag,:RotationTime);",
						wInstance.Result);
			} else {
				wSQL = MessageFormat.format(
						"UPDATE {0}.andon_config SET YHTS_C6_ZB = :YHTS_C6_ZB,YHTS_C5_ZB = :YHTS_C5_ZB,JCTS_C6_ZB = :JCTS_C6_ZB,JCTS_C5_ZB = :JCTS_C5_ZB,FGJS_One_ZB = :FGJS_One_ZB,FGJS_Two_ZB = :FGJS_Two_ZB,FGJS_Three_ZB = :FGJS_Three_ZB,FGJS_Four_ZB = :FGJS_Four_ZB,JHJF_Month = :JHJF_Month,JHJF_Month_C6 = :JHJF_Month_C6,JHJF_Month_C5 = :JHJF_Month_C5,Uri = :Uri,Type = :Type,EditID = :EditID,Editor = :Editor,EditTime = :EditTime,YHTS_C6 = :YHTS_C6,YHTS_C5 = :YHTS_C5,JCTS_C6 = :JCTS_C6,JCTS_C5 = :JCTS_C5,StopFlag = :StopFlag,FGJS_One = :FGJS_One,FGJS_Two = :FGJS_Two,FGJS_Three = :FGJS_Three,FGJS_Four = :FGJS_Four,RepairFlag = :RepairFlag,LJXJ_Year = :LJXJ_Year,LJXJ_Year_C6 = :LJXJ_Year_C6,LJXJ_Year_C5 = :LJXJ_Year_C5,SJJF_Month = :SJJF_Month,SJJF_Month_C6 = :SJJF_Month_C6,SJJF_Month_C5 = :SJJF_Month_C5,PlanFlag = :PlanFlag,NotDone_One = :NotDone_One,Done_One = :Done_One,NotDone_Two = :NotDone_Two,Done_Two = :Done_Two,NotDone_Three = :NotDone_Three,Done_Three = :Done_Three,NotDone_Four = :NotDone_Four,Done_Four = :Done_Four,ProgressFlag = :ProgressFlag,Total_Ncr = :Total_Ncr,Doing_Ncr = :Doing_Ncr,Total_Exc = :Total_Exc,Doing_Exc = :Doing_Exc,NcrExcFlag = :NcrExcFlag,Grade_1 = :Grade_1,Grade_2 = :Grade_2,Grade_3 = :Grade_3,Grade_4 = :Grade_4,Grade_5 = :Grade_5,Grade_6 = :Grade_6,Grade_7 = :Grade_7,Grade_8 = :Grade_8,Grade_9 = :Grade_9,Grade_10 = :Grade_10,Grade_11 = :Grade_11,Grade_12 = :Grade_12,Grade_Quality = :Grade_Quality,GradeFlag = :GradeFlag,RotationTime=:RotationTime WHERE ID = :ID;",
						wInstance.Result);
			}

			wSQL = this.DMLChange(wSQL);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("ID", wAndonConfig.ID);
			wParamMap.put("YHTS_C6_ZB", wAndonConfig.YHTS_C6_ZB);
			wParamMap.put("YHTS_C5_ZB", wAndonConfig.YHTS_C5_ZB);
			wParamMap.put("JCTS_C6_ZB", wAndonConfig.JCTS_C6_ZB);
			wParamMap.put("JCTS_C5_ZB", wAndonConfig.JCTS_C5_ZB);
			wParamMap.put("FGJS_One_ZB", wAndonConfig.FGJS_One_ZB);
			wParamMap.put("FGJS_Two_ZB", wAndonConfig.FGJS_Two_ZB);
			wParamMap.put("FGJS_Three_ZB", wAndonConfig.FGJS_Three_ZB);
			wParamMap.put("FGJS_Four_ZB", wAndonConfig.FGJS_Four_ZB);
			wParamMap.put("JHJF_Month", wAndonConfig.JHJF_Month);
			wParamMap.put("JHJF_Month_C6", wAndonConfig.JHJF_Month_C6);
			wParamMap.put("JHJF_Month_C5", wAndonConfig.JHJF_Month_C5);
			wParamMap.put("Uri", wAndonConfig.Uri);
			wParamMap.put("Type", wAndonConfig.Type);
			wParamMap.put("EditID", wAndonConfig.EditID);
			wParamMap.put("Editor", wAndonConfig.Editor);
			wParamMap.put("EditTime", wAndonConfig.EditTime);
			wParamMap.put("YHTS_C6", wAndonConfig.YHTS_C6);
			wParamMap.put("YHTS_C5", wAndonConfig.YHTS_C5);
			wParamMap.put("JCTS_C6", wAndonConfig.JCTS_C6);
			wParamMap.put("JCTS_C5", wAndonConfig.JCTS_C5);
			wParamMap.put("StopFlag", wAndonConfig.StopFlag);
			wParamMap.put("FGJS_One", wAndonConfig.FGJS_One);
			wParamMap.put("FGJS_Two", wAndonConfig.FGJS_Two);
			wParamMap.put("FGJS_Three", wAndonConfig.FGJS_Three);
			wParamMap.put("FGJS_Four", wAndonConfig.FGJS_Four);
			wParamMap.put("RepairFlag", wAndonConfig.RepairFlag);
			wParamMap.put("LJXJ_Year", wAndonConfig.LJXJ_Year);
			wParamMap.put("LJXJ_Year_C6", wAndonConfig.LJXJ_Year_C6);
			wParamMap.put("LJXJ_Year_C5", wAndonConfig.LJXJ_Year_C5);
			wParamMap.put("SJJF_Month", wAndonConfig.SJJF_Month);
			wParamMap.put("SJJF_Month_C6", wAndonConfig.SJJF_Month_C6);
			wParamMap.put("SJJF_Month_C5", wAndonConfig.SJJF_Month_C5);
			wParamMap.put("PlanFlag", wAndonConfig.PlanFlag);
			wParamMap.put("NotDone_One", wAndonConfig.NotDone_One);
			wParamMap.put("Done_One", wAndonConfig.Done_One);
			wParamMap.put("NotDone_Two", wAndonConfig.NotDone_Two);
			wParamMap.put("Done_Two", wAndonConfig.Done_Two);
			wParamMap.put("NotDone_Three", wAndonConfig.NotDone_Three);
			wParamMap.put("Done_Three", wAndonConfig.Done_Three);
			wParamMap.put("NotDone_Four", wAndonConfig.NotDone_Four);
			wParamMap.put("Done_Four", wAndonConfig.Done_Four);
			wParamMap.put("ProgressFlag", wAndonConfig.ProgressFlag);
			wParamMap.put("Total_Ncr", wAndonConfig.Total_Ncr);
			wParamMap.put("Doing_Ncr", wAndonConfig.Doing_Ncr);
			wParamMap.put("Total_Exc", wAndonConfig.Total_Exc);
			wParamMap.put("Doing_Exc", wAndonConfig.Doing_Exc);
			wParamMap.put("NcrExcFlag", wAndonConfig.NcrExcFlag);
			wParamMap.put("Grade_1", wAndonConfig.Grade_1);
			wParamMap.put("Grade_2", wAndonConfig.Grade_2);
			wParamMap.put("Grade_3", wAndonConfig.Grade_3);
			wParamMap.put("Grade_4", wAndonConfig.Grade_4);
			wParamMap.put("Grade_5", wAndonConfig.Grade_5);
			wParamMap.put("Grade_6", wAndonConfig.Grade_6);
			wParamMap.put("Grade_7", wAndonConfig.Grade_7);
			wParamMap.put("Grade_8", wAndonConfig.Grade_8);
			wParamMap.put("Grade_9", wAndonConfig.Grade_9);
			wParamMap.put("Grade_10", wAndonConfig.Grade_10);
			wParamMap.put("Grade_11", wAndonConfig.Grade_11);
			wParamMap.put("Grade_12", wAndonConfig.Grade_12);
			wParamMap.put("Grade_Quality", wAndonConfig.Grade_Quality);
			wParamMap.put("GradeFlag", wAndonConfig.GradeFlag);
			wParamMap.put("RotationTime", wAndonConfig.RotationTime);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			SqlParameterSource wSqlParameterSource = new MapSqlParameterSource(wParamMap);

			nameJdbcTemplate.update(wSQL, wSqlParameterSource, keyHolder);

			if (wAndonConfig.getID() <= 0) {
				wResult = keyHolder.getKey().intValue();
				wAndonConfig.setID(wResult);
			} else {
				wResult = wAndonConfig.getID();
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
	public ServiceResult<Integer> DeleteList(BMSEmployee wLoginUser, List<AndonConfig> wList,
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
			for (AndonConfig wItem : wList) {
				wIDList.add(String.valueOf(wItem.ID));
			}
			String wSql = MessageFormat.format("delete from {1}.andon_config WHERE ID IN({0}) ;",
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
	public AndonConfig SelectByID(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		AndonConfig wResult = new AndonConfig();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResult;
			}

			List<AndonConfig> wList = SelectList(wLoginUser, wID, wErrorCode);
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
	public List<AndonConfig> SelectList(BMSEmployee wLoginUser, int wID, OutResult<Integer> wErrorCode) {
		List<AndonConfig> wResultList = new ArrayList<AndonConfig>();
		try {
			ServiceResult<String> wInstance = this.GetDataBaseName(wLoginUser.getCompanyID(), MESDBSource.APS,
					wLoginUser.getID(), 0);
			wErrorCode.set(wInstance.ErrorCode);
			if (wErrorCode.Result != 0) {
				return wResultList;
			}

			String wSQL = MessageFormat.format(
					"SELECT * FROM {0}.andon_config WHERE  1=1  and ( :wID <= 0 or :wID = ID );", wInstance.Result);

			Map<String, Object> wParamMap = new HashMap<String, Object>();

			wParamMap.put("wID", wID);

			wSQL = this.DMLChange(wSQL);

			List<Map<String, Object>> wQueryResult = nameJdbcTemplate.queryForList(wSQL, wParamMap);

			for (Map<String, Object> wReader : wQueryResult) {
				AndonConfig wItem = new AndonConfig();

				wItem.ID = StringUtils.parseInt(wReader.get("ID"));
				wItem.YHTS_C6_ZB = StringUtils.parseInt(wReader.get("YHTS_C6_ZB"));
				wItem.YHTS_C5_ZB = StringUtils.parseInt(wReader.get("YHTS_C5_ZB"));
				wItem.JCTS_C6_ZB = StringUtils.parseInt(wReader.get("JCTS_C6_ZB"));
				wItem.JCTS_C5_ZB = StringUtils.parseInt(wReader.get("JCTS_C5_ZB"));
				wItem.FGJS_One_ZB = StringUtils.parseInt(wReader.get("FGJS_One_ZB"));
				wItem.FGJS_Two_ZB = StringUtils.parseInt(wReader.get("FGJS_Two_ZB"));
				wItem.FGJS_Three_ZB = StringUtils.parseInt(wReader.get("FGJS_Three_ZB"));
				wItem.FGJS_Four_ZB = StringUtils.parseInt(wReader.get("FGJS_Four_ZB"));
				wItem.JHJF_Month = StringUtils.parseInt(wReader.get("JHJF_Month"));
				wItem.JHJF_Month_C6 = StringUtils.parseInt(wReader.get("JHJF_Month_C6"));
				wItem.JHJF_Month_C5 = StringUtils.parseInt(wReader.get("JHJF_Month_C5"));
				wItem.Uri = StringUtils.parseString(wReader.get("Uri"));
				wItem.Type = StringUtils.parseInt(wReader.get("Type"));
				wItem.EditID = StringUtils.parseInt(wReader.get("EditID"));
				wItem.Editor = StringUtils.parseString(wReader.get("Editor"));
				wItem.EditTime = StringUtils.parseCalendar(wReader.get("EditTime"));
				wItem.YHTS_C6 = StringUtils.parseDouble(wReader.get("YHTS_C6"));
				wItem.YHTS_C5 = StringUtils.parseDouble(wReader.get("YHTS_C5"));
				wItem.JCTS_C6 = StringUtils.parseDouble(wReader.get("JCTS_C6"));
				wItem.JCTS_C5 = StringUtils.parseDouble(wReader.get("JCTS_C5"));

				wItem.YHTS_C6 = new BigDecimal(wItem.YHTS_C6).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				wItem.YHTS_C5 = new BigDecimal(wItem.YHTS_C5).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				wItem.JCTS_C6 = new BigDecimal(wItem.JCTS_C6).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				wItem.JCTS_C5 = new BigDecimal(wItem.JCTS_C5).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

				wItem.StopFlag = StringUtils.parseInt(wReader.get("StopFlag"));
				wItem.FGJS_One = StringUtils.parseInt(wReader.get("FGJS_One"));
				wItem.FGJS_Two = StringUtils.parseInt(wReader.get("FGJS_Two"));
				wItem.FGJS_Three = StringUtils.parseInt(wReader.get("FGJS_Three"));
				wItem.FGJS_Four = StringUtils.parseInt(wReader.get("FGJS_Four"));
				wItem.RepairFlag = StringUtils.parseInt(wReader.get("RepairFlag"));
				wItem.LJXJ_Year = StringUtils.parseInt(wReader.get("LJXJ_Year"));
				wItem.LJXJ_Year_C6 = StringUtils.parseInt(wReader.get("LJXJ_Year_C6"));
				wItem.LJXJ_Year_C5 = StringUtils.parseInt(wReader.get("LJXJ_Year_C5"));
				wItem.SJJF_Month = StringUtils.parseInt(wReader.get("SJJF_Month"));
				wItem.SJJF_Month_C6 = StringUtils.parseInt(wReader.get("SJJF_Month_C6"));
				wItem.SJJF_Month_C5 = StringUtils.parseInt(wReader.get("SJJF_Month_C5"));
				wItem.PlanFlag = StringUtils.parseInt(wReader.get("PlanFlag"));
				wItem.NotDone_One = StringUtils.parseInt(wReader.get("NotDone_One"));
				wItem.Done_One = StringUtils.parseInt(wReader.get("Done_One"));
				wItem.NotDone_Two = StringUtils.parseInt(wReader.get("NotDone_Two"));
				wItem.Done_Two = StringUtils.parseInt(wReader.get("Done_Two"));
				wItem.NotDone_Three = StringUtils.parseInt(wReader.get("NotDone_Three"));
				wItem.Done_Three = StringUtils.parseInt(wReader.get("Done_Three"));
				wItem.NotDone_Four = StringUtils.parseInt(wReader.get("NotDone_Four"));
				wItem.Done_Four = StringUtils.parseInt(wReader.get("Done_Four"));
				wItem.ProgressFlag = StringUtils.parseInt(wReader.get("ProgressFlag"));
				wItem.Total_Ncr = StringUtils.parseInt(wReader.get("Total_Ncr"));
				wItem.Doing_Ncr = StringUtils.parseInt(wReader.get("Doing_Ncr"));
				wItem.Total_Exc = StringUtils.parseInt(wReader.get("Total_Exc"));
				wItem.Doing_Exc = StringUtils.parseInt(wReader.get("Doing_Exc"));
				wItem.NcrExcFlag = StringUtils.parseInt(wReader.get("NcrExcFlag"));
				wItem.Grade_1 = StringUtils.parseInt(wReader.get("Grade_1"));
				wItem.Grade_2 = StringUtils.parseInt(wReader.get("Grade_2"));
				wItem.Grade_3 = StringUtils.parseInt(wReader.get("Grade_3"));
				wItem.Grade_4 = StringUtils.parseInt(wReader.get("Grade_4"));
				wItem.Grade_5 = StringUtils.parseInt(wReader.get("Grade_5"));
				wItem.Grade_6 = StringUtils.parseInt(wReader.get("Grade_6"));
				wItem.Grade_7 = StringUtils.parseInt(wReader.get("Grade_7"));
				wItem.Grade_8 = StringUtils.parseInt(wReader.get("Grade_8"));
				wItem.Grade_9 = StringUtils.parseInt(wReader.get("Grade_9"));
				wItem.Grade_10 = StringUtils.parseInt(wReader.get("Grade_10"));
				wItem.Grade_11 = StringUtils.parseInt(wReader.get("Grade_11"));
				wItem.Grade_12 = StringUtils.parseInt(wReader.get("Grade_12"));
				wItem.Grade_Quality = StringUtils.parseInt(wReader.get("Grade_Quality"));
				wItem.GradeFlag = StringUtils.parseInt(wReader.get("GradeFlag"));
				wItem.RotationTime = StringUtils.parseInt(wReader.get("RotationTime"));

				wResultList.add(wItem);
			}
		} catch (Exception ex) {
			wErrorCode.set(MESException.DBSQL.getValue());
			logger.error(ex.toString());
		}
		return wResultList;
	}

	private AndonConfigDAO() {
		super();
	}

	public static AndonConfigDAO getInstance() {
		if (Instance == null)
			Instance = new AndonConfigDAO();
		return Instance;
	}
}
