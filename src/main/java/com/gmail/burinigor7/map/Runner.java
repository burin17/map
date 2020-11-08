package com.gmail.burinigor7.map;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Runner {
    public static void main(String[] args) {
//        HashMap<Car, String> map = new HashMap<>();
//        for(int i = 0; i < 11; ++i)
//            map.put(new Car(i, Integer.toString(i)), Integer.toString(i));
//        map.put(new Car(1, "zxc"), "qwe");
//        map.put(new Car(1, "tg"), "zxc");
//        System.out.println(capacity(map));
//        Map.Entry<?, ?>[] table = table(map);
//        for(Map.Entry<?, ?> entry : table) {
//            if(entry == null) System.out.println("null");
//            else {
//                System.out.print(entry.getKey() + " ---> ");
//                bucketList(entry);
//            }
//        }
        Hashmap<Integer, String> map = new Hashmap<>();
        for(int i = 0; i < 12; ++i) {
            map.put(i, Integer.toString(i));
        }
        map.put(33, "33");
        System.out.println(map);
        System.out.println("capacity = " + capacity(map));
    }

    static int capacity(Hashmap<?, ?> map) {
        Class<? extends Hashmap> clss = map.getClass();
        try {
            Field f = clss.getDeclaredField("table");
            f.setAccessible(true);
            return ((Object[])f.get(map)).length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Map.Entry<?, ?>[] table(HashMap<?, ?> map) {
        Class<? extends HashMap> clss = map.getClass();
        try {
            Field tableField = clss.getDeclaredField("table");
            tableField.setAccessible(true);
            return (Map.Entry<?, ?>[]) tableField.get(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void bucketList(Map.Entry entry) {
        try {
            Class c = entry.getClass();
            Field map = c.getDeclaredField("next");
            map.setAccessible(true);
            Map.Entry next = (Map.Entry) map.get(entry);
            if(next != null) {
                System.out.print(next.getKey() + " ---> ");
                bucketList(next);
            } else System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Car {
    int i;
    String model;

    public Car(int i, String model) {
        this.i = i;
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return i == car.i &&
                Objects.equals(model, car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(i);
    }

    @Override
    public String toString() {
        return "Car{" +
                "i=" + i +
                ", model='" + model + '\'' +
                '}';
    }
}

class A {
    private String s;

    public A(String s) {
        this.s = s;
    }

}