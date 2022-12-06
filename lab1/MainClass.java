import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Scanner;

//Варіант 14
// +Задано квадрат і пряму, які не мають спільних точок.
// +Через центр квадрата проведено перпендикуляр до прямої.
// +Квадрат, обертаючись навколо свого центру проти годинникової стрілки,
// +рухається до прямої так, що центр квадрата залишається на перпендикулярі.
// +Зупинити рух квадрата в момент його дотику з прямою.
public class MainClass extends PApplet {
    public static void main(String[] args) {
        PApplet.main("MainClass", args);
    }

    class Point {
        public float x;
        public float y;
        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
        public float calcLength(Point other){
            return (float) Math.sqrt(Math.pow((this.x - other.x),2) + Math.pow((this.y - other.y),2));
        }
        public float calcLength(float x, float y){
            return (float) Math.sqrt(Math.pow((this.x - x),2) + Math.pow((this.y - y),2));
        }
    }

    class Square{
        public float side;
        Square(float s){
            this.side = s;
        }
    }
public int countOfPoints = 0;
public Point Oy_st, Oy_en, Ox_st, Ox_en;
public Square square;
public float x = 0 , y = 0;
public float angle = 0;
public float m = 0, n = 0;
public float l = 0;

public void mouseReleased(){
    if (countOfPoints == 0) {
        Oy_st = new Point(mouseX, mouseY);
        countOfPoints+=1;
    }
    else if (countOfPoints == 1) {
        Oy_en = new Point(mouseX, mouseY);
        countOfPoints+=1;
    }
    else if (countOfPoints == 2) {
        Ox_st = new Point(mouseX, mouseY);
        countOfPoints+=1;
        m = mouseX;
        n = mouseY;
        x = (Oy_st.x * Oy_st.x * Ox_st.x - 2 * Oy_st.x * Oy_en.x * Ox_st.x + Oy_en.x * Oy_en.x * Ox_st.x + Oy_en.x *
                (Oy_st.y - Oy_en.y) * (Oy_st.y - Ox_st.y) - Oy_st.x * (Oy_st.y - Oy_en.y) * (Oy_en.y - Ox_st.y)) / ((Oy_st.x - Oy_en.x) *
                (Oy_st.x - Oy_en.x) + (Oy_st.y - Oy_en.y) * (Oy_st.y - Oy_en.y));
        y = (Oy_en.x * Oy_en.x * Oy_st.y + Oy_st.x * Oy_st.x * Oy_en.y + Oy_en.x * Ox_st.x * (Oy_en.y - Oy_st.y) - Oy_st.x *
                (Ox_st.x * (Oy_en.y - Oy_st.y) + Oy_en.x * (Oy_st.y + Oy_en.y)) + (Oy_st.y - Oy_en.y) * (Oy_st.y - Oy_en.y) * Ox_st.y) / ((
                Oy_st.x - Oy_en.x) * (Oy_st.x - Oy_en.x) + (Oy_st.y - Oy_en.y) * (Oy_st.y - Oy_en.y));
        Ox_en = new Point(x,y);
        countOfPoints+=1;
        square = new Square(Ox_st.calcLength(Ox_en) / 5);
    }
    else {
        stroke(random(255), random(255),random(255));
    }
}
    public void settings() {
        size(800, 600);
    }
    public void setup() {
        smooth();
        strokeWeight(2);
    }
    public void draw() {
        background(255);
        noFill();
        if (countOfPoints == 2) {
            line(Oy_st.x, Oy_st.y, Oy_en.x, Oy_en.y); //вертикальна (вісь у)
        }
        else if (countOfPoints == 4){
            line(Oy_st.x-((Oy_en.x-Oy_st.x)*50), Oy_st.y-((Oy_en.y-Oy_st.y)*50),
                    Oy_en.x+(((Oy_en.x-Oy_st.x)*50)), Oy_en.y+((Oy_en.y-Oy_st.y)*50)); //вертикальна (вісь у)
            line(Ox_st.x, Ox_st.y, Ox_en.x, Ox_en.y); //горизонтальна (вісь х)


            if(Ox_st.calcLength(m,n) + square.side*Math.sqrt(2)/2 <= Ox_st.calcLength(Ox_en)){
                translate(m,n);
                rotate(angle);
                rect(-(square.side/2), -(square.side/2), square.side, square.side);
                ellipse(0,0,3,3);
                angle -= 0.5;
                m += -(Ox_st.x-Ox_en.x)/100;
                n += -(Ox_st.y-Ox_en.y)/100;
            }
            else{
                translate(m,n);
                rotate(angle);
                rect(-(square.side/2), -(square.side/2), square.side, square.side);
                ellipse(0,0,3,3);
            }
        }
    }
}