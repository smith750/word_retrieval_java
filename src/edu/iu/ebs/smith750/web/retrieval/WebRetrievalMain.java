package edu.iu.ebs.smith750.web.retrieval;

import java.util.ArrayList;
import java.util.List;

public class WebRetrievalMain {
	public static final int HIGH_COUNT = 100;
	
	public static void main(String[] args) {
		final long start = System.currentTimeMillis();
		List<Page> pages = new ArrayList<>();
		pages.add(new Page("Wordsworth Poetry 6", "http://gutenberg.pglaf.org/4/7/6/5/47651/47651-0.txt"));
		pages.add(new Page("Great Expectations", "http://gutenberg.pglaf.org/1/4/0/1400/1400.txt"));
		pages.add(new Page("Moby-Dick", "http://gutenberg.pglaf.org/2/7/0/2701/2701.txt"));
		pages.add(new Page("Emma", "http://gutenberg.pglaf.org/1/5/158/158.txt"));
		
		TextRetriever.retrieveFullText(pages, (PageContents c) -> {
			if (c.succeeded()) {
				synchronized (c) {
					System.out.println(c.getPage().getName()+" succeeded.");
					final List<WordCount> distributions = WordDistributions.calculateWordDistributions(c.getContents());
					//System.out.println("Distinct word count: "+distributions.size());
					final int highCount = (distributions.size() < HIGH_COUNT) ? distributions.size() : HIGH_COUNT;
					for (int i = 0; i < highCount; i++) {
						System.out.println(buildWordCountMessage(i+1, distributions.get(i)));
					}
				}
			} else {
				System.out.println(c.getPage().getName()+" failed. Exception: "+c.getFailureMessage());
			}
		});
		
		final long end = System.currentTimeMillis();
		System.out.println("Time to complete = "+(end-start)+"ms");
	}
	
	private static String buildWordCountMessage(int count, WordCount wordCount) {
		StringBuilder s = new StringBuilder();
		s.append(count);
		s.append(": ");
		s.append(wordCount.getWord());
		s.append(" (");
		s.append(wordCount.getCount());
		s.append(')');
		return s.toString();
	}
}
