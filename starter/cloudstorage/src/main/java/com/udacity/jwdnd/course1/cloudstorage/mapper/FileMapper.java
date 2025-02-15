package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFilesForUser(int userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId} AND filename = #{fileName}")
    File getFileForUserWithName(int userId, String fileName);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(int fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{id}")
    int deleteFileById(int id);
}
