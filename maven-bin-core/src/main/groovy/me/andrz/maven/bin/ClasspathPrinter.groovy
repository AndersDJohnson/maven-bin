package me.andrz.maven.bin

/**
 *
 */
class ClasspathPrinter {

    public static void main(String[] args) {
        // Get the system class loader
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader()

        // Get the URLs
        URL[] urls = ((URLClassLoader)sysClassLoader).getURLs()

        String classpath = ''
        for (URL url : urls) {
            String file = url.getFile()
            classpath += getPathSeparator() + file
        }

        System.out.println("classpath = ${classpath}")
    }

    public static String getPathSeparator() {
        return System.getProperty('path.separator')
    }
}
