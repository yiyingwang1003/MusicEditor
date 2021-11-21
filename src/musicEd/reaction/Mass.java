package musicEd.reaction;

import musicEd.music.I;
import java.awt.Graphics;

public abstract class Mass extends Reaction.List implements I.Show {
    public Layer layer;

    public Mass(String layerName) {
        layer = Layer.byName.get(layerName);
        if (layer != null) {
            layer.add(this);
        } else {
            System.out.println("Bad layerName!" + layerName);
        }
    }

    public void delete() {clearAll(); layer.remove(this);}

    // Explain this later.
    public boolean equals(Object o) {return this == o;}

    public void show(Graphics g){}
}