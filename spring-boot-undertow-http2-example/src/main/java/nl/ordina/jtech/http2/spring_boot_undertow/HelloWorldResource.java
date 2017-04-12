package nl.ordina.jtech.http2.spring_boot_undertow;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.ServerConnection;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloWorldResource {

    @RequestMapping("/hello")
    @ResponseBody
    public String helloWorld() {
        final HttpServerExchange exchange = ServletRequestContext.requireCurrent().getExchange();
        final ServerConnection connection = exchange.getConnection();
        System.out.println("helloWorld thread: " + Thread.currentThread().getName());
        System.out.println("Connection type: " + connection.getClass().getName());
        final boolean attempted = connection.pushResource("/pushed", new HttpString("GET"), new HeaderMap());

        return "look ma, no hands! Push attempted: " + attempted;
    }

    @RequestMapping("/pushed")
    @ResponseBody
    public String pushed() {
        System.out.println("pushed thread: " + Thread.currentThread().getName());
        return "Don't push me!";
    }

    @RequestMapping("/akamai")
    @ResponseBody
    public String akamai() {
        RestTemplate http2Template = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        RestTemplate http11Template = new RestTemplate();

        String http11Response = http11Template.getForObject("https://http2.akamai.com/", String.class);
        String http2Response = http2Template.getForObject("https://http2.akamai.com/", String.class);

        return "HTTP/1.1 : " + http11Response.contains("You are using HTTP/2 right now!") + "<br/>" +
                "HTTP/2 : " + http2Response.contains("You are using HTTP/2 right now!");
    }

}
