package reflection;

import org.junit.jupiter.api.Test;

class Junit4TestRunner {

  @Test
  void run() throws Exception {
    Class<Junit4Test> clazz = Junit4Test.class;

    for (var method : clazz.getDeclaredMethods()) {
      if (method.isAnnotationPresent(MyTest.class)) {
        method.invoke(clazz.getDeclaredConstructor().newInstance());
      }
    }
  }
}
