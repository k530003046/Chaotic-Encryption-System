package crypto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.security.Key;  
import java.security.KeyFactory;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.Signature;  
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  
import java.security.spec.PKCS8EncodedKeySpec;  
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;  
import java.util.Map;  
  
import javax.crypto.Cipher;  
  
/** *//** 
 *
 */  
public class RSA {  
  
    /** *//** 
     * Encryption Algorithm
     */  
    public static final String KEY_ALGORITHM = "RSA";  
      
    /** *//** 
     * Signature Algorithm 
     */  
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";  
  
    /** *//** 
     * Obtain the public key
     */  
    private static final String PUBLIC_KEY = "RSAPublicKey";  
      
    /** *//** 
     * Obtain the private key
     */  
    private static final String PRIVATE_KEY = "RSAPrivateKey";  
      
    /** *//** 
     * Maximum size of encryption block
     */  
    private static final int MAX_ENCRYPT_BLOCK = 117;  
      
    /** *//** 
     * Maximum size of decryption block 
     */  
    private static final int MAX_DECRYPT_BLOCK = 128;  
  
    /** *//** 
     * <p> 
     * Generate the pair of public key and private key 
     * </p> 
     *  
     * @return 
     * @throws Exception 
     */  
    public static Map<String, Object> genKeyPair() throws Exception {  
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);  
        keyPairGen.initialize(1024);  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();  
        Map<String, Object> keyMap = new HashMap<String, Object>(2);  
        keyMap.put(PUBLIC_KEY, publicKey);  
        keyMap.put(PRIVATE_KEY, privateKey);  
        return keyMap;  
    }  
      
    /** *//** 
     * <p> 
     * Sign with private key 
     * </p> 
     *  
     * @param data (Encrypted)
     * @param privateKey 私钥(BASE64) 
     *  
     * @return 
     * @throws Exception 
     */  
    public static String sign(byte[] data, String privateKey) throws Exception {  
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes); 
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initSign(privateK);  
        signature.update(data);  
        return Base64.getEncoder().encodeToString(signature.sign());  
    }  
  
    /** *//** 
     * <p> 
     * Verify the signature
     * </p> 
     *  
     * @param data (Encrypted) 
     * @param publicKey 公钥(BASE64) 
     * @param sign 
     *  
     * @return 
     * @throws Exception 
     *  
     */  
    public static boolean verify(byte[] data, String publicKey, String sign)  
            throws Exception {  
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        PublicKey publicK = keyFactory.generatePublic(keySpec);  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initVerify(publicK);  
        signature.update(data);  
        return signature.verify(Base64.getDecoder().decode(sign));  
    }  
  
    /** *//** 
     * <P> 
     * Decrypted with private key 
     * </p> 
     *  
     * @param encryptedData  
     * @param privateKey (BASE64) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)  
            throws Exception {  
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
   
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close(); 
        File file = new File("RSA_Decrypt_key");
        if(file.getParentFile() != null)
        	file.getParentFile().mkdir();
        file.createNewFile();
        FileWriter fr = new FileWriter(file);
        fr.write(new String(decryptedData));
        fr.flush();
        fr.close();
        
        return decryptedData;  
    }  
  
    /** *//** 
     * <p> 
     * Decrypted with public key
     * </p> 
     *  
     * @param encryptedData
     * @param publicKey (BASE64) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)  
            throws Exception {  
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, publicK);  
        int inputLen = encryptedData.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_DECRYPT_BLOCK;  
        }  
        byte[] decryptedData = out.toByteArray();  
        out.close();  
        return decryptedData;  
    }  
  
    /** *//** 
     * <p> 
     * Encrypted with public key
     * </p> 
     *  
     * @param data  
     * @param publicKey (BASE64) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)  
            throws Exception {  
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);  
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key publicK = keyFactory.generatePublic(x509KeySpec);  
        
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, publicK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
         
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }  
  
    /** *//** 
     * <p> 
     * Encypted with private key
     * </p> 
     *  
     * @param data  
     * @param privateKey (BASE64) 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)  
            throws Exception {  
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);  
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);  
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateK);  
        int inputLen = data.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
          
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);  
            } else {  
                cache = cipher.doFinal(data, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * MAX_ENCRYPT_BLOCK;  
        }  
        byte[] encryptedData = out.toByteArray();  
        out.close();  
        return encryptedData;  
    }  
  
    /** *//** 
     * <p> 
     * Obtain the private key
     * </p> 
     *  
     * @param keyMap  
     * @return 
     * @throws Exception 
     */  
    public static String getPrivateKey(Map<String, Object> keyMap)  
            throws Exception {  
        Key key = (Key) keyMap.get(PRIVATE_KEY);  
        File privateFile = new File("PrivateKey");
        if(privateFile.getParentFile() != null)
        	privateFile.getParentFile().mkdir();
        privateFile.createNewFile();
        FileWriter publicKeyOS = new FileWriter(privateFile);
        publicKeyOS.write(Base64.getEncoder().encodeToString(key.getEncoded()));
        publicKeyOS.flush();
        publicKeyOS.close();
        return Base64.getEncoder().encodeToString(key.getEncoded());  
    }  
  
    /** *//** 
     * <p> 
     * Obtain the public key
     * </p> 
     *  
     * @param keyMap 
     * @return 
     * @throws Exception 
     */  
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {  
        Key key = (Key) keyMap.get(PUBLIC_KEY);  
        File publicFile = new File("PublicKey");
        if(publicFile.getParentFile() != null)
        	publicFile.getParentFile().mkdir();
        publicFile.createNewFile();
        FileWriter publicKeyOS = new FileWriter(publicFile);
        publicKeyOS.write(Base64.getEncoder().encodeToString(key.getEncoded()));
        publicKeyOS.flush();
        publicKeyOS.close();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
    public static void main(String[] args) throws Exception {
		Map map = genKeyPair();
		String publicKey = getPublicKey(map);
		System.out.println(publicKey);
		String privateKey = getPrivateKey(map);
		System.out.println(privateKey);
		
		String plainText = "Hello world!";
		System.out.println(plainText);
		byte[] cipherText = encryptByPublicKey(plainText.getBytes(), publicKey);
		System.out.println(new String(cipherText));
		
		byte[] decryptedText = decryptByPrivateKey(cipherText, privateKey);
		System.out.println(new String(decryptedText));
		
		String sign = sign(plainText.getBytes(), privateKey);
		boolean result = verify(decryptedText, publicKey, sign);
		System.out.println(result);
		
	}
}