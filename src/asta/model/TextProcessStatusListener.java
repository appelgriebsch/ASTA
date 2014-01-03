/**
 * A Students Text Analyzer
 */
package asta.model;

import java.util.EventListener;

/**
 * an interface that a listener to process status updates has to support
 * 
 * @author andreas.gerlach
 */
public interface TextProcessStatusListener extends EventListener {
	
	/**
	 * receives a process update event with the information like 
	 * current percentage complete, word found etc.
	 * 
	 * @param evt the event object encapsulating the process status information
	 * @author andreas.gerlach
	 */
	void update(TextProcessStatusEvent evt);
	
	/**
	 * receives a process finish event with the information like
	 * raw-text and text meta data information
	 * 
	 * @param evt the event object encapsulating the process finish information
	 * @author andreas.gerlach
	 */
	void finish(TextProcessFinishEvent evt);
}
