/*
package panels;

import app.Primitive;
import io.github.humbleui.jwm.Event;
import io.github.humbleui.jwm.EventKey;
import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.RRect;
import misc.CoordinateSystem2i;
import misc.Misc;
import misc.Vector2i;

import java.util.ArrayList;

*/
/**
 * Панель игры
 *//*

public class PanelPrimitives extends Panel {
    */
/**
     * Список примитивов
     *//*

    private final ArrayList<Primitive> primitives = new ArrayList<>();
    */
/**
     * Положение текущего примитива
     *//*

    private int primitivePos;

    */
/**
     * Конструктор панели
     *
     * @param window          окно
     * @param drawBG          нужно ли рисовать подложку
     * @param backgroundColor цвет фона
     * @param padding         отступы
     *//*

    public PanelPrimitives(Window window, boolean drawBG, int backgroundColor, int padding) {
        super(window, drawBG, backgroundColor, padding);
        // добавляем точку
        primitives.add(((canvas, windowCS, p) -> canvas.drawRRect(
                RRect.makeXYWH(200, 200, 4, 4, 2), p)
        ));
        primitivePos = 0;
        // добавляем линию
        */
/*primitives.add((canvas, windowCS, p) -> {
            // опорные точки линии
            Vector2i pointA = new Vector2i(200, 200);
            Vector2i pointB = new Vector2i(500, 600);
            // вектор, ведущий из точки A в точку B
            Vector2i delta = Vector2i.subtract(pointA, pointB);
            // получаем максимальную длину отрезка на экране, как длину диагонали экрана
            int maxDistance = (int) windowCS.getSize().length();
            // получаем новые точки для рисования, которые гарантируют, что линия
            // будет нарисована до границ экрана
            Vector2i renderPointA = Vector2i.sum(pointA, Vector2i.mult(delta, maxDistance));
            Vector2i renderPointB = Vector2i.sum(pointA, Vector2i.mult(delta, -maxDistance));
            // рисуем линию*//*

            canvas.drawLine(renderPointA.x, renderPointA.y, renderPointB.x, renderPointB.y, p);
        });
        primitivePos = 0;
        // добавляем параллельный прямоугольник
        primitives.add((canvas, windowCS, p) -> {
            // левая верхняя вершина
            Vector2i pointA = new Vector2i(200, 100);
            // правая нижняя
            Vector2i pointC = new Vector2i(300, 500);

            // рассчитываем опорные точки прямоугольника
            Vector2i pointB = new Vector2i(pointA.x, pointC.y);
            Vector2i pointD = new Vector2i(pointC.x, pointA.y);

            // рисуем его стороны
            canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, p);
            canvas.drawLine(pointB.x, pointB.y, pointC.x, pointC.y, p);
            canvas.drawLine(pointC.x, pointC.y, pointD.x, pointD.y, p);
            canvas.drawLine(pointD.x, pointD.y, pointA.x, pointA.y, p);
        });
    }

    */
/**
     * Обработчик событий
     *
     * @param e событие
     *//*

    @Override
    public void accept(Event e) {
        // кнопки клавиатуры
        if (e instanceof EventKey eventKey) {
            // кнопка нажата с Ctrl
            if (eventKey.isPressed()) {
                switch (eventKey.getKey()) {
                    // Следующий примитив
                    case LEFT -> primitivePos = (primitivePos - 1 + primitives.size()) % primitives.size();
                    // Предыдущий примитив
                    case RIGHT -> primitivePos = (primitivePos + 1) % primitives.size();
                }
            }
        }
    }


    */
/**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     *//*

    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        // создаём перо
        Paint p = new Paint();
        // задаём цвет
        p.setColor(Misc.getColor(200, 255, 255, 255));
        // задаём толщину пера
        p.setStrokeWidth(5);
        // рисуем текущий примитив
        primitives.get(primitivePos).render(canvas, windowCS, p);
    }
}
*/
