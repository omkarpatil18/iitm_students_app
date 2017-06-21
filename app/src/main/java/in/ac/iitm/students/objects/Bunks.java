package in.ac.iitm.students.objects;

/**
 * Created by SAM10795 on 19-06-2017.
 */

public class Bunks extends Course {
    private int bunk_tot;
    private int bunk_done;

    public int getBunk_done() {
        return bunk_done;
    }

    public int getBunk_tot() {
        return bunk_tot;
    }

    public void setBunk_done(int bunk_done) {
        this.bunk_done = bunk_done;
    }

    public void setBunk_tot(int bunk_tot) {
        this.bunk_tot = bunk_tot;
    }
}
