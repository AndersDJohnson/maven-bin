package me.andrz.maven.bin.env

/**
 *
 */
class EnvUtils {

    static Map<String, String> env
    static Map<String, String> envLookup

    /**
     * Case insensitive env lookup.
     *
     * @return
     */
    public static Map<String,String> getenv() {
        env = System.getenv()
        envLookup = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER)
        envLookup.putAll(env)
        return envLookup
    }

}
