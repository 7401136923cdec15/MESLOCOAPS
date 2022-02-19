package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;

/**
 * 工作时间点
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-19 09:43:10
 * @LastEditTime 2020-1-19 09:43:18
 *
 */
public class APSWorkHour implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public int MaxWorkHour = 17;

	public int MinWorkHour = 8;

	public int MiddleWorkHour = 12;

	public APSWorkHour() {
	}

	public int getMaxWorkHour() {
		return MaxWorkHour;
	}

	public void setMaxWorkHour(int maxWorkHour) {
		MaxWorkHour = maxWorkHour;
	}

	public int getMinWorkHour() {
		return MinWorkHour;
	}

	public void setMinWorkHour(int minWorkHour) {
		MinWorkHour = minWorkHour;
	}

	public int getMiddleWorkHour() {
		return MiddleWorkHour;
	}

	public void setMiddleWorkHour(int middleWorkHour) {
		MiddleWorkHour = middleWorkHour;
	}
}
