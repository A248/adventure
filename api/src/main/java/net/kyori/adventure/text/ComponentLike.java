/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
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
package net.kyori.adventure.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;

/**
 * Something that can be represented as a {@link Component}.
 *
 * @since 4.0.0
 */
@FunctionalInterface
public interface ComponentLike {
  /**
   * Converts a list of {@link ComponentLike}s to a list of {@link Component}s.
   *
   * @param likes the component-likes
   * @return the components
   * @since 4.8.0
   */
  static @NonNull List<Component> asComponents(final @NonNull List<? extends ComponentLike> likes) {
    return asComponents(likes, null);
  }

  /**
   * Converts a list of {@link ComponentLike}s to a list of {@link Component}s.
   *
   * <p>Only components that match {@code filter} will be returned.</p>
   *
   * @param likes the component-likes
   * @param filter the component filter
   * @return the components
   * @since 4.8.0
   */
  static @NonNull List<Component> asComponents(final @NonNull List<? extends ComponentLike> likes, final @Nullable Predicate<? super Component> filter) {
    if(likes.isEmpty()) {
      // We do not need to create a new list if the one we are copying is empty - we can
      // simply just return our known-empty list instead.
      return Collections.emptyList();
    }
    final int size = likes.size();
    final List<Component> components = new ArrayList<>(size);
    for(int i = 0; i < size; i++) {
      final ComponentLike like = likes.get(i);
      final Component component = like.asComponent();
      if(filter == null || filter.test(component)) {
        components.add(component);
      }
    }
    return Collections.unmodifiableList(components);
  }

  /**
   * Gets a {@link Component} representation.
   *
   * @return a component
   * @since 4.0.0
   */
  @Contract(pure = true)
  @NonNull Component asComponent();
}
