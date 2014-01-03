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
 * the statistic panel hosts the statistical information of the current
 * processed text
 * 
 * @author andreas.p&ouml;rtner
 */
@SuppressWarnings("serial")
class StatisticPanel extends JPanel {

	/**
	 * the labels to show the information from the text meta data
	 * about the nmbrOfWords, nmbrOfChars, ...
	 */
	private JLabel _nmbrOfWords = null, _nmbrOfChars = null,
			_nmbrOfAlphaChars = null, _nmbrOfVowel = null,
			_nmbrOfConsonants = null, _nmbrOfPunctuation = null;

	/**
	 * constructs a new statistic panel and initializes its UI
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	public StatisticPanel() {

		initializeUI();
	}

	/**
	 * set-up a new grid-layout with a flexible number of rows but 2 columns:
	 * one hosting the label with the name of the counter and the other with the value
	 * of the counter
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	void initializeUI() {

		this.setLayout(new GridLayout(0, 2, 5, 5));

		_nmbrOfWords = new JLabel("0", JLabel.RIGHT);
		_nmbrOfChars = new JLabel("0", JLabel.RIGHT);
		_nmbrOfAlphaChars = new JLabel("0", JLabel.RIGHT);
		_nmbrOfVowel = new JLabel("0", JLabel.RIGHT);
		_nmbrOfConsonants = new JLabel("0", JLabel.RIGHT);
		_nmbrOfPunctuation = new JLabel("0", JLabel.RIGHT);

		this.add(new JLabel("Anzahl Wörter"));
		this.add(_nmbrOfWords);

		this.add(new JLabel("Anzahl Zeichen"));
		this.add(_nmbrOfChars);

		this.add(new JLabel("Anzahl Satzzeichen"));
		this.add(_nmbrOfPunctuation);

		this.add(new JLabel("Anzahl alphanum. Zeichen"));
		this.add(_nmbrOfAlphaChars);

		this.add(new JLabel("Anzahl Vokale"));
		this.add(_nmbrOfVowel);

		this.add(new JLabel("Anzahl Konsonanten"));
		this.add(_nmbrOfConsonants);

		// set the font of the descriptive labels to BOLD
		for (Component c : this.getComponents()) {

			if ((c instanceof JLabel) && (c != _nmbrOfAlphaChars)
					&& (c != _nmbrOfChars) && (c != _nmbrOfConsonants)
					&& (c != _nmbrOfPunctuation) && (c != _nmbrOfVowel)
					&& (c != _nmbrOfWords))

				c.setFont(c.getFont().deriveFont(Font.BOLD));
		}
	}

	/**
	 * sets the meta data information object whose counter should be used 
	 * to display the values for the various statistical information
	 * 
	 * @param data - the meta information object hosting the counter values
	 * @author andreas.p&ouml;rtner
	 */
	void setTextMetaData(TextMetaData data) {


        _nmbrOfAlphaChars.setText(String.format("%d", data.getNumberOfAlphaCharacters()));
		_nmbrOfChars.setText(String.format("%d", data.getNumberOfCharacters()));
		_nmbrOfConsonants.setText(String.format("%d", data.getNumberOfConsonants()));
		_nmbrOfVowel.setText(String.format("%d", data.getNumberOfVowels()));
		_nmbrOfWords.setText(String.format("%d", data.getNumberOfWords()));
		_nmbrOfPunctuation.setText(String.format("%d", data.getNumberOfPunctuation()));
	}
	
	/**
	 * clears the values in the sub-view and initializes
	 * the sub view content with some meaningful default values
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	void clearValues() {
		
		_nmbrOfAlphaChars.setText(String.format("%d", 0));
		_nmbrOfChars.setText(String.format("%d", 0));
		_nmbrOfConsonants.setText(String.format("%d", 0));
		_nmbrOfVowel.setText(String.format("%d", 0));
		_nmbrOfWords.setText(String.format("%d", 0));
		_nmbrOfPunctuation.setText(String.format("%d", 0));
	}
}
