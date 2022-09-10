package de.marcus.javagame.datahandling.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.marcus.javagame.datahandling.Loadable;
import de.marcus.javagame.entities.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Inventory extends Loadable {
    @JsonIgnore
    public static final int MAX_ITEM_STACK = 64;
    @JsonIgnore
    public static final int MAX_POTION_STACK = 6;
    @JsonIgnore
    public static final int MAX_WEAPON_STACK = 1;
    @JsonIgnore
    public static final int INVENTORY_SIZE = 30;
    @JsonIgnore
    public static final int HOTBAR_SIZE = 10;
    @JsonIgnore
    Player p;

    @JsonProperty("inventory_data")
    public List<InventorySlot> inventory;


    @JsonProperty("hotbar_data")
    public List<InventorySlot> hotbar;

    public Inventory(List<InventorySlot> inventory, List<InventorySlot> hotbar) {
        this.inventory = inventory;
        this.hotbar = hotbar;
    }

    public Inventory() {
        this.inventory = new ArrayList<>();
        this.hotbar = new ArrayList<>();
        p = null;
    }

    @JsonIgnore
    public void setPlayer(Player player) {
        this.p = player;
    }

    public boolean removeItem(int slot, int amount) {
        InventorySlot remove = inventory.get(slot);
        int itemsLeft = remove.getItemCount() - amount;

        if (remove.getItem().isDeletable()) {
            if(itemsLeft < 0)
                inventory.remove(remove);
            else
                remove.setItemCount(itemsLeft);
            return true;
        }
        return false;

    }

    public boolean useItem(int selectedItem) {
        InventorySlot slot = inventory.get(selectedItem);
        int itemsLeft = slot.getItemCount() - 1;

        if (slot.getItem().isUsable()) {
            if(itemsLeft < 0) {
                inventory.remove(selectedItem);
            } else {
                slot.setItemCount(itemsLeft);
            }
            p.useItem(slot.getItem());
            return true;
        }

        return false;

    }

    /**
     * @return If the item was added
     */
    public boolean addItem(InventorySlot slot) {

        if (!inventory.isEmpty()) {
            for (InventorySlot inventorySlot : inventory) {
                if (inventorySlot.getItem().equals(slot.getItem()) && (inventorySlot.getItemCount() + slot.getItemCount()) < inventorySlot.getItem().getMaxStackSize()) {
                    inventorySlot.setItemCount(inventorySlot.getItemCount() + slot.getItemCount());
                    return true;
                } else if (inventory.size() < INVENTORY_SIZE) {
                    inventory.add(slot);
                    return true;
                } else {
                    return false;
                }
            }
            //This should never happen. At least I hope so...
            return false;
        } else {
            inventory.add(slot);
            return true;
        }

    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventory=" + inventory +
                ", hotbar=" + hotbar +
                '}';
    }


}
