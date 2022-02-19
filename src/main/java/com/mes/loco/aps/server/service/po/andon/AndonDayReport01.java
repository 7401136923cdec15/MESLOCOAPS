package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产日报第一页
 */
public class AndonDayReport01 implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 统计数据
	 */
	public AndonDayReportTJ TJData = new AndonDayReportTJ();
	/**
	 * 工位部分
	 */
	public AndonDayReportPart PartData = new AndonDayReportPart();
	/**
	 * C5在修车
	 */
	public List<AndonDayReportCar> C5RepairingList = new ArrayList<AndonDayReportCar>();
	/**
	 * C6在修车
	 */
	public List<AndonDayReportCar> C6RepairingList = new ArrayList<AndonDayReportCar>();
	/**
	 * 待回送机车
	 */
	public List<AndonDayReportCar> C5ToBackList = new ArrayList<AndonDayReportCar>();
	public List<AndonDayReportCar> C6ToBackList = new ArrayList<AndonDayReportCar>();

	public AndonDayReport01() {
		super();
	}

	public AndonDayReport01(AndonDayReportTJ tJData, AndonDayReportPart partData,
			List<AndonDayReportCar> c5RepairingList, List<AndonDayReportCar> c6RepairingList,
			List<AndonDayReportCar> c5ToBackList, List<AndonDayReportCar> c6ToBackList) {
		super();
		TJData = tJData;
		PartData = partData;
		C5RepairingList = c5RepairingList;
		C6RepairingList = c6RepairingList;
		C5ToBackList = c5ToBackList;
		C6ToBackList = c6ToBackList;
	}

	public AndonDayReportTJ getTJData() {
		return TJData;
	}

	public AndonDayReportPart getPartData() {
		return PartData;
	}

	public List<AndonDayReportCar> getC5RepairingList() {
		return C5RepairingList;
	}

	public List<AndonDayReportCar> getC6RepairingList() {
		return C6RepairingList;
	}

	public void setTJData(AndonDayReportTJ tJData) {
		TJData = tJData;
	}

	public void setPartData(AndonDayReportPart partData) {
		PartData = partData;
	}

	public void setC5RepairingList(List<AndonDayReportCar> c5RepairingList) {
		C5RepairingList = c5RepairingList;
	}

	public void setC6RepairingList(List<AndonDayReportCar> c6RepairingList) {
		C6RepairingList = c6RepairingList;
	}

	public List<AndonDayReportCar> getC5ToBackList() {
		return C5ToBackList;
	}

	public List<AndonDayReportCar> getC6ToBackList() {
		return C6ToBackList;
	}

	public void setC5ToBackList(List<AndonDayReportCar> c5ToBackList) {
		C5ToBackList = c5ToBackList;
	}

	public void setC6ToBackList(List<AndonDayReportCar> c6ToBackList) {
		C6ToBackList = c6ToBackList;
	}
}
