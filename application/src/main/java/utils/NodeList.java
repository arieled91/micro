package utils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NodeList<T> {

    private List<T> nodeList = new ArrayList<>();
    private int currentIndex = -1;

    public void add(T t) {
        nodeList.add(t);
    }

    @Nullable public T get() {
        if(currentIndex<0) return next();
        return nodeList.get(currentIndex);
    }

    @Nullable public T next() {
        if(hasNext()) {
            currentIndex++;
            return nodeList.get(currentIndex);
        }
        return null;
    }

    @Nullable public T previous() {
        if(hasPrevious()) {
            currentIndex--;
            return nodeList.get(currentIndex);
        }
        return null;
    }

    public boolean hasNext(){
        return nodeList.size()-1>=currentIndex+1;
    }

    public boolean hasPrevious(){
        return currentIndex-1>=0;
    }

    public static NodeList<Character> fromCharArray(char... chars){
        NodeList<Character> list = new NodeList<>();
        for (char c : chars) {
            list.add(c);
        }
        list.add('\0');
        return list;
    }

    public List<T> list() {
        return nodeList;
    }
}