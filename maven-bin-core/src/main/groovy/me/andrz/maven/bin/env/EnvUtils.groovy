package me.andrz.maven.bin.env

/**
 *
 */
class EnvUtils {


    static Map<String, String> envLookup

    static {
        Map<String, String> env = System.getenv()
        envLookup = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER)
        envLookup.putAll(env)
    }

    /**
     * Case insensitive env lookup.
     *
     * @return
     */
    public static Map<String,String> getenv() {
        return envLookup
    }

    /**
     * Case insensitive set for overrides.
     *
     * @param name
     * @param value
     */
    public static void setenv(String name, String value) {
        envLookup.put(name, value)
    }

    public static String[] toEnvStrings(def env) {
        return env.collect { k, v -> "$k=$v" }
    }

}
