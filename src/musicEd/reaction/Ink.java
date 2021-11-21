package musicEd.reaction;
//import java.awt.Color;
import java.awt.*;
import java.util.ArrayList;

import musicEd.music.*;
import musicEd.graphicsLib.G;
import musicEd.graphicsLib.G.BBox;

public class Ink implements I.Show{
    public Norm norm;
    public G.VS vs;
    public static G.VS TEMP = new G.VS(100, 100, 100, 100);
    public static Buffer BUFFER = new Buffer();
    public static final int K = UC.normSampleSize;
    public Ink() {
        //super(BUFFER.n);
        //for (int i = 0; i<BUFFER.n; i++){
        //    points[i].set(BUFFER.points[i]);
        //}
        norm = new Norm();
        vs = BUFFER.bbox.getNewVS();
    }
    
    @Override
    public void show(Graphics g){
        g.setColor(UC.inkColor);
        norm.drawAt(g, vs);
        //draw(g);
    }

    //-----------------Buffer----------------------------
    public static class Buffer extends G.PL implements I.Show, I.Area{
        public static final int MAX = UC.inkBufferMax;
        public int n; // how many points that actually in Buffer
        public BBox bbox = new BBox();
        private Buffer(){super(MAX);}//private: we only want one buffer 
        public void add(int x, int y){
            if(n<MAX){
                points[n].set(x, y);
                n++;// equal to "points[n++].set(x,y);" ---> standard C programming
                bbox.add(x, y);
            }
        }

        public void subSample(G.PL pl) {
            int K = pl.size();
            for (int i = 0; i < K; i++) {
                pl.points[i].set(this.points[i * (n - 1) / (K - 1)]); // linear interpretation (copy the first to the first, last to the last, the rest are prpportionately)
            }
        }

        public void clear(){n=0;}
        public void show(Graphics g){
            drawN(g, n);
            //bbox.draw(g);
        }

        public boolean hit(int x, int y){return true;}
        public void dn(int x, int y){clear(); bbox.set(x, y); add(x, y);}
        public void up(int x, int y){}
        public void drag(int x, int y){add(x, y);}
    }
    //-----------------Buffer----------------------------
    //-------------------Ink.list-------------------------
    public static class List extends ArrayList<Ink> implements I.Show{

        @Override
        public void show(Graphics g) {for (Ink ink: this){ink.show(g);}}
        
    }
    //-------------------Ink.list---------------------------
    // ------------------------Ink.Norm-----------------------------------
    public static class Norm extends G.PL{
        public static final int N = UC.normSampleSize, MAX = UC.normCoordMax;
        public static final G.VS NCS = new G.VS(0, 0, MAX, MAX);

        public Norm() {
            super(N);
            BUFFER.subSample(this);
            G.V.T.set(BUFFER.bbox, NCS);
            this.transform();
        }

        public void drawAt(Graphics g, G.VS vs) {
            G.V.T.set(NCS, vs);
            for (int i = 1; i < N; i++) {
                g.drawLine(points[i - 1].tx(), points[i - 1].ty(), points[i].tx(), points[i].ty());
            }
        }
        public int dist(Norm n) {
            int res = 0;
            for(int i = 0; i < N; i++) {
                int dx = points[i].x - n.points[i].x, dy = points[i].y - n.points[i].y;
                res += dx * dx + dy * dy;
            }
            return res;
        }

        public void blend(Norm norm, int nblend) {
            for(int i = 0; i < N; i++) {
                points[i].blend(norm.points[i], nblend);
            }
        }
    }// ------------------------Ink.Norm-----------------------------------
}
