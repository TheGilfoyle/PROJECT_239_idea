/*
package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import misc.Misc;
import misc.Vector2d;

import java.util.Objects;

*/
/**
 * Класс точки
 *//*

public class Rectangle {
    */
/**
     * Координаты точки 1
     *//*

    public final Vector2i pointA;
    */
/**
     * Координаты точки 1
     *//*

    public final Vector2i pointB;

    */
/**
     * Конструктор точки
     *
     * @param pointA положение точки
     * @param pointB множество, которому она принадлежит
     *//*

    @JsonCreator
    public Point(@JsonProperty("pointA") Vector2i pointA, @JsonProperty("pointB") Vector2i pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }


    */
/**
     * Получить положение
     * (нужен для json)
     *
     * @return положение
     *//*

    public Vector2d getPos() {
        return pos;
    }


    */
/**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     *//*

    @Override
    public String toString() {
        return "Point{" +
                "pointSetType=" + pointSet +
                ", pos=" + pos +
                '}';
    }

    */
/**
     * Проверка двух объектов на равенство
     *
     * @param o объект, с которым сравниваем текущий
     * @return флаг, равны ли два объекта
     *//*

    @Override
    public boolean equals(Object o) {
        // если объект сравнивается сам с собой, тогда объекты равны
        if (this == o) return true;
        // если в аргументе передан null или классы не совпадают, тогда объекты не равны
        if (o == null || getClass() != o.getClass()) return false;
        // приводим переданный в параметрах объект к текущему классу
        Point point = (Point) o;
        return pointSet.equals(point.pointSet) && Objects.equals(pos, point.pos);
    }

    */
/**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     *//*

    @Override
    public int hashCode() {
        return Objects.hash(pointSet, pos);
    }
}
*/
