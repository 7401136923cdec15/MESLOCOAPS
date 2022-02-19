package com.mes.loco.aps.server.controller.aps;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mes.loco.aps.server.controller.BaseController;
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.SFCService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSBOMItem;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.excel.ExcelData;
import com.mes.loco.aps.server.service.po.mss.MSSBOMItem;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.sfc.SFCBOMTask;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;
import com.mes.loco.aps.server.utils.aps.ExcelReader;

@RestController
@RequestMapping("/api/APSBOM")
public class APSBOMItemController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(APSBOMItemController.class);

	@Autowired
	APSService wAPSService;

	@Autowired
	SFCService wSFCService;

	/**
	 * 条件查询台车BOMM
	 */
	@GetMapping("/All")
	public Object All(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wBMSEmployee = GetSession(request);

			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			String wWBSNo = StringUtils.parseString(request.getParameter("WBSNo"));
			String wPartNo = StringUtils.parseString(request.getParameter("PartNo"));
			int wLineID = StringUtils.parseInt(request.getParameter("LineID"));
			int wProductID = StringUtils.parseInt(request.getParameter("ProductID"));
			int wCustomerID = StringUtils.parseInt(request.getParameter("CustomerID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wPartPointID = StringUtils.parseInt(request.getParameter("PartPointID"));
			int wMaterialID = StringUtils.parseInt(request.getParameter("MaterialID"));
			String wMaterialNo = StringUtils.parseString(request.getParameter("MaterialNo"));
			int wBOMType = StringUtils.parseInt(request.getParameter("BOMType"));
			int wReplaceType = StringUtils.parseInt(request.getParameter("ReplaceType"));
			int wOutsourceType = StringUtils.parseInt(request.getParameter("OutsourceType"));
			String wStatus = StringUtils.parseString(request.getParameter("Status"));

			int wDifferenceItem = StringUtils.parseInt(request.getParameter("DifferenceItem"));
			int wOverQuota = StringUtils.parseInt(request.getParameter("OverQuota"));
			int wSourceType = StringUtils.parseInt(request.getParameter("SourceType"));

			ServiceResult<List<APSBOMItem>> wServiceResult = wAPSService.APS_QueryBOMItemList(wBMSEmployee, wOrderID,
					wWBSNo, wPartNo, wLineID, wProductID, wCustomerID, wPartID, wPartPointID, wMaterialID, wMaterialNo,
					wBOMType, wReplaceType, wOutsourceType, StringUtils.parseIntList(wStatus.split(",")),
					wDifferenceItem, wOverQuota, wSourceType);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.getResult(), null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode(), wServiceResult.getResult(),
						null);
			}
		} catch (Exception ex) {
			logger.error("APSBOMItemController All  Error:", ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@GetMapping("/Info")
	public Object Info(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wBMSEmployee = GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));

			ServiceResult<APSBOMItem> wServiceResult = wAPSService.APS_QueryBOMItemByID(wBMSEmployee, wID);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.getResult());
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode(), null,
						wServiceResult.getResult());
			}
		} catch (Exception ex) {
			logger.error("APSBOMItemController Info  Error:", ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping("/Create")
	public Object Create(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wBMSEmployee = GetSession(request);

			if (!wParam.containsKey("data") || !wParam.containsKey("RouteID")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}
			int wRouteID = StringUtils.parseInt(wParam.get("RouteID"));
			List<APSBOMItem> wBOMItemList = CloneTool.CloneArray(wParam.get("data"), APSBOMItem.class);

			ServiceResult<List<APSBOMItem>> wServiceResult = wAPSService.APS_CreateBOMItem(wBMSEmployee, wRouteID,
					wBOMItemList);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.getResult(), null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode(), wServiceResult.getResult(),
						null);
			}
		} catch (Exception ex) {
			logger.error("APSBOMItemController Create Error:", ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 根据订单创建台车BOM
	 */
	@GetMapping("/CreateByOrder")
	public Object CreateByOrder(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_CreateBomItemByOrder(wLoginUser, wOrderID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 对比出不存在的必换件、必修件，推送到SAP
	 */
	@GetMapping("/AddAPSBOMToSAP")
	public Object AddAPSBOMToSAP(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wBOMID = StringUtils.parseInt(request.getParameter("BOMID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_AddAPSBOMToSAP(wLoginUser, wBOMID, wOrderID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取订单列表 (提示有无台车BOM)
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/OrderList")
	public Object OrderList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			List<Integer> wCustomerList = CloneTool.CloneArray(wParam.get("CustomerList"), Integer.class);
			List<Integer> wLineList = CloneTool.CloneArray(wParam.get("LineList"), Integer.class);
			Calendar wStartTime = StringUtils.parseCalendar(wParam.get("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(wParam.get("EndTime"));
			List<Integer> wStatusList = CloneTool.CloneArray(wParam.get("StatusList"), Integer.class);
			List<Integer> wProductList = CloneTool.CloneArray(wParam.get("ProductList"), Integer.class);
			String wPartNo = StringUtils.parseString(wParam.get("PartNo"));
			List<Integer> wActiveList = CloneTool.CloneArray(wParam.get("ActiveList"), Integer.class);

			ServiceResult<List<OMSOrder>> wServiceResult = wAPSService.APS_QueryOrderBOMList(wLoginUser, wCustomerList,
					wLineList, wStatusList, wProductList, wPartNo, wActiveList, wStartTime, wEndTime);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping("/OrderStart")
	public Object OrderStart(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wBMSEmployee = GetSession(request);

			if (!wParam.containsKey("LineID") || !wParam.containsKey("ProductID") || !wParam.containsKey("CustomerID")
					|| !wParam.containsKey("RouteID") || !wParam.containsKey("OrderID") || !wParam.containsKey("WBSNo")
					|| !wParam.containsKey("PartNo")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			int wLineID = StringUtils.parseInt(wParam.get("LineID"));
			int wRouteID = StringUtils.parseInt(wParam.get("RouteID"));
			int wProductID = StringUtils.parseInt(wParam.get("ProductID"));
			int wCustomerID = StringUtils.parseInt(wParam.get("CustomerID"));
			int wOrderID = StringUtils.parseInt(wParam.get("OrderID"));
			String wWBSNo = StringUtils.parseString(wParam.get("WBSNo"));
			String wPartNo = StringUtils.parseString(wParam.get("PartNo"));

			ServiceResult<List<APSBOMItem>> wServiceResult = wAPSService.APS_CreateBOMItem(wBMSEmployee, wRouteID,
					wLineID, wProductID, wCustomerID, wOrderID, wWBSNo, wPartNo);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.getResult(), null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode(), wServiceResult.getResult(),
						null);
			}
		} catch (Exception ex) {
			logger.error("APSBOMItemController Create Error:", ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping("/Update")
	public Object Update(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wBMSEmployee = GetSession(request);

			if (!wParam.containsKey("data")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			APSBOMItem wBOMItem = CloneTool.Clone(wParam.get("data"), APSBOMItem.class);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_UpdateBOMItem(wBMSEmployee, wBOMItem);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wBOMItem);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode(), null, wBOMItem);
			}
		} catch (Exception ex) {
			logger.error("APSBOMItemController Update Error:", ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 批量保存台车bom，同时同步到erp系统
	 */
	@PostMapping("/UpdateList")
	public Object UpdateList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			List<APSBOMItem> wDataList = CloneTool.CloneArray(wParam.get("data"), APSBOMItem.class);
			int wSFCBOMTaskID = StringUtils.parseInt(wParam.get("SFCBOMTaskID"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_UpdateBOMItemList(wLoginUser, wDataList,
					wSFCBOMTaskID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping("/Audit")
	public Object Audit(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

//			BMSEmployee wBMSEmployee = GetSession(request);

//			if (!wParam.containsKey("data")) {
//				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
//				return wResult;
//			}
//			APSBOMItem wBOMItem = CloneTool.Clone(wParam.get("data"), APSBOMItem.class);
//
//			ServiceResult<Integer> wServiceResult = wAPSService.APS_AuditBOMItem(wBMSEmployee, wBOMItem);
//
//			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
//				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wBOMItem);
//			} else {
//				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode(), null, wBOMItem);
//			}
		} catch (Exception ex) {
			logger.error("APSBOMItemController Audit Error:", ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping("/Delete")
	public Object Delete(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wBMSEmployee = GetSession(request);

			if (!wParam.containsKey("data")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}
			APSBOMItem wBOMItem = CloneTool.Clone(wParam.get("data"), APSBOMItem.class);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_DeleteBOMItem(wBMSEmployee, wBOMItem);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wBMSEmployee);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode(), null, wBMSEmployee);
			}
		} catch (Exception ex) {
			logger.error("APSBOMItemController Update Error:", ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping("/UpdateProperty")
	public Object UpdateProperty(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wBMSEmployee = GetSession(request);

			if (!wParam.containsKey("data")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}
			APSBOMItem wBOMItem = CloneTool.Clone(wParam.get("data"), APSBOMItem.class);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_BOMItemUpdateProperty(wBMSEmployee, wBOMItem);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wBMSEmployee);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode(), null, wBMSEmployee);
			}
		} catch (Exception ex) {
			logger.error("APSBOMItemController Update Error:", ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 查询需要删除的台车BOM集合
	 */
	@GetMapping("/QueryDeleteList")
	public Object QueryDeleteList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			String wMaterialNos = StringUtils.parseString(request.getParameter("MaterialNos"));

			ServiceResult<List<APSBOMItem>> wServiceResult = wAPSService.APS_QueryQueryDeleteBomItemList(wLoginUser,
					wOrderID, wPartID, wMaterialNos);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 手动推送偶换件的台车BOM到SAP和MES
	 */
	@GetMapping("/BOMTaskToSAP")
	public Object BOMTaskToSAP(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wBOMTaskID = StringUtils.parseInt(request.getParameter("BOMTaskID"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_BOMTaskToSAP(wLoginUser, wBOMTaskID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "手动推送成功!", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 手动推送标准BOM(新增的)，新增台车BOM到SAP
	 */
	@PostMapping("/SendToSAP")
	public Object SendToSAP(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			List<MSSBOMItem> wDataList = CloneTool.CloneArray(wParam.get("data"), MSSBOMItem.class);

			ServiceResult<String> wServiceResult = wAPSService.APS_SendMoreToSAP(wLoginUser, wDataList);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 导入台车BOM
	 */
	@PostMapping("/ImportAPSBOM")
	public Object ImportAPSBOM(HttpServletRequest request, @RequestParam("file") MultipartFile[] files,
			@RequestParam("OrderID") int wOrderID) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			if (files.length == 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：没有要上传的Excel文件！");
			}

			ServiceResult<String> wServiceResult = new ServiceResult<String>();
			ServiceResult<ExcelData> wExcelData = null;
			String wOriginalFileName = null;
			for (MultipartFile wMultipartFile : files) {
				wOriginalFileName = wMultipartFile.getOriginalFilename();

				if (wOriginalFileName.contains("xlsx") || wOriginalFileName.contains("XLSX")) {
					wExcelData = ExcelReader.getInstance().readMultiSheetExcel(wMultipartFile.getInputStream(),
							wOriginalFileName, "xlsx", 1000000);
				} else if (wOriginalFileName.contains("xls") || wOriginalFileName.contains("XLS")) {
					wExcelData = ExcelReader.getInstance().readMultiSheetExcel(wMultipartFile.getInputStream(),
							wOriginalFileName, "xls", 1000000);
				}

				if (StringUtils.isNotEmpty(wExcelData.FaultCode)) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, wExcelData.FaultCode);
					return wResult;
				}

				wServiceResult = wAPSService.IPT_ImportAPSBOM(wLoginUser, wExcelData.Result, wOriginalFileName,
						wOrderID);

				if (!StringUtils.isEmpty(wServiceResult.FaultCode))
					break;
			}

			if (StringUtils.isEmpty(wServiceResult.Result)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "导入成功!", null, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.Result);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 根据订单，推送，偶换件，委外必修件到台车bom
	 */
	@GetMapping("/SendToSAPPro")
	public Object SendToSAPPro(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wSFCBOMTaskID = StringUtils.parseInt(request.getParameter("SFCBOMTaskID"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_SendToSAPPro(wLoginUser, wOrderID, wSFCBOMTaskID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 推送工艺变更，新增的物料到SAP
	 */
	@GetMapping("/ChangeLogAddToSAP")
	public Object ChangeLogToAdd(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wLogID = StringUtils.parseInt(request.getParameter("LogID"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_ChangeLogAddToSAP(wLoginUser, wLogID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 推送单条台车bom到SAP，属性修改
	 */
	@GetMapping("/PropertyChange")
	public Object PropertyChange(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wID = StringUtils.parseInt(request.getParameter("ID"));
			String wDeleteID = StringUtils.parseString(request.getParameter("DeleteID"));
			double wNumber = StringUtils.parseDouble(request.getParameter("Number"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_BomItemPropertyChange(wLoginUser, wID, wDeleteID,
					wNumber);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "修改成功!", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 将偶换件，委外必修件的评估类型由常规新件改为修复旧件
	 */
	@GetMapping("/AssessTypeChange")
	public Object AssessTypeChange(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_AssessTypeChange(wLoginUser);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "变更成功!", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 添加删除标记
	 */
	@PostMapping("/DeleteListByImport")
	public Object DeleteListByImport(HttpServletRequest request, @RequestParam("file") MultipartFile[] files,
			@RequestParam("OrderID") int wOrderID) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			if (files.length == 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：没有要上传的Excel文件！");
			}

			ServiceResult<String> wServiceResult = new ServiceResult<String>();
			ServiceResult<ExcelData> wExcelData = null;
			String wOriginalFileName = null;
			for (MultipartFile wMultipartFile : files) {
				wOriginalFileName = wMultipartFile.getOriginalFilename();

				if (wOriginalFileName.contains("xlsx") || wOriginalFileName.contains("XLSX")) {
					wExcelData = ExcelReader.getInstance().readMultiSheetExcel(wMultipartFile.getInputStream(),
							wOriginalFileName, "xlsx", 1000000);
				} else if (wOriginalFileName.contains("xls") || wOriginalFileName.contains("XLS")) {
					wExcelData = ExcelReader.getInstance().readMultiSheetExcel(wMultipartFile.getInputStream(),
							wOriginalFileName, "xls", 1000000);
				}

				if (StringUtils.isNotEmpty(wExcelData.FaultCode)) {
					wResult = GetResult(RetCode.SERVER_CODE_ERR, wExcelData.FaultCode);
					return wResult;
				}

				wServiceResult = wAPSService.APS_DeleteListByImport(wLoginUser, wExcelData.Result, wOrderID);

				if (!StringUtils.isEmpty(wServiceResult.FaultCode))
					break;
			}

			if (StringUtils.isEmpty(wServiceResult.Result)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "导入成功!", null, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.Result);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 判断是否超定额了
	 */
	@PostMapping("/IsOverQuota")
	public Object IsOverQuota(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			SFCBOMTask wData = CloneTool.Clone(wParam.get("data"), SFCBOMTask.class);

			ServiceResult<String> wServiceResult = wSFCService.SFC_IsOverEqua(wLoginUser, wData);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 根据SourceID查询标准BOMID
	 */
	@GetMapping("/BOMID")
	public Object QueryBOMID(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wSourceID = StringUtils.parseInt(request.getParameter("SourceID"));
			int wType = StringUtils.parseInt(request.getParameter("SourceType"));
			String wCustomerCode = StringUtils.parseString(request.getParameter("CustomerCode"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_QueryBOMID(wLoginUser, wSourceID, wType,
					wCustomerCode);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "RouteID", wServiceResult.CustomResult.get("RouteID"));
				this.SetResult(wResult, "BomID", wServiceResult.CustomResult.get("BomID"));
				this.SetResult(wResult, "BomName", wServiceResult.CustomResult.get("BomName"));
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 推送单条物料
	 */
	@GetMapping("/SendSingleMaterial")
	public Object SendSingleMaterial(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wBOMID = StringUtils.parseInt(request.getParameter("BOMID"));
			String wOrders = StringUtils.parseString(request.getParameter("Orders"));
			String wItemIDs = StringUtils.parseString(request.getParameter("ItemIDs"));

			if (StringUtils.isEmpty(wOrders) || StringUtils.isEmpty(wItemIDs) || wBOMID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			ServiceResult<Integer> wServiceResult = wAPSService.APS_SendSingleMaterial(wLoginUser, wBOMID, wOrders,
					wItemIDs);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "推送成功!", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 根据主键ID集合重新推送台车BOM
	 */
	@PostMapping("/ReSendByIDList")
	public Object ReSendByIDList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			List<Integer> wIDList = CloneTool.CloneArray(wParam.get("data"), Integer.class);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_ReSendBOMByIDList(wLoginUser, wIDList);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}