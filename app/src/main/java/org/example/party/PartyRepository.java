package org.example.party;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.Optional;

@Mapper
public interface PartyRepository {

    @Results(id = "partyResults", value = {
            @Result(property = "id", column = "ID"),
            @Result(property = "version", column = "VERSION"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "createdBy", column = "CREATED_BY"),
            @Result(property = "modifiedAt", column = "MODIFIED_AT"),
            @Result(property = "modifiedBy", column = "MODIFIED_BY"),
            @Result(property = "type", column = "TYPE_FLAG"),
            @Result(property = "key29", column = "KEY29"),
            @Result(property = "customer", column = "KEY29", one = @One(select = "org.example.party.CustomerRepository.findCustomerById", fetchType = FetchType.LAZY)),
            @Result(property = "bank", column = "KEY29", one = @One(select = "org.example.party.BankRepository.findBankById", fetchType = FetchType.LAZY)),
    })
    @Select("SELECT * FROM PARTY  WHERE ID = #{ID}")
    Optional<Party> findPartyById(Long id);


}
