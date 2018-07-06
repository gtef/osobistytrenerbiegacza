package com.example.jakub.osobistytrenerbiegacza;

import com.example.jakub.osobistytrenerbiegacza.model.Advice;
import com.example.jakub.osobistytrenerbiegacza.model.Plan;
import com.example.jakub.osobistytrenerbiegacza.model.Training;
import com.example.jakub.osobistytrenerbiegacza.model.TrainingType;
import com.example.jakub.osobistytrenerbiegacza.model.User;
import com.example.jakub.osobistytrenerbiegacza.utils.PlanGeneration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Jakub on 2015-10-24.
 */
public class Singleton implements Serializable{
    private static final long serialVersionUID =1L;

    private final static Singleton singleton = new Singleton();
    public static Singleton getInstance() {
        return singleton;
    }

    private User user;
    private int staminaLevel;
    private int age;

    private int sex;//1 - M, 2 - K

    private Singleton() {
        user = new User();
//        user.setAdvices(getInitialAdvices());
//        TrainingType tt01 = new TrainingType(1, -1, 160, -1, 1, -1, "Lekki bieg 160s.");
//        TrainingType tt02 = new TrainingType(1, -1, 8, -1, 1, -1, "Lekki bieg 8s.");
//        TrainingType tt03 = new TrainingType(1, -1, 10, -1, 1, -1, "Lekki bieg 10s.");
//        TrainingType tt04 = new TrainingType(2, -1, 4, -1, 3, 5, "Z.B. 3x 4s, przerwa 5s");
//        TrainingType tt05 = new TrainingType(3, 4, -1, -1, 3, 5, "Odcinki 3x 4m, przerwa 5s");
//        TrainingType tt06 = new TrainingType(4, -1, 6, -1, -1, -1, "6s ćwiczeń");
//        TrainingType tt07 = new TrainingType(5, 4, -1, -1, 3, -1, "Skip A 3x4m");
//        Training t1 = new Training(1, Arrays.asList(tt01, tt02, tt03));
//        Training t1 = new Training(1, Arrays.asList(tt01, tt07, tt06
//                , tt05, tt04, tt02, tt03
//        ));
//        Training t2 = new Training(2, Arrays.asList(tt04, tt04));
//        Training t3 = new Training(3, Arrays.asList(tt05, tt05));
//        Training t4 = new Training(4, Arrays.asList(tt06, tt06));
//        Training t5 = new Training(5, Arrays.asList(tt07, tt07));


//        Plan plan = new Plan(1, 1, 42.195, false, Arrays.asList(t1,t2,t3,t4,t5), new Date());

//        user.setPlan(plan);
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStaminaLevel(int staminaLevel) {
        this.staminaLevel = staminaLevel;
    }

    public int getStaminaLevel() {
        return staminaLevel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void clearData() {
        user = new User();
        staminaLevel = 0;
        age = 0;
        sex = 0;
    }

    @Override
    public String toString() {
        return "Singleton{" +
                "user=" + user +
                ", staminaLevel=" + staminaLevel +
                ", age=" + age +
                ", sex=" + sex +
                '}';
    }
}