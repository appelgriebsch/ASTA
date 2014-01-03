/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import asta.App;
import asta.controller.AppController;
import asta.model.IFileHandler;
import asta.model.ITextProcessingStrategy;

/**
 * the toolbar view offers the commands the user can choose from
 * 
 * @author andreas.p&ouml;rtner
 * @author andreas.gerlach
 */
@SuppressWarnings("serial")
class ToolbarView extends JToolBar {

	/**
	 * the command to open a file browser and select a file for analyzing it
	 */
	private AbstractAction _openAct = null;

	/**
	 * the command to start the analyzing process on a selected file
	 */
	private AbstractAction _runAct = null;

	/**
	 * the command to stop the current active analyzing process
	 */
	private AbstractAction _stopAct = null;

	/**
	 * the command to show an information dialog about the authors and copyright
	 * of this application
	 */
	private AbstractAction _infoAct = null;

	/**
	 * a selection box where the user can choose one of the existing file
	 * processing strategies that should be used during the analyze
	 */
	private JComboBox _cmbProcessingStrategies = null;

	/**
	 * constructs a new toolbar view and initializes its UI
	 * 
	 * @author andreas.p&ouml;rtner
	 * @author andreas.gerlach
	 */
	public ToolbarView() {

		initializeUI();
	}

	/**
	 * creates the various commands and add them as buttons to the toolbar menu
	 * additionally add the selection box with the available processing strategy
	 * items from the global application settings
	 * 
	 * @author andreas.p&ouml;rtner
	 * @author andreas.gerlach
	 */
	void initializeUI() {

		setFloatable(false);

		// create the open command that will trigger a file selection
		// box to be shown
		_openAct = new AbstractAction("Open", new ImageIcon(
				App.class.getResource("images/open_icn.png"))) {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						try {

							// open up a file selection dialog and
							// set the available file filter information
							// based on the existing file handler from
							// the global application settings
							JFileChooser dlgFile = new JFileChooser();

							for (IFileHandler fileHandler : 
								AppController.getInstance().getSettings().getAvailableFileHandler()) {

								dlgFile.addChoosableFileFilter(fileHandler.getFileFilter());
							}

							int result = dlgFile.showOpenDialog(AppController.getInstance().getMainWindow());

							// when user acknowledge the dialog with OK
							// inform the app controller about the selected file
							if (result == JFileChooser.APPROVE_OPTION) {
								
								AppController.getInstance().openFile(
										dlgFile.getSelectedFile());
							}

						} catch (IOException e) {

							AppController.getInstance().handleException(e);
						}

						updateView();
					}
				});
			}
		};

		// initialize the selection box containing the existing text processing strategies
		// the user can choose from
		// automatically select the first item
		_cmbProcessingStrategies = new JComboBox(
				AppController.getInstance().getSettings().getAvailableTextProcessors().toArray());
		_cmbProcessingStrategies.setSelectedIndex(0);

		// create the command that starts the analyzing process on a selected file
		_runAct = new AbstractAction("Run", new ImageIcon(
				App.class.getResource("images/search_icn.png"))) {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						// check if a file is selected in the app controller
						if (AppController.getInstance().isFileSelected()) {

							try {

								// and if so start the analyzing process on it
								// using the selected text processing strategy
								AppController.getInstance().analyzeFile(
												(ITextProcessingStrategy) _cmbProcessingStrategies.getSelectedItem());

							} catch (InterruptedException e) {

								AppController.getInstance().handleException(e);

							} finally {

								updateView();
							}
						}
					}
				});
			}
		};

		// creates the command to stop the currently active text analyzing process
		_stopAct = new AbstractAction("Stop", new ImageIcon(
				App.class.getResource("images/stop_icn.png"))) {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						// stop the analyzing process if active
						if (AppController.getInstance().isAnalyzingInProgress()) {
						
							AppController.getInstance().stopAnalyzing();
							updateView();
						}
					}
				});
			}
		};

		// creates the comand that triggers the about dialog to be shown on screen
		_infoAct = new AbstractAction("Stop", new ImageIcon(
				App.class.getResource("images/info_icn.png"))) {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane
						.showMessageDialog(
								AppController.getInstance().getMainWindow(),
								"ASTA - A students Text Analyzer\n\n" +
								"(c) Copyright 2010 by\nA. Gerlach & A. Pörtner,\n" +
								"FHDW Bielefeld ify208",
								"Information",
								JOptionPane.INFORMATION_MESSAGE,
								new ImageIcon(App.class
										.getResource("images/about_icn.png")));
			}
		};

		// set the enabled/disabled status of the commands based
		// on the current state in the app controller
		updateView();

		// add the various commands to the toolbar
		this.add(_openAct);
		_openAct.putValue(AbstractAction.NAME, "Datei öffnen");
		_openAct.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Eine neue Datei öffnen");

		this.addSeparator();

		this.add(_cmbProcessingStrategies);
		
		this.addSeparator();

		this.add(_runAct);
		_runAct.putValue(AbstractAction.NAME, "Analyse starten");
		_runAct.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Startet den Processor zur Analyse der Datei");

		this.add(_stopAct);
		_stopAct.putValue(AbstractAction.NAME, "Analyse stoppen");
		_stopAct.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Stoppt die aktuelle Analyse");

		this.addSeparator();

		this.add(_infoAct);
		_infoAct.putValue(AbstractAction.NAME, "Info Text");
		_infoAct.putValue(AbstractAction.SHORT_DESCRIPTION,
				"Zeigt ein Informationsfenster");
	}

	/**
	 * informs the toolbar view about the finishing of an active analyzing process
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	void setProcessFinished() {

		updateView();
	}

	/**
	 * sets the enabled/disabled state of the toolbar items based on the current state
	 * in the app controller
	 * 
	 * @author andreas.p&ouml;rtner
	 * @author andreas.gerlach
	 */
	void updateView() {

		_openAct.setEnabled(!AppController.getInstance().isAnalyzingInProgress());
		_runAct.setEnabled(AppController.getInstance().isFileSelected()
							&& !AppController.getInstance().isAnalyzingInProgress());
		_stopAct.setEnabled(AppController.getInstance().isAnalyzingInProgress());
	}
}