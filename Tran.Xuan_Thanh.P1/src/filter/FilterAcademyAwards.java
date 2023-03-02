package filter;

import java.lang.reflect.Array;
import java.time.Month;
import java.util.*;

import io.TextParser;
import model.Actor;
import model.Nominee;

public class FilterAcademyAwards {
	private TreeSet<Actor> actors = new TreeSet<> ();
	private TreeSet<Nominee> nominees = new TreeSet<> ();
	
	public FilterAcademyAwards(){
		this.init();
	}
	
	/*  read and parse text from data files and 
	 *   create instances of the classes Actor and
	 *   Nominee that are stored in the respective
	 *   TreeSet instances 
	 *   */
	public void init(){
		TextParser.parseData(TextParser.ACTORS, actors);
		TextParser.parseData(TextParser.AWARDS, nominees);
	}
	
	/* ********************************************* */
	
	/* *** Aufgabenteil (2a) *** */
	
	private Actor getActor(String aName){
		Iterator<Actor> it = actors.iterator();
		Actor name2 = new Actor(aName);

		while(it.hasNext()){
			Actor tmp = it.next();
			if(aName.equals(tmp.getName()))
				return tmp;
		}
		return null;
	}

	public Vector<String> getActorNames(){
		// TODO
		Vector<String> names = new Vector<>();

		Iterator<Actor> it = actors.iterator();
		while(it.hasNext()){
			Actor tmp = it.next();
			names.add(tmp.getName());
		}

		return names;
	}
	
	public Vector<Integer> getYears(){
		// TODO
		Vector<Integer> year = new Vector<>();

		Iterator<Nominee> it = nominees.iterator();
		while(it.hasNext()){
			Nominee tmp = it.next();
			boolean dublicate = false;

			for(int i = 0; i < year.size(); i++){
				if(year.get(i) == tmp.getYear())
					dublicate = true;
			}
			if(!dublicate){
				year.add(tmp.getYear());
			}
		}
		return year;
	}

	/* *** Aufgabenteil (2b) *** */


	public String getAwardsActor(String aName){
		// TODO
		StringBuffer awards = new StringBuffer();
		StringBuffer nominations = new StringBuffer();
		boolean hasNominees = false;
		boolean hasAwards = false;
		Actor actor;

		if(getActor(aName) == null){
			actor = new Actor(aName);
		}
		else
			actor = getActor(aName);

		awards.append("Awards:\n ");
		nominations.append("Nominations:\n ");

		Iterator<Nominee> it = nominees.iterator();
		while(it.hasNext()) {
			Nominee tmp = it.next();

			if (tmp.getActor().equals(actor)) {
				if (tmp.isWon()) {
					awards.append(tmp.getYear() + ": " + tmp.getMovie() + " (" + tmp.getCharacter() + ", Category: " + tmp.getCategory() + ")\n ");
					hasAwards = true;
				} else {
					nominations.append(tmp.getYear() + ": " + tmp.getMovie() + " (" + tmp.getCharacter() + ", Category: " + tmp.getCategory() + ")\n ");
					hasNominees = true;
				}
			}
		}
		if(!hasAwards){
			awards.append("---\n");
		}
		if(!hasNominees){
			nominations.append("---\n");
		}
		return actor.toString() + "\n " + awards + "\n " + nominations;    // Dummy return
	}
	
	
	/* *** Aufgabenteil (2c) *** */
	
	// TODO
	private class FilmCount implements Comparable<FilmCount>{
		String film;
		int nominations = 1;

		public FilmCount(String film){
			this.film = film;
		}

		public FilmCount(String film, int nominations){
			this.film = film;
			this.nominations = nominations;
		}

		public String getFilm() {
			return film;
		}

		public int getNominations() {
			return nominations;
		}

		public void setNominations(int nominations) {
			this.nominations = nominations;
		}

		public String toString(){
			return getFilm() + " : " + getNominations();
		}

		@Override
		public boolean equals(Object o){
			if(o instanceof FilmCount){
				return getFilm().equals(((FilmCount) o).film);
			}
			return false;
		}

		/**
		 * film == o.film
		 * 		return 0
		 * nominations == o.noimations
		 * 		return 1
		 * else
		 * 		return -1
		 *
		 */
		@Override
		public int compareTo(FilmCount o) {
			if(getFilm().equals(o.getFilm())){
				if(getNominations() == o.getNominations()){
					return 1;
				}
				else
					return 0;
			}
			else
				return -1;
		}
	}

	public String getTopThreeMovies(int y){
		// TODO

		HashMap<String,FilmCount> counter = new HashMap<>();

		Iterator<Nominee> it = nominees.iterator();


		while(it.hasNext()){
			Nominee tmp = it.next();
			boolean hasFilm = false;
			Iterator itCounter = counter.entrySet().iterator();

			if(tmp.getYear() == y) {
				while (itCounter.hasNext()) {
					Map.Entry pair = (Map.Entry) itCounter.next();

					if(tmp.getMovie().equals(pair.getKey())){
						hasFilm = true;
					}
				}

				//Film noch nicht vorhanden
				//erstellt einen neuen Film
				if(!hasFilm){
					FilmCount film = new FilmCount(tmp.getMovie());
					counter.put(tmp.getMovie(),film);
				}
				//erhöht die Zählervariable

				//value holen
				//Kontruktor erhöhen
				else{
					FilmCount valueGetter = new FilmCount(tmp.getMovie(),
							counter.get(tmp.getMovie()).getNominations() + 1);
					counter.replace(tmp.getMovie(),valueGetter);

				}
			}
		}

		FilmCount[] valueOfFilm = counter.values().toArray(new FilmCount[0]);
		Arrays.sort(valueOfFilm);


		StringBuffer highestValues = new StringBuffer();
		//höchsten drei Elemente

	/*	for(int i = valueOfFilm.length-1; i >= 0; i--){
			highestValues.append(valueOfFilm[i] + "\n");
		}

	 */

		highestValues.append(valueOfFilm[valueOfFilm.length-1] + "\n");
		highestValues.append(valueOfFilm[valueOfFilm.length-2] + "\n");
		highestValues.append(valueOfFilm[valueOfFilm.length-3] + "\n");
		for(int i = valueOfFilm.length-4; i>=0; i--){
			if(valueOfFilm[valueOfFilm.length-3].nominations == valueOfFilm[i].nominations){
				highestValues.append(valueOfFilm[i] + "\n");
			}
			else{
				break;
			}
		}

		return highestValues.toString();
	}
	
	

	/* *** Aufgabenteil (2d) *** */

	
	public String filterActors(Month m, int d){
		// TODO

		Iterator<Actor> it = actors.iterator();
		ArrayList<Actor> list = new ArrayList<>();

		while(it.hasNext()){
			Actor actor = it.next();

			if(d != 0){
				if(actor.getBirth().getMonth().equals(m) && actor.getBirth().getDayOfMonth() == d){
					if(!list.contains(actor))
						list.add(actor);
				}
			}
			else{
				if(actor.getBirth().getMonth().equals(m)){
					if(!list.contains(actor))
						list.add(actor);
				}
			}
		}
		Comparator<Actor> aClass = new Comparator<Actor>() {
			@Override
			public int compare(Actor a1, Actor a2) {

				return a1.getBirth().getYear() - a2.getBirth().getYear();
			}
		};
		java.util.Collections.sort(list);
		StringBuffer stringBuffer = new StringBuffer();

		for (Actor actor : list) {
			stringBuffer.append(actor.toString() + "\n");
		}


		return stringBuffer.toString();
	}


	
	
	public static void main(String[] args){
		FilterAcademyAwards faw = new FilterAcademyAwards();
		//System.out.println(faw.getWinners(Category.ACTRESS_LR));
		//System.out.println(faw.getTopThreeMovies(2010));
		System.out.println(faw.filterActors(Month.MARCH, 20));

	}


}
