package edu.lehigh.cse216_luna.phase1.android.ajv218.phase1_android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kjh219 on 3/31/17.
 */

public class DummyCode {
    public DummyCode(){

    }

    public List<String> insertDummyCode(){
        // Initialize a new String array
        final String[] animals = {
                "A",
                "Screaming",
                "Comes",
                "Across",
                "The",
                "Sky"
        };

        // Initialize an array list from array
        final List<String> animalsList = new ArrayList(Arrays.asList(animals));
        return animalsList;
    }
}
