package de.inmysparetime.vodim;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
	private String version;

	private Map<String,Map<String,Object>> gauges = new HashMap<>();
	private Map<String,Map<String,Object>> counters = new HashMap<>();
	private Map<String,Map<String,Object>> histograms= new HashMap<>();
	private Map<String,Map<String,Object>> meters= new HashMap<>();
	private Map<String,Map<String,Object>> timers= new HashMap<>();

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String,Map<String,Object>> getGauges() {
		return gauges;
	}

	public void setGauges(Map<String,Map<String,Object>> gauges) {
		this.gauges = gauges;
	}

	public Map<String,Map<String,Object>> getCounters() {
		return counters;
	}

	public void setCounters(Map<String,Map<String,Object>> counters) {
		this.counters = counters;
	}

	public Map<String,Map<String,Object>> getHistograms() {
		return histograms;
	}

	public void setHistograms(Map<String,Map<String,Object>> histograms) {
		this.histograms = histograms;
	}

	public Map<String,Map<String,Object>> getMeters() {
		return meters;
	}

	public void setMeters(Map<String,Map<String,Object>> meters) {
		this.meters = meters;
	}

	public Map<String,Map<String,Object>> getTimers() {
		return timers;
	}

	public void setTimers(Map<String,Map<String,Object>> timers) {
		this.timers = timers;
	}
	
	
}
