package com.mes.loco.aps.server.service.po.sfc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 派工默认人员
 * 
 * @author ShrisJava
 *
 */
public class SFCDefaultPerson implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 车型ID
	 */
	public int ProductID = 0;
	/**
	 * 修程
	 */
	public int LineID = 0;
	/**
	 * 工位
	 */
	public int PartID = 0;
	/**
	 * 工序
	 */
	public int PartPointID = 0;
	/**
	 * 默认人员
	 */
	public List<Integer> PersonIDList = new ArrayList<Integer>();

	public SFCDefaultPerson() {
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getLineID() {
		return LineID;
	}

	public void setLineID(int lineID) {
		LineID = lineID;
	}

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public int getPartPointID() {
		return PartPointID;
	}

	public void setPartPointID(int partPointID) {
		PartPointID = partPointID;
	}

	public List<Integer> getPersonIDList() {
		return PersonIDList;
	}

	public void setPersonIDList(List<Integer> personIDList) {
		PersonIDList = personIDList;
	}

	public int getProductID() {
		return ProductID;
	}

	public void setProductID(int productID) {
		ProductID = productID;
	}
}
