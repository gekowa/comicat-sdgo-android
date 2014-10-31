package cn.sdgundam.comicatsdgo.data_model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xhguo on 9/25/2014.
 */
public class VideoListItem implements Serializable {
    String title;
    String title2;
    String imageURL;
    int gdPostCategory;

    int postId;

    String videoHost;
    String videoId;
    String videoId2;

    Date created;
    String createdBy;

    public VideoListItem(int postId, String title, String title2, String imageURL, int gdPostCategory, Date created) {
        this.postId = postId;
        this.title = title;
        this.title2 = title2;
        this.imageURL = imageURL;
        this.gdPostCategory = gdPostCategory;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getGdPostCategory() {
        return gdPostCategory;
    }

    public void setGdPostCategory(int gdPostCategory) {
        this.gdPostCategory = gdPostCategory;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getVideoHost() {
        return videoHost;
    }

    public void setVideoHost(String videoHost) {
        this.videoHost = videoHost;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoId2() {
        return videoId2;
    }

    public void setVideoId2(String videoId2) {
        this.videoId2 = videoId2;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
