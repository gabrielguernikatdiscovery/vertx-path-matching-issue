package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    HttpServer server = vertx.createHttpServer();

    Router router = Router.router(vertx);

    router.route("/some/path/*").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response.putHeader("content-type", "text/plain");
      response.end("Hello World from Vert.x-Web!");
    });

    server.requestHandler(router).listen(8080);
  }
}
