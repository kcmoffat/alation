package alation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.io.Serializable;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Constructor {
	private Serializable D;
	private Map<String, PriorityQueue<Pair>> index;
	
	/**
	 * Allocate a Constructor, create a Map of query substrings 
	 * to name/score pairs, and serialize the Map into a JSON String.
	 * Assuming input is a List<Pair>, not a file, and is non-null
	 * @param inputList	
	 */
	public Constructor (List<Pair> inputList) {
		index = new HashMap<String, PriorityQueue<Pair>> ();
		for (Pair pair : inputList) {
			String name = pair.name();
			List<String> suffixes = splitStringByUnderscore(name);
			for (String suffix : suffixes) {
				List<String> prefixes = getPrefixList(suffix);
				for (String prefix : prefixes) {
					PriorityQueue<Pair> values = index.get(prefix);
					if (values == null) {
						values = new PriorityQueue<Pair> ();
						values.add(pair);
						index.put(prefix, values);
					} else if (values.size() < 10){
						values.add(pair);
					} else {
						Pair minValue = values.peek();
						if (pair.compareTo(minValue) > 0) {
							values.remove();
							values.add(pair);
						}
					}
				}
			}
		}
		
		Type hashMapType = new TypeToken<HashMap<String, PriorityQueue<Pair>>> () {}.getType();
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