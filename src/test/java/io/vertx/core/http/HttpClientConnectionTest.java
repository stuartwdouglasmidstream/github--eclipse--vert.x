/*
 * Copyright (c) 2011-2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package io.vertx.core.http;

import io.netty.buffer.Unpooled;
import io.vertx.core.MultiMap;
import io.vertx.core.http.impl.HttpClientInternal;
import io.vertx.core.http.impl.HttpRequestHead;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.net.SocketAddress;
import io.vertx.test.core.TestUtils;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class HttpClientConnectionTest extends HttpTestBase {

  private File tmp;
  protected HttpClientInternal client;
  protected SocketAddress peerAddress;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.client = (HttpClientInternal) super.client;
    this.peerAddress = SocketAddress.inetSocketAddress(requestOptions.getPort(), requestOptions.getHost());
  }

  @Test
  public void testGet() throws Exception {
    server.requestHandler(req -> {
      req.response().end("Hello World");
    });
    startServer(testAddress);
    client.connect(testAddress, peerAddress)
      .compose(conn -> conn.createRequest((ContextInternal) vertx.getOrCreateContext()))
      .compose(request -> request
        .send()
        .andThen(onSuccess(resp -> assertEquals(200, resp.statusCode())))
        .compose(HttpClientResponse::body))
      .onComplete(onSuccess(body -> {
        assertEquals("Hello World", body.toString());
        testComplete();
      }));
    await();
  }

  @Test
  public void testStreamGet() throws Exception {
    waitFor(3);
    server.requestHandler(req -> {
      req.response().end("Hello World");
    });
    startServer(testAddress);
    client.connect(testAddress, peerAddress).onComplete(onSuccess(conn -> {
      conn.createStream((ContextInternal) vertx.getOrCreateContext(), onSuccess(stream -> {
        stream.writeHead(new HttpRequestHead(
          HttpMethod.GET, "/", MultiMap.caseInsensitiveMultiMap(), DEFAULT_HTTP_HOST_AND_PORT, "", null), false, Unpooled.EMPTY_BUFFER, true, new StreamPriority(), false, onSuccess(v -> {
        }));
        stream.headHandler(resp -> {
          assertEquals(200, resp.statusCode);
          complete();
        });
        stream.endHandler(headers -> {
          assertEquals(0, headers.size());
          complete();
        });
        stream.closeHandler(v -> {
          complete();
        });
      }));
    }));
    await();
  }

  @Test
  public void testConnectionClose() throws Exception {
    waitFor(2);
    server.requestHandler(req -> {
      req.response().close();
    });
    startServer(testAddress);
    client.connect(testAddress, peerAddress).onComplete(onSuccess(conn -> {
      AtomicInteger evictions = new AtomicInteger();
      conn.evictionHandler(v -> {
        assertEquals(1, evictions.incrementAndGet());
        complete();
      });
      conn.createStream((ContextInternal) vertx.getOrCreateContext(), onSuccess(stream -> {
        stream.writeHead(new HttpRequestHead(
          HttpMethod.GET, "/", MultiMap.caseInsensitiveMultiMap(), DEFAULT_HTTP_HOST_AND_PORT, "", null), false, Unpooled.EMPTY_BUFFER, true, new StreamPriority(), false, onSuccess(v -> {
        }));
        stream.headHandler(resp -> {
          fail();
        });
        stream.endHandler(headers -> {
          fail();
        });
        stream.closeHandler(v -> {
          assertEquals(1, evictions.get());
          complete();
        });
      }));
    }));
    await();
  }
}
