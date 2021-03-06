package edu.iu.ebs.smith750.web.retrieval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class WordDistributions {
	public static <T> T foldWords(String s, T memo, BiFunction<T, String, T> accumulator) {
		if (s == null || s.length() == 0) {
			return memo;
		}
		
		T newMemo = memo;
		int start = 0;
		boolean wordMode = false;
		for (int i = 0; i < s.length(); i++) {
			if (!isWordCharacter(s.charAt(i))) {
				if (wordMode) {
					newMemo = accumulator.apply(newMemo, s.substring(start, i).toLowerCase());
				}
				start = i+1;
				wordMode = false;
			} else {
				wordMode = true;
			}
		}
		if (isWordCharacter(s.charAt(s.length()-1))) {
			newMemo = accumulator.apply(newMemo, s.substring(start).toLowerCase());
		}
		return newMemo;
	}
	
	public static boolean isWordCharacter(char i) {
		return (i >= 'a' && i <= 'z') || (i >= 'A' && i <= 'Z') || i == '-' || i == '\'';
	}
	
	public static Map<String, AtomicInteger> generateWordDistributions(String s) {
		HashMap<String, AtomicInteger> distributions = foldWords(s, new HashMap<String, AtomicInteger>(), (HashMap<String, AtomicInteger> memo, String word) -> {
			AtomicInteger count = memo.get(word);
			if (count == null) {
				count = new AtomicInteger(1);
				memo.put(word, count);
			} else {
				count.incrementAndGet();
			}
			return memo;
		});
		
		return distributions;
	}
	
	public static List<WordCount> calculateWordDistributions(String s) {
		final Map<String, AtomicInteger> distributions = generateWordDistributions(s);
		Object[] counts = distributions.keySet().parallelStream()
								.map((String word) -> new WordCount(word, distributions.get(word).get()))
								.sorted((WordCount a, WordCount b) -> b.getCount() - a.getCount())
								.toArray();
		List<WordCount> wordCounts = new ArrayList<WordCount>();
		for (Object count : counts) {
			wordCounts.add((WordCount)count);
		}
		return wordCounts;
	}
}
