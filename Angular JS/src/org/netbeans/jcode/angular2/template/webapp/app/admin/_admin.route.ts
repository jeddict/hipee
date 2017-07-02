<%#
 Copyright 2013-2017 the original author or authors.

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

 Portions Copyright 2013-2017 Gaurav Gupta
-%>
import { Routes } from '@angular/router';

import {
    <%_ if (enableAudits) { _%>
    auditsRoute,
    <%_ } _%>
    <%_ if (enableConfiguration) { _%>
    configurationRoute,
    <%_ } _%>
    <%_ if (enableDocs) { _%>
    docsRoute,
    <%_ } _%>
    <%_ if (enableHealth) { _%>
    healthRoute,
    <%_ } _%>
    <%_ if (enableLogs) { _%>
    logsRoute,
    <%_ } _%>
    <%_ if (enableMetrics) { _%>
    metricsRoute,
    <%_ } _%>
    <%_ if (applicationType === 'gateway') { _%>
    gatewayRoute,
    <%_ } _%>
    <%_ if (websocket === 'spring-websocket') { _%>
    trackerRoute,
    <%_ } _%>
    <%_ if (!skipUserManagement) { _%>
    userMgmtRoute,
    userDialogRoute
    <%_ } _%>
} from './';

import { UserRouteAccessService } from '../shared';

let ADMIN_ROUTES = [
    <%_ if (enableAudits) { _%>
    auditsRoute,
    <%_ } _%>
    <%_ if (enableConfiguration) { _%>
    configurationRoute,
    <%_ } _%>
    <%_ if (enableDocs) { _%>
    docsRoute,
    <%_ } _%>
    <%_ if (enableHealth) { _%>
    healthRoute,
    <%_ } _%>
    <%_ if (enableLogs) { _%>
    logsRoute,
    <%_ } _%>
    <%_ if (applicationType === 'gateway') { _%>
    gatewayRoute,
    <%_ } _%>
    <%_ if (websocket === 'spring-websocket') { _%>
    trackerRoute,
    <%_ } _%>
    <%_ if (!skipUserManagement) { _%>
    ...userMgmtRoute,
    <%_ } _%>
    <%_ if (enableMetrics) { _%>
    metricsRoute
    <%_ } _%>
];


export const adminState: Routes = [{
    path: '',
    data: {
        authorities: ['ROLE_ADMIN']
    },
    canActivate: [UserRouteAccessService],
    children: ADMIN_ROUTES
},
    <%_ if (!skipUserManagement) { _%>
    ...userDialogRoute
    <%_ } _%>
];
