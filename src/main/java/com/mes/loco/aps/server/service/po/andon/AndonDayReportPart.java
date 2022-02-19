package com.mes.loco.aps.server.service.po.andon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 生产日报第一页第二部分
 */
public class AndonDayReportPart implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public List<String> C5Title = new ArrayList<String>(
			Arrays.asList("入厂", "机车预检", "机车一级解体", "机车外部清洁", "转向架解体", "机车二级解体", "车体检修", "车体油漆", "上台位", "电气检修", "机制检修",
					"设备吊装", "线管连接", "转向架总成", "机车总成", "低压试验", "高压试验", "试运", "交验"));
	public List<Integer> C5Plan = new ArrayList<Integer>();
	public List<Integer> C5Real = new ArrayList<Integer>();
	public List<Integer> C5Out = new ArrayList<Integer>();

	public List<String> C6Title = new ArrayList<String>(
			Arrays.asList("入厂", "机车预检", "机车一级解体", "机车外部清洁", "转向架解体", "机车二级解体1", "机车二级解体2", "机车二级解体3", "车体检修", "车体油漆",
					"上台位", "组装1", "组装2", "组装3", "组装4", "组装5", "转向架总成", "机车总成", "低压试验", "高压试验", "试运", "交验"));
	public List<Integer> C6Plan = new ArrayList<Integer>();
	public List<Integer> C6Real = new ArrayList<Integer>();
	public List<Integer> C6Out = new ArrayList<Integer>();

	public AndonDayReportPart() {
		super();
	}

	public AndonDayReportPart(List<String> c5Title, List<Integer> c5Plan, List<Integer> c5Real, List<Integer> c5Out,
			List<String> c6Title, List<Integer> c6Plan, List<Integer> c6Real, List<Integer> c6Out) {
		super();
		C5Title = c5Title;
		C5Plan = c5Plan;
		C5Real = c5Real;
		C5Out = c5Out;
		C6Title = c6Title;
		C6Plan = c6Plan;
		C6Real = c6Real;
		C6Out = c6Out;
	}

	public List<String> getC5Title() {
		return C5Title;
	}

	public List<Integer> getC5Plan() {
		return C5Plan;
	}

	public List<Integer> getC5Real() {
		return C5Real;
	}

	public List<Integer> getC5Out() {
		return C5Out;
	}

	public List<String> getC6Title() {
		return C6Title;
	}

	public List<Integer> getC6Plan() {
		return C6Plan;
	}

	public List<Integer> getC6Real() {
		return C6Real;
	}

	public List<Integer> getC6Out() {
		return C6Out;
	}

	public void setC5Title(List<String> c5Title) {
		C5Title = c5Title;
	}

	public void setC5Plan(List<Integer> c5Plan) {
		C5Plan = c5Plan;
	}

	public void setC5Real(List<Integer> c5Real) {
		C5Real = c5Real;
	}

	public void setC5Out(List<Integer> c5Out) {
		C5Out = c5Out;
	}

	public void setC6Title(List<String> c6Title) {
		C6Title = c6Title;
	}

	public void setC6Plan(List<Integer> c6Plan) {
		C6Plan = c6Plan;
	}

	public void setC6Real(List<Integer> c6Real) {
		C6Real = c6Real;
	}

	public void setC6Out(List<Integer> c6Out) {
		C6Out = c6Out;
	}

	@Override
	public String toString() {
		return "AndonDayReportPart [C5Title=" + C5Title + ", C5Plan=" + C5Plan + ", C5Real=" + C5Real + ", C5Out="
				+ C5Out + ", C6Title=" + C6Title + ", C6Plan=" + C6Plan + ", C6Real=" + C6Real + ", C6Out=" + C6Out
				+ "]";
	}
}
