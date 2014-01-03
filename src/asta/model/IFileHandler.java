/**
 * A Students Text Analyzer
 */
package asta.model;

import java.io.IOException;
import java.util.Iterator;

import javax.swing.filechooser.FileFilter;

/**
 * an interface for handling file access and providing information about the file content
 * 
 * @author  andreas.gerlach
 */
public interface IFileHandler extends Iterator<String> {

    /**
     * getter to retrieve the length of the file
     * 
     * @return the length of the file
     * @author andreas.gerlach
     */
    public long getFileLength();
    
    /**
     * getter to retrieve the current position in the file
     * 
     * @return the current position in the file
     * @author andreas.gerlach
     */
    public long getCurrentPosition();

    /**
     * getter to retrieve the raw-text of the file's content
     * 
	 * @return the raw-text of the file's content
	 */
    public StringBuffer getPlainText();

    /**
     * getter to retrieve a file filter object specifying what kind of
     * files this file handler is capable to open and understand
     * 
	 * @return the file filter object with the supported file types
	 * @author andreas.gerlach
	 */
    public FileFilter getFileFilter();

    /**
     * getter to retrieve the name of the file
     * 
	 * @return the name of the file
	 * @author andreas.gerlach
	 */
    public String getFile();

    /**
     * setter to set the file that should be opened and read
     * 
	 * @param  fileName the name of the file to be opened and read
	 * @author andreas.gerlach
	 */
    public void setFile(String fileName) throws IOException;
}

