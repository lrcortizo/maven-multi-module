package es.uvigo.esei.xcs.composite.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class PersonTest {
	@Test
	public void testPersonStringStringString() {
		final String name = "John";
		final String surname = "Smith";
		final String phone = "555-112233";
		
		final Person person = new Person(name, surname, phone);
		
		assertThat(person.getName(), is(equalTo(name)));
		assertThat(person.getSurname(), is(equalTo(surname)));
		assertThat(person.getPhone(), is(equalTo(phone)));
	}
}
