package com.hp_display;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;
import top.hendrixshen.magiclib.event.render.impl.RenderEventHandler;
import top.hendrixshen.magiclib.render.impl.TextRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MobInfoRenderer {
    private static final List<Entity> list = Lists.newArrayList();
    public static final Logger LOGGER = LoggerFactory.getLogger("hp_display");

    public static void init() {
        RenderEventHandler.registerPostRenderEntityEvent(MobInfoRenderer::collect);
        RenderEventHandler.registerPostRenderLevelEvent(MobInfoRenderer::render);
    }

    private static void collect(Entity entity, RenderContext context, float tickDelta) {
        if (entity instanceof MobEntity || entity instanceof PlayerEntity) MobInfoRenderer.list.add(entity);
    }

    public static void render(World level, RenderContext context, float tickDelta) {
        for (Entity entity : MobInfoRenderer.list) {
            if (entity instanceof MobEntity || entity instanceof PlayerEntity) {
                TextRenderer renderer = TextRenderer.create();

                if (entity instanceof MobEntity) {
                    renderer.addLine(entity.getType().getName().getString());
                    renderer.addLine(String.format("%.2f", ((LivingEntity) entity).getHealth()) + " Health");
                } else {
                    renderer.addLine(entity.getName().getString());
                    renderer.addLine(String.format("%.2f", ((PlayerEntity) entity).getHealth()) + " Health");
                }

                Position position = entity.getEyePos();
                renderer.pos(position.getX(), position.getY() + 0.8F, position.getZ());

                MobInfoRenderer.rotationAround(renderer, entity.getEyePos().add(0, 0.8F, 0), 0.6);

                renderer.bgColor((int) (MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24)
                        .fontScale(0.023F)
                        .render(context);
            }
        }

        MobInfoRenderer.list.clear();
    }

    private static TextRenderer rotationAround(@NotNull TextRenderer renderer, @NotNull Position centerPos, double range) {
        Position camPos = MinecraftClient.getInstance().gameRenderer.getCamera().getPos();
        float xAngle = (float) Math.atan2(camPos.getZ() - centerPos.getZ(), camPos.getX() - centerPos.getX());
        float yAngle = (float) Math.atan2(camPos.getX()- centerPos.getX(), camPos.getZ() - centerPos.getZ());
        return renderer.pos(range * Math.cos(xAngle) + centerPos.getX(), centerPos.getY(), range * Math.cos(yAngle) + centerPos.getZ());
    }
}
