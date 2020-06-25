package eu.toqix.mlgrush.Utils;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;

public class MessageCreator {

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String t(String message) {
        return translate(message);
    }
    public static TextComponent generateComponent(String message) {
        return new TextComponent(t(message));
    }
    public static String kickCreator(String row1, String row2, boolean yz_k) {
        StringBuilder toReturn = new StringBuilder(t("&7\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500[ &c&lMLG-Rush&7 ]\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n\n"));
        toReturn.append(t(row1));
        toReturn.append(t("\n\n"));
        toReturn.append(t(row2));
        if(yz_k) {
            toReturn.append(t("\n&7&oyz_k\n"));
        }
        else {
            toReturn.append(t("\n\n"));
        }
        toReturn.append(t("&7\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500"));
        return toReturn.toString();
    }
}
