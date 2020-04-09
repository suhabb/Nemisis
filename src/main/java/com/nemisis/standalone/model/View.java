package com.nemisis.standalone.model;

public class View {
    private int age;
    private int weight;
    private int height;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "View{" +
            "age=" + age +
            ", weight=" + weight +
            ", height=" + height +
            '}';
    }

    @Override
    public int hashCode() {
        int result = age;
        result = 31 * result + weight;
        result = 31 * result + height;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        View view = (View) o;

        if (age != view.age) return false;
        if (height != view.height) return false;
        if (weight != view.weight) return false;

        return true;
    }
}