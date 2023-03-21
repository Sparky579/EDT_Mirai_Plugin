package org.example.test;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;

public final class Plugintest extends JavaPlugin {
    public static final Plugintest INSTANCE = new Plugintest();

    private Plugintest() {
        super(new JvmPluginDescriptionBuilder("org.example.test.plugin.test", "1.0-SNAPSHOT")
                .name("FirstPlugin")
                .author("Kevin327")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
        GlobalEventChannel.INSTANCE.registerListenerHost(new FirstModule());
    }
}