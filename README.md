# Infor-Data POC Spring Boot Application using oracle 18c Database

## Applicaiton workflow
![Screenshot](https://github.com/Sherpa99/gs-infordata-poc/blob/master/docs/images/WorkFlowDiagram.png)
### Spring_Boot Base RESTApi build based on lasted feature as mentioned
* SpringBoot Version : 2.3.0.RELEASE
* Java Version: 1.8
* JPA Repository
  * No-Code Repositories
  * Reduced boilerplate code
  * Generated Queries

### Infrastrucure:
* IBM Virtual Private Cloud
* Database hosted in us-south in VSI - Vitual Server Instance based in Dallas
* Application hosted in eu-de region on OCP - OpenShift Container Platform based in Frankfort

### Data Storage/Repository
* Oracle Database 
* ICR Image repository
* Github


### Oracle Database 

* Oracle JDBC Connection String
```console
spring.datasource.url=jdbc:oracle:thin:@//${ORACLE_SERVICE_HOST}:${ORACLE_SERVICE_PORT}/xepdb1
spring.datasource.username=${ORACLE_USERNAME}
spring.datasource.password=${ORACLE_PASSWORD}
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.Oracle10gDialect
spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
```

### YAML Codes for creating K8S different objects:

* Deployment yaml
```console

kind: Deployment
apiVersion: apps/v1
metadata:
  name: infordata-poc-stagig
  namespace: dev
  labels:
    app: infordata-poc-staging
spec:
  replicas: 1
  selector:
    matchLabels:
      app: infordata-poc-staging
  template:
    metadata:
      labels:
        app: infordata-poc-staging
    spec:
      containers:
        - name: infordata-staging
          image: de.icr.io/infordata_poc_ir/infordata-gs-poc:v1
          env:
          - name: ORACLE_USERNAME
            valueFrom:
              secretKeyRef:
                name: oracle
                key: ORA_USER
          - name: ORACLE_PASSWORD
            valueFrom:
              secretKeyRef:
                name: oracle
                key: ORA_PASSWORD
      restartPolicy: Always
      terminationGracePeriodSeconds: 30
      dnsPolicy: ClusterFirst
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 25%
      maxSurge: 25%
  revisionHistoryLimit: 10
  progressDeadlineSeconds: 600

```
* Create deployment
```console
oc apply -f deployment.yaml
```
* Create application service
```console
oc expose deployment infordata-staging --type="NodePort" --port=8080
```
* Create application route (Load Balancer)
```console
oc expose svc/infordata-staging
```
* DB Secret
```console
apiVersion: v1
kind: Secret
metadata:
  name: oracle
  namespace: dev
type: Opaque
data:
  ORA_USER: <encripted user name>
  ORA_PASSWORD: <encripted password>
```
* Create deployment
```console
oc apply -f oradb-secret.yaml
```
* DB Service
```console
kind: Service
apiVersion: v1
metadata:
  name: oracle
spec:
  ports:
    - port: 1521
      targetPort: 1539
```
* Create deployment
```console
oc apply -f svcoradb.yaml
```
* DB Endpoint
```console
kind: Endpoints
apiVersion: v1
metadata:
  name: oracle
subsets:
  - addresses:
      - ip: 192.168.0.9
    ports:
      - port: 1539
```
* Create deployment
```console
oc apply -f eporadb.yaml
```

### REST EndPoints fetching data from a table sample oracle database

1) Fetch total record count
```console
curl 'infordata-poc-stagig-dev.infordata-poc-cluster-2bef1f4b4097001da9502000c44fc2b2-0000.eu-de.containers.appdomain.cloud/id/customers/count'
```
Expected out put: 319

2) Fetch the record by id
```console
curl 'infordata-poc-stagig-dev.infordata-poc-cluster-2bef1f4b4097001da9502000c44fc2b2-0000.eu-de.containers.appdomain.cloud/id/county/count'
```
Expected output: 25
