package edu.iu.ebs.smith750.web.retrieval;

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
}
