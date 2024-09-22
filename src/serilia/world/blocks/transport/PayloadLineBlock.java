package serilia.world.blocks.transport;

import arc.math.geom.Position;
import mindustry.world.blocks.payloads.Payload;
import serilia.types.TransportLine;

public interface PayloadLineBlock{
    TransportLine<Payload> line();
    Position lPos();
}
