package de.inmysparetime.vodim.reader;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.inmysparetime.vodim.Metrics;

public class MetricsURLReader implements MetricsReader {
	private final ObjectMapper mapper = new ObjectMapper(); 
	private final URL url;
	public MetricsURLReader(final URL url) {
		this.url = url;
	}
	@Override
	public Metrics readMetrics() throws MetricsReaderException {
		try {
			return mapper.readValue(url, Metrics.class);
		} catch (IOException e) {
			throw new MetricsReaderException("Cannot read metrics from url '" + url +"'.", e);
		}
	}

}
