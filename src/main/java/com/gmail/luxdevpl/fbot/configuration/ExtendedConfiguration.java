package com.gmail.luxdevpl.fbot.configuration;

import com.google.common.collect.*;
import org.diorite.cfg.annotations.CfgClass;
import org.diorite.cfg.annotations.CfgCollectionStyle;
import org.diorite.cfg.annotations.CfgComment;
import org.diorite.cfg.annotations.CfgName;
import org.diorite.cfg.annotations.defaults.CfgDelegateDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@CfgClass(name = "ExtendedConfiguration")
@CfgDelegateDefault("{new}")

@CfgComment(" ")
@CfgComment(" ")
@CfgComment("█▀▀ █▀▀▄ █▀▀█ ▀▀█▀▀")
@CfgComment("█▀▀ █▀▀▄ █░░█ ░░█░░")
@CfgComment("▀░░ ▀▀▀░ ▀▀▀▀ ░░▀░░")
@CfgComment("https://github.com/xdev-pl")
@CfgComment(" ")
@CfgComment("For help see: https://tsforum.pl/forum/311-automatyzuj%C4%85cy-bot-fbot/")
@CfgComment("Welcome to the advanded module configuration file.")
@CfgComment(" ")

public class ExtendedConfiguration {

    @CfgComment("Ustawienia dotyczace klasy zarzadzajacej aktualizatorem kanalow/informacji administracyjnych")
    @CfgName("admin-updater-module-settings")
    public OtherModulesSettings adminUpdaterModuleSettings = new OtherModulesSettings();

    @CfgComment("Ustawienia dotyczace klasy zarzadzajacej aktualizatorem kanalow informacyjnych")
    @CfgName("channel-updater-module-settings")
    public ChannelUpdaterModuleSettings channelUpdaterModuleSettings = new ChannelUpdaterModuleSettings();

    @CfgComment("Ustawienia dotyczace klasy zarzadzajacej centrum pomocy")
    @CfgName("help-center-module-settings")
    public HelpCenterModule helpCenterModuleSettings = new HelpCenterModule();

    @CfgComment("Ustawienia dotyczace klasy zarzadzajacej aktualizatorem topek serwerowych")
    @CfgName("top-updater-module-settings")
    public TopUpdaterModuleSettings topUpdaterModuleSettings = new TopUpdaterModuleSettings();

    @CfgComment("Ustawienia dotyczace rzeczy które się wykonują po wejściu użytkownika na serwer.")
    @CfgName("client-join-server-settings")
    public ClientJoinServer clientJoinServer = new ClientJoinServer();

    @CfgComment("Ustawienia od generycznego modułu aktualizatora klienta")
    @CfgName("client-updater-module-settings")
    public GenericClientUpdaterModule genericClientUpdaterModule = new GenericClientUpdaterModule();

    @CfgComment("Ustawienia od modułu aktualizatora kanałow klanowych")
    @CfgName("clan-status-updater-module-settings")
    public ClanStatusUpdaterModuleSettings clanStatusUpdaterModuleSettings = new ClanStatusUpdaterModuleSettings();

    @CfgComment("Ustawienia od funkcji teleportu klanowego")
    @CfgName("clan-teleport-function-settings")
    public ClanTeleportFunctionSettings clanTeleportFunctionSettings = new ClanTeleportFunctionSettings();

    @CfgComment("Ustawienia do eventu ktory obsługuje edycje kanału")
    @CfgName("channel-edited-listener-settings")
    public ChannelEditedListenerSettings channelEditedListener = new ChannelEditedListenerSettings();

    @CfgComment("Ustawienia do eventu ktory obsługuje edycje uprawnien uzytkownika")
    @CfgName("client-permission-update-listener-settings")
    public ClientOwnPermissionUpdateListenerSettings clientOwnPermissionUpdateListener = new ClientOwnPermissionUpdateListenerSettings();

    @CfgComment("Ustawienia do eventu ktory obsługuje aktualizacje rangi uzytkownika")
    @CfgName("client-added-to-servergroup-listener-settings")
    public ClientAddedToServerGroupListenerSettings clientAddedToServerGroupListener = new ClientAddedToServerGroupListenerSettings();

    public static class OtherModulesSettings {

        @CfgComment("Lista grup administracyjnych, podawaj wszystko według schematu poniżej.")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<Integer> adminsGroupsList = ImmutableList.<Integer> builder()
                .add(1)
                .add(2)
                .add(3)
                .build();

        @CfgComment("Lista administratorow, podawaj ich UniqueID i ID kanału wedlug podanego schematu.")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public Map<String, Integer> staffUids = ImmutableMap.<String, Integer>builder()
                .put("jakies_uid_administratora_x", 1)
                .put("jakies_uid_administratora_y", 2)
                .put("jakies_uid_administratora_z", 3)
                .build();


        @CfgComment("Wartosci dla metody ktora ustawia na kanale admina czy jest online/afk/offline")
        @CfgComment("Jest to napis który będzie się wyświetlać na kanale administratora w niektórych przypadkach (np będzie away)")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<String> statusValues = ImmutableList.<String>builder()
                .add(" Niedostępny [✘]")
                .add(" Dostępny [✔]")
                .add(" Zaraz wracam [✈]")
                .build();

        @CfgComment("Opisy kanałów administracji")
        @CfgComment("Tutaj mozesz skonfigurowac opisy poszczegolnych kanalow administracji")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public Map<String, ArrayList<String>> adminChannelsDescription = ImmutableMap.<String, ArrayList<String>> builder()
                .put("uid_1", new ArrayList<>(Arrays.asList("Administrator: AMELKA", "Kontakt: xyz", "Snap: xyz", "costam: xyz", " ", "[hr]", "[b]Ostatnio aktywny/a: %lastTimeActive")))
                .put("uid_2", new ArrayList<>(Arrays.asList("Administrator: luxdevpl", "Kontakt: xyz", "Snap: xyz", "costam: xyz", " ", "[hr]", "[b]Ostatnio aktywny/a: %lastTimeActive")))
                .build();
    }

    public static class ChannelUpdaterModuleSettings {

        @CfgComment("Czy funkcja najnowszych uzytkownikow ma byc wlaczona?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean newUsersChannelStatus = false;

        @CfgComment("Czy funkcja rekordu uzytkownikow ma byc wlaczona?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean usersRecordChannelStatus = false;

        @CfgComment("Czy funkcja zbanowanych uzytkownikow ma byc wlaczona?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean bannedUsersInfoStatus = false;

        @CfgComment("Czy kanal od aktualizacji statusu administracji ma byc aktualizowany?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean adminStatusOnChannelStatus = false;

        @CfgComment("Czy kanal z uzytkownikami z innego kraju ma byc wlaczony?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean usersFromOtherCountryChannelStatus = false;

        @CfgComment("Czy bot ma aktualizowac kanaly administratorow? (status)")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean administrativeChannelsStatus = false;

        @CfgComment("Nazwa serwera ts3")
        public String virtualServerName = "Serwer TeamSpeak 3 [ONLINE: %online]";

        @CfgComment("ID Kanalu na ktorym ma byc opis aktywnej administracji")
        public int staffStatusChannelId = 0;

        @CfgComment("ID kanalu z najnowszymi uzytkownikami")
        public int newUserChannelId = 0;

        @CfgComment("Nazwa kanalu z aktualna godzina")
        public String timeChannelName = "Godzina: %hms";

        @CfgComment("ID kanalu z godzina")
        public int timeChannelId = 0;

        @CfgComment("ID kanalu z lista banow")
        public int bannedUsersChannelId = 0;

        @CfgComment("ID kanalu z iloscia zarejestrowanych osob")
        public int registerdClientsChannelId = 0;

        @CfgComment("Nazwa kanalu z iloscia zarejestrowanych osob")
        public String registerClientsChannelName = "Zarejestrowanych osob: %registred";

        @CfgComment("ID kanalu z iloscia unikalnych osob")
        public int uniqueClientAmountChannelId = 0;

        @CfgComment("Nazwa kanalu z iloscia unikalnych osob")
        public String uniqueClientAmountChannelName = "Unikalnych osob: %unique";

        @CfgComment("Nazwa kanalu z aktualna iloscia % straconych pakietow")
        public String lostPacketsChannelName = "Stracone pakiety: %packets";

        @CfgComment("ID kanalu z straconymi pakietami")
        public int lostPacketsChannelid = 0;

        @CfgComment("Nazwa kanalu z aktualna iloscia online osob")
        public String onlineChannelName = "Online: %online";

        @CfgComment("ID kanalu z aktualna iloscia osob")
        public int onlineChannelId = 0;

        @CfgComment("Nazwa kanalu z aktualna iloscia kanalow serwera")
        public String channelsAmountName = "Ilość Kanałów: %channels";

        @CfgComment("ID kanalu z aktualna iloscia kanalow serwera")
        public int channelsAmountId = 0;

        @CfgComment("Nazwa kanalu z aktualna iloscia kanalow serwera")
        public String pingAmountChannelName = "Ping: %ping";

        @CfgComment("ID kanalu z aktualna iloscia pingu serwera")
        public int pingAmountChannelId = 0;

        @CfgComment("ID kanalu z aktualnym rekordem uzytkownikow")
        public int newRecordChannelId = 0;

        @CfgComment("Nazwa kanalu z aktualnym rekordem uzytkownikow")
        public String newRecordChannelName = "Rekord uzytkownikow: %record";

        @CfgComment("ID Kanalu z uzytkownikami z innego kraju")
        public int usersFromAnotherCountryChannelId = 0;

        @CfgComment("Opis kanalu z rekordem uzytkownikow")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<String> newRecordChannelDescription = ImmutableList.<String>builder()
                .add("[hr]")
                .add("[size=14] Aktualny rekord uzytkownikow [/size]")
                .add("[hr]")
                .add("Aktualny rekord uzytkownikow wynosi %record")
                .add("Zostal ustanowiony: %date")
                .add("[hr]")
                .build();

        @CfgComment("Ile ma byc osob w liscie najnowszych uzytkownikow?")
        public int newUsersAmountIndex = 10;

        @CfgComment("Opis kanalu z najnowszymi uzytkownikami")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<String> newUsersChannelDescription = ImmutableList.<String>builder()
                .add("[hr]")
                .add("[size=14] 10 Najnowszych uzytkownikow na naszym Serwerze[/size]")
                .add("[hr]")
                .add("%newUsers")
                .add("[hr]")
                .build();

        @CfgComment("Jaki kraj jest domyslnym krajem na ktorym przebywaja uzytkownicy?")
        public String defaultCountry = "PL";

        @CfgComment("Opis kanalu z osobami z zagranicy")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<String> usersFromOtherCountryChannelDescription = ImmutableList.<String>builder()
                .add("[hr]")
                .add("[size=14] Osoby z innego kraju [/size]")
                .add("[hr]")
                .add("%users")
                .add("[hr]")
                .build();

    }

    public static class HelpCenterModule {

        @CfgComment("Czy sprawdzanie wydarzen na centrum pomocy ma byc wlaczone?")
        @CfgComment("Automatyczna ranga - mezczyna/kobieta, oczekiwanie na administratora itd.")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean helpCenterModuleStatus = true;

        @CfgComment("Co ile czasu bot ma sprawdzac kanal do pomocy uzytkownikom")
        public int helpCenterModuleInterval = 10;

        @CfgComment("ID Grupy kobieta")
        public int womanGroupId = 0;

        @CfgComment("ID Grupy mezczyna")
        public int manGroupId = 0;

        @CfgComment("ID Kanalu na ktorym na byc nadawana ranga kobieta")
        public int womanGroupAssignerChannelId = 0;

        @CfgComment("ID Kanalu na ktorym ma byc nadawana ranga mezczyzna")
        public int manGroupAssignerChannelId = 0;

        @CfgComment("ID Grup typu Administrator")
        @CfgComment("Wszyscy z grup typu Administrator będą dostawać powiadomienia z centrum pomocy.")
        public List<Integer> administratorGroupId = ImmutableList.<Integer> builder()
                .add(1)
                .add(2)
                .build();

        @CfgComment("Czy ma byc wymagany spedzony czas aby stworzyc uzytkownikowi prywatna strefe?")
        @CfgComment("Zostaw na: 0 aby nie byl wymagany, czas podawaj w minutach, np. 10")
        public int channelCreatorTimeRequirement = 0;

        @CfgComment("ID Kanalu na ktory ma wejsc uzytkownik aby dostac prywatna strefe.")
        public int channelCreatorChannelId = 0;

        @CfgComment("Jaka ma byc ilosc subkanalow stworzonych w prywatnej strefie uzytkownika?")
        public int channelsToBeCreatedAmount = 3;

        @CfgComment("ID Kanalu na ktorym są uzytkownicy którzy oczekują pomocy administratora")
        public int helpCenterAdminChannelid = 0;

        @CfgComment("ID kanalu od ktorego maja sie tworzyc prywatne kanaly uzytkownikow")
        public int startWithPrivateChannelsId = 0;

        @CfgComment("ID grupy ktora ma nadac bot po stworzeniu prywatnej strefy")
        public int channelAdminGroup = 0;

        @CfgComment("Czy ma byc wymagany spedzony czas aby nadac kobiete/mezczyzne?")
        @CfgComment("Zostaw na: 0 aby nie byl wymagany, czas podawaj w minutach, np. 10")
        public int timeSpentRequirement = 0;

        @CfgComment("Wiadomosc do administratorow gdy ktos na nich oczekuje")
        public String someoneNeedHelp = "Użytkownik %nickname oczekuje na twoją pomoc!";

        @CfgComment("Informacja gdy uzytkownik jest juz zarejestrowany")
        public String userAlreadyRegistered = "Jestes juz zarejestrowany/a.";

        @CfgComment("Jak ma sie rozpoczynac nazwa subkanalu?")
        @CfgComment("Zostaw spacje na koncu, po nazwie bedzie podany numer.")
        public String subchannelStartWith = "Kanal ";

        @CfgComment("Opis nowo stworzonego prywatnego kanału")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<String> newCreatedPrivateChannelDescription = ImmutableList.<String>builder()
                .add("[hr]")
                .add("[size=14] Prywatny kanał [/size]")
                .add("[hr]")
                .add("Stworzony: %date")
                .add("Właściciel: %owner")
                .add("[hr]")
                .build();

    }

    public static class TopUpdaterModuleSettings {

        @CfgComment("Czy wykonywanie funkcji topek ma byc wlaczone?")
        @CfgComment("Top polaczen, spedzonego czasu, ilosci polaczen.")
        @CfgComment("true - wlaczony | false - wylaczony")
        @CfgName("top-updater-module-status")
        public boolean topUpdaterModuleStatus = true;

        @CfgComment("Ile ma byc wynikow w topkach?")
        @CfgComment("Standardowo: 10")
        public int topUpdaterModuleTopIndexAmount = 10;

        @CfgComment("Co ile czasu maja sie aktualizowac topki?")
        @CfgComment("Podawaj w sekundach.")
        public int topUpdaterModuleInterval = 10;

        @CfgComment("ID Kanalu do Top Ilości połączeń")
        public int topUpdaterModuleTopConnectionsChannelid = 0;

        @CfgComment("ID Kanalu do Top Najdłuższych połączeń")
        public int topUpdaterModuleTopLongestConnectionsChannelid = 0;

        @CfgComment("ID Kanalu do Top spedzonego czasu na serwerze.")
        public int getTopUpdaterModuleTopTimeSpentChannelid = 0;

    }

    public static class ClientJoinServer {

        @CfgComment("Ustawienia dotyczące wiadomości powitalnej.")
        public WelcomeMessageSettings welcomeMessage = new WelcomeMessageSettings();

        @CfgComment("Ustawienia dotyczace sprawdzania nicku uzytkownika.")
        public BadNicknamesChecker badNicknames = new BadNicknamesChecker();

        public static class WelcomeMessageSettings {

            @CfgComment("Czy ma byc wysylana wiadomosc powitalna?")
            @CfgComment("true - wlaczony | false - wylaczony")
            @CfgComment("welcome-message-status")
            public boolean status = true;

            @CfgComment("Wiadomosc powitalna po wejsciu na serwer")
            @CfgComment("Wszystkie dostępne zmienne są opisane poniżej")
            @CfgComment(" ")
            @CfgComment("%online - Aktualna ilość uzytkownikow online.")
            @CfgComment("%bans - Ilość banów.")
            @CfgComment("%channels - Ilość kanałów.")
            @CfgComment("%totalConnections - Suma wszystkich połączeń klienta z serwerem.")
            @CfgComment("%firstConnection - Data pierwszego połączenia się klienta z serwerem.")
            @CfgComment("%lastConnection - Data ostatniego połączenia się klienta z serwerem.")
            @CfgComment("%clientIp - Adres IP klienta.")
            @CfgComment("%clientCountry - Kraj klienta w formacie 2-Znakowego ciągu (Country Code)")
            @CfgComment("%clientName - Pseudonim klienta.")
            @CfgComment("%clientUid - Unikalne ID klienta.")
            @CfgComment(" ")
            @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
            public List<String> joinMessage = ImmutableList.<String>builder()
                    .add("Witaj [b]%clientName[/b] na naszym serwerze!")
                    .add(" ")
                    .add("Masz juz w sumie [b]%totalConnections[/b] połączeń z naszym serwerem.")
                    .add("Twoje unikalne ID to [b]%clientUid[/b]")
                    .add("Twoje pierwsze polaczenie nastąpiło [b]%firstConnection[/b].")
                    .add("Aktualnie na serwerze jest [b]%online[/b] uzytkownikow.")
                    .add(" ")
                    .add("[b][color=green] Życzymy milych oraz udanych rozmów [/b]")
                    .add(" ")
                    .build();

        }

        public static class BadNicknamesChecker {

            @CfgComment("Pseudonimy ktore maja byc blokowane.")
            @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
            public List<String> nicknames = ImmutableList.<String>builder()
                    .add("kurwa")
                    .add("admin")
                    .add("serveradmin")
                    .add("chuj")
                    .add("skurwiel")
                    .build();

            @CfgComment("Powod wyrzucenia podczas gdy bot wykryje zly nick uzytkownika")
            public String kickReason = "Kultury czlowieku z tym nickiem co?";

        }

//        @CfgComment("Czy bot ma blokowac VPN?")
//        @CfgName("client-join-vpn-kick-status")
//        public boolean clientJoinVpnKickStatus = true;
//
//        @CfgComment("Powod wyrzucenia podczas gdy bot wykryje ze uzytkownik korzysta z VPN")
//        @CfgName("client-join-vpn-kickmessage")
//        public String clientJoinVpnKickMessage = "Tutaj nie tolerujemy VPN :)";

    }

    public static class ClanStatusUpdaterModuleSettings{

        @CfgComment("Czy aktualizator klanow ma byc wlaczony?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean clanStatusUpdaterStatus = false;

        @CfgComment("Co ile czasu maja sie aktualizowac informacje o klanach?")
        @CfgComment("Podawaj w sekundach.")
        @CfgName("clan-status-updater-module-interval")
        public int clanStatusUpdaterModuleInterval = 55;

        @CfgComment("Wyglad kanalu w ktorym ma byc wypisywana informacja w nazwie.")
        @CfgName("clan-status-updater-channelname")
        public String clanStatusUpdaterChannelName = "%clanName - %active osób.";

        @CfgComment("ID Kanalow na ktorych ma byc wpisywana ilosc aktywnych osob oraz ID grupy ktora ma byc sprawdzana")
        @CfgComment("Pierwsza liczba jest ID kanału, drugą ID grupy.")
        @CfgName("clan-status-updater-channels-and-groupsid")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public Map<Integer, Integer> clanStatusUpdaterModuleChannelIdToGroupId = ImmutableMap.<Integer, Integer>builder()
                .put(1, 2)
                .put(2, 3)
                .build();
    }

    public static class GenericClientUpdaterModule {

        @CfgComment("Czy poziomy uzytkownikow maja byc wlaczone?")
        @CfgComment("Jesli nie masz zrobionych pod to rang wylacz funkcje")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean levelingUsersStatus = false;

        @CfgComment("Czy uzytkownik ma dostac range po spędzonego wymaganego czasu na serwerze?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean timeRankUpdateStatus = false;

        @CfgComment("Tutaj ustalasz od jakiej ilości czasu ma mieć użytkownik wyższy poziom")
        @CfgComment("Przyklad: Uzytkownik X ma naliczone 15 minut od pierwszego wejścia i dostaje rangę o ID 1")
        @CfgComment("Ważne: Czas jest w minutach więc musisz pomnożyć na godziny. Możesz dodać ile chcesz wpisów.")
        @CfgName("level-time-group-id")
        public Map<Integer, Integer> levelingTimeGroupsId = ImmutableMap.<Integer, Integer>builder()
                .put(15, 1)
                .put(30, 2)
                .put(60, 3)
                .put(120, 4)
                .put(180, 5)
                .build();

        @CfgComment("Po jakim czasie uzytkownik ma dostać range za spędzony czas?")
        @CfgComment("Czas podawaj w minutach.")
        public int timeRankUpdateRequirement = 60;

        @CfgComment("ID Rangi ktora ma dostac uzytkownik po spedzeniu wymaganego czasu")
        public int timeRankUpdateGroupId = 0;
    }

    public static class ClanTeleportFunctionSettings {

        @CfgComment("Czy funkcja ma byc wlaczona?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean clanTeleportFunctionStatus = false;

        @CfgComment("Nazwy klanów oraz ich kanał do teleportacji")
        @CfgName("clan-teleport-function-clan-map")
        public Map<String, Integer> clansMap = ImmutableMap.<String, Integer>builder()
                .put("PLAY", 1)
                .put("PL4Y", 2)
                .build();

    }

    public static class ChannelEditedListenerSettings {

        @CfgComment("Czy funkcja ma byc wlaczona?")
        @CfgComment("true - wlaczony | false - wylaczony")
        @CfgName("channel-edited-listener-status")
        public boolean channelEditedListenerStatus = false;

        @CfgComment("Lista rzeczy które maja być usuwane przy edycji kanału przez użytkownika")
        @CfgName("channel-edited-listener-filter")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<String> channelDescriptionFilter = ImmutableList.<String>builder()
                .add("facebook.com")
                .add("adf.ly")
                .build();

    }

    public static class ClientOwnPermissionUpdateListenerSettings {

        @CfgComment("Czy bot ma blokować nadania uprawnień na klienta?")
        @CfgComment("true - wlaczony | false - wylaczony")
        @CfgName("client-own-permission-update-listener-status")
        public boolean clientOwnPermissionUpdateListenerStatus = false;
    }

    public static class ClientAddedToServerGroupListenerSettings {

        @CfgComment("Czy funkcja ma byc wlaczona?")
        @CfgComment("true - wlaczony | false - wylaczony")
        @CfgName("client-added-to-servergroup-listener-status")
        public boolean listenerStatus = false;

        @CfgComment("Lista chronionych grup")
        @CfgName("secured-groups-list")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<Integer> securedGroups = ImmutableList.<Integer> builder()
                .add(3)
                .build();

        @CfgComment("Chronione grupy, do kazdego administratora jest przypisana jakas ilosc grup chronionych.")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public Map<String, ArrayList<Integer>> securedGroupsUids = ImmutableMap.<String, ArrayList<Integer>> builder()
                .put("uid_1", new ArrayList<>(Arrays.asList(1, 2)))
                .put("uid_2", new ArrayList<>(Arrays.asList(1, 2)))
                .build();

    }

}