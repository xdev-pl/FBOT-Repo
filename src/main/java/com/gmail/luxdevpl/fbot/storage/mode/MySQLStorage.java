package com.gmail.luxdevpl.fbot.storage.mode;

import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.basic.TS3Server;
import com.gmail.luxdevpl.fbot.basic.enums.TopTypes;
import com.gmail.luxdevpl.fbot.storage.util.IDatabase;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MySQLStorage extends IDatabase {

    /**
     * this method loads current informations about the client from database
     * @param client object of client
     */
    void loadClientInfo(IClient client);

    /**
     * this method is saving current informations about the client to the database.
     * @param client object of client
     */
    void saveClientInfo(IClient client);

    void saveServerInfo(TS3Server server);

    void loadServerInfo(TS3Server server);

    void saveBestFriends(Map<String, Long> friendsMap);

    /**
     * this method is getting top x from database for specified TopTypes
     * @param topTypes
     * @param indexAmount
     * @return entries.
     */
    Map<String, Long> getTopValues(TopTypes topTypes, int indexAmount);

    /**
     * this method is getting x newest users on the server.
     * @param indexAmount
     * @return newest users (as nickname)
     */
    List<String> getNewUsers(int indexAmount);

    Map<String, Long> getBestFriends(int databaseClientId);

    /**
     * this method is getting user's nickname from the database
     * @param databaseId key to search
     * @return nickname(Optional)
     */
    Optional<String> getNicknameByDatabaseId(int databaseId);
//
//    /**
//     *
//     * @param databaseId
//     * @return
//     */
//    Optional<String> getLastConnectedMillis(int databaseId);



}
