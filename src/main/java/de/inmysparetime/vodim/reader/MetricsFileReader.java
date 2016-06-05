package de.inmysparetime.vodim.reader;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.inmysparetime.vodim.Metrics;

public class MetricsFileReader implements MetricsReader {
	private final ObjectMapper mapper = new ObjectMapper(); 
	private final File file;
	public MetricsFileReader(final File file) {
		this.file = file;
	}
	@Override
	public Metrics readMetrics() throws MetricsReaderException {
		try {
			return mapper.readValue(file, Metrics.class);
		} catch (IOException e) {
			throw new MetricsReaderException("Cannot read metrics from file '" + file.getAbsolutePath() +"'.", e);
		}
	}

}
