package embl.ebi.variation.commons.models.metadata;

import org.apache.commons.validator.EmailValidator;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;

/**
 * Created by tom on 12/10/15.
 */
public class Organisation {

    private String name;
    private String email; // one or multiple emails?
    private String address; // one or multiple addresses?

    private Set<Study> studies = new HashSet<>();


    public Organisation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(emailValidator.isValid(email)){
            this.email = email;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Study> getStudies() {
        return Collections.unmodifiableSet(studies);
    }

    // TODO between study and centre, who is responsible for the "adding" of the other?
    // can a study exist without a centre? would a study be "spawned" from a centre, meaning centre would be adding the study
//    public void setStudies(Set<Study> studies) {
//        this.studies.clear();
//        for(Study study: studies){
//            addStudy(study);
//        }
//    }

    void addStudy(Study study){
        studies.add(study);
    }


    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        }else if (!(e instanceof Organisation)) {
            return false;
        }else {
            return Objects.equals(((Organisation) e).getName(), name);
        }
    }

    @Override
    public int hashCode() {
        if (address != null){
            int result = 17;
            result = 31 * result + name.hashCode();
            result = 31 * result + address.hashCode();
            return result;
        }else{
            return name.hashCode();
        }
    }
}
