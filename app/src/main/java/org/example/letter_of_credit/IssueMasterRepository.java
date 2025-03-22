package org.example.letter_of_credit;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.Optional;

@Mapper
public interface IssueMasterRepository {


    @Results(id = "issueMasterResults", value = {
            @Result(property = "id", column = "ID", id = true),
            @Result(property = "version", column = "VERSION"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "createdBy", column = "CREATED_BY"),
            @Result(property = "modifiedAt", column = "MODIFIED_AT"),
            @Result(property = "modifiedBy", column = "MODIFIED_BY"),

            @Result(property = "reference", column = "REFERENCE"),
            @Result(property = "inputBranch", column = "INPUT_BRANCH"),
            @Result(property = "behalfBranch", column = "BEHALF_BRANCH"),
            @Result(property = "issueDate", column = "ISSUE_DATE"),
            @Result(property = "expiryDate", column = "EXPIRY_DATE"),
            @Result(property = "amount.value", column = "AMOUNT"),
            @Result(property = "amount.ccy", column = "CCY"),
            @Result(property = "applicant", column = "APPLICANT_FK", one = @One(select = "org.example.party.PartyRepository.findPartyById", fetchType = FetchType.LAZY)),
            @Result(property = "beneficiary", column = "BENEFICIARY_FK", one = @One(select = "org.example.party.PartyRepository.findPartyById", fetchType = FetchType.LAZY)),
    })
    @Select("SELECT * FROM ISSUE_MASTER WHERE ID = #{id} ")
    Optional<IssueMaster> findIssueMasterById(Long id);

}

