package com.mes.loco.aps.server.service.po.sap;

import java.io.Serializable;

/**
 * SAP需求的台车bom输入结构
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-12-29 19:26:39
 * @LastEditTime 2020-12-29 19:26:44
 *
 */
public class APSBomData implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public String INPUT1 = "01";

	public INPUT2 INPUT2 = new INPUT2();

	public APSBomData() {
	}

	public void setINPUT1(String iNPUT1) {
		INPUT1 = iNPUT1;
	}

	public void setINPUT2(INPUT2 iNPUT2) {
		INPUT2 = iNPUT2;
	}
}
