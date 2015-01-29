package fr.vidal.oss.testing.tomcat8;

import java.util.Objects;

import static java.lang.String.format;
import static java.util.Objects.hash;

public class WebAppParameter {

   private String name;
   private String value;

   private WebAppParameter(String name, String value) {
      this.name = name;
      this.value = value;
   }

   public static WebAppParameter parameter(String key, String value) {
      return new WebAppParameter(key, value);
   }

   public String name() {
      return name;
   }

   public String value() {
      return value;
   }

   @Override
   public int hashCode() {
      return hash(name, value);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }
      final WebAppParameter other = (WebAppParameter) obj;
      return Objects.equals(this.name, other.name) && Objects.equals(this.value, other.value);
   }

   @Override
   public String toString() {
      return format("%s=%s", name, value);
   }
}
