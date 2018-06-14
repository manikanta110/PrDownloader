package com.svmexample.prdownloader;

import android.app.Application;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();

        PRDownloader.initialize(this,config);
    }
}
