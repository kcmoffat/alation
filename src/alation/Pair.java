package alation;

/**
 * Wrapper class for name/score pairs
 *
 */
public class Pair implements Comparable<Pair>{
	private String name;
	private int score;
	
	public Pair (String name, int score) {
		this.name = name.toLowerCase();
		this.score = score;
	}
	
	public String name () {
		return name;
	}
	
	public int score () {
		return score;
	}
	
	public int compareTo(Pair p) {
		return p.score() - this.score(); // reversed to sort decreasing
	}
	
	public String toString () {
		return name + ": " + score;
	}
}
