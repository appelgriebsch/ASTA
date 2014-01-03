/**
 * A Students Text Analyzer
 */
package asta.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.filechooser.FileFilter;

import asta.controller.AppController;

/**
 * a file handler implementation to open and read
 * plain text files
 * 
 * @author andreas.gerlach
 */
public class PlainTextFileHandler implements IFileHandler {

	/**
	 * the reference to the file that should be handled
	 */
	private File _file = null;

	/**
	 * the actual position in the file
	 */
	private long _filePos = 0;

	/**
	 * a buffer containing the file content that has been read so far
	 */
	private StringBuffer _fileContent = null;

	/**
	 * line endings used by the windows operating system
	 */
	private final String WINLINE_ENDINGS = new String("\r\n");
	
	/**
	 * line endings used by the *nix operating systems
	 */
	private final String UNIXLINE_ENDINGS = new String("\n");

	/**
	 * the charset of the file that should be used (by default: UTF8)
	 */
	private Charset _fileCharset = null;
	
	/**
     * getter to retrieve the length of the file
     * 
     * @return the length of the file
     * @author andreas.gerlach
     */
	public long getFileLength() {

		if (_file == null)
			return 0;

		return _file.length();
	}

	/**
     * getter to retrieve the current position in the file
     * 
     * @return the current position in the file
     * @author andreas.gerlach
     */
	public long getCurrentPosition() {

		return _filePos;
	}

	/**
     * getter to retrieve the raw-text of the file's content
     * 
	 * @return the raw-text of the file's content
	 */
	public StringBuffer getPlainText() {

		if (_fileContent != null)
			return _fileContent;

		return null;
	}

	/**
     * getter to retrieve a file filter object specifying what kind of
     * files this file handler is capable to open and understand
     * 
	 * @return the file filter object with the supported file types
	 * @author andreas.gerlach
	 */
	public FileFilter getFileFilter() {

		return new FileFilter() {
			
			/**
			 * getter to retrieve the description shown in the file selector
			 * 
			 * @return the description shown in the file selector
			 * @author andreas.gerlach
			 */
			@Override
			public String getDescription() {
				
				return "unformatierter Text";
			}
			
			/**
			 * check if the file is acceptable and understood by this file handler
			 * 
			 * @param f the file in question
			 * @return TRUE/FALSE
			 * @author andreas.gerlach
			 */
			@Override
			public boolean accept(File f) {
				
				if (f.isDirectory())
					return true;
					
				String fileName = f.getName().toLowerCase();
				
				return (fileName.endsWith(".txt") ||
						fileName.endsWith(".html") ||
						fileName.endsWith(".htm") ||
						fileName.endsWith(".xml"));
			}
		};
	}

	/**
     * getter to retrieve the name of the file
     * 
	 * @return the name of the file
	 * @author andreas.gerlach
	 */
	public String getFile() {

		if (_file == null)
			return null;

		return _file.getName();
	}

	/**
     * setter to set the file that should be opened and read
     * 
	 * @param  fileName the name of the file to be opened and read
	 * @author andreas.gerlach
	 */
	public void setFile(String fileName) throws IOException {

		if (_file != null)
			throw new IllegalStateException("File is already set!");

		_file = new File(fileName);
		
		if (!_file.canRead()) {
			_file = null;
			throw new IOException("File not readable!");
		}

		_fileContent = new StringBuffer((int) _file.length());
		_fileCharset = Charset.forName("UTF8");
	}

	/**
	 * checks if the end-of-file has been reached
	 * 
	 * @return TRUE/FALSE
	 * @author andreas.gerlach
	 */
	public boolean hasNext() {

		return (getCurrentPosition() < getFileLength());
	}

	/**
	 * get the next chunk of information from the file
	 * by default the file content is loaded in chunks of 64kb in size
	 * additionally it will check for windows line endings and convert them to *nix ones
	 * 
	 * @return a string representation of the actual chunk
	 * @author andreas.gerlach
	 */
	public String next() {

		BufferedInputStream stream = null;

		try {

			byte[] buffer = new byte[64 * 1024]; // 64kb buffer

			// open the file and go the the current position
			stream = new BufferedInputStream(new FileInputStream(_file));
			stream.skip(getCurrentPosition());

			// read the next 64kb chunk
			int bytesRead = stream.read(buffer, 0, buffer.length);

			// convert it into a UTF-8 string and check the line endings
			String result = new String(buffer, 0, bytesRead, _fileCharset);
			result = result.replaceAll(WINLINE_ENDINGS, UNIXLINE_ENDINGS);	
													// replace line endings from Windows to *nix

			_fileContent.append(result);
			_filePos += bytesRead;

			return result;

		} catch (IOException ex) {

			AppController.getInstance().handleException(ex);

		} finally {

			try {

				stream.close();

			} catch (IOException ex) {

				AppController.getInstance().handleException(ex);
			}
		}

		return null;
	}

	/**
    * not used in this scenario
    */
	public void remove() {
		// do nothing here
	}
}
