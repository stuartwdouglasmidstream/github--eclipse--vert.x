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

package io.vertx.core.net;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.metrics.Measured;

/**
 * A TCP client.
 * <p>
 * Multiple connections to different servers can be made using the same instance.
 * <p>
 * This client supports a configurable number of connection attempts and a configurable
 * delay between attempts.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@VertxGen
public interface NetClient extends Measured {

  /**
   * Open a connection to a server at the specific {@code port} and {@code host}.
   * <p>
   * {@code host} can be a valid host name or IP address. The connect is done asynchronously and on success, a
   * {@link NetSocket} instance is supplied via the {@code connectHandler} instance
   *
   * @param port  the port
   * @param host  the host
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  NetClient connect(int port, String host, Handler<AsyncResult<NetSocket>> connectHandler);

  /**
   * Like {@link #connect(int, String, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<NetSocket> connect(int port, String host);

  /**
   * Open a connection to a server at the specific {@code port} and {@code host}.
   * <p>
   * {@code host} can be a valid host name or IP address. The connect is done asynchronously and on success, a
   * {@link NetSocket} instance is supplied via the {@code connectHandler} instance
   *
   * @param port the port
   * @param host the host
   * @param serverName the SNI server name
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  NetClient connect(int port, String host, String serverName, Handler<AsyncResult<NetSocket>> connectHandler);

  /**
   * Like {@link #connect(int, String, String, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<NetSocket> connect(int port, String host, String serverName);

  /**
   * Open a connection to a server at the specific {@code remoteAddress}.
   * <p>
   * The connect is done asynchronously and on success, a {@link NetSocket} instance is supplied via the {@code connectHandler} instance
   *
   * @param remoteAddress the remote address
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  NetClient connect(SocketAddress remoteAddress, Handler<AsyncResult<NetSocket>> connectHandler);

  /**
   * Like {@link #connect(SocketAddress, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<NetSocket> connect(SocketAddress remoteAddress);

  /**
   * Open a connection to a server at the specific {@code remoteAddress}.
   * <p>
   * The connect is done asynchronously and on success, a {@link NetSocket} instance is supplied via the {@code connectHandler} instance
   *
   * @param remoteAddress the remote address
   * @param serverName the SNI server name
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  NetClient connect(SocketAddress remoteAddress, String serverName, Handler<AsyncResult<NetSocket>> connectHandler);

  /**
   * Like {@link #connect(SocketAddress, String, Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<NetSocket> connect(SocketAddress remoteAddress, String serverName);

  /**
   * Close the client.
   * <p>
   * Any sockets which have not been closed manually will be closed here. The close is asynchronous and may not
   * complete until some time after the method has returned.
   */
  void close(Handler<AsyncResult<Void>> handler);

  /**
   * Like {@link #close(Handler)} but returns a {@code Future} of the asynchronous result
   */
  Future<Void> close();

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
}
