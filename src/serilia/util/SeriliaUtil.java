package serilia.util;

import arc.Core;
import arc.graphics.g2d.TextureRegion;

public class SeriliaUtil {
    public static TextureRegion[][] splitLayers(String name, int size, int layerCount){
        TextureRegion[][] layers = new TextureRegion[layerCount][];

        for(int i = 0; i < layerCount; i++){
            layers[i] = split(name, size, i);
        }
        return layers;
    }
    public static TextureRegion[] split(String name, int size, int layer){
        TextureRegion tex = Core.atlas.find(name);
        int margin = 2;
        int countX = tex.width / size;
        TextureRegion[] tiles = new TextureRegion[countX];

        for(int step = 0; step < countX; step++){
            tiles[step] = new TextureRegion(tex, 1 + (step * (margin + size)), 1 + (layer * (margin + size)), size, size);
        }
        return tiles;
    }
}
