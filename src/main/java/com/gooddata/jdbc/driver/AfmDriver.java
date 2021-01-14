package com.gooddata.jdbc.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * GoodData JDBC driver
 */
public class AfmDriver implements java.sql.Driver {

	private final static Logger LOGGER = Logger.getLogger(AfmDriver.class.getName());

	public final static int MAJOR_VERSION = 0;
	public final static int MINOR_VERSION = 5;
	public final static String VERSION = String.format("%x.%x", MAJOR_VERSION, MINOR_VERSION);

	/**
	 * Setups the logging from the ~/.logging config file
	 * @throws IOException if the config file doesn't exist
	 */
	static void setupLogging() throws IOException {
		Logger log = Logger.getGlobal();
		FileInputStream fis =  new FileInputStream(String.format("%s/.logging",
			System.getProperty("user.home")));
		LogManager.getLogManager().readConfiguration(fis);
		log.setUseParentHandlers(false);
		fis.close();
	}

	 static {

		 try {
		 		setupLogging();
	        	LOGGER.info("GooodData JDBC Driver started");
	            DriverManager.registerDriver(new AfmDriver());
	        } catch (IOException | SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }

	/**
	 * Default constructor
	 */
    public AfmDriver() {}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Connection connect(String url, Properties info) throws SQLException {
		if (this.acceptsURL(url)) {
			try {

				return new AfmConnection(url, info);
			} catch (IOException e) {
				throw new SQLException(e);
			}
		}
		else {
			throw new SQLException("Invalid JDBC URL. The driver URL must follow this format" +
					" 'jdbc:gd://<gooddata-server>/gdc/projects/<workspace-id>'.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptsURL(String url) {
		return url.startsWith("jdbc:gd:");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		throw new SQLFeatureNotSupportedException("Driver.getPropertyInfo is not supported yet.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMajorVersion() {
		return MAJOR_VERSION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinorVersion() {
		return MINOR_VERSION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean jdbcCompliant() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Logger getParentLogger() {
		return LOGGER;
	}

}