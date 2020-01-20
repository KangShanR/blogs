import java.util.Arrays;

public class Test{
    public static void main(String[] args){
        String[] strs = new String[]{"adg", "adfsdf"};
        Arrays.stream(strs).forEach(System.out::println);;
        System.out.println("Hello, world!");
    }
}