
package oktmo;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OktmoMain {
    public static void main(String[] args) throws IOException {
        OktmoReader oktmoReader = new OktmoReader();
        OktmoData oktmoDataGroup = new OktmoData();
        oktmoReader.readGroups("data-201710.csv", oktmoDataGroup);

        //oktmoDataGroup.getMapGroups().forEach((k, v) -> System.out.println(v));
        System.out.println("\n"+OktmoAnalyzer.findMostPopularPlaceName("Муниципальные образования Республики Башкортостан", oktmoDataGroup));
        OktmoAnalyzer.printStatusTableForRegion("Муниципальные образования Республики Башкортостан", oktmoDataGroup);

        /*OktmoData oktmoData = new OktmoData();
        OktmoData oktmoDataRegex = new OktmoData();
        oktmoReader.readPlaces("data-201710.csv", oktmoData);
        oktmoReader.readPlacesUsePattern("data-201710.csv", oktmoDataRegex);
        List<String> listWithFilter1 = OktmoAnalyzer.findNameUsePattern(oktmoData.getListPlace(), "(\\w){0,2}(ово)$");
        List<String> listWithFilter2 = OktmoAnalyzer.findNameUsePattern(oktmoData.getListPlace(), "^(?i)([^аеёиоуыэюя]).*\\1$");*/

    }
}
