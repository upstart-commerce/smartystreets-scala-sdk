package org.upstartcommerce.smartystreets.common.exception

import akka.http.scaladsl.server.RejectionError

/*
Copyright 2019 UpStart Commerce, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

/**
  * Represents an exception raised due to the inability to unmarshal SmartyStreets API response body
  *
  * @param message Message from SmartyStreets, should not be processed in any way
  * @param cause   Error thrown by the unmarshaler.
  *
  * @author Yan Doroshenko
  */
case class DeserializationException(message: String, cause: RejectionError) extends SmartyStreetsException {
  override def getCause: Throwable = cause
}
