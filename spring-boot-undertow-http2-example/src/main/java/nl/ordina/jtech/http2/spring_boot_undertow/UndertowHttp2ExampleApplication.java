package nl.ordina.jtech.http2.spring_boot_undertow;

import io.undertow.UndertowOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

/**
 * cmdline: mvn package; java -Xbootclasspath/p:alpn-boot-8.1.2.v20141202.jar -jar target/spring-boot-undertow-http2-example-1.0.0.jar
 * browser: https://localhost:8444/hello
 * curl -k -v --http2 https://localhost:8444/hello 2>&1 | grep ign
 * curl -k --http1.1 https://localhost:8444/hello
 */
@SpringBootApplication
public class UndertowHttp2ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(UndertowHttp2ExampleApplication.class, args);
    }

    @Bean
    UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.addBuilderCustomizers(
                builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
        return factory;
    }

}
