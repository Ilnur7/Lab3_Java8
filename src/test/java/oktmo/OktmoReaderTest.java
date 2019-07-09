package oktmo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OktmoReaderTest {

    OktmoReader oktmoReader = new OktmoReader();
    OktmoData oktmoData = new OktmoData();
    OktmoData oktmoDataRegex = new OktmoData();

    @Test
    void readPlaces() throws IOException {

        oktmoReader.readPlaces("data-201710.csv", oktmoData);
        List<Place> listPlace = oktmoData.getListPlace();
        int countSettlement = listPlace.size();

        //проверка 10-ого объекта
        assertEquals(155868, countSettlement);
        assertEquals("Боровское", listPlace.get(10).getName());
        assertEquals("с", listPlace.get(10).getStatus());
        assertEquals(1601417101, listPlace.get(10).getCode());
        //проверка последнего объекта
        assertEquals("Биробиджан", listPlace.get(countSettlement-1).getName());
        assertEquals("г", listPlace.get(countSettlement-1).getStatus());
        assertEquals(99701000001L, listPlace.get(countSettlement-1).getCode());

    }

    @Test
    void readPlacesUsePattern() {
        oktmoReader.readPlacesUsePattern("data-201710.csv", oktmoDataRegex);
        List<Place> listPlaceRegex = oktmoDataRegex.getListPlace();
        int countSettlement = listPlaceRegex.size();
        //проверка 10-ого объекта
        assertEquals(155868, countSettlement);
        assertEquals("Боровское", listPlaceRegex.get(10).getName());
        assertEquals("с", listPlaceRegex.get(10).getStatus());
        assertEquals(1601417101, listPlaceRegex.get(10).getCode());
        //проверка последнего объекта
        assertEquals("Биробиджан", listPlaceRegex.get(countSettlement-1).getName());
        assertEquals("г", listPlaceRegex.get(countSettlement-1).getStatus());
        assertEquals(99701000001L, listPlaceRegex.get(countSettlement-1).getCode());
    }

    @Test
    void checkSameData() {
        Collections.sort(oktmoData.getListPlace());
        Collections.sort(oktmoDataRegex.getListPlace());
        assertTrue(oktmoData.getListPlace().equals(oktmoData.getListPlace()));
    }

    @Test
    void checkCount() throws IOException {
        oktmoReader.readGroups("data-201710.csv", oktmoData);
        assertEquals(14, oktmoData.getMapGroups().get(42618000000L).getInsideGroups().size());
        assertEquals("Чапаевское", oktmoData.getMapGroups().get(35652482000L).getName());
    }


}