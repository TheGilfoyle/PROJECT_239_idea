package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;

import java.util.Objects;

/**
 * Класс прямоугольника
 **/

public class MyRect {
    //Первая точка
    Point a;
    //Вторая точка
    Point c;
    @JsonCreator
    public MyRect(@JsonProperty("a") Point a, @JsonProperty("c") Point c) {
        this.a = a;
        this.c = c;
    }

    void paint(CoordinateSystem2i windowCS, CoordinateSystem2d taskCS, Canvas canvas){
        // создаём перо
        try (var p = new Paint()) {
            // левая верхняя вершина
            Vector2i pointA = windowCS.getCoords(a.pos,taskCS);
            // правая нижняя
            Vector2i pointC = windowCS.getCoords(c.pos,taskCS);

            // рассчитываем опорные точки прямоугольника
            Vector2i pointB = new Vector2i(pointA.x, pointC.y);
            Vector2i pointD = new Vector2i(pointC.x, pointA.y);

            // рисуем его стороны
            canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, p);
            canvas.drawLine(pointB.x, pointB.y, pointC.x, pointC.y, p);
            canvas.drawLine(pointC.x, pointC.y, pointD.x, pointD.y, p);
            canvas.drawLine(pointD.x, pointD.y, pointA.x, pointA.y, p);
        }
    }
    void section(CoordinateSystem2i windowCS, CoordinateSystem2d taskCS, Canvas canvas){
        // создаём перо
        try (var p = new Paint()) {
            // левая верхняя вершина
            Vector2i pointA = windowCS.getCoords(a.pos,taskCS);
            // правая нижняя
            Vector2i pointC = windowCS.getCoords(c.pos,taskCS);

            // рисуем его стороны
            canvas.drawLine(pointA.x, pointA.y, pointC.x, pointC.y, p);
        }
    }

    public Point getA() {
        return a;
    }

    public void setA(Point a) {
        this.a = a;
    }

    public Point getC() {
        return c;
    }

    public void setC(Point c) {
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyRect myRect = (MyRect) o;
        return Objects.equals(a, myRect.a) && Objects.equals(c, myRect.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, c);
    }
}
