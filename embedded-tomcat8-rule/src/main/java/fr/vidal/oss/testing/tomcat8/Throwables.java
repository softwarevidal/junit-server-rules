package fr.vidal.oss.testing.tomcat8;

class Throwables {
    public static RuntimeException propagate(Exception exception) {
        throw new RuntimeException(exception.getMessage(), exception);
    }
}
