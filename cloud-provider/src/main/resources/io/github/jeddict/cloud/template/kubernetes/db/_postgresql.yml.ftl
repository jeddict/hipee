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

 Portions Copyright 2013-2019 Gaurav Gupta
-->
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: ${DB_SVC}
  namespace: ${K8S_NS}
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: ${DB_SVC}
    spec:
      volumes:
      - name: data
        emptyDir: {}
      containers:
      - name: ${DB_TYPE}
        image: ${DB_TYPE}:${DB_VERSION}
        env:
        - name: POSTGRES_USER
          value: ${DB_USER}
        - name: POSTGRES_PASSWORD
          value: ${DB_PASSWORD}
        - name: POSTGRES_DB
          value: ${DB_NAME}
        ports:
        - containerPort: ${DB_PORT}
        volumeMounts:
        - name: data
          mountPath: /var/lib/postgresql/
---
apiVersion: v1
kind: Service
metadata:
  name: ${DB_SVC}
  namespace: ${K8S_NS}
spec:
  selector:
    app: ${DB_SVC}
  ports:
  - port: ${DB_PORT}
