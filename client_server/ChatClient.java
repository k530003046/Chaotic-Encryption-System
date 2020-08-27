package client_server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class ChatClient extends UnicastRemoteObject implements IChatClient, Serializable{
	QQ gui = new QQ();
	IChatService service;
	String name;
	String file_name;
	
	protected ChatClient(QQ qq, String user) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		gui = qq;
		name = user;
	}
	

	public String getName() {
		return name;
	}

	public void receiveMessage(String msg) throws RemoteException {
		gui.setTxt(msg);
	}
	
	public void startClient() throws RemoteException, MalformedURLException, NotBoundException {
		try {
			service = (IChatService)Naming.lookup("rmi://localhost:3000/chat");
			service.addClient(this, this.getName()+" has enrolled in chat room");
			service.updateAllClient(); 
		}catch(RemoteException e) {
			e.printStackTrace();
		}
	}
	public void sendMessage() throws RemoteException {
		try {
			service.sendMessage(this, gui.getTxt(),"user");
		}catch(RemoteException e) {
			e.printStackTrace();
		}
	}
	// to insert a client at the right side of GUI 
	public void receiveClient(IChatClient c) throws RemoteException {
		gui.setUser(c.getName(), c);
	}
	public void quit() throws RemoteException{
		service.removeClient(this, this.getName()+" has quited chat room");
	}

	public void rmvCmp() throws RemoteException {
		gui.removeComponents();
		gui.updateComponents();
	}
	public byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		// ��ȡ�ļ���С
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
		// �ļ�̫���޷���ȡ
		throw new IOException("File is to large "+file.getName());
		}
		// ����һ�������������ļ�����
		byte[] bytes = new byte[(int)length];
		// ��ȡ���ݵ�byte������
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
		&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		offset += numRead;
		}
		// ȷ���������ݾ�����ȡ
		if (offset < bytes.length) {
		throw new IOException("Could not completely read file "+file.getName());
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
		}
    public void updateFile(byte[]fileByte, String fileName) throws RemoteException{
    	service.sendFile(this,fileByte,fileName);  	
    }
    public void getFile(byte[] bfile,String fileName) {  
    	file_name = fileName;
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            file = new File(fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        } 
        gui.setImage(fileName);
    }
    public void privateSend(QQ qq, IChatClient c) throws RemoteException {
    	String msg = qq.getTxt();
    	service.sendAlone(qq.getClient(), c, msg);
    }
}
