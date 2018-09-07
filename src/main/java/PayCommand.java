import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class PayCommand implements CommandExecutor {

    public String Prefix = ChatColor.DARK_PURPLE + "[PeSaEconomy] ";
    public int PayAmount;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        Player a = Bukkit.getPlayer(args[0]);
        String msg;
        int i;
        if (a != null) {
            msg = "";
            for (i = 1; i < args.length; i++) {
                msg = msg + " " + args[i];
            }
            String uri = "URI HERE";
            MongoClientURI clientURI = new MongoClientURI(uri);
            MongoClient mongoClient = new MongoClient(clientURI);

            MongoDatabase mongoDatabase = mongoClient.getDatabase("MongoDB");
            MongoCollection<Document> collection2 = mongoDatabase.getCollection("Minecraft");
            Document playerdoc = new Document("UUID", player.getUniqueId().toString());
            Document found = (Document) collection2.find(playerdoc).first();
            if (found == null) {
                player.sendMessage(ChatColor.RED + "Datenbank Fehler");


            } else {

                int money = found.getInteger("money");
                int amount = Integer.parseInt(args[1]);
                int newmoney = money - amount;
                PayAmount = amount;
                System.out.println(amount);
                System.out.println(newmoney);
                Bson updatedvalue = new Document("money", newmoney);
                Bson updateoperation = new Document("$set", updatedvalue);
                collection2.updateOne(found, updateoperation);
                player.sendMessage(Prefix + ChatColor.AQUA + "Du hast " + ChatColor.RED + amount + ChatColor.AQUA + " gegeben");

            }
            Document playerdoc2 = new Document("UUID", a.getUniqueId().toString());
            Document found2 = (Document) collection2.find(playerdoc2).first();
            if (found == null) {
                player.sendMessage(ChatColor.RED + "Datenbank Fehler");


            } else {

                int money = found2.getInteger("money");
                int amount = Integer.parseInt(args[1]);
                int newmoney = money + PayAmount;
                System.out.println(amount);
                System.out.println(newmoney);
                Bson updatedvalue = new Document("money", newmoney);
                Bson updateoperation = new Document("$set", updatedvalue);
                collection2.updateOne(found2, updateoperation);
                player.sendMessage(Prefix + ChatColor.AQUA +  player.getName() + "Hat dir " + ChatColor.RED + amount + ChatColor.AQUA + " Geld gegeben");

            }


        } else {

            player.sendMessage("Dieser Spieler ist nicht auf dem Server!");
        }
        return true;

    }
}

