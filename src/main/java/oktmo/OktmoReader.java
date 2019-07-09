package oktmo;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OktmoReader {

    public void readGroups(String fileName, OktmoData data) throws IOException {
        String s;
        /*String regexRegion = "\"(?!(?:00))(\\d+)\";\"(000)\";\"(000)\";\"(000)\";\"\\d+\";\"      \\d+\";\"(.*?)\\s*(([А-ЯЁA-Z0-9].*?)|([а-я\\s^-]*)|(\".*?))\";.*";
        String regexDistrict = "\"(?!(?:00))(\\d+)\";\"(\\d+)\";\"(000)\";\"(000)\";\"\\d+\";\"   \\d+\";\"(.*?)\\s*(([А-ЯЁA-Z0-9].*?)|([а-я\\s^-]*)|(\".*?))\";.*";
        String regexSettlement = "\"(?!(?:00))(\\d+)\";\"(\\d+)\";\"(\\d+)\";\"(000)\";\"\\d+\";\"\\d+\";\"(.*?)\\s*(([А-ЯЁA-Z0-9].*?)|([а-я\\s^-]*)|(\".*?))\";.*";
        String regexVillage = "\"(?!(?:00))(\\d+)\";\"(\\d+)\";\"(\\d+)\";\"(?!(?:000))(\\d+)\";\"\\d+\";\"\\d+\";\"(.*?)\\s*(([А-ЯЁA-Z0-9].*?)|([а-я\\s^-]*)|(\".*?))\";.*";*/
        String regex = "\"(\\d+)\";\"(\\d+)\";\"(\\d+)\";\"(\\d+)\";\"\\d+\";\"\\d+\";\"(.*?)\\s*(([А-ЯЁA-Z0-9].*?)|([а-я\\s^-]*)|(\".*?))\";.*";
        /*Pattern patternRegion = Pattern.compile(regexRegion, Pattern.UNICODE_CASE);
        Pattern patternDistrict = Pattern.compile(regexDistrict, Pattern.UNICODE_CASE);
        Pattern patternSettlement = Pattern.compile(regexSettlement, Pattern.UNICODE_CASE);
        Pattern patternVillage = Pattern.compile(regexVillage, Pattern.UNICODE_CASE);*/
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
        Matcher matcher;
        long codeLastRegion = 0L;
        long codeLastDistrict = 0L;
        File file = new File(fileName);
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "cp1251"));
        ){
            while (((s = reader.readLine()) != null )) {
                matcher = pattern.matcher(s);
                OktmoGroup group;

                if (matcher.matches()){
                    long code = Long.parseLong(matcher.group(1)+matcher.group(2)+matcher.group(3)+matcher.group(4));
                    if ((code % 1_000_000_000 == 0) && (code !=0)){
                        group = createNewGroup(matcher, code, OktmoLevel.REGION);
                        if (group != null){
                            data.addGroup(group);
                            data.addGroupString(group);
                            codeLastRegion = group.getCode();
                        }
                    }else if ((code % 1_000_000 == 0) && (code !=0)){
                        group = createNewGroup(matcher, code, OktmoLevel.DISTRICT);
                        if (group != null){
                            data.addGroup(group);
                            data.addGroupString(group);
                            codeLastDistrict = group.getCode();
                            data.getMapGroups().get(codeLastRegion).addInsideGroup(group);
                        }
                    }else if ((code % 1_000 == 0) && (code !=0)){
                        group = createNewGroup(matcher, code, OktmoLevel.SETTLEMENT);
                        if (group != null){
                            data.addGroup(group);
                            data.addGroupString(group);
                            data.getMapGroups().get(codeLastDistrict).addInsideGroup(group);
                        }
                    }else if (code != 0){
                        Place place = createNewPlace(matcher);
                        data.addVillageInMap(place);
                    }

                }

                /*matcher = patternRegion.matcher(s);
                if (matcher.matches()){
                    group = createNewGroup(matcher, OktmoLevel.REGION);
                    if (group != null){
                        data.addGroup(group);
                        codeLastRegion = group.getCode();
                    }
                    continue;
                }

                matcher = patternDistrict.matcher(s);
                if (matcher.matches()){
                    group = createNewGroup(matcher, OktmoLevel.DISTRICT);
                    if (group != null){
                        data.addGroup(group);
                        codeLastDistrict = group.getCode();
                        data.getMapGroups().get(codeLastRegion).addInsideGroup(group);
                    }
                    continue;
                }

                matcher = patternSettlement.matcher(s);
                if (matcher.matches()){
                    group = createNewGroup(matcher, OktmoLevel.SETTLEMENT);
                    if (group != null){
                        data.addGroup(group);
                        data.getMapGroups().get(codeLastDistrict).addInsideGroup(group);
                    }
                    continue;
                }

                matcher = patternVillage.matcher(s);
                if (matcher.matches()){
                    Place place = createNewPlace(matcher);
                    data.addVillageInMap(place);
                    continue;
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OktmoGroup createNewGroup(Matcher matcher, long code, OktmoLevel level){
        String name = matcher.group(6).replace("\"", "");
        if (!(name.contains("Населенные пункты") | name.contains("Городские поселения") |
                name.contains("Межселенные территории") | name.contains("Сельские поселения") |
                name.contains("Городские округа") | name.contains("Внутригородские округа"))){
            OktmoGroup group = new OktmoGroup(name, code, level);
            return group;
        }else return null;
    }

    // из второй лабы
    public void readPlacesUsePattern(String fileName, OktmoData data){
        long start = System.nanoTime();
        int lineCount = 0;
        Place place;
        String s;
        String regex = "\"(\\d+)\";\"(\\d+)\";\"(\\d+)\";\"(?!(?:000))(\\d+)\";\"\\d+\";\"\\d+\";\"(?!(?:Насел[её]нные\\s+пункты))" +
                "(.*?)\\s*(([А-ЯЁA-Z0-9].*?)|([а-я\\s^-]*)|(\".*?))\";.*";
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher;

        try{
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "cp1251"));
            while (((s = reader.readLine()) != null )) {
                lineCount++;
                matcher = pattern.matcher(s);
                if (matcher.matches()){
                    place = createNewPlace(matcher);
                    data.addPlace(place);
                    data.addStatus(place.getStatus());
                }
            }
        }catch (Exception e) {
            System.out.println("Error in line " + lineCount);
            System.out.println(data.getListPlace().get(data.getListPlace().size()-1));
            e.printStackTrace();
        }
        long time = System.nanoTime() - start;
        double timeOfWork = (double)time / 1000000000.0;
        System.out.println("Время работы с регулярным выражением: " + timeOfWork + " сек.");
        System.out.println("Количество записей с регулярным выражением: " + data.getListPlace().size());
    }

    public Place createNewPlace(Matcher matcher){
        Long codeSettlement = Long.parseLong(matcher.group(1)+matcher.group(2)+matcher.group(3)+matcher.group(4));
        String statusSettlement = matcher.group(5);
        String nameSettlement = matcher.group(6).replace("\"", "");
        Place place = new Place(codeSettlement,statusSettlement,nameSettlement);
        return place;
    }

    public void readPlaces(String fileName, OktmoData data) throws IOException {
        int lineCount = 0;
        long start = System.nanoTime();
        String s;
        String [] arrStr = null;
        try{
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "cp1251"));
            while (((s = reader.readLine()) != null )) {
                lineCount++;
                s = s.replace("\"", "");
                arrStr = s.split(";");
                Place place = writePlace(arrStr);
                if (place != null){
                    data.addPlace(place);
                    data.addStatus(place.getStatus());
                }
            }
        }catch (Exception e) {
            System.out.println("Error in line " + lineCount);
            System.out.println(data.getListPlace().get(data.getListPlace().size()-1));
            e.printStackTrace();
        }
        long time = System.nanoTime() - start;
        double timeOfWork = (double)time / 1000000000.0;
        System.out.println("Время работы с методами: " + timeOfWork + " сек.");
        System.out.println("Количество записей с методами: " + data.getListPlace().size());
    }

    public Place writePlace(String[] arrStr){

        Long codeSettlement = getCodeSettlement(arrStr);
        String nameSettlement = getNameSettlement(arrStr);
        String statusSettlement;
        Place place = null;

        if ((codeSettlement!=0)){
            int indexEnd = findIndex(nameSettlement);
            statusSettlement = nameSettlement.substring(0, indexEnd).trim();
            nameSettlement = nameSettlement.substring(indexEnd).trim();
            place = new Place(codeSettlement, statusSettlement, nameSettlement);

        }
        return place;
    }

    public long getCodeSettlement(String[] arrStr){
        Long codeSettlement;
        if (!((arrStr[3].equals("000")))){
            codeSettlement = Long.parseLong((arrStr[0] + arrStr[1] + arrStr[2] + arrStr[3]).trim());
            return codeSettlement;
        }else {
            return 0;
        }
    }

    public String getNameSettlement(String[] arrStr){
        String nameSettlement = arrStr[6];
        return nameSettlement;
    }

    public int findIndex(String nameSettlement){
        int indexEnd=0;
        char[]arrCharNameTown = nameSettlement.toCharArray();
        for (int i = 0; i < arrCharNameTown.length; i++) {
            if ((Character.isUpperCase(arrCharNameTown[i])) | (Character.isDigit(arrCharNameTown[i]))) {
                indexEnd = i;
                break;
            }
        }
        return indexEnd;
    }


}
