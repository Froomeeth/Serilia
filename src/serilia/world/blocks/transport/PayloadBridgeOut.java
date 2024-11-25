package serilia.world.blocks.transport;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.graphics.Layer;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.PayloadBlock;
import mindustry.world.meta.BlockGroup;
import serilia.types.TransportLine;

import static mindustry.Vars.world;

public class PayloadBridgeOut extends PayloadBlock{
    public float speed = 1f;

    public PayloadBridgeOut(String name){
        super(name);
        rotate = true;
        update = true;
        solid = true;
        group = BlockGroup.transportation;
        outputsPayload = true;
        acceptsPayload = false;
        quickRotate = false;
    }

    public class PayloadBridgeOutBuild extends PayloadBlockBuild<Payload> implements PayloadLineBlock{
        public TransportLine<Payload> line = new TransportLine<>();
        public int lastChange = -2;

        @Override
        public void updateTile(){
            if(lastChange != world.tileChanges){
                lastChange = world.tileChanges;
                line.clearSegments();
            }

            line.update(speed);

            if(payload != null){
                payload.update(null, this);
                moveOutPayload();
            }else{
                if((payload = line.checkAndClearFirst()) != null){
                    payload.set(x, y, rotdeg());
                }
            }
        }

        @Override
        public void draw(){
            super.draw();

            Draw.rect(region, x, y);
            Draw.rect(outRegion, x, y, rotdeg());
            drawPayload();
            Draw.rect(topRegion, x, y);

            if(line.gaps.isEmpty() || line.segLenRot.isEmpty()) return;

            float countSeg = 0f, countGap = line.gaps.get(0);
            Vec2 p = Tmp.v1.set(this);
            Vec2 v = Tmp.v2.set(1f, 1f);
            Vec2 last = Tmp.v3.set(this);
            int j = 0;


            for(int i = 0 ; i < line.segLenRot.size; i += 2){ //for(int i = line.segLenRot.size - 2 ; i > 0; i -= 2){
                countSeg += line.segLenRot.get(i);
                v.setAngle(line.segLenRot.get(i + 1));
                p.add(v.setLength(line.segLenRot.get(i)));

                Draw.z(100);
                Mathf.rand.setSeed(i);
                Lines.stroke(4, Tmp.c1.rand());
                Lines.line(last.x, last.y, p.x, p.y);
                last = p;
                Draw.z(35);


                Draw.z(Layer.flyingUnitLow);
                while(j < line.gaps.size && countGap - line.sizes.get(j)/2 <= countSeg){
                    Payload item = line.items.get(j);
                    v.setLength(countSeg - countGap - line.sizes.get(j)/2);

                    item.set(p.getX() - v.x, p.getY() - v.y, line.segLenRot.get(i + 1));
                    item.draw();

                    countGap += (j + 1 < line.gaps.size ? line.gaps.get(j + 1) : 0f) + line.sizes.get(j);
                    j++;
                }
            }
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
