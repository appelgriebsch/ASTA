/**
 * A Students Text Analyzer
 */
package asta.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import asta.App;
import asta.controller.AppController;

/**
 * stores application wide settings
 * 
 * @author andreas.gerlach
 * @author andreas.p&ouml;rtner
 */
public class Settings {

	/**
	 * the maximum font size for the tag cloud
	 */
	private int _maxFontSize = 48;

	/**
	 * the minimum font size for the tag cloud
	 */
	private int _minFontSize = 8;

	/**
	 * the black-list of words that should be ignored in the frequency list & tag cloud
	 */
	private ArrayList<String> _arrIgnorableWords = new ArrayList<String>();

	/**
	 * getter to retrieve the maximum font size for the tag cloud
	 * 
	 * @return the maximum font size for the tag cloud
	 * @author andreas.p&ouml;rtner
	 */
	public int getMaxFontSize() {

		return _maxFontSize;
	}

	/**
	 * setter to specify the maximum font size for the tag cloud
	 * 
	 * @param size the maximum font size for the tag cloud
	 * @author andreas.p&ouml;rtner
	 */
	public void setMaxFontSize(int size) {

		_maxFontSize = size;
	}

	/**
	 * getter to retrieve the minimum font size for the tag cloud
	 * 
	 * @return the minimum font size for the tag cloud
	 * @author andreas.p&ouml;rtner
	 */
	public int getMinFontSize() {

		return _minFontSize;
	}

	/**
	 * setter to specify the minimum font size for the tag cloud
	 * 
	 * @param size the minimum font size for the tag cloud
	 * @author andreas.p&ouml;rtner
	 */
	public void setMinFontSize(int size) {

		_minFontSize = size;
	}

	/**
	 * checks if the word given is in the black-list of words
	 * 
	 * @param word the word that is checked against the black-list
	 * @return TRUE/FALSE
	 * @author andreas.p&ouml;rtner
	 */
	public boolean shouldIgnoreWord(String word) {

		return (_arrIgnorableWords.indexOf(word) >= 0);
	}

	/**
	 * add a new word to the black-list of words
	 * 
	 * @param word the word that should be added to the black-list
	 * @author andreas.p&ouml;rtner
	 */
	public void addIgnorableWord(String word) {

		if (_arrIgnorableWords.indexOf(word) == -1)
			_arrIgnorableWords.add(word);
	}

	/**
	 * removes a word from the black-list of words
	 * 
	 * @param word the word that should be removed from the black-list
	 * @author andreas.p&ouml;rtner
	 */
	public void removeIgnorableWord(String word) {

		int i = _arrIgnorableWords.indexOf(word);

		if (i >= 0)
			_arrIgnorableWords.remove(i);
	}

	/**
	 * getter to retrieve the black-list of words
	 * 
	 * @return the black-list of words
	 * @author andreas.p&ouml;rtner
	 */
	public String[] getIgnorableWords() {

		return _arrIgnorableWords.toArray(new String[0]);
	}

	/**
	 * getter to retrieve all known file handler based on the package and interface specification
	 * 
	 * @return a list of all known file handler
	 * @author andreas.gerlach
	 */
	public ArrayList<IFileHandler> getAvailableFileHandler() {

		ArrayList<IFileHandler> lstResult = new ArrayList<IFileHandler>();

		String packageName = "asta.model";

		// find all classes in the package
		for (String className : FindClasses(packageName)) {

			Object o = null;

			try {

				// try to create an instance of the specific class
				o = Class.forName(packageName + "." + className).newInstance();

				// and check if it is of type IFileHandler
				if (o instanceof IFileHandler) {

					lstResult.add((IFileHandler) o);
				}

			} catch (InstantiationException e) {

			} catch (IllegalAccessException e) {

			} catch (ClassNotFoundException e) {

			}
		}

		return lstResult;
	}

	/**
	 * getter to retrieve all known processing strategies based on the package and interface specification
	 * 
	 * @return a list of all known processing strategies
	 * @author andreas.gerlach
	 */
	public ArrayList<ITextProcessingStrategy> getAvailableTextProcessors() {

		ArrayList<ITextProcessingStrategy> lstResult = new ArrayList<ITextProcessingStrategy>();

		String packageName = "asta.model";

		// find all classes in the package
		for (String className : FindClasses(packageName)) {

			Object o = null;

			try {

				// try to create a new instance of the specific class
				o = Class.forName(packageName + "." + className).newInstance();

				// and check if it is of type ITextProcessingStrategy
				if (o instanceof ITextProcessingStrategy) {

					lstResult.add((ITextProcessingStrategy) o);
				}

			} catch (InstantiationException e) {

			} catch (IllegalAccessException e) {

			} catch (ClassNotFoundException e) {

			}
		}

		return lstResult;
	}

	/**
	 * find all classes in the specified java package
	 * whether it be placed in a JAR file or as single items on the physical disc
	 * 
	 * @param packageName the name of the package to be examined
	 * @return a list of classes belonging to this package
	 * @author andreas.gerlach
	 */
	ArrayList<String> FindClasses(String packageName) {

		ArrayList<String> result = new ArrayList<String>();

		URL currentLoc = App.class.getProtectionDomain().getCodeSource().getLocation();
		
		File directory = new File(String.format("%s%s", currentLoc.getFile(),
				packageName.replace(".", "/")));

		if (directory.exists()) {

			String[] files = directory.list();

			for (String file : files) {

				if (file.endsWith(".class")) {

					String className = file.substring(0, file.length() - 6);
					
					result.add(className);
				}
			}
		} else {

			try {

				String pathName = packageName.replace(".", "/");
				
				JarFile jarFile = new JarFile(currentLoc.getFile() + "ASTA.jar");
				Enumeration<JarEntry> jarEntries = jarFile.entries();

				while (jarEntries.hasMoreElements()) {

					JarEntry jarEntry = jarEntries.nextElement();
					String jarEntryName = jarEntry.getName();
					
					if (jarEntryName.startsWith(pathName)
							&& jarEntryName.endsWith(".class")) {

						String className = jarEntryName.substring(pathName.length() + 1, 
																	jarEntryName.length() - 6);
						
						result.add(className);
					}
				}

			} catch (IOException e) {

				AppController.getInstance().handleException(e);
			}

		}

		return result;
	}
}
