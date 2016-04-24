package com.setiawanpaiman.sunnyreader.mockdata;

import com.setiawanpaiman.sunnyreader.data.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class MockComment {

    public static Comment COMMENT1 = create(1L, 10239231L, "Author Comment 1", "Comment 1", 10L, new ArrayList<Long>(), false);
    public static Comment COMMENT2 = create(2L, 10239232L, "Author Comment 2", "Comment 2", 20L, new ArrayList<Long>(), false);
    public static Comment COMMENT3 = create(3L, 10239233L, "Author Comment 3", "Comment 3", 30L, new ArrayList<Long>(), false);

    public static String COMMENT1_JSON = "{\n"
            + "  \"by\" : \"Author Comment 1\",\n"
            + "  \"id\" : 1,\n"
            + "  \"kids\" : [ ],\n"
            + "  \"parent\" : 10,\n"
            + "  \"text\" : \"Comment 1\",\n"
            + "  \"time\" : 10239231,\n"
            + "  \"type\" : \"comment\"\n"
            + "}";

    public static String COMMENT2_JSON = "{\n"
            + "  \"by\" : \"Author Comment 2\",\n"
            + "  \"id\" : 2,\n"
            + "  \"kids\" : [ ],\n"
            + "  \"parent\" : 20,\n"
            + "  \"text\" : \"Comment 2\",\n"
            + "  \"time\" : 10239232,\n"
            + "  \"type\" : \"comment\"\n"
            + "}";

    public static String COMMENT3_JSON = "{\n"
            + "  \"by\" : \"Author Comment 3\",\n"
            + "  \"id\" : 3,\n"
            + "  \"kids\" : [ ],\n"
            + "  \"parent\" : 30,\n"
            + "  \"text\" : \"Comment 3\",\n"
            + "  \"time\" : 10239233,\n"
            + "  \"type\" : \"comment\"\n"
            + "}";

    public static String COMMENT_INVALID_JSON = "{\n"
            + "  \"id\" : 1,\n"
            + "  \"invalid\" : \"Invalid value\",\n"
            + "}";

    public static Comment create(long id, long timestamp, String author, String text,
                                 long parentId, List<Long> commentIds, boolean deleted) {
        return Comment.newBuilder(id)
                .setTimestamp(timestamp)
                .setAuthor(author)
                .setText(text)
                .setParentId(parentId)
                .setCommentIds(commentIds)
                .setDeleted(deleted)
                .build();
    }
}
