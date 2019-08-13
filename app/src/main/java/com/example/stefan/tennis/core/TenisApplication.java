package com.example.stefan.tennis.core;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.example.stefan.tennis.workers.DemoJobCreator;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class TenisApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //FlowManager.init(this);

        ApplicationContext.init(this);
        JobManager.create(this).addJobCreator(new DemoJobCreator());

        FlowManager.init(new FlowConfig.Builder(this)
                // .addDatabaseHolder(GeneratedDatabaseHolder.class)
                .openDatabasesOnInit(true) //Triggers migrations asap
                .build());
    }
}
