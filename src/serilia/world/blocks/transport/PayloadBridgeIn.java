package serilia.world.blocks.transport;

import arc.graphics.g2d.Draw;
import arc.math.geom.Geometry;
import arc.math.geom.Position;
import arc.util.Tmp;
import mindustry.gen.Building;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.PayloadBlock;
import mindustry.world.meta.BlockGroup;
import serilia.types.TransportLine;

import static mindustry.Vars.world;

public class PayloadBridgeIn extends PayloadBlock{
    public float range = 100;

    public PayloadBridgeIn(String name){
        super(name);
        rotate = true;
        update = true;
        solid = true;
        acceptsPayload = true;
        group = BlockGroup.transportation;
        quickRotate = false;
    }

    public class PayloadBridgeInBuild extends PayloadBlockBuild<Payload> implements PayloadLineBlock{
        public TransportLine<Payload> line;
        public float dst;
        public int lastChange = -2;

        @Override
        public void updateTile(){
            if(lastChange != world.tileChanges || line == null){
                lastChange = world.tileChanges;
                line = null;

                int offset = size / 2;
                for(int i = 1 + offset; i <= range + offset; i++){
                    var other = world.build(tile.x + i * Geometry.d4[rotation].x, tile.y + i * Geometry.d4[rotation].y);

                    if(other instanceof PayloadLineBlock l){
                        if(l.line() == null) break;

                        line = l.line();
                        line.addSegment(Tmp.v1.set(this).sub(l.lPos()));
                        dst = line.length;
                        break;
                    }
                }
            }

            if(payload != null){
                payload.update(null, this);
                if(line != null && moveInPayload() && line.insert(payload, payload.size(), dst)){
                    payload = null;
                }
            }
        }

        @Override
        public void draw(){
            super.draw();
            Draw.rect(region, x, y);
            drawPayload();
            Draw.rect(topRegion, x, y);
        }

        @Override
        public boolean acceptPayload(Building source, Payload payload){
            return this.payload == null;
        }

        @Override
        public TransportLine<Payload> line(){
            return line;
        }

        @Override
        public Position lPos(){
            return this;
        }
    }
}
