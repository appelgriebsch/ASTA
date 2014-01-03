/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import asta.controller.AppController;

/**
 * the progress view shows the current state of the analyzing process
 * 
 * @author andreas.p&ouml;rtner
 */
@SuppressWarnings("serial")
class ProgressView extends JPanel {

	/**
	 * the progress bar visualizing the current state in the progress itself 
	 */
	private JProgressBar _progressBar = null;

	/**
	 * a status label showing some hints / messages of the current progress state
	 */
	private JLabel _statusLabel = null;

	/**
	 * an header showing the actual selected file
	 */
	private JLabel _header = null;
	
	/**
	 * constructs a new instance of the progress view and initializes its UI
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	public ProgressView() {

		initializeUI();
	}

	/**
	 * constructs the UI of the progress view by utilizing a grid layout manager with 
	 * 3 columns and 1 row that hosts the header label, progress bar and status label
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	void initializeUI() {

		setLayout(new GridLayout(1, 3, 10, 10));

		_progressBar = new JProgressBar();
		_progressBar.setValue(0);
		
		_statusLabel = new JLabel();
		_header = new JLabel("Keine Datei geöffnet...");
		
		this.add(_header);
		this.add(_progressBar);
		this.add(_statusLabel);
	}

	/**
	 * set the information about the selected file into the header label
	 * 
	 * @param fileName the name of the file selected
	 * @author andreas.p&ouml;rtner
	 */
	void setSelectedFile(String fileName) {
		
		_header.setText(String.format("Ausgewählte Datei: %s", fileName));
		
		// cleanup progress bar
		setStatus(0, "");
	}
	
	/**
	 * set the information about the analyzing progress state
	 * 
	 * @param percentage the number of successfully analyzed parts of the file in percent
	 * @param message the message to display for the current progress state
	 * @author andreas.p&ouml;rtner
	 */
	void setStatus(int percentage, String message) {
				
		_header.setText(String.format("Analysiere Datei: %s", 
				AppController.getInstance().getCurrentFile().getFile()));
		
		if (percentage < 0) {
			
			_progressBar.setIndeterminate(true);

		} else {
			
			_progressBar.setIndeterminate(false);
			_progressBar.setValue(percentage);
		}

		_statusLabel.setText(message);
	}
}
