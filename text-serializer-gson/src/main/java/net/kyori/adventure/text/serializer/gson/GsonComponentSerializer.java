/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.text.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNbtComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.util.NameMap;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class GsonComponentSerializer implements ComponentSerializer<Component, Component, String> {
  public static final GsonComponentSerializer INSTANCE = new GsonComponentSerializer();

  public static final Consumer<GsonBuilder> GSON_BUILDER_CONFIGURER = builder -> {
    // core
    builder.registerTypeHierarchyAdapter(Key.class, KeySerializer.INSTANCE);
    // text
    builder.registerTypeHierarchyAdapter(Component.class, new ComponentSerializerImpl());
    builder.registerTypeAdapter(Style.class, new StyleSerializer());
    builder.registerTypeAdapter(ClickEvent.Action.class, NameMapSerializer.of("click action", ClickEvent.Action.NAMES));
    builder.registerTypeAdapter(HoverEvent.Action.class, NameMapSerializer.of("hover action", HoverEvent.Action.NAMES));
    builder.registerTypeAdapter(HoverEvent.ShowItem.class, new ShowItemSerializer());
    builder.registerTypeAdapter(HoverEvent.ShowEntity.class, new ShowEntitySerializer());
    builder.registerTypeAdapter(TextColorWrapper.class, new TextColorWrapper.Serializer());
    builder.registerTypeHierarchyAdapter(TextColor.class, TextColorSerializer.INSTANCE);
    builder.registerTypeAdapter(TextDecoration.class, NameMapSerializer.of("text decoration", TextDecoration.NAMES));
    builder.registerTypeHierarchyAdapter(BlockNbtComponent.Pos.class, BlockNbtComponentPosSerializer.INSTANCE);
  };

  static final Gson GSON = createGson();

  private static Gson createGson() {
    final GsonBuilder builder = new GsonBuilder();
    GSON_BUILDER_CONFIGURER.accept(builder);
    return builder.create();
  }

  @Override
  public @NonNull Component deserialize(final @NonNull String string) {
    return GSON.fromJson(string, Component.class);
  }

  @Override
  public @NonNull String serialize(final @NonNull Component component) {
    return GSON.toJson(component);
  }
}
