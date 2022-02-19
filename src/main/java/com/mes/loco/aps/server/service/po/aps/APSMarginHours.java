package com.mes.loco.aps.server.service.po.aps;

import java.util.Calendar;

public class APSMarginHours {
	public int MarginHours = 0;
	public Calendar StartTime = Calendar.getInstance();
	public int ErrorCode = 0;

	public APSMarginHours() {
		this.MarginHours = 0;
		this.StartTime = Calendar.getInstance();
		this.ErrorCode = 0;
	}
}
