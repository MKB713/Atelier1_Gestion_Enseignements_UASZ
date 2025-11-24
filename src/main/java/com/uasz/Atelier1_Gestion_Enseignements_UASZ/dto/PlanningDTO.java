package com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PlanningDTO {

    private String title;
    private Map<LocalDate, List<SeanceDTO>> schedule;

    public PlanningDTO() {
        this.schedule = new TreeMap<>(); // TreeMap to keep dates sorted
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<LocalDate, List<SeanceDTO>> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<LocalDate, List<SeanceDTO>> schedule) {
        this.schedule = schedule;
    }

    public void addSeance(LocalDate date, SeanceDTO seance) {
        this.schedule.computeIfAbsent(date, k -> new ArrayList<>()).add(seance);
    }
}
