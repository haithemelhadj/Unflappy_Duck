package tn.esprit.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CLIFreelance {

    public static void main(String[] args) throws ParseException {
        List<Integer> i = new ArrayList<>();
        i.add(2);
        System.out.println(i.get(0).intValue());
    }
}
