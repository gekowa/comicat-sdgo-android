package cn.sdgundam.comicatsdgo.api_model;

import java.util.List;

/**
 * Created by xhguo on 10/23/2014.
 */
public class PostList<T> {
    int category;
    List<T> postListItems;

    public PostList() { }

    public PostList(int category) {
        this.category = category;
    }

    public PostList(int category, List<T> postListItems) {
        this.category = category;
        this.postListItems = postListItems;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public List<T> getPostListItems() {
        return postListItems;
    }

    public void setPostListItems(List<T> postListItems) {
        this.postListItems = postListItems;
    }
}
