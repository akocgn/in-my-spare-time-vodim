package de.inmysparetime.vodim.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import de.inmysparetime.vodim.Metrics;
import de.inmysparetime.vodim.reader.MetricsFileReader;

public class MetricsFileReaderTest {

	@Test
	public void testReadMetrics() throws Exception {
		final MetricsFileReader reader = new MetricsFileReader(new File("src/test/resources/sampledata.json"));
		final Metrics metrics = reader.readMetrics();
		assertNotNull(metrics);
		assertEquals("3.0.0", metrics.getVersion());
		assertEquals(73, metrics.getGauges().size());
	}

	@Test
	public void testReadMetricsOnlyVersion() throws Exception {
		final MetricsFileReader reader = new MetricsFileReader(new File("src/test/resources/sampledata_only_version.json"));
		final Metrics metrics = reader.readMetrics();
		assertNotNull(metrics);
		assertEquals("3.0.0", metrics.getVersion());
		assertEquals(0, metrics.getGauges().size());
	}

	@Test
	public void testReadMetricsVersionAndGauges() throws Exception {
		final MetricsFileReader reader = new MetricsFileReader(new File("src/test/resources/sampledata_version_and_gauges.json"));
		final Metrics metrics = reader.readMetrics();
		assertNotNull(metrics);
		assertEquals("3.0.0", metrics.getVersion());
		assertEquals(73, metrics.getGauges().size());
	}
}
