import validation.Person;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.FileAssert;
import org.testng.annotations.Test;
import javax.inject.Inject;


/**
 * Author: Arup Malakar
 * License: apache v2
 */

@Test
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestValidation extends AbstractTestNGSpringContextTests {

    // Spring based aspect works only with objects created by spring
    @Inject
    Person person;

    public void testNullParamValidation() {
        try {
            person.setName(null);
            FileAssert.fail("Was expecting an exception");
        } catch (Exception e) {
            System.out.println("Validation in work: " + e);
        }
    }
}


