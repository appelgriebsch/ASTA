/**
 * A Students Text Analyzer
 */
package asta.model;

/**
 * class for storing a single position of a word in the text that is being examined
 * 
 * @author andreas.gerlach
 */
public class WordPosition {

	/**
	 * the start position of the word in the text
	 */
	private long _startPos = -1;
	
	/**
	 * the end position of the word in the text
	 */
	private long _endPos = -1;
	
	/**
	 * constructs a new position for a specific word
	 * 
	 * @param startPos the position in the text where the word starts
	 * @param word the word itself (used for calculating the end position based on the word's length)
	 * @author andreas.gerlach
	 */
	public WordPosition(long startPos, String word) {
		
		_startPos = startPos;
		_endPos = _startPos + word.length();
	}
	
	/**
	 * getter to retrieve the start position of the word
	 * 
	 * @return the start position of the word
	 * @author andreas.gerlach
	 */
	public long getStartPosition() {
		
		return _startPos;
	}
	
	/**
	 * getter to retrieve the end position of the word
	 * 
	 * @return the end position of the word
	 * @author andreas.gerlach
	 */
	public long getEndPosition() {
		
		return _endPos;
	}
}
