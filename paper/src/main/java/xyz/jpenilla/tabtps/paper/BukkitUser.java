/*
 * This file is part of TabTPS, licensed under the MIT License.
 *
 * Copyright (c) 2020-2024 Jason Penilla
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
package xyz.jpenilla.tabtps.paper;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.tabtps.common.AbstractUser;
import xyz.jpenilla.tabtps.common.TabTPS;
import xyz.jpenilla.tabtps.common.util.Serializers;

import static xyz.jpenilla.tabtps.paper.util.SpigotReflection.spigotReflection;

@DefaultQualifier(NonNull.class)
public final class BukkitUser extends AbstractUser<Player> {
  private final BukkitAudiences audiences;
  private @MonotonicNonNull Audience audience;

  private BukkitUser(final TabTPS tabTPS, final Player player) {
    super(tabTPS, player, player.getUniqueId());
    this.audiences = ((TabTPSPlugin) tabTPS.platform()).audiences();
  }

  public static BukkitUser from(final TabTPS tabTPS, final Player player) {
    return new BukkitUser(tabTPS, player);
  }

  @Override
  public Component displayName() {
    return Serializers.LEGACY_SECTION.deserialize(this.base().getDisplayName());
  }

  @Override
  public boolean hasPermission(final String permissionString) {
    return this.base().hasPermission(permissionString);
  }

  @Override
  public boolean online() {
    return this.base().isOnline();
  }

  @Override
  public int ping() {
    if (PaperLib.getMinecraftVersion() >= 17) {
      return this.base().getPing();
    }
    return PaperLib.getMinecraftVersion() < 16 || !PaperLib.isPaper()
      ? spigotReflection().ping(this.base())
      : this.base().spigot().getPing();
  }

  @Override
  public Audience audience() {
    if (this.audience == null) {
      this.audience = this.audiences.player(this.base());
    }
    return this.audience;
  }
}
