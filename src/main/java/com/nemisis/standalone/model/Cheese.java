package com.nemisis.standalone.model;

public class Cheese implements Cloneable {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public Cheese clone() {
        Cheese cheese = new Cheese();
        cheese.setAge(this.getAge());
        return cheese;
    }

    @Override
    public int hashCode() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cheese cheese = (Cheese) o;

        return age == cheese.age;
    }

    @Override
    public String toString() {
        return "Cheese{" +
            "age=" + age +
            '}';
    }
}