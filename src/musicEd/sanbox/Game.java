package musicEd.sanbox;

import musicEd.graphicsLib.Window;
import musicEd.music.I;
import musicEd.music.UC;
import musicEd.graphicsLib.G;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.Timer;
import java.awt.event.MouseEvent;


public class Game extends Window implements ActionListener{
    //public static G.VS theVS = new G.VS(100, 100, 200, 300);
    //public static Color color = G.rndColor();

    public static Square.List squares = new Square.List();
    public static Square theSquare;
    public static boolean dragging = false;
    public static G.V mouseDelta = new G.V(0, 0);
    public static Timer timer;
    public static G.V pressedLoc = new G.V(0, 0);

    public static final int WW = UC.mainWindowWidth, WH = UC.mainWindowHeight; // define constant, final = do not change

    public Game(){
        super("Game", WW, WH);
        timer = new Timer(30, this); //Game itself is the listener
        timer.setInitialDelay(5000);
        timer.start();
    }
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
        if(theSquare == null){
            dragging = false;
            theSquare = new Square(x, y);
            squares.add(theSquare);
        }else{
            pressedLoc.set(x, y);
            theSquare.dv.set(0, 0);
            dragging = true;
            mouseDelta.set(theSquare.loc.x - x, theSquare.loc.y - y);
        }
        ///theSquare = new Square(x, y);
        //squares.add(new Square(me.getX(), me.getY()));
        ///squares.add(theSquare);
        repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent me){
        if(dragging){
            theSquare.dv.set(me.getX() - pressedLoc.x, me.getY() - pressedLoc.y);
        }

    }

    public void mouseDragged(MouseEvent me){
        int x = me.getX(), y = me.getY();
        if(dragging){theSquare.move(x + mouseDelta.x, y + mouseDelta.y);} 
        else{theSquare.reSize(x, y);}
        repaint();
    }
    //--------------------------------------------
    public static class Square extends G.VS implements I.Draw{
        Color color = G.rndColor();

        public G.V dv = new G.V(G.rnd(20) - 10, G.rnd(20) - 10);

        public Square (int x, int y){super(x, y, 100, 100);}

        public void reSize(int x, int y){
            if(x > loc.x && y > loc.y){
                size.set(x - loc.x,y - loc.y);
            }
        }

        public void move(int x, int y){loc.set(x,y);}

        public void draw(Graphics g){
            fill(g, color);
            moveAndBounce();
        }

        public void moveAndBounce(){
            if(xL() < 0 && dv.x < 0){dv.x = -dv.x;}
            if(xH() > WW && dv.x > 0){dv.x = -dv.x;}
            if(yL() < 0 && dv.y < 0){dv.y = -dv.y;}
            if(yH() > WH && dv.y > 0){dv.y = -dv.y;}
            loc.add(dv);

        }

        //----------------------
        public static class List extends ArrayList<Square>{
            public void draw(Graphics g){for(Square s: this){s.draw(g);} }
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
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        
    }
}
