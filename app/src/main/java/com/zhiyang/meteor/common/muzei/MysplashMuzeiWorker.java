package com.zhiyang.meteor.common.muzei;

import android.content.Context;
import android.net.Uri;

import com.google.android.apps.muzei.api.provider.Artwork;
import com.google.android.apps.muzei.api.provider.ProviderContract;
import com.zhiyang.meteor.R;
import com.zhiyang.meteor.common.di.component.DaggerApplicationComponent;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.network.service.PhotoService;
import com.zhiyang.meteor.common.utils.manager.MuzeiOptionManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MysplashMuzeiWorker extends Worker
        implements MuzeiUpdateHelper.OnUpdateCallback {

    @Inject PhotoService service;

    public MysplashMuzeiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        DaggerApplicationComponent.create().inject(this);
    }

    static void enqueue() {
        WorkManager.getInstance()
                .enqueue(new OneTimeWorkRequest.Builder(MysplashMuzeiWorker.class)
                        .setConstraints(new Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build())
                        .build());
    }

    @NonNull
    @Override
    public Result doWork() {
        if (MuzeiUpdateHelper.update(getApplicationContext(), service, this)) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    @Override
    public void onUpdateSucceed(@NonNull List<Photo> photoList) {
        List<Artwork> artworkList = new ArrayList<>(photoList.size());
        for (Photo p : photoList) {
            artworkList.add(new Artwork.Builder()
                    .title(getApplicationContext().getString(R.string.by) + " " + p.user.name)
                    .byline(getApplicationContext().getString(R.string.on) + " " + p.created_at.split("T")[0])
                    .persistentUri(Uri.parse(p.getDownloadUrl()))
                    .token(p.id)
                    .webUri(Uri.parse(p.links.html))
                    .build());
        }
        if (MuzeiOptionManager.getInstance(getApplicationContext()).getCacheMode().equals("keep")) {
            ProviderContract.getProviderClient(getApplicationContext(), MysplashMuzeiArtProvider.class)
                    .addArtwork(artworkList);
        } else {
            ProviderContract.getProviderClient(getApplicationContext(), MysplashMuzeiArtProvider.class)
                    .setArtwork(artworkList);
        }
    }

    @Override
    public void onUpdateFailed() {
        // do nothing.
    }
}