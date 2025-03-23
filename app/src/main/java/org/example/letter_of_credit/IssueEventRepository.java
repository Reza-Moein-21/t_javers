package org.example.letter_of_credit;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.FetchType;

import java.util.Optional;

@Mapper
public interface IssueEventRepository {


    @Results(id = "issueEventMap", value = {
            @Result(property = "id", column = "ID", id = true),
            @Result(property = "version", column = "VERSION"),
            @Result(property = "createdBy", column = "CREATED_BY"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "modifiedAt", column = "MODIFIED_AT"),
            @Result(property = "modifiedBy", column = "MODIFIED_BY"),

            @Result(property = "applicantReference", column = "APPLICANT_REFERENCE"),
            @Result(property = "issueDate", column = "ISSUE_DATE"),
            @Result(property = "expiryDate", column = "EXPIRY_DATE"),
            @Result(property = "amount.value", column = "AMOUNT"),
            @Result(property = "amount.ccy", column = "CCY"),
            @Result(property = "master", column = "MASTER_FK", one = @One(select = "org.example.letter_of_credit.IssueMasterRepository.findIssueMasterById")),
            @Result(property = "applicant", column = "APPLICANT_FK", one = @One(select = "org.example.party.PartyRepository.findPartyById")),
            @Result(property = "beneficiary", column = "BENEFICIARY_FK", one = @One(select = "org.example.party.PartyRepository.findPartyById")),
    })
    @Select("SELECT * FROM ISSUE_EVENT WHERE ID = #{ID} ")
    Optional<IssueEvent> findIssueEventById(Long id);

    @UpdateProvider(type = IssueEventSqlProvider.class, method = "updateIssueEvent")
    int updateIssueEvent(IssueEvent issueEvent);

    class IssueEventSqlProvider {

        public String updateIssueEvent(IssueEvent issueEvent) {
            return new SQL()
                    .UPDATE("ISSUE_EVENT")
                    .SET("VERSION = #{version}")
                    .SET("MODIFIED_AT = sysdate")
                    .SET("MODIFIED_BY = #{modifiedBy}")
                    .SET("APPLICANT_REFERENCE = #{applicantReference}")
                    .SET("ISSUE_DATE = #{issueDate}")
                    .SET("EXPIRY_DATE = #{expiryDate}")
                    .SET("AMOUNT = #{amount.value}")
                    .SET("CCY = #{amount.ccy}")
                    .SET("MASTER_FK = #{master.id}")
                    .SET("APPLICANT_FK = #{applicant.id}")
                    .SET("BENEFICIARY_FK = #{beneficiary.id}")

                    .WHERE("ID = #{id}")
                    .toString();
        }
    }
}
