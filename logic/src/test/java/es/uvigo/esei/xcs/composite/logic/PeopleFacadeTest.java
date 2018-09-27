package es.uvigo.esei.xcs.composite.logic;

import static es.uvigo.esei.xcs.composite.logic.PeopleDataset.newPerson;
import static es.uvigo.esei.xcs.composite.logic.PeopleDataset.people;
import static es.uvigo.esei.xcs.composite.logic.PeopleDataset.person;
import static es.uvigo.esei.xcs.composite.logic.PeopleDataset.personWithPhone;
import static es.uvigo.esei.xcs.composite.logic.PeopleDataset.toDelete;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.JdbcBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.xcs.composite.domain.Person;

public class PeopleFacadeTest extends JdbcBasedDBTestCase {
	private PeopleFacade facade;
	
	@Override
	protected String getConnectionUrl() {
		return "jdbc:derby:target/composite;create=true";
	}

	@Override
	protected String getDriverClass() {
		return "org.apache.derby.jdbc.EmbeddedDriver";
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder()
			.build(this.getClass().getResource("dataset.xml"));
	}
	
	@Before
	@Override
	protected void setUp() throws Exception {
		this.facade = new PeopleFacade(this.getConnection().getConnection());
		this.createTable();
		
		super.setUp();
	}
	
	@After
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		this.dropTable();
	}

	@Test
	public void testCreate() {
		this.facade.create(newPerson());
		
		assertEqualsToDataset("dataset-create.xml");
	}

	@Test
	public void testDelete() {
		this.facade.delete(toDelete());
		
		assertEqualsToDataset("dataset-delete.xml");
	}

	@Test
	public void testList() {
		final List<Person> people = this.facade.list();
		
		assertThat(people, is(containsInAnyOrder(people())));
		assertEqualsToDataset("dataset.xml");
	}

	@Test
	public void testGetByCompleteName() {
		final String name = "John";
		final String surname = "Smith";
		
		final Person person = this.facade.getByCompleteName(name, surname);
		
		assertThat(person, is(person(name, surname)));
		assertEqualsToDataset("dataset.xml");
	}

	@Test
	public void testGetByPhone() {
		final String phone = "555-112233";
		
		final Person person = this.facade.getByPhone(phone);
		
		assertThat(person, is(personWithPhone(phone)));
		assertEqualsToDataset("dataset.xml");
	}
	
	private void assertEqualsToDataset(String datasetXml) {
		try {
	        // Fetch database data after executing your code
	        final IDataSet databaseDataSet = getConnection().createDataSet();
	        final ITable actualTable = databaseDataSet.getTable(PeopleFacade.TABLE_NAME);
	
	        // Load expected data from an XML dataset
	        final IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
	        	.build(this.getClass().getResource(datasetXml));
	        final ITable expectedTable = expectedDataSet.getTable(PeopleFacade.TABLE_NAME);
	
	        // Assert actual database table match expected table
	        Assertion.assertEquals(expectedTable, actualTable);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean existsTable() throws Exception {
		final Connection connection = this.getConnection().getConnection();
		
		try (final ResultSet result = connection.getMetaData().getTables(null, null, PeopleFacade.TABLE_NAME, null)) {
			return result.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void createTable() throws Exception {
		final Connection connection = this.getConnection().getConnection();
		
		if (!existsTable()) {
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
	
	public void dropTable() throws Exception {
		final Connection connection = this.getConnection().getConnection();
		
		if (existsTable()) {
			try (final Statement statement = connection.createStatement()) {
				statement.executeUpdate("DROP TABLE " + PeopleFacade.TABLE_NAME);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
