package client_server;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class ChatServer {
	public static void main(String[] args){
		try{
			ChatService server=new ChatService();
            LocateRegistry.createRegistry(3000);
            Naming.bind("rmi://localhost:3000/chat", server);
            System.out.println("[System] Chat Remote Object is ready:");
		}
		catch(Exception e){
			System.out.println("[Error]Failed to create connection between server"+e);
		}
	}
}
