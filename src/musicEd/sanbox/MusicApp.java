package musicEd.sanbox;

import musicEd.graphicsLib.G;
import musicEd.graphicsLib.Window;
// import musicEd.music.Beam;
// import musicEd.music.Glyph;
import musicEd.music.Page;
import musicEd.music.UC;
import musicEd.reaction.Gesture;
import musicEd.reaction.Ink;
import musicEd.reaction.Layer;
import musicEd.reaction.Reaction;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MusicApp extends Window{
    static{
        new Layer("BACK");
        new Layer("NOTE");
        new Layer("FORE");
        
    }

    public MusicApp(){
        super("Music", UC.mainWindowWidth, UC.mainWindowHeight);
        Reaction.initialReactions.addReaction(new Reaction("E-E"){  // create page
            public int bid(Gesture g){return 0;}
            public void act(Gesture g){
                new Page(g.vs.yM());
                this.disable();
            }
        });
    }

        // test for polygons
    // public static int[] xPoly = {100, 200, 200, 100};
    // public static int[] yPoly = {50, 70, 80, 60};
    // public static Polygon poly = new Polygon(xPoly, yPoly, 4);

    public void paintComponent(Graphics g){
        G.fillBackground(g);
        Layer.ALL.show(g);
        g.setColor(Color.BLACK);

        Ink.BUFFER.show(g);
        // if (Page.PAGE != null){
        //     int H = 32;
        //     Glyph.HEAD_Q.showAt(g, H, 200, Page.PAGE.yMargin.lo + 4 * H);
        //     g.setColor(Color.RED);
        //     g.drawRect(200, Page.PAGE.yMargin.lo + 3 * H, 24*H/10, 2*H);
        // }

        // if (Page.PAGE != null){
        //     Glyph.CLEF_G.showAt(g, 8, 100, Page.PAGE.yMargin.lo+4*8);
        //     Glyph.HEAD_W.showAt(g, 8, 200, Page.PAGE.yMargin.lo+4*8);

        // }
    }

    public void mousePressed(MouseEvent me){
        Gesture.AREA.dn(me.getX(), me.getY());
        repaint();
    }

    public void mouseDragged(MouseEvent me){
        Gesture.AREA.drag(me.getX(), me.getY());
        repaint();
    }

    public void mouseReleased(MouseEvent me){
        Gesture.AREA.up(me.getX(), me.getY());
        repaint();
    }
}
