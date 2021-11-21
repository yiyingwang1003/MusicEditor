package musicEd;

import musicEd.graphicsLib.*;
import musicEd.sanbox.*;
//import musicEd.sanbox.Squares;
//import musicEd.sanbox.Game;
//import musicEd.sanbox.Paint;

public class Main {
    public static void main(String [] args){
        System.out.println("Music Editor!");
        //Window.PANEL = new Squares();
        //Window.PANEL = new PaintInk();
       //Window.PANEL = new Game();
        //Window.PANEL = new Paint();
        //Window.PANEL = new ShapeTrainer();
        // Window.PANEL = new ReactionTest();
        Window.PANEL = new MusicApp();
        Window.launch();
    }
}
