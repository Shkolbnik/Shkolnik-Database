package kuli.yoshi.client;

import kuli.yoshi.client.render.RenderHud;
import kuli.yoshi.client.render.RenderUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AntitledkuliClient implements ClientModInitializer {
    private static KeyBinding testRenderKey;
    
    // KeyBindings для настройки параметров
    private static KeyBinding key0;
    private static KeyBinding key1;
    private static KeyBinding key2;
    private static KeyBinding key3;
    private static KeyBinding key4;
    private static KeyBinding keyPlus;
    private static KeyBinding keyMinus;
    private static KeyBinding keyUp;
    private static KeyBinding keyDown;
    
    @Override
    public void onInitializeClient() {
        // Регистрация клавиши для показа тестового рендеринга (R по умолчанию)
        testRenderKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.testrender",
                GLFW.GLFW_KEY_R,
                "key.categories.antitledkuli"
        ));
        
        // Регистрация клавиш для настройки
        registerConfigKeys();

        // Обработка нажатия клавиши для показа/скрытия интерфейса
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (testRenderKey.wasPressed()) {
                // Переключаем отображение тестовых прямоугольников
                RenderHud.toggleHud();
            }
            
            // Обрабатываем клавиши настройки
            handleConfigKeys();
        });
        
        // Регистрация обработчика для рендеринга HUD
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            // Передаем размеры экрана
            int width = MinecraftClient.getInstance().getWindow().getScaledWidth();
            int height = MinecraftClient.getInstance().getWindow().getScaledHeight();
            
            // Рендерим прямоугольники, если включено
            RenderHud.render(drawContext, width, height);
        });
    }
    
    /**
     * Регистрирует кнопки для настройки параметров рендеринга
     */
    private void registerConfigKeys() {
        String category = "key.categories.antitledkuli.config";
        
        // Регистрируем клавиши выбора прямоугольников
        key0 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.select.none", GLFW.GLFW_KEY_0, category));
        key1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.select.rect1", GLFW.GLFW_KEY_1, category));
        key2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.select.rect2", GLFW.GLFW_KEY_2, category));
        key3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.select.rect3", GLFW.GLFW_KEY_3, category));
        key4 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.select.rect4", GLFW.GLFW_KEY_4, category));
                
        // Клавиши настройки радиуса
        keyPlus = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.radius.increase", GLFW.GLFW_KEY_EQUAL, category));
        keyMinus = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.radius.decrease", GLFW.GLFW_KEY_MINUS, category));
                
        // Клавиши настройки прозрачности
        keyUp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.opacity.increase", GLFW.GLFW_KEY_UP, category));
        keyDown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.opacity.decrease", GLFW.GLFW_KEY_DOWN, category));
    }
    
    /**
     * Обрабатывает нажатия клавиш для настройки
     */
    private void handleConfigKeys() {
        // Обрабатываем только если HUD отображается
        if (!RenderHud.isShowing()) return;
        
        // Проверяем нажатие каждой клавиши
        if (key0.wasPressed()) {
            RenderHud.handleKeyInput(48); // Клавиша 0
        }
        if (key1.wasPressed()) {
            RenderHud.handleKeyInput(49); // Клавиша 1
        }
        if (key2.wasPressed()) {
            RenderHud.handleKeyInput(50); // Клавиша 2
        }
        if (key3.wasPressed()) {
            RenderHud.handleKeyInput(51); // Клавиша 3
        }
        if (key4.wasPressed()) {
            RenderHud.handleKeyInput(52); // Клавиша 4
        }
        if (keyPlus.wasPressed()) {
            RenderHud.handleKeyInput(61); // Клавиша +
        }
        if (keyMinus.wasPressed()) {
            RenderHud.handleKeyInput(45); // Клавиша -
        }
        if (keyUp.wasPressed()) {
            RenderHud.handleKeyInput(265); // Стрелка вверх
        }
        if (keyDown.wasPressed()) {
            RenderHud.handleKeyInput(264); // Стрелка вниз
        }
    }
}
