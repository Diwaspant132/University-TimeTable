package timetable;

// ============================================================
//  PROGRAMMER 3 — Data Persistence Module
//  Uses Java built-in serialization (zero external JAR needed)
//  Simulates JDBC-style API: insert / fetchAll / fetchTimetable
//  Data saved to: timetable_data.ser in working directory
// ============================================================

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseModule {

    // Each session stored as String[7]: day,slot,subject,faculty,room,dept,semester
    private static final List<String[]> SESSION_STORE = new ArrayList<>();
    private static final String DATA_FILE = "timetable_data.ser";

    // ── Initialise: load from file or seed defaults ──────────
    @SuppressWarnings("unchecked")
    public static void initDB() {
        File f = new File(DATA_FILE);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                List<String[]> loaded = (List<String[]>) ois.readObject();
                SESSION_STORE.clear();
                SESSION_STORE.addAll(loaded);
                System.out.println("[DB] Loaded " + SESSION_STORE.size() + " sessions from file.");
                return;
            } catch (Exception e) {
                System.err.println("[DB] Could not read data file, seeding fresh: " + e.getMessage());
            }
        }
        seedDefaults();
        persist();
    }

    // ── Seed sample data ─────────────────────────────────────
    private static void seedDefaults() {
        String[][] rows = {
                { "Monday", "08:00 - 09:00", "Data Structures", "Dr. Sharma", "Room 101", "Computer Science",
                        "Semester 1" },
                { "Monday", "09:00 - 10:00", "Algorithms", "Dr. Gupta", "Room 102", "Computer Science", "Semester 1" },
                { "Monday", "10:00 - 11:00", "DBMS", "Dr. Mehta", "Room 201", "Computer Science", "Semester 2" },
                { "Tuesday", "08:00 - 09:00", "Networks", "Dr. Gupta", "Lab A", "Computer Science", "Semester 1" },
                { "Tuesday", "09:00 - 10:00", "Operating Systems", "Dr. Sharma", "Room 101", "Computer Science",
                        "Semester 1" },
                { "Tuesday", "11:00 - 12:00", "Software Engg", "Dr. Mehta", "Room 202", "Computer Science",
                        "Semester 2" },
                { "Wednesday", "08:00 - 09:00", "Signals", "Dr. Rao", "Room 202", "Electronics", "Semester 1" },
                { "Wednesday", "09:00 - 10:00", "Circuits", "Dr. Patel", "Lab B", "Electronics", "Semester 1" },
                { "Wednesday", "10:00 - 11:00", "Microprocessors", "Dr. Rao", "Room 101", "Electronics", "Semester 2" },
                { "Thursday", "10:00 - 11:00", "Artificial Intelligence", "Dr. Gupta", "Room 101", "Computer Science",
                        "Semester 3" },
                { "Thursday", "11:00 - 12:00", "Machine Learning", "Dr. Sharma", "Lab A", "Computer Science",
                        "Semester 3" },
                { "Friday", "08:00 - 09:00", "VLSI Design", "Dr. Mehta", "Room 202", "Electronics", "Semester 2" },
                { "Friday", "09:00 - 10:00", "Embedded Systems", "Dr. Patel", "Lab B", "Electronics", "Semester 3" },
                { "Friday", "11:00 - 12:00", "Compilers", "Dr. Sharma", "Room 102", "Computer Science", "Semester 3" },
        };
        SESSION_STORE.clear();
        for (String[] r : rows)
            SESSION_STORE.add(r);
        System.out.println("[DB] Seeded " + SESSION_STORE.size() + " default sessions.");
    }

    // ── Persist to disk ──────────────────────────────────────
    private static void persist() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(new ArrayList<>(SESSION_STORE));
        } catch (IOException e) {
            System.err.println("[DB] Warning: Could not save data: " + e.getMessage());
        }
    }

    // ── INSERT ───────────────────────────────────────────────
    public static boolean insertSession(String day, String slot, String subject,
            String faculty, String room,
            String dept, String sem) {
        SESSION_STORE.add(new String[] { day, slot, subject, faculty, room, dept, sem });
        persist();
        return true;
    }

    // ── SELECT all ───────────────────────────────────────────
    public static List<String[]> fetchAllSessions() {
        return SESSION_STORE.stream()
                .map(r -> new String[] { r[0], r[1], r[2], r[3], r[4], r[5] })
                .collect(Collectors.toList());
    }

    // ── SELECT by dept + semester ────────────────────────────
    public static List<String[]> fetchTimetable(String dept, String sem) {
        return SESSION_STORE.stream()
                .filter(r -> r[5].equals(dept) && r[6].equals(sem))
                .map(r -> new String[] { r[0], r[1], r[2], r[4] }) // day,slot,subject,room
                .collect(Collectors.toList());
    }

    // ── DELETE by day + slot + dept ──────────────────────────
    public static boolean deleteSession(String day, String slot, String dept) {
        boolean removed = SESSION_STORE.removeIf(
                r -> r[0].equals(day) && r[1].equals(slot) && r[5].equals(dept));
        if (removed)
            persist();
        return removed;
    }

    // ── DELETE all ───────────────────────────────────────────
    public static void clearAll() {
        SESSION_STORE.clear();
        persist();
    }
}