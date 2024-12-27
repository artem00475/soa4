FROM bitnami/wildfly:33.0.2
ADD wildfly-33.0.2.Final/standalone/configuration/my-standalone.xml /opt/bitnami/wildfly/standalone/configuration/
ADD wildfly-33.0.2.Final/standalone/configuration/application.keystore /opt/bitnami/wildfly/standalone/configuration/
ADD wildfly-33.0.2.Final/standalone/configuration/application.truststore /opt/bitnami/wildfly/standalone/configuration/
ADD wildfly-33.0.2.Final/bin/my-standalone.sh /opt/bitnami/wildfly/bin/

ADD target/second-service.war /opt/bitnami/wildfly/standalone/deployments/

CMD [ "/opt/bitnami/wildfly/bin/my-standalone.sh", "-c", "my-standalone.xml"]
