package com.example.stefan.tennis.workers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class DemoJobCreator implements JobCreator {

    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        switch (tag) {
            case NotificariZilnice.TAG:
                return new NotificariZilnice();
            default:
                return null;
        }
    }
}