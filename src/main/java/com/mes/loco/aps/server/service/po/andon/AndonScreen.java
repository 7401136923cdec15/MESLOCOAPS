package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.exc.EXCCallTaskBPM;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.mtc.MTCTask;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;

/**
 * 大屏实时信息
 */
public class AndonScreen implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public boolean Configuration = false;

	public boolean Business = false;

	/**
	 * 年度统计
	 */
	public AndonYearReport YearReport = new AndonYearReport();

	/**
	 * 订单行数据
	 */
	public List<OrderTimeInfo> TrainItemList = new ArrayList<OrderTimeInfo>();

	// 配置数据(台位)
	public List<AndonWorkspace> WorkspaceList = new ArrayList<AndonWorkspace>();
	// 配置数据(库位)
	public List<AndonStoreHouse> StoreHouseList = new ArrayList<AndonStoreHouse>();
	// 配置数据(车型)
	public List<AndonProduct> ProductList = new ArrayList<AndonProduct>();

	/**
	 * 异常信息
	 */
	public List<EXCCallTaskBPM> EXCCallTaskBPMList = new ArrayList<EXCCallTaskBPM>();
	/**
	 * 移车单
	 */
	public List<MTCTask> MTCTaskList = new ArrayList<MTCTask>();
	/**
	 * 台车状态
	 */
	public List<FMCWorkspace> FMCWorkspaceList = new ArrayList<FMCWorkspace>();

	public List<OMSOrder> OMSOrderList = new ArrayList<OMSOrder>();

	public boolean isConfiguration() {
		return Configuration;
	}

	public void setConfiguration(boolean configuration) {
		Configuration = configuration;
	}

	public boolean isBusiness() {
		return Business;
	}

	public void setBusiness(boolean business) {
		Business = business;
	}

	public AndonYearReport getYearReport() {
		return YearReport;
	}

	public void setYearReport(AndonYearReport yearReport) {
		YearReport = yearReport;
	}

	public List<AndonWorkspace> getWorkspaceList() {
		return WorkspaceList;
	}

	public void setWorkspaceList(List<AndonWorkspace> workspaceList) {
		WorkspaceList = workspaceList;
	}

	public List<AndonStoreHouse> getStoreHouseList() {
		return StoreHouseList;
	}

	public void setStoreHouseList(List<AndonStoreHouse> storeHouseList) {
		StoreHouseList = storeHouseList;
	}

	public List<AndonProduct> getProductList() {
		return ProductList;
	}

	public void setProductList(List<AndonProduct> productList) {
		ProductList = productList;
	}

	public List<OrderTimeInfo> getTrainItemList() {
		return TrainItemList;
	}

	public void setTrainItemList(List<OrderTimeInfo> trainItemList) {
		TrainItemList = trainItemList;
	}

	public List<EXCCallTaskBPM> getEXCCallTaskBPMList() {
		return EXCCallTaskBPMList;
	}

	public void setEXCCallTaskBPMList(List<EXCCallTaskBPM> eXCCallTaskBPMList) {
		EXCCallTaskBPMList = eXCCallTaskBPMList;
	}

	public List<MTCTask> getMTCTaskList() {
		return MTCTaskList;
	}

	public void setMTCTaskList(List<MTCTask> mTCTaskList) {
		MTCTaskList = mTCTaskList;
	}

	public List<FMCWorkspace> getFMCWorkspaceList() {
		return FMCWorkspaceList;
	}

	public void setFMCWorkspaceList(List<FMCWorkspace> fMCWorkspaceList) {
		FMCWorkspaceList = fMCWorkspaceList;
	}

	public List<OMSOrder> getOMSOrderList() {
		return OMSOrderList;
	}

	public void setOMSOrderList(List<OMSOrder> oMSOrderList) {
		OMSOrderList = oMSOrderList;
	}
}
