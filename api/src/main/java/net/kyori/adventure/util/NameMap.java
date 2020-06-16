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
package net.kyori.adventure.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A name-value map.
 *
 * @param <E> the type
 */
public final class NameMap<E> {
  private final Map<String, E> nameToValue;
  private final Map<E, String> valueToName;

  private NameMap(final Map<String, E> nameToValue, final Map<E, String> valueToName) {
    this.nameToValue = nameToValue;
    this.valueToName = valueToName;
  }

  /**
   * Creates a name map.
   *
   * @param type the type
   * @param namer the name provider
   * @param <E> the type
   * @return the name map
   */
  public static <E extends Enum<E>> @NonNull NameMap<E> create(final Class<E> type, final @NonNull Function<E, String> namer) {
    return create(type, namer, type.getEnumConstants());
  }

  /**
   * Creates a name map.
   *
   * @param <E> the type
   * @param type the type
   * @param namer the name provider
   * @param constants the constants
   * @return the name map
   */
  @SafeVarargs
  public static <E extends Enum<E>> @NonNull NameMap<E> create(final Class<E> type, final @NonNull Function<E, String> namer, final @NonNull E@NonNull... constants) {
    return create(constants, length -> new EnumMap<>(type), namer);
  }

  /**
   * Creates a name map.
   *
   * @param namer the name provider
   * @param constants the constants
   * @param <E> the type
   * @return the name map
   */
  @SafeVarargs
  public static <E> @NonNull NameMap<E> create(final @NonNull Function<E, String> namer, final @NonNull E@NonNull... constants) {
    return create(constants, HashMap::new, namer);
  }

  @SuppressWarnings("ForLoopReplaceableByForEach")
  private static <E> @NonNull NameMap<E> create(final E[] constants, final IntFunction<Map<E, String>> valueToNameFactory, final @NonNull Function<E, String> namer) {
    final int length = constants.length;
    final Map<String, E> nameToValue = new HashMap<>(length);
    final Map<E, String> valueToName = valueToNameFactory.apply(length); // to support using EnumMap instead of HashMap when possible
    for(int i = 0; i < length; i++) {
      final E constant = constants[i];
      final String name = namer.apply(constant);
      nameToValue.put(name, constant);
      valueToName.put(constant, name);
    }
    return new NameMap<>(Collections.unmodifiableMap(nameToValue), Collections.unmodifiableMap(valueToName));
  }

  /**
   * Gets the name for a value.
   *
   * @param value the value
   * @return the name
   */
  public @NonNull String name(final @NonNull E value) {
    return this.valueToName.get(value);
  }

  /**
   * Gets a value by its name.
   *
   * @param name the name
   * @return the value
   */
  public @NonNull Optional<E> value(final @NonNull String name) {
    return Optional.ofNullable(this.nameToValue.get(name));
  }
}
