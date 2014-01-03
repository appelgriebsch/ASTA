/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.CardLayout;

import javax.swing.JPanel;

import asta.model.TextMetaData;

/**
 * the status view panel switches between the analyzing progress view 
 * and the tag cloud that is build after the analyzing has been finished
 * 
 * @author andreas.gerlach
 */
@SuppressWarnings("serial")
class StatusView extends JPanel {

	/**
	 * the progress view shown during the analyzing process itself
	 */
	private ProgressView _progressView = null;

	/**
	 * the tag cloud view shown after the analyzing progress has been finished successfully
	 */
	private TagCloudView _tagCloudView = null;

	/**
	 * constructs a new status view and initializes its UI
	 * 
	 * @author andreas.gerlach
	 */
	public StatusView() {

		initializeUI();
	}

	/**
    * uses a card layout to switch between both views depending of the
    * actual state of the analyzing progress 
    * by default it will show the progress view panel
    * 
    * @author andreas.gerlach
    */
	void initializeUI() {

		setLayout(new CardLayout());
		
		_progressView = new ProgressView();
		this.add("PROGRESSVIEW", _progressView);
	}

	/**
	 * informs the progress view panel about the file selected
	 *  
	 * @param fileName the name of the file that has been selected
	 * @author andreas.gerlach
	 */
	void setSelectedFile(String fileName) {
		
		_progressView.setSelectedFile(fileName);
		
		this.setPreferredSize(_progressView.getPreferredSize());
		((CardLayout) this.getLayout()).show(this, "PROGRESSVIEW");
	}
	
	/**
	 * informs the tag cloud view about the resulting text meta data information
	 * that has been built during the analyzing process
	 * it will automatically bring the tag cloud view to the front in the status view panel
	 * 
	 * @param data the text meta data information object where the tag cloud view can find the
	 * information about the words and their frequency
	 * @author andreas.gerlach
	 */
	void setTextMetaData(TextMetaData data) {

		_tagCloudView = new TagCloudView();
		_tagCloudView.setTextMetaData(data);
		this.add("TAGCLOUDVIEW", _tagCloudView);

		this.setPreferredSize(_tagCloudView.getPreferredSize());
		((CardLayout) this.getLayout()).show(this, "TAGCLOUDVIEW");
	}
	
	/**
	 * informs the progress view about the current state of the analyzing progress
	 * it will automatically bring the progress view to the front in the status view panel
	 * 
	 * @param percent the number of completed analyzing progress operations in percent
	 * @param message the message that is bind to the current progress state
	 * @author andreas.gerlach
	 */
	void setProgressInformation(int percent, String message) {

		_progressView.setStatus(percent, message);

		this.setPreferredSize(_progressView.getPreferredSize());
		((CardLayout) this.getLayout()).show(this, "PROGRESSVIEW");
	}
}
