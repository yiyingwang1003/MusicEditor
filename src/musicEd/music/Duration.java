package musicEd.music;

import musicEd.reaction.Mass;
import java.awt.*;


public abstract class Duration extends Mass{  // abstract : some function are not implement
    public int nFlag = 0, nDot = 0;

    public Duration(){super("NOTE");}

    public abstract void show(Graphics g);  // because abstract, Duration does not know "show"
    public void incFlag(){if(nFlag < 4){nFlag++;}}
    public void dacFlag(){if(nFlag > -2){nFlag--;}}
    public void cycleDot(){nDot++; if (nDot > 3){nDot = 0;}}
}
