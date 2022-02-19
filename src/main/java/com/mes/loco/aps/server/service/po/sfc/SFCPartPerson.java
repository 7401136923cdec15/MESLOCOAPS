package com.mes.loco.aps.server.service.po.sfc;

import java.io.Serializable;

/**
 * 工位人员
 */
public class SFCPartPerson implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	public int PartID = 0;
	public int PersonID = 0;
	public String PartName = "";
	public String PersonName = "";

	public int getPartID() {
		return PartID;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public int getPersonID() {
		return PersonID;
	}

	public void setPersonID(int personID) {
		PersonID = personID;
	}

	public String getPartName() {
		return PartName;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public String getPersonName() {
		return PersonName;
	}

	public void setPersonName(String personName) {
		PersonName = personName;
	}
}
