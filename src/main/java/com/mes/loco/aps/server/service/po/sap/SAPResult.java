package com.mes.loco.aps.server.service.po.sap;

import java.io.Serializable;

/**
 * SAP返回信息
 * 
 * @author YouWang·Peng
 * @CreateTime 2021-7-22 15:37:08
 *
 */
public class SAPResult implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public String status = "";
	public String msg = "";

	public String getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
