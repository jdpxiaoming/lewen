package com.poe.lewen.bean;

public class history_video {

	private String deviceId;
	private String channelId;
	private String playaddr;//rtmp地址
	private String err;//错误代码 0
	private String errdesc;//错误描述
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getPlayaddr() {
		return playaddr;
	}
	public void setPlayaddr(String playaddr) {
		this.playaddr = playaddr;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
	public String getErrdesc() {
		return errdesc;
	}
	public void setErrdesc(String errdesc) {
		this.errdesc = errdesc;
	}
}
