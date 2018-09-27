package es.uvigo.esei.xcs.composite.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.uvigo.esei.xcs.composite.domain.Person;
import es.uvigo.esei.xcs.composite.logic.PeopleFacade;

public class PersonManager {
	public static void main(String[] args) {
		// H2 database
		final String url = "jdbc:h2:./target/composite";
		final String user = "sa";
		final String password = "";
		
		// Derby database
//		final String url = "jdbc:derby:target/composite;create=true";
//		final String user = "";
//		final String password = "";
		
		try (final Connection connection = DriverManager.getConnection(url, user, password)) {
			final PeopleFacade facade = new PeopleFacade(connection);
			
			dropTable(connection);
			createTable(connection);
			
			final Person[] people = {
				new Person("John", "Smith", "555-111222"),
				new Person("John", "Doe", "555-222333"),
				new Person("Mary", "Smith", "555-333444"),
				new Person("Mary", "Doe", "555-444555"),
				new Person("Julian", "Smith", "555-555666"),
				new Person("Julian", "Doe", "555-666777")
			};
			
			for (Person person : people) {
				facade.create(person);
				System.out.println("Created: " + person);
			}
			
			final Person marySmith = facade.getByCompleteName("Mary", "Smith");
			System.out.println("The phone of Mary Smith is: " + marySmith.getPhone());
			
			final Person phoneOwner = facade.getByPhone("555-555666");
			System.out.println("The owner of the phone 555-555666 is "
				+ phoneOwner.getName() + " " + phoneOwner.getSurname());
			
			System.out.println("Deleting: " + people[3]);
			facade.delete(people[3]);
			
			System.out.println("Current list of people:");
			facade.list().forEach(System.out::println);
		} catch (SQLException e) {
			System.out.println("Error execution application: " + e);
		}
	}
	
	public static boolean existsTable(Connection connection) {
		try (final ResultSet result = connection.getMetaData().getTables(null, null, PeopleFacade.TABLE_NAME, null)) {
			return result.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void createTable(Connection connection) {
		if (!existsTable(connection)) {
			try (final Statement statement = connection.createStatement()) {
				statement.executeUpdate(
					"CREATE TABLE " + PeopleFacade.TABLE_NAME + "(" +
						"name VARCHAR(255)," +
						"surname VARCHAR(255), " +
						"phone VARCHAR(20)" +
					")"
				);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static void dropTable(Connection connection) {
		if (existsTable(connection)) {
			try (final Statement statement = connection.createStatement()) {
				statement.executeUpdate("DROP TABLE " + PeopleFacade.TABLE_NAME);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
