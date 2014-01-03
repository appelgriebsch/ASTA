/**
 * A Students Text Analyzer
 */
package asta.model;

import javax.swing.event.EventListenerList;

/**
 * the generic text processing strategy that evaluates plain text and informs
 * about words found within it
 * 
 * @author andreas.gerlach
 */
public class GenericTextProcessingStrategy implements ITextProcessingStrategy {

	/**
	 * the event handlers that has been registered to receive status updates
	 */
	private EventListenerList listeners = new EventListenerList();

	/**
	 * analyze the file content and prepares the text meta data information
	 * based on the words, characters and punctuation found in the file
	 * 
	 * @param file the file handler that knows how to read the file
	 * @return the file meta information like number of words, chars etc.
	 * 
	 * @author andreas.gerlach
	 */
	public TextMetaData parse(IFileHandler file) {

		TextMetaData metaData = new TextMetaData();
		StringBuilder bldWord = new StringBuilder();

		// the caret position holds the current position in the text stream
		// and is used to store the position of the words that have been found
		// which is useful information to highlight the words later on
		long caretPosition = 0;
		
		while (file.hasNext()) {

			// we iterate over the file content (which is obviously text based)
			String filePart = file.next();
			
			for (int i = 0; i < filePart.length(); ++i) {

				char c = filePart.charAt(i);
				boolean isWordFinisihed = false;

				int type = Character.getType(c);

				// check for a punctuation character
				switch (type) {
				
				case Character.START_PUNCTUATION:
				case Character.INITIAL_QUOTE_PUNCTUATION:
				case Character.FINAL_QUOTE_PUNCTUATION:
				case Character.END_PUNCTUATION:
				case Character.DASH_PUNCTUATION:
				case Character.CONNECTOR_PUNCTUATION:
				case Character.SPACE_SEPARATOR:
				case Character.LINE_SEPARATOR:
				case Character.PARAGRAPH_SEPARATOR:
				case Character.CONTROL:
					
					isWordFinisihed = true;
					break;
					
				case Character.OTHER_PUNCTUATION:

					// if we found a punctuation between a letter or digit
					// aka 1.2.3 or V.1.2 or something like this
					// we will see this as a single word
					if ((i > 0)
							&& (i != filePart.length() - 1)
							&& Character
									.isLetterOrDigit(filePart.charAt(i - 1))
							&& Character
									.isLetterOrDigit(filePart.charAt(i + 1))) {

						isWordFinisihed = false;
					}
					else {
						
						isWordFinisihed = true;
					}
					break;

				default:

					isWordFinisihed = false;
					break;
				}

				// if we figured out that we have finished one word
				if (isWordFinisihed) {

					if (bldWord.length() > 0) {

						// we have to store this information in the meta data
						String newWord = bldWord.toString();
						
						WordPosition pos = new WordPosition(caretPosition + i - newWord.length(), newWord);
						
						metaData.addWord(newWord, pos);

						// and inform the UI about the process update
						int percentage = (int) (pos.getEndPosition() * 100 / file.getFileLength());
						
						String message = String.format("Wort \"%s\" gefunden",
								newWord);

						notifyTextProcessStatusUpdate(new TextProcessStatusEvent(
								this, percentage, message));

						bldWord = new StringBuilder();
					}
					
					// when the word is finished, we have examined a valid punctuation character before
					// which is the signal to the text processing strategy that the word is finished
					metaData.addPunctuation(c);
					
				} else {

					// otherwise we are still in-between the word and have to build it 
					// char-by-char
					bldWord.append(c);
				}
			}
			
			caretPosition += filePart.length();
		}

		// notify that we will have to sort the list of examined words
		// based on their frequency, which might take a while
		notifyTextProcessStatusUpdate(new TextProcessStatusEvent(this, -1,
				"Sortiere Wortliste..."));

		metaData.sortWordsByFrequency();

		// after all the process is finished, so refresh the UI here
		notifyTextProcessStatusFinish(new TextProcessFinishEvent(this,
				file.getPlainText(), metaData));
		
		return metaData;
	}

	/**
     * add a new listener to the list of listeners connected to the status update events
     * 
     * @param listener the listener object that wants to receive status updates
     * @author andreas.gerlach
     */
	@Override
	public void addTextProcessListener(TextProcessStatusListener listener) {

		listeners.add(TextProcessStatusListener.class, listener);
	}

	/**
	 * remove a listener from the list of connected listeners
	 * 
	 * @param listener the listener object that don't want to receive status updates any longer
	 * @author andreas.gerlach
	 */
	@Override
	public void removeTextProcessListener(TextProcessStatusListener listener) {

		listeners.remove(TextProcessStatusListener.class, listener);
	}

	/**
	 * overridden toString representation of the object
	 * this is used in the UI to fill the combo-box of available processing strategies
	 * the operator can choose from
	 * 
	 * @return the meaningful name of this processing strategy
	 * @author andreas.gerlach
	 */
	@Override
	public String toString() {
		
		return "Standard Text Analyse";
	}
	
	/**
	 * sends out a message to any registered listener for process status updates
	 * 
	 * @param evt the event containing the current percentage completed and the word found
	 * @author andreas.gerlach
	 */
	void notifyTextProcessStatusUpdate(TextProcessStatusEvent evt) {

		for (TextProcessStatusListener l : listeners
				.getListeners(TextProcessStatusListener.class)) {

			l.update(evt);
		}
	}

	/**
	 * sends out a message to any registered listener for process finished
	 * 
	 * @param text the raw-text that has been read
	 * @param metaData the meta information of the content like number of words, chars etc.
	 * @author andreas.gerlach
	 */
	void notifyTextProcessStatusFinish(TextProcessFinishEvent evt) {

		for (TextProcessStatusListener l : listeners
				.getListeners(TextProcessStatusListener.class)) {

			l.finish(evt);
		}
	}
}
