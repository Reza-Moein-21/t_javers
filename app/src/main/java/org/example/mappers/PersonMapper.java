package org.example.mappers;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
interface PersonMapper {
    @Select("select p.* from person p where p.id = #{id} ")
    Optional<Person> findOne(Long id);

    @Select("""
            select p.* from person p
            
            OFFSET #{offset} ROWS
            FETCH NEXT #{limit} ROWS ONLY
            """)
    List<Person> findAll(@Param("offset") int offset, @Param("limit") int limit);

    @Select("select count(*) from person")
    long totalItems();

    @Insert("insert into person(ID,FULL_NAME) values (#{id},#{fullName})")
    int insert(Person p);

    @Update("update person set FULL_NAME=#{fullName} where ID=#{id}")
    int update(Person p);
}
