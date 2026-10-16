package budgetprogram;

import java.util.ArrayList;

public class Repository <T>{
    private ArrayList<T> list;
    public Repository(){
        list = new ArrayList<>();
    }

    public int  size(){
        return  list.size();
    }

    public void add(T t) {
        list.add(t);
    }
}
