import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class EcoCommand implements CommandExecutor{

    public String Prefix = ChatColor.DARK_PURPLE +  "[PeSaEconomy] ";
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        Player player = (Player) sender;

        if(sender instanceof Player){

            String uri = "URI HERE";
            MongoClientURI clientURI = new MongoClientURI(uri);
            MongoClient mongoClient = new MongoClient(clientURI);

            MongoDatabase mongoDatabase = mongoClient.getDatabase("MongoDB");
            MongoCollection<Document> collection2 = mongoDatabase.getCollection("Minecraft");
            Document playerdoc = new Document("UUID", player.getUniqueId().toString());
            Document found = (Document) collection2.find(playerdoc).first();
            if (found == null){
                player.sendMessage(ChatColor.RED + "Datenbank Fehler");


            }else{

                int money = found.getInteger("money");
                player.sendMessage( Prefix + ChatColor.AQUA + "Du hast "+ ChatColor.RED +  money + ChatColor.AQUA + " Geld");
            }
               
        }else{
            
        }


        return true;
    }
}
