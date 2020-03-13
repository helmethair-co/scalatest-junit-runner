package co.helmethair.scalatest.scala;

import scala.collection.Iterator;
import scala.collection.immutable.HashMap$;
import scala.collection.immutable.IndexedSeq;
import scala.collection.mutable.HashSet$;

import java.util.*;

public interface ScalaConversions {
    static <T> scala.collection.immutable.Set<T> asScalaSet(Set<T> set) {
        scala.collection.mutable.HashSet<T> scalaSet = HashSet$.MODULE$.empty();
        //method reference is not virtual enough to be compatible with multiple Scala versions
        set.forEach(e -> scalaSet.add(e));
        return scalaSet.toSet();
    }

    static <T> Set<T> setAsJavaSet(scala.collection.immutable.Set<T> set) {
        Iterator<T> iterator = set.toIterator();
        Set<T> javaSet = new HashSet<>();
        while (iterator.hasNext()) {
            T e = iterator.next();
            javaSet.add(e);
        }
        return javaSet;
    }

    static <T> Collection<T> asJavaCollection(IndexedSeq<T> seq) {
        List<T> javaCollection = new ArrayList<>(seq.size());
        Iterator<T> iterator = seq.toIterator();
        while (iterator.hasNext()) {
            T e = iterator.next();
            javaCollection.add(e);
        }
        return javaCollection;
    }

    static <K, V> scala.collection.immutable.Map<K, V> asScalaMap(java.util.Map<K, V> map) {
        scala.collection.mutable.HashMap<K, V> scalaMap = scala.collection.mutable.HashMap$.MODULE$.empty();
        map.forEach(scalaMap::put);
        return HashMap$.MODULE$.<K, V>empty().$plus$plus(scalaMap.toSeq());
        java.util.Iterator<Map.Entry<K, V>> i = map.entrySet().iterator();
    }
}
