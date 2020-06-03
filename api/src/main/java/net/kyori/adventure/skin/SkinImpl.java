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
package net.kyori.adventure.skin;

import java.util.Objects;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/* package */ final class SkinImpl implements Skin, Examinable {
  /* package */ final static Skin EMPTY = new SkinImpl(null, null);
  private final String data;
  private final String signature;

  /* package */ SkinImpl(final @Nullable String data, final @Nullable String signature) {
    this.data = data;
    this.signature = signature;
  }

  @Override
  public @Nullable String data() {
    return this.data;
  }

  @Override
  public @Nullable String signature() {
    return this.signature;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("data", this.data),
      ExaminableProperty.of("signature", this.signature)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(!(other instanceof Skin)) return false;
    final Skin that = (Skin) other;
    return Objects.equals(this.data, that.data()) && Objects.equals(this.signature, that.signature());
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(this.data);
    result = (31 * result) + Objects.hashCode(this.signature);
    return result;
  }
}
