
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public interface ParserInterface extends Remote {

	public Map<String, Integer> parse_log(String log) throws RemoteException;

}
