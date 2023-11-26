package hr.java.production.sort;

import hr.java.production.model.Item;

import java.io.Serializable;
import java.util.Comparator;

public class VolumeSorter implements Comparator<Item>, Serializable {
    @Override
    public int compare(Item i1, Item i2) {
        int volumeComparison = i2.calculateVolume().compareTo(i1.calculateVolume());
        if (volumeComparison != 0) {
            return volumeComparison;
        }
        return i1.getName().compareTo(i2.getName());
    }
}
