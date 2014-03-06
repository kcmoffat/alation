package alation;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.io.Serializable;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class QueryServer {
	Map<String, List<Pair>> index;
	
	/**
	 * Allocate a new QueryServer, and parse the input JSON into
	 * a Map
	 * @param D
	 */
	public QueryServer (Serializable D) {
		Type hashMapType = new TypeToken<HashMap<String, List<Pair>>> () {}.getType();
		Gson gson = new Gson();
		this.index = gson.fromJson((String)D, hashMapType);
	}
	
	/**
	 * Returns the top 10 names by score matching the query String s
	 * in the following ways:
	 * 1)  name starts with s
	 * 2)  name contains '_s'
	 * @param s	query String
	 * @return	List<String>	top 10 names matching s in decreasing order
	 */
	public List<String> query (String s) {
		List<String> result = null;
		s = s.toLowerCase();
		List<Pair> matches = index.get(s);
		if (matches != null) {
			result = new LinkedList<String> ();
			for (int i = 0; i < Math.min(10,  matches.size()); i++) {
				result.add(matches.get(i).name());
			}
		}
		return result;
	}
}
