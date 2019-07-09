package oktmo;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OktmoAnalyzer {

    private static List<String> listNameUsePattern = new ArrayList<String>();

    public static List<String> findNameUsePattern(List<Place> list, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        String name;
        for (Place place: list){
            name = place.getName();
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()){
                listNameUsePattern.add(name);
            }
        }
        return listNameUsePattern;
    }

    public static List<Place> findAllPlacesInGroup(OktmoGroup group, OktmoData data){
        //List<Place> foundPlaces = new ArrayList<>();
        Long beginIndex = group.getCode();
        Long endIndex = 0L;
        if (group.getLevel().equals(OktmoLevel.REGION)){
            endIndex = beginIndex + 1000000000;
        }else if (group.getLevel().equals(OktmoLevel.DISTRICT)){
            endIndex = beginIndex + 1000000;
        }else if (group.getLevel().equals(OktmoLevel.SETTLEMENT)){
            endIndex = beginIndex + 1000;
        }
        //data.getMapVillage().subMap(beginIndex, endIndex).forEach((k, v) -> foundPlaces.add(v));
        List<Place> foundPlaces = new ArrayList( data.getMapVillage().subMap(beginIndex, endIndex).values());
        //data.getMapVillage().subMap(beginIndex, endIndex).values().stream().collect(Collectors.toCollection(TreeSet::new));*/
        return foundPlaces;
    }

    public static String findMostPopularPlaceName(String regionName, OktmoData data){
        OktmoGroup oktmoGroup = data.getMapGroupsString().get(regionName);
        List<Place> listPlaceInsideRegion = findAllPlacesInGroup(oktmoGroup,data);
        Map<String, Long> map = listPlaceInsideRegion.stream()
                .collect(Collectors.groupingBy(Place::getName, Collectors.counting()));

        Map<String, AtomicInteger> mapAtomic = new HashMap<>();
        listPlaceInsideRegion.forEach((place) ->{
            if (mapAtomic.containsKey(place.getName())) {
                mapAtomic.get(place.getName()).incrementAndGet();
            } else {
                mapAtomic.put(place.getName(), new AtomicInteger(1));
            }
        });
        System.out.println(map.size() == mapAtomic.size());
        String keyOfMaxValue = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
        return  keyOfMaxValue;

        // другие способы
        /*map.entrySet().stream().max((k, v) -> k.getValue() > v.getValue() ? 1 : -1).get().getKey();
        Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
        Collections.max(map.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();*/

    }

    public static void printStatusTableForRegion(String regionName, OktmoData data){
        OktmoGroup oktmoGroup = data.getMapGroupsString().get(regionName);
        List<Place> listPlaceInsideRegion = findAllPlacesInGroup(oktmoGroup,data);
        Map<String, Long> map = listPlaceInsideRegion.stream()
                .collect(Collectors.groupingBy(Place::getStatus, Collectors.counting()));
        System.out.println(map);
    }
}
