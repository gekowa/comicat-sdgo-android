package cn.sdgundam.comicatsdgo.video;

/**
 * Created by xhguo on 10/29/2014.
 */
public interface VideoInfoListener {
    public void clickedOnUnit(String unitId);
    public void clickedOnVideo(int postId, String videoHost, String videoId, String videoId2);
}
