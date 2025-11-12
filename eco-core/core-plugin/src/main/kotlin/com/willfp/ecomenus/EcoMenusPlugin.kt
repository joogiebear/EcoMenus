package com.willfp.ecomenus

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecomenus.commands.CommandEcoMenus
import com.willfp.ecomenus.libreforge.EffectOpenMenu
import com.willfp.ecomenus.menus.EcoMenus
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.loader.LibreforgePlugin
import com.willfp.libreforge.loader.configs.ConfigCategory
import net.kyori.adventure.text.logger.slf4j.ComponentLogger

/**
 * Main plugin class for EcoMenus.
 *
 * EcoMenus is a powerful GUI menu system that allows server administrators to create
 * complex, interactive inventory-based menus using YAML configuration files without writing code.
 *
 * Features:
 * - YAML-based menu configuration
 * - LibreForge integration for conditions and effects
 * - Dynamic slot configuration with click handlers
 * - Multi-page menu support
 * - Menu navigation with parent tracking
 * - PlaceholderAPI support
 */
class EcoMenusPlugin : LibreforgePlugin() {
    /**
     * Handles plugin enable logic.
     * Registers custom LibreForge effects for menu interactions.
     */
    override fun handleEnable() {
        Effects.register(EffectOpenMenu)
    }

    /**
     * Loads plugin commands.
     * @return List of plugin commands including the main /ecomenus command
     */
    override fun loadPluginCommands(): List<PluginCommand> {
        return listOf(
            CommandEcoMenus(this)
        )
    }

    /**
     * Loads configuration categories.
     * @return List of config categories including the EcoMenus registry
     */
    override fun loadConfigCategories(): List<ConfigCategory> {
        return listOf(
            EcoMenus
        )
    }
}
