package cn.sdgundam.comicatsdgo.data_model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xhguo on 9/25/2014.
 */
public class HomeInfo implements Serializable {
    Date generated;
    CarouselInfo[] carousel;
    VideoListItem[] videoList;
    PostInfo[] postList;
    UnitInfoShort[] units;

    public Date getGenerated() {
        return generated;
    }

    public void setGenerated(Date generated) {
        this.generated = generated;
    }

    public CarouselInfo[] getCarousel() {
        return carousel;
    }

    public void setCarousel(CarouselInfo[] carousel) {
        this.carousel = carousel;
    }

    public VideoListItem[] getVideoList() {
        return videoList;
    }

    public void setVideoList(VideoListItem[] videoList) {
        this.videoList = videoList;
    }

    public PostInfo[] getPostList() {
        return postList;
    }

    public void setPostList(PostInfo[] postList) {
        this.postList = postList;
    }

    public UnitInfoShort[] getUnits() {
        return units;
    }

    public void setUnits(UnitInfoShort[] units) {
        this.units = units;
    }
}
