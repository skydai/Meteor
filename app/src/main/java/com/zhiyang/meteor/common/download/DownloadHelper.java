package com.zhiyang.meteor.common.download;

import android.app.DownloadManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhiyang.meteor.common.di.component.DaggerApplicationComponent;
import com.zhiyang.meteor.common.download.imp.AndroidDownloaderService;
import com.zhiyang.meteor.common.download.imp.DownloaderService;
import com.zhiyang.meteor.common.download.imp.FileDownloaderService;
import com.zhiyang.meteor.common.network.json.Collection;
import com.zhiyang.meteor.common.network.json.Photo;
import com.zhiyang.meteor.common.db.DownloadMissionEntity;
import com.zhiyang.meteor.common.network.service.PhotoService;
import com.zhiyang.meteor.common.utils.FileUtils;
import com.zhiyang.meteor.common.db.DatabaseHelper;
import com.zhiyang.meteor.common.utils.manager.SettingsOptionManager;

import java.util.List;

import javax.inject.Inject;

/**
 * Download helper.
 *
 * A helper class that makes operations of {@link DownloadManager} easier.
 *
 * */

public class DownloadHelper {

    private static DownloadHelper instance;

    public static DownloadHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DownloadHelper.class) {
                if (instance == null) {
                    instance = new DownloadHelper(context);
                }
            }
        }
        return instance;
    }

    private DownloaderService downloaderService;
    @Inject PhotoService photoService;

    private DownloadHelper(Context context) {
        bindDownloader(context, SettingsOptionManager.getInstance(context).getDownloader());
        DaggerApplicationComponent.create().inject(this);
    }

    private void bindDownloader(Context context, String downloader) {
        switch (downloader) {
            case "mysplash":
                this.downloaderService = new FileDownloaderService(context);
                break;

            default: // system.
                this.downloaderService = new AndroidDownloaderService(context);
                break;
        }
    }

    public boolean switchDownloader(Context context, String downloader) {
        if (DatabaseHelper.getInstance(context)
                .readDownloadEntityCount(DownloaderService.RESULT_DOWNLOADING) > 0) {
            return false;
        }
        bindDownloader(context, downloader);
        return true;
    }

    public void addMission(Context c, Photo p, @DownloaderService.DownloadTypeRule int type) {
        if (FileUtils.createDownloadPath(c)) {
            downloaderService.addMission(c, new DownloadMissionEntity(c, p, type), true);
            photoService.downloadPhoto(p.links.download_location);
        }
    }

    public void addMission(Context c, Collection collection) {
        if (FileUtils.createDownloadPath(c)) {
            downloaderService.addMission(c, new DownloadMissionEntity(collection), true);
        }
    }

    @Nullable
    public DownloadMission restartMission(Context c, long missionId) {
        DownloadMissionEntity entity = DatabaseHelper.getInstance(c).readDownloadEntity(missionId);
        if (entity == null) {
            return null;
        } else {
            DownloadMission mission = new DownloadMission(entity);
            mission.entity.missionId = downloaderService.restartMission(c, entity);
            mission.entity.result = DownloaderService.RESULT_DOWNLOADING;
            mission.process = 0;
            return mission.entity.missionId == -1 ? null : mission;
        }
    }

    public void removeMission(Context c, @NonNull DownloadMissionEntity entity) {
        downloaderService.removeMission(c, entity, true);
    }

    public void clearMission(Context c, @Nullable List<DownloadMissionEntity> entityList) {
        if (entityList != null) {
            downloaderService.clearMission(c, entityList);
        }
    }

    public void updateMissionResult(Context c,
                                    @NonNull DownloadMissionEntity entity,
                                    @DownloaderService.DownloadResultRule int result) {
        downloaderService.updateMissionResult(c, entity, result);
    }

    public DownloadMission getDownloadMission(Context context, @NonNull DownloadMissionEntity entity) {
        return new DownloadMission(
                entity,
                downloaderService.getMissionProcess(context, entity));
    }

    public void addOnDownloadListener(@NonNull DownloaderService.OnDownloadListener l) {
        downloaderService.addOnDownloadListener(l);
    }

    public void removeOnDownloadListener(@NonNull DownloaderService.OnDownloadListener l) {
        downloaderService.removeOnDownloadListener(l);
    }
}

