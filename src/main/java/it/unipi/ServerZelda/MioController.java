/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.ServerZelda;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import static it.unipi.ServerZelda.ServerZeldaApplication.dbName;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Antonio
 */
@Controller
@RequestMapping(path="/ZeldaWiki")
public class MioController {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CharacterRepository characterRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MonsterRepository monsterRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private DungeonRepository dungeonRepository;
    @Autowired
    private BossRepository bossRepository;
    @Autowired
    private CredenzialiRepository credenzialeRepository;
    
    private LinguaggioServer lang;
    private String linguaCorrente;
    private static final Logger logger = LogManager.getLogger(MioController.class);
    
    
    
    /**
     * 
     * @param language 
     */
    private void settaLinguaggio(String language) {
        if (linguaCorrente != null && linguaCorrente.equals(language))
            return;
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("linguaggioServer", LinguaggioServer.class);
        lang = (LinguaggioServer) xstream.fromXML(getClass().getResource("languages/" + language + ".xml"));
        linguaCorrente = language;
    }
    
    /**
     * 
     * @param url
     * @return
     * @throws IOException 
     */
    private JsonArray getDataAsJsonArray(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();
        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
        JsonObject rootObject = json.getAsJsonObject();
        return rootObject.get("data").getAsJsonArray();
    }
    
    
    /**
     * 
     * @throws IOException
     * @throws ParseException 
     */
    private void addGames() throws IOException, ParseException {
        JsonArray Games = getDataAsJsonArray(new URL("https://zelda.fanapis.com/api/games"));
        //Specifico il formato della data e che è in inglese.
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH); //mi serve per inserire la data in Sql.
        SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //formato per MySQL
        java.util.Date utilDate; //uso UtilDate perché non dà problemi e fornisce i formati di conversione per le date.
        JsonObject iter;
        Gson gson = new Gson();
        for (int i = 0; i < Games.size(); ++i) {
            iter = Games.get(i).getAsJsonObject();
            //trim() toglie gli spazi a inizio e a fine riga.
            utilDate = dateFormat.parse(iter.get("released_date").getAsString().trim());
            //sovrascrivo la data con un formato compatibile con MySQL
            iter.addProperty("released_date", mysqlDateFormat.format(utilDate));
            //inserisco nel database l'elemento
            gameRepository.save(gson.fromJson(iter, Game.class));
        }
    }

    /**
     * Non uso generics per le varie add()
     * perché i tipi generici non conservano il tipo effettive a runtime (type erasure).
     * In più Gson ha problemi con i tipi parametrici, quindi si potrebbe usare il TypToken di Gson.
     * Però, al tempo stesso, quello crea problemi con CrudRepository
     * Ognuna della varie add() riceve i dati sottoforma di JSON e li inserisci nella corrispondente
     * tabella sfruttando Hibernate.
    */
    
    
    /**
     * 
     * @throws IOException
     * @throws ParseException 
     */
    @Transactional
    private void addCharacters() throws IOException, ParseException {
        JsonArray Characters = getDataAsJsonArray(new URL("https://zelda.fanapis.com/api/characters"));
        Gson gson = new Gson();
        Character[] arrayCharacters = gson.fromJson(Characters, Character[].class);
        for (int i = 0; i < arrayCharacters.length; ++i)
            characterRepository.save(arrayCharacters[i]);
    }
    
    /**
     * 
     * @throws IOException
     * @throws ParseException 
     */
    @Transactional
    private void addMonsters() throws IOException, ParseException {
        JsonArray Monsters = getDataAsJsonArray(new URL("https://zelda.fanapis.com/api/monsters"));
        Gson gson = new Gson();
        Monster[] arrayMonsters = gson.fromJson(Monsters, Monster[].class);
        for (int i = 0; i < arrayMonsters.length; ++i)
            monsterRepository.save(arrayMonsters[i]);
    }
    
    /**
     * 
     * @throws IOException
     * @throws ParseException 
     */
    @Transactional
    private void addDungeons() throws IOException, ParseException {
        JsonArray Dungeons = getDataAsJsonArray(new URL("https://zelda.fanapis.com/api/dungeons"));
        Gson gson = new Gson();
        Dungeon[] arrayDungeons = gson.fromJson(Dungeons, Dungeon[].class);
        for (int i = 0; i < arrayDungeons.length; ++i)
            dungeonRepository.save(arrayDungeons[i]);
    }
    
    
    /**
     * 
     * @throws IOException
     * @throws ParseException 
     */
    @Transactional
    private void addPlaces() throws IOException, ParseException {
        JsonArray Places = getDataAsJsonArray(new URL("https://zelda.fanapis.com/api/places"));
        Gson gson = new Gson();
        Place[] arrayPlaces = gson.fromJson(Places, Place[].class);
        for (int i = 0; i < arrayPlaces.length; ++i)
            placeRepository.save(arrayPlaces[i]);
    }
    
    /**
     * 
     * @throws IOException
     * @throws ParseException 
     */
    @Transactional
    private void addBosses() throws IOException, ParseException {
        JsonArray Bosses = getDataAsJsonArray(new URL("https://zelda.fanapis.com/api/bosses"));
        Gson gson = new Gson();
        Boss[] arrayBosses = gson.fromJson(Bosses, Boss[].class);
        for (int i = 0; i < arrayBosses.length; ++i)
            bossRepository.save(arrayBosses[i]);
    }
    
    /**
     * 
     * @throws IOException
     * @throws ParseException 
     */
    @Transactional
    private void addItems() throws IOException, ParseException {
        JsonArray Items = getDataAsJsonArray(new URL("https://zelda.fanapis.com/api/items"));
        Gson gson = new Gson();
        Item[] arrayItems = gson.fromJson(Items, Item[].class);
        for (int i = 0; i < arrayItems.length; ++i)
            itemRepository.save(arrayItems[i]);
    }

    /**
     * 
     * @param language
     * @return 
     */
    @Transactional
    @GetMapping(path="/populate")
    public @ResponseBody Messaggio popolulate(@RequestParam String language) {
        settaLinguaggio(language);
        try {
            /*
            A ogni apertura del cliente, si permette di ripremere il tasto popola (
            si prova a scaricare sempre i dati per vedere se ci
            sono aggiornamenti). Ovviamente, se l'operazione a buon fine, quel client non potrà ripremere
            popola. Questo è implementato nel client.
            Nel server, si permette di richiamare la popola a prescindere dal fatto che le tabelle
            siano già popolate.
            
            Nel JSON di games c'è un'entrata duplicata, ma CrudRepository me l'ha fatta scavalcare.
            */
            addGames();
            addCharacters();
            addDungeons();
            addItems();
            addBosses();
            addMonsters();
            addPlaces();
        }
        catch(ParseException | IOException e) {
            logger.error(e.getMessage());
            return new Messaggio(lang.genericError, 500);
        }
        //return new Messaggio(lang.populationSuccess, 200);
        logger.info(lang.creationSuccess);
        return new Messaggio(lang.creationSuccess, 200);
    }
    
    
    /***
     * @RequestBody per il corpo
     * @RequestParam per i query parameters
     * @param credenziali
     * @param language
     * @return 
     * A prescindere dal faatto che un utente aggiunga elementi ai suoi preferiti, si creano le tabelle
     * dove questi, eventualmente, saranno inseriti. Oltre a questo, ovviamente, si registra l'utente.
     */
    @Transactional
    @PostMapping(path="/signup")
    public @ResponseBody Messaggio signup(@RequestBody Credenziali credenziali, @RequestParam String language) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root");
            PreparedStatement ps = co.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");) {
            settaLinguaggio(language);
            ps.setString(1, credenziali.username);
            ps.setString(2, credenziali.password);
            ps.executeUpdate();
            co.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS`" + credenziali.username + "game" + "` (`name` VARCHAR(100) NOT NULL, PRIMARY KEY (`name`));");
            co.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS`" + credenziali.username + "character" + "` (`name` VARCHAR(100) NOT NULL, PRIMARY KEY (`name`));");
            co.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS`" + credenziali.username + "dungeon" + "` (`name` VARCHAR(100) NOT NULL, PRIMARY KEY (`name`));");
            co.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS`" + credenziali.username + "item" + "` (`name` VARCHAR(100) NOT NULL, PRIMARY KEY (`name`));");
            co.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS`" + credenziali.username + "boss" + "` (`name` VARCHAR(100) NOT NULL, PRIMARY KEY (`name`));");
            co.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS`" + credenziali.username + "place" + "` (`name` VARCHAR(100) NOT NULL, PRIMARY KEY (`name`));");
            co.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS`" + credenziali.username + "monster" + "` (`name` VARCHAR(100) NOT NULL, PRIMARY KEY (`name`));");
        } catch (SQLException e) {
           logger.error(e.getMessage());
           if (e.getMessage().equals("Unknown database '" + dbName + "'")) //Non parte mai, a regola (a meno che Hybernate non faccia scherzi).
               return new Messaggio(lang.alreadyPopulatedDB, 500);
           else if (e.getMessage().contains("Duplicate entry"))
               return new Messaggio(lang.alreadyExistingUser, 500);
           return new Messaggio(lang.genericError, 500);
       }
        logger.info(lang.userSignedUp);
        return new Messaggio(lang.userSignedUp, 200);
    }
    
    /**
     * 
     * @param credenziali
     * @param language
     * @return
     * Metto transactional per evitare che il database venga modficato mentre un utente logga.
     */
    @Transactional
    @PostMapping(path="/signin")
    public @ResponseBody Messaggio signin(@RequestBody Credenziali credenziali, @RequestParam String language) {
        settaLinguaggio(language);
        Credenziali foundCredentials = credenzialeRepository.findByUsernameAndPassword(credenziali.username, credenziali.password);
        if (credenziali.equals(foundCredentials))
            return new Messaggio(lang.userSignedIn, 200);
        else {
             //questa fa match solo con lo username. La findByUsernameAndPassword() dà match se e solo se sono trova corrispondenza, nello stesso record, sia per username che per password
            if (credenziali.username.equals(credenzialeRepository.findByUsername(credenziali.username).username))
                return new Messaggio(lang.wrongPassword, 500);
            return new Messaggio(lang.wrongCredentials, 500);
        }
    }
    
    /**
     * 
     * @param searchClass
     * @return 
     */
    @Transactional
    @PostMapping(path="searchGame")
    public @ResponseBody Game[] searchGame(@RequestBody SearchClass searchClass) {
        return gameRepository.findByNameContaining(searchClass.keyword);
    }
    
    /**
     * 
     * @param searchClass
     * @return 
     */
    @PostMapping(path="searchCharacter")
    public @ResponseBody Character[] searchCharacter(@RequestBody SearchClass searchClass) {
        return characterRepository.findByNameContaining(searchClass.keyword);
    }
    
    /**
     * 
     * @param searchClass
     * @return 
     */
    @PostMapping(path="searchItem")
    public @ResponseBody Item[] searchItem(@RequestBody SearchClass searchClass) {
        return itemRepository.findByNameContaining(searchClass.keyword);
    }
    
    /**
     * 
     * @param searchClass
     * @return 
     */
    @PostMapping(path="searchPlace")
    public @ResponseBody Place[] searchPlace(@RequestBody SearchClass searchClass) {
        return placeRepository.findByNameContaining(searchClass.keyword);
    }
    
    /**
     * 
     * @param searchClass
     * @return 
     */
    @PostMapping(path="searchMonster")
    public @ResponseBody Monster[] searchMonster(@RequestBody SearchClass searchClass) {
        return monsterRepository.findByNameContaining(searchClass.keyword);
    }
    
    /**
     * 
     * @param searchClass
     * @return 
     */
    @PostMapping(path="searchBoss")
    public @ResponseBody Boss[] searchBoss(@RequestBody SearchClass searchClass) {
        return bossRepository.findByNameContaining(searchClass.keyword);
    }
    
    /**
     * 
     * @param searchClass
     * @return 
     */
    @PostMapping(path="searchDungeon")
    public @ResponseBody Dungeon[] searchDungeon(@RequestBody SearchClass searchClass) {
        return dungeonRepository.findByNameContaining(searchClass.keyword);
    }
    
    /**
     * Tutte le funzioni del tipo addElementToFavorites() hanno una struttura comune, quindi chiamano una
     * funzione addToFavorites() che, se dà true, significa che l'elemento è stato aggiunto ai preferiti
     * dell'utente.
     * @param gameName
     * @param username
     * @return: ritornano un messaggio al chiamante che comprende anche lo status code.
     */
    @PostMapping(path="addGameToFavorites")
    public @ResponseBody Messaggio addGameToFavorites(@RequestBody SearchClass gameName, @RequestParam String username) {
        if (addToFavorites("game", username, gameName.keyword))
            return new Messaggio("OK", 200); //l'utente non lo legge: se è positivo, appare il tasto MenuItem "Rimuovi dai preferiti".
        return new Messaggio(lang.genericError,500);
    }
    
    /**
     * 
     * @param characterName
     * @param username
     * @return 
     */
    @PostMapping(path="addCharacterToFavorites")
    public @ResponseBody Messaggio addCharacterToFavorites(@RequestBody SearchClass characterName, @RequestParam String username) {
        if (addToFavorites("character", username, characterName.keyword))
            return new Messaggio("OK", 200); //l'utente non lo legge: se è positivo, appare il tasto MenuItem "Rimuovi dai preferiti".
        return new Messaggio(lang.genericError,500);
    }
    
    /**
     * 
     * @param itemName
     * @param username
     * @return 
     */
    @PostMapping(path="addItemToFavorites")
    public @ResponseBody Messaggio addItemToFavorites(@RequestBody SearchClass itemName, @RequestParam String username) {
        if (addToFavorites("item", username, itemName.keyword))
            return new Messaggio("OK", 200); //l'utente non lo legge: se è positivo, appare il tasto MenuItem "Rimuovi dai preferiti".
        return new Messaggio(lang.genericError,500);
    }
    
    /**
     * 
     * @param bossName
     * @param username
     * @return 
     */
    @PostMapping(path="addBossToFavorites")
    public @ResponseBody Messaggio addBossToFavorites(@RequestBody SearchClass bossName, @RequestParam String username) {
        if (addToFavorites("boss", username, bossName.keyword))
            return new Messaggio("OK", 200); //l'utente non lo legge: se è positivo, appare il tasto MenuItem "Rimuovi dai preferiti".
        return new Messaggio(lang.genericError,500);
    }
    
    /**
     * 
     * @param placeName
     * @param username
     * @return 
     */
    @PostMapping(path="addPlaceToFavorites")
    public @ResponseBody Messaggio addPlaceToFavorites(@RequestBody SearchClass placeName, @RequestParam String username) {
        if (addToFavorites("place", username, placeName.keyword))
            return new Messaggio("OK", 200); //l'utente non lo legge: se è positivo, appare il tasto MenuItem "Rimuovi dai preferiti".
        return new Messaggio(lang.genericError,500);
    }
    
    /**
     * 
     * @param dungeonName
     * @param username
     * @return 
     */
    @PostMapping(path="addDungeonToFavorites")
    public @ResponseBody Messaggio addDungeonToFavorites(@RequestBody SearchClass dungeonName, @RequestParam String username) {
        if (addToFavorites("dungeon", username, dungeonName.keyword))
            return new Messaggio("OK", 200); //l'utente non lo legge: se è positivo, appare il tasto MenuItem "Rimuovi dai preferiti".
        return new Messaggio(lang.genericError,500);
    }
    
    /**
     * 
     * @param monsterName
     * @param username
     * @return 
     */
    @PostMapping(path="addMonsterToFavorites")
    public @ResponseBody Messaggio addMonsterToFavorites(@RequestBody SearchClass monsterName, @RequestParam String username) {
        if (addToFavorites("monster", username, monsterName.keyword))
            return new Messaggio("OK", 200); //l'utente non lo legge: se è positivo, appare il tasto MenuItem "Rimuovi dai preferiti".
        return new Messaggio(lang.genericError,500);
    }
    
    
    /**
     * 
     * @param tableName
     * @param username
     * @param elementName
     * @return 
     */
    private boolean addToFavorites(String tableName, String username, String elementName) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root");
            PreparedStatement ps = co.prepareStatement("REPLACE INTO " + username + tableName + " (name) VALUES (?)")) {
            //co.createStatement().executeUpdate("USE " + dbName); //se già presente, l'eccezione viene catturata
            co.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS`" + username + tableName + "` (`name` VARCHAR(100) NOT NULL, PRIMARY KEY (`name`));");
            ps.setString(1, elementName);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
    
    
    
    /**
     * Per trovare i preferiti mi basta il nome della tabella (game, gungeon, monster, place, intem, boss)
     * e lo username, entrambi minuscoli (il nome della tabella deve essere anche al singolare).
     * Il nome della tabella è incluso nel path (lì inizia con la maiuscola per il camelCase).
     * Si usano funzioni non coi generics per via del type erasure, che non supporta l'operatore new.
     * @param username
     * @return 
     */
    @GetMapping(path="searchFavoriteGame")
    public @ResponseBody Game[] searchFavoriteGame(@RequestParam String username) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root")) {
            Statement st = co.createStatement();
            List<Game> al = new ArrayList<>();
            ResultSet rs = st.executeQuery("SELECT c.* FROM " + username + "game ac INNER JOIN `games` c ON ac.name = c.name;");
            while(rs.next()) {
                al.add(new Game(rs.getString("name"), rs.getString("description"),
                        rs.getDate("released_date")));
            }
            if (al.isEmpty())
                return null;
            return al.toArray(new Game[0]); //passare un array vuoto del tipo desiderato è un modo per far capire il tipo alla toArray() a runtime.
        }
        catch (SQLException e) { //lanciata in caso di connessione non riuscita.
            logger.error(e.getMessage());
            return null;
        }
    }
    
    /**
     * 
     * @param username
     * @return 
     */
    @GetMapping(path="searchFavoriteCharacter")
    public @ResponseBody Character[] searchFavoriteCharacter(@RequestParam String username) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root")) {
            Statement st = co.createStatement();
            List<Character> al = new ArrayList<>();
            ResultSet rs = st.executeQuery("SELECT c.* FROM " + username + "character ac INNER JOIN `characters` c ON ac.name = c.name;");
            while(rs.next()) {
                al.add(new Character(rs.getString("name"), rs.getString("description"),
                        rs.getString("gender"), rs.getString("race")));
            }
            if (al.isEmpty())
                return null;
            return (Character[])al.toArray(new Character[0]);
        }
        catch (SQLException e) { //lanciata in caso di connessione non riuscita.
            logger.error(e.getMessage());
            return null;
        }
    }
    
    
    /***********
     * Si noti che non si usano generics perché danno problemi con l'operatore new (type erasure)
     * @param username
     * @return 
     */
    
    /**
     * 
     * @param username
     * @return 
     */
    @GetMapping(path="searchFavoriteItem")
    public @ResponseBody Item[] searchFavoriteItem(@RequestParam String username) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root")) {
            Statement st = co.createStatement();
            List<Item> al = new ArrayList<>();
            ResultSet rs = st.executeQuery("SELECT c.* FROM " + username + "item ac INNER JOIN `items` c ON ac.name = c.name;");
            while(rs.next()) {
                al.add(new Item(rs.getString("name"), rs.getString("description")));
            }
            if (al.isEmpty())
                return null;
            return (Item[])al.toArray(new Item[0]);
        }
        catch (SQLException e) { //lanciata in caso di connessione non riuscita.
            logger.error(e.getMessage());
            return null;
        }
        
    }
    /**
     * 
     * @param username
     * @return 
     */
    @GetMapping(path="searchFavoriteBoss")
    public @ResponseBody Boss[] searchFavoriteBoss(@RequestParam String username) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root")) {
            Statement st = co.createStatement();
            List<Boss> al = new ArrayList<>();
            ResultSet rs = st.executeQuery("SELECT c.* FROM " + username + "boss ac INNER JOIN `bosses` c ON ac.name = c.name;");
            while(rs.next()) {
                al.add(new Boss(rs.getString("name"), rs.getString("description")));
            }
            if (al.isEmpty())
                return null;
            return (Boss[])al.toArray(new Boss[0]);
        }
        catch (SQLException e) { //lanciata in caso di connessione non riuscita.
            logger.error(e.getMessage());
            return null;
        }
        
    }
    
    /**
     * 
     * @param username
     * @return 
     */
    @GetMapping(path="searchFavoritePlace")
    public @ResponseBody Place[] searchFavoritePlace(@RequestParam String username) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root")) {
            Statement st = co.createStatement();
            List<Place> al = new ArrayList<>();
            ResultSet rs = st.executeQuery("SELECT c.* FROM " + username + "place ac INNER JOIN `places` c ON ac.name = c.name;");
            while(rs.next()) {
                al.add(new Place(rs.getString("name"), rs.getString("description")));
            }
            if (al.isEmpty())
                return null;
            return (Place[])al.toArray(new Place[0]);
        }
        catch (SQLException e) { //lanciata in caso di connessione non riuscita.
            logger.error(e.getMessage());
            return null;
        }
        
    }
    
    /**
     * 
     * @param username
     * @return 
     */
    @GetMapping(path="searchFavoriteDungeon")
    public @ResponseBody Dungeon[] searchFavoriteDungeon(@RequestParam String username) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root")) {
            Statement st = co.createStatement();
            List<Dungeon> al = new ArrayList<>();
            ResultSet rs = st.executeQuery("SELECT c.* FROM " + username + "dungeon ac INNER JOIN `dungeons` c ON ac.name = c.name;");
            while(rs.next()) {
                al.add(new Dungeon(rs.getString("name"), rs.getString("description")));
            }
            if (al.isEmpty())
                return null;
            return (Dungeon[])al.toArray(new Dungeon[0]);
        }
        catch (SQLException e) { //lanciata in caso di connessione non riuscita.
            logger.error(e.getMessage());
            return null;
        }
        
    }
    
    /**
     * 
     * @param username
     * @return 
     */
    @GetMapping(path="searchFavoriteMonster")
    public @ResponseBody Monster[] searchFavoriteMonster(@RequestParam String username) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root")) {
            Statement st = co.createStatement();
            List<Monster> al = new ArrayList<>();
            ResultSet rs = st.executeQuery("SELECT c.* FROM " + username + "monster ac INNER JOIN `monsters` c ON ac.name = c.name;");
            while(rs.next()) {
                al.add(new Monster(rs.getString("name"), rs.getString("description")));
            }
            if (al.isEmpty())
                return null;
            return (Monster[])al.toArray(new Monster[0]);
        }
        catch (SQLException e) { //lanciata in caso di connessione non riuscita.
            logger.error(e.getMessage());
            return null;
        }
        
    }
    
    
    /**
     * 
     * @param searchClass: ci si mette dell'elemento da eliminare (nel campo keyword della classe SearchClass)
     * @param tableName: stringa della forma: username + tableName. Entrambe le stringhe devono contenere
     * solo caratteri minuscoli. tableName deve essere al singolare (game, dungeon, item, boss,
     * monster, character).
     * @return 
     */
    @Transactional
    @PostMapping(path="removeFromFavorites")
    public @ResponseBody Messaggio removeFromFavorites(@RequestBody SearchClass searchClass, @RequestParam String tableName) {
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root");) {
            String elementName = searchClass.keyword;
            logger.info("Deleted = " + "DELETE FROM " + tableName + " WHERE name = '" + elementName + "'");
            co.createStatement().executeUpdate("DELETE FROM " + tableName + " WHERE name = '" + elementName + "'");
            return new Messaggio("OK", 200);
        }
        catch (SQLException e) { //lanciata in caso di connessione non riuscita.
            logger.error(e.getMessage());
            return new Messaggio("OK", 500);
        } 
    }
    
}
