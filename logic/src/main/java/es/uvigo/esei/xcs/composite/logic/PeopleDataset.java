package es.uvigo.esei.xcs.composite.logic;

import static java.util.Arrays.stream;

import es.uvigo.esei.xcs.composite.domain.Person;

public final class PeopleDataset {
	private PeopleDataset() {}
	
	public static Person[] people() {
		return new Person[] {
		    new Person("John", "Smith", "555-112233"),
		    new Person("Mary", "Doe", "555-223344"),
		    new Person("Foo", "Bar", "555-334455"),
		    new Person("Loren", "Ipsum", "555-445566")
		};
	}
	
	public static Person person(String name, String surname) {
		return stream(people())
			.filter(p -> p.getName().equals(name) && p.getSurname().equals(surname))
			.findAny()
		.orElseThrow(IllegalArgumentException::new);
	}
	
	public static Person personWithPhone(String phone) {
		return stream(people())
			.filter(p -> p.getPhone().equals(phone))
			.findAny()
		.orElseThrow(IllegalArgumentException::new);
	}
	
	public static Person newPerson() {
		return new Person("Anne", "Manne", "555-556677");
	}
	
	public static Person toDelete() {
		return people()[2];
	}
}
