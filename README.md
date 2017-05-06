# integrate-activiti-with-spring
In this post, you will learn how to integrate Activiti's engine and REST API into your Spring application. At the same time, you will be able to adapt the Process Engine to your needs by modifying the database connection and the Async Job Executor.

<h2 style="text-align: justify;">Contribute Code</h2>
<p style="text-align: justify;">If you would like to become an active contributor to this project please follow these simple steps:</p>

<ol>
 	<li style="text-align: justify;">Fork it</li>
 	<li style="text-align: justify;">Create your feature branch</li>
 	<li style="text-align: justify;">Commit your changes</li>
 	<li style="text-align: justify;">Push to the branch</li>
 	<li style="text-align: justify;">Create new Pull Request</li>
</ol>

<p class="sect1"><strong>Source code</strong> can be downloaded from <a href="https://github.com/canchito-dev/integrate-activiti-with-spring" target="_blank" rel="noopener noreferrer">github</a>.</p>

<h2 class="sect1">What you’ll need</h2>
<ul>
 	<li>About 20 minutes</li>
 	<li>A favorite IDE</li>
 	<li><a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank" rel="noopener noreferrer">JDK 7</a> or later. It can be made to work with JDK6, but it will need configuration tweaks. Please check the Spring Boot documentation</li>
 	<li>An empty Spring project. You can follow the steps from <a href="http://canchito-dev.com/public/blog/2017/04/16/spring-initializer/" target="_blank" rel="noopener noreferrer">here</a>.</li>
</ul>
<h2>Introduction</h2>
<p style="text-align: justify;"><a href="https://www.activiti.org/" target="_blank" rel="noopener noreferrer">Activiti</a> is an open source, light-weight process definition driven and Business Process Management engine. As you will see, it is easy to integrate with any Java technology or project.</p>
<p style="text-align: justify;">A process definition is a typical workflow made up of individual boxes called tasks, which specify what action needs to be performed. It is visualized as a flow-chart-like diagram based on the BPMN 2.0 standard. Thanks to BPMN, business can now understand their business  procedures in a graphical notation.</p>
<p style="text-align: justify;">Activiti's source code can be downloaded from <a href="https://github.com/Activiti/Activiti" target="_blank" rel="noopener noreferrer">github</a>. The project was founded and is sponsored by <a href="https://www.alfresco.com/" target="_blank" rel="noopener noreferrer">Alfresco</a> and distributed under the Apache license, but enjoys contributions from all across the globe and industries.</p>

<h2>Spring Boot Integration</h2>
<p style="text-align: justify;">Integrating Activiti's engine into a Spring microservice is quite easy. Basically, you just need to add the needed dependencies and a database. Within minutes, you can have a production-ready service up and running with the capability to orchestrate human workflows to achieve a certain goal.</p>

<h2>Getting Started</h2>
<p style="text-align: justify;">Once you have created an empty project and imported into your favorite IDE, it is time to modify the <code>pom.xml</code> file. If you have not created the project yet, you can follow the steps described in <a href="http://canchito-dev.com/public/blog/2017/04/16/spring-initializer/" target="_blank" rel="noopener noreferrer">here</a>.</p>
<p style="text-align: justify;">Let's open the <code>pom.xml</code> file, and add the dependencies needed to get Spring Boot, Activiti and a database. We’ll use an H2 in memory database to keep things simple.</p>
<p style="text-align: justify;">First we add the version of Activiti that we will be using as a property. Notice that we will use the latest current version 5.22.0.</p>

<pre class="EnlighterJSRAW" data-enlighter-language="xml" data-enlighter-theme="classic">&lt;properties&gt;
  &lt;project.build.sourceEncoding&gt;UTF-8&lt;/project.build.sourceEncoding&gt;
  &lt;project.reporting.outputEncoding&gt;UTF-8&lt;/project.reporting.outputEncoding&gt;
  &lt;java.version&gt;1.8&lt;/java.version&gt;
  &lt;activiti.version&gt;5.22.0&lt;/activiti.version&gt;
&lt;/properties&gt;</pre>
<p style="text-align: justify;">Next, we add two dependencies. But do not remove the previously added ones.</p>

<pre class="EnlighterJSRAW" data-enlighter-language="xml" data-enlighter-theme="classic">&lt;dependencies&gt;		
  &lt;!-- Activiti BPM workflow engine --&gt;
  &lt;dependency&gt;&lt;!-- Activiti Spring Boot Starter Basic --&gt;
    &lt;groupId&gt;org.activiti&lt;/groupId&gt;
    &lt;artifactId&gt;activiti-spring-boot-starter-basic&lt;/artifactId&gt;
    &lt;version&gt;${activiti.version}&lt;/version&gt;
  &lt;/dependency&gt;&lt;!-- Activiti Spring Boot Starter Basic --&gt;
  
  &lt;dependency&gt;
    &lt;groupId&gt;com.h2database&lt;/groupId&gt;
    &lt;artifactId&gt;h2&lt;/artifactId&gt;
    &lt;scope&gt;runtime&lt;/scope&gt;
  &lt;/dependency&gt;
&lt;/dependencies&gt;</pre>
<p style="text-align: justify;">That's it. Yuo have successfully included Activiti into your project. Now let's give it a test run. Wait a minute! When you try to execute the project, it fails! How come you ask?! This is the error you are seeing:</p>

<pre class="EnlighterJSRAW" data-enlighter-theme="enlighter" data-enlighter-linenumbers="false">Error starting ApplicationContext. To display the auto-configuration report re-run your application with 'debug' enabled.
2017-04-16 22:08:23.599 ERROR 10308 --- [           main] o.s.boot.SpringApplication               : Application startup failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'springProcessEngineConfiguration' defined in class path resource [org/activiti/spring/boot/DataSourceProcessEngineAutoConfiguration$DataSourceProcessEngineConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.activiti.spring.SpringProcessEngineConfiguration]: Factory method 'springProcessEngineConfiguration' threw exception; nested exception is java.io.FileNotFoundException: class path resource [processes/] cannot be resolved to URL because it does not exist
  at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:599) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
  at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1173) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
  ...
  ...
  ...
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.activiti.spring.SpringProcessEngineConfiguration]: Factory method 'springProcessEngineConfiguration' threw exception; nested exception is java.io.FileNotFoundException: class path resource [processes/] cannot be resolved to URL because it does not exist
  at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:189) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
  at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:588) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
  ... 17 common frames omitted
Caused by: java.io.FileNotFoundException: class path resource [processes/] cannot be resolved to URL because it does not exist
  at org.springframework.core.io.ClassPathResource.getURL(ClassPathResource.java:187) ~[spring-core-4.3.7.RELEASE.jar:4.3.7.RELEASE]
  at org.springframework.core.io.support.PathMatchingResourcePatternResolver.findPathMatchingResources(PathMatchingResourcePatternResolver.java:463) ~[spring-core-4.3.7.RELEASE.jar:4.3.7.RELEASE]
  at org.springframework.core.io.support.PathMatchingResourcePatternResolver.getResources(PathMatchingResourcePatternResolver.java:292) ~[spring-core-4.3.7.RELEASE.jar:4.3.7.RELEASE]

  ...
  ...
  ...</pre>
<p style="text-align: justify;">There is a nice feature that is enabled when Activiti is integrated with Spring. This feature automatically deploys process definitions found under <code>classpath:process/</code>, every time the process engine is created. However, since Activiti's version <em>5.19.0.2</em>, if there are no process definitions in that folder, the process engine cannot be started. Give it a shot, change Activiti's version to<em> 5.19.0</em> and try to start it. We will change the version to <em>5.19.0</em> for now, and later own we will add  process definition and test version <em>5.22.0</em> again.</p>
<p style="text-align: justify;">You could already run this application using version <em>5.19.0</em>. However, it won’t do anything functionally but behind the scenes it already:</p>

<ul>
 	<li style="text-align: justify;">Created an in-memory H2 database</li>
 	<li style="text-align: justify;">Created an Activiti process engine using that database</li>
 	<li style="text-align: justify;">Exposed all Activiti services as Spring Beans</li>
 	<li style="text-align: justify;">Configured the Activiti async job executor, mail server, etc.</li>
</ul>
<h2>Adding REST Support</h2>
<p style="text-align: justify;">Until now, we have embedded the Activiti's engine into our application. But sometimes we need to we able to allow machine to machine communication (M2M). One way of doing this is by exchanging information via REST messages. So let's add the following dependencies to our project:</p>

<pre class="EnlighterJSRAW" data-enlighter-language="xml" data-enlighter-theme="classic">&lt;dependency&gt;&lt;!-- Activiti Spring Boot Starter Rest Api --&gt;
  &lt;groupId&gt;org.activiti&lt;/groupId&gt;
  &lt;artifactId&gt;activiti-spring-boot-starter-rest-api&lt;/artifactId&gt;
  &lt;version&gt;${activiti.version}&lt;/version&gt;
&lt;/dependency&gt;&lt;!-- Activiti Spring Boot Starter Rest Api --&gt;</pre>
<p style="text-align: justify;">This dependency takes the Activiti REST API (which is written in Spring MVC) and exposes this fully in our application. For a more detail information about the REST API, please visit <a href="https://www.activiti.org/userguide/index.html" target="_blank" rel="noopener noreferrer">Activiti's user guide</a>.</p>
<p style="text-align: justify;">At this point, if you run the application, you will see all the REST endpoints exposed. However, if you try to do a request, you will get an 401 - Unauthorized response. This is because all REST-resources require a valid Activiti-user to be authenticated by default, but none is automatically created when including the REST API dependency. So, let's add a valid user to our application, by copying and pasting the below code into our main class.</p>

<pre class="EnlighterJSRAW" data-enlighter-language="java" data-enlighter-theme="classic">@Bean
InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {

    return new InitializingBean() {
        public void afterPropertiesSet() throws Exception {

            Group group = identityService.newGroup("user");
            group.setName("users");
            group.setType("security-role");
            identityService.saveGroup(group);

            User admin = identityService.newUser("admin");
            admin.setPassword("admin");
            identityService.saveUser(admin);

        }
    };
}</pre>
<p style="text-align: justify;">This code snippet does two things:</p>

<ol>
 	<li style="text-align: justify;">It creates the group called <em>users</em>, and sets it type to <em>security-role</em>.</li>
 	<li style="text-align: justify;">It add a user called <em>admin</em> to the previously created group with the password <em>admin</em>.</li>
</ol>
<p style="text-align: justify;">Now, do the request again, but remember to include the <em>Basic Authentication</em> in you header, specifying the username and password that was just created. Remember not to include the above code in your production ready application. You do not want to add a back door!</p>

<h2 style="text-align: justify;">Changing the Data Source</h2>
<p style="text-align: justify;"><em><strong>NOTE:</strong> In this section, we will be creating a property class called <code>ActivitiDataSourceProperties</code>. Nevertheless, its usage will not be obvious until further in the post, once we reach the section Overriding The <code>SpringProcessEngineConfiguration</code> Bean.</em></p>
<p style="text-align: justify;">An in-memory database like H2 is good for a testing environment. But once you move the application to a production ready environment, you will probably will be using other type of database like MySQL or Oracle.</p>
<p style="text-align: justify;">To change the database that Activiti must use, simple override the default by providing a data source bean. To do this, we will perform two steps:</p>

<ul style="text-align: justify;">
 	<li style="text-align: justify;">Modify the data source properties in the <code>application.properties</code> file found in the <code>classpath</code>.</li>
 	<li style="text-align: justify;">Create a data source bean, which will override the default one.</li>
</ul>
<p style="text-align: justify;">Start by opening the <code>application.properties</code> file found in the <code>classpath</code> and add the following properties. Do not worry, it is probable that the file is completely empty. As these properties are managed by the <code>DataSourceProperties</code> class, Spring enables an auto-complete feature.</p>

<ul>
 	<li style="text-align: justify;"><strong>spring.datasource.driver-class-name:</strong> Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.</li>
 	<li style="text-align: justify;"><strong>spring.datasource.password:</strong> Login password of the database.</li>
 	<li style="text-align: justify;"><strong>spring.datasource.url:</strong> JDBC url of the database.</li>
 	<li style="text-align: justify;"><strong>spring.datasource.username:</strong> Login username of the database.</li>
</ul>
<p style="text-align: justify;">By adding this properties, we will be overriding the default values found in Spring's <code>DataSourceProperties</code> class.</p>
<p style="text-align: justify;">Second, we need to override the default bean. We can create the new bean in the file where the main function is, for example. Copy and paste the following code:</p>

<pre class="EnlighterJSRAW" data-enlighter-language="java" data-enlighter-theme="classic">  @Autowired
  private DataSourceProperties properties;
  
  @Bean(name = "datasource.activiti")
  public DataSource activitiDataSource() {
    return DataSourceBuilder.create(this.properties.getClassLoader())
        .url(this.properties.getUrl())
        .username(this.properties.getUsername())
        .password(this.properties.getPassword())
        .driverClassName(this.properties.getDriverClassName())
            .build();
  }</pre>
<p style="text-align: justify;">By autowiring the <code>DataSourceProperties</code>, Spring automatically will read the <code>application.properties</code> file and assigns the values that it finds within the file. In other words, it will look for properties which start with <em>"spring.datasource"</em> and that at the same time it finds a matching getter.</p>
<p style="text-align: justify;">Since we are using a properties class, we need to add an additional dependency:</p>

<pre class="EnlighterJSRAW" data-enlighter-language="xml" data-enlighter-theme="classic">&lt;dependency&gt;
  &lt;groupId&gt;org.springframework.boot&lt;/groupId&gt;
  &lt;artifactId&gt;spring-boot-configuration-processor&lt;/artifactId&gt;
  &lt;optional&gt;true&lt;/optional&gt;
&lt;/dependency&gt;</pre>
<p style="text-align: justify;">Here, we are using the Spring's helper class <code>DataSourceBuilder</code>, which is a convenience class for building a data source with common implementations and properties.</p>
<p style="text-align: justify;">The data source that is constructed based on the default provided JDBC properties will have the default <a href="http://www.mybatis.org/" target="_blank" rel="noopener noreferrer">MyBatis</a> connection pool settings. The following attributes can optionally be set to tweak that connection pool (taken from the <a href="http://www.mybatis.org/" target="_blank" rel="noopener noreferrer">MyBatis</a> documentation):</p>

<ul>
 	<li style="text-align: justify;"><strong>jdbcMaxActiveConnections</strong>: The number of active connections that the connection pool at maximum at any time can contain. Default is 10.</li>
 	<li style="text-align: justify;"><strong>jdbcMaxIdleConnections</strong>: The number of idle connections that the connection pool at maximum at any time can contain.</li>
 	<li style="text-align: justify;"><strong>jdbcMaxCheckoutTime</strong>: The amount of time in milliseconds a connection can be checked out from the connection pool before it is forcefully returned. Default is 20000 (20 seconds).</li>
 	<li style="text-align: justify;"><strong>jdbcMaxWaitTime</strong>: This is a low level setting that gives the pool a chance to print a log status and re-attempt the acquisition of a connection in the case that it is taking unusually long (to avoid failing silently forever if the pool is misconfigured) Default is 20000 (20 seconds).</li>
</ul>
<p style="text-align: justify;">By default, these four properties are not exposed in the <code>ActivitiProperties</code> class, and that is why, you do not see them in the <code>application.properties</code> files. So, we will create our own properties class which will have getters and setters for these four properties.</p>
<p style="text-align: justify;">Create a package where are newly property classes will reside. I will called it <code>com.canchitodev.example.configuration.properties</code>. And inside, create a class which name will be <code>ActivitiDataSourceProperties</code>. Once you have all these, copy and paste the below code. Remember that the getters and setters are removed for simplicity.</p>

<pre class="EnlighterJSRAW" data-enlighter-language="java" data-enlighter-theme="classic">package com.canchitodev.example.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="activiti.datasource")
public class ActivitiDataSourceProperties {
  
  private String url;
  private String username;
  private String password;
  private String driverClassName;
  private Integer jdbcMaxWaitTime=20000;
  private Integer jdbcMaxCheckoutTime=20000;
  private Integer jdbcMaxIdleConnections=10;
  private Integer jdbcMaxActiveConnections=10;
  private Boolean dbEnableEventLogging=true;
  
  // Getters and setters are removed for simplicity
  @Override
  public String toString() {
    return "ActivitiDataSourceProperties [url=" + url + ", username=" + username + ", password=" + password
        + ", driverClassName=" + driverClassName + ", jdbcMaxWaitTime=" + jdbcMaxWaitTime
        + ", jdbcMaxCheckoutTime=" + jdbcMaxCheckoutTime + ", jdbcMaxIdleConnections=" + jdbcMaxIdleConnections
        + ", jdbcMaxActiveConnections=" + jdbcMaxActiveConnections + ", dbEnableEventLogging="
        + dbEnableEventLogging + "]";
  }
}</pre>
<p style="text-align: justify;">The annotation <code>@ConfigurationProperties(prefix="activiti.datasource")</code> tells Spring that the class <code>ActivitiDataSourceProperties</code> will be a place holder for properties, and which properties are to be mapped to all those properties found in the <code>application.properties</code> file that start with <em>"activiti.datasource"</em>. As a result, Spring will set the value of the properties:</p>

<ul>
 	<li><strong>activiti.datasource.url:</strong> JDBC URL of the database.</li>
 	<li><strong>activiti.datasource.username:</strong> Username to connect to the database.</li>
 	<li><strong>activiti.datasource.password:</strong> Password to connect to the database.</li>
 	<li><strong>activiti.datasource.driver-class-name:</strong> Implementation of the driver for the specific database type.</li>
 	<li><strong>activiti.datasource.jdbc-max-wait-time:</strong> This is a low level setting that gives the pool a chance to print a log status and re-attempt the acquisition of a connection in the case that it is taking unusually long (to avoid failing silently forever if the pool is misconfigured) Default is 20000 (20 seconds).</li>
 	<li><strong>activiti.datasource.jdbc-max-checkout-time:</strong> The amount of time in milliseconds a connection can be checked out from the connection pool before it is forcefully returned. Default is 20000 (20 seconds).</li>
 	<li><strong>activiti.datasource.jdbc-max-idle-connections:</strong> The number of idle connections that the connection pool at maximum at any time can contain.</li>
 	<li><strong>activiti.datasource.jdbc-max-active-connections:</strong> The number of active connections that the connection pool at maximum at any time can contain. Default is 10.</li>
</ul>
<p style="text-align: justify;">The first 4 will not be used at the moment, but the rest can be copied into the <code>application.properties</code> file and assigned a value.</p>
<p style="text-align: justify;"><em><strong>NOTE</strong>: If you would like to learn more about properties, I recommend reading the post "<a href="https://spring.io/blog/2013/10/30/empowering-your-apps-with-spring-boot-s-property-support" target="_blank" rel="noopener noreferrer">Empowering your apps with Spring Boot's property support</a>" by <a href="https://spring.io/team/gturnquist" target="_blank" rel="noopener noreferrer">Greg Turnquist</a>.</em></p>

<h2 style="text-align: justify;">Configuring The Async Job Executor</h2>
<p style="text-align: justify;"><em><strong>NOTE:</strong> In this section, we will be creating a property class called ActivitiAsycExecutorProperties. Nevertheless, its usage will not be obvious until further in the post, once we reach the section Overriding The <code>SpringProcessEngineConfiguration</code> Bean.</em></p>
<p style="text-align: justify;">Since version 5.17, Activiti offers two ways for it to execute the jobs. The first one is the Job Executor which is a component that manages a couple of threads to fire timers and also asynchronous messages. By default, it is still used and activated when the process engine starts, but we will be disabling it. If you would like to read more about it, please refer to the advance section of <a href="https://www.activiti.org/userguide/index.html#_job_executor_design" target="_blank" rel="noopener noreferrer">Activiti's user guide</a>.</p>
<p style="text-align: justify;">The second one is the Async Job Executor. The Async executor is a component that manages a thread pool to fire timers and other asynchronous tasks. Moreover it is a more performance and more database friendly way of executing asynchronous jobs in the Activiti Engine. It’s therefore recommended to switch to the Async executor. By default, the it is not enabled.</p>
<p style="text-align: justify;"><strong>IMPORTANT:</strong> Only one executor can be enabled, as they both executors deal with timers and asynchronous jobs in the Activiti Engine.</p>
<p style="text-align: justify;">Activiti recommends using the Async Job Executor due to the following advantages:</p>

<ul>
 	<li style="text-align: justify;">Less database queries because asynchronous jobs are executed without polling the database</li>
 	<li style="text-align: justify;">For non-exclusive jobs there’s no chance to run into OptimisticLockingExceptions anymore</li>
 	<li>
<p style="text-align: justify;">Exclusive jobs are now locked at process instance level instead of the cumbersome logic of queuing exclusive jobs in the Job Executor</p>
</li>
</ul>
<p style="text-align: justify;">The Async Job Executor can be configured to meet your needs:</p>

<ul>
 	<li style="text-align: justify;"><strong>corePoolSize:</strong> The minimal number of threads that are kept alive in the thread pool for job execution. Default value is 2.</li>
 	<li style="text-align: justify;"><strong>maxPoolSize:</strong> The maximum number of threads that are kept alive in the thread pool for job execution. Default value is 10.</li>
 	<li style="text-align: justify;"><strong>keepAliveTime:</strong> The time (in milliseconds) a thread used for job execution must be kept alive before it is destroyed. Default setting is 0. Having a non-default setting of 0 takes resources, but in the case of many job executions it avoids creating new threads all the time. Default value is 5000.</li>
 	<li style="text-align: justify;"><strong>queueSize:</strong> The size of the queue on which jobs to be executed are placed. Default value is 100.</li>
 	<li style="text-align: justify;"><strong>maxTimerJobsPerAcquisition:</strong> The number of timer jobs that are fetched from the database in one query. Default value is 1.</li>
 	<li style="text-align: justify;"><strong>maxAsyncJobsDuePerAcquisition:</strong> The number of asynchronous jobs due that are fetched from the database in one query. Default value is 1.</li>
 	<li style="text-align: justify;"><strong>defaultAsyncJobAcquireWaitTimeInMillis:</strong> The time in milliseconds between asynchronous job due queries being executed. Default value is 10000.</li>
 	<li style="text-align: justify;"><strong>defaultTimerJobAcquireWaitTimeInMillis:</strong> The time in milliseconds between timer job queries being executed. Default value is 10000.</li>
 	<li style="text-align: justify;"><strong>timerLockTimeInMillis:</strong> The time in milliseconds that a timer job is locked before being retried again. The Activiti Engine considers the timer job to have failed after this period of time and will retry. Default value is 300000.</li>
 	<li style="text-align: justify;"><strong>asyncJobLockTimeInMillis:</strong> The time in milliseconds that an asynchronous job is locked before being retried again. The Activiti Engine considers the asynchronous job to have failed after this period of time and will retry. Default value is 300000.</li>
</ul>
<p style="text-align: justify;">Inside the package <code>com.canchitodev.example.configuration.properties</code>, create a class which name will be <code>ActivitiAsynExecutorProperties</code>. Once you have all these, copy and paste the below code. Remember that the getters and setters are removed for simplicity.</p>

<pre class="EnlighterJSRAW" data-enlighter-language="java" data-enlighter-theme="classic">package com.canchitodev.example.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="activiti.async-executor")
public class ActivitiAsycExecutorProperties {
  
  private Integer corePoolSize=2;
  private Integer maxPoolSize=10;
  private Integer keepAliveTime=5000;
  private Integer queueSize=100;
  private Integer maxTimerJobsPerAcquisition=1;
  private Integer maxAsyncJobsDuePerAcquisition=1;
  private Integer defaultAsyncJobAcquireWaitTimeInMillis=10000;
  private Integer defaultTimerJobAcquireWaitTimeInMillis=10000;
  private Integer timerLockTimeInMillis=300000;
  private Integer asyncJobLockTimeInMillis=300000;
  
  // Getters and setters are removed for simplicity
  
  @Override
  public String toString() {
    return "ActivitiAsycExecutorProperties [corePoolSize=" + corePoolSize + ", maxPoolSize=" + maxPoolSize
        + ", keepAliveTime=" + keepAliveTime + ", queueSize=" + queueSize + ", maxTimerJobsPerAcquisition="
        + maxTimerJobsPerAcquisition + ", maxAsyncJobsDuePerAcquisition=" + maxAsyncJobsDuePerAcquisition
        + ", defaultAsyncJobAcquireWaitTimeInMillis=" + defaultAsyncJobAcquireWaitTimeInMillis
        + ", defaultTimerJobAcquireWaitTimeInMillis=" + defaultTimerJobAcquireWaitTimeInMillis
        + ", timerLockTimeInMillis=" + timerLockTimeInMillis + ", asyncJobLockTimeInMillis="
        + asyncJobLockTimeInMillis + "]";
  }
}</pre>
<p style="text-align: justify;">The annotation <code>@ConfigurationProperties(prefix="activiti.async-executor")</code> tells Spring that the class <code>ActivitiAsynExecutorProperties</code> will be a place holder for properties, and which properties are to be mapped to all those properties found in the <code>application.properties</code> file that start with <em>"activiti.async-executor"</em>.</p>
<p style="text-align: justify;"><strong>NOTE</strong>: If you would like to learn more about properties, I recommend reading the post <em>"<a href="https://spring.io/blog/2013/10/30/empowering-your-apps-with-spring-boot-s-property-support" target="_blank" rel="noopener noreferrer">Empowering your apps with Spring Boot's property support</a>"</em> by <a href="https://spring.io/team/gturnquist" target="_blank" rel="noopener noreferrer">Greg Turnquist</a>.</p>

<h2 style="text-align: justify;">Overriding the <code>SpringProcessEngineConfiguration</code> Bean</h2>
<p style="text-align: justify;">Now that we have created all the properties that we need, we can now proceed to modifying Activiti's process engine. For simplicity, we will be doing all these modifications on the class where the main function is. In my case, the class <code>IntegrateActivitiWithSpringApplication</code> found in package <code>com.canchitodev.example</code> is where the main method is found.</p>

<pre class="EnlighterJSRAW" data-enlighter-language="java" data-enlighter-theme="classic">@SpringBootApplication
@EnableConfigurationProperties(value={ActivitiAsycExecutorProperties.class, ActivitiDataSourceProperties.class})
public class IntegrateActivitiWithSpringApplication {
  
  @Autowired
  private DataSourceProperties dataSourceproperties;
  
  @Autowired
  private ActivitiAsycExecutorProperties activitiAsycExecutorProperties;
  
  @Autowired
  private ActivitiDataSourceProperties activitiDataSourceProperties;

  public static void main(String[] args) {
    SpringApplication.run(IntegrateActivitiWithSpringApplication.class, args);
  }
  ...
  ...
  ...</pre>
<p style="text-align: justify;"> This fragment of code from the class <code>IntegrateActivitiWithSpringApplication</code> shows a couple of key components:</p>

<ul>
 	<li style="text-align: justify;"><code>@EnableConfigurationProperties</code> leverages <code>ActivitiAsyncExecutorProperties</code> and <code>ActivitiDataSourceProperties</code> as a source of properties and makes them available to the entire class.</li>
 	<li style="text-align: justify;">We are autowiring <code>ActivitiAsyncExecutorProperties</code> and <code>ActivitiDataSourceProperties</code> so that an instance of them is automatically created.</li>
</ul>
Further below, we will add the function which will modify the process engine configuration using the properties that we created in section <em>Changing the Data Source</em> and <em>Changing the Async job Executor</em>.
<pre class="EnlighterJSRAW" data-enlighter-language="java" data-enlighter-theme="classic">...
...
@Bean
public BeanPostProcessor activitiSpringProcessEngineConfigurer() {
    return new BeanPostProcessor() {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof SpringProcessEngineConfiguration) {
            	// If it is the SpringProcessEngineConfiguration, we want to add or modify some configuration.
                  SpringProcessEngineConfiguration config = (SpringProcessEngineConfiguration) bean;
                  
                  // Database
                  config.setJdbcMaxActiveConnections(activitiDataSourceProperties.getJdbcMaxActiveConnections());
                  config.setJdbcMaxIdleConnections(activitiDataSourceProperties.getJdbcMaxIdleConnections());
                  config.setJdbcMaxCheckoutTime(activitiDataSourceProperties.getJdbcMaxCheckoutTime());
                  config.setJdbcMaxWaitTime(activitiDataSourceProperties.getJdbcMaxWaitTime());
                  config.setEnableDatabaseEventLogging(activitiDataSourceProperties.getDbEnableEventLogging());
                  
                  // Async Job Executor
                  DefaultAsyncJobExecutor asyncExecutor = new DefaultAsyncJobExecutor();
                  asyncExecutor.setAsyncJobLockTimeInMillis(activitiAsycExecutorProperties.getAsyncJobLockTimeInMillis());
                  asyncExecutor.setCorePoolSize(activitiAsycExecutorProperties.getCorePoolSize());
                  asyncExecutor.setDefaultAsyncJobAcquireWaitTimeInMillis(activitiAsycExecutorProperties.getDefaultAsyncJobAcquireWaitTimeInMillis());
                  asyncExecutor.setDefaultTimerJobAcquireWaitTimeInMillis(activitiAsycExecutorProperties.getDefaultTimerJobAcquireWaitTimeInMillis());
                  asyncExecutor.setKeepAliveTime(activitiAsycExecutorProperties.getKeepAliveTime());
                  asyncExecutor.setMaxAsyncJobsDuePerAcquisition(activitiAsycExecutorProperties.getMaxAsyncJobsDuePerAcquisition());
                  asyncExecutor.setMaxPoolSize(activitiAsycExecutorProperties.getMaxPoolSize());
                  asyncExecutor.setMaxTimerJobsPerAcquisition(activitiAsycExecutorProperties.getMaxTimerJobsPerAcquisition());
                  asyncExecutor.setQueueSize(activitiAsycExecutorProperties.getQueueSize());
                  asyncExecutor.setTimerLockTimeInMillis(activitiAsycExecutorProperties.getTimerLockTimeInMillis());
                  config.setAsyncExecutor(asyncExecutor);
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }           
    };
}
...
...</pre>
<p style="text-align: justify;">Finally, open the <code>application.properties</code> file and add the following properties:</p>

<ul>
 	<li style="text-align: justify;"><strong>spring.activiti.async-executor-activate:</strong> Instructs the Activiti Engine to startup the Async Executor thread pool at startup. Can be true (activate) of false (suspend)</li>
 	<li style="text-align: justify;"><strong>spring.activiti.async-executor-enabled:</strong> Enables the Async executor instead of the old Job executor. Can be true (enable) of false (disable)</li>
 	<li style="text-align: justify;"><strong>spring.activiti.check-process-definitions:</strong> Whether to automatically deploy resources. Can be true (deploy) of false (not deploy)</li>
 	<li style="text-align: justify;"><strong>spring.activiti.history-level:</strong> Following history levels can be configured:
<ul>
 	<li style="text-align: justify;"><em>none:</em> skips all history archiving. This is the most performant for runtime process execution, but no historical information will be available</li>
 	<li style="text-align: justify;"><em>activity:</em> archives all process instances and activity instances. At the end of the process instance, the latest values of the top level process instance variables will be copied to historic variable instances. No details will be archived</li>
 	<li style="text-align: justify;"><em>audit:</em> This is the default. It archives all process instances, activity instances, keeps variable values continuously in sync and all form properties that are submitted so that all user interaction through forms is traceable and can be audited</li>
 	<li style="text-align: justify;"><em>full:</em> This is the highest level of history archiving and hence the slowest. This level stores all information as in the audit level plus all other possible details, mostly this are process variable updates</li>
</ul>
</li>
 	<li style="text-align: justify;"><strong>spring.activiti.job-executor-activate:</strong> Instructs the Activiti Engine to startup the Job Executor. Can be true (activate) of false (suspend)</li>
</ul>
By setting the property <strong>spring.activiti.check-process-definitions</strong> to false, we can now change back to Activiti's version <em>5.22</em> without getting an error when running our application, as we have instructed Activiti not to deploy the resources automatically.
<h2 style="text-align: justify;">Summary</h2>
<p style="text-align: justify;">In this post, you will learned:</p>

<ul>
 	<li style="text-align: justify;">How to integrate Activiti's engine and REST API into your Spring application.</li>
 	<li style="text-align: justify;">Use Activiti with an in-memory H2 database and/or with other databases.</li>
 	<li style="text-align: justify;">Create configuration property classes, which were used for modifying Activiti's data source and Async Job Executor behavior.</li>
 	<li style="text-align: justify;">Configured Activiti's process engine to fit your needs.</li>
</ul>
<p style="text-align: justify;">Hope you enjoyed this post as much as I did writing it. Please leave your comments and feedback.</p>
