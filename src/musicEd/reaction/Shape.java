package musicEd.reaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import musicEd.graphicsLib.G;
import musicEd.music.UC;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Shape implements Serializable{
    public static String fileName = UC.pathToShapeDB;
    public String name;
    public Prototype.List prototypes = new Prototype.List();
    //public static HashMap<String, Shape> DB = loadShapeDB(); // DB=database. map: a string has a corresponding shape
    public static Database DB = Database.load();
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> SHAPES = DB.values(); //values() is a function in map class. collection: the shapes in the map

    public Shape(String name) {
        this.name = name;
    }

    public static Shape recognize(Ink ink) { // can return null.
        if (ink.vs.size.x < UC.dotSize && ink.vs.size.y < UC.dotSize) {return DOT;}
        Shape bestMatch = null; int bestSoFar = UC.noMatchDist;
        for (Shape s : SHAPES) {
            int d = s.prototypes.bestDist(ink.norm);
            if (d < bestSoFar) {bestMatch = s; bestSoFar = d;}
        }
        return bestMatch;
    }

    public static void saveDB() {DB.save();}

    //---------------------------Shape.Prototype--------------------------------
    public static class Prototype extends Ink.Norm {
        int nBlend = 1;

        public void blend(Ink.Norm norm) {
            blend(norm, nBlend++);
        }

        //---------------------------Shape.Prototype.List-------------------------
        public static class List extends ArrayList<Prototype> {
            private static int m = 10, w = 60; // m stands for margin
            private static G.VS showBox = new G.VS(m, m, w, w);
            public static Prototype bestMatch; // set as a side-effect in bestDist
            
            public void show(Graphics g) {
                g.setColor(Color.ORANGE);
                for (int i = 0; i < size(); i++) {
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }
            }

            public int bestDist(Ink.Norm norm) {
                bestMatch = null;
                int bestSoFar = UC.noMatchDist;
                for (Prototype p : this) {
                    int d = p.dist(norm);
                    if (d < bestSoFar) {
                        bestMatch = p;
                        bestSoFar = d;
                    }
                }
                return bestSoFar;
            }

            public void train(Ink.Norm norm) {
                if (bestDist(norm) < UC.noMatchDist) {
                    bestMatch.blend(norm);
                } else {
                    // this is not similar to any existing prototypes, so we add it.
                    add(new Shape.Prototype());
                }
            }
        } //---------------------------Shape.Prototype.List-------------------------
    }//---------------------------Shape.Prototype--------------------------------

    //---------------------------Shape.Database--------------------------------
    public static class Database extends HashMap<String, Shape> {
        private Database() { // singleton
            super();
            put("DOT", new Shape("DOT"));
        } 

        public Shape forceGet(String name) { // will always succeed
            if (!DB.containsKey(name)) {DB.put(name, new Shape(name));}
            return DB.get(name);
        }

        public static boolean isLegal(String name) {
            return !name.equals("") && !name.equals("DOT");
        }

        public void train(String name, Ink.Norm norm) {
            if (isLegal(name)) {
                forceGet(name).prototypes.train(norm);
            }
        }

        public static Database load() {
            Database res = null;
            try {
                System.out.println("Attempting DB Load..." + fileName);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
                res = (Database) ois.readObject();
                System.out.println("Successfully Loaded. Found: " + res.keySet());
                ois.close();
            } catch (Exception e) {
                System.out.println("Load DB Failed.");
                System.out.println(e);
                res = new Database();
            }
            return res;
        }

        public void save() {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
                oos.writeObject(DB);
                System.out.println("Successfully Saved: " + fileName);
                oos.close();
            } catch (Exception e) {
                System.out.println("Save DB Failed.");
                System.out.println(e);
            }
        }
    }//---------------------------Shape.Database--------------------------------

    // public static HashMap<String, Shape> loadShapeDB(){  
    //     HashMap<String, Shape> res = new HashMap<>();
    //     res.put("DOT", new Shape("DOT"));
    //     try{
    //         System.out.println("Attempting DB load..." + fileName);
    //         ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
    //         res = (HashMap<String, Shape>) ois.readObject();
    //         System.out.println("DB successfully load found-" + res.keySet());  // keyset: always the string found in database
    //         ois.close();
    //     }catch(Exception e){
    //         System.out.println("Load DB fail.");
    //         System.out.println(e);
    //     }
    //     return res;
    // }

    // public static void saveShapeDB(){
    //     try{
    //         ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
    //         oos.writeObject(DB);
    //         System.out.println("DB successfully saved-" + fileName);
    //         oos.close();
    //     }catch(Exception e){
    //         System.out.println("Save fail.");
    //         System.out.println(e);
    //     }
    // }
    // public static Shape recognize(Ink ink){//can return null
    //     if (ink.vs.size.x < UC.dotSize && ink.vs.size.y < UC.dotSize){return DOT;}
    //     Shape bestMatch = null; int bestSoFar = UC.noMatchDist;
    //     for(Shape s : SHAPES){
    //         int d = s.prototypes.bestDist(ink.norm);
    //         if(d < bestSoFar){bestMatch = s; bestSoFar= d;}
    //     }
    //     return bestMatch;
    // }
    // //--------------------------Prototype-------------------------//
    // public static class Prototype extends Ink.Norm{
    //     int nBlend = 1;
    //     public void blend(Ink.Norm norm) {
    //         blend(norm, nBlend++);
    //     }
    //     //--------------Prototype.List--------------------//
    //     public static class List extends ArrayList<Prototype> {
    //         public static Prototype bestMatch;// Set as a side affect
    //         private static int m = 10, w = 60;
    //         private static G.VS showBox = new G.VS(m, m, w, w);
    //         public void show(Graphics g) {
    //             g.setColor(Color.ORANGE);
    //             for(int i = 0; i < size(); i++) {
    //                 Prototype p = get(i);
    //                 int x = m + i * (m + w);
    //                 showBox.loc.set(x, m);
    //                 p.drawAt(g, showBox);
    //                 g.drawString("" + p.nBlend, x, 20);
    //             }
    //         }
    //         public int bestDist(Ink.Norm norm) {
    //             bestMatch = null;
    //             int bestSoFar = UC.noMatchDist;
    //             for(Prototype p: this) {
    //                 int d = p.dist(norm);
    //                 if(d < bestSoFar) {
    //                     bestMatch = p;
    //                     bestSoFar = d;
    //                 }
    //             }
    //             return bestSoFar;
    //         }
    //     }
    // }
}
