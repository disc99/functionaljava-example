package example.validation;

import example.data.User;
import fj.data.NonEmptyList;
import fj.data.Option;
import fj.data.Validation;
import org.junit.Assert;
import org.junit.Test;

import static example.validation.UserValidations.nameOk;
import static fj.Semigroup.nonEmptyListSemigroup;
import static fj.Semigroup.stringSemigroup;
import static fj.data.Validation.fail;
import static fj.data.Validation.success;
import static example.validation.UserValidations.ageOk;
import static example.validation.UserValidations.emailOk;
import static org.hamcrest.CoreMatchers.is;

public class ValidationTest {

    @Test
    public void testStringSemigroup() throws Exception {
        // Setup
        User user = new User(5, null, "email_email.com");

        // Exercise
        Option<String> v = ageOk(user).accumulate(stringSemigroup, nameOk(user), emailOk(user));

        // Verify
        Assert.assertThat(v.toString(), is("Some(too young.no name.no email.)"));
    }

    @Test
    public void testNonEmptyListSemigroup() throws Exception {
        // Setup
        User user = new User(5, null, "email_email.com");

        // Exercise
        Option<NonEmptyList<String>> v = ageOk(user).nel().accumulate(nonEmptyListSemigroup(), nameOk(user).nel(), emailOk(user).nel());

        // Verify
        Assert.assertThat(v.toString(), is("Some(List(too young.,no name.,no email.))"));
    }

    @Test
    public void testNonEmptyListSemigroupValid() throws Exception {
        // Setup
        User user = new User(22, "Tom", "email@email.com");

        // Exercise
        Option<NonEmptyList<String>> v = ageOk(user).nel().accumulate(nonEmptyListSemigroup(), nameOk(user).nel(), emailOk(user).nel());

        // Verify
        Assert.assertThat(v.toString(), is("None"));
    }
}

class UserValidations {

    static Validation<String, String> ageOk(User u) {
        return u.age > 18 ? success("age ok.") : fail("too young.");
    }

    static Validation<String, String> nameOk(User u){
        return  u.name != null ? success("name ok.") : fail("no name.");
    }

    static Validation<String, String> emailOk(User u){
        return u.email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") ? success("email ok.") : fail("no email.");
    }
}