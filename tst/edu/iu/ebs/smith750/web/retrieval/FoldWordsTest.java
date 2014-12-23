package edu.iu.ebs.smith750.web.retrieval;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

public class FoldWordsTest {

	@Test
	public void testfoldWords() {
		final String test1 = "A happy fox";
		assertForFoldWords(test1);
		
		final String test2 = "  A     hapPy FOx   ";
		assertForFoldWords(test2);
		
		final String test3 = null;
		final ArrayList<String> words = WordDistributions.foldWords(test3, new ArrayList<String>(), (ArrayList<String> memo, String word)-> { memo.add(word); return memo; });
		Assert.assertEquals("result should be 0 words", 0, words.size());
		
		final String test4 = null;
		final ArrayList<String> words2 = WordDistributions.foldWords(test4, new ArrayList<String>(), (ArrayList<String> memo, String word)-> { memo.add(word); return memo; });
		Assert.assertEquals("result should be 0 words", 0, words2.size());
		
		final String test5 = "  , ! ~~ A,!^^happy*FOX";
		assertForFoldWords(test5);
	}
	
	protected void assertForFoldWords(String s) {
		final ArrayList<String> words = WordDistributions.foldWords(s, new ArrayList<String>(), (ArrayList<String> memo, String word)-> { memo.add(word); return memo; });
		Assert.assertEquals("result should be 3 words", 3, words.size());
		Assert.assertEquals("first result should be 'a'", "a", words.get(0));
		Assert.assertEquals("last result should be 'fox'", "fox", words.get(2));
	}

}
