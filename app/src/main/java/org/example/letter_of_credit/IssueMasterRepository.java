package org.example.letter_of_credit;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;
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
            @Result(property = "applicant", column = "APPLICANT_FK", one = @One(select = "org.example.party.PartyRepository.findPartyById")),
            @Result(property = "beneficiary", column = "BENEFICIARY_FK", one = @One(select = "org.example.party.PartyRepository.findPartyById")),
    })
    @Select("SELECT * FROM ISSUE_MASTER WHERE ID = #{id} ")
    Optional<IssueMaster> findIssueMasterById(Long id);


    @ResultMap("issueMasterResults")
    @SelectProvider(type = IssueMasterSQLProvider.class, method = "selectIssueMaster")
    List<IssueMaster> searchIssueMasterBySearchParam(IssueMasterSearchParam param, int offset, int limit);

    @SelectProvider(type = IssueMasterSQLProvider.class, method = "selectIssueMasterTotal")
    long totalIssueMasterBySearchParam(IssueMasterSearchParam param);


    class IssueMasterSQLProvider {

        public String selectIssueMaster(IssueMasterSearchParam param, int offset, int limit) {
            return applySearchQuery(param, new SQL().SELECT("*"))
                    .toString()
                    .concat("""
                            
                            OFFSET %s ROWS
                            FETCH NEXT %s ROWS ONLY
                            """.formatted(offset, limit));
        }

        public String selectIssueMasterTotal(IssueMasterSearchParam param) {
            return applySearchQuery(param, new SQL().SELECT("count(ID)")).toString();
        }

        private SQL applySearchQuery(IssueMasterSearchParam param, SQL baseSql) {
            final var sql = baseSql
                    .FROM("ISSUE_MASTER");

            if (param.reference() != null && !param.reference().isBlank()) {
                sql.WHERE("REFERENCE like '%" + param.reference().trim() + "%' ");
            }

            if (param.inputBranch() != null && !param.inputBranch().isEmpty()) {
                sql.WHERE("INPUT_BRANCH in (" + String.join(",", param.inputBranch()) + ") ");
            }

            return sql;
        }
    }
}

