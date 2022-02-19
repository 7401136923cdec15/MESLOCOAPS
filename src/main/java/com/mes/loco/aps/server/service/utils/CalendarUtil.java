package com.mes.loco.aps.server.service.utils;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calendar工具类
 * 
 * @author PengYouWang
 * @CreateTime 2020年1月4日14:37:59
 * @LastEditTime 2020年1月4日14:38:05
 *
 */
public class CalendarUtil {

	private static Logger logger = LoggerFactory.getLogger(CalendarUtil.class);

	private CalendarUtil() {
	}

	/**
	 * 获取日期部分
	 * 
	 * @param wCalenar
	 * @return
	 */
	public static Calendar GetDate(Calendar wCalenar) {
		Calendar wResult = Calendar.getInstance();
		try {
			int wYear = wCalenar.get(Calendar.YEAR);
			int wMonth = wCalenar.get(Calendar.MONTH);
			int wDate = wCalenar.get(Calendar.DATE);
			int wHour = 0;
			int wMinute = 0;
			int wSecond = 0;
			wResult.set(wYear, wMonth, wDate, wHour, wMinute, wSecond);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 获取日期间隔天数
	 * 
	 * @param wStart
	 * @param wEnd
	 * @return
	 */
	public static int getDayInterval(Date wStart, Date wEnd) {
		int wResult = 0;
		try {
			final long wNd = 1000 * 24 * 60 * 60;
			Date wStarDay = new Date(wStart.getTime() - wStart.getTime() % wNd);
			Date wEndDay = new Date(wEnd.getTime() - wEnd.getTime() % wNd);
			wResult = (int) ((wEndDay.getTime() - wStarDay.getTime()) / wNd);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}
}
