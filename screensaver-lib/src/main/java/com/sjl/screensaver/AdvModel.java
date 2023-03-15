package com.sjl.screensaver;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename ScreenSaverAdv
 * @time 2020/11/5 11:33
 * @copyright(C) 2020 song
 */
public class AdvModel {
    public static final int TYPE_IMG = 0;
    public static final int TYPE_VIDEO = 1;
    /**
     * 0图片，1视频
     */
    private int type;
    /**
     * 资源,支持资源id,本地file,建议先下载好资源文件
     */
    private Object model;

    /**
     * 持续时间，单位毫秒，图片起作用
     */
    private int persistTime;

    public AdvModel(int type, Object model) {
        this.type = type;
        this.model = model;
    }

    public AdvModel(int type, Object model, int persistTime) {
        this.type = type;
        this.model = model;
        this.persistTime = persistTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public int getPersistTime() {
        return persistTime;
    }

    public void setPersistTime(int persistTime) {
        this.persistTime = persistTime;
    }
}
