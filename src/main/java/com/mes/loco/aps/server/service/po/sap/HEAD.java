package com.mes.loco.aps.server.service.po.sap;

import java.io.Serializable;

/**
 * SAP需求的台车bom输入子结构
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-12-29 19:26:39
 * @LastEditTime 2020-12-29 19:26:44
 *
 */
public class HEAD implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * BOM ID必填
	 */
	public int ID = 0;
	/**
	 * BOM类型 必填
	 */
	public String ZSCLX = "";
	/**
	 * WBS要素 必填
	 */
	public String MAT_PSPNR = "";
	/**
	 * 车型 必填
	 */
	public String ZCHEX = "";
	/**
	 * 修程 必填
	 */
	public String ZXIUC = "";
	/**
	 * 局段信息 必填
	 */
	public String ZJDXX = "";
	/**
	 * 工厂 必填
	 */
	public String WERKS = "1900";
	/**
	 * 标准文本码 可选
	 */
	public String KTEXT = "";
	/**
	 * 工作中心 可选
	 */
	public String ARBPL = "";
	/**
	 * 领料部门 可选
	 */
	public String ZZFC = "";
	/**
	 * 删除标识 可选 X
	 */
	public String ZDELE = "";
	/**
	 * 创建日期 可选
	 */
	public String ERDAT = "";
	/**
	 * 创建时间 可选
	 */
	public String ERTIM = "";
	/**
	 * 创建者姓名 可选
	 */
	public String ERNAM = "";
	/**
	 * 更改日期 可选
	 */
	public String AEDAT = "";
	/**
	 * 更改时间 可选
	 */
	public String AETIM = "";
	/**
	 * 更改者姓名 可选
	 */
	public String AENAM = "";

	public HEAD() {
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setZSCLX(String zSCLX) {
		ZSCLX = zSCLX;
	}

	public void setMAT_PSPNR(String mAT_PSPNR) {
		MAT_PSPNR = mAT_PSPNR;
	}

	public void setZCHEX(String zCHEX) {
		ZCHEX = zCHEX;
	}

	public void setZXIUC(String zXIUC) {
		ZXIUC = zXIUC;
	}

	public void setZJDXX(String zJDXX) {
		ZJDXX = zJDXX;
	}

	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}

	public void setKTEXT(String kTEXT) {
		KTEXT = kTEXT;
	}

	public void setARBPL(String aRBPL) {
		ARBPL = aRBPL;
	}

	public void setZZFC(String zZFC) {
		ZZFC = zZFC;
	}

	public void setZDELE(String zDELE) {
		ZDELE = zDELE;
	}

	public void setERDAT(String eRDAT) {
		ERDAT = eRDAT;
	}

	public void setERTIM(String eRTIM) {
		ERTIM = eRTIM;
	}

	public void setERNAM(String eRNAM) {
		ERNAM = eRNAM;
	}

	public void setAEDAT(String aEDAT) {
		AEDAT = aEDAT;
	}

	public void setAETIM(String aETIM) {
		AETIM = aETIM;
	}

	public void setAENAM(String aENAM) {
		AENAM = aENAM;
	}
}
