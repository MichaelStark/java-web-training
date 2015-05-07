package by.stark.sample.wicketweb;

import java.io.File;
import java.net.URL;

import org.apache.wicket.util.time.Duration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class WicketJettyRunner {

	public static void main(String[] args) throws Exception {
		new WicketJettyRunner().startServer();
	}

	public static void startServer() throws Exception {
		final int timeout = (int) Duration.ONE_HOUR.getMilliseconds();

		final Server server = new Server();
		final SocketConnector connector = new SocketConnector();

		// Set some timeout options to make debugging easier.
		connector.setMaxIdleTime(timeout);
		connector.setSoLingerTime(-1);
		connector.setPort(8081);
		server.addConnector(connector);

		/*
		 * final Resource keystore = Resource.newClassPathResource("/keystore");
		 * if (keystore != null && keystore.exists()) { // if a keystore for a
		 * SSL certificate is available, start a SSL // connector on port 8443.
		 * // By default, the quickstart comes with a Apache Wicket Quickstart
		 * // Certificate that expires about half way september 2021. Do not //
		 * use this certificate anywhere important as the passwords are //
		 * available in the source.
		 * 
		 * connector.setConfidentialPort(8443);
		 * 
		 * final SslContextFactory factory = new SslContextFactory();
		 * factory.setKeyStoreResource(keystore);
		 * factory.setKeyStorePassword("wicket");
		 * factory.setTrustStoreResource(keystore);
		 * factory.setKeyManagerPassword("wicket"); final SslSocketConnector
		 * sslConnector = new SslSocketConnector(factory);
		 * sslConnector.setMaxIdleTime(timeout); sslConnector.setPort(8443);
		 * sslConnector.setAcceptors(4); server.addConnector(sslConnector);
		 * 
		 * System.out.println(
		 * "SSL access to the quickstart has been enabled on port 8443");
		 * System.out.println(
		 * "You can access the application using SSL on https://localhost:8443"
		 * ); System.out.println(); }
		 */
		final WebAppContext bb = new WebAppContext();
		bb.setServer(server);
		bb.setContextPath("/");
		bb.setWar("src/main/webapp");

		// START JMX SERVER
		// MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		// MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		// server.getContainer().addEventListener(mBeanContainer);
		// mBeanContainer.start();

		server.setHandler(bb);

		// setup JNDI
		final EnvConfiguration envConfiguration = new EnvConfiguration();
		final URL url = new File("src/main/webapp/WEB-INF/jetty-env.xml")
				.toURI().toURL();
		envConfiguration.setJettyEnvXml(url);
		bb.setConfigurations(new Configuration[] { new WebInfConfiguration(),
				envConfiguration, new WebXmlConfiguration() });

		try {
			System.out
					.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
			server.start();
			System.in.read();
			System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
			server.stop();
			server.join();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
