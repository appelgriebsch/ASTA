/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;

import org.jdesktop.swingx.JXEditorPane;

import asta.model.WordPosition;

/**
 * the text view control will show the plain text of the
 * analyzed file on the screen and shows the selected word
 * by highlighting it
 * 
 * @author andreas.gerlach
 */
@SuppressWarnings("serial")
class TextView extends JPanel {

	/**
	 * a reference to the editor pane
	 */
	private JXEditorPane _editorPane = null;
	
	/**
	 * constructs a new text view control and
	 * initializes its UI
	 * 
	 * @author andreas.gerlach
	 */
	public TextView() {

		initializeUI();
	}

	/**
    * set-up the editor pane (read-only, double buffered, w/o autoscroll functionality)
    * and the default highlighting strategy
    * the editor pane will be hosted in a scrollpane view to allow scrolling down and sideways
    * through the text view
    * 
    * @author andreas.gerlach
    */
	private void initializeUI() {

		_editorPane = new JXEditorPane();
		_editorPane.setEditable(false);
		_editorPane.setDoubleBuffered(true);
		_editorPane.setAutoscrolls(false);
		
		DefaultHighlighter highlighter = new DefaultHighlighter();
		highlighter.setDrawsLayeredHighlights(true);		

		_editorPane.setHighlighter(highlighter);

		JScrollPane scrollPane = new JScrollPane(_editorPane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * informs the text view about the plain text of the file that has been
	 * analyzed
	 * 
	 * @param plainText the plain text of the file to be displayed in the text view
	 * @author andreas.gerlach
	 */
	void setText(StringBuffer plainText) {

		_editorPane.setText(plainText.toString());
	}
	
	/**
	 * informs the text view about the positions of the selected word so that it
	 * can be highlighted in the UI
	 * 
	 * @param positions an array with the positions of the selected word
	 * @author andreas.gerlach
	 */
	void highlight(WordPosition[] positions) {

		removeHighlights();
		
		// use a default highlight painter with color Yellow to mark the words
		// in the text view
		DefaultHighlightPainter painter = new DefaultHighlightPainter(Color.YELLOW);
		Highlighter highlighter = _editorPane.getHighlighter();
		
		for(WordPosition pos : positions) {
			
			try {
				
				if (pos.getEndPosition() > Integer.MAX_VALUE)
					break;
				
				highlighter.addHighlight((int) pos.getStartPosition(),
							   (int) pos.getEndPosition(), painter);
				
			} catch (BadLocationException e) {
			}
		}
		
		this.invalidate();
	}
	
	/**
	 * remove all current highlights in the text view
	 * 
	 * @author andreas.gerlach
	 */
	void removeHighlights() {
		
		Highlighter highlighter = _editorPane.getHighlighter();
		highlighter.removeAllHighlights();
	}
}
