package model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// TODO
public class Actor  implements Comparable<Actor>{
	private String name;
	private LocalDate birth;
	private LocalDate death;
	private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public Actor(String name){
		this.name = name;
	}
	
	public Actor(String name, LocalDate birth) {
		this(name);
		this.birth = birth;
	}
	
	public Actor(String name, LocalDate birth, LocalDate death) {
		this(name,birth);
		this.death = death;
	}
	

	public String getName() {
		// TODO
		String[] split = this.name.split(" ");
		StringBuilder strB = new StringBuilder();

		for(int i = split.length-1; i >= 0;i--) {
			if (i == 0) {
				strB.append(split[i]);
			} else {
				strB.append(split[i] + " ");
			}
		}
		String splitName = strB.toString();

		return splitName;
	}

	public LocalDate getBirth() {
		return birth;
	}
	
	public LocalDate getDeath() {
		return death;
	}
	
	public String toString(){
		if (this.birth == null) { // birth unknown
			return this.name;
		}
		else if (this.death != null) { // person already died
			return this.name + " :  *" + dtf.format(this.birth) + ", \u271D " + dtf.format(this.death);
		} else { // person is still alive	
			// TODO
				Period age = Period.between(getBirth(),LocalDate.now());

			return this.name + " (*" + dtf.format(this.birth) + ", age: " + age.getYears() + ')';
		}
	}


	// TODO


	@Override
	public boolean equals(Object o) {
		if(o instanceof Actor){
			return this.name.equals(((Actor) o).name);
		}
		return false;
	}


	/**
	 * Wenn Nachname stimmt
	 * 		return 0
	 * Wenn Nachnamme + Vorname
	 * 		return 1
	 * 	else
	 * 		return -1
	 *
	 */
	@Override
	public int compareTo(Actor actor2) {
		String name1[] = getName().split(" ");
		String name2[] = actor2.getName().split(" ");

		//Nachname
		if(name1[0].equals(name2[0])){
			//Mittel/Vorname
			if(name1[1].equals(name2[1])){
				//Vorname
				if(name1.length == 3 || name2.length == 3){
					if(name1[2].equals(name2[2])){
						return 1;
					}
				}else
					return 1;
			}
			return 0;
		}
		else {
			return -1;
		}
	}



}