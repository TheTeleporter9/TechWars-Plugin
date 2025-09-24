package org.solocode.techwars.TechTree;

import org.solocode.teams.Team;
import java.util.ArrayList;
import java.util.List;

public class TeamResearchData {
    private final Team team;
    private List<String> unlockedStages;
    private int currentStage;
    
    public TeamResearchData(Team team) {
        this.team = team;
        loadData();
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        Object unlockedData = team.getData("unlockedStages");
        if (unlockedData instanceof List) {
            this.unlockedStages = (List<String>) unlockedData;
        } else {
            this.unlockedStages = new ArrayList<>();
            team.setData("unlockedStages", this.unlockedStages);
        }
        
        Object stageData = team.getData("currentStage");
        if (stageData instanceof Integer) {
            this.currentStage = (Integer) stageData;
        } else {
            this.currentStage = 1;
            team.setData("currentStage", this.currentStage);
        }
    }
    
    public List<String> getUnlockedStages() {
        return new ArrayList<>(unlockedStages);
    }
    
    public int getCurrentStage() {
        return currentStage;
    }
    
    public void unlockStage(int stage) {
        if (!unlockedStages.contains(String.valueOf(stage))) {
            unlockedStages.add(String.valueOf(stage));
            currentStage = stage + 1;
            saveData();
        }
    }
    
    public void resetProgress() {
        unlockedStages.clear();
        currentStage = 1;
        saveData();
    }
    
    private void saveData() {
        team.setData("unlockedStages", unlockedStages);
        team.setData("currentStage", currentStage);
    }
}