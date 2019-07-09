package oktmo;

import java.util.*;

public class OktmoData {

    private List<Place> listPlace = new ArrayList<Place>();
    private Set<String> setStatus = new HashSet<String>();
    private List<Place> sortedPlaces = new ArrayList<Place>();
    private Map<Long, OktmoGroup> mapGroups = new TreeMap();
    private Map<String, OktmoGroup> mapGroupsString = new TreeMap();
    private SortedMap<Long, Place> mapVillage = new TreeMap();

    public SortedMap<Long, Place> getMapVillage() {
        return mapVillage;
    }

    public List<Place> getListPlace() {
        return listPlace;
    }

    public Set<String> getSetStatus() {
        return setStatus;
    }

    public Map<Long, OktmoGroup> getMapGroups() {
        return mapGroups;
    }

    public Map<String, OktmoGroup> getMapGroupsString() {
        return mapGroupsString;
    }

    public List<Place> getSortedByNamePlaces() {
        sortedPlaces.addAll(getListPlace());
        Collections.sort(sortedPlaces, new Comparator<Place>(){
            public int compare(Place obj1, Place obj2) {
                String str1 = obj1.getName();
                String str2 = obj2.getName();
                return str1.compareTo(str2);
            }
        });
        return sortedPlaces;
    }

    public void addPlace(Place place){
        getListPlace().add(place);
    }

    public void addStatus(String status){
        getSetStatus().add(status);
    }

    public void addGroup(OktmoGroup group){
        getMapGroups().put(group.getCode(), group);
    }

    public void addVillageInMap(Place place){
        getMapVillage().put(place.getCode(), place);
    }

    public void addGroupString(OktmoGroup group){
        getMapGroupsString().put(group.getName(),group);
    }


}
