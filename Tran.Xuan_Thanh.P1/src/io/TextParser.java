package io;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import general.Parameters;
import general.ParseActorException;
import general.ParseNomineeExceotion;
import model.Actor;
import model.Category;
import model.Nominee;

import static model.Category.*;

public class TextParser {
	public static final int ACTORS = 1;   // Flag for file with actors
	public static final int AWARDS = 2;   // Flag for file with nominees

	/* a map to store objects of class Actor, using his or her name
	 * as the key 
	 * */
	private static HashMap<String, Actor> aMap = new HashMap<>();


	private static void parseActorsLine(String line, TreeSet<Actor> coll){
		// TODO
		String[] attributes = line.split(";");
		LocalDate birth, death;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String[] birthDate = attributes[1].split("\\.");

		String name = attributes[0].replaceAll("[ \\t]+$+","");
		birth = LocalDate.of(Integer.parseInt(birthDate[2]),Integer.parseInt(birthDate[1]),Integer.parseInt(birthDate[0]));
		//Wenn Todestag vorhanden
		if (attributes.length == 3) {
			String[] deathDate = attributes[2].split("\\.");
			death = LocalDate.of(Integer.parseInt(deathDate[2]), Integer.parseInt(deathDate[1]), Integer.parseInt(deathDate[0]));
			Actor actor = new Actor(name, birth, death);

			//Einfügen in Treeset und HashMap
			coll.add(actor);
			aMap.put(name, actor);
		}
		//Exception Handling
		else if(attributes.length > 3){
			try {
				throw new ParseActorException("");
			} catch (ParseActorException e) {
				e.printStackTrace();
			}
		}
		else {
			//Einfügen in Treeset und HashMap
			Actor actor = new Actor(name, birth);
			coll.add(actor);
			aMap.put(name, actor);
		}
	}
	
	
	private static void parseNomineesLine(String line, TreeSet<Nominee> coll){
		// TODO
		String[] attributes = line.split(",");

		int year;
		Category category;
		Actor actor;
		//5 Elemente
		//[0] Jahr
		//[1] Kategorie
		//[2] Schauspieler
		//[3] Film + Rolle
		//[4] Ja oder Nein
		if(attributes.length == 5){
			year = Integer.parseInt(attributes[0]);
			category = parseCategory(attributes[1]);
			String[] filmCharacter;
			actor = new Actor(attributes[2]);
			boolean nominate = false, hasActor = false;

			filmCharacter = parseFilmCharacter(attributes[3]);
			if(attributes[4].equals("YES")){
				nominate = true;
			}

			Iterator it = aMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				//Wenn Actor bereits vorhanden
				if (pair.getKey().equals(attributes[2])) {
					Nominee nominee = new Nominee(category,year,filmCharacter[0],filmCharacter[1],nominate, (Actor) pair.getValue());
					coll.add(nominee);
					hasActor = true;

				}
			}
			//Wenn Actor noch nicht vorhanden
			if(!hasActor) {
				aMap.put(attributes[2], actor);
				Nominee nominee = new Nominee(category, year, filmCharacter[0], filmCharacter[1], nominate, actor);
				coll.add(nominee);
			}
			else{
				actor = aMap.get(attributes[2]);
				Nominee nominee = new Nominee(category, year, filmCharacter[0], filmCharacter[1], nominate, actor);
				coll.add(nominee);
			}
		}
		//Exception Handling
		else if(attributes.length > 5){
			try {
				throw new ParseNomineeExceotion("");
			} catch (ParseNomineeExceotion e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * this method parse film and character into a Sting Array
	 * @param FilmCharac Film + Character/Role
	 * @return [0] Film
	 * 		   [1] Character/Role
	 */
	public static String[] parseFilmCharacter(String FilmCharac){
		String[] tmp = FilmCharac.split("\\{");
		String[] character = tmp[1].split("'");
		tmp[1] = character[1];

		return tmp;
	}


	/**
	 * A method to parse a String to an Category Object
	 * @returns a category object
	 */
	public static Category parseCategory(String category){
		Category tmp;
		switch (category){
			case "Actress -- Leading Role":
				tmp = ACTRESS_LR;
				return tmp;
			case "Actor -- Leading Role":
				tmp = ACTOR_LR;
				return tmp;
			case "Actress -- Supporting Role":
				tmp = ACTRESS_SR;
				return tmp;
			case "Actor -- Supporting Role":
				tmp = ACTOR_SR;
				return tmp;
			case "Best Picture":
				tmp = BEST_PICTURE;
				return tmp;
			case "Directing":
				tmp = DIRECTING;
				return tmp;
			default:
				return null;
		}
	}


	@SuppressWarnings("unchecked")
	public static <T>  void parseData (int objectType, TreeSet<T> set){
		StringBuffer fileContent;
		String[] lines;            // array to store lines of a text
		switch (objectType) {
		case ACTORS:
			fileContent = TextReader.getText(Parameters.actorsFile);
			// TODO
			lines = fileContent.toString().split("\n");
			for (String line : lines) {
				parseActorsLine(line, (TreeSet<Actor>) set);
			}
			break;

		case AWARDS:
			fileContent = TextReader.getText(Parameters.nomineesFile);
			// TODO
			lines = fileContent.toString().split("\n");
			for(String line : lines){
				parseNomineesLine(line, (TreeSet<Nominee>) set);
			}
			break;
		default:
			break;
		}
	
	}
	
	public static void main(String[] args) {
		// Test
		TextParser.parseData(ACTORS, new TreeSet<Actor>());
		TextParser.parseData(AWARDS, new TreeSet<Nominee>());
	}

}
