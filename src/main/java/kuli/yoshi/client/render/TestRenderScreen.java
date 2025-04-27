package kuli.yoshi.client.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Тестовый экран для демонстрации RenderUtil
 */
public class TestRenderScreen extends Screen {
    // Флаг, определяющий, должен ли отображаться фон
    private boolean showBackground = false;
    
    // Текстура для фона (может быть null, если не нужна)
    private static final Identifier BACKGROUND_TEXTURE = null;

    public TestRenderScreen() {
        super(Text.literal("Тестовый экран рендеринга"));
    }
    
    /**
     * Переключает режим отображения фона
     */
    public void toggleBackground() {
        this.showBackground = !this.showBackground;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Отрисовываем прозрачный фон вместо стандартного затемнения
        if (showBackground) {
            // Используем стандартный затемненный фон
            this.renderBackground(context, mouseX, mouseY, delta);
        } else {
            // Рисуем полупрозрачный фон (почти прозрачный)
            context.fill(0, 0, this.width, this.height, 0x22000000);
        }
        
        // Отрисовываем заголовок
        context.drawCenteredTextWithShadow(
            this.textRenderer, 
            this.title, 
            this.width / 2, 
            10, 
            0xFFFFFF
        );
        
        // Подсказка для переключения режима фона
        context.drawTextWithShadow(
            this.textRenderer,
            Text.literal("Нажмите B для переключения фона"),
            10, 
            this.height - 20, 
            0xFFAA00
        );
        
        // Обычный прямоугольник
        RenderUtil.drawRect(context, 50, 50, 200, 100, 0xAA3366CC);
        
        // Прямоугольник с тенью
        RenderUtil.drawRect(context, 50, 200, 200, 100, 0xAA3366CC);
        RenderUtil.drawRectShadow(context, 50, 200, 200, 100, 10, 0x55000000);
        
        // Закругленный прямоугольник
        RenderUtil.drawRoundedRect(context, 300, 50, 200, 100, 15, 0xAA33CC66);
        
        // Закругленный прямоугольник с тенью
        RenderUtil.drawRoundedRect(context, 300, 200, 200, 100, 15, 0xAA33CC66);
        RenderUtil.drawRoundedRectShadow(context, 300, 200, 200, 100, 15, 10, 0x55000000);
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Клавиша B для переключения фона
        if (keyCode == 66) { // 66 - код клавиши B
            toggleBackground();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        // Не останавливаем игру при открытии экрана
        return false;
    }
} 