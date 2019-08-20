package com.test.cameraandvideo.options;

import top.defaults.camera.AspectRatio;

public class AspectRatioItem implements PickerItemWrapper<AspectRatio> {

    private AspectRatio aspectRatio;
    private int img;

    public AspectRatioItem(AspectRatio ratio) {
        aspectRatio = ratio;
    }

    @Override
    public String getText() {
        return aspectRatio.toString();
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public AspectRatio get() {
        return aspectRatio;
    }
}
