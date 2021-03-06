package com.vtw.deploy.script.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gmdadmin")
public class GmdadminConfig {

	private String host;
	private String username;
	private String password;
	private String tbwappWarPath;
	private String xmlSources;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTbwappWarPath() {
		return tbwappWarPath;
	}

	public void setTbwappWarPath(String tbwappWarPath) {
		this.tbwappWarPath = tbwappWarPath;
	}

	public String getXmlSources() {
		return xmlSources;
	}

	public void setXmlSources(String xmlSources) {
		this.xmlSources = xmlSources;
	}

	
}
