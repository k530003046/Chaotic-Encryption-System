package client_server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
public class ImageRGB {

	public int[] value;
	public int[] times;
	public static int[] getMatrixRGB(BufferedImage image){
        int w = image.getWidth();
        int h = image.getHeight();
        int[] intArray = new int[w * h];
        int[] matrixRGB = new int[w * h * 3];
        image.getRGB(0, 0, w, h, intArray, 0, w);
        for(int i = 0; i < 5; i++) {
        	System.out.print(intArray[i] + " ");
        }
        System.out.println();
        // ARGB->RGB
        for(int i=0,b=0;i<intArray.length;++i){
            matrixRGB[b++]=((intArray[i]&0x00FF0000)>>16);
            matrixRGB[b++]=((intArray[i]&0x0000FF00)>>8);
            matrixRGB[b++]=(intArray[i]&0x000000FF);
        }
        return matrixRGB;
	}
	public static int[] getRGB(int[] rgbMatrix) {		
		int[] rgbArray = new int[rgbMatrix.length / 3];
		for(int i = 0, b = 0; b < rgbMatrix.length; i++) {
			rgbArray[i] = (rgbMatrix[b++] * 256 + rgbMatrix[b++]) * 256 + rgbMatrix[b++];
		}
		for(int i = 0; i < 5; i++) {System.out.print(rgbArray[i] + " ");}
		System.out.println();
		return rgbArray;
	}
	public static int[] getMatrixR(int[] matrixRGB) {
		int[] matrixR = new int[matrixRGB.length / 3];
		for(int i = 0, b = 0; i < matrixRGB.length; i+=3) {
			matrixR[b] = matrixRGB[i];
		}
		return matrixR;
	}
	public static int[] getMatrixG(int[] matrixRGB) {
		int[] matrixG = new int[matrixRGB.length / 3];
		for(int i = 1, b = 0; i < matrixRGB.length; i+=3) {
			matrixG[b] = matrixRGB[i];
		}
		return matrixG;
	}
	public static int[] getMatrixB(int[] matrixRGB) {
		int[] matrixB = new int[matrixRGB.length / 3];
		for(int i = 2, b = 0; i < matrixRGB.length; i+=3) {
			matrixB[b] = matrixRGB[i];
		}
		return matrixB;
	}
	
	public static void countArrary(int[] arr) {
		Map<Integer, Integer>map = new HashMap<Integer, Integer>();
		for(int i = 0; i < arr.length; i++) {
			if(map.get(arr[i])!= null) {
				map.put(arr[i], arr[i+1]);
			}else {
				map.put(arr[i], 1);
			}
		}
		Set<Integer>keySet = map.keySet();
		Iterator<Integer> it = keySet.iterator();
		int i = 0;
		while(it.hasNext()) {
			Integer key = it.next();
			Integer value = map.get(key);
			System.out.print(key + "shows" + value + "times ");
		}
	}
}