package serilia.types;

import arc.math.geom.Vec2;
import arc.struct.FloatSeq;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;

/**Similar purpose as the power graph, but for belts and adjacent things. Implementation follows that of Factorio.*/
public class TransportLine<T>{
    /**What the line contains. Technically does nothing.*/
    public Seq<T> items = new Seq<>();
    /**Store item size and gap to the next item. TODO REWORK: MERGE GAP AND SIZE SEQS!*/
    public FloatSeq sizes = new FloatSeq();
    public FloatSeq gaps = new FloatSeq();

    /**Length and rotation of drawn segments.*/
    public FloatSeq segLenRot = new FloatSeq(); //this could just be vectors but calling len() every time is stupid
    /**Total length of the line. Used to add items to the end of it, and some linking stuff. TODO not very stable since items can just shoot out the back sometimes.*/
    public float length = 0;

    public TransportLine(){}


    /**Moves items on transport line. Returns whether the line is empty/full and can be skipped in the next updates.*/
    public boolean update(float speed){
        float move = speed * Time.delta;
        if(!gaps.isEmpty()){
            for(int i = 0; i < gaps.size; i++){
                gaps.incr(i, -Math.min(gaps.get(i), move));
                move -= gaps.get(i);
                if(move <= 0f) return false;
            }
        }
        return true;
    }


    /**Add a line segment for drawing purposes.*/
    public void addSegment(float len, float rot){
        segLenRot.add(len, rot);
        length += len;
    }
    /**Add a line segment for drawing purposes.*/
    public void addSegment(Vec2 vec){
        segLenRot.add(vec.len(), vec.angle());
        length += vec.len();
    }

    public void clearSegments(){
        segLenRot.clear();
        length = 0f;
    }


    /**Attempts adding an item to the end of the line. Returns whether the item was added.*/
    public boolean add(T item, float size){
        float gap = length - (gaps.sum() + sizes.sum()); //should not need two loops, but if it doesn't cause issues I don't care
        if(gap >= size / 2){
            ad(item, size, gap);
            return true;
        }
        return false;
    }

    /**Attempts inserting an item onto the line at a specified distance from the start of the line. Returns whether item was added.*/
    public boolean insert(T item, float size, float dst){
        float count = 0f;
        float prev = 0f;

        if(!gaps.isEmpty()){
            for(int i = 0; i < gaps.size; i++){
                if(prev >= dst) return false; //it doesn't stop where it should sometimes

                float gap = gaps.get(i);
                count += gap + sizes.get(i);

                if(count >= dst && gap >= size /*&& dst - count <= size * 0.8f*/){ //if closest possible gap and large enough
                    gaps.incr(i, -size);
                    ins(i, item, size, Math.min(dst - prev, gap - size));
                    return true;
                }
                prev = count;
            }
        }

        if(/*dst <= length &&*/ count < dst && dst - count > size){ //if last/only item on the line
            ad(item, size, gaps.isEmpty() ? dst : dst - count);
            return true;
        }
        return false;
    }



    /**Returns whether the first item is close enough to the front.*/
    public boolean checkFirst(){
        return !gaps.isEmpty() && gaps.first() <= 0.0001f;
    }

    /**Removes and returns the first item.*/
    public T clearFirst(){
        if(gaps.isEmpty()) return null;

        float s =
        sizes.removeIndex(0);
        gaps.removeIndex(0);                       //Ideally you wouldn't have it copy the whole arrays over thrice
        if(!gaps.isEmpty()) gaps.incr(0, s); //but if it causes problems I'll make the index considered first change instead.
        return items.remove(0);
    }

    /**Removes and returns the first item if it is close enough to the front.*/
    public T checkAndClearFirst(){
        if(checkFirst()) return clearFirst();
        return null;
    }



    void ins(int i, T item, float size, float gap){
        sizes.insert(i, size);
        gaps.insert(i, gap);
        items.insert(i, item);
    }

    void ad(T item, float size, float gap){
        sizes.add(size);
        gaps.add(gap);
        items.add(item);
    }
}
