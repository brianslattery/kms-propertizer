# KMS Propertizer

Simpe utility to take properties from the environment, and optionally encrypted via AWS KMS, and store them into the IIQ properties file (standard Java properties).

The utility takes an input of a base iiq.properties file and augment it with additional properties. It is capable of running the `iiq encrypt` functionality, and will do so for the `dataSource.password` property.

The original use-case for this design was running IdentityIQ in Docker, and wanting a way to configure IIQ database connectivity with dynamic credentials. The ability to be able to spin up a database, and pass that connection to the container was also desired. 

**Note: You must have an active license and ability to obtain IdentityIQ from SailPoint. Do not ask for any of these files, I will not provide them.**

### propertizer.properties
Configuration with `propertizer.properties` is currently the most complete. It allows for prefixing a property value with meta prefixes to direct the handling.

* `KMS`: The value is encrypted via KMS
* `ENV`: The value should be sourced from the environment
	* ex. `EXPORT MYVAR=my-val!` could be referenced as `myProp=ENV::MYVAR`. The output properties file would show `myProp=my-val!`

#### Basic
~~~~
# Sample Input for KMS Propertizer - basic
dataSource.username=test-username
dataSource.password=test-password
dataSource.url=test-url
~~~~

#### Environment
~~~~
# Sample Input for KMS Propertizer - environment
dataSource.username=ENV::username
dataSource.password=test-password
dataSource.url=test-url
~~~~

#### Environment and KMS
~~~~
# Sample Input for KMS Propertizer - environment
dataSource.username=ENV:KMS::username
dataSource.password=test-password
dataSource.url=test-url
~~~~

### Sample Run Script
Sample using CLI args.

~~~~
java -cp "target/kms-propertizer-0.0.1.jar:target/lib/*:<iiq-root>WEB-INF/lib/*:<iiq-root>/WEB-INF/classes" \
 net.brianjslattery.oss.propertizer.Propertizer \
 -input <path-to-iiq-starter.properties> \
 -input <path-to-iiq.properties> \
 -dbuser <iiq-username> \
 -dbpass <iiq-password>
 ~~~~