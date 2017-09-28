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
package org.jcode.cloud.generator;

import java.io.IOException;
import java.util.Map;
import org.jcode.docker.generator.DockerGenerator;
import org.netbeans.jcode.console.Console;
import static org.netbeans.jcode.console.Console.BOLD;
import static org.netbeans.jcode.console.Console.FG_RED;
import static org.netbeans.jcode.console.Console.UNDERLINE;
import static org.netbeans.jcode.core.util.FileUtil.expandTemplate;
import org.netbeans.jcode.core.util.JavaSourceHelper;
import org.netbeans.jcode.layer.ConfigData;
import org.netbeans.jcode.layer.Generator;
import org.netbeans.jcode.layer.Technology;
import org.netbeans.jcode.parser.ejs.EJSParser;
import static org.netbeans.jcode.parser.ejs.EJSUtil.copyDynamicFile;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Gaurav Gupta
 */
@ServiceProvider(service = Generator.class)
@Technology(label = "Cloud", panel = CloudConfigPanel.class, entityGenerator = false,
        tabIndex = 2, sibling = {DockerGenerator.class})
public final class CloudGenerator extends DockerGenerator implements Generator {

    private static final String TEMPLATE = "org/jcode/cloud/template/";

    private static final String KUBERNETES_TEMPLATE = TEMPLATE + "kubernetes/";
    private static final String OPENSHIFT_TEMPLATE = TEMPLATE + "openshift/";

    @ConfigData
    private CloudConfigData cloudConfig;

    @Override
    public void execute() throws IOException {
        if (!appConfigData.isCompleteApplication()) {
            return;
        }
        generateKubernetes();
        generateOpenshift();
    }

    private void generateKubernetes() {
        if (config.isDockerActivated() && cloudConfig.getKubernetesConfigData().isEnabled()) {
            try {
                boolean generateNamespace = !"default".equals(cloudConfig.getKubernetesConfigData().getNamespace());
                boolean generateIngress = "ClusterIP".equals(cloudConfig.getKubernetesConfigData().getServiceType()); // and 'gateway' or 'monolith'
                String applicationWithNS = getApplicationName() + (generateNamespace ? " --namespace " + cloudConfig.getKubernetesConfigData().getNamespace() : "");
                handler.info("Kubernetes",
                          "Use this command to start minikube:\n"
                        + "\t\t " + Console.wrap("minikube start", BOLD)
                        + "\n\t\t"
                        + "Use this command to be able to work with the docker daemon:\n"
                        + "\t\t " + Console.wrap("eval $(minikube docker-env)", BOLD)
                        + "\n\t\t"
                        + "You can deploy all your apps by running:\n"
                        + (generateNamespace ? "\t\t " + Console.wrap("kubectl apply -f k8s/namespace.yml", BOLD) + "\n" : "")
                        + "\t\t " + Console.wrap("kubectl apply -f k8s/" + getApplicationName(), BOLD) + "\n"
                        + "\n\t\t"
                        + "Use this command to find your application's IP addresses:\n"
                        + "\t\t " + Console.wrap("kubectl get svc " + applicationWithNS, BOLD)
                        + "\n\t\t"
                        + "Use this command to open your application in browser:\n"
                        + "\t\t " + Console.wrap("minikube service " + applicationWithNS, BOLD)
                );

                handler.progress(Console.wrap(CloudGenerator.class, "MSG_Progress_Kubernetes_Generating", FG_RED, BOLD, UNDERLINE));
                FileObject targetFolder = project.getProjectDirectory().getFileObject("k8s");
                if (targetFolder == null) {
                    targetFolder = project.getProjectDirectory().createFolder("k8s");
                }
                Map<String, Object> params = getParams();
                params.put("APP_SERVER_PORT", "8080");
                params.put("INGRESS_DOMAIN", cloudConfig.getKubernetesConfigData().getIngressDomain());
                params.put("K8S_SVC_TYPE", cloudConfig.getKubernetesConfigData().getServiceType());
                params.put("K8S_NS", cloudConfig.getKubernetesConfigData().getNamespace());
                params.put("DOCKER_IMAGE", config.getDockerNamespace().replace("${project.groupId}", getPOMManager().getGroupId())
                        + "/" + getApplicationName()
                        + ":" + getPOMManager().getVersion());
                params.put("APP_NAME", getApplicationName());

                if (generateIngress) {
                    handler.progress(expandTemplate(KUBERNETES_TEMPLATE + "_ingress.yml.ftl", targetFolder,
                            getApplicationName() + "/" + getApplicationName() + "_ingress.yml", params));
                }
                if (generateNamespace) {
                    handler.progress(expandTemplate(KUBERNETES_TEMPLATE + "_namespace.yml.ftl", targetFolder,
                            "namespace.yml", params));
                }
                handler.progress(expandTemplate(KUBERNETES_TEMPLATE + "db/_" + config.getDatabaseType().name().toLowerCase() + ".yml.ftl", targetFolder,
                        getApplicationName() + "/" + getApplicationName() + "_" + config.getDatabaseType().name().toLowerCase() + ".yml", params));
                handler.progress(expandTemplate(KUBERNETES_TEMPLATE + "_deployment.yml.ftl", targetFolder,
                        getApplicationName() + "/" + getApplicationName() + "_deployment.yml", params));
                handler.progress(expandTemplate(KUBERNETES_TEMPLATE + "_service.yml.ftl", targetFolder,
                        getApplicationName() + "/" + getApplicationName() + "_service.yml", params));

            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private void generateOpenshift() {
        if (config.isDockerActivated() && cloudConfig.getOpenshiftConfigData().isEnabled()) {
            try {
                String applicationPath = getApplicationName() + "/" + getApplicationName();
                String dbType = config.getDatabaseType().getDockerImage().toLowerCase();
                
                handler.info("OpenShift",
                        "You can deploy all your apps by running:\n"
                        + "\t\t " + Console.wrap("ocp/ocp-apply.sh", BOLD) + "\n"
                        + "\n\t\t"
                        + "Use this command to find your application's IP addresses:\n"
                        + "\t\t " + Console.wrap("oc get svc " + getApplicationName(), BOLD)
                );

                Map<String, Object> params = getParams();
                params.put("targetImageName", config.getDockerNamespace().replace("${project.groupId}", getPOMManager().getGroupId())
                        + "/" + getApplicationName()
                        + ":" + getPOMManager().getVersion());
                params.put("baseName", getApplicationName());
                params.put("DOCKER_" + config.getDatabaseType().name(), config.getDatabaseType().getDockerImage()+":"+config.getDatabaseVersion());
                params.put("openshiftNamespace", cloudConfig.getOpenshiftConfigData().getNamespace());
                params.put("serverPort", "8080");
                params.put("prodDatabaseType", dbType);
                params.put("createdBy", JavaSourceHelper.getAuthor());
                params.put("storageType", cloudConfig.getOpenshiftConfigData().getDatabaseStorageType());
                
                EJSParser parser = new EJSParser();
                parser.addContext(params);
                FileObject targetFolder = project.getProjectDirectory().getFileObject("ocp");
                if (targetFolder == null) {
                    targetFolder = project.getProjectDirectory().createFolder("ocp");
                }
                
                copyDynamicFile(parser.getParserManager(), 
                        OPENSHIFT_TEMPLATE + "_apply.sh", 
                        targetFolder, "ocp-apply.sh", handler);
                copyDynamicFile(parser.getParserManager(), 
                        OPENSHIFT_TEMPLATE + "scc/_scc-config.yml", 
                        targetFolder, "registry/scc-config.yml", handler);
                copyDynamicFile(parser.getParserManager(), 
                        OPENSHIFT_TEMPLATE + "_deployment.yml", 
                        targetFolder, applicationPath + "-deployment.yml", handler);
                copyDynamicFile(parser.getParserManager(),
                        OPENSHIFT_TEMPLATE + "db/_" + dbType + ".yml",
                        targetFolder, applicationPath + "-" + dbType + ".yml", handler);

            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

}
