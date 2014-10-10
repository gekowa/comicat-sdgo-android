package cn.sdgundam.comicatsdgo.data_model;

import java.util.Date;

/**
 * Created by xhguo on 9/25/2014.
 */
public class PostInfo {
    int postId;
    String title;
    int gdPostType;
    int gdPostCategory;
    String videoHost;
    String videoId;
    String contentHTML;
    Date created;
    String createdBy;

    String userName;
    int authorId;
    String briefText;

    int clicks;

    int listStyle;

    public PostInfo() { }

    public PostInfo(int postId, String title, int gdPostCategory, Date created, int listStyle) {
        this.postId = postId;
        this.title = title;
        this.gdPostCategory = gdPostCategory;
        this.created = created;
        this.listStyle = listStyle;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGdPostType() {
        return gdPostType;
    }

    public void setGdPostType(int gdPostType) {
        this.gdPostType = gdPostType;
    }

    public int getGdPostCategory() {
        return gdPostCategory;
    }

    public void setGdPostCategory(int gdPostCategory) {
        this.gdPostCategory = gdPostCategory;
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

    public String getContentHTML() {
        return contentHTML;
    }

    public void setContentHTML(String contentHTML) {
        this.contentHTML = contentHTML;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getBriefText() {
        return briefText;
    }

    public void setBriefText(String briefText) {
        this.briefText = briefText;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }


    public int getListStyle() { return listStyle; }

    public void setListStyle(int listStyle) { this.listStyle = listStyle; }
}
