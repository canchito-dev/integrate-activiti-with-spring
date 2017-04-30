/**
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2017, canchito-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author 		José Carlos Mendoza Prego
 * @copyright	Copyright (c) 2017, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/integrate-activiti-with-spring
 **/
package com.canchitodev.example;

import javax.sql.DataSource;

import org.activiti.engine.impl.asyncexecutor.DefaultAsyncJobExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.BeansException;
// Uncomment if you will use an in-memory database like H2
//import org.activiti.engine.IdentityService;
//import org.activiti.engine.identity.Group;
//import org.activiti.engine.identity.User;
//import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.canchitodev.example.configuration.properties.ActivitiAsycExecutorProperties;
import com.canchitodev.example.configuration.properties.ActivitiDataSourceProperties;

@SpringBootApplication
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
	
	/**
	 * Activiti's REST API is secured by basic auth, and won’t have any users by default. Let’s add an admin user to the system as shown below. 
	 * Don’t do this in a production system of course, there you’ll want to hook in the authentication to LDAP or something else.
	 * 
	 * If you are not using an in-memory database like H2, this bean is only required once. After the user and group have been created, you can
	 * comment this block
	 **/
//	@Bean
//	InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {
//
//	    return new InitializingBean() {
//	        public void afterPropertiesSet() throws Exception {
//
//	            Group group = identityService.newGroup("user");
//	            group.setName("users");
//	            group.setType("security-role");
//	            identityService.saveGroup(group);
//
//	            User admin = identityService.newUser("admin");
//	            admin.setPassword("admin");
//	            identityService.saveUser(admin);
//
//	        }
//	    };
//	}
	
	/**
	 * Activiti's user guide - 5.7.3. Changing the database and connection pool
	 * https://www.activiti.org/userguide/index.html#_changing_the_database_and_connection_pool
	 **/
	@Bean(name = "datasource.activiti")
	public DataSource activitiDataSource() {
		return DataSourceBuilder.create(this.dataSourceproperties.getClassLoader())
				.url(this.dataSourceproperties.getUrl())
				.username(this.dataSourceproperties.getUsername())
				.password(this.dataSourceproperties.getPassword())
				.driverClassName(this.dataSourceproperties.getDriverClassName())
		        .build();
	}
	
	/**
	 * Exposing configuration beans with Spring Boot
	 * https://forums.activiti.org/content/exposing-configuration-beans-spring-boot
	 **/
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
}