package budgetprogram;

import java.util.ArrayList;

public class Repository <T>{
    private ArrayList<Integer> list;
    public Repository(){
        list = new ArrayList<>();
    }

    public int  size(){
        return  list.size();
    }
}
