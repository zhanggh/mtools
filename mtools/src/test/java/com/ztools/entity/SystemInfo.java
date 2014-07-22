package com.ztools.entity;

import org.springframework.stereotype.Repository;

@Repository("sysInfo")
public class SystemInfo {

	public String version = "1.2v";

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
