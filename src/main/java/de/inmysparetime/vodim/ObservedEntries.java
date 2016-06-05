package de.inmysparetime.vodim;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.chart.XYChart;

public class ObservedEntries<OBSERVED> {
	private final Map<String, XYChart.Series<String, OBSERVED>> observedMap = new HashMap<>();

	public boolean isRegistered(final String key) {
		return observedMap.containsKey(key);
	}

	public void add(final String timestamp, final String key, final OBSERVED observed) {
		if (observed == null) {
			throw new IllegalArgumentException("Called add with a null observed value.");
		}
		final XYChart.Series<String, OBSERVED> list = observedMap.get(key);
		if (list != null) {
			list.getData().add(new XYChart.Data<String, OBSERVED>(timestamp, observed));
		}
	}

	public XYChart.Series<String, OBSERVED> register(final String key) {
		final XYChart.Series<String, OBSERVED> observableListWrapper = new XYChart.Series<String, OBSERVED>();
		final int idx = key.lastIndexOf('.');
		observableListWrapper.setName(idx>-1 ? key.substring(idx) : key);
		this.observedMap.put(key, observableListWrapper);
		return observableListWrapper;
	}

	public Map<String, XYChart.Series<String, OBSERVED>> getObservedMap() {
		return observedMap;
	}
	
	
}
