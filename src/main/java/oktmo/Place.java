package oktmo;

import java.util.Objects;

public class Place implements Comparable<Place> {

    private long code;
    private String status;
    private String name;

    public Place(long code, String status, String name) {
        this.code = code;
        this.status = status;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Place{" +
                "status='" + status + '\'' + ", name='" + name + '\'' + ", code='" + code + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return code == place.code &&
                Objects.equals(status, place.status) &&
                Objects.equals(name, place.name);
    }

    @Override
    public int compareTo(Place o) {
        int result = this.name.compareTo(o.name);
        if (result == 0) {
            result = this.status.compareTo(o.status);
        }
        if (result == 0){
            Long thisCode = this.code;
            result = thisCode.compareTo(o.code);
        }
        return result;
    }
}
