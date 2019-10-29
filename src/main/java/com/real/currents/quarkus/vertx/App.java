package com.real.currents.quarkus.vertx;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.reactivex.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/start")
public class App {

    @Inject
    Vertx vertx;

    static Logger logger = LoggerFactory.getLogger(App.class);

    private String HELLO_WORLD = "Hello, world!";
    private CharSequence[] plaintextHeaders;

    void onStart(@Observes StartupEvent ev) {

        CharSequence HEADER_SERVER = HttpHeaders.createOptimized("server");
        CharSequence HEADER_DATE = HttpHeaders.createOptimized("date");
        CharSequence HEADER_CONTENT_TYPE = HttpHeaders.createOptimized("content-type");
        CharSequence HEADER_CONTENT_LENGTH = HttpHeaders.createOptimized("content-length");
        CharSequence HELLO_WORLD_LENGTH = HttpHeaders.createOptimized("" + HELLO_WORLD.length());
        CharSequence RESPONSE_TYPE_PLAIN = HttpHeaders.createOptimized("text/plain");
        CharSequence RESPONSE_TYPE_HTML = HttpHeaders.createOptimized("text/html; charset=UTF-8");
        CharSequence RESPONSE_TYPE_JSON = HttpHeaders.createOptimized("application/json");
        CharSequence SERVER = HttpHeaders.createOptimized("vert.x");
        CharSequence dateString = HttpHeaders.createOptimized(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now()));

        plaintextHeaders = new CharSequence[]{
            HEADER_CONTENT_TYPE, RESPONSE_TYPE_PLAIN,
            HEADER_SERVER, SERVER,
            HEADER_DATE, dateString,
            HEADER_CONTENT_LENGTH, HELLO_WORLD_LENGTH};

        vertx.exceptionHandler(err -> logger.error(err.getLocalizedMessage()));
        printConfig(this.vertx);

//        vertx.deployVerticle(SomeVerticle.getClass().getName(),
//            new DeploymentOptions().setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE), event -> {
//                if (event.succeeded()) {
//                    logger.info("SomeVerticle deployed: " + event.result());
//                } else {
//                    logger.error("Unable to deploy verticle: " + event.cause());
//                }
//            });
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String startApp() {
        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Start App ("+ printConfig(this.vertx) +")</title>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "  <script type=\"text/javascript\" src=\"scripts/app.js\"></script></body>\n" +
            "</html>";
    }

//    @GET
//    @Path("/object")
//    @Produces(MediaType.APPLICATION_JAVASCRIPT_MEDIA_TYPE)
//    public JSONPObject object(@PathParam("id") String id) {
//        return new JSONPObject(
//            "(function doSomething (arg) { console.log(arg); })",
//            "argument"
//        );
//    }

    private static String printConfig(Vertx vertx) {
        String version = "unknown";

        if (vertx != null) try {
            boolean nativeTransport = vertx.isNativeTransportEnabled();

            InputStream in = Vertx.class.getClassLoader().getResourceAsStream("META-INF/vertx/vertx-version.txt");
            if (in == null) {
                in = Vertx.class.getClassLoader().getResourceAsStream("vertx-version.txt");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[256];
            while (true) {
                int amount = in.read(buffer);
                if (amount == -1) {
                    break;
                }
                out.write(buffer, 0, amount);
            }
            version = out.toString();

            logger.info("Vertx: " + version);
            logger.info("Event Loop Size: " + VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE);
            logger.info("Native transport : " + nativeTransport);

        } catch (IOException e) {
            logger.error("Could not read Vertx version", e);
        } else {
            logger.error("Could not get Vertx instance", vertx);
        }

        return version;
    }

}
