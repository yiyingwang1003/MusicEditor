package musicEd.music;

import java.awt.Color;
import java.awt.Graphics;

import musicEd.graphicsLib.G;
import musicEd.reaction.Gesture;
import musicEd.reaction.Mass;
import musicEd.reaction.Reaction;

public class Staff extends Mass{

    public Sys sys;  // dad
    public int iStaff;  // index in the sys
    public Staff.Fmt fmt;
    public Clef initialClef = null;

    public Staff(Sys sys, int iStaff){
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        fmt = sys.page.sysFmt.get(iStaff);


        addReaction(new Reaction("S-S"){

            @Override
            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                G.LoHi m = Page.PAGE.xMargin;
                if (x < m.lo || x > m.hi){return UC.noBid;}
                int d = Math.abs(y1-Staff.this.yTop()) + Math.abs(y2-Staff.this.yBot());
                return (d<30) ? d : UC.noBid;
            }

            @Override
            public void act(Gesture gest) {
                int rightMargin = Page.PAGE.xMargin.hi;
                int x = gest.vs.xM();
                if (x > rightMargin - UC.marginSnap) { // x is close to the right margin
                    x = rightMargin;
                }
                new Bar(Staff.this.sys, x);
                
            }
            
        });

        addReaction(new Reaction("S-S") { // Toggle bar continues.
            public int bid(Gesture gest) {
                if (Staff.this.sys.iSys != 0) return UC.noBid; // only bar continues in first system.
                int y1 = gest.vs.yL(), y2 = gest.vs.yH();
                int iStaff = Staff.this.iStaff;
                int iLastStaff = Page.PAGE.sysFmt.size() - 1;
                if (iStaff == iLastStaff) return UC.noBid; // this is the last staff which cannot continue.
                if (Math.abs(y1 - Staff.this.yBot()) > 20) return UC.noBid;
                Staff nextStaff = Staff.this.sys.staffs.get(iStaff + 1);
                if (Math.abs(y2 - nextStaff.yTop()) > 20) return UC.noBid;
                return 10;
            }

            public void act(Gesture gest) {
                Page.PAGE.sysFmt.get(Staff.this.iStaff).toggleBarContinues();
            }
        });

        addReaction(new Reaction("SE-SW"){  // F clef
            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                G.LoHi m = Page.PAGE.xMargin;
                if(x < m.lo || x > m.hi){return UC.noBid;}
                int d = Math.abs(y1 - Staff.this.yTop()) + Math.abs(y2 - Staff.this.yBot());
                return (d > 50)? UC.noBid : d;
            }

            public void act(Gesture gest) {
               Staff.this.initialClef = Clef.F;
            }

        });

        addReaction(new Reaction("SW-SE"){  // G clef
            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                G.LoHi m = Page.PAGE.xMargin;
                if(x < m.lo || x > m.hi){return UC.noBid;}
                int d = Math.abs(y1 - Staff.this.yTop()) + Math.abs(y2 - Staff.this.yBot()); // ? =
                return (d > 50)? UC.noBid : d;
            }

            public void act(Gesture gest) {
               Staff.this.initialClef = Clef.G;
            }

        });

    }

    public int yTop(){return sys.yTop() + sysOff();}
    public int yBot(){return yTop() + fmt.height();}
    public int sysOff(){return sys.page.sysFmt.staffOffsets.get(iStaff);}
    public void show(Graphics g){
        G.LoHi m = Page.PAGE.xMargin;
        if (initialClef != null){
            initialClef.showAt(g, m.lo+4*fmt.H, yTop(), fmt.H);
        }
    }

    //----------format--------------
    public static class Fmt{
        public int nLines = 5, H = 8;  // 8=half gap between two lines
        public boolean barContinues = false;

        public void toggleBarContinues() {barContinues = !barContinues;}

        public int height(){return 2 * H * (nLines - 1);}
        
        public void showAt(Graphics g, int y, Page page){
            g.setColor(Color.GRAY);
            int x1 = page.xMargin.lo, x2 = page.xMargin.hi, h = 2*H;
            for (int i = 0; i < nLines; i++){
                g.drawLine(x1, y, x2, y);
                y += h;  // only change locally
            }
        }
    }//-----------format--------------
}
