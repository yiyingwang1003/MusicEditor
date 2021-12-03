package musicEd.music;

import musicEd.reaction.Mass;

public class Beam extends Mass{
    public Stem.List stems = new Stem.List();
    public Beam(Stem stem1, Stem stem2){
        super("NOTE");
        addStem(stem1);
        addStem(stem2);
    }

    public void addStem(Stem s){
        if (s.beam == null){
            stems.add(s);
            s.beam = this;
            s.nFlag = 1;
            stems.sort();
        }
    }
}
