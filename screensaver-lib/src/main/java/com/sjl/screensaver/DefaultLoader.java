package com.sjl.screensaver;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    public void loadImage(Context context, Object model, final ImageView imageView) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;

        RequestOptions options = new RequestOptions()
                .dontAnimate();

        RequestBuilder<Bitmap> bitmapRequestBuilder = Glide.with(context).asBitmap();
        RequestBuilder<Bitmap> load = bitmapRequestBuilder.load(model);
        load.apply(options).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
                float scale = width / resource.getWidth();
                int newWidth = (int) (resource.getWidth() * scale);
                int newHeight = (int) (resource.getHeight() * scale);
                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                lp.width = newWidth;
                lp.height = newHeight;
                imageView.setLayoutParams(lp);
            }
        });
}

    @Override
    public void loadVideo(Context context, Object model, SurfaceView surfaceView) {
        if (mediaPlayerUtils == null) {
            mediaPlayerUtils = new MediaPlayerUtils();
        }
        if (model instanceof Integer) {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd((Integer) model);
            mediaPlayerUtils.play(afd, surfaceView);
        }else if (model instanceof String) {
            AssetManager assetManager = context.getAssets();
            try {
                AssetFileDescriptor afd = assetManager.openFd((String) model);
                mediaPlayerUtils.play(afd, surfaceView);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else if (model instanceof File) {
            mediaPlayerUtils.play((File)model, surfaceView);
        }else if (model instanceof Uri) {
            mediaPlayerUtils.play((Uri)model, surfaceView);
        }else {
            throw new RuntimeException("不支持该格式播放：" + model);
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
