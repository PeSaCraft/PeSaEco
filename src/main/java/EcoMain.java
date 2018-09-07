import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class EcoMain extends JavaPlugin implements Listener {

    private MongoCollection<Document> collection;
    public String Prefix = ChatColor.DARK_PURPLE +  "[PeSaEconomy] ";


    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        String uri = "[URI HERE !!]";
        MongoClientURI clientURI = new MongoClientURI(uri);
        MongoClient mongoClient = new MongoClient(clientURI);

        MongoDatabase mongoDatabase = mongoClient.getDatabase("MongoDB");
        collection = mongoDatabase.getCollection("Minecraft");

        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[MONGODB] Database Connected");

        this.getCommand("eco").setExecutor((CommandExecutor)new EcoCommand());
        this.getCommand("pay").setExecutor((CommandExecutor) new PayCommand());
        this.getCommand("ecoset").setExecutor((CommandExecutor) new EcoSetCommand());
        System.out.println("[PeSaEco gestartet");
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Document playerdoc = new Document("UUID", player.getUniqueId().toString());
        Document found = (Document) collection.find(playerdoc).first();
        if (found == null){
            playerdoc.append("money", 1000);
            playerdoc.append("name", player.getName());
            collection.insertOne(playerdoc);


            this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Player created in Database");
        }else{
            int money = found.getInteger("money");

            this.getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Player found in Database");

        }
    }


}