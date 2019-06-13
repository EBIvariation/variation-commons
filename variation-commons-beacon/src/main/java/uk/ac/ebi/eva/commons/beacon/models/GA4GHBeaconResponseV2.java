/*
 * European Variation Archive (EVA) - Open-access database of all types of genetic
 * variation data from all species
 *
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.eva.commons.beacon.models;

import java.util.List;

public interface GA4GHBeaconResponseV2 {

    String getId();

    String getName();

    String getApiVersion();

    BeaconOrganization getOrganization();

    String getDescription();

    String getVersion();

    String getWelcomeUrl();

    String getAlternativeUrl();

    String getCreateDateTime();

    String getUpdateDateTime();

    List<BeaconDataset> getDatasets();
}
