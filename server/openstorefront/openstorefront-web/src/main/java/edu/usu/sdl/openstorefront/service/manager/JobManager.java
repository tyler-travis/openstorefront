/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.job.LookupImportListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.jobs.DirectoryScanJob;

/**
 * Handles Automation jobs
 *
 * @author dshurtleff
 */
public class JobManager
{

	private static final Logger log = Logger.getLogger(JobManager.class.getName());

	private static final String JOB_GROUP_SYSTEM = "SYSTEM";
	private static Scheduler scheduler;

	public static void initialize()
	{
		try {
			StdSchedulerFactory factory = new StdSchedulerFactory(FileSystemManager.getConfig("quartz.properties").getPath());
			scheduler = factory.getScheduler();

			//Init system Jobs
			initSystemJobs();

			scheduler.start();

		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Failed to init quartz.", ex);
		}
	}

	private static void initSystemJobs() throws SchedulerException
	{
		log.log(Level.FINEST, "Setting up Lookup Import Job...");
		setupLookupCodeJob();
	}

	private static void setupLookupCodeJob() throws SchedulerException
	{
		JobDetail job = JobBuilder.newJob(DirectoryScanJob.class)
				.withIdentity("LookupImport", JOB_GROUP_SYSTEM)
				.build();

		job.getJobDataMap().put(DirectoryScanJob.DIRECTORY_NAME, FileSystemManager.IMPORT_LOOKUP_DIR);
		LookupImportListener lookupImportListener = new LookupImportListener();
		job.getJobDataMap().put(DirectoryScanJob.DIRECTORY_SCAN_LISTENER_NAME, lookupImportListener.getClass().getName());
		scheduler.getContext().put(lookupImportListener.getClass().getName(), lookupImportListener);
		Trigger trigger = newTrigger()
				.withIdentity("lookupTrigger", JOB_GROUP_SYSTEM)
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(30)
						.repeatForever())
				.build();

		// Tell quartz to schedule the job using our trigger
		scheduler.scheduleJob(job, trigger);
	}

	public static void shutdown()
	{
		try {
			scheduler.shutdown();
		} catch (SchedulerException ex) {
			throw new OpenStorefrontRuntimeException("Failed to init quartz.", ex);
		}
	}

}