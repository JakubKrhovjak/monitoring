vm variables
-javaagent:opentelemetry-javaagent.jar

env variables
OTEL_LOGS_EXPORTER=otlp;OTEL_METRICS_EXPORTER=otlp;OTEL_RESOURCES_ATTRIBUTES=service.name=test-service, service.namespace=test-namespace, service.version=1.0;OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf