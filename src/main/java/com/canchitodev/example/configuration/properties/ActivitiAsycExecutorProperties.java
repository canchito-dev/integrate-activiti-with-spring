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
package com.canchitodev.example.configuration.properties;

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
	
	public Integer getCorePoolSize() {
		return corePoolSize;
	}
	public void setCorePoolSize(Integer corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	
	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	
	public Integer getKeepAliveTime() {
		return keepAliveTime;
	}
	public void setKeepAliveTime(Integer keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
	
	public Integer getQueueSize() {
		return queueSize;
	}
	public void setQueueSize(Integer queueSize) {
		this.queueSize = queueSize;
	}
	
	public Integer getMaxTimerJobsPerAcquisition() {
		return maxTimerJobsPerAcquisition;
	}
	public void setMaxTimerJobsPerAcquisition(Integer maxTimerJobsPerAcquisition) {
		this.maxTimerJobsPerAcquisition = maxTimerJobsPerAcquisition;
	}
	
	public Integer getMaxAsyncJobsDuePerAcquisition() {
		return maxAsyncJobsDuePerAcquisition;
	}
	public void setMaxAsyncJobsDuePerAcquisition(Integer maxAsyncJobsDuePerAcquisition) {
		this.maxAsyncJobsDuePerAcquisition = maxAsyncJobsDuePerAcquisition;
	}
	
	public Integer getDefaultAsyncJobAcquireWaitTimeInMillis() {
		return defaultAsyncJobAcquireWaitTimeInMillis;
	}
	public void setDefaultAsyncJobAcquireWaitTimeInMillis(Integer defaultAsyncJobAcquireWaitTimeInMillis) {
		this.defaultAsyncJobAcquireWaitTimeInMillis = defaultAsyncJobAcquireWaitTimeInMillis;
	}
	
	public Integer getDefaultTimerJobAcquireWaitTimeInMillis() {
		return defaultTimerJobAcquireWaitTimeInMillis;
	}
	public void setDefaultTimerJobAcquireWaitTimeInMillis(Integer defaultTimerJobAcquireWaitTimeInMillis) {
		this.defaultTimerJobAcquireWaitTimeInMillis = defaultTimerJobAcquireWaitTimeInMillis;
	}
	
	public Integer getTimerLockTimeInMillis() {
		return timerLockTimeInMillis;
	}
	public void setTimerLockTimeInMillis(Integer timerLockTimeInMillis) {
		this.timerLockTimeInMillis = timerLockTimeInMillis;
	}
	
	public Integer getAsyncJobLockTimeInMillis() {
		return asyncJobLockTimeInMillis;
	}
	public void setAsyncJobLockTimeInMillis(Integer asyncJobLockTimeInMillis) {
		this.asyncJobLockTimeInMillis = asyncJobLockTimeInMillis;
	}
	
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
}