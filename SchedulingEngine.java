package timetable;

import java.util.*;


class SchedulingEngine {

    private static final Map<String, String> CS_MAP = new LinkedHashMap<>() {{
        put("Data Structures", "S. Pyne");
        put("Algorithms", "TK Mishra");
        put("Networks", "RK Mohapatra");
        put("OOPD", "S. Ahsa");
        put("DBMS", "S. Panigrahi");
        put("Artificial Intelligence", "P. Bhattacharya");
    }};

    private static final Map<String, String> EC_MAP = new LinkedHashMap<>() {{
        put("Signals", "S. Mohanty");
        put("Circuits", "D. Patra");
        put("VLSI Design", "RK. Mohapatra");
        put("Artificial Intelligence", "P. Bhattacharya");
    }};

    private static final String[]   DAYS    = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
    private static final String[]   SLOTS   = {
        "08:00 - 09:00","09:00 - 10:00","10:00 - 11:00","11:00 - 12:00",
        "13:00 - 14:00","14:00 - 15:00","15:00 - 16:00"
    };

    static String checkConflict(String day, String slot, String faculty, String room) {
        return DatabaseModule.fetchAllSessions().stream()
            .filter(r -> r[0].equals(day) && r[1].equals(slot))
            .map(r -> {
                if (r[3].equals(faculty)) return "Faculty '" + faculty + "' already booked at " + slot + " on " + day;
                if (r[4].equals(room))    return "Room '"    + room    + "' already occupied at " + slot + " on " + day;
                return null;
            })
            .filter(Objects::nonNull)
            .findFirst().orElse(null);
    }

    static int autoGenerate(String dept, String sem) {
        Map<String, String> map = dept.equals("Computer Science") ? CS_MAP : EC_MAP;
        String designatedRoom = dept.equals("Computer Science") ? "cs-323" : "EC-234";
        List<String> subjects = new ArrayList<>(map.keySet());

        Set<String> usedFac  = new HashSet<>();
        int count = 0;
        Random rnd = new Random();

        for (String day : DAYS) {
            usedFac.clear();
            for (String slot : SLOTS) {
                String sub  = subjects.get(rnd.nextInt(subjects.size()));
                String fac  = map.get(sub);
                String room = designatedRoom;
                
                if (usedFac.contains(fac)) continue;
                if (checkConflict(day, slot, fac, room) != null) continue;
                
                usedFac.add(fac);
                DatabaseModule.insertSession(day, slot, sub, fac, room, dept, sem);
                count++;
            }
        }
        return count;
    }
}