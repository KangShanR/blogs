import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Test{
    public static void main(String[] args){
        // String[] strs = new String[]{"adg", "adfsdf"};
        // Arrays.stream(strs).forEach(System.out::println);

        Node<String, String> loHead = null, loTail = null;
        Node<String, String> hiHead = null, hiTail = null,
        e = new Node<String,String>(5, "key5", "", new Node<>(9, "key9", "", 
        new Node<>(13, "key13", "", new Node<>(17, "key17", "", null))));
        Node<String, String> next;
        
        do {
            next = e.next;
            if ((e.hash & 4) == 0) {
                if (loTail == null)
                    loHead = e;
                else
                    loTail.next = e;
                loTail = e;
            } else {
                if (hiTail == null)
                    hiHead = e;
                else
                    hiTail.next = e;
                hiTail = e;
            }
        } while ((e = next) != null);
        loTail.next = null;
        hiTail.next = null;
        System.out.println(loHead);
        System.out.println(hiHead);
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value + ";next:" + next;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }
}