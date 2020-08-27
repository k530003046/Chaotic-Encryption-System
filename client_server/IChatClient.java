package client_server;
import java.net.MalformedURLException;
import java.rmi.*;

public interface IChatClient extends Remote{
	public String getName() throws RemoteException;
	public void receiveMessage(String msg) throws RemoteException;
	
	/*
	public void connect();
	public void disconnect();
	public void check();
	*/
	// ------ 添加部份 
	public void sendMessage() throws RemoteException;
	public void startClient() throws RemoteException, MalformedURLException, NotBoundException;
	public void receiveClient(IChatClient c) throws RemoteException;
	public void quit() throws RemoteException;
	public void rmvCmp() throws RemoteException;
	public void getFile(byte[] fileByte, String fileName) throws RemoteException;
	public void privateSend(QQ qq, IChatClient b) throws RemoteException;
}
