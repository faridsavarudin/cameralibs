package com.test.cameraandvideo.model;

public class PathItem {
    private String path;
    private boolean selected;

    public PathItem(String path, boolean selected) {
        this.path = path;
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public PathItem() {
    }
}
