package alation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.Serializable;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Constructor {
	private Serializable D;
	private Map<String, List<Pair>> index;
	
	/**
	 * Allocate a Constructor, create a Map of query substrings 
	 * to name/score pairs, and serialize the Map into a JSON String.
	 * Assuming input is a List<Pair>, not a file.
	 * @param inputList
	 */
	public Constructor (List<Pair> inputList) {
		index = new HashMap<String, List<Pair>> ();
		for (Pair pair : inputList) {
			String name = pair.name();
			List<String> suffixes = splitStringByUnderscore(name);
			for (String suffix : suffixes) {
				List<String> prefixes = getPrefixList(suffix);
				for (String prefix : prefixes) {
					List<Pair> values = index.get(prefix);
					if (values == null) {
						values = new LinkedList<Pair> ();
						values.add(pair);
						index.put(prefix, values);
					} else {
						values.add(pair);
					}
				}
			}
		}
		Iterator<List<Pair>> values = index.values().iterator();
		while (values.hasNext()) {
			List<Pair> next = values.next();
			Collections.sort(next);
		}
		
		Type hashMapType = new TypeToken<HashMap<String, List<Pair>>> () {}.getType();
		Gson gson = new Gson();
		this.D = gson.toJson(index, hashMapType);
	}
	
	/**
	 *  Splits name into list of suffixes based on occurrences of "_".
	 *  
	 *  Example usage: "quarterly_international_revenue" would return:
	 *  {"quarterly_international_revenue", "international_revenue", "revenue"}
	 * @param name	input string
	 * @return List<String>	list of substrings in name split by "_"
	 */
	public static List<String> splitStringByUnderscore(String name) {
		List<String> result = new LinkedList<String> ();
		result.add(name);
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '_') {
				String substring = name.substring(i+1, name.length());
				result.add(substring);
			}
		}
		return result;
	}
	/**
	 * Returns a list of all possible prefixes of name
	 * @param name
	 * @return List<String>	list of prefixes of name
	 */
	public static List<String> getPrefixList(String name) {
		List<String> result = new LinkedList<String> ();
		for (int i = 1; i <= name.length(); i++) {
			String prefix = name.substring(0, i);
			result.add(prefix);
		}
		return result;
	}
	
	public Serializable getJson () {
		return this.D;
	}
}