
package ecmgs.jbmp;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.InputStream;

/*
 * @author ecmgs
 *
 */
public class Jbmp {
	
	private Dimension size;
	
	private InputStream file;
	
	private int doffset;
	private int dlength;
	
	private int idata[];
	
	// Sets the miminum propierties for the class to work
	public Jbmp (String path) {
		
		// Reserving memory for storing the dimensions of the images
		size = new Dimension();
		
		// Try catch because we are working with files
		try {
			
			// Opening the file
			file = new FileInputStream(path);
			
			// Array with the BMP header propierties
			byte BMPHeader[] = new byte[0xE];
			
			// Saves the BMP header information in the array
			file.read(BMPHeader);
			
			// Getting where actual data starts
			for (int i = 3; i >= 0; i--) {
				if (BMPHeader[0xA+i] == 0) continue;
				
				doffset += (((int) (BMPHeader[0xA+i] < 8 ? BMPHeader[0xA+i] & 0xFF : BMPHeader[0xA+i])) << i * 8);
			}
			
			System.out.println("offset:" + doffset);
			
			// DIB header
			byte DIBHeader[] = new byte[doffset];
			
			// Stores the content of from the Header of the file to the array
			file.read(DIBHeader, 0xE, doffset-0xE);
			
			// Gets the with of the image
			for (int i = 3; i >= 0; i--) {
				if (DIBHeader[0x12+i] == 0) continue;
				
				size.width += (((int) (DIBHeader[0x12+i] < 0 ? DIBHeader[0x12+i] & 0xFF : DIBHeader[0x12+i])) << i * 8 );

			}
			
			// Gets the height of the image
			for (int i = 3; i >= 0; i--) {
				if (DIBHeader[0x16+i] == 0) continue;
				
				size.height += (((int) (DIBHeader[0x16+i] < 0 ? DIBHeader[0x16+i] & 0xFF : DIBHeader[0x16+i])) << i * 8 );
				
			}
			
			System.out.println(size.toString());
			
			// Gets the length of the color data
			for (int i = 3; i >= 0; i--) {
				if (DIBHeader[0x22+i] == 0) continue;
				
				dlength += (((int) (DIBHeader[0x22+i] < 0 ? DIBHeader[0x22+i] & 0xFF : DIBHeader[0x22+i])) << i * 8 );
				
			}
			
			System.out.println("data length: "+dlength);
			
			// stores the pixels into an array
			byte[] bidata = new byte[doffset+dlength];
			
			file.read(bidata, doffset, dlength);
			
			idata = new int[doffset+dlength];
			
			for (int i = 0; i < dlength; i++) {
				idata[doffset+i] = ((int) (bidata[doffset+i] < 0 ? bidata[doffset+i] & 0xFF : bidata[doffset+i]));
			}
			
		} catch (Exception e) {
			// Prints if there is an error
			e.printStackTrace();
		}
	}
	
	// Returns a map with the colors
	public int[][][] getImage() {
		
		int[][][] ret = new int[1152][648][3];
		
		int i = doffset;
		
		// Sets all the values
		for (int y = size.height-1; y > 0; y --) {
			
			for (int x = size.width-1; x > 0; x-- ) {
				
				ret[x][y][0] = idata[i];
				ret[x][y][1] = idata[i+1];
				ret[x][y][2] = idata[i+2];
				
				i+=3;
			}
		}
		
		return ret;
		
	}
	
	//Returns the size of the image
	public Dimension getSize() {
		return size;
	}
}
