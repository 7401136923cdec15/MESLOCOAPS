package com.mes.loco.aps.server.service.po.mcs;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 传输日志实体类
 */
public class MCSLogInfo implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 局段
	 */
	public String CustomerName = "";
	/**
	 * 修程
	 */
	public String LineName = "";
	/**
	 * 车型
	 */
	public String ProductNo = "";
	/**
	 * 车号
	 */
	public String PartNo = "";
	/**
	 * 版本信息
	 */
	public String VersionNo = "";
	/**
	 * 文件名称
	 */
	public String FileName = "";
	/**
	 * 文件路径
	 */
	public String FilePath = "";
	/**
	 * 文件类型
	 */
	public String FileType = "";
	/**
	 * 创建时间
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 创建时间文本
	 */
	public String CreateTimeStr = "";

	public MCSLogInfo() {
	}

	public MCSLogInfo(int iD, String customerName, String lineName, String productNo, String partNo, String versionNo,
			String fileName, String filePath, String fileType, Calendar createTime, String createTimeStr) {
		super();
		ID = iD;
		CustomerName = customerName;
		LineName = lineName;
		ProductNo = productNo;
		PartNo = partNo;
		VersionNo = versionNo;
		FileName = fileName;
		FilePath = filePath;
		FileType = fileType;
		CreateTime = createTime;
		CreateTimeStr = createTimeStr;
	}

	public String getFileName() {
		return FileName;
	}

	public String getFilePath() {
		return FilePath;
	}

	public String getFileType() {
		return FileType;
	}

	public Calendar getCreateTime() {
		return CreateTime;
	}

	public String getCreateTimeStr() {
		return CreateTimeStr;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public void setFilePath(String filePath) {
		FilePath = filePath;
	}

	public void setFileType(String fileType) {
		FileType = fileType;
	}

	public void setCreateTime(Calendar createTime) {
		CreateTime = createTime;
	}

	public void setCreateTimeStr(String createTimeStr) {
		CreateTimeStr = createTimeStr;
	}

	public int getID() {
		return ID;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public String getLineName() {
		return LineName;
	}

	public String getProductNo() {
		return ProductNo;
	}

	public String getPartNo() {
		return PartNo;
	}

	public String getVersionNo() {
		return VersionNo;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public void setLineName(String lineName) {
		LineName = lineName;
	}

	public void setProductNo(String productNo) {
		ProductNo = productNo;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public void setVersionNo(String versionNo) {
		VersionNo = versionNo;
	}
}
