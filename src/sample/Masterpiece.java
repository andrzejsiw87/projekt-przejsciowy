package sample;

public class Masterpiece {

    private String author;
    private String picture;
    private int weight;
    private int width;
    private int height;

    Masterpiece(String author, String picture, int weight, int width, int height) {
        this.author = author;
        this.picture = picture;
        this.weight = weight;
        this.width = width;
        this.height = height;
    }

    public String getAuthor() {
        return author;
    }

    public String getPicture() {
        return picture;
    }

    public int getWeight() {
        return weight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Integer getArea() {
        return height * width;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
