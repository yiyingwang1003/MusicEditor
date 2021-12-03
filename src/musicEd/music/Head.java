package musicEd.music;

import musicEd.reaction.Gesture;
import musicEd.reaction.Mass;
import musicEd.reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;


public class Head extends Mass implements Comparable<Head>{
    public Staff staff;
    public int line;  // line is the y coodinate
    public Time time;
    public Stem stem = null;
    public boolean wrongSide = false;
    public Glyph forcedGlyph = null;  // for violin cases

    public Head(Staff staff, int x, int y){
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.gTime(x);
        
        this.line = staff.lineOfY(y);
        //System.out.println("line:" + line);  // for debug

        time.heads.add(this);

        // add reaction
        addReaction(new Reaction("S-S"){  // stem or unstem
            public int bid(Gesture gest) {
                System.out.println("Bidding...");
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                int W = Head.this.W();
                if(y1 > y || y2 < y){return UC.noBid;}  // out of range
                int hL = Head.this.time.x, hR = hL + W;
                if (x < hL - W || x > hR + W){return UC.noBid;}
                if (x < hL + W / 2){return hL - x;}
                if (x > hR - W / 2){return x - hR;}

                return UC.noBid;
            }

            public void act(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                int W = Head.this.W();
                Staff staff = Head.this.staff;
                Time t = Head.this.time;
                boolean up = (x > t.x + W / 2);
                if (Head.this.stem == null){
                    t.stemHeads(staff, up, y1, y2);
                }else{
                    t.unStemHeads(y1, y2);
                }
            }

        });

        addReaction(new Reaction("DOT"){
            public int bid(Gesture gest){
                int xH = Head.this.X(), yH = Head.this.Y(), h = Head.this.staff.H(), w = Head.this.W();
                int x = gest.vs.xM(), y = gest.vs.yM();
                if (x < xH || x > xH + 2 * w || y < yH - h || y > yH + h){return UC.noBid;}
                return Math.abs(xH + w - x) + Math.abs(yH - y);
            }
            public void act(Gesture gest){
                if (Head.this.stem != null){
                    Head.this.stem.cycleDot();
                }
            }
        });
    }

    public int W(){return 24*staff.H()/10;}
    public int Y(){return staff.yLine(line);}
    public int X(){
        int res = time.x;
        if (wrongSide){
            res += (stem != null && stem.isUp) ? W() : -W();
        }
        return res;
    }

    public void delete(){ // stub
        time.heads.remove(this);
    }
    public void unStem(){
        if (stem != null){
            stem.heads.remove(this);  // get off the old stem
            if (stem.heads.size() == 0){
                stem.deleteStem();  // delete the empty stem
            }
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s){
        if(stem != null){
            unStem();
        }
        s.heads.add(this);
        stem = s;
    }
    public void show(Graphics g){
        int H = staff.H();
        Glyph glyph = forcedGlyph != null ? forcedGlyph : normalGlyph();

        // g.setColor(wrongSide? Color.GREEN : Color.BLUE);
        // if (stem != null && stem.heads.size() != 0 && this == stem.firstHead()){
        //     g.setColor(Color.RED);
        // }
        g.setColor(Color.BLACK);
        glyph.showAt(g, H, X(), staff.yLine(line));

        if (stem != null){
            int off = UC.restAugDotOffSet, sp = UC.augDotSpacing;
            for (int i = 0; i<stem.nDot; i++){
                g.fillOval(time.x + off + i * sp, Y() - 3 * H / 2, 2 * H / 3, 2 * H / 3);

            }
        }
    }
    public Glyph normalGlyph(){
        if (stem == null){return Glyph.HEAD_Q;}
        if (stem.nFlag == -1){return Glyph.HEAD_HALF;}
        if (stem.nFlag == -2){return Glyph.HEAD_W;}

        return Glyph.HEAD_Q;
    }

    @Override
    public int compareTo(Head o) {
        return (staff.iStaff != o.staff.iStaff) ? staff.iStaff - o.staff.iStaff : line - o.line;
    }

    //-----------list-------------
    public static class List extends ArrayList<Head>{
        public void sort(){
            Collections.sort(this);
        }
    }

   
}
