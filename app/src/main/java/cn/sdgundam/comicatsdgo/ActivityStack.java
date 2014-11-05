package cn.sdgundam.comicatsdgo;

import java.util.Stack;

/**
 * Created by xhguo on 11/5/2014.
 */
public class ActivityStack {
    private Stack<String> stack;

    private static ActivityStack instance;

    private ActivityStack() {
        stack = new Stack<String>();
    }

    public static ActivityStack getInstance() {
        if (instance == null) {
            instance = new ActivityStack();
        }
        return instance;
    }

    public void push(String objectType, String objectId) {
        stack.push(objectType + ":" + objectId);
    }

    public void pop() {
        stack.pop();
    }

    public void empty() {
        stack.empty();
    }

    public boolean isInStack(String objectType, String objectId) {
        int position = stack.search(objectType + ":" + objectId);
        return position > 0;
    }

    public int getStackSize() {
        return stack.size();
    }
}
