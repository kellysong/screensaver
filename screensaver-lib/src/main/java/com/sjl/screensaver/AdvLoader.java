package com.sjl.screensaver;

import android.content.Context;
import android.view.SurfaceView;
import android.widget.ImageView;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename AdvLoader
 * @time 2020/11/5 17:49
 * @copyright(C) 2020 song
 */
public interface AdvLoader {
    void loadImage(Context context, Object model, ImageView imageView);

    void loadVideo(Context context, Object model, SurfaceView surfaceView);


    void setLoaderListener(int type, LoaderListener loaderListener);

    /**
     * 针对视频有效
     */
    void stop();

    void clear();
}
