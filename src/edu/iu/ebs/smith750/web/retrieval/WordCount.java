package edu.iu.ebs.smith750.web.retrieval;

public class WordCount {
	private String word;
	private int count;
	
	public WordCount(String word, int count) {
		this.word = word;
		this.count = count;
	}

	public String getWord() {
		return word;
	}

	public int getCount() {
		return count;
	}
	
	public String toString() {
		return word+": "+count;
	}
}
