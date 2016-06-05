package de.inmysparetime.vodim.reader;

public class MetricsReaderException extends Exception {

	public MetricsReaderException(final String text, final Exception cause) {
		super(text, cause);
	}

	public MetricsReaderException(final String text) {
		super(text);
	}

}
