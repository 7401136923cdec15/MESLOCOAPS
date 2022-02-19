package com.mes.loco.aps.server.service.po.ipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 表单值
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-1-13 09:58:19
 * @LastEditTime 2020-4-9 22:07:59
 *
 */
public class IPTValue implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public long ID;

	public long StandardID;

	public long IPTItemID;

	public String Value;

	public String Remark;

	public int Result;

	// ---------广机特有-----------
	public int TaskID = 0;
	public int IPTMode = 0;

	public int ItemType = 0;
	public List<String> ImagePath = new ArrayList<String>();
	public List<String> VideoPath = new ArrayList<String>();
	public int SolveID = 0;
	public int SubmitID = 0;
	public String Submitor = "";
	public Calendar SubmitTime = Calendar.getInstance();

	// 20200506新增
	/**
	 * 厂家
	 */
	public String Manufactor = "";
	/**
	 * 型号
	 */
	public String Modal = "";
	/**
	 * 编号
	 */
	public String Number = "";
	/**
	 * 提交状态(1保存、2提交)
	 */
	public int Status = 0;

	public IPTValue() {
		Value = "";
		Result = 1;
		Remark = "";
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getTaskID() {
		return TaskID;
	}

	public String getManufactor() {
		return Manufactor;
	}

	public void setManufactor(String manufactor) {
		Manufactor = manufactor;
	}

	public String getModal() {
		return Modal;
	}

	public void setModal(String modal) {
		Modal = modal;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public void setTaskID(int taskID) {
		TaskID = taskID;
	}

	public int getIPTMode() {
		return IPTMode;
	}

	public void setIPTMode(int iPTMode) {
		IPTMode = iPTMode;
	}

	public long getStandardID() {
		return StandardID;
	}

	public void setStandardID(long standardID) {
		StandardID = standardID;
	}

	public long getIPTItemID() {
		return IPTItemID;
	}

	public void setIPTItemID(long iPTItemID) {
		IPTItemID = iPTItemID;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public int getResult() {
		return Result;
	}

	public void setResult(int result) {
		Result = result;
	}

	public int getItemType() {
		return ItemType;
	}

	public void setItemType(int itemType) {
		ItemType = itemType;
	}

	public List<String> getImagePath() {
		return ImagePath;
	}

	public void setImagePath(List<String> imagePath) {
		ImagePath = imagePath;
	}

	public List<String> getVideoPath() {
		return VideoPath;
	}

	public void setVideoPath(List<String> videoPath) {
		VideoPath = videoPath;
	}

	public int getSolveID() {
		return SolveID;
	}

	public void setSolveID(int solveID) {
		SolveID = solveID;
	}

	public int getSubmitID() {
		return SubmitID;
	}

	public void setSubmitID(int submitID) {
		SubmitID = submitID;
	}

	public String getSubmitor() {
		return Submitor;
	}

	public void setSubmitor(String submitor) {
		Submitor = submitor;
	}

	public Calendar getSubmitTime() {
		return SubmitTime;
	}

	public void setSubmitTime(Calendar submitTime) {
		SubmitTime = submitTime;
	}
}