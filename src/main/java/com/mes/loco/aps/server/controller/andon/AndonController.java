package com.mes.loco.aps.server.controller.andon;

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
import org.springframework.web.bind.annotation.RestController;

import com.mes.loco.aps.server.controller.BaseController;
import com.mes.loco.aps.server.service.AndonService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanAreaCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanClassCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanPartCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayReport01;
import com.mes.loco.aps.server.service.po.andon.AndonJournal;
import com.mes.loco.aps.server.service.po.andon.AndonLocomotiveProductionStatus;
import com.mes.loco.aps.server.service.po.andon.AndonOrder;
import com.mes.loco.aps.server.service.po.andon.AndonScreen;
import com.mes.loco.aps.server.service.po.andon.AndonTaskStep;
import com.mes.loco.aps.server.service.po.andon.OrderTimeInfo;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-4-2 16:57:38
 */
@RestController
@RequestMapping("/api/Andon")
public class AndonController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(AndonController.class);

	@Autowired
	AndonService wAndonService;

	/**
	 * ??????????????????????????????
	 */
	@GetMapping("/TrainOrderInfoList")
	public Object TrainOrderInfoList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<List<OrderTimeInfo>> wServiceResult = wAndonService
					.Andon_QueryTrainOrderInfoListNew(wLoginUser);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * ????????????
	 */
	@GetMapping("/AndonScreen")
	public Object AndonScreen(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<AndonScreen> wServiceResult = wAndonService.Andon_QueryAndonScreen(wLoginUser);

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
	 * ??????????????????????????????
	 */
	@GetMapping("/PositionInfo")
	public Object PositionInfo(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			ServiceResult<AndonOrder> wServiceResult = wAndonService.Andon_QueryPositionInfo(wLoginUser, wOrderID);

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
	 * ??????????????????
	 */
	@GetMapping("/CreateJournal")
	public Object CreateJournal(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<AndonJournal> wServiceResult = wAndonService.Andon_CreateJournal(wLoginUser);

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
	 * ??????????????????
	 */
	@PostMapping("/SubmitJournal")
	public Object SubmitJournal(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			AndonJournal wAndonJournal = CloneTool.Clone(wParam.get("data"), AndonJournal.class);

			ServiceResult<Integer> wServiceResult = wAndonService.Andon_SubmitJournal(wLoginUser, wAndonJournal);

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
	 * ??????ID??????????????????
	 */
	@GetMapping("/JournalInfo")
	public Object JournalInfo(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			int wID = StringUtils.parseInt(request.getParameter("ID"));

			ServiceResult<AndonJournal> wServiceResult = wAndonService.Andon_QueryJournalInfo(wLoginUser, wID);

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
	 * ?????????????????????????????????
	 */
	@GetMapping("/JournalEmployeeAll")
	public Object JournalEmployeeAll(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<AndonJournal>> wServiceResult = wAndonService.Andon_QueryJournalEmployeeAll(wLoginUser,
					wStartTime, wEndTime);

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
	 * ????????????????????????????????????
	 */
	@GetMapping("/ProductionStatus")
	public Object ProductionStatus(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<AndonLocomotiveProductionStatus> wServiceResult = wAndonService
					.Andon_QueryProductionStatus(wLoginUser);

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
	 * ????????????????????????
	 */
	@GetMapping("/PartDayPlanRate")
	public Object PartDayPlanRate(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wDate = StringUtils.parseCalendar(request.getParameter("Date"));
			String wPartIDs = StringUtils.parseString(request.getParameter("PartIDs"));
			String wOrderIDs = StringUtils.parseString(request.getParameter("OrderIDs"));

			// ??????????????????
			Calendar wSTime = (Calendar) wDate.clone();
			wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);

			Calendar wETime = (Calendar) wDate.clone();
			wETime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 23, 59, 59);

			ServiceResult<List<AndonDayPlanPartCashingRate>> wServiceResult = wAndonService
					.Andon_QueryPartDayPlanRate(wLoginUser, wSTime, wETime, wPartIDs, wOrderIDs);

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
	 * ????????????????????????
	 */
	@GetMapping("/PartMonthPlanRate")
	public Object PartMonthPlanRate(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wSDate = StringUtils.parseCalendar(request.getParameter("SDate"));
			Calendar wEDate = StringUtils.parseCalendar(request.getParameter("EDate"));
			String wPartIDs = StringUtils.parseString(request.getParameter("PartIDs"));
			String wOrderIDs = StringUtils.parseString(request.getParameter("OrderIDs"));

			wSDate.set(wSDate.get(Calendar.YEAR), wSDate.get(Calendar.MONTH), wSDate.get(Calendar.DATE), 0, 0, 0);
			wEDate.set(wEDate.get(Calendar.YEAR), wEDate.get(Calendar.MONTH), wEDate.get(Calendar.DATE), 23, 59, 59);

			ServiceResult<List<AndonDayPlanPartCashingRate>> wServiceResult = wAndonService
					.Andon_QueryPartMonthPlanRate(wLoginUser, wSDate, wEDate, wPartIDs, wOrderIDs);

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
	 * ????????????????????????
	 */
	@GetMapping("/ClassDayPlanRate")
	public Object ClassDayPlanRate(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wDate = StringUtils.parseCalendar(request.getParameter("Date"));
			String wClassIDs = StringUtils.parseString(request.getParameter("ClassIDs"));
			String wOrderIDs = StringUtils.parseString(request.getParameter("OrderIDs"));

			// ??????????????????
			Calendar wSTime = (Calendar) wDate.clone();
			wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);

			Calendar wETime = (Calendar) wDate.clone();
			wETime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 23, 59, 59);

			ServiceResult<List<AndonDayPlanClassCashingRate>> wServiceResult = wAndonService
					.Andon_QueryClassDayPlanRate(wLoginUser, wSTime, wETime, wClassIDs, wOrderIDs);

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
	 * ????????????????????????
	 */
	@GetMapping("/ClassMonthPlanRate")
	public Object ClassMonthPlanRate(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wSDate = StringUtils.parseCalendar(request.getParameter("SDate"));
			Calendar wEDate = StringUtils.parseCalendar(request.getParameter("EDate"));
			String wClassIDs = StringUtils.parseString(request.getParameter("ClassIDs"));
			String wOrderIDs = StringUtils.parseString(request.getParameter("OrderIDs"));

			wSDate.set(wSDate.get(Calendar.YEAR), wSDate.get(Calendar.MONTH), wSDate.get(Calendar.DATE), 0, 0, 0);
			wEDate.set(wEDate.get(Calendar.YEAR), wEDate.get(Calendar.MONTH), wEDate.get(Calendar.DATE), 23, 59, 59);

			ServiceResult<List<AndonDayPlanClassCashingRate>> wServiceResult = wAndonService
					.Andon_QueryClassMonthPlanRate(wLoginUser, wSDate, wEDate, wClassIDs, wOrderIDs);

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
	 * ????????????????????????
	 */
	@GetMapping("/AreaDayPlanRate")
	public Object AreaDayPlanRate(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wDate = StringUtils.parseCalendar(request.getParameter("Date"));
			String wAreaIDs = StringUtils.parseString(request.getParameter("AreaIDs"));
			String wOrderIDs = StringUtils.parseString(request.getParameter("OrderIDs"));

			// ??????????????????
			Calendar wSTime = (Calendar) wDate.clone();
			wSTime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 0, 0, 0);

			Calendar wETime = (Calendar) wDate.clone();
			wETime.set(wSTime.get(Calendar.YEAR), wSTime.get(Calendar.MONTH), wSTime.get(Calendar.DATE), 23, 59, 59);

			ServiceResult<List<AndonDayPlanAreaCashingRate>> wServiceResult = wAndonService
					.Andon_QueryAreaDayPlanRate(wLoginUser, wSTime, wETime, wAreaIDs, wOrderIDs);

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
	 * ????????????????????????
	 */
	@GetMapping("/AreaMonthPlanRate")
	public Object AreaMonthPlanRate(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wSDate = StringUtils.parseCalendar(request.getParameter("SDate"));
			Calendar wEDate = StringUtils.parseCalendar(request.getParameter("EDate"));
			String wAreaIDs = StringUtils.parseString(request.getParameter("AreaIDs"));
			String wOrderIDs = StringUtils.parseString(request.getParameter("OrderIDs"));

			wSDate.set(wSDate.get(Calendar.YEAR), wSDate.get(Calendar.MONTH), wSDate.get(Calendar.DATE), 0, 0, 0);
			wEDate.set(wEDate.get(Calendar.YEAR), wEDate.get(Calendar.MONTH), wEDate.get(Calendar.DATE), 23, 59, 59);

			ServiceResult<List<AndonDayPlanAreaCashingRate>> wServiceResult = wAndonService
					.Andon_QueryAreaMonthPlanRate(wLoginUser, wSDate, wEDate, wAreaIDs, wOrderIDs);

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
	 * ?????????????????????
	 */
	@PostMapping("/ExportAreaRate")
	public Object ExportAreaRate(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			List<AndonDayPlanAreaCashingRate> wList = CloneTool.CloneArray(wParam.get("data"),
					AndonDayPlanAreaCashingRate.class);

			ServiceResult<String> wServiceResult = wAndonService.Andon_ExportAreaRate(wLoginUser, wList);

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
	 * ?????????????????????
	 */
	@PostMapping("/ExportClassRate")
	public Object ExportClassRate(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			List<AndonDayPlanClassCashingRate> wList = CloneTool.CloneArray(wParam.get("data"),
					AndonDayPlanClassCashingRate.class);

			ServiceResult<String> wServiceResult = wAndonService.Andon_ExportClassRate(wLoginUser, wList);

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
	 * ?????????????????????
	 */
	@PostMapping("/ExportPartRate")
	public Object ExportPartRate(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			List<AndonDayPlanPartCashingRate> wList = CloneTool.CloneArray(wParam.get("data"),
					AndonDayPlanPartCashingRate.class);

			ServiceResult<String> wServiceResult = wAndonService.Andon_ExportPartRate(wLoginUser, wList);

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
	 * ??????????????????????????????
	 */
	@GetMapping("/ExportTrainStatus")
	public Object ExportTrainStatus(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<String> wServiceResult = wAndonService.Andon_ExportTrainStatus(wLoginUser);

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
	 * ??????????????????????????????
	 */
	@GetMapping("/PartList")
	public Object PartList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			String wAreaID = StringUtils.parseString(request.getParameter("AreaID"));
			String wClassID = StringUtils.parseString(request.getParameter("ClassID"));

			ServiceResult<List<FPCPart>> wServiceResult = wAndonService.APS_QueryPartList(wLoginUser, wAreaID,
					wClassID);

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
	 * ???????????????2???
	 */
	@GetMapping("/ProductionDaily2")
	public Object ProductionDaily2(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			int Year = StringUtils.parseInt(request.getParameter("Year"));

			if (Year <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "?????????????????????????????????!");
			}

			ServiceResult<Integer> wServiceResult = wAndonService.Andon_ProductionDaily2(wLoginUser, Year);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
				this.SetResult(wResult, "C5Total", wServiceResult.CustomResult.get("C5Total"));
				this.SetResult(wResult, "C5", wServiceResult.CustomResult.get("C5"));
				this.SetResult(wResult, "C6Total", wServiceResult.CustomResult.get("C6Total"));
				this.SetResult(wResult, "C6", wServiceResult.CustomResult.get("C6"));
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
	 * ?????????????????????
	 */
	@GetMapping("/ProductionDaily3")
	public Object ProductionDaily3(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			int Year = StringUtils.parseInt(request.getParameter("Year"));

			if (Year <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "?????????????????????????????????!");
			}

			ServiceResult<Integer> wServiceResult = wAndonService.Andon_ProductionDaily3(wLoginUser, Year);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
				this.SetResult(wResult, "C5", wServiceResult.CustomResult.get("C5"));
				this.SetResult(wResult, "C6", wServiceResult.CustomResult.get("C6"));
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
	 * ???????????????1???
	 */
	@GetMapping("/ProductionDaily1")
	public Object ProductionDaily1(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			int wYear = StringUtils.parseInt(request.getParameter("Year"));
			int wMonth = StringUtils.parseInt(request.getParameter("Month"));

			if (wYear <= 0 || wMonth <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "??????????????????????????????????????????!");
			}

			ServiceResult<AndonDayReport01> wServiceResult = wAndonService.Andon_ProductionDaily1(wLoginUser, wYear,
					wMonth);

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
	 * ??????????????????
	 */
	@GetMapping("/ExportProductionPlan")
	public Object ExportProductionPlan(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<String> wServiceResult = wAndonService.Andon_ExportProductionPlan(wLoginUser);

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
	 * ????????????????????????????????????????????????????????????
	 */
	@GetMapping("/AreaStepDetails")
	public Object AreaStepDetails(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ????????????
			int wStatus = StringUtils.parseInt(request.getParameter("Status"));
			int wWorkAreaID = StringUtils.parseInt(request.getParameter("WorkAreaID"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			// ????????????
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wStepID = StringUtils.parseInt(request.getParameter("StepID"));

			if (wWorkAreaID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "??????????????????????????????ID?????????????????????0!");
			}

			ServiceResult<List<AndonTaskStep>> wServiceResult = wAndonService.Andon_QueryAreaStepDetails(wLoginUser,
					wStatus, wWorkAreaID, wStartTime, wEndTime, wOrderID, wPartID, wStepID);

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

}
