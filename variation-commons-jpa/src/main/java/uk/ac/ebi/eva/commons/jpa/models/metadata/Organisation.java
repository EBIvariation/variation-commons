/*
 * Copyright 2015 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.jpa.models.metadata;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by tom on 12/10/15.
 */
@Entity
@Table(indexes = {@Index(name = "organisation_unique", columnList = "name,address", unique = true)})
public class Organisation extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -8470868229663325878L;

    private String name;
    private String email; // one or multiple emails?
    private String address; // one or multiple addresses?

    public Organisation() {
        this.name = null;
        this.email = null;
        this.address = null;
    }

    public Organisation(Long id) {
        this.setId(id);
    }

    public Organisation(String name, String address) {
        setName(name);
        setAddress(address);
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        Objects.requireNonNull(name, "Name not specified");
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (emailValidator.isValid(email) || email == null) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email address: " + email + " is not valid");
        }
    }

    public String getAddress() {
        return address;
    }

    public final void setAddress(String address) {
        Objects.requireNonNull(address, "Address not specified");
        this.address = address;
    }

    @Override
    public String toString() {
        return "Organisation{" + "name=" + name + ", address=" + address + ", email=" + email + '}';
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        } else if (!(e instanceof Organisation)) {
            return false;
        } else {
            return (Objects.equals(((Organisation) e).getName(), name)
                    && Objects.equals(((Organisation) e).getAddress(), address));
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }
}
