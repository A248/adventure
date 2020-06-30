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
package net.kyori.adventure.text.minimessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.kyori.adventure.text.minimessage.Tokens.BOLD;
import static net.kyori.adventure.text.minimessage.Tokens.CLICK;
import static net.kyori.adventure.text.minimessage.Tokens.CLOSE_TAG;
import static net.kyori.adventure.text.minimessage.Tokens.COLOR;
import static net.kyori.adventure.text.minimessage.Tokens.HOVER;
import static net.kyori.adventure.text.minimessage.Tokens.INSERTION;
import static net.kyori.adventure.text.minimessage.Tokens.ITALIC;
import static net.kyori.adventure.text.minimessage.Tokens.KEYBIND;
import static net.kyori.adventure.text.minimessage.Tokens.OBFUSCATED;
import static net.kyori.adventure.text.minimessage.Tokens.SEPARATOR;
import static net.kyori.adventure.text.minimessage.Tokens.STRIKETHROUGH;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_END;
import static net.kyori.adventure.text.minimessage.Tokens.TAG_START;
import static net.kyori.adventure.text.minimessage.Tokens.TRANSLATABLE;
import static net.kyori.adventure.text.minimessage.Tokens.UNDERLINED;

/* package */ class MiniMessageSerializer {

  private MiniMessageSerializer() {
  }

  @NonNull
  /* package */ static String serialize(final @NonNull Component component) {
    final StringBuilder sb = new StringBuilder();

    final List<Component> components = new ArrayList<>();
    components.add(component);

    for (int i = 0; i < components.size(); i++) {
      final Component comp = components.get(i);

      // add childs
      components.addAll(comp.children());

      // # start tags

      // ## get prev comp
      Component prevComp = null;
      if (i > 0) {
        prevComp = components.get(i - 1);
      }

      // ## color
      // ### white is not important
      if (!NamedTextColor.WHITE.equals(comp.color()) && comp.color() != null && (prevComp == null || prevComp.color() != comp.color())) {
        sb.append(startColor(Objects.requireNonNull(comp.color())));
      }

      // ## decoration
      // ### only start if prevComp didn't start
      if (comp.hasDecoration(TextDecoration.BOLD) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.BOLD))) {
        sb.append(startTag(BOLD));
      }
      if (comp.hasDecoration(TextDecoration.ITALIC) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.ITALIC))) {
        sb.append(startTag(ITALIC));
      }
      if (comp.hasDecoration(TextDecoration.OBFUSCATED) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.OBFUSCATED))) {
        sb.append(startTag(OBFUSCATED));
      }
      if (comp.hasDecoration(TextDecoration.STRIKETHROUGH) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.STRIKETHROUGH))) {
        sb.append(startTag(STRIKETHROUGH));
      }
      if (comp.hasDecoration(TextDecoration.UNDERLINED) && (prevComp == null || !prevComp.hasDecoration(TextDecoration.UNDERLINED))) {
        sb.append(startTag(UNDERLINED));
      }

      // ## hover
      // ### only start if prevComp didn't start the same one
      final HoverEvent<?> hov = comp.hoverEvent();
      if (hov != null && (prevComp == null || areDifferent(hov, prevComp.hoverEvent()))) {
        // TODO make sure the value cast is right
        sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", HOVER, HoverEvent.Action.NAMES.key(hov.action()), serialize((Component) hov.value()))));
      }

      // ## click
      // ### only start if prevComp didn't start the same one
      final ClickEvent click = comp.clickEvent();
      if (click != null && (prevComp == null || areDifferent(click, prevComp.clickEvent()))) {
        sb.append(startTag(String.format("%s" + SEPARATOR + "%s" + SEPARATOR + "\"%s\"", CLICK, ClickEvent.Action.NAMES.key(click.action()), click.value())));
      }

      // ## insertion
      // ### only start if prevComp didn't start the same one
      final String insert = comp.insertion();
      if (insert != null && (prevComp == null || !insert.equals(prevComp.insertion()))) {
        sb.append(startTag(INSERTION + SEPARATOR + insert));
      }

      // # append text
      if (comp instanceof TextComponent) {
        sb.append(((TextComponent) comp).content());
      } else {
        handleDifferentComponent(comp, sb);
      }

      // # end tags

      // ## get next comp
      Component nextComp = null;
      if (i + 1 < components.size()) {
        nextComp = components.get(i + 1);
      }

      // ## color
      // ### only end color if next comp is white and current isn't
      if (nextComp != null && comp.color() != NamedTextColor.WHITE && comp.color() != null) {
        if (nextComp.color() == NamedTextColor.WHITE || nextComp.color() == null) {
          sb.append(endColor(Objects.requireNonNull(comp.color())));
        }
      }

      // ## decoration
      // ### only end decoration if next tag is different
      if (nextComp != null) {
        if (comp.hasDecoration(TextDecoration.BOLD) && !nextComp.hasDecoration(TextDecoration.BOLD)) {
          sb.append(endTag(BOLD));
        }
        if (comp.hasDecoration(TextDecoration.ITALIC) && !nextComp.hasDecoration(TextDecoration.ITALIC)) {
          sb.append(endTag(ITALIC));
        }
        if (comp.hasDecoration(TextDecoration.OBFUSCATED) && !nextComp.hasDecoration(TextDecoration.OBFUSCATED)) {
          sb.append(endTag(OBFUSCATED));
        }
        if (comp.hasDecoration(TextDecoration.STRIKETHROUGH) && !nextComp.hasDecoration(TextDecoration.STRIKETHROUGH)) {
          sb.append(endTag(STRIKETHROUGH));
        }
        if (comp.hasDecoration(TextDecoration.UNDERLINED) && !nextComp.hasDecoration(TextDecoration.UNDERLINED)) {
          sb.append(endTag(UNDERLINED));
        }
      }

      // ## hover
      // ### only end hover if next tag is different
      if (nextComp != null && comp.hoverEvent() != null) {
        if (areDifferent(Objects.requireNonNull(comp.hoverEvent()), nextComp.hoverEvent())) {
          sb.append(endTag(HOVER));
        }
      }

      // ## click
      // ### only end click if next tag is different
      if (nextComp != null && comp.clickEvent() != null) {
        if (areDifferent(Objects.requireNonNull(comp.clickEvent()), nextComp.clickEvent())) {
          sb.append(endTag(CLICK));
        }
      }

      // ## insertion
      // ### only end insertion if next tag is different
      if (nextComp != null && comp.insertion() != null) {
        if (!Objects.equals(comp.insertion(), nextComp.insertion())) {
          sb.append(endTag(INSERTION));
        }
      }
    }

    return sb.toString();
  }

  private static boolean areDifferent(final @NonNull ClickEvent c1, final @Nullable ClickEvent c2) {
    if (c2 == null) return true;
    return !c1.equals(c2) && (!c1.action().equals(c2.action()) || !c1.value().equals(c2.value()));
  }

  private static boolean areDifferent(final @NonNull HoverEvent<?> h1, final @Nullable HoverEvent<?> h2) {
    if (h2 == null) return true;
    return !h1.equals(h2) && (!h1.action().equals(h2.action()));// TODO also compare value
  }

  private static @NonNull String startColor(final @NonNull TextColor color) {
    if (color instanceof NamedTextColor) {
      return startTag(Objects.requireNonNull(NamedTextColor.NAMES.key((NamedTextColor) color)));
    } else {
      return startTag(COLOR + SEPARATOR + color.asHexString());
    }
  }

  private static @NonNull String endColor(final @NonNull TextColor color) {
    if (color instanceof NamedTextColor) {
      return endTag(Objects.requireNonNull(NamedTextColor.NAMES.key((NamedTextColor) color)));
    } else {
      return endTag(COLOR + SEPARATOR + color.asHexString());
    }
  }


  private static @NonNull String startTag(final @NonNull String content) {
    return TAG_START + content + TAG_END;
  }


  private static @NonNull String endTag(final @NonNull String content) {
    return TAG_START + CLOSE_TAG + content + TAG_END;
  }

  private static void handleDifferentComponent(final @NonNull Component component, final @NonNull StringBuilder sb) {
    if (component instanceof KeybindComponent) {
      sb.append(startTag(KEYBIND + SEPARATOR + ((KeybindComponent) component).keybind()));
    } else if (component instanceof TranslatableComponent) {
      sb.append(startTag(TRANSLATABLE + SEPARATOR + ((TranslatableComponent) component).key()));
    }
  }
}
