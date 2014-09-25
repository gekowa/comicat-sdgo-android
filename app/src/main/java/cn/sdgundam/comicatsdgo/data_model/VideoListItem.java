package cn.sdgundam.comicatsdgo.data_model;

import java.util.Date;

/**
 * Created by xhguo on 9/25/2014.
 */
public class VideoListItem {
    String title;
    String title2;
    String imageURL;
    int gdPostCategory;

    int postId;
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
