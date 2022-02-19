package com.mes.loco.aps.server.service.po.sap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SAP需求的台车bom输入子结构
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-12-29 19:26:39
 * @LastEditTime 2020-12-29 19:26:44
 *
 */
public class INPUT2 implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public String MODE = "";

	public HEAD HEAD = new HEAD();

	public List<ITEM> ITEM = new ArrayList<ITEM>();

	public INPUT2() {
	}

	public void setMODE(String mODE) {
		MODE = mODE;
	}

	public void setHEAD(HEAD hEAD) {
		HEAD = hEAD;
	}

	public void setITEM(List<ITEM> iTEM) {
		ITEM = iTEM;
	}
}
