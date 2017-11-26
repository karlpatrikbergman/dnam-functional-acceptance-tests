package com.infinera.metro.test.acceptance.foo;


import lombok.extern.slf4j.Slf4j;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("com.infinera.metro.test.acceptance.foo")
//@SelectClasses({Foo1Test.class, Foo2Test.class})
@Slf4j
public class FooTestSuite {


//Does not work
//    @BeforeAll
//    public static void beforeAll() {
//        log.info("Running before All");
//        System.out.println("Running before all");
//    }
//
//    @AfterAll
//    public static void afterAll() {
//        log.info("Running after All");
//        System.out.println("Running after all");
//    }

}
