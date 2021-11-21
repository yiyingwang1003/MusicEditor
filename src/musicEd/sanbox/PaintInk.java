package musicEd.sanbox;

import java.awt.*;

import java.awt.event.*;
import musicEd.graphicsLib.G;
import musicEd.graphicsLib.Window;
import musicEd.music.UC;
import musicEd.reaction.*;
import musicEd.reaction.Shape;

public class PaintInk extends Window{
    public static Ink.List inkList = new Ink.List();
    //test
    //static{inkList.add(new Ink());}
    public static Shape.Prototype.List pList = new Shape.Prototype.List();
    public PaintInk(){
        super("PaintInk", UC.mainWindowWidth, UC.mainWindowHeight);

    }
    public void paintComponent(Graphics g){
        G.fillBackground(g);
        g.setColor(Color.BLUE);
        inkList.show(g);
        g.setColor(Color.RED);
        Ink.BUFFER.show(g);
        g.drawString("count:"+Ink.BUFFER.n, 100, 100);
        //g.fillRect(100, 100, 100, 100);
        if(inkList.size() > 1) {
            int last = inkList.size() - 1;
            int dist = inkList.get(last).norm.dist(inkList.get(last - 1).norm);
            g.setColor(dist > UC.noMatchDist ? Color.RED : Color.BLACK);
            g.drawString("dist, " + dist, 60, 600);
        }
        pList.show(g);
    }

    public void mousePressed(MouseEvent me){
        Ink.BUFFER.dn(me.getX(), me.getY());
        repaint();
    }
    public void mouseDragged(MouseEvent me){
        Ink.BUFFER.drag(me.getX(), me.getY());
        repaint();
    }
    public void mouseReleased(MouseEvent me){    
        //inkList.add(new Ink());
        Ink ink = new Ink();
        Shape.Prototype proto;
        inkList.add(ink);
        if(pList.bestDist(ink.norm) < UC.noMatchDist) {
            proto = Shape.Prototype.List.bestMatch;
            proto.blend(ink.norm);

        } else {
            proto = new Shape.Prototype(); 
            pList.add(proto);
        }
        ink.norm = proto;
        repaint();
    }

}
