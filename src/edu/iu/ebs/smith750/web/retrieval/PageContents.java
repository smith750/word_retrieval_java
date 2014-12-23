package edu.iu.ebs.smith750.web.retrieval;

public class PageContents {
	private Page page;
	private String contents;
	private Exception failure;
	
	public PageContents(Page page, String contents) {
		this.page = page;
		this.contents = contents;
	}
	
	public PageContents(Page page, Exception failure) {
		this.page = page;
		this.failure = failure;
	}

	public Page getPage() {
		return page;
	}
	
	public boolean succeeded() {
		return failure == null;
	}

	public String getContents() {
		return contents;
	}
	
	public Exception getFailure() {
		return failure;
	}
	
	public String getFailureMessage() {
		return failure.getMessage();
	}
	
}
