<#--
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

 Portions Copyright 2013-2017 Gaurav Gupta
-->
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: ${APP_NAME}
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: ${APP_NAME}
    spec:
      containers:
      - name: ${APP_NAME}-app
        image: ${DOCKER_IMAGE}
        imagePullPolicy: IfNotPresent
        env:
        - name: JAVA_OPTS
          value: " -Xmx256m -Xms256m"
        resources:
          requests:
            memory: "256Mi"
            cpu: "500m"
          limits:
            memory: "512Mi"
            cpu: "1"
        ports:
        - name: web
          containerPort: 8080
<#--    readinessProbe:
          httpGet:
            path: /management/health
            port: web
        livenessProbe:
          httpGet:
            path: /management/health
            port: web
          initialDelaySeconds: 180
-->
