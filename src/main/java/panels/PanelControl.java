package panels;

import app.MyRect;
import app.Point;
import app.Task;

import java.lang.annotation.Target;
import java.util.ArrayList;

import controls.*;
import dialogs.PanelInfo;
import io.github.humbleui.jwm.*;
import io.github.humbleui.skija.Canvas;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;

import java.util.List;

import static app.Application.PANEL_PADDING;
import static app.Colors.FIELD_BACKGROUND_COLOR;
import static app.Colors.FIELD_TEXT_COLOR;

/**
 * Панель управления
 */
public class PanelControl extends GridPanel {


    /**
     * Кнопки
     */
    public List<Button> buttons;
    /**
     * Текст задания
     */
    MultiLineLabel task;
    /**
     * Заголовки
     */
    public List<Label> labels;
    /**
     * Поля ввода
     */

    public List<Input> inputs;
    /**
     * Сброс решения задачи
     */
    private void cancelTask()
    {
        PanelRendering.task.cancel();
        // Задаём новый текст кнопке решения
        solve.text = "Решить";
    }
    /**
     * Кнопка "решить"
     */
    public static Button solve;

    /**
     * Панель управления
     *
     * @param window     окно
     * @param drawBG     флаг, нужно ли рисовать подложку
     * @param color      цвет подложки
     * @param padding    отступы
     * @param gridWidth  кол-во ячеек сетки по ширине
     * @param gridHeight кол-во ячеек сетки по высоте
     * @param gridX      координата в сетке x
     * @param gridY      координата в сетке y
     * @param colspan    кол-во колонок, занимаемых панелью
     * @param rowspan    кол-во строк, занимаемых панелью
     */
    public PanelControl(
            Window window, boolean drawBG, int color, int padding, int gridWidth, int gridHeight,
            int gridX, int gridY, int colspan, int rowspan
    ) {
        super(window, drawBG, color, padding, gridWidth, gridHeight, gridX, gridY, colspan, rowspan);

        // создаём списки
        inputs = new ArrayList<>();
        labels = new ArrayList<>();
        buttons = new ArrayList<>();

        int gw = 10;

        // задание
        task = new MultiLineLabel(
                window, false, backgroundColor, PANEL_PADDING,
                6, gw, 0, 0, 6, 3, Task.TASK_TEXT,
                false, true);
        // добавление вручную
        Label xLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, gw, 0, 3, 1, 1, "X", true, true);
        labels.add(xLabel);
        Input xField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, gw, 1, 3, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xField);
        Label yLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, gw, 3, 3, 1, 1, "Y", true, true);
        labels.add(yLabel);
        Input yField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, gw, 4, 3, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yField);

        // добавление вручную
        Label x1Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, gw, 0, 6, 1, 1, "X1", true, true);
        labels.add(x1Label);
        Input x1Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, gw, 1, 6, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(x1Field);
        Label y1Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, gw, 3, 6, 1, 1, "Y1", true, true);
        labels.add(y1Label);
        Input y1Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, gw, 4, 6, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(y1Field);
        // случайное добавление
        Label cntLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, gw, 0, 5, 1, 1, "Кол-во", true, true);
        labels.add(cntLabel);

        Input cntField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, gw, 1, 5, 2, 1, "5", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(cntField);

        Button addPoints = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, gw, 3, 5, 4, 1, "Добавить\nслучайные точки",
                true, true);
        addPoints.setOnClick(() -> {
            // если числа введены верно
            if (!cntField.hasValidIntValue()) {
                PanelLog.warning("кол-во точек указано неверно");
            } else
                PanelRendering.task.addRandomPoints(cntField.intValue());
        });
        buttons.add(addPoints);
        Button addPoint = new Button(
                window, false, backgroundColor, 0,
                6, gw, 1, 4, 4, 1, "Добавить точку",
                true, true);
        addPoint.setOnClick(() -> {
            Task.solved = false;
            // если числа введены верно
            if (!xField.hasValidDoubleValue()) {
                PanelLog.warning("X координата введена неверно");
            } else if (!yField.hasValidDoubleValue())
                PanelLog.warning("Y координата введена неверно");
            else
                PanelRendering.task.addPoint(new Vector2d(xField.doubleValue(), yField.doubleValue()));
        });
        buttons.add(addPoint);
        Button addRectangle = new Button(
                window, false, backgroundColor, 0,
                6, gw, 0, 7, 4, 1, "Добавить\nпрямоугольник",
                true, true);
        addRectangle.setOnClick(() -> {
            // если числа введены верно
            if (!xField.hasValidDoubleValue())
                PanelLog.warning("X координата первой точки введена неверно");
            else if (!yField.hasValidDoubleValue())
                PanelLog.warning("Y координата первой точки введена неверно");
            else if (!x1Field.hasValidDoubleValue())
                PanelLog.warning("X координата второй точки введена неверно");
            else if (!y1Field.hasValidDoubleValue())
                PanelLog.warning("Y координата второй точки введена неверно");
            else
                PanelRendering.task.addRectangle(xField.doubleValue(),yField.doubleValue(),x1Field.doubleValue(),y1Field.doubleValue());
        });

        buttons.add(addRectangle);
        Button addRandomRectangle = new Button(
                window, false, backgroundColor, 0,
                6, gw, 3, 7, 4, 1, "Добавить cлучайный\nпрямоугольник",
                true, true);
        buttons.add(addRandomRectangle);
        addRandomRectangle.setOnClick(() -> PanelRendering.task.addRandomRectangle());
        /*buttons.add(addToFirstSet);

        Button addToSecondSet = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 3, 3, 1, "Y1",
                true, true);
        addToSecondSet.setOnClick(() -> {
            // если числа введены верно
            if (!xField.hasValidDoubleValue()) {
                PanelLog.warning("X координата введена неверно");
            } else if (!yField.hasValidDoubleValue())
                PanelLog.warning("Y координата введена неверно");
            else {
                PanelRendering.task.addPoint(
                        new Vector2d(xField.doubleValue(), yField.doubleValue()), Point.PointSet.SECOND_SET
                );
            }
        });
        buttons.add(addToSecondSet);*/
        // управление
        Button load = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, gw, 0, 8, 3, 1, "Загрузить",
                true, true);
        load.setOnClick(() -> {
            PanelRendering.load();
            cancelTask();

        });
        buttons.add(load);

        Button save = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, gw, 3, 8, 3, 1, "Сохранить",
                true, true);
        save.setOnClick(PanelRendering::save);
        buttons.add(save);

        Button clear = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, gw, 0, 9, 3, 1, "Очистить",
                true, true);
        clear.setOnClick(() -> PanelRendering.task.clear());
        buttons.add(clear);

        solve = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, gw, 3, 9, 3, 1, "Решить",
                true, true);
        solve.setOnClick(() -> {
            if (!PanelRendering.task.isSolved()) {
                PanelRendering.task.solve();
                String s = "Задача решена\n" +
                        "Всего точек: " + (PanelRendering.task.getSingle().size() > 4 ? PanelRendering.task.getSingle().size() - 4: PanelRendering.task.getSingle().size()) + "\n" +
                        "Размер полученного отрезка: " + Task.length;

                PanelInfo.show(s + "\n\nНажмите Esc, чтобы вернуться");
                PanelLog.success(s);
                solve.text = "Сбросить";
            }
            else {
                cancelTask();
            }
            window.requestFrame();
        });
        buttons.add(solve);
    }
    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // вызываем обработчик предка
        super.accept(e);
        // событие движения мыши
        if (e instanceof EventMouseMove ee) {
            for (Input input : inputs)
                input.accept(ee);

            for (Button button : buttons) {
                if (lastWindowCS != null)
                    button.checkOver(lastWindowCS.getRelativePos(new Vector2i(ee)));
            }
            // событие нажатия мыши
        } else if (e instanceof EventMouseButton ee) {
            if (!lastInside || !ee.isPressed())
                return;

            Vector2i relPos = lastWindowCS.getRelativePos(lastMove);
            // пробуем кликнуть по всем кнопкам
            for (Button button : buttons) {
                button.click(relPos);
            }
            // перебираем поля ввода
            for (Input input : inputs) {
                // если клик внутри этого поля
                if (input.contains(relPos)) {
                    // переводим фокус на это поле ввода
                    input.setFocus();
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода текста
        } else if (e instanceof EventTextInput ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода клавиш
        } else if (e instanceof EventKey ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
        }
    }

    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        task.paint(canvas, windowCS);
        // выводим поля ввода
        for (Input input : inputs) {
            input.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Label label : labels) {
            label.paint(canvas, windowCS);
        }   // выводим кнопки
        for (Button button : buttons) {
            button.paint(canvas, windowCS);
        }
    }
}
