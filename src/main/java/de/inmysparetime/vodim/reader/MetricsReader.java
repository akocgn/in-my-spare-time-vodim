package de.inmysparetime.vodim.reader;

import de.inmysparetime.vodim.Metrics;

public interface MetricsReader {
	Metrics readMetrics() throws MetricsReaderException;
}
