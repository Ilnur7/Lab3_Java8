package oktmo;

import java.util.ArrayList;
import java.util.List;

public class OktmoGroup {
    private String name;
    private long code;
    private OktmoLevel level;

    private List<OktmoGroup> insideGroups = new ArrayList<OktmoGroup>();

    public OktmoGroup(String name, long code, OktmoLevel level) {
        this.name = name;
        this.code = code;
        this.level = level;
    }

    public OktmoLevel getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public long getCode() {
        return code;
    }

    public List<OktmoGroup> getInsideGroups() {
        return insideGroups;
    }

    public void addInsideGroup(OktmoGroup oktmoGroup) {
        insideGroups.add(oktmoGroup);
    }

    @Override
    public String toString() {
        return "OktmoGroup{" +
                "level='" + level + '\'' +
                ", code=" + code +
                ", name=" + name +
                ", insideGroups=" + insideGroups +
                '}';
    }
}
