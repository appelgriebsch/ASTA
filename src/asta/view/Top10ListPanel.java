/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import asta.model.TextMetaData;

/**
 * the top-10-list-panel shows a table of the top 10 words from the text analyzing process
 * 
 * @author andreas.gerlach
 */
@SuppressWarnings("serial")
class Top10ListPanel extends JPanel {

	/**
	 * constructs a new Top10ListPanel and initializes its UI 
	 */
	public Top10ListPanel() {

		initializeUI();
	}

	/**
	 * set-up a grid layout with a flexible number of rows and 4 columns showing
	 * the current position, the word at this position, the frequency of that word
	 * in the text as well as the occurrence of this word in the text in percent
	 * 
	 * @author andreas.gerlach
	 */
	void initializeUI() {

		this.setLayout(new GridLayout(0, 4, 5, 5));

		this.add(new JLabel("No."));
		this.add(new JLabel("Wort"));
		this.add(new JLabel("Anzahl", JLabel.RIGHT));
		this.add(new JLabel("Anteil %", JLabel.RIGHT));

		// set the font of each label in the header column to be BOLD
		for (Component c : this.getComponents()) {

			if (c instanceof JLabel)
				c.setFont(c.getFont().deriveFont(Font.BOLD));
		}
	}

	/**
	 * informs the top-10-list-panel about the result of the analyzing progress
	 * and states the text meta data information object where the control can take
	 * the information about the words and their frequency
	 * 
	 * @param data - the text meta data information object that is the base for the words
	 * and their frequency
	 * @author andreas.gerlach
	 */
	void setTextMetaData(TextMetaData data) {

		this.clearValues();
		
		// get the TOP-10-words from the text meta data
		String[] arrWords = data.getTopWordsByFrequency(10);

		// and fill out the table with the information about the word and its frequency accordingly
		for (int i = 0; i < arrWords.length; ++i) {

			String word = arrWords[i];
			float freqWord = (float) data.getFrequencyOfWord(word) * 100
					/ data.getNumberOfWords();

			this.add(new JLabel(String.format("%d.", i + 1)));

			this.add(new JLabel(word));

			this.add(new JLabel(String.format("%d",
					data.getFrequencyOfWord(word)), JLabel.RIGHT));

			this.add(new JLabel(String.format("%.2f", freqWord), JLabel.RIGHT));
		}
	}
	
	/**
	 * clears the values in the sub-view and initializes
	 * the sub view content with some meaningful default values
	 * 
	 * @author andreas.gerlach
	 */
	void clearValues() {
		
		// clean-up the UI
		this.removeAll();

		// re-build the header column of the table
		this.initializeUI();
	}
}
