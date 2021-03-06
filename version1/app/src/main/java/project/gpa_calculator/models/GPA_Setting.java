package project.gpa_calculator.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GPA_Setting implements Iterable<GPA>, Serializable {

    private static GPA_Setting instance;
    private String docID;
    private ArrayList<GPA> GPAs = new ArrayList<>();

    private List<Integer> sort_num;

    @Override
    public Iterator<GPA> iterator() {
        return new Iterator<GPA>() {
            private final Iterator<GPA> GPAI = GPAs.iterator();

            @Override
            public boolean hasNext() {
                return GPAI.hasNext();
            }

            @Override
            public GPA next() {
                if (GPAI.hasNext())
                    return GPAI.next();
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("no changes allowed");
            }
        };

    }


    /**
     * basic setting that match University of Toronto GPA
     */
    private GPA_Setting() {
        GPAs.add(new GPA(90, 100, 4.0, "A+"));
        GPAs.add(new GPA(85, 89, 4.0, "A"));
        GPAs.add(new GPA(80, 84, 3.7, "A-"));
        GPAs.add(new GPA(77, 79, 3.3, "B+"));
        GPAs.add(new GPA(73, 76, 3.0, "B"));
        GPAs.add(new GPA(70, 72, 2.7, "B-"));
        GPAs.add(new GPA(67, 69, 2.3, "C+"));
        GPAs.add(new GPA(63, 66, 2.0, "C"));
        GPAs.add(new GPA(60, 62, 1.7, "C-"));
        GPAs.add(new GPA(57, 59, 1.3, "D+"));
        GPAs.add(new GPA(53, 56, 1.0, "D"));
        GPAs.add(new GPA(50, 52, 0.7, "D-"));
        GPAs.add(new GPA(0, 49, 0.0, "F"));

    }

    public static GPA_Setting getInstance() {
        if (instance == null) {
            instance = new GPA_Setting();
        }
        return instance;
    }

    public ArrayList<GPA> getGPAs() {
        return GPAs;
    }

    public void setGPAs(ArrayList<GPA> GPAs) {
        this.GPAs = GPAs;
    }

    public void add(GPA new_gpa) {
        GPAs.add(new_gpa);
    }

    public void remove(int pos) {
        GPAs.remove(pos);
    }

    public void update(int pos, GPA new_gpa) {
        GPAs.set(pos, new_gpa);
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public boolean checkValidity() {
        heap();
        ArrayList<GPA> sorted = new ArrayList<>();
        while (!GPAs.isEmpty()) {
            sorted.add(GPAs.get(0));
            GPAs.set(0, GPAs.get(GPAs.size() - 1));
            GPAs.remove(GPAs.size() - 1);
            heap();
        }
        GPAs = sorted;
        for (int i = 1; i < sorted.size() - 1; i++) {
            if (sorted.get(i).lower != sorted.get(i + 1).upper - 1 || sorted.get(i).lower > sorted.get(i).upper) {
                return false;
            }
        }

        return true;
    }

    /**
     * Given a score in percentage, return the corresponding GPA
     *
     * @param score Score in percentage
     * @return the corresponding GPA
     */
    public double calculate_GPA(double score) {
        for (GPA g : this) {
            if (g.getLower() < score && score < g.getUpper()) {
                return g.getGrade_point();
            }
        }
        return -1;
    }

    public void heap() {
        for (int i = 0; i < GPAs.size(); i++) {
            if (i != 0) {
                int j = i;
                while (GPAs.get(j).upper > GPAs.get((int) Math.floor(j / 2)).upper) {
                    GPA temp = GPAs.get(j);
                    GPAs.set(j, GPAs.get((int) Math.floor(j / 2)));
                    GPAs.set((int) Math.floor(j / 2), temp);
                    j = (int) Math.floor(j / 2);
                }
            }
        }
    }

    public String getDocID() {
        return docID;
    }
}
