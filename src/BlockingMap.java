import java.util.LinkedList;
import java.util.Map;

public class BlockingMap {

    public Segment[] segments = new Segment[32];
    volatile int  size = 0;

    protected static final class Segment {

        public LinkedList<Entry> entryList = new LinkedList<>();

        public Segment() {
        }


        protected synchronized int getCount() {
            return this.entryList.size();
        }



    }

    void add(Object key, Object value)
    {
        Entry newEntry = new Entry( hash(key), key,value);

        //Блокируем сегмент прежде чем туда что-то добавлять

        if(segments[ newEntry.hash ] == null)
            segments[ newEntry.hash ] = new Segment();
        synchronized ( segments[ newEntry.hash ] ) {
            segments[newEntry.hash].entryList.add(newEntry);
            size++;
        }
    }

    void  set(Object key, Object newValue)
    {

        if(get(key) == null)
            add(key,newValue);
        else
        {
            int h = hash(key);
            if(segments[ h] == null)
                segments[ h] = new Segment();
            synchronized ( segments[h])
            {
                for ( Entry e : segments[h].entryList)
                {
                    if(e.key == key) {
                        e.value = newValue;
                        return;
                    }
                }
            }
        }



    }

    Object get(Object key)
    {
       int h = hash(key);
        if(segments[ h] == null)
            segments[ h] = new Segment();
       for(Entry e :  segments[h].entryList)
       {
           if(e.key.equals(key))
               return e.value;
       }
       return null;
    }

    void remove(Object key)
    {
        if( get( key)==null)
            return;

        int h = hash(key);
        if(segments[ h] == null)
            segments[ h] = new Segment();
        synchronized (segments[h])
        {
            segments[h].entryList.removeIf( (Entry e)->e.key.equals(key) );
            size --;
        }
    }


    static int hash(Object x) {
        int h = Math.abs( x.hashCode() );
        return h%32;
    }


    static class Entry implements Map.Entry {
        protected final Object key;
        protected volatile Object value;
        protected final int hash;


        public Entry(int hash, Object key, Object value) {

            this.value = value;
            this.hash = hash;
            this.key = key;
        }

        @Override
        public Object getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            synchronized (this.value) {
                this.value = value;
            }
            return value;
        }

        // Code goes here like getter/setter
    }




}
