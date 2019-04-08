/**
 * Copyright 2013-2019 the original author or authors from the Jeddict project (https://jeddict.github.io/).
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
import static io.github.jeddict.client.web.main.domain.Constant.GATEWAY_APPLICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.Constant.JWT_AUTHENTICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.Constant.OAUTH2_AUTHENTICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.Constant.SESSION_AUTHENTICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.Constant.UAA_AUTHENTICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.Constant.WEBSOCKET;

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
            
            dataFilter.put("shared/reducers/user-management.ts", () -> OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth-oauth2.service.ts", () -> OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("base64.service.ts", () -> OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth-jwt.service.ts", () -> JWT_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()) || UAA_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth.interceptor.ts", () -> OAUTH2_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()) || JWT_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()) || UAA_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("auth-session.service.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));

            dataFilter.put("sessions.reducer.spec.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.reducer.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("sessions.tsx", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()));
            dataFilter.put("cookie-utils.ts", () -> SESSION_AUTHENTICATION_TYPE.equals(config.getAuthenticationType()) && WEBSOCKET.equals(config.getWebsocket()));
            
            dataFilter.put("gateway.tsx", () -> GATEWAY_APPLICATION_TYPE.equals(config.getApplicationType()));
            
           
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
            dataFilter.put("tracker.tsx", () -> WEBSOCKET.equals(config.getWebsocket()));
            dataFilter.put("tracker.json", () -> WEBSOCKET.equals(config.getWebsocket()));
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
            dataFilter.put("metrics.json", () -> config.isEnableMetrics());

            //Logs
            dataFilter.put("logs.tsx", () -> config.isEnableLogs());
            dataFilter.put("logs.json", () -> config.isEnableLogs());

            //Health
            dataFilter.put("health-modal.tsx", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.tsx", () -> config.isEnableHealth());//admin/health
            dataFilter.put("health.json", () -> config.isEnableHealth());//admin/health

            //Configuration
            dataFilter.put("configuration.tsx", () -> config.isEnableConfiguration());
            dataFilter.put("configuration.json", () -> config.isEnableConfiguration());

            //Audit
            dataFilter.put("audits.tsx", () -> config.isEnableAudits());
            dataFilter.put("audits.json", () -> config.isEnableAudits());//admin/audits/

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
            dataFilter.put("_bootstrap-variables.scss", () -> config.isUseSass());
            dataFilter.put("app.scss", () -> config.isUseSass());
            dataFilter.put("home.scss", () -> config.isUseSass());
            dataFilter.put("rtl.scss", () -> config.isUseSass() && config.isEnableI18nRTL());
            dataFilter.put("header.scss", () -> config.isUseSass());
            dataFilter.put("footer.scss", () -> config.isUseSass());
            dataFilter.put("password-strength-bar.scss", () -> config.isUseSass());

            //css
            dataFilter.put("app.css", () -> !config.isUseSass());
            dataFilter.put("home.css", () -> !config.isUseSass());
            dataFilter.put("rtl.css", () -> !config.isUseSass() && config.isEnableI18nRTL());
            dataFilter.put("header.css", () -> !config.isUseSass());
            dataFilter.put("footer.css", () -> !config.isUseSass());
            dataFilter.put("password-strength-bar.css", () -> !config.isUseSass());

            //Profile
            dataFilter.put("application-profile.spec.ts", () -> config.isEnableProfile());
            dataFilter.put("application-profile.ts", () -> config.isEnableProfile());
            
            //Skip UserManagement
            dataFilter.put("user-management.reducer.spec.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.spec.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-delete-dialog.tsx", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-detail.tsx", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management-update.tsx", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.reducer.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.tsx", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.ts", () -> !config.isSkipUserManagement());
            dataFilter.put("user-management.json", () -> !config.isSkipUserManagement());
        }
        return dataFilter;
    }

}
