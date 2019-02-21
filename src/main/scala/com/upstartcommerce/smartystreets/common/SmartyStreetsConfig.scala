package com.upstartcommerce.smartystreets.common

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
  * Represent configuration for SmartyStreets API
  *
  * @param authId    Authentication ID provided by SmartyStreets
  * @param authToken Authentication token provided by SmartyStreets
  * @param endpoint  Endpoint where SmartyStreets API is running
  *
  * @author Yan Doroshenko
  */
case class SmartyStreetsConfig(
                                authId: String,
                                authToken: String,
                                endpoint: String = "https://us-street.api.smartystreets.com"
                              )
