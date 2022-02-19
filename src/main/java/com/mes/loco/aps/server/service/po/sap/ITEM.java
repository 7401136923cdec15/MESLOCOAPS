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
public class ITEM implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * BOM ID 必填
	 */
	public int ID = 0;
	/**
	 * BOMItem ID 必填
	 */
	public int ITEMID = 0;
	/**
	 * 工位 必填
	 */
	public String USR00 = "";
	/**
	 * 仓库号 可选
	 */
	public String ZLGORT = "1200";
	/**
	 * 评估类型 可选
	 */
	public String BWTAR = "常规新件";
	/**
	 * 物料编号 必填
	 */
	public String MATNR = "";
	/**
	 * 客供料标识 可选
	 */
	public String ZFLAGKGL = "";
	/**
	 * 附件标志 可选
	 */
	public String ZFLAGFJ = "";
	/**
	 * 检修件分类 可选
	 */
	public String ZFLAGJXJ = "";
	/**
	 * 组合 WBS元素 可选
	 */
	public String GRPNR = "";
	/**
	 * 数量 必填
	 */
	public String MENGE = "";
	/**
	 * 基本计量单位 必填
	 */
	public String MEINS = "";
	/**
	 * 备注 可选
	 */
	public String ZREMARK = "";
	/**
	 * 定容号组 可选
	 */
	public String ZDRHH = "";
	/**
	 * 修复旧件标识 可选
	 */
	public String XFJJ = "";
	/**
	 * 领料数量 可选
	 */
	public String ZLLSL = "";
	/**
	 * 必换、偶换 可选
	 */
	public String ZBHOH = "";
	/**
	 * 偶换率 可选
	 */
	public String ZZDHL = "";
	/**
	 * 是否是互换件 可选
	 */
	public String ZSFHH = "";
	/**
	 * 委外必修 、委外偶修 可选
	 */
	public String ZZZWW = "";
	/**
	 * 原拆原装要求 可选
	 */
	public String ZYCYZ = "";
	/**
	 * 是否拆解下车 可选
	 */
	public String ZCJXC = "";
	/**
	 * 是否拆解下车 可选
	 */
	public String ZWTDW = "";
	/**
	 * 备注 可选
	 */
	public String ZZZBZ = "";
	/**
	 * 是否超修程 可选
	 */
	public String ZSCXC = "";
	/**
	 * 删除标识 可选
	 */
	public String ZDELE = "";
	/**
	 * 标准文本码 可选
	 */
	public String KTEXT = "";
	/**
	 * 领料部门 可选
	 */
	public String ZZFC = "";
	/**
	 * 质量损失大类 可选
	 */
	public String ZZLDL = "";
	/**
	 * 质量损失小类 可选
	 */
	public String ZZLXL = "";
	/**
	 * 工序描述
	 */
	public String KTTXT = "";
	/**
	 * 标识：反冲 可选
	 */
	public String RGEKZ = "";
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

	public ITEM() {
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setITEMID(int iTEMID) {
		ITEMID = iTEMID;
	}

	public void setUSR00(String uSR00) {
		USR00 = uSR00;
	}

	public void setZLGORT(String zLGORT) {
		ZLGORT = zLGORT;
	}

	public void setBWTAR(String bWTAR) {
		BWTAR = bWTAR;
	}

	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}

	public void setZFLAGKGL(String zFLAGKGL) {
		ZFLAGKGL = zFLAGKGL;
	}

	public void setZFLAGFJ(String zFLAGFJ) {
		ZFLAGFJ = zFLAGFJ;
	}

	public void setZFLAGJXJ(String zFLAGJXJ) {
		ZFLAGJXJ = zFLAGJXJ;
	}

	public void setGRPNR(String gRPNR) {
		GRPNR = gRPNR;
	}

	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}

	public void setMEINS(String mEINS) {
		MEINS = mEINS;
	}

	public void setZREMARK(String zREMARK) {
		ZREMARK = zREMARK;
	}

	public void setZDRHH(String zDRHH) {
		ZDRHH = zDRHH;
	}

	public void setXFJJ(String xFJJ) {
		XFJJ = xFJJ;
	}

	public void setZLLSL(String zLLSL) {
		ZLLSL = zLLSL;
	}

	public void setZBHOH(String zBHOH) {
		ZBHOH = zBHOH;
	}

	public void setZZDHL(String zZDHL) {
		ZZDHL = zZDHL;
	}

	public void setZSFHH(String zSFHH) {
		ZSFHH = zSFHH;
	}

	public void setZZZWW(String zZZWW) {
		ZZZWW = zZZWW;
	}

	public void setZYCYZ(String zYCYZ) {
		ZYCYZ = zYCYZ;
	}

	public void setZCJXC(String zCJXC) {
		ZCJXC = zCJXC;
	}

	public void setZWTDW(String zWTDW) {
		ZWTDW = zWTDW;
	}

	public void setZZZBZ(String zZZBZ) {
		ZZZBZ = zZZBZ;
	}

	public void setZSCXC(String zSCXC) {
		ZSCXC = zSCXC;
	}

	public void setZDELE(String zDELE) {
		ZDELE = zDELE;
	}

	public void setKTEXT(String kTEXT) {
		KTEXT = kTEXT;
	}

	public void setZZFC(String zZFC) {
		ZZFC = zZFC;
	}

	public void setZZLDL(String zZLDL) {
		ZZLDL = zZLDL;
	}

	public void setZZLXL(String zZLXL) {
		ZZLXL = zZLXL;
	}

	public void setKTTXT(String kTTXT) {
		KTTXT = kTTXT;
	}

	public void setRGEKZ(String rGEKZ) {
		RGEKZ = rGEKZ;
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
