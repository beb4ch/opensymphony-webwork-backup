package com.opensymphony.webwork.showcase.person;

import java.util.ArrayList;
import java.util.List;

/**
 * User: plightbo
 * Date: Sep 20, 2005
 * Time: 6:27:07 PM
 */
public class PersonManager {
    private List people = new ArrayList();
    private static long COUNT = 0;

    public void createPerson(Person person) {
        person.setId(new Long(++COUNT));
        people.add(person);
    }

    public List getPeople() {
        return people;
    }
}
