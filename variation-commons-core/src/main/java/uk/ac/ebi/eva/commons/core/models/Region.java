/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.core.models;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private String chromosome;
    private Long start;
    private Long end;

    public Region() {
    }

    public Region(String chromosome, Long start) {
        this(chromosome, start, null);
    }

    public Region(String chromosome, Long start, Long end) {
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
    }

    public Region(String region) {
        if (region != null && !region.equals("")) {
            if (region.indexOf(':') != -1) {
                String[] fields = region.split("[:-]", -1);
                if (fields.length == 3) {
                    this.chromosome = fields[0];
                    this.start = Long.parseLong(fields[1]);
                    this.end = Long.parseLong(fields[2]);
                } else if (fields.length == 2) {
                    this.chromosome = fields[0];
                    this.start = Long.parseLong(fields[1]);
                    this.end = null;
                }
            } else {
                this.chromosome = region;
                this.start = null;
                this.end = null;
            }
        }
    }

    public static Region parseRegion(String regionString) {
        Region region = null;
        if (regionString != null && !regionString.equals("")) {
            if (regionString.indexOf(':') != -1) {
                String[] fields = regionString.split("[:-]", -1);
                if (fields.length == 3) {
                    region = new Region(fields[0], Long.parseLong(fields[1]), Long.parseLong(fields[2]));
                } else if (fields.length == 2) {
                    region = new Region(fields[0], Long.parseLong(fields[1]));
                }
            } else {
                region = new Region(regionString);
            }
        }
        return region;
    }

    public static List<Region> parseRegions(String regionsString) {
        List<Region> regions = null;
        if (regionsString != null && !regionsString.equals("")) {
            String[] regionItems = regionsString.split(",");
            regions = new ArrayList<>(regionItems.length);
            String[] fields;
            for (String regionString : regionItems) {
                if (regionString.indexOf(':') != -1) {
                    fields = regionString.split("[:-]", -1);
                    if (fields.length == 3) {
                        regions.add(new Region(fields[0], Long.parseLong(fields[1]), Long.parseLong(fields[2])));
                    } else {
                        regions.add(null);
                    }
                } else {
                    regions.add(new Region(regionString));
                }
            }
        }
        return regions;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Region region = (Region) o;

        if (chromosome != null ? !chromosome.equals(region.chromosome) : region.chromosome != null) {
            return false;
        }
        if (start != null ? !start.equals(region.start) : region.start != null) {
            return false;
        }
        return end != null ? end.equals(region.end) : region.end == null;
    }

    @Override
    public int hashCode() {
        int result = chromosome != null ? chromosome.hashCode() : 0;
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    public boolean contains(String chr, long pos) {
        if (this.chromosome.equals(chr) && this.start <= pos && this.end >= pos) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.chromosome);

        if (this.start != null && this.end != null) {
            sb.append(":").append(this.start).append("-").append(this.end);
        } else if (this.start != null && this.end == null) {
            sb.append(":").append(this.start);
        }

        return sb.toString();
    }
}
