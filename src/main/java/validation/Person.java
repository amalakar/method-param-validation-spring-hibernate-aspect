package validation;

import javax.validation.constraints.NotNull;

/**
 * Author: Arup Malakar
 * License: apache v2
 */
@AutoValidating
public class Person {

    private String name;

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
