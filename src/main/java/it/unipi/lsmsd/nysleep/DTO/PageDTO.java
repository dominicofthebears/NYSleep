package it.unipi.lsmsd.nysleep.DTO;

import java.util.LinkedList;
import java.util.List;

public class PageDTO <T>{
    private LinkedList<T> entries;
    private int totCount;

    public LinkedList<T> getEntries() {
        return entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = new LinkedList<T>(entries);
        this.totCount=entries.size();
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
