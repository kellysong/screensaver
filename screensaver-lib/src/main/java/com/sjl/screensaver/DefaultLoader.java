package com.sjl.screensaver;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DefaultLoader
 * @time 2020/11/5 16:37
 * @copyright(C) 2020 song
 */
public class DefaultLoader implements AdvLoader {

    private MediaPlayerUtils mediaPlayerUtils;

    @Override
    public void loadImage(Context context, Object model, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .centerCrop();
        RequestManager with = Glide.with(context);
        RequestBuilder<Drawable> load;
        if (model instanceof Integer) {
            load = with.load((Integer) model);
        } else {
            load = with.load((File) model);
        }
        load.apply(options).into(imageView);
    }

    @Override
    public void loadVideo(Context context, Object model, SurfaceView surfaceView) {
        if (mediaPlayerUtils == null) {
            mediaPlayerUtils = new MediaPlayerUtils();
        }
        if (model instanceof Integer) {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd((Integer) model);
            mediaPlayerUtils.play(afd, surfaceView);
        } else {
        }

    }

    @Override
    public void setLoaderListener(int type, LoaderListener loaderListener) {
        if (type == AdvModel.TYPE_IMG) {
            //图片不需要
            loaderListener.onFinish();
        } else {
            mediaPlayerUtils.setLoaderListener(loaderListener);
        }
    }

    @Override
    public void stop() {
        if (mediaPlayerUtils != null) {
            mediaPlayerUtils.stop();
        }
    }


    @Override
    public void clear() {
        if (mediaPlayerUtils != null) {
            mediaPlayerUtils.release();
            mediaPlayerUtils = null;
        }
    }

}
