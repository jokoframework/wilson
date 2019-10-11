package io.github.jokoframework.wilson.constants;

/**
 * 
 * @author bsandoval
 *
 */
public class ApiPaths {
    public static final String API_PATTERN = "/api/.*";
    public static final String BASE = "/api";
    
    public static final String ROOT_DIAGNOSTIC = "/diagnostic";
    public static final String SUFFIX_HEART_BEAT = "/heartbeat";
    
    public static final String API_SECURE = "/api/secure";

    /**
     * routes for joko
     */
    public static final String API_SESSIONS = BASE + "/sessions";

    /**
     * routes for user's management
     */
    public static final String ROOT_USERS = API_SECURE + "/users";
    
    public static final String USERS_HEARTBEAT = ROOT_USERS + SUFFIX_HEART_BEAT;
    
    public static final String USERS_BY_NAME = ROOT_USERS + "/{username}";
    
    public static final String USERS_CSV = ROOT_USERS + "/csv";

    /**
     * routes for countries management
     */
    public static final String COUNTRIES = BASE + "/countries";

    /**
     * master route for Wilson's proxy functions
     */
    public static final String WILSON_MASTER = BASE + "/master";

    /**
     * routes for read operations
     */
    public static final String WILSON_INSERT_READ_OPERATION = BASE + "/insert/read-operation";

    public static final String WILSON_LIST_READ_OPERATION = BASE + "/list/read-operation";

    public static final String WILSON_UPDATE_READ_OPERATION_CACHE = BASE + "/update/cache";

    /**
     * routes for write operations
     */
    public static final String WILSON_INSERT_WRITE_OPERATION = BASE + "/insert/write-operation";

    public static final String WILSON_LIST_WRITE_OPERATION = BASE + "/list/write-operation";

    /**
     * routes for write cache
     */
    public static final String WILSON_LIST_WRITE_CACHE = BASE + "/list/write-cache";

    /**
     * routes for people management
     */
    public static final String ROOT_PERSON = API_SECURE + "/person";

    public static final String PERSON_HEARTBEAT = ROOT_PERSON + SUFFIX_HEART_BEAT;

    public static final String PERSON_BY_NAME = ROOT_PERSON + "/{name}";

    public static final String PERSON_CSV = ROOT_PERSON + "/csv";

    /**
     * route for scheduler management
     */
    public static final String TEST_GET_STORED_SECRET = BASE + "/test/get/secret";

    private ApiPaths() {
        
    }
}
