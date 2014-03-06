Algorithm:
Main idea:  For every input name, I precalculate all possible query strings that would match that word (according to the rules in the spec) and store them as keys in a Map.  The associated values in this Map are sorted lists of name/score pairs that match the given key.  By precalcuting the relevant query strings and presorting the associated list of name/score pairs in the Constructor Program, I’m able to ensure a sub-linear (constant) query time.  The tradeoff is increased memory usage and a longer preprocessing time.

Runtime:
Let n=number of name/score pairs in the input list.
Let m=the number of bytes in the largest word
Let k=the largest number of underscores in any given word
For each input word, we create a key in our Map for all O(m) possible prefixes.  We do the same for each suffix after the O(k) underscores.  With constant time inserts, creating the Map takes O(nmk) time.  Next we sort each list of name/score pairs for every key.  This runtime can vary depending on the distribution of words to prefixes, but a (not very tight) upper bound would be O(n^2logn).  Finally, running a query takes O(1) after the server has deserialized the JSON, since Map.put() is O(1), and the query’s list of matching names is already sorted (so takes O(1) to return the first 10 names).

Memory usage:
Memory usage was the limiting factor in my tests.  Before testing, I did the following calculation to see if my algorithm would be feasible.  I initially had an error, which made the algorithm seem more feasible with 1MM entries.  
Let’s assume we have 4GiB memory = 2^32 bytes as on my laptop.  If n is the number of entries, then n= 1MM ~=2^20 entries.  First let’s consider the number of keys in the Map.  Let m and k be defined as above.  Then we will have on the order of O(nmk) keys taking up O(nm^2k) space (note: this is where I had an error - my m^2 term was initially m, making the calculation seem a little more optimistic at first).  So we would have to have m^2k < 2^12 for the number of keys to fit in memory, which doesn’t even account for the memory required by lists of name/score pairs associated with that key.  At this point it looks like 4GB will not be enough for 1MM entries.
	Indeed in my tests, this algorithm was able to complete queries with up to 400,000 entries (after upping the memory allocated to the JVM to 4096MB), but ran out of memory beyond that.  

Other notes:
-One design decision was when to sort the list of names (by score) matching a query string.  To achieve a sublinear query time, the only possibility was to sort these names during Construction.  Otherwise, there could be cases where all n names share a prefix, and finding the top 10 names by score would involve sorting all n names.  That would make the runtime of query O(nlogn) in the worst case, which doesn’t fit the sublinear requirement.
-Not quite sure if I fulfilled the serializable requirement - I haven’t had much experience with serialization, so read about it and some libraries that provide serialization functionality.  The gson lib seemed to be well regarded and easiest to use, so I went with that after looking at org.json 
