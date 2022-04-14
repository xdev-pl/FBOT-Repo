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
@CfgComment("Discord for help: luxkret#7721 or luxdevpl@gmail.com")
@CfgComment("Welcome to the advanced module configuration file.")
@CfgComment(" ")

public class ExtendedConfiguration {

    @CfgComment("Ustawienia dotyczace funkcji aktualizatora wszystkie co odnosi sie do administracji, kanały itp")
    @CfgName("admin-updater-module-settings")
    public AdminChannelUpdaterModule adminUpdaterModuleSettings = new AdminChannelUpdaterModule();

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

    public static class AdminChannelUpdaterModule {

        //TODO podpiac booleany
        @CfgComment("Czy bot ma aktualizować status online/offline/afk w nazwie kanału administratora?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean channelNameStatusEnabled = true;

        @CfgComment("Czy bot ma aktualizować status administratora w >opisie< kanału administratora?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean channelDescStatusEnabled = true;

        @CfgComment("Czy kanal od aktualizacji statusu administracji ma byc aktualizowany?")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean adminStatusOnChannelStatus = false;

        @CfgComment("ID Kanalu na ktorym ma byc opis aktywnej administracji")
        public int staffStatusChannelId = 0;

        @CfgComment("Lista grup administracyjnych, podawaj wszystko według schematu poniżej.")
        @CfgComment("Musisz tu podać wszystkie id grup administratorow np. Mod,Helper,TestAdmin itp")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public List<Integer> administrativeGroupIds = ImmutableList.<Integer> builder()
                .add(1)
                .add(2)
                .add(3)
                .build();

        @CfgComment("Lista administratorow, podawaj ich UUID i ich ID kanału wedlug podanego schematu.")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public Map<String, Integer> administrativeUuids = ImmutableMap.<String, Integer>builder()
                .put("podaj_tu_uuid_admina_numer_1", 1)
                .put("podaj_tu_uuid_admina_numer_2", 2)
                .put("podaj_tu_uuid_admina_numer_3", 3)
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
        @CfgComment("Kanały te są aktualizowane gdy administrator zmieni swój status, np. wyjdzie z ts3, bedzie afk, lub wejdzie na ts")
        @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
        public Map<String, ArrayList<String>> adminChannelsDescription = ImmutableMap.<String, ArrayList<String>> builder()
                .put("uid_luxdevpl", new ArrayList<>(Arrays.asList(
                        "Administrator: luxdevpl", "Kontakt: kontakt", "Snapchat: snapchat", "forum: forum", " ", "[hr]", "[b]Ostatnio aktywny/a: %lastTimeActive")))
                .put("uid_jakisinnyadmin", new ArrayList<>(Arrays.asList(
                        "Administrator: luxdevpl", "Kontakt: kontakt", "Snapchat: snapchat", "forum: forum", " ", "[hr]", "[b]Ostatnio aktywny/a: %lastTimeActive")))
                .put("uid_trzeciadmin", new ArrayList<>(Arrays.asList(
                        "Administrator: luxdevpl", "Kontakt: kontakt", "Snapchat: snapchat", "forum: forum", " ", "[hr]", "[b]Ostatnio aktywny/a: %lastTimeActive")))
                .build();
    }

    public static class ChannelUpdaterModuleSettings {

        //todo add new booleans check
        @CfgComment("Tutaj ustawisz czy poszczegolne ustawienia maja byc wlaczone lub wylaczone")
        @CfgComment("W kazdym miejscu mozesz zostawic true albo false")
        @CfgComment("true - wlaczony | false - wylaczony")
        public ChannelUpdaterFunctionsStatus channelUpdaterFunctionsStatus = new ChannelUpdaterFunctionsStatus();

        @CfgComment("Tu ustawisz wszystkie nazwy kanałów, i zmienisz niektóre opisy.")
        public ChannelUpdaterModuleValues channelUpdaterModuleValues = new ChannelUpdaterModuleValues();

        @CfgComment("Tu ustawisz wszystkie id kanałów")
        public ChannelUpdaterValuesID channelUpdaterValuesID = new ChannelUpdaterValuesID();

        @CfgComment("Tu znajduje sie reszta ustawien")
        public ChannelUpdaterValuesOther channelUpdaterValuesOther = new ChannelUpdaterValuesOther();

        public static class ChannelUpdaterFunctionsStatus {

            @CfgComment("Czy bot ma aktualizować kanał z najnowszymi uzytkownikami na ts3?")
            public boolean newUsersEnabled = false;

            @CfgComment("Czy kanał z rekordem uzytkownikow ma sie zmieniac?")
            public boolean usersRecordEnabled = false;

            @CfgComment("Czy kanał ze zbanowanymi uzytkownikami ma sie zmieniac?")
            public boolean bannedUsersInfoEnabled = false;

            @CfgComment("Czy kanal z uzytkownikami z innego kraju sie zmieniac?")
            public boolean usersFromOtherCountryEnabled = false;

            @CfgComment("Czy kanał z aktualna godzina ma sie zmieniać?")
            public boolean timeChannelEnabled = false;
            @CfgComment("Czy nazwa serwera ma sie zmieniac?")
            public boolean virtualServerNameEnabled = false;

            @CfgComment("Czy nazwa kanalu z aktualna iloscia osob online ma sie zmieniac?")
            public boolean onlineChannelEnabled = false;

            @CfgComment("Czy kanal z unikalnymi uzytkownikami ma sie zmieniac?")
            public boolean uniqueClientAmountEnabled = false;

            @CfgComment("Czy kanal ze straconymi pakietami ma sie zmieniac?")
            public boolean lostPacketEnabled = false;

            @CfgComment("Czy kanał z iloscia kanałów na ts3 ma sie zmieniac?")
            public boolean channelsAmountEnabled = false;

            @CfgComment("Czy kanal z iloscia prywatnych kanałów ma sie zmieniac?")
            public boolean privateChannelsAmountEnabled = false;

            @CfgComment("Czy kanał z aktualnym pingiem ma sie zmieniac?")
            public boolean pingAmountEnabled = false;

            @CfgComment("Czy kanał z iloscia zarejestrowanych uzytkownikow ma sie zmieniac?")
            public boolean registeredClientsEnabled = false;


        }

        public static class ChannelUpdaterModuleValues {
            @CfgComment("Nazwa serwera ts3")
            public String virtualServerName = "Najlepszy serwer TeamSpeak3! [Aktualnie online: %online]";

            @CfgComment("Nazwa kanalu z aktualna godzina")
            public String timeChannelName = "Aktualna godzina: %hms";

            @CfgComment("Nazwa kanalu z iloscia zarejestrowanych osob")
            @CfgComment("Jest to liczba aktualnie zarejestrowanych osob na twoim ts3")
            public String registerClientsChannelName = "Zarejestrowanych osob: %registred";

            @CfgComment("Nazwa kanalu z aktualna iloscia online osob")
            public String onlineChannelName = "Aktualnie mamy %online osób online";

            @CfgComment("Nazwa kanalu z iloscia unikalnych osob")
            @CfgComment("Jest to liczba unikalnych osob ktore weszly na serwer od poczatku ts3")
            public String uniqueClientAmountChannelName = "Unikalnych osob: %unique";

            @CfgComment("Nazwa kanalu z aktualna iloscia % straconych pakietow")
            public String lostPacketsChannelName = "Stracone pakiety: %packets";

            @CfgComment("Nazwa kanalu z aktualna iloscia kanalow serwera")
            public String channelsAmountName = "Ilość Kanałów: %channels";

            //todo add
            @CfgComment("Nazwa kanalu z aktualna iloscia prywatnych kanalow serwera")
            public String privateChannelsAmountName = "Ilość prywatnych kanałów: %channels";

            @CfgComment("Nazwa kanalu z aktualna iloscia kanalow serwera")
            public String pingAmountChannelName = "Aktualny Ping: %ping";

            @CfgComment("Nazwa kanalu z aktualnym rekordem uzytkownikow")
            public String newRecordChannelName = "Rekord online: %record";

            @CfgComment("Opis kanalu z rekordem uzytkownikow")
            @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
            public List<String> newRecordChannelDescription = ImmutableList.<String>builder()
                    .add("[hr]")
                    .add("[size=14] Aktualny rekord uzytkownikow [/size]")
                    .add("[hr]")
                    .add("[b]Aktualny rekord uzytkownikow wynosi %record")
                    .add("[b]Zostal ustanowiony: %date")
                    .add("[hr]")
                    .build();

            @CfgComment("Opis kanalu z najnowszymi uzytkownikami")
            @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
            public List<String> newUsersChannelDescription = ImmutableList.<String>builder()
                    .add("[hr]")
                    .add("[size=14] 10 Najnowszych uzytkownikow na naszym Serwerze[/size]")
                    .add("[hr]")
                    .add("%newUsers")
                    .add("[hr]")
                    .build();

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

        public static class ChannelUpdaterValuesID {
            @CfgComment("ID kanalu z najnowszymi uzytkownikami")
            public int newUserChannelId = 0;

            @CfgComment("ID kanalu z godzina")
            public int timeChannelId = 0;

            @CfgComment("ID kanalu z lista banow")
            public int bannedUsersChannelId = 0;

            @CfgComment("ID kanalu z iloscia zarejestrowanych osob")
            public int registerdClientsChannelId = 0;

            @CfgComment("ID kanalu z iloscia unikalnych osob")
            public int uniqueClientAmountChannelId = 0;

            @CfgComment("ID kanalu z straconymi pakietami")
            public int lostPacketsChannelid = 0;

            @CfgComment("ID kanalu z aktualna iloscia osob")
            public int onlineChannelId = 0;

            @CfgComment("ID kanalu z aktualna iloscia kanalow serwera")
            public int channelsAmountId = 0;

            @CfgComment("ID kanalu z aktualna iloscia pingu serwera")
            public int pingAmountChannelId = 0;

            @CfgComment("ID kanalu z aktualnym rekordem uzytkownikow")
            public int newRecordChannelId = 0;

            @CfgComment("ID Kanalu z uzytkownikami z innego kraju")
            public int usersFromAnotherCountryChannelId = 0;
        }

        public static class ChannelUpdaterValuesOther {
            @CfgComment("Ile ma byc osob w liscie najnowszych uzytkownikow?")
            public int newUsersAmountIndex = 10;

            @CfgComment("Jaki kraj jest domyslnym krajem na ktorym przebywaja uzytkownicy?")
            @CfgComment("PL, DE, GB, FRA etc.")
            public String defaultCountry = "PL";
        }

    }

    public static class HelpCenterModule {

        //TODO SPRAWDZANIE BOOLEAN

        @CfgComment("Czy ogólnie całe centrum pomocy ma być aktywne?")
        @CfgComment("Automatyczna ranga - mezczyna/kobieta, oczekiwanie na administratora, prywatny kanał itd.")
        @CfgComment("true - wlaczony | false - wylaczony")
        public boolean helpCenterModuleStatus = true;

        @CfgComment("Poszczegolne ustawienia dot. nadawania rang.")
        public RankAssignerModule rankAssignerModule = new RankAssignerModule();

        @CfgComment("Poszczegolne ustawienia dot. pomocy uzytkownikowi")
        public AdminHelpSectorModule adminHelpSectorModule = new AdminHelpSectorModule();

        @CfgComment("Poszczegolne ustawienia dot. prywatnych kanałów")
        public PrivateChannelAssignerModule privateChannelAssignerModule = new PrivateChannelAssignerModule();

        public static class RankAssignerModule {
            @CfgComment("Czy sekcja od nadawania rang (mezczyzna, kobieta) ma byc aktywna?")
            @CfgComment("true - wlaczony | false - wylaczony")
            public boolean rankAssignerEnabled = true;

            @CfgComment("Czy ma byc wymagany spedzony czas aby nadac kobiete/mezczyzne?")
            @CfgComment("Zostaw na: 0 aby nie byl wymagany, czas podawaj w minutach, np. 10")
            public int timeSpentRequirement = 0;

            @CfgComment("Informacja gdy uzytkownik jest juz zarejestrowany")
            public String userAlreadyRegistered = "Jestes juz zarejestrowany/a.";

            @CfgComment("Podaj tu id grupy kobieta:")
            public int womanGroupId = 0;

            @CfgComment("Podaj tu id grupy mezczyzna:")
            public int manGroupId = 0;

            @CfgComment("ID Kanalu na ktorym na byc nadawana ranga kobieta:")
            public int womanGroupAssignerChannelId = 0;

            @CfgComment("ID Kanalu na ktorym ma byc nadawana ranga mezczyzna:")
            public int manGroupAssignerChannelId = 0;

        }

        public static class AdminHelpSectorModule {
            @CfgComment("Czy sekcja od pomocy uzytkownikowi przez administratora (czekam na admina) ma byc wlaczona?")
            @CfgComment("true - wlaczony | false - wylaczony")
            public boolean adminHelpSectorEnabled = true;

            @CfgComment("Czy ma byc wymagany spedzony czas aby pomóc uzytkownikowi?")
            @CfgComment("Zostaw na: 0 aby nie byl wymagany, czas podawaj w minutach, np. 10")
            public int timeSpentRequirement = 0;

            @CfgComment("Wiadomosc do administratorow gdy ktos na nich oczekuje")
            public String someoneNeedHelp = "Użytkownik %nickname oczekuje na twoją pomoc!";

            @CfgComment("Co ile sekund bot ma wysylac powiadomienia o pobycie uzytkownikow?")
            public int adminHelpSectorInterval = 10;

            @CfgComment("ID Kanalu na ktorym są uzytkownicy którzy oczekują pomocy administratora")
            public int helpCenterAdminChannelid = 0;

            @CfgComment("Podaj tu wszystkie id grup ktore maja byc powiadamiane o potrzebnej pomocy")
            public List<Integer> administratorGroupId = ImmutableList.<Integer> builder()
                    .add(1)
                    .add(2)
                    .build();
        }

        public static class PrivateChannelAssignerModule {
            @CfgComment("Czy sekcja od nadawania rang (mezczyzna, kobieta) ma byc aktywna?")
            @CfgComment("true - wlaczony | false - wylaczony")
            public boolean privateChannelAssignerEnabled = true;

            @CfgComment("Czy ma byc wymagany spedzony czas aby stworzyc uzytkownikowi prywatna strefe?")
            @CfgComment("Zostaw na: 0 aby nie byl wymagany, czas podawaj w minutach, np. 10")
            public int channelCreatorTimeRequirement = 0;

            @CfgComment("ID Kanalu na ktory ma wejsc uzytkownik aby dostac prywatna strefe.")
            public int channelCreatorChannelId = 0;

            @CfgComment("Jaka ma byc ilosc subkanalow stworzonych w prywatnej strefie uzytkownika?")
            public int channelsToBeCreatedAmount = 3;

            @CfgComment("ID kanalu od ktorego maja sie tworzyc prywatne kanaly uzytkownikow")
            @CfgComment("Od tego kanału beda wszystkie kanały prywatne, zazwyczaj jest to ostatni kanał na ts.")
            public int startWithPrivateChannelsId = 0;

            @CfgComment("ID grupy kanałowej ktora ma nadac bot po stworzeniu prywatnej strefy")
            public int channelAdminGroup = 0;

            @CfgComment("Jak ma sie rozpoczynac nazwa subkanalu?")
            @CfgComment("Zostaw spacje na koncu, po nazwie bedzie podany numer podkanalu.")
            public String subchannelStartWith = "Kanal ";

            @CfgComment("Opis nowo stworzonego prywatnego kanału")
            @CfgCollectionStyle(CfgCollectionStyle.CollectionStyle.ALWAYS_NEW_LINE)
            public List<String> newCreatedPrivateChannelDescription = ImmutableList.<String>builder()
                    .add("[hr]")
                    .add("[size=14] Prywatny kanał [/size]")
                    .add("[hr]")
                    .add("[center][b]Stworzony: %date")
                    .add("Właściciel: %owner")
                    .add("[hr]")
                    .build();

        }

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