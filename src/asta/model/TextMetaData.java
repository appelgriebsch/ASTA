/**
 * A Students Text Analyzer
 */
package asta.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import asta.controller.AppController;

/**
 * the meta data information model for the text analyzer
 * holds the statistic values of the file that has been examined
 * 
 * @author andreas.gerlach
 * @author andreas.p&ouml;rtner
 */
public class TextMetaData {

	/**
	 * the map holding the word examined and their frequency counter
	 */
	private Map<String, Integer> _mapWordFrequencies = new HashMap<String, Integer>();

	/**
	 * the map holding the positions of the words found in the text 
	 * this is later used to highlight the words in the text
	 */
	private Map<String, ArrayList<WordPosition>> _mapWordPositions = 
										new HashMap<String, ArrayList<WordPosition>>();
	
	/**
	 * the array of words found in the text sorted by their frequency
	 */
	private ArrayList<String> _arrWordsByFrequency = null;
	
	/**
	 * holds the number of vowels found in the text 
	 */
	private int _nmbrOfVowels = 0;

	/**
	 * holds the number of consonants found in the text
	 */
	private int _nmbrOfConsonants = 0;

	/**
	 * holds the number of punctuation found in the text
	 */
	private int _nmbrOfPunctuations = 0;

	/**
	 * holds the number of alpha-numerical chars found in the text
	 */
	private int _nmbrOfAlphaChars = 0;

	/**
	 * holds the number of all characters (non-punctuation chars) in the text
	 */
	private int _nmbrOfAllChars = 0;

	/**
	 * an array defining the chars that counts as vowels
	 */
	private final char[] arrVowels = new char[] { 'a', 'e', 'i', 'o', 'u', 
												  '\u00E4', '\u00F6', '\u00FC' };

	/**
	 * getter to retrieve the number of characters in the text
	 * 
	 * @return the number of characters in the text
	 * @author andreas.p&ouml;rtner
	 */
	public int getNumberOfCharacters() {

		return _nmbrOfAllChars;
	}

	/**
	 * getter to retrieve the number of alpha-numerical characters in the text
	 * 
	 * @return the number of alpha-numerical characters in the text
	 * @author andreas.p&ouml;rtner
	 */
	public int getNumberOfAlphaCharacters() {

		return _nmbrOfAlphaChars;
	}

	/**
	 * getter to retrieve the number of consonants in the text
	 * 
	 * @return the number of consonants in the text
	 * @author andreas.p&ouml;rtner
	 */
	public int getNumberOfConsonants() {

		return _nmbrOfConsonants;
	}

	/**
	 * getter to retrieve the number of punctuation in the text
	 * 
	 * @return the number of punctuation in the text
	 * @author andreas.p&ouml;rtner
	 */
	public int getNumberOfPunctuation() {

		return _nmbrOfPunctuations;
	}

	/**
	 * getter to retrieve the number of vowels in the text
	 * 
	 * @return the number of vowels in the text
	 * @author andreas.p&ouml;rtner
	 */
	public int getNumberOfVowels() {

		return _nmbrOfVowels;
	}

	/**
	 * getter to retrieve the number of words in the text
	 * 
	 * @return the number of words in the text
	 * @author andreas.gerlach
	 */
	public int getNumberOfWords() {

		int n = 0;

		for (Integer i : _mapWordFrequencies.values()) {

			n += i.intValue();
		}

		return n;
	}

	/**
	 * builds a sorted array of the words in the hash-map
	 * the array is sorted by the frequency of the words in the hash-map
	 * 
	 * @author andreas.gerlach
	 */
	void sortWordsByFrequency() {
		
		_arrWordsByFrequency = new ArrayList<String>(_mapWordFrequencies.keySet());
		Collections.sort(_arrWordsByFrequency, new WordFrequencyComparator(this));
	}
	
	/**
	 * getter to retrieve the list of words by frequency
	 * it will take the list of ignorable words from the global settings into account
	 * and remove those words from the return value
	 * 
	 * @return the list of words by frequency reduced by the ignorable words from the settings
	 * @author andreas.gerlach
	 */
	public String[] getWordsByFrequency() {

		// hash-map has to be sorted before (if not been done already)
		if (_arrWordsByFrequency == null)
			sortWordsByFrequency();
		
		ArrayList<String> tempList = new ArrayList<String>(_arrWordsByFrequency);
		
		for(String w: AppController.getInstance().getSettings().getIgnorableWords()) {
			
			if (tempList.contains(w)) {
				
				tempList.remove(w);
			}
		}

		return tempList.toArray(new String[0]);
	}

	/**
	 * getter to retrieve a top n list of words sorted by frequency
	 * internally it will use the {@link #getWordsByFrequency} operation
	 * 
	 * @param nmbrOfWords the number of words to retrieve from the list of words
	 * @return the list of words sorted by frequency
	 * @author andreas.gerlach
	 */
	public String[] getTopWordsByFrequency(int nmbrOfWords) {

		return Arrays.copyOf(getWordsByFrequency(), nmbrOfWords);
	}

	/**
	 * getter to retrieve the frequency value of a specific word from the text
	 * 
	 * @param word the word which frequency is of interest
	 * @return the frequency value of the word
	 * @author andreas.gerlach
	 */
	public int getFrequencyOfWord(String word) {

		int i = 0;

		if (_mapWordFrequencies.containsKey(word)) {

			i = _mapWordFrequencies.get(word).intValue();
		}

		return i;
	}

	/**
	 * getter to retrieve the position of the given word in the text
	 * 
	 * @param word the word which positions are of interest
	 * @return an array of the positions of the word in the text used for highlighting it
	 * @author andreas.gerlach
	 */
	public WordPosition[] getWordPositions(String word) {
		
		if (!_mapWordPositions.containsKey(word))
			return new WordPosition[0];
		
		return _mapWordPositions.get(word).toArray(new WordPosition[0]);
	}
	
	/**
	 * adds a word into the hash-map of examined words and adds the number
	 * of chars, alpha-chars, vowels and consonants of this word to the counters
	 * 
	 * @param word the word to be added and evaluated
	 * @param position the position where the word has been found in the text
	 * @author andreas.gerlach
	 */
	void addWord(String word, WordPosition position) {

		// check if hash-map contains already a value for the word
		if (!_mapWordFrequencies.containsKey(word)) {
			_mapWordFrequencies.put(word, new Integer(1));
		} else {

			int nVal = _mapWordFrequencies.get(word).intValue();
			_mapWordFrequencies.put(word, new Integer(++nVal));
		}

		// check if the map of word positions already contains a value for this word
		if (!_mapWordPositions.containsKey(word)) {
			_mapWordPositions.put(word, new ArrayList<WordPosition>());
		}
		
		_mapWordPositions.get(word).add(position);
		
		// examine and add the number of chars, alpha-chars, ... to the specific counters
		_nmbrOfAllChars += word.length();

		for (char c : word.toCharArray()) {

			if (Character.isDigit(c)) {

				continue;
			}

			_nmbrOfAlphaChars++;

			if (Arrays.binarySearch(arrVowels, Character.toLowerCase(c)) < 0) {

				_nmbrOfConsonants++;
			} else {

				_nmbrOfVowels++;
			}
		}
	}

	/**
	 * when examined a punctuation in the text used this method to increase the
	 * counter accordingly
	 * 
	 * @param c the punctuation char found in the text
	 * @author andreas.p&ouml;rtner
	 */
	void addPunctuation(char c) {

		_nmbrOfPunctuations++;
		_nmbrOfAllChars++;
	}

	/**
	 * compares the frequencies of two words
	 * and is used to sort the array of available words in the text
	 * 
	 * @author andreas.gerlach
	 */
	class WordFrequencyComparator implements Comparator<String> {

		/**
		 * a reference to the text meta data to retrieve the
		 * frequency of a given word
		 */
		private TextMetaData _metaData = null;

		/**
		 * constructs a new instance of a word frequency comparator and specifies
		 * the text meta data object to use
		 * 
		 * @param metaData the text meta data object to use for getting the frequencies of words
		 * @author andreas.gerlach
		 */
		public WordFrequencyComparator(TextMetaData metaData) {

			_metaData = metaData;
		}

		/**
		 * compares to strings by querying for their frequencies in the text meta data information
		 * object and evaluating those values
		 * 
		 * @param o1 the first string to check
		 * @param o2 the second string to check
		 * @return the result of the comparison (1 = lesser, 0 = equality or -1 = higher)
		 * @author andreas.gerlach
		 */
		public int compare(String o1, String o2) {

			int nFreqO1 = _metaData.getFrequencyOfWord(o1);
			int nFreqO2 = _metaData.getFrequencyOfWord(o2);

			if (nFreqO1 < nFreqO2)
				return 1;
			else if (nFreqO1 == nFreqO2)
				return 0;
			else
				return -1;
		}
	}
}
