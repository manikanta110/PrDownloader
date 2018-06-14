package com.svmexample.prdownloader;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView progressOne;
    Button cancel,tart;
    String dirpath;
    int downloadOne;
    private String Url = "http://d8.o2tvseries.com/The%20Originals/Season%2005/The%20Originals%20-%20S05E08%20HD%20(TvShows4Mobile.Com).mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();
        dirpath = Utils.getRootDirPath(getApplicationContext());
        onclickListener();

    }

    private void onclickListener() {

        tart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Status.RUNNING == PRDownloader.getStatus(downloadOne)){
                    PRDownloader.pause(downloadOne);
                    return;

                }

                tart.setEnabled(false);
                progressBar.setIndeterminate(true);
                progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);



                if (Status.PAUSED == PRDownloader.getStatus(downloadOne)) {
                    PRDownloader.resume(downloadOne);
                    return;
                }

            downloadOne = PRDownloader.download(Url,dirpath,"My_video").build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                            progressBar.setIndeterminate(false);
                            tart.setEnabled(true);
                            tart.setText(R.string.pause);
                            cancel.setEnabled(true);


                        }
                    }).setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                            tart.setText(R.string.resume);

                        }
                    }).setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel() {

                            tart.setText(R.string.start);
                            cancel.setEnabled(false);
                            progressBar.setProgress(0);
                            progressOne.setText("");
                            downloadOne = 0;
                            progressBar.setIndeterminate(false);

                        }
                    })

                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {

                            long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                            progressBar.setProgress((int) progressPercent);
                            progressOne.setText(Utils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                            progressBar.setIndeterminate(false);



                        }
                    }).start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            tart.setEnabled(false);
                            cancel.setEnabled(false);
                            tart.setText(R.string.completed);

                        }

                        @Override
                        public void onError(Error error) {


                            tart.setText(R.string.start);
                            Toast.makeText(getApplicationContext(), getString(R.string.some_error_occurred) + " " + "1", Toast.LENGTH_SHORT).show();
                            progressOne.setText("");
                            progressBar.setProgress(0);
                            downloadOne = 0;
                            cancel.setEnabled(false);
                            progressBar.setIndeterminate(false);
                            tart.setEnabled(true);



                        }
                    });






            }
        });
    }

    private void initviews() {
        progressBar= findViewById(R.id.progressBarOne);
        progressOne = findViewById(R.id.textViewProgressOne);
        cancel = findViewById(R.id.buttonCancelOne);
        tart = findViewById(R.id.buttonStartOne);
    }
}
