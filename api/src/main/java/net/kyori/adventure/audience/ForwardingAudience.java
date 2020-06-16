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
package net.kyori.adventure.audience;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An audience that forwards to another audience.
 */
@FunctionalInterface
public interface ForwardingAudience extends Audience {
  /**
   * Gets the forwarded audience.
   *
   * @return the forwarded audience
   */
  @NonNull Audience audience();

  @Override
  default void sendMessage(final @NonNull Component message) {
    this.audience().sendMessage(message);
  }

  @Override
  default void sendActionBar(final @NonNull Component message) {
    this.audience().sendActionBar(message);
  }

  @Override
  default void showTitle(final @NonNull Title title) {
    this.audience().showTitle(title);
  }

  @Override
  default void clearTitle() {
    this.audience().clearTitle();
  }

  @Override
  default void resetTitle() {
    this.audience().resetTitle();
  }

  @Override
  default void showBossBar(final @NonNull BossBar bar) {
    this.audience().showBossBar(bar);
  }

  @Override
  default void hideBossBar(final @NonNull BossBar bar) {
    this.audience().hideBossBar(bar);
  }

  @Override
  default void playSound(final @NonNull Sound sound) {
    this.audience().playSound(sound);
  }

  @Override
  default void playSound(final @NonNull Sound sound, final double x, final double y, final double z) {
    this.audience().playSound(sound, x, y, z);
  }

  @Override
  default void stopSound(final @NonNull SoundStop stop) {
    this.audience().stopSound(stop);
  }
}
