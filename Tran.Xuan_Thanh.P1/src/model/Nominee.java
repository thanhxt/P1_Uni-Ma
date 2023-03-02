package model;

// TODO
public class Nominee implements Comparable<Nominee>{
	private Category category;
	private int year;
	private String movie;
	private String character;
	private boolean won;
	private Actor actor;

	
	public Nominee(Category c, int y, String movie, String character, boolean wonP, Actor a){
		this.category = c;
		this.year = y;
		this.movie = movie;
		this.character = character;
		this.won = wonP;
		this.actor = a;
	}


	public Category getCategory() {
		return category;
	}


	public int getYear() {
		return year;
	}


	public String getMovie() {
		return movie;
	}


	public String getCharacter() {
		return character;
	}


	public boolean isWon() {
		return won;
	}


	public Actor getActor() {
		return actor;
	}
	
	// TODO

	@Override
	public boolean equals(Object o){
		if(o instanceof Nominee){
			return this.actor.equals(o);
		}
		return false;
	}

	/**
	 * Wenn Film Stimmt
	 * 		return -1
	 * Wenn Film + Schauspieler
	 * 		return 1
	 * else
	 * 		return 0
	 *
	 */
	@Override
	public int compareTo(Nominee nominee2) {
		if(getMovie().equals(nominee2.getMovie())){
			if(getActor().equals(nominee2.getActor())){
				return 1;
			}else
				return 0;
		}
		return -1;
	}
}
