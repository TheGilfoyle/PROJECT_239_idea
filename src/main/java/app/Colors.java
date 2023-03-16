package app;

import misc.Misc;

/**
 * Класс цветов
 */
public class Colors {
    /**
     * Цвет подложки
     */
    public static final int STATS_BACKGROUND_COLOR = Misc.getColor(64, 51, 200, 51);
    /**
     * Цвет подложки
     */
    public static final int STATS_COLOR = Misc.getColor(255, 51, 200, 51);
    /**
     * Цвет текста
     */
    public static final int STATS_TEXT_COLOR = Misc.getColor(255, 255, 255, 255);
    /**
     * Цвет кнопки
     */
    public static final int BUTTON_COLOR = Misc.getColor(80, 0, 0, 0);
    /**
     * Цвет сетки
     */
    public static final int TASK_GRID_COLOR = Misc.getColor(64, 255, 255, 255);
    /**
     * Цвет подложки поля ввода
     */
    public static final int FIELD_BACKGROUND_COLOR = Misc.getColor(255, 255, 255, 255);
    /**
     * Цвет текста
     */
    public static final int FIELD_TEXT_COLOR = Misc.getColor(255, 0, 0, 0);
    /**
     * Цвет текста
     */
    public static final int MULTILINE_TEXT_COLOR = Misc.getColor(64, 255, 255, 255);
    /**
     * цвет фона
     */
    public static final int APP_BACKGROUND_COLOR = Misc.getColor(255, 38, 70, 83);

    /**
     * Цвет текста заголовка
     */
    public static final int LABEL_TEXT_COLOR = Misc.getColor(64, 255, 255, 255);
    /**
     * цвет подложки панелей
     */
    public static final int PANEL_BACKGROUND_COLOR = Misc.getColor(32, 0, 0, 0);
    /**
     * Запрещённый конструктор
     */
    private Colors() {
        throw new AssertionError("Вызов этого конструктора запрещён");
    }
}