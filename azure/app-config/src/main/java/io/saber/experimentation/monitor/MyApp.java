package io.saber.experimentation.monitor;


import com.azure.opentelemetry.exporters.azuremonitor.AzureMonitorExporter;
import com.azure.opentelemetry.exporters.azuremonitor.AzureMonitorExporterBuilder;
import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.trace.Span;
//import io.opentelemetry.trace.Status;
import io.opentelemetry.trace.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyApp {

    private static final Logger logger = LoggerFactory.getLogger(MyApp.class.getName());

    public static void main(String[] args) {

        AzureMonitorExporter exporter = new AzureMonitorExporterBuilder()
                .instrumentationKey("fc197122-2104-4a8e-a371-ad87d71832cf")
                .buildExporter();

        //Build the OpenTelemetry BatchSpansProcessor with the NewRelicSpanExporter
        BatchSpanProcessor spanProcessor = BatchSpanProcessor.newBuilder(exporter).build();

        //Add the rootSpan processor to the default TracerSdkProvider
        OpenTelemetrySdk.getTracerProvider().addSpanProcessor(spanProcessor);

        //Create an OpenTelemetry Tracer
        Tracer tracer = OpenTelemetry.getTracerProvider().get("opentel-example", "1.0");

        //Create a basic rootSpan.  You only need to specify the name of the rootSpan. The start and end time of the
        // rootSpan is automatically set by the OpenTelemetry SDK.
        Span rootSpan = tracer
                .spanBuilder("getCustomerOrder")
                .setAttribute("ai.sampling.percentage", 1d)
                .startSpan();
        //Key:value pairs can be used to affix metadata to spans, events, metrics, and distributed contexts in order to
        // query, filter, and  analyze trace data.  Add a simple attribute to our rootSpan.
        rootSpan.setAttribute("Root-span-attribute", 1);

        try (Scope scope = tracer.withSpan(rootSpan)) {

            //Just pause to pretend we're doing something
            Thread.sleep(300);

            //Add a couple of child spans to the root span
            Span childSpan = tracer
                    .spanBuilder("getCustomerRecord")
                    .setAttribute("ai.sampling.percentage", 1d)
                    .startSpan();
            logger.info("Active Span: " + tracer.getCurrentSpan().toString());
            Thread.sleep(500);
            childSpan.end();

            Span childSpan2 = tracer
                    .spanBuilder("getOrderDetails")
                    .setAttribute("ai.sampling.percentage", 1d)
                    .startSpan();
            childSpan2.setAttribute("customer-id", 1);
            childSpan2.setAttribute("order-no", 100);

            Thread.sleep(1500);
            childSpan2.end();

            Thread.sleep(1000);
            CompletableResultCode result = exporter.flush();
            while (true) {
            }
        } catch (Throwable t) {
//            Status status = Status.UNKNOWN.withDescription("Cunning error message goes here!");
//            rootSpan.setStatus(status);
        } finally {
            rootSpan.end(); // closing the scope does not end the rootSpan, this has to be done manually
            spanProcessor.shutdown();
        }
    }
}
