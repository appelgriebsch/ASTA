/**
 * A Students Text Analyzer
 */
package asta.model;

/**
 * an interface specification for a text processing
 * strategy
 *
 * @author andreas.gerlach
 */
public interface ITextProcessingStrategy {

    /**
     * parses a junk of data and informs about
     * words, punctuation and special keywords found
     * 
     * @param file a reference to the file handler for accessing the files
     *              content
     *              
     * @return the text meta information generated
     * @author andreas.gerlach
     */
    public TextMetaData parse(IFileHandler file);
    
    /**
     * add a new listener to the list of listeners connected to the status update events
     * 
     * @param listener the listener object that wants to receive status updates
     * @author andreas.gerlach
     */
    public void addTextProcessListener(TextProcessStatusListener listener);
    
    /**
	 * remove a listener from the list of connected listeners
	 * 
	 * @param listener the listener object that don't want to receive status updates any longer
	 * @author andreas.gerlach
	 */
    public void removeTextProcessListener(TextProcessStatusListener listener);
    
    /**
	 * overridden toString representation of the object
	 * this is used in the UI to fill the combo-box of available processing strategies
	 * the operator can choose from
	 * 
	 * @return the meaningful name of this processing strategy
	 * @author andreas.gerlach
	 */
    @Override
    public String toString();
}
