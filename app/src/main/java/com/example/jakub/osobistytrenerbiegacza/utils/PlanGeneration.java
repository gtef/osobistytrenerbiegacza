package com.example.jakub.osobistytrenerbiegacza.utils;

import com.example.jakub.osobistytrenerbiegacza.Singleton;
import com.example.jakub.osobistytrenerbiegacza.model.Plan;
import com.example.jakub.osobistytrenerbiegacza.model.Training;
import com.example.jakub.osobistytrenerbiegacza.model.TrainingType;
import com.example.jakub.osobistytrenerbiegacza.model.User;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Jakub on 2015-11-15.
 */
public class PlanGeneration {

    public static int getPlanDuration(String distance) {
        switch (distance) {
            case "Maraton": return 80;
            case "Półmaraton": return 60;
            case "10 km": return 42;
            default: return 0;
        }
    }

    public static void generate(String distance, int time, Date startDate) {
        switch (distance) {
            case "Maraton": generateMarathonPlan(time,startDate); break;
            case "Półmaraton": generateHalfMarathonPlan(time,startDate); break;
            case "10 km": generateTenKmPlan(time,startDate); break;
        }
    }

//    private static void generateTenKmPlan(int time) {
//        TrainingType tt1 = new TrainingType(1, -1, 3600, -1, 1, -1, "Lekki bieg 60 min.");
//        TrainingType tt2 = new TrainingType(2, -1, 30, -1, 3, 60, "Odcinki 3x30s, przerwa 60s");
//        TrainingType tt3 = new TrainingType(3, 100, -1, -1, 2, 60, "Przebieżki 2x100m, przerwa 60s");
//        Training t1 = new Training(1, Arrays.asList(tt1,tt2));
//        Training t2 = new Training(2,Arrays.asList(tt2,tt3));
//        Training t3 = new Training(3,Arrays.asList(tt1));
//        Plan plan = new Plan(1, 3, 42.195, false, Arrays.asList(t1,t2,t3));
//        User u = Singleton.getInstance().getUser();
//        u.setPlan(plan);
//    }
//
//    //TODO generate plan
//    private static void generateHalfMarathonPlan(int time) {
//        TrainingType tt1 = new TrainingType(1, -1, 3600, -1, 1, -1, "Lekki bieg 60 min.");
//        TrainingType tt2 = new TrainingType(2, -1, 30, -1, 3, 60, "Odcinki 3x30s, przerwa 60s");
//        TrainingType tt3 = new TrainingType(3, 100, -1, -1, 2, 60, "Przebieżki 2x100m, przerwa 60s");
//        Training t1 = new Training(1, Arrays.asList(tt1,tt2));
//        Training t2 = new Training(2,Arrays.asList(tt2,tt3));
//        Training t3 = new Training(3,Arrays.asList(tt1));
//        Plan plan = new Plan(1, 3, 42.195, false, Arrays.asList(t1,t2,t3));
//        User u = Singleton.getInstance().getUser();
//        u.setPlan(plan);
//    }
//
//    //TODO generate plan
//    private static void generateMarathonPlan(int time) {
//        TrainingType tt1 = new TrainingType(1, -1, 3600, -1, 1, -1, "Lekki bieg 60 min.");
//        TrainingType tt2 = new TrainingType(2, -1, 30, -1, 3, 60, "Odcinki 3x30s, przerwa 60s");
//        TrainingType tt3 = new TrainingType(3, 100, -1, -1, 2, 60, "Przebieżki 2x100m, przerwa 60s");
//        Training t1 = new Training(1, Arrays.asList(tt1,tt2));
//        Training t2 = new Training(2,Arrays.asList(tt2,tt3));
//        Training t3 = new Training(3,Arrays.asList(tt1));
//        Plan plan = new Plan(1, 3, 42.195, false, Arrays.asList(t1,t2,t3));
//        User u = Singleton.getInstance().getUser();
//        u.setPlan(plan);
//    }


//    private static void generateTenKmPlan(int time, Date startDate) {
//        TrainingType tt01 = new TrainingType(1, -1, 3, -1, 1, -1, "Lekki bieg 3s.");
//        TrainingType tt02 = new TrainingType(1, -1, 4, -1, 1, -1, "Lekki bieg 4s.");
//        TrainingType tt03 = new TrainingType(1, -1, 2, -1, 1, -1, "Lekki bieg 2s.");
//        TrainingType tt2 = new TrainingType(1, -1, 3600, -1, 1, -1, "Lekki bieg 60 min.");
//        TrainingType tt3 = new TrainingType(2, -1, 30, -1, 3, 60, "Odcinki 3x30s, przerwa 60s");
//        TrainingType tt4 = new TrainingType(3, 100, -1, -1, 2, 60, "Przebieżki 2x100m, przerwa 60s");
//        Training t1 = new Training(1, Arrays.asList(tt01,tt02,tt03));
//        Training t2 = new Training(2, Arrays.asList(tt2,tt3));
//        Training t3 = new Training(3,Arrays.asList(tt3,tt4));
//        Training t4 = new Training(4,Arrays.asList(tt2));
//        Plan plan = new Plan(1, 3, 42.195, false, Arrays.asList(t1,t2,t3,t4), startDate);
//        User u = Singleton.getInstance().getUser();
//        u.setPlan(plan);
//    }

    //TODO generate plan
    private static void generateHalfMarathonPlan(int time, Date startDate) {
        TrainingType tt01 = new TrainingType(1, -1, 3, -1, 1, -1, "Lekki bieg 3s.");
        TrainingType tt02 = new TrainingType(1, -1, 4, -1, 1, -1, "Lekki bieg 4s.");
        TrainingType tt03 = new TrainingType(1, -1, 2, -1, 1, -1, "Lekki bieg 2s.");
        TrainingType tt2 = new TrainingType(1, -1, 3600, -1, 1, -1, "Lekki bieg 60 min.");
        TrainingType tt3 = new TrainingType(2, -1, 30, -1, 3, 60, "Odcinki 3x30s, przerwa 60s");
        TrainingType tt4 = new TrainingType(3, 100, -1, -1, 2, 60, "Przebieżki 2x100m, przerwa 60s");
        Training t1 = new Training(1, Arrays.asList(tt01,tt02,tt03));
        Training t2 = new Training(2, Arrays.asList(tt2,tt3));
        Training t3 = new Training(3,Arrays.asList(tt3,tt4));
        Training t4 = new Training(4,Arrays.asList(tt2));
        Plan plan = new Plan(1, 3, 42.195, false, Arrays.asList(t1,t2,t3,t4), startDate);
        User u = Singleton.getInstance().getUser();
        u.setPlan(plan);
    }

    //TODO generate plan
    private static void generateMarathonPlan(int time, Date startDate) {
        TrainingType tt01 = new TrainingType(1, -1, 3, -1, 1, -1, "Lekki bieg 3s.");
        TrainingType tt02 = new TrainingType(1, -1, 4, -1, 1, -1, "Lekki bieg 4s.");
        TrainingType tt03 = new TrainingType(1, -1, 2, -1, 1, -1, "Lekki bieg 2s.");
        TrainingType tt2 = new TrainingType(1, -1, 3600, -1, 1, -1, "Lekki bieg 60 min.");
        TrainingType tt3 = new TrainingType(2, -1, 30, -1, 3, 60, "Odcinki 3x30s, przerwa 60s");
        TrainingType tt4 = new TrainingType(3, 100, -1, -1, 2, 60, "Przebieżki 2x100m, przerwa 60s");
        Training t1 = new Training(1, Arrays.asList(tt01,tt02,tt03));
        Training t2 = new Training(3, Arrays.asList(tt2,tt3));
        Training t3 = new Training(5,Arrays.asList(tt3,tt4));
        Training t4 = new Training(6,Arrays.asList(tt2));

        TrainingType ttB = new TrainingType(6, -1, -1, -1, -1, -1, "Odpoczynek");
        Training t5 = new Training(2, Arrays.asList(ttB));
        Training t6 = new Training(4, Arrays.asList(ttB));
        Plan plan = new Plan(1, 3, 42.195, false, Arrays.asList(t1,t5,t2,t6,t3,t4), startDate);
        User u = Singleton.getInstance().getUser();
        u.setPlan(plan);
    }


    /**
     *
     * @param time requested time in minutes
     * @param startDate date when we want to stat a plan
     */
    private static void generateTenKmPlan(int time, Date startDate) {
        int numberOfTrainings = 42;
        Training[] planArray = new Training[numberOfTrainings];

        int dayOFWeek, week;
        if (time <= 47) {//three days of rest. Training MON WED FRI SUN
            dayOFWeek = 1;
            week = 1;
            for(int i=0; i<planArray.length; i++) {
                if (dayOFWeek == 8) {
                    dayOFWeek = 1;
                    ++week;
                }
                switch (dayOFWeek) {
                    case 1: planArray[i] = getMondayTraining10km(time, week, i + 1); break;
                    case 3: planArray[i] = getWednesdayTraining10km(time, week, i + 1); break;
                    case 5: planArray[i] = getFridayTraining10km(time, week, i + 1); break;
                    case 7: planArray[i] = getSundayTraining10km(time, week, i + 1); break;
                    default:planArray[i] = getRestTraining(i+1);
                }
                ++dayOFWeek;
            }
        } else {//four days of rest. Training TUE THU SUN
            dayOFWeek = 1;
            week = 1;
            for(int i=0; i<planArray.length; i++) {
                if (dayOFWeek == 8) {
                    dayOFWeek = 1;
                    ++week;
                }
                switch (dayOFWeek) {
                    case 2: planArray[i] = getTuesdayTraining10km(time, week, i + 1); break;
                    case 5: planArray[i] = getFridayTraining10km(time, week, i + 1); break;
                    case 7: planArray[i] = getSundayTraining10km(time, week, i + 1); break;
                    default:planArray[i] = getRestTraining(i+1);
                }
                ++dayOFWeek;
            }
        }
        //set 10 km competition
        planArray[numberOfTrainings-1] = new Training(numberOfTrainings, Arrays.asList(new TrainingType(3,10000,-1,-1,1,0,"Start: 10 km")));

        Plan plan = new Plan(1, numberOfTrainings, 10, false, Arrays.asList(planArray), startDate);
        User u = Singleton.getInstance().getUser();
        u.setPlan(plan);
    }

    private static Training getMondayTraining10km(int time, int week, int trainingNumber) {
        int distance = -1;
        int times = -1;
        int breakTime = -1;
        double velocity = -1;
        switch (week) {
            case 1:
                velocity = 6.0*(double)time;
                distance = 1000;
                breakTime = 120;
                times = 7;
                break;
            case 2:
                distance = 8000;
                velocity = 6.0*(double)time+90.0;
                break;
            case 3:
                velocity = 6.0*(double)time-10.0;
                distance = 1000;
                breakTime = 120;
                times = 7;
                break;
            case 4:
                velocity = 6.0*(double)time + 20.0;
                distance = 3000;
                breakTime = 180;
                times = 3;
                break;
            case 5:
                velocity = 6.0*(double)time;
                distance = 2000;
                breakTime = 120;
                times = 4;
                break;
            case 6:
                velocity = 6.0*(double)time;
                distance = 1000;
                breakTime = 120;
                times = 4;
                break;
        }

        velocity = Math.floor(velocity);
        if (week == 2) {
            if(velocity<=400) {
                return new Training(trainingNumber,Arrays.asList(new TrainingType(3,distance,-1,-1,1,0,"BC1 "+distance/1000+" km w tempie "+ DateFormatter.minPerKm2(velocity))));
            }else {
                int t = (int)(distance/1000)*400/60;
                return new Training(trainingNumber, Arrays.asList(new TrainingType(1, -1, t, -1, 1, -1, "Lekki bieg "+t+" min")));
            }
        } else {
            return new Training(trainingNumber, Arrays.asList(
                    new TrainingType(1, -1, 600, -1, 1, -1, "Lekki bieg 10 min"),
                    new TrainingType(4, -1, 900, -1, -1, -1, "15 min ćwiczenia"),
                    new TrainingType(3, distance, -1, -1, times, breakTime, "Odcinki " + times + "x " + distance / 1000 + " km w tempie " + DateFormatter.minPerKm2(velocity) + ", przerwa " + breakTime / 60+" min"),
                    new TrainingType(1, -1, 300, -1, 1, -1, "Lekki bieg 5 min")));
        }
    }

    private static Training getTuesdayTraining10km(int time, int week, int trainingNumber) {
        int distance = -1;
        int times = -1;
        int breakTime = -1;
        double velocity = -1;
        switch (week) {
            case 1:
                velocity = 6.0*(double)time;
                distance = 1000;
                breakTime = 120;
                times = 4;
                break;
            case 2:
                distance = 7000;
                velocity = 6.0*(double)time+30.0;
                break;
            case 3:
                velocity = 6.0*(double)time-10.0;
                distance = 2000;
                breakTime = 120;
                times = 4;
                break;
            case 4:
                velocity = 6.0*(double)time + 20.0;
                distance = 3000;
                breakTime = 180;
                times = 3;
                break;
            case 5:
                velocity = 6.0*(double)time;
                distance = 1000;
                breakTime = 120;
                times = 5;
                break;
            case 6:
                distance = 6000;
                velocity = 6.0*(double)time+60.0;
                break;
        }

        velocity = Math.floor(velocity);
        if (week == 2 || week == 6) {
            if(velocity<=400) {
                return new Training(trainingNumber,Arrays.asList(new TrainingType(3,distance,-1,-1,1,0,"BC1 "+distance/1000+" km w tempie "+ DateFormatter.minPerKm2(velocity))));
            }else {
                int t = (int)(distance/1000)*400/60;
                return new Training(trainingNumber, Arrays.asList(new TrainingType(1, -1, t, -1, 1, -1, "Lekki bieg "+t+" min")));
            }
        } else {
            return new Training(trainingNumber, Arrays.asList(
                    new TrainingType(1, -1, 600, -1, 1, -1, "Lekki bieg 10 min"),
                    new TrainingType(4, -1, 900, -1, -1, -1, "15 min ćwiczenia"),
                    new TrainingType(3, distance, -1, -1, times, breakTime, "Odcinki " + times + "x " + distance / 1000 + " km w tempie " + DateFormatter.minPerKm2(velocity) + ", przerwa " + breakTime / 60+" min"),
                    new TrainingType(1, -1, 300, -1, 1, -1, "Lekki bieg 5 min")));
        }
    }

    private static Training getWednesdayTraining10km(int time, int week, int trainingNumber) {
        //y=-2/5x+26 round down
        //y=-1/5x+19 round down
        int distance = -1;
        switch (week) {
            case 1:
            case 4:
                distance = (int) (-0.4 * time + 26) * 1000;
                break;
            case 2:
            case 3:
            case 5:
                distance = (int) (-0.2 * time + 19) * 1000;
                break;
            case 6:
                distance = 7000;
                break;
        }

        double x = 0.2 * (double) time - 6.0;
        double velocity = week % 2 != 0 ? 5.0 * Math.pow(x, 2.0) + 5.0 * x + 230.0 : 5.0 * Math.pow(x, 2.0) + 5.0 * x + 255.0;
        velocity = Math.floor(velocity);

        return new Training(trainingNumber, Arrays.asList(new TrainingType(3, distance, -1, -1, 1, 0, "BC2 " + distance / 1000 + " km w tempie " + DateFormatter.minPerKm2(velocity))));

    }

    private static Training getFridayTraining10km(int time, int week, int trainingNumber) {
        int distance = -1, distance2 = -1;
        int times = -1;
        int breakTime = -1;
        double velocity = -1, velocity2 = -1;
        switch (week) {
            case 1:
                velocity = 1.8*(double)time + 12.0;
                distance = 400;
                breakTime = 120;
                times = 10;
                break;
            case 2:
                velocity = 6.0*(double)time + 5.0;
                distance = 5000;
                break;
            case 3:
                velocity = 0.4*(double)time + 28.0;
                distance = 200;
                breakTime = 60;
                times = 15;
                break;
            case 4:
                velocity = 6.0*(double)time - 5.0;
                distance = 5000;
                break;
            case 5:
                velocity = 1.8*(double)time - 9.0;
                distance = 300;
                breakTime = 60;
                times = 10;
                break;
            case 6:
                velocity = (time+5)/10 * 60.0;
                velocity2 = velocity + 60;
                distance = 1000;
                distance2 = 2000;
                breakTime = 180;
                times = 2;
                break;
        }

        velocity = Math.floor(velocity);
        velocity2 = Math.floor(velocity2);

        if (week == 1 || week == 3 || week == 5) {
            return new Training(trainingNumber, Arrays.asList(
                    new TrainingType(1, -1, 900, -1, 1, -1, "Lekki bieg 15 min"),
                    new TrainingType(3, distance, -1, -1, times, breakTime, "Odcinki " + times + "x " + distance + " m w " + (int)velocity + " s, przerwa " + breakTime / 60+" min"),
                    new TrainingType(1, -1, 300, -1, 1, -1, "Lekki bieg 5 min")));
        } else if (week == 2 || week == 4) {
            return new Training(trainingNumber, Arrays.asList(
                    new TrainingType(1, -1, 900, -1, 1, -1, "Lekki bieg 15 min"),
                    new TrainingType(3,distance,-1,-1,1,0,"BC2 "+distance/1000+" km w tempie "+ DateFormatter.minPerKm2(velocity)),
                    new TrainingType(1, -1, 300, -1, 1, -1, "Lekki bieg 5 min")));
        }else if (week == 6) {
            return new Training(trainingNumber, Arrays.asList(
                    new TrainingType(1, -1, 900, -1, 1, -1, "Lekki bieg 15 min"),
                    new TrainingType(3, distance, -1, -1, times, breakTime, "Odcinki " + times + "x " + distance / 1000 + " km w tempie " + DateFormatter.minPerKm2(velocity) + ", przerwa " + breakTime / 60+" min"),
                    new TrainingType(3,distance2,-1,-1,1,0,"BC2 "+distance2/1000+" km w tempie "+ DateFormatter.minPerKm2(velocity2)),
                    new TrainingType(1, -1, 300, -1, 1, -1, "Lekki bieg 5 min")));
        } else {
            return null;
        }
    }

    private static Training getSundayTraining10km(int time, int week, int trainingNumber) {
        int distance = -1;
        switch (week) {
            case 1:
            case 2:
            case 5:distance = 10000; break;
            case 3:
            case 4:distance = 15000; break;
            case 6:distance = 10000;break;
        }

        double velocitySlow = (double)time*6.0 + 90.0;
        double velocityFast = (double)time*6.0 + 60.0;
        double velocity = week % 2 == 0 ? velocitySlow:velocityFast;
        velocity = Math.floor(velocity);
        if(velocity<=400) {
            return new Training(trainingNumber,Arrays.asList(new TrainingType(3,distance,-1,-1,1,0,"BC1 "+distance/1000+" km w tempie "+ DateFormatter.minPerKm2(velocity))));
        } else {
            int t = (int)(distance/1000)*400/60;
            return new Training(trainingNumber, Arrays.asList(new TrainingType(1, -1, t, -1, 1, -1, "Lekki bieg "+t+" min")));
        }
    }

    private static Training getRestTraining(int trainingNumber) {
        return new Training(trainingNumber, Arrays.asList(new TrainingType(6, -1, -1, -1, -1, -1, "Odpoczynek")));
    }
}