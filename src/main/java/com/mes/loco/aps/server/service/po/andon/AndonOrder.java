package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;

/**
 * 车辆实时信息
 */

public class AndonOrder implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	public int OrderID = 0;
	/**
	 * 车号(带#)
	 */
	public String PartNo = "";
	/**
	 * 工位ID
	 */
	public int PartID = 0;
	/**
	 * 工位名称
	 */
	public String PartName = "";
	/**
	 * 台位ID
	 */
	public int PlaceID = 0;
	/**
	 * 台位名称
	 */
	public String PlaceName = "";
	/**
	 * 库位ID
	 */
	public int StockID = 0;
	/**
	 * 库位名称
	 */
	public String StockName = "";

	public int getOrderID() {
		return OrderID;
	}

	public String getPartNo() {
		return PartNo;
	}

	public int getPartID() {
		return PartID;
	}

	public String getPartName() {
		return PartName;
	}

	public int getPlaceID() {
		return PlaceID;
	}

	public String getPlaceName() {
		return PlaceName;
	}

	public int getStockID() {
		return StockID;
	}

	public String getStockName() {
		return StockName;
	}

	public void setOrderID(int orderID) {
		OrderID = orderID;
	}

	public void setPartNo(String partNo) {
		PartNo = partNo;
	}

	public void setPartID(int partID) {
		PartID = partID;
	}

	public void setPartName(String partName) {
		PartName = partName;
	}

	public void setPlaceID(int placeID) {
		PlaceID = placeID;
	}

	public void setPlaceName(String placeName) {
		PlaceName = placeName;
	}

	public void setStockID(int stockID) {
		StockID = stockID;
	}

	public void setStockName(String stockName) {
		StockName = stockName;
	}
}
