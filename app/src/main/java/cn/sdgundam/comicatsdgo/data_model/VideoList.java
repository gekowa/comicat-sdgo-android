package cn.sdgundam.comicatsdgo.data_model;

import java.util.List;

/**
 * Created by xhguo on 10/20/2014.
 */
public class VideoList {
    int category;
    List<VideoListItem> videoListItems;

    public VideoList() { }

    public VideoList(int category) {
        this.category = category;
    }

    public VideoList(int category, List<VideoListItem> videoListItems) {
        this.category = category;
        this.videoListItems = videoListItems;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public List<VideoListItem> getVideoListItems() {
        return videoListItems;
    }

    public void setVideoListItems(List<VideoListItem> videoListItems) {
        this.videoListItems = videoListItems;
    }
}
