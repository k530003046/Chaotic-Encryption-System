package crypto;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import rmi.QQ;

public class CMET_2 {

	public static final double CHAOTIC_BEHAVIOR = 3.78;
	public static final double CHOATIC_BEHAVIOR_1 = 3.63;
	public static final double CHAOTIC_BEHAVIOR_2 = 3.98;
	
	public CMET_2() {}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String key = keyGen(64);
		String noise = noiseGen(key);
		String msg = "Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!qte";
		String cipher = encryptByNoise(noise, msg);
		File file = new File("cipher.txt");
		if(file.getParentFile() != null)
			file.mkdir();
		String plain = decryptByNoise(noise, file);
		
		Scanner s = new Scanner(System.in);
		System.out.println("Please input the path of file");
		String path = s.next();
		System.out.println(path);
		File img = new File(path);
		File cipherI = new File("cipherI.png");
		encryptWithImage(img, noise);
		decryptWithImage(cipherI, noise);
	}

	/**
	 * <p>
	 * Key Generator (ASCII)
	 * </p>
	 * 
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public static String keyGen(int size) throws Exception {
		System.out.println("-------------- keyGen -------------");
		byte[] key = new byte[size / 8];
		Random rand = new Random();
		for(int i = 0; i < key.length; i++)
			key[i] = (byte) (33 + rand.nextInt(94));
		File keyFile = new File("key");
		if(keyFile.getParentFile() != null)
			keyFile.getParentFile().mkdir();
		keyFile.createNewFile();
		FileOutputStream out = new FileOutputStream(keyFile);
		out.write(key);
		out.close();
		for(int i = 0; i < key.length; i++)
			System.out.print(key[i] + " ");
		System.out.println();
		String keyS = new String(key, "ISO8859-1");
		System.out.println(keyS);
		return keyS;
	}
	
	/**
	 * <p>
	 * Noise Generator 
	 * </p>
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String noiseGen(String keyS) throws Exception {
		System.out.println("--------------- noiseGen --------------");
		byte[] key = keyS.getBytes("ISO8859-1");
		for(int m = 0; m < key.length; m++)
			System.out.print(key[m] + " ");
		System.out.println();
			
		double[] mapDoubles = new double[key.length];
		byte[] mapBytes = new byte[key.length * 8];
		for(int m = 0; m < key.length; m++) {
			double temp = key[m];
			temp /= 256;
			for(int n = 0; n < (int) key[m]; n++)
				temp = logistic_map(temp, CHAOTIC_BEHAVIOR);
			temp *= key[m];
			mapDoubles[m] = temp;
		}
		int inputLen = mapBytes.length;
		int offSet = 0;
		int i = 0;
		
		while(inputLen > offSet) {
			byte[] temp = new byte[8];
			temp = doubleToByte(mapDoubles[i]);
			for(int j = 0; j < 8; j++) {
				mapBytes[offSet] = temp[j];
				offSet++;
			}
			i++;
		}
		for(int m = 0; m < mapBytes.length; m++)
			System.out.print(mapBytes[m] + " ");
		String noise = new String(mapBytes, "ISO8859-1");
		System.out.println("\n" + noise);
		return noise;
	}
	
	/**
	 * <p>
	 * Encrypt with noise
	 * </p>
	 * 
	 * @param noiseS
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptByNoise(String noiseS, String data) throws Exception {
		System.out.println("------------------ encryptByNoise ---------------");
		
		byte[] noise = noiseS.getBytes("ISO8859-1");
		for(int m = 0; m < noise.length; m++)
			System.out.print(noise[m] + " ");
		System.out.println();
		
		int controlByteNum = (int) ((noise.length / 128) + 0.5) + 1;
		System.out.println("Control Byte number: " + controlByteNum);
		byte[] plainByte = data.getBytes("ISO8859-1");
		byte[] cipherByte;
		// if the raw data is txt
		// if the raw data is image
		//cipherByte[0] = 1;
		
		/**
		 * <p>
		 * The plain data is smaller than the noise
		 * The control byte records the negative difference between of the number of plain data bytes and 128
		 * </p>
		 */
		if(plainByte.length <= noise.length) {
			cipherByte = new byte[noise.length + controlByteNum];
			System.out.println("using < now");
			cipherByte[0] = 0;
			int quotient = (noise.length - plainByte.length) / 128;
			int mod = (noise.length - plainByte.length) % 128;
			if(quotient == 0) {
				for(int i = 1; i < controlByteNum - 1; i++)
					cipherByte[i] = 0;
				cipherByte[controlByteNum - 1] = (byte) -mod;
			}
			else {
				for(int i = 1; i < quotient + 1; i++) 
					cipherByte[i] = -128;
				cipherByte[quotient + 1] = (byte) -mod;
				for(int j = quotient + 2; j < controlByteNum - 1; j++)
					cipherByte[j] = 0;
			}
			
			for(int i = 0; i < noise.length; i++) {
				if(i < plainByte.length) {
					cipherByte[i + controlByteNum] = (byte) (noise[i] ^ plainByte[i]);
				}
				else {
					if((i + controlByteNum) >= cipherByte.length)
						break;
					cipherByte[i + controlByteNum] = noise[i];
				}
			}
		}
		
		/**
		 * <p>
		 * The plain data is bigger than the number of noise byte
		 * The first byte records the number of loop the noise has been used (quotient)
		 * The several bytes record the difference of the remainder and 127
		 * </p>
		 */
		else {
			cipherByte = new byte[plainByte.length + controlByteNum];
			System.out.println("using > now");
			int quotient = plainByte.length / 128;
			System.out.println(quotient);
			int mod = plainByte.length % 128;
			cipherByte[0] = (byte) quotient;
			cipherByte[1] = (byte) mod;
			for(int i = 2; i < controlByteNum - 1; i++) 
				cipherByte[i] = 0;
			
			int offSet = controlByteNum;
			int inputLen = cipherByte.length;
			int k = 0;
			while(inputLen > offSet) {
				if(k == noise.length) 
					k = 0;
				cipherByte[offSet] = (byte) (noise[k] ^ plainByte[offSet - controlByteNum]);
				k++;
				offSet++;
			}
		}
				
		String cipherText = Base64.getEncoder().encodeToString(cipherByte);
		for(int m = 0; m < cipherByte.length; m++)
			System.out.print(cipherByte[m] + " ");
		System.out.println();
		System.out.println(cipherText);
		System.out.println(cipherByte.length);
		
		File file = new File("cipher.txt");
		if(file.getParentFile() != null)
			file.getParentFile().mkdir();
		file.createNewFile();
		QQ.fileWriter(cipherText, file);
		return cipherText;
	}
	
	/**
	 * <p>
	 * Encrypt the image with chaotic noise
	 * </p>
	 * 
	 * @param file
	 * @param noiseS
	 * @throws IOException
	 */
	public static void encryptWithImage(File file, String noiseS) throws IOException {
		System.out.println("------------ encryptWithImage --------------");
		byte[] noiseByte = noiseS.getBytes("ISO8859-1");
		byte[] temp = new byte[8];
		double[] noiseD = new double[noiseByte.length / 8];
		int inputLen = noiseByte.length;
		int offSet = 0;
		int k = 0;
		int l = 0;
		int[] noiseI = new int[noiseByte.length / 8];
		while(inputLen > offSet) {
			temp[k] = noiseByte[offSet];
			k++;
			offSet++;
			if(k == 8) {
				noiseD[l] = getDouble(temp);
				if(l == noiseD.length) {
					break;
				}
				k = 0;
				noiseI[l] = (int) (476381 * noiseD[l]);
				System.out.print(noiseD[l] + " " + 476381 * noiseD[l] + " " + noiseI[l]);
				System.out.println();
				l++;
			}
		}
		
		BufferedImage bi = ImageIO.read(file);
		File cipher = new File("cipherI.png");
		
		for(int i = 0; i < bi.getWidth(); i++) {
			int n = 0;
			for(int j = 0; j < bi.getHeight(); j++) {
				bi.setRGB(i, j, bi.getRGB(i, j) ^ noiseI[n]);
				n++;
				if(n == noiseI.length)
					n = 0;
			}
		}
		ImageIO.write(bi, "png", cipher);
	}
	
	/**
	 * <p>
	 * Decrypt the cipher image with noise
	 * </p>
	 * 
	 * @param file
	 * @param noiseS
	 * @throws IOException
	 */
	public static void decryptWithImage(File file, String noiseS) throws IOException {
		System.out.println("------------- decryptWithNoise ------------");
		byte[] noiseByte = noiseS.getBytes("ISO8859-1");
		byte[] temp = new byte[8];
		double[] noiseD = new double[noiseByte.length / 8];
		int inputLen = noiseByte.length;
		int offSet = 0;
		int k = 0;
		int l = 0;
		int[] noiseI = new int[noiseByte.length / 8];
		while(inputLen > offSet) {
			temp[k] = noiseByte[offSet];
			k++;
			offSet++;
			if(k == 8) {
				noiseD[l] = getDouble(temp);
				if(l == noiseD.length) {
					break;
				}
				k = 0;
				noiseI[l] = (int) (476381 * noiseD[l]);
				System.out.print(noiseD[l] + " " + 476381 * noiseD[l] + " " + noiseI[l]);
				System.out.println();
				l++;
			}
		}
		
		BufferedImage bi = ImageIO.read(file);
		File plain = new File("plainI.png");
		
		for(int i = 0; i < bi.getWidth(); i++) {
			int n = 0;
			for(int j = 0; j < bi.getHeight(); j++) {
				bi.setRGB(i, j, bi.getRGB(i, j) ^ noiseI[n]);
				n++;
				if(n == noiseI.length)
					n = 0;
			}
		}
		ImageIO.write(bi, "png", plain);
	}
	
	/**
	 * <p>
	 * Decrypt the cipher text with noise
	 * </p>
	 * 
	 * @param noiseS
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String decryptByNoise(String noiseS, File file) throws Exception {
		System.out.println("---------------- decryptByNoise --------------");
		
		byte[] noise = noiseS.getBytes("ISO8859-1");
		int controlByteNum = (int) ((noise.length / 128) + 0.5) + 1;
		byte[] cipherByte;
		
		/*byte[] cipherByte = data.getBytes("ISO8859-1");
		System.out.println(cipherByte.length);
		for(int m = 0; m < cipherByte.length; m++)
			System.out.print(cipherByte[m] + " ");
		System.out.println();
		*/
		//
		
		String s = QQ.fileReader(file);
		System.out.println(s + "!");
		cipherByte = Base64.getDecoder().decode(s);
		
//		FileInputStream fin = new FileInputStream(file);
//		int buffer[] = new int[controlByteNum];
//		int n = 0;
//		System.out.println(controlByteNum);
//		for(int m = 0; m < controlByteNum - 1; m++) {
//			if(m == buffer.length)
//				break;
//			n = fin.read();
//			buffer[m] = n;
//			System.out.print(buffer[m]);
//		}
//		System.out.println();
//		System.out.println(buffer[0]);
//		if(buffer[0] == 0) {
//			cipherByte = new byte[noise.length + controlByteNum];
//		}
//		else {
//			cipherByte = new byte[buffer[0] * 128 + buffer[1] + controlByteNum];
//		}
//		for(int m = 0; m < controlByteNum - 1; m++)
//			cipherByte[m] = (byte) buffer[m];
//		//
//		
//		int a = controlByteNum - 1;
//		while( n != -1) {
//			if(a == cipherByte.length)
//				break;
//			n = fin.read();
//			cipherByte[a] = (byte) n;
//			a++;
//		}
//		fin.close();
//		for(int m = 0; m < cipherByte.length; m++)
//			System.out.print(cipherByte[m] + " ");
//		System.out.println();
		//
		
		int sum;
		if(cipherByte[0] == 0) {
			System.out.println("Use 0");
			sum = noise.length;
			for(int i = 1; i < controlByteNum; i++)
				sum += cipherByte[i];
			byte[] plainByte = new byte[sum];
			int k = 0;
			for(int i = controlByteNum; i < cipherByte.length; i++) {
				if(k < plainByte.length) {
					plainByte[k] = (byte) (cipherByte[i] ^ noise[i - controlByteNum]);
					k++;
				}
				if(k == plainByte.length) {
					for(int j = 0; j < plainByte.length; j++)
						System.out.print(plainByte[j] + " ");
					System.out.println();
					String plainText = new String(plainByte, "ISO8859-1");
					System.out.println(plainText);
					return plainText;
				}
			}
			
		}
		System.out.println("Use 128");
		sum = 128 * cipherByte[0] + cipherByte[1];
		System.out.println(sum);
		for(int i = 1; i < controlByteNum; i++)
			sum += cipherByte[i];
		byte[] plainByte = new byte[sum];
		
		int offSet = controlByteNum;
		int inputLen = plainByte.length;
		int k = 0;
		while(inputLen > offSet) {
			if(offSet == cipherByte.length)
				break;
			if(k == noise.length) 
				k = 0;
			plainByte[offSet - controlByteNum] = (byte) (noise[k] ^ cipherByte[offSet]);
			k++;
			offSet++;
		}
		String plainText = new String(plainByte, "ISO8859-1");
		System.out.println(plainText);
		return plainText;
		
	}

	/**
	 * <p>
	 * Logistic mapping to indicate the chaotic behavior
	 * </p>
	 * 
	 * @param x
	 * @param lamda
	 * @return
	 */
	public static double logistic_map(double x, double lamda) {
		return lamda * x * (1 - x);
	}
	
	public static double[] logistic_map_2D(double x, double y, double lamda1, double lamda2, double u, double u1) {
		double x_prime = u* lamda1* x* (1 - x)+ u1* y;
		double y_prime = u* lamda2* x* (1 - y)+ u1* x;
		double[] result = {x_prime, y_prime};
		return result;
	}
	
	public static double[][] gen2DTransMatrix(Dimension d, double lamda1, double lamda2, double u, double u1, double[] key) {
		int m = (int) d.getSize().getHeight();
		int n = (int) d.getSize().getWidth();
		int size = m * n;
		
		double x = key[0];
		double y = key[1];
		double x_prime, y_prime;
		double [] P = new double[size];
		P[0] = x;
		double [] Q = new double[size];
		Q[0] = y;
		for(int i = 0; i < size; i++) {
			x_prime = logistic_map_2D(x, y, lamda1, lamda2, u, u1)[0];
			y_prime = logistic_map_2D(x, y, lamda1, lamda2, u, u1)[1];
			x = x_prime;
			y = y_prime;
			P[i + 1] = x;
			Q[i + 1] = y;
		}
		
		for(int i = 0; i < size; i++) {
			P[i] = (P[i] * Math.pow(10, 15)) % Math.pow(10, 6);
			Q[i] = (Q[i] * Math.pow(10, 15)) % Math.pow(10, 6);
		}
		
		double[][] transMatrix_X = new double[m][n];
		double[][] transMatrix_Y = new double[m][n];
		int i = 0, j = 0;
		for(int k = 0; k < size; k++) {
			transMatrix_X[i][j] = P[k] % m;
			transMatrix_Y[i][j] = Q[k] % n;
			i = (int)(k / n);
			j = k % n;
		}
		return transMatrix_X;
	}
	
	/**
	  * <p>
	  * double to byte (double -> long -> byte)
	  * transform double to long which means it can be calculate correctly and make the encryption reversible
	  * </p>
	  * 
	  * @param num
	  * @return
	  */
	 public static byte[] doubleToByte(double num) {    
	        byte[] b = new byte[8];    
	        long l = Double.doubleToLongBits(num);    
	        for (int i = 0; i < 8; i++) {    
	            b[i] = new Long(l).byteValue();    
	            l = l >> 8;    
	        	System.out.print(b[i] + " ");
	        }  
	        System.out.println();
	        return b;  
	    }  
	 
	 /**
	  * <p>
	  * byte to double
	  * transpose the bit then and gate with hexadecimal number of byte. 
	  * </p>
	  * 
	  * @param b
	  * @return
	  */
	 public static double getDouble(byte[] b) {    
	        long m;    
	        m = b[0];    
	        m &= 0xff;    
	        m |= ((long) b[1] << 8);    
	        m &= 0xffff;    
	        m |= ((long) b[2] << 16);    
	        m &= 0xffffff;    
	        m |= ((long) b[3] << 24);    
	        m &= 0xffffffffl;    
	        m |= ((long) b[4] << 32);    
	        m &= 0xffffffffffl;    
	        m |= ((long) b[5] << 40);    
	        m &= 0xffffffffffffl;    
	        m |= ((long) b[6] << 48);    
	        m &= 0xffffffffffffffl;    
	        m |= ((long) b[7] << 56);    
	        return Double.longBitsToDouble(m);    
	    }  
	 
}   
