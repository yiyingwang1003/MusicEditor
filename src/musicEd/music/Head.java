package musicEd.music;

import musicEd.reaction.Mass;
import java.awt.*;

public class Head extends Mass{
    public Staff staff;
    public int line;  // line is the y coodinate
    public Time time;

    public Head(Staff staff, int x, int y){
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.gTime(x);
        
        this.line = staff.lineOfY(y);
        //System.out.println("line:" + line);  // for debug
    }

    public void show(Graphics g){
        int H = staff.H();
        Glyph.HEAD_Q.showAt(g, H, time.x, staff.yLine(line));
    }
}
