package com.mes.loco.aps.server.service.po.oms;

import java.io.Serializable;

/**
 * 基础数据检查
 * 
 * @author PengYouWang
 * @CreateTime 2020-7-5 14:57:34
 * @LastEditTime 2020-7-5 14:57:37
 *
 */
public class OMSCheckMsg implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 检查类型
	 */
	public int Type = 0;
	/**
	 * 提示内容
	 */
	public String Message = "";

	// 辅助属性
	public String TypeText = "";

	public OMSCheckMsg() {
	}

	public OMSCheckMsg(int type, String message, String typeText) {
		super();
		Type = type;
		Message = message;
		TypeText = typeText;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getTypeText() {
		return TypeText;
	}

	public void setTypeText(String typeText) {
		TypeText = typeText;
	}
}
