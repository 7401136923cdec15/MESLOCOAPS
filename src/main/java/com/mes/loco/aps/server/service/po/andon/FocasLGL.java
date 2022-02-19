package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 台车订单实时列表
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-5-22 23:04:50
 * @LastEditTime 2020-1-19 09:43:18
 *
 */
public class FocasLGL implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 零公里得分
	 */
	public String lgl = "";
	/**
	 * 月份
	 */
	public String month = "";

	public FocasLGL() {
	}

	public String getLgl() {
		return lgl;
	}

	public String getMonth() {
		return month;
	}

	public void setLgl(String lgl) {
		this.lgl = lgl;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}
