package com.poe.lewen.bean;

/**
 * socket登录返回数据
 * @author poe.Cai
 */
public class rsp_login {

	private String type;
	private String cmd;
	private String userId;
	private String roleId;
	private String userLoginRet;
	private String ierrorCode;
	private String menuShow;
	private String err;
	
	
	public rsp_login() {
		this.cmd="";
		this.err="";
		this.ierrorCode="";
		this.menuShow="";
		this.roleId="";
		this.type="";
		this.userId="";
		this.userLoginRet="";
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getUserLoginRet() {
		return userLoginRet;
	}
	public void setUserLoginRet(String userLoginRet) {
		this.userLoginRet = userLoginRet;
	}
	public String getIerrorCode() {
		return ierrorCode;
	}
	public void setIerrorCode(String ierrorCode) {
		this.ierrorCode = ierrorCode;
	}
	public String getMenuShow() {
		return menuShow;
	}
	public void setMenuShow(String menuShow) {
		this.menuShow = menuShow;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
	
}
