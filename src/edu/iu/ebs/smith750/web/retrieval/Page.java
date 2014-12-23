package edu.iu.ebs.smith750.web.retrieval;

import org.apache.http.client.methods.HttpGet;

public class Page {
	private String name;
	private String url;
	
	public Page(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
	
	public HttpGet buildGetRequest() {
		return new HttpGet(getUrl());
	}
}
