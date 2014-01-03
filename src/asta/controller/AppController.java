/**
 * A Students Text Analyzer
 */
package asta.controller;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import asta.model.IFileHandler;
import asta.model.ITextProcessingStrategy;
import asta.model.Settings;
import asta.model.TextMetaData;
import asta.model.TextProcessFinishEvent;
import asta.model.TextProcessStatusEvent;
import asta.model.TextProcessStatusListener;
import asta.view.MainWindow;

/**
 * The Controller of the application implemented as singleton
 * The controller is the glue code between the model and the view
 * 
 * @author andreas.gerlach
 * @author andreas.p&ouml;rtner
 */
@SuppressWarnings("deprecation")
public class AppController implements TextProcessStatusListener {

	/**
	 * required static member for singleton pattern
	 */
	private static AppController _instance = null;

	/**
	 * the main window reference to notify the view about
	 * updates of the analyzing process
	 */
	private MainWindow _mainWnd = null;

	/**
     * the file handler instance that knows how to read
     * the file object selected by the user
     */
	private IFileHandler _fileHandler = null;

	/***
	 * the result of the analyzing process
	 */
	private TextMetaData _theMetaData = null;

	/**
     * the reference to the thread that performs the
     * analyze. its needed to stop the analyzing process
     * in-between
     */
	private Thread _textProcessingThread = null;

	/**
	 * a central place to store application specific settings
	 * 
	 * for future enhancements: provide a UI to configure the settings
	 * and store / reload them
	 */
	private Settings _theSettings = null;

	/**
	 * a reference to the selected word
	 */
	private String _theSelectedWord = null;
	
	/**
	 * the private constructor of the controller
	 * (as described in the Singleton pattern)
	 * 
	 * @author andreas.gerlach
	 */
	private AppController() {

		_theSettings = new Settings();
	}

	/**
	 * the singleton pattern always returns the same instance of an object
	 * therefore there is a static variable holding the singleton instance 
	 * 
	 * @return the only one instance of the controller
	 * @author andreas.gerlach
	 */
	public static AppController getInstance() {

		if (_instance == null)
			_instance = new AppController();

		return _instance;
	}

	/**
	 * initialize the main window and shows it on screen
	 * the minimum size of the window is set to 800x600 pixel
	 * 
	 * @param mainWnd the main window to display on screen
	 * @author andreas.p&ouml;rtner
	 */
	public void run(MainWindow mainWnd) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
			AppController.getInstance().handleException(ex);
		} catch (InstantiationException ex) {
			AppController.getInstance().handleException(ex);
		} catch (IllegalAccessException ex) {
			AppController.getInstance().handleException(ex);
		} catch (UnsupportedLookAndFeelException ex) {
			AppController.getInstance().handleException(ex);
		}

		_mainWnd = mainWnd;
		_mainWnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_mainWnd.setTitle("ASTA - A Student's Text Analyzer");
		_mainWnd.setMinimumSize(new Dimension(800, 600));
		_mainWnd.setVisible(true);
	}

	/**
	 * initializes and starts the background thread to
	 * analyze the content of the file
	 * 
	 * @param strategy the strategy used to analyze the file content
	 * @throws InterruptedException the thread was aborted
	 * 
	 * @author andreas.gerlach
	 */
	public void analyzeFile(ITextProcessingStrategy strategy) throws InterruptedException {

		// check if an old thread is still running and wait until it is finished
		if (_textProcessingThread != null) {

			if (_textProcessingThread.isAlive())
				_textProcessingThread.join();

			_textProcessingThread = null;
		}

		_theMetaData = null;
		_theSelectedWord = null;
		
		_textProcessingThread = new Thread(new WorkerThread(_fileHandler, strategy));
		_textProcessingThread.start();
	}

	/**
	 * getter to access the Main Window object
	 * 
	 * @return the main window object of the swing application
	 * @author andreas.p&ouml;rtner
	 */
	public MainWindow getMainWindow() {

		return _mainWnd;
	}

	/**
	 * checks the existing file handler for support to open and load
	 * the file given and if possible initializes the correct file handler object
	 * with the file
	 * 
	 * @param selectedFile the file that is selected to be analyzed
	 * @throws IOException if the file was not found or is not readable
	 * @author andreas.gerlach
	 */
	public void openFile(File selectedFile) throws IOException {

		// check which one of the existing file handler supports the given
		// file type
		for (IFileHandler fileHandler : getSettings().getAvailableFileHandler()) {

			if (fileHandler.getFileFilter().accept(selectedFile)) {

				_fileHandler = fileHandler;
				break;
			}
		}

		// if found a file handler, do the initialization of it
		if (_fileHandler != null) {

			try {

				_fileHandler.setFile(selectedFile.getAbsolutePath());
				getMainWindow().setSelectedFile(selectedFile.getName());

			} catch (IOException e) {

				_fileHandler = null;
				throw e;
			}
		}
	}

	/**
	 * getter to access the actual file handler for opening and reading the file content
	 * 
	 * @return the actual file handler
	 * @author andreas.gerlach
	 */
	public IFileHandler getCurrentFile() {

		return _fileHandler;
	}

	/**
	 * shows a dialog with the message of the exception that has occurred
	 * additionally it will stop the analyzing process
	 * 
	 * @param ex the exception that has occurred
	 * @author andreas.p&ouml;rtner
	 */
	public void handleException(Exception ex) {

		JOptionPane.showMessageDialog(getMainWindow(), ex.getMessage());
		this.stopAnalyzing();
	}

	/**
	 * getter to retrieve the information whether a file is selected
	 * 
	 * @return TRUE/FALSE
	 * @author andreas.gerlach
	 */
	public boolean isFileSelected() {

		return (_fileHandler != null);
	}

	/**
	 * getter to retrieve the information whether an analyzing process
	 * is already active
	 * 
	 * @return TRUE/FALSE
	 * @author andreas.gerlach
	 */
	public boolean isAnalyzingInProgress() {

		return ((_textProcessingThread != null) && (_textProcessingThread
				.isAlive()));
	}

	/**
	 * an event handler for the process update event 
	 * that will inform the UI about the current progress information
	 * 
	 * @param evt the event information like percentage completed, word found
	 * @author andreas.gerlach
	 */
	@Override
	public void update(TextProcessStatusEvent evt) {

		this.getMainWindow().setProcessInformation(evt.getPercentage(),
				evt.getMessage());
	}

	/**
	 * an event handler for the process finish event
	 * that will inform the UI about the process results
	 * 
	 * @param evt the event information like the raw-text, text meta data
	 * @author andreas.gerlach
	 */
	@Override
	public void finish(TextProcessFinishEvent evt) {

		_textProcessingThread = null;
		_fileHandler = null;

		_theMetaData = evt.getMetaData();

		this.getMainWindow().setProcessResult(evt.getText(), _theMetaData);
	}

	/**
	 * getter to retrieve the currently selected and highlighted word
	 * 
	 * @return the currently selected and highlighted word
	 * @author andreas.gerlach
	 */
	public String getSelectedWord() {
		
		return _theSelectedWord;
	}
	
	/**
	 * selects the given word by highlighting it in the UI
	 * 
	 * @param word the word that should be highlighted
	 * @author andreas.gerlach
	 */
	public void setSelectedWord(String word) {

		_theSelectedWord = word;
		
		if (_theMetaData != null)
			this.getMainWindow().highlightWord(
					_theMetaData.getWordPositions(word));
	}

	/**
	 * adds the given word to the black-list of words in the settings
	 * and actualize the UI accordingly (mainly the top 10 list & tag cloud will be effected)
	 * the word will removed from the word-frequency list but still counts for 
	 * number of chars, alpha-chars and words
	 * 
	 * @param word the word that should be ignored in the top 10 word list statistic
	 * @author andreas.gerlach
	 */
	public void setIgnorableWord(String word) {

		_theSettings.addIgnorableWord(word);

		if (_theMetaData != null) {

			this.getMainWindow().updateMetaData(_theMetaData);
			
			// if the ignored word is currently selected
			// remove selection
			if (word == getSelectedWord()) {
				
				this.deselectWord(word);
			}
		}
	}

	/**
	 * deselect a word and remove the highlights in the UI text view
	 * 
	 * @param word the word to deselect
	 * @author andreas.gerlach
	 */
	public void deselectWord(String word) {

		_theSelectedWord = null;
		this.getMainWindow().removeHighlights();
	}

	/**
	 * stops the current active analyze process of the file by
	 * interrupting the running background thread
	 * 
	 * @author andreas.gerlach
	 */
	public void stopAnalyzing() {

		if ((_textProcessingThread != null)
				&& (_textProcessingThread.isAlive())) {

			_textProcessingThread.stop();
		}

		_textProcessingThread = null;
	}

	
	/**
	 * getter to retrieve the global application settings
	 * 
	 * @return the application settings object
	 * @author andreas.p&ouml;rtner
	 */
	public Settings getSettings() {

		return _theSettings;
	}

	/**
	 * the background thread object to process and analyze the
	 * file content
	 * 
	 * @author andreas.gerlach
	 */
	class WorkerThread implements Runnable {

		/**
	     * the file handler instance that knows how to read
	     * the file
	     */
		IFileHandler _fileHandler = null;

		/**
    	 * the strategy used to analyze the file content
    	 */
		ITextProcessingStrategy _strategy = null;

		/**
		 * constructor to instantiate a new worker thread object
		 * 
		 * @param fileHandler the file handler that should be used to open the file
		 * @param strategy the processing strategy that should be used to analyze the file content
		 * @author andreas.gerlach
		 */
		public WorkerThread(IFileHandler fileHandler,
				ITextProcessingStrategy strategy) {

			_fileHandler = fileHandler;
			_strategy = strategy;
		}

		/**
		 * parses and analyzes the file content
		 */
		@Override
		public void run() {

			_strategy.addTextProcessListener(AppController.getInstance());
			_strategy.parse(_fileHandler);
			_strategy.removeTextProcessListener(AppController.getInstance());
		}
	}
}
