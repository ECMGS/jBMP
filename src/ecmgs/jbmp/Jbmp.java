/**
 * 
 */
package ecmgs.jbmp;

import java.awt.Color;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/*
 * @author ecmgs
 *
 */
public class Jbmp {
	
	String path;
	
	InputStream file;
	
	public Jbmp () {
		this.path = "";
	}
	
	public Jbmp (String path) {
		this.path = path;	
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public boolean loadFile() {
		boolean ret = true;
		
		try {
			file = new FileInputStream(path);
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		
		return ret;
	}
	
}
