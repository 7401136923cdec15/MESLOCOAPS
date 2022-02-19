package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 空闲时间段
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-7-22 20:25:13
 * @LastEditTime 2020-7-22 20:25:17
 *
 */
public class APSShiftDuration implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	public int ID = 0;
	public long ShiftS = 0;
	public long ShiftE = 0;
	public Calendar DateS = Calendar.getInstance();
	public Calendar DateE = Calendar.getInstance();

	public APSShiftDuration() {
	}

	public long getShiftS() {
		return ShiftS;
	}

	public void setShiftS(long shiftS) {
		ShiftS = shiftS;
	}

	public long getShiftE() {
		return ShiftE;
	}

	public void setShiftE(long shiftE) {
		ShiftE = shiftE;
	}

	public Calendar getDateS() {
		return DateS;
	}

	public void setDateS(Calendar dateS) {
		DateS = dateS;
	}

	public Calendar getDateE() {
		return DateE;
	}

	public void setDateE(Calendar dateE) {
		DateE = dateE;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
