package de.inmysparetime.vodim;

import java.util.Optional;

import de.inmysparetime.vodim.reader.MetricsReader;
import de.inmysparetime.vodim.reader.MetricsReaderException;

public class Processor {
	
	private final MetricsReader reader;
	private final MetricsHistory history;
	public Processor(final MetricsReader reader, final MetricsHistory history) {
		this.reader = reader;
		this.history = history;
	}
	
	
	public ObservedEntries<Number> getGaugeHistory() {
		return history.getGaugeHistory();
	}
	public boolean hasGaugeHistory() {
		return !history.getGaugeHistory().getObservedMap().isEmpty();
	}

	public ObservedEntries<Number> getMeterHistory() {
		return history.getMeterHistory();
	}
	public boolean hasMeterHistory() {
		return !history.getMeterHistory().getObservedMap().isEmpty();
	}
	Optional<Metrics> initialize() {
		try {
			final Metrics firstMetrics = reader.readMetrics();
			history.initialize(firstMetrics);
			history.update(firstMetrics);
			return Optional.of(firstMetrics);
		} catch (final MetricsReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Optional.empty();
		}
	}

}
