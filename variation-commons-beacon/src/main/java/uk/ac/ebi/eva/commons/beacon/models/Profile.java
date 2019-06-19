package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Profile
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class Profile   {
  @JsonProperty("country")
  private RestrictionsUL country = null;

  @JsonProperty("allowedCountries")
  @Valid
  private List<ProfileDescription> allowedCountries = null;

  @JsonProperty("organisation")
  private RestrictionsUL organisation = null;

  @JsonProperty("allowedOrganisations")
  @Valid
  private List<ProfileDescription> allowedOrganisations = null;

  @JsonProperty("nonProfitOrganisation")
  private RestrictionsULF nonProfitOrganisation = null;

  @JsonProperty("allowedNonProfitOrganisations")
  @Valid
  private List<ProfileDescription> allowedNonProfitOrganisations = null;

  @JsonProperty("profitOrganisation")
  private RestrictionsULF profitOrganisation = null;

  @JsonProperty("allowedProfitOrganisations")
  @Valid
  private List<ProfileDescription> allowedProfitOrganisations = null;

  @JsonProperty("person")
  private RestrictionsUL person = null;

  @JsonProperty("allowedPersons")
  @Valid
  private List<ProfileDescription> allowedPersons = null;

  @JsonProperty("academicProfessional")
  private RestrictionsULF academicProfessional = null;

  @JsonProperty("allowedAcademicProfessionals")
  @Valid
  private List<ProfileDescription> allowedAcademicProfessionals = null;

  @JsonProperty("clinicalProfessional")
  private RestrictionsULF clinicalProfessional = null;

  @JsonProperty("allowedClinicalProfessionals")
  @Valid
  private List<ProfileDescription> allowedClinicalProfessionals = null;

  @JsonProperty("profitProfessional")
  private RestrictionsULF profitProfessional = null;

  @JsonProperty("allowedProfitProfessionals")
  @Valid
  private List<ProfileDescription> allowedProfitProfessionals = null;

  @JsonProperty("nonProfessional")
  private RestrictionsULF nonProfessional = null;

  @JsonProperty("allowedNonProfessionals")
  @Valid
  private List<ProfileDescription> allowedNonProfessionals = null;

  @JsonProperty("nonProfitPurpose")
  private RestrictionsULF nonProfitPurpose = null;

  @JsonProperty("allowedNonProfitPurposes")
  @Valid
  private List<ProfileDescription> allowedNonProfitPurposes = null;

  @JsonProperty("profitPurpose")
  private RestrictionsULF profitPurpose = null;

  @JsonProperty("allowedProfitPurposes")
  @Valid
  private List<ProfileDescription> allowedProfitPurposes = null;

  @JsonProperty("researchPurpose")
  private RestrictionsULF researchPurpose = null;

  @JsonProperty("allowedResearchPurposes")
  @Valid
  private List<ProfileDescription> allowedResearchPurposes = null;

  @JsonProperty("allowedResearchProfiles")
  @Valid
  private List<ResearchDescription> allowedResearchProfiles = null;

  @JsonProperty("clinicalPurpose")
  private RestrictionsULF clinicalPurpose = null;

  @JsonProperty("allowedClinicalPurpose")
  @Valid
  private List<ProfileDescription> allowedClinicalPurpose = null;

  @JsonProperty("allowedClinicalProfiles")
  @Valid
  private List<ClinicalDescription> allowedClinicalProfiles = null;

  public Profile country(RestrictionsUL country) {
    this.country = country;
    return this;
  }

  /**
   * Get country
   * @return country
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsUL getCountry() {
    return country;
  }

  public void setCountry(RestrictionsUL country) {
    this.country = country;
  }

  public Profile allowedCountries(List<ProfileDescription> allowedCountries) {
    this.allowedCountries = allowedCountries;
    return this;
  }

  public Profile addAllowedCountriesItem(ProfileDescription allowedCountriesItem) {
    if (this.allowedCountries == null) {
      this.allowedCountries = new ArrayList<ProfileDescription>();
    }
    this.allowedCountries.add(allowedCountriesItem);
    return this;
  }

  /**
   * Get allowedCountries
   * @return allowedCountries
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedCountries() {
    return allowedCountries;
  }

  public void setAllowedCountries(List<ProfileDescription> allowedCountries) {
    this.allowedCountries = allowedCountries;
  }

  public Profile organisation(RestrictionsUL organisation) {
    this.organisation = organisation;
    return this;
  }

  /**
   * Get organisation
   * @return organisation
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsUL getOrganisation() {
    return organisation;
  }

  public void setOrganisation(RestrictionsUL organisation) {
    this.organisation = organisation;
  }

  public Profile allowedOrganisations(List<ProfileDescription> allowedOrganisations) {
    this.allowedOrganisations = allowedOrganisations;
    return this;
  }

  public Profile addAllowedOrganisationsItem(ProfileDescription allowedOrganisationsItem) {
    if (this.allowedOrganisations == null) {
      this.allowedOrganisations = new ArrayList<ProfileDescription>();
    }
    this.allowedOrganisations.add(allowedOrganisationsItem);
    return this;
  }

  /**
   * Get allowedOrganisations
   * @return allowedOrganisations
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedOrganisations() {
    return allowedOrganisations;
  }

  public void setAllowedOrganisations(List<ProfileDescription> allowedOrganisations) {
    this.allowedOrganisations = allowedOrganisations;
  }

  public Profile nonProfitOrganisation(RestrictionsULF nonProfitOrganisation) {
    this.nonProfitOrganisation = nonProfitOrganisation;
    return this;
  }

  /**
   * Get nonProfitOrganisation
   * @return nonProfitOrganisation
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getNonProfitOrganisation() {
    return nonProfitOrganisation;
  }

  public void setNonProfitOrganisation(RestrictionsULF nonProfitOrganisation) {
    this.nonProfitOrganisation = nonProfitOrganisation;
  }

  public Profile allowedNonProfitOrganisations(List<ProfileDescription> allowedNonProfitOrganisations) {
    this.allowedNonProfitOrganisations = allowedNonProfitOrganisations;
    return this;
  }

  public Profile addAllowedNonProfitOrganisationsItem(ProfileDescription allowedNonProfitOrganisationsItem) {
    if (this.allowedNonProfitOrganisations == null) {
      this.allowedNonProfitOrganisations = new ArrayList<ProfileDescription>();
    }
    this.allowedNonProfitOrganisations.add(allowedNonProfitOrganisationsItem);
    return this;
  }

  /**
   * Get allowedNonProfitOrganisations
   * @return allowedNonProfitOrganisations
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedNonProfitOrganisations() {
    return allowedNonProfitOrganisations;
  }

  public void setAllowedNonProfitOrganisations(List<ProfileDescription> allowedNonProfitOrganisations) {
    this.allowedNonProfitOrganisations = allowedNonProfitOrganisations;
  }

  public Profile profitOrganisation(RestrictionsULF profitOrganisation) {
    this.profitOrganisation = profitOrganisation;
    return this;
  }

  /**
   * Get profitOrganisation
   * @return profitOrganisation
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getProfitOrganisation() {
    return profitOrganisation;
  }

  public void setProfitOrganisation(RestrictionsULF profitOrganisation) {
    this.profitOrganisation = profitOrganisation;
  }

  public Profile allowedProfitOrganisations(List<ProfileDescription> allowedProfitOrganisations) {
    this.allowedProfitOrganisations = allowedProfitOrganisations;
    return this;
  }

  public Profile addAllowedProfitOrganisationsItem(ProfileDescription allowedProfitOrganisationsItem) {
    if (this.allowedProfitOrganisations == null) {
      this.allowedProfitOrganisations = new ArrayList<ProfileDescription>();
    }
    this.allowedProfitOrganisations.add(allowedProfitOrganisationsItem);
    return this;
  }

  /**
   * Get allowedProfitOrganisations
   * @return allowedProfitOrganisations
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedProfitOrganisations() {
    return allowedProfitOrganisations;
  }

  public void setAllowedProfitOrganisations(List<ProfileDescription> allowedProfitOrganisations) {
    this.allowedProfitOrganisations = allowedProfitOrganisations;
  }

  public Profile person(RestrictionsUL person) {
    this.person = person;
    return this;
  }

  /**
   * Get person
   * @return person
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsUL getPerson() {
    return person;
  }

  public void setPerson(RestrictionsUL person) {
    this.person = person;
  }

  public Profile allowedPersons(List<ProfileDescription> allowedPersons) {
    this.allowedPersons = allowedPersons;
    return this;
  }

  public Profile addAllowedPersonsItem(ProfileDescription allowedPersonsItem) {
    if (this.allowedPersons == null) {
      this.allowedPersons = new ArrayList<ProfileDescription>();
    }
    this.allowedPersons.add(allowedPersonsItem);
    return this;
  }

  /**
   * Get allowedPersons
   * @return allowedPersons
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedPersons() {
    return allowedPersons;
  }

  public void setAllowedPersons(List<ProfileDescription> allowedPersons) {
    this.allowedPersons = allowedPersons;
  }

  public Profile academicProfessional(RestrictionsULF academicProfessional) {
    this.academicProfessional = academicProfessional;
    return this;
  }

  /**
   * Get academicProfessional
   * @return academicProfessional
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getAcademicProfessional() {
    return academicProfessional;
  }

  public void setAcademicProfessional(RestrictionsULF academicProfessional) {
    this.academicProfessional = academicProfessional;
  }

  public Profile allowedAcademicProfessionals(List<ProfileDescription> allowedAcademicProfessionals) {
    this.allowedAcademicProfessionals = allowedAcademicProfessionals;
    return this;
  }

  public Profile addAllowedAcademicProfessionalsItem(ProfileDescription allowedAcademicProfessionalsItem) {
    if (this.allowedAcademicProfessionals == null) {
      this.allowedAcademicProfessionals = new ArrayList<ProfileDescription>();
    }
    this.allowedAcademicProfessionals.add(allowedAcademicProfessionalsItem);
    return this;
  }

  /**
   * Get allowedAcademicProfessionals
   * @return allowedAcademicProfessionals
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedAcademicProfessionals() {
    return allowedAcademicProfessionals;
  }

  public void setAllowedAcademicProfessionals(List<ProfileDescription> allowedAcademicProfessionals) {
    this.allowedAcademicProfessionals = allowedAcademicProfessionals;
  }

  public Profile clinicalProfessional(RestrictionsULF clinicalProfessional) {
    this.clinicalProfessional = clinicalProfessional;
    return this;
  }

  /**
   * Get clinicalProfessional
   * @return clinicalProfessional
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getClinicalProfessional() {
    return clinicalProfessional;
  }

  public void setClinicalProfessional(RestrictionsULF clinicalProfessional) {
    this.clinicalProfessional = clinicalProfessional;
  }

  public Profile allowedClinicalProfessionals(List<ProfileDescription> allowedClinicalProfessionals) {
    this.allowedClinicalProfessionals = allowedClinicalProfessionals;
    return this;
  }

  public Profile addAllowedClinicalProfessionalsItem(ProfileDescription allowedClinicalProfessionalsItem) {
    if (this.allowedClinicalProfessionals == null) {
      this.allowedClinicalProfessionals = new ArrayList<ProfileDescription>();
    }
    this.allowedClinicalProfessionals.add(allowedClinicalProfessionalsItem);
    return this;
  }

  /**
   * Get allowedClinicalProfessionals
   * @return allowedClinicalProfessionals
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedClinicalProfessionals() {
    return allowedClinicalProfessionals;
  }

  public void setAllowedClinicalProfessionals(List<ProfileDescription> allowedClinicalProfessionals) {
    this.allowedClinicalProfessionals = allowedClinicalProfessionals;
  }

  public Profile profitProfessional(RestrictionsULF profitProfessional) {
    this.profitProfessional = profitProfessional;
    return this;
  }

  /**
   * Get profitProfessional
   * @return profitProfessional
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getProfitProfessional() {
    return profitProfessional;
  }

  public void setProfitProfessional(RestrictionsULF profitProfessional) {
    this.profitProfessional = profitProfessional;
  }

  public Profile allowedProfitProfessionals(List<ProfileDescription> allowedProfitProfessionals) {
    this.allowedProfitProfessionals = allowedProfitProfessionals;
    return this;
  }

  public Profile addAllowedProfitProfessionalsItem(ProfileDescription allowedProfitProfessionalsItem) {
    if (this.allowedProfitProfessionals == null) {
      this.allowedProfitProfessionals = new ArrayList<ProfileDescription>();
    }
    this.allowedProfitProfessionals.add(allowedProfitProfessionalsItem);
    return this;
  }

  /**
   * Get allowedProfitProfessionals
   * @return allowedProfitProfessionals
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedProfitProfessionals() {
    return allowedProfitProfessionals;
  }

  public void setAllowedProfitProfessionals(List<ProfileDescription> allowedProfitProfessionals) {
    this.allowedProfitProfessionals = allowedProfitProfessionals;
  }

  public Profile nonProfessional(RestrictionsULF nonProfessional) {
    this.nonProfessional = nonProfessional;
    return this;
  }

  /**
   * Get nonProfessional
   * @return nonProfessional
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getNonProfessional() {
    return nonProfessional;
  }

  public void setNonProfessional(RestrictionsULF nonProfessional) {
    this.nonProfessional = nonProfessional;
  }

  public Profile allowedNonProfessionals(List<ProfileDescription> allowedNonProfessionals) {
    this.allowedNonProfessionals = allowedNonProfessionals;
    return this;
  }

  public Profile addAllowedNonProfessionalsItem(ProfileDescription allowedNonProfessionalsItem) {
    if (this.allowedNonProfessionals == null) {
      this.allowedNonProfessionals = new ArrayList<ProfileDescription>();
    }
    this.allowedNonProfessionals.add(allowedNonProfessionalsItem);
    return this;
  }

  /**
   * Get allowedNonProfessionals
   * @return allowedNonProfessionals
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedNonProfessionals() {
    return allowedNonProfessionals;
  }

  public void setAllowedNonProfessionals(List<ProfileDescription> allowedNonProfessionals) {
    this.allowedNonProfessionals = allowedNonProfessionals;
  }

  public Profile nonProfitPurpose(RestrictionsULF nonProfitPurpose) {
    this.nonProfitPurpose = nonProfitPurpose;
    return this;
  }

  /**
   * Get nonProfitPurpose
   * @return nonProfitPurpose
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getNonProfitPurpose() {
    return nonProfitPurpose;
  }

  public void setNonProfitPurpose(RestrictionsULF nonProfitPurpose) {
    this.nonProfitPurpose = nonProfitPurpose;
  }

  public Profile allowedNonProfitPurposes(List<ProfileDescription> allowedNonProfitPurposes) {
    this.allowedNonProfitPurposes = allowedNonProfitPurposes;
    return this;
  }

  public Profile addAllowedNonProfitPurposesItem(ProfileDescription allowedNonProfitPurposesItem) {
    if (this.allowedNonProfitPurposes == null) {
      this.allowedNonProfitPurposes = new ArrayList<ProfileDescription>();
    }
    this.allowedNonProfitPurposes.add(allowedNonProfitPurposesItem);
    return this;
  }

  /**
   * Get allowedNonProfitPurposes
   * @return allowedNonProfitPurposes
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedNonProfitPurposes() {
    return allowedNonProfitPurposes;
  }

  public void setAllowedNonProfitPurposes(List<ProfileDescription> allowedNonProfitPurposes) {
    this.allowedNonProfitPurposes = allowedNonProfitPurposes;
  }

  public Profile profitPurpose(RestrictionsULF profitPurpose) {
    this.profitPurpose = profitPurpose;
    return this;
  }

  /**
   * Get profitPurpose
   * @return profitPurpose
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getProfitPurpose() {
    return profitPurpose;
  }

  public void setProfitPurpose(RestrictionsULF profitPurpose) {
    this.profitPurpose = profitPurpose;
  }

  public Profile allowedProfitPurposes(List<ProfileDescription> allowedProfitPurposes) {
    this.allowedProfitPurposes = allowedProfitPurposes;
    return this;
  }

  public Profile addAllowedProfitPurposesItem(ProfileDescription allowedProfitPurposesItem) {
    if (this.allowedProfitPurposes == null) {
      this.allowedProfitPurposes = new ArrayList<ProfileDescription>();
    }
    this.allowedProfitPurposes.add(allowedProfitPurposesItem);
    return this;
  }

  /**
   * Get allowedProfitPurposes
   * @return allowedProfitPurposes
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedProfitPurposes() {
    return allowedProfitPurposes;
  }

  public void setAllowedProfitPurposes(List<ProfileDescription> allowedProfitPurposes) {
    this.allowedProfitPurposes = allowedProfitPurposes;
  }

  public Profile researchPurpose(RestrictionsULF researchPurpose) {
    this.researchPurpose = researchPurpose;
    return this;
  }

  /**
   * Get researchPurpose
   * @return researchPurpose
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getResearchPurpose() {
    return researchPurpose;
  }

  public void setResearchPurpose(RestrictionsULF researchPurpose) {
    this.researchPurpose = researchPurpose;
  }

  public Profile allowedResearchPurposes(List<ProfileDescription> allowedResearchPurposes) {
    this.allowedResearchPurposes = allowedResearchPurposes;
    return this;
  }

  public Profile addAllowedResearchPurposesItem(ProfileDescription allowedResearchPurposesItem) {
    if (this.allowedResearchPurposes == null) {
      this.allowedResearchPurposes = new ArrayList<ProfileDescription>();
    }
    this.allowedResearchPurposes.add(allowedResearchPurposesItem);
    return this;
  }

  /**
   * Get allowedResearchPurposes
   * @return allowedResearchPurposes
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedResearchPurposes() {
    return allowedResearchPurposes;
  }

  public void setAllowedResearchPurposes(List<ProfileDescription> allowedResearchPurposes) {
    this.allowedResearchPurposes = allowedResearchPurposes;
  }

  public Profile allowedResearchProfiles(List<ResearchDescription> allowedResearchProfiles) {
    this.allowedResearchProfiles = allowedResearchProfiles;
    return this;
  }

  public Profile addAllowedResearchProfilesItem(ResearchDescription allowedResearchProfilesItem) {
    if (this.allowedResearchProfiles == null) {
      this.allowedResearchProfiles = new ArrayList<ResearchDescription>();
    }
    this.allowedResearchProfiles.add(allowedResearchProfilesItem);
    return this;
  }

  /**
   * Get allowedResearchProfiles
   * @return allowedResearchProfiles
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ResearchDescription> getAllowedResearchProfiles() {
    return allowedResearchProfiles;
  }

  public void setAllowedResearchProfiles(List<ResearchDescription> allowedResearchProfiles) {
    this.allowedResearchProfiles = allowedResearchProfiles;
  }

  public Profile clinicalPurpose(RestrictionsULF clinicalPurpose) {
    this.clinicalPurpose = clinicalPurpose;
    return this;
  }

  /**
   * Get clinicalPurpose
   * @return clinicalPurpose
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getClinicalPurpose() {
    return clinicalPurpose;
  }

  public void setClinicalPurpose(RestrictionsULF clinicalPurpose) {
    this.clinicalPurpose = clinicalPurpose;
  }

  public Profile allowedClinicalPurpose(List<ProfileDescription> allowedClinicalPurpose) {
    this.allowedClinicalPurpose = allowedClinicalPurpose;
    return this;
  }

  public Profile addAllowedClinicalPurposeItem(ProfileDescription allowedClinicalPurposeItem) {
    if (this.allowedClinicalPurpose == null) {
      this.allowedClinicalPurpose = new ArrayList<ProfileDescription>();
    }
    this.allowedClinicalPurpose.add(allowedClinicalPurposeItem);
    return this;
  }

  /**
   * Get allowedClinicalPurpose
   * @return allowedClinicalPurpose
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileDescription> getAllowedClinicalPurpose() {
    return allowedClinicalPurpose;
  }

  public void setAllowedClinicalPurpose(List<ProfileDescription> allowedClinicalPurpose) {
    this.allowedClinicalPurpose = allowedClinicalPurpose;
  }

  public Profile allowedClinicalProfiles(List<ClinicalDescription> allowedClinicalProfiles) {
    this.allowedClinicalProfiles = allowedClinicalProfiles;
    return this;
  }

  public Profile addAllowedClinicalProfilesItem(ClinicalDescription allowedClinicalProfilesItem) {
    if (this.allowedClinicalProfiles == null) {
      this.allowedClinicalProfiles = new ArrayList<ClinicalDescription>();
    }
    this.allowedClinicalProfiles.add(allowedClinicalProfilesItem);
    return this;
  }

  /**
   * Get allowedClinicalProfiles
   * @return allowedClinicalProfiles
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ClinicalDescription> getAllowedClinicalProfiles() {
    return allowedClinicalProfiles;
  }

  public void setAllowedClinicalProfiles(List<ClinicalDescription> allowedClinicalProfiles) {
    this.allowedClinicalProfiles = allowedClinicalProfiles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profile profile = (Profile) o;
    return Objects.equals(this.country, profile.country) &&
        Objects.equals(this.allowedCountries, profile.allowedCountries) &&
        Objects.equals(this.organisation, profile.organisation) &&
        Objects.equals(this.allowedOrganisations, profile.allowedOrganisations) &&
        Objects.equals(this.nonProfitOrganisation, profile.nonProfitOrganisation) &&
        Objects.equals(this.allowedNonProfitOrganisations, profile.allowedNonProfitOrganisations) &&
        Objects.equals(this.profitOrganisation, profile.profitOrganisation) &&
        Objects.equals(this.allowedProfitOrganisations, profile.allowedProfitOrganisations) &&
        Objects.equals(this.person, profile.person) &&
        Objects.equals(this.allowedPersons, profile.allowedPersons) &&
        Objects.equals(this.academicProfessional, profile.academicProfessional) &&
        Objects.equals(this.allowedAcademicProfessionals, profile.allowedAcademicProfessionals) &&
        Objects.equals(this.clinicalProfessional, profile.clinicalProfessional) &&
        Objects.equals(this.allowedClinicalProfessionals, profile.allowedClinicalProfessionals) &&
        Objects.equals(this.profitProfessional, profile.profitProfessional) &&
        Objects.equals(this.allowedProfitProfessionals, profile.allowedProfitProfessionals) &&
        Objects.equals(this.nonProfessional, profile.nonProfessional) &&
        Objects.equals(this.allowedNonProfessionals, profile.allowedNonProfessionals) &&
        Objects.equals(this.nonProfitPurpose, profile.nonProfitPurpose) &&
        Objects.equals(this.allowedNonProfitPurposes, profile.allowedNonProfitPurposes) &&
        Objects.equals(this.profitPurpose, profile.profitPurpose) &&
        Objects.equals(this.allowedProfitPurposes, profile.allowedProfitPurposes) &&
        Objects.equals(this.researchPurpose, profile.researchPurpose) &&
        Objects.equals(this.allowedResearchPurposes, profile.allowedResearchPurposes) &&
        Objects.equals(this.allowedResearchProfiles, profile.allowedResearchProfiles) &&
        Objects.equals(this.clinicalPurpose, profile.clinicalPurpose) &&
        Objects.equals(this.allowedClinicalPurpose, profile.allowedClinicalPurpose) &&
        Objects.equals(this.allowedClinicalProfiles, profile.allowedClinicalProfiles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(country, allowedCountries, organisation, allowedOrganisations, nonProfitOrganisation, allowedNonProfitOrganisations, profitOrganisation, allowedProfitOrganisations, person, allowedPersons, academicProfessional, allowedAcademicProfessionals, clinicalProfessional, allowedClinicalProfessionals, profitProfessional, allowedProfitProfessionals, nonProfessional, allowedNonProfessionals, nonProfitPurpose, allowedNonProfitPurposes, profitPurpose, allowedProfitPurposes, researchPurpose, allowedResearchPurposes, allowedResearchProfiles, clinicalPurpose, allowedClinicalPurpose, allowedClinicalProfiles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profile {\n");
    
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    allowedCountries: ").append(toIndentedString(allowedCountries)).append("\n");
    sb.append("    organisation: ").append(toIndentedString(organisation)).append("\n");
    sb.append("    allowedOrganisations: ").append(toIndentedString(allowedOrganisations)).append("\n");
    sb.append("    nonProfitOrganisation: ").append(toIndentedString(nonProfitOrganisation)).append("\n");
    sb.append("    allowedNonProfitOrganisations: ").append(toIndentedString(allowedNonProfitOrganisations)).append("\n");
    sb.append("    profitOrganisation: ").append(toIndentedString(profitOrganisation)).append("\n");
    sb.append("    allowedProfitOrganisations: ").append(toIndentedString(allowedProfitOrganisations)).append("\n");
    sb.append("    person: ").append(toIndentedString(person)).append("\n");
    sb.append("    allowedPersons: ").append(toIndentedString(allowedPersons)).append("\n");
    sb.append("    academicProfessional: ").append(toIndentedString(academicProfessional)).append("\n");
    sb.append("    allowedAcademicProfessionals: ").append(toIndentedString(allowedAcademicProfessionals)).append("\n");
    sb.append("    clinicalProfessional: ").append(toIndentedString(clinicalProfessional)).append("\n");
    sb.append("    allowedClinicalProfessionals: ").append(toIndentedString(allowedClinicalProfessionals)).append("\n");
    sb.append("    profitProfessional: ").append(toIndentedString(profitProfessional)).append("\n");
    sb.append("    allowedProfitProfessionals: ").append(toIndentedString(allowedProfitProfessionals)).append("\n");
    sb.append("    nonProfessional: ").append(toIndentedString(nonProfessional)).append("\n");
    sb.append("    allowedNonProfessionals: ").append(toIndentedString(allowedNonProfessionals)).append("\n");
    sb.append("    nonProfitPurpose: ").append(toIndentedString(nonProfitPurpose)).append("\n");
    sb.append("    allowedNonProfitPurposes: ").append(toIndentedString(allowedNonProfitPurposes)).append("\n");
    sb.append("    profitPurpose: ").append(toIndentedString(profitPurpose)).append("\n");
    sb.append("    allowedProfitPurposes: ").append(toIndentedString(allowedProfitPurposes)).append("\n");
    sb.append("    researchPurpose: ").append(toIndentedString(researchPurpose)).append("\n");
    sb.append("    allowedResearchPurposes: ").append(toIndentedString(allowedResearchPurposes)).append("\n");
    sb.append("    allowedResearchProfiles: ").append(toIndentedString(allowedResearchProfiles)).append("\n");
    sb.append("    clinicalPurpose: ").append(toIndentedString(clinicalPurpose)).append("\n");
    sb.append("    allowedClinicalPurpose: ").append(toIndentedString(allowedClinicalPurpose)).append("\n");
    sb.append("    allowedClinicalProfiles: ").append(toIndentedString(allowedClinicalProfiles)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
