package kuli.yoshi.client.render;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

/**
 * Класс для тестирования RenderUtil
 */
public class RenderTest implements ClientModInitializer {
    private static KeyBinding testScreenKey;

    @Override
    public void onInitializeClient() {
        // Регистрация клавиши для открытия тестового экрана (R по умолчанию)
        testScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antitledkuli.testscreen",
                GLFW.GLFW_KEY_R,
                "key.categories.antitledkuli"
        ));

        // Обработка нажатия клавиши
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (testScreenKey.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new TestRenderScreen());
            }
        });
    }
} 