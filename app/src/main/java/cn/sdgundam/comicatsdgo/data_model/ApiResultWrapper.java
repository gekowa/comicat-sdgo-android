package cn.sdgundam.comicatsdgo.data_model;

/**
 * Created by xhguo on 10/20/2014.
 */
public class ApiResultWrapper<T> {
    private Exception e;
    private T payload;

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public ApiResultWrapper() { }

    public ApiResultWrapper(T payload) {
        this.payload = payload;
    }

    public ApiResultWrapper(Exception e) {
        this.e = e;
    }
}
