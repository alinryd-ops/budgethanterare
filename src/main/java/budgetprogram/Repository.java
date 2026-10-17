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
        if (t == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        list.add(t);
    }

    public ArrayList<T> getList(){
        return list;
    }
}
