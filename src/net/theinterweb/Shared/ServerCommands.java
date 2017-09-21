package net.theinterweb.Shared;

public class ServerCommands {
    // Login Commands
    public static final String LOGIN_PREFIX                 = "LOGIN U:////3////";
    public static final String LOGIN_PASSWORD_PREFIX        = "P:////3////";
    public static final String LOGIN_VALID_PASSWORD         = "VALIDPASSWORD";
    public static final String LOGIN_INVALID_PASSWORD       = "INVALIDPASSWORD";
    public static final String LOGIN_USERNAME_PREFIX        = "U:";
    public static final String LOGIN_RANK_PREFIX            = "://://://R";

    // Sign Up Commands
    public static final String SIGN_UP_PREFIX               = "SIGNUP ";
    public static final String SIGN_UP_USERNAME_PREFIX      = "U:////3////";
    public static final String SIGN_UP_PASSWORD_PREFIX      = "P:////3////";
    public static final String SIGN_UP_SUCCESS              = "SIGNUPSUCCESS";
    public static final String SIGN_UP_FAIL                 = "SIGNUPFAIL";

    // Start Page Commands
    public static final String START_PAGE_JOIN_LINE         = "JOINLINE";
    public static final String START_PAGE_WAITING           = "WAITINGFORPLAYERS";
    public static final String START_PAGE_LOGOUT            = "LOGOUT";

    // Make Deck Commands
    public static final String MAKE_DECK_GET_DECK           = "GETDECK";
    public static final String MAKE_DECK_DECK_FINISHED      = "DONEWITHDECKCARDS";
    public static final String MAKE_DECK_COLLECTION         = "GETCOLLECTION";
    public static final String MAKE_DECK_COLLECTION_DONE    = "DONEWITHCOLLECTIONCARDS";
    public static final String MAKE_DECK_SAVE               = "SAVEDECKSTART";
    public static final String MAKE_DECK_SAVE_DONE          = "SAVEDECKEND";

    // Open Pack Commands
    public static final String OPEN_PACK_GET_INFO           = "GETPACKINFO";
    public static final String OPEN_PACK_PACK_PREFIX        = "PACKS:";
    public static final String OPEN_PACK_GOLD_PREFIX        = "GOLD:";
    public static final String OPEN_PACK_BUY_PACK           = "BUYPACK";
    public static final String OPEN_PACK_NO_GOLD            = "BUYPACKNOGOLD";
    public static final String OPEN_PACK_BUY_SUCCESS        = "BUYPACKSUCCESS";
    public static final String OPEN_PACK_OPEN               = "OPENAPACK";
    public static final String OPEN_PACK_OPEN_DONE          = "PACKFINISHED";
    public static final String OPEN_PACK_NO_PACKS           = "NOPACKSAVAILABLE";

    // Collection Commands
    public static final String COLLECTION_GET_WHOLE         = "GETWHOLECOLLECTION";
    public static final String COLLECTION_GET_WHOLE_DONE    = "DONEWITHWHOLECOLLECTION";
    public static final String COLLECTION_GET_DUST          = "GETDUST";
    public static final String COLLECTION_DUST_PREFIX       = "DUST:";
    public static final String COLLECTION_DUST_NOT_HAVE     = "DUSTHAVEZERO";
    public static final String COLLECTION_DUST_BASIC_ERR    = "DUSTFAILBASIC";
    public static final String COLLECTION_DUST_NON_SELC     = "DUSTNOCARDSELECTED";
    public static final String COLLECTION_DUST_SUCCESS      = "DUSTSUCCESS";
    public static final String COLLECTION_CRAFT_PREFIX      = "CRAFT:";
    public static final String COLLECTION_CRAFT_BASIC_ERR   = "CRAFTFAILBASIC";
    public static final String COLLECTION_CRAFT_FAIL_DUST   = "CRAFTFAILNOTENOUGHDUST";
    public static final String COLLECTION_CRAFT_NON_SELEC   = "CRAFTNOCARDSELECTED";
    public static final String COLLECTION_CRAFT_SUCCESS     = "CRAFTSUCCESS";

    // Gameplay Commands
    public static final String GAME_READY_TO_START          = "READY2START";
    public static final String GAME_ENABLE_CONTROLS         = "ENABLECONTROLS";
    public static final String GAME_DISABLE_CONTROLS        = "DISABLECONTROLS";
    public static final String GAME_BASIC_INFO_PREFIX       = "BASICINFO:";
    public static final String GAME_FRIEND_BOARD_PREFIX     = "FBOARD:";
    public static final String GAME_ENEMY_BOARD_PREFIX      = "EBOARD:";
    public static final String GAME_RESULT_PREFIX           = "RESULT:";
    public static final String GAME_RESULT_WIN              = "WIN";
    public static final String GAME_RESULT_LOSE             = "LOSE";
    public static final String GAME_RESULT_TIE              = "TIE";
    public static final String GAME_END_TURN                = "END";
    public static final String GAME_ERROR_PREFIX            = "ERROR:";
    public static final String GAME_ERROR_MANA_LOW          = "MANALOW";
    public static final String GAME_ERROR_FATIGUE           = "FATIGUE";
    public static final String GAME_ERROR_BURNED            = "BURNED";
    public static final String GAME_ERROR_TAUNT             = "TAUNT";
    public static final String GAME_PLAY_PREFIX             = "PLAY:";
    public static final String GAME_ATTACK_PREFIX           = "ATTACK:";
    public static final String GAME_GET_TARGET_PREFIX       = "GETTARGET:";
}