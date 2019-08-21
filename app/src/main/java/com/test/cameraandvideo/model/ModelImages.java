package com.test.cameraandvideo.model;

import java.util.List;


public class ModelImages {
    String folder;
    List<PathItem> pathItem;
    private boolean selected;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public List<PathItem> getPathItem() {
        return pathItem;
    }

    public void setPathItem(List<PathItem> pathItem) {
        this.pathItem = pathItem;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public ModelImages(String folder, List<PathItem> pathItem, boolean selected, boolean selectedItem) {
        this.folder = folder;
        this.pathItem = pathItem;
        this.selected = selected;
    }

    public ModelImages() {
    }
}
