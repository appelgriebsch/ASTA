/**
 * A Students Text Analyzer
 */
package asta;

import asta.controller.AppController;
import asta.view.MainWindow;

/**
 * Entry Point of the Java Swing Application
 * 
 * @author andreas.p&ouml;rtner
 */
public class App {

	/**
	 * Starts the application and initializes the
	 * Main window
	 * 
	 * @param args not used in this scenario
	 * @author andreas.p&ouml;rtner
	 */
	public static void main(String[] args) {
		
		AppController.getInstance().run(
				new MainWindow());
	}
}
