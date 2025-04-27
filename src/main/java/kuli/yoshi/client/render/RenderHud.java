package kuli.yoshi.client.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/**
 * Класс для рендеринга тестовых прямоугольников прямо на экране
 */
public class RenderHud {
    // Флаг отображения HUD
    private static boolean showHud = false;
    
    // Настройки радиуса закругления
    private static int cornerRadius = 15;
    private static final int MIN_RADIUS = 0;
    private static final int MAX_RADIUS = 50;
    
    // Настройки прозрачности
    private static float opacity = 0.7f;
    private static final float MIN_OPACITY = 0.1f;
    private static final float MAX_OPACITY = 1.0f;
    
    // Выбранный прямоугольник (0 - нет выбора, 1-4 - прямоугольники на экране)
    private static int selectedRect = 0;
    
    /**
     * Переключает отображение тестовых прямоугольников
     */
    public static void toggleHud() {
        showHud = !showHud;
    }
    
    /**
     * Проверяет, отображается ли HUD
     */
    public static boolean isShowing() {
        return showHud;
    }
    
    /**
     * Обрабатывает ввод клавиш для настройки
     */
    public static boolean handleKeyInput(int keyCode) {
        if (!showHud) return false;
        
        // Выбор прямоугольника (клавиши 1-4)
        if (keyCode >= 49 && keyCode <= 52) { // 49 = клавиша 1, 52 = клавиша 4
            selectedRect = keyCode - 48; // 1-4
            return true;
        }
        
        // Сброс выбора (клавиша 0)
        if (keyCode == 48) { // 48 = клавиша 0
            selectedRect = 0;
            return true;
        }
        
        // Если прямоугольник выбран
        if (selectedRect > 0) {
            // Увеличить радиус (клавиша +)
            if (keyCode == 61 || keyCode == 107) { // + на разных клавиатурах
                cornerRadius = Math.min(cornerRadius + 1, MAX_RADIUS);
                return true;
            }
            
            // Уменьшить радиус (клавиша -)
            if (keyCode == 45 || keyCode == 109) { // - на разных клавиатурах
                cornerRadius = Math.max(cornerRadius - 1, MIN_RADIUS);
                return true;
            }
            
            // Увеличить прозрачность (клавиша стрелка вверх)
            if (keyCode == 265) { // Стрелка вверх
                opacity = Math.min(opacity + 0.05f, MAX_OPACITY);
                return true;
            }
            
            // Уменьшить прозрачность (клавиша стрелка вниз)
            if (keyCode == 264) { // Стрелка вниз
                opacity = Math.max(opacity - 0.05f, MIN_OPACITY);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Рендерит тестовые прямоугольники на экране
     */
    public static void render(DrawContext context, int screenWidth, int screenHeight) {
        if (!showHud) return;
        
        // Отрисовка заголовка
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.literal("Тестовые прямоугольники (R - скрыть)"),
            10,
            10,
            0xFFFFFF,
            true
        );
        
        // Отрисовка информации о настройках
        String settingsInfo = String.format(
            "Радиус закругления: %d (+ или - для изменения)",
            cornerRadius
        );
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.literal(settingsInfo),
            10,
            25,
            0xFFFFFF,
            true
        );
        
        String opacityInfo = String.format(
            "Прозрачность: %.2f (↑ или ↓ для изменения)",
            opacity
        );
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.literal(opacityInfo),
            10,
            40,
            0xFFFFFF,
            true
        );
        
        // Инструкция по выбору
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            Text.literal("Нажмите 1-4 для выбора прямоугольника, 0 для сброса"),
            10,
            55,
            0xFFFFFF,
            true
        );
        
        // Отображение выбранного прямоугольника
        if (selectedRect > 0) {
            String selectedInfo = String.format("Выбран прямоугольник %d", selectedRect);
            context.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(selectedInfo),
                10,
                70,
                0xFFAA00,
                true
            );
        }
        
        // Цвета прямоугольников - разделяем на основной цвет и альфа-канал для настройки прозрачности
        int blueRectColor = 0x3366CC;
        int blueRectBorderColor = selectedRect == 1 ? 0xFFFFFF : 0x000000;
        
        int greenRectColor = 0x33CC66;
        int greenRectBorderColor = selectedRect == 2 ? 0xFFFFFF : 0x000000;
        
        int shadowColor = 0x000000;
        
        // Обычный прямоугольник
        int rectX1 = 50, rectY1 = 100, rectWidth1 = 200, rectHeight1 = 100;
        RenderUtil.drawRect(context, rectX1, rectY1, rectWidth1, rectHeight1, blueRectColor, opacity);
        
        // Если выбран первый прямоугольник, отображаем рамку
        if (selectedRect == 1) {
            drawSelectionBorder(context, rectX1, rectY1, rectWidth1, rectHeight1, blueRectBorderColor);
        }
        
        // Прямоугольник с тенью
        int rectX2 = 50, rectY2 = 250, rectWidth2 = 200, rectHeight2 = 100;
        RenderUtil.drawRect(context, rectX2, rectY2, rectWidth2, rectHeight2, blueRectColor, opacity);
        RenderUtil.drawRectShadow(context, rectX2, rectY2, rectWidth2, rectHeight2, 10, shadowColor, opacity);
        
        // Если выбран второй прямоугольник, отображаем рамку
        if (selectedRect == 2) {
            drawSelectionBorder(context, rectX2, rectY2, rectWidth2, rectHeight2, blueRectBorderColor);
        }
        
        // Закругленный прямоугольник
        int rectX3 = 300, rectY3 = 100, rectWidth3 = 200, rectHeight3 = 100;
        RenderUtil.drawRoundedRect(context, rectX3, rectY3, rectWidth3, rectHeight3, cornerRadius, greenRectColor, opacity);
        
        // Если выбран третий прямоугольник, отображаем рамку
        if (selectedRect == 3) {
            drawSelectionBorder(context, rectX3, rectY3, rectWidth3, rectHeight3, greenRectBorderColor);
        }
        
        // Закругленный прямоугольник с тенью
        int rectX4 = 300, rectY4 = 250, rectWidth4 = 200, rectHeight4 = 100;
        RenderUtil.drawRoundedRect(context, rectX4, rectY4, rectWidth4, rectHeight4, cornerRadius, greenRectColor, opacity);
        RenderUtil.drawRoundedRectShadow(context, rectX4, rectY4, rectWidth4, rectHeight4, cornerRadius, 10, shadowColor, opacity);
        
        // Если выбран четвертый прямоугольник, отображаем рамку
        if (selectedRect == 4) {
            drawSelectionBorder(context, rectX4, rectY4, rectWidth4, rectHeight4, greenRectBorderColor);
        }
    }
    
    /**
     * Отрисовывает рамку выделения вокруг выбранного прямоугольника
     */
    private static void drawSelectionBorder(DrawContext context, int x, int y, int width, int height, int color) {
        // Верхняя линия
        context.fill(x - 2, y - 2, x + width + 2, y, color);
        // Нижняя линия
        context.fill(x - 2, y + height, x + width + 2, y + height + 2, color);
        // Левая линия
        context.fill(x - 2, y, x, y + height, color);
        // Правая линия
        context.fill(x + width, y, x + width + 2, y + height, color);
    }
} 