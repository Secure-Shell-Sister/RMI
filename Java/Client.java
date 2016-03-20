
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Client{
	public static void main(String[] args){
		if(System.getSecurityManager() == null){
			System.setProperty("java.security.policy", "file:./security.policy");
			System.setSecurityManager(new SecurityManager());
		}
		try{
			List<Map<String, Integer> > results = new ArrayList<Map<String, Integer> >();
			String name = "LogParser";
			String log_location = "/home/revant/Documents/SISTER/RPC/NewRPC/var/log";
			if(args.length > 0){
				log_location = args[0];
			}
			Registry registry = LocateRegistry.getRegistry();
			ParserInterface engine = (ParserInterface) registry.lookup(name);

			Path dir = Paths.get(log_location);
			try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "secure*")){
				for(Path now: stream){
					String content = new String(Files.readAllBytes(now));
					//System.out.println(content);
					Map<String, Integer> result = engine.parse_log(content);
					results.add(result);
				}
			}

			Map<String, Integer> final_result = new HashMap<String, Integer>();
			for(int i = 0; i < results.size(); i++){
				Map<String, Integer> now = results.get(i);
				for(Map.Entry<String, Integer> entry: now.entrySet()){
					if(final_result.containsKey(entry.getKey())){
						final_result.put(entry.getKey(), final_result.get(entry.getKey()) + entry.getValue());
					}
					else{
						final_result.put(entry.getKey(), entry.getValue());
					}
				}
			}


			for(Map.Entry<String, Integer> entry: final_result.entrySet()){
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
		} catch(Exception e){
			System.err.println("LogParser exception:");
			e.printStackTrace();
		}
	}
}
