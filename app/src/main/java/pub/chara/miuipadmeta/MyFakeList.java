package pub.chara.miuipadmeta;



import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyFakeList implements List {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains( Object o) {
        return true;
    }

   
    @Override
    public Iterator iterator() {
        return null;
    }

   
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        return true;
    }

    @Override
    public boolean remove( Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf( Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf( Object o) {
        return 0;
    }

   
    @Override
    public ListIterator listIterator() {
        return null;
    }

   
    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

   
    @Override
    public List subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return true;
    }

   
    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }
}
