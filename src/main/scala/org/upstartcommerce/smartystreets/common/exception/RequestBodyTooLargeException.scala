package org.upstartcommerce.smartystreets.common.exception

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

/** Represents an exception raised from the SmartyStreets API returning 413 REQUEST ENTITY TOO LARGE HTTP status, which is returned if
  * request body size exceeds the maximum of 32K
  *
  * @param message
  *   Message from SmartyStreets, should not be processed in any way
  *
  * @author
  *   Yan Doroshenko
  */
case class RequestBodyTooLargeException(message: String) extends SmartyStreetsException
