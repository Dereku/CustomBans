package me.itzrex.cbans.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Item
{
  private ItemStack i;
  private ItemMeta m;
  private List<String> l = new ArrayList();
  private byte dyecolor = 20;
  private byte potion = 0;
  private byte dataid = 0;
  private LeatherArmorMeta lc;
  private String skull;
  
  public Item(Material m, int amount)
  {
    this.i = new ItemStack(m, amount);
    this.m = this.i.getItemMeta();
    if (isLeather()) {
      this.lc = ((LeatherArmorMeta)this.i.getItemMeta());
    }
  }
  
  public Item(Material m)
  {
    this.i = new ItemStack(m);
    this.m = this.i.getItemMeta();
    if (isLeather()) {
      this.lc = ((LeatherArmorMeta)this.i.getItemMeta());
    }
  }
  
  public void setName(String name)
  {
    this.m.setDisplayName(name);
  }
  
  public Material getMaterial()
  {
    return this.i.getType();
  }
  
  public int getAmount()
  {
    return this.i.getAmount();
  }
  
  public void removeLore()
  {
    this.l.clear();
  }
  
  public void addLore(String... lore)
  {
    String[] arrayOfString;
    int k = (arrayOfString = lore).length;
    for (int j = 0; j < k; j++)
    {
      String s = arrayOfString[j];
      this.l.add(s);
    }
  }
  
  public void setLore(String... lore)
  {
    clearLore();
    String[] arrayOfString;
    int k = (arrayOfString = lore).length;
    for (int j = 0; j < k; j++)
    {
      String s = arrayOfString[j];
      this.l.add(s);
    }
  }
  
  public void clearLore()
  {
    this.l.clear();
  }
  
  public void setSkullOwner(String name)
  {
    this.skull = name;
  }
  
  public void setMaterial(Material m)
  {
    this.i.setType(m);
  }
  
  public void setAmount(int amount)
  {
    this.i.setAmount(amount);
  }
  
  public void setColor(int colorid)
  {
    this.dyecolor = ((byte)colorid);
  }
  
  public void setData(int data)
  {
    this.dataid = ((byte)data);
  }
  
  public byte getData()
  {
    return this.dataid;
  }
  
  public void addEnchantment(Enchantment ench, int level)
  {
    this.m.addEnchant(ench, level, true);
  }
  
  private boolean isLeather()
  {
    if ((getMaterial() == Material.LEATHER_BOOTS) || 
      (getMaterial() == Material.LEATHER_CHESTPLATE) || 
      (getMaterial() == Material.LEATHER_HELMET) || 
      (getMaterial() == Material.LEATHER_LEGGINGS)) {
      return true;
    }
    return false;
  }
  
  public void setLeatherColor(Color c)
  {
    if (isLeather()) {
      this.lc.setColor(c);
    }
  }
  
  public void setPotion(int id)
  {
    this.potion = ((byte)id);
  }
  
  public ItemStack getItem()
  {
    if (this.dyecolor < 20) {
      this.i = new ItemStack(this.i.getType(), this.i.getAmount(), this.dyecolor);
    }
    if (this.potion > 0) {
      this.i = new ItemStack(this.i.getType(), this.i.getAmount(), this.potion);
    }
    if (this.dataid != 0) {
      this.i = new ItemStack(this.i.getType(), this.i.getAmount(), this.dataid);
    }
    if ((this.skull != null) && (this.i.getType() == Material.SKULL_ITEM))
    {
      SkullMeta meta = (SkullMeta)this.i.getItemMeta();
      meta.setOwner(this.skull);
      meta.setDisplayName(this.m.getDisplayName());
      meta.setLore(this.l);
      this.i.setItemMeta(meta);
    }
    this.m.setLore(this.l);
    if (isLeather())
    {
      this.lc.setDisplayName(this.m.getDisplayName());
      this.lc.setLore(this.l);
      this.i.setItemMeta(this.lc);
    }
    else
    {
      this.i.setItemMeta(this.m);
    }
    return this.i;
  }
}