package musicEd.music;

import java.awt.Graphics;

public class Stem extends Duration{
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;

    public Stem(Staff staff, boolean isUp){
        this.staff = staff;
        this.isUp = isUp;
    }

    @Override
    public void show(Graphics g) {
        if (nFlag >= -1 && heads.size() > 0){
            int x = X(), h = staff.H(), yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x, yH, x, yB);
        }
        
    }

    public Head firstHead(){return heads.get(isUp ? heads.size() - 1 : 0);}
    public Head lastHead(){return heads.get(isUp ? 0 : heads.size() - 1);}

    public int yFirstHead(){
        Head h = firstHead();
        return h.staff.yLine(h.line);
    }

    public int yBeamEnd(){
        Head h = lastHead();
        int line = h.line;
        line += isUp ? -7 : 7; // default length of 7
        int flagInc = nFlag > 2 ? 2*(nFlag - 2) : 0;  // determine if the stem need to be extend
        line += isUp ? -flagInc : flagInc;
        if ((isUp && line > 4) || (!isUp && line < 4)){
            line = 4;
        }
        return h.staff.yLine(line);
    }

    public int X(){

        //return firstHead().time.x;
        Head h = firstHead();
        return h.time.x + (isUp ? h.W() : 0);
    }
    public void deleteStem(){
        deleteMass();
    }

    public void setWrongSide(){
        // stub
    }
    
}
