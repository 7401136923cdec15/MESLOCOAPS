package com.mes.loco.aps.server.service.po.wms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 产线工位领料需求集合
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-11 13:51:10
 */
public class WMSLinePartLLs implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public List<WMSLinePartLL> header = new ArrayList<WMSLinePartLL>();

	public WMSLinePartLLs() {
		super();
	}

	public WMSLinePartLLs(List<WMSLinePartLL> header) {
		super();
		this.header = header;
	}

	public List<WMSLinePartLL> getHeader() {
		return header;
	}

	public void setHeader(List<WMSLinePartLL> header) {
		this.header = header;
	}
}
