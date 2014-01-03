/**
 * A Students Text Analyzer
 */
package asta;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import asta.model.GenericTextProcessingStrategy;
import asta.model.IFileHandler;
import asta.model.PlainTextFileHandler;
import asta.model.TextMetaData;
import asta.model.TextProcessFinishEvent;
import asta.model.TextProcessStatusEvent;
import asta.model.TextProcessStatusListener;

/**
 * Entry point of the console application 
 * used for test purposes
 * 
 * @author andreas.p&ouml;rtner
 *
 */
public class Console {

	/**
	 * the starting point of the console application
	 * 
	 * @param args
	 * 		command-line arguments: args[0] - name of the file to analyze
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {
			ShowUsage();
			return;
		}
		
		// check file exist & can be opened for reading
		File fi = new File(args[0]);
		
		if (!fi.exists() ||
			!fi.canRead()) {
			
			System.out.println(String.format("File %s not found or cannot be opened for reading!", 
					fi.getAbsolutePath()));
			
			return;
		}
		
		// create default text handler & processing strategy
		IFileHandler fiHandler = new PlainTextFileHandler();
		GenericTextProcessingStrategy procStrat = new GenericTextProcessingStrategy();
		ConsoleOutListener listener = new ConsoleOutListener();
		
		try {
			
			System.setOut(new PrintStream(System.out, true, "UTF-8"));
			
			fiHandler.setFile(fi.getAbsolutePath());
			
			procStrat.addTextProcessListener(listener);
			procStrat.parse(fiHandler);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		finally {
			
			procStrat.removeTextProcessListener(listener);
		}
	}

	/**
	 * shows the command-line help for the calling conventions
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	private static void ShowUsage() {
		
		System.out.println("=========================================");
		System.out.println("ASTA - Test Console Application");
		System.out.println("=========================================");
		System.out.println("USAGE:");
		System.out.println("\tjava asta.TestMain <FileName>");
	}
}

/**
 * An internal event listener that dumps the data of the analysing process
 * to the system console
 * 
 * @author andreas.gerlach
 *
 */
class ConsoleOutListener implements TextProcessStatusListener {

	/**
	 * to visualize the progress bar we need to know
	 * how many chars of our console line are already in use
	 */
	private int nFilled = 0;
	
	/**
	 * the width of the console line
	 * usually 80 chars wide
	 */
	private final int nConsoleWidth = 80;
	
	/**
	 * catch the update event and displays a textual
	 * progress bar on the screen
	 * 
	 * @author andreas.gerlach
	 */
	@Override
	public void update(TextProcessStatusEvent evt) {
		
		int nFill = (evt.getPercentage() * nConsoleWidth / 100) - nFilled;
		
		for (int i = 0; i < nFill; ++i)
			System.out.append('#');
		
		nFilled += nFill;
	}

	/**
	 * catch the finish event and displays all words
	 * and their frequency ordered descending
	 * additionally prints out the number of words / chars found
	 * 
	 * @author andreas.p&ouml;rtner
	 */
	@Override
	public void finish(TextProcessFinishEvent evt) {
		
		int i = 0;
		TextMetaData data = evt.getMetaData();
		
		for(String s: data.getWordsByFrequency()) {
			
			System.out.print(String.format("\n%d. %s (%d)", ++i,
					s, data.getFrequencyOfWord(s)));
		}
		
		System.out.println(String.format("\nSum(Words): %d, Sum(Chars): %d",
				data.getNumberOfWords(), data.getNumberOfCharacters()));
	}
}
