package org.upstartcommerce.smartystreets.common.util

import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json._

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

/** Wraps the utility stuff used in other places
  *
  * @author
  *   Yan Doroshenko
  */
object Utils {

  /** Defines the implicit configuration for Play JSON to use snake_case
    *
    * @author
    *   Yan Doroshenko
    */
  trait JsonSnakeCase {
    implicit val c: JsonConfiguration = JsonConfiguration(SnakeCase)
  }

  /** Used for conversion between Y/N and Boolean.
    *   - '''Y''' - true
    *   - '''N''' - false
    *
    * @author
    *   Yan Doroshenko
    */
  object CharBoolean extends Enumeration {
    type CharBoolean = Value

    protected case class Member(code: Char, value: Boolean) extends super.Val

    val True: Member = Member('Y', value = true)
    val False: Member = Member('N', value = false)

    implicit def valueToVal(v: Value): Member = v.asInstanceOf[Member]

    implicit val f: Format[Value] =
      new Format[Value] {
        override def writes(o: Value): JsValue = JsString(o.code.toString)

        override def reads(json: JsValue): JsResult[Value] = JsSuccess(values.find(_.code == json.as[String].head).get)
      }

  }

}
