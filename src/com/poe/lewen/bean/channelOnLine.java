package com.poe.lewen.bean;

public class channelOnLine {

	private String device_name; //设备名称
	
	private String device_type;//设备类型
	
	private String userName;//设备登录用户
	
	private String userPsw;//设备登录密码
	
	private String device_ipAddr;//设备IP
	
	private String player_Addr;//播放地址
	
	private String device_portNo;//设备端口号
	
	private String channelNo;//通道编号
	
	private String channelName;//通道名称
	
	private String playerAddrType;//播放地址类型
	
	/**
	 * 对应服务器的 channel_id
	 */
	private String device_id;//设备编号

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPsw() {
		return userPsw;
	}

	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}

	public String getDevice_ipAddr() {
		return device_ipAddr;
	}

	public void setDevice_ipAddr(String device_ipAddr) {
		this.device_ipAddr = device_ipAddr;
	}

	public String getPlayer_Addr() {
		return player_Addr;
	}

	public void setPlayer_Addr(String player_Addr) {
		this.player_Addr = player_Addr;
	}

	public String getDevice_portNo() {
		return device_portNo;
	}

	public void setDevice_portNo(String device_portNo) {
		this.device_portNo = device_portNo;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getPlayerAddrType() {
		return playerAddrType;
	}

	public void setPlayerAddrType(String playerAddrType) {
		this.playerAddrType = playerAddrType;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	
}
