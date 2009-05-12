package org.mazur.subrosa.gui;

import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Version: $Id$
 *
 * @author Roman Mazur (mailto: mazur.roman@gmail.com)
 *
 */
public final class IconsFactory {

  private static TreeMap<String, Icon> map = new TreeMap<String, Icon>();

  private IconsFactory() {}
  
  public static Icon getIcon(final String name) {
    Icon icon = map.get(name);
    if (icon != null) { return icon; }
    icon = new ImageIcon(IconsFactory.class.getResource("/images/" + name +".gif"));
    map.put(name, icon);
    return icon;
  }
  
}
