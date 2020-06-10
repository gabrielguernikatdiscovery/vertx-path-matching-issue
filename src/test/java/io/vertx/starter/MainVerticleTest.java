package io.vertx.starter;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.lang.String.format;

@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

  private Vertx vertx;

  @Before
  public void setUp(TestContext tc) {
    vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName(), tc.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext tc) {
    vertx.close(tc.asyncAssertSuccess());
  }

  @Test
  public void pathWithoutSlash(TestContext tc) {
    /*
    This should work according to the docs in
    https://vertx.io/docs/vertx-web/java/#_routing_by_paths_that_begin_with_something
    I believe this bug was introduced in
    https://github.com/vert-x3/vertx-web/commit/628f49c950b864b88288bf2ca23ef126acf5be25
     */
    callAndExpect200("/some/path", tc);
  }

  @Test
  public void pathWithSlash(TestContext tc) {
    callAndExpect200("/some/path/", tc);
  }

  @Test
  public void pathWithSubdir(TestContext tc) {
    callAndExpect200("/some/path/subdir", tc);
  }

  @Test
  public void pathWithSubdirAndHtmlFile(TestContext tc) {
    callAndExpect200("/some/path/subdir/blah.html", tc);
  }

  @Test
  public void pathWithNonMatchingSubpath(TestContext tc) {
    callAndExpect404("/some/bath", tc);
  }

  private void callAndExpect200(String path, TestContext tc) {
    callAndExpectStatus(path, 200, tc);
  }

  private void callAndExpect404(String path, TestContext tc) {
    callAndExpectStatus(path, 404, tc);
  }

  private void callAndExpectStatus(String path, int expectedStatusCode, TestContext tc) {
    Async async = tc.async();
    vertx.createHttpClient().getNow(8080, "localhost", path, response -> {
      tc.assertEquals(response.statusCode(), expectedStatusCode,
        format("Expected %s but got %s", expectedStatusCode, response.statusCode()));
      async.complete();
    });
  }
}
