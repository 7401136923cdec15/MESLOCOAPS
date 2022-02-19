package com.mes.loco.aps.server.service.po.sch;

import com.mes.loco.aps.server.service.po.sch.SCHSecondment;
import java.io.Serializable;
import java.util.Calendar;

public class SCHSecondment implements Serializable {
	private static final long serialVersionUID = 1L;
	public int ID;
	public int SendID;
	public String SendName = "";

	public Calendar SendTime = Calendar.getInstance();

	public int SecondDepartmentID;

	public String SecondDepartment = "";

	public boolean IsOverArea;

	public int AreaID;

	public String AreaName = "";

	public int SecondAuditID;

	public String SecondAuditor = "";
	public Calendar SendAuditTime = Calendar.getInstance();

	public int BeSecondAuditID;

	public String BeSecondAuditor = "";
	public Calendar BeSecondAuditTime = Calendar.getInstance();

	public int SecondPersonID;

	public String SecondPerson = "";

	public Calendar ValidDate = Calendar.getInstance();

	public int BeSecondDepartmentID;

	public String BeSecondDepartment = "";

	public int Status;

	public int IsExclude = 1;

	public Calendar ApplyValidDate = Calendar.getInstance();

	public SCHSecondment() {
		Calendar wBaseTime = Calendar.getInstance();
		wBaseTime.set(2000, 0, 1);

		this.SendTime = wBaseTime;
		this.SendAuditTime = wBaseTime;
		this.BeSecondAuditTime = wBaseTime;
		this.ValidDate = wBaseTime;
		this.ApplyValidDate = wBaseTime;
	}

	public int getID() {
		return this.ID;
	}

	public void setID(int iD) {
		this.ID = iD;
	}

	public int getAreaID() {
		return this.AreaID;
	}

	public void setAreaID(int areaID) {
		this.AreaID = areaID;
	}

	public String getAreaName() {
		return this.AreaName;
	}

	public Calendar getApplyValidData() {
		return this.ApplyValidDate;
	}

	public void setApplyValidData(Calendar applyValidData) {
		this.ApplyValidDate = applyValidData;
	}

	public void setAreaName(String areaName) {
		this.AreaName = areaName;
	}

	public int getSecondAuditID() {
		return this.SecondAuditID;
	}

	public void setSecondAuditID(int secondAuditID) {
		this.SecondAuditID = secondAuditID;
	}

	public String getSecondAuditor() {
		return this.SecondAuditor;
	}

	public void setSecondAuditor(String secondAuditor) {
		this.SecondAuditor = secondAuditor;
	}

	public int getBeSecondAuditID() {
		return this.BeSecondAuditID;
	}

	public void setBeSecondAuditID(int beSecondAuditID) {
		this.BeSecondAuditID = beSecondAuditID;
	}

	public String getBeSecondAuditor() {
		return this.BeSecondAuditor;
	}

	public void setBeSecondAuditor(String beSecondAuditor) {
		this.BeSecondAuditor = beSecondAuditor;
	}

	public int getBeSecondDepartmentID() {
		return this.BeSecondDepartmentID;
	}

	public void setBeSecondDepartmentID(int beSecondDepartmentID) {
		this.BeSecondDepartmentID = beSecondDepartmentID;
	}

	public String getBeSecondDepartment() {
		return this.BeSecondDepartment;
	}

	public void setBeSecondDepartment(String beSecondDepartment) {
		this.BeSecondDepartment = beSecondDepartment;
	}

	public Calendar getBeSecondAuditTime() {
		return this.BeSecondAuditTime;
	}

	public void setBeSecondAuditTime(Calendar beSeondAuditTime) {
		this.BeSecondAuditTime = beSeondAuditTime;
	}

	public int getSecondDepartmentID() {
		return this.SecondDepartmentID;
	}

	public void setSecondDepartmentID(int secondDepartmentID) {
		this.SecondDepartmentID = secondDepartmentID;
	}

	public String getSecondDepartment() {
		return this.SecondDepartment;
	}

	public void setSecondDepartment(String secondDepartment) {
		this.SecondDepartment = secondDepartment;
	}

	public int getSecondPersonID() {
		return this.SecondPersonID;
	}

	public int getSendID() {
		return this.SendID;
	}

	public void setSendID(int sendID) {
		this.SendID = sendID;
	}

	public String getSendName() {
		return this.SendName;
	}

	public void setSendName(String sendName) {
		this.SendName = sendName;
	}

	public void setSecondPersonID(int secondPersonID) {
		this.SecondPersonID = secondPersonID;
	}

	public String getSecondPerson() {
		return this.SecondPerson;
	}

	public void setSecondPerson(String secondPerson) {
		this.SecondPerson = secondPerson;
	}

	public Calendar getSendTime() {
		return this.SendTime;
	}

	public void setSendTime(Calendar sendTime) {
		this.SendTime = sendTime;
	}

	public int getStatus() {
		return this.Status;
	}

	public void setStatus(int status) {
		this.Status = status;
	}

	public boolean isIsOverArea() {
		return this.IsOverArea;
	}

	// \/\*\s*\d*\s*\*\/

	public void setIsOverArea(boolean isOverArea) {
		this.IsOverArea = isOverArea;
	}

	public Calendar getSendAuditTime() {
		return this.SendAuditTime;
	}

	public void setSendAuditTime(Calendar sendAuditTime) {
		this.SendAuditTime = sendAuditTime;
	}

	public Calendar getValidDate() {
		return this.ValidDate;
	}

	public void setValidDate(Calendar validDate) {
		this.ValidDate = validDate;
	}

	public int getIsExclude() {
		return this.IsExclude;
	}

	public void setIsExclude(int isExclude) {
		this.IsExclude = isExclude;
	}
}

