/**
 * A Students Text Analyzer
 */
package asta.view;

import java.awt.Color;
import java.awt.Paint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.jCharts.chartData.ChartDataException;
import org.jCharts.chartData.PieChartDataSet;
import org.jCharts.encoders.PNGEncoder;
import org.jCharts.nonAxisChart.PieChart2D;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.PieChart2DProperties;
import org.jCharts.properties.PropertyException;
import org.jCharts.types.PieLabelType;
import org.jdesktop.swingx.JXImagePanel;

import asta.App;
import asta.controller.AppController;
import asta.model.TextMetaData;

/**
 * the Diagram panel offers a pie-2d-chart of the 
 * various counters from the text meta data information
 * like nmbrOfVowels, nmbrOfConsonants, nmbrOfPunctuation, ...
 * 
 * @author andreas.gerlach
 */
@SuppressWarnings({"deprecation", "serial"})
class DiagramPanel extends JPanel {

	/**
	 * the encapsulated image view to display the 2d-pie-chart
	 */
	private JXImagePanel _imgPanel = null;

	/**
	 * constructs a new diagram panel and initializes its UI
	 * 
	 * @author andreas.gerlach
	 */
	public DiagramPanel() {

		initializeUI();
	}

	/**
	 * creates a new instance of the contained Image View
	 * and adds it to the panel
	 * 
	 * @author andreas.gerlach
	 */
	void initializeUI() {

		_imgPanel = new JXImagePanel();
		this.add(_imgPanel);
		setDefaultDiagramImage();
	}

	/**
	 * informs the 2d-pie-chart control about the text meta information and
	 * builds the 2d-pie-chart based on the counter in this object
	 * 
	 * @param data the text meta information object whose counter should be used
	 * @author andreas.gerlach
	 */
	void setTextMetaData(TextMetaData data) {

		// builds the data rows & labels of the current 2d-pie-chart
		String[] labels = { "Vokale", "Konsonanten", "Satzzeichen", "sonstige" };

		double others = data.getNumberOfCharacters()
				- data.getNumberOfAlphaCharacters()
				- data.getNumberOfPunctuation();

		double[] points = { data.getNumberOfVowels(),
				data.getNumberOfConsonants(), data.getNumberOfPunctuation(),
				others };

		Paint[] paints = { Color.yellow, Color.green, Color.blue, Color.red };

		ByteArrayOutputStream memStream = new ByteArrayOutputStream();

		try {

			// initialize the 2d-pie-chart and get a PNG image out of it
			PieChart2DProperties pieChart2DProperties = new PieChart2DProperties();
			pieChart2DProperties.setPieLabelType(PieLabelType.VALUE_LABELS);
			pieChart2DProperties.setRoundingPowerOfTen(2);

			PieChartDataSet pieChartDataSet = new PieChartDataSet("Verteilung",
					points, labels, paints, pieChart2DProperties);

			PieChart2D pieChart2D = new PieChart2D(pieChartDataSet,
					new LegendProperties(), new ChartProperties(), 300, 300);

			PNGEncoder.encode(pieChart2D, memStream);
			memStream.flush();

			// display the PNG image of the 2d-pie-chart on the contained image view
			_imgPanel.setImage(ImageIO.read(new ByteArrayInputStream(
					memStream.toByteArray())));

		} catch (ChartDataException e) {

			AppController.getInstance().handleException(e);

		} catch (PropertyException e) {

			AppController.getInstance().handleException(e);

		} catch (IOException e) {

			AppController.getInstance().handleException(e);

		} finally {

			try {

				memStream.close();

			} catch (IOException e) {

				AppController.getInstance().handleException(e);
			}
		}
	}
	
	/**
	 * sets the default image for a pie-chart w/o having text meta information available
	 * 
	 * @author andreas.gerlach
	 */
	void setDefaultDiagramImage() {
		
		try {
			
			_imgPanel.setImage(ImageIO.read(
					App.class.getResource("images/piechart-nodata.png")));
			
		} catch (IOException e) {
		}

	}
}