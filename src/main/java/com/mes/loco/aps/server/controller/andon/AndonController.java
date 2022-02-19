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
	 * 查询台车订单实时列表
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
	 * 看板数据
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
	 * 获取车辆实时位置信息
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
	 * 创建报表日志
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
	 * 提交报表日志
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

			// 获取参数
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
	 * 根据ID查询报表日志
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

			// 获取参数
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
	 * 查询自己提交的报表日志
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

			// 获取参数
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
	 * 获取广州电力机车生产状态
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
	 * 工位日计划兑现率
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

			// 处理查询时间
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
	 * 工位月计划兑现率
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
	 * 班组日计划兑现率
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

			// 处理查询时间
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
	 * 班组月计划兑现率
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
	 * 工区日计划兑现率
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

			// 处理查询时间
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
	 * 工区月计划兑现率
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
	 * 导出工区兑现率
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

			// 获取参数
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
	 * 导出班组兑现率
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

			// 获取参数
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
	 * 导出工位兑现率
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

			// 获取参数
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
	 * 导出生产概况看板数据
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
	 * 通过工区班组筛选工位
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

			// 获取参数
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
	 * 生产日报第2页
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

			// 获取参数
			int Year = StringUtils.parseInt(request.getParameter("Year"));

			if (Year <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：请输入正确的年份!");
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
	 * 生产日表第三页
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

			// 获取参数
			int Year = StringUtils.parseInt(request.getParameter("Year"));

			if (Year <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：请输入正确的年份!");
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
	 * 生产日表第1页
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
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：请输入正确的年份或月份!");
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
	 * 导出生产计划
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
	 * 通过工区查看未完成、已完成的工序任务详情
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

			// 获取参数
			int wStatus = StringUtils.parseInt(request.getParameter("Status"));
			int wWorkAreaID = StringUtils.parseInt(request.getParameter("WorkAreaID"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			// 查询条件
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wStepID = StringUtils.parseInt(request.getParameter("StepID"));

			if (wWorkAreaID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：参数错误，工区ID不能小于或等于0!");
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
