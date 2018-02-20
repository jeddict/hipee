#!/usr/bin/env sh
<%#
 Copyright 2013-2017 the original author or authors from the JHipster project.

 This file is part of the JHipster project, see https://jhipster.github.io/
 for more information.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Portions Copyright 2013-2018 Gaurav Gupta
-%>
# Use this script to run oc commands to create resources in the selected namespace. Files are ordered
# in proper order. 'oc process' processes the template as resources which is again piped to
# 'oc apply' to create those resources in OpenShift namespace
oc process -f registry/scc-config.yml | oc apply -f -
<%_ var appName = baseName.toLowerCase(); _%>
oc process -f <%- appName %>/<%- appName %>-<%- prodDatabaseType %>.yml | oc apply -f -
oc process -f <%- appName %>/<%- appName %>-deployment.yml | oc apply -f -
