package fr.vidal.oss.testing.tomcat8;

class Preconditions {
    public static void checkState(boolean condition) {
        if (!condition) {
            throw new IllegalStateException();
        }
    }
}
