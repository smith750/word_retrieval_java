package edu.iu.ebs.smith750.web.retrieval;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class WordDistributions {
	public static <T> T foldWords(String s, T memo, BiFunction<T, String, T> accumulator) {
		T newMemo = memo;
		int i = 0;
		while (!isWordCharacter(s.charAt(i)) && i < s.length()) {
			i += 1;
		}
		int start = i;
		int end = 0;
		for (; i <= s.length(); i++) {
			if (!isWordCharacter(s.charAt(i))) {
				if (end < start) {
					newMemo = accumulator.apply(newMemo, s.substring(start, i).toLowerCase());
					end = i;
				}
				start = i;
			}
		}
		return newMemo;
	}
	
	public static boolean isWordCharacter(char i) {
		return (i >= 'a' && i <= 'z') || (i >= 'A' && i <= 'Z') || i == '-';
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
		/*WordCount[] counts = (WordCount[])distributions.keySet().stream()
								.map((String word) -> new WordCount(word, distributions.get(word).get()))
								.toArray();
		  Arrays.sort(counts, (WordCount a, WordCount b) -> a.getCount() - b.getCount());
		  return Arrays.asList(counts);
		*/
		List<WordCount> counts = new ArrayList<>();
		for (String word : distributions.keySet()) {
			counts.add(new WordCount(word, distributions.get(word).get()));
		}
		Collections.sort(counts, (WordCount a, WordCount b) -> b.getCount() - a.getCount());
		/*WordCount[] counts = 
				(WordCount[])distributions.keySet().stream()
					.map((String word) -> new WordCount(word, distributions.get(word).get()))
					.sorted((WordCount a, WordCount b) -> a.getCount() - b.getCount()).toArray();*/
		return counts;
	}
}
