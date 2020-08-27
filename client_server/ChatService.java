package client_server;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ChatService extends UnicastRemoteObject implements IChatService{
	Vector clients;
	log chatlog=new log();
	public ChatService() throws RemoteException{
		super();
		clients=new Vector();
	}
	
	//This method is to announce the new chatter, and add it to the list.
	public void addClient(IChatClient c, String msg) throws RemoteException {
		clients.addElement(c);			
		this.sendMessage(c,msg,"system");
		updateAllClient();

	}
	
	//This method is to announce the leave of a chatter, and remove it from the list.
	public void removeClient(IChatClient c, String msg) throws RemoteException {
		this.sendMessage(c,msg,"system");
		clients.removeElement(c);
		updateAllClient();
		
	}
	
	//This method is to broadcast to all user a message.
	//Depending on its type, it will send a system message or a user message.
	//Though these two message are same.
	public void sendMessage(IChatClient c, String msg, String type) throws RemoteException {
		if(!clients.contains(c))
			return;		
		String user=c.getName();
		if(type.equals("system")){
			for(int i=0;i<clients.size();i++){
				((IChatClient)clients.elementAt(i)).receiveMessage("******[System]: "+msg+"******");
			}
			chatlog.write("******[System]: "+msg+"******");
		}
		else if(type.equals("user")){
			for(int i=0;i<clients.size();i++){				
				((IChatClient)clients.elementAt(i)).receiveMessage("["+user+"]: "+msg);	
			}
			chatlog.write("["+user+"]: "+msg);	
		}
	}
	


	//this method is to update the user list in all client GUI. 
	public void updateAllClient() throws RemoteException {
		for(int i = 0; i < clients.size(); i++) {
			((IChatClient)clients.elementAt(i)).rmvCmp();
			for(int x = 0; x < clients.size(); x++) {
				((IChatClient)clients.elementAt(i)).receiveClient((IChatClient)clients.elementAt(x));
			}
		}
	}
	
	public void sendFile(IChatClient c,byte[]fileByte,String fileName) throws RemoteException{
		sendMessage(c, c.getName()+" has sent a file: " + fileName, "user");
		for(int i=0;i<clients.size();i++){
			if(!c.equals(((IChatClient)clients.elementAt(i))))
			((IChatClient)clients.elementAt(i)).getFile(fileByte,fileName);
		}
	}
	public void sendAlone(IChatClient a, IChatClient b, String msg) throws RemoteException{
		String user = a.getName();
		b.receiveMessage("["+user+"]: "+"(Private)"+msg);
		chatlog.write("["+user+"]: "+"(Private)"+msg);
	}
	
}
