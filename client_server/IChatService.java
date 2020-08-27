package client_server;
import java.rmi.*;

public interface IChatService extends Remote{
	public void addClient(IChatClient c, String msg) throws RemoteException;
	public void removeClient(IChatClient c, String msg) throws RemoteException;
	public void sendMessage(IChatClient c, String msg, String type) throws RemoteException;
	public void updateAllClient() throws RemoteException;
	public void sendFile(IChatClient c,byte[]fileByte,String fileName) throws RemoteException;
	public void sendAlone(IChatClient a, IChatClient b, String msg) throws RemoteException;
}
