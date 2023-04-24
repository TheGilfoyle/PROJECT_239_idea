package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Paint;
import lombok.Getter;
import misc.*;
import panels.PanelLog;
import panels.PanelRendering;

import io.github.humbleui.skija.Canvas;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static app.Colors.TASK_GRID_COLOR;
import static panels.PanelControl.solve;


/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {

    /**
     * Порядок разделителя сетки, т.е. раз в сколько отсечек
     * будет нарисована увеличенная
     */
    private static final int DELIMITER_ORDER = 10;
    /**
     * коэффициент колёсика мыши
     */
    private static final float WHEEL_SENSITIVE = -0.001f;
    private Graphics canvas;

    /**
     * Добавить случайные точки
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandomPoints(int cnt) {
        // если создавать точки с полностью случайными координатами,
        // то вероятность того, что они совпадут крайне мала
        // поэтому нужно создать вспомогательную малую целочисленную ОСК
        // для получения случайной точки мы будем запрашивать случайную
        // координату этой решётки (их всего 30х30=900).
        // после нам останется только перевести координаты на решётке
        // в координаты СК задачи
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);

        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты на решётке
            Vector2i gridPos = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos = ownCS.getCoords(gridPos, addGrid);
            // сработает примерно в половине случаев
            if (ThreadLocalRandom.current().nextBoolean())
                addPoint(pos);
            else
                addPoint(pos);
        }
    }

    /**
     * Получить положение курсора мыши в СК задачи
     *
     * @param x        координата X курсора
     * @param y        координата Y курсора
     * @param windowCS СК окна
     * @return вещественный вектор положения в СК задачи
     */
    @JsonIgnore
    public Vector2d getRealPos(int x, int y, CoordinateSystem2i windowCS) {
        return ownCS.getCoords(x, y, windowCS);
    }

    /**
     * Рисование курсора мыши
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     * @param font     шрифт
     * @param pos      положение курсора мыши
     */
    public void paintMouse(Canvas canvas, CoordinateSystem2i windowCS, Font font, Vector2i pos) {
        if(!solved)
            solve.text = "Решить";
        // создаём перо
        try (var paint = new Paint().setColor(TASK_GRID_COLOR)) {
            // сохраняем область рисования
            canvas.save();
            // рисуем перекрестие
            canvas.drawRect(Rect.makeXYWH(0, pos.y - 1, windowCS.getSize().x, 2), paint);
            canvas.drawRect(Rect.makeXYWH(pos.x - 1, 0, 2, windowCS.getSize().y), paint);
            // смещаемся немного для красивого вывода текста
            canvas.translate(pos.x + 3, pos.y - 5);
            // положение курсора в пространстве задачи
            Vector2d realPos = getRealPos(pos.x, pos.y, lastWindowCS);
            // выводим координаты
            canvas.drawString(realPos.toString(), 0, 0, font, paint);
            // восстанавливаем область рисования
            canvas.restore();
        }
    }

    /**
     * Очистить задачу
     */
    public void clear() {
        points.clear();
        solved = false;
    }

//    public static void main(String[] args) {
//        Point a = new Point(new Vector2d(0, 0));
//        Point b = new Point(new Vector2d(1, 1));
//        Point c = new Point(new Vector2d(-1, 1));
//        Point d = new Point(new Vector2d(1, -1));
//        System.out.println(line(a, b, c, d));
//    }
    //Уравнение прямой и определение пересечения отрезка с ней
    public static Point line(Point p3, Point p4, Point p1, Point p2)
    {
        double k1 = 0;
        double b1 = 0;
        double k2 = 0;
        double b2 = 0;
        boolean t1 = true;
        boolean t2 = true;
        double x = 0;
        double y = 0;
        if(p1.pos.x!=p2.pos.x) // отрезок
        {
            k1 = (p1.pos.y - p2.pos.y) / (p1.pos.x - p2.pos.x);
            b1 = -(k1*p1.pos.x)+p1.pos.y;
            //System.out.println("y = "+k1+"x+"+b1);
        }
        else
        {
            k1 = 0;
            b1 = p2.pos.x;
            //System.out.println("x = "+k1+"y+"+b1);
            t1 = false;
        }
        if(p3.pos.x!=p4.pos.x) // прямая
        {
            k2 = (p3.pos.y - p4.pos.y) / (p3.pos.x - p4.pos.x);
            b2 = -(k2*p3.pos.x)+p3.pos.y;
            //System.out.println("y = "+k2+"x+"+b2);
        }
        else
        {
            k2 = 0;
            b2 = p4.pos.x;
            //System.out.println("x = "+k2+"y+"+b2);
            t2 = false;
        }
        if(k1 == k2 && t1 == t2) // прямые параллельны
            return null;
        else if(t1 == t2) // обе прямые имеют вид y = kx+b
        {
            x = (b2-b1)/(k1-k2);
            y = x*k1+b1;
            //System.out.println("x = " + x + ", первый конец отрезка: " +Math.min(p3.pos.x, p4.pos.x) + ", второй конец отрезка: " + Math.max(p3.pos.x, p4.pos.x));
            if(x >= Math.min(p1.pos.x, p2.pos.x) && x <= Math.max(p1.pos.x, p2.pos.x))
                return new Point(new Vector2d(x, y));
            else
                return null;
        }
        else // одна из прямых параллельна y
        {
            if(!t1) // отрезок
            {
                x = p1.pos.x;
                y = x*k2+b2;
                if(y >= Math.min(p1.pos.y, p2.pos.y) && y <= Math.max(p1.pos.y, p2.pos.y))
                    return new Point(new Vector2d(x, y));
                else
                    return null;
            }
            else // прямая
            {
                x = p3.pos.x;
                y = x*k1+b1;
                if(x >= Math.min(p1.pos.x, p2.pos.x) && x <= Math.max(p1.pos.x, p2.pos.x))
                    return new Point(new Vector2d(x, y));
                else
                    return null;
            }
        }

    }

    static double distance(Point a, Point b)
    {
        return Math.sqrt(Math.pow((a.pos.x-b.pos.x), 2)+Math.pow((a.pos.y-b.pos.y), 2));
    }
    /**
     *  задачу
     */
    public static double length;
    public static Point[] max = new Point[4];
    public void solve() {
        // очищаем списки
        crossed.clear();
        single.clear();
        Point p1 = rect.a;
        Point p2 = new Point(new Vector2d(rect.a.pos.x, rect.c.pos.y));
        Point p3 = rect.c;
        Point p4 = new Point(new Vector2d(rect.c.pos.x, rect.a.pos.y));
        Point middle = new Point(new Vector2d((rect.a.pos.x + rect.c.pos.x) / 2, (rect.a.pos.y + rect.a.pos.x) / 2));
        // перебираем пары точек
        if (p1.pos.x == p3.pos.x || p1.pos.y == p3.pos.y) {
            solved = false;
            return;
        }
        while (p1.pos.x < p3.pos.x || p1.pos.y < p3.pos.y) {
            Point temp = p1;
            p1 = p2;
            p2 = p3;
            p3 = p4;
            p4 = temp;
        }
        if (p4.pos.y > p2.pos.y) {
            Point temp = p2;
            p2 = p4;
            p4 = temp;
        }
        int m = points.size();
        length = 0;
        if (p1.pos.x != p3.pos.x || p1.pos.y != p3.pos.y)
        {
            for (int i = 0; i < m - 1; i++) {
                for (int j = i + 1; j < m; j++) {
                    // сохраняем точки
                    Point a = points.get(i);
                    Point b = points.get(j);
                    Point c, d;
                    //if(!Objects.equals(line(a, b, p1, p3), p1) && Objects.equals(line(a, b, p1, p3), p3) && Objects.equals(line(a, b, p2, p4), p2) && Objects.equals(line(a, b, p2, p4), p4))
                    if (!(line(a, b, p1, p3) == (null)) && (line(a, b, p2, p4) == (null))) {
                        Point n = new Point(Objects.requireNonNull(line(a, b, p1, p3)).getPos());
                        if (n.pos.x > middle.pos.x) {
                            c = new Point(new Vector2d(line(a, b, p1, p2).pos.x, line(a, b, p1, p2).pos.y));
                            d = new Point(new Vector2d(line(a, b, p1, p4).pos.x, line(a, b, p1, p4).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p1, p2).pos.x, line(a, b, p1, p2).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p1, p4).pos.x, line(a, b, p1, p4).pos.y));
                        } else {
                            c = new Point(new Vector2d(line(a, b, p3, p2).pos.x, line(a, b, p3, p2).pos.y));
                            d = new Point(new Vector2d(line(a, b, p3, p4).pos.x, line(a, b, p3, p4).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p3, p2).pos.x, line(a, b, p3, p2).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p3, p4).pos.x, line(a, b, p3, p4).pos.y));
                        }
                        if (distance(c, d) > length) {
                            max[0] = c;
                            max[1] = d;
                            max[2] = a;
                            max[3] = b;
                            length = distance(c, d);
                        }
                    } else if ((line(a, b, p1, p3) == (null)) && !(line(a, b, p2, p4) == (null))) {
                        Point n = new Point(Objects.requireNonNull(line(a, b, p2, p4)).getPos());
                        if (n.pos.x > middle.pos.x) {
                            c = new Point(new Vector2d(line(a, b, p3, p4).pos.x, line(a, b, p3, p4).pos.y));
                            d = new Point(new Vector2d(line(a, b, p1, p4).pos.x, line(a, b, p1, p4).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p3, p4).pos.x, line(a, b, p3, p4).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p1, p4).pos.x, line(a, b, p1, p4).pos.y));
                        } else {
                            c = new Point(new Vector2d(line(a, b, p3, p2).pos.x, line(a, b, p3, p2).pos.y));
                            d = new Point(new Vector2d(line(a, b, p1, p2).pos.x, line(a, b, p1, p2).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p3, p2).pos.x, line(a, b, p3, p2).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p1, p2).pos.x, line(a, b, p1, p2).pos.y));
                        }
                        if (distance(c, d) > length) {
                            max[0] = c;
                            max[1] = d;
                            max[2] = a;
                            max[3] = b;
                            length = distance(c, d);
                        }
                    } else if (!(line(a, b, p1, p3) == (null)) && !(line(a, b, p2, p4) == (null))) {
                        if ((line(a, b, p1, p2) == null)) {
                            c = new Point(new Vector2d(line(a, b, p3, p2).pos.x, line(a, b, p3, p2).pos.y));
                            d = new Point(new Vector2d(line(a, b, p1, p4).pos.x, line(a, b, p1, p4).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p3, p2).pos.x, line(a, b, p3, p2).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p1, p4).pos.x, line(a, b, p1, p4).pos.y));
                        } else {
                            c = new Point(new Vector2d(line(a, b, p3, p4).pos.x, line(a, b, p3, p4).pos.y));
                            d = new Point(new Vector2d(line(a, b, p1, p2).pos.x, line(a, b, p1, p2).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p3, p4).pos.x, line(a, b, p3, p4).pos.y));
//                        PanelRendering.task.addPoint(new Vector2d(line(a, b, p1, p2).pos.x, line(a, b, p1, p2).pos.y));
                        }
                        if (distance(c, d) > length) {
                            max[0] = c;
                            max[1] = d;
                            max[2] = a;
                            max[3] = b;
                            length = distance(c, d);
                        }
                    }
                }
            }
        }
        if(max[0] != null) {
            length = ((double) Math.round(length * 100)) / 100;
            PanelRendering.task.addPoint(max[0].getPos());
            PanelRendering.task.addPoint(max[1].getPos());
            PanelRendering.task.addPoint(max[3].getPos());
            PanelRendering.task.addPoint(max[2].getPos());
            line = new Line(new Vector2d(max[0].pos.x, max[0].pos.y), new Vector2d(max[1].pos.x, max[1].pos.y), this);
            PanelLog.info("Размер отрезка: " + length);
            System.out.println(max[0] + " " + max[1]);
            lines.add(max[0]);
            lines.add(max[1]);
            crossed.add(max[2]);
            crossed.add(max[3]);
            System.out.println(crossed);
        }
            /// добавляем вс
            for (Point point : points)
                if (!crossed.contains(point) || !lines.contains(point))
                    single.add(point);
            if(max[0] != null)
            // задача решена
                solved = true;

    }

    /**
     * Масштабирование области просмотра задачи
     *
     * @param delta  прокрутка колеса
     * @param center центр масштабирования
     */
    public void scale(float delta, Vector2i center) {
        if (lastWindowCS == null) return;
        // получаем координаты центра масштабирования в СК задачи
        Vector2d realCenter = ownCS.getCoords(center, lastWindowCS);
        // выполняем масштабирование
        ownCS.scale(1 + delta * WHEEL_SENSITIVE, realCenter);
    }

    /**
     * Отмена решения задачи
     */
    public void cancel() {
        points.removeIf(lines::contains);
        solved = false;
        length = 0;
    }

    /**
     * проверка, решена ли задача
     *
     * @return флаг
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Флаг, решена ли задача
     */
    public static boolean solved;
    /**
     * Цвет пересечения
     */
    public static final int CROSSED_COLOR = Misc.getColor(200, 0, 255, 255);
    public static final int Point_color = Misc.getColor(200, 1, 160, 73);
    /**
     * Цвет разности
     */
    public static final int SUBTRACTED_COLOR = Misc.getColor(200, 255, 255, 0);

    /**
     * Добавить точку
     *
     * @param pos положение
     */
    public void addPoint(Vector2d pos) {
        solved = false;
        points.removeIf(lines::contains);
        Point newPoint = new Point(pos);
        points.add(newPoint);
        // Добавляем в лог запись информации
        PanelLog.info("точка " + newPoint + " добавлена");
    }

    /**
     * Добавить случайный прямоугольник
     */
    public void addRandomRectangle() {
        // левая верхняя вершина
        solved = false;
        Vector2d pointA = ownCS.getRandomCoords();
        Vector2d pointB = ownCS.getRandomCoords();
        rect = new MyRect(new Point(pointA), new Point(pointB));
    }
    public void addRectangle(double x1, double y1, double x2, double y2) {
        // левая верхняя вершина
        Vector2d pointA = new Vector2d(x1,y1);
        Vector2d pointB = new Vector2d(x2,y2);
        rect = new MyRect(new Point(pointA), new Point(pointB));
    }

    /**
     * Список точек в пересечении
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> crossed;

    /**
     * Список точек на линии
     */
    @Getter
    @JsonIgnore
    public static ArrayList<Point> lines;
    /**
     * Список точек в разности
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> single;
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество точек, и "параллельный"\040
            прямоугольник. Множество точек образует все прямые,
            которые могут быть построены парами точек множества.\040
            Найти такую прямую (и такие две точки, через которые\040
            она проходит), что эта прямая пересекает указанный
            прямоугольник, и при этом длина отрезка прямой,\040
            находящейся внутри прямоугольника, максимальна.""";

    /**
     * Вещественная система координат задачи
     */
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    @Getter
    private final ArrayList<Point> points;
    /**
     * Список точек
     */
    @Getter
    private MyRect rect;

    /**
     * Список прямых
     */
    @Getter
    private Line line;
    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;

    /**
     * последняя СК окна
     */
    protected CoordinateSystem2i lastWindowCS;

//    /**
//     * Задача
//     *
//     * @param ownCS  СК задачи
//     * @param points массив точек
//     */
//    @JsonCreator
//    public Task(
//            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
//            @JsonProperty("points") ArrayList<Point> points,
//            @JsonProperty("rect") MyRect rect, @JsonProperty("solved") boolean solved) {
//        this.ownCS = ownCS;
//        this.points = points;
//        this.crossed = new ArrayList<>();
//        this.single = new ArrayList<>();
//        this.rect = rect;
//        this.lines = new ArrayList<>();
//        this.solved = solved;
//        this.line =  new Line(new Vector2d(-1, 1), new Vector2d(0, 0), this);
//    }

    public static boolean hidden = false;
    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("points") ArrayList<Point> points,
            @JsonProperty("rect") MyRect rect, @JsonProperty("solved") boolean solved,
            @JsonProperty("line") Line line) {
        this.ownCS = ownCS;
        this.points = points;
        this.crossed = new ArrayList<>();
        this.single = new ArrayList<>();
        this.rect = rect;
        this.lines = new ArrayList<>();
        this.solved = false;
        this.line = line;
        hidden = solved;
    }
    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    private void renderTask(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();

        // создаём перо
        try (var paint = new Paint()) {
            paint.setColor(SUBTRACTED_COLOR);
            rect.paint(windowCS, ownCS, canvas);
            if(isSolved() )
            {
                if(max[0] != null)
                {
                    Vector2i a = windowCS.getCoords(max[0].pos.x, max[0].pos.y, ownCS);
                    Vector2i b = windowCS.getCoords(max[1].pos.x, max[1].pos.y, ownCS);
                    canvas.drawLine(a.x, a.y, b.x, b.y, paint);
                }
                line.renderLine(canvas, windowCS);
            }
            for (Point p : points) {
                if (!solved) {
                    paint.setColor(p.getColor());
                } else {
                    if (crossed.contains(p))
                        paint.setColor(CROSSED_COLOR);
                    else if(lines.contains(p))
                        paint.setColor(Point_color);
                    else
                        paint.setColor(SUBTRACTED_COLOR);
                }
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPos = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                // рисуем точку
                canvas.drawRect(Rect.makeXYWH(windowPos.x - POINT_SIZE, windowPos.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }

        }
        canvas.restore();
    }

    /**
     * Рисование
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;
        // рисуем координатную сетку
        renderGrid(canvas, lastWindowCS);
        // рисуем задачу
        renderTask(canvas, windowCS);
    }

    /**
     * Рисование сетки
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void renderGrid(Canvas canvas, CoordinateSystem2i windowCS) {
        // сохраняем область рисования
        canvas.save();
        // получаем ширину штриха(т.е. по факту толщину линии)
        float strokeWidth = 0.03f / (float) ownCS.getSimilarity(windowCS).y + 0.5f;
        // создаём перо соответствующей толщины
        try (var paint = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(strokeWidth).setColor(TASK_GRID_COLOR)) {
            // перебираем все целочисленные отсчёты нашей СК по оси X
            for (int i = (int) (ownCS.getMin().x); i <= (int) (ownCS.getMax().x); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(i, 0, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % DELIMITER_ORDER == 0 ? 5 : 2;
                // рисуем вертикальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y + strokeHeight, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y - strokeHeight, paint);
            }
            // перебираем все целочисленные отсчёты нашей СК по оси Y
            for (int i = (int) (ownCS.getMin().y); i <= (int) (ownCS.getMax().y); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(0, i, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % 10 == 0 ? 5 : 2;
                // рисуем горизонтальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x + strokeHeight, windowPos.y, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x - strokeHeight, windowPos.y, paint);
            }
        }
        // восстанавливаем область рисования
        canvas.restore();
    }

    Vector2d prevClickPos = null;

    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     * @param mouseButton кнопка мыши
     */
    public void click(Vector2i pos, MouseButton mouseButton) {
        if(!solved)
            solve.text = "Решить";
        if (lastWindowCS == null) return;
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        // если левая кнопка мыши, добавляем в первое множество

        if (mouseButton.equals(MouseButton.PRIMARY)) {
            addPoint(taskPos);
            // если правая, то во второе
            prevClickPos = null;
        } else if (mouseButton.equals(MouseButton.SECONDARY)) {

            if (prevClickPos != null) {
                rect = new MyRect(new Point(taskPos), new Point(prevClickPos));
                prevClickPos = null;
            } else {
                prevClickPos = taskPos;
            }
        }

    }
}