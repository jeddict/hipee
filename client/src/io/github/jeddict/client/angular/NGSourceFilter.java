/**
 * Copyright 2013-2018 the original author or authors from the Jeddict project (https://jeddict.github.io/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.jeddict.client.angular;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import io.github.jeddict.client.web.main.domain.ApplicationSourceFilter;
import io.github.jeddict.client.web.main.domain.BaseApplicationConfig;
import static io.github.jeddict.client.web.main.domain.BaseApplicationConfig.GATEWAY_APPLICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.BaseApplicationConfig.JWT_AUTHENTICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.BaseApplicationConfig.OAUTH2_AUTHENTICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.BaseApplicationConfig.SESSION_AUTHENTICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.BaseApplicationConfig.UAA_AUTHENTICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.BaseApplicationConfig.WEBSOCKET;

public class NGSourceFilter extends ApplicationSourceFilter {

    private Map<String, Supplier<Boolean>> dataFilter;

    public NGSourceFilter(BaseApplicationConfig config) {
        super(config);
    }

    @Override
    protected Map<String, Supplier<Boolean>> getGeneratorFilter() {
        if (dataFilter == null) {
            dataFilter = new HashMap<>();

            //AuthenticationType
            dataFilter.put("auth-jwt.service.ts", () -> 
                    JWT_AUTHENTICATION_TYPE.equals(config.getAuthenticationType())
                    || UAA_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth.interceptor.ts", () -> JWT_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth-session.service.ts", () -> 
                    SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType())
                    || OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            
            // session specific
            dataFilter.put("session.model.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.service.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.route.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.component.html", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.component.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.component.spec.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.json", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));

            
            //ApplicationType
            dataFilter.put("gateway.component.html", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway.component.ts", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway.route.ts", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway-route.model.ts", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway-routes.service.ts", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway.json", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));

            //Social Login
//            dataFilter.put("social-register.component.html", () -> config.isEnableSocialSignIn());
//            dataFilter.put("social-register.component.ts", () -> config.isEnableSocialSignIn());
//            dataFilter.put("social-auth.component.ts", () -> config.isEnableSocialSignIn() && JWT_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
//            dataFilter.put("social.route.ts", () -> config.isEnableSocialSignIn());
//            dataFilter.put("social.service.ts", () -> config.isEnableSocialSignIn());
//            dataFilter.put("social.component.ts", () -> config.isEnableSocialSignIn());
//            dataFilter.put("social.component.html", () -> config.isEnableSocialSignIn());
//            dataFilter.put("social.json", () -> config.isEnableSocialSignIn());  

            //WebSocket
            dataFilter.put("tracker.component.html", () -> WEBSOCKET.equals(config.getWebsocket()));//admin
            dataFilter.put("tracker.component.ts", () -> WEBSOCKET.equals(config.getWebsocket()));//admin
            dataFilter.put("tracker.route.ts", () -> WEBSOCKET.equals(config.getWebsocket()));//admin
            dataFilter.put("tracker.service.ts", () -> WEBSOCKET.equals(config.getWebsocket()));//shared
            dataFilter.put("window.service.ts", () -> WEBSOCKET.equals(config.getWebsocket()));//shared
            dataFilter.put("mock-tracker.service.ts", () -> WEBSOCKET.equals(config.getWebsocket()));//shared
            dataFilter.put("tracker.json", () -> WEBSOCKET.equals(config.getWebsocket()));//shared

            //Language
            dataFilter.put("jhi-translate.directive.ts", () -> config.isEnableTranslation());//shared/language
            dataFilter.put("translate-partial-loader.provider.ts", () -> config.isEnableTranslation());//shared/language
            dataFilter.put("find-language-from-key.pipe.ts", () -> config.isEnableTranslation());//shared/language
            dataFilter.put("language.constants.ts", () -> config.isEnableTranslation());//shared/language
            dataFilter.put("language.service.ts", () -> config.isEnableTranslation());//shared/language
            dataFilter.put("jhi-missing-translation.config.ts", () -> config.isEnableTranslation());//shared/language
            dataFilter.put("language.helper.ts", () -> config.isEnableTranslation());//shared/language            
            dataFilter.put("translation.config.ts", () -> config.isEnableTranslation());
            dataFilter.put("translation-storage.provider.ts", () -> config.isEnableTranslation());
            dataFilter.put("active-menu.directive.ts", () -> config.isEnableTranslation());//layouts/navbar
            dataFilter.put("i18n.constants.ts", () -> config.isEnableTranslation());
            
            //Metrics
            dataFilter.put("metrics.component.ts", () -> config.isEnableMetrics());
            dataFilter.put("metrics.component.html", () -> config.isEnableMetrics());
            dataFilter.put("metrics-modal.component.ts", () -> config.isEnableMetrics());
            dataFilter.put("metrics-modal.component.html", () -> config.isEnableMetrics());
            dataFilter.put("metrics.service.ts", () -> config.isEnableMetrics());
            dataFilter.put("metrics.route.ts", () -> config.isEnableMetrics());
            dataFilter.put("metrics.json", () -> config.isEnableMetrics());

            //Logs
            dataFilter.put("logs.component.ts", () -> config.isEnableLogs());
            dataFilter.put("logs.component.html", () -> config.isEnableLogs());
            dataFilter.put("logs.service.ts", () -> config.isEnableLogs());
            dataFilter.put("logs.route.ts", () -> config.isEnableLogs());
            dataFilter.put("log.model.ts", () -> config.isEnableLogs());
            dataFilter.put("logs.json", () -> config.isEnableLogs());

            //Health
            dataFilter.put("health.component.ts", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.component.html", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health-modal.component.ts", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health-modal.component.html", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.service.ts", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.route.ts", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.component.spec.ts", () -> config.isEnableHealth());//test
            dataFilter.put("health.json", () -> config.isEnableHealth());//admin/health
            
            //Configuration
            dataFilter.put("configuration.component.ts", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.component.html", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.route.ts", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.service.ts", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.json", () -> config.isEnableConfiguration());

            //Audit
            dataFilter.put("audits.component.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.component.html", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.route.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.service.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audit-data.model.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audit.model.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.component.spec.ts", () -> config.isEnableAudits());
            dataFilter.put("audits.service.spec.ts", () -> config.isEnableAudits());
            dataFilter.put("audits.json", () -> config.isEnableAudits());//admin/audits/

            //Docs
            dataFilter.put("docs.component.ts", () -> config.isEnableDocs());
            dataFilter.put("docs.component.html", () -> config.isEnableDocs());
            dataFilter.put("docs.route.ts", () -> config.isEnableDocs());
            dataFilter.put("swagger-ui/_index.html", () -> config.isEnableDocs());
            dataFilter.put("swagger-ui/images/throbber.gif", () -> config.isEnableDocs());
            dataFilter.put("swagger-ui/config/resource.json", () -> config.isEnableDocs());
            dataFilter.put("swagger-ui/config/ui.json", () -> config.isEnableDocs());

            //SCSS
            dataFilter.put("_bootstrap-variables.scss", () -> config.isUseSass());
            dataFilter.put("global.scss", () -> config.isUseSass());
            dataFilter.put("vendor.scss", () -> config.isUseSass());
            dataFilter.put("rtl.scss", () -> config.isUseSass() && config.isEnableI18nRTL());
            dataFilter.put("page-ribbon.scss", () -> config.isEnableProfile() && config.isUseSass());
            dataFilter.put("navbar.scss", () -> config.isUseSass());
            dataFilter.put("home.scss", () -> config.isUseSass());
            dataFilter.put("password-strength-bar.scss", () -> config.isUseSass() && !OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));

            //css
            dataFilter.put("documentation.css", () -> !config.isUseSass());
            dataFilter.put("global.css", () -> !config.isUseSass());
            dataFilter.put("vendor.css", () -> !config.isUseSass());
            dataFilter.put("rtl.css", () -> !config.isUseSass() && config.isEnableI18nRTL());
            dataFilter.put("page-ribbon.css", () -> config.isEnableProfile() && !config.isUseSass());
            dataFilter.put("navbar.css", () -> !config.isUseSass());
            dataFilter.put("home.css", () -> !config.isUseSass());
            dataFilter.put("password-strength-bar.css", () -> !config.isUseSass() && !OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));

            //Profile
            dataFilter.put("profile.service.ts", () -> config.isEnableProfile());//layouts/profiles
            dataFilter.put("profile-info.model.ts", () -> config.isEnableProfile());//layouts/profiles
            dataFilter.put("page-ribbon.component.ts", () -> config.isEnableProfile());//layouts/profiles
            
            //Skip UserManagement
            dataFilter.put("user-management-detail.component.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-detail.component.html", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-dialog.component.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-dialog.component.html", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-delete-dialog.component.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-delete-dialog.component.html", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.component.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.component.html", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.route.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-modal.service.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-route-access-service.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user.model.ts", () -> !config.isSkipUserManagement() || OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("user.service.ts", () -> !config.isSkipUserManagement() || OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("user-management-detail.component.spec.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-dialog.component.spec.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-delete-dialog.component.spec.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.component.spec.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.json", () -> !config.isSkipUserManagement());
        }
        return dataFilter;
    }

}
