package pdemanget.filedb.bean;

import java.time.LocalDate;

import pdemanget.filedb.Dic;

/**
 * @author fil
 */
public class Person implements Dic{
	public String lastName;
	public String firstName;
	public LocalDate birthDate;
	public float size;
	public String phone;
	public String email;
	
	public Person() {
		
	}
	public Person(String lastName, String firstName, LocalDate birthDate, float size) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.birthDate = birthDate;
		this.size = size;
	}

	@Override
	public long getId() {

		return 0;
	}
	@Override
	public String toString() {
		return "Person [lastName=" + lastName + ", firstName=" + firstName + ", birthDate=" + birthDate + ", size="
				+ size + ", phone=" + phone + ", email=" + email + "]";
	}
	
	
	

}
