
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserRemote implements ParserInterface{
	private static final long serialVersionUID = 227L;

	ParserRemote() throws RemoteException{
		super();
	}

	public Map<String, Integer> parse_log(String log){
		Map<String, Integer> category_counter = new HashMap<String, Integer>();
		String pattern = "\\[\\d+\\]: (.+?) (for|from)";

		Pattern r = Pattern.compile(pattern);
		String[] lines = log.split("\r\n|\r|\n");
		int line_counter = lines.length;


		Matcher matcher = r.matcher(log);
		int matching_count = 0;

		while(matcher.find()){
			matching_count += 1;
			String key_string = "";

			if(matcher.group(2) == "for"){
				key_string = matcher.group(1);
			}
			else{
				int last_index = matcher.group(1).lastIndexOf(" ");
				if(last_index > 0){
					key_string = matcher.group(1).substring(0, last_index);
				}
				else{
					key_string = matcher.group(1);
				}
			}

			if(category_counter.containsKey(key_string)){
				category_counter.put(key_string, category_counter.get(key_string) + 1);
			}
			else{
				category_counter.put(key_string, 1);
			}
		}

		category_counter.put("Unidentified", line_counter - matching_count);
		return category_counter;
	}
}
