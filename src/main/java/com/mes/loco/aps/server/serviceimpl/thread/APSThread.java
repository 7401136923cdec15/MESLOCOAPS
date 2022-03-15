package com.mes.loco.aps.server.serviceimpl.thread;

import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.sfc.SFCAttemptRun;
import com.mes.loco.aps.server.service.utils.Configuration;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.APSServiceImpl;
import com.mes.loco.aps.server.serviceimpl.RPTServiceImpl;
import com.mes.loco.aps.server.serviceimpl.dao.BaseDAO;
import com.mes.loco.aps.server.serviceimpl.dao.andon.AndonDAO;
import com.mes.loco.aps.server.serviceimpl.dao.sfc.SFCAttemptRunDAO;
import com.mes.loco.aps.server.serviceimpl.thread.APSThread;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class APSThread implements DisposableBean {
	private static final Logger logger = LoggerFactory.getLogger(APSThread.class);

	private BMSEmployee AdminUser;
	boolean mIsStart;
	private int Ticks = 0;

	@PostConstruct
	public void init() {
		Run();
	}

	public APSThread() {
		this.AdminUser = BaseDAO.SysAdmin;
		this.mIsStart = false;

	}

	public APSThread(BMSEmployee wLoginUser) {
		this.mIsStart = false;
		this.AdminUser = wLoginUser;
	}

	private void Run() {

		try {
			if (this.mIsStart)
				return;
			this.mIsStart = true;

			// 10秒后启动线程，防止其他服务未启动，导致访问超时
			Thread.sleep(10000L);
			logger.info("APSThread Started!!");
			new Thread(() -> {
				while (this.mIsStart) {
					try {
						Thread.sleep(1000L);

						if (this.Ticks % 2 == 0) {
							// 安灯数据
							AndonDAO.getInstance().Andon_UpdateBusiness(this.AdminUser);
							// 触发日计划
							APSServiceImpl.getInstance().APS_TriggerDayPlans(AdminUser);
							// 流程引擎消息
							this.SFCBiz();
							// 次日重新制定日计划
//							APSServiceImpl.getInstance().APS_ReMakingDayPlan(AdminUser);
						}
						if (this.Ticks % 20 == 0) {
							// 自动生成竣工确认单
//							APSServiceImpl.getInstance().APS_AutoGenerateConfirmForm(AdminUser);

							// 出厂申请消息提示
							APSServiceImpl.getInstance().APS_AutoSendMessageToApplyOutPlant(this.AdminUser);
							// 安灯配置信息
							AndonDAO.getInstance().Andon_ResetConfiguration(AdminUser);
							// 自动同步台车BOM
							APSServiceImpl.getInstance().APS_HandleBOMOnTime();
						}

						if (this.Ticks % 60 == 0) {
							// 自动完成工位任务
//							APSServiceImpl.getInstance().APS_AutoFinishTaskPart(AdminUser);
							// 班次切换，新增日、周报表数据源
							RPTServiceImpl.getInstance().RPT_SetProductShifts(AdminUser);
						}

						// 一小时一轮询
						String sendInteval = Configuration.readConfigString("sendInteval", "config/config");
						int wInterval = StringUtils.parseInt(sendInteval);
						if (this.Ticks % wInterval == 0) {
							// 自动将物料需求计划生成物料配送单
							APSServiceImpl.getInstance().APS_AutoCreateDeliveryOrder(AdminUser);
							// 自动推送物料配送单至WMS
							APSServiceImpl.getInstance().APS_AutoSendDemandList(AdminUser);
						}

						this.Ticks++;

						if (Ticks > 10000)
							this.Ticks = 1;

					} catch (Exception ex) {
						logger.error(ex.toString());
					}
				}
			}).start();
		} catch (Exception ex) {
			logger.error(StringUtils.Format("QMS start failed error:{0}", new Object[] { ex.toString() }));
		}
	}

	private void SFCBiz() {
		try {
			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			SFCAttemptRun wSFCAttemptRun = null;
			for (int i = 0; i < APSConstans.SFCAttemptRunTaskResource.size(); i++) {
				wSFCAttemptRun = APSConstans.SFCAttemptRunTaskResource.get();
				boolean wIsTrue = SFCAttemptRunDAO.getInstance().AutoJudge(this.AdminUser, wSFCAttemptRun, wErrorCode);
				if (!wIsTrue)
					APSConstans.SFCAttemptRunTaskResource.add(wSFCAttemptRun);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	public void destroy() throws Exception {
		this.mIsStart = false;
	}
}