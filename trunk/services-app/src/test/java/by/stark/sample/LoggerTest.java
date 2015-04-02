package by.stark.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTest.class);

	public static void main(String[] args) {
		LOGGER.error("Test ERROR message.");
		LOGGER.warn("Test WARN message.");
		LOGGER.info("Test INFO message.");
		LOGGER.debug("Test DEBUG message.");
	}

}
