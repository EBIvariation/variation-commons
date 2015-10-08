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

package embl.ebi.variation.commons.models.metadata;

import java.util.*;

/**
 * Created by parce on 05/10/15.
 */
public class Dataset {
    private long id;
    // EVA PRO (ega_ega_dataset table)
    private String submissionId;
    private String centerName;
    private int statusId;
    private Date holdDate;
    private boolean protect;
    private int version;
    private String md5;
    private Date auditTime;
    private String auditUser;
    private String auditOsuser;
    private Date firstCleated;
    private Date firstPublic;
    private Boolean editable;
    private Date lastUpdated;
    //private String egaSubmissionId; // TODO: this is a foreign key?? replace by object reference

    private Set<FileGenerator> fileGenerators;

    public Dataset(int id) {
        this.id = id;
    }

    public void addFileGenerator(FileGenerator generator) {
        generator.setDataset(this);
    }

    void internalAddFileGenerator(FileGenerator generator) {
        if (fileGenerators == null) {
            fileGenerators = new HashSet<FileGenerator>();
        }
        fileGenerators.add(generator);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Date getHoldDate() {
        return holdDate;
    }

    public void setHoldDate(Date holdDate) {
        this.holdDate = holdDate;
    }

    public boolean isProtect() {
        return protect;
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditOsuser() {
        return auditOsuser;
    }

    public void setAuditOsuser(String auditOsuser) {
        this.auditOsuser = auditOsuser;
    }

    public Date getFirstCleated() {
        return firstCleated;
    }

    public void setFirstCleated(Date firstCleated) {
        this.firstCleated = firstCleated;
    }

    public Date getFirstPublic() {
        return firstPublic;
    }

    public void setFirstPublic(Date firstPublic) {
        this.firstPublic = firstPublic;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<FileGenerator> getFileGenerators() {
        return fileGenerators;
    }

    public void setFileGenerators(Set<FileGenerator> fileGenerators) {
        this.fileGenerators = fileGenerators;
    }
}
