package kuli.yoshi.client.render;

import net.minecraft.client.gui.DrawContext;

/**
 * Утилитный класс для рендеринга в Minecraft 1.21
 */
public class RenderUtil {
    
    /**
     * Рисует прямоугольник
     */
    public static void drawRect(DrawContext graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + height, color);
    }
    
    /**
     * Рисует прямоугольник с указанной прозрачностью
     */
    public static void drawRect(DrawContext graphics, int x, int y, int width, int height, int color, float opacity) {
        int alpha = Math.max(0, Math.min(255, (int)(opacity * 255))) << 24;
        int newColor = (color & 0x00FFFFFF) | alpha;
        graphics.fill(x, y, x + width, y + height, newColor);
    }
    
    /**
     * Рисует закругленный прямоугольник с улучшенным алгоритмом
     */
    public static void drawRoundedRect(DrawContext graphics, int x, int y, int width, int height, int radius, int color) {
        drawRoundedRect(graphics, x, y, width, height, radius, color, 1.0f);
    }
    
    /**
     * Рисует закругленный прямоугольник с указанной прозрачностью
     */
    public static void drawRoundedRect(DrawContext graphics, int x, int y, int width, int height, int radius, int color, float opacity) {
        int alpha = Math.max(0, Math.min(255, (int)(opacity * 255))) << 24;
        int newColor = (color & 0x00FFFFFF) | alpha;
        
        // Ограничиваем радиус, чтобы он не был больше половины ширины или высоты
        radius = Math.min(radius, Math.min(width / 2, height / 2));
        
        if (radius <= 0) {
            // Если радиус <= 0, рисуем обычный прямоугольник
            drawRect(graphics, x, y, width, height, newColor);
            return;
        }
        
        // Центральная часть (без углов)
        drawRect(graphics, x + radius, y, width - 2 * radius, height, newColor);
        drawRect(graphics, x, y + radius, width, height - 2 * radius, newColor);
        
        // Улучшенная отрисовка закругленных углов с anti-aliasing эффектом
        drawImprovedCorner(graphics, x + radius, y + radius, radius, 0, newColor); // Левый верхний
        drawImprovedCorner(graphics, x + width - radius, y + radius, radius, 1, newColor); // Правый верхний
        drawImprovedCorner(graphics, x + width - radius, y + height - radius, radius, 2, newColor); // Правый нижний
        drawImprovedCorner(graphics, x + radius, y + height - radius, radius, 3, newColor); // Левый нижний
    }
    
    /**
     * Улучшенный метод отрисовки закругленных углов
     */
    private static void drawImprovedCorner(DrawContext graphics, int centerX, int centerY, int radius, int corner, int color) {
        // Используем улучшенный алгоритм Брезенхема для круга
        for (int y = 0; y <= radius; y++) {
            for (int x = 0; x <= radius; x++) {
                double distance = Math.sqrt(x * x + y * y);
                // Проверяем, находится ли пиксель внутри круга с небольшим сглаживанием
                if (distance <= radius + 0.5) {
                    // Применяем коррекцию для более гладких краев
                    double alphaFactor = 1.0;
                    if (distance > radius - 0.5) {
                        // Добавляем сглаживание по краям
                        alphaFactor = 1.0 - (distance - (radius - 0.5));
                        if (alphaFactor < 0) alphaFactor = 0;
                    }
                    
                    // Если альфа слишком мала, пропускаем пиксель
                    if (alphaFactor < 0.1) continue;
                    
                    // Рассчитываем цвет с учетом сглаживания
                    int drawColor = color;
                    if (alphaFactor < 1.0) {
                        int alpha = (int)((color >> 24 & 0xFF) * alphaFactor);
                        drawColor = (alpha << 24) | (color & 0x00FFFFFF);
                    }
                    
                    int drawX = 0;
                    int drawY = 0;
                    
                    switch (corner) {
                        case 0: // Левый верхний
                            drawX = centerX - x;
                            drawY = centerY - y;
                            break;
                        case 1: // Правый верхний
                            drawX = centerX + x;
                            drawY = centerY - y;
                            break;
                        case 2: // Правый нижний
                            drawX = centerX + x;
                            drawY = centerY + y;
                            break;
                        case 3: // Левый нижний
                            drawX = centerX - x;
                            drawY = centerY + y;
                            break;
                    }
                    
                    graphics.fill(drawX, drawY, drawX + 1, drawY + 1, drawColor);
                }
            }
        }
    }
    
    /**
     * Рисует тень для прямоугольника
     */
    public static void drawRectShadow(DrawContext graphics, int x, int y, int width, int height, int size, int color) {
        drawRectShadow(graphics, x, y, width, height, size, color, 1.0f);
    }
    
    /**
     * Рисует тень для прямоугольника с указанной прозрачностью
     */
    public static void drawRectShadow(DrawContext graphics, int x, int y, int width, int height, int size, int color, float opacity) {
        // Базовая прозрачность для тени
        int baseAlpha = Math.max(0, Math.min(255, (int)(opacity * 255)));
        
        for(int i = 0; i < size; i++) {
            // Уменьшаем прозрачность с увеличением расстояния от объекта
            int alpha = (int)((baseAlpha * (size - i)) / size);
            int shadowColor = (alpha << 24) | (color & 0xFFFFFF);
            
            // Тень снизу
            graphics.fill(x + i, y + height + i, x + width - i, y + height + i + 1, shadowColor);
            
            // Тень справа
            graphics.fill(x + width + i, y + i, x + width + i + 1, y + height - i, shadowColor);
            
            // Угол тени
            graphics.fill(x + width + i, y + height + i, x + width + i + 1, y + height + i + 1, shadowColor);
        }
    }
    
    /**
     * Рисует тень для закругленного прямоугольника
     */
    public static void drawRoundedRectShadow(DrawContext graphics, int x, int y, int width, int height, int radius, int size, int color) {
        drawRoundedRectShadow(graphics, x, y, width, height, radius, size, color, 1.0f);
    }
    
    /**
     * Рисует тень для закругленного прямоугольника с указанной прозрачностью
     */
    public static void drawRoundedRectShadow(DrawContext graphics, int x, int y, int width, int height, int radius, int size, int color, float opacity) {
        // Ограничиваем радиус
        radius = Math.min(radius, Math.min(width / 2, height / 2));
        
        // Базовая прозрачность для тени
        int baseAlpha = Math.max(0, Math.min(255, (int)(opacity * 255)));
        
        for(int i = 0; i < size; i++) {
            // Уменьшаем прозрачность с увеличением расстояния от объекта
            int alpha = (int)((baseAlpha * (size - i)) / size);
            int shadowColor = (alpha << 24) | (color & 0xFFFFFF);
            
            // Тень снизу (с учетом закругления)
            graphics.fill(x + radius + i, y + height + i, x + width - radius - i, y + height + i + 1, shadowColor);
            
            // Тень справа (с учетом закругления)
            graphics.fill(x + width + i, y + radius + i, x + width + i + 1, y + height - radius - i, shadowColor);
            
            // Закругленные углы теней с улучшенным алгоритмом
            drawImprovedShadowCorner(graphics, x + width - radius, y + height, radius, i, shadowColor, opacity);
        }
    }
    
    /**
     * Улучшенный метод для рисования углов тени
     */
    private static void drawImprovedShadowCorner(DrawContext graphics, int x, int y, int radius, int offset, int color, float opacity) {
        // Улучшенный алгоритм для плавного отображения закругленных углов теней
        for (int dy = 0; dy <= radius + offset; dy++) {
            for (int dx = 0; dx <= radius + offset; dx++) {
                double distance = Math.sqrt(dx * dx + dy * dy);
                // Проверяем, находится ли точка в зоне тени угла
                if (distance <= radius + offset && distance >= radius - 0.5 + offset) {
                    // Расчет прозрачности в зависимости от расстояния для плавного перехода
                    double alphaFactor = 1.0 - Math.abs(distance - (radius + offset - 0.5)) / 1.0;
                    if (alphaFactor < 0) alphaFactor = 0;
                    if (alphaFactor > 1) alphaFactor = 1;
                    
                    // Применяем коэффициент прозрачности к цвету
                    int alpha = (int)((color >> 24 & 0xFF) * alphaFactor * opacity);
                    int shadowColor = (alpha << 24) | (color & 0x00FFFFFF);
                    
                    graphics.fill(x + dx, y + dy, x + dx + 1, y + dy + 1, shadowColor);
                }
            }
        }
    }
} 