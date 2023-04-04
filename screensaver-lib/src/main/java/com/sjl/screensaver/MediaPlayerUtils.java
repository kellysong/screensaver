package com.sjl.screensaver;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.File;

import androidx.annotation.NonNull;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename MediaPlayerUtils
 * @time 2020/11/5 18:10
 * @copyright(C) 2020 song
 */
public class MediaPlayerUtils implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener {
    private static final String TAG = MediaPlayerUtils.class.getSimpleName();

    private MediaPlayer mMediaPlayer;
    private SurfaceView mSurfaceView;
    private MyCallback myCallback;
    private LoaderListener loaderListener;


    public MediaPlayerUtils() {
        initPlayer();
    }

    private void initPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                changeVideoSize();
            }
        });

    }

    /**
     * 播放应用的资源文件或原始资源文件
     *
     * @param afd         /res/raw中或者asserts
     * @param surfaceView
     */
    public synchronized void play(AssetFileDescriptor afd, SurfaceView surfaceView) {
        play(afd, null, null, surfaceView);
    }

    /**
     * 播放外部存储上的音频文件
     *
     * @param file
     * @param surfaceView
     */
    public synchronized void play(File file, SurfaceView surfaceView) {
        play(null, file, null, surfaceView);
    }

    /**
     * 播放来自网络的音频文件
     *
     * @param uri         Uri uri = Uri.parse("http://www.xxx.com/test.mp4");
     * @param surfaceView
     */
    public synchronized void play(Uri uri, SurfaceView surfaceView) {
        play(null, null, uri, surfaceView);
    }

    public synchronized void play(AssetFileDescriptor afd, File file, Uri uri, SurfaceView surfaceView) {
        if (mMediaPlayer == null) {
            return;
        }
        if (this.mSurfaceView == null) {
            this.mSurfaceView = surfaceView;
            this.mSurfaceView.setZOrderOnTop(false);
            SurfaceHolder holder = this.mSurfaceView.getHolder();
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            if (afd != null) {
                myCallback = new MyCallback(mMediaPlayer, afd);
            } else if (file != null) {
                myCallback = new MyCallback(mMediaPlayer, file);
            } else if (uri != null) {
                myCallback = new MyCallback(mMediaPlayer, uri,surfaceView.getContext());
            }
            holder.addCallback(myCallback);
        } else {
            if (!mMediaPlayer.isPlaying()) {
                try {
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mMediaPlayer.setDisplay(this.mSurfaceView.getHolder());
                    afd.close();
                    mMediaPlayer.prepareAsync();

                } catch (Exception e) {
                    Log.e(TAG, "播放异常2", e);
                }

            }
        }


    }


    private static class MyCallback implements SurfaceHolder.Callback {
        private AssetFileDescriptor afd;
        private File file;
        private Uri uri;
        private  Context context;
        private MediaPlayer mMediaPlayer;
        private boolean firstPlay;

        public MyCallback(MediaPlayer mediaPlayer, AssetFileDescriptor afd) {
            init(mediaPlayer);
            this.afd = afd;
        }

        public MyCallback(MediaPlayer mediaPlayer, File file) {
            init(mediaPlayer);
            this.file = file;
        }
        public MyCallback(MediaPlayer mediaPlayer, Uri uri, Context context) {
            init(mediaPlayer);
            this.uri = uri;
            this.context = context;
        }

        private void init(MediaPlayer mediaPlayer) {
            this.mMediaPlayer = mediaPlayer;
            this.firstPlay = true;
        }

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            Log.i(TAG, "surfaceCreated");
            if (firstPlay) {
                try {
                    mMediaPlayer.reset();
                    if (afd != null){
                        mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    }else if (file != null){
                        mMediaPlayer.setDataSource(file.getAbsolutePath());
                    }else if (uri != null){
                        mMediaPlayer.setDataSource(context, uri);
                    }
                    mMediaPlayer.setDisplay(holder);
                    if (afd != null){
                        afd.close();
                    }

                    mMediaPlayer.prepareAsync();
                } catch (Exception e) {
                    Log.e(TAG, "播放异常1", e);
                } finally {
                    firstPlay = false;
                }
            }


        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            Log.i(TAG, "surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            Log.i(TAG, "surfaceDestroyed");
//            mPlayer.release();
        }
    }

    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void start() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mSurfaceView != null) {
            SurfaceHolder holder = mSurfaceView.getHolder();
            holder.removeCallback(myCallback);
            mSurfaceView = null;
            myCallback = null;
        }
        stop();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        loaderListener = null;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG, "onPrepared");
        start();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        changeVideoSize();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion");
        stop();
        if (loaderListener != null) {
            loaderListener.onFinish();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "播放回调出错：" + what + "," + extra);
        return false;
    }

    /**
     * 视频尺寸自适应
     */
    public void changeVideoSize() {
        int videoWidth = mMediaPlayer.getVideoWidth();
        int videoHeight = mMediaPlayer.getVideoHeight();

        int surfaceWidth = mSurfaceView.getWidth();
        int surfaceHeight = mSurfaceView.getHeight();

        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
        float max;
        Context context = this.mSurfaceView.getContext();
//        ViewParent parent = this.mSurfaceView.getParent();
//        FrameLayout mParent = (FrameLayout) parent;
        if (context.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //竖屏模式下按视频宽度计算放大倍数值
            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
        } else {
            //横屏模式下按视频高度计算放大倍数值
            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
        }

        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
        videoWidth = (int) Math.ceil((float) videoWidth / max);
        videoHeight = (int) Math.ceil((float) videoHeight / max);

        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(videoWidth, videoHeight);
        params.gravity = Gravity.CENTER;
        mSurfaceView.setLayoutParams(params);
    }

    public void setLoaderListener(LoaderListener loaderListener) {
        this.loaderListener = loaderListener;
    }

    public void setLoopPlay(boolean loop) {
        mMediaPlayer.setLooping(loop);
    }

}
