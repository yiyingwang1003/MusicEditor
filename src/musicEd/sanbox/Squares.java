package musicEd.sanbox;

import musicEd.graphicsLib.Window;
import musicEd.music.I;
import musicEd.graphicsLib.G;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Squares extends Window{
    //public static G.VS theVS = new G.VS(100, 100, 200, 300);
    //public static Color color = G.rndColor();

    public static Square.List squares = new Square.List();
    public static Square theSquare;
    //public static boolean dragging = false;
    public static G.V mouseDelta = new G.V(0, 0);

    public static I.Area curArea;
    public static Square BACKGROUND = new Square(0, 0){
        @Override
        public void dn(int x, int y){
            //dragging = false;
            theSquare = new Square(x, y);
            squares.add(theSquare);
        }

        @Override
        public void drag(int x, int y){theSquare.reSize(x, y);}
    };
    static {
        BACKGROUND.color = Color.WHITE;
        BACKGROUND.size.set(5000, 5000);
        squares.add(BACKGROUND);
    }

    public Squares(){super("squares", 800, 800);}
    public void paintComponent(Graphics g){
        G.fillBackground(g);
        //theVS.fill(g, color);
        squares.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent me){
        //if(theVS.hit(me.getX(), me.getY())){
        //    color = G.rndColor();
        //    repaint();
        //}
        int x = me.getX(), y = me.getY();
        theSquare = squares.hit(x, y);
        curArea = squares.hit(x, y);
        curArea.dn(x, y);
        /*theSquare = squares.hit(x, y);
        if(theSquare == null){
            //dragging = false;
            theSquare = new Square(x, y);
            squares.add(theSquare);
        }else{
            //dragging = true;
            mouseDelta.set(theSquare.loc.x - x, theSquare.loc.y - y);
        }
        ///theSquare = new Square(x, y);
        //squares.add(new Square(me.getX(), me.getY()));
        ///squares.add(theSquare);*/
        repaint();
    }

    public void mouseDragged(MouseEvent me){
        int x = me.getX(), y = me.getY();
        curArea.drag(x, y);
        /*if(dragging){theSquare.move(x + mouseDelta.x, y + mouseDelta.y);} 
        else{theSquare.reSize(x, y);}*/
        repaint();
    }
    //--------------------------------------------
    public static class Square extends G.VS implements I.Area{
        Color color = G.rndColor();
        public Square (int x, int y){super(x, y, 100, 100);}

        public void reSize(int x, int y){
            if(x > loc.x && y > loc.y){
                size.set(x - loc.x, y - loc.y);
            }
        }

        public void move(int x, int y){
            loc.set(x,y);
        }
        //----------------------
        public static class List extends ArrayList<Square>{
            public void draw(Graphics g){for(Square s: this){s.fill(g, s.color);} }
            public Square hit(int x, int y){
                Square res = null;
                for(Square s: this){
                    if(s.hit(x,y)){
                        res = s;
                    }// return the last one
                }
                return res;
            }
        }
        @Override
        public void dn(int x, int y) {
            //dragging = true;
            mouseDelta.set(theSquare.loc.x - x, theSquare.loc.y - y);
            
        }

        @Override
        public void up(int x, int y) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void drag(int x, int y) {
            theSquare.move(x + mouseDelta.x, y + mouseDelta.y);
            
        }
    }
}
