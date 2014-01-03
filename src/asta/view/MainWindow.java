/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import asta.model.TextMetaData;
import asta.model.WordPosition;

/**
 * the main window of the application that hosts the
 * different controls and panels in the defined position
 * it is also used for communicating any changes to the text meta information
 * to the contained controls
 * 
 * @author andreas.gerlach
 * @author andreas.p&ouml;rtner
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	/**
	 * member to access the toolbar view
	 */
	private ToolbarView _toolbarView = null;

	/**
	 * member to access the text view
	 */
	private TextView _textView = null;

	/**
	 * member to access the statistic view
	 */
	private StatisticView _statisticView = null;

	/**
	 * member to access the status view
	 */
	private StatusView _statusView = null;

	/**
	 * constructs a new main window and initializes its UI
	 * 
	 * @author andreas.gerlach
	 */
	public MainWindow() {

		initializeUI();
	}

	/**
	 * initialize the main window layout, constructs the neccessary controls
	 * and place them at the correct location
	 * 
	 * @author andreas.gerlach
	 */
	private void initializeUI() {

		setLayout(new BorderLayout(5, 5));

		// add toolbar on north position of window
		_toolbarView = new ToolbarView();
		this.add(_toolbarView, BorderLayout.NORTH);

		// add text view on center position of window
		_textView = new TextView();
		this.add(_textView, BorderLayout.CENTER);

		// add the statistic view on east position of window
		_statisticView = new StatisticView();
		this.add(_statisticView, BorderLayout.EAST);

		// add the status view on the south position of window
		_statusView = new StatusView();
		this.add(_statusView, BorderLayout.SOUTH);
	}

	/**
	 * informs the status bar view about the file that has been selected
	 * so that it can display those information
	 * 
	 * @param fileName the name of the file that has been selected
	 * @author andreas.p&ouml;rtner
	 */
	public void setSelectedFile(String fileName) {
		
		_statusView.setSelectedFile(fileName);

		clearSubViews();
	}
	
	/**
	 * informs the status bar view about the file analyzing progress so that
	 * it can update the progress bar and status information
	 * 
	 * @param nPercentage the number in percentage that the analyze process has been completed
	 * @param message the status information that should be displayed 
	 * @author andreas.gerlach
	 */
	public void setProcessInformation(int nPercentage, String message) {

		_statusView.setProgressInformation(nPercentage, message);
	}

	/**
	 * informs the contained controls about the finishing of the text analyzing so 
	 * that they can use the text meta data information to display the current results
	 * 
	 * @param text the text that has been analyzed
	 * @param data the text meta data of that text
	 * @author andreas.gerlach
	 */
	public void setProcessResult(StringBuffer text, TextMetaData data) {

		updateMetaData(data);
				
		_textView.setText(text);		
		_toolbarView.setProcessFinished();
	}
	
	/**
	 * informs the statistic and status view about updates done on the meta data information 
	 * object - e.g. by ignoring a word from the top-n-list.
	 * this will result in a refresh of the controls
	 * 
	 * @param data the updated text meta data information object
	 * @author andreas.p&ouml;rtner
	 */
	public void updateMetaData(TextMetaData data) {
		
		_statisticView.setTextMetaData(data);
		_statusView.setTextMetaData(data);
	}
	
	/**
	 * informs the text view about the positions of the word that has been selected
	 * so that it can use the information to highlight the words in the text
	 * 
	 * @param wordPositions an array holding the positions of the word being selected
	 * @author andreas.gerlach
	 */
	public void highlightWord(WordPosition[] wordPositions) {
		
		_textView.highlight(wordPositions);
	}
	
	/**
	 * informs the text view about removing the current highlights because the word selection
	 * has changed or the word that has been selected before will be going to be ignored
	 * 
	 * @author andreas.gerlach
	 */
	public void removeHighlights() {
		
		_textView.removeHighlights();
	}
	
	/**
	 * clears the values in the sub-views and initializes
	 * the sub view content with some meaningful default values
	 * 
	 * @author andreas.gerlach
	 */
	void clearSubViews() {
		
		_textView.setText(new StringBuffer());
		_statusView.setProgressInformation(0, "");
		_statisticView.clearValues();
	}
}
