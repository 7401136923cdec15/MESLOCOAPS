package com.mes.loco.aps.server.service.mesenum;

import com.mes.loco.aps.server.service.mesenum.BPMEventModule;
import com.mes.loco.aps.server.service.po.cfg.CFGItem;
import java.util.ArrayList;
import java.util.List;

public enum BPMEventModule {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 自检
	 */
	SCZJ(1003, "自检"),
	/**
	 * 异常
	 */
	SCCall(1012, "异常"),
	/**
	 * 移车
	 */
	SCMovePart(1020, "移车"),
	/**
	 * NCR报告
	 */
	TechNCR(3002, "NCR报告"),
	/**
	 * 过程检返修
	 */
	TechRepair(3008, "过程检返修"),
	/**
	 * 不合格评审申请
	 */
	DeviceDJ(4001, "不合格评审申请"),
	/**
	 * 月计划审批
	 */
	SCMonthAudit(8100, "月计划审批"),
	/**
	 * 周计划审批
	 */
	SCWeekAudit(8101, "周计划审批"),
	/**
	 * 日计划审批
	 */
	SCDayAudit(8102, "日计划审批"),
	/**
	 * 生产派工
	 */
	SCDispatching(8103, "生产派工"),
	/**
	 * 借调
	 */
	ToLoan(8104, "借调"),
	/**
	 * 日计划下达
	 */
	DayPlanIssue(8105, "日计划下达"),
	/**
	 * 问题项派工
	 */
	ProblemPG(8106, "问题项派工"),
	/**
	 * 预检报告审批
	 */
	YJReport(8107, "预检报告审批"),
	/**
	 * 质量派工
	 */
	QTDispatching(8108, "质量派工"),
	/**
	 * 出厂申请
	 */
	OutPlantApply(8109, "出厂申请"),
	/**
	 * 转序
	 */
	TurnOrder(8110, "转序"),
	/**
	 * 完工打卡
	 */
	FinishClock(8111, "完工打卡"),
	/**
	 * 互检
	 */
	MutualCheck(8112, "互检"),
	/**
	 * 专检
	 */
	SpecialCheck(8113, "专检"),
	/**
	 * 预检
	 */
	PreCheck(8114, "预检"),
	/**
	 * 预检问题项处理
	 */
	PreProblemHandle(8115, "预检问题项处理"),
	/**
	 * 工位提示
	 */
	StationTip(8116, "工位提示"),
	/**
	 * 终检
	 */
	FinalCheck(8117, "终检"),
	/**
	 * 出厂检
	 */
	OutCheck(8118, "出厂检"),
	/**
	 * 偶换件不合格评审
	 */
	OccasionNCR(8201, "偶换件不合格评审"),
	/**
	 * 试运申请
	 */
	AttemptRun(8202, "试运申请"),
	/**
	 * 借调申请(本工区)
	 */
	ToLoanApply(8203, "本工区借调申请"),
	/**
	 * 积压物资退库申请
	 */
	ReturnOverMaterial(8204, "积压物资退库申请"),
	/**
	 * 借调申请(跨工区)
	 */
	ToLoanApplyK(8205, "跨工区借调申请"),
	/**
	 * 临时性检查
	 */
	TempCheck(8206, "临时性检查"),
	/**
	 * 标准审批
	 */
	StandardAudit(8207, "标准审批"),
	/**
	 * 生产日志
	 */
	ProductionLog(8208, "生产日志"),
	/**
	 * 工艺变更-返修
	 */
	SBOMChange_Repair(8209, "工艺变更-返修"),
	/**
	 * 工艺变更-异常评审
	 */
	SBOMChange_Exception(8210, "工艺变更-异常评审"),
	/**
	 * 转向架互换
	 */
	BogiesChange(8211, "转向架互换"),
	/**
	 * 工艺变更通知
	 */
	TechChangeNotice(8213, "工艺变更通知"),
	/**
	 * 工艺变更
	 */
	SBOMChange(8214, "工艺变更"),
	/**
	 * 物料采购通知
	 */
	MaterialPurchase(8216, "物料采购通知"),
	/**
	 * 工艺变更-不合格评审
	 */
	MaterialChangeNCR(8217, "工艺变更-不合格评审"),
	/**
	 * 工艺变更-修改工序
	 */
	StepChangeUpdateFile(8218, "工艺变更-修改工艺文件"),
	/**
	 * 台车bom创建
	 */
	APSBOMCreateNotify(8300, "台车bom创建"),
	/**
	 * 偶换件台车BOM
	 */
	APSBOMError(8301, "偶换件台车BOM"),
	/**
	 * 转序确认
	 */
	SFCTurnOrderConfirm(8400, "转序确认"),
	/**
	 * 台车BOM审批
	 */
	APMBomAudit(8500, "台车BOM审批"),
	/**
	 * 工序任务取消
	 */
	TaskStepCancel(8600, "工序任务取消");

	private int value;
	private String lable;

	BPMEventModule(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	public static BPMEventModule getEnumType(int val) {

		for (BPMEventModule type : BPMEventModule.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return BPMEventModule.Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (BPMEventModule type : BPMEventModule.values()) {
			CFGItem wItem = new CFGItem();
			wItem.ID = type.getValue();
			wItem.ItemName = type.getLable();
			wItem.ItemText = type.getLable();
			wItemList.add(wItem);
		}
		return wItemList;
	}

	public int getValue() {
		return this.value;
	}

	public String getLable() {
		return this.lable;
	}
}
