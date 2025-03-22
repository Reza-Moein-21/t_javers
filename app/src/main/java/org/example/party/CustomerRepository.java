package org.example.party;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface CustomerRepository {

    @Results(id = "customerResults", value = {
            @Result(property = "id", column = "ID"),
            @Result(property = "version", column = "VERSION"),
            @Result(property = "createdAt", column = "CREATED_AT"),
            @Result(property = "createdBy", column = "CREATED_BY"),
            @Result(property = "modifiedAt", column = "MODIFIED_AT"),
            @Result(property = "modifiedBy", column = "MODIFIED_BY"),
            @Result(property = "cif", column = "CIF"),
            @Result(property = "fullName", column = "FULL_NAME"),
    })
    @Select("SELECT * FROM CUSTOMER  WHERE ID = #{ID}")
    Optional<Customer> findCustomerById(Long id);


}
