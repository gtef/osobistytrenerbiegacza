package com.example.jakub.osobistytrenerbiegacza.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jakub on 2015-10-22.
 */
public class User implements Serializable {
    List<Advice> advices;
    List<Training> history;
    Plan plan;

    public User() {
        history = new ArrayList<>();
        plan = new Plan(0, 0, 0, false, new LinkedList<Training>(), new Date());
        advices= getInitialAdvices();
    }

    public User(List<Advice> advices, List<Training> history, Plan plan) {
        this.advices = advices;
        this.history = history;
        this.plan = plan;
    }

    public List<Advice> getAdvices() {
        return advices;
    }

    public void setAdvices(List<Advice> advices) {
        this.advices = advices;
    }

    public List<Training> getHistory() {
        return history;
    }

    public void setHistory(List<Training> history) {
        this.history = history;
    }

    public void addToHistory(Training training) {
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(training);
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }



    private List<Advice> getInitialAdvices() {
        Advice a1 = new Advice(1, 1, "Jedz dużo makaronu to łatwiej ci się będzie żyło.");
        Advice a2 = new Advice(2, 1, "Rzeczy, które sam ugotujesz są zdrowsze od tych zrobionych w fast foodach.");
        Advice a3 = new Advice(3, 2, "Buty-skarpetki nie są są lepsze od zwykłych butów. Łatwiej dostaniesz kontuzji.");
        return Arrays.asList(a1, a2, a3);
    }

    @Override
    public String toString() {
        return "User{" +
                "advices=" + advices +
                ", history=" + history +
                ", plan=" + plan +
                '}';
    }
}