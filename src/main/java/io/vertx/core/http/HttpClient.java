/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.core.http;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.metrics.Measured;
import io.vertx.core.net.SSLOptions;

import java.util.List;
import java.util.function.Function;

/**
 * An asynchronous HTTP client.
 * <p>
 * It allows you to make requests to HTTP servers, and a single client can make requests to any server.
 * <p>
 * This gives the benefits of keep alive when the client is loaded but means we don't keep connections hanging around
 * unnecessarily when there would be no benefits anyway.
 * <p>
 * The client is designed to be reused between requests.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@VertxGen
public interface HttpClient extends Measured {

  /**
   * Create an HTTP request to send to the server. The {@code handler}
   * is called when the request is ready to be sent.
   *
   * @param options    the request options
   * @param handler    the handler called when the request is ready to be sent
   */
  void request(RequestOptions options, Handler<AsyncResult<HttpClientRequest>> handler);

  /**
   * Like {@link #request(RequestOptions, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<HttpClientRequest> request(RequestOptions options);

  /**
   * Create an HTTP request to send to the server at the {@code host} and {@code port}. The {@code handler}
   * is called when the request is ready to be sent.
   *
   * @param method     the HTTP method
   * @param port       the port
   * @param host       the host
   * @param requestURI the relative URI
   * @param handler    the handler called when the request is ready to be sent
   */
  void request(HttpMethod method, int port, String host, String requestURI, Handler<AsyncResult<HttpClientRequest>> handler);

  /**
   * Like {@link #request(HttpMethod, int, String, String, Handler)} but returns a {@code Future} of the asynchronous result
   */
  default Future<HttpClientRequest> request(HttpMethod method, int port, String host, String requestURI) {
    return request(new RequestOptions().setMethod(method).setPort(port).setHost(host).setURI(requestURI));
  }

  /**
   * Create an HTTP request to send to the server at the {@code host} and default port. The {@code handler}
   * is called when the request is ready to be sent.
   *
   * @param method     the HTTP method
   * @param host       the host
   * @param requestURI the relative URI
   * @param handler    the handler called when the request is ready to be sent
   */
  void request(HttpMethod method, String host, String requestURI, Handler<AsyncResult<HttpClientRequest>> handler);

  /**
   * Like {@link #request(HttpMethod, String, String, Handler)} but returns a {@code Future} of the asynchronous result
   */
  default Future<HttpClientRequest> request(HttpMethod method, String host, String requestURI) {
    return request(new RequestOptions().setMethod(method).setHost(host).setURI(requestURI));
  }

  /**
   * Create an HTTP request to send to the server at the default host and port. The {@code handler}
   * is called when the request is ready to be sent.
   *
   * @param method     the HTTP method
   * @param requestURI the relative URI
   * @param handler    the handler called when the request is ready to be sent
   */
  void request(HttpMethod method, String requestURI, Handler<AsyncResult<HttpClientRequest>> handler);

  /**
   * Like {@link #request(HttpMethod, String, Handler)} but returns a {@code Future} of the asynchronous result
   */
  default Future<HttpClientRequest> request(HttpMethod method, String requestURI) {
    return request(new RequestOptions().setMethod(method).setURI(requestURI));
  }

  /**
   * Connect a WebSocket to the specified port, host and relative request URI
   * @param port  the port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param handler  handler that will be called with the WebSocket when connected
   * @deprecated instead use {@link WebSocketClient#connect(int, String, String, Handler)}
   */
  @Deprecated
  void webSocket(int port, String host, String requestURI, Handler<AsyncResult<WebSocket>> handler);

  /**
   * Like {@link #webSocket(int, String, String, Handler)} but returns a {@code Future} of the asynchronous result
   * @deprecated instead use {@link WebSocketClient#connect(int, String, String)}
   */
  @Deprecated
  Future<WebSocket> webSocket(int port, String host, String requestURI);

  /**
   * Connect a WebSocket to the host and relative request URI and default port
   * @param host  the host
   * @param requestURI  the relative URI
   * @param handler  handler that will be called with the WebSocket when connected
   * @deprecated instead use {@link WebSocketClient#connect(int, String, String, Handler)}
   */
  @Deprecated
  void webSocket(String host, String requestURI, Handler<AsyncResult<WebSocket>> handler);

  /**
   * Like {@link #webSocket(String, String, Handler)} but returns a {@code Future} of the asynchronous result
   * @deprecated instead use {@link WebSocketClient#connect(int, String, String)}
   */
  @Deprecated
  Future<WebSocket> webSocket(String host, String requestURI);

  /**
   * Connect a WebSocket at the relative request URI using the default host and port
   * @param requestURI  the relative URI
   * @param handler  handler that will be called with the WebSocket when connected
   * @deprecated instead use {@link WebSocketClient#connect(int, String, String, Handler)}
   */
  @Deprecated
  void webSocket(String requestURI, Handler<AsyncResult<WebSocket>> handler);

  /**
   * Like {@link #webSocket(String, Handler)} but returns a {@code Future} of the asynchronous result
   * @deprecated instead use {@link WebSocketClient#connect(int, String, String)}
   */
  @Deprecated
  Future<WebSocket> webSocket(String requestURI);

  /**
   * Connect a WebSocket with the specified options.
   *
   * @param options  the request options
   * @deprecated instead use {@link WebSocketClient#connect(WebSocketConnectOptions, Handler)}
   */
  @Deprecated
  void webSocket(WebSocketConnectOptions options, Handler<AsyncResult<WebSocket>> handler);

  /**
   * Like {@link #webSocket(WebSocketConnectOptions, Handler)} but returns a {@code Future} of the asynchronous result
   * @deprecated instead use {@link WebSocketClient#connect(WebSocketConnectOptions)}
   */
  @Deprecated
  Future<WebSocket> webSocket(WebSocketConnectOptions options);

  /**
   * Connect a WebSocket with the specified absolute url, with the specified headers, using
   * the specified version of WebSockets, and the specified WebSocket sub protocols.
   *
   * @param url            the absolute url
   * @param headers        the headers
   * @param version        the WebSocket version
   * @param subProtocols   the subprotocols to use
   * @param handler handler that will be called if WebSocket connection fails
   * @deprecated instead use {@link WebSocketClient#connect(WebSocketConnectOptions, Handler)}
   */
  @Deprecated
  void webSocketAbs(String url, MultiMap headers, WebsocketVersion version, List<String> subProtocols, Handler<AsyncResult<WebSocket>> handler);

  /**
   * Like {@link #webSocketAbs(String, MultiMap, WebsocketVersion, List, Handler)} but returns a {@code Future} of the asynchronous result
   * @deprecated instead use {@link WebSocketClient#connect(WebSocketConnectOptions)}
   */
  @Deprecated
  Future<WebSocket> webSocketAbs(String url, MultiMap headers, WebsocketVersion version, List<String> subProtocols);

  /**
   * <p>Update the client with new SSL {@code options}, the update happens if the options object is valid and different
   * from the existing options object.
   *
   * <p>The boolean succeeded future result indicates whether the update occurred.
   *
   * @param options the new SSL options
   * @return a future signaling the update success
   */
  default Future<Boolean> updateSSLOptions(SSLOptions options) {
    return updateSSLOptions(options, false);
  }

  /**
   * Like {@link #updateSSLOptions(SSLOptions)}  but supplying a handler that will be called when the update
   * happened (or has failed).
   *
   * @param options the new SSL options
   * @param handler the update handler
   */
  default void updateSSLOptions(SSLOptions options, Handler<AsyncResult<Boolean>> handler) {
    Future<Boolean> fut = updateSSLOptions(options);
    if (handler != null) {
      fut.onComplete(handler);
    }
  }

  /**
   * <p>Update the client with new SSL {@code options}, the update happens if the options object is valid and different
   * from the existing options object.
   *
   * <p>The {@code options} object is compared using its {@code equals} method against the existing options to prevent
   * an update when the objects are equals since loading options can be costly, this can happen for share TCP servers.
   * When object are equals, setting {@code force} to {@code true} forces the update.
   *
   * <p>The boolean succeeded future result indicates whether the update occurred.
   *
   * @param options the new SSL options
   * @param force force the update when options are equals
   * @return a future signaling the update success
   */
  Future<Boolean> updateSSLOptions(SSLOptions options, boolean force);

  /**
   * Like {@link #updateSSLOptions(SSLOptions)}  but supplying a handler that will be called when the update
   * happened (or has failed).
   *
   * @param options the new SSL options
   * @param force force the update when options are equals
   * @param handler the update handler
   */
  default void updateSSLOptions(SSLOptions options, boolean force, Handler<AsyncResult<Boolean>> handler) {
    Future<Boolean> fut = updateSSLOptions(options, force);
    if (handler != null) {
      fut.onComplete(handler);
    }
  }

  /**
   * Set a connection handler for the client. This handler is called when a new connection is established.
   *
   * @return a reference to this, so the API can be used fluently
   * @deprecated instead use {@link HttpClientBuilder#withConnectHandler(Handler)}
   */
  @Deprecated
  @Fluent
  HttpClient connectionHandler(Handler<HttpConnection> handler);

  /**
   * Set a redirect handler for the http client.
   * <p>
   * The redirect handler is called when a {@code 3xx} response is received and the request is configured to
   * follow redirects with {@link HttpClientRequest#setFollowRedirects(boolean)}.
   * <p>
   * The redirect handler is passed the {@link HttpClientResponse}, it can return an {@link HttpClientRequest} or {@code null}.
   * <ul>
   *   <li>when null is returned, the original response is processed by the original request response handler</li>
   *   <li>when a new {@code Future<HttpClientRequest>} is returned, the client will send this new request</li>
   * </ul>
   * The new request will get a copy of the previous request headers unless headers are set. In this case,
   * the client assumes that the redirect handler exclusively managers the headers of the new request.
   * <p>
   * The handler must return a {@code Future<HttpClientRequest>} unsent so the client can further configure it and send it.
   *
   * @param handler the new redirect handler
   * @return a reference to this, so the API can be used fluently
   * @deprecated instead use {@link HttpClientBuilder#withRedirectHandler(Function)}
   */
  @Deprecated
  @Fluent
  HttpClient redirectHandler(Function<HttpClientResponse, Future<RequestOptions>> handler);

  /**
   * @return the current redirect handler.
   */
  @Deprecated
  @GenIgnore
  Function<HttpClientResponse, Future<RequestOptions>> redirectHandler();

  /**
   * Close the client. Closing will close down any pooled connections.
   * Clients should always be closed after use.
   */
  void close(Handler<AsyncResult<Void>> handler);

  /**
   * Like {@link #close(Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> close();

}
