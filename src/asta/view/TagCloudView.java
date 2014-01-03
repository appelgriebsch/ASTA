/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import asta.controller.AppController;
import asta.model.TextMetaData;

/**
 * the tag cloud view builds and shows a tag cloud based on the 
 * word frequencies in the text meta data object 
 * 
 * @author andreas.gerlach
 */
@SuppressWarnings("serial")
class TagCloudView extends JPanel {

	/**
	 * a pop-up menu showing options for each word
	 * in the tag cloud view
	 */
	private JPopupMenu _popMenu = null;

	/**
	 * the panel hosting the various labels of the top-10
	 * words that will be displayed in the tag cloud
	 */
	private JPanel _tagPanel = null;

	/**
	 * constructs a new tag cloud view and initializes its UI
	 * 
	 * @author andreas.gerlach
	 */
	public TagCloudView() {

		initializeUI();
	}

	/**
	 * initializes a new borderlayout in which it hosts the panel showing the 
	 * various labels with the words from the text meta data information
	 * in case the labels will be wider than the available application width it will
	 * display an horizontal scrollbar
	 * 
	 * @author andreas.gerlach
	 */
	void initializeUI() {

		this.setLayout(new BorderLayout());

		_tagPanel = new JPanel(true);
		_tagPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

		JScrollPane scrollPane = new JScrollPane(_tagPanel,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * informs the tag cloud view about the text meta data information that is resulting from
	 * the current text analyzing progress
	 * here it will find the necessary information about the top-10 words and their frequencies
	 * respectively
	 * 
	 * @param data the text meta data  object where to get the information about the top-10
	 * words and their frequency
	 * @author andreas.gerlach
	 */
	void setTextMetaData(TextMetaData data) {

		// clean-up any existing labels in the tag cloud panel
		_tagPanel.removeAll();

		// get the top 10 list of words from the text meta information object
		String[] words = data.getTopWordsByFrequency(10);

		// get the minimum and maximum frequency of the words in the top-10-list
		int minFreq = data.getFrequencyOfWord((words.length > 1 ? words[words.length - 1]
						: words[0]));
		int maxFreq = data.getFrequencyOfWord(words[0]);

		// calculate the range of frequency
		float freqRange = maxFreq - minFreq;
		
		// get the range of font-sizes based on the values in the global settings
		float fontRange = AppController.getInstance().getSettings().getMaxFontSize()
				- AppController.getInstance().getSettings().getMinFontSize();

		// for each word in the top-10-word list
		for (String w : words) {

			// create a label and initialize its font size setting based on the weight of
			// the current examined word in accordance to the ranges calculated before
			JLabel wordLabel = new JLabel(w);
			int freqCurrent = data.getFrequencyOfWord(w);

			Font freqFont = (freqRange == 0 ? wordLabel.getFont().deriveFont(
					AppController.getInstance().getSettings().getMinFontSize())
					: wordLabel.getFont().deriveFont(
							AppController.getInstance().getSettings()
									.getMinFontSize()
									+ (freqCurrent - minFreq)
									* (fontRange / freqRange)));

			wordLabel.setFont(freqFont);
			wordLabel.setToolTipText(w);
			
			// set-up a mouse-listener on the label that will display a context menu
			// as soon as the mouse button is pressed in front of a label
			wordLabel.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {

					// when the label is clicked
					// offer a pop-up menu to select or ignore the 
					// word represented by the label
					final JLabel theLabel = (JLabel) arg0.getSource();

					if (_popMenu != null) {

						_popMenu.setVisible(false);
						_popMenu = null;
					}

					_popMenu = new JPopupMenu();

					_popMenu.add(new AbstractAction(String.format(
							"Wort \"%s\" auswählen", theLabel.getText())) {

						@Override
						public void actionPerformed(ActionEvent arg0) {

							setSelectedWord(theLabel);
							_popMenu.setVisible(false);
						}
					});

					_popMenu.add(new AbstractAction(String.format(
							"Wort \"%s\" ignorieren", theLabel.getText())) {

						@Override
						public void actionPerformed(ActionEvent arg0) {

							setIgnorableWord(theLabel);
							_popMenu.setVisible(false);
						}
					});

					_popMenu.setLocation(arg0.getLocationOnScreen());
					_popMenu.setVisible(true);
				}

				// if the mouse enters the label 
				// change the foreground color of it to blue
				// if the label contains the currently selected word
				// do not touch it
				@Override
				public void mouseEntered(MouseEvent arg0) {

					JLabel theLabel = (JLabel) arg0.getSource();

					if (theLabel.getText() != 
								AppController.getInstance().getSelectedWord())
						
						theLabel.setForeground(Color.BLUE);
				}

				// if the mouse leaves the label
				// restore the original foreground color (Black)
				// if the label contains the currently selected word
				// do not touch it
				@Override
				public void mouseExited(MouseEvent arg0) {

					JLabel theLabel = (JLabel) arg0.getSource();

					if (theLabel.getText() != 
								AppController.getInstance().getSelectedWord())
						
						theLabel.setForeground(Color.BLACK);
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

			});

			// highlight the selected word in the tag cloud
			// by painting it in a different foreground color (Red)
			if (w == AppController.getInstance().getSelectedWord())
				markWordLabel(wordLabel);
			
			_tagPanel.add(wordLabel);
		}
	}

	/**
	 * informs the tag cloud view about the word that has been
	 * selected by the user by choosing the option from the pop-up menu
	 * the selected word is given the the app controller so that the rest of the UI
	 * can be informed as well
	 * 
	 * @param word the label with the word that has been selected
	 * @author andreas.gerlach
	 */
	void setSelectedWord(final JLabel theLabel) {

		if (theLabel.getText() != AppController.getInstance().getSelectedWord()) {

			// restore the original foreground color on all labels (Black)
			unmarkAllLabels();

			// highlight the selected word in the tag cloud
			// by painting it in a different foreground color (Red)
			markWordLabel(theLabel);

			// inform the app controller about the selected word
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					AppController.getInstance().setSelectedWord(
							theLabel.getText());
				}
			});
		}
	}

	/**
	 * informs the tag cloud view about the word that should be 
	 * ignored from the top-10-list when the user has chosen the
	 * option from the pop-up menu
	 * also informs the app controller about the word to be ignored to that
	 * the rest of the UI can also refresh this status
	 * 
	 * @param theLabel the label with the word that should be ignored
	 * @author andreas.gerlach
	 */
	void setIgnorableWord(final JLabel theLabel) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				AppController.getInstance().setIgnorableWord(theLabel.getText());
			}
		});
	}

	/**
	 * marks the label with the currently selected word
	 * by setting its foreground color to red and the font style to Bold
	 * 
	 * @param theLabel the label containing the currently selected word
	 * @author andreas.gerlach
	 */
	void markWordLabel(JLabel theLabel) {

		theLabel.setForeground(Color.RED);
		theLabel.setFont(theLabel.getFont().deriveFont(Font.BOLD));
	}

	/**
	 * reset the font-style and foreground color on all available labels
	 * 
	 * @author andreas.gerlach
	 */
	void unmarkAllLabels() {

		for (Component c : _tagPanel.getComponents()) {

			if (c instanceof JLabel) {

				c.setForeground(Color.BLACK);
				c.setFont(c.getFont().deriveFont(Font.PLAIN));
			}
		}
	}
}
