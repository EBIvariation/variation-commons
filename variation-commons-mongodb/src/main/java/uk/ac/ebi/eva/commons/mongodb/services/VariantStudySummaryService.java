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
package uk.ac.ebi.eva.commons.mongodb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.projections.VariantStudySummary;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

/**
 * Mongo persistence service that returns {@link VariantStudySummary} projections
 */
@Service
public class VariantStudySummaryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * the equivalent intended query is:
     * db.files.aggregate([
     * {$group: {_id : {studyId : "$sid", studyName : "$sname"}, filesCount : {$sum : 1}}},
     * {$project: {"studyId" : "$_id.studyId", "studyName" : "$_id.studyName", "_id" : 0, "filesCount":"$filesCount" }}
     * ])
     * See also the inner explanation of those 2 stages
     *
     * @see #groupAndCount
     * @see #projectAndFlatten
     */
    public List<VariantStudySummary> findAll() {
        Aggregation aggregation = Aggregation.newAggregation(
                groupAndCount(),
                projectAndFlatten()
        );

        AggregationResults<VariantStudySummary> studies = mongoTemplate.aggregate(aggregation,
                VariantSourceMongo.class,
                VariantStudySummary.class);

        return studies.getMappedResults();
    }

    /**
     * Group by both "studyName" and "studyId", and count the documents in the group into an accumulator field
     * "filesCount". The idea behind grouping by 2 fields is to include both fields without further projections or
     * queries (this group operation is not intended for having the combinations because a studyId will have
     * always the same studyName).
     */
    private GroupOperation groupAndCount() {
        return group(VariantStudySummary.STUDY_ID, VariantStudySummary.STUDY_NAME)
                .count().as(VariantStudySummary.FILES_COUNT);
    }

    /**
     * Flatten the document from {_id: {sid, sname}, filesCount} to {sid, sname, filesCount} which is ready to
     * be mapped into a {@link VariantStudySummary}.
     */
    private ProjectionOperation projectAndFlatten() {
        return project(VariantStudySummary.FILES_COUNT)
                .and(VariantStudySummary.ID + "." + VariantStudySummary.STUDY_ID)
                .as(VariantStudySummary.STUDY_ID)
                .and(VariantStudySummary.ID + "." + VariantStudySummary.STUDY_NAME)
                .as(VariantStudySummary.STUDY_NAME);
    }

    /**
     * the equivalent intended query is:
     * db.files.aggregate([
     * {$match: {date : {$gte : fromDate}}},
     * {$group: {_id : {studyId : "$sid", studyName : "$sname"}, filesCount : {$sum : 1}}},
     * {$project: {"studyId" : "$_id.studyId", "studyName" : "$_id.studyName", "_id" : 0, "filesCount":"$filesCount" }}
     * ])
     * See also the inner explanation of those 2 stages
     *
     * @see #matchByFromDate(Date)
     * @see #groupAndCount
     * @see #projectAndFlatten
     */
    public List<VariantStudySummary> findByFromDate(Date fromDate) {
        Aggregation aggregation = Aggregation.newAggregation(
                matchByFromDate(fromDate),
                groupAndCount(),
                projectAndFlatten()
        );

        AggregationResults<VariantStudySummary> studies = mongoTemplate.aggregate(aggregation,
                VariantSourceMongo.class,
                VariantStudySummary.class);

        return studies.getMappedResults();
    }

    private MatchOperation matchByFromDate(Date fromDate) {
        return match(Criteria.where("date").gte(fromDate));
    }

    /**
     * the equivalent intended query is:
     * db.files.aggregate([
     * {$match : { $or:[{"sid" : "studyNameOrId"} , {"sname": "studyNameOrId"}]} },
     * {$group:{_id: {studyId:"$sid",studyName:"$sname"}, filesCount:{$sum:1}}},
     * {$project:{"studyId" : "$_id.studyId", "studyName" : "$_id.studyName", "_id" : 0, "filesCount":"$filesCount" }}
     * ])
     * See also the inner explanation of those 3 stages
     *
     * @see #matchByNameOrId
     * @see #groupAndCount
     * @see #projectAndFlatten
     */
    public VariantStudySummary findByStudyNameOrStudyId(String studyNameOrId) {
        Aggregation aggregation = Aggregation.newAggregation(
                matchByNameOrId(studyNameOrId),
                groupAndCount(),
                projectAndFlatten()
        );

        AggregationResults<VariantStudySummary> studies = mongoTemplate.aggregate(aggregation,
                VariantSourceMongo.class,
                VariantStudySummary.class);

        VariantStudySummary variantStudySummary;
        if (studies.getMappedResults().size() == 0) {
            variantStudySummary = null;
        } else {
            variantStudySummary = studies.getMappedResults().get(0);
        }

        return variantStudySummary;
    }

    /**
     * Match only the documents that contain the argument as studyName or studyId.
     */
    private MatchOperation matchByNameOrId(String studyNameOrId) {
        return match(new Criteria().orOperator(
                Criteria.where(VariantStudySummary.STUDY_ID).is(studyNameOrId),
                Criteria.where(VariantStudySummary.STUDY_NAME).is(studyNameOrId)));
    }
}
