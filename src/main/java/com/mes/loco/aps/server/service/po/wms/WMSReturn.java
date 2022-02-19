package com.mes.loco.aps.server.service.po.wms;

import java.io.Serializable;

/**
 * WMS返回
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-11 10:41:01
 */
public class WMSReturn implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public String returnFlag = "";
	public String returnCode = "";
	public String returnDesc = "";
	public Object resultInfo = new Object();

	public WMSReturn() {
		super();
	}

	public WMSReturn(String returnFlag, String returnCode, String returnDeesc, Object resultInfo) {
		super();
		this.returnFlag = returnFlag;
		this.returnCode = returnCode;
		this.returnDesc = returnDeesc;
		this.resultInfo = resultInfo;
	}

	public String getReturnFlag() {
		return returnFlag;
	}

	public void setReturnFlag(String returnFlag) {
		this.returnFlag = returnFlag;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnDeesc() {
		return returnDesc;
	}

	public void setReturnDeesc(String returnDeesc) {
		this.returnDesc = returnDeesc;
	}

	public Object getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(Object resultInfo) {
		this.resultInfo = resultInfo;
	}
}
