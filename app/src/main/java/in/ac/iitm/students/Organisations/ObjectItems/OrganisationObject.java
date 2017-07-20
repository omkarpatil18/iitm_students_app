package in.ac.iitm.students.Organisations.ObjectItems;

/**
 * Created by rohithram on 2/7/17.
 */

public class OrganisationObject {

    public String logo_url;
    public String org_name;
    public String pageid;
    public String org_about;
    public Boolean isYoutube = false ;
    public String channelID = " ";

    public  OrganisationObject(String logo_url,String org_name,String pageid,String org_about){
        this.logo_url = logo_url;
        this.org_name = org_name;
        this.pageid = pageid;
        this.org_about = org_about;

    }

}
