package cn.sdgundam.comicatsdgo.data_model;

/**
 * Created by xhguo on 10/20/2014.
 */
public class ApiResultWrapper<T> {
    private Exception e;
    private T wrappingObject;

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public T getWrappingObject() {
        return wrappingObject;
    }

    public void setWrappingObject(T wrappingObject) {
        this.wrappingObject = wrappingObject;
    }

    public ApiResultWrapper() { }

    public ApiResultWrapper(T wrappingObject) {
        this.wrappingObject = wrappingObject;
    }

    public ApiResultWrapper(Exception e) {
        this.e = e;
    }
}
