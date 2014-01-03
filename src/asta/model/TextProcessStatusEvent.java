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
public class TextProcessStatusEvent extends EventObject {

	/**
	 * the message to be displayed on screen
	 */
	private String _message = null;
	
	/**
	 * the percentage that has been completed
	 */
	private int _percentage = 0;
	
	/**
	 * initializes a new event object with the percentage and message specified
	 * 
	 * @param source the source of the event
	 * @param percentage the percentage completed
	 * @param message the actual message for the client
	 * @author andreas.gerlach
	 */
	public TextProcessStatusEvent(Object source, int percentage, String message) {
		
		super(source);
		
		_percentage = percentage;
		_message = message;
	}

	/**
	 * getter to retrieve the current percentage completed
	 * 
	 * @return the current percentage completed
	 * @author andreas.gerlach
	 */
	public int getPercentage() {
		
		return _percentage;
	}
	
	/**
	 * getter to retrieve the current message for the client
	 * 
	 * @return the current message for the client
	 * @author andreas.gerlach
	 */
	public String getMessage() {
		
		return _message;
	}
}
