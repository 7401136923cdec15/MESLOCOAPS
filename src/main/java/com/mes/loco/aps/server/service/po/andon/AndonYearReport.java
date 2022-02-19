package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

public class AndonYearReport implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 全年修浚数
	 */
	public int Year = 0;
	/**
	 * 全年修浚数
	 */
	public int AnnualNum = 0;
	/**
	 * 全年C5修浚
	 */
	public int AnnualC5Num = 0;
	/**
	 * 全年C6修竣
	 */
	public int AnnualC6Num = 0;
	/**
	 * 本月修竣
	 */
	public int ThisMonthNum = 0;
	/**
	 * 在场机车
	 */
	public int LocoCar = 0;
	/**
	 * 在修机车
	 */
	public int RepairCar = 0;
	/**
	 * 工位待完工任务
	 */
	public int stationTask = 0;
	/**
	 * 今日异常记录
	 */
	public int ExcTask = 0;
	/**
	 * 今日不合格评审数
	 */
	public int ncrTask = 0;
	/**
	 * 今日返修数
	 */
	public int repairTask = 0;

	public AndonYearReport() {
	}

	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public int getAnnualNum() {
		return AnnualNum;
	}

	public void setAnnualNum(int annualNum) {
		AnnualNum = annualNum;
	}

	public int getAnnualC5Num() {
		return AnnualC5Num;
	}

	public void setAnnualC5Num(int annualC5Num) {
		AnnualC5Num = annualC5Num;
	}

	public int getAnnualC6Num() {
		return AnnualC6Num;
	}

	public void setAnnualC6Num(int annualC6Num) {
		AnnualC6Num = annualC6Num;
	}

	public int getThisMonthNum() {
		return ThisMonthNum;
	}

	public void setThisMonthNum(int thisMonthNum) {
		ThisMonthNum = thisMonthNum;
	}

	public int getLocoCar() {
		return LocoCar;
	}

	public void setLocoCar(int locoCar) {
		LocoCar = locoCar;
	}

	public int getRepairCar() {
		return RepairCar;
	}

	public void setRepairCar(int repairCar) {
		RepairCar = repairCar;
	}

	public int getStationTask() {
		return stationTask;
	}

	public void setStationTask(int stationTask) {
		this.stationTask = stationTask;
	}

	public int getExcTask() {
		return ExcTask;
	}

	public void setExcTask(int excTask) {
		ExcTask = excTask;
	}

	public int getNcrTask() {
		return ncrTask;
	}

	public void setNcrTask(int ncrTask) {
		this.ncrTask = ncrTask;
	}

	public int getRepairTask() {
		return repairTask;
	}

	public void setRepairTask(int repairTask) {
		this.repairTask = repairTask;
	}

}
