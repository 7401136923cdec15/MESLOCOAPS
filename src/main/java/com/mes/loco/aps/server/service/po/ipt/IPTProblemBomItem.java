package com.mes.loco.aps.server.service.po.ipt;

import java.io.Serializable;

import com.mes.loco.aps.server.service.po.mss.MSSBOMItem;

/**
 * 问题项BOMItem
 * 
 * @author ShrisJava
 *
 */
public class IPTProblemBomItem implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 标准BOM子项
	 */
	public MSSBOMItem MSSBOMItem = new MSSBOMItem();
	/**
	 * 数量
	 */
	public int Number = 0;

	public MSSBOMItem getMSSBOMItem() {
		return MSSBOMItem;
	}

	public void setMSSBOMItem(MSSBOMItem mSSBOMItem) {
		MSSBOMItem = mSSBOMItem;
	}

	public int getNumber() {
		return Number;
	}

	public void setNumber(int number) {
		Number = number;
	}
}
