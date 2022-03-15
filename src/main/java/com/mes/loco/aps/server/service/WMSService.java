package com.mes.loco.aps.server.service;

import java.util.Calendar;
import java.util.List;

import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSBOMItem;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.mss.MSSBOM;
import com.mes.loco.aps.server.service.po.mss.MSSBOMItem;
import com.mes.loco.aps.server.service.po.mss.MSSMaterial;
import com.mes.loco.aps.server.service.po.wms.WMSPickDemand;
import com.mes.loco.aps.server.service.utils.Configuration;

public interface WMSService {

	static String ServerUrl = Configuration.readConfigString("wms.server.url", "config/config");

	static String ServerName = Configuration.readConfigString("wms.server.project.name", "config/config");

	APIResult MSS_QueryBOM(BMSEmployee wLoginUser, int wBOMID, String wBOMNo, int wRouteIDF);

	APIResult MSS_SaveBOM(BMSEmployee wLoginUser, MSSBOM wMSSBOM);

	APIResult MSS_SaveBOMItem(BMSEmployee wLoginUser, MSSBOMItem wMSSBOMItem);

	APIResult MSS_QueryBOMAll(BMSEmployee wLoginUser, String wName, String wBOMNo, int wWorkShopID, int wBOMType,
			int wProductID, int wStatus);

	APIResult MSS_QueryBOMItemAll(BMSEmployee wLoginUser, int wBOMID, int wLineID, int wProductID, int wCustomerID,
			int wPlaceID, int wPartPointID, int wBOMType, int wReplaceType, int wRouteID);

	APIResult MSS_QueryBOMItemByID(BMSEmployee wLoginUser, int wBOMItemID);

	APIResult MSS_QueryUnitList(BMSEmployee wLoginUser);

	APIResult MSS_QueryMaterialList(BMSEmployee wLoginUser, String wMaterialNo);

	APIResult MSS_QueryMaterialByID(BMSEmployee wLoginUser, int wMaterialID);

	APIResult MSS_SaveMaterial(BMSEmployee wLoginUser, MSSMaterial wMSSMaterial);

	APIResult APS_SaveBOMItemList(BMSEmployee wLoginUser, List<APSBOMItem> wAPSBOMItemList);

	APIResult MSS_ConfigAll(BMSEmployee wLoginUser, String wPartConfigNo, String wPartConfigName, int wActive,
			String wProductNo, int wCustomerID, int wLineID);

	APIResult APS_OrderStart(BMSEmployee wLoginUser, int wLineID, int wProductID, int wCustomerID, int wOrderID,
			String wWBSNo, String wPartNo);

	APIResult APS_BOMItemUpdate(BMSEmployee wLoginUser, APSBOMItem wAPSBOMItem);

	ServiceResult<List<WMSPickDemand>> WMS_QueryPickDemandList(BMSEmployee wLoginUser, String wOrderType,
			String wDemandNo, int wProductID, int wLineID, int wCustomerID, int wOrderID, int wPartID, int wStatus,
			String wMaterial, Calendar wStartTime, Calendar wEndTime);

	ServiceResult<WMSPickDemand> WMS_QueryPickDemand(BMSEmployee wLoginUser, int wDemandID);

	ServiceResult<Integer> WMS_TriggerPickDemandTask(BMSEmployee wLoginUser, int wOrderID, int wPartID);

	/**
	 * 手动推送
	 */
	ServiceResult<Integer> WMS_ManualPush(BMSEmployee wLoginUser, int wDemandID);
}
