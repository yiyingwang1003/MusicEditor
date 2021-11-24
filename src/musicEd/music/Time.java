package musicEd.music;

import java.util.ArrayList;

public class Time {  // time is part of the system
    public int x;
    private Time(int x, List tl){this.x = x; tl.add(this);}  // tl = time list
    //-------------list--------------
    public static class List extends ArrayList<Time>{
        public Sys sys;
        public List(Sys sys){this.sys = sys;}
        public Time gTime(int x){
            Time res = null;
            int dist = UC.snapTime;
            for (Time t : this){
                int d = Math.abs(t.x - x);
                if (d < dist){
                    dist = d;
                    res = t;  // it only finds time if less than snapTime
                }
            }
            return (res != null)? res : new Time(x, this);  // "this" is the list
        }
    }
}
