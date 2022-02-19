package com.mes.loco.aps.server.shristool;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.mcs.MCSLogInfo;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.mcs.MCSLogInfoDAO;
import com.mes.loco.aps.server.utils.Constants;

public class CalendarDAO {
	private static CalendarDAO Instance;

	private static Logger logger = LoggerFactory.getLogger(CalendarDAO.class);

	private CalendarDAO() {
		super();
	}

	public static CalendarDAO getInstance() {
		if (Instance == null)
			Instance = new CalendarDAO();
		return Instance;
	}

	//
	public Calendar getDate(Calendar wCalendar, int wDays) {
		Calendar wDate = (Calendar) wCalendar.clone();
		wDate.add(Calendar.DATE, wDays);
		wDate.set(Calendar.HOUR_OF_DAY, 0);
		wDate.set(Calendar.MINUTE, 0);
		wDate.set(Calendar.SECOND, 0);
		wDate.set(Calendar.MILLISECOND, 0);
		return wDate;
	}

	// new DateTime(2016, 1, 1) CalendarDAO.getInstance().setDate(2016, 1, 1)
	public Calendar setDate(int Year, int wMonth, int wDays) {
		Calendar wDate = Calendar.getInstance();

		wDate.set(Year, wMonth, wDays, 0, 0, 0);
		wDate.set(Calendar.MILLISECOND, 0);
		return wDate;
	}

	public Calendar getDate() {
		Calendar wDate = Calendar.getInstance();
		wDate.set(Calendar.HOUR_OF_DAY, 0);
		wDate.set(Calendar.MINUTE, 0);
		wDate.set(Calendar.SECOND, 0);
		wDate.set(Calendar.MILLISECOND, 0);
		return wDate;
	}

	public int subBySecond(Calendar wCalendar1, Calendar wCalendar2) {
		int wSeconds = 0;
		long wMillis1 = wCalendar1.getTimeInMillis();
		long wMillis2 = wCalendar2.getTimeInMillis();

		wSeconds = (int) ((wMillis1 - wMillis2) / 1000);

		return wSeconds;
	}

	/**
	 * 写入字符串到文件
	 */
	public void WriteStringToFile(String wFilePath, String wContent) {
		try {
			FileOutputStream wFos = new FileOutputStream(wFilePath);
			wFos.write(wContent.getBytes());
			wFos.close();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 数据源文件写入
	 */
	public void WriteLogFile(String wContent, String wTitle, OMSOrder wOrder) {
		try {
			// ①创建目录
			String wDirePath = StringUtils.Format("{0}static/datasource/",
					new Object[] { Constants.getConfigPath().replace("config/", "") });
			File wDirFile = new File(wDirePath);
			if (!wDirFile.exists()) {
				wDirFile.mkdirs();
			}
			// ②创建文件路径
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMddHHmmss");
			String wTimestamp = wSDF.format(Calendar.getInstance().getTime());

			UUID wUUID = UUID.randomUUID();
			wTimestamp = wUUID.toString().replace("-", "");

			String wFilePath = StringUtils.Format("{0}{1}{2}.xml", wDirePath, wTitle, wTimestamp);
			// ③写入文件
			WriteStringToFile(wFilePath, wContent);
			// ④写入数据库
			WriteToDB(wTitle, wTimestamp, wFilePath, wOrder);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 传输日志记录在数据库
	 */
	private void WriteToDB(String wTitle, String wTimestamp, String wFilePath, OMSOrder wOrder) {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);

			String wFileName = StringUtils.Format("{0}{1}.xml", wTitle, wTimestamp);
			String wFileType = "";
			if (wTitle.equals("台车bom")) {
				wFileType = "SAP台车BOM";
			} else if (wTitle.contains("台车WBS")) {
				wFileType = "SAP台车WBS";
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wCurrentTime = wSDF.format(Calendar.getInstance().getTime());

			MCSLogInfo wInfo = new MCSLogInfo(0, wOrder.Customer, wOrder.LineName, wOrder.ProductNo, wOrder.PartNo, "",
					wFileName, wFilePath, wFileType, Calendar.getInstance(), wCurrentTime);
			MCSLogInfoDAO.getInstance().Update(BaseDAO.SysAdmin, wInfo, wErrorCode);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
}
