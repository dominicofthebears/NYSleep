package nysleep.DTO;

import java.util.List;

public class PageDTO <T>{
    private List<T> entries;
    private int totCount;

    public List<T> getEntries() {
        return entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = entries;
    }

    public int getTotCount() {
        return totCount;
    }

    public void setTotCount(int totCount) {
        this.totCount = totCount;
    }

    @Override
    public String toString(){
        return "PageDTO{" +
               "entries=" + entries +
               ", totCount=" + totCount +
               '}';
    }
}
