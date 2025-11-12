package com.willfp.ecomenus.menus

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.gui.addPage
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.page.PageChanger
import com.willfp.eco.core.gui.slot.FillerMask
import com.willfp.eco.core.gui.slot.MaskItems
import com.willfp.ecomenus.components.ConfigurableSlot
import com.willfp.ecomenus.components.addComponent
import com.willfp.ecomenus.components.impl.PositionedPageChanger
import com.willfp.ecomponent.menuStateVar
import com.willfp.libreforge.ViolationContext
import org.bukkit.entity.Player
import java.util.Stack

/**
 * Extension property to track the stack of previous menus for navigation.
 * This allows implementing "back" functionality in menu hierarchies.
 */
val Menu.previousMenus by menuStateVar<Stack<Menu>>("previous-menu", Stack())

/**
 * Safe pop operation on Stack that returns null instead of throwing EmptyStackException.
 * @return The popped element or null if the stack is empty
 */
fun <T> Stack<T>.popOrNull(): T? =
    if (this.empty()) null else this.pop()

/**
 * Opens a menu for a player with optional parent menu tracking.
 *
 * @param player The player to open the menu for
 * @param from The parent menu that this menu is being opened from (for back navigation)
 */
fun Menu.open(
    player: Player,
    from: Menu? = null
) {
    this.open(player)
    if (from != null) {
        this.previousMenus[player] += from
    }
}

/**
 * Closes a menu for a player and returns to the previous menu if one exists.
 * If no previous menu exists, closes the inventory entirely.
 *
 * @param player The player whose menu should be closed
 */
fun Menu.close(player: Player) =
    this.previousMenus[player].popOrNull()?.open(player) ?: player.closeInventory()

/**
 * Builds a Menu instance from YAML configuration.
 *
 * @param plugin The plugin instance
 * @param menu The EcoMenu that this Menu represents
 * @param config The configuration containing menu definition
 * @return A fully configured Menu instance
 */
fun buildMenu(plugin: EcoPlugin, menu: EcoMenu, config: Config): Menu {
    val pageConfigs = config.getSubsections("pages")

    val slots = mutableListOf<ConfigurableSlot>()

    for (slotConfig in config.getSubsections("slots")) {
        val slot = ConfigurableSlot(
            ViolationContext(plugin, "menu ${menu.id}"),
            slotConfig
        )

        slots += slot
    }

    return menu(config.getInt("rows")) {
        title = config.getFormattedString("title")

        allowChangingHeldItem()

        maxPages(pageConfigs.size)

        addComponent(
            PositionedPageChanger(
                PageChanger.Direction.FORWARDS,
                config.getSubsection("forwards-arrow")
            )
        )

        addComponent(
            PositionedPageChanger(
                PageChanger.Direction.BACKWARDS,
                config.getSubsection("backwards-arrow")
            )
        )

        for (page in pageConfigs) {
            val mask = FillerMask(
                MaskItems.fromItemNames(page.getStrings("mask.items")),
                *page.getStrings("mask.pattern").toTypedArray()
            )

            val pageNumber = page.getInt("page")

            addPage(pageNumber) {
                setMask(mask)

                for (slot in slots) {
                    if (slot.page == null || pageNumber == slot.page) {
                        slot.add(this)
                    }
                }
            }
        }

        onClose { event, _ ->
            // Safe cast - event.player should always be a Player in inventory events
            (event.player as? Player)?.let { menu.handleClose(it) }
        }
    }
}
