package i5.las2peer.services;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import i5.las2peer.api.p2p.ServiceNameVersion;
import i5.las2peer.connectors.webConnector.WebConnector;
import i5.las2peer.p2p.LocalNode;
import i5.las2peer.p2p.LocalNodeManager;
import i5.las2peer.security.ServiceAgentImpl;
import i5.las2peer.security.UserAgentImpl;
import i5.las2peer.services.databaseService.DatabaseService;
import i5.las2peer.testing.MockAgentFactory;

/**
 * Example Test Class demonstrating a basic JUnit test structure.
 *
 */
public class DatabaseServiceTest {

	private static LocalNode node;
	private static WebConnector connector;
	private static ByteArrayOutputStream logStream;

	private static UserAgentImpl testAgent;
	private static final String testPass = "adamspass";

	// during testing, the specified service version does not matter
	private static final ServiceNameVersion testExampleService = new ServiceNameVersion(
			DatabaseService.class.getCanonicalName(), "1.0");

	private static final String mainPath = "example/";

	/**
	 * Called before the tests start.
	 * 
	 * Sets up the node and initializes connector and users that can be used throughout the tests.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void startServer() throws Exception {
		// start node
		node = new LocalNodeManager().newNode();
		testAgent = MockAgentFactory.getAdam();
		testAgent.unlock(testPass); // agent must be unlocked in order to be stored
		node.storeAgent(testAgent);
		node.launch();

		ServiceAgentImpl testService = ServiceAgentImpl.createServiceAgent(testExampleService, "a pass");
		testService.unlock("a pass");

		node.registerReceiver(testService);

		// start connector
		logStream = new ByteArrayOutputStream();

		connector = new WebConnector(true, 0, false, 0); // port: 0 => use system defined port
		connector.setLogStream(new PrintStream(logStream));
		connector.start(node);
		testAgent = MockAgentFactory.getAdam(); // get a locked agent
	}

	/**
	 * Called after the tests have finished. Shuts down the server and prints out the connector log file for reference.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void shutDownServer() throws Exception {
		connector.stop();
		node.shutDown();

		connector = null;
		node = null;

		System.out.println("Connector-Log:");
		System.out.println("--------------");

		System.out.println(logStream.toString());
	}

	@Test
	public void asdf() {

	}

}
