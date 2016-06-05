package de.inmysparetime.vodim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MetricsHistory {
	private static final String TOKEN_VALUE = "value";

	final ObservedEntries<Number> gaugeHistory = new ObservedEntries<>();

	final ObservedEntries<Number> meterHistory = new ObservedEntries<>();

	public void initialize(final Metrics metrics) {
		// Create an observable value for each metrics.
		metrics.getGauges().keySet().forEach(k->gaugeHistory.register(k));
		metrics.getMeters().keySet().forEach(k->meterHistory.register(k));
	}
	
	public ObservedEntries<Number> getGaugeHistory() {
		return gaugeHistory;
	}

	public ObservedEntries<Number> getMeterHistory() {
		return meterHistory;
	}

	public void update (final Metrics metrics) {
		final String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		metrics.getGauges().forEach((k,v)->updateGauge(timestamp, k,v));
		metrics.getMeters().forEach((k,v)->updateMeters(timestamp, k,v));
	}

	private void updateMeters(String timestamp, String label, Map<String, Object> valueMap) {
		// TODO Auto-generated method stub
		// Not yet implemented.
		// Relevant ist hier erstmal nur die m1_rate, der Schnitt innerhalb der Minute. Den
		// Rest sieht man ja im Diagramm.  
//	    "telegram.scheduled.udp(3).timestamp" : {
//	      "count" : 1,
//	      "m15_rate" : 0.1934432200964012,
//	      "m1_rate" : 0.12130613194252672,
//	      "m5_rate" : 0.1809674836071919,
//	      "mean_rate" : 0.026800567730341087,
//	      "units" : "events/second"
//	    }
		if (!meterHistory.isRegistered(label)) {
			System.out.println("Got unexpected value '" + label + "'.");
			return;
		}
		final Object value = valueMap.get("m1_rate");
		if (value != null) {
			if (value instanceof Number) {
				meterHistory.add(timestamp, label, (Number) value);
			}
		}
	}

	private void updateGauge(final String timestamp, final String label, Map<String, Object> valueMap) {
		if (!gaugeHistory.isRegistered(label)) {
			System.out.println("Got unexpected value '" + label + "'.");
			return;
		}
		final Object value = valueMap.get(TOKEN_VALUE);
		if (value != null) {
			if (value instanceof Number) {
				gaugeHistory.add(timestamp, label, (Number) value);
			}
		}
	}
}
