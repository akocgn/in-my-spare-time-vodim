package de.inmysparetime.vodim;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.inmysparetime.vodim.reader.MetricsFileReader;
import de.inmysparetime.vodim.reader.MetricsReader;
import de.inmysparetime.vodim.reader.MetricsURLReader;
import javafx.application.Application;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VodimMain extends Application {
	private final static Logger LOG = LogManager.getLogger(VodimMain.class);
	private static final int PREFERED_WIDTH = 1024;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		final String source = getParameters().getNamed().get("src");
		MetricsReader reader = null;
		if (source.startsWith("http")) {
			try {
				reader = new MetricsURLReader(new URL(source));
			} catch (MalformedURLException e) {
				LOG.error("Cannot reach '"+source+"' as URL.", e);
				System.exit(-1);
			}
		} else {
			reader = new MetricsFileReader(new File(source));
		}
		final MetricsHistory history = new MetricsHistory();
		final Processor processor = new Processor(reader, history);
		primaryStage.setTitle("VoDiM");
		final Service<Optional<Metrics>> service = new ServiceFactory().createInitializationService(processor,
				mx -> initialiseUI(primaryStage, processor, mx));
		service.start();

		final ScheduledService<Metrics> updateservice = new ServiceFactory().createUpdateService(Duration.seconds(30),
			reader, history);
		updateservice.setRestartOnFailure(true);
		updateservice.setDelay(Duration.seconds(30));
		updateservice.start();
		primaryStage.show();
	}

	private Metrics initialiseUI(final Stage primaryStage, final Processor processor, final Metrics mx) {
		final TabPane root = new TabPane();
		root.setPrefWidth(PREFERED_WIDTH);
		if (processor.hasGaugeHistory()) {
			displayGauges("Gauges", root, processor.getGaugeHistory());
		}
		if (processor.hasMeterHistory()) {
			displayGauges("Meters", root, processor.getMeterHistory());
		}
		// if (!mx.getCounters().isEmpty()) {
		// displayCounters(root, mx.getCounters());
		// }
		// if (!mx.getMeters().isEmpty()) {
		// displayMeters(root, mx.getMeters());
		// }
		// if (!mx.getHistograms().isEmpty()) {
		// displayHistograms(root, mx.getHistograms());
		// }
		// if (!mx.getTimers().isEmpty()) {
		// displayTimers(root, mx.getTimers());
		// }
		final Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("vodim.css").toExternalForm());
		primaryStage.setScene(scene);
		return mx;

	}

	private void displayGauges(final String title, TabPane root, ObservedEntries<Number> observedEntries) {
		final Tab tab = new Tab(title);

		// Zunächst einmal jeden Messwert in eine eigene Tabelle.
		// Erstelle für jeden Präfix eine Tabelle.
		final VBox vbox = new VBox();
		vbox.setPrefWidth(PREFERED_WIDTH);
		final Map<String, LineChart<String, Number>> charts = new HashMap<>();
		boolean odd = true;
		for (final Entry<String, Series<String, Number>> entry : observedEntries.getObservedMap().entrySet()) {
			final String prefix = getPrefix(entry.getKey());
			final long count = observedEntries.getObservedMap().keySet().stream()
					.filter(k -> getPrefix(k).equals(prefix)).count();
			final String chartLabel;
			if (count > 1) {
				chartLabel = prefix;
			} else {
				chartLabel = entry.getKey();
			}
			LineChart<String, Number> chart = charts.get(chartLabel);
			if (chart == null) {
				final CategoryAxis xAxis = new CategoryAxis();
				xAxis.setLabel("Time");

				final NumberAxis yAxis = new NumberAxis();
				yAxis.setLabel("Events");
				chart = new LineChart<>(xAxis, yAxis);
				chart.setTitle(chartLabel);
				charts.put(chartLabel, chart);
				vbox.getChildren().add(chart);
				chart.getStyleClass().add(odd ? "chartentry-odd" : "chartentry-even");
				odd = !odd;
			}
			chart.getData().add(entry.getValue());
		}
		ScrollPane pane = new ScrollPane(vbox);
		tab.setContent(pane);
		root.getTabs().add(tab);
	}

	private String getPrefix(final String value) {
		final int lastIdx = value.lastIndexOf('.');
		return lastIdx > 0 ? value.substring(0, lastIdx) : value;
	}

	private void displayCounters(TabPane root, Map<String, Map<String, Object>> gauges) {
		final Tab tab = new Tab("Counters");
		root.getTabs().add(tab);
	}

	private void displayMeters(TabPane root, Map<String, Map<String, Object>> gauges) {
		final Tab tab = new Tab("Meters");
		root.getTabs().add(tab);
	}

	private void displayHistograms(TabPane root, Map<String, Map<String, Object>> gauges) {
		final Tab tab = new Tab("Histograms");
		root.getTabs().add(tab);
	}

	private void displayTimers(TabPane root, Map<String, Map<String, Object>> gauges) {
		final Tab tab = new Tab("Timers");
		root.getTabs().add(tab);
	}
}