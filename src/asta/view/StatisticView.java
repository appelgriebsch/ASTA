/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import asta.model.TextMetaData;

/**
 * the statistic view hosts the sections with the
 * statistical information, the 2d-pie-chart and the Top-10-List of words
 * 
 * @author andreas.p&ouml;rtner
 */
@SuppressWarnings("serial")
class StatisticView extends JPanel {

	/**
	 * a reference to the panel showing the statistical information
	 */
	private StatisticPanel _statisticPanel = null;

	/**
	 * a reference to the panel hosting the 2d-pie-chart
	 */
	private DiagramPanel _graphicViewPanel = null;

	/**
	 * a reference to the panel building the Top-10-List of words
	 */
	private Top10ListPanel _top10TablePanel = null;

	/**
	 * constructs a new statistical view instance and initializes its UI
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	public StatisticView() {

		initializeUI();
	}

	/**
    * initializes the UI of the statistic view by setting up the different regions
    * hosting the statistical information, the 2d-pie-chart and the Top-10-List of words
    * 
    * @author andreas.p&ouml;rtner
    */
	@SuppressWarnings("deprecation")
	void initializeUI() {

		JXTaskPaneContainer tpc = new JXTaskPaneContainer();

		_statisticPanel = new StatisticPanel();
		_graphicViewPanel = new DiagramPanel();
		_top10TablePanel = new Top10ListPanel();

		JXTaskPane p = new JXTaskPane("Statistik");
		p.add(_statisticPanel);
		p.setCollapsed(false);

		JXTaskPane p2 = new JXTaskPane("Diagramm");
		p2.add(_graphicViewPanel);
		p2.setCollapsed(true);

		JXTaskPane p3 = new JXTaskPane("Top 10 Liste");
		p3.add(_top10TablePanel);
		p3.setCollapsed(true);

		tpc.add(p);
		tpc.add(p2);
		tpc.add(p3);

		// add the task-panes to a scrollpane and allow
		// it to scroll vertically only.
		JScrollPane scrollPane = new JScrollPane(tpc,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.setLayout(new GridLayout(1, 1));

		this.add(scrollPane);
	}

	/**
	 * informs each of the regions about the text meta data information object
	 * that has been examined during the text analyzing process
	 * 
	 * @param data - the text meta data information that is the outcome of the analyzing progress
	 * @author andreas.p&ouml;rtner
	 */
	void setTextMetaData(TextMetaData data) {

		_statisticPanel.setTextMetaData(data);
		_graphicViewPanel.setTextMetaData(data);
		_top10TablePanel.setTextMetaData(data);
	}
	
	/**
	 * clears the values in the sub-views and initializes
	 * the sub view content with some meaningful default values
	 * 
	 * @author andreas.gerlach
	 */
	void clearValues() {
		
		_statisticPanel.clearValues();
		_graphicViewPanel.setDefaultDiagramImage();
		_top10TablePanel.clearValues();
	}
}
