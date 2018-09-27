package es.uvigo.esei.xcs.composite.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.esei.xcs.composite.domain.Person;

public class PeopleFacade {
	public final static String TABLE_NAME = "PEOPLE";
	
	private Connection connection;

	public PeopleFacade(Connection connection) {
		this.connection = connection;
	}
	
	public void create(Person person) {
		final String query = "INSERT INTO " + TABLE_NAME + " (name, surname, phone) " +
			"VALUES (?, ?, ?)";
		try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
			statement.setString(1, person.getName());
			statement.setString(2, person.getSurname());
			statement.setString(3, person.getPhone());
			
			if (statement.executeUpdate() != 1) {
				throw new RuntimeException("Unexpected number of modified rows");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void delete(Person person) {
		final String query = "DELETE FROM " + TABLE_NAME +
			" WHERE name = ? AND surname = ?";
		try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
			statement.setString(1, person.getName());
			statement.setString(2, person.getSurname());
			
			if (statement.executeUpdate() != 1) {
				throw new RuntimeException("Unexpected number of modified rows");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Person> list() {
		final String query = "SELECT * FROM " + TABLE_NAME;
		try (final Statement statement = this.connection.createStatement()) {
			try (final ResultSet result = statement.executeQuery(query)) {
				final List<Person> people = new LinkedList<>();
				
				while (result.next()) {
					people.add(new Person(
						result.getString(1),
						result.getString(2),
						result.getString(3)
					));
				}
				
				return people;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Person getByCompleteName(String name, String surname) {
		final String query = "SELECT * FROM " + TABLE_NAME +
			" WHERE name = ? AND surname = ?";
		try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
			statement.setString(1, name);
			statement.setString(2, surname);
			
			try (final ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return new Person(
						result.getString(1),
						result.getString(2),
						result.getString(3)
					);
				} else {
					throw new IllegalArgumentException("Person not found");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Person getByPhone(String phone) {
		final String query = "SELECT * FROM " + TABLE_NAME +
			" WHERE phone = ?";
		try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
			statement.setString(1, phone);
			
			try (final ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return new Person(
						result.getString(1),
						result.getString(2),
						result.getString(3)
					);
				} else {
					throw new IllegalArgumentException("Phone not found");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
