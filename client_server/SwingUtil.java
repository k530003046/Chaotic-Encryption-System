package client_server;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.net.URL;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class SwingUtil {
	public static final double ALIGMENT_CENTER = 0.5;
	public static final double ALIGMENT_LEFT = 0.0;
	public static final double ALIGMENT_RIGHT = 1.0;
	
	
	/**
	 * <p>
	 * Create a ImageIcon object which is 
	 * capable to adjust its size according to
	 * the size of components
	 * </p>
	 * @param image
	 * @param constrained
	 * @return icon
	 */
	public static ImageIcon createAutoAdjustIcon(Image image, boolean constrained, double zoomX, double zoomY) {
		ImageIcon icon = new ImageIcon(image){
			@Override
			public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
				Point startPoint = new Point(0, 0);
				Dimension cSize = c.getSize();
				Dimension imgSize = new Dimension(this.getIconWidth(), this.getIconHeight());
				
				if(constrained) {
					double ratio = 1.0 * imgSize.width / imgSize.height;
					imgSize.width = (int) Math.max(imgSize.width, imgSize.height * ratio);
					imgSize.height = (int) (imgSize.width / ratio);
					imgSize.width = (int) (cSize.width * zoomX);
					imgSize.height = (int) (cSize.height * zoomY);
					startPoint.x = (int) ((cSize.width - imgSize.width) * c.getAlignmentX());
					startPoint.y = (int) ((cSize.height - imgSize.height) * c.getAlignmentY());
				}
				else {
					imgSize = cSize;
				}
				
				if(this.getImageObserver() == null) {
					g.drawImage(getImage(), startPoint.x, startPoint.y, imgSize.width, imgSize.height, c);
				}else {
					g.drawImage(getImage(), startPoint.x, startPoint.y, imgSize.width, imgSize.height, getImageObserver());
				}
			}
		};
		return icon;
	}
	
	/**
	 * <p>
	 * Create an Icon object from file systems to adjust the components
	 * </p>
	 * @param filename
	 * @param constrained
	 * @return
	 */
	public static ImageIcon createAutoAdjustIcon(String filename, boolean constrained, double zoomX, double zoomY) {
		return createAutoAdjustIcon(new ImageIcon(filename).getImage(), constrained, zoomX, zoomY);
	}
	/**
	 * <p>
	 * Create an Icon object from URL to adjust the components
	 * </p>
	 * @param url
	 * @param constrained
	 * @return
	 */
	public static ImageIcon createAutoAdjustIcon(URL url, boolean constrained, double zoomX, double zoomY) {
		return createAutoAdjustIcon(new ImageIcon(url).getImage(), constrained, zoomX, zoomY);
	}

}
