/**
 * Copyright [2017] Gaurav Gupta
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
package org.netbeans.jcode.ng.main.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.netbeans.jcode.core.util.StringHelper.camelCase;
import static org.netbeans.jcode.core.util.StringHelper.firstUpper;
import static org.netbeans.jcode.core.util.StringHelper.kebabCase;
import org.netbeans.jcode.i18n.Language;

/**
 *
 * @author jGauravGupta
 */
public abstract class NGApplicationConfig {

    public static final String JWT_AUTHENTICATION_TYPE = "jwt";
    public static final String UAA_AUTHENTICATION_TYPE = "uaa";
    public static final String SESSION_AUTHENTICATION_TYPE = "session";
    public static final String OAUTH2_AUTHENTICATION_TYPE = "oauth2";

    public static final String MICROSERVICE_APPLICATION_TYPE = "microservice";
    public static final String UAA_APPLICATION_TYPE = "uaa";
    public static final String GATEWAY_APPLICATION_TYPE = "gateway";
    public static final String MONOLITH_APPLICATION_TYPE = "monolith";

    public static final String SPRING_WEBSOCKET = "spring-websocket";

    public static final String ELASTIC_SEARCH_ENGINE = "elasticsearch";
    public static final String SQL_DATABASE_TYPE = "sql";
    public static final String CASSANDRA_DATABASE_TYPE = "cassandra";
    public static final String MONGODB_DATABASE_TYPE = "mongodb";
    public static final String H2_DISK_DATABASE_TYPE = "h2Disk";
    public static final String H2_MEMORY_DATABASE_TYPE = "h2Memory";

    //Path
    public final String MAIN_DIR = "src/main/";
    public final String TEST_DIR = "src/test/";
    public final String CLIENT_MAIN_SRC_DIR = MAIN_DIR + "webapp/";
    public final String CLIENT_TEST_SRC_DIR = TEST_DIR + "javascript/";
    public final String MAIN_SRC_DIR = CLIENT_MAIN_SRC_DIR;
    public final String TEST_SRC_DIR = CLIENT_TEST_SRC_DIR;
    public final String CLIENT_DIST_DIR = "www/";
    public final String ANGULAR_DIR = MAIN_DIR + "webapp/app/";
    public final String BUILD_DIR;
    public final String DIST_DIR;

    private String applicationPath;//rest path
    private String buildTool;
    private String clientPackageManager;
    private String applicationType;//gateway , monolith

    protected String baseName;
    private String capitalizedBaseName;
    private String camelizedBaseName;
    private String dasherizedBaseName;
    private String lowercaseBaseName;
    private List<NGEntity> entities;
    private String restPackage;
    private String authenticationType;

    //i18n
    private boolean enableTranslation;
    private String nativeLanguage;
    private Set<String> languages;
    private Set<Language> languageInstances;
    private boolean enableI18nRTL;

    private String jhiPrefix;
    private String jhiPrefixCapitalized;

    //Persistence
    private String searchEngine;
    private String databaseType;
    private String devDatabaseType;
    private String hibernateCache;//ehcache2

    private String websocket;
    private String messageBroker;//kafka
    private String clientFramework;// angular1
    private boolean useSass;

    //test
    private String[] testFrameworks = {};//protractor
    private boolean protractorTests;

    //filter    
    private boolean enableSocialSignIn;
    private boolean skipUserManagement;
    private boolean skipClient;
    private boolean skipServer;

    private boolean enableMetrics;
    private boolean enableLogs;
    private boolean enableHealth;
    private boolean enableConfiguration;
    private boolean enableAudits;
    private boolean enableProfile;
    private boolean enableDocs;
    
    private Map<String,List<String>> enumTypes = new HashMap<>();

    public NGApplicationConfig(String baseName, String buildTool) {
        this.baseName = baseName;
        this.buildTool = buildTool;
        if ("maven".equals(buildTool)) {
            BUILD_DIR = "target/";
        } else {
            BUILD_DIR = "build/";
        }
        DIST_DIR = BUILD_DIR + CLIENT_DIST_DIR;
    }

    /**
     * @return the jhiPrefixCapitalized
     */
    public String getJhiPrefixCapitalized() {
        if (jhiPrefixCapitalized == null) {
            jhiPrefixCapitalized = firstUpper(getJhiPrefix());
        }
        return jhiPrefixCapitalized;
    }

    /**
     * @return the authenticationType
     */
    public String getAuthenticationType() {
        if (authenticationType == null) {
            authenticationType = "jwt";
        }
        return authenticationType;
    }

    /**
     * @param authenticationType the authenticationType to set
     */
    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    /**
     * @return the applicationType
     */
    public String getApplicationType() {
        if (applicationType == null) {
            applicationType = MONOLITH_APPLICATION_TYPE;
        }
        return applicationType;
    }

    /**
     * @return the enableTranslation
     */
    public boolean isEnableTranslation() {
        return enableTranslation;
    }

    /**
     * @param enableTranslation the enableTranslation to set
     */
    public void setEnableTranslation(boolean enableTranslation) {
        this.enableTranslation = enableTranslation;
    }

    /**
     * @return the enableSocialSignIn
     */
    public boolean isEnableSocialSignIn() {
        return enableSocialSignIn;
    }

    /**
     * @return the enableSocialSignIn
     */
    public void setEnableSocialSignIn(boolean enableSocialSignIn) {
        this.enableSocialSignIn = enableSocialSignIn;
    }

    /**
     * @return the websocket
     */
    public String getWebsocket() {
        return websocket;
    }

    /**
     * @param websocket the websocket to set
     */
    public void setWebsocket(String websocket) {
        this.websocket = websocket;
    }

    /**
     * @return the searchEngine
     */
    public String getSearchEngine() {
        return searchEngine;
    }

    /**
     * @param searchEngine the searchEngine to set
     */
    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    /**
     * @return the databaseType
     */
    public String getDatabaseType() {
        if (databaseType == null) {
            databaseType = SQL_DATABASE_TYPE;
        }
        return databaseType;
    }

    /**
     * @param databaseType the databaseType to set
     */
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * @return the jhiPrefix
     */
    public String getJhiPrefix() {
        if (jhiPrefix == null) {
            jhiPrefix = EMPTY;
        }
        return jhiPrefix;
    }

    /**
     * @param jhiPrefix the jhiPrefix to set
     */
    public void setJhiPrefix(String jhiPrefix) {
        this.jhiPrefix = jhiPrefix;
    }

    /**
     * @return the devDatabaseType
     */
    public String getDevDatabaseType() {
        return devDatabaseType;
    }

    /**
     * @param devDatabaseType the devDatabaseType to set
     */
    public void setDevDatabaseType(String devDatabaseType) {
        this.devDatabaseType = devDatabaseType;
    }

    /**
     * @return the baseName
     */
    public String getBaseName() {
        if (baseName == null) {
            baseName = EMPTY;
        }
        return baseName;
    }

    /**
     * @return the capitalizedBaseName
     */
    public String getCapitalizedBaseName() {
        if (capitalizedBaseName == null) {
            capitalizedBaseName = firstUpper(getBaseName());
        }
        return capitalizedBaseName;
    }

    /**
     * @return the camelizedBaseName
     */
    public String getCamelizedBaseName() {
        if (camelizedBaseName == null) {
            camelizedBaseName = camelCase(baseName);
        }
        return camelizedBaseName;
    }

    public boolean isSkipUserManagement() {
        return skipUserManagement;
    }

    /**
     * @return the dasherizedBaseName
     */
    public String getDasherizedBaseName() {
        if (dasherizedBaseName == null) {
            dasherizedBaseName = kebabCase(baseName);
        }
        return dasherizedBaseName;
    }

    /**
     * @return the lowercaseBaseName
     */
    public String getLowercaseBaseName() {
        if (lowercaseBaseName == null) {
            lowercaseBaseName = baseName.toLowerCase();
        }
        return lowercaseBaseName;
    }

    /**
     * @return the useSass
     */
    public boolean isUseSass() {
        return useSass;
    }

    /**
     * @param useSass the useSass to set
     */
    public void setUseSass(boolean useSass) {
        this.useSass = useSass;
    }

    /**
     * @return the hibernateCache
     */
    public String getHibernateCache() {
        return hibernateCache;
    }

    /**
     * @param hibernateCache the hibernateCache to set
     */
    public void setHibernateCache(String hibernateCache) {
        this.hibernateCache = hibernateCache;
    }

    /**
     * @return the languages
     */
    public Set<String> getLanguages() {
        if (languages == null) {
            languages = new HashSet<>();
        }
        return languages;
    }

    /**
     * @param languages the languages to set
     */
    public void setLanguages(Set<String> languages) {
        this.languages = new HashSet<>(languages);
        this.languages.add(getNativeLanguage());
    }

    /**
     * @return the languageInstances
     */
    public Set<Language> getLanguageInstances() {
        return languageInstances;
    }

    /**
     * @param languageInstances the languageInstances to set
     */
    public void setLanguageInstances(Set<Language> languageInstances) {
        this.languageInstances = languageInstances;
    }
    
    /**
     * @return the enableI18nRTL
     */
    public boolean isEnableI18nRTL() {
        return enableI18nRTL;
    }

    /**
     * @param enableI18nRTL the enableI18nRTL to set
     */
    public void setEnableI18nRTL(boolean enableI18nRTL) {
        this.enableI18nRTL = enableI18nRTL;
    }

    /**
     * @return the applicationPath
     */
    public String getApplicationPath() {
        return applicationPath;
    }

    /**
     * @param applicationPath the applicationPath to set
     */
    public void setApplicationPath(String applicationPath) {
        this.applicationPath = applicationPath;
    }

    /**
     * @return the entities
     */
    public List<NGEntity> getEntities() {
        return entities;
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities(List<NGEntity> entities) {
        this.entities = entities;
    }

    /**
     * @return the enableMetrics
     */
    public boolean isEnableMetrics() {
        return enableMetrics;
    }

    /**
     * @param enableMetrics the enableMetrics to set
     */
    public void setEnableMetrics(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    /**
     * @return the enableLogs
     */
    public boolean isEnableLogs() {
        return enableLogs;
    }

    /**
     * @param enableLogs the enableLogs to set
     */
    public void setEnableLogs(boolean enableLogs) {
        this.enableLogs = enableLogs;
    }

    /**
     * @return the enableHealth
     */
    public boolean isEnableHealth() {
        return enableHealth;
    }

    /**
     * @param enableHealth the enableHealth to set
     */
    public void setEnableHealth(boolean enableHealth) {
        this.enableHealth = enableHealth;
    }

    /**
     * @return the enableConfiguration
     */
    public boolean isEnableConfiguration() {
        return enableConfiguration;
    }

    /**
     * @param enableConfiguration the enableConfiguration to set
     */
    public void setEnableConfiguration(boolean enableConfiguration) {
        this.enableConfiguration = enableConfiguration;
    }

    /**
     * @return the enableAudits
     */
    public boolean isEnableAudits() {
        return enableAudits;
    }

    /**
     * @param enableAudits the enableAudits to set
     */
    public void setEnableAudits(boolean enableAudits) {
        this.enableAudits = enableAudits;
    }

    /**
     * @return the enableProfile
     */
    public boolean isEnableProfile() {
        return enableProfile;
    }

    /**
     * @param enableProfile the enableProfile to set
     */
    public void setEnableProfile(boolean enableProfile) {
        this.enableProfile = enableProfile;
    }

    /**
     * @return the enableDocs
     */
    public boolean isEnableDocs() {
        return enableDocs;
    }

    /**
     * @param enableDocs the enableDocs to set
     */
    public void setEnableDocs(boolean enableDocs) {
        this.enableDocs = enableDocs;
    }

    /**
     * @return the restPackage
     */
    public String getRestPackage() {
        return restPackage;
    }

    /**
     * @param restPackage the restPackage to set
     */
    public void setRestPackage(String restPackage) {
        this.restPackage = restPackage;
    }

    /**
     * @return the messageBroker
     */
    public String getMessageBroker() {
        return messageBroker;
    }

    /**
     * @param messageBroker the messageBroker to set
     */
    public void setMessageBroker(String messageBroker) {
        this.messageBroker = messageBroker;
    }

    /**
     * @return the clientFramework
     */
    public String getClientFramework() {
        return clientFramework;
    }

    /**
     * @param clientFramework the clientFramework to set
     */
    public void setClientFramework(String clientFramework) {
        this.clientFramework = clientFramework;
    }

    public String getBuildTool() {
        return buildTool;
    }

    /**
     * @return the protractorTests
     */
    public boolean isProtractorTests() {
        return protractorTests;
    }

    /**
     * @param protractorTests the protractorTests to set
     */
    public void setProtractorTests(boolean protractorTests) {
        this.protractorTests = protractorTests;
    }

    /**
     * @return the clientPackageManager
     */
    public String getClientPackageManager() {
        return clientPackageManager;
    }

    /**
     * @param clientPackageManager the clientPackageManager to set
     */
    public void setClientPackageManager(String clientPackageManager) {
        this.clientPackageManager = clientPackageManager;
    }

    /**
     * @return the skipClient
     */
    public boolean isSkipClient() {
        return skipClient;
    }

    /**
     * @param skipClient the skipClient to set
     */
    public void setSkipClient(boolean skipClient) {
        this.skipClient = skipClient;
    }

    /**
     * @return the skipServer
     */
    public boolean isSkipServer() {
        return skipServer;
    }

    /**
     * @param skipServer the skipServer to set
     */
    public void setSkipServer(boolean skipServer) {
        this.skipServer = skipServer;
    }

    public String getSrcDir() {
        return MAIN_SRC_DIR;
    }

    public String getTestDir() {
        return TEST_SRC_DIR;
    }

    public String getBuildDir() {
        return BUILD_DIR;
    }

    public String getDistDir() {
        return DIST_DIR;
    }

    /**
     * @return the nativeLanguage
     */
    public String getNativeLanguage() {
        return nativeLanguage;
    }

    /**
     * @param nativeLanguage the nativeLanguage to set
     */
    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    /**
     * @return the testFrameworks
     */
    public String[] getTestFrameworks() {
        return testFrameworks;
    }

    /**
     * @return the angularAppName
     */
    public abstract String getAngularAppName();

    /**
     * @return the enumTypes
     */
    public Map<String,List<String>> getEnumTypes() {
        return enumTypes;
    }
    
    public void addEnumType(String enumType, List<String> varTypes){
        enumTypes.put(enumType, varTypes);
    }

}
