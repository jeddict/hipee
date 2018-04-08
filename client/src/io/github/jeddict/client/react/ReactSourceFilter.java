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
package io.github.jeddict.client.react;

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

public class ReactSourceFilter extends ApplicationSourceFilter {

    private Map<String, Supplier<Boolean>> dataFilter;

    public ReactSourceFilter(BaseApplicationConfig config) {
        super(config);
    }

    @Override
    protected Map<String, Supplier<Boolean>> getGeneratorFilter() {
        if (dataFilter == null) {
            dataFilter = new HashMap<>();

            //AuthenticationType
//            !oauth2
//                                { file: 'modules/login/login.tsx', method: 'processJsx' },
//                { file: 'modules/login/login-modal.tsx', method: 'processJsx' }
//            session
//  { file: 'modules/account/sessions/sessions.tsx', method: 'processJsx' },
//                { file: 'modules/account/sessions/sessions.reducer.ts', method: 'processJsx' }
//oauth2
//'shared/util/url-utils.ts'
//app/modules/account/sessions/sessions.reducer.ts
//app/modules/account/sessions/sessions.tsx
            
            dataFilter.put("auth-oauth2.service.ts", () -> OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("base64.service.ts", () -> OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth-jwt.service.ts", () -> JWT_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()) || UAA_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth.interceptor.ts", () -> OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()) || JWT_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()) || UAA_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth-session.service.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));

            dataFilter.put("session.model.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.service.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.route.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.component.html", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.component.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.json", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.component.spec.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));

            
            //ApplicationType
//              { file: 'modules/administration/gateway/gateway.tsx', method: 'processJsx' }
            dataFilter.put("gateway.component.html", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway.component.ts", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway.route.ts", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway-route.model.ts", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway-routes.service.ts", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            dataFilter.put("gateway.json", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));

            //Social Login
            dataFilter.put("social-register.component.html", () -> config.isEnableSocialSignIn());
            dataFilter.put("social-register.component.ts", () -> config.isEnableSocialSignIn());
            dataFilter.put("social-auth.component.ts", () -> config.isEnableSocialSignIn() && JWT_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("social.route.ts", () -> config.isEnableSocialSignIn());
            dataFilter.put("social.service.ts", () -> config.isEnableSocialSignIn());
            dataFilter.put("social.component.ts", () -> config.isEnableSocialSignIn());
            dataFilter.put("social.component.html", () -> config.isEnableSocialSignIn());
            dataFilter.put("social.json", () -> config.isEnableSocialSignIn());  

            //WebSocket
//            websocket-middleware.ts
//   { file: 'modules/administration/tracker/tracker.tsx', method: 'processJsx' }
            dataFilter.put("tracker.component.html", () -> WEBSOCKET.equals(config.getWebsocket()));
            dataFilter.put("tracker.component.ts", () -> WEBSOCKET.equals(config.getWebsocket()));
            dataFilter.put("tracker.route.ts", () -> WEBSOCKET.equals(config.getWebsocket()));
            dataFilter.put("tracker.service.ts", () -> WEBSOCKET.equals(config.getWebsocket()));
            dataFilter.put("window.service.ts", () -> WEBSOCKET.equals(config.getWebsocket()));
            dataFilter.put("tracker.json", () -> WEBSOCKET.equals(config.getWebsocket()));
            dataFilter.put("mock-tracker.service.ts", () -> WEBSOCKET.equals(config.getWebsocket()));
            dataFilter.put("websocket-middleware.ts", () -> WEBSOCKET.equals(config.getWebsocket()));

            
            
            
            //Language
//            translation.ts
// 'shared/reducers/locale.ts'
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

            //Metrics
            dataFilter.put("metrics-modal.tsx", () -> config.isEnableMetrics());
            dataFilter.put("metrics.tsx", () -> config.isEnableMetrics());
            
            dataFilter.put("metrics.component.ts", () -> config.isEnableMetrics());
            dataFilter.put("metrics-modal.component.ts", () -> config.isEnableMetrics());
            dataFilter.put("metrics.service.ts", () -> config.isEnableMetrics());
            dataFilter.put("metrics.component.html", () -> config.isEnableMetrics());
            dataFilter.put("metrics-modal.component.html", () -> config.isEnableMetrics());
            dataFilter.put("metrics.route.ts", () -> config.isEnableMetrics());
            dataFilter.put("metrics.json", () -> config.isEnableMetrics());

            //Logs
            dataFilter.put("logs.tsx", () -> config.isEnableLogs());
            
            dataFilter.put("logs.component.ts", () -> config.isEnableLogs());
            dataFilter.put("log.model.ts", () -> config.isEnableLogs());
            dataFilter.put("logs.service.ts", () -> config.isEnableLogs());
            dataFilter.put("logs.component.html", () -> config.isEnableLogs());
            dataFilter.put("logs.route.ts", () -> config.isEnableLogs());
            dataFilter.put("logs.json", () -> config.isEnableLogs());

            //Health
            dataFilter.put("health-modal.tsx", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.tsx", () -> config.isEnableHealth());//admin/health
            
            dataFilter.put("health.component.ts", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health-modal.component.ts", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.service.ts", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.component.html", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health-modal.component.html", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.route.ts", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.json", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.component.spec.ts", () -> config.isEnableHealth());//test
            
            

            //Configuration
            dataFilter.put("configuration.component.ts", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.component.html", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.route.ts", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.service.ts", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.json", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.tsx", () -> config.isEnableConfiguration());

            //Audit
            dataFilter.put("audit-data.model.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audit.model.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.component.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.component.html", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.route.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.service.ts", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.json", () -> config.isEnableAudits());//admin/audits/
            dataFilter.put("audits.component.spec.ts", () -> config.isEnableAudits());
            dataFilter.put("audits.tsx", () -> config.isEnableAudits());


            //Docs
            dataFilter.put("docs.tsx", () -> config.isEnableDocs());
            
            dataFilter.put("docs.component.ts", () -> config.isEnableDocs());
            dataFilter.put("docs.component.html", () -> config.isEnableDocs());
            dataFilter.put("docs.route.ts", () -> config.isEnableDocs());
            dataFilter.put("swagger-ui/_index.html", () -> config.isEnableDocs());
            dataFilter.put("swagger-ui/images/throbber.gif", () -> config.isEnableDocs());
            dataFilter.put("swagger-ui/config/resource.json", () -> config.isEnableDocs());
            dataFilter.put("swagger-ui/config/ui.json", () -> config.isEnableDocs());

            //SCSS
//            postcss.config.js
//   'app.scss',
//                '_bootstrap-variables.scss',
//'modules/home/home.scss',
//    'shared/layout/header/header.scss',
//                'shared/layout/footer/footer.scss',
//                'shared/layout/password/password-strength-bar.scss'
            
            dataFilter.put("_bootstrap-variables.scss", () -> config.isUseSass());
            dataFilter.put("app.scss", () -> config.isUseSass());
            dataFilter.put("home.scss", () -> config.isUseSass());
            dataFilter.put("header.scss", () -> config.isUseSass());
            dataFilter.put("footer.scss", () -> config.isUseSass());
            
            dataFilter.put("content/scss/_global.scss", () -> config.isUseSass());
            dataFilter.put("content/scss/_vendor.scss", () -> config.isUseSass());
            dataFilter.put("content/scss/_rtl.scss", () -> config.isUseSass() && config.isEnableI18nRTL());
            dataFilter.put("app/layouts/profiles/_page-ribbon.scss", () -> config.isEnableProfile() && config.isUseSass());
            dataFilter.put("app/layouts/navbar/_navbar.scss", () -> config.isUseSass());
            dataFilter.put("app/account/password/_password-strength-bar.scss", () -> config.isUseSass());

            //css
//              'app.css'
//  'modules/home/home.css',
// 'shared/layout/header/header.css',
//                'shared/layout/footer/footer.css',
//                'shared/layout/password/password-strength-bar.css'
            dataFilter.put("app.css", () -> !config.isUseSass());
            dataFilter.put("home.css", () -> !config.isUseSass());
            dataFilter.put("header.css", () -> !config.isUseSass());
            dataFilter.put("footer.css", () -> !config.isUseSass());
            
            dataFilter.put("content/css/_documentation.css", () -> !config.isUseSass());
            dataFilter.put("content/css/_global.css", () -> !config.isUseSass());
            dataFilter.put("content/css/_vendor.css", () -> !config.isUseSass());
            dataFilter.put("content/css/_rtl.css", () -> !config.isUseSass() && config.isEnableI18nRTL());
            dataFilter.put("app/layouts/profiles/_page-ribbon.css", () -> config.isEnableProfile() && !config.isUseSass());
            dataFilter.put("app/layouts/navbar/_navbar.css", () -> !config.isUseSass());
            dataFilter.put("app/account/password/_password-strength-bar.css", () -> !config.isUseSass());

            //Profile
            dataFilter.put("profile.service.ts", () -> config.isEnableProfile());//layouts/profiles
            dataFilter.put("profile-info.model.ts", () -> config.isEnableProfile());//layouts/profiles
            dataFilter.put("page-ribbon.component.ts", () -> config.isEnableProfile());//layouts/profiles
            
            //Search Engine
//            dataFilter.put("entity-search.service.ts", () -> ELASTIC_SEARCH_ENGINE.equals(config.getSearchEngine()));

            //Skip UserManagement
//                          { file: 'modules/administration/user-management/index.tsx', method: 'processJsx' },
//                { file: 'modules/administration/user-management/user-management.tsx', method: 'processJsx' },
//                { file: 'modules/administration/user-management/user-management-dialog.tsx', method: 'processJsx' },
//                { file: 'modules/administration/user-management/user-management-detail.tsx', method: 'processJsx' },
//                { file: 'modules/administration/user-management/user-management-delete-dialog.tsx', method: 'processJsx' },
//                'modules/administration/user-management/user-management.reducer.ts'
// 'spec/app/modules/administration/user-management/user-management.reducer.spec.ts'
            dataFilter.put("user-management.component.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-detail.component.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-dialog.component.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-delete-dialog.component.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user.model.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user.service.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.state.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.component.html", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-detail.component.html", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-dialog.component.html", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-delete-dialog.component.html", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.json", () -> !config.isSkipUserManagement());
        }
        return dataFilter;
    }

}
