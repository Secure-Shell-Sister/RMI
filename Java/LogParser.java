
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.HashMap;

public class LogParser {
	public static void main(String[] args){
		if(System.getSecurityManager() == null){
			System.setProperty("java.security.policy", "file:./security.policy");
			System.setSecurityManager(new SecurityManager());
		}
		try{
			String name = "LogParser";
			ParserInterface engine = new ParserRemote();
			ParserInterface stub = (ParserInterface) UnicastRemoteObject.exportObject(engine, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, engine);

			System.out.println("LogParser bound");
		} catch(Exception e){
			System.err.println("LogParser excception:");
			e.printStackTrace();
		}
	}
}
