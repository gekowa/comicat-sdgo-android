package cn.sdgundam.comicatsdgo.data_model;

/**
 * Created by xhguo on 9/25/2014.
 */
public class CarouselInfo {

    String title;
    String imageURL;
    int gdPostType;
    int postId;

    public CarouselInfo(String title, String imageURL, int gdPostType, int postId) {
        this.title = title;
        this.imageURL = imageURL;
        this.gdPostType = gdPostType;
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getGdPostType() {
        return gdPostType;
    }

    public void setGdPostType(int gdPostType) {
        this.gdPostType = gdPostType;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }


}
