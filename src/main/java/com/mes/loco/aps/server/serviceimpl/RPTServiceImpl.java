package com.mes.loco.aps.server.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mes.loco.aps.server.service.RPTService;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.FMCShiftLevel;
import com.mes.loco.aps.server.service.mesenum.MESException;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.crm.CRMCustomer;
import com.mes.loco.aps.server.service.po.fmc.FMCLine;
import com.mes.loco.aps.server.service.po.fpc.FPCProduct;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.rpt.RPTCustomerShift;
import com.mes.loco.aps.server.service.po.rpt.RPTDataDetails;
import com.mes.loco.aps.server.service.po.rpt.RPTDataDetailsC;
import com.mes.loco.aps.server.service.po.rpt.RPTLineNumberTrend;
import com.mes.loco.aps.server.service.po.rpt.RPTLineStopTrend;
import com.mes.loco.aps.server.service.po.rpt.RPTMonthData;
import com.mes.loco.aps.server.service.po.rpt.RPTMonthNumberTrend;
import com.mes.loco.aps.server.service.po.rpt.RPTMonthStopTrend;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPart;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPartLine;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPartMonth;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPartTJ;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPartTree;
import com.mes.loco.aps.server.service.po.rpt.RPTProductShift;
import com.mes.loco.aps.server.service.po.rpt.RPTQuarterData;
import com.mes.loco.aps.server.service.po.rpt.RPTYearNumberTrend;
import com.mes.loco.aps.server.service.po.rpt.RPTYearProductShift;
import com.mes.loco.aps.server.service.po.rpt.RPTYearStopTrend;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.dao.rpt.RPTCustomerShiftDAO;
import com.mes.loco.aps.server.serviceimpl.dao.rpt.RPTOrderPartDAO;
import com.mes.loco.aps.server.serviceimpl.dao.rpt.RPTProductShiftDAO;
import com.mes.loco.aps.server.serviceimpl.utils.MESServer;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSUtils;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-6-17 17:47:35
 * @LastEditTime 2020-6-17 17:47:39
 *
 */
@Service
public class RPTServiceImpl implements RPTService {
	private static Logger logger = LoggerFactory.getLogger(RPTServiceImpl.class);

	public RPTServiceImpl() {
	}

	private static RPTService Instance;

	public static RPTService getInstance() {
		if (Instance == null)
			Instance = new RPTServiceImpl();
		return Instance;
	}

	@Override
	public ServiceResult<Integer> RPT_SetProductShifts(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// 触发日报表函数
			Calendar wStartTime = Calendar.getInstance();
			wStartTime.set(Calendar.HOUR_OF_DAY, 0);
			wStartTime.set(Calendar.MINUTE, 0);
			wStartTime.set(Calendar.SECOND, 0);
			Calendar wEndTime = Calendar.getInstance();
			wEndTime.set(Calendar.HOUR_OF_DAY, 23);
			wEndTime.set(Calendar.MINUTE, 59);
			wEndTime.set(Calendar.SECOND, 59);
			// APS
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);

			List<RPTProductShift> wList = RPTProductShiftDAO.getInstance().SelectList(wLoginUser, -1, wShiftID, -1,
					wErrorCode);
			if (wList.size() <= 0) {
				// 修程列表
				List<FMCLine> wLineList = APSConstans.GetFMCLineList().values().stream().collect(Collectors.toList());
				if (wLineList == null || wLineList.size() <= 0) {
					return wResult;
				}
				// 创建数据
				wLineList.forEach(p -> {
					wResult.Result = RPTProductShiftDAO.getInstance().RPT_AddData(wLoginUser, p.ID, wShiftID,
							wStartTime, wEndTime, wErrorCode);
					RPTProductShiftDAO.getInstance().UpdateIsMonthLastDay(wLoginUser, wShiftID, wErrorCode);
				});
			}

			List<RPTCustomerShift> wCusList = RPTCustomerShiftDAO.getInstance().SelectList(wLoginUser, -1, wShiftID, -1,
					wErrorCode);
			if (wCusList.size() <= 0) {
				// 局段列表
				List<CRMCustomer> wCustomerList = APSConstans.GetCRMCustomerList().values().stream()
						.collect(Collectors.toList());
				if (wCustomerList == null || wCustomerList.size() <= 0) {
					return wResult;
				}
				// 创建数据
				wCustomerList.forEach(p -> {
					wResult.Result = RPTCustomerShiftDAO.getInstance().RPT_AddData(wLoginUser, p.ID, wShiftID,
							wStartTime, wEndTime, wErrorCode);
				});
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTProductShift>> RPT_QueryProductShiftList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<RPTProductShift>> wResult = new ServiceResult<List<RPTProductShift>>();
		wResult.Result = new ArrayList<RPTProductShift>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// QMS
			wResult.Result = RPTProductShiftDAO.getInstance().SelectList(wLoginUser, wStartTime, wEndTime, -1,
					wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTProductShift>> RPT_QueryProductShiftInfo(BMSEmployee wLoginUser, int wLineID,
			Calendar wShiftDate, APSShiftPeriod wAPSShiftPeriod) {
		ServiceResult<List<RPTProductShift>> wResult = new ServiceResult<List<RPTProductShift>>();
		wResult.Result = new ArrayList<RPTProductShift>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			int wShiftID = 0;
			switch (wAPSShiftPeriod) {
			case Month:
				Calendar wMonthDay = APSUtils.getInstance().getLastDayOfMonth(wShiftDate);
				wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wMonthDay, APSShiftPeriod.Day,
						FMCShiftLevel.Day, 0);
				break;
			case Week:
				Calendar wWeekDay = APSUtils.getInstance().getLastOfWeek(wShiftDate);
				wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wWeekDay, APSShiftPeriod.Day,
						FMCShiftLevel.Day, 0);
				break;
			case Day:
				wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wShiftDate, APSShiftPeriod.Day,
						FMCShiftLevel.Day, 0);
				break;
			default:
				break;
			}

			wResult.Result = RPTProductShiftDAO.getInstance().SelectList(wLoginUser, -1, wShiftID, wLineID, wErrorCode);

			if (wResult.Result.size() > 0) {
				wResult.Result.forEach(p -> p.LineName = APSConstans.GetFMCLineName(p.LineID));
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTOrderPart>> RPT_QueryRPTOrderPartList(BMSEmployee wLoginUser, int wLineID,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<RPTOrderPart>> wResult = new ServiceResult<List<RPTOrderPart>>();
		wResult.Result = new ArrayList<RPTOrderPart>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = RPTOrderPartDAO.getInstance().SelectList(wLoginUser, wLineID, wStartTime, wEndTime,
					wErrorCode);
			if (wResult.Result.size() > 0) {
				wResult.Result = wResult.Result.stream()
						.collect(Collectors.collectingAndThen(Collectors.toCollection(
								() -> new TreeSet<>(Comparator.comparing(o -> o.getOrderID() + ";" + o.getPartID()))),
								ArrayList::new));
			}

			List<RPTOrderPartTree> wTreeList = new ArrayList<RPTOrderPartTree>();
			if (wResult.Result.size() > 0) {
				List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", wLineID, -1,
						-1, "", "", 1, null, wStartTime, wEndTime, null, null, wErrorCode);
				wOrderList.addAll(OMSOrderDAO.getInstance().SelectList(wLoginUser, -1, -1, "", wLineID, -1, -1, "", "",
						1, null, null, null, wStartTime, wEndTime, wErrorCode));
				Map<Integer, List<OMSOrder>> wOMSOrderListMap = wOrderList.stream()
						.collect(Collectors.groupingBy(p -> p.ID));
				wResult.Result.forEach(p -> {
//					p.PlantDate.add(Calendar.DATE, -1);
//					p.LaterDay += 1;
					p.Order = wOMSOrderListMap.containsKey(p.OrderID) ? wOMSOrderListMap.get(p.OrderID).get(0)
							: new OMSOrder();
				});

				Map<Integer, List<RPTOrderPart>> wRPTOrderPartListMap = wResult.Result.stream()
						.collect(Collectors.groupingBy(p -> p.LineID));
				List<RPTOrderPart> wTempList = null;
				for (Integer wItemLineID : wRPTOrderPartListMap.keySet()) {
					wTempList = wRPTOrderPartListMap.get(wItemLineID);

					if (wTempList == null || wTempList.size() <= 0) {
						continue;
					}

					RPTOrderPartTree wTree = new RPTOrderPartTree();
					wTree.LineID = wItemLineID;
					wTree.LineName = wTempList.get(0).Line;
					wTree.TreeList = new ArrayList<RPTOrderPart>();
					for (RPTOrderPart wRPTOrderPart : wTempList) {
						wTree.TreeList.add(wRPTOrderPart);
					}
					wTreeList.add(wTree);
				}
			}
			wResult.CustomResult.put("Tree", wTreeList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTCustomerShift>> RPT_QueryCustomerShiftList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<List<RPTCustomerShift>> wResult = new ServiceResult<List<RPTCustomerShift>>();
		wResult.Result = new ArrayList<RPTCustomerShift>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// QMS
			wResult.Result = RPTCustomerShiftDAO.getInstance().SelectList(wLoginUser, wStartTime, wEndTime, wErrorCode);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<RPTCustomerShift> RPT_QueryCustomerShiftInfo(BMSEmployee wLoginUser, int wCustomerID,
			Calendar wShiftDate, APSShiftPeriod wAPSShiftPeriod) {
		ServiceResult<RPTCustomerShift> wResult = new ServiceResult<RPTCustomerShift>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			int wShiftID = 0;
			switch (wAPSShiftPeriod) {
			case Month:
				Calendar wMonthDay = APSUtils.getInstance().getLastDayOfMonth(wShiftDate);
				wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wMonthDay, APSShiftPeriod.Day,
						FMCShiftLevel.Day, 0);
				break;
			case Week:
				Calendar wWeekDay = APSUtils.getInstance().getLastOfWeek(wShiftDate);
				wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wWeekDay, APSShiftPeriod.Day,
						FMCShiftLevel.Day, 0);
				break;
			case Day:
				wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, wShiftDate, APSShiftPeriod.Day,
						FMCShiftLevel.Day, 0);
				break;
			default:
				break;
			}

			List<RPTCustomerShift> wList = RPTCustomerShiftDAO.getInstance().SelectList(wLoginUser, -1, wShiftID,
					wCustomerID, wErrorCode);
			if (wList != null && wList.size() > 0) {
				wResult.Result = wList.get(0);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Integer> RPT_SetCustomerShifts(BMSEmployee wLoginUser) {
		ServiceResult<Integer> wResult = new ServiceResult<Integer>(0);
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// APS
			int wShiftID = MESServer.MES_QueryShiftID(wLoginUser.CompanyID, Calendar.getInstance(), APSShiftPeriod.Day,
					FMCShiftLevel.Day, 0);
			if (wShiftID != APSConstans.mShiftID) {
				APSConstans.mShiftID = wShiftID;
				// 触发日报表函数20200617
				Calendar wStartTime = Calendar.getInstance();
				wStartTime.set(Calendar.HOUR_OF_DAY, 0);
				wStartTime.set(Calendar.MINUTE, 0);
				wStartTime.set(Calendar.SECOND, 0);
				Calendar wEndTime = Calendar.getInstance();
				wEndTime.set(Calendar.HOUR_OF_DAY, 23);
				wEndTime.set(Calendar.MINUTE, 59);
				wEndTime.set(Calendar.SECOND, 59);
				// 修程列表
				List<CRMCustomer> wCustomerList = APSConstans.GetCRMCustomerList().values().stream()
						.collect(Collectors.toList());
				if (wCustomerList == null || wCustomerList.size() <= 0) {
					return wResult;
				}
				// 创建数据
				wCustomerList.forEach(p -> {
					wResult.Result = RPTCustomerShiftDAO.getInstance().RPT_AddData(wLoginUser, p.ID, wShiftID,
							wStartTime, wEndTime, wErrorCode);
				});
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<Map<Integer, List<RPTOrderPartTJ>>> RPT_QueryRPTOrderPartTJList(BMSEmployee wLoginUser,
			int wYear) {
		ServiceResult<Map<Integer, List<RPTOrderPartTJ>>> wResult = new ServiceResult<Map<Integer, List<RPTOrderPartTJ>>>();
		wResult.Result = new HashMap<Integer, List<RPTOrderPartTJ>>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			wResult.Result = RPTOrderPartDAO.getInstance().SelectList(wLoginUser, wYear, wErrorCode);

			List<RPTOrderPartMonth> wTree = new ArrayList<RPTOrderPartMonth>();

			if (wResult.Result != null && wResult.Result.size() > 0) {

				for (int wMonth : wResult.Result.keySet()) {
					RPTOrderPartMonth wRPTOrderPartMonth = new RPTOrderPartMonth();
					wRPTOrderPartMonth.MonthID = wMonth;
					wRPTOrderPartMonth.Month = wMonth + "月";

					List<RPTOrderPartTJ> wList = wResult.Result.get(wMonth);

					List<Integer> wLineIDList = wList.stream().map(p -> p.LineID).distinct()
							.collect(Collectors.toList());
					List<RPTOrderPartLine> wPartLineList = new ArrayList<RPTOrderPartLine>();
					for (Integer wLineID : wLineIDList) {
						List<RPTOrderPartTJ> wLineItemList = wList.stream().filter(p -> p.LineID == wLineID)
								.collect(Collectors.toList());

						RPTOrderPartLine wRPTOrderPartLine = new RPTOrderPartLine();
						wRPTOrderPartLine.Month = wMonth + "月";
						wRPTOrderPartLine.LineID = wLineID;
						wRPTOrderPartLine.LineName = wLineItemList.get(0).LineName;
						wRPTOrderPartLine.TreeList = wLineItemList;
						wPartLineList.add(wRPTOrderPartLine);
					}

					wRPTOrderPartMonth.TreeList = wPartLineList;
					wTree.add(wRPTOrderPartMonth);
				}
			}

			// 排序
			wTree.sort(Comparator.comparing(RPTOrderPartMonth::getMonthID, Comparator.reverseOrder()));

			// 剔除超过月份的数据
			if (wYear == Calendar.getInstance().get(Calendar.YEAR)) {
				int wThisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
				wTree.removeIf(p -> p.MonthID > wThisMonth);
			}

			wResult.CustomResult.put("Tree", wTree);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTYearProductShift>> RPT_QueryYearProductShiftList(BMSEmployee wLoginUser,
			Calendar wStartTime, Calendar wEndTime) {
		ServiceResult<List<RPTYearProductShift>> wResult = new ServiceResult<List<RPTYearProductShift>>();
		wResult.Result = new ArrayList<RPTYearProductShift>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①根据时间段获取ShiftID集合
			List<Integer> wList = RPTProductShiftDAO.getInstance().SelectShiftIDList(wLoginUser, wStartTime, wEndTime,
					wErrorCode);
			if (wList == null || wList.size() <= 0) {
				return wResult;
			}
			// ②遍历shiftID，获取年数据
			SimpleDateFormat wSDF = new SimpleDateFormat("yyyy");
			for (Integer wShiftID : wList) {
				List<RPTProductShift> wRPTProductShiftList = RPTProductShiftDAO.getInstance().SelectList(wLoginUser, -1,
						wShiftID, -1, wErrorCode);
				if (wRPTProductShiftList == null || wRPTProductShiftList.size() <= 0) {
					continue;
				}

				int wYear = Integer.parseInt(wSDF.format(wRPTProductShiftList.get(0).ShiftDate.getTime()));

				RPTYearProductShift wRPTYearProductShift = new RPTYearProductShift();
				wRPTYearProductShift.Year = wYear;
				wRPTYearProductShift.RPTCustomerShiftList = wRPTProductShiftList;
				wResult.Result.add(wRPTYearProductShift);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTQuarterData>> RPT_QueryQuarterData(BMSEmployee wLoginUser, int wYear) {
		ServiceResult<List<RPTQuarterData>> wResult = new ServiceResult<List<RPTQuarterData>>();
		wResult.Result = new ArrayList<RPTQuarterData>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①根据年份获取四个季度的开始时间和结束时间
			// ①第一季度1-3月
			Calendar wQStart1 = Calendar.getInstance();
			wQStart1.set(wYear, 0, 1, 0, 0, 0);
			Calendar wQEnd1 = Calendar.getInstance();
			wQEnd1.set(wYear, 2, APSUtils.getInstance().getDaysByYearMonth(wYear, 3), 23, 59, 59);
			// ②第二季度4-6月
			Calendar wQStart2 = Calendar.getInstance();
			wQStart2.set(wYear, 3, 1, 0, 0, 0);
			Calendar wQEnd2 = Calendar.getInstance();
			wQEnd2.set(wYear, 5, APSUtils.getInstance().getDaysByYearMonth(wYear, 6), 23, 59, 59);
			// ③第三季度7-9月
			Calendar wQStart3 = Calendar.getInstance();
			wQStart3.set(wYear, 6, 1, 0, 0, 0);
			Calendar wQEnd3 = Calendar.getInstance();
			wQEnd3.set(wYear, 8, APSUtils.getInstance().getDaysByYearMonth(wYear, 9), 23, 59, 59);
			// ④第四季度10-12月
			Calendar wQStart4 = Calendar.getInstance();
			wQStart4.set(wYear, 9, 1, 0, 0, 0);
			Calendar wQEnd4 = Calendar.getInstance();
			wQEnd4.set(wYear, 11, APSUtils.getInstance().getDaysByYearMonth(wYear, 12), 23, 59, 59);
			// ②获取修程列表
			List<FMCLine> wLineList = APSConstans.GetFMCLineList().values().stream().collect(Collectors.toList());
			// ③遍历修程列表，根据修程和季度开始时间、结束时间获取订单统计数据
			RPTQuarterData wRPTQuarterData = null;
			int wFinish = 0;
			for (FMCLine wLine : wLineList) {
				// ①第一季度
				wFinish = OMSOrderDAO.getInstance().SelectCountByQuarter(wLoginUser, wQStart1, wQEnd1, wLine.ID,
						wErrorCode);
				wRPTQuarterData = new RPTQuarterData(wYear, 1, wLine.ID, wFinish, "第一季度", wLine.Name);
				wResult.Result.add(wRPTQuarterData);
				// ②第二季度
				wFinish = OMSOrderDAO.getInstance().SelectCountByQuarter(wLoginUser, wQStart2, wQEnd2, wLine.ID,
						wErrorCode);
				wRPTQuarterData = new RPTQuarterData(wYear, 2, wLine.ID, wFinish, "第二季度", wLine.Name);
				wResult.Result.add(wRPTQuarterData);
				// ③第三季度
				wFinish = OMSOrderDAO.getInstance().SelectCountByQuarter(wLoginUser, wQStart3, wQEnd3, wLine.ID,
						wErrorCode);
				wRPTQuarterData = new RPTQuarterData(wYear, 3, wLine.ID, wFinish, "第三季度", wLine.Name);
				wResult.Result.add(wRPTQuarterData);
				// ④第四季度
				wFinish = OMSOrderDAO.getInstance().SelectCountByQuarter(wLoginUser, wQStart4, wQEnd4, wLine.ID,
						wErrorCode);
				wRPTQuarterData = new RPTQuarterData(wYear, 4, wLine.ID, wFinish, "第四季度", wLine.Name);
				wResult.Result.add(wRPTQuarterData);
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTMonthData>> RPT_QueryMonthData(BMSEmployee wLoginUser, int wYear) {
		ServiceResult<List<RPTMonthData>> wResult = new ServiceResult<List<RPTMonthData>>();
		wResult.Result = new ArrayList<RPTMonthData>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①获取修程列表
			List<FMCLine> wLineList = APSConstans.GetFMCLineList().values().stream().collect(Collectors.toList());
			// ②循环月份和修程列表
			RPTMonthData wRPTMonthData = null;
			for (int i = 1; i <= 12; i++) {
				// ③获取月份的开始时间和结束时间
				Calendar wStartTime = Calendar.getInstance();
				wStartTime.set(wYear, i - 1, 1, 0, 0, 0);
				Calendar wEndTime = Calendar.getInstance();
				wEndTime.set(wYear, i - 1, APSUtils.getInstance().getDaysByYearMonth(wYear, i), 23, 59, 59);
				for (FMCLine wLine : wLineList) {
					// ④根据开始时间、结束时间、修程获取完成数
					int wFinish = OMSOrderDAO.getInstance().SelectCountByQuarter(wLoginUser, wStartTime, wEndTime,
							wLine.ID, wErrorCode);
					wRPTMonthData = new RPTMonthData(wYear, i, wLine.ID, wFinish, i + "月", wLine.Name);
					wResult.Result.add(wRPTMonthData);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTDataDetails>> RPT_QueryDataDetails(BMSEmployee wLoginUser, int wYear, int wQID,
			int wQType, boolean wShowZero) {
		ServiceResult<List<RPTDataDetails>> wResult = new ServiceResult<List<RPTDataDetails>>();
		wResult.Result = new ArrayList<RPTDataDetails>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			Calendar wStartTime = Calendar.getInstance();
			Calendar wEndTime = Calendar.getInstance();
			switch (wQType) {
			case 1:// 季度
				switch (wQID) {
				case 1:// 1季度
					wStartTime.set(wYear, 0, APSUtils.getInstance().getDaysByYearMonth(wYear, 1), 0, 0, 0);
					wEndTime.set(wYear, 2, APSUtils.getInstance().getDaysByYearMonth(wYear, 3), 23, 59, 59);
					break;
				case 2:// 2季度
					wStartTime.set(wYear, 3, APSUtils.getInstance().getDaysByYearMonth(wYear, 4), 0, 0, 0);
					wEndTime.set(wYear, 5, APSUtils.getInstance().getDaysByYearMonth(wYear, 6), 23, 59, 59);
					break;
				case 3:// 3季度
					wStartTime.set(wYear, 6, APSUtils.getInstance().getDaysByYearMonth(wYear, 7), 0, 0, 0);
					wEndTime.set(wYear, 8, APSUtils.getInstance().getDaysByYearMonth(wYear, 9), 23, 59, 59);
					break;
				case 4:// 4季度
					wStartTime.set(wYear, 9, APSUtils.getInstance().getDaysByYearMonth(wYear, 10), 0, 0, 0);
					wEndTime.set(wYear, 11, APSUtils.getInstance().getDaysByYearMonth(wYear, 12), 23, 59, 59);
					break;
				default:
					break;
				}
				break;
			case 2:// 月份
				wStartTime.set(wYear, wQID - 1, 1, 0, 0, 0);
				wEndTime.set(wYear, wQID - 1, APSUtils.getInstance().getDaysByYearMonth(wYear, wQID), 23, 59, 59);
				break;
			default:
				break;
			}

			// 根据开始时间、结束时间获取完成的所有订单
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectFinishListByTime(wLoginUser, wStartTime,
					wEndTime, wErrorCode);

			if (wShowZero) {// 统计0
				// ①获取局段列表
				List<CRMCustomer> wCustomerList = APSConstans.GetCRMCustomerList().values().stream()
						.collect(Collectors.toList());
				// ②获取车型列表
				List<FPCProduct> wProductList = APSConstans.GetFPCProductList().values().stream()
						.collect(Collectors.toList());
				// ③获取修程列表
				List<FMCLine> wLineList = APSConstans.GetFMCLineList().values().stream().collect(Collectors.toList());
				// ④循环统计数据
				RPTDataDetails wRPTDataDetails = null;
				for (CRMCustomer wCRMCustomer : wCustomerList) {
					for (FPCProduct wFPCProduct : wProductList) {
						for (FMCLine wFMCLine : wLineList) {
							int wFinish = 0;
							if (wOrderList.stream().anyMatch(p -> p.CustomerID == wCRMCustomer.ID
									&& p.ProductID == wFPCProduct.ID && p.LineID == wFMCLine.ID)) {
								wFinish = (int) wOrderList.stream().filter(p -> p.CustomerID == wCRMCustomer.ID
										&& p.ProductID == wFPCProduct.ID && p.LineID == wFMCLine.ID).count();
							}
							wRPTDataDetails = new RPTDataDetails(wCRMCustomer.ID, wFPCProduct.ID, wFMCLine.ID, wFinish,
									wCRMCustomer.CustomerName, wFPCProduct.ProductNo, wFMCLine.Name);
							wResult.Result.add(wRPTDataDetails);
						}
					}
				}
			} else {// 不统计0
				// ①获取订单中局段列表
				List<Integer> wCustomerIDList = wOrderList.stream().map(p -> p.CustomerID).distinct()
						.collect(Collectors.toList());
				// ②获取订单中车型列表
				List<Integer> wProductIDList = wOrderList.stream().map(p -> p.ProductID).distinct()
						.collect(Collectors.toList());
				// ③获取订单中修程列表
				List<Integer> wLineIDList = wOrderList.stream().map(p -> p.LineID).distinct()
						.collect(Collectors.toList());
				// ④循环统计数据
				RPTDataDetails wRPTDataDetails = null;
				for (Integer wCustomerID : wCustomerIDList) {
					for (Integer wProductID : wProductIDList) {
						for (Integer wLineID : wLineIDList) {
							int wFinish = 0;
							if (wOrderList.stream().anyMatch(p -> p.CustomerID == wCustomerID
									&& p.ProductID == wProductID && p.LineID == wLineID)) {
								wFinish = (int) wOrderList.stream().filter(p -> p.CustomerID == wCustomerID
										&& p.ProductID == wProductID && p.LineID == wLineID).count();
								wRPTDataDetails = new RPTDataDetails(wCustomerID, wProductID, wLineID, wFinish,
										APSConstans.GetCRMCustomerName(wCustomerID),
										APSConstans.GetFPCProduct(wProductID).ProductNo,
										APSConstans.GetFMCLineName(wLineID));
								wResult.Result.add(wRPTDataDetails);
							}
						}
					}
				}
			}

			// 将C5、C6完成数统计出来
			List<RPTDataDetailsC> wRPTDataDetailsCList = new ArrayList<RPTDataDetailsC>();
			RPTDataDetailsC wRPTDataDetailsC = null;
			if (wResult.Result.size() > 0) {
				for (RPTDataDetails wRPTDataDetails : wResult.Result) {
					if (wRPTDataDetailsCList.stream().anyMatch(p -> p.CustomerID == wRPTDataDetails.CustomerID
							&& p.ProductID == wRPTDataDetails.ProductID)) {
						continue;
					}

					List<RPTDataDetails> wList = wResult.Result.stream()
							.filter(p -> p.CustomerID == wRPTDataDetails.CustomerID
									&& p.ProductID == wRPTDataDetails.ProductID && p.LineID == 1)
							.collect(Collectors.toList());
					int wC5Finish = 0;
					for (RPTDataDetails wItem : wList) {
						wC5Finish += wItem.Finish;
					}
					wList = wResult.Result.stream()
							.filter(p -> p.CustomerID == wRPTDataDetails.CustomerID
									&& p.ProductID == wRPTDataDetails.ProductID && p.LineID == 2)
							.collect(Collectors.toList());
					int wC6Finish = 0;
					for (RPTDataDetails wItem : wList) {
						wC6Finish += wItem.Finish;
					}
					wRPTDataDetailsC = new RPTDataDetailsC(wRPTDataDetails.CustomerID, wRPTDataDetails.ProductID,
							wC5Finish, wC6Finish, wRPTDataDetails.Customer, wRPTDataDetails.ProductNo);
					wRPTDataDetailsCList.add(wRPTDataDetailsC);
				}
			}
			wResult.CustomResult.put("Details", wRPTDataDetailsCList);

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<List<RPTDataDetails>> RPT_QueryYearProduct(BMSEmployee wLoginUser, int wYear) {
		ServiceResult<List<RPTDataDetails>> wResult = new ServiceResult<List<RPTDataDetails>>();
		wResult.Result = new ArrayList<RPTDataDetails>();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①获取开始时间和结束时间
			Calendar wStartTime = Calendar.getInstance();
			wStartTime.set(wYear, 0, 1, 0, 0, 0);
			Calendar wEndTime = Calendar.getInstance();
			wEndTime.set(wYear, 11, 31, 23, 59, 59);
			// ②根据开始时间和结束时间获取完成订单
			List<OMSOrder> wOrderList = OMSOrderDAO.getInstance().SelectFinishListByTime(wLoginUser, wStartTime,
					wEndTime, wErrorCode);
			// ③统计车型列表
			List<Integer> wProductIDList = wOrderList.stream().map(p -> p.ProductID).distinct()
					.collect(Collectors.toList());
			// ④统计修程列表
			List<Integer> wLineIDList = wOrderList.stream().map(p -> p.LineID).distinct().collect(Collectors.toList());
			// ⑤遍历获取数据
			RPTDataDetails wRPTDataDetails = null;
			for (Integer wProductID : wProductIDList) {
				for (Integer wLineID : wLineIDList) {
					int wFinish = (int) wOrderList.stream()
							.filter(p -> p.ProductID == wProductID && p.LineID == wLineID).count();
					wRPTDataDetails = new RPTDataDetails(0, wProductID, wLineID, wFinish, "",
							APSConstans.GetFPCProduct(wProductID).ProductNo, APSConstans.GetFMCLineName(wLineID));
					wResult.Result.add(wRPTDataDetails);
				}
			}

			wResult.setFaultCode(MESException.getEnumType(wErrorCode.Result).getLable());
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<RPTYearNumberTrend> RPT_QueryYearNumberTrend(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<RPTYearNumberTrend> wResult = new ServiceResult<RPTYearNumberTrend>();
		wResult.Result = new RPTYearNumberTrend();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①根据时间段、是否为每月最后一天查询修程统计数据
			List<RPTProductShift> wSourceList = RPTProductShiftDAO.getInstance().SelectList(wLoginUser, wStartTime,
					wEndTime, 1, wErrorCode);
			// ②根据开始时间和结束时间获取年月
			List<RPTMonthNumberTrend> wMonthTrendList = this.RPT_QueryMonthTrendList(wStartTime, wEndTime);
			// ③获取统计数据中最大的项作为年度进厂、出厂、在厂数据源
			int wYearEnterC5 = 0;
			int wYearOutC5 = 0;
			int wYearInC5 = 0;
			int wYearEnterC6 = 0;
			int wYearOutC6 = 0;
			int wYearInC6 = 0;
			if (wSourceList.size() > 0) {
				int wShiftID = wSourceList.stream().max(Comparator.comparing(RPTProductShift::getShiftID))
						.get().ShiftID;
				List<RPTProductShift> wShiftList = wSourceList.stream().filter(p -> p.ShiftID == wShiftID)
						.collect(Collectors.toList());
				if (wShiftList.stream().anyMatch(p -> p.LineID == 1)) {
					RPTProductShift wItem = wShiftList.stream().filter(p -> p.LineID == 1).findFirst().get();
					wYearEnterC5 = wItem.MonthEnter;
					wYearOutC5 = wItem.MonthOut;
					wYearInC5 = wItem.RealPlant;
				}
				if (wShiftList.stream().anyMatch(p -> p.LineID == 2)) {
					RPTProductShift wItem = wShiftList.stream().filter(p -> p.LineID == 2).findFirst().get();
					wYearEnterC6 = wItem.MonthEnter;
					wYearOutC6 = wItem.MonthOut;
					wYearInC6 = wItem.RealPlant;
				}
			}
			// ①修程列表
			List<FMCLine> wLineList = APSConstans.GetFMCLineList().values().stream().collect(Collectors.toList());
			// ④遍历年月数据，统计数据源
			for (RPTMonthNumberTrend wRPTMonthNumberTrend : wMonthTrendList) {
				String wYearMonth = StringUtils.Format("{0}{1}", wRPTMonthNumberTrend.Year,
						String.format("%02d", wRPTMonthNumberTrend.Month));
				for (FMCLine wFMCLine : wLineList) {
					RPTLineNumberTrend wRPTLineNumberTrend = new RPTLineNumberTrend();
					wRPTLineNumberTrend.LineID = wFMCLine.ID;
					wRPTLineNumberTrend.LineName = wFMCLine.Name;
					if (wSourceList.stream().anyMatch(p -> String.valueOf(p.ShiftID).substring(0, 6).equals(wYearMonth)
							&& p.LineID == wFMCLine.ID)) {
						RPTProductShift wItem = wSourceList.stream()
								.filter(p -> String.valueOf(p.ShiftID).substring(0, 6).equals(wYearMonth)
										&& p.LineID == wFMCLine.ID)
								.findFirst().get();
						wRPTLineNumberTrend.EnterPlant = wItem.MonthEnter;
						wRPTLineNumberTrend.OutPlant = wItem.MonthOut;
						wRPTLineNumberTrend.InPlant = wItem.RealPlant;
					}
					wRPTMonthNumberTrend.RPTLineNumberTrendList.add(wRPTLineNumberTrend);
				}
			}
			// ⑤添加返回结果
			wResult.Result.YearEnterC5 = wYearEnterC5;
			wResult.Result.YearInC5 = wYearInC5;
			wResult.Result.YearOutC5 = wYearOutC5;
			wResult.Result.YearEnterC6 = wYearEnterC6;
			wResult.Result.YearInC6 = wYearInC6;
			wResult.Result.YearOutC6 = wYearOutC6;
			wResult.Result.RPTMonthNumberTrendList = wMonthTrendList;
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据开始时间和结束时间获取年月(进出厂)
	 */
	private List<RPTMonthNumberTrend> RPT_QueryMonthTrendList(Calendar wStartTime, Calendar wEndTime) {
		List<RPTMonthNumberTrend> wResult = new ArrayList<RPTMonthNumberTrend>();
		try {
			if (wStartTime == null || wEndTime == null || wStartTime.compareTo(wEndTime) >= 0) {
				return wResult;
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");

			// ①获取开始时间的年、月
			int wYear = wStartTime.get(Calendar.YEAR);
			int wMonth = wStartTime.get(Calendar.MONTH);
			// ②遍历获取
			Calendar wS = Calendar.getInstance();
			wS.set(wYear, wMonth, 1, 0, 0, 0);
			while (Integer.parseInt(wSDF.format(wS.getTime())) <= Integer.parseInt(wSDF.format(wEndTime.getTime()))) {
				wYear = wS.get(Calendar.YEAR);
				wMonth = wS.get(Calendar.MONTH) + 1;

				RPTMonthNumberTrend wRPTMonthNumberTrend = new RPTMonthNumberTrend();
				wRPTMonthNumberTrend.Year = wYear;
				wRPTMonthNumberTrend.Month = wMonth;
				wResult.add(wRPTMonthNumberTrend);

				wS.add(Calendar.MONTH, 1);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	@Override
	public ServiceResult<RPTYearStopTrend> RPT_QueryYearStopTrend(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime) {
		ServiceResult<RPTYearStopTrend> wResult = new ServiceResult<RPTYearStopTrend>();
		wResult.Result = new RPTYearStopTrend();
		OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
		try {
			// ①根据时间段、是否为每月最后一天查询修程统计数据
			List<RPTProductShift> wSourceList = RPTProductShiftDAO.getInstance().SelectList(wLoginUser, wStartTime,
					wEndTime, 1, wErrorCode);
			// ②根据开始时间和结束时间获取年月
			List<RPTMonthStopTrend> wMonthTrendList = this.RPT_QueryMonthStopTrendList(wStartTime, wEndTime);
			// ③获取统计数据中最大的项作为年度停时数据源
			double wC5RepairPeriod = 0;
			double wC5TelegraphPeriod = 0;
			double wC6RepairPeriod = 0;
			double wC6TelegraphPeriod = 0;
			if (wSourceList.size() > 0) {
				int wShiftID = wSourceList.stream().max(Comparator.comparing(RPTProductShift::getShiftID))
						.get().ShiftID;
				List<RPTProductShift> wShiftList = wSourceList.stream().filter(p -> p.ShiftID == wShiftID)
						.collect(Collectors.toList());
				if (wShiftList.stream().anyMatch(p -> p.LineID == 1)) {
					RPTProductShift wItem = wShiftList.stream().filter(p -> p.LineID == 1).findFirst().get();
					wC5RepairPeriod = wItem.AvgRepairPeriod;
					wC5TelegraphPeriod = wItem.AvgTelegraphPeriod;
				}
				if (wShiftList.stream().anyMatch(p -> p.LineID == 2)) {
					RPTProductShift wItem = wShiftList.stream().filter(p -> p.LineID == 2).findFirst().get();
					wC6RepairPeriod = wItem.AvgRepairPeriod;
					wC6TelegraphPeriod = wItem.AvgTelegraphPeriod;
				}
			}
			// ①修程列表
			List<FMCLine> wLineList = APSConstans.GetFMCLineList().values().stream().collect(Collectors.toList());
			// ④遍历年月数据，统计数据源
			for (RPTMonthStopTrend wRPTMonthStopTrend : wMonthTrendList) {
				String wYearMonth = StringUtils.Format("{0}{1}", wRPTMonthStopTrend.Year,
						String.format("%02d", wRPTMonthStopTrend.Month));
				for (FMCLine wFMCLine : wLineList) {
					RPTLineStopTrend wRPTLineStopTrend = new RPTLineStopTrend();
					wRPTLineStopTrend.LineID = wFMCLine.ID;
					wRPTLineStopTrend.LineName = wFMCLine.Name;
					if (wSourceList.stream().anyMatch(p -> String.valueOf(p.ShiftID).substring(0, 6).equals(wYearMonth)
							&& p.LineID == wFMCLine.ID)) {
						RPTProductShift wItem = wSourceList.stream()
								.filter(p -> String.valueOf(p.ShiftID).substring(0, 6).equals(wYearMonth)
										&& p.LineID == wFMCLine.ID)
								.findFirst().get();
						wRPTLineStopTrend.RepairPeriod = wItem.AvgMonthRepairPeriod;
						wRPTLineStopTrend.TelegraphPeriod = wItem.AvgMonthTelegraphPeriod;
					}
					wRPTMonthStopTrend.RPTLineStopTrendList.add(wRPTLineStopTrend);
				}
			}
			// ⑤添加返回结果
			wResult.Result.C5RepairPeriod = wC5RepairPeriod;
			wResult.Result.C5TelegraphPeriod = wC5TelegraphPeriod;
			wResult.Result.C6RepairPeriod = wC6RepairPeriod;
			wResult.Result.C6TelegraphPeriod = wC6TelegraphPeriod;
			wResult.Result.RPTMonthNumberTrendList = wMonthTrendList;
		} catch (Exception e) {
			wResult.FaultCode += e.toString();
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 根据开始时间和结束时间获取年月(停时)
	 */
	private List<RPTMonthStopTrend> RPT_QueryMonthStopTrendList(Calendar wStartTime, Calendar wEndTime) {
		List<RPTMonthStopTrend> wResult = new ArrayList<RPTMonthStopTrend>();
		try {
			if (wStartTime == null || wEndTime == null || wStartTime.compareTo(wEndTime) >= 0) {
				return wResult;
			}

			SimpleDateFormat wSDF = new SimpleDateFormat("yyyyMMdd");

			// ①获取开始时间的年、月
			int wYear = wStartTime.get(Calendar.YEAR);
			int wMonth = wStartTime.get(Calendar.MONTH);
			// ②遍历获取
			Calendar wS = Calendar.getInstance();
			wS.set(wYear, wMonth, 1, 0, 0, 0);
			while (Integer.parseInt(wSDF.format(wS.getTime())) <= Integer.parseInt(wSDF.format(wEndTime.getTime()))) {
				wYear = wS.get(Calendar.YEAR);
				wMonth = wS.get(Calendar.MONTH) + 1;

				RPTMonthStopTrend wRPTMonthNumberTrend = new RPTMonthStopTrend();
				wRPTMonthNumberTrend.Year = wYear;
				wRPTMonthNumberTrend.Month = wMonth;
				wResult.add(wRPTMonthNumberTrend);

				wS.add(Calendar.MONTH, 1);
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}
}
