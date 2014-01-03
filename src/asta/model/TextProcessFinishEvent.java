/**
 * A Students Text Analyzer
 */
package asta.model;

import java.util.EventObject;

/**
 * the event structure for process status updates
 * 
 * @author andreas.gerlach
 */
@SuppressWarnings("serial")
public class TextProcessFinishEvent extends EventObject {

	/**
	 * the raw-text of the file being analyzed
	 */
	private StringBuffer _text = null;
	
	/**
	 * the text meta information of the file being analyzed
	 */
	private TextMetaData _data = null;
	
	/**
	 * initializes a new event object with the text and meta data information given
	 * 
	 * @param source the source of the event
	 * @param text the raw-text of the file being analyzed
	 * @param data the meta data information of the file being analyzed
	 * @author andreas.gerlach
	 */
	public TextProcessFinishEvent(Object source, StringBuffer text, TextMetaData data) {
		
		super(source);
		
		_text = text;
		_data = data;
	}
	
	/**
	 * getter to retrieve the raw-text of the file being analyzed
	 * 
	 * @return the raw-text of the file being analyzed
	 * @author andreas.gerlach
	 */
	public StringBuffer getText() {
		
		return _text;
	}

	/**
	 * getter to retrieve the meta data of the file being analyzed
	 * 
	 * @return the meta data of the file being analyzed
	 * @author andreas.gerlach
	 */
	public TextMetaData getMetaData() {
		
		return _data;
	}
}
